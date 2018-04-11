package main.java.com.banfftech.platformmanager.common;

import com.auth0.jwt.JWTExpiredException;
import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import java.io.InputStream;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.condition.EntityConditionList;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.entity.util.EntityUtil;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.entity.util.EntityUtilProperties;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.rmi.CORBA.Util;
import javax.servlet.http.HttpServletRequest;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.entity.GenericEntity;
import javax.servlet.http.HttpServletResponse;
import org.apache.ofbiz.entity.condition.EntityOperator;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import org.apache.ofbiz.base.util.UtilHttp;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;


import java.io.DataInputStream;
import java.sql.Timestamp;
import java.io.FileOutputStream;
import org.apache.ofbiz.entity.model.ModelEntity;
import org.omg.CORBA.portable.Delegate;
import sun.net.www.content.text.Generic;


import javax.servlet.ServletException;

import static main.java.com.banfftech.platformmanager.util.HttpHelper.sendGet;
/**
 * Created by S on 2017/9/11.
 */
public class SmsServices {


    public final static String module = SmsServices.class.getName();
    private static String url = null;
    private static String appkey = null;
    private static String secret = null;
    private static String smsFreeSignName = null;
    private static String smsTemplateCode = null;

    /**
     * Get Tel Captcha
     *
     * @param dctx
     * @param context
     * @return
     */
    public static Map<String, Object> getTelCaptcha(DispatchContext dctx, Map<String, Object> context) {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        String teleNumber = (String) context.get("teleNumber");
        Map<String,Object> resultMap = ServiceUtil.returnSuccess();
        //String smsType = (String) context.get("smsType");// LOGIN 或 REGISTER


        java.sql.Timestamp nowTimestamp = org.apache.ofbiz.base.util.UtilDateTime.nowTimestamp();
        int validTime = Integer.valueOf(EntityUtilProperties.getPropertyValue("pe", "sms.validTime", "900", delegator));
        int intervalTime = Integer
                .valueOf(EntityUtilProperties.getPropertyValue("pe", "sms.intervalTime", "20", delegator));
        boolean sendSMS = false;

        // Find SmsData
        EntityCondition captchaCondition = EntityCondition.makeCondition(
                EntityCondition.makeCondition("teleNumber", EntityOperator.EQUALS, teleNumber),
                EntityUtil.getFilterByDateExpr(), EntityCondition.makeCondition("isValid", EntityOperator.EQUALS, "N"));

        GenericValue sms = null;
        try {
            sms = EntityUtil.getFirst(delegator.findList("SmsValidateCode", captchaCondition, null,
                    UtilMisc.toList("-" + ModelEntity.CREATE_STAMP_FIELD), null, false));
        } catch (GenericEntityException e) {
            // TODO Sms-Ga EXCEPTION
        }

        if (UtilValidate.isEmpty(sms)) {
            sendSMS = true;
        } else {
            org.apache.ofbiz.base.util.Debug.logInfo("The user tel:[" + teleNumber + "]  verfiy code["
                    + sms.getString("captcha") + "], check the interval time , if we'll send new code", module);
            // 如果已有未验证的记录存在，则检查是否过了再次重发的时间间隔，没过就忽略本次请求
            if (org.apache.ofbiz.base.util.UtilDateTime.nowTimestamp().after(org.apache.ofbiz.base.util.UtilDateTime
                    .adjustTimestamp((java.sql.Timestamp) sms.get("fromDate"), Calendar.SECOND, intervalTime))) {
                sms.set("thruDate", nowTimestamp);
                try {
                    sms.store();
                } catch (GenericEntityException e) {

                    // return
                    // ServiceUtil.returnError("CloudCardInternalServiceError"));
                }
                org.apache.ofbiz.base.util.Debug.logInfo("The user tel:[" + teleNumber + "]  will get new verfiy code!",
                        module);
                sendSMS = true;
            }
        }

        if (sendSMS) {
            // 生成验证码
            String captcha = org.apache.ofbiz.base.util.UtilFormatOut
                    .padString(String.valueOf(Math.round((Math.random() * 10e6))), 6, false, '0');
            Map<String, Object> smsValidateCodeMap = UtilMisc.toMap();
            smsValidateCodeMap.put("teleNumber", teleNumber);
            smsValidateCodeMap.put("captcha", captcha);
//            smsValidateCodeMap.put("smsType", smsType);
            smsValidateCodeMap.put("isValid", "N");
            smsValidateCodeMap.put("fromDate", nowTimestamp);
            smsValidateCodeMap.put("thruDate",
                    org.apache.ofbiz.base.util.UtilDateTime.adjustTimestamp(nowTimestamp, Calendar.SECOND, validTime));
            try {
                GenericValue smstGV = delegator.makeValue("SmsValidateCode", smsValidateCodeMap);
                smstGV.create();
            } catch (GenericEntityException e) {

                // return ServiceUtil.returnError(PeSendFailedError"));
            }

            // Send Message
            context.put("phone", teleNumber);
            context.put("code", captcha);
            context.put("product", "友评");

            resultMap.put("captcha",captcha);
            //暂时不发
             sendMessage(dctx, context);
        }

        return resultMap;
    }









    /**
     * Send Message
     *
     * @param dctx
     * @param context
     * @return
     */
    public static Map<String, Object> sendMessage(DispatchContext dctx, Map<String, Object> context) {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();

        // Scope Param
        Locale locale = (Locale) context.get("locale");
        String phone = (String) context.get("phone");
        String code = (String) context.get("code");
        String product = (String) context.get("product");
        String smsTemplateCode = (String) context.get("smsTemplateCode");

        if(null == smsTemplateCode || smsTemplateCode.trim().equals("")){
            smsTemplateCode = "SMS_53800010";
        }

        // Initial Message Config
        getSmsProperty(delegator);

        // 临时硬代码
        TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", "24620277",
                "e698163c26c748be50266f88a11714f1");

        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        req.setExtend("");
        req.setSmsType("normal");
        req.setSmsFreeSignName("友评");

        String json = "{\"code\":\"" + code + "\",\"product\":\"" + "友评" + "\""+"}";
        org.apache.ofbiz.base.util.Debug.logInfo(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> CAPTCHA == "+json, module);
        req.setSmsParamString(json);
        req.setRecNum(phone);
        req.setSmsTemplateCode(smsTemplateCode);

        AlibabaAliqinFcSmsNumSendResponse rsp = null;
        try {
            rsp = client.execute(req);
        } catch (ApiException e) {

        }
        if (rsp != null && !rsp.isSuccess()) {
            org.apache.ofbiz.base.util.Debug
                    .logInfo("something wrong when send the short message, response body:" + rsp.getBody(), module);
        }

        // Service Foot
        return ServiceUtil.returnSuccess();
    }


    /**
     * Get Sms Property
     *
     * @param delegator
     */
    public static void getSmsProperty(Delegator delegator) {
        url = EntityUtilProperties.getPropertyValue("pe", "sms.url", delegator);
        appkey = EntityUtilProperties.getPropertyValue("pe", "sms.appkey", delegator);
        secret = EntityUtilProperties.getPropertyValue("pe", "sms.secret", delegator);
        smsFreeSignName = EntityUtilProperties.getPropertyValue("pe", "sms.smsFreeSignName", delegator);
        smsTemplateCode = EntityUtilProperties.getPropertyValue("pe", "sms.smsTemplateCode", delegator);
    }
}
