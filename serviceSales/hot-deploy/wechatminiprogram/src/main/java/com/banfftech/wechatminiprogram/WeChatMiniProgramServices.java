package main.java.com.banfftech.wechatminiprogram;

import main.java.com.banfftech.personmanager.PersonManagerQueryServices;
import main.java.com.banfftech.personmanager.PersonManagerServices;
import main.java.com.banfftech.platformmanager.constant.PeConstant;
import main.java.com.banfftech.platformmanager.wechat.AccessToken;
import main.java.com.banfftech.platformmanager.wechat.WeChatUtil;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.base.util.UtilHttp;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import sun.net.www.content.text.Generic;
import org.apache.ofbiz.base.util.Debug;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import static main.java.com.banfftech.personmanager.PersonManagerQueryServices.module;
import static main.java.com.banfftech.personmanager.PersonManagerQueryServices.queryPersonBaseInfo;
import static main.java.com.banfftech.personmanager.PersonManagerServices.*;
import static main.java.com.banfftech.platformmanager.util.HttpHelper.sendGet;
import static main.java.com.banfftech.platformmanager.wechat.WeChatUtil.getAccessToken;

/**
 * Created by S on 2017/11/20.
 */
public class WeChatMiniProgramServices {


    public final static String module = WeChatMiniProgramServices.class.getName();


    public static final String resourceError = "WechatMiniProgramErrorUiLabels.xml";

    public static String getRandomActionName(int number) {

        switch (number) {
            default:
                return "也搜索过相同 'aaaaa'";
            case 1:
                return "有你想要'sadsad'";
            case 2:
                return "是专家!,关于这方面 'asdsadsad'";
            case 3:
                return "的朋友沈寅麟关注过 'sadsad'";
            case 4:
                return "的朋友王玉亮购买过 'ABAB'";
            case 5:
                return "的朋友冯浩分享过 'AAAC'";
            case 6:
                return "的同学李玉峰在提供 'BBB'";
            case 7:
                return "的邻居董梦洁提供 'AAA'";


        }

    }


    /**
     * 2C发货
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> quickShipEntireOrder2C(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();

        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));


        String orderId = (String) context.get("orderId");
        String trackingNumber = (String) context.get("trackingNumber");


        GenericValue orderHeader = delegator.findOne("OrderHeader", UtilMisc.toMap("orderId", orderId), false);

        if (null == orderHeader) {
            return ServiceUtil.returnError(UtilProperties.getMessage(resourceError, "ORDER_NOT_FOUND", locale));
        }

        //拿originFacilityId
        String originFacilityId = orderHeader.getString("originFacilityId");
        Map<String, Object> serviceResult = dispatcher.runSync("quickShipEntireOrder", UtilMisc.toMap("userLogin", admin, "orderId", orderId, "originFacilityId", originFacilityId));
        if (!ServiceUtil.isSuccess(serviceResult)) {
            Debug.logInfo("*quickShipEntireOrder2C fail:" + orderId, module);
            return serviceResult;
        }


        GenericValue orderItemShipGroup = EntityQuery.use(delegator).from("OrderItemShipGroup").where("orderId", orderId).queryFirst();

        Map<String, Object> updateResult = dispatcher.runSync("updateOrderItemShipGroup", UtilMisc.toMap("userLogin", admin, "orderId", orderId, "shipGroupSeqId", orderItemShipGroup.getString("shipGroupSeqId"), "trackingNumber", trackingNumber));
        if (!ServiceUtil.isSuccess(updateResult)) {
            Debug.logInfo("*updateOrderItemShipGroup fail:" + orderId, module);
            return updateResult;
        }


        return resultMap;
    }


    /**
     * 检查当前SKU是否能够下单
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> checkSkusIsRight(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();

        GenericValue userLogin = (GenericValue) context.get("userLogin");
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));


        String msg = null;

        String productId = (String) context.get("productId");
        String amount = (String) context.get("amount");
        String price = (String) context.get("price");
        String neiMai = (String) context.get("neiMai");
        GenericValue productPrice = EntityQuery.use(delegator).from("ProductAndPriceView").where(UtilMisc.toMap("productId", productId)).queryFirst();

        if(null!=neiMai && neiMai.equals("true")){
            //TODO 暂时不效验价格
        }else{
            //检查价格
            String priceStr = productPrice.get("price") + "";
            if (!priceStr.equals(price)) {
                msg = productPrice.getString("productName") + "的价格已经调整!";
                resultMap.put("checkMsg", msg);
                return resultMap;
            }
        }

        GenericValue category = EntityQuery.use(delegator).from("ProductAndCategoryMember").where("productId", productId).queryFirst();
        String productStoreId = category.getString("productStoreId");
        GenericValue store = EntityQuery.use(delegator).from("ProductStore").where("productStoreId", productStoreId).queryFirst();
        String inventoryFacilityId = store.getString("inventoryFacilityId");
        //检查库存
        Map<String, Object> getInventoryAvailableByFacilityMap = dispatcher.runSync("getInventoryAvailableByFacility", UtilMisc.toMap("userLogin", admin,
                "facilityId", inventoryFacilityId, "productId", productId));
        if (ServiceUtil.isSuccess(getInventoryAvailableByFacilityMap)) {
            BigDecimal atpt = (BigDecimal) getInventoryAvailableByFacilityMap.get("availableToPromiseTotal");
//            String availableToPromiseTotal = getInventoryAvailableByFacilityMap.get("availableToPromiseTotal") + "";
            if (atpt.intValue() < Integer.parseInt(amount)) {
                msg = productPrice.getString("productName") + "的库存不足!";
                resultMap.put("checkMsg", msg);
                return resultMap;
            }
        }

        return resultMap;
    }


    /**
     * assocCustToSalesRep
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> assocCustToSalesRep(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();

        GenericValue userLogin = (GenericValue) context.get("userLogin");
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));


        String salesPartyId = (String) context.get("salesPartyId");
        String partyId = (String) userLogin.get("partyId");


        //建立我与他的partyRelationship
        boolean isSuccess = assocCustToSalesRep(admin, delegator, dispatcher, partyId, salesPartyId);
        if (!isSuccess) {
            Debug.logInfo("*Assoc Cust To SalesRep Relationship Fail:" + isSuccess, module);
            return ServiceUtil.returnError(UtilProperties.getMessage(resourceError, "ASSOC_PRELATION_ERROR", locale));
        }

        return resultMap;
    }


    /**
     * 加入转发链
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> joinForwardChain(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();

        GenericValue userLogin = (GenericValue) context.get("userLogin");
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));
        String objectId = (String) context.get("objectId");
        String objectType = (String) context.get("objectType");
        String dateKey = (String) context.get("dateKey");
        String partyIdFrom = (String) context.get("partyIdFrom");
        String partyId = userLogin.getString("partyId");
        String appId = (String) context.get("appId");


        Debug.logInfo("*joinForwardChain", module);
        Debug.logInfo("objectId:" + objectId, module);
        Debug.logInfo("appId:" + appId, module);
        Debug.logInfo("objectType:" + objectType, module);
        Debug.logInfo("dateKey:" + dateKey, module);
        Debug.logInfo("partyIdFrom:" + partyIdFrom, module);

        String productStoreId = "";

        GenericValue queryAppConfig =
                EntityQuery.use(delegator).from("PartyStoreAppConfig").where(
                        "idValue", appId).queryFirst();
        //为空说明现在是2C 卖家店铺
        String appServiceType = "2C";
        if (null != queryAppConfig) {
            appServiceType = queryAppConfig.getString("appServiceType");
            productStoreId = queryAppConfig.getString("productStoreId");
        }


        /**
         * 1.LogicBlock
         * 根据线索找到上层链,记录/更新到我Chain的临时表
         */
        GenericValue beforeForwardChain = EntityQuery.use(delegator).from("WorkEffortAndPartyReFerrer").where(UtilMisc.toMap("partyId", partyIdFrom, "description", dateKey)).queryFirst();
        if (null == beforeForwardChain) {
            Debug.logError("*before ForwardChain Not Found! partyIdFrom:" + partyIdFrom + "|dateKey:" + dateKey, module);
            return ServiceUtil.returnError(UtilProperties.getMessage(resourceError, "FORWARDCHAIN_NOT_FOUND", locale));
        }
        String workEffortId = beforeForwardChain.getString("workEffortId");
        GenericValue shareChain = delegator.makeValidValue("ShareChain", UtilMisc.toMap("partyId", partyId, "workEffortId", workEffortId));
        delegator.createOrStore(shareChain);


        /**
         * 2.LogicBlock
         * 将我点开的这个动作,记录到链条中
         */
        addAddressRoleToWorkeffort(dispatcher, delegator, admin, partyId, workEffortId);
        /**
         * 3.LogicBlock
         * 如果发给我的人,他是一位销售代表,那么他就是我的销售代表。
         */
        GenericValue amISalesRep = EntityQuery.use(delegator).from("ProductStoreRole").where("productStoreId", productStoreId, "partyId", partyId, "roleTypeId", "SALES_REP").queryFirst();
        GenericValue isSalesRep = EntityQuery.use(delegator).from("ProductStoreRole").where("productStoreId", productStoreId, "partyId", partyIdFrom, "roleTypeId", "SALES_REP").queryFirst();

        //自己就是销售代表就不要管这个逻辑了
        if (null != isSalesRep && amISalesRep == null) {
            //建立我与他的partyRelationship
            boolean isSuccess = assocCustToSalesRep(admin, delegator, dispatcher, partyId, partyIdFrom);
            if (!isSuccess) {
                Debug.logInfo("*Assoc Cust To SalesRep Relationship Fail:" + isSuccess, module);
                return ServiceUtil.returnError(UtilProperties.getMessage(resourceError, "ASSOC_PRELATION_ERROR", locale));
            }
        }

        /**
         * 4.LogicBlock
         * 如果初始链中的是一个产品,则要增加浏览量。
         */
        updateProductBizData(Integer.parseInt("1"), delegator, dispatcher, admin, partyId, objectId, workEffortId, "ADDRESSEE_PRODUCT");

        GenericValue forwardChainFact = EntityQuery.use(delegator).from("YpForwardChainFact").where(
                "partyIdTo", partyIdFrom).orderBy("-createDate").queryFirst();

        Map<String, String> personInfoMap = queryPersonBaseInfo(delegator, partyId);

        //此处需要增加销售代表的逻辑.如果自己是销售代表,则base就是自己
        String base = forwardChainFact == null ? partyIdFrom : forwardChainFact.getString("basePartyId");
        if (isSalesRep != null) {
            //FROM IS BASE .因为传给你的人他是销售代表.你变成了他的首行
            base = partyIdFrom;
        }
        // base 不会成为自己的 to
        // from 不会成为自己的 to
//        Debug.logInfo("-> ASYNC[IN_FORWARDCHAIN_FACT]--------------------------------------",module);
        Debug.logInfo("NOW:"+new Date(),module);
        Debug.logInfo("-> TO:"+partyId,module);
        Debug.logInfo("-> FROM:"+partyIdFrom,module);
        String staticPartyIdFrom = partyIdFrom;
        Debug.logInfo("-> BASE:"+base,module);
//
//        boolean isFirstView = true;
//        GenericValue forwardChainFactTemp = EntityQuery.use(delegator).from("YpForwardChainFactTemp").where(
//                "partyIdTo", partyId).queryFirst();
//        Map<String,String> userInfo = queryPersonBaseInfo(delegator, partyId);

        //打开自己的链接了!,这个时候如果存在深度链接,应更改为自己成base,否则点入者挂载出错
        if (partyId.equals(partyIdFrom)) {
                GenericValue myForwardChainFactTemp = EntityQuery.use(delegator).from("YpForwardChainFactTemp").where(
                "partyIdTo", partyId).queryFirst();

            // 更新我的软连接
            if(null!=myForwardChainFactTemp){
                myForwardChainFactTemp.set("basePartyId",partyId);
                myForwardChainFactTemp.store();
                Debug.logInfo("update ... " + myForwardChainFactTemp,module);
            }
        } else {
            //是别人转给你的，理应加在他后面。

            //转给你的人他拥有base? 那你跟在他后面
            GenericValue forwardChainFactTemp = EntityQuery.use(delegator).from("YpForwardChainFactTemp").where(
                    "partyIdTo", partyIdFrom).orderBy("-createDate").queryFirst();
            if (null != forwardChainFactTemp) {
                String fromPartyId = forwardChainFactTemp.getString("partyIdFrom");
                String basePartyId = forwardChainFactTemp.getString("basePartyId");
                dispatcher.runSync("inForwardChainFact", UtilMisc.toMap(
                        "userLogin", admin,
                        "partyIdFrom", staticPartyIdFrom,
                        "partyIdTo", partyId,
                        "workEffortId", workEffortId,
                        "basePartyId", basePartyId,
                        "firstName", personInfoMap.get("firstName"),
                        "objectInfo", personInfoMap.get("headPortrait"),
                        "createDate", new Timestamp(new Date().getTime())));
                //并且你会继承他的base

                 GenericValue newForwardChainFactTemp = delegator.makeValidValue("YpForwardChainFactTemp", UtilMisc.toMap(
                    "partyIdFrom", staticPartyIdFrom,
                         "fcId", delegator.getNextSeqId("YpForwardChainFactTemp"),
                    "partyIdTo", partyId,
                    "workEffortId", workEffortId,
                    "basePartyId", basePartyId,
                    "createDate", new Timestamp(new Date().getTime())
                    ));
                delegator.create(newForwardChainFactTemp);
            } else {
                //转给你的人没有base, 那你就是他下的1级
                //创建下级关系
                dispatcher.runSync("inForwardChainFact", UtilMisc.toMap(
                        "userLogin", admin,
                        "partyIdFrom", partyIdFrom,
                        "partyIdTo", partyId,
                        "workEffortId", workEffortId,
                        "basePartyId", partyIdFrom,
                        "firstName", personInfoMap.get("firstName"),
                        "objectInfo", personInfoMap.get("headPortrait"),
                        "createDate", new Timestamp(new Date().getTime())));
                //增加你的链深度关系
                GenericValue newForwardChainFactTemp = delegator.makeValidValue(
                        "YpForwardChainFactTemp", UtilMisc.toMap(
                                "fcId", delegator.getNextSeqId("YpForwardChainFactTemp"),
                        "partyIdFrom", partyIdFrom,
                        "partyIdTo", partyId,
                        "workEffortId", workEffortId,
                        "basePartyId", partyIdFrom,
                        "createDate", new Timestamp(new Date().getTime())
                ));
                delegator.create(newForwardChainFactTemp);
            }

        }


        //说明加入别人的链路
//        if(null!=forwardChainFactTemp){
//            isFirstView = !isFirstView;
//            Debug.logInfo("-> forwardChainFactTemp:"+forwardChainFactTemp,module);
//
//            // INIT SERVICE FIELD
//            String    fromPartyId = forwardChainFactTemp.getString("partyIdFrom");
//            String basePartyId = forwardChainFactTemp.getString("basePartyId");
//             workEffortId = forwardChainFactTemp.getString("workEffortId");
//            Debug.logInfo("-> fromPartyId:"+fromPartyId,module);
//
//            dispatcher.runSync("inForwardChainFact", UtilMisc.toMap(
//                    "userLogin", admin,
//                    "partyIdFrom", fromPartyId,
//                    "partyIdTo", partyId,
//                    "workEffortId", workEffortId,
//                    "basePartyId", basePartyId,
//                    "firstName", userInfo.get("firstName"),
//                    "objectInfo", userInfo.get("headPortrait"),
//                    "createDate", new Timestamp(new Date().getTime())));
//        }

//        if(base.equals(partyId) || partyIdFrom.equals(partyId)){
//                    forwardChainFactTemp.remove();
//        }else{
//
//            if(forwardChainFactTemp!=null){
//                forwardChainFactTemp.set("partyIdFrom", partyIdFrom);
////                forwardChainFactTemp.set("partyIdTo", partyId);
//                forwardChainFactTemp.set("workEffortId", workEffortId);
//                forwardChainFactTemp.set("basePartyId", base);
//                forwardChainFactTemp.set("createDate", new Timestamp(new Date().getTime()));
//                forwardChainFactTemp.store();
//            }else{

        // 记录到 olap fact temp
//            GenericValue ypForwardChainFactTemp = delegator.makeValidValue("YpForwardChainFactTemp", UtilMisc.toMap(
//                    "partyIdFrom", partyIdFrom,
//                    "partyIdTo", partyId,
//                    "workEffortId", workEffortId,
//                    "basePartyId", base,
//                    "createDate", new Timestamp(new Date().getTime())
//                    ));
//                delegator.create(ypForwardChainFactTemp);
//            }
//            dispatcher.runSync("inForwardChainFact", UtilMisc.toMap(
//                    "userLogin", admin,
//                    "partyIdFrom", partyIdFrom,
//                    "partyIdTo", partyId,
//                    "workEffortId", workEffortId,
//                    "basePartyId", base,
//                    "firstName", personInfoMap.get("firstName"),
//                    "objectInfo", personInfoMap.get("headPortrait"),
//                    "createDate", new Timestamp(new Date().getTime())));
        //}

        //CONTACT
        dispatcher.runAsync("createPartyToPartyRelation", UtilMisc.toMap("userLogin", admin, "partyIdFrom", partyId, "partyIdTo", partyIdFrom, "relationShipType", PeConstant.CONTACT));


//        if(isFirstView){
//            dispatcher.runSync("inForwardChainFact", UtilMisc.toMap(
//                    "userLogin", admin,
//                    "partyIdFrom", partyIdFrom,
//                    "partyIdTo", partyId,
//                    "workEffortId", workEffortId,
//                    "basePartyId", base,
//                    "firstName", userInfo.get("firstName"),
//                    "objectInfo", userInfo.get("headPortrait"),
//                    "createDate", new Timestamp(new Date().getTime())));
//        }

        return resultMap;
    }

    /**
     * 建立销售代表和客户的关系
     *
     * @param delegator
     * @param dispatcher
     * @param partyId
     * @param partyIdFrom
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    private static boolean assocCustToSalesRep(GenericValue admin, Delegator delegator, LocalDispatcher dispatcher, String partyId, String partyIdFrom)
            throws GenericEntityException, GenericServiceException {
        GenericValue dataRelation = EntityQuery.use(delegator).from("PartyRelationship").where(
                "partyIdFrom", partyIdFrom,
                "partyIdTo", partyId,
                "partyRelationshipTypeId", "CUSTOMER_REL",
                "roleTypeIdTo", "PLACING_CUSTOMER",
                "roleTypeIdFrom", "SALES_REP").queryFirst();
        Debug.logInfo("*dataRelation=" + dataRelation, module);
        //Create
        if (null == dataRelation) {
            Map<String, Object> createPartyRelationshipInMap = new HashMap<String, Object>();
            createPartyRelationshipInMap.put("partyIdFrom", partyIdFrom);
            createPartyRelationshipInMap.put("fromDate", org.apache.ofbiz.base.util.UtilDateTime.nowTimestamp());
            createPartyRelationshipInMap.put("partyIdTo", partyId);
            createPartyRelationshipInMap.put("partyRelationshipTypeId", "CUSTOMER_REL");
            createPartyRelationshipInMap.put("roleTypeIdTo", "PLACING_CUSTOMER");
            createPartyRelationshipInMap.put("roleTypeIdFrom", "SALES_REP");
            GenericValue pr = delegator.makeValue("PartyRelationship", createPartyRelationshipInMap);
            pr.create();

        } else {
            //Delete
            Map<String, Object> deletePartyRelationshipInMap = new HashMap<String, Object>();
            deletePartyRelationshipInMap.put("userLogin", admin);
            deletePartyRelationshipInMap.put("partyIdFrom", partyIdFrom);
            deletePartyRelationshipInMap.put("fromDate", dataRelation.get("fromDate"));
            deletePartyRelationshipInMap.put("partyIdTo", partyId);
            deletePartyRelationshipInMap.put("roleTypeIdTo", "PLACING_CUSTOMER");
            deletePartyRelationshipInMap.put("roleTypeIdFrom", "SALES_REP");
            Map<String, Object> serviceResultMap = dispatcher.runSync("deletePartyRelationship", deletePartyRelationshipInMap);
            if (!ServiceUtil.isSuccess(serviceResultMap)) {
                Debug.logError("*Mother Fuck Delete PartyRealtion OutMap Error:" + serviceResultMap, module);
                return false;
            }

            //Create

            Map<String, Object> createPartyRelationshipInMap = new HashMap<String, Object>();
            createPartyRelationshipInMap.put("fromDate", org.apache.ofbiz.base.util.UtilDateTime.nowTimestamp());
            createPartyRelationshipInMap.put("partyIdFrom", partyIdFrom);
            createPartyRelationshipInMap.put("partyIdTo", partyId);
            createPartyRelationshipInMap.put("partyRelationshipTypeId", "CUSTOMER_REL");
            createPartyRelationshipInMap.put("roleTypeIdTo", "PLACING_CUSTOMER");
            createPartyRelationshipInMap.put("roleTypeIdFrom", "SALES_REP");
            GenericValue pr = delegator.makeValue("PartyRelationship", createPartyRelationshipInMap);
            pr.create();
        }
        return true;
    }

    /**
     * 创建链 、 或创建子链加入父链
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> createForwardChain(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();

        GenericValue userLogin = (GenericValue) context.get("userLogin");
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));
        String objectId = (String) context.get("objectId");
        String objectType = (String) context.get("objectType");
        String dateKey = (String) context.get("dateKey");
        if (null == userLogin) {
            Debug.logError("*CreateShareChain-Access_Tarjeta:userLoginIS_NULL", module);
            return ServiceUtil.returnError(UtilProperties.getMessage(resourceError, "TARJETA_NOT_FOUND", locale));
        }

        String partyId = userLogin.getString("partyId");

        /**
         * objectType ENUM
         * PRODUCT、CATALOG、APP
         */
        GenericValue shareChain = EntityQuery.use(delegator).from("ShareChain").where("partyId", partyId).queryFirst();
        String newWorkEffortId = "";
        if (null != shareChain) {
            //说明上层有链,这层需要创建子链
            String beforeChainId = shareChain.getString("workEffortId");
            Map<String, Object> createWorkEffortMap = UtilMisc.toMap("userLogin", userLogin, "currentStatusId", "CAL_IN_PLANNING",
                    "workEffortName", "子链路", "workEffortTypeId", "EVENT", "description", dateKey,
                    "actualStartDate", org.apache.ofbiz.base.util.UtilDateTime.nowTimestamp(), "percentComplete", new Long(1));
            Map<String, Object> serviceResultByCreateWorkEffortMap = dispatcher.runSync("createWorkEffort",
                    createWorkEffortMap);
            if (!ServiceUtil.isSuccess(serviceResultByCreateWorkEffortMap)) {
                Debug.logInfo("*Create WorkEffort Fail:" + serviceResultByCreateWorkEffortMap, module);
                return ServiceUtil.returnError(UtilProperties.getMessage(resourceError, "WORK_EFFORT_CREATE_FAIL", locale));
            }
            newWorkEffortId = (String) serviceResultByCreateWorkEffortMap.get("workEffortId");

            //关联父子链路
            Map<String, Object> createWorkEffortAssocMap = UtilMisc.toMap("userLogin", userLogin,
                    "workEffortIdFrom", beforeChainId, "workEffortIdTo", newWorkEffortId, "workEffortAssocTypeId", "ROUTING_COMPONENT");
            Map<String, Object> createWorkEffortAssocResultMap = dispatcher.runSync("createWorkEffortAssoc",
                    createWorkEffortAssocMap);
            if (!ServiceUtil.isSuccess(createWorkEffortAssocResultMap)) {
                Debug.logInfo("*Create WorkEffort Fail:" + createWorkEffortAssocResultMap, module);
                return ServiceUtil.returnError(UtilProperties.getMessage(resourceError, "WORK_EFFORT_CREATE_FAIL", locale));
            }

            switch (objectType) {
                case "PRODUCT":
                    Map<String, Object> createWorkEffortGoodStandardMap = UtilMisc.toMap("userLogin", userLogin, "statusId", "WEGS_CREATED",
                            "workEffortGoodStdTypeId", "GENERAL_SALES", "workEffortId", newWorkEffortId, "productId", objectId);
                    Map<String, Object> createWorkEffortGoodStandardResultMap = dispatcher.runSync("createWorkEffortGoodStandard", createWorkEffortGoodStandardMap);
                    if (!ServiceUtil.isSuccess(createWorkEffortGoodStandardResultMap)) {
                        Debug.logInfo("*Create WorkEffortGoodStandard Fail:" + createWorkEffortGoodStandardMap, module);
                    }
                    GenericValue category = EntityQuery.use(delegator).from("ProductAndCategoryMember").where("productId", objectId).queryFirst();
                    String rowStoreId = category.getString("productStoreId");
                    GenericValue iamSalesRep = EntityQuery.use(delegator).from("ProductStoreRole").where("productStoreId", rowStoreId, "partyId", partyId, "roleTypeId", "SALES_REP").queryFirst();
                    Debug.logInfo("*createForwardChain iamSalesRep?" + iamSalesRep, module);
                    Debug.logInfo("*createForwardChain rowStoreId=" + rowStoreId, module);
                    Debug.logInfo("*createForwardChain partyId=" + partyId, module);
                    if (iamSalesRep != null) {
                        // create ForWard Count
                        createProductBizData(delegator, dispatcher, admin, partyId, objectId, newWorkEffortId, "FORWARD_PRODUCT");

                        break;
                    } else {
                        // Update ForWard Count
                        updateProductBizData(Integer.parseInt("1"), delegator, dispatcher, admin, partyId, objectId, beforeChainId, "FORWARD_PRODUCT");
                        break;
                    }

                case "CATALOG":
                    Map<String, Object> createWorkEffortNoteMap = UtilMisc.toMap("userLogin", userLogin,
                            "workEffortId", newWorkEffortId, "noteName", objectType, "noteInfo", objectId);
                    Map<String, Object> createWorkEffortNoteResultMap = dispatcher.runSync("createWorkEffortNote", createWorkEffortNoteMap);
                    if (!ServiceUtil.isSuccess(createWorkEffortNoteResultMap)) {
                        Debug.logInfo("*createWorkEffortNote(catalog)Fail:" + createWorkEffortNoteResultMap, module);
                    }
                    break;
                case "APP":
                    createWorkEffortNoteMap = UtilMisc.toMap("userLogin", userLogin,
                            "workEffortId", newWorkEffortId, "noteName", objectType, "noteInfo", objectId);
                    createWorkEffortNoteResultMap = dispatcher.runSync("createWorkEffortNote", createWorkEffortNoteMap);
                    if (!ServiceUtil.isSuccess(createWorkEffortNoteResultMap)) {
                        Debug.logInfo("*createWorkEffortNote(app)Fail:" + createWorkEffortNoteResultMap, module);
                    }
                    break;
            }

        } else {
            //没有上层链路,单纯创建链
            Map<String, Object> createWorkEffortMap = UtilMisc.toMap("userLogin", userLogin, "currentStatusId", "CAL_IN_PLANNING",
                    "workEffortName", "根链路", "workEffortTypeId", "EVENT", "description", dateKey,
                    "actualStartDate", org.apache.ofbiz.base.util.UtilDateTime.nowTimestamp(), "percentComplete", new Long(1));
            Map<String, Object> serviceResultByCreateWorkEffortMap = dispatcher.runSync("createWorkEffort",
                    createWorkEffortMap);
            if (!ServiceUtil.isSuccess(serviceResultByCreateWorkEffortMap)) {
                Debug.logInfo("*Create WorkEffort Fail:" + serviceResultByCreateWorkEffortMap, module);
                return ServiceUtil.returnError(UtilProperties.getMessage(resourceError, "WORK_EFFORT_CREATE_FAIL", locale));
            }
            newWorkEffortId = (String) serviceResultByCreateWorkEffortMap.get("workEffortId");
            switch (objectType) {
                case "PRODUCT":
                    Map<String, Object> createWorkEffortGoodStandardMap = UtilMisc.toMap("userLogin", userLogin, "statusId", "WEGS_CREATED",
                            "workEffortGoodStdTypeId", "GENERAL_SALES", "workEffortId", newWorkEffortId, "productId", objectId);
                    Map<String, Object> createWorkEffortGoodStandardResultMap = dispatcher.runSync("createWorkEffortGoodStandard", createWorkEffortGoodStandardMap);
                    if (!ServiceUtil.isSuccess(createWorkEffortGoodStandardResultMap)) {
                        Debug.logInfo("*Create WorkEffortGoodStandard Fail:" + createWorkEffortGoodStandardMap, module);
                    }

                    //创建我的产品业务事件
                    createProductBizData(delegator, dispatcher, admin, partyId, objectId, newWorkEffortId, "FORWARD_PRODUCT");

                    break;
                case "CATALOG":
                    Map<String, Object> createWorkEffortNoteMap = UtilMisc.toMap("userLogin", userLogin,
                            "workEffortId", newWorkEffortId, "noteName", objectType, "noteInfo", objectId);
                    Map<String, Object> createWorkEffortNoteResultMap = dispatcher.runSync("createWorkEffortNote", createWorkEffortNoteMap);
                    if (!ServiceUtil.isSuccess(createWorkEffortNoteResultMap)) {
                        Debug.logInfo("*createWorkEffortNote(catalog)Fail:" + createWorkEffortNoteResultMap, module);
                    }
                    break;
                case "APP":
                    createWorkEffortNoteMap = UtilMisc.toMap("userLogin", userLogin,
                            "workEffortId", newWorkEffortId, "noteName", objectType, "noteInfo", objectId);
                    createWorkEffortNoteResultMap = dispatcher.runSync("createWorkEffortNote", createWorkEffortNoteMap);
                    if (!ServiceUtil.isSuccess(createWorkEffortNoteResultMap)) {
                        Debug.logInfo("*createWorkEffortNote(app)Fail:" + createWorkEffortNoteResultMap, module);
                    }
                    break;
            }


        }


        //把自己加入到链中
        addRefreRoleToWorkeffort(dispatcher, delegator, admin, partyId, newWorkEffortId);


        //Map<String,String> userInfo = queryPersonBaseInfo(delegator,partyId);
        //创建自己的OLAP链或加入别人的转发链

//        String fromPartyId = "NO_PARTY";
//        String basePartyId = partyId;
//        String workEffortId = "NA";
//        String partyIdTo    = "NO_PARTY";
//
//        GenericValue forwardChainFactTemp  = EntityQuery.use(delegator).from("YpForwardChainFactTemp").where(
//                "partyIdTo", partyId).queryFirst();

        //说明加入别人的链路
        // if(null!=forwardChainFactTemp){

        // INIT SERVICE FIELD
//            fromPartyId = forwardChainFactTemp.getString("partyIdFrom");
//            basePartyId = forwardChainFactTemp.getString("basePartyId");
//            workEffortId = forwardChainFactTemp.getString("workEffortId");
//            partyIdTo  = partyId;
        //}

//        dispatcher.runSync("inForwardChainFact", UtilMisc.toMap(
//                "userLogin", admin,
//                "partyIdFrom", fromPartyId,
//                "partyIdTo", partyIdTo,
//                "workEffortId", workEffortId,
//                "basePartyId", basePartyId,
//                "firstName", userInfo.get("firstName"),
//                "objectInfo", userInfo.get("headPortrait"),
//                "createDate", new Timestamp(new Date().getTime())));


        resultMap.put("workEffortId", newWorkEffortId);

        return resultMap;
    }

    /**
     * 创建我的唯一产品业务统计数据
     *
     * @param delegator
     * @param dispatcher
     * @param admin
     * @param partyId
     * @param productId
     * @param objectId
     * @param bizTypeId
     * @throws GenericEntityException
     * @throws GenericServiceExcetpion
     */
    private static void createProductBizData(Delegator delegator, LocalDispatcher dispatcher, GenericValue admin, String partyId, String productId, String objectId, String bizTypeId) throws GenericEntityException, GenericServiceException {

        // Find If Not Exsits

        GenericValue productBizData = EntityQuery.use(delegator).from("ProductBizData").where("ownerPartyId", partyId, "productId", productId).queryFirst();

        if (null == productBizData) {
            // Create
            Map<String, Object> createProductBizData = new HashMap<String, Object>();
            createProductBizData.put("ownerPartyId", partyId);
            createProductBizData.put("productId", productId);
            createProductBizData.put("addresseeCount", "0");
            createProductBizData.put("forwardCount", "0");
            createProductBizData.put("buyCount", "0");
            String dataId = delegator.getNextSeqId("ProductBizData");
            createProductBizData.put("dataId", dataId);
            createProductBizData.put("fromDate", org.apache.ofbiz.base.util.UtilDateTime.nowTimestamp());
            GenericValue bizData = delegator.makeValue("ProductBizData", createProductBizData);
            bizData.create();

            // Do Create Detail
            //  createProductBizDataDetail(delegator,dataId, objectId, partyId, bizTypeId,productId);
        }

    }

    /**
     * updateProductBizDataFromOrder
     *
     * @param count
     * @param delegator
     * @param dispatcher
     * @param admin
     * @param partyId
     * @param productId
     * @param objectId
     * @param bizTypeId
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static void updateProductBizDataFromOrder(String salesRepId, int count, Delegator delegator, LocalDispatcher dispatcher, GenericValue admin, String partyId, String productId, String objectId, String bizTypeId) throws GenericEntityException, GenericServiceException {


        String ownerPartyId = salesRepId;

        GenericValue productBizData = EntityQuery.use(delegator).from("ProductBizData").where("ownerPartyId", ownerPartyId, "productId", productId).queryFirst();

        if (null == productBizData) {

        } else {
            String dataId = productBizData.getString("dataId");

            GenericValue isExsitsBizData = EntityQuery.use(delegator).from("ProductBizDataDetail").where("bizTypeId", bizTypeId, "dataId", dataId, "partyId", partyId, "objectId", productId).queryFirst();
            //已经记录过则不可刷单
            if (null == isExsitsBizData) {
                switch (bizTypeId) {
                    case "FORWARD_PRODUCT":
                        String forwardCount = productBizData.getString("forwardCount");
                        productBizData.set("forwardCount", (Integer.parseInt(forwardCount) + count) + "");
                        break;
                    case "ADDRESSEE_PRODUCT":
                        String addresseeCount = productBizData.getString("addresseeCount");
                        productBizData.set("addresseeCount", (Integer.parseInt(addresseeCount) + count) + "");
                        break;
                    case "BUY_PRODUCT":
                        String buyCount = productBizData.getString("buyCount");
                        productBizData.set("buyCount", (Integer.parseInt(buyCount) + count) + "");
                        break;
                }

                productBizData.store();

                // Do Create Detail
                createProductBizDataDetail(delegator, dataId, objectId, partyId, bizTypeId, productId);
            }
        }

    }


    /**
     * updateProductBizData
     *
     * @param delegator
     * @param dispatcher
     * @param admin
     * @param partyId
     * @param productId
     * @param objectId
     * @param bizTypeId
     * @throws GenericEntityException
     * @throws GenericServiceExcetpion
     */
    public static void updateProductBizData(int count, Delegator delegator, LocalDispatcher dispatcher, GenericValue admin, String partyId, String productId, String objectId, String bizTypeId) throws GenericEntityException, GenericServiceException {


        String workEffortId = objectId;

        GenericValue workEffortAndReferrer = EntityQuery.use(delegator).from("WorkEffortAndPartyReFerrer").where("workEffortId", workEffortId).queryFirst();

        if (null != workEffortAndReferrer) {


            String ownerPartyId = workEffortAndReferrer.getString("partyId");

            GenericValue productBizData = EntityQuery.use(delegator).from("ProductBizData").where("ownerPartyId", ownerPartyId, "productId", productId).queryFirst();

            if (null == productBizData) {
                //创建我的产品业务事件2
                //createProductBizData(delegator, dispatcher, admin, ownerPartyId, productId, workEffortId, "FORWARD_PRODUCT");
            } else {
                String dataId = productBizData.getString("dataId");
                GenericValue isExsitsBizData = EntityQuery.use(delegator).from("ProductBizDataDetail").where("bizTypeId", bizTypeId, "dataId", dataId, "partyId", partyId, "objectId", productId).queryFirst();
                //已经记录过则不可刷单
                // 2018-06-26 我管你记没记过 无限刷
                // if (null == isExsitsBizData) {
                if (1 < 2) {
                    switch (bizTypeId) {
                        case "FORWARD_PRODUCT":
                            String forwardCount = productBizData.getString("forwardCount");
                            productBizData.set("forwardCount", (Integer.parseInt(forwardCount) + count) + "");
                            break;
                        case "ADDRESSEE_PRODUCT":
                            String addresseeCount = productBizData.getString("addresseeCount");
                            productBizData.set("addresseeCount", (Integer.parseInt(addresseeCount) + count) + "");
                            break;
                        case "BUY_PRODUCT":
                            String buyCount = productBizData.getString("buyCount");
                            productBizData.set("buyCount", (Integer.parseInt(buyCount) + count) + "");
                            break;
                    }

                    productBizData.store();

                    // Do Create Detail
                    createProductBizDataDetail(delegator, dataId, objectId, partyId, bizTypeId, productId);
                }
            }
        }
    }

    /**
     * 创建产品业务事件数据明细
     *
     * @param dataId
     * @param objectId
     * @param partyId
     * @param bizTypeId
     */
    private static void createProductBizDataDetail(Delegator delegator, String dataId, String objectId, String partyId, String bizTypeId, String productId) throws GenericEntityException, GenericServiceException {

        // Create Detail Data
        Map<String, Object> createProductBizDataDetail = new HashMap<String, Object>();
        createProductBizDataDetail.put("partyId", partyId);
        createProductBizDataDetail.put("objectId", objectId);
        createProductBizDataDetail.put("dataId", dataId);
        createProductBizDataDetail.put("productId", productId);
        createProductBizDataDetail.put("bizTypeId", bizTypeId);
        createProductBizDataDetail.put("fromDate", org.apache.ofbiz.base.util.UtilDateTime.nowTimestamp());
        GenericValue productBizDataDetail = delegator.makeValue("ProductBizDataDetail", createProductBizDataDetail);
        productBizDataDetail.create();
    }


    /**
     * addPartyToStoreRole
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> addPartyToStoreRole(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();

        GenericValue userLogin = (GenericValue) context.get("userLogin");

        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        String productStoreId = (String) context.get("productStoreId");
        String roleTypeId = (String) context.get("roleTypeId");


        String partyId = (String) userLogin.get("partyId");

        // is Exsits Role ?
        GenericValue productStoreRole = EntityQuery.use(delegator).from("ProductStoreRole").where("partyId", partyId, "roleTypeId", roleTypeId, "productStoreId", productStoreId).queryFirst();

        if (null == productStoreRole) {
            Map<String, Object> createProductStoreRoleMap = new HashMap<String, Object>();
            createProductStoreRoleMap.put("partyId", partyId);
            createProductStoreRoleMap.put("userLogin", admin);
            createProductStoreRoleMap.put("productStoreId", productStoreId);
            createProductStoreRoleMap.put("roleTypeId", roleTypeId);
            Map<String, Object> createProductStoreRoleOutMap = dispatcher.runSync("createProductStoreRole", createProductStoreRoleMap);
            if (!ServiceUtil.isSuccess(createProductStoreRoleOutMap)) {
                Debug.logError("*Mother Fuck Create Product OutMap Error:" + createProductStoreRoleOutMap, module);
                return createProductStoreRoleOutMap;
            }
        }

        return resultMap;
    }


    /**
     * addRoleToStore
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> addRoleToStore(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();

        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));
        String openId = (String) context.get("openId");
        String productStoreId = (String) context.get("productStoreId");
        String roleTypeId = (String) context.get("roleTypeId");


        GenericValue partyIdentification = EntityQuery.use(delegator).from("PartyIdentification").where("idValue", openId, "partyIdentificationTypeId", "WX_MINIPRO_OPEN_ID").queryFirst();
        String partyId = "NA";
        if (UtilValidate.isNotEmpty(partyIdentification)) {
            partyId = (String) partyIdentification.get("partyId");
        }
        // is Exsits Role ?
        GenericValue productStoreRole = EntityQuery.use(delegator).from("ProductStoreRole").where("partyId", partyId, "roleTypeId", roleTypeId, "productStoreId", productStoreId).queryFirst();

        if (null == productStoreRole) {
            Map<String, Object> createProductStoreRoleMap = new HashMap<String, Object>();
            createProductStoreRoleMap.put("partyId", partyId);
            createProductStoreRoleMap.put("userLogin", admin);
            createProductStoreRoleMap.put("productStoreId", productStoreId);
            createProductStoreRoleMap.put("roleTypeId", roleTypeId);
            Map<String, Object> createProductStoreRoleOutMap = dispatcher.runSync("createProductStoreRole", createProductStoreRoleMap);
            if (!ServiceUtil.isSuccess(createProductStoreRoleOutMap)) {
                Debug.logError("*Mother Fuck Create Product OutMap Error:" + createProductStoreRoleOutMap, module);
                return createProductStoreRoleOutMap;
            }
        }

        return resultMap;
    }


    /**
     * wechat ReleaseResource
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> wechatReleaseResource(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();

        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        BigDecimal quantityTotal = new BigDecimal("99999999");

        String tel = (String) context.get("tel");

        String media_id = (String) context.get("media_id");

        //TODO ADD MEDIA _ID

        String unioId = (String) context.get("unioId");

        String kuCun = (String) context.get("kuCun");

        String priceStr = (String) context.get("price");
        //详细地址
        String address = (String) context.get("address");

        //经纬度
        String latitude = (String) context.get("latitude");
        String longitude = (String) context.get("longitude");

        System.out.println("o>>>>>>>>>>>>>>>>>>>>>>>>>> address = " + address);
        System.out.println("o>>>>>>>>>>>>>>>>>>>>>>>>>> latitude = " + latitude);
        System.out.println("o>>>>>>>>>>>>>>>>>>>>>>>>>> longitude = " + longitude);

        if (UtilValidate.isNotEmpty(kuCun) && !kuCun.trim().equals("")) {
            quantityTotal = new BigDecimal(kuCun);
        }
        String productName = (String) context.get("productName");

        String description = (String) context.get("description");
        //默认的价格是0

        BigDecimal price = BigDecimal.ZERO;
        if (UtilValidate.isNotEmpty(priceStr)) {
            price = new BigDecimal(priceStr);
        }


//        GenericValue partyIdentification = EntityQuery.use(delegator).from("PartyIdentification").where("idValue", unioId, "partyIdentificationTypeId", "WX_UNIO_ID").queryFirst();
//        String partyId = "NA";
//        if (UtilValidate.isNotEmpty(partyIdentification)) {
//            partyId = (String) partyIdentification.get("partyId");
//        }

        //逻辑暂时改为 openID
        GenericValue partyIdentification = EntityQuery.use(delegator).from("PartyIdentification").where("idValue", unioId, "partyIdentificationTypeId", "WX_MINIPRO_OPEN_ID").queryFirst();
        String partyId = "NA";
        if (UtilValidate.isNotEmpty(partyIdentification)) {
            partyId = (String) partyIdentification.get("partyId");
        }


        GenericValue userLogin = EntityQuery.use(delegator).from("UserLogin").where("partyId", partyId).queryFirst();

        if (UtilValidate.isNotEmpty(tel)) {
            GenericValue telecomNumber = EntityQuery.use(delegator).from("TelecomNumberAndPartyView").where(UtilMisc.toMap("contactNumber", tel, "partyId", partyId, "contactMechPurposeTypeId", "PHONE_MOBILE", "contactMechTypeId", "TELECOM_NUMBER")).queryFirst();
            if (UtilValidate.isNotEmpty(telecomNumber)) {
            } else {
                // Create Person Contact Info
                Map<String, Object> inputTelecom = UtilMisc.toMap();
                inputTelecom.put("partyId", partyId);
                inputTelecom.put("contactNumber", tel);
                inputTelecom.put("contactMechTypeId", "TELECOM_NUMBER");
                inputTelecom.put("contactMechPurposeTypeId", "PHONE_MOBILE");
                inputTelecom.put("userLogin", admin);
                Map<String, Object> createTelecom = null;
                createTelecom = dispatcher.runSync("createPartyTelecomNumber", inputTelecom);

            }
        }
        //图片
        String filePaths = (String) context.get("filePath");
        String[] filePathsArray = filePaths.split(",");
        System.out.println("->File filePaths = " + filePaths);
        System.out.println("->File filePathsArray = " + filePathsArray);

        //创建产品
        Map<String, Object> createProductInMap = new HashMap<String, Object>();
        createProductInMap.put("userLogin", admin);
        long ctm = System.currentTimeMillis();
        createProductInMap.put("internalName", partyId + "_" + ctm);
        createProductInMap.put("productName", productName);
        createProductInMap.put("isVariant", "Y");
        createProductInMap.put("productTypeId", PeConstant.PRODUCT_TYPE_ID);
        createProductInMap.put("description", description);

        String productId = "";

        for (int i = 0; i < filePathsArray.length; i++) {
            System.out.println("->File Path = " + filePathsArray[i]);
            if (i == 0) {
                createProductInMap.put("smallImageUrl", "http://" + filePathsArray[i] + "?x-oss-process=image/resize,m_pad,h_50,w_50");
                createProductInMap.put("detailImageUrl", "http://" + filePathsArray[i]);
                //调用服务创建产品(资源)
                Map<String, Object> createProductOutMap = dispatcher.runSync("createProduct", createProductInMap);
                if (!ServiceUtil.isSuccess(createProductOutMap)) {
                    Debug.logError("*Mother Fuck Create Product OutMap Error:" + createProductOutMap, module);
                    return createProductOutMap;
                }
                productId = (String) createProductOutMap.get("productId");
            }
            if (i > 0 && i <= 2) {
                //创建产品内容和数据资源附图
                createProductContentAndDataResource("SINGLE_PRODUCT_IMAGE", delegator, dispatcher, admin, productId, "", filePathsArray[i], i);
            }
            if (i > 2 && i <= 4) {
                createProductContentAndDataResource("DETAIL_PRODUCT_IMAGE", delegator, dispatcher, admin, productId, "", filePathsArray[i], i);
            }
            if (i > 4) {
                createProductContentAndDataResource("MATCH_PRODUCT_IMAGE", delegator, dispatcher, admin, productId, "", filePathsArray[i], i);
            }
        }


        //创建产品价格(默认所有变形产品都和虚拟产品价格一致)
        Map<String, Object> createProductPriceInMap = new HashMap<String, Object>();
        createProductPriceInMap.put("userLogin", admin);
        createProductPriceInMap.put("productId", productId);
        createProductPriceInMap.put("currencyUomId", PeConstant.DEFAULT_CURRENCY_UOM_ID);
        createProductPriceInMap.put("price", price);
        createProductPriceInMap.put("productPricePurposeId", PeConstant.PRODUCT_PRICE_DEFAULT_PURPOSE);
        createProductPriceInMap.put("productPriceTypeId", PeConstant.PRODUCT_PRICE_DEFAULT_TYPE_ID);
        createProductPriceInMap.put("productStoreGroupId", PeConstant.NA);

        Map<String, Object> createProductPriceServiceResultMap = dispatcher.runSync("createProductPrice", createProductPriceInMap);

        if (!ServiceUtil.isSuccess(createProductPriceServiceResultMap)) {
            Debug.logError("*Mother Fuck Create Product Price Error:" + createProductPriceServiceResultMap, module);
            return createProductPriceServiceResultMap;

        }

        GenericValue prodCatalogRole = EntityQuery.use(delegator).from("ProdCatalogRole").where("partyId", partyId, "roleTypeId", "ADMIN").queryFirst();


        GenericValue prodCatalogCategory = EntityQuery.use(delegator).from("ProdCatalogCategory").where("prodCatalogId", prodCatalogRole.get("prodCatalogId")).queryFirst();
        //产品关联分类
        String productCategoryId = (String) prodCatalogCategory.get("productCategoryId");
        Map<String, Object> addProductToCategoryInMap = new HashMap<String, Object>();
        addProductToCategoryInMap.put("userLogin", admin);
        addProductToCategoryInMap.put("productId", productId);
        addProductToCategoryInMap.put("productCategoryId", productCategoryId);
        Map<String, Object> addProductToCategoryServiceResultMap = dispatcher.runSync("addProductToCategory", addProductToCategoryInMap);
        if (!ServiceUtil.isSuccess(addProductToCategoryServiceResultMap)) {
            Debug.logError("*Mother Fuck added Product To Category Error:" + addProductToCategoryServiceResultMap, module);
            return addProductToCategoryServiceResultMap;
        }

        //找到仓库
        GenericValue facility = EntityQuery.use(delegator).from("Facility").where("ownerPartyId", partyId).queryFirst();

        //为产品创建库存量
        Map<String, Object> receiveInventoryProductIn = UtilMisc.toMap("userLogin", userLogin,
                "facilityId", (String) facility.get("facilityId"),
                "inventoryItemTypeId", PeConstant.DEFAULT_INV_ITEM,
                "productId", productId,
                "description ", "卖家发布产品时的录入库存",
                "quantityAccepted", quantityTotal,
                "quantityRejected", BigDecimal.ZERO,
                "unitCost", price,
                "ownerPartyId", partyId,
                "partyId", partyId,
                "uomId", PeConstant.DEFAULT_CURRENCY_UOM_ID,
                "currencyUomId", PeConstant.DEFAULT_CURRENCY_UOM_ID);

        Map<String, Object> receiveInventoryProductOut = dispatcher.runSync("receiveInventoryProduct", receiveInventoryProductIn
        );
        if (!ServiceUtil.isSuccess(receiveInventoryProductOut)) {
            Debug.logError("*Mother Fuck Receive Inventory Product Error:" + receiveInventoryProductOut, module);
            return receiveInventoryProductOut;

        }

        if (UtilValidate.isNotEmpty(address)) {
            dispatcher.runSync("createProductAttribute", UtilMisc.toMap("userLogin", admin, "productId", productId, "attrName", "address", "attrValue", address + ""));

            //先找有没有PartyAndPostalAddress
            //PartyAndPostalAddress

            List<GenericValue> partyAndPostalAddress = EntityQuery.use(delegator).from("PartyAndPostalAddress").where("partyId", partyId, "address1", address).queryList();

            if (null != partyAndPostalAddress && partyAndPostalAddress.size() > 0) {

            } else {
                //发货地址:SHIP_ORIG_LOCATION
                String contactMechPurposeTypeId = "SHIP_ORIG_LOCATION";
                Map<String, Object> createPartyPostalAddressOutMap = dispatcher.runSync("createPartyPostalAddress",
                        UtilMisc.toMap("userLogin", admin, "partyId", partyId, "countryGeoId", PeConstant.DEFAULT_GEO_COUNTRY, "city", PeConstant.DEFAULT_CITY_GEO_COUNTRY, "address1", address, "postalCode", PeConstant.DEFAULT_POST_CODE,
                                "contactMechPurposeTypeId", contactMechPurposeTypeId));
//            String contactMechId = (String) createPartyPostalAddressOutMap.get("contactMechId");
                if (!ServiceUtil.isSuccess(createPartyPostalAddressOutMap)) {
                    return createPartyPostalAddressOutMap;
                }
            }
        }
        if (UtilValidate.isNotEmpty(longitude)) {
            dispatcher.runSync("createProductAttribute", UtilMisc.toMap("userLogin", admin, "productId", productId, "attrName", "longitude", "attrValue", longitude + ""));
        }
        if (UtilValidate.isNotEmpty(latitude)) {
            dispatcher.runSync("createProductAttribute", UtilMisc.toMap("userLogin", admin, "productId", productId, "attrName", "latitude", "attrValue", latitude + ""));
        }


        //给产品增加用户角色
        Map<String, Object> addProductRoleServiceResoutMap = dispatcher.runSync("addProductRole", UtilMisc.toMap("userLogin", admin, "productId", productId, "partyId", partyId, "roleTypeId", "ADMIN"));
        if (!ServiceUtil.isSuccess(addProductRoleServiceResoutMap)) {
            Debug.logError("*Mother Fuck Added ProductRoleService  Error:" + addProductRoleServiceResoutMap, module);
            return addProductRoleServiceResoutMap;

        }


        //media
        Debug.logInfo(">>>> media_id:" + media_id, module);
        if (UtilValidate.isNotEmpty(media_id)) {
            GenericValue productMedia = EntityQuery.use(delegator).from("PartyAttribute").where("partyId", partyId, "attrName", "media_id").queryFirst();
            if (null != productMedia) {
                dispatcher.runSync("updatePartyAttribute", UtilMisc.toMap("userLogin", admin, "partyId", partyId, "attrName", "media_id", "attrValue", media_id));
            } else {
                dispatcher.runSync("createPartyAttribute", UtilMisc.toMap("userLogin", admin, "partyId", partyId, "attrName", "media_id", "attrValue", media_id));
            }
        }

        resultMap.put("productId", productId);
        resultMap.put("payToPartyId", partyId);

        return resultMap;
    }


    /**
     * autoCreate FriendRelation
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> autoCreateFriendRelation(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();

        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        String resourcesName = (String) context.get("resourcesName");

        if (null != resourcesName) {
            resourcesName = resourcesName.replaceAll("\"", "");
        }

        Date date = new Date();

        SimpleDateFormat formatter;

        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String pushDate = "" + formatter.format(date);


        String partyIdFrom = "admin";
        String openId = "oeLJkxAP6eivcZkflpPiDFNg";
        String productId = "10000";

        String message = resourcesName;
        String firstName = "提醒";
        String payToPartyId = "10000";
        String tarjeta = "123";
        String jumpUrl = null;


        Map<String, String> personInfoMap = queryPersonBaseInfo(delegator, payToPartyId);


        // 发送模版消息
        AccessToken accessToken = getAccessToken(PeConstant.WECHAT_GZ_APP_ID, PeConstant.ACCESS_KEY_SECRET);
        String URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
        String url = URL.replace("ACCESS_TOKEN", accessToken.getToken());

        JSONObject jsobj1 = new JSONObject();
        JSONObject jsobj2 = new JSONObject();
        JSONObject jsobj3 = new JSONObject();
        JSONObject jsobj4 = new JSONObject();
        JSONObject jsobj5 = new JSONObject();


        jsobj1.put("touser", openId);
        jsobj1.put("template_id", "aFCzhfNrWb0GsEr0ZCVuijLPAQ6cPzPedORxyKHBzbs");
        jsobj1.put("url", "https://www.yo-pe.com/pejump/" + partyIdFrom + "/" + partyIdFrom + "111" + "/" + payToPartyId + "/" + productId);

        JSONObject jsobjminipro = new JSONObject();
        jsobjminipro.put("appid", "wx299644ef4c9afbde");
        jsobjminipro.put("pagepath", "pages/home/home");
        jsobj1.put("miniprogram", jsobjminipro);


        jsobj3.put("value", firstName + "给您发了一条消息");
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


        String friends = (String) context.get("friends");

        if (friends != null) {

            String[] friendArray = friends.split(",");

            for (String name : friendArray) {

                String nextUserName = name.substring(name.lastIndexOf("\":\"") + 3, name.lastIndexOf("\""));


                GenericValue person = EntityQuery.use(delegator).from("Person").where("firstName", nextUserName).queryFirst();


                String nextPartyId = "";

                if (null == person) {
                    //先创建人员
                    Map<String, Object> createPartyInMap = UtilMisc.toMap("userLogin", admin, "nickname", nextUserName,
                            "firstName", nextUserName, "lastName", " ", "gender", "M");
                    Map<String, Object> createPerson = dispatcher.runSync("createUpdatePerson", createPartyInMap);
                    nextPartyId = (String) createPerson.get("partyId");
                } else {
                    nextPartyId = (String) person.get("partyId");
                }


                GenericValue relation = EntityQuery.use(delegator).from("PartyRelationship").where("partyIdFrom", nextPartyId).queryFirst();

                if (relation == null) {
                    //创建人员关系
                    Map<String, Object> createPartyRelationMap = new HashMap<String, Object>();
                    createPartyRelationMap.put("userLogin", admin);
                    createPartyRelationMap.put("partyIdFrom", nextPartyId);
                    createPartyRelationMap.put("partyIdTo", "admin");
                    createPartyRelationMap.put("partyRelationshipTypeId", "FRIEND");
                    dispatcher.runSync("createPartyRelationship", createPartyRelationMap);
                }

                int max = 7;
                int min = 1;

                Random random = new Random();
                int randomNumber = random.nextInt(max) % (max - min + 1) + min;
                //创建购物记录
                GenericValue shopingList = EntityQuery.use(delegator).from("ShoppingList").where("partyId", nextPartyId, "listName", resourcesName).queryFirst();

                if (null == shopingList) {


                    Map<String, Object> createShoppingListMap = new HashMap<String, Object>();
                    createShoppingListMap.put("userLogin", admin);
                    createShoppingListMap.put("listName", resourcesName);
                    createShoppingListMap.put("description", nextUserName + getRandomActionName(randomNumber));
                    createShoppingListMap.put("shoppingListTypeId", "SLT_FREQ_PURCHASES");
                    createShoppingListMap.put("isPublic", "Y");
                    createShoppingListMap.put("partyId", nextPartyId);

                    dispatcher.runSync("createShoppingList", createShoppingListMap);
                }

            }


        } else {
            return resultMap;
        }

        resultMap.put("resourcesName", resourcesName);
        return resultMap;
    }


    /**
     * jscode2session
     *
     * @param request
     * @param response
     * @return
     * @throws GenericServiceException
     * @throws GenericEntityException
     */
    public static String jscode2session(HttpServletRequest request, HttpServletResponse response)
            throws GenericServiceException, GenericEntityException {

        // Servlet Head

        Locale locale = UtilHttp.getLocale(request);

        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");

        Delegator delegator = (Delegator) request.getAttribute("delegator");

        HttpSession session = request.getSession();

        String code = (String) request.getParameter("code");


        String responseStr2 = sendGet(PeConstant.WECHAT_MINI_PROGRAM_SNS_PATH,
                "appid=" + PeConstant.WECHAT_MINI_PROGRAM_APP_ID +
                        "&secret=" + PeConstant.WECHAT_MINI_PROGRAM_APP_SECRET_ID +
                        "&js_code=" + code + "&grant_type=authorization_code");

        JSONObject jsonMap2 = JSONObject.fromObject(responseStr2);

        String unionid = (String) jsonMap2.get("unionid");

        System.out.println("*MiniProgramm-jscode2session jsonMap2 = " + jsonMap2);
        System.out.println("*unionid = " + unionid);
        request.setAttribute("unionid", unionid);

        return "success";
    }

}
