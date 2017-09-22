package main.java.com.banfftech.platformmanager.common;

import com.auth0.jwt.JWTExpiredException;
import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;

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
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.*;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.omg.CORBA.portable.Delegate;
import sun.net.www.content.text.Generic;

/**
 * Created by S on 2017/9/8.
 */
public class PlatformLoginWorker {


    public final static String module = PlatformLoginWorker.class.getName();

    public static final String TOKEN_KEY_ATTR = "tarjeta";

    public static final String resourceError = "PlatformManagerErrorUiLabels.xml";

    /**
     * 验证是否拥身份令牌
     *
     * @param request
     * @param response
     * @return
     */
    public static String checkTarjetaLogin(HttpServletRequest request, HttpServletResponse response) {


        //Servlet Head
        HttpSession session = request.getSession();
        Delegator delegator = (Delegator) request.getAttribute("delegator");

        Debug.logInfo("token verify...", module);

        String token = request.getParameter("tarjeta");
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
        } else {
            Debug.logWarning("*Could not find userLogin for token: " + token, module);
            Debug.logWarning("*Claims User Is : " + claims.get("user"), module);
        }
        return "success";
    }





    /**
     * App Tel Login
     *
     * @param dctx
     * @param context
     * @return
     */
    public static Map<String, Object> userAppLogin(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException,GenericServiceException {


        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");

        //Tel Number
        String userLoginId = (String) context.get("userLoginId");
        String uuid        = (String) context.get("uuid");


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
            Debug.logError("*CaptchaException:" + captcha,module);
            return ServiceUtil.returnError(UtilProperties.getMessage(resourceError, "InternalServiceError", locale));
        }

        //NotFound Captcha
        if (UtilValidate.isEmpty(smsList)) {
            Debug.logError("*CaptchaNotExistError:" + captcha + "|tel:" + userLoginId,module);
            return ServiceUtil.returnError(UtilProperties.getMessage(resourceError, "CaptchaNotExistError", locale));
        } else {
            GenericValue sms = smsList.get(0);
            //Check Is Right
            if (sms.get("captcha").equals(captcha)) {

                // 新用户情况
                if(null ==userLogin || null == userLogin.get("partyId")){

                    Debug.logInfo("*CREATE NEW USER",module);
                    //立即注册
                    Map<String,Object> createPeUserMap = new HashMap<String, Object>();
                    createPeUserMap.put("tel",userLoginId);
                    createPeUserMap.put("userLogin",admin);
                    createPeUserMap.put("uuid",uuid);
                    Map<String,Object> serviceResultMap =  dispatcher.runSync("createPeUser",createPeUserMap);
                    String newUserLoginId = (String) serviceResultMap.get("userLoginId");
                    userLogin   = EntityQuery.use(delegator).from("UserLogin").where("userLoginId", newUserLoginId, "enabled", "Y").queryFirst();
                    result.put("newUser", "Y");
                }else{
                    result.put("newUser", "N");
                }

                String enabled = null;

                if(null !=userLogin && null != userLogin.get("partyId")){
                    enabled = (String) userLogin.get("enabled");
                }

                if(null == enabled || enabled.equals("N")){
                    Debug.logError("*TelDisabledError:" + userLoginId,module);
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
                    Debug.logError("*InternalServiceError:" + captcha,module);
                    return ServiceUtil.returnError(UtilProperties.getMessage(resourceError, "InternalServiceError", locale));
                }

                result.put("tarjeta", token);
                result.put("partyId", (String) userLogin.get("partyId"));

            } else {

                Debug.logError("*CaptchaCheckFailedError:" + captcha,module);
                return ServiceUtil.returnError(UtilProperties.getMessage(resourceError, "UserNotFoundError", locale));

            }
        }


        return result;
    }
















}
