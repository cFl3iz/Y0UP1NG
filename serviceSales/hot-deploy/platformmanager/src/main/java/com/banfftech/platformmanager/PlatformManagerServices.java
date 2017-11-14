package main.java.com.banfftech.platformmanager;


import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;
import net.sf.json.JSONObject;
import main.java.com.banfftech.platformmanager.constant.PeConstant;
import main.java.com.banfftech.platformmanager.util.EmojiHandler;
import main.java.com.banfftech.platformmanager.util.UtilTools;
import main.java.com.banfftech.platformmanager.wechat.AccessToken;
import main.java.com.banfftech.platformmanager.wechat.WeChatUtil;
import org.apache.ofbiz.base.util.UtilValidate;
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
import org.eclipse.birt.chart.extension.datafeed.GanttEntry;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;

import static main.java.com.banfftech.personmanager.PersonManagerQueryServices.queryPersonBaseInfo;
import static main.java.com.banfftech.platformmanager.wechat.WeChatUtil.getAccessToken;


/**
 * Created by Administrator on 2017/9/12.
 */
public class PlatformManagerServices {

    public final static String module = PlatformManagerServices.class.getName();











    /**
     * Push WeChat MessageInfo
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> pushWeChatMessageInfo(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> result = ServiceUtil.returnSuccess();


        String openId        = (String) context.get("openId");
        String productId     = (String) context.get("productId");
        String date     = (String) context.get("date");
        String message     = (String) context.get("message");
        String firstName     = (String) context.get("firstName");
        String payToPartyId  = (String) context.get("payToPartyId");
        String tarjeta       = (String) context.get("tarjeta");

        firstName = EmojiHandler.decodeJava(firstName);


        Map<String,String> personInfoMap =  queryPersonBaseInfo(delegator,payToPartyId);


        // 发送模版消息
        AccessToken accessToken = getAccessToken(PeConstant.WECHAT_GZ_APP_ID,PeConstant.ACCESS_KEY_SECRET);
        String URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
        String url = URL.replace("ACCESS_TOKEN", accessToken.getToken());

        JSONObject jsobj1 = new JSONObject();
        JSONObject jsobj2 = new JSONObject();
        JSONObject jsobj3 = new JSONObject();
        JSONObject jsobj4 = new JSONObject();
        JSONObject jsobj5 = new JSONObject();

        String url2 = "http://www.yo-pe.com:3400/WebManager/control/miniChat?" +
                "productId="+productId+"&payToPartyId=" +
                ""+payToPartyId+"&payToPartyHead="+personInfoMap.get("headPortrait")+"&payToPartyFirstName="+personInfoMap.get("firstName");
        System.out.println("*============================================================URL = " + url2);
        jsobj1.put("touser",openId);
        jsobj1.put("template_id","aFCzhfNrWb0GsEr0ZCVuijLPAQ6cPzPedORxyKHBzbs");
        jsobj1.put("url",url2);

        jsobj3.put("value", firstName+"给您发了一条消息");
        jsobj3.put("color", "#173177");
        jsobj2.put("first", jsobj3);

        jsobj4.put("value", "消息提醒");
        jsobj4.put("color", "#173177");
        jsobj2.put("keyword1", jsobj4);

        jsobj5.put("value", message);
        jsobj5.put("color", "#173177");
        jsobj2.put("keyword2", jsobj5);

//        jsobj6.put("value", date);
//        jsobj6.put("color", "#173177");
//        jsobj2.put("keyword3", jsobj6);
//        jsobj7.put("value", position);
//        jsobj7.put("color", "#173177");
//        jsobj2.put("keyword4", jsobj7);

//        jsobj8.put("value", "届时，我们期待您的参加！");
//        jsobj8.put("color", "#173177");
//        jsobj2.put("remark", jsobj8);

        jsobj1.put("data", jsobj2);


        WeChatUtil.PostSendMsg(jsobj1, url);

        return result;
    }





    /**
     * send Message
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> sendMessage(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        GenericValue userLogin = (GenericValue) context.get("userLogin");

        String partyIdFrom = (String) userLogin.get("partyId");
        String partyIdTo = (String) context.get("partyIdTo");
        String message = (String) context.get("message");
        String orderId = (String) context.get("orderId");

        if(partyIdFrom.equals(partyIdTo)){
            //TODO IF EQUALS , PARTY ID TO  = CUSTOMER
            GenericValue orderMap = EntityQuery.use(delegator).from("OrderHeaderItemAndRoles").where("orderId",orderId).queryFirst();
            partyIdTo = (String) orderMap.get("partyId");
        }


        Map<String,Object> createMessageLogMap = new HashMap<String, Object>();

        createMessageLogMap.put("partyIdFrom",partyIdFrom);
        createMessageLogMap.put("messageId", delegator.getNextSeqId("MessageLog"));
        createMessageLogMap.put("partyIdTo",partyIdTo);
        createMessageLogMap.put("message",message);
        createMessageLogMap.put("fromDate",org.apache.ofbiz.base.util.UtilDateTime.nowTimestamp());

        GenericValue msg = delegator.makeValue("MessageLog", createMessageLogMap);
        msg.create();




        // 查询registrationID
        EntityCondition pConditions = EntityCondition.makeCondition("partyId", partyIdTo);
        List<EntityCondition> devTypeExprs = new ArrayList<EntityCondition>();
        devTypeExprs.add(EntityCondition.makeCondition("partyIdentificationTypeId", "JPUSH_ANDROID"));
        devTypeExprs.add(EntityCondition.makeCondition("partyIdentificationTypeId", "JPUSH_IOS"));
        EntityCondition devCondition = EntityCondition.makeCondition(devTypeExprs, EntityOperator.OR);
        pConditions = EntityCondition.makeCondition(pConditions, devCondition);

        List<GenericValue> partyIdentifications =  delegator.findList("PartyIdentification", pConditions, null, UtilMisc.toList("-createdStamp"), null, false);
        GenericValue  partyIdentification = (GenericValue) partyIdentifications.get(0);
        String regId = (String) partyIdentification.getString("idValue");
        String partyIdentificationTypeId = (String) partyIdentification.get("partyIdentificationTypeId");
        Map<String, Object> result = ServiceUtil.returnSuccess();
        result.put("regId", regId);
        GenericValue person = delegator.findOne("Person",UtilMisc.toMap("partyId",partyIdFrom),false);
        result.put("partyIdFrom",person.get("firstName") + ":"+message);
        result.put("partyIdTo",partyIdFrom);
        result.put("deviceType",partyIdentificationTypeId);

        result.put("message","message:" + partyIdTo + ":" + partyIdFrom+":"+orderId+"");

        return result;
    }




}
