package main.java.com.banfftech.boom;


import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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

import static main.java.com.banfftech.personmanager.PersonManagerQueryServices.module;
import static main.java.com.banfftech.personmanager.PersonManagerQueryServices.queryBomPersonBaseInfo;
import static main.java.com.banfftech.personmanager.PersonManagerQueryServices.queryPersonBaseInfo;

/**
 * Created by S on 2018/8/29.
 */
public class BoomQueryServices {


    public static final String resourceUiLabels = "CommonEntityLabels.xml";


    /**
     * queryMyPurchaseOrderList
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryMyPurchaseOrderList(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = userLogin.getString("partyId");

        List<Map<String,Object>> returnList = new ArrayList<Map<String, Object>>();


        Set<String> fieldSet = new HashSet<String>();
        fieldSet.add("orderId");
        fieldSet.add("partyId");
        fieldSet.add("statusId");
        fieldSet.add("currencyUom");
        fieldSet.add("grandTotal");
        fieldSet.add("productId");
        fieldSet.add("quantity");
        fieldSet.add("unitPrice");
        fieldSet.add("isViewed");
        fieldSet.add("roleTypeId");
        fieldSet.add("orderDate");
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        EntityCondition findConditions = EntityCondition.makeCondition("roleTypeId", EntityOperator.EQUALS, "BILL_TO_CUSTOMER");
        EntityCondition findConditions2 = EntityCondition.makeCondition("partyId", EntityOperator.EQUALS, partyId);
        EntityCondition genericCondition = EntityCondition.makeCondition(findConditions, EntityOperator.AND, findConditions2);
        List<GenericValue> queryOrderList = delegator.findList("PurchaseOrderHeaderItemAndRoles",
                genericCondition, fieldSet,
                UtilMisc.toList("orderDate DESC"), null, false);
        if(queryOrderList.size()>0){
            for(GenericValue order : queryOrderList){
                Map<String, Object> rowMap = new HashMap<String, Object>();

                rowMap = order.getAllFields();
//isViewed
                String productId = (String) order.get("productId");
                String isViewed  = (String) order.get("isViewed");
                GenericValue product = delegator.findOne("Product", UtilMisc.toMap("productId", productId), false);

                rowMap.put("isViewed",isViewed);

                rowMap.put("productName", "" + product.get("productName"));

                rowMap.put("detailImageUrl", (String) product.get("detailImageUrl"));

                String statusId = (String) order.get("statusId");

                if(statusId.equals("ORDER_APPROVED") && null!=isViewed&&isViewed.equals("Y")){
                    rowMap.put("statusId","ORDER_VIEWED");
                }

                String orderId = order.getString("orderId");

                GenericValue custOrderRole = EntityQuery.use(delegator).from("OrderRole").where("orderId", orderId, "roleTypeId", "SHIP_FROM_VENDOR").queryFirst();

                String payToPartyId =custOrderRole.getString("partyId");

                rowMap.put("salesPersonInfoMap", queryBomPersonBaseInfo(delegator, payToPartyId,partyId));

                rowMap.put("custPersonInfoMap", queryBomPersonBaseInfo(delegator, partyId,partyId));




                String uomId = product.getString("quantityUomId");
                GenericValue uom =  EntityQuery.use(delegator).from("Uom").where(
                        "uomId", uomId).queryFirst();
                String uomDescription = uom.getString("description");
                String cndescription = UtilProperties.getMessage(resourceUiLabels, "Uom.description." + uomId, new Locale("zh"));
                rowMap.put("uomDescription",cndescription.indexOf("Uom.description")>-1?uomDescription:cndescription);

                rowMap.put("orderDate", sdf.format((Timestamp)order.get("orderDate")));

                returnList.add(rowMap);
            }
        }


        resultMap.put("orderList",returnList);
        return resultMap;
    }


    /**
     * quickQueryCount
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> quickQueryCount(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = userLogin.getString("partyId");

        Long supplierCount = EntityQuery.use(delegator).from("PartyRelationship").where(
                "partyIdFrom", partyId, "roleTypeIdTo", "LEAD", "partyRelationshipTypeId", "LEAD_OWNER" ).queryCount();
        Long rawMaterialsCount = EntityQuery.use(delegator).from("ProductAndRole").where(
                "partyId", partyId, "roleTypeId", "ADMIN", "productTypeId", "RAW_MATERIAL").queryCount();
        Long finishGoodCount = EntityQuery.use(delegator).from("ProductAndRole").where(
                "partyId", partyId, "roleTypeId", "ADMIN", "productTypeId", "FINISHED_GOOD").queryCount();

        EntityCondition findConditions = EntityCondition.makeCondition("roleTypeId", EntityOperator.EQUALS, "SHIP_FROM_VENDOR");
        EntityCondition findConditions2 = EntityCondition.makeCondition("partyId", EntityOperator.EQUALS, partyId);
        EntityCondition genericCondition = EntityCondition.makeCondition(findConditions, EntityOperator.AND, findConditions2);
        EntityCondition statusConditions = EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, "ORDER_APPROVED");
        EntityCondition genericCondition2 = EntityCondition.makeCondition(genericCondition, EntityOperator.AND, statusConditions);
        Long salesOrderCount = EntityQuery.use(delegator).from("PurchaseOrderHeaderItemAndRoles").where(
                genericCondition2).queryCount();
        EntityCondition findConditionsCust = EntityCondition.makeCondition("roleTypeId", EntityOperator.EQUALS, "BILL_TO_CUSTOMER");
        EntityCondition genericCondition3 = EntityCondition.makeCondition(findConditionsCust, EntityOperator.AND, findConditions2);
        Long purchaseOrderCount = EntityQuery.use(delegator).from("PurchaseOrderHeaderItemAndRoles").where(
                genericCondition3).queryCount();


        //生产计划
        GenericValue relation = EntityQuery.use(delegator).from("PartyRelationship").where(
                "partyIdFrom", partyId, "partyRelationshipTypeId", "OWNER" ).queryFirst();
        String partyGroupId = relation.getString("partyIdTo");
        GenericValue facility =  EntityQuery.use(delegator).from("Facility").where(
                "ownerPartyId", partyGroupId ).queryFirst();
        String facilityId = facility.getString("facilityId");
        Long productionsCount = EntityQuery.use(delegator).from("WorkEffortAndGoods").where(
                "workEffortTypeId", "PROD_ORDER_HEADER", "facilityId", facilityId).queryCount();




        Map<String,Object> queryCountMap = new HashMap<String, Object>();
        queryCountMap.put("supplierCount",supplierCount.intValue());
        queryCountMap.put("rawMaterialsCount",rawMaterialsCount.intValue());
        queryCountMap.put("finishGoodCount",finishGoodCount.intValue());
        queryCountMap.put("salesOrderCount",salesOrderCount.intValue());
        queryCountMap.put("purchaseOrderCount",purchaseOrderCount.intValue());
        queryCountMap.put("productionsCount",productionsCount.intValue());

        resultMap.put("queryCountMap",queryCountMap);
        return resultMap;
    }




    public static Map<String, Object> queryMySalesOrderList(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = userLogin.getString("partyId");

        List<Map<String,Object>> returnList = new ArrayList<Map<String, Object>>();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        Set<String> fieldSet = new HashSet<String>();
        fieldSet.add("orderId");
        fieldSet.add("partyId");
        fieldSet.add("statusId");
        fieldSet.add("currencyUom");
        fieldSet.add("grandTotal");
        fieldSet.add("productId");
        fieldSet.add("quantity");
        fieldSet.add("unitPrice");
        fieldSet.add("isViewed");
        fieldSet.add("roleTypeId");
        fieldSet.add("orderDate");

        EntityCondition findConditions = EntityCondition.makeCondition("roleTypeId", EntityOperator.EQUALS, "SHIP_FROM_VENDOR");
        EntityCondition findConditions2 = EntityCondition.makeCondition("partyId", EntityOperator.EQUALS, partyId);
        EntityCondition genericCondition = EntityCondition.makeCondition(findConditions, EntityOperator.AND, findConditions2);

        EntityCondition statusConditions = EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, "ORDER_APPROVED");

        EntityCondition genericCondition2 = EntityCondition.makeCondition(genericCondition, EntityOperator.AND, statusConditions);


        List<GenericValue> queryOrderList = delegator.findList("PurchaseOrderHeaderItemAndRoles",
                genericCondition2, fieldSet,
                UtilMisc.toList("orderDate DESC"), null, false);
        if(queryOrderList.size()>0){
            for(GenericValue order : queryOrderList){
                Map<String, Object> rowMap = new HashMap<String, Object>();

                rowMap = order.getAllFields();

                String productId = (String) order.get("productId");
                String isViewed = (String) order.get("isViewed");

                GenericValue product = delegator.findOne("Product", UtilMisc.toMap("productId", productId), false);

                rowMap.put("productName", "" + product.get("productName"));
                rowMap.put("isViewed",isViewed);

                rowMap.put("detailImageUrl", (String) product.get("detailImageUrl"));

                String statusId = (String) order.get("statusId");


                if(statusId.equals("ORDER_APPROVED") && null!=isViewed&&isViewed.equals("Y")){
                    rowMap.put("statusId","ORDER_VIEWED");
                }

                String orderId = order.getString("orderId");

                GenericValue custOrderRole = EntityQuery.use(delegator).from("OrderRole").where("orderId", orderId, "roleTypeId", "BILL_TO_CUSTOMER").queryFirst();

                String custId =custOrderRole.getString("partyId");

                rowMap.put("salesPersonInfoMap", queryBomPersonBaseInfo(delegator, partyId,partyId));

                rowMap.put("custPersonInfoMap", queryBomPersonBaseInfo(delegator, custId, partyId));
                String uomId = product.getString("quantityUomId");
                GenericValue uom =  EntityQuery.use(delegator).from("Uom").where(
                        "uomId", uomId).queryFirst();
                String uomDescription = uom.getString("description");
                String cndescription = UtilProperties.getMessage(resourceUiLabels, "Uom.description." + uomId, new Locale("zh"));
                rowMap.put("uomDescription",cndescription.indexOf("Uom.description")>-1?uomDescription:cndescription);
                rowMap.put("orderDate", sdf.format(order.get("orderDate")));
                returnList.add(rowMap);
            }
        }


        resultMap.put("orderList",returnList);
        return resultMap;
    }

    /**
     * queryProductionRouting
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryProductionRouting(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = userLogin.getString("partyId");


        GenericValue relation = EntityQuery.use(delegator).from("PartyRelationship").where(
                "partyIdFrom", partyId, "partyRelationshipTypeId", "OWNER" ).queryFirst();

        String partyGroupId = relation.getString("partyIdTo");

        GenericValue facility =  EntityQuery.use(delegator).from("Facility").where(
                "ownerPartyId", partyGroupId ).queryFirst();

        String facilityId = facility.getString("facilityId");




        List<GenericValue> productionList = EntityQuery.use(delegator).from("WorkEffortAndGoods").where(
                "workEffortTypeId", "PROD_ORDER_HEADER","facilityId",facilityId).orderBy("-createdDate").queryList();



        List<Map<String,Object>> returnList = new ArrayList<Map<String, Object>>();


        String workEffortName = null;
        String workEffortId   = null;
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String beforeWorkEffort = null;




        if(productionList.size()>0){
            for(GenericValue gv : productionList){

                Map<String,Object> rowMap = new HashMap<String, Object>();
                List<Map<String,Object>> supplierProductList = new ArrayList<Map<String, Object>>();

                workEffortId = gv.getString("workEffortId");

                String productId = gv.getString("productId");
                GenericValue parentProduct = delegator.findOne("Product", false, UtilMisc.toMap("productId",productId));
                String productionProductName = parentProduct.getString("productName");
                String detailImageUrl =   parentProduct.getString("detailImageUrl");
                String statusId  = gv.getString("statusId");
                String currentStatusId  = gv.getString("currentStatusId");

                rowMap.put("statusId",statusId);

                rowMap.put("currentStatusId",currentStatusId);
                rowMap.put("workEffortId",workEffortId);

                String sdfDate = sdf.format(gv.get("createdDate"));
                //生产数量
                Double estimatedQuantity = (Double) gv.get("estimatedQuantity");
                BigDecimal quantityToProduce = (BigDecimal) gv.get("quantityToProduce");

                rowMap.put("productionDate",sdfDate);

                rowMap.put("estimatedQuantity",estimatedQuantity);
                rowMap.put("quantityToProduce",quantityToProduce);

                rowMap.put("workEffortName", "生产["+productionProductName+"] X ("+estimatedQuantity+") ");
                rowMap.put("productName",productionProductName);
                rowMap.put("detailImageUrl",detailImageUrl);

                GenericValue workEffort = EntityQuery.use(delegator).from("WorkEffort").where(
                        "workEffortId",workEffortId).queryFirst();
                List<GenericValue> childWorkEfforts  = workEffort.getRelated("ChildWorkEffort", UtilMisc.toMap("workEffortTypeId", "PROD_ORDER_TASK"), UtilMisc.toList("priority"), false);
                GenericValue  childWorkEffort = childWorkEfforts.get(0);
                String childWorkEffortId = childWorkEffort.getString("workEffortId");

                List<GenericValue> childWorkEffortGoods = EntityQuery.use(delegator).from("WorkEffortAndGoods").where(
                        "workEffortId",childWorkEffortId).orderBy("-createdDate").queryList();
                if(childWorkEffortGoods.size()>0){
                    for(GenericValue childGoods : childWorkEffortGoods){

                        Map<String,Object> rowProductMap = new HashMap<String, Object>();
                        String childProductId = childGoods.getString("productId");
                        GenericValue childProduct = delegator.findOne("Product", false, UtilMisc.toMap("productId",childProductId));
                        Double childEstimatedQuantity = (Double) childGoods.get("estimatedQuantity");
                        detailImageUrl =   childProduct.getString("detailImageUrl");

                        rowProductMap.put("compProductId",childProductId);
                        rowProductMap.put("childWorkEffortId",childWorkEffortId);
                        rowProductMap.put("productName",childProduct.getString("productName"));
                        rowProductMap.put("estimatedQuantity",childEstimatedQuantity);
                        rowProductMap.put("detailImageUrl",detailImageUrl);


                        List<GenericValue>  suppliers = EntityQuery.use(delegator).from("SupplierProduct").where(
                                "productId", childProductId).queryList();
                        List<Map<String,Object>> supplierList = new ArrayList<Map<String, Object>>();
                        if(suppliers.size()>0){
                            for(GenericValue supplier : suppliers){
                                Map<String,Object> rowSupplier = new HashMap<String, Object>();

                                String supplierPartyId = supplier.getString("partyId");

                                Map<String,String> supplierInfo =  queryBomPersonBaseInfo(delegator, supplierPartyId, partyId);
                                rowSupplier.put("name",supplierInfo.get("aliasCompanyName")+"-"+supplierInfo.get("aliasName") );
                                rowSupplier.put("supplierInfo", supplierInfo);
                                rowSupplier.put("partyId",supplierPartyId);

//                                GenericValue partyGroup =  EntityQuery.use(delegator).from("PartyGroup").where(
//                                        "partyId", supplierPartyId).queryFirst();
//
//                                if(null!= partyGroup){
//                                    rowSupplier.put("supplierName",partyGroup.getString("groupName"));
//                                    rowSupplier.put("supplierPartyId",supplierPartyId);
//                                }else{
//                                    GenericValue person =  EntityQuery.use(delegator).from("Person").where(
//                                            "partyId", supplierPartyId).queryFirst();
//                                    if(null!=person){
//                                        rowSupplier.put("supplierName",person.getString("lastName")+person.getString("firstName"));
//                                        rowSupplier.put("supplierPartyId",supplierPartyId);
//                                    }
//                                }

                                supplierList.add(rowSupplier);
                            }
                        }

                        rowProductMap.put("supplierList",supplierList);





                        String uomId = childProduct.getString("quantityUomId");
                        GenericValue uom =  EntityQuery.use(delegator).from("Uom").where(
                                "uomId", uomId).queryFirst();
                        String uomDescription = uom.getString("description");
                        String cndescription = UtilProperties.getMessage(resourceUiLabels, "Uom.description." + uomId, new Locale("zh"));
                        rowProductMap.put("uomDescription",cndescription.indexOf("Uom.description")>-1?uomDescription:cndescription);


                        supplierProductList.add(rowProductMap);
                    }
                }



                rowMap.put("supplierProductList",supplierProductList);

                returnList.add(rowMap);
            }
        }


        resultMap.put("productionRunList",returnList);
        return resultMap;
    }


    /**
     * queryMyInfo
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryMyInfo(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = userLogin.getString("partyId");

        Map<String,Object> returnMap =new HashMap<String, Object>();

        GenericValue teleContact = EntityQuery.use(delegator).from("TelecomNumberAndPartyView").where("partyId", partyId).queryFirst();


        if (null != teleContact) {
            String contactNumber = (String) teleContact.get("contactNumber");
            returnMap.put("contactTel", contactNumber);
        }


        GenericValue relation = EntityQuery.use(delegator).from("PartyRelationship").where(
                "partyIdFrom", partyId, "partyRelationshipTypeId", "OWNER" ).queryFirst();

        String partyGroupId = relation.getString("partyIdTo");

        GenericValue partyGroup = EntityQuery.use(delegator).from("PartyGroup").where(
                "partyId", partyGroupId).queryFirst();

        String groupName = partyGroup.getString("groupName");

        returnMap.put("partyGroupId",partyGroupId);
        returnMap.put("groupName",groupName);
        returnMap.put("roleType","OWNER");

        resultMap.put("userInfo",returnMap);
        return resultMap;
    }




    /**
     * Query MyOrderList
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryMyOrderList(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = userLogin.getString("partyId");

        String orderTypeId = (String) context.get("orderTypeId");


        List<GenericValue> orderList = EntityQuery.use(delegator).from("ProductAndRole").where(
                "partyId", partyId, "roleTypeId", "ADMIN", "productTypeId", "RAW_MATERIAL").orderBy("-fromDate").queryList();

        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");



        return resultMap;
    }

    /**
     * 查询原辅料
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryMyRawMaterials(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = userLogin.getString("partyId");

        List<GenericValue> productList = EntityQuery.use(delegator).from("ProductAndRole").where(
                "partyId", partyId, "roleTypeId", "ADMIN","productTypeId","RAW_MATERIAL").orderBy("-fromDate").queryList();

        List<Map<String,Object>> returnList = new ArrayList<Map<String, Object>>();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if(productList.size()>0){
            for(GenericValue gv : productList){
                Map<String,Object> rowMap = new HashMap<String, Object>();
                String productId = gv.getString("productId");
                rowMap.put("productId",productId);


                rowMap.put("productName",gv.getString("productName"));
                rowMap.put("imagePath",gv.getString("detailImageUrl"));
                rowMap.put("createdDate",sdf.format(gv.get("createdDate")));
                String uomId = gv.getString("quantityUomId");
                rowMap.put("quantityUomId",uomId);

                GenericValue uom =  EntityQuery.use(delegator).from("Uom").where(
                        "uomId", uomId).queryFirst();

                String uomDescription = uom.getString("description");
                rowMap.put("description",uomDescription);

                String cndescription = UtilProperties.getMessage(resourceUiLabels, "Uom.description." + uomId, new Locale("zh"));
                rowMap.put("uomDescription",cndescription.indexOf("Uom.description")>-1?uomDescription:cndescription);


                List<GenericValue> suppliers = EntityQuery.use(delegator).from("SupplierProduct").where(
                        "productId", productId).queryList();

                List<Map<String,Object>> supplierList = new ArrayList<Map<String, Object>>();
                if(null!=suppliers&&suppliers.size()>0){
                    for(GenericValue supplier : suppliers){
                        Map<String,Object> rowSupplier = new HashMap<String, Object>();
                        String supplierPartyId = supplier.getString("partyId");


                        Map<String,String> supplierInfo =  queryBomPersonBaseInfo(delegator, supplierPartyId, partyId);
                        rowSupplier.put("name",supplierInfo.get("aliasCompanyName")+"-"+supplierInfo.get("aliasName") );
                        rowSupplier.put("supplierInfo", supplierInfo);
                        rowSupplier.put("partyId",supplierPartyId);
//                        GenericValue partyGroup =  EntityQuery.use(delegator).from("PartyGroup").where(
//                                "partyId", supplierPartyId).queryFirst();
//                        //说明该线索人还没登录验证过
//                        if(null ==partyGroup){
//                            GenericValue person =  EntityQuery.use(delegator).from("Person").where(
//                                    "partyId", supplierPartyId).queryFirst();
//                            rowSupplier.put("partyId",supplierPartyId);
//                            rowSupplier.put("name",person.getString("lastName")+person.getString("firstName"));
//                        }else{
//                            rowSupplier.put("partyId",supplierPartyId);
//                            rowSupplier.put("name",partyGroup.getString("groupName"));
//                        }

                        supplierList.add(rowSupplier);
                    }
                }

                rowMap.put("supplierList",supplierList);

                returnList.add(rowMap);
            }
        }

        resultMap.put("rawMaterialsList",returnList);
        return resultMap;
    }


    /**
     * queryMyFinishedGood
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryMyFinishedGood(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = userLogin.getString("partyId");

        List<GenericValue> productList = EntityQuery.use(delegator).from("ProductAndRole").where(
                "partyId", partyId, "roleTypeId", "ADMIN","productTypeId","FINISHED_GOOD").orderBy("-fromDate").queryList();

        List<Map<String,Object>> returnList = new ArrayList<Map<String, Object>>();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if(productList.size()>0){
            for(GenericValue gv : productList){
                Map<String,Object> rowMap = new HashMap<String, Object>();
                String productId = gv.getString("productId");

                String uomParentId = gv.getString("quantityUomId");


                GenericValue uomParent =  EntityQuery.use(delegator).from("Uom").where(
                        "uomId", uomParentId).queryFirst();
                if(null!=uomParent){
                    String uomDescription = uomParent.getString("description");
                    rowMap.put("description",uomDescription);
                    rowMap.put("uomId",uomParentId);
                    String cndescription = UtilProperties.getMessage(resourceUiLabels, "Uom.description." + uomParentId, new Locale("zh"));
                    rowMap.put("zh_description",cndescription.indexOf("Uom.description")>-1?uomDescription:cndescription);
                }



                rowMap.put("productId",productId);

                rowMap.put("productName",gv.getString("productName"));
                rowMap.put("imagePath",gv.getString("detailImageUrl"));
                rowMap.put("createdDate",sdf.format(gv.get("createdDate")));


                List<GenericValue> manufComponents  = EntityQuery.use(delegator).from("ProductAssoc").where(
                        "productId", productId,"productAssocTypeId","MANUF_COMPONENT").queryList();

                List<Map<String,Object>> manuList =new ArrayList<Map<String, Object>>();
                if(null!=manufComponents&&manufComponents.size()>0){
                    for(GenericValue row : manufComponents){
                        Map<String,Object> rowComponent = new HashMap<String, Object>();
                        String rowProductId = row.getString("productIdTo");
                        BigDecimal quantity  = (BigDecimal)row.get("quantity");
                        rowComponent.put("productId",rowProductId);
                        rowComponent.put("quantity",quantity.intValue());
                        GenericValue rowProd = EntityQuery.use(delegator).from("Product").where(
                                "productId", rowProductId).queryFirst();
                        rowComponent.put("productName",rowProd.getString("productName"));
                        rowComponent.put("imagePath",rowProd.getString("detailImageUrl"));
                        rowComponent.put("fromDate",sdf.format(row.get("fromDate")));


                        String uomId = rowProd.getString("quantityUomId");


                         GenericValue uom =  EntityQuery.use(delegator).from("Uom").where(
                                 "uomId", uomId).queryFirst();
                        if(null!=uom){
                        String uomDescription = uom.getString("description");
                        rowComponent.put("manufComponentDescription",uomDescription);
                        String cndescription = UtilProperties.getMessage(resourceUiLabels, "Uom.description." + uomId, new Locale("zh"));
                        rowComponent.put("manufComponentZhDescription",cndescription.indexOf("Uom.description")>-1?uomDescription:cndescription);
                        }



                        manuList.add(rowComponent);
                    }
                }
                rowMap.put("manuList",manuList);

                returnList.add(rowMap);
            }
        }

        resultMap.put("finishedGoodList",returnList);
        return resultMap;
    }



    public static Map<String, Object> queryQuantityUom(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();


//        List<String> types = new ArrayList<String>();
//        types.add("CURRENCY_MEASURE");
//
//        EntityCondition findConditions = EntityCondition
//                .makeCondition("uomTypeId", EntityOperator.NOT_IN, types);
        EntityCondition findConditions = EntityCondition
                .makeCondition("uomTypeId", EntityOperator.EQUALS, "BOM_MEASURE");

        List<GenericValue> uomList = EntityQuery.use(delegator).from("Uom").where(findConditions).orderBy("-createdStamp").queryList();

        List<Map<String,Object>> returnList = new ArrayList<Map<String, Object>>();

            for(GenericValue gv : uomList){
                Map<String,Object> rowMap = new HashMap<String, Object>();
                String uomId = gv.getString("uomId");

                String abbreviation = gv.getString("abbreviation");

                rowMap.put("uomId",uomId);
                String description = gv.getString("description");
                rowMap.put("description",description);

                String cndescription = UtilProperties.getMessage(resourceUiLabels, "Uom.description." + uomId, new Locale("zh"));
                rowMap.put("zh_description",cndescription.indexOf("Uom.description")>-1?description:cndescription);
                rowMap.put("abbreviation",abbreviation);


                returnList.add(rowMap);
            }


        resultMap.put("uomList",returnList);
        return resultMap;
    }


    /**
     * queryMySupplierList
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryMySupplierList(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = userLogin.getString("partyId");



        List<Map<String,Object>> returnList = new ArrayList<Map<String, Object>>();

        List<GenericValue> relationList = EntityQuery.use(delegator).from("PartyRelationshipAndContactMechDetail").where(
                "partyIdFrom", partyId, "roleTypeIdTo", "LEAD","partyRelationshipTypeId","LEAD_OWNER").orderBy("-fromDate").queryList();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String beforePartyId = null;
        if(relationList.size()>0){
              for(GenericValue gv : relationList){
                Map<String,Object> rowMap = new HashMap<String, Object>();
                String partyIdTo = gv.getString("partyIdTo");

                  String contactMechTypeId = gv.getString("contactMechTypeId");

                Debug.logInfo("PartyRelationshipAndContactMechDetail=>:"+gv,module);


                if(beforePartyId!=null && beforePartyId.equals(partyIdTo)){
                    rowMap = returnList.get(returnList.size()-1);
                    String tnContactNumber = gv.getString("tnContactNumber");
                    if(null!=tnContactNumber){
                        rowMap.put("tnContactNumber", gv.getString("tnContactNumber"));
                    }
                    returnList.remove(returnList.size()-1);
                    returnList.add(rowMap);
                    continue;
                }

                  if(beforePartyId!=null   && !beforePartyId.equals(partyIdTo)){
                      if(contactMechTypeId.equals("TELECOM_NUMBER")){
                          String tnContactNumber = gv.getString("tnContactNumber");
                          if(null!=tnContactNumber){
                              rowMap.put("tnContactNumber", gv.getString("tnContactNumber"));
                          }
                      }
                  }


                Map<String,String> supplierInfo =  queryBomPersonBaseInfo(delegator, partyIdTo, partyId);
                 rowMap.put("name",supplierInfo.get("aliasCompanyName")+"-"+supplierInfo.get("aliasName") );
                rowMap.put("supplierInfo",supplierInfo);
                rowMap.put("partyId",partyIdTo);
                  String paAddress1 =  gv.getString("paAddress1");
                  if(null!=paAddress1){
                      rowMap.put("paAddress1",paAddress1);
                  }
//                rowMap.put("tel",supplierInfo.get("userLoginId"));
                rowMap.put("avatar",supplierInfo.get("headPortrait"));
                rowMap.put("orderSize","0");
                rowMap.put("fromDate",sdf.format(gv.get("fromDate")));
                  if (UtilValidate.isEmpty(rowMap.get("tnContactNumber"))){
                      rowMap.put("tnContactNumber", supplierInfo.get("contactNumber"));
                  }
                returnList.add(rowMap);
                  beforePartyId=partyIdTo;
            }
        }

      resultMap.put("supplierList",returnList);
        return resultMap;
    }


}
