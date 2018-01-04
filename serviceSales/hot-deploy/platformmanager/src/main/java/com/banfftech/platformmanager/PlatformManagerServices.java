package main.java.com.banfftech.platformmanager;


import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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

    public static final String resourceUiLabels = "PlatformManagerUiLabels.xml";


    /**
     * createSimpleCarrierShipmentMethod(单独创建系统货运方式)
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> createSimpleCarrierShipmentMethod(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();

        Delegator delegator = dispatcher.getDelegator();

        Locale locale = (Locale) context.get("locale");

        Map<String, Object> result = ServiceUtil.returnSuccess();

        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));


        String carrierCode = (String) context.get("carrierCode");

        String name = (String) context.get("name");

        String code = (String) context.get("code");

        //Create PartyGroup
        dispatcher.runSync("createPartyGroup",UtilMisc.toMap("userLogin",admin,"partyId",carrierCode,"groupName",name ));
        //Create Role
        Map<String, Object> createPartyRoleMap = UtilMisc.toMap("userLogin", admin, "partyId", carrierCode,
                "roleTypeId", "CARRIER");

        dispatcher.runSync("createPartyRole", createPartyRoleMap);

        //Create CarrierShipmentMethod
        dispatcher.runSync("createCarrierShipmentMethod",UtilMisc.toMap("userLogin",admin,"carrierServiceCode",code,"partyId",carrierCode,"roleTypeId","CARRIER","shipmentMethodTypeId","EXPRESS","sequenceNumber",new Long("10")));

        return result;
    }




    /**
     * 订单状态变更推送
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> pushOrderStatusInfo(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> result = ServiceUtil.returnSuccess();


        String openId        = (String) context.get("openId");
        String orderId     = (String) context.get("orderId");
        String date     = (String) context.get("date");

        String payToPartyId  = (String) context.get("payToPartyId");
        String tarjeta       = (String) context.get("tarjeta");
        String messageInfo = (String) context.get("messageInfo");

        String jumpUrl       = (String) context.get("jumpUrl");

        GenericValue orderHeader = delegator.findOne("OrderHeader",UtilMisc.toMap("orderId",orderId),false);




        String orderStatus =  UtilProperties.getMessage(resourceUiLabels,orderHeader.get("statusId")+"", locale);


        // 发送模版消息
        AccessToken accessToken = getAccessToken(PeConstant.WECHAT_GZ_APP_ID, PeConstant.ACCESS_KEY_SECRET);
        String URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
        String url = URL.replace("ACCESS_TOKEN", accessToken.getToken());

        JSONObject jsobj1 = new JSONObject();
        JSONObject jsobj2 = new JSONObject();
        JSONObject jsobj3 = new JSONObject();
        JSONObject jsobj4 = new JSONObject();
        JSONObject jsobj5 = new JSONObject();
        JSONObject jsobj6 = new JSONObject();
        JSONObject jsobj7 = new JSONObject();
        JSONObject jsobj8 = new JSONObject();
        JSONObject jsobjminipro = new JSONObject();
//        jsobjminipro.put("appid","wx119831dae45a3f3f");
//        jsobjminipro.put("pagepath","pages/index/");
//        jsobj1.put("miniprogram",jsobjminipro);
//        String url2 = "http://www.lyndonspace.com:3400/WebManager/control/miniChat?" +
//                "orderId="+orderId+"&payToPartyId=" +
//                ""+payToPartyId+"&tarjeta="+tarjeta+"&payToPartyHead="+personInfoMap.get("headPortrait")+"&payToPartyFirstName="+personInfoMap.get("firstName");
//        System.out.println("*============================================================URL = " + url2);
        jsobj1.put("touser", openId);
        jsobj1.put("template_id","akqWpgJdI14Hm6vaisBd_-UfkzIInu_P-8l4FaNCHkU");
        jsobj1.put("url",jumpUrl);



        jsobj3.put("value", "订单状态更新啦!");
        jsobj3.put("color", "#173177");
        jsobj2.put("first", jsobj3);

        jsobj4.put("value", orderId);
        jsobj4.put("color", "#173177");
        jsobj2.put("keyword1", jsobj4);




        if(orderStatus.toLowerCase().equals("created")){
            orderStatus  = "订单已创建";
           // messageInfo= personInfoMap.get("firstName")+"正在处理您的订单";
        }else{
            orderStatus  = "订单已发货";
           // messageInfo= "物流公司:"+"物流单号:";
        }


        jsobj5.put("value", orderStatus);
        jsobj5.put("color", "#173177");
        jsobj2.put("keyword2", jsobj5);

        jsobj6.put("value", messageInfo);
        jsobj6.put("color", "#173177");
        jsobj2.put("keyword3", jsobj6);

        jsobj7.put("value", date);
        jsobj7.put("color", "#173177");
        jsobj2.put("keyword4", jsobj7);

        jsobj8.put("value", "友评平台竭诚为您服务^_^");
        jsobj8.put("color", "#173177");
        jsobj2.put("remark", jsobj8);

        jsobj1.put("data", jsobj2);





        WeChatUtil.PostSendMsg(jsobj1, url);

        return result;
    }


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
        System.out.println("*pushWeChatMessageInfo============================================================");
        String partyIdFrom   = (String) context.get("partyIdFrom");
        String openId        = (String) context.get("openId");
        String productId     = (String) context.get("productId");
        String date     = (String) context.get("date");
        String message     = (String) context.get("message");
        String firstName     = (String) context.get("firstName");
        String payToPartyId  = (String) context.get("payToPartyId");
        String tarjeta       = (String) context.get("tarjeta");
        String jumpUrl   = (String) context.get("url");




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
        String url2 = "";
        if(jumpUrl!=null && !jumpUrl.trim().equals("")){
            url2 = jumpUrl;
        }else{
             url2 = "http://www.lyndonspace.com:3400/WebManager/control/miniChat?" +
                    "productId="+productId+"&payToPartyId=" +
                    ""+payToPartyId+"&tarjeta="+tarjeta+"&payToPartyHead="+personInfoMap.get("headPortrait")+"&payToPartyFirstName="+personInfoMap.get("firstName");
            System.out.println("*============================================================URL = " + url2);
        }


        jsobj1.put("touser",openId);
        jsobj1.put("template_id","aFCzhfNrWb0GsEr0ZCVuijLPAQ6cPzPedORxyKHBzbs");
        jsobj1.put("url","https://www.yo-pe.com/pejump/"+partyIdFrom+"/"+partyIdFrom+"111"+"/"+payToPartyId+"/"+productId);

        JSONObject jsobjminipro = new JSONObject();
        jsobjminipro.put("appid","wx299644ef4c9afbde");
        jsobjminipro.put("pagepath","pages/chatView/chatView?username="+payToPartyId+"&password="+payToPartyId+"111&payToPartyId="+partyIdFrom+"&productId="+productId);
        jsobj1.put("miniprogram",jsobjminipro);

        System.out.println("pages/chatView/chatView?username="+payToPartyId+"&password="+payToPartyId+"111&payToPartyId="+partyIdFrom+"&productId="+productId);


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
        createMessageLogMap.put("messageLogTypeId", "TEXT");
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











    /**
     * sendAppAnd WeChatMessage
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> sendAppAndWeChatMessage(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> result = ServiceUtil.returnSuccess();

        String partyIdFrom = (String) context.get("partyIdFrom");
        String partyIdTo = (String) context.get("partyIdTo");
        String message = (String) context.get("message");
        String orderId = (String) context.get("orderId");





        if(partyIdFrom.equals(partyIdTo)){
//            //TODO IF EQUALS , PARTY ID TO  = CUSTOMER
//            GenericValue orderMap = EntityQuery.use(delegator).from("OrderHeaderItemAndRoles").where("orderId",orderId).queryFirst();
//            partyIdTo = (String) orderMap.get("partyId");
            return result;
        }


        Map<String,Object> createMessageLogMap = new HashMap<String, Object>();

        createMessageLogMap.put("partyIdFrom",partyIdFrom);
        createMessageLogMap.put("messageId", delegator.getNextSeqId("MessageLog"));
        createMessageLogMap.put("partyIdTo",partyIdTo);
        createMessageLogMap.put("message",message);
        createMessageLogMap.put("fromDate",org.apache.ofbiz.base.util.UtilDateTime.nowTimestamp());

        createMessageLogMap.put("messageLogTypeId", "TEXT");

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

        result.put("regId", regId);
        GenericValue person = delegator.findOne("Person",UtilMisc.toMap("partyId",partyIdFrom),false);
        result.put("partyIdFrom",person.get("firstName") + ":"+message);
        result.put("partyIdTo",partyIdFrom);
        result.put("deviceType",partyIdentificationTypeId);

        result.put("message","message:" + partyIdTo + ":" + partyIdFrom+":"+orderId+"");






        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));



        List<GenericValue> partyIdentificationList = EntityQuery.use(delegator).from("PartyIdentification").where("partyId", partyIdTo, "partyIdentificationTypeId", "WX_GZ_OPEN_ID").queryList();

        if (null != partyIdentificationList && partyIdentificationList.size() > 0) {

            String openId = (String) partyIdentificationList.get(0).get("idValue");

            Map<String, Object> pushWeChatMessageInfoMap = new HashMap<String, Object>();


            pushWeChatMessageInfoMap.put("message", message);

            pushWeChatMessageInfoMap.put("userLogin", admin);

            System.out.println("*PUSH WE CHAT GONG ZHONG PLATFORM !!!!!!!!!!!!!!!!!!!!!!!");

            Date date = new Date();

            SimpleDateFormat formatter;

            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String pushDate = "" + formatter.format(date);

            pushWeChatMessageInfoMap.put("date", pushDate);

            pushWeChatMessageInfoMap.put("openId", openId);

            pushWeChatMessageInfoMap.put("productId", "");

            pushWeChatMessageInfoMap.put("payToPartyId", partyIdTo);

            pushWeChatMessageInfoMap.put("url", "");

            //推微信
            dispatcher.runSync("pushWeChatMessageInfo", pushWeChatMessageInfoMap);

        }



        // 不用Ecs 推送App
        Map<String, Object> pushNotifOrMessageInfoMap = new HashMap<String, Object>();
        pushNotifOrMessageInfoMap.put("message",message);
        pushNotifOrMessageInfoMap.put("productId","10000");
        pushNotifOrMessageInfoMap.put("content","");
        pushNotifOrMessageInfoMap.put("deviceType",partyIdentificationTypeId);
        pushNotifOrMessageInfoMap.put("objectId","10000");
        pushNotifOrMessageInfoMap.put("regId",regId);
         pushNotifOrMessageInfoMap.put("sendType","one");


        GenericValue userLogin =  EntityQuery.use(delegator).from("UserLogin").where("partyId",partyIdFrom,"enabled","Y").queryFirst();

        pushNotifOrMessageInfoMap.put("userLogin",userLogin);

        dispatcher.runSync("pushNotifOrMessage",pushNotifOrMessageInfoMap);


        return result;
    }



}
