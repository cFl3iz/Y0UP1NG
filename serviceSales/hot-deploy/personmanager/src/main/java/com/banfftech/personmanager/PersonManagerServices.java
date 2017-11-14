package main.java.com.banfftech.personmanager;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

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

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;

import main.java.com.banfftech.platformmanager.oss.OSSUnit;

import static main.java.com.banfftech.platformmanager.wechat.WeChatUtil.getAccessToken;

/**
 * Created by S on 2017/9/12.
 */
public class PersonManagerServices {


    public final static String module = PersonManagerServices.class.getName();


    public static final String resourceError = "PlatformManagerErrorUiLabels.xml";



    /**
     * PartyRelationShipENUM
     */
    public enum relationType {

        C2CRSS,CONTACT;

        public static relationType getRelationType(String relationType) {

            return valueOf(relationType.toUpperCase());

        }
    }


    /**
     * markOrOutMarkProduct
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> markOrOutMarkProduct(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();

        Delegator delegator = dispatcher.getDelegator();

        Locale locale = (Locale) context.get("locale");


        GenericValue userLogin = (GenericValue) context.get("userLogin");

        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        Map<String, Object> resultMap = ServiceUtil.returnSuccess();

        String productId = (String) context.get("productId");

        String markIt    = (String) context.get("markIt");

        String partyId = (String) userLogin.get("partyId");

        if(markIt.equals("true")){
            dispatcher.runSync("addPartyToProduct",UtilMisc.toMap("userLogin",admin,"partyId",partyId,"productId",productId,"roleTypeId","PLACING_CUSTOMER"));
        }else{
            GenericValue partyMarkRole = EntityQuery.use(delegator).from("ProductRole").where("partyId", partyId,"productId",productId, "roleTypeId", "PLACING_CUSTOMER").queryFirst();
            dispatcher.runSync("removePartyFromProduct",UtilMisc.toMap("userLogin",admin,"partyId",partyId,"productId",productId,"roleTypeId","PLACING_CUSTOMER","fromDate",partyMarkRole.get("fromDate")));
        }



        return resultMap;
    }






    /**
     * push Message
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> pushMessage(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String,Object> createMessageLogMap = new HashMap<String, Object>();
        Map<String,Object> pushWeChatMessageInfoMap = new HashMap<String, Object>();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        String text = (String) context.get("text");

        String objectId = (String) context.get("objectId");

        String partyIdTo = (String) context.get("partyIdTo");

        String partyIdFrom = (String) context.get("partyIdFrom");

        if(UtilValidate.isEmpty(partyIdFrom)){
            partyIdFrom = (String) userLogin.get("partyId");
        }

        pushWeChatMessageInfoMap.put("userLogin",userLogin);

        pushWeChatMessageInfoMap.put("message",text);

        System.out.println("========================================= partyIdTo = " +partyIdTo);

        // 查询registrationID
        EntityCondition pConditions = EntityCondition.makeCondition("partyId", partyIdTo);
        List<EntityCondition> devTypeExprs = new ArrayList<EntityCondition>();
        devTypeExprs.add(EntityCondition.makeCondition("partyIdentificationTypeId", "JPUSH_ANDROID"));
        devTypeExprs.add(EntityCondition.makeCondition("partyIdentificationTypeId", "JPUSH_IOS"));
        EntityCondition devCondition = EntityCondition.makeCondition(devTypeExprs, EntityOperator.OR);
        pConditions = EntityCondition.makeCondition(pConditions, devCondition);

        List<GenericValue> partyIdentifications =  delegator.findList("PartyIdentification", pConditions, null, UtilMisc.toList("-createdStamp"), null, false);

        GenericValue person = delegator.findOne("Person",UtilMisc.toMap("partyId",partyIdFrom),false);
        createMessageLogMap.put("message",text);
        if(person!=null){
            text = person.get("firstName")+":"+text;
        }

        pushWeChatMessageInfoMap.put("firstName",person.get("firstName"));

        if(null != partyIdentifications && partyIdentifications.size()>0){


            GenericValue  partyIdentification = (GenericValue) partyIdentifications.get(0);
            String jpushId = (String) partyIdentification.getString("idValue");
            String partyIdentificationTypeId = (String) partyIdentification.get("partyIdentificationTypeId");

            //查角标
            EntityCondition findValCondition = EntityCondition.makeCondition(
                    EntityCondition.makeCondition("badge", EntityOperator.EQUALS, "true"),
                    EntityCondition.makeCondition("partyIdTo", EntityOperator.EQUALS, partyIdTo));
            long count = delegator.findCountByCondition("MessageLog", findValCondition, null, null);

            String badege_str = count+"";

            try{
                dispatcher.runSync("pushNotifOrMessage",UtilMisc.toMap("userLogin",admin,"badge",badege_str,"message","message","content",text,"regId",jpushId,"deviceType",partyIdentificationTypeId,"sendType","","objectId",partyIdFrom));
            } catch (GenericServiceException e1) {
                Debug.logError(e1.getMessage(), module);
                return ServiceUtil.returnError(UtilProperties.getMessage(resourceError, "JPushError", locale));
            }


        }




        if(!UtilValidate.isEmpty(partyIdFrom)) {
            createMessageLogMap.put("partyIdFrom", partyIdFrom);
        }

        createMessageLogMap.put("messageId", delegator.getNextSeqId("MessageLog"));

        createMessageLogMap.put("partyIdTo",partyIdTo);

        createMessageLogMap.put("badge","true");

        if(!UtilValidate.isEmpty(objectId)){
            createMessageLogMap.put("objectId",objectId);
        }

        createMessageLogMap.put("fromDate",org.apache.ofbiz.base.util.UtilDateTime.nowTimestamp());

        GenericValue msg = delegator.makeValue("MessageLog", createMessageLogMap);

        msg.create();








        List<GenericValue> partyIdentificationList = EntityQuery.use(delegator).from("PartyIdentification").where("partyId", partyIdTo, "partyIdentificationTypeId", "WX_GZ_OPEN_ID").queryList();


        if (null != partyIdentificationList && partyIdentificationList.size() > 0) {
            System.out.println("*PUSH WE CHAT GONG ZHONG PLATFORM !!!!!!!!!!!!!!!!!!!!!!!");
            Date date = new Date();

            SimpleDateFormat formatter;

            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String pushDate = ""+formatter.format(date);

            pushWeChatMessageInfoMap.put("date",pushDate);
            String openId = (String) partyIdentificationList.get(0).get("idValue");
            pushWeChatMessageInfoMap.put("openId",openId);
            //推微信
            dispatcher.runSync("pushWeChatMessageInfo",pushWeChatMessageInfoMap);
        } else {

        }






        Map<String, Object> resultMap = ServiceUtil.returnSuccess();

        return resultMap;
    }




        /**
         * Affirm Order
         * @param dctx
         * @param context
         * @return
         * @throws GenericEntityException
         * @throws GenericServiceException
         */
    public static Map<String, Object> affirmOrder(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");

        GenericValue userLogin = (GenericValue) context.get("userLogin");
        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        String orderId = (String) context.get("orderId");

        Map<String, Object> updateOrderStatusInMap = new HashMap<String, Object>();
        updateOrderStatusInMap.put("userLogin", admin);
        updateOrderStatusInMap.put("orderId", orderId);
        updateOrderStatusInMap.put("statusId", PeConstant.ORDER_APPROVED_STATUS_ID);

        Map<String, Object> updateOrderStatusOutMap = dispatcher.runSync("changeOrderStatus", updateOrderStatusInMap);

        if (ServiceUtil.isError(updateOrderStatusOutMap)) {
            return updateOrderStatusOutMap;
        }




        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        return resultMap;
    }
















    /**
     * salesDiscontinuation
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> salesDiscontinuation(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");

        GenericValue userLogin = (GenericValue) context.get("userLogin");
        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        String productId = (String) context.get("productId");

        Map<String, Object> updateProductInMap = new HashMap<String, Object>();
        updateProductInMap.put("userLogin", admin);
        updateProductInMap.put("productId", productId);
        updateProductInMap.put("salesDiscontinuationDate", new Timestamp(new Date().getTime()));

        Map<String, Object> updateProductOutMap = dispatcher.runSync("updateProduct", updateProductInMap);

        if (ServiceUtil.isError(updateProductOutMap)) {
            return updateProductOutMap;
        }




        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        return resultMap;
    }










    /**
     * Matching Contact
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> matchingContact(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");

        GenericValue userLogin = (GenericValue)  context.get("userLogin");

        String partyId = (String) userLogin.get("partyId");

        String contacts = (String) context.get("contacts");

        // Split
        String[] contactsArray = contacts.split(",");

        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

       int count = 0;

        if (!UtilValidate.isEmpty(contactsArray)) {
            count = forSearchPeUsers(partyId,delegator,dispatcher,contactsArray,admin);
        }









        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        resultMap.put("count", count + "");
        return resultMap;
    }

    /**
     * Search User Is Exsits ?
     * @param delegator
     * @param dispatcher
     * @param contactsArray
     * @param admin
     * @return
     */
    private static int forSearchPeUsers(String partyIdFrom,Delegator delegator, LocalDispatcher dispatcher, String[] contactsArray, GenericValue admin) throws GenericEntityException, GenericServiceException {

      int count = 0;

        for (int i = 0; i < contactsArray.length; i++) {
            Map<String,Object> rowMap = new HashMap<String, Object>();

            String tel = contactsArray[i].substring(contactsArray[i].indexOf(":") + 1).replace("-","").replace("\"","");
            tel = tel.replaceAll("]", "");

            String nickName = contactsArray[i].substring(1, contactsArray[i].indexOf(":"));
            GenericValue teleContact = EntityQuery.use(delegator).from("TelecomNumberAndPartyView").where("contactNumber", tel).queryFirst();


            if (null != teleContact) {
                String partyId = (String) teleContact.get("partyId");
                Map<String,Object>   addPartyRelationShipInMap = new HashMap<String, Object>();
                addPartyRelationShipInMap.put("userLogin",admin);
                addPartyRelationShipInMap.put("partyIdFrom",partyId);
                addPartyRelationShipInMap.put("partyIdTo",partyIdFrom);
                addPartyRelationShipInMap.put("relationEnum","CONTACT");
                dispatcher.runSync("addPartyRelationShip",addPartyRelationShipInMap);
                count ++;
            }
           // createActivityMembers(userLogin, workEffortId, nickName, tel, dispatcher, delegator, false, null, null, null, null, null);
        }


        return count;
    }


    /**
     * checkSubscribe
     * @param request
     * @param response
     * @return
     * @throws GenericServiceException
     * @throws GenericEntityException
     */
    public static String checkSubscribe(HttpServletRequest request, HttpServletResponse response)
            throws GenericServiceException, GenericEntityException {

        // Servlet Head

        Locale locale = UtilHttp.getLocale(request);

        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");

        Delegator delegator = (Delegator) request.getAttribute("delegator");

        HttpSession session = request.getSession();
        // productId
        String productId = (String) request.getParameter("productId");



        request.setAttribute("subscribe","");
        return "success";
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

        // 不需要 String openid = WeChatUtils.getOpenId("",APP_KEY,ACCESS_KEY_SECRET);
        String openId        = (String) context.get("openId");
//        String workEffortName     = (String) context.get("workEffortName");
//        String workEffortId     = (String) context.get("workEffortId");
//        String position     = (String) context.get("position");
        String date     = (String) context.get("date");
        String message     = (String) context.get("message");
        String firstName     = (String) context.get("firstName");

        firstName = EmojiHandler.decodeJava(firstName);

        // 发送模版消息
        AccessToken accessToken = getAccessToken(PeConstant.WECHAT_GZ_APP_ID,PeConstant.ACCESS_KEY_SECRET);
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

        jsobj1.put("touser",openId);
        jsobj1.put("template_id","aFCzhfNrWb0GsEr0ZCVuijLPAQ6cPzPedORxyKHBzbs");
        jsobj1.put("url","www.yo-pe.com:3400/WebManager/control/shareProduct?productId="+"");

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
     * Query Message
     * @param request
     * @param response
     * @return
     * @throws GenericServiceException
     * @throws GenericEntityException
     */
    public static String queryMessage(HttpServletRequest request, HttpServletResponse response)
            throws GenericServiceException, GenericEntityException {

        // Servlet Head

        Locale locale = UtilHttp.getLocale(request);

        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");

        Delegator delegator = (Delegator) request.getAttribute("delegator");

        HttpSession session = request.getSession();

        GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");

        GenericValue admin = delegator.findOne("UserLogin", UtilMisc.toMap("userLoginId", "admin"), false);

        // productId
        String productId = (String) request.getParameter("productId");




        return "success";
    }



    /**
     * add Product Content
     * @param request
     * @param response
     * @return
     * @throws GenericServiceException
     * @throws GenericEntityException
     */
    public static String addProductContent(HttpServletRequest request, HttpServletResponse response)
            throws GenericServiceException, GenericEntityException {

        // Servlet Head

        Locale locale = UtilHttp.getLocale(request);

        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");

        Delegator delegator = (Delegator) request.getAttribute("delegator");

        HttpSession session = request.getSession();

        GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");





        GenericValue admin = delegator.findOne("UserLogin", UtilMisc.toMap("userLoginId", "admin"), false);

        // productId
        String productId = (String) request.getParameter("productId");

        String descriptions = (String) request.getParameter("descriptions");




            try {

                ServletFileUpload dfu = new ServletFileUpload(new DiskFileItemFactory(10240, null));

                List<FileItem> items = dfu.parseRequest(request);


                int itemSize = 0;


                // if(null!=items){
                if (null != items) {
                    itemSize = items.size();

                    int count = 1;

                    for (FileItem item : items) {


                        InputStream in = item.getInputStream();

                        String fileName = item.getName();


                        if (!UtilValidate.isEmpty(fileName)) {


                            long tm = System.currentTimeMillis();
                            String pictureKey = OSSUnit.uploadObject2OSS(in, item.getName(), OSSUnit.getOSSClient(), null,
                                    "personerp", PeConstant.PRODUCT_OSS_PATH, tm);

                            if (pictureKey != null && !pictureKey.equals("") && count <=4) {
                                //创建产品内容和数据资源附图
                                createProductContentAndDataResource(delegator,dispatcher, admin, productId, "", "https://personerp.oss-cn-hangzhou.aliyuncs.com/" + PeConstant.DEFAULT_HEAD_DISK + tm + fileName.substring(fileName.indexOf(".")),count);
                                count++;
                            }
                        }

                    }
                }


            } catch (Exception e) {

                e.printStackTrace();

            }

        return "success";

    }

    /**
     * 创建产品内容及数据资源
     * @param dispatcher
     * @param admin
     * @param productId
     * @param discription
     * @param dataInfo
     */
    private static void createProductContentAndDataResource(Delegator delegator,LocalDispatcher dispatcher, GenericValue admin, String productId, String description, String dataInfo,int count)throws GenericServiceException {

        // Create Content
        String contentTypeId =  "ADDITIONAL_IMAGE_"+count;
        String contentId     = delegator.getNextSeqId("Content");
        dispatcher.runSync("createProductContent",UtilMisc.toMap("userLogin",admin,"productContentTypeId",contentTypeId,"productId",productId,"contentId",contentId));

        // Create DataResource
        Map<String, Object> dataResourceCtx = new HashMap<String, Object>();
        dataResourceCtx.put("objectInfo", dataInfo);
        dataResourceCtx.put("dataResourceName",  description);
        dataResourceCtx.put("userLogin", admin);
        dataResourceCtx.put("dataResourceTypeId", "SHORT_TEXT");
        dataResourceCtx.put("mimeTypeId", "text/html");
        Map<String, Object> dataResourceResult = new HashMap<String, Object>();
        try {
            dataResourceResult = dispatcher.runSync("createDataResource", dataResourceCtx);
        } catch (GenericServiceException e) {
            Debug.logError(e, module);

        }

        // Update Content
        Map<String, Object> contentCtx = new HashMap<String, Object>();
        contentCtx.put("contentId", contentId);
        contentCtx.put("dataResourceId", dataResourceResult.get("dataResourceId"));
        contentCtx.put("userLogin", admin);
        try {
            dispatcher.runSync("updateContent", contentCtx);
        } catch (GenericServiceException e) {
            Debug.logError(e, module);

        }

    }


    /**
     * release Resource
     *
     * @param request
     * @param response
     * @return
     * @throws GenericServiceException
     */
    public static String releaseResource(HttpServletRequest request, HttpServletResponse response)
            throws GenericServiceException, GenericEntityException {

        // Servlet Head

        Locale locale = UtilHttp.getLocale(request);

        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");

        Delegator delegator = (Delegator) request.getAttribute("delegator");

        HttpSession session = request.getSession();

        GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");

        String partyId = (String) userLogin.get("partyId");



        GenericValue admin = delegator.findOne("UserLogin", UtilMisc.toMap("userLoginId", "admin"), false);

        // 分类Id
        String productCategoryId = (String) request.getParameter("productCategoryId");

        // 价格
        String productPrice = (String) request.getParameter("productPrice");

        String productName = (String) request.getParameter("productName");

        String defaultImageUrl = (String) request.getParameter("defaultImageUrl");

        // Default Price
        BigDecimal price = BigDecimal.ZERO;

        if (!UtilValidate.isEmpty(productPrice)) {
            price = new BigDecimal(productPrice);
        }

        // 不分梨用户
        if (null == productCategoryId) {
            productCategoryId = createPersonStoreAndCatalogAndCategory(admin, delegator, dispatcher, partyId);

        }


        //创建产品Map
        Map<String, Object> createProductInMap = new HashMap<String, Object>();
        createProductInMap.put("userLogin", admin);
        long ctm = System.currentTimeMillis();
        createProductInMap.put("internalName", partyId + "_" + ctm);
        createProductInMap.put("productName", productName);
        createProductInMap.put("productTypeId", PeConstant.PRODUCT_TYPE_ID);


        if (!UtilValidate.isEmpty(defaultImageUrl)) {
            createProductInMap.put("smallImageUrl", defaultImageUrl);
            createProductInMap.put("detailImageUrl", defaultImageUrl);
        } else {

            try {

                ServletFileUpload dfu = new ServletFileUpload(new DiskFileItemFactory(10240, null));

                List<FileItem> items = dfu.parseRequest(request);


                int itemSize = 0;


                // if(null!=items){
                if (null != items) {
                    itemSize = items.size();


                    for (FileItem item : items) {


                        InputStream in = item.getInputStream();

                        String fileName = item.getName();


                        if (!UtilValidate.isEmpty(fileName)) {


                            long tm = System.currentTimeMillis();
                            String pictureKey = OSSUnit.uploadObject2OSS(in, item.getName(), OSSUnit.getOSSClient(), null,
                                    "personerp", PeConstant.PRODUCT_OSS_PATH, tm);

                            if (pictureKey != null && !pictureKey.equals("")) {


                                createProductInMap.put("smallImageUrl", PeConstant.OSS_PATH + PeConstant.PRODUCT_OSS_PATH + tm + fileName.substring(fileName.indexOf(".")) + "?x-oss-process=image/resize,m_pad,h_50,w_50");
                                createProductInMap.put("detailImageUrl", PeConstant.OSS_PATH + PeConstant.PRODUCT_OSS_PATH + tm + fileName.substring(fileName.indexOf(".")));
                            }
                        }

                    }
                }


            } catch (Exception e) {

                e.printStackTrace();

            }


        }


        // Create Product
        Map<String, Object> createProductOutMap = dispatcher.runSync("createProduct", createProductInMap);
        String productId = (String) createProductOutMap.get("productId");


        // Create Product Price
        Map<String, Object> createProductPriceInMap = new HashMap<String, Object>();
        createProductPriceInMap.put("userLogin", admin);
        createProductPriceInMap.put("productId", productId);
        createProductPriceInMap.put("currencyUomId", PeConstant.DEFAULT_CURRENCY_UOM_ID);
        createProductPriceInMap.put("price", price);
        createProductPriceInMap.put("productPricePurposeId", PeConstant.PRODUCT_PRICE_DEFAULT_PURPOSE);
        createProductPriceInMap.put("productPriceTypeId", PeConstant.PRODUCT_PRICE_DEFAULT_TYPE_ID);
        createProductPriceInMap.put("productStoreGroupId", PeConstant.NA);
        dispatcher.runSync("createProductPrice", createProductPriceInMap);


        // Add Product To Category 产品 关联 分类
        Map<String, Object> addProductToCategoryInMap = new HashMap<String, Object>();
        addProductToCategoryInMap.put("userLogin", admin);
        addProductToCategoryInMap.put("productId", productId);
        addProductToCategoryInMap.put("productCategoryId", productCategoryId);
        dispatcher.runSync("addProductToCategory", addProductToCategoryInMap);

        request.setAttribute("productId",productId);

        return "success";

    }


    /**
     * Add Party RelationShip
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> addPartyRelationShip(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        String partyIdFrom = (String) context.get("partyIdFrom");
        String partyIdTo = (String) context.get("partyIdTo");
        String relationEnum = (String) context.get("relationEnum");

        Map<String, Object> serviceResultMap = null;
        switch (relationType.getRelationType(relationEnum)) {
            case C2CRSS:
                serviceResultMap = createRelationC2CRSS(delegator, dispatcher, admin, partyIdFrom, partyIdTo);
                if (ServiceUtil.isError(serviceResultMap)) {
                    return serviceResultMap;
                }
            case CONTACT:
                 serviceResultMap = createRelationCONTACT(delegator, dispatcher, admin, partyIdFrom, partyIdTo);
                if (ServiceUtil.isError(serviceResultMap)) {
                    return serviceResultMap;
                }
        }


        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        return resultMap;
    }

    /**
     * 建立C2C资源服务双方关系
     *
     * @param delegator
     * @param dispatcher
     * @param admin
     * @param partyIdFrom
     * @param partyIdTo
     */
    private static Map<String, Object> createRelationC2CRSS(Delegator delegator, LocalDispatcher dispatcher, GenericValue admin, String partyIdTo, String partyIdFrom) throws GenericServiceException {

        String partyRelationshipTypeId = "";
        // Create Supplier Relation
        partyRelationshipTypeId = PeConstant.SUPPLIER;
        String roleTypeIdFrom = "BILL_TO_CUSTOMER";
        String roleTypeIdTo = "SHIP_FROM_VENDOR";
        Map<String, Object> createPartyRelationshipInMap = new HashMap<String, Object>();
        createPartyRelationshipInMap.put("roleTypeIdFrom", roleTypeIdFrom);
        createPartyRelationshipInMap.put("roleTypeIdTo", roleTypeIdTo);
        createPartyRelationshipInMap.put("userLogin", admin);
        createPartyRelationshipInMap.put("partyIdFrom", partyIdTo);
        createPartyRelationshipInMap.put("partyIdTo", partyIdFrom);
        createPartyRelationshipInMap.put("partyRelationshipTypeId", partyRelationshipTypeId);
        Map<String, Object> createPartyRelationshipOutMap = dispatcher.runSync("createPartyRelationship", createPartyRelationshipInMap);

        if (ServiceUtil.isError(createPartyRelationshipOutMap)) {
            return createPartyRelationshipOutMap;
        }

        partyRelationshipTypeId = PeConstant.CUSTOMER;
        createPartyRelationshipInMap = new HashMap<String, Object>();

        createPartyRelationshipInMap.put("roleTypeIdFrom", roleTypeIdTo);
        createPartyRelationshipInMap.put("roleTypeIdTo", roleTypeIdFrom);

        createPartyRelationshipInMap.put("userLogin", admin);
        createPartyRelationshipInMap.put("partyIdFrom", partyIdFrom);
        createPartyRelationshipInMap.put("partyIdTo", partyIdTo);
        createPartyRelationshipInMap.put("partyRelationshipTypeId", partyRelationshipTypeId);
        createPartyRelationshipOutMap = dispatcher.runSync("createPartyRelationship", createPartyRelationshipInMap);

        if (ServiceUtil.isError(createPartyRelationshipOutMap)) {
            return createPartyRelationshipOutMap;
        }


        return ServiceUtil.returnSuccess();
    }


    /**
     * createRelationCONTACT
     * @param delegator
     * @param dispatcher
     * @param admin
     * @param partyIdTo
     * @param partyIdFrom
     * @return
     * @throws GenericServiceException
     */
    private static Map<String, Object> createRelationCONTACT(Delegator delegator, LocalDispatcher dispatcher, GenericValue admin, String partyIdTo, String partyIdFrom) throws GenericServiceException {

        String partyRelationshipTypeId = "";
        // Create Supplier Relation
        partyRelationshipTypeId = PeConstant.CONTACT;

        Map<String, Object> createPartyRelationshipInMap = new HashMap<String, Object>();

        createPartyRelationshipInMap.put("userLogin", admin);
        createPartyRelationshipInMap.put("partyIdFrom", partyIdTo);
        createPartyRelationshipInMap.put("partyIdTo", partyIdFrom);
        createPartyRelationshipInMap.put("partyRelationshipTypeId", partyRelationshipTypeId);
        Map<String, Object> createPartyRelationshipOutMap = dispatcher.runSync("createPartyRelationship", createPartyRelationshipInMap);

        if (ServiceUtil.isError(createPartyRelationshipOutMap)) {
            return createPartyRelationshipOutMap;
        }

        partyRelationshipTypeId = PeConstant.CONTACT;
        createPartyRelationshipInMap = new HashMap<String, Object>();

        createPartyRelationshipInMap.put("userLogin", admin);
        createPartyRelationshipInMap.put("partyIdFrom", partyIdFrom);
        createPartyRelationshipInMap.put("partyIdTo", partyIdTo);
        createPartyRelationshipInMap.put("partyRelationshipTypeId", partyRelationshipTypeId);
        createPartyRelationshipOutMap = dispatcher.runSync("createPartyRelationship", createPartyRelationshipInMap);

        if (ServiceUtil.isError(createPartyRelationshipOutMap)) {
            return createPartyRelationshipOutMap;
        }


        return ServiceUtil.returnSuccess();
    }

    /**
     * Update PersonInfo
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
//    public static Map<String, Object> updatePersonInfo(DispatchContext dctx, Map<String, Object> context)
//            throws GenericEntityException, GenericServiceException {
//
//        // Service Head
//        LocalDispatcher dispatcher = dctx.getDispatcher();
//        Delegator delegator = dispatcher.getDelegator();
//        Locale locale = (Locale) context.get("locale");
//        GenericValue userLogin = (GenericValue) context.get("userLogin");
//
//
//
//        // Scope Param
//        String partyId = (String) userLogin.get("partyId");
//        String firstName = (String) context.get("firstName");
////        if(EmojiFilter.containsEmoji(firstName)){
////            //包含emoji表情
////            firstName = EmojiHandler.encodeJava(firstName);
////        }
//        String gender = (String) context.get("gender");
//
//        dispatcher.runSync("updatePerson",UtilMisc.toMap("userLogin",userLogin,
//                "partyId",partyId,"firstName",firstName,"lastName","NA","gender",gender));
//
//
//
//        // Service Foot
//        Map<String, Object> result = ServiceUtil.returnSuccess();
//
//        return result;
//    }


    /**
     * upload Head Portrait
     *
     * @param request
     * @param response
     * @return
     * @throws GenericServiceException
     */
    public static String updatePersonInfo(HttpServletRequest request, HttpServletResponse response)
            throws GenericServiceException, GenericEntityException {

        // Servlet Head

        Locale locale = UtilHttp.getLocale(request);

        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");

        Delegator delegator = (Delegator) request.getAttribute("delegator");

        HttpSession session = request.getSession();

        GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");

        String partyId = (String) userLogin.get("partyId");

        String firstName = (String) request.getParameter("firstName");

        String noteInfo  = (String) request.getParameter("noteInfo");

        String gender = (String) request.getParameter("gender");

        if (!UtilValidate.isEmpty(gender)) {
            gender = "M";
        }



        GenericValue admin = delegator.findOne("UserLogin", UtilMisc.toMap("userLoginId", "admin"), false);


        if(!UtilValidate.isEmpty(noteInfo)){
            dispatcher.runSync("createPartyNote", UtilMisc.toMap("userLogin", admin,
                    "partyId", partyId,"note",noteInfo,"noteName","个人说明"));
        }

        dispatcher.runSync("updatePerson", UtilMisc.toMap("userLogin", admin,
                "partyId", partyId, "firstName", firstName, "lastName", "NA", "gender", gender));


        try {

            ServletFileUpload dfu = new ServletFileUpload(new DiskFileItemFactory(10240, null));

            List<FileItem> items = dfu.parseRequest(request);


            int itemSize = 0;

            if (null != items) {
                itemSize = items.size();
            }
            if (null != dfu && null != items) {


                for (FileItem item : items) {


                    InputStream in = item.getInputStream();

                    String fileName = item.getName();


                    if (!UtilValidate.isEmpty(fileName)) {


                        long tm = System.currentTimeMillis();
                        String pictureKey = OSSUnit.uploadObject2OSS(in, item.getName(), OSSUnit.getOSSClient(), null,
                                "personerp", PeConstant.DEFAULT_HEAD_DISK, tm);

                        if (pictureKey != null && !pictureKey.equals("")) {

                            createContentAndDataResource(partyId, delegator, admin, dispatcher, pictureKey, "https://personerp.oss-cn-hangzhou.aliyuncs.com/" + PeConstant.DEFAULT_HEAD_DISK + tm + fileName.substring(fileName.indexOf(".")));

                        }
                    }

                }
            }
        } catch (Exception e) {

            e.printStackTrace();

        }

        return "success";

    }


    /**
     * Create Content & DataResource &&  ASSOC Party
     *
     * @param delegator
     * @param userLogin
     * @param dispatcher
     * @param pictureKey
     * @return
     * @throws GenericServiceException
     * @throws GenericEntityException
     * @author S.Y.L
     */
    public static String createContentAndDataResource(String partyId, Delegator delegator, GenericValue userLogin, LocalDispatcher dispatcher, String pictureKey, String path)
            throws GenericServiceException, GenericEntityException {

        // 1.CREATE DATA RESOURCE
        Map<String, Object> createDataResourceMap = UtilMisc.toMap("userLogin", userLogin, "partyId", "admin",
                "dataResourceTypeId", "LOCAL_FILE", "dataCategoryId", "PERSONAL", "dataResourceName", pictureKey,
                "mimeTypeId", "image/jpeg", "isPublic", "Y", "dataTemplateTypeId", "NONE", "statusId", "CTNT_PUBLISHED",
                "objectInfo", path);


        Map<String, Object> serviceResultByDataResource = dispatcher.runSync("createDataResource",
                createDataResourceMap);
        String dataResourceId = (String) serviceResultByDataResource.get("dataResourceId");


        Map<String, Object> createContentMap = UtilMisc.toMap("userLogin", userLogin, "createdByUserLogin", userLogin.get("userLoginId"), "contentTypeId",
                "DOCUMENT", "statusId", "CTNT_PUBLISHED", "mimeTypeId", "image/png", "dataResourceId", dataResourceId);


        Map<String, Object> serviceResultByCreateContentMap = dispatcher.runSync("createContent", createContentMap);
        String contentId = (String) serviceResultByCreateContentMap.get("contentId");


        Map<String, Object> assocContentPartyMap = UtilMisc.toMap("userLogin", userLogin, "contentId", contentId, "partyId", partyId, "partyContentTypeId", "LGOIMGURL");
        dispatcher.runSync("createPartyContent", assocContentPartyMap);

        return null;
    }


    /**
     * Place Resource Order
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> placeResourceOrder(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = (String) userLogin.get("partyId");


        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));
        String productStoreId = (String) context.get("productStoreId");
        //   String number  = (String)  context.get("number");
        String payToPartyId = (String) context.get("payToPartyId");
        String productId = (String) context.get("productId");
        String price = (String) context.get("price");
        String prodCatalogId = (String) context.get("prodCatalogId");

        BigDecimal subTotal = BigDecimal.ZERO;
        BigDecimal grandTotal = BigDecimal.ZERO;


        if (!UtilValidate.isEmpty(price)) {
            grandTotal = subTotal = new BigDecimal(price);
        }


        // Do Create OrderHeader
        Map<String, Object> createOrderHeaderInMap = new HashMap<String, Object>();
        createOrderHeaderInMap.put("userLogin", userLogin);
        createOrderHeaderInMap.put("productStoreId", productStoreId);
        createOrderHeaderInMap.put("salesChannelEnumId", "UNKNWN_SALES_CHANNEL");
        createOrderHeaderInMap.put("currencyUom", PeConstant.DEFAULT_CURRENCY_UOM_ID);
        createOrderHeaderInMap.put("orderTypeId", PeConstant.SALES_ORDER);
        createOrderHeaderInMap.put("statusId", PeConstant.ORDER_CREATED_STATUS_ID);
        createOrderHeaderInMap.put("remainingSubTotal", subTotal);
        createOrderHeaderInMap.put("grandTotal", grandTotal);

        Map<String, Object> createOrderHeaderOutMap = dispatcher.runSync("createOrderHeader", createOrderHeaderInMap);

        if (ServiceUtil.isError(createOrderHeaderOutMap)) {
            return createOrderHeaderOutMap;
        }

        String orderId = (String) createOrderHeaderOutMap.get("orderId");


        // Add Product To Order Item

        Map<String, Object> appendOrderItemInMap = new HashMap<String, Object>();

        appendOrderItemInMap.put("userLogin", userLogin);
        appendOrderItemInMap.put("orderId", orderId);
        appendOrderItemInMap.put("productId", productId);
        appendOrderItemInMap.put("quantity", BigDecimal.ONE);
        appendOrderItemInMap.put("amount", grandTotal);
        appendOrderItemInMap.put("shipGroupSeqId", "00001");
        appendOrderItemInMap.put("prodCatalogId", prodCatalogId);
        appendOrderItemInMap.put("basePrice", grandTotal);
        appendOrderItemInMap.put("overridePrice", price);

        //appendOrderItem
        Map<String, Object> appendOrderItemOutMap = null;
        try {
         appendOrderItemOutMap = dispatcher.runSync("appendOrderItem", appendOrderItemInMap);
        }catch (GenericServiceException e1) {
        Debug.logError(e1.getMessage(), module);
        return ServiceUtil.returnError(UtilProperties.getMessage(resourceError, "ProductNoLongerForSale", locale));
        }



        // ADD ORDER ROLE
        Map<String, Object> addOrderRoleInMap = new HashMap<String, Object>();
        addOrderRoleInMap.put("userLogin", userLogin);
        addOrderRoleInMap.put("orderId", orderId);
        addOrderRoleInMap.put("partyId", partyId);
        addOrderRoleInMap.put("roleTypeId", "BILL_TO_CUSTOMER");
        Map<String, Object> addOrderRoleOutMap = dispatcher.runSync("addOrderRole", addOrderRoleInMap);

        if (ServiceUtil.isError(addOrderRoleOutMap)) {
            return addOrderRoleOutMap;
        }





        GenericValue teleContact = EntityQuery.use(delegator).from("TelecomNumberAndPartyView").where("partyId", payToPartyId).queryFirst();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        if(null!=teleContact) {
            String contactNumber = (String) teleContact.get("contactNumber");
            resultMap.put("contactTel", contactNumber);
        }

        resultMap.put("partyIdFrom", partyId);
        resultMap.put("partyIdTo", payToPartyId);
        resultMap.put("relationEnum", "C2CRSS");
        resultMap.put("orderId", orderId);


        //推送先不用ECA
        // 查询registrationID
        EntityCondition pConditions = EntityCondition.makeCondition("partyId", payToPartyId);
        List<EntityCondition> devTypeExprs = new ArrayList<EntityCondition>();
        devTypeExprs.add(EntityCondition.makeCondition("partyIdentificationTypeId", "JPUSH_ANDROID"));
        devTypeExprs.add(EntityCondition.makeCondition("partyIdentificationTypeId", "JPUSH_IOS"));
        EntityCondition devCondition = EntityCondition.makeCondition(devTypeExprs, EntityOperator.OR);
        pConditions = EntityCondition.makeCondition(pConditions, devCondition);

        List<GenericValue> partyIdentifications =  delegator.findList("PartyIdentification", pConditions, null, UtilMisc.toList("-createdStamp"), null, false);

        GenericValue person = delegator.findOne("Person",UtilMisc.toMap("partyId",partyId),false);
        String maiJiaName = (String) person.get("firstName");


        if(null != partyIdentifications && partyIdentifications.size()>0){


            GenericValue  partyIdentification = (GenericValue) partyIdentifications.get(0);
            String jpushId = (String) partyIdentification.getString("idValue");
            String partyIdentificationTypeId = (String) partyIdentification.get("partyIdentificationTypeId");


            try{
                dispatcher.runSync("pushNotifOrMessage",UtilMisc.toMap("userLogin",admin,"message","order","content",maiJiaName+"购买了您的产品!点我查看!","regId",jpushId,"deviceType",partyIdentificationTypeId,"sendType","","objectId",orderId));
            } catch (GenericServiceException e1) {
                Debug.logError(e1.getMessage(), module);
                return ServiceUtil.returnError(UtilProperties.getMessage(resourceError, "JPushError", locale));
            }


        }



        return resultMap;
    }























    /**
     * Create User
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> createPeUser(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");

        String partyId, userLoginId = "";
        String uuid = (String) context.get("uuid");
        String tel = (String) context.get("tel");
        String openId = (String) context.get("openId");

        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));


        // Create Party Block
        int random = (int) (Math.random() * 1000000 + 1);
        Map<String, Object> createPartyInMap = UtilMisc.toMap("userLogin", admin, "nickname", "#" + random,
                "firstName", "#" + random, "lastName", " ", "gender", "M");
        Map<String, Object> createPerson = dispatcher.runSync("createUpdatePerson", createPartyInMap);
        partyId = (String) createPerson.get("partyId");


        // Create PartyIdentification Block
        if(!UtilValidate.isEmpty(uuid)){
            Map<String, Object> createPartyIdentificationInMap = UtilMisc.toMap("userLogin", admin, "partyId",
                    partyId, "idValue", uuid, "partyIdentificationTypeId", "CARD_ID");
            dispatcher.runSync("createPartyIdentification", createPartyIdentificationInMap);
        }


        if(!UtilValidate.isEmpty(openId)){
            Map<String, Object> createPartyIdentificationWxInMap = UtilMisc.toMap("userLogin", admin, "partyId",
                    partyId, "idValue", openId, "partyIdentificationTypeId", "WX_UNIO_ID");
            dispatcher.runSync("createPartyIdentification", createPartyIdentificationWxInMap);
        }


        // Create UserLogin Block
        Map<String, Object> createUserLoginInMap = UtilMisc.toMap("userLogin", admin, "userLoginId",
                tel, "partyId", partyId, "currentPassword", "ofbiz",
                "currentPasswordVerify", "ofbiz", "enabled", "Y");
        Map<String, Object> createUserLogin = dispatcher.runSync("createUserLogin", createUserLoginInMap);


        // Search New UserLogin
        userLoginId = (String) EntityQuery.use(delegator).from("UserLogin").where("partyId", partyId).queryFirst()
                .get("userLoginId");

        // Grant Permission Block
        Map<String, Object> userLoginSecurityGroupInMap = UtilMisc.toMap("userLogin", admin, "userLoginId",
                userLoginId, "groupId", "FULLADMIN");
        dispatcher.runSync("addUserLoginToSecurityGroup", userLoginSecurityGroupInMap);


        if (!UtilValidate.isEmpty(tel)) {
            // Create Party Telecom Number
            Map<String, Object> inputTelecom = UtilMisc.toMap();
            inputTelecom.put("partyId", partyId);
            inputTelecom.put("contactNumber", tel);
            inputTelecom.put("contactMechTypeId", "TELECOM_NUMBER");
            inputTelecom.put("contactMechPurposeTypeId", "PHONE_MOBILE");
            inputTelecom.put("userLogin", admin);
            Map<String, Object> createTelecom = null;
            createTelecom = dispatcher.runSync("createPartyTelecomNumber", inputTelecom);

        }

        createPersonStoreAndCatalogAndCategory(admin, delegator, dispatcher, partyId);



        // Create Party Role 授予当事人 意向客户 角色 用于mark product
        GenericValue partyMarkRole = EntityQuery.use(delegator).from("PartyRole").where("partyId", partyId, "roleTypeId", "PLACING_CUSTOMER").queryFirst();
        if (null == partyMarkRole) {
            Map<String, Object> createPartyMarkRoleMap = UtilMisc.toMap("userLogin", admin, "partyId", partyId,
                    "roleTypeId", "PLACING_CUSTOMER");
            dispatcher.runSync("createPartyRole", createPartyMarkRoleMap);
        }



        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        resultMap.put("userLoginId", userLoginId);
        return resultMap;
    }


    /**
     * 创建分类创建店铺创建目录并且关联当事人
     *
     * @param admin
     * @param delegator
     * @param dispatcher
     * @param partyId
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static String createPersonStoreAndCatalogAndCategory(GenericValue admin, Delegator delegator, LocalDispatcher dispatcher, String partyId) throws GenericEntityException, GenericServiceException {

        // Create Party Role 授予当事人角色,如果没有
        GenericValue partyRole = EntityQuery.use(delegator).from("PartyRole").where("partyId", partyId, "roleTypeId", "ADMIN").queryFirst();
        if (null == partyRole) {
            Map<String, Object> createPartyRoleMap = UtilMisc.toMap("userLogin", admin, "partyId", partyId,
                    "roleTypeId", "ADMIN");
            dispatcher.runSync("createPartyRole", createPartyRoleMap);
        }


        // 客户角色是否拥有
        GenericValue partyCustRole = EntityQuery.use(delegator).from("PartyRole").where("partyId", partyId, "roleTypeId", "BILL_TO_CUSTOMER").queryFirst();
        if (null == partyCustRole) {
            Map<String, Object> createCustPartyRoleMap = UtilMisc.toMap("userLogin", admin, "partyId", partyId,
                    "roleTypeId", "BILL_TO_CUSTOMER");
            dispatcher.runSync("createPartyRole", createCustPartyRoleMap);
        }

        // 厂家角色
        GenericValue partyVendorRole = EntityQuery.use(delegator).from("PartyRole").where("partyId", partyId, "roleTypeId", "SHIP_FROM_VENDOR").queryFirst();
        if (null == partyVendorRole) {
            Map<String, Object> createVendorPartyRoleMap = UtilMisc.toMap("userLogin", admin, "partyId", partyId,
                    "roleTypeId", "SHIP_FROM_VENDOR");
            dispatcher.runSync("createPartyRole", createVendorPartyRoleMap);
        }


        // 创建店铺
        Map<String, Object> createPersonStoreOutMap = dispatcher.runSync("createPersonStore", UtilMisc.toMap("userLogin", admin,
                "partyId", partyId));

        String productStoreId = (String) createPersonStoreOutMap.get("storeId");


        // 关联店铺角色
        Map<String, Object> createProductStoreRoleOutMap = dispatcher.runSync("createProductStoreRole", UtilMisc.toMap("userLogin", admin,
                "partyId", partyId,"productStoreId",productStoreId,"roleTypeId","ADMIN"));




        // CreateProd Catalog 创建目录
        Map<String, Object> createProdCatalogInMap = new HashMap<String, Object>();
        createProdCatalogInMap.put("userLogin", admin);
        createProdCatalogInMap.put("catalogName", partyId);
        Map<String, Object> createProdCatalogOutMap = dispatcher.runSync("createProdCatalog", createProdCatalogInMap);
        String prodCatalogId = (String) createProdCatalogOutMap.get("prodCatalogId");

        // createProductCategory 创建我的分类
        Map<String, Object> createProductCategoryInMap = new HashMap<String, Object>();
        createProductCategoryInMap.put("userLogin", admin);
        createProductCategoryInMap.put("productCategoryTypeId", "CATALOG_CATEGORY");
        createProductCategoryInMap.put("categoryName", partyId);
        Map<String, Object> createProductCategoryOutMap = dispatcher.runSync("createProductCategory", createProductCategoryInMap);
        String productCategoryId = (String) createProductCategoryOutMap.get("productCategoryId");


        // Add ProdCatalog To Party 目录关联当事人
        Map<String, Object> addProdCatalogToPartyInMap = new HashMap<String, Object>();
        addProdCatalogToPartyInMap.put("userLogin", admin);
        addProdCatalogToPartyInMap.put("prodCatalogId", prodCatalogId);
        addProdCatalogToPartyInMap.put("partyId", partyId);
        addProdCatalogToPartyInMap.put("roleTypeId", "ADMIN");
        Map<String, Object> addProdCatalogToPartyOutMap = dispatcher.runSync("addProdCatalogToParty", addProdCatalogToPartyInMap);

        // Create Product Store Catalog 目录关联店铺
        Map<String, Object> createProductStoreCatalogInMap = new HashMap<String, Object>();
        createProductStoreCatalogInMap.put("userLogin", admin);
        createProductStoreCatalogInMap.put("prodCatalogId", prodCatalogId);
        createProductStoreCatalogInMap.put("productStoreId", productStoreId);
        Map<String, Object> createProductStoreCatalogOutMap = dispatcher.runSync("createProductStoreCatalog", createProductStoreCatalogInMap);

        // Add Product Category To ProdCatalog 目录关联分类
        Map<String, Object> addProductCategoryToProdCatalogInMap = new HashMap<String, Object>();
        addProductCategoryToProdCatalogInMap.put("userLogin", admin);
        addProductCategoryToProdCatalogInMap.put("prodCatalogCategoryTypeId", "PCCT_PURCH_ALLW");
        addProductCategoryToProdCatalogInMap.put("prodCatalogId", prodCatalogId);
        addProductCategoryToProdCatalogInMap.put("productCategoryId", productCategoryId);

        Map<String, Object> addProductCategoryToProdCatalogOutMap = dispatcher.runSync("addProductCategoryToProdCatalog", addProductCategoryToProdCatalogInMap);

        return productCategoryId;
    }


    /**
     * Create PE Person Store
     *
     * @param dctx
     * @param context
     * @return
     */
    public static Map<String, Object> createPersonStore(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));
        // 当事人
        String partyId = (String) context.get("partyId");

        // 商家名
        String storeName = partyId;

        // 返回的店铺Id
        String personStoreId = null;

        try {
            // Create Product Store
            Map<String, Object> createProductStoreOutMap = dispatcher.runSync("createProductStore", UtilMisc.toMap("userLogin", admin,
                    "defaultCurrencyUomId", PeConstant.DEFAULT_CURRENCY_UOM_ID, "storeName", storeName, "payToPartyId", partyId));
            if (!ServiceUtil.isSuccess(createProductStoreOutMap)) {
                return createProductStoreOutMap;
            }
            personStoreId = (String) createProductStoreOutMap.get("productStoreId");


        } catch (GenericServiceException e1) {
            Debug.logError(e1.getMessage(), module);
            return ServiceUtil.returnError(UtilProperties.getMessage(resourceError, "InternalServiceError", locale));
        }


        // 信用评分
//            Map<String, Object> createPartyClassificationOutMap = dispatcher.runSync("createPartyClassification",
//                    UtilMisc.toMap("userLogin", userLogin, "partyId", personStoreId, "partyClassificationGroupId", level));
//            if (!ServiceUtil.isSuccess(createPartyClassificationOutMap)) {
//                return createPartyClassificationOutMap;
//            }


        Map<String, Object> result = ServiceUtil.returnSuccess();
        result.put("storeId", personStoreId);
        return result;
    }


}
