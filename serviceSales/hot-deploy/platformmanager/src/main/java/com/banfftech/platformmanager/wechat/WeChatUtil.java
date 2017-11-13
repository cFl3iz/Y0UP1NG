package main.java.com.banfftech.platformmanager.wechat;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;



import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import main.java.com.banfftech.platformmanager.constant.PeConstant;
import main.java.com.banfftech.platformmanager.util.EmojiFilter;
import main.java.com.banfftech.platformmanager.util.EmojiHandler;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.FileUtil;
import org.apache.ofbiz.base.util.StringUtil;
import org.apache.ofbiz.base.util.UtilDateTime;
import org.apache.ofbiz.base.util.UtilGenerics;
import org.apache.ofbiz.base.util.UtilHttp;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.model.ModelEntity;
import org.apache.ofbiz.entity.transaction.GenericTransactionException;
import org.apache.ofbiz.entity.transaction.TransactionUtil;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.minilang.MiniLangException;
import org.apache.ofbiz.minilang.SimpleMapProcessor;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ModelService;
import org.apache.ofbiz.service.ServiceAuthException;
import org.apache.ofbiz.service.ServiceUtil;

import static main.java.com.banfftech.platformmanager.util.HttpHelper.sendGet;
import static main.java.com.banfftech.platformmanager.util.HttpHelper.sendPost;


/**
 * Created by S.Y.L on 2017/5/2.
 */
public class WeChatUtil {

    public final static String module = WeChatUtil.class.getName();

    //此处应当从数据库取值,苟且先将硬代码使用。

    //We Chat App Key ID
    private static String APP_KEY = PeConstant.WECHAT_GZ_APP_ID;
    //We Caht Secret
    private static String ACCESS_KEY_SECRET = PeConstant.ACCESS_KEY_SECRET;







    /**
     * Get Access Token / By Code
     *
     * @author S.Y.L
     * @param  code
     * @return Map
     */
    public static Map<String,Object> getWeChatAccess(String code)  {


        Map<String,Object> returnMap = new HashMap<String, Object>();
        StringBuffer requestParamSB = new StringBuffer();

        requestParamSB.append("appid="+APP_KEY);//AK
        requestParamSB.append("&secret="+ACCESS_KEY_SECRET);//SC
        requestParamSB.append("&code="+code);//用户微信授权代码
        requestParamSB.append("&grant_type=authorization_code");//授予类型

        String responseStr = sendGet(PeConstant.WX_ACCESS_PATH,requestParamSB.toString());

        JSONObject jsonMap = JSONObject.fromObject(responseStr);

        if(null == jsonMap.get("access_token") || null== jsonMap.get("openid")){
            //Debug.logInfo("*Not Foud AccessToken " +module);
        }


        String accessToken = (String) jsonMap.get("access_token");
        String openId      = (String) jsonMap.get("openid");


        returnMap.put("accessToken",accessToken);
        returnMap.put("openId", openId);
        returnMap.put("code", code);


        return returnMap;
    }


//    /**
//     * Get Access Token
//     *
//     * @return
//     * @throws GenericServiceException
//     */
//    public static String getWeChatAccess()
//            throws GenericServiceException {
//
//        // Servlet Head
//
//
//        StringBuffer requestParamSB = new StringBuffer();
//        requestParamSB.append("?grant_type=client_credential");//授予类型
//        requestParamSB.append("&appid="+APP_KEY);//AK
//        requestParamSB.append("&secret="+ACCESS_KEY_SECRET);//SC
//
//
//        // Do HTTP Get
//        String responseStr = sendGet(PeConstant.WX_GET_ACCESS_TOKEN,requestParamSB.toString());
//
//        JSONObject jsonMap = JSONObject.fromObject(responseStr);
//
//        if(null == jsonMap.get("access_token") || null== jsonMap.get("openid")){
//            //THROW ERROR
//        }
//        String accessToken = (String) jsonMap.get("access_token");
//
//
//
//
//        return accessToken;
//    }







    /**
     * Get We Chat User Info
     *
     * @author S.Y.L
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> getWeChatUserInfo(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> result = ServiceUtil.returnSuccess();
        Map<String,String> weChatUserInfo = new HashMap<String, String>();

       StringBuffer requestParamSB = new StringBuffer();

        String contentAK=(String)context.get("accessToken");



        //此处微信做了更改，一律从新获取ak
        StringBuffer requestParamSBa = new StringBuffer();

        requestParamSBa.append("appid="+APP_KEY);//AK
        requestParamSBa.append("&secret="+ACCESS_KEY_SECRET);//SC
        requestParamSBa.append("&grant_type=client_credential");

        // Do HTTP Get
        String responseStrAk = sendPost(PeConstant.WX_USER_INFO_CGI_BIN_TOKEN_PATH, requestParamSBa.toString());

        JSONObject jsonMapAk = JSONObject.fromObject(responseStrAk);

        String realAk   =(String) jsonMapAk.get("access_token");
        System.out.println("==================================================jsonMapAk= " +jsonMapAk);
        System.out.println("==================================================realAk= " +realAk);
        System.out.println("==================================================openId= " +context.get("openId"));
        if(context.get("openId")!=null){
            requestParamSB.append("access_token="+realAk);//AK
            requestParamSB.append("&openid="+(String)context.get("openId"));//AK
        }else{
            return result;
        }



        // Do HTTP Get
        String responseStr = sendGet(PeConstant.WX_USER_INFO_CGI_BIN_PATH,requestParamSB.toString());

        JSONObject jsonMap = JSONObject.fromObject(responseStr);
        System.out.println("==================================================jsonMap= " + jsonMap);
        if(null != jsonMap.get("nickname")){
//            weChatUserInfo.put("weChatId",(String) context.get("openId"));
//            weChatUserInfo.put("nickname",(String) jsonMap.get("nickname"));
//            weChatUserInfo.put("headimgurl",(String) jsonMap.get("headimgurl"));
//            weChatUserInfo.put("sex",jsonMap.get("sex")+"");
//            weChatUserInfo.put("language",(String) jsonMap.get("language"));
//            weChatUserInfo.put("city",(String) jsonMap.get("city"));
//            weChatUserInfo.put("province",(String) jsonMap.get("province"));
//            weChatUserInfo.put("country",(String) jsonMap.get("country"));
//            String uuid = (String) jsonMap.get("unionid");
//            weChatUserInfo.put("unionid",(String) jsonMap.get("unionid"));
            //关键字段:是否订阅,这将影响我们是否要他重复关注公众号
            weChatUserInfo.put("subscribe",jsonMap.get("subscribe")+"");

        }



        //用户信息又得单独去获取,否则不关注还拿不到了
        StringBuffer requestParamUserInfoSB = new StringBuffer();
        requestParamUserInfoSB.append("access_token="+contentAK);//AK
        requestParamUserInfoSB.append("&openid="+context.get("openId"));//SC


        String jsonUserInfoMapStr = sendGet(PeConstant.WX_USER_INFO_PATH,requestParamUserInfoSB.toString());

        JSONObject jsonUserInfoMap = JSONObject.fromObject(jsonUserInfoMapStr);

        String wxNickName = (String) jsonUserInfoMap.get("nickname");
        System.out.println("==================================================jsonUserInfoMap= " + jsonUserInfoMap);
        System.out.println("==================================================wxNickName= " + wxNickName);
        if(EmojiFilter.containsEmoji(wxNickName)){
            //包含emoji表情
            wxNickName = EmojiHandler.encodeJava(wxNickName);
        }
        if(null != jsonUserInfoMap.get("nickname")){
            weChatUserInfo.put("weChatId",(String) jsonUserInfoMap.get("openid"));
            weChatUserInfo.put("nickname",wxNickName);
            weChatUserInfo.put("headimgurl",(String) jsonUserInfoMap.get("headimgurl"));
            weChatUserInfo.put("sex",jsonUserInfoMap.get("sex")+"");
            weChatUserInfo.put("language",(String) jsonUserInfoMap.get("language"));
            weChatUserInfo.put("city",(String) jsonUserInfoMap.get("city"));
            weChatUserInfo.put("province",(String) jsonUserInfoMap.get("province"));
            weChatUserInfo.put("country",(String) jsonUserInfoMap.get("country"));
            String uuid = (String) jsonUserInfoMap.get("unionid");
            weChatUserInfo.put("unionid",(String) jsonUserInfoMap.get("unionid"));
        }

//        List<GenericValue> partyIdentificationList = EntityQuery.use(delegator).from("PartyIdentification").where("idValue", (String) jsonUserInfoMap.get("unionid")).queryList();
//
//        String   partyId ="";
//
//        if(partyIdentificationList.size()>1){
//
//            partyId =  (String) partyIdentificationList.get(partyIdentificationList.size()-1).get("partyId");
//
//        }else{
//            if(null != partyIdentificationList && partyIdentificationList.size()>0 && partyIdentificationList.get(0) != null){
//
//                partyId =(String) partyIdentificationList.get(0).get("partyId");
//
//            }
//        }


        // Service Foot
        result.put("weChatUserInfo",weChatUserInfo);
       // result.put("nowPartyId",partyId);
        return result;
    }
}
