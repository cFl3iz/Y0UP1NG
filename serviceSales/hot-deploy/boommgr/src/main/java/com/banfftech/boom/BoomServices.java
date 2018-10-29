package main.java.com.banfftech.boom;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import main.java.com.banfftech.personmanager.PersonManagerQueryServices;
import main.java.com.banfftech.personmanager.PersonManagerServices;
import main.java.com.banfftech.platformmanager.common.PlatformLoginWorker;
import org.apache.ofbiz.base.util.Debug;

import main.java.com.banfftech.platformmanager.util.HttpHelper;
import org.apache.ofbiz.base.util.UtilDateTime;
import org.apache.ofbiz.base.util.GeneralException;
import org.apache.ofbiz.base.util.ObjectType;
import org.apache.ofbiz.entity.condition.EntityExpr;
import main.java.com.banfftech.platformmanager.aliyun.util.HttpUtils;
import main.java.com.banfftech.platformmanager.constant.PeConstant;
import main.java.com.banfftech.platformmanager.util.EmojiHandler;
import main.java.com.banfftech.platformmanager.util.UtilTools;
import main.java.com.banfftech.platformmanager.wechat.AccessToken;
import main.java.com.banfftech.platformmanager.wechat.WeChatUtil;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
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
import org.apache.ofbiz.order.order.OrderChangeHelper;
import org.apache.ofbiz.entity.util.EntityUtilProperties;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.entity.GenericEntity;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.base.util.UtilHttp;
import org.apache.ofbiz.service.ModelService;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.rmi.CORBA.Util;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;


import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;

import main.java.com.banfftech.platformmanager.oss.OSSUnit;


import net.sf.json.JSONArray;
import org.json.JSONObject;
import org.omg.CORBA.portable.Delegate;
import sun.net.www.content.text.Generic;
import sun.security.krb5.Config;

import static main.java.com.banfftech.boom.BoomQueryServices.getMyGroup;
import static main.java.com.banfftech.personmanager.PersonManagerQueryServices.queryPersonBaseInfo;
import static main.java.com.banfftech.platformmanager.common.PlatformLoginWorker.getToken;
import static main.java.com.banfftech.platformmanager.common.PlatformLoginWorker.module;
import static main.java.com.banfftech.platformmanager.wechat.WeChatUtil.getAccessToken;
import static main.java.com.banfftech.wechatminiprogram.WeChatMiniProgramServices.updateProductBizData;
import static main.java.com.banfftech.wechatminiprogram.WeChatMiniProgramServices.updateProductBizDataFromOrder;

/**
 * Created by S on 2018/8/29.
 */
public class BoomServices {


    public final static String module = BoomServices.class.getName();

    private static String createGroup(Delegator delegator, LocalDispatcher dispatcher, GenericValue admin, String vender, String venderLocal) throws GenericEntityException, GenericServiceException {
        String partyId = (String) delegator.getNextSeqId("Party");
        Map<String, Object> serviceResultByCreatePartyMap = dispatcher.runSync("createPartyGroup",
                UtilMisc.toMap("userLogin", admin, "groupName", vender, "groupNameLocal", venderLocal));
        partyId = (String) serviceResultByCreatePartyMap.get("partyId");
        //Grant Role
        dispatcher.runSync("createPartyRole",
                UtilMisc.toMap("userLogin", admin, "partyId", partyId, "roleTypeId", "INTERNAL_ORGANIZATIO"));
        dispatcher.runSync("createPartyRole",
                UtilMisc.toMap("userLogin", admin, "partyId", partyId, "roleTypeId", "MANUFACTURER"));
        dispatcher.runSync("createPartyRole",
                UtilMisc.toMap("userLogin", admin, "partyId", partyId, "roleTypeId", "VENDOR"));
        dispatcher.runSync("createPartyRole",
                UtilMisc.toMap("userLogin", admin, "partyId", partyId, "roleTypeId", "CUSTOMER"));
        dispatcher.runSync("createPartyRole",
                UtilMisc.toMap("userLogin", admin, "partyId", partyId, "roleTypeId", "LEAD"));
        dispatcher.runSync("createPartyRole",
                UtilMisc.toMap("userLogin", admin, "partyId", partyId, "roleTypeId", "ACCOUNT_LEAD"));
        // 角色
        dispatcher.runSync("createPartyRole",
                UtilMisc.toMap("userLogin", admin, "partyId", partyId, "roleTypeId", "OWNER"));
        dispatcher.runSync("createPartyRole",
                UtilMisc.toMap("userLogin", admin, "partyId", partyId, "roleTypeId", "ADMIN"));



        String userLoginId = delegator.getNextSeqId("UserLogin");
        // Create UserLogin Block
        Map<String, Object> createUserLoginInMap = UtilMisc.toMap("userLogin", admin, "userLoginId",
                userLoginId, "partyId", partyId, "currentPassword", "ofbiz",
                "currentPasswordVerify", "ofbiz", "enabled", "Y");
        Map<String, Object> createUserLogin = dispatcher.runSync("createUserLogin", createUserLoginInMap);


        // Grant Permission Block
        Map<String, Object> userLoginSecurityGroupInMap = UtilMisc.toMap("userLogin", admin, "userLoginId",
                userLoginId, "groupId", "FULLADMIN");
        dispatcher.runSync("addUserLoginToSecurityGroup", userLoginSecurityGroupInMap);

        return partyId;
    }


    public static Map<String, Object> removeDeliveryPlan(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException, UnsupportedEncodingException {
        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> result = ServiceUtil.returnSuccess();
        GenericValue userLogin = (GenericValue) context.get("userLogin");

        Locale locale = (Locale) context.get("locale");

        String date = (String) context.get("date");
        String productId = (String) context.get("productId");
        String enumId = (String) context.get("enumId");


        String partyId = userLogin.getString("partyId");
        Map<String,Object> myGroup = getMyGroup(delegator,partyId);
        String partyGroupId = (String) myGroup.get("partyId");

        //已经存在就不添加了
        GenericValue deliveryPlansItem =EntityQuery.use(delegator).from("DeliveryPlansItem").where(
                "planId", partyGroupId + "/" + date,"productId",productId,"enumId",enumId).queryFirst();
        if(deliveryPlansItem!=null){
            deliveryPlansItem.remove();
        }

//        List<GenericValue> planDataList = EntityQuery.use(delegator).from("DeliveryPlans").where(
//                "planId", key).queryList();
//        if(planDataList!=null){
//            for(GenericValue gv : planDataList){
//                gv.remove();
//            }
//        }
        return result;
    }


    public static Map<String, Object> updateDeliveryPlanItem(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException, UnsupportedEncodingException {
        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> result = ServiceUtil.returnSuccess();
        GenericValue userLogin = (GenericValue) context.get("userLogin");

        Locale locale = (Locale) context.get("locale");

        String productId = (String) context.get("productId");
        String date = (String) context.get("date");
        String quantity = (String) context.get("quantity");
        String enumId = (String) context.get("enumId");

        String partyId = userLogin.getString("partyId");
        Map<String, Object> myGroup = getMyGroup(delegator, partyId);
        String partyGroupId = (String) myGroup.get("partyId");

        GenericValue gv = EntityQuery.use(delegator).from("DeliveryPlansItem").where(
                "planId", partyGroupId + "/" + date, "productId", productId,"enumId",enumId).queryFirst();
        gv.set("quantity",quantity);
        gv.store();
        return result;
    }

    /**
     * createDeliveryPlan
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     * @throws UnsupportedEncodingException
     */
    public static Map<String, Object> createDeliveryPlan(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException, UnsupportedEncodingException {
        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> result = ServiceUtil.returnSuccess();
        GenericValue userLogin = (GenericValue) context.get("userLogin");

        Locale locale = (Locale) context.get("locale");

        String productId = (String) context.get("productId");
        String date = (String) context.get("date");
        String quantity = (String) context.get("quantity");
        String enumId = (String) context.get("enumId");
        String fromDate = (String) context.get("fromDate");

        String partyId = userLogin.getString("partyId");
        Map<String,Object> myGroup = getMyGroup(delegator,partyId);
        String partyGroupId = (String) myGroup.get("partyId");

        //已经存在就不添加了
        if(EntityQuery.use(delegator).from("DeliveryPlansItem").where(
                "planId", partyGroupId + "/" + date,"productId",productId,"enumId",enumId).queryFirst()!=null){
            return result;
        }


        // 选择了一个日期
        if(UtilValidate.isNotEmpty(fromDate) && !fromDate.equals("null")  ) {

        }else{
            String planId = partyGroupId + "/" + date;
            // Create New Plan
            GenericValue plan = EntityQuery.use(delegator).from("DeliveryPlans").where(
                    "planId", planId).queryFirst();
            if(null==plan){
                Map<String,Object> createMap = new HashMap<String, Object>();
                            createMap.put("planId", planId);
                            createMap.put("payToParty", partyGroupId);
                            createMap.put("createByParty", partyId);
                            createMap.put("fromDate", org.apache.ofbiz.base.util.UtilDateTime.nowTimestamp());
                            GenericValue createEntity = delegator.makeValue("DeliveryPlans", createMap);
                            createEntity.create();
            }
            GenericValue product = delegator.findOne("Product", UtilMisc.toMap("productId", productId), false);
            String uomId = product.getString("quantityUomId");
            String productName = product.getString("productName");
            GenericValue uom =  EntityQuery.use(delegator).from("Uom").where(
                    "uomId", uomId).queryFirst();
            String uomDescription = uom.getString("description");


            // Added Item
            Map<String,Object> createMap = new HashMap<String, Object>();
            createMap.put("planId", planId);
            createMap.put("payToParty", partyGroupId);
            createMap.put("productId", productId);
            createMap.put("productName", productName);
            createMap.put("createByParty", partyId);
            createMap.put("seq", (String) delegator.getNextSeqId("DeliveryPlansItem"));
            createMap.put("enumId", enumId);
            createMap.put("uomDescription", uomDescription);
            createMap.put("quantity", quantity);
            createMap.put("fromDate", org.apache.ofbiz.base.util.UtilDateTime.nowTimestamp());
            GenericValue createEntity = delegator.makeValue("DeliveryPlansItem", createMap);
            createEntity.create();
        }

//        JSONArray array = JSONArray.fromObject(planData);
//
//        String planKey = org.apache.ofbiz.base.util.UtilDateTime.nowTimestamp()+":"+partyId;
//
//        if (null != array) {
//
//            for(int i=0 ; i < array.size() ;i++){
//                    net.sf.json.JSONObject jsonObj = array.getJSONObject(i);
//                    String enumId   = jsonObj.getString("enumId");
//                    JSONArray innerArray = (JSONArray) jsonObj.get("products");
//
//                    if (null != innerArray) {
//                        for(int y=0 ; y < innerArray.size() ;y++){
//                            net.sf.json.JSONObject innerJsonObj = innerArray.getJSONObject(y);
//                            String productId   = innerJsonObj.getString("productId");
//                            String uomDescription   = innerJsonObj.getString("uomDescription");
//                            String productName   = innerJsonObj.getString("productName");
//                            String quantity   = innerJsonObj.getString("quantity");
//                            Map<String,Object> createMap = new HashMap<String, Object>();
//                            createMap.put("productId", productId);
//                            createMap.put("planKey", planKey);
//                            createMap.put("uomDescription", uomDescription);
//                            createMap.put("productName", productName);
//                            createMap.put("enumId", enumId);
//                            createMap.put("quantity", quantity);
//                            createMap.put("outQuantity", "0");
//                            createMap.put("payToParty", partyGroupId);
//                            createMap.put("createByParty", partyId);
//                            createMap.put("fromDate", org.apache.ofbiz.base.util.UtilDateTime.nowTimestamp());
//                            createMap.put("dpId", (String) delegator.getNextSeqId("DeliveryPlan"));
//
//                            GenericValue createEntity = delegator.makeValue("DeliveryPlan", createMap);
//                            createEntity.create();
//                        }
//                    }
//
//
//                }
//
//        }

        return result;
    }




    public static Map<String, Object> addEmp(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException, UnsupportedEncodingException {
        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> result = ServiceUtil.returnSuccess();
        GenericValue userLogin = (GenericValue) context.get("userLogin");

        Locale locale = (Locale) context.get("locale");

        String partyGroupId = (String) context.get("partyGroupId");

        String partyId = userLogin.getString("partyId");

        Map<String,Object> createPartyRelationshipInMap = new HashMap<String, Object>();
        createPartyRelationshipInMap.put("roleTypeIdFrom", "_NA_");
        createPartyRelationshipInMap.put("roleTypeIdTo", "_NA_");
//                createPartyRelationshipInMap.put("userLogin", admin);
        createPartyRelationshipInMap.put("partyIdFrom", partyGroupId);
        createPartyRelationshipInMap.put("partyIdTo",partyId );
        createPartyRelationshipInMap.put("partyRelationshipTypeId",  "EMPLOYMENT");
        createPartyRelationshipInMap.put("fromDate", org.apache.ofbiz.base.util.UtilDateTime.nowTimestamp());


        GenericValue partyRelationship = delegator.makeValue("PartyRelationship", createPartyRelationshipInMap);

        partyRelationship.create();


        return result;
    }


    /**
     * addedOrderNote
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     * @throws UnsupportedEncodingException
     */
    public static Map<String, Object> addedOrderNote(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException, UnsupportedEncodingException {
        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> result = ServiceUtil.returnSuccess();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));
        Locale locale = (Locale) context.get("locale");

        String orderId = (String) context.get("orderId");
        String note = (String) context.get("note");

        String partyId = userLogin.getString("partyId");

        Map<String, Object> createOrderNoteOut = dispatcher.runSync("createOrderNote", UtilMisc.toMap("userLogin", admin, "orderId",
                orderId, "noteName", "供应商行为", "note", note, "internalNote", "N"));

        return result;
    }


    public static Map<String, Object> addProductionTemp(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException, UnsupportedEncodingException {
        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> result = ServiceUtil.returnSuccess();
        GenericValue userLogin = (GenericValue) context.get("userLogin");

        Locale locale = (Locale) context.get("locale");

        String orderId = (String) context.get("orderId");

        String partyId = userLogin.getString("partyId");
//        GenericValue relation = EntityQuery.use(delegator).from("PartyRelationship").where(
//                "partyIdFrom", partyId, "partyRelationshipTypeId", "OWNER" ).queryFirst();
//
//        String partyGroupId = relation.getString("partyIdTo");

        Map<String,Object> myGroup = getMyGroup(delegator,partyId);
        String partyGroupId = (String) myGroup.get("partyId");

        GenericValue facility =  EntityQuery.use(delegator).from("Facility").where(
                "ownerPartyId", partyGroupId ).queryFirst();

        String facilityId = facility.getString("facilityId");


        List<GenericValue> orderItems =  EntityQuery.use(delegator).from("OrderItem").where(
                "orderId", orderId).queryList();

//        for(GenericValue item : orderItems){
//            String productId = item.getString("productId");
//            GenericValue product = EntityQuery.use(delegator).from("Product").where("productId", productId).queryFirst();
//            BigDecimal quantity = (BigDecimal) item.get("quantity");
//
//            GenericValue  productionTemp =   EntityQuery.use(delegator).from("ProductionTemp").where(
//                    "productId", productId,"facilityId",facilityId).queryFirst();
//            if(productionTemp!=null){
//                productionTemp.set("count",(Integer.parseInt(""+productionTemp.get("count"))+ quantity.intValue())+"" );
//                productionTemp.store();
//            }else{
//                productionTemp = delegator.makeValue("ProductionTemp", UtilMisc.toMap());
//                productionTemp.set("tempId",(String) delegator.getNextSeqId("ProductionTemp"));
//                productionTemp.set("count",quantity.intValue()+"");
//                productionTemp.set("productId",productId);
//                productionTemp.set("facilityId",facilityId);
//                productionTemp.set("productName",product.getString("productName"));
//                productionTemp.set("type","FINISHED_GOOD");
//                productionTemp.set("detailImage",product.getString("detailImageUrl"));
//                productionTemp.create();
//            }
//
//
//        }


        return result;
    }


    public static Map<String, Object> removeMyLead(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException, UnsupportedEncodingException {
        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> result = ServiceUtil.returnSuccess();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        Locale locale = (Locale) context.get("locale");

        String leadId = (String) context.get("leadId");

        String partyId = (String) userLogin.get("partyId");


        GenericValue relation = EntityQuery.use(delegator).from("PartyRelationship").where(
                "partyIdFrom", partyId, "roleTypeIdTo", "LEAD", "partyRelationshipTypeId", "LEAD_OWNER", "partyIdTo", leadId).queryFirst();
        if (null != relation) {
            relation.remove();
//            dispatcher.runSync("deletePartyRelationship",UtilMisc.toMap("userLogin",userLogin,
//                    "partyIdFrom",partyId,"partyIdTo",leadId,"fromDate",relation.get("fromDate"),"roleTypeIdTo", "LEAD"));
        }


        return result;
    }


    public static Map<String, Object> viewedOrder(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException, UnsupportedEncodingException {
        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> result = ServiceUtil.returnSuccess();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        Locale locale = (Locale) context.get("locale");

        String orderId = (String) context.get("orderId");


        //下单的当事人,创建服务会检查他有没有创建权限等。
        String partyId = (String) userLogin.get("partyId");

        GenericValue  order =  EntityQuery.use(delegator).from("OrderHeader").where("orderId",orderId).queryFirst();

        order.set("isViewed","Y");

        order.store();

        return result;
    }




    public static Map<String, Object> updateOrderStatus(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException, UnsupportedEncodingException {
        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> result = ServiceUtil.returnSuccess();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        Locale locale = (Locale) context.get("locale");

        String orderId = (String) context.get("orderId");
        String statusId = (String) context.get("statusId");


        //下单的当事人,创建服务会检查他有没有创建权限等。
        String partyId = (String) userLogin.get("partyId");


        dispatcher.runSync("updateOrderHeader", UtilMisc.toMap("userLogin", userLogin,
                "orderId", orderId, "statusId", statusId));

        return result;
    }


    /**
     * create PurchaseOrder
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     * @throws UnsupportedEncodingException
     */
    public static Map<String, Object> createPurchaseOrder(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException, UnsupportedEncodingException {
        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> result = ServiceUtil.returnSuccess();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        Locale locale = (Locale) context.get("locale");

        String itemArray = (String) context.get("itemArray");

        String workEffortId = (String) context.get("workEffortId");

        String autoApproveOrder = (String) context.get("autoApproveOrder");

        //下单的当事人,创建服务会检查他有没有创建权限等。
        String partyId = (String) userLogin.get("partyId");


        Map<String,Object> myGroup = getMyGroup(delegator,partyId);
        String partyGroupId = (String) myGroup.get("partyId");
        partyId = partyGroupId;

        if (null != itemArray) {
            for (String rowStr : itemArray.split(",")) {
                String productId = rowStr.substring(0, rowStr.indexOf(":"));
                String quantityStr = rowStr.substring(rowStr.indexOf(":") + 1, rowStr.lastIndexOf(":"));
                String supplierPartyId = rowStr.substring(rowStr.lastIndexOf(":") + 1, rowStr.indexOf("/"));
                String beiZhu = rowStr.substring(rowStr.indexOf("/") + 1);

                //供应商产品仓库暂无
//                String originFacilityId = (String) context.get("originFacilityId");

                //供应商
                String billFromVendorPartyId = supplierPartyId;
                // Quantity  Amount
                BigDecimal quantity = new BigDecimal(quantityStr);

                //最终客户、收货客户、意向客户等客户当事人
                String billToCustomerPartyId, endUserCustomerPartyId, placingCustomerPartyId, shipToCustomerPartyId = partyId;
                //发货人就是供应商
                String shipFromVendorPartyId = billFromVendorPartyId;

                //StoreOrderMap
                Map<String, Object> createOrderServiceIn = new HashMap<String, Object>();

                //Order Items
                List<GenericValue> orderItemList = new ArrayList<GenericValue>();

                GenericValue product = EntityQuery.use(delegator).from("Product").where("productId", productId).queryFirst();
                GenericValue itemProduct = delegator.makeValue("OrderItem", UtilMisc.toMap());

                itemProduct.set("productId", productId);
                itemProduct.set("shipBeforeDate", null);
//        itemProduct.set("productCategoryId", productCategoryId);
                // Unit Price = List Price
                itemProduct.set("shoppingListId", null);
                itemProduct.set("cancelBackOrderDate", null);
                // Desc To Order Item List
                itemProduct.set("itemDescription", product.get("productName"));
                itemProduct.set("selectedAmount", BigDecimal.ZERO);
                itemProduct.set("orderItemTypeId", PeConstant.ORDER_ITEM_TYPE);
                itemProduct.set("orderItemSeqId", "00001");


                itemProduct.set("unitPrice", BigDecimal.ZERO);
                itemProduct.set("isModifiedPrice", "N");
                itemProduct.set("unitListPrice", BigDecimal.ZERO);


                itemProduct.set("quantity", quantity);
                itemProduct.set("comments", null);
                itemProduct.set("statusId", PeConstant.ORDER_ITEM_APPROVED_STATUS_ID);
                itemProduct.set("quoteItemSeqId", null);
                itemProduct.set("externalId", null);
                itemProduct.set("supplierProductId", null);
//        itemProduct.set("prodCatalogId", prodCatalogId);


                System.out.println("->itemProduct:" + itemProduct);

                orderItemList.add(itemProduct);

                createOrderServiceIn.put("currencyUom", PeConstant.DEFAULT_CURRENCY_UOM_ID);
                createOrderServiceIn.put("orderName", "PURCHASE_" + partyId + "_TO_" + billFromVendorPartyId + "_BUY_" + productId);
                createOrderServiceIn.put("orderItems", orderItemList);
                createOrderServiceIn.put("orderTypeId", PeConstant.PURCHASE_ORDER);
                createOrderServiceIn.put("partyId", partyId);
                createOrderServiceIn.put("billFromVendorPartyId", billFromVendorPartyId);
//                createOrderServiceIn.put("productStoreId", productStoreId);
//                createOrderServiceIn.put("originFacilityId", originFacilityId);

                createOrderServiceIn.put("billToCustomerPartyId", partyId);
                createOrderServiceIn.put("endUserCustomerPartyId", partyId);
                createOrderServiceIn.put("placingCustomerPartyId", partyId);
                createOrderServiceIn.put("shipToCustomerPartyId", partyId);

                createOrderServiceIn.put("supplierPartyId", billFromVendorPartyId);
                createOrderServiceIn.put("shipFromVendorPartyId", billFromVendorPartyId);
                createOrderServiceIn.put("userLogin", admin);


                //Do Run Service
                Map<String, Object> createOrderOut = dispatcher.runSync("storeOrder", createOrderServiceIn);

                if (!ServiceUtil.isSuccess(createOrderOut)) {
                    return createOrderOut;
                }


                Map<String, Object> createOrderNoteOut = dispatcher.runSync("createOrderNote", UtilMisc.toMap("userLogin", admin, "orderId",
                        createOrderOut.get("orderId"), "noteName", "采购商备注", "note", beiZhu, "internalNote", "N"));


                if(autoApproveOrder!=null && autoApproveOrder.equals("Y")){
                    GenericValue orderHeader = delegator.findOne("OrderHeader", UtilMisc.toMap("orderId", createOrderOut.get("orderId")+""), false);

//                    GenericValue relation = EntityQuery.use(delegator).from("PartyRelationship").where(
//                            "partyIdFrom", partyId, "partyRelationshipTypeId", "OWNER" ).queryFirst();
//                    String partyGroupId = relation.getString("partyIdTo");

//                    Map<String,Object> myGroup = getMyGroup(delegator,partyId);
//                    String partyGroupId = (String) myGroup.get("partyId");

                    GenericValue productStoreRole = EntityQuery.use(delegator).from("ProductStoreRole").where("partyId",partyGroupId, "roleTypeId", "ADMIN").queryFirst();
                    if(null == productStoreRole){
                        //create
                        // 创建店铺
                        Map<String, Object> createPersonStoreOutMap = dispatcher.runSync("createProductStore", UtilMisc.toMap("userLogin", admin,
                                "payToPartyId", partyId, "storeName", partyGroupId+"Store"
                                ,"title","autoCreateFrom Order", "defaultCurrencyUomId", PeConstant.DEFAULT_CURRENCY_UOM_ID, "reserveInventory", "Y"));
                        String productStoreId = (String) createPersonStoreOutMap.get("productStoreId");
                        // 关联店铺角色
                        Map<String, Object> createProductStoreRoleOutMap = dispatcher.runSync("createProductStoreRole", UtilMisc.toMap("userLogin", admin,
                                "partyId", partyGroupId, "productStoreId", productStoreId, "roleTypeId", "ADMIN"));
                        orderHeader.set("productStoreId",""+productStoreId);
                    }else{
                        orderHeader.set("productStoreId",""+productStoreRole.get("productStoreId"));
                    }
                    orderHeader.store();

                    dispatcher.runSync("updateOrderHeader", UtilMisc.toMap("userLogin", userLogin,
                            "orderId", createOrderOut.get("orderId")+"", "statusId", "ORDER_APPROVED"));
//                    OrderChangeHelper.approveOrder(dispatcher, userLogin, createOrderOut.get("orderId")+"");

                }

            }

            Map<String,Object> changeStatusRun =  dispatcher.runSync("changeProductionRunStatus", UtilMisc.toMap("userLogin", admin, "productionRunId", workEffortId, "statusId", "PRUN_DOC_PRINTED"));
            Debug.logInfo("changeStatusRun=>"+changeStatusRun,module);
        }


//        String orderId = (String) createOrderOut.get("orderId");


        return result;
    }


    /**
     * 企业Bom注册
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     * @throws UnsupportedEncodingException
     */
    public static Map<String, Object> register(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException, UnsupportedEncodingException {
        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> result = ServiceUtil.returnSuccess();
        GenericValue userLogin = null;
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        Locale locale = (Locale) context.get("locale");

        String unioId = (String) context.get("unioId");
        String fromPartyGroupId = (String) context.get("partyGroupId");
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

        String organizationName = (String) context.get("organizationName");
        String name = (String) context.get("name");


        if (captcha != null && !captcha.trim().equals("")) {
            boolean checkCaptcha = false;
            checkCaptcha = PlatformLoginWorker.checkCaptchaIsRight(delegator, captcha, userLogin, tel, locale);
            Debug.logInfo("tel:" + tel + ",checkCaptcha=>" + checkCaptcha, module);

            if (checkCaptcha) {









                // Check is Exsit Contact Number ?..
                List<GenericValue> teleContact = EntityQuery.use(delegator).from("TelecomNumberAndPartyAndRelationshipView").where(
                        "contactNumber", tel, "roleTypeIdTo", "LEAD", "partyRelationshipTypeId", "LEAD_OWNER", "contactMechTypeId", "TELECOM_NUMBER").queryList();


                userLogin = PersonManagerServices.justCreatePartyPersonUserLogin(delegator, dispatcher, name, nickName);

                String partyId = (String) userLogin.getString("partyId");

                // 角色
                dispatcher.runSync("createPartyRole",
                        UtilMisc.toMap("userLogin", admin, "partyId", partyId, "roleTypeId", "OWNER"));
                dispatcher.runSync("createPartyRole",
                        UtilMisc.toMap("userLogin", admin, "partyId", partyId, "roleTypeId", "ADMIN"));

                dispatcher.runSync("createPartyRole",
                        UtilMisc.toMap("userLogin", admin, "partyId", partyId, "roleTypeId", "WORKER"));
                dispatcher.runSync("createPartyRole",
                        UtilMisc.toMap("userLogin", admin, "partyId", partyId, "roleTypeId", "LEAD"));
                dispatcher.runSync("createPartyRole",
                        UtilMisc.toMap("userLogin", admin, "partyId", partyId, "roleTypeId", "ACCOUNT_LEAD"));


                // 接收账单的客户是否拥有
                GenericValue partyCustRole = EntityQuery.use(delegator).from("PartyRole").where("partyId", partyId, "roleTypeId", "BILL_TO_CUSTOMER").queryFirst();
                if (null == partyCustRole) {
                    Map<String, Object> createCustPartyRoleMap = UtilMisc.toMap("userLogin", admin, "partyId", partyId,
                            "roleTypeId", "BILL_TO_CUSTOMER");
                    dispatcher.runSync("createPartyRole", createCustPartyRoleMap);
                }

                // 收货的客户是否拥有
                GenericValue partyCustShipRole = EntityQuery.use(delegator).from("PartyRole").where("partyId", partyId, "roleTypeId", "SHIP_TO_CUSTOMER").queryFirst();
                if (null == partyCustShipRole) {
                    Map<String, Object> createPartyCustShipRoleMap = UtilMisc.toMap("userLogin", admin, "partyId", partyId,
                            "roleTypeId", "SHIP_TO_CUSTOMER");
                    dispatcher.runSync("createPartyRole", createPartyCustShipRoleMap);
                }

                //最终客户角色是否拥有
                GenericValue partyEndCustRole = EntityQuery.use(delegator).from("PartyRole").where("partyId", partyId, "roleTypeId", "END_USER_CUSTOMER").queryFirst();
                if (null == partyEndCustRole) {
                    Map<String, Object> createPartyEndCustRoleMap = UtilMisc.toMap("userLogin", admin, "partyId", partyId,
                            "roleTypeId", "END_USER_CUSTOMER");
                    dispatcher.runSync("createPartyRole", createPartyEndCustRoleMap);
                }
                GenericValue partyCUSTOMERRole = EntityQuery.use(delegator).from("PartyRole").where("partyId", partyId, "roleTypeId", "CUSTOMER").queryFirst();
                if (null == partyCUSTOMERRole) {
                    Map<String, Object> createPartyCUSTOMERRoleMap = UtilMisc.toMap("userLogin", admin, "partyId", partyId,
                            "roleTypeId", "CUSTOMER");
                    dispatcher.runSync("createPartyRole", createPartyCUSTOMERRoleMap);
                }

                //是通过转发进来的
                if(null!=fromPartyGroupId&&fromPartyGroupId.length()>3&& !fromPartyGroupId.trim().equals("null")){
                    Map<String,Object>   createPartyRelationshipInMap = new HashMap<String, Object>();
                    createPartyRelationshipInMap.put("roleTypeIdFrom", "_NA_");
                    createPartyRelationshipInMap.put("roleTypeIdTo", "_NA_");
                    createPartyRelationshipInMap.put("partyIdFrom", fromPartyGroupId);
                    createPartyRelationshipInMap.put("partyIdTo",partyId );
                    createPartyRelationshipInMap.put("partyRelationshipTypeId",  "EMPLOYMENT");
                    createPartyRelationshipInMap.put("fromDate", org.apache.ofbiz.base.util.UtilDateTime.nowTimestamp());
                    Debug.logInfo("createPartyRelationshipInMap:"+createPartyRelationshipInMap,module);

                    GenericValue partyRelationship = delegator.makeValue("PartyRelationship", createPartyRelationshipInMap);
                    partyRelationship.create();




                    // 创建联系电话
                    Map<String, Object> inputTelecom = UtilMisc.toMap();
                    inputTelecom.put("partyId", partyId);
                    inputTelecom.put("contactNumber", tel);
                    inputTelecom.put("contactMechTypeId", "TELECOM_NUMBER");
                    inputTelecom.put("contactMechPurposeTypeId", "PHONE_MOBILE");
                    inputTelecom.put("userLogin", admin);
                    Map<String, Object> createTelecom = dispatcher.runSync("createPartyTelecomNumber", inputTelecom);

                    Map<String, String> userInfoMap = new HashMap<String, String>();
                    userInfoMap.put("nickname", nickName);
                    userInfoMap.put("sex", gender);
                    userInfoMap.put("language", language);
                    userInfoMap.put("headimgurl", avatarUrl);
                    //注册后端流程
                    main.java.com.banfftech.platformmanager.common.PlatformLoginWorker.updatePersonAndIdentificationLanguage(appId, admin, partyId, delegator, openId, userInfoMap, userLogin, dispatcher);


                    if (city != null && !city.equals("") && province != null) {


                        //  邮政地址
                        String contactMechPurposeTypeId = "POSTAL_ADDRESS";
                        Map<String, Object> createPartyPostalAddressOutMap = dispatcher.runSync("createPartyPostalAddress",
                                UtilMisc.toMap("userLogin", admin, "toName", name, "partyId", partyId, "countryGeoId", PeConstant.DEFAULT_GEO_COUNTRY, "city", city, "address1", province + "-" + country, "address2", city, "postalCode", PeConstant.DEFAULT_POST_CODE
                                ));
                        String contactMechId = (String) createPartyPostalAddressOutMap.get("contactMechId");
                        if (!ServiceUtil.isSuccess(createPartyPostalAddressOutMap)) {
                            return createPartyPostalAddressOutMap;
                        }
                    }
                    result.put("tarjeta", getToken(userLogin.getString("userLoginId"), delegator));
                    result.put("partyId", partyId);
                    result.put("userInfo", PersonManagerQueryServices.queryPersonBaseInfo(delegator, partyId));

                    result.put("partyGroupId", fromPartyGroupId);

                    return result;
                }


                String groupId = createGroup(delegator, dispatcher, admin, organizationName, "");

                dispatcher.runSync("createPartyAcctgPreference", UtilMisc.toMap("userLogin", admin
                        , "partyId", groupId, "baseCurrencyUomId", "CNY"));


                // ToDo Merge Party、Person、UserLogin Relationship ....
                // Create Emp to PartyGroup From Lead
                if (teleContact.size() > 0) {
                    for (GenericValue row : teleContact) {

                        String beforePartyId = row.getString("partyId");
//PARTYIDTO

                        GenericValue relation = EntityQuery.use(delegator).from("PartyRelationship").where(
                                "partyIdTo", beforePartyId, "partyRelationshipTypeId", "LEAD_OWNER").queryFirst();
                        if (relation != null) {
                            String partyIdFrom = relation.getString("partyIdFrom");

                            mergeChangeRelation(delegator, dispatcher, admin, partyIdFrom, beforePartyId, groupId);
                            relation.remove();
                            mergeChangeOrder(delegator, dispatcher, admin, partyIdFrom, beforePartyId, groupId);
                            mergeProductsSupplier(delegator,dispatcher,admin,beforePartyId,groupId);
                            mergeAliasForg(delegator, partyIdFrom,beforePartyId,groupId);
                        }


                    }
                }


                Map<String, Object> createPartyRelationshipInMap = new HashMap<String, Object>();

                createPartyRelationshipInMap.put("userLogin", admin);
                createPartyRelationshipInMap.put("roleTypeIdTo", "ADMIN");
                createPartyRelationshipInMap.put("roleTypeIdFrom", "ADMIN");
                createPartyRelationshipInMap.put("partyIdFrom", partyId);
                createPartyRelationshipInMap.put("partyIdTo", groupId);
                createPartyRelationshipInMap.put("partyRelationshipTypeId", "OWNER");
                Map<String, Object> createPartyRelationshipOutMap = dispatcher.runSync("createPartyRelationship", createPartyRelationshipInMap);
                if (ServiceUtil.isError(createPartyRelationshipOutMap)) {
                    return createPartyRelationshipOutMap;
                }

                //EMPLOYMENT
//                createPartyRelationshipInMap = new HashMap<String, Object>();
//
//                createPartyRelationshipInMap.put("userLogin", admin);
//                createPartyRelationshipInMap.put("roleTypeIdTo", "_NA_");
//                createPartyRelationshipInMap.put("roleTypeIdFrom", "_NA_");
//                createPartyRelationshipInMap.put("partyIdFrom", partyId);
//                createPartyRelationshipInMap.put("partyIdTo", groupId);
//                createPartyRelationshipInMap.put("partyRelationshipTypeId", "EMPLOYMENT");
//                  createPartyRelationshipOutMap = dispatcher.runSync("createPartyRelationship", createPartyRelationshipInMap);
//                if (ServiceUtil.isError(createPartyRelationshipOutMap)) {
//                    return createPartyRelationshipOutMap;
//                }

             createPartyRelationshipInMap = new HashMap<String, Object>();
                createPartyRelationshipInMap.put("roleTypeIdFrom", "_NA_");
                createPartyRelationshipInMap.put("roleTypeIdTo", "_NA_");
//                createPartyRelationshipInMap.put("userLogin", admin);
                createPartyRelationshipInMap.put("partyIdFrom", groupId);
              createPartyRelationshipInMap.put("partyIdTo",partyId );
                createPartyRelationshipInMap.put("partyRelationshipTypeId",  "EMPLOYMENT");
                createPartyRelationshipInMap.put("fromDate", org.apache.ofbiz.base.util.UtilDateTime.nowTimestamp());


                GenericValue partyRelationship = delegator.makeValue("PartyRelationship", createPartyRelationshipInMap);

                partyRelationship.create();



                // 创建联系电话
                Map<String, Object> inputTelecom = UtilMisc.toMap();
                inputTelecom.put("partyId", partyId);
                inputTelecom.put("contactNumber", tel);
                inputTelecom.put("contactMechTypeId", "TELECOM_NUMBER");
                inputTelecom.put("contactMechPurposeTypeId", "PHONE_MOBILE");
                inputTelecom.put("userLogin", admin);
                Map<String, Object> createTelecom = dispatcher.runSync("createPartyTelecomNumber", inputTelecom);

                Map<String, String> userInfoMap = new HashMap<String, String>();
                userInfoMap.put("nickname", nickName);
                userInfoMap.put("sex", gender);
                userInfoMap.put("language", language);
                userInfoMap.put("headimgurl", avatarUrl);
                //注册后端流程
                main.java.com.banfftech.platformmanager.common.PlatformLoginWorker.updatePersonAndIdentificationLanguage(appId, admin, partyId, delegator, openId, userInfoMap, userLogin, dispatcher);


                if (city != null && !city.equals("") && province != null) {


                    //  邮政地址
                    String contactMechPurposeTypeId = "POSTAL_ADDRESS";
                    Map<String, Object> createPartyPostalAddressOutMap = dispatcher.runSync("createPartyPostalAddress",
                            UtilMisc.toMap("userLogin", admin, "toName", name, "partyId", partyId, "countryGeoId", PeConstant.DEFAULT_GEO_COUNTRY, "city", city, "address1", province + "-" + country, "address2", city, "postalCode", PeConstant.DEFAULT_POST_CODE
                            ));
                    String contactMechId = (String) createPartyPostalAddressOutMap.get("contactMechId");
                    if (!ServiceUtil.isSuccess(createPartyPostalAddressOutMap)) {
                        return createPartyPostalAddressOutMap;
                    }
                }
// Create Facility
                Map<String, Object> createFacilityOutMap = dispatcher.runSync("createFacility", UtilMisc.toMap("userLogin", admin,
                        "ownerPartyId", groupId, "facilityTypeId", "WAREHOUSE", "facilityName", partyId, "defaultInventoryItemTypeId", "NON_SERIAL_INV_ITEM"));
                if (!ServiceUtil.isSuccess(createFacilityOutMap)) {
                    return createFacilityOutMap;
                }




                //其实自己也是自己的供应商
                createPartyRelationshipInMap = new HashMap<String, Object>();
                createPartyRelationshipInMap.put("userLogin", admin);
                createPartyRelationshipInMap.put("roleTypeIdTo", "LEAD");
                createPartyRelationshipInMap.put("roleTypeIdFrom", "OWNER");
                createPartyRelationshipInMap.put("partyRelationshipTypeId", "LEAD_OWNER");
                createPartyRelationshipInMap.put("partyIdTo",groupId );
                createPartyRelationshipInMap.put("partyIdFrom", groupId);
                createPartyRelationshipOutMap = dispatcher.runSync("createPartyRelationship", createPartyRelationshipInMap);

                //自己给自己的备注
                GenericValue aliasForg = delegator.makeValue("AliasForg", UtilMisc.toMap());
                aliasForg.set("aliasId", (String) delegator.getNextSeqId("AliasForg"));
                aliasForg.set("partyIdFrom", groupId);
                aliasForg.set("partyIdTo", groupId);
                aliasForg.set("aliasName", "自有仓库"+"-"+name);
                aliasForg.set("aliasAddress", province + " " + city + " "+country );
                aliasForg.create();

                result.put("tarjeta", getToken(userLogin.getString("userLoginId"), delegator));
                result.put("partyId", partyId);
                result.put("userInfo", PersonManagerQueryServices.queryPersonBaseInfo(delegator, partyId));



                    result.put("partyGroupId", groupId);


            } else {
                return ServiceUtil.returnError("check fail");
            }
        }


        return result;
    }

    private static void mergeAliasForg(Delegator delegator, String partyIdFrom, String beforePartyId, String partyId)throws GenericEntityException {
        //别人给我取的别名
        List<GenericValue> aliasForg = EntityQuery.use(delegator).from("AliasForg").where(
                "partyIdTo", beforePartyId).queryList();
        if(null!=aliasForg && aliasForg.size()>0){
            for(GenericValue storeRow : aliasForg){
                storeRow.set("partyIdTo",partyId);
                storeRow.store();
            }
        }
    }

    private static void mergeProductsSupplier(Delegator delegator, LocalDispatcher dispatcher, GenericValue admin, String beforePartyId, String partyId) throws GenericEntityException, GenericServiceException {

        List<GenericValue>  suppliers = EntityQuery.use(delegator).from("SupplierProduct").where(
                "partyId", beforePartyId).queryList();
        if(suppliers.size()>0){
            for(GenericValue rowSupplier : suppliers){
                    String productId = rowSupplier.getString("productId");
                rowSupplier.remove();
                dispatcher.runSync("createSupplierProduct",
                        UtilMisc.toMap("userLogin", admin, "productId", productId,
                                "partyId", partyId,
                                "availableFromDate", UtilDateTime.nowTimestamp(),
                                "currencyUomId", "CNY", "lastPrice", BigDecimal.ZERO, "minimumOrderQuantity", BigDecimal.ONE, "supplierProductId", productId));
            }
        }

    }

    private static void mergeChangeRelation(Delegator delegator, LocalDispatcher dispatcher, GenericValue admin, String partyIdFrom, String beforePartyId, String partyId) throws GenericEntityException, GenericServiceException {
        Debug.logInfo("Now Meger Relation before:" + beforePartyId + " to=> " + partyId + " from : " + partyIdFrom, module);
        Map<String, Object> createPartyRelationshipInMap = new HashMap<String, Object>();
        createPartyRelationshipInMap.put("userLogin", admin);
        createPartyRelationshipInMap.put("roleTypeIdTo", "LEAD");
        createPartyRelationshipInMap.put("roleTypeIdFrom", "OWNER");
        createPartyRelationshipInMap.put("partyRelationshipTypeId", "LEAD_OWNER");
        createPartyRelationshipInMap.put("partyIdTo", partyId);
        createPartyRelationshipInMap.put("partyIdFrom", partyIdFrom);
        Map<String, Object> createPartyRelationshipOutMap = dispatcher.runSync("createPartyRelationship", createPartyRelationshipInMap);

    }

    private static void mergeChangeOrder(Delegator delegator, LocalDispatcher dispatcher, GenericValue admin, String partyIdFrom, String beforePartyId, String partyId) throws GenericEntityException, GenericServiceException {
//        EntityCondition findConditions = EntityCondition.makeCondition("roleTypeId", EntityOperator.EQUALS, "BILL_TO_CUSTOMER");
//        EntityCondition findConditions2 = EntityCondition.makeCondition("partyId", EntityOperator.EQUALS, partyIdFrom);
//        EntityCondition custCondition = EntityCondition.makeCondition(findConditions, EntityOperator.AND, findConditions2);


        EntityCondition venderCondition1 = EntityCondition.makeCondition("roleTypeId", EntityOperator.EQUALS, "SHIP_FROM_VENDOR");
        EntityCondition venderCondition2 = EntityCondition.makeCondition("partyId", EntityOperator.EQUALS, beforePartyId);
        EntityCondition venderCondition = EntityCondition.makeCondition(venderCondition1, EntityOperator.AND, venderCondition2);

//        EntityCondition allCondition = EntityCondition.makeCondition(custCondition, EntityOperator.AND, venderCondition);

        List<GenericValue> queryOrderList = delegator.findList("PurchaseOrderHeaderItemAndRoles",
                venderCondition, null,
                null, null, false);
        Debug.logInfo("*merge oder:" + queryOrderList, module);
        if (queryOrderList.size() > 0) {
            // 发货厂家角色
            GenericValue partyVendorRole = EntityQuery.use(delegator).from("PartyRole").where("partyId", partyId, "roleTypeId", "SHIP_FROM_VENDOR").queryFirst();
            if (null == partyVendorRole) {
                Map<String, Object> createVendorPartyRoleMap = UtilMisc.toMap("userLogin", admin, "partyId", partyId,
                        "roleTypeId", "SHIP_FROM_VENDOR");
                dispatcher.runSync("createPartyRole", createVendorPartyRoleMap);
            }
            //发出账单的厂家
            GenericValue partyBillVendorRole = EntityQuery.use(delegator).from("PartyRole").where("partyId", partyId, "roleTypeId", "BILL_FROM_VENDOR").queryFirst();
            if (null == partyBillVendorRole) {
                Map<String, Object> createPartyBillVendorRoleMap = UtilMisc.toMap("userLogin", admin, "partyId", partyId,
                        "roleTypeId", "BILL_FROM_VENDOR");
                dispatcher.runSync("createPartyRole", createPartyBillVendorRoleMap);
            }


            for (GenericValue order : queryOrderList) {
                String orderId = order.getString("orderId");
                Debug.logInfo("Now Meger Order Role => " + order, module);
                dispatcher.runSync("removeOrderRole", UtilMisc.toMap("userLogin", admin, "orderId", orderId, "partyId", beforePartyId, "roleTypeId", "SHIP_FROM_VENDOR"));
                dispatcher.runSync("removeOrderRole", UtilMisc.toMap("userLogin", admin, "orderId", orderId, "partyId", beforePartyId, "roleTypeId", "BILL_FROM_VENDOR"));

                dispatcher.runSync("addOrderRole", UtilMisc.toMap("userLogin", admin, "orderId", orderId, "partyId", partyId, "roleTypeId", "SHIP_FROM_VENDOR"));
                dispatcher.runSync("addOrderRole", UtilMisc.toMap("userLogin", admin, "orderId", orderId, "partyId", partyId, "roleTypeId", "BILL_FROM_VENDOR"));
            }
        }
    }


    /**
     * boomUserLogin
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     * @throws java.io.UnsupportedEncodingException
     */
    public static Map<String, Object> boomUserLogin(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException, UnsupportedEncodingException {
        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> result = ServiceUtil.returnSuccess();
        GenericValue userLogin = null;
        String unioId = (String) context.get("unioId");
        //小程序的OPEN ID 也要存
        String openId = (String) context.get("openId");
        GenericValue miniProgramIdentification = EntityQuery.use(delegator).from("PartyIdentification").where("idValue", openId, "partyIdentificationTypeId", "WX_MINIPRO_OPEN_ID").queryFirst();
        if (miniProgramIdentification != null) {
            userLogin = EntityQuery.use(delegator).from("UserLogin").where("partyId", miniProgramIdentification.get("partyId"), "enabled", "Y").queryFirst();
            String tarjeta = getToken(userLogin.get("userLoginId") + "", delegator);
            result.put("tarjeta", tarjeta);
            result.put("userInfo", PersonManagerQueryServices.queryPersonBaseInfo(delegator, miniProgramIdentification.get("partyId") + ""));
            result.put("openId", openId);
            result.put("partyId", miniProgramIdentification.get("partyId") + "");


            Map<String,Object> myGroup = getMyGroup(delegator,""+miniProgramIdentification.get("partyId"));
            if(null!=myGroup){
                String partyGroupId = (String) myGroup.get("partyId");
                result.put("partyGroupId", partyGroupId);
            }

            return result;
        }




        result.put("tarjeta", null);
        result.put("userInfo", null);
        result.put("partyId", null);

        return result;
    }


    /**
     * createProductionRouting
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> createProductionRouting(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));
//
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = userLogin.getString("partyId");

        String productId = (String) context.get("productId");
        String quantity = (String) context.get("quantity");

        GenericValue product = delegator.findOne("Product", false, UtilMisc.toMap("productId", productId));

        String workEffortName = product.getString("productName");

//        GenericValue relation = EntityQuery.use(delegator).from("PartyRelationship").where(
//                "partyIdFrom", partyId, "partyRelationshipTypeId", "OWNER").queryFirst();
//
//        String partyGroupId = relation.getString("partyIdTo");
        Map<String,Object> myGroup = getMyGroup(delegator,partyId);
        String partyGroupId = (String) myGroup.get("partyId");


//        createProductionRun
        GenericValue facility = EntityQuery.use(delegator).from("Facility").where(
                "ownerPartyId", partyGroupId).queryFirst();
        String facilityId = facility.getString("facilityId");
        BigDecimal pRQuantity = new BigDecimal(quantity);
//"routingId",routingId,
//        Map<String,Object> createProductionRunMap  = dispatcher.runSync("createProductionRunsForProductBom",UtilMisc.toMap("userLogin",userLogin,
//                "facilityId",facilityId,"quantity",pRQuantity,"workEffortName",workEffortName
//                ,"productId",productId,"startDate",org.apache.ofbiz.base.util.UtilDateTime.nowTimestamp()));
//        String productionRunId = (String) createProductionRunMap.get("productionRunId");

        //createProductionRunsForProductBom
//
//        dispatcher.runSync("addProductionRunRoutingTask",UtilMisc.toMap("userLogin",userLogin,
//                "productionRunId",productionRunId,"routingTaskId",routingId,"priority",new Long(1)));

        dispatcher.runSync("createRequirement", UtilMisc.toMap("userLogin", userLogin, "requirementTypeId", "INTERNAL_REQUIREMENT"
                , "facilityId", facilityId, "productId", productId, "statusId", "REQ_APPROVED", "requirementStartDate", org.apache.ofbiz.base.util.UtilDateTime.nowTimestamp(),
                "quantity", new BigDecimal(quantity)));

        List<GenericValue> productAssocList =   EntityQuery.use(delegator).from("ProductAssoc").where(
                "productId", productId).queryList();
        if(productAssocList.size()>0){
            for(GenericValue rowAssoc : productAssocList){
                BigDecimal rowQuantity = (BigDecimal) rowAssoc.get("quantity");
                String rowProductId = (String) rowAssoc.get("productIdTo");
                GenericValue  productionTemp =   EntityQuery.use(delegator).from("ProductionTemp").where(
                        "productId", rowProductId,"facilityId",facilityId).queryFirst();
                 if(productionTemp!=null){
                     productionTemp.set("count",(Integer.parseInt(""+productionTemp.get("count"))+ (rowQuantity.intValue()* Integer.parseInt(quantity)))+"" );
                    productionTemp.store();
                 }else{
                     productionTemp = delegator.makeValue("ProductionTemp", UtilMisc.toMap());
                     GenericValue rowProduct = delegator.findOne("Product", false, UtilMisc.toMap("productId", rowProductId));
                     productionTemp.set("tempId",(String) delegator.getNextSeqId("ProductionTemp"));
                     productionTemp.set("count",(rowQuantity.intValue()* Integer.parseInt(quantity))+"");
                     productionTemp.set("productId",rowProductId);
                     productionTemp.set("facilityId",facilityId);
                     productionTemp.set("productName",rowProduct.getString("productName"));
                     productionTemp.set("type","MANUF_COMPONENT");
                     productionTemp.set("detailImage",rowProduct.getString("detailImageUrl"));
                     productionTemp.create();
                 }





                GenericValue  productionTempFinnish =   EntityQuery.use(delegator).from("ProductionTemp").where(
                        "productId", productId,"facilityId",facilityId).queryFirst();
                if(productionTempFinnish!=null){
                    productionTempFinnish.set("count",(Integer.parseInt(""+productionTempFinnish.get("count"))+  Integer.parseInt(quantity))+"" );
                    productionTempFinnish.store();
                }else{
                    productionTempFinnish = delegator.makeValue("ProductionTemp", UtilMisc.toMap());
                    productionTempFinnish.set("tempId",(String) delegator.getNextSeqId("ProductionTemp"));
                    productionTempFinnish.set("count", Integer.parseInt(quantity)+"" );
                    productionTempFinnish.set("productId",productId);
                    productionTempFinnish.set("facilityId",facilityId);
                    productionTempFinnish.set("productName",product.getString("productName"));
                    productionTempFinnish.set("type","FINISHED_GOOD");
                    productionTempFinnish.set("detailImage",product.getString("detailImageUrl"));
                    productionTempFinnish.create();
                }
            }

        }



        return resultMap;
    }

    /**
     * updateMyFinishedGood
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> updateMyFinishedGood(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));
//
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = userLogin.getString("partyId");

        String productName = (String) context.get("productName");
        String quantityUomId = (String) context.get("quantityUomId");
        String imagePath = (String) context.get("imagePath");
        String rawMaterials = (String) context.get("rawMaterials");
        String productId = (String) context.get("productId");


        GenericValue product  =  EntityQuery.use(delegator).from("Product").where(
                "productId", productId).queryFirst();
        product.set("productName",productName);
        product.set("detailImageUrl",imagePath!=null?imagePath:"");
        product.set("smallImageUrl",imagePath!=null?imagePath:"");
        product.set("quantityUomId",quantityUomId);
        product.store();

        List<GenericValue> productAssoc =  EntityQuery.use(delegator).from("ProductAssoc").where(
                "productId", productId,"productAssocTypeId", "MANUF_COMPONENT").queryList();
        if(null!= productAssoc && productAssoc.size()>0){
            for(GenericValue assoc : productAssoc){
                assoc.remove();
            }
        }
        if (rawMaterials != null && rawMaterials.length() > 2) {
            for (String rowProduct : rawMaterials.split(",")) {
                String productIdFrom = rowProduct.substring(0, rowProduct.indexOf(":"));
                String count = rowProduct.substring(rowProduct.indexOf(":") + 1);
                if(count.trim().equals("0")){
                    continue;
                }
                dispatcher.runSync("createProductAssoc", UtilMisc.toMap("userLogin", admin, "productIdTo", productIdFrom, "productId", productId,"productAssocTypeId", "MANUF_COMPONENT",
                         "quantity", new BigDecimal(count), "fromDate", org.apache.ofbiz.base.util.UtilDateTime.nowTimestamp()));
            }
        }

        return resultMap;
    }


    /**
     * createMyFinishedGood
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> createMyFinishedGood(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));
//
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = userLogin.getString("partyId");

        String productName = (String) context.get("productName");
        String quantityUomId = (String) context.get("quantityUomId");
        String imagePath = (String) context.get("imagePath");
        String rawMaterials = (String) context.get("rawMaterials");


        Map<String, Object> createProductInMap = new HashMap<String, Object>();
        createProductInMap.put("userLogin", admin);
        long ctm = System.currentTimeMillis();
        createProductInMap.put("internalName", productName);
        createProductInMap.put("productName", productName);
        createProductInMap.put("productTypeId", "FINISHED_GOOD");
//        createProductInMap.put("description", description);
        createProductInMap.put("detailImageUrl", imagePath);
        createProductInMap.put("smallImageUrl", imagePath);
        createProductInMap.put("quantityUomId", quantityUomId);

        Map<String,Object> myGroup = getMyGroup(delegator,partyId);
        String partyGroupId = (String) myGroup.get("partyId");
//        GenericValue relation = EntityQuery.use(delegator).from("PartyRelationship").where(
//                "partyIdFrom", partyId, "partyRelationshipTypeId", "OWNER").queryFirst();
//
//        String partyGroupId = relation.getString("partyIdTo");


        GenericValue facility = EntityQuery.use(delegator).from("Facility").where(
                "ownerPartyId", partyGroupId).queryFirst();
        String facilityId = facility.getString("facilityId");
        createProductInMap.put("facilityId", facilityId);

        Map<String, Object> createProductOutMap = dispatcher.runSync("createProduct", createProductInMap);
        if (ServiceUtil.isError(createProductOutMap)) {
            return createProductOutMap;
        }


        String productId = (String) createProductOutMap.get("productId");


        dispatcher.runSync("createProductFacility", UtilMisc.toMap("userLogin", userLogin,
                "productId", productId, "facilityId", facilityId, "minimumStock", BigDecimal.ZERO, "reorderQuantity", new BigDecimal("10000"), "daysToShip", new Long(10)));


        if (rawMaterials != null && rawMaterials.length() > 2) {
            for (String rowProduct : rawMaterials.split(",")) {
                String productIdFrom = rowProduct.substring(0, rowProduct.indexOf(":"));
                String count = rowProduct.substring(rowProduct.indexOf(":") + 1);
                dispatcher.runSync("createProductAssoc", UtilMisc.toMap("userLogin", admin, "productIdTo", productIdFrom, "productId", productId
                        , "quantity", new BigDecimal(count), "productAssocTypeId", "MANUF_COMPONENT", "fromDate", org.apache.ofbiz.base.util.UtilDateTime.nowTimestamp()));
            }
        }


        dispatcher.runSync("addProductRole", UtilMisc.toMap("userLogin", admin, "roleTypeId", "ADMIN", "productId", productId, "partyId", partyGroupId));
        dispatcher.runSync("createCostComponent", UtilMisc.toMap("userLogin", admin, "costComponentTypeId", "GEN_COST", "costUomId", "CNY", "productId", productId, "partyId", partyId));


        return resultMap;
    }


    /**
     * empJoinPartyGroup
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> empJoinPartyGroup(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));
//
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = userLogin.getString("partyId");
        String roleTypeIdFrom = (String) context.get("roleTypeIdFrom");
        String partyGroupId = (String) context.get("partyGroupId");
        String empId = (String) context.get("empId");

        Map<String, Object> createPartyRelationshipInMap = new HashMap<String, Object>();
        createPartyRelationshipInMap.put("userLogin", admin);
        createPartyRelationshipInMap.put("roleTypeIdTo", "_NA_");
        createPartyRelationshipInMap.put("roleTypeIdFrom", roleTypeIdFrom);
        createPartyRelationshipInMap.put("partyRelationshipTypeId", "EMPLOYMENT");
        createPartyRelationshipInMap.put("partyIdTo", partyGroupId);
        createPartyRelationshipInMap.put("partyIdFrom", empId);

        Map<String, Object> createPartyRelationshipOutMap = dispatcher.runSync("createPartyRelationship", createPartyRelationshipInMap);
        if (ServiceUtil.isError(createPartyRelationshipOutMap)) {
            return createPartyRelationshipOutMap;
        }

        return resultMap;
    }




    public static Map<String, Object> removeMyRawMaterials(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));
//
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = userLogin.getString("partyId");

        Map<String,Object> myGroup = getMyGroup(delegator,partyId);
        String partyGroupId = (String) myGroup.get("partyId");

        String productId = (String) context.get("productId");

        GenericValue productRole = EntityQuery.use(delegator).from("ProductRole").where(
                "partyId", partyGroupId,"productId",productId,"roleTypeId","ADMIN").queryFirst();
        Debug.logInfo("partyGroupId:"+partyGroupId,module);
        Debug.logInfo("productId:"+productId,module);
        Debug.logInfo("productRole:"+productRole,module);
        if(productRole!=null){
            productRole.remove();
        }

        return resultMap;
    }



    public static Map<String, Object> updateRawMaterials(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));
//
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = userLogin.getString("partyId");

        String productName = (String) context.get("productName");
        String quantityUomId = (String) context.get("quantityUomId");
        String imagePath = (String) context.get("imagePath");
        String suppliers = (String) context.get("suppliers");
        String productId = (String) context.get("productId");


        GenericValue product  =  EntityQuery.use(delegator).from("Product").where(
                "productId", productId).queryFirst();
        Debug.logInfo("productNameL"+productName,module);
        product.set("productName", productName);
        product.set("detailImageUrl",imagePath!=null?imagePath:"");
        product.set("smallImageUrl",imagePath!=null?imagePath:"");
        product.set("quantityUomId",quantityUomId);

        product.store();
        List<GenericValue> supplierProducts =  EntityQuery.use(delegator).from("SupplierProduct").where(
                "productId", productId).queryList();
        if(null!= supplierProducts && supplierProducts.size()>0){
            for(GenericValue supplierRow : supplierProducts){
                        supplierRow.remove();
            }
        }



        if (suppliers != null && suppliers.length() > 2) {
            for (String rowSupplier : suppliers.split(",")) {
                dispatcher.runSync("createSupplierProduct",
                        UtilMisc.toMap("userLogin", admin, "productId", productId,
                                "partyId", rowSupplier,
                                "availableFromDate", UtilDateTime.nowTimestamp(),
                                "currencyUomId", "CNY", "lastPrice", BigDecimal.ZERO, "minimumOrderQuantity", BigDecimal.ONE, "supplierProductId", productId));
            }
        }

        return resultMap;
    }


    /**
     * createRawMaterials
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> createRawMaterials(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));



        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = userLogin.getString("partyId");


        Map<String,Object> myGroup =  getMyGroup(delegator,partyId);
        String partyGroupId = (String) myGroup.get("partyId");

        String productName = (String) context.get("productName");
        String quantityUomId = (String) context.get("quantityUomId");
        String imagePath = (String) context.get("imagePath");
        String suppliers = (String) context.get("suppliers");


        Map<String, Object> createProductInMap = new HashMap<String, Object>();
        createProductInMap.put("userLogin", admin);
        long ctm = System.currentTimeMillis();
        createProductInMap.put("internalName", productName);
        createProductInMap.put("productName", productName);
        createProductInMap.put("productTypeId", "RAW_MATERIAL");
//        createProductInMap.put("description", description);
        createProductInMap.put("detailImageUrl", imagePath!=null?imagePath:"");
        createProductInMap.put("smallImageUrl", imagePath!=null?imagePath:"");
        createProductInMap.put("quantityUomId", quantityUomId);

        Map<String, Object> createProductOutMap = dispatcher.runSync("createProduct", createProductInMap);
        if (ServiceUtil.isError(createProductOutMap)) {
            return createProductOutMap;
        }

        String productId = (String) createProductOutMap.get("productId");

        if (suppliers != null && suppliers.length() > 2) {
            for (String rowSupplier : suppliers.split(",")) {
                dispatcher.runSync("createSupplierProduct",
                        UtilMisc.toMap("userLogin", admin, "productId", productId,
                                "partyId", rowSupplier,
                                "availableFromDate", UtilDateTime.nowTimestamp(),
                                "currencyUomId", "CNY", "lastPrice", BigDecimal.ZERO, "minimumOrderQuantity", BigDecimal.ONE, "supplierProductId", productId));
            }
        }


        dispatcher.runSync("addProductRole", UtilMisc.toMap("userLogin", admin, "roleTypeId", "ADMIN", "productId", productId,
                "partyId", partyGroupId));

        return resultMap;
    }


    /**
     * updateMyLead
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> updateMyLead(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = userLogin.getString("partyId");
        Map<String,Object> myGroup = getMyGroup(delegator,partyId);
        String partyGroupId = (String) myGroup.get("partyId");
        String supplierName = (String) context.get("supplierName");
        String supplierTel = (String) context.get("supplierTel");
        String leadId = (String) context.get("leadId");
        String provinceName = (String) context.get("provinceName");
        String cityName = (String) context.get("cityName");
        String countyName = (String) context.get("countyName");
        String detailInfo = (String) context.get("detailInfo");
        String companyName = (String) context.get("companyName");

        String firstName = supplierName;
        String lastName = " ";

        if (supplierName.length() >= 4) {
            firstName = supplierName.substring(0, 2);
            lastName = supplierName.substring(2 + 1);
        }
        if (supplierName.length() <= 3) {
            lastName = supplierName.substring(0, 1);
            firstName = supplierName.substring(1);
        }

//        dispatcher.runSync("updatePerson",UtilMisc.toMap("userLogin",admin,"partyId",leadId
//        ,"firstName",firstName,"lastName",lastName));
        GenericValue person = EntityQuery.use(delegator).from("Person").where(
                "partyId", leadId).queryFirst();
        Debug.logInfo("firstName:" + firstName, module);
        Debug.logInfo("lastName:" + lastName, module);
        person.set("firstName", firstName);
        person.set("lastName", lastName);
        person.store();
        GenericValue telecomNumberAndPartyView = EntityQuery.use(delegator).from("TelecomNumberAndPartyView").where(
                "partyId", leadId).queryFirst();
        if(null!= telecomNumberAndPartyView){
            String contactMechId = telecomNumberAndPartyView.getString("contactMechId");
            GenericValue telecomNumber= EntityQuery.use(delegator).from("TelecomNumber").where(
                    "contactMechId", contactMechId).queryFirst();
            telecomNumber.set("contactNumber",supplierTel);
            telecomNumber.store();
        }else{
            String contactMechId = (String) delegator.getNextSeqId("ContactMech");
            dispatcher.runSync("createPartyTelecomNumber",UtilMisc.toMap("contactNumber",supplierTel,
                    "userLogin",admin,"contactMechId",contactMechId,"partyId",leadId));
        }
        GenericValue partyAndPostalAddress = EntityQuery.use(delegator).from("PartyAndPostalAddress").where(
                "partyId", leadId,"contactMechTypeId","POSTAL_ADDRESS").queryFirst();
        if(null!=partyAndPostalAddress){
            String contactMechId = partyAndPostalAddress.getString("contactMechId");
            GenericValue postalAddress= EntityQuery.use(delegator).from("PostalAddress").where(
                    "contactMechId", contactMechId).queryFirst();
            postalAddress.set("address1",provinceName + " " + cityName + " "+countyName + " " + detailInfo);
            postalAddress.store();
        }else{
            String contactMechPurposeTypeId = "POSTAL_ADDRESS";
            Map<String, Object> createPartyPostalAddressOutMap = dispatcher.runSync("createPartyPostalAddress",
                    UtilMisc.toMap("userLogin", admin, "toName", supplierName, "partyId", leadId, "countryGeoId",
                            PeConstant.DEFAULT_GEO_COUNTRY, "city", cityName, "address1", provinceName + " " + cityName + " "+countyName + " " + detailInfo, "postalCode", PeConstant.DEFAULT_POST_CODE
                    ));
        }

//        EntityCondition findConditions = EntityCondition
//                .makeCondition("contactMechId", EntityOperator.NOT_IN, types);
//        EntityCondition partyConditions = EntityCondition
//                .makeCondition("partyId", EntityOperator.EQUALS, leadId);
//        EntityCondition genericCondition = EntityCondition.makeCondition(findConditions, EntityOperator.AND, partyConditions);
//
//        GenericValue partyAddressContactMech = EntityQuery.use(delegator).from("PartyContactMech").
//                where(genericCondition).queryFirst();
//        String addressContactId = partyAddressContactMech.getString("contactMechId");
//
//        String postalCode= "200000";
//
//        dispatcher.runSync("createUpdateAddress", UtilMisc.toMap("userLogin", admin, "partyId", leadId, "city", cityName == null ? "" : cityName,
//              "postalCode",postalCode,"firstName", firstName,"lastName", lastName,  "address1",  provinceName + " " + cityName + " "+countyName + " " + detailInfo, "contactMechId", addressContactId));
//
//        dispatcher.runSync("updateTelecomNumber", UtilMisc.toMap("userLogin", admin, "contactNumber", supplierTel, "contactMechId", telContactId));


        GenericValue aliasForg = EntityQuery.use(delegator).from("AliasForg").where(
                "partyIdTo", leadId,"partyIdFrom",partyGroupId).queryFirst();

        aliasForg.set("aliasName", companyName+"-"+lastName+firstName);
        aliasForg.set("aliasAddress", provinceName + " " + cityName + " "+countyName + " "  + detailInfo);
        aliasForg.store();

        resultMap.put("leadInfo", UtilMisc.toMap("partyId", leadId));
        return resultMap;
    }


    /**
     * CreateMySupplier
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> createMyLead(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = userLogin.getString("partyId");
        Map<String,Object> myGroup = getMyGroup(delegator,partyId);
        String partyGroupId = (String) myGroup.get("partyId");
        partyId = partyGroupId;
        userLogin =  EntityQuery.use(delegator).from("UserLogin").where("partyId", partyId).queryFirst();


        String supplierName = (String) context.get("supplierName");
        String supplierTel = (String) context.get("supplierTel");
        String appId = (String) context.get("appId");
        String companyName = (String) context.get("companyName");
        Debug.logInfo("create my lead appid:" + appId, module);
        //已经存在系统中的供应商,增加一个线索关联就行
        List<GenericValue> hasUser = EntityQuery.use(delegator).from("PartyAndTelecomNumber").where(
                "contactNumber", supplierTel).queryList();
        String provinceName = (String) context.get("provinceName");
        String cityName = (String) context.get("cityName");
        String countyName = (String) context.get("countyName");
        String detailInfo = (String) context.get("detailInfo");
        String firstName = supplierName;
        String lastName = " ";

        if (supplierName.length() >= 4) {
            firstName = supplierName.substring(0, 2);
            lastName = supplierName.substring(2 + 1);
        }
        if (supplierName.length() <= 3) {
            lastName = supplierName.substring(0, 1);
            firstName = supplierName.substring(1);
        }

        if(supplierName.length()<2){
            lastName = " ";
            firstName = supplierName;
        }

        if (hasUser.size() > 0) {


            for (GenericValue gv : hasUser) {
                String oldPartyId = (String) gv.getString("partyId");
                GenericValue miniProgramIdentification = EntityQuery.use(delegator).from("PartyIdentification").where("appId", appId, "partyId", oldPartyId, "partyIdentificationTypeId", "WX_MINIPRO_OPEN_ID").queryFirst();
                if (miniProgramIdentification != null) {

                    // LEAD是否拥有
                    GenericValue partyLeadRole = EntityQuery.use(delegator).from("PartyRole").where("partyId", oldPartyId, "roleTypeId", "LEAD").queryFirst();
                    if (null == partyLeadRole) {
                        Map<String, Object> createCustPartyRoleMap = UtilMisc.toMap("userLogin", admin, "partyId", oldPartyId,
                                "roleTypeId", "LEAD");
                        dispatcher.runSync("createPartyRole", createCustPartyRoleMap);
                    }

                    String idValue = (String) miniProgramIdentification.get("idValue");
                    if (null != idValue && !idValue.equals("")) {
                        Map<String, Object> createPartyRelationshipInMap = new HashMap<String, Object>();
                        createPartyRelationshipInMap.put("userLogin", admin);
                        createPartyRelationshipInMap.put("roleTypeIdTo", "LEAD");
                        createPartyRelationshipInMap.put("roleTypeIdFrom", "OWNER");
                        createPartyRelationshipInMap.put("partyRelationshipTypeId", "LEAD_OWNER");
                        createPartyRelationshipInMap.put("partyIdTo", oldPartyId);
                        createPartyRelationshipInMap.put("partyIdFrom", partyId);
                        Map<String, Object> createPartyRelationshipOutMap = dispatcher.runSync("createPartyRelationship", createPartyRelationshipInMap);

                        // Create Alias
                        GenericValue aliasForg = delegator.makeValue("AliasForg", UtilMisc.toMap());
                        aliasForg.set("aliasId", (String) delegator.getNextSeqId("AliasForg"));
                        aliasForg.set("partyIdFrom", partyId);
                        aliasForg.set("partyIdTo", oldPartyId);
                        aliasForg.set("aliasName", companyName+"-"+lastName+firstName);
                        aliasForg.set("aliasAddress", provinceName + " " + cityName + " "+countyName + " "  + detailInfo);
                        aliasForg.create();

                        return resultMap;
                    }
                }
            }
        }




        //是否已经添加过

        Long hasData = EntityQuery.use(delegator).from("PartyRelationshipAndContactMechDetail").where(
                "partyIdFrom", partyId, "roleTypeIdTo", "LEAD", "partyRelationshipTypeId", "LEAD_OWNER", "tnContactNumber", supplierTel).queryCount();
        if (hasData > 0) {
            return ServiceUtil.returnError("已经存在的线索");

        }





        Map<String, Object> createLeadMap = new HashMap<String, Object>();

        createLeadMap.put("userLogin", userLogin);
        createLeadMap.put("firstName", firstName);
        createLeadMap.put("lastName", lastName);
        createLeadMap.put("countryGeoId", "CHN");
        createLeadMap.put("city", cityName == null ? "" : cityName);
        if (null != countyName && null != provinceName && null != detailInfo) {
            createLeadMap.put("address1", provinceName + " " + cityName + " "+countyName + " "  + detailInfo);
        }
        createLeadMap.put("countryCode", "86");
        createLeadMap.put("postalCode", "200000");
        createLeadMap.put("contactNumber", supplierTel);


        Map<String, Object> createLeadOutMap = dispatcher.runSync("createLead", createLeadMap);


        if (ServiceUtil.isError(createLeadOutMap)) {
            return createLeadOutMap;
        }
        String resultPartyId = (String) createLeadOutMap.get("partyId");


        dispatcher.runSync("createPartyRole",
                UtilMisc.toMap("userLogin", admin, "partyId", resultPartyId, "roleTypeId", "SUPPLIER"));


        // Create Alias

        GenericValue aliasForg = delegator.makeValue("AliasForg", UtilMisc.toMap());

        aliasForg.set("aliasId", (String) delegator.getNextSeqId("AliasForg"));
        aliasForg.set("partyIdFrom", partyId);
        aliasForg.set("partyIdTo", resultPartyId);
        aliasForg.set("aliasName", companyName+"-"+lastName+firstName);
        aliasForg.set("aliasAddress", provinceName + " " + cityName + " "+countyName + " "  + detailInfo);
        aliasForg.create();

        resultMap.put("leadInfo", UtilMisc.toMap("partyId", resultPartyId));
        return resultMap;
    }


    private static String createSupplier(Delegator delegator, LocalDispatcher dispatcher, GenericValue admin, String vender, String venderLocal) throws GenericEntityException, GenericServiceException {
        String partyId = "";
        Map<String, Object> serviceResultByCreatePartyMap = dispatcher.runSync("createPartyGroup",
                UtilMisc.toMap("userLogin", admin, "groupName", vender, "groupNameLocal", venderLocal));
        partyId = (String) serviceResultByCreatePartyMap.get("partyId");
        //Grant Role
        dispatcher.runSync("createPartyRole",
                UtilMisc.toMap("userLogin", admin, "partyId", partyId, "roleTypeId", "SUPPLIER"));
        dispatcher.runSync("createPartyRole",
                UtilMisc.toMap("userLogin", admin, "partyId", partyId, "roleTypeId", "VENDOR"));
        dispatcher.runSync("createPartyRole",
                UtilMisc.toMap("userLogin", admin, "partyId", partyId, "roleTypeId", "MANUFACTURER"));
        dispatcher.runSync("createPartyRole",
                UtilMisc.toMap("userLogin", admin, "partyId", partyId, "roleTypeId", "CUSTOMER"));

        dispatcher.runSync("createPartyRole",
                UtilMisc.toMap("userLogin", admin, "partyId", partyId, "roleTypeId", "ACCOUNT_LEAD"));
        return partyId;
    }


}
