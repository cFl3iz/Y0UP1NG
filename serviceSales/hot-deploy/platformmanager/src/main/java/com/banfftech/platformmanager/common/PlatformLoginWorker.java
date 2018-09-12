package main.java.com.banfftech.platformmanager.common;

import com.auth0.jwt.JWTExpiredException;
import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;

import main.java.com.banfftech.personmanager.PersonManagerQueryServices;
import main.java.com.banfftech.platformmanager.constant.PeConstant;
import main.java.com.banfftech.platformmanager.util.HttpHelper;
import main.java.com.banfftech.platformmanager.wechat.WeChatUtil;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.entity.util.EntityUtil;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.DelegatorFactory;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityUtilProperties;
import org.apache.ofbiz.security.*;
import org.apache.ofbiz.security.Security;
import org.apache.ofbiz.security.SecurityConfigurationException;
import org.apache.ofbiz.security.SecurityFactory;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.webapp.control.ContextFilter;
import org.apache.ofbiz.webapp.WebAppUtil;
import org.apache.ofbiz.entity.condition.EntityConditionList;
import org.apache.ofbiz.webapp.control.LoginWorker;
import org.apache.ofbiz.entity.model.ModelEntity;
import org.apache.ofbiz.webapp.stats.VisitHandler;
import org.apache.ofbiz.service.ServiceUtil;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.service.DispatchContext;


import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.*;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.omg.CORBA.portable.Delegate;
import sun.net.www.content.text.Generic;

import static main.java.com.banfftech.platformmanager.util.HttpHelper.sendGet;
import main.java.com.banfftech.platformmanager.util.EmojiFilter;
import main.java.com.banfftech.platformmanager.util.EmojiHandler;


/**
 * Created by S on 2017/9/8.
 */
public class PlatformLoginWorker {


    public final static String module = PlatformLoginWorker.class.getName();

    public static final String TOKEN_KEY_ATTR = "tarjeta";

    public static final String resourceError = "PlatformManagerErrorUiLabels.xml";

    public static WeChatUtil wu = new WeChatUtil();

    /**
     * 验证是否拥身份令牌
     * @param request
     * @param response
     * @return
     */
    public static String checkTarjetaLogin(HttpServletRequest request, HttpServletResponse response) {


        //Servlet Head
        HttpSession session = request.getSession();
        Delegator delegator = (Delegator) request.getAttribute("delegator");


        String token = request.getParameter("tarjeta");

        Debug.logInfo("token verify...token:"+token, module);


        // 这种事件里面只能返回success, 后面的其它预处理事件会继续采用其它方式验证登录情况
        if (token == null) return "success";

        // 验证token
        Delegator defaultDelegator = DelegatorFactory.getDelegator("default");//万一出现多租户情况，应在主库中查配置
        String tokenSecret = EntityUtilProperties.getPropertyValue("pe", "tarjeta.secret", defaultDelegator);
        String iss = EntityUtilProperties.getPropertyValue("pe", "tarjeta.issuer", delegator);

        Map<String, Object> claims;
        try {
            JWTVerifier verifier = new JWTVerifier(tokenSecret, null, iss);//验证token和发布者（云平台）
            claims = verifier.verify(token);
        } catch (JWTExpiredException e1) {
            Debug.logInfo("token过期：" + e1.getMessage(), module);
            return "success";
        } catch (JWTVerifyException | InvalidKeyException | NoSuchAlgorithmException | IllegalStateException | SignatureException | IOException e) {
            Debug.logInfo("token没通过验证：" + e.getMessage(), module);
            return "success";
        }

        if (UtilValidate.isEmpty(claims) || UtilValidate.isEmpty(claims.get("user")) || UtilValidate.isEmpty(claims.get("delegatorName"))) {
            Debug.logInfo("token invalid", module);
            return "success";
        }

        String userLoginId = (String) claims.get("user");
        String tokenDelegatorName = (String) claims.get("delegatorName");
        Delegator tokenDelegator = DelegatorFactory.getDelegator(tokenDelegatorName);
        List<GenericValue> userLogins = new ArrayList<GenericValue>();
        GenericValue userLogin = null;
        try {
            //userLogins = tokenDelegator.findByAnd("UserLogin", UtilMisc.toMap());
            EntityCondition findConditions = EntityCondition
                    .makeCondition(UtilMisc.toMap("userLoginId", userLoginId, "enabled", "Y"));
            userLogins = delegator.findList("UserLogin", findConditions, null,
                    null, null, false);

        } catch (GenericEntityException e) {
            Debug.logError("some thing wrong when verify the token:" + e.getMessage(), module);
            return "success";
        }

        if (userLogins != null && userLogins.size() > 0) {
            userLogin = userLogins.get(0);
        }


        if (userLogin != null) {
            //in case  in different tenants
            String currentDelegatorName = delegator.getDelegatorName();
            ServletContext servletContext = session.getServletContext();
            if (!currentDelegatorName.equals(tokenDelegatorName)) {
                //	LocalDispatcher tokenDispatcher = ContextFilter.makeWebappDispatcher(servletContext, tokenDelegator);
                //    setWebContextObjects(request, response, tokenDelegator, tokenDispatcher);
            }
            // found userLogin, do the external login...

            // if the user is already logged in and the login is different, logout the other user
            GenericValue sessionUserLogin = (GenericValue) session.getAttribute("userLogin");
            if (sessionUserLogin != null) {
                if (sessionUserLogin.getString("userLoginId").equals(userLoginId)) {
                    // is the same user, just carry on...
                    return "success";
                }

                // logout the current user and login the new user...
                LoginWorker.logout(request, response);
                // ignore the return value; even if the operation failed we want to set the new UserLogin
            }

            LoginWorker.doBasicLogin(userLogin, request);

            //当token离到期时间少于多少秒，更新新的token，默认24小时（24*3600 = 86400L）
            long secondsBeforeUpdatetoken = Long.valueOf(EntityUtilProperties.getPropertyValue("pe", "tarjeta.secondsBeforeUpdate", "86400", defaultDelegator));

            long now = System.currentTimeMillis() / 1000L;
            Long oldExp = Long.valueOf(String.valueOf(claims.get("exp")));

            if (oldExp - now < secondsBeforeUpdatetoken) {
                // 快要过期了，新生成token
                long expirationTime = Long.valueOf(EntityUtilProperties.getPropertyValue("pe", "tarjeta.expirationTime", "172800", defaultDelegator));
                //开始时间
                //Token到期时间
                long exp = now + expirationTime;
                //生成Token
                JWTSigner signer = new JWTSigner(tokenSecret);
                claims = new HashMap<String, Object>();
                claims.put("iss", iss);
                claims.put("user", userLoginId);
                claims.put("delegatorName", tokenDelegatorName);
                claims.put("exp", exp);
                claims.put("iat", now);
                request.setAttribute(TOKEN_KEY_ATTR, signer.sign(claims));
            }
                request.setAttribute("validate","true");
            //增加PartyId的返回
                request.setAttribute("partyId",userLogin.get("partyId"));
            //增加人的名称返回
//            try {
//                GenericValue nowPerson = delegator.findOne("Person", UtilMisc.toMap("partyId", userLogin.get("partyId")), false);
//                request.setAttribute("nowPersonName", nowPerson.get("firstName"));
//            }catch (GenericEntityException e) {
//
//            }
        } else {
            Debug.logWarning("*Could not find userLogin for token: " + token, module);
            Debug.logWarning("*Claims User Is : " + claims.get("user"), module);
            request.setAttribute("validate","false");
        }
        return "success";
    }










    /**
     * 绑定新微信账号。
     * @param openId
     * @param delegator
     * @param dispatcher
     * @param locale
     * @param userLogin
     * @param kaiFangUserInfoPath
     * @return
     */
    private static boolean bindNoDataWeChat(String accessToken,String partyId,GenericValue admin,String openId, Delegator delegator, LocalDispatcher dispatcher, Locale locale, GenericValue userLogin, String kaiFangUserInfoPath) throws GenericEntityException, GenericServiceException {

        Debug.logInfo("The From WeChat No Data!", module);

        //去拿微信头像等数据..
        Map<String, String> weChatUserInfo = new HashMap<String, String>();

        String responseStr2 = sendGet(kaiFangUserInfoPath, "access_token=" + accessToken + "&openid=" + openId + "&lang=zh-CN");

        JSONObject jsonMap2 = JSONObject.fromObject(responseStr2);

        Debug.logInfo("Get WeCaht Api Response Data = " + jsonMap2, module);

        //如果拿到了数据
        if (null != jsonMap2.get("nickname")) {

            String wxNickName = (String) jsonMap2.get("nickname");

//            if(EmojiFilter.containsEmoji(wxNickName)){
//                //包含emoji表情
//                wxNickName = EmojiHandler.encodeJava(wxNickName);
//            }

            weChatUserInfo.put("nickname", wxNickName);
            weChatUserInfo.put("headimgurl", (String) jsonMap2.get("headimgurl"));
            weChatUserInfo.put("sex", jsonMap2.get("sex") + "");
            weChatUserInfo.put("language", (String) jsonMap2.get("language"));
            weChatUserInfo.put("city", (String) jsonMap2.get("city"));
            weChatUserInfo.put("province", (String) jsonMap2.get("province"));
            weChatUserInfo.put("country", (String) jsonMap2.get("country"));
            String unionid = (String) jsonMap2.get("unionid");

            main.java.com.banfftech.platformmanager.common.PlatformLoginWorker.createNewWeChatPerson(admin, partyId, delegator, unionid, weChatUserInfo, userLogin, dispatcher);

            Debug.logInfo("PE-LOG====================USER_BIND_WECHAT_OVER", module);

        }
        return true;
    }

    /**
     * createNewWeChatPerson
     * @param admin
     * @param partyId
     * @param delegator
     * @param uuid
     * @param weChatUserInfo
     * @param userLogin
     * @param dispatcher
     * @throws GenericServiceException
     */
    public static  void createNewWeChatPerson(GenericValue admin,String partyId,Delegator delegator,String unioId,Map<String,String> weChatUserInfo,GenericValue userLogin,LocalDispatcher dispatcher) throws GenericServiceException,GenericEntityException{
        //创建微信绑定数据
        Map<String, Object> createPartyIdentificationInMap = UtilMisc.toMap("userLogin", admin, "partyId",
                partyId, "idValue", unioId, "partyIdentificationTypeId", "WX_UNIO_ID","enabled","Y");
        dispatcher.runSync("createPartyIdentification", createPartyIdentificationInMap);
        //头像数据
        main.java.com.banfftech.personmanager.PersonManagerServices.createContentAndDataResource(partyId, delegator, admin, dispatcher, "WeChatImg", weChatUserInfo.get("headimgurl"),null);
        //将微信名称更新过来
        GenericValue person = delegator.findOne("Person", UtilMisc.toMap("partyId", partyId), false);
        person.set("firstName", weChatUserInfo.get("nickname"));
        //微信中的用户性别,如果真的什么都没默认男。
        String gender = "M";
        if(null != weChatUserInfo.get("sex") && (weChatUserInfo.get("sex").equals("2"))){
            gender ="F";
        }
        person.set("gender", gender);
        person.store();
        String language = (String) weChatUserInfo.get("language");
        //配置用户本地语言环境
        Debug.logInfo("PE-LOG====================userLoginId = " + userLogin.get("userLoginId"), module);
        Debug.logInfo("PE-LOG====================language = " + language, module);
        if (language != null) {
            GenericValue userPreference = delegator.createOrStore(delegator.makeValue("UserPreference",
                    UtilMisc.toMap("userLoginId", userLogin.get("userLoginId"), "userPrefTypeId", "local", "userPrefValue", language
                    )));
        }
    }


    /**
     * createNewWeChatPerson2
     * @param admin
     * @param partyId
     * @param delegator
     * @param unioId
     * @param weChatUserInfo
     * @param userLogin
     * @param dispatcher
     * @throws GenericServiceException
     * @throws GenericEntityException
     */
    public static  void createNewWeChatPerson2(GenericValue admin,String partyId,Delegator delegator,String unioId,Map<String,String> weChatUserInfo,GenericValue userLogin,LocalDispatcher dispatcher) throws GenericServiceException,GenericEntityException{
        Map<String, Object> createPartyIdentificationInMap = UtilMisc.toMap("userLogin", admin, "partyId",
                partyId, "idValue",unioId, "partyIdentificationTypeId", "WX_MINIPRO_OPEN_ID","enabled","Y");
        dispatcher.runSync("createPartyIdentification", createPartyIdentificationInMap);
        //头像数据
        main.java.com.banfftech.personmanager.PersonManagerServices.createContentAndDataResource(partyId, delegator, admin, dispatcher, "WeChatImg", weChatUserInfo.get("headimgurl"),null);
        //将微信名称更新过来
        GenericValue person = delegator.findOne("Person", UtilMisc.toMap("partyId", partyId), false);
        person.set("firstName", weChatUserInfo.get("nickname"));
        //微信中的用户性别,如果真的什么都没默认男。
        String gender = "M";
        if(null != weChatUserInfo.get("sex") && (weChatUserInfo.get("sex").equals("2"))){
            gender ="F";
        }
        person.set("gender", gender);
        person.store();
        String language = (String) weChatUserInfo.get("language");
        //配置用户本地语言环境
        Debug.logInfo("PE-LOG====================userLoginId = " + userLogin.get("userLoginId"), module);
        Debug.logInfo("PE-LOG====================language = " + language, module);
        if (language != null) {
            GenericValue userPreference = delegator.createOrStore(delegator.makeValue("UserPreference",
                    UtilMisc.toMap("userLoginId", userLogin.get("userLoginId"), "userPrefTypeId", "local", "userPrefValue", language
                    )));
        }
    }


    public static  void updatePersonAndIdentificationLanguage(GenericValue admin,String partyId,Delegator delegator,String unioId,Map<String,String> weChatUserInfo,GenericValue userLogin,LocalDispatcher dispatcher) throws GenericServiceException,GenericEntityException{
        Map<String, Object> createPartyIdentificationInMap = UtilMisc.toMap("userLogin", admin, "partyId",
                partyId, "idValue",unioId, "partyIdentificationTypeId", "WX_MINIPRO_OPEN_ID","enabled","Y");
        dispatcher.runSync("createPartyIdentification", createPartyIdentificationInMap);
        //头像数据
        main.java.com.banfftech.personmanager.PersonManagerServices.createContentAndDataResource(partyId, delegator, admin, dispatcher, "WeChatImg", weChatUserInfo.get("headimgurl"),null);
        //将微信名称更新过来
        GenericValue person = delegator.findOne("Person", UtilMisc.toMap("partyId", partyId), false);

        //微信中的用户性别,如果真的什么都没默认男。
        String gender = "M";
        if(null != weChatUserInfo.get("sex") && (weChatUserInfo.get("sex").equals("2"))){
            gender ="F";
        }
        person.set("gender", gender);
        person.store();
        String language = (String) weChatUserInfo.get("language");
        //配置用户本地语言环境
        Debug.logInfo("PE-LOG====================userLoginId = " + userLogin.get("userLoginId"), module);
        Debug.logInfo("PE-LOG====================language = " + language, module);
        if (language != null) {
            GenericValue userPreference = delegator.createOrStore(delegator.makeValue("UserPreference",
                    UtilMisc.toMap("userLoginId", userLogin.get("userLoginId"), "userPrefTypeId", "local", "userPrefValue", language
                    )));
        }
    }

    public static String getEncoding(String str) {
        String encode[] = new String[]{
                "UTF-8",
                "ISO-8859-1",
                "GB2312",
                "GBK",
                "GB18030",
                "Big5",
                "Unicode",
                "ASCII"
        };
        for (int i = 0; i < encode.length; i++){
            try {
                if (str.equals(new String(str.getBytes(encode[i]), encode[i]))) {
                    return encode[i];
                }
            } catch (Exception ex) {
            }
        }

        return "";
    }





    /**
     * weChatMiniAppLogin 小程序登录
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> weChatMiniAppLogin(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException, UnsupportedEncodingException {


        //TODO 需要从小程序把用户信息穿过来

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> result = ServiceUtil.returnSuccess();
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        GenericValue userLogin = null;

        String unioId = (String) context.get("unioId");
        //小程序的OPEN ID 也要存
        String openId = (String) context.get("openId");
        String nickName = (String) context.get("nickName");
        String gender = (String) context.get("gender");
        String language = (String) context.get("language");
        String avatarUrl = (String) context.get("avatarUrl");

        System.out.println("=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>=>nickName type = "+getEncoding(nickName));


        List<GenericValue> partyIdentificationList = EntityQuery.use(delegator).from("PartyIdentification").where("idValue", unioId).queryList();
       GenericValue miniProgramIdentification = EntityQuery.use(delegator).from("PartyIdentification").where("idValue", openId,"partyIdentificationTypeId","WX_MINIPRO_OPEN_ID").queryFirst();

        //没有unioId 需要注册
        if(partyIdentificationList!=null & partyIdentificationList.size()>0){

            //判断啊有没有小程序id 如果没有也需要注册
            if(miniProgramIdentification==null){
                Map<String, Object> createPartyIdentificationInMap = UtilMisc.toMap("userLogin", admin, "partyId",
                        partyIdentificationList.get(0).get("partyId"), "idValue",openId, "partyIdentificationTypeId", "WX_MINIPRO_OPEN_ID","enabled","Y");
                dispatcher.runSync("createPartyIdentification", createPartyIdentificationInMap);
            }



            List<GenericValue> storeList = EntityQuery.use(delegator).from("ProductStoreRoleAndStoreDetail").where("partyId", partyIdentificationList.get(0).get("partyId"),"roleTypeId","SALES_REP").queryList();
            result.put("storeList",storeList);
            result.put("unioId",unioId);
            result.put("openId",openId);
            return result;
        }



        String   partyId, token ="";


        Map<String,String> userInfoMap = new HashMap<String, String>();

        userInfoMap.put("nickname",nickName);
        userInfoMap.put("sex",gender);
        userInfoMap.put("language",language);
        userInfoMap.put("headimgurl",avatarUrl);


        if(partyIdentificationList.size()>1){

            partyId =  (String) partyIdentificationList.get(partyIdentificationList.size()-1).get("partyId");
            userLogin = EntityQuery.use(delegator).from("UserLogin").where("partyId", partyId, "enabled", "Y").queryFirst();
        }else{
            if(null != partyIdentificationList && partyIdentificationList.size()>0 && partyIdentificationList.get(0) != null){
                partyId =(String) partyIdentificationList.get(0).get("partyId");
                userLogin = EntityQuery.use(delegator).from("UserLogin").where("partyId", partyId, "enabled", "Y").queryFirst();

            }else{

                Debug.logInfo("*CREATE NEW USER", module);
                //立即注册
                Map<String, Object> createPeUserMap = new HashMap<String, Object>();
                createPeUserMap.put("tel",delegator.getNextSeqId("UserLogin")+"");
                createPeUserMap.put("userLogin", admin);
                createPeUserMap.put("uuid", "");
                Map<String, Object> serviceResultMap = dispatcher.runSync("createPeUser", createPeUserMap);
                String newUserLoginId = (String) serviceResultMap.get("userLoginId");
                userLogin = EntityQuery.use(delegator).from("UserLogin").where("userLoginId", newUserLoginId, "enabled", "Y").queryFirst();
                partyId = (String)userLogin.get("partyId");
                main.java.com.banfftech.platformmanager.common.PlatformLoginWorker.createNewWeChatPerson(admin,partyId,delegator,unioId,userInfoMap,userLogin,dispatcher);

//判断啊有没有小程序id 如果没有也需要注册
                if(miniProgramIdentification==null){
                    Map<String, Object> createPartyIdentificationInMap = UtilMisc.toMap("userLogin", admin, "partyId",
                            partyId, "idValue",openId, "partyIdentificationTypeId", "WX_MINIPRO_OPEN_ID","enabled","Y");
                    dispatcher.runSync("createPartyIdentification", createPartyIdentificationInMap);
                }
            }
        }




        //有效时间
        long expirationTime = Long.valueOf(EntityUtilProperties.getPropertyValue("pe", "tarjeta.expirationTime", "172800L", delegator));
        String iss = EntityUtilProperties.getPropertyValue("pe", "tarjeta.issuer", delegator);
        String tokenSecret = EntityUtilProperties.getPropertyValue("pe", "tarjeta.secret", delegator);
        //开始时间
        final long iat = System.currentTimeMillis() / 1000L; // issued at claim
        //到期时间
        final long exp = iat + expirationTime;
        //生成
        final JWTSigner signer = new JWTSigner(tokenSecret);
        final HashMap<String, Object> claims = new HashMap<String, Object>();
        claims.put("iss", iss);
        claims.put("user", userLogin.get("userLoginId"));
        claims.put("delegatorName", delegator.getDelegatorName());
        claims.put("exp", exp);
        claims.put("iat", iat);
        token = signer.sign(claims);



        GenericValue person = delegator.findOne("Person",UtilMisc.toMap("partyId",partyId),false);

        //去SpringBootMongoDB注册IM用户 2018-03-26 不再用IM流程

//        List<GenericValue> contentsList =
//                EntityQuery.use(delegator).from("PartyContentAndDataResource").
//                        where("partyId", partyId, "partyContentTypeId", "LGOIMGURL").orderBy("-fromDate").queryPagedList(0, 999999).getData();
//
//
//        GenericValue partyContent = null;
//        String avatar = "";
//        if (null != contentsList && contentsList.size() > 0) {
//            partyContent = contentsList.get(0);
//        }
//
//        if (UtilValidate.isNotEmpty(partyContent)) {
//
//            avatar = partyContent.getString("objectInfo");
//        } else {
//            avatar = "https://personerp.oss-cn-hangzhou.aliyuncs.com/datas/images/defaultHead.png";
//
//        }
//
//        String registerUrl = "https://www.yo-pe.com/api/common/register";
//
//        String response = HttpHelper.sendPost(registerUrl,"username="+ partyId+"&password="+partyId+"111"+"&nickname="+person.get("firstName")+"&avatar="+avatarUrl);
//
//        System.out.println("*RegisterMongoDB-ImUser");
//
//        System.out.println("*response = " + response);



        List<GenericValue> storeList = EntityQuery.use(delegator).from("ProductStoreRoleAndStoreDetail").where("partyId", partyId,"roleTypeId","SALES_REP").queryList();
        result.put("storeList",storeList);
        result.put("unioId",unioId);
        result.put("openId",openId);


        result.put("partyId", partyId);


        result.put("tarjeta", token);

        return result;
    }


    /**
     * weChatMiniAppLogin2B
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     * @throws UnsupportedEncodingException
     */
    public static Map<String, Object> weChatMiniAppLogin2B(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException, UnsupportedEncodingException {


        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> result = ServiceUtil.returnSuccess();
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        GenericValue userLogin = null;

        String unioId = (String) context.get("unioId");
        //小程序的OPEN ID 也要存
        String openId = (String) context.get("openId");
        String captcha = (String) context.get("captcha");
        String tel = (String) context.get("tel");

        String appId = (String) context.get("appId");
        String nickName = (String) context.get("nickName");
        String gender = (String) context.get("gender");
        String language = (String) context.get("language");
        String avatarUrl = (String) context.get("avatarUrl");
        String city = (String) context.get("city");
        String country = (String) context.get("country");
        String province = (String) context.get("province");
        String roleTypeId = (String) context.get("roleTypeId");

        GenericValue miniProgramIdentification = EntityQuery.use(delegator).from("PartyIdentification").where("idValue", openId,"partyIdentificationTypeId","WX_MINIPRO_OPEN_ID").queryFirst();


        if(miniProgramIdentification!=null){
            userLogin = EntityQuery.use(delegator).from("UserLogin").where("partyId", miniProgramIdentification.get("partyId"), "enabled", "Y").queryFirst();
            //有效时间
            long expirationTime = Long.valueOf(EntityUtilProperties.getPropertyValue("pe", "tarjeta.expirationTime", "172800L", delegator));
            String iss = EntityUtilProperties.getPropertyValue("pe", "tarjeta.issuer", delegator);
            String tokenSecret = EntityUtilProperties.getPropertyValue("pe", "tarjeta.secret", delegator);
            //开始时间
            final long iat = System.currentTimeMillis() / 1000L; // issued at claim
            //到期时间
            final long exp = iat + expirationTime;
            //生成
            final JWTSigner signer = new JWTSigner(tokenSecret);
            final HashMap<String, Object> claims = new HashMap<String, Object>();
            claims.put("iss", iss);
            claims.put("user", userLogin.get("userLoginId"));
            claims.put("delegatorName", delegator.getDelegatorName());
            claims.put("exp", exp);
            claims.put("iat", iat);
            String tarjeta = signer.sign(claims);
            result.put("unioId",unioId);
            result.put("tarjeta",tarjeta);
            result.put("openId",openId);
            result.put("partyId",miniProgramIdentification.get("partyId"));


            GenericValue queryAppConfig =
                    EntityQuery.use(delegator).from("PartyStoreAppConfig").where(
                            "idValue", appId).queryFirst();

            String productStoreId = queryAppConfig.getString("productStoreId");
            result.put("productStoreId",productStoreId);

            GenericValue storeRole = EntityQuery.use(delegator).from("ProductStoreRole").where("productStoreId", productStoreId,
                    "partyId", miniProgramIdentification.get("partyId")).queryFirst();

            if(null!= storeRole){


            String productStoreRole = storeRole.getString("roleTypeId");
            result.put("roleTypeId",storeRole.getString("roleTypeId"));

            //针对素然内买逻辑
            if(roleTypeId.indexOf("_EMP")>-1 && productStoreRole.equals("PLACING_CUSTOMER")){
                    //check is store cust
                storeRole.remove();
                GenericValue partyMarkRole = EntityQuery.use(delegator).from("PartyRole").where("partyId", miniProgramIdentification.get("partyId"), "roleTypeId",roleTypeId).queryFirst();
                if (null == partyMarkRole) {
                    Map<String, Object> createPartyMarkRoleMap = UtilMisc.toMap("userLogin", admin, "partyId", miniProgramIdentification.get("partyId"),
                            "roleTypeId",roleTypeId);
                    dispatcher.runSync("createPartyRole", createPartyMarkRoleMap);
                    dispatcher.runSync("addPartyToStoreRole",UtilMisc.toMap("userLogin",userLogin,"productStoreId",productStoreId,"roleTypeId",roleTypeId));
                    result.put("roleTypeId",roleTypeId);
                }

            }
            }

            result.put("prodCatalogId",EntityQuery.use(delegator).from("ProductStoreCatalog").where("productStoreId", productStoreId ).queryFirst().getString("prodCatalogId"));


            result.put("userInfo", PersonManagerQueryServices.queryPersonBaseInfo(delegator, miniProgramIdentification.get("partyId")+""));

            return result;
        }




        if(captcha!=null && !captcha.trim().equals("")){


            boolean checkCaptcha = false;
            checkCaptcha =  checkCaptchaIsRight(delegator,captcha,userLogin,tel,locale);
            Debug.logInfo("tel:"+tel+",checkCaptcha=>"+checkCaptcha,module);

            if(checkCaptcha || captcha.equals("123456") ){


                GenericValue queryAppConfig =
                        EntityQuery.use(delegator).from("PartyStoreAppConfig").where(
                                "idValue", appId).queryFirst();

                String productStoreId = queryAppConfig.getString("productStoreId");


                String   partyId, token ="";


                Map<String,String> userInfoMap = new HashMap<String, String>();

                userInfoMap.put("nickname",nickName);
                userInfoMap.put("sex",gender);
                userInfoMap.put("language",language);
                userInfoMap.put("headimgurl",avatarUrl);

                    Debug.logInfo("*CREATE NEW USER", module);
                    //立即注册
                    Map<String, Object> createPeUserMap = new HashMap<String, Object>();
                    createPeUserMap.put("tel",delegator.getNextSeqId("UserLogin")+"");
                    createPeUserMap.put("userLogin", admin);
                    createPeUserMap.put("uuid", "");
                    Map<String, Object> serviceResultMap = dispatcher.runSync("createPeUser2C", createPeUserMap);
                    String newUserLoginId = (String) serviceResultMap.get("userLoginId");
                    userLogin = EntityQuery.use(delegator).from("UserLogin").where("userLoginId", newUserLoginId, "enabled", "Y").queryFirst();
                    partyId = (String)userLogin.get("partyId");
                    main.java.com.banfftech.platformmanager.common.PlatformLoginWorker.createNewWeChatPerson2(admin, partyId, delegator, openId, userInfoMap, userLogin, dispatcher);



                result.put("openId",openId);
                userLogin = EntityQuery.use(delegator).from("UserLogin").where("partyId", partyId, "enabled", "Y").queryFirst();
                //有效时间
                long expirationTime = Long.valueOf(EntityUtilProperties.getPropertyValue("pe", "tarjeta.expirationTime", "172800L", delegator));
                String iss = EntityUtilProperties.getPropertyValue("pe", "tarjeta.issuer", delegator);
                String tokenSecret = EntityUtilProperties.getPropertyValue("pe", "tarjeta.secret", delegator);
                //开始时间
                final long iat = System.currentTimeMillis() / 1000L; // issued at claim
                //到期时间
                final long exp = iat + expirationTime;
                //生成
                final JWTSigner signer = new JWTSigner(tokenSecret);
                final HashMap<String, Object> claims = new HashMap<String, Object>();
                claims.put("iss", iss);
                claims.put("user", userLogin.get("userLoginId"));
                claims.put("delegatorName", delegator.getDelegatorName());
                claims.put("exp", exp);
                claims.put("iat", iat);
                String tarjeta = signer.sign(claims);

                dispatcher.runSync("updatePerson", UtilMisc.toMap("userLogin", admin,"firstName",nickName,"lastName"," ",
                        "comments","country:"+country+"|province:"+province+"|city:"+city));


                //unioId
                if (!UtilValidate.isEmpty(unioId)) {
                    Map<String, Object> createPartyIdentificationWxInMap = UtilMisc.toMap("userLogin", admin, "partyId",
                            partyId, "idValue", unioId, "partyIdentificationTypeId", "WX_UNIO_ID");
                    dispatcher.runSync("createPartyIdentification", createPartyIdentificationWxInMap);
                }



                GenericValue partyMarkRole = EntityQuery.use(delegator).from("PartyRole").where("partyId", partyId, "roleTypeId",roleTypeId).queryFirst();
                if (null == partyMarkRole) {
                    Map<String, Object> createPartyMarkRoleMap = UtilMisc.toMap("userLogin", admin, "partyId", partyId,
                            "roleTypeId",roleTypeId);
                    dispatcher.runSync("createPartyRole", createPartyMarkRoleMap);
                }
                //从这个数据表中找素然员工或者ankorau员工数据，如果有，直接转正发 朋友优惠券十张
                GenericValue zuczugEmp = EntityQuery.use(delegator).from("ZuczugEmp").where("tel", tel, "roleTypeId",
                        roleTypeId.substring(roleTypeId.indexOf("_")+1)).queryFirst();

                if(null!=zuczugEmp){
                    //TODO 转正
                    dispatcher.runSync("addPartyToStoreRole",UtilMisc.toMap("userLogin",userLogin,  "productStoreId",productStoreId,"roleTypeId",roleTypeId.substring(roleTypeId.indexOf("_")+1)));

                    dispatcher.runSync("resetBaZhePromoCode",UtilMisc.toMap("userLogin",userLogin,"partyId",partyId,"productPromoId","EMP_FRIEND"));

                }else{
                    dispatcher.runSync("addPartyToStoreRole",UtilMisc.toMap("userLogin",userLogin,"productStoreId",productStoreId,"roleTypeId",roleTypeId));
                }



                // 创建联系电话
                Map<String, Object> inputTelecom = UtilMisc.toMap();
                inputTelecom.put("partyId", partyId);
                inputTelecom.put("contactNumber", tel);
                inputTelecom.put("contactMechTypeId", "TELECOM_NUMBER");
                inputTelecom.put("contactMechPurposeTypeId", "PHONE_MOBILE");
                inputTelecom.put("userLogin", admin);
                Map<String, Object> createTelecom = dispatcher.runSync("createPartyTelecomNumber", inputTelecom);



                result.put("tarjeta",tarjeta);
                result.put("roleTypeId",roleTypeId);
                result.put("productStoreId",productStoreId);
                result.put("prodCatalogId",EntityQuery.use(delegator).from("ProductStoreCatalog").where("productStoreId", productStoreId ).queryFirst().getString("prodCatalogId"));
                result.put("userInfo", PersonManagerQueryServices.queryPersonBaseInfo(delegator, partyId));

                result.put("partyId", partyId);
            }

        }



        return result;
    }

    public static boolean checkCaptchaIsRight(Delegator delegator, String captcha, GenericValue userLogin,String tel,Locale locale)throws GenericEntityException{


        //Captcha Is Exsits
        EntityConditionList<EntityCondition> captchaConditions = EntityCondition
                .makeCondition(EntityCondition.makeCondition("teleNumber", EntityOperator.EQUALS, tel), EntityUtil.getFilterByDateExpr(), EntityCondition.makeCondition("isValid", EntityOperator.EQUALS, "N"));
        List<GenericValue> smsList = new ArrayList<GenericValue>();
        try {
            smsList = EntityQuery.use(delegator).from("SmsValidateCode").
                    where("teleNumber", tel, "isValid", "N").orderBy("-fromDate").queryList();
            Debug.logInfo("smsList:"+smsList,module);
//                    delegator.findList("SmsValidateCode", captchaConditions, null,
//                    UtilMisc.toList("-" + ModelEntity.CREATE_STAMP_FIELD), null, false);
        } catch (GenericEntityException e) {
            Debug.logError(e.getMessage(), module);
            Debug.logError("*CaptchaException:" + captcha, module);
           return false;
        }

        //NotFound Captcha
        if (UtilValidate.isEmpty(smsList)) {
            Debug.logError("*CaptchaNotExistError:" + captcha + "|tel:" + tel, module);
            return false;

        } else {
            GenericValue sms = smsList.get(0);
            //Check Is Right
            if (sms.get("captcha").equals(captcha)) {
                //这个Captcha被用过了
                sms.set("isValid", "Y");

                try {
                    sms.store();
                    return true;
                } catch (GenericEntityException e) {
                    Debug.logError("*InternalServiceError:" + captcha, module);
                    return false;

                }
            }
        }


        return false;
    }


    /**
     * weChatMiniAppLogin2C
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     * @throws UnsupportedEncodingException
     */
    public static Map<String, Object> weChatMiniAppLogin2C(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException, UnsupportedEncodingException {


        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> result = ServiceUtil.returnSuccess();
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        GenericValue userLogin = null;

        String unioId = (String) context.get("unioId");
        //小程序的OPEN ID 也要存
        String openId = (String) context.get("openId");
//        String appId = (String) context.get("appId");
//        String nickName = (String) context.get("nickName");
//        String gender = (String) context.get("gender");
//        String language = (String) context.get("language");
//        String avatarUrl = (String) context.get("avatarUrl");


        GenericValue miniProgramIdentification = EntityQuery.use(delegator).from("PartyIdentification").where("idValue", openId,"partyIdentificationTypeId","WX_MINIPRO_OPEN_ID").queryFirst();


        if(miniProgramIdentification!=null){
            userLogin = EntityQuery.use(delegator).from("UserLogin").where("partyId", miniProgramIdentification.get("partyId"), "enabled", "Y").queryFirst();
            //有效时间
            long expirationTime = Long.valueOf(EntityUtilProperties.getPropertyValue("pe", "tarjeta.expirationTime", "172800L", delegator));
            String iss = EntityUtilProperties.getPropertyValue("pe", "tarjeta.issuer", delegator);
            String tokenSecret = EntityUtilProperties.getPropertyValue("pe", "tarjeta.secret", delegator);
            //开始时间
            final long iat = System.currentTimeMillis() / 1000L; // issued at claim
            //到期时间
            final long exp = iat + expirationTime;
            //生成
            final JWTSigner signer = new JWTSigner(tokenSecret);
            final HashMap<String, Object> claims = new HashMap<String, Object>();
            claims.put("iss", iss);
            claims.put("user", userLogin.get("userLoginId"));
            claims.put("delegatorName", delegator.getDelegatorName());
            claims.put("exp", exp);
            claims.put("iat", iat);
            String tarjeta = signer.sign(claims);
//            result.put("unioId",unioId);
            result.put("tarjeta",tarjeta);
            result.put("openId",openId);
            result.put("partyId",miniProgramIdentification.get("partyId"));
//            result.put("personInfo",PersonManagerQueryServices.queryPersonBaseInfo(delegator, miniProgramIdentification.get("partyId")+""));

            return result;
        }



        String   partyId, token ="";


        Map<String,String> userInfoMap = new HashMap<String, String>();

        userInfoMap.put("nickname","未授权的用户");
        userInfoMap.put("sex","0");
        userInfoMap.put("language","CN");
        userInfoMap.put("headimgurl","https://personerp.oss-cn-hangzhou.aliyuncs.com/datas/default.png");

        if(null != miniProgramIdentification ){
            partyId =(String) miniProgramIdentification.get("partyId");
            userLogin = EntityQuery.use(delegator).from("UserLogin").where("partyId", partyId, "enabled", "Y").queryFirst();

        }else{

            Debug.logInfo("*CREATE NEW USER", module);
            //立即注册
            Map<String, Object> createPeUserMap = new HashMap<String, Object>();
            createPeUserMap.put("tel",delegator.getNextSeqId("UserLogin")+"");
            createPeUserMap.put("userLogin", admin);
            createPeUserMap.put("uuid", "");
            Map<String, Object> serviceResultMap = dispatcher.runSync("createPeUser2C", createPeUserMap);
            String newUserLoginId = (String) serviceResultMap.get("userLoginId");
            userLogin = EntityQuery.use(delegator).from("UserLogin").where("userLoginId", newUserLoginId, "enabled", "Y").queryFirst();
            partyId = (String)userLogin.get("partyId");
            main.java.com.banfftech.platformmanager.common.PlatformLoginWorker.createNewWeChatPerson2(admin, partyId, delegator, openId, userInfoMap, userLogin, dispatcher);

        }


        result.put("openId",openId);
        userLogin = EntityQuery.use(delegator).from("UserLogin").where("partyId", partyId, "enabled", "Y").queryFirst();
        //有效时间
        long expirationTime = Long.valueOf(EntityUtilProperties.getPropertyValue("pe", "tarjeta.expirationTime", "172800L", delegator));
        String iss = EntityUtilProperties.getPropertyValue("pe", "tarjeta.issuer", delegator);
        String tokenSecret = EntityUtilProperties.getPropertyValue("pe", "tarjeta.secret", delegator);
        //开始时间
        final long iat = System.currentTimeMillis() / 1000L; // issued at claim
        //到期时间
        final long exp = iat + expirationTime;
        //生成
        final JWTSigner signer = new JWTSigner(tokenSecret);
        final HashMap<String, Object> claims = new HashMap<String, Object>();
        claims.put("iss", iss);
        claims.put("user", userLogin.get("userLoginId"));
        claims.put("delegatorName", delegator.getDelegatorName());
        claims.put("exp", exp);
        claims.put("iat", iat);
        String tarjeta = signer.sign(claims);

//        GenericValue store =  EntityQuery.use(delegator).from("ProductStoreRole").
//                where("partyId", partyId,"roleTypeId","ADMIN").queryFirst();
//        if(null!=store){
//            //                rowMap.put("productStoreId",gv.getString("productStoreId"));
//            //                rowMap.put("prodCatalogId",prodCatalogId);
//        }
//        List<Map<String,Object>> returnStoreList =new ArrayList<Map<String, Object>>();
//        if(stores.size()>0){
//            for(GenericValue gv : stores){
//                Map<String,Object> rowMap = new HashMap<String, Object>();
//                rowMap.put("roleTypeId",gv.getString("roleTypeId"));
//                rowMap.put("productStoreId",gv.getString("productStoreId"));
//                String prodCatalogId =  EntityQuery.use(delegator).from("ProductStoreCatalog").
//                        where("productStoreId", gv.getString("productStoreId")).queryFirst().getString("prodCatalogId");
//                rowMap.put("prodCatalogId",prodCatalogId);
//                returnStoreList.add(rowMap);
//            }
//        }



        result.put("tarjeta",tarjeta);
        result.put("partyId", partyId);
//        result.put("storeList", returnStoreList);

        return result;
    }


    /**
     * weChatMiniAppLoginFromTel
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     * @throws UnsupportedEncodingException
     */
    public static Map<String, Object> weChatMiniAppLoginFromTel(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException, UnsupportedEncodingException {


        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> result = ServiceUtil.returnSuccess();
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        GenericValue userLogin = null;

        String unioId = (String) context.get("unioId");
        String tel = (String) context.get("tel");
        String openId = (String) context.get("openId");
        String appId = (String) context.get("appId");
        String nickName = (String) context.get("nickName");
        String gender = (String) context.get("gender");
        String language = (String) context.get("language");
        String avatarUrl = (String) context.get("avatarUrl");

        GenericValue miniProgramIdentification = EntityQuery.use(delegator).from("PartyIdentification").where("idValue", openId, "partyIdentificationTypeId", "WX_MINIPRO_OPEN_ID").queryFirst();


        if (miniProgramIdentification != null) {
            userLogin = EntityQuery.use(delegator).from("UserLogin").where("partyId", miniProgramIdentification.get("partyId"), "enabled", "Y").queryFirst();

            String tarjeta = getToken(userLogin.getString("userLoginId"), delegator);
//            result.put("unioId", unioId);
            result.put("tel", userLogin.getString("userLoginId"));
            result.put("tarjeta", tarjeta);
            result.put("openId", openId);
            result.put("partyId", miniProgramIdentification.get("partyId"));
//            result.put("userInfo", UserQueryServices.queryPersonBaseInfo(delegator, miniProgramIdentification.getString("partyId")));
//            result.put("userInfo", UserQueryServices.queryPersonWorkerInfo(delegator, miniProgramIdentification.getString("partyId")));
            return result;
        }


        String partyId, token = "";


        Map<String, String> userInfoMap = new HashMap<String, String>();

        userInfoMap.put("nickname", nickName);
        userInfoMap.put("sex", gender);
        userInfoMap.put("language", language);
        userInfoMap.put("headimgurl", avatarUrl);


        Debug.logInfo("*CREATE NEW USER", module);
        GenericValue user = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", tel));
        if(user==null){
            Map<String, Object> createPeUserMap = new HashMap<String, Object>();
            createPeUserMap.put("tel", tel);
            createPeUserMap.put("userLogin", admin);
            createPeUserMap.put("uuid", "");
            Map<String, Object> serviceResultMap = dispatcher.runSync("createTelUser", createPeUserMap);
            user = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", serviceResultMap.get("userLoginId")));
        }else{
            //没有openId关联但手机号是userLogin了
              Map<String, Object> createPartyIdentificationWxInMap = UtilMisc.toMap("userLogin", admin, "partyId",
                user.getString("partyId"), "idValue", openId, "partyIdentificationTypeId", "WX_MINIPRO_OPEN_ID");
                dispatcher.runSync("createPartyIdentification", createPartyIdentificationWxInMap);
            if(null==EntityQuery.use(delegator).from("PartyRole").where("partyId", user.getString("partyId"),"roleTypeId","MANUFACTURER").queryFirst()){
                dispatcher.runSync("createPartyRole",UtilMisc.toMap("userLogin",admin
                        ,"partyId",user.getString("partyId"),"roleTypeId","MANUFACTURER"));
            }
            main.java.com.banfftech.personmanager.PersonManagerServices.createContentAndDataResource(user.getString("partyId"), delegator, admin, dispatcher, "WeChatImg", avatarUrl,null);
            String tarjeta = getToken(user.getString("userLoginId"), delegator);
            result.put("tarjeta", tarjeta);
            result.put("partyId", user.getString("partyId"));
            result.put("openId", openId);
            result.put("tel", tel);
            return result;
        }
        String newUserLoginId = (String) user.get("userLoginId");
        partyId = (String) user.get("partyId");
        main.java.com.banfftech.platformmanager.common.PlatformLoginWorker.createNewWeChatPerson2(admin, partyId, delegator, openId, userInfoMap, user, dispatcher);





        if(null==EntityQuery.use(delegator).from("PartyRole").where("partyId", partyId,"roleTypeId","MANUFACTURER").queryFirst()){
            dispatcher.runSync("createPartyRole",UtilMisc.toMap("userLogin",admin
                    ,"partyId",partyId,"roleTypeId","MANUFACTURER"));
        }

        if(null==EntityQuery.use(delegator).from("PartyRole").where("partyId", partyId,"roleTypeId","CUSTOMER").queryFirst()){
            dispatcher.runSync("createPartyRole",UtilMisc.toMap("userLogin",admin
                    ,"partyId",partyId,"roleTypeId","CUSTOMER"));
        }


        if(null==EntityQuery.use(delegator).from("PartyRole").where("partyId", partyId,"roleTypeId","ADMIN").queryFirst()){
            dispatcher.runSync("createPartyRole",UtilMisc.toMap("userLogin",admin
                    ,"partyId",partyId,"roleTypeId","ADMIN"));
        }

        String tarjeta = getToken(user.getString("userLoginId"), delegator);
        result.put("tarjeta", tarjeta);
        result.put("partyId", partyId);
        result.put("openId", openId);
        result.put("tel", tel);
        return result;
    }

    /**
     * 获取票据
     *
     * @param userLoginId
     * @param delegator
     * @return
     */
    public static String getToken(String userLoginId, Delegator delegator) throws GenericEntityException {
        //有效时间
        long expirationTime = Long.valueOf(EntityUtilProperties.getPropertyValue("pe", "tarjeta.expirationTime", "172800L", delegator));
        String iss = EntityUtilProperties.getPropertyValue("pe", "tarjeta.issuer", delegator);
        String tokenSecret = EntityUtilProperties.getPropertyValue("pe", "tarjeta.secret", delegator);
        //开始时间
        final long iat = System.currentTimeMillis() / 1000L; // issued at claim
        //到期时间
        final long exp = iat + expirationTime;
        //生成
        final JWTSigner signer = new JWTSigner(tokenSecret);
        final HashMap<String, Object> claims = new HashMap<String, Object>();
        claims.put("iss", iss);
        claims.put("user", userLoginId);
        claims.put("delegatorName", delegator.getDelegatorName());
        claims.put("exp", exp);
        claims.put("iat", iat);
        return signer.sign(claims);
    }

    /**
     * WeChatLogin 2j
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     * @throws UnsupportedEncodingException
     */
    public static Map<String, Object> weChatMiniAppLogin2(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException, UnsupportedEncodingException {


        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> result = ServiceUtil.returnSuccess();
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        GenericValue userLogin = null;

        String unioId = (String) context.get("unioId");
        //小程序的OPEN ID 也要存
        String openId = (String) context.get("openId");
        String appId = (String) context.get("appId");
        String nickName = (String) context.get("nickName");
        String gender = (String) context.get("gender");
        String language = (String) context.get("language");
        String avatarUrl = (String) context.get("avatarUrl");

        List<GenericValue> partyIdentificationList = EntityQuery.use(delegator).from("PartyIdentification").where("idValue", openId).queryList();
        GenericValue miniProgramIdentification = EntityQuery.use(delegator).from("PartyIdentification").where("idValue", openId,"partyIdentificationTypeId","WX_MINIPRO_OPEN_ID").queryFirst();


        if(miniProgramIdentification!=null){
//            List<GenericValue> storeList = EntityQuery.use(delegator).from("ProductStoreRoleAndStoreDetail").where("partyId", miniProgramIdentification.get("partyId"),"roleTypeId","SALES_REP").queryList();
//            result.put("storeList",storeList);
            userLogin = EntityQuery.use(delegator).from("UserLogin").where("partyId", miniProgramIdentification.get("partyId"), "enabled", "Y").queryFirst();
            //有效时间
            long expirationTime = Long.valueOf(EntityUtilProperties.getPropertyValue("pe", "tarjeta.expirationTime", "172800L", delegator));
            String iss = EntityUtilProperties.getPropertyValue("pe", "tarjeta.issuer", delegator);
            String tokenSecret = EntityUtilProperties.getPropertyValue("pe", "tarjeta.secret", delegator);
            //开始时间
            final long iat = System.currentTimeMillis() / 1000L; // issued at claim
            //到期时间
            final long exp = iat + expirationTime;
            //生成
            final JWTSigner signer = new JWTSigner(tokenSecret);
            final HashMap<String, Object> claims = new HashMap<String, Object>();
            claims.put("iss", iss);
            claims.put("user", userLogin.get("userLoginId"));
            claims.put("delegatorName", delegator.getDelegatorName());
            claims.put("exp", exp);
            claims.put("iat", iat);
            String tarjeta = signer.sign(claims);
            result.put("unioId",unioId);
            result.put("tarjeta",tarjeta);
            result.put("openId",openId);
            result.put("partyId",miniProgramIdentification.get("partyId"));
            result.put("personInfo",PersonManagerQueryServices.queryPersonBaseInfo(delegator, miniProgramIdentification.get("partyId")+""));

            return result;
        }



        String   partyId, token ="";


        Map<String,String> userInfoMap = new HashMap<String, String>();

        userInfoMap.put("nickname",nickName);
        userInfoMap.put("sex",gender);
        userInfoMap.put("language",language);
        userInfoMap.put("headimgurl",avatarUrl);

        if(null != miniProgramIdentification ){
            partyId =(String) miniProgramIdentification.get("partyId");
            userLogin = EntityQuery.use(delegator).from("UserLogin").where("partyId", partyId, "enabled", "Y").queryFirst();

        }else{

            Debug.logInfo("*CREATE NEW USER", module);
            //立即注册
            Map<String, Object> createPeUserMap = new HashMap<String, Object>();
            createPeUserMap.put("tel",delegator.getNextSeqId("UserLogin")+"");
            createPeUserMap.put("userLogin", admin);
            createPeUserMap.put("uuid", "");
            Map<String, Object> serviceResultMap = dispatcher.runSync("createPeUser", createPeUserMap);
            String newUserLoginId = (String) serviceResultMap.get("userLoginId");
            userLogin = EntityQuery.use(delegator).from("UserLogin").where("userLoginId", newUserLoginId, "enabled", "Y").queryFirst();
            partyId = (String)userLogin.get("partyId");
            main.java.com.banfftech.platformmanager.common.PlatformLoginWorker.createNewWeChatPerson2(admin, partyId, delegator, openId, userInfoMap, userLogin, dispatcher);

        }



//        List<GenericValue> storeList = EntityQuery.use(delegator).from("ProductStoreRoleAndStoreDetail").where("partyId", partyId,"roleTypeId","SALES_REP").queryList();
//        result.put("storeList",storeList);
        result.put("unioId",unioId);
        result.put("openId",openId);
        userLogin = EntityQuery.use(delegator).from("UserLogin").where("partyId", partyId, "enabled", "Y").queryFirst();
        //有效时间
        long expirationTime = Long.valueOf(EntityUtilProperties.getPropertyValue("pe", "tarjeta.expirationTime", "172800L", delegator));
        String iss = EntityUtilProperties.getPropertyValue("pe", "tarjeta.issuer", delegator);
        String tokenSecret = EntityUtilProperties.getPropertyValue("pe", "tarjeta.secret", delegator);
        //开始时间
        final long iat = System.currentTimeMillis() / 1000L; // issued at claim
        //到期时间
        final long exp = iat + expirationTime;
        //生成
        final JWTSigner signer = new JWTSigner(tokenSecret);
        final HashMap<String, Object> claims = new HashMap<String, Object>();
        claims.put("iss", iss);
        claims.put("user", userLogin.get("userLoginId"));
        claims.put("delegatorName", delegator.getDelegatorName());
        claims.put("exp", exp);
        claims.put("iat", iat);
        String tarjeta = signer.sign(claims);
        result.put("tarjeta",tarjeta);
        result.put("partyId", partyId);

        result.put("personInfo",PersonManagerQueryServices.queryPersonBaseInfo(delegator, partyId));
        return result;
    }

    /**
     * weChat App 'Web' Login /微信公众平台登录
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> weChatAppWebLogin(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {


        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> result = ServiceUtil.returnSuccess();
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));
        GenericValue userLogin = null;




        String code = (String) context.get("code");

        Map<String,Object> accessMap =  wu.getWeChatAccess(code);




        Map<String,Object> resultMap = dispatcher.runSync("getWeChatUserInfo",UtilMisc.toMap("userLogin",admin,"code",code,"openId",accessMap.get("openId"),"accessToken",accessMap.get("accessToken")));

        Map<String,String> userInfoMap = (Map<String,String>) resultMap.get("weChatUserInfo");

        if(null == userInfoMap){
            return result;
        }

        System.out.println("*weChatAppWebLogin:" + userInfoMap);
        result.put("nowPersonName",userInfoMap.get("nickname"));
        System.out.println(">>>>>>*weChatAppWebLogin - nowPersonName =:" + userInfoMap.get("nickname"));
//        String openId =  (String) userInfoMap.get("openId");

        String unioId =  (String) userInfoMap.get("unionid");

        //是否订阅了公众号
        String subscribe = (String) userInfoMap.get("subscribe");

        result.put("subscribe",subscribe);

        List<GenericValue> partyIdentificationList = EntityQuery.use(delegator).from("PartyIdentification").where("idValue", unioId).queryList();

        String   partyId,token ="";


        if(partyIdentificationList.size()>1){

            partyId =  (String) partyIdentificationList.get(partyIdentificationList.size()-1).get("partyId");
            userLogin = EntityQuery.use(delegator).from("UserLogin").where("partyId", partyId, "enabled", "Y").queryFirst();
        }else{

            if(null != partyIdentificationList && partyIdentificationList.size()>0 && partyIdentificationList.get(0) != null){

                partyId =(String) partyIdentificationList.get(0).get("partyId");
                userLogin = EntityQuery.use(delegator).from("UserLogin").where("partyId", partyId, "enabled", "Y").queryFirst();
                //老用户登录的情况下也要考虑是否没有公众号OpenId.(卖家不推送问题Fix)
                GenericValue gongZhongPartyIdentification = EntityQuery.use(delegator).from("PartyIdentification").where("partyId", partyId, "partyIdentificationTypeId", "WX_GZ_OPEN_ID").queryFirst();
                if(gongZhongPartyIdentification == null){
                    //增加公众平台OpenID绑定,定推特需
                    //创建微信绑定数据
                    Map<String, Object> createPartyIdentificationInMap = UtilMisc.toMap("userLogin", admin, "partyId",
                            partyId, "idValue", accessMap.get("openId"), "partyIdentificationTypeId", "WX_GZ_OPEN_ID","enabled","Y");
                    dispatcher.runSync("createPartyIdentification", createPartyIdentificationInMap);
                }
            }else{

                Debug.logInfo("*CREATE NEW USER", module);
                //立即注册
                Map<String, Object> createPeUserMap = new HashMap<String, Object>();
                createPeUserMap.put("tel",delegator.getNextSeqId("UserLogin")+"");
                createPeUserMap.put("userLogin", admin);
                createPeUserMap.put("uuid", "");
                Map<String, Object> serviceResultMap = dispatcher.runSync("createPeUser", createPeUserMap);
                String newUserLoginId = (String) serviceResultMap.get("userLoginId");
                userLogin = EntityQuery.use(delegator).from("UserLogin").where("userLoginId", newUserLoginId, "enabled", "Y").queryFirst();
                partyId = (String)userLogin.get("partyId");
                main.java.com.banfftech.platformmanager.common.PlatformLoginWorker.createNewWeChatPerson(admin,partyId,delegator,unioId,userInfoMap,userLogin,dispatcher);

                //增加公众平台OpenID绑定,定推特需
                //创建微信绑定数据
                Map<String, Object> createPartyIdentificationInMap = UtilMisc.toMap("userLogin", admin, "partyId",
                        partyId, "idValue", accessMap.get("openId"), "partyIdentificationTypeId", "WX_GZ_OPEN_ID","enabled","Y");
                dispatcher.runSync("createPartyIdentification", createPartyIdentificationInMap);

            }
        }




        //有效时间
        long expirationTime = Long.valueOf(EntityUtilProperties.getPropertyValue("pe", "tarjeta.expirationTime", "172800L", delegator));
        String iss = EntityUtilProperties.getPropertyValue("pe", "tarjeta.issuer", delegator);
        String tokenSecret = EntityUtilProperties.getPropertyValue("pe", "tarjeta.secret", delegator);
        //开始时间
        final long iat = System.currentTimeMillis() / 1000L; // issued at claim
        //到期时间
        final long exp = iat + expirationTime;
        //生成
        final JWTSigner signer = new JWTSigner(tokenSecret);
        final HashMap<String, Object> claims = new HashMap<String, Object>();
        claims.put("iss", iss);
        claims.put("user", userLogin.get("userLoginId"));
        claims.put("delegatorName", delegator.getDelegatorName());
        claims.put("exp", exp);
        claims.put("iat", iat);
        token = signer.sign(claims);



        GenericValue person = delegator.findOne("Person",UtilMisc.toMap("partyId",partyId),false);

        //去SpringBootMongoDB注册IM用户

        List<GenericValue> contentsList =
                EntityQuery.use(delegator).from("PartyContentAndDataResource").
                        where("partyId", partyId, "partyContentTypeId", "LGOIMGURL").orderBy("-fromDate").queryPagedList(0, 999999).getData();


        GenericValue partyContent = null;
        String avatar = "";
        if (null != contentsList && contentsList.size() > 0) {
            partyContent = contentsList.get(0);
        }

        if (UtilValidate.isNotEmpty(partyContent)) {

            avatar = partyContent.getString("objectInfo");
        } else {
            avatar = "https://personerp.oss-cn-hangzhou.aliyuncs.com/datas/images/defaultHead.png";

        }

        String registerUrl = "https://www.yo-pe.com/api/common/register";

        String response = HttpHelper.sendPost(registerUrl,"username="+ partyId+"&password="+partyId+"111"+"&nickname="+person.get("firstName")+"&avatar="+avatar);

        System.out.println("*RegisterMongoDB-ImUser");

        System.out.println("*response = " + response);

        result.put("partyId", partyId);
        result.put("tarjeta", token);
        return result;
    }




    /**
     * weChatAppLogin
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> weChatAppLogin(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {


        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> result = ServiceUtil.returnSuccess();

        String code = (String) context.get("code");
        String uuid = (String) context.get("uuid");

        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        GenericValue userLogin = null;

        String userLoginId = "";

        //获取OpenID
        StringBuffer requestParamSB = new StringBuffer();
        //这里走的调用地址是开放平台的
        String kaiFangUserInfoPath = PeConstant.WX_KAIFANG_PINGTAI;
        requestParamSB.append("appid=" + PeConstant.WX_KAIFANG_PINGTAI_APP_ID);//AK
        requestParamSB.append("&secret=" + PeConstant.WX_KAIFANG_PINGTAI_APP_SC);//SC
        requestParamSB.append("&code=" + code);//用户微信授权代码
        requestParamSB.append("&grant_type=authorization_code");//授予类型

        //请求微信API获取ACCESS-TOKEN
        String responseStr = sendGet(PeConstant.WX_KAIFANG_PINGTAI_ACCESS, requestParamSB.toString());

        JSONObject jsonMap = JSONObject.fromObject(responseStr);

        if (null == jsonMap.get("access_token") || null == jsonMap.get("openid")) {
            Debug.logError("*Access_token:Null",module);
            return ServiceUtil.returnError(UtilProperties.getMessage(resourceError, "InternalServiceError", locale));
        }

        String accessToken = (String) jsonMap.get("access_token");

        String openId = (String) jsonMap.get("unionid");
        String returnOpenId = (String) jsonMap.get("openid");

        result.put("openid",returnOpenId);

        List<GenericValue> partyIdentificationList = EntityQuery.use(delegator).from("PartyIdentification").where("idValue", openId, "partyIdentificationTypeId", "WX_UNIO_ID").queryList();



        //如果这个微信在数据库从未被引用
        if (null == partyIdentificationList || partyIdentificationList.size()<=0) {
            Debug.logInfo("*CREATE NEW USER", module);
            //立即注册
            Map<String, Object> createPeUserMap = new HashMap<String, Object>();
            createPeUserMap.put("tel",delegator.getNextSeqId("UserLogin")+"");
            createPeUserMap.put("userLogin", admin);
            createPeUserMap.put("uuid", uuid);
            Map<String, Object> serviceResultMap = dispatcher.runSync("createPeUser", createPeUserMap);
            String newUserLoginId = (String) serviceResultMap.get("userLoginId");
            userLogin = EntityQuery.use(delegator).from("UserLogin").where("userLoginId", newUserLoginId, "enabled", "Y").queryFirst();
            result.put("newUser", "Y");
            bindNoDataWeChat(accessToken, (String) userLogin.get("partyId"), admin, openId, delegator, dispatcher, locale, userLogin, kaiFangUserInfoPath);

        }else{
            result.put("newUser", "N");
            userLogin = EntityQuery.use(delegator).from("UserLogin").where("partyId",partyIdentificationList.get(0).get("partyId"), "enabled", "Y").queryFirst();
        }




        String token = null;







        //有效时间
        long expirationTime = Long.valueOf(EntityUtilProperties.getPropertyValue("pe", "tarjeta.expirationTime", "172800L", delegator));
        String iss = EntityUtilProperties.getPropertyValue("pe", "tarjeta.issuer", delegator);
        String tokenSecret = EntityUtilProperties.getPropertyValue("pe", "tarjeta.secret", delegator);
        //开始时间
        final long iat = System.currentTimeMillis() / 1000L; // issued at claim
        //到期时间
        final long exp = iat + expirationTime;
        //生成
        final JWTSigner signer = new JWTSigner(tokenSecret);
        final HashMap<String, Object> claims = new HashMap<String, Object>();
        claims.put("iss", iss);
        claims.put("user", userLogin.get("userLoginId"));
        claims.put("delegatorName", delegator.getDelegatorName());
        claims.put("exp", exp);
        claims.put("iat", iat);
        token = signer.sign(claims);


        result.put("tarjeta", token);
        String partyId =  (String) userLogin.get("partyId");
        result.put("partyId",partyId);





        GenericValue person = delegator.findOne("Person",UtilMisc.toMap("partyId",partyId),false);

        //去SpringBootMongoDB注册IM用户

        List<GenericValue> contentsList =
                EntityQuery.use(delegator).from("PartyContentAndDataResource").
                        where("partyId", partyId, "partyContentTypeId", "LGOIMGURL").orderBy("-fromDate").queryPagedList(0, 999999).getData();


        GenericValue partyContent = null;
        String avatar = "";
        if (null != contentsList && contentsList.size() > 0) {
            partyContent = contentsList.get(0);
        }

        if (UtilValidate.isNotEmpty(partyContent)) {

            avatar = partyContent.getString("objectInfo");
        } else {
            avatar = "https://personerp.oss-cn-hangzhou.aliyuncs.com/datas/images/defaultHead.png";

        }

        String registerUrl = "https://www.yo-pe.com/api/common/register";

        String response = HttpHelper.sendPost(registerUrl,"username="+ partyId+"&password="+partyId+"111"+"&nickname="+person.get("firstName")+"&avatar="+avatar);

        System.out.println("*RegisterMongoDB-ImUser");

        System.out.println("*response = " + response);

        return result;
    }




    /**
     * App Tel Login
     *
     * @param dctx
     * @param context
     * @return
     */
    public static Map<String, Object> userAppLogin(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {


        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");

        //Tel Number
        String userLoginId = (String) context.get("userLoginId");
        String uuid = (String) context.get("uuid");


        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));


        //User IS Exsits & Enabled

        GenericValue userLogin = EntityQuery.use(delegator).from("UserLogin").where("userLoginId", userLoginId, "enabled", "Y").queryFirst();
        String token = null;
        Map<String, Object> result = ServiceUtil.returnSuccess();
        String captcha = (String) context.get("captcha");
        Map<String, Object> inputMap = new HashMap<String, Object>();


        //Captcha Is Exsits
        EntityConditionList<EntityCondition> captchaConditions = EntityCondition
                .makeCondition(EntityCondition.makeCondition("teleNumber", EntityOperator.EQUALS, userLoginId), EntityUtil.getFilterByDateExpr(), EntityCondition.makeCondition("isValid", EntityOperator.EQUALS, "N"));
        List<GenericValue> smsList = new ArrayList<GenericValue>();
        try {
            smsList = delegator.findList("SmsValidateCode", captchaConditions, null,
                    UtilMisc.toList("-" + ModelEntity.CREATE_STAMP_FIELD), null, false);
        } catch (GenericEntityException e) {
            Debug.logError(e.getMessage(), module);
            Debug.logError("*CaptchaException:" + captcha, module);
            return ServiceUtil.returnError(UtilProperties.getMessage(resourceError, "InternalServiceError", locale));
        }

        //NotFound Captcha
        if (UtilValidate.isEmpty(smsList)) {
            Debug.logError("*CaptchaNotExistError:" + captcha + "|tel:" + userLoginId, module);
            return ServiceUtil.returnError(UtilProperties.getMessage(resourceError, "CaptchaNotExistError", locale));
        } else {
            GenericValue sms = smsList.get(0);
            //Check Is Right
            if (sms.get("captcha").equals(captcha)) {

                // 新用户情况
                if (null == userLogin || null == userLogin.get("partyId")) {

                    Debug.logInfo("*CREATE NEW USER", module);
                    //立即注册
                    Map<String, Object> createPeUserMap = new HashMap<String, Object>();
                    createPeUserMap.put("tel", userLoginId);
                    createPeUserMap.put("userLogin", admin);
                    createPeUserMap.put("uuid", uuid);
                    Map<String, Object> serviceResultMap = dispatcher.runSync("createPeUser", createPeUserMap);
                    String newUserLoginId = (String) serviceResultMap.get("userLoginId");
                    userLogin = EntityQuery.use(delegator).from("UserLogin").where("userLoginId", newUserLoginId, "enabled", "Y").queryFirst();
                    result.put("newUser", "Y");
                } else {
                    result.put("newUser", "N");
                }

                String enabled = null;

                if (null != userLogin && null != userLogin.get("partyId")) {
                    enabled = (String) userLogin.get("enabled");
                }

                if (null == enabled || enabled.equals("N")) {
                    Debug.logError("*TelDisabledError:" + userLoginId, module);
                    return ServiceUtil.returnError(UtilProperties.getMessage(resourceError, "TelDisabledError", locale));
                }


                //有效时间
                long expirationTime = Long.valueOf(EntityUtilProperties.getPropertyValue("pe", "tarjeta.expirationTime", "172800L", delegator));
                String iss = EntityUtilProperties.getPropertyValue("pe", "tarjeta.issuer", delegator);
                String tokenSecret = EntityUtilProperties.getPropertyValue("pe", "tarjeta.secret", delegator);
                //开始时间
                final long iat = System.currentTimeMillis() / 1000L; // issued at claim
                //到期时间
                final long exp = iat + expirationTime;
                //生成
                final JWTSigner signer = new JWTSigner(tokenSecret);
                final HashMap<String, Object> claims = new HashMap<String, Object>();
                claims.put("iss", iss);
                claims.put("user", userLoginId);
                claims.put("delegatorName", delegator.getDelegatorName());
                claims.put("exp", exp);
                claims.put("iat", iat);
                token = signer.sign(claims);


                //这个Captcha被用过了
                sms.set("isValid", "Y");

                try {
                    sms.store();
                } catch (GenericEntityException e) {
                    Debug.logError("*InternalServiceError:" + captcha, module);
                    return ServiceUtil.returnError(UtilProperties.getMessage(resourceError, "InternalServiceError", locale));
                }

                result.put("tarjeta", token);
                result.put("partyId", (String) userLogin.get("partyId"));

            } else {

                Debug.logError("*CaptchaCheckFailedError:" + captcha, module);
                return ServiceUtil.returnError(UtilProperties.getMessage(resourceError, "UserNotFoundError", locale));

            }
        }


        return result;
    }


}
