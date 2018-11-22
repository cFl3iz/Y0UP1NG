package main.java.com.banfftech.wechatminiprogram;

import net.sf.json.JSONObject;
import main.java.com.banfftech.personmanager.PersonManagerQueryServices;
import main.java.com.banfftech.platformmanager.constant.PeConstant;
import org.apache.ofbiz.entity.GenericEntity;
import org.apache.ofbiz.base.util.collections.PagedList;
import com.auth0.jwt.JWTExpiredException;
import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;
import org.apache.ofbiz.base.util.Debug;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.apache.ofbiz.product.product.ProductWorker;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.entity.util.EntityUtil;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;
import org.apache.ofbiz.entity.util.EntityUtilProperties;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.util.EntityFindOptions;
import org.apache.ofbiz.entity.condition.EntityConditionList;
import org.apache.ofbiz.entity.condition.EntityOperator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.geom.GeneralPath;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static main.java.com.banfftech.personmanager.PersonManagerQueryServices.module;
import static main.java.com.banfftech.personmanager.PersonManagerQueryServices.queryPersonAddressInfo;
import static main.java.com.banfftech.personmanager.PersonManagerQueryServices.queryPersonBaseInfo;
import static main.java.com.banfftech.platformmanager.util.UtilTools.dateToStr;

/**
 * Created by S on 2017/11/20.
 */
public class WeChatOrderQueryServices {


    public final static String module = WeChatOrderQueryServices.class.getName();


    public static Map<String, Object> queryClientList(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        GenericValue userLogin = (GenericValue) context.get("userLogin");

        //销售代表的PartyId
        String partyId = userLogin.getString("partyId");

        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();

        List<GenericValue> partyRelationships = EntityQuery.use(delegator).from("PartyRelationship").where(
                "partyIdTo", partyId,"partyRelationshipTypeId","AGENT").queryList();
        Map<String,Object> queryFilter = new HashMap<String, Object>();
        if(null!= partyRelationships){
            for(GenericValue gv : partyRelationships){
                String partyIdFrom = gv.getString("partyIdFrom");
                if(!queryFilter.containsKey(partyIdFrom)){
                    Map<String,Object> rowMap =new HashMap<String, Object>();
                    rowMap.put("clientInfo",queryPersonBaseInfo(delegator,partyIdFrom));
                    GenericValue store = EntityQuery.use(delegator).from("ProductStore").where("payToPartyId", partyIdFrom).queryFirst();
                    rowMap.put("productStoreId",store.getString("productStoreId"));
                    rowMap.put("partyIdFrom",partyIdFrom);
                    returnList.add(rowMap);
                    queryFilter.put(partyIdFrom,null);
                }
            }
        }

        resultMap.put("clientList",returnList);
        return resultMap;
    }




    /**
     * QuerySku
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> querySku(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));


        String productId = (String) context.get("productId");
        String color = (String) context.get("color");
        String size = (String) context.get("size");

        if (color == null && size == null || color.trim().equals("") && size.trim().equals("")) {
            //说明是SKU查自己库存 , 就不需要往下进行

            GenericValue category = EntityQuery.use(delegator).from("ProductAndCategoryMember").where("productId", productId).queryFirst();
            String productStoreId = category.getString("productStoreId");
            GenericValue store = EntityQuery.use(delegator).from("ProductStore").where("productStoreId", productStoreId).queryFirst();
            String inventoryFacilityId = store.getString("inventoryFacilityId");
            //获得库存信息 getInventoryAvailableByFacility
            Map<String, Object> getInventoryAvailableByFacilityMap = dispatcher.runSync("getInventoryAvailableByFacility", UtilMisc.toMap("userLogin", admin,
                    "facilityId", inventoryFacilityId, "productId", productId));
            if (ServiceUtil.isSuccess(getInventoryAvailableByFacilityMap)) {
                BigDecimal quantityOnHandTotal = (BigDecimal) getInventoryAvailableByFacilityMap.get("quantityOnHandTotal");
                BigDecimal availableToPromiseTotal = (BigDecimal) getInventoryAvailableByFacilityMap.get("availableToPromiseTotal");
                resultMap.put("quantityOnHandTotal", quantityOnHandTotal.intValue() + "");
                resultMap.put("availableToPromiseTotal", availableToPromiseTotal.intValue() + "");
            }

            resultMap.put("sku", productId);

            return resultMap;
        }

        Debug.logInfo("productId: " + productId, module);
        String virId = productId.substring(0, productId.indexOf("-"));
        Debug.logInfo("virId: " + virId, module);

        //0181BA04-44-F
        List<GenericValue> skus = EntityQuery.use(delegator).from("ProductAssoc").where("productId", virId).queryList();
        Debug.logInfo("skus: " + skus, module);

        String skuId = "";
        if (skus != null && skus.size() > 0) {
            for (GenericValue sku : skus) {
                Debug.logInfo("Row SKU : " + sku, module);
                GenericValue isExsitsColor = EntityQuery.use(delegator).from("ProductFeatureAndAppl").where("productId", sku.getString("productIdTo"), "productFeatureTypeId", "COLOR", "description", color).queryFirst();
                GenericValue isExsitsSize = EntityQuery.use(delegator).from("ProductFeatureAndAppl").where("productId", sku.getString("productIdTo"), "productFeatureTypeId", "SIZE", "description", size).queryFirst();

                if (isExsitsColor != null && isExsitsSize != null) {
                    skuId = sku.getString("productIdTo");
                }
            }
        }

        Debug.logInfo("Find SKU : " + skuId, module);
        GenericValue category = EntityQuery.use(delegator).from("ProductAndCategoryMember").where("productId", skuId).queryFirst();
        String productStoreId = category.getString("productStoreId");
        GenericValue store = EntityQuery.use(delegator).from("ProductStore").where("productStoreId", productStoreId).queryFirst();
        String inventoryFacilityId = store.getString("inventoryFacilityId");
        //获得库存信息 getInventoryAvailableByFacility
        Map<String, Object> getInventoryAvailableByFacilityMap = dispatcher.runSync("getInventoryAvailableByFacility", UtilMisc.toMap("userLogin", admin,
                "facilityId", inventoryFacilityId, "productId", skuId));
        if (ServiceUtil.isSuccess(getInventoryAvailableByFacilityMap)) {
            resultMap.put("quantityOnHandTotal", getInventoryAvailableByFacilityMap.get("quantityOnHandTotal") + "");
            resultMap.put("availableToPromiseTotal", getInventoryAvailableByFacilityMap.get("availableToPromiseTotal") + "");
        }

        resultMap.put("sku", skuId);
        return resultMap;
    }


    /**
     * Query OrderCpsReport
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryOrderCpsReport(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        GenericValue userLogin = (GenericValue) context.get("userLogin");

        //销售代表的PartyId
        String partyId = userLogin.getString("partyId");
        String statusId = (String) context.get("statusId");
        String year = (String) context.get("year");
        List<Map<String, Object>> returnOrderList = new ArrayList<Map<String, Object>>();

        //年度销售总额
        Double allOrderGrandTotal = 0.0;
        //年度订单总量
        int orderCount = 0;

        //月销售总额
        Double moGrandTotal = 0.0;

        EntityCondition findConditions = EntityCondition.makeCondition("roleTypeId", EntityOperator.EQUALS, "SALES_REP");
        EntityCondition findConditions2 = EntityCondition.makeCondition("partyId", EntityOperator.EQUALS, partyId);
        EntityCondition genericCondition = EntityCondition.makeCondition(findConditions, EntityOperator.AND, findConditions2);
        EntityCondition findConditions3 = EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, statusId);
        EntityCondition findConditions4 = EntityCondition.makeCondition(genericCondition, EntityOperator.AND, findConditions3);


        //以月份查询
        for (int m = 1; m <= 12; m++) {

            Map<String, Object> mothMap = new HashMap<String, Object>();

            String greaterStr = year + "-" + (m == 1 ? "01" : m) + "-01";
            String lessStr = year + "-" + (m + 1) + "-01";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            long tsg = 0;
            long tsl = 0;
            try {
                Date dateGreater = simpleDateFormat.parse(greaterStr);
                Date dateLess = simpleDateFormat.parse(lessStr);
                tsg = (long) dateGreater.getTime();
                tsl = (long) dateLess.getTime();
            } catch (ParseException px) {
                px.printStackTrace();
            }

            EntityCondition findConditionsDateGreater = EntityCondition.makeCondition("orderDate", EntityOperator.GREATER_THAN, org.apache.ofbiz.base.util.UtilDateTime.getTimestamp(tsg));
            EntityCondition findConditionsDateLess = EntityCondition.makeCondition("orderDate", EntityOperator.LESS_THAN, org.apache.ofbiz.base.util.UtilDateTime.getTimestamp(tsl));
            EntityCondition findConditions5 = EntityCondition.makeCondition(findConditionsDateGreater, EntityOperator.AND, findConditionsDateLess);
            EntityCondition findConditions6 = EntityCondition.makeCondition(findConditions5, EntityOperator.AND, findConditions4);

            List<GenericValue> orderList = delegator.findList("OrderHeaderAndRoles", findConditions6, null, UtilMisc.toList("-orderDate"), null, false);
            moGrandTotal = 0.0;
            if (null != orderList && orderList.size() > 0) {
                List<Map<String, Object>> rowList = new ArrayList<Map<String, Object>>();
                for (GenericValue gv : orderList) {
                    Map<String, Object> rowMap = new HashMap<String, Object>();
                    String orderId = gv.getString("orderId");
                    GenericValue orderItem = EntityQuery.use(delegator).from("OrderItem").where("orderId", orderId).queryFirst();
                    GenericValue orderHeaderAndRoles = EntityQuery.use(delegator).from("OrderHeaderAndRoles").where("orderId", orderId, "roleTypeId", "BILL_TO_CUSTOMER").queryFirst();
                    GenericValue orderHeader = EntityQuery.use(delegator).from("OrderHeader").where("orderId", orderId).queryFirst();
                    String productId = orderItem.getString("productId");
                    GenericValue product = EntityQuery.use(delegator).from("Product").where("productId", productId).queryFirst();
                    String productName = (String) product.get("productName");
                    BigDecimal grandTotal = (BigDecimal) orderHeader.get("grandTotal");
                    DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String orderDateStr = "";
                    try {
                        orderDateStr = sdf.format(orderHeader.get("orderDate"));
                    } catch (Exception e) {
                    }
                    rowMap.put("productName", productName);
                    rowMap.put("price", grandTotal);
                    rowMap.put("orderDate", orderDateStr);
                    String orderCustPartyId = orderHeaderAndRoles.getString("partyId");
                    Map<String, String> personInfoMap = queryPersonBaseInfo(delegator, orderCustPartyId);
                    rowMap.put("orderCustInfo", personInfoMap);
                    rowList.add(rowMap);
                    //总数增加
                    allOrderGrandTotal = allOrderGrandTotal + (grandTotal.doubleValue());
                    moGrandTotal = moGrandTotal + (grandTotal.doubleValue());
                    orderCount += 1;
                }
                mothMap.put("data", rowList);
                mothMap.put("monthGrandTotal", moGrandTotal);
            } else {
                mothMap.put("data", null);
            }

            returnOrderList.add(mothMap);
        }

        resultMap.put("orderList", returnOrderList);
        resultMap.put("allOrderGrandTotal", allOrderGrandTotal + "");
        resultMap.put("orderCount", orderCount + "");
        return resultMap;
    }

    /**
     * 返回今年的月份列表
     *
     * @param year
     * @return
     */
    private static List<String> getMList(String year) {

        List<String> mList = new ArrayList<String>();


        return mList;
    }


    /**
     * Query Share CpsReport
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryShareCpsReport(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        GenericValue userLogin = (GenericValue) context.get("userLogin");

        //销售代表的PartyId
        String partyId = userLogin.getString("partyId");

        List<GenericValue> productShareList = EntityQuery.use(delegator).from("WorkEffortPartyAssignAndRoleType").where("roleTypeId", "REFERRER", "partyId", partyId).queryList();

        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
        //分享总计
        int shareCount = 0;
        //总销售
        int salesRepCount = 0;
        if (productShareList != null && productShareList.size() > 0) {

            for (GenericValue gv : productShareList) {
                Map<String, Object> rowMap = new HashMap<String, Object>();
                String workEffortId = gv.getString("workEffortId");
                GenericValue workEffortProduct = EntityQuery.use(delegator).from("WorkEffortProductGoods").where("workEffortId", workEffortId).queryFirst();
                GenericValue workEffort = EntityQuery.use(delegator).from("WorkEffort").where("workEffortId", workEffortId).queryFirst();
                rowMap.put("backWorkEffortId", workEffortId);
                String rowShareCount = workEffort.getString("shareCount");
                int rowShare = Integer.parseInt(rowShareCount == null ? "0" : rowShareCount);
                shareCount += rowShare;
                String productId = workEffortProduct.getString("productId");
                GenericValue productAndPriceView = EntityQuery.use(delegator).from("ProductAndPriceView").where("productId", productId).queryFirst();
                rowMap.put("productInfo", productAndPriceView);

                //产品分享者列表
                List<Map<String, Object>> productPartys = new ArrayList<Map<String, Object>>();
                List<GenericValue> workEffortPartyRoleAndProduct =
                        EntityQuery.use(delegator).from("WorkEffortPartyRoleAndProduct").
                                where("productId", productId, "roleTypeId", "SALES_REP", "partyId", partyId, "description", productId + partyId).queryList();
                if (null != workEffortPartyRoleAndProduct) {
                    for (GenericValue rowGeneric : workEffortPartyRoleAndProduct) {

                        String innerWorkEffort = rowGeneric.getString("workEffortId");
                        rowMap.put("innerWorkEffort", innerWorkEffort);
                        List<GenericValue> referrerRoles = EntityQuery.use(delegator).from("WorkEffortPartyAssignAndRoleType").where("roleTypeId", "REFERRER", "workEffortId", innerWorkEffort).orderBy("-fromDate").queryPagedList(0, 5).getData();
                        for (int i = 0; i < referrerRoles.size(); i++) {

                            GenericValue referrer = (GenericValue) referrerRoles.get(i);

                            Map<String, Object> rowParty = new HashMap<String, Object>();
                            String rolePartyId = referrer.getString("partyId");
                            Map<String, String> rowPerson = new HashMap<String, String>();

                            if (!rolePartyId.equals(partyId)) {
//                                Debug.logInfo("roleParty="+rolePartyId+"|partyId="+partyId+"="+(rolePartyId.equals(partyId)),module);
                                rowPerson = queryPersonBaseInfo(delegator, rolePartyId);
                                rowParty.put("shareParty", rowPerson);
                                productPartys.add(rowParty);
                            }
                            //最近的一个人
                            if (i + 1 == workEffortPartyRoleAndProduct.size() && rowPerson != null && rowPerson.get("firstName") != null) {
                                rowMap.put("lastShareDesc", "刚刚" + rowPerson.get("firstName") + "帮你转发了");
                                DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                String fromDate = sdf.format(referrer.get("fromDate"));
                                rowMap.put("repotDeta", fromDate);
                            }
                        }


                    }
                }
                rowMap.put("productSharePartys", productPartys);
                rowMap.put("addressCount", workEffort.get("addressCount") == null ? "0" : workEffort.get("addressCount"));
                rowMap.put("refreCount", workEffort.get("shareCount") == null ? "0" : workEffort.get("shareCount"));
                EntityCondition findConditions = null;
                if (productId.indexOf("-") > 0) {
                    findConditions = EntityCondition.makeCondition("productId", EntityOperator.LIKE, "%" + productId.substring(0, productId.indexOf("-")) + "%");
                } else {
                    findConditions = EntityCondition.makeCondition("productId", EntityOperator.EQUALS, productId);
                }
                EntityCondition findConditions2 = EntityCondition.makeCondition("roleTypeId", EntityOperator.EQUALS, "SALES_REP");
                EntityCondition findConditions3 = EntityCondition.makeCondition("partyId", EntityOperator.EQUALS, partyId);
                EntityCondition genericCondition = EntityCondition.makeCondition(findConditions, EntityOperator.AND, findConditions2);
                EntityCondition findConditions4 = EntityCondition.makeCondition(genericCondition, EntityOperator.AND, findConditions3);


                List<GenericValue> queryMyResourceOrderList = delegator.findList("OrderHeaderItemAndRoles",
                        findConditions4, null,
                        null, null, false);
                //   EntityOperator.LIKE

                rowMap.put("salesRepOrderCount", queryMyResourceOrderList == null ? "0" : queryMyResourceOrderList.size() + "");

                returnList.add(rowMap);
            }
        }

        Long salesOrderCount = EntityQuery.use(delegator).from("OrderHeaderItemAndRoles").where("roleTypeId", "SALES_REP", "partyId", partyId).queryCount();
        salesRepCount += Integer.parseInt(salesOrderCount + "");

        resultMap.put("shareInfoList", returnList);
        resultMap.put("total", returnList.size() + "");

        resultMap.put("shareCount", shareCount + "");
        resultMap.put("salesRepCount", salesRepCount + "");

        return resultMap;
    }


    /**
     * GetSkuFromProductFeatureDesc
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> getSkuFromProductFeatureDesc(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));
        String productFeatureSelect = (String) context.get("productFeatureSelect");
        String virtualId = (String) context.get("virtualId");

        String color = productFeatureSelect.substring(productFeatureSelect.indexOf("=") + 1, productFeatureSelect.indexOf(","));
        String size = productFeatureSelect.substring(productFeatureSelect.lastIndexOf("=") + 1);
        //ProductVirtualAndVariantInfo
        List<GenericValue> productVirtualAndVariantInfo =
                EntityQuery.use(delegator).from("ProductVirtualAndVariantInfo")
                        .where("productId", virtualId).queryList();
        String variantId = "";
        Debug.logInfo("productVirtualAndVariantInfo=" + productVirtualAndVariantInfo, module);
        if (null != productVirtualAndVariantInfo) {
            for (GenericValue gv : productVirtualAndVariantInfo) {
                String productFeatureTypeId = (String) gv.get("productFeatureTypeId");
                String description = (String) gv.get("description");
                Debug.logInfo("productFeatureTypeId=" + productFeatureTypeId, module);
                Debug.logInfo("description=" + description, module);
                Debug.logInfo("color=" + color, module);
                Debug.logInfo("description.equals(color.trim())=" + description.equals(color.trim()), module);
                if (productFeatureTypeId.equals("COLOR") && description.equals(color.trim())) {
                    String variantProductId = (String) gv.get("variantProductId");
                    Debug.logInfo("variantProductId=" + variantProductId, module);
                    for (GenericValue gv2 : productVirtualAndVariantInfo) {
                        if (variantProductId.equals((String) gv2.get("variantProductId")) && ((String) gv2.get("productFeatureTypeId")).equals("SIZE") && ((String) gv2.get("description")).equals(size.trim())) {
                            variantId = (String) gv2.get("variantProductId");
                        }
                    }
                }


            }
        }


        Debug.logInfo("color=" + color + "|size=" + size, module);

        resultMap.put("productId", variantId);

        return resultMap;
    }


    /**
     * 查询小程序的应用配置内容数据等
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryMiniAppConfig(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        String appId = (String) context.get("appId");
        Debug.logInfo("-> APP_ID: " + appId, module);


        Map<String, List<String>> returnMap = new HashMap<String, List<String>>();

        GenericValue queryAppConfig = EntityQuery.use(delegator).from("PartyStoreAppConfig").where(
                "idValue", appId).queryFirst();

        // 店铺ID
        String productStoreId = queryAppConfig.getString("productStoreId");
        // 组织ID
        String compPartyId = EntityQuery.use(delegator).from("ProductStore").where(
                "productStoreId", productStoreId).queryFirst().getString("payToPartyId");


        Set<String> fieldSet = new HashSet<String>();
        fieldSet.add("partyId");
        fieldSet.add("partyContentTypeId");
        fieldSet.add("dataResourceId");

        EntityCondition findConditions = EntityCondition
                .makeCondition("partyContentTypeId", EntityOperator.LIKE, "MINIPROGRAM" + "%");

        EntityCondition findConditions2 = EntityCondition
                .makeCondition("partyId", compPartyId);
        EntityCondition findConditions3 = EntityCondition
                .makeCondition(findConditions, EntityOperator.AND, findConditions2);

        List<GenericValue> appContentList = delegator.findList("PartyContentAndDataResource",
                findConditions3, fieldSet, null, null, true);


        if (null != appContentList) {
            for (GenericValue content : appContentList) {
                List<String> rowList = new ArrayList<String>();

                String contentType = content.getString("partyContentTypeId");

                String dataResourceId = content.getString("dataResourceId");

                if (returnMap.containsKey(contentType)) {
                    rowList = returnMap.get(contentType);
                }
                if (contentType.equals("MINIPROGRAM_TBAR")) {
                    String textData = EntityQuery.use(delegator).from("ElectronicText").where(
                            "dataResourceId", dataResourceId).queryFirst().getString("textData");
                    rowList.add(textData);
                }


                if (contentType.equals("MINIPROGRAM_HOME")) {
                    String textData = EntityQuery.use(delegator).from("ElectronicText").where(
                            "dataResourceId", dataResourceId).queryFirst().getString("textData");
                    rowList.add(textData);
                }

                if (!contentType.equals("MINIPROGRAM_TBAR") && !contentType.equals("MINIPROGRAM_HOME")) {
                    String objectInfo = EntityQuery.use(delegator).from("DataResource").where(
                            "dataResourceId", dataResourceId).queryFirst().getString("objectInfo");
                    rowList.add(objectInfo);
                }

                if (!returnMap.containsKey(contentType)) {
                    returnMap.put(contentType, rowList);
                }

            }
        }


        resultMap.put("appContentDataResource", returnMap);

        return resultMap;
    }


    /**
     * Query ProductStoreAndRole
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryProductStoreAndRole(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));
        String openId = (String) context.get("openId");
        String appId = (String) context.get("appId");
        GenericValue partyIdentification = EntityQuery.use(delegator).from("PartyIdentification").where("idValue", openId, "partyIdentificationTypeId", "WX_MINIPRO_OPEN_ID").queryFirst();

        String partyId = partyIdentification.getString("partyId");

        String productStoreId = "";
        String prodCatalogId = "";
        String appServiceType = "2B";
        String isDemoStore = "false";
        String hasShoppingCart = "false";
        String groupName = "";

        Debug.logInfo("-> APP_ID: " + appId, module);


        if (appId != null) {
            // 查询小程序配置
            GenericValue queryAppConfig =
                    EntityQuery.use(delegator).from("PartyStoreAppConfig").where(
                            "idValue", appId).queryFirst();
            appServiceType = queryAppConfig.getString("appServiceType");
            isDemoStore = queryAppConfig.getString("isDemoStore");
            hasShoppingCart = queryAppConfig.getString("hasShoppingCart");
            productStoreId = queryAppConfig.getString("productStoreId");
            groupName = queryAppConfig.getString("groupName");

//            // (友评2C) wx299644ef4c9afbde
//            if (PeConstant.ZUCZUG_MINI_PROGRAM_APP_ID.equals(appId.trim())) {
//                productStoreId = "ZUCZUGSTORE";
//            }
//            //素然小程序(素然)wx1106576d138cd8e6
//            if (PeConstant.ZUCZUG_ANKORAU_MINI_PROGRAM_APP_ID.equals(appId.trim())) {
//                productStoreId = "ZUCZUGSTORE";
//            }
//            //Demo小程序wxe8b50001e5ea4383
//            if (PeConstant.DEMO_WECHAT_MINI_PROGRAM_APP_ID.equals(appId.trim())) {
////                GenericValue store = EntityQuery.use(delegator).from("ProductStore").where(UtilMisc.toMap("payToPartyId", partyIdentification.getString("partyId"))).queryFirst();
////                productStoreId = (String) store.get("productStoreId");
//                //暂时先用素然的
//                productStoreId = "ZUCZUGSTORE";
//            }
//            //不分梨白酒 wx119831dae45a3f3f
//            if (PeConstant.BUFENLI_MINI_PROGRAM_APP_ID.equals(appId.trim())) {
//                productStoreId = "KANGCHENGSTORE";
//            }
        }
        GenericValue role = EntityQuery.use(delegator).from("ProductStoreRole").where("productStoreId", productStoreId, "partyId", partyIdentification.get("partyId"), "roleTypeId", "SALES_REP").queryFirst();

        // TO C 的情况下 拿自己的 ProductStoreID
        if (!appServiceType.toUpperCase().equals("2B")) {
            productStoreId = (String) EntityQuery.use(delegator).from("ProductStore").where("payToPartyId", partyId).queryFirst().get("productStoreId");
            if (null == role) {
                Map<String, Object> createProductStoreRoleOutMap = dispatcher.runSync("createProductStoreRole", UtilMisc.toMap("userLogin", admin,
                        "partyId", partyId, "productStoreId", productStoreId, "roleTypeId", "SALES_REP"));
            }
            resultMap.put("isSalesRep", "true");

            //To C 可能有media pay
            GenericValue media = EntityQuery.use(delegator).from("PartyAttribute").where("partyId", partyId, "attrName", "media_id").queryFirst();

            if (null != media) {
                resultMap.put("pay_media", "https://personerp.oss-cn-hangzhou.aliyuncs.com/datas/pay_qr_code/" + media.getString("attrValue") + ".png");
            }


        } else {
            if (null == role) {
                resultMap.put("isSalesRep", "false");
            } else {
                resultMap.put("isSalesRep", "true");
            }
        }

        EntityCondition findConditionsStore = EntityCondition.makeCondition(UtilMisc.toMap("productStoreId", productStoreId));

        // List<GenericValue> storeList = EntityQuery.use(delegator).from("ProductStoreRoleAndStoreDetail").where("partyId", partyIdentification.get("partyId")).queryList();

        List<GenericValue> storeList = delegator.findList("ProductStoreCatalog",
                findConditionsStore, null,
                UtilMisc.toList("fromDate"), null, false);
        if (null != storeList && storeList.size() > 0) {

            prodCatalogId = (String) storeList.get(0).get("prodCatalogId");
            resultMap.put("onlineProdCatalogs", storeList);
        }

        Debug.logInfo("-> storeList: " + storeList, module);


        // Query  ProductStorePromoAndAppl  & ProductPromoAction
        List<Map<String, Object>> returnPromos = new ArrayList<Map<String, Object>>();
        List<GenericValue> storePromoAndAction = EntityQuery.use(delegator).from("StorePromoAndAction").where("productStoreId", productStoreId).queryList();

        if (null != storePromoAndAction && storePromoAndAction.size() > 0) {
            for (GenericValue promo : storePromoAndAction) {
                Map<String, Object> rowMap = new HashMap<String, Object>();
                String productPromoId = promo.getString("productPromoId");
                String promoName = promo.getString("promoName");

                if (promo.get("amount") == null || UtilValidate.isEmpty(promo.get("amount"))) {
                    //maybe action service
                    continue;
                }

                String amount = promo.get("amount") + "";
                GenericValue cond = EntityQuery.use(delegator).from("ProductPromoCond").where("productPromoId", productPromoId).queryFirst();
                String condValue = cond.get("condValue") + "";

                rowMap.put("promoName", promoName);
                rowMap.put("conditionValue", condValue);

                rowMap.put("discount", (Double.parseDouble(amount + "")) * 0.01);

                returnPromos.add(rowMap);
            }
        }

        resultMap.put("partyId", partyIdentification.getString("partyId"));
        resultMap.put("prodCatalogId", prodCatalogId);
        resultMap.put("productStoreId", productStoreId);
        resultMap.put("storePromos", returnPromos);

        resultMap.put("appServiceType", appServiceType);
        resultMap.put("isDemoStore", isDemoStore);
        resultMap.put("hasShoppingCart", hasShoppingCart);
        resultMap.put("appName", groupName);

        GenericValue forwardChainFact = EntityQuery.use(delegator).from("YpForwardChainFact").where(
                "basePartyId", partyId).queryFirst();

        if (null == forwardChainFact) {

            String fromPartyId = "NO_PARTY";
            String basePartyId = partyId;
            String workEffortId = "NA";
            String partyIdTo = "NO_PARTY";
            Map<String, String> userInfo = queryPersonBaseInfo(delegator, partyId);
            dispatcher.runSync("inForwardChainFact", UtilMisc.toMap(
                    "userLogin", admin,
                    "partyIdFrom", fromPartyId,
                    "partyIdTo", partyIdTo,
                    "workEffortId", workEffortId,
                    "basePartyId", basePartyId,
                    "firstName", userInfo.get("firstName"),
                    "objectInfo", userInfo.get("headPortrait"),
                    "createDate", new Timestamp(new Date().getTime())));
        }

        return resultMap;
    }


    /**
     * queryCatalogSkuProductDetail
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryCatalogSkuProductDetail(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));
        String productId = (String) context.get("productId");
        GenericValue product = EntityQuery.use(delegator).from("Product").where("productId", productId).queryFirst();
        Map<String, Object> hiddenMap = new HashMap<String, Object>();

        Map<String, Object> allField = product.getAllFields();

        String desc = (String) allField.get("description");

        List<GenericValue> skus = new ArrayList<GenericValue>();

        if (null != desc && !desc.equals("")) {
            desc = desc.replaceAll("/", "\n/");
        }
        allField.put("description", desc);

        //用虚拟产品随便找一个sku变形去拿价格 , fix 其实自己就是sku
        String vir_productId = (String) product.get("productId");
        // GenericValue sku_product   = EntityQuery.use(delegator).from("ProductAssoc").where("productId",vir_productId).queryFirst();
        GenericValue productPrice = EntityQuery.use(delegator).from("ProductPrice").where("productId", vir_productId).queryFirst();
        allField.put("price", productPrice.get("price"));


        GenericValue productOnePrice = EntityQuery.use(delegator).from("ProductPrice").where("productId", vir_productId, "productPriceTypeId", "MINIMUM_PRICE").queryFirst();
        if (null != productOnePrice) {
            allField.put("oneMouthPrice", productOnePrice.get("price"));
        }

        GenericValue productTwoPrice = EntityQuery.use(delegator).from("ProductPrice").where("productId", vir_productId, "productPriceTypeId", "ONE_MOUTH_PRICE").queryFirst();
        if (null != productTwoPrice) {
            allField.put("twoMouthPrice", productTwoPrice.get("price"));
        }

        GenericValue vir_product = EntityQuery.use(delegator).from("ProductAssoc").where("productIdTo", productId).queryFirst();
        String[] imgAttr = new String[]{
                product.getString("detailImageUrl")};

        Map<String, String> isExsitsPath = new HashMap<String, String>();

        if (vir_product != null) {

            //获取SPU的尺码规格
            GenericValue productContentAndElectronicText = EntityQuery.use(delegator).from("ProductContentAndElectronicText").where("productId", productId.substring(0, productId.indexOf("-"))).queryFirst();
            Debug.logInfo("productId+:" + productId.substring(0, productId.indexOf("-")), module);
            Debug.logInfo("productContentAndElectronicText:" + productContentAndElectronicText, module);
            List<String> spuSpecTitleList = new ArrayList<String>();
            List<Map<String, String>> spuSpecRowList = new ArrayList<Map<String, String>>();
            if (null != productContentAndElectronicText) {
                String textData = productContentAndElectronicText.getString("textData");
                String title = textData.substring(0, textData.indexOf("-"));
                String rowData = textData.substring(textData.indexOf("-") + 1);
                String[] titleArray = title.split(",");
                String[] rowDataArray = rowData.split(",");
                for (String strTitle : titleArray) {
                    spuSpecTitleList.add(strTitle);
                }
                int titleLen = spuSpecTitleList.size();
                int rowCount = 1;
                Map<String, String> rowDataMap = new HashMap<String, String>();
                for (String strRow : rowDataArray) {
                    if (rowCount == titleLen) {
                        rowDataMap.put("code" + rowCount, strRow);
                        spuSpecRowList.add(rowDataMap);
                        //初始化
                        rowCount = 1;
                        rowDataMap = new HashMap<String, String>();
                    } else {
                        rowDataMap.put("code" + rowCount, strRow);
                        rowCount++;
                    }
                }
            }

            String rowVirId = (String) vir_product.get("productId");


            skus = EntityQuery.use(delegator).from("ProductAssoc").where("productIdTo", productId).queryList();

            Set<String> fieldSet = new HashSet<String>();

            fieldSet.add("drObjectInfo");
            fieldSet.add("productId");
            fieldSet.add("productContentTypeId");


            List<Map<String, Object>> pictures = new ArrayList<Map<String, Object>>();


            Map<String, Map<String, Object>> featureMap = new HashMap<String, Map<String, Object>>();

            for (GenericValue rowSku : skus) {
                String rowSkuId = rowSku.getString("productIdTo");
                GenericValue rowProduct = EntityQuery.use(delegator).from("Product").where("productId", rowSkuId).queryFirst();
                // 不管怎么样 这个变形产品得有图
                String detailImageUrl = rowProduct.getString("detailImageUrl");
                Debug.logInfo("detailImageUrl:" + detailImageUrl, module);
                //如果没有图的默认不看 针对zuczug
                if (detailImageUrl.indexOf("DEFAULT_PRODUCT") >= 0) {
                    GenericValue rowColor = EntityQuery.use(delegator).from("ProductFeatureAndAppl").where("productId", rowSkuId, "productFeatureTypeId", "COLOR").queryFirst();
                    hiddenMap.put(rowSkuId, rowColor.getString("productFeatureId"));
                }

                //1 查询搭配图
                EntityCondition genericProductConditions = EntityCondition.makeCondition("productId", EntityOperator.EQUALS, rowSkuId);
                EntityCondition matchTypeConditions = EntityCondition.makeCondition("productContentTypeId", EntityOperator.EQUALS, "MATCH_PRODUCT_IMAGE");
                EntityCondition matchCondition = EntityCondition.makeCondition(genericProductConditions, EntityOperator.AND, matchTypeConditions);
                List<GenericValue> matchPictures = delegator.findList("ProductContentAndInfo", matchCondition, fieldSet,
                        null, null, false);
                if (matchPictures != null && matchPictures.size() > 0) {
                    for (GenericValue pict : matchPictures) {
                        Map<String, Object> rowMap = new HashMap<String, Object>();
                        String drObjectInfo = (String) pict.get("drObjectInfo");
                        if (!isExsitsPath.containsKey(drObjectInfo)) {
                            isExsitsPath.put(drObjectInfo, "");
                            rowMap.put("drObjectInfo", drObjectInfo);
                            pictures.add(rowMap);
                        }
                    }
                    allField.put("matchPictures", matchPictures);
                }
                //2 查询单品图
                EntityCondition singleTypeConditions = EntityCondition.makeCondition("productContentTypeId", EntityOperator.EQUALS, "SINGLE_PRODUCT_IMAGE");
                EntityCondition singleCondition = EntityCondition.makeCondition(singleTypeConditions, EntityOperator.AND, genericProductConditions);
                List<GenericValue> singlePictures = delegator.findList("ProductContentAndInfo", singleCondition, fieldSet,
                        null, null, false);
                GenericValue rowColor = EntityQuery.use(delegator).from("ProductFeatureAndAppl").where("productId", rowSkuId, "productFeatureTypeId", "COLOR").queryFirst();

                if (singlePictures != null && singlePictures.size() > 0) {
                    int sigleIndex = 0;
                    for (GenericValue pict : singlePictures) {
                        Map<String, Object> rowMap = new HashMap<String, Object>();
                        String drObjectInfo = (String) pict.get("drObjectInfo");
                        if (!isExsitsPath.containsKey(drObjectInfo)) {
                            isExsitsPath.put(drObjectInfo, "");
                            rowMap.put("drObjectInfo", drObjectInfo);
                            pictures.add(rowMap);
                            if (sigleIndex == 0) {
                                featureMap.put(rowColor.get("description") + "", rowMap);
                                sigleIndex++;
                            }
                        }

                    }
                    allField.put("singlePictures", singlePictures);

                } else {
                    featureMap.put(rowColor == null ? "普通" : rowColor.get("description") + "", UtilMisc.toMap("drObjectInfo", "https://personerp.oss-cn-hangzhou.aliyuncs.com/datas/serviceSales/3333.jpg"));
                }

                //3 查询细节图
                EntityCondition detailTypeConditions = EntityCondition.makeCondition("productContentTypeId", EntityOperator.EQUALS, "DETAIL_PRODUCT_IMAGE");
                EntityCondition detailCondition = EntityCondition.makeCondition(detailTypeConditions, EntityOperator.AND, genericProductConditions);
                List<GenericValue> detailPictures = delegator.findList("ProductContentAndInfo", detailCondition, fieldSet,
                        null, null, false);
                if (detailPictures != null && detailPictures.size() > 0) {
                    List<Map<String, Object>> detailList = new ArrayList<Map<String, Object>>();
                    for (GenericValue pict : detailPictures) {
                        Map<String, Object> rowMap = new HashMap<String, Object>();
                        String drObjectInfo = (String) pict.get("drObjectInfo");

                        if (drObjectInfo.indexOf("end.jpg") > -1) {
                            allField.put("endImage", drObjectInfo);
                        } else {
                            rowMap.put("drObjectInfo", drObjectInfo);
                            detailList.add(rowMap);
                        }
//                        if (!isExsitsPath.containsKey(drObjectInfo)) {
//                            isExsitsPath.put(drObjectInfo, "");
//                            rowMap.put("drObjectInfo", drObjectInfo);
//                            pictures.add(rowMap);
//                        }
                    }
                    allField.put("detailPictures", detailList);
                }

            }

            Debug.logInfo("QUERY DETAIL pictures:" + pictures, module);
            if (pictures != null && pictures.size() > 0) {
                imgAttr = new String[pictures.size()];
                int index = 0;
                for (Map<String, Object> productContent : pictures) {
                    String drObjectInfo = (String) productContent.get("drObjectInfo");
                    imgAttr[index] = drObjectInfo;
                    index++;
                }
            }


            List<GenericValue> gvs = EntityQuery.use(delegator).from("ProductFeatureAndAppl").where("productId", rowVirId, "productFeatureCategoryId", "PRODUCT_SIZE").queryList();


            List<Map<String, Object>> productFeatureList = new ArrayList<Map<String, Object>>();
            for (GenericValue gv : gvs) {

                Map<String, Object> innerMap = new HashMap<String, Object>();
                String productFeatureId = gv.getString("productFeatureId");
                String innerAttr = gv.getString("productFeatureTypeId");
                String innerDesc = gv.getString("description");

                //还没有图片的变形产品 不显示该特征型号
                if (hiddenMap != null && hiddenMap.size() > 0 && isHiddenSku(productFeatureId, innerAttr, rowVirId, hiddenMap, delegator, skus)) {
                    continue;
                }


                switch (innerAttr) {
                    case "COLOR": {
                        innerMap.put("COLOR_DESC", innerDesc);
                        break;
                    }
                    case "SIZE": {
                        innerMap.put("SIZE_DESC", innerDesc);
                        break;
                    }
                    default: {
                        break;
                    }
                }

                productFeatureList.add(innerMap);

            }
            GenericValue colorFeature = EntityQuery.use(delegator).from("ProductFeatureAndAppl").where("productId", productId, "productFeatureCategoryId", "PRODUCT_COLOR").queryFirst();
            Map<String, Object> innerMap = new HashMap<String, Object>();
            String innerDesc = colorFeature.getString("description");
            innerMap.put("COLOR_DESC", innerDesc);
            productFeatureList.add(innerMap);


            //图片数组
//            List<String> list = new ArrayList<String>();
//            if(imgAttr.length>0){
//                for( int i = 0 ; i< imgAttr.length;i++){
//                    String str = imgAttr[i];
//                    if(str.indexOf(".jpg")>0){
//                        list.add(str);
//                    }
//                }
//            }
            allField.put("imgArray", imgAttr);
            //所有特征
            allField.put("features", productFeatureList);
            //特征切换图片
            allField.put("featureMap", featureMap);


            allField.put("spuSpecTitleList", spuSpecTitleList);
            allField.put("spuSpecRowList", spuSpecRowList);
        }


        GenericValue category = EntityQuery.use(delegator).from("ProductAndCategoryMember").where("productId", productId).queryFirst();
        //写死
        String productStoreId = "ZUCZUGSTORE";
        String prodCatalogId = "";

        productStoreId = category.getString("productStoreId");
        GenericValue prodCatalog = EntityQuery.use(delegator).from("ProductStoreCatalog").where("productStoreId", productStoreId).queryFirst();
        prodCatalogId = prodCatalog.getString("prodCatalogId");


        GenericValue store = EntityQuery.use(delegator).from("ProductStore").where("productStoreId", productStoreId).queryFirst();
        String inventoryFacilityId = store.getString("inventoryFacilityId");
        //获得库存信息 getInventoryAvailableByFacility
        Map<String, Object> getInventoryAvailableByFacilityMap = dispatcher.runSync("getInventoryAvailableByFacility", UtilMisc.toMap("userLogin", admin,
                "facilityId", inventoryFacilityId, "productId", productId));
        if (ServiceUtil.isSuccess(getInventoryAvailableByFacilityMap)) {
            allField.put("quantityOnHandTotal", getInventoryAvailableByFacilityMap.get("quantityOnHandTotal"));
            allField.put("availableToPromiseTotal", getInventoryAvailableByFacilityMap.get("availableToPromiseTotal"));
        }


        resultMap.put("productDetail", allField);
        resultMap.put("productStoreId", productStoreId);
        resultMap.put("prodCatalogId", prodCatalogId);


        return resultMap;
    }


    public static Map<String, Object> queryNeiMaiCatalogProductDetail(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));
        String productId = (String) context.get("productId");
        GenericValue product = EntityQuery.use(delegator).from("Product").where("productId", productId).queryFirst();
        Map<String, Object> hiddenMap = new HashMap<String, Object>();

        Map<String, Object> allField = product.getAllFields();

        String desc = (String) allField.get("description");

        List<GenericValue> skus = new ArrayList<GenericValue>();

        if (null != desc && !desc.equals("")) {
            desc = desc.replaceAll("/", "\n/");
        }
        allField.put("description", desc);

        //用虚拟产品随便找一个sku变形去拿价格 , fix 其实自己就是sku
        String vir_productId = (String) product.get("productId");
        // GenericValue sku_product   = EntityQuery.use(delegator).from("ProductAssoc").where("productId",vir_productId).queryFirst();
        GenericValue productPrice = EntityQuery.use(delegator).from("ProductPrice").where("productId", vir_productId).queryFirst();
        allField.put("price", productPrice.get("price"));


        GenericValue productOnePrice = EntityQuery.use(delegator).from("ProductPrice").where("productId", vir_productId, "productPriceTypeId", "MINIMUM_PRICE").queryFirst();
        if (null != productOnePrice) {
            allField.put("oneMouthPrice", productOnePrice.get("price"));
        }

        GenericValue productTwoPrice = EntityQuery.use(delegator).from("ProductPrice").where("productId", vir_productId, "productPriceTypeId", "ONE_MOUTH_PRICE").queryFirst();
        if (null != productTwoPrice) {
            allField.put("twoMouthPrice", productTwoPrice.get("price"));
        }

        GenericValue vir_product = EntityQuery.use(delegator).from("ProductAssoc").where("productIdTo", productId).queryFirst();
        String[] imgAttr = new String[]{
                product.getString("detailImageUrl")};

        Map<String, String> isExsitsPath = new HashMap<String, String>();

        if (vir_product != null) {

            //获取SPU的尺码规格
            GenericValue productContentAndElectronicText = EntityQuery.use(delegator).from("ProductContentAndElectronicText").where("productId", productId.substring(0, productId.indexOf("-"))).queryFirst();
            Debug.logInfo("productId+:" + productId.substring(0, productId.indexOf("-")), module);
            Debug.logInfo("productContentAndElectronicText:" + productContentAndElectronicText, module);
            List<String> spuSpecTitleList = new ArrayList<String>();
            List<Map<String, String>> spuSpecRowList = new ArrayList<Map<String, String>>();
            if (null != productContentAndElectronicText) {
                String textData = productContentAndElectronicText.getString("textData");
                String title = textData.substring(0, textData.indexOf("-"));
                String rowData = textData.substring(textData.indexOf("-") + 1);
                String[] titleArray = title.split(",");
                String[] rowDataArray = rowData.split(",");
                for (String strTitle : titleArray) {
                    spuSpecTitleList.add(strTitle);
                }
                int titleLen = spuSpecTitleList.size();
                int rowCount = 1;
                Map<String, String> rowDataMap = new HashMap<String, String>();
                for (String strRow : rowDataArray) {
                    if (rowCount == titleLen) {
                        rowDataMap.put("code" + rowCount, strRow);
                        spuSpecRowList.add(rowDataMap);
                        //初始化
                        rowCount = 1;
                        rowDataMap = new HashMap<String, String>();
                    } else {
                        rowDataMap.put("code" + rowCount, strRow);
                        rowCount++;
                    }
                }
            }

            String rowVirId = (String) vir_product.get("productId");


            skus = EntityQuery.use(delegator).from("ProductAssoc").where("productId", rowVirId, "productIdTo", productId).queryList();

            Set<String> fieldSet = new HashSet<String>();

            fieldSet.add("drObjectInfo");
            fieldSet.add("productId");
            fieldSet.add("productContentTypeId");


            List<Map<String, Object>> pictures = new ArrayList<Map<String, Object>>();


            Map<String, Map<String, Object>> featureMap = new HashMap<String, Map<String, Object>>();

            for (GenericValue rowSku : skus) {
                String rowSkuId = rowSku.getString("productIdTo");
                GenericValue rowProduct = EntityQuery.use(delegator).from("Product").where("productId", rowSkuId).queryFirst();
                // 不管怎么样 这个变形产品得有图
                String detailImageUrl = rowProduct.getString("detailImageUrl");
                Debug.logInfo("detailImageUrl:" + detailImageUrl, module);
                //如果没有图的默认不看 针对zuczug
                if (detailImageUrl.indexOf("DEFAULT_PRODUCT") >= 0) {
                    GenericValue rowColor = EntityQuery.use(delegator).from("ProductFeatureAndAppl").where("productId", rowSkuId, "productFeatureTypeId", "COLOR").queryFirst();
                    hiddenMap.put(rowSkuId, rowColor.getString("productFeatureId"));
                }

                //1 查询搭配图
                EntityCondition genericProductConditions = EntityCondition.makeCondition("productId", EntityOperator.EQUALS, rowSkuId);
                EntityCondition matchTypeConditions = EntityCondition.makeCondition("productContentTypeId", EntityOperator.EQUALS, "MATCH_PRODUCT_IMAGE");
                EntityCondition matchCondition = EntityCondition.makeCondition(genericProductConditions, EntityOperator.AND, matchTypeConditions);
                List<GenericValue> matchPictures = delegator.findList("ProductContentAndInfo", matchCondition, fieldSet,
                        null, null, false);
                if (matchPictures != null && matchPictures.size() > 0) {
                    for (GenericValue pict : matchPictures) {
                        Map<String, Object> rowMap = new HashMap<String, Object>();
                        String drObjectInfo = (String) pict.get("drObjectInfo");
                        if (!isExsitsPath.containsKey(drObjectInfo)) {
                            isExsitsPath.put(drObjectInfo, "");
                            rowMap.put("drObjectInfo", drObjectInfo);
                            pictures.add(rowMap);
                        }
                    }
                }
                //2 查询单品图
                EntityCondition singleTypeConditions = EntityCondition.makeCondition("productContentTypeId", EntityOperator.EQUALS, "SINGLE_PRODUCT_IMAGE");
                EntityCondition singleCondition = EntityCondition.makeCondition(singleTypeConditions, EntityOperator.AND, genericProductConditions);
                List<GenericValue> singlePictures = delegator.findList("ProductContentAndInfo", singleCondition, fieldSet,
                        null, null, false);
                GenericValue rowColor = EntityQuery.use(delegator).from("ProductFeatureAndAppl").where("productId", rowSkuId, "productFeatureTypeId", "COLOR").queryFirst();

                if (singlePictures != null && singlePictures.size() > 0) {
                    int sigleIndex = 0;
                    for (GenericValue pict : singlePictures) {
                        Map<String, Object> rowMap = new HashMap<String, Object>();
                        String drObjectInfo = (String) pict.get("drObjectInfo");
                        if (!isExsitsPath.containsKey(drObjectInfo)) {
                            isExsitsPath.put(drObjectInfo, "");
                            rowMap.put("drObjectInfo", drObjectInfo);
                            pictures.add(rowMap);
                            if (sigleIndex == 0) {
                                featureMap.put(rowColor.get("description") + "", rowMap);
                                sigleIndex++;
                            }
                        }

                    }
                } else {
                    featureMap.put(rowColor == null ? "普通" : rowColor.get("description") + "", UtilMisc.toMap("drObjectInfo", "https://personerp.oss-cn-hangzhou.aliyuncs.com/datas/serviceSales/3333.jpg"));
                }

                //3 查询细节图
                EntityCondition detailTypeConditions = EntityCondition.makeCondition("productContentTypeId", EntityOperator.EQUALS, "DETAIL_PRODUCT_IMAGE");
                EntityCondition detailCondition = EntityCondition.makeCondition(detailTypeConditions, EntityOperator.AND, genericProductConditions);
                List<GenericValue> detailPictures = delegator.findList("ProductContentAndInfo", detailCondition, fieldSet,
                        null, null, false);
                if (detailPictures != null && detailPictures.size() > 0) {
                    for (GenericValue pict : detailPictures) {
                        Map<String, Object> rowMap = new HashMap<String, Object>();
                        String drObjectInfo = (String) pict.get("drObjectInfo");
                        if (!isExsitsPath.containsKey(drObjectInfo)) {
                            isExsitsPath.put(drObjectInfo, "");
                            rowMap.put("drObjectInfo", drObjectInfo);
                            pictures.add(rowMap);
                        }
                    }
                }

            }

            Debug.logInfo("QUERY DETAIL pictures:" + pictures, module);
            if (pictures != null && pictures.size() > 0) {
                imgAttr = new String[pictures.size()];
                int index = 0;
                for (Map<String, Object> productContent : pictures) {
                    String drObjectInfo = (String) productContent.get("drObjectInfo");
                    imgAttr[index] = drObjectInfo;
                    index++;
                }
            }


            List<GenericValue> gvs = EntityQuery.use(delegator).from("ProductFeatureAndAppl").where("productId", rowVirId, "productFeatureCategoryId", "PRODUCT_SIZE").queryList();


            List<Map<String, Object>> productFeatureList = new ArrayList<Map<String, Object>>();
            for (GenericValue gv : gvs) {

                Map<String, Object> innerMap = new HashMap<String, Object>();
                String productFeatureId = gv.getString("productFeatureId");
                String innerAttr = gv.getString("productFeatureTypeId");
                String innerDesc = gv.getString("description");

                //还没有图片的变形产品 不显示该特征型号
                if (hiddenMap != null && hiddenMap.size() > 0 && isHiddenSku(productFeatureId, innerAttr, rowVirId, hiddenMap, delegator, skus)) {
                    continue;
                }


                switch (innerAttr) {
                    case "COLOR": {
                        innerMap.put("COLOR_DESC", innerDesc);
                        break;
                    }
                    case "SIZE": {
                        innerMap.put("SIZE_DESC", innerDesc);
                        break;
                    }
                    default: {
                        break;
                    }
                }

                productFeatureList.add(innerMap);

            }
            GenericValue colorFeature = EntityQuery.use(delegator).from("ProductFeatureAndAppl").where("productId", productId, "productFeatureCategoryId", "PRODUCT_COLOR").queryFirst();
            Map<String, Object> innerMap = new HashMap<String, Object>();
            String innerDesc = colorFeature.getString("description");
            innerMap.put("COLOR_DESC", innerDesc);
            productFeatureList.add(innerMap);
            //图片数组

            List<String> list = new ArrayList<String>();
            for (int i=0; i<imgAttr.length; i++) {
                if(imgAttr[i].indexOf("jpg")>0){
                    Debug.logInfo("imgAttr[i]:"+imgAttr[i],module);
                    list.add(imgAttr[i]);
                }
            }

            String[] newStr =  list.toArray(new String[1]); //返回一个包含所有对象的指定类型的数组

            allField.put("imgArray", newStr);
            //所有特征
            allField.put("features", productFeatureList);
            //特征切换图片
            allField.put("featureMap", featureMap);


            allField.put("spuSpecTitleList", spuSpecTitleList);
            allField.put("spuSpecRowList", spuSpecRowList);
        }


        GenericValue category = EntityQuery.use(delegator).from("ProductAndCategoryMember").where("productId", productId).queryFirst();
        //写死
        String productStoreId = "ZUCZUGSTORE";
        String prodCatalogId = "";

        productStoreId = category.getString("productStoreId");
        GenericValue prodCatalog = EntityQuery.use(delegator).from("ProductStoreCatalog").where("productStoreId", productStoreId).queryFirst();
        prodCatalogId = prodCatalog.getString("prodCatalogId");


        GenericValue store = EntityQuery.use(delegator).from("ProductStore").where("productStoreId", productStoreId).queryFirst();
        String inventoryFacilityId = store.getString("inventoryFacilityId");
        //获得库存信息 getInventoryAvailableByFacility
        Map<String, Object> getInventoryAvailableByFacilityMap = dispatcher.runSync("getInventoryAvailableByFacility", UtilMisc.toMap("userLogin", admin,
                "facilityId", inventoryFacilityId, "productId", productId));
        if (ServiceUtil.isSuccess(getInventoryAvailableByFacilityMap)) {
            allField.put("quantityOnHandTotal", getInventoryAvailableByFacilityMap.get("quantityOnHandTotal"));
            allField.put("availableToPromiseTotal", getInventoryAvailableByFacilityMap.get("availableToPromiseTotal"));
        }


        resultMap.put("productDetail", allField);
        resultMap.put("productStoreId", productStoreId);
        resultMap.put("prodCatalogId", prodCatalogId);


        return resultMap;
    }

    /**
     * QueryCatalogProductDetail
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryCatalogProductDetail(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));
        String productId = (String) context.get("productId");
        GenericValue product = EntityQuery.use(delegator).from("Product").where("productId", productId).queryFirst();
        Map<String, Object> hiddenMap = new HashMap<String, Object>();

        Map<String, Object> allField = product.getAllFields();

        String desc = (String) allField.get("description");
        List<String> spuSpecTitleList = new ArrayList<String>();
        List<Map<String, String>> spuSpecRowList = new ArrayList<Map<String, String>>();
        List<GenericValue> skus = new ArrayList<GenericValue>();

        if (null != desc && !desc.equals("")) {
            desc = desc.replaceAll("/", "\n/");
        }
        allField.put("description", desc);

        //用虚拟产品随便找一个sku变形去拿价格 , fix 其实自己就是sku
        String vir_productId = (String) product.get("productId");
        // GenericValue sku_product   = EntityQuery.use(delegator).from("ProductAssoc").where("productId",vir_productId).queryFirst();
        GenericValue productPrice = EntityQuery.use(delegator).from("ProductPrice").where("productId", vir_productId).queryFirst();
        allField.put("price", productPrice.get("price"));


        GenericValue productOnePrice = EntityQuery.use(delegator).from("ProductPrice").where("productId", vir_productId, "productPriceTypeId", "MINIMUM_PRICE").queryFirst();
        if (null != productOnePrice) {
            allField.put("oneMouthPrice", productOnePrice.get("price"));
        }
        GenericValue productTwoPrice = EntityQuery.use(delegator).from("ProductPrice").where("productId", vir_productId, "productPriceTypeId", "ONE_MOUTH_PRICE").queryFirst();
        if (null != productTwoPrice) {
            allField.put("twoMouthPrice", productTwoPrice.get("price"));
        }

        GenericValue vir_product = EntityQuery.use(delegator).from("ProductAssoc").where("productIdTo", productId).queryFirst();
        String[] imgAttr = new String[]{
                product.getString("detailImageUrl")};

        Map<String, String> isExsitsPath = new HashMap<String, String>();

        if (vir_product != null) {
            GenericValue productContentAndElectronicText = null;
            if(productId.indexOf("-")>-1){
            //获取SPU的尺码规格
              productContentAndElectronicText = EntityQuery.use(delegator).from("ProductContentAndElectronicText").where("productId", productId.substring(0, productId.indexOf("-"))).queryFirst();
                Debug.logInfo("productId+:" + productId.substring(0, productId.indexOf("-")), module);
                Debug.logInfo("productContentAndElectronicText:" + productContentAndElectronicText, module);
            }


            if(productContentAndElectronicText!=null){


            String textData = productContentAndElectronicText.getString("textData");
            String title = textData.substring(0, textData.indexOf("-"));
            String rowData = textData.substring(textData.indexOf("-") + 1);
            String[] titleArray = title.split(",");
            String[] rowDataArray = rowData.split(",");
            for (String strTitle : titleArray) {
                spuSpecTitleList.add(strTitle);
            }
            int titleLen = spuSpecTitleList.size();
            int rowCount = 1;
            Map<String, String> rowDataMap = new HashMap<String, String>();
            for (String strRow : rowDataArray) {
                if (rowCount == titleLen) {
                    rowDataMap.put("code" + rowCount, strRow);
                    spuSpecRowList.add(rowDataMap);
                    //初始化
                    rowCount = 1;
                    rowDataMap = new HashMap<String, String>();
                } else {
                    rowDataMap.put("code" + rowCount, strRow);
                    rowCount++;
                }
            }
            }

            String rowVirId = (String) vir_product.get("productId");


            skus = EntityQuery.use(delegator).from("ProductAssoc").where("productId", rowVirId).queryList();

            Set<String> fieldSet = new HashSet<String>();

            fieldSet.add("drObjectInfo");
            fieldSet.add("productId");
            fieldSet.add("productContentTypeId");


            List<Map<String, Object>> pictures = new ArrayList<Map<String, Object>>();


            Map<String, Map<String, Object>> featureMap = new HashMap<String, Map<String, Object>>();

            for (GenericValue rowSku : skus) {
                String rowSkuId = rowSku.getString("productIdTo");
                GenericValue rowProduct = EntityQuery.use(delegator).from("Product").where("productId", rowSkuId).queryFirst();
                // 不管怎么样 这个变形产品得有图
                String detailImageUrl = rowProduct.getString("detailImageUrl");
                Debug.logInfo("detailImageUrl:" + detailImageUrl, module);
                //如果没有图的默认不看 针对zuczug
                if (detailImageUrl.indexOf("DEFAULT_PRODUCT") >= 0) {
                    GenericValue rowColor = EntityQuery.use(delegator).from("ProductFeatureAndAppl").where("productId", rowSkuId, "productFeatureTypeId", "COLOR").queryFirst();
                    hiddenMap.put(rowSkuId, rowColor.getString("productFeatureId"));
                }

                //1 查询搭配图
                EntityCondition genericProductConditions = EntityCondition.makeCondition("productId", EntityOperator.EQUALS, rowSkuId);
                EntityCondition matchTypeConditions = EntityCondition.makeCondition("productContentTypeId", EntityOperator.EQUALS, "MATCH_PRODUCT_IMAGE");
                EntityCondition matchCondition = EntityCondition.makeCondition(genericProductConditions, EntityOperator.AND, matchTypeConditions);
                List<GenericValue> matchPictures = delegator.findList("ProductContentAndInfo", matchCondition, fieldSet,
                        null, null, false);
                if (matchPictures != null && matchPictures.size() > 0) {
                    for (GenericValue pict : matchPictures) {
                        Map<String, Object> rowMap = new HashMap<String, Object>();
                        String drObjectInfo = (String) pict.get("drObjectInfo");
                        if (!isExsitsPath.containsKey(drObjectInfo)) {
                            isExsitsPath.put(drObjectInfo, "");
                            rowMap.put("drObjectInfo", drObjectInfo);
                            pictures.add(rowMap);
                        }
                    }
                }
                //2 查询单品图
                EntityCondition singleTypeConditions = EntityCondition.makeCondition("productContentTypeId", EntityOperator.EQUALS, "SINGLE_PRODUCT_IMAGE");
                EntityCondition singleCondition = EntityCondition.makeCondition(singleTypeConditions, EntityOperator.AND, genericProductConditions);
                List<GenericValue> singlePictures = delegator.findList("ProductContentAndInfo", singleCondition, fieldSet,
                        null, null, false);
                GenericValue rowColor = EntityQuery.use(delegator).from("ProductFeatureAndAppl").where("productId", rowSkuId, "productFeatureTypeId", "COLOR").queryFirst();

                if (singlePictures != null && singlePictures.size() > 0) {
                    int sigleIndex = 0;
                    for (GenericValue pict : singlePictures) {
                        Map<String, Object> rowMap = new HashMap<String, Object>();
                        String drObjectInfo = (String) pict.get("drObjectInfo");
                        if (!isExsitsPath.containsKey(drObjectInfo)) {
                            isExsitsPath.put(drObjectInfo, "");
                            rowMap.put("drObjectInfo", drObjectInfo);
                            pictures.add(rowMap);
                            if (sigleIndex == 0) {
                                if(rowColor!=null){
                                    featureMap.put(rowColor.get("description") + "", rowMap);
                                    sigleIndex++;
                                }
                            }
                        }

                    }
                } else {
                    featureMap.put(rowColor == null ? "普通" : rowColor.get("description") + "", UtilMisc.toMap("drObjectInfo", "https://personerp.oss-cn-hangzhou.aliyuncs.com/datas/serviceSales/3333.jpg"));
                }

                //3 查询细节图
                EntityCondition detailTypeConditions = EntityCondition.makeCondition("productContentTypeId", EntityOperator.EQUALS, "DETAIL_PRODUCT_IMAGE");
                EntityCondition detailCondition = EntityCondition.makeCondition(detailTypeConditions, EntityOperator.AND, genericProductConditions);
                List<GenericValue> detailPictures = delegator.findList("ProductContentAndInfo", detailCondition, fieldSet,
                        null, null, false);
                if (detailPictures != null && detailPictures.size() > 0) {
                    for (GenericValue pict : detailPictures) {
                        Map<String, Object> rowMap = new HashMap<String, Object>();
                        String drObjectInfo = (String) pict.get("drObjectInfo");
                        if (!isExsitsPath.containsKey(drObjectInfo)) {
                            isExsitsPath.put(drObjectInfo, "");
                            rowMap.put("drObjectInfo", drObjectInfo);
                            pictures.add(rowMap);
                        }
                    }
                }

            }

            Debug.logInfo("QUERY DETAIL pictures:" + pictures, module);
            if (pictures != null && pictures.size() > 0) {
                imgAttr = new String[pictures.size()];
                int index = 0;
                for (Map<String, Object> productContent : pictures) {
                    String drObjectInfo = (String) productContent.get("drObjectInfo");
                    imgAttr[index] = drObjectInfo;
                    index++;
                }
            }


            List<GenericValue> gvs = EntityQuery.use(delegator).from("ProductFeatureAndAppl").where("productId", rowVirId).queryList();
            List<Map<String, Object>> productFeatureList = new ArrayList<Map<String, Object>>();


//            GenericValue productRoleAdmin
//                    = EntityQuery.use(delegator).from("ProductRole").where(
//                    "roleTypeId", "ADMIN", "productId", rowVirId).queryFirst();
//            Debug.logInfo(">>productRoleAdmin:"+productRoleAdmin,module);
//            if(productRoleAdmin !=null){
//                String rowProductAdminId = productRoleAdmin.getString("partyId");
//                Debug.logInfo(">>rowProductAdminId:"+rowProductAdminId,module);
//                GenericValue salesStore = EntityQuery.use(delegator).from("ProductStore").where("payToPartyId", rowProductAdminId).queryFirst();
//                Debug.logInfo(">>salesStore:"+salesStore,module);
//                String rowProductStoreId = salesStore.getString("productStoreId");
//                allField.put("salesStoreId",rowProductStoreId);
//                allField.put("salesStoreName",salesStore.getString("storeName"));
//            }


            GenericValue beforeProduct
                    = EntityQuery.use(delegator).from("ProductAssoc").where("productAssocTypeId", "PRODUCT_OBSOLESCENCE", "productId", productId).queryFirst();
            if (beforeProduct != null) {
                String beforeProductId = beforeProduct.getString("productIdTo");
                GenericValue beforePrice = EntityQuery.use(delegator).from("ProductPrice").where("productId", beforeProductId).queryFirst();
                allField.put("beforePrice", beforePrice.get("price"));
                GenericValue productRoleAdmin
                        = EntityQuery.use(delegator).from("ProductRole").where(
                        "roleTypeId", "ADMIN", "productId", beforeProductId).queryFirst();

                Debug.logInfo(">>productRoleAdmin:" + productRoleAdmin, module);
                if (productRoleAdmin != null) {
                    String rowProductAdminId = productRoleAdmin.getString("partyId");
                    Debug.logInfo(">>rowProductAdminId:" + rowProductAdminId, module);
                    GenericValue salesStore = EntityQuery.use(delegator).from("ProductStore").where("payToPartyId", rowProductAdminId).queryFirst();
                    Debug.logInfo(">>salesStore:" + salesStore, module);
                    String rowProductStoreId = salesStore.getString("productStoreId");
                    allField.put("salesStoreId", rowProductStoreId);
                    allField.put("beforeProductId", beforeProductId);
                    allField.put("salesStoreName", salesStore.getString("storeName"));
                }


            }


            for (GenericValue gv : gvs) {

                Map<String, Object> innerMap = new HashMap<String, Object>();
                String productFeatureId = gv.getString("productFeatureId");
                String innerAttr = gv.getString("productFeatureTypeId");
                String innerDesc = gv.getString("description");

                //还没有图片的变形产品 不显示该特征型号
                if (hiddenMap != null && hiddenMap.size() > 0 && isHiddenSku(productFeatureId, innerAttr, rowVirId, hiddenMap, delegator, skus)) {
                    continue;
                }


                switch (innerAttr) {
                    case "COLOR": {
//                        GenericValue isSelect = EntityQuery.use(delegator).from("ProductFeatureAndAppl").where("productId", productId, "productFeatureTypeId", "COLOR", "description", innerDesc).queryFirst();
//                        if (isSelect != null) {
//                            innerMap.put("COLOR_DESC", innerDesc);
//                        } else {
//                            innerMap.put("COLOR_DESC", innerDesc);
//                        }
                        innerMap.put("COLOR_DESC", innerDesc);
                        break;
                    }
                    case "SIZE": {
//                        GenericValue isSelect = EntityQuery.use(delegator).from("ProductFeatureAndAppl").where("productId", productId, "productFeatureTypeId", "SIZE", "description", innerDesc).queryFirst();
//                        if (isSelect != null) {
//                            innerMap.put("SIZE_DESC", innerDesc);
//                        } else {
//                            innerMap.put("SIZE_DESC", innerDesc);
//                        }
                        innerMap.put("SIZE_DESC", innerDesc);
                        break;
                    }
                    default: {
                        break;
                    }
                }

                productFeatureList.add(innerMap);

            }

            List<String> list = new ArrayList<String>();
            for (int z=0; z<imgAttr.length;z++) {
                if(imgAttr[z].toLowerCase().indexOf("jpg")>0||imgAttr[z].toLowerCase().indexOf("png")>0){
                    Debug.logInfo("imgAttr[i]:"+imgAttr[z],module);
                    list.add(imgAttr[z]);
                }
            }

            String[] newStr =  list.toArray(new String[1]); //返回一个包含所有对象的指定类型的数组
            //图片数组
            allField.put("imgArray", newStr);
            //所有特征
            allField.put("features", productFeatureList);
            //特征切换图片
            allField.put("featureMap", featureMap);


            allField.put("spuSpecTitleList", spuSpecTitleList);
            allField.put("spuSpecRowList", spuSpecRowList);
        } else {
            //白酒写死
            if (productId.equals("KC2018050401")) {
                imgAttr = new String[]{
                        "https://personerp.oss-cn-hangzhou.aliyuncs.com/datas/product_img/DETAIL_PICTURE%403x.png"};
            }
            allField.put("imgArray", imgAttr);

        }


        GenericValue category = EntityQuery.use(delegator).from("ProductAndCategoryMember").where("productId", productId).orderBy("fromDate").queryFirst();
        //写死
        String productStoreId = "ZUCZUGSTORE";
        String prodCatalogId = "";
        if (category != null) {
            productStoreId = category.getString("productStoreId");
            GenericValue prodCatalog = EntityQuery.use(delegator).from("ProductStoreCatalog").where("productStoreId", productStoreId).queryFirst();
            prodCatalogId = prodCatalog.getString("prodCatalogId");
        } else {
            GenericValue productCategoryMember = EntityQuery.use(delegator).from("ProductCategoryMember").where("productId", productId).queryFirst();
            GenericValue prodCatalogCategory = EntityQuery.use(delegator).from("ProdCatalogCategory").where("productCategoryId", productCategoryMember.getString("productCategoryId")).queryFirst();
            prodCatalogId = prodCatalogCategory.getString("prodCatalogId");
        }


        GenericValue store = EntityQuery.use(delegator).from("ProductStore").where("productStoreId", productStoreId).queryFirst();
        String inventoryFacilityId = store.getString("inventoryFacilityId");
        //获得库存信息 getInventoryAvailableByFacility
        if (null != inventoryFacilityId) {

            Map<String, Object> getInventoryAvailableByFacilityMap = dispatcher.runSync("getInventoryAvailableByFacility", UtilMisc.toMap("userLogin", admin,
                    "facilityId", inventoryFacilityId, "productId", productId));
            if (ServiceUtil.isSuccess(getInventoryAvailableByFacilityMap)) {
                allField.put("quantityOnHandTotal", getInventoryAvailableByFacilityMap.get("quantityOnHandTotal"));
                allField.put("availableToPromiseTotal", getInventoryAvailableByFacilityMap.get("availableToPromiseTotal"));
            }
        }


        // 2 c 找media_id
        GenericValue media = EntityQuery.use(delegator).from("PartyAttribute").where("partyId", store.getString("payToPartyId"), "attrName", "media_id").queryFirst();

        if (null != media) {
            allField.put("media_id", media.getString("attrValue"));
        }


        //查询店铺配置
        GenericValue queryAppConfig =
                EntityQuery.use(delegator).from("PartyStoreAppConfig").where(
                        "productStoreId", productStoreId).queryFirst();
        String appServiceType = "2B";
        if (null != queryAppConfig) {
            appServiceType = queryAppConfig.getString("appServiceType");
        } else {
            //店铺没查到数据 说明是卖家自己的店
            appServiceType = "2C";
        }
        // 2C
        if (appServiceType.toUpperCase().equals("2C")) {
            HashSet<String> fieldSet = new HashSet<String>();
            fieldSet.add("drObjectInfo");
            fieldSet.add("productId");
            fieldSet.add("contentId");
            EntityCondition findConditions3 = EntityCondition
                    .makeCondition("productId", EntityOperator.EQUALS, productId);
            List<GenericValue> pictures = delegator.findList("ProductContentAndInfo",
                    findConditions3, fieldSet,
                    null, null, false);
//            List<Map<String,Object>> morePicture = new ArrayList<Map<String, Object>>();
//            Map<String,Object> rowPicMapFirst = new HashMap<String, Object>();
//            rowPicMapFirst.put("drObjectInfo",(String) product.get("detailImageUrl"));
//            rowPicMapFirst.put("contentId","308561217_784838898");
//            morePicture.add(rowPicMapFirst);

            if (null != pictures && pictures.size() > 0) {
                imgAttr = new String[pictures.size() + 1];
                imgAttr[0] = (String) product.get("detailImageUrl");
                int i = 1;
                for (GenericValue gvPic : pictures) {

                    imgAttr[i] = gvPic.getString("drObjectInfo");
//                    Map<String,Object> rowPicMap = gvPic.getAllFields();
//                    morePicture.add(rowPicMap);
                    i++;
                }


                List<String> list = new ArrayList<String>();
                for (int z=0; z<imgAttr.length;z++) {
                    if(imgAttr[z].indexOf("jpg")>0){
                        Debug.logInfo("imgAttr[i]:"+imgAttr[z],module);
                        list.add(imgAttr[z]);
                    }
                }

                String[] newStr =  list.toArray(new String[1]); //返回一个包含所有对象的指定类型的数组
                allField.remove("imgArray");
                allField.put("imgArray", newStr);

                Debug.logInfo(":imgAttr=" + imgAttr, module);



//                allField.put("imgArray", imgAttr);
            }
//            rowMap.put("morePicture", morePicture);

        }

        resultMap.put("productDetail", allField);
        resultMap.put("productStoreId", productStoreId);
        resultMap.put("prodCatalogId", prodCatalogId);


        return resultMap;
    }


    /**
     * 2C TODO PAY
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> getProductPayInfo(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));
        String productId = (String) context.get("productId");


        GenericValue category = EntityQuery.use(delegator).from("ProductAndCategoryMember").where("productId", productId).queryFirst();
        String productStoreId = category.getString("productStoreId");

        GenericValue store = EntityQuery.use(delegator).from("ProductStore").where("productStoreId", productStoreId).queryFirst();

        GenericValue productMedia = EntityQuery.use(delegator).from("PartyAttribute").where("partyId", store.getString("payToPartyId"), "attrName", "media_id").queryFirst();
        GenericValue product = EntityQuery.use(delegator).from("Product").where("productId", productId).queryFirst();

        if (null != productMedia) {
            resultMap.put("media_id", productMedia.getString("attrValue"));
        }
        resultMap.put("productName", product.getString("productName"));
        return resultMap;
    }


    /**
     * IS HIDDEN
     *
     * @param productFeatureId
     * @param innerAttr
     * @param rowVirId
     * @param hiddenMap
     * @param delegator
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    private static boolean isHiddenSku(String productFeatureId, String innerAttr, String rowVirId, Map<String, Object> hiddenMap, Delegator delegator, List<GenericValue> skus) throws GenericEntityException, GenericServiceException {

        if (skus.size() == 0) {
            return false;
        }
        for (GenericValue gv : skus) {
            String productId = gv.getString("productIdTo");
            if (hiddenMap.containsKey(productId) && hiddenMap.get(productId).equals(productFeatureId)) {
                return true;
            }
        }

        return false;
    }


    /**
     * queryStoreProducts
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryStoreProducts(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();


        String productStoreId = (String) context.get("productStoreId");


        String viewIndexStr = (String) context.get("viewIndexStr");


        int viewIndex = 0;
        if (viewIndexStr != null) {
            viewIndex = Integer.parseInt(viewIndexStr);
        }

        int viewSize = 600;
        int lowIndex = 0;
        int highIndex = 0;
        Long resourceCount;


        Debug.logInfo("*productStoreId=" + productStoreId, module);

        GenericValue productStoreCatalog = EntityQuery.use(delegator).from("ProductStoreCatalog").where("productStoreId", productStoreId).queryFirst();

        Debug.logInfo("*productStoreCatalog=" + productStoreCatalog, module);


        GenericValue prodCatalogCategory = EntityQuery.use(delegator).from("ProdCatalogCategory").where("prodCatalogId", productStoreCatalog.getString("prodCatalogId")).queryFirst();
        String productCategoryId = (String) prodCatalogCategory.get("productCategoryId");


        //"isVirtual", "Y","isVariant","N"
        List<String> orderBy = UtilMisc.toList("-createdDate");
        PagedList<GenericValue> myContactListPage = null;
        myContactListPage = EntityQuery.use(delegator).from("ProductCategoryMemberAndProdDetail").
                where("productCategoryId", productCategoryId, "isVariant", "Y").orderBy(orderBy)
                .distinct()
                .queryPagedList(viewIndex, viewSize);

        List<GenericValue> productList = myContactListPage.getData();

        resourceCount = EntityQuery.use(delegator).from("ProductCategoryMemberAndProdDetail").where("productCategoryId", productCategoryId, "isVariant", "Y").queryCount();

        GenericValue nowStore
                = EntityQuery.use(delegator).from("ProductStore").where(
                "productStoreId", productStoreId).queryFirst();
        String nowPartyId = nowStore.getString("payToPartyId");
        lowIndex = myContactListPage.getStartIndex();
        highIndex = myContactListPage.getEndIndex();
        List<Map<String, Object>> returnProductList = new ArrayList<Map<String, Object>>();
        int count = 0;
        String beforeVir = "NA";
        Set<String> fieldSet = new HashSet<String>();

        fieldSet.add("drObjectInfo");
        fieldSet.add("contentId");
        fieldSet.add("productId");
        if (null != productList) {
            for (GenericValue gv : productList) {
                Map<String, Object> rowMap = gv.getAllFields();


//                if (null != gv.get("salesDiscontinuationDate")) {
//                    continue;
//                }

                //自己就是sku
                String skuId = (String) rowMap.get("productId");


                GenericValue beforeProduct
                        = EntityQuery.use(delegator).from("ProductAssoc").where("productAssocTypeId", "PRODUCT_OBSOLESCENCE", "productId", skuId).orderBy("-fromDate").queryFirst();
                if (beforeProduct != null) {
                    String beforeProductId = beforeProduct.getString("productIdTo");
                    GenericValue productPrice = EntityQuery.use(delegator).from("ProductPrice").where("productId", beforeProductId).queryFirst();
                    rowMap.put("beforePrice", productPrice.get("price"));


                    GenericValue productRoleAdmin
                            = EntityQuery.use(delegator).from("ProductRole").where(
                            "roleTypeId", "ADMIN", "productId", beforeProductId).queryFirst();

                    Debug.logInfo(">>productRoleAdmin:" + productRoleAdmin, module);
                    if (productRoleAdmin != null) {
                        String rowProductAdminId = productRoleAdmin.getString("partyId");
                        Debug.logInfo(">>rowProductAdminId:" + rowProductAdminId, module);
                        GenericValue salesStore = EntityQuery.use(delegator).from("ProductStore").where("payToPartyId", rowProductAdminId).queryFirst();
                        Debug.logInfo(">>salesStore:" + salesStore, module);
                        String rowProductStoreId = salesStore.getString("productStoreId");
                        rowMap.put("salesStoreId", rowProductStoreId);
                        rowMap.put("beforeProductId", beforeProductId);
                        rowMap.put("salesStoreName", salesStore.getString("storeName"));
                    }


                }


                GenericValue pkey
                        = EntityQuery.use(delegator).from("ProductKeyword").where("keyword", "RM" + nowPartyId,
                        "keywordTypeId", "KWT_TAG", "productId", skuId, "statusId", "KW_APPROVED").queryFirst();
                if (null != pkey) {
                    rowMap.put("reMai", "Y");
                }


//                if(productRoleAdmin!=null){
//                    String partyId = productRoleAdmin.getString("partyId");
//                    GenericValue productStoreRole
//                            = EntityQuery.use(delegator).from("ProductStore").where(
//                            "payToPartyId", partyId).queryFirst();
//                    String fromStoreId = productStoreRole.getString("productStoreId");
//                    rowMap.put("baseStoreId",fromStoreId);
//                    rowMap.put("storeName",productStoreRole.getString("storeName"));
//                }

                EntityCondition findConditions3 = EntityCondition
                        .makeCondition("productId", EntityOperator.EQUALS, skuId);

                List<GenericValue> rowPictures = delegator.findList("ProductContentAndInfo",
                        findConditions3, fieldSet,
                        null, null, false);
                int index = 0;
                rowMap.put("rowPictures", rowPictures);
                GenericValue vir_product = EntityQuery.use(delegator).from("ProductAssoc").where("productIdTo", skuId).queryFirst();

                count++;
                GenericValue productPrice = EntityQuery.use(delegator).from("ProductPrice").where("productId", skuId).queryFirst();
                rowMap.put("price", productPrice.get("price"));


                returnProductList.add(rowMap);

            }
        }

        resultMap.put("productList", returnProductList);

        //总共有多少页码
        int countIndex = (Integer.parseInt(resourceCount + "") % viewSize);


        resultMap.put("total", count);

        resultMap.put("from", viewIndex);
        resultMap.put("current_page", viewIndex + 1);
        resultMap.put("last_page", Integer.parseInt(resourceCount + ""));
        return resultMap;
    }


    /**
     * queryCatalogSkuProduct
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryCatalogSkuProduct(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();

        String openId = (String) context.get("openId");
        String prodCatalogId = (String) context.get("prodCatalogId");

        System.out.println("*OPENID = " + openId);

        String viewIndexStr = (String) context.get("viewIndexStr");


        int viewIndex = 0;
        if (viewIndexStr != null) {
            viewIndex = Integer.parseInt(viewIndexStr);
        }

        int viewSize = 600;
        int lowIndex = 0;
        int highIndex = 0;
        Long resourceCount;

        GenericValue prodCatalogCategory = EntityQuery.use(delegator).from("ProdCatalogCategory").where("prodCatalogId", prodCatalogId).queryFirst();
        String productCategoryId = (String) prodCatalogCategory.get("productCategoryId");


        //"isVirtual", "Y","isVariant","N"
        List<String> orderBy = UtilMisc.toList("-createdDate");
        PagedList<GenericValue> myContactListPage = null;
        myContactListPage = EntityQuery.use(delegator).from("ProductCategoryMemberAndProdDetail").
                where("productCategoryId", productCategoryId, "isVirtual", "N").orderBy(orderBy).queryPagedList(viewIndex, viewSize);

        List<GenericValue> productList = myContactListPage.getData();

        resourceCount = EntityQuery.use(delegator).from("ProductCategoryMemberAndProdDetail").where("productCategoryId", productCategoryId, "isVirtual", "N").queryCount();


        lowIndex = myContactListPage.getStartIndex();
        highIndex = myContactListPage.getEndIndex();
        List<Map<String, Object>> returnProductList = new ArrayList<Map<String, Object>>();
        int count = 0;
        String beforeVir = "NA";
        Set<String> fieldSet = new HashSet<String>();

        fieldSet.add("drObjectInfo");
        fieldSet.add("contentId");
        fieldSet.add("productId");

        Debug.logInfo("=>myContactListPage=" + productList, module);

        if (null != myContactListPage) {
            for (GenericValue gv : productList) {
                Map<String, Object> rowMap = gv.getAllFields();
                //自己就是sku
                String skuId = (String) rowMap.get("productId");

                //有单品图就拿单品图,否则就拿首图
                HashSet<String> imgFieldSet = new HashSet<String>();
                imgFieldSet.add("drObjectInfo");
                imgFieldSet.add("productId");
                imgFieldSet.add("productContentTypeId");
                EntityCondition genericProductConditions = EntityCondition.makeCondition("productId", EntityOperator.EQUALS, skuId);
                EntityCondition singleTypeConditions = EntityCondition.makeCondition("productContentTypeId", EntityOperator.EQUALS, "SINGLE_PRODUCT_IMAGE");
                EntityCondition singleTypeConditions2 = EntityCondition.makeCondition("drObjectInfo", EntityOperator.LIKE, "%jpg");
                EntityCondition singleCondition = EntityCondition.makeCondition(singleTypeConditions, EntityOperator.AND, genericProductConditions);
                List<GenericValue> singlePictures = delegator.findList("ProductContentAndInfo", singleCondition, imgFieldSet,
                        null, null, false);
                if (singlePictures != null && singlePictures.size() > 0) {

                        rowMap.put("showImageUrl", singlePictures.get(0).get("drObjectInfo") + "");


                } else {
                    rowMap.put("showImageUrl", "无");
                }

//                EntityCondition findConditions3 = EntityCondition
//                        .makeCondition("productId", EntityOperator.EQUALS, skuId);
//
//                List<GenericValue> rowPictures = delegator.findList("ProductContentAndInfo",
//                        findConditions3, fieldSet,
//                        null, null, false);

                count++;
                GenericValue productPrice = EntityQuery.use(delegator).from("ProductPrice").where("productId", skuId).queryFirst();
                if (null != productPrice) {
                    rowMap.put("price", productPrice.get("price"));
                } else {
                    Debug.logInfo("can't find price productId:" + skuId, module);
                }


                returnProductList.add(rowMap);

            }
        }

        resultMap.put("productList", returnProductList);

        //总共有多少页码
        int countIndex = (Integer.parseInt(resourceCount + "") % viewSize);


        resultMap.put("total", count);

        resultMap.put("from", viewIndex);
        resultMap.put("current_page", viewIndex + 1);
        resultMap.put("last_page", Integer.parseInt(resourceCount + ""));


        return resultMap;
    }


    /**
     * queryCatalogProduct
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryCatalogProduct(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();

        String openId = (String) context.get("openId");
        String prodCatalogId = (String) context.get("prodCatalogId");

        System.out.println("*OPENID = " + openId);

        String viewIndexStr = (String) context.get("viewIndexStr");


        int viewIndex = 0;
        if (viewIndexStr != null) {
            viewIndex = Integer.parseInt(viewIndexStr);
        }

        int viewSize = 2000;
        int lowIndex = 0;
        int highIndex = 0;
        Long resourceCount;

        GenericValue partyIdentification = EntityQuery.use(delegator).from("PartyIdentification").where("idValue", openId, "partyIdentificationTypeId", "WX_MINIPRO_OPEN_ID").queryFirst();
        String partyId = "NA";


        if (UtilValidate.isNotEmpty(partyIdentification)) {
            partyId = (String) partyIdentification.get("partyId");
        }
        GenericValue prodCatalogCategory = EntityQuery.use(delegator).from("ProdCatalogCategory").where("prodCatalogId", prodCatalogId).queryFirst();
        String productCategoryId = (String) prodCatalogCategory.get("productCategoryId");

        //查看了列表，清空temp
        GenericValue forwardChainFactTemp = EntityQuery.use(delegator).from("YpForwardChainFactTemp").where(
                "partyIdTo", partyId).queryFirst();

        if (null != forwardChainFactTemp) {
            forwardChainFactTemp.remove();
        }

        //"isVirtual", "Y","isVariant","N"
        List<String> orderBy = UtilMisc.toList("-createdDate");
        PagedList<GenericValue> myContactListPage = null;
        myContactListPage = EntityQuery.use(delegator).from("ProductCategoryMemberAndProdDetail").
                where("productCategoryId", productCategoryId, "isVirtual", "N").orderBy(orderBy)
                .distinct()
                .queryPagedList(viewIndex, viewSize);

        List<GenericValue> productList = myContactListPage.getData();

        resourceCount = EntityQuery.use(delegator).from("ProductCategoryMemberAndProdDetail").where("productCategoryId", productCategoryId, "isVirtual", "N").queryCount();


        lowIndex = myContactListPage.getStartIndex();
        highIndex = myContactListPage.getEndIndex();
        List<Map<String, Object>> returnProductList = new ArrayList<Map<String, Object>>();
        int count = 0;
        String beforeVir = "NA";
        Set<String> fieldSet = new HashSet<String>();

        fieldSet.add("drObjectInfo");
        fieldSet.add("contentId");

        fieldSet.add("productId");
        if (null != myContactListPage) {
            for (GenericValue gv : myContactListPage) {
                Map<String, Object> rowMap = gv.getAllFields();
                //自己就是sku
                String skuId = (String) rowMap.get("productId");


                //有单品图就拿单品图,否则就拿首图
                HashSet<String> imgFieldSet = new HashSet<String>();
                imgFieldSet.add("drObjectInfo");
                imgFieldSet.add("productId");
                imgFieldSet.add("productContentTypeId");
                EntityCondition genericProductConditions = EntityCondition.makeCondition("productId", EntityOperator.EQUALS, skuId);
                EntityCondition singleTypeConditions = EntityCondition.makeCondition("productContentTypeId", EntityOperator.EQUALS, "SINGLE_PRODUCT_IMAGE");
                EntityCondition singleCondition = EntityCondition.makeCondition(singleTypeConditions, EntityOperator.AND, genericProductConditions);
                List<GenericValue> singlePictures = delegator.findList("ProductContentAndInfo", singleCondition, imgFieldSet,
                        null, null, false);
                if (singlePictures != null && singlePictures.size() > 0) {
                    rowMap.put("showImageUrl", singlePictures.get(0).get("drObjectInfo") + "");
                } else {
                    rowMap.put("showImageUrl", "无");
                }


                EntityCondition findConditions3 = EntityCondition
                        .makeCondition("productId", EntityOperator.EQUALS, skuId);

                List<GenericValue> rowPictures = delegator.findList("ProductContentAndInfo",
                        findConditions3, fieldSet,
                        null, null, false);
                int index = 0;

                GenericValue vir_product = EntityQuery.use(delegator).from("ProductAssoc").where("productIdTo", skuId).queryFirst();
                if (vir_product != null) {
                    String detailImageUrl = (String) gv.getString("detailImageUrl");

                    String rowVirId = (String) vir_product.get("productId");
                    //别展示相同产品了
                    if (rowVirId.equals(beforeVir)) {

                    } else {
                        Debug.logInfo("detailImageUrl:" + detailImageUrl, module);
                        //如果没有图的默认不看 针对zuczug
                        if (detailImageUrl.indexOf("DEFAULT_PRODUCT") < 0) {
                            count++;
                            GenericValue productPrice = EntityQuery.use(delegator).from("ProductPrice").where("productId", skuId).queryFirst();
                            rowMap.put("price", productPrice.get("price"));

                            GenericValue productOnePrice = EntityQuery.use(delegator).from("ProductPrice").where("productId", skuId, "productPriceTypeId", "MINIMUM_PRICE").queryFirst();
                            if (null != productOnePrice) {
                                rowMap.put("oneMouthPrice", productOnePrice.get("price"));
                            }

                            returnProductList.add(rowMap);
                            beforeVir = rowVirId;
                        }
                    }
                } else {
                    count++;
                    GenericValue productPrice = EntityQuery.use(delegator).from("ProductPrice").where("productId", skuId).queryFirst();
                    rowMap.put("price", productPrice.get("price"));

                    GenericValue productOnePrice = EntityQuery.use(delegator).from("ProductPrice").where("productId", skuId, "productPriceTypeId", "MINIMUM_PRICE").queryFirst();
                    if (null != productOnePrice) {
                        rowMap.put("oneMouthPrice", productOnePrice.get("price"));
                    }

                    returnProductList.add(rowMap);
                }
            }
        }

        resultMap.put("productList", returnProductList);

        //总共有多少页码
        int countIndex = (Integer.parseInt(resourceCount + "") % viewSize);
        //viewIndex 当前页码


//        if (resourceCount != 0 && resourceCount > viewSize) {
//            resultMap.put("total", Integer.parseInt(resourceCount + "") % viewSize == 0 ? Integer.parseInt(resourceCount + "") / viewSize : Integer.parseInt(resourceCount + "") / viewSize + 1);
//        } else {
//            if (null == resourceCount || resourceCount == 0) {
//                resultMap.put("total", -1);
//            } else {
//                resultMap.put("total", 1);
//            }
//
//        }

        resultMap.put("total", count);

        resultMap.put("from", viewIndex);
        resultMap.put("current_page", viewIndex + 1);
        resultMap.put("last_page", Integer.parseInt(resourceCount + ""));


        GenericValue forwardChainFact = EntityQuery.use(delegator).from("YpForwardChainFact").where(
                "basePartyId", partyId).queryFirst();
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));
        if (null == forwardChainFact) {

            String fromPartyId = "NO_PARTY";
            String basePartyId = partyId;
            String workEffortId = "NA";
            String partyIdTo = "NO_PARTY";
            Map<String, String> userInfo = queryPersonBaseInfo(delegator, partyId);
            dispatcher.runSync("inForwardChainFact", UtilMisc.toMap(
                    "userLogin", admin,
                    "partyIdFrom", fromPartyId,
                    "partyIdTo", partyIdTo,
                    "workEffortId", workEffortId,
                    "basePartyId", basePartyId,
                    "firstName", userInfo.get("firstName"),
                    "objectInfo", userInfo.get("headPortrait"),
                    "createDate", new Timestamp(new Date().getTime())));
        }
        GenericValue myForwardChainFactTemp = EntityQuery.use(delegator).from("YpForwardChainFactTemp").where(
                "partyIdTo", partyId).queryFirst();

        // 更新我的软连接
        if (null != myForwardChainFactTemp) {
            myForwardChainFactTemp.set("basePartyId", partyId);
            myForwardChainFactTemp.store();
            Debug.logInfo("update ... " + myForwardChainFactTemp, module);
        }
        return resultMap;
    }


    /**
     * 内买专用目录产品列表
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */

    public static Map<String, Object> queryNeiMaiCatalogProduct(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

//Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
        boolean isAkrEmp = false;

        String openId = (String) context.get("openId");
        String prodCatalogId = (String) context.get("prodCatalogId");
        String roleTypeId = (String) context.get("roleTypeId");

        if(null!= roleTypeId && roleTypeId.equals("ANKORAU_EMP")){
            isAkrEmp = !isAkrEmp;
        }

        String viewIndexStr = (String) context.get("viewIndexStr");
        String viewSizeStr = (String) context.get("viewSizeStr");

        System.out.println("*queryNeiMaiCatalogProduct viewIndexStr= " + viewIndexStr);
        System.out.println("*roleTypeId= " + roleTypeId);
        Debug.logInfo("roleTypeId:" + roleTypeId, module);
        Debug.logInfo("isAkrEmp:"+isAkrEmp,module);
        System.out.println("*isAkrEmp= " + isAkrEmp);
        System.out.println("*queryNeiMaiCatalogProduct viewSizeStr= " + viewSizeStr);

        int viewIndex = 0;
        if (viewIndexStr != null) {
            viewIndex = Integer.parseInt(viewIndexStr);
        }
        int viewSize = 20;
        if (viewSizeStr != null) {
            viewSize = Integer.parseInt(viewSizeStr);
        }
        // zuc zug emp
        if(isAkrEmp==false){
            viewSize = 50000;
        }

        int lowIndex = 0;
        int highIndex = 0;
        Long resourceCount;

        GenericValue partyIdentification = EntityQuery.use(delegator).from("PartyIdentification").where("idValue", openId, "partyIdentificationTypeId", "WX_MINIPRO_OPEN_ID").queryFirst();
        String partyId = "NA";


        if (UtilValidate.isNotEmpty(partyIdentification)) {
            partyId = (String) partyIdentification.get("partyId");
        }
        GenericValue prodCatalogCategory = EntityQuery.use(delegator).from("ProdCatalogCategory").where("prodCatalogId", prodCatalogId).queryFirst();
        String productCategoryId = (String) prodCatalogCategory.get("productCategoryId");


        //"isVirtual", "Y","isVariant","N"
        List<String> orderBy = UtilMisc.toList("-createdDate");
        PagedList<GenericValue> myContactListPage = null;
        myContactListPage = EntityQuery.use(delegator).from("ProductCategoryMemberAndProdDetail").
                where("productCategoryId", productCategoryId, "isVirtual", "N").orderBy(orderBy)
                .distinct()
                .queryPagedList(viewIndex, viewSize);

        List<GenericValue> productList = myContactListPage.getData();

        resourceCount = EntityQuery.use(delegator).from("ProductCategoryMemberAndProdDetail").where("productCategoryId", productCategoryId, "isVirtual", "N").queryCount();


        lowIndex = myContactListPage.getStartIndex();
        highIndex = myContactListPage.getEndIndex();
        List<Map<String, Object>> returnProductList = new ArrayList<Map<String, Object>>();
        int count = 0;
        String beforeVir = "NA";
        Set<String> fieldSet = new HashSet<String>();

        fieldSet.add("drObjectInfo");
        fieldSet.add("contentId");

        fieldSet.add("productId");
        if (null != myContactListPage) {
            for (GenericValue gv : myContactListPage) {
                boolean isOneMouthPrice = false;
                Map<String, Object> rowMap = gv.getAllFields();
                //自己就是sku
                String skuId = (String) rowMap.get("productId");
                EntityCondition findConditions3 = EntityCondition
                        .makeCondition("productId", EntityOperator.EQUALS, skuId);

                List<GenericValue> rowPictures = delegator.findList("ProductContentAndInfo",
                        findConditions3, fieldSet,
                        null, null, false);
                int index = 0;

                GenericValue vir_product = EntityQuery.use(delegator).from("ProductAssoc").where("productIdTo", skuId).queryFirst();
                if (vir_product != null) {
                    String detailImageUrl = (String) gv.getString("detailImageUrl");

                    String rowVirId = (String) vir_product.get("productId");
                    //别展示相同产品了
                    if (rowVirId.equals(beforeVir)) {

                    } else {
                        Debug.logInfo("detailImageUrl:" + detailImageUrl, module);

                        //如果没有图的默认不看 针对zuczug
                        if (detailImageUrl.indexOf("DEFAULT_PRODUCT") < 0) {
                            count++;
                            GenericValue productPrice = EntityQuery.use(delegator).from("ProductPrice").where("productId", skuId).queryFirst();
                            rowMap.put("price", productPrice.get("price"));

                            GenericValue productOnePrice = EntityQuery.use(delegator).from("ProductPrice").where("productId", skuId, "productPriceTypeId", "MINIMUM_PRICE").queryFirst();
                            if (null != productOnePrice) {
                                rowMap.put("oneMouthPrice", productOnePrice.get("price"));
                            }
                            GenericValue productTwoPrice = EntityQuery.use(delegator).from("ProductPrice").where("productId", skuId, "productPriceTypeId", "ONE_MOUTH_PRICE").queryFirst();
                            if (null != productTwoPrice) {
                                rowMap.put("twoMouthPrice", productTwoPrice.get("price"));
                                isOneMouthPrice = !isOneMouthPrice;
                            }
                            // 有最低价格的，只给akr看
//                            System.out.println("*isOneMouthPrice= " + isOneMouthPrice);
                            Debug.logInfo("isOneMouthPrice:"+isOneMouthPrice,module);
                            if(isAkrEmp){
                                returnProductList.add(rowMap);
                                beforeVir = rowVirId;
                            }else{
                                if(isOneMouthPrice){
                                    returnProductList.add(rowMap);
                                    beforeVir = rowVirId;
                                }else{
                                    beforeVir = rowVirId;
                                }
                            }


                        }
                    }
                } else {
                    count++;
                    GenericValue productPrice = EntityQuery.use(delegator).from("ProductPrice").where("productId", skuId).queryFirst();
                    rowMap.put("price", productPrice.get("price"));

                    GenericValue productOnePrice = EntityQuery.use(delegator).from("ProductPrice").where("productId", skuId, "productPriceTypeId", "MINIMUM_PRICE").queryFirst();
                    if (null != productOnePrice) {
                        rowMap.put("oneMouthPrice", productOnePrice.get("price"));
                    }

                    returnProductList.add(rowMap);
                }
            }
        }

        resultMap.put("productList", returnProductList);

        //总共有多少页码
        int countIndex = (Integer.parseInt(resourceCount + "") % viewSize);


        resultMap.put("total", count);

        resultMap.put("from", viewIndex);
        resultMap.put("current_page", viewIndex + 1);
        resultMap.put("last_page", Integer.parseInt(resourceCount + ""));


        return resultMap;


    }

//    public static Map<String, Object> queryNeiMaiCatalogProduct(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {
//
//        //Service Head
//        LocalDispatcher dispatcher = dctx.getDispatcher();
//        Delegator delegator = dispatcher.getDelegator();
//        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
//
//        String openId = (String) context.get("openId");
//        String prodCatalogId = (String) context.get("prodCatalogId");
//        Debug.logInfo("prodCatalogId"+prodCatalogId,module);
//        String viewIndexStr = (String) context.get("viewIndexStr");
//
//
//        int viewIndex = 0;
//        if (viewIndexStr != null) {
//            viewIndex = Integer.parseInt(viewIndexStr);
//        }
//
//        int viewSize = 600;
//        int lowIndex = 0;
//        int highIndex = 0;
//        Long resourceCount;
//
//        GenericValue prodCatalogCategory = EntityQuery.use(delegator).from("ProdCatalogCategory").where("prodCatalogId", prodCatalogId).queryFirst();
//        String productCategoryId = (String) prodCatalogCategory.get("productCategoryId");
//
//        List<String> orderBy = UtilMisc.toList("-createdDate");
//        PagedList<GenericValue> myContactListPage = null;
//        myContactListPage = EntityQuery.use(delegator).from("ProductCategoryMemberAndProdDetail").
//                where("productCategoryId", productCategoryId, "isVirtual", "N").orderBy(orderBy)
//                .distinct()
//                .queryPagedList(viewIndex, viewSize);
//
//
//
//        resourceCount = EntityQuery.use(delegator).from("ProductCategoryMemberAndProdDetail").where("productCategoryId", productCategoryId, "isVirtual", "N").queryCount();
//
//
//        List<Map<String, Object>> returnProductList = new ArrayList<Map<String, Object>>();
//        int count = 0;
//        Set<String> fieldSet = new HashSet<String>();
//
//        fieldSet.add("drObjectInfo");
//        fieldSet.add("contentId");
//
//        fieldSet.add("productId");
//        if (null != myContactListPage) {
//            for (GenericValue gv : myContactListPage) {
//                Map<String, Object> rowMap = gv.getAllFields();
//                //自己就是sku
//                String skuId = (String) rowMap.get("productId");
//                int index = 0;
//
//                GenericValue vir_product = EntityQuery.use(delegator).from("ProductAssoc").where("productIdTo", skuId).queryFirst();
//                if (vir_product != null) {
//                    String detailImageUrl = (String) gv.getString("detailImageUrl");
//
//                    String rowVirId = (String) vir_product.get("productId");
//
//                    GenericValue productFeatureAndAppl = EntityQuery.use(delegator).from("ProductFeatureAndAppl").where(
//                            "productId", skuId,"productFeatureTypeId","COLOR").queryFirst();
//                    String colorDesc = productFeatureAndAppl.getString("description");
//                    rowMap.put("productName",rowMap.get("productName")+"-"+colorDesc);
//                        Debug.logInfo("detailImageUrl:" + detailImageUrl, module);
//                        //如果没有图的默认不看 针对zuczug
//                        if (detailImageUrl.indexOf("DEFAULT_PRODUCT") < 0) {
//                            count++;
//                            GenericValue productPrice = EntityQuery.use(delegator).from("ProductPrice").where("productId", skuId).queryFirst();
//                            rowMap.put("price", productPrice.get("price"));
//
//                            GenericValue productOnePrice = EntityQuery.use(delegator).from("ProductPrice").where("productId", skuId,"productPriceTypeId","MINIMUM_PRICE").queryFirst();
//                            if(null!=productOnePrice){
//                                rowMap.put("oneMouthPrice", productOnePrice.get("price"));
//                            }
//
//                            returnProductList.add(rowMap);
//
//                        }
//
//                }
//            }
//        }
//
//        resultMap.put("productList", returnProductList);
//
//
//        resultMap.put("total", count);
//        resultMap.put("from", viewIndex);
//        resultMap.put("current_page", viewIndex + 1);
//        resultMap.put("last_page", Integer.parseInt(resourceCount + ""));
//        return resultMap;
//    }


    /**
     * 查询好友的资源列表
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryContactResourceList(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();

        String openId = (String) context.get("openId");
        System.out.println("*OPENID = " + openId);

        String viewIndexStr = (String) context.get("viewIndexStr");


        int viewIndex = 0;
        if (viewIndexStr != null) {
            viewIndex = Integer.parseInt(viewIndexStr);
        }

        int viewSize = 3;
        int lowIndex = 0;
        int highIndex = 0;
        int resourceCount = 0;

        GenericValue partyIdentification = EntityQuery.use(delegator).from("PartyIdentification").where("idValue", openId, "partyIdentificationTypeId", "WX_MINIPRO_OPEN_ID").queryFirst();
        String partyId = "NA";

        if (UtilValidate.isNotEmpty(partyIdentification)) {
            partyId = (String) partyIdentification.get("partyId");
        }


        //查询联系人列表
        List<String> orderBy = UtilMisc.toList("-createdDate");
        PagedList<GenericValue> myContactListPage = null;

        myContactListPage = EntityQuery.use(delegator).from("PartyContactResources").
                where("partyIdTo", partyId, "partyRelationshipTypeId", PeConstant.CONTACT, "roleTypeId", "ADMIN").orderBy(orderBy)
                .distinct()
                .queryPagedList(viewIndex, viewSize);

        List<GenericValue> myContactList = myContactListPage.getData();


        List<GenericValue> myContactListCountList = EntityQuery.use(delegator).from("PartyContactResources").
                where("partyIdTo", partyId, "partyRelationshipTypeId", PeConstant.CONTACT, "roleTypeId", "ADMIN")
                .orderBy(orderBy)
                .distinct()
                .queryList();
        resourceCount = myContactListCountList.size();

        lowIndex = myContactListPage.getStartIndex();
        highIndex = myContactListPage.getEndIndex();

        if (null != myContactList) {


            for (GenericValue gv : myContactList) {

                Map<String, Object> rowMap = new HashMap<String, Object>();

                String contactPartyId = (String) gv.get("partyIdFrom");

                if (partyId.equals(contactPartyId)) {
                    continue;
                }

                Map<String, String> userInfoMap = queryPersonBaseInfo(delegator, contactPartyId);

                Timestamp createdDateTp = (Timestamp) gv.get("createdDate");

                rowMap.put("created", dateToStr(createdDateTp, "yyyy-MM-dd HH:mm:ss"));

                rowMap.put("partyId", partyId);

                if (null != gv.get("salesDiscontinuationDate")) {
                    continue;
                }

                rowMap.put("salesDiscontinuationDate", gv.get("salesDiscontinuationDate"));

                rowMap.put("user", userInfoMap);

                rowMap.put("contactPartyId", contactPartyId);

                String productId = (String) gv.get("productId");

                GenericValue productAddress = EntityQuery.use(delegator).from("ProductAttribute").where("attrName", "address", "productId", productId).queryFirst();
                if (null != productAddress) {
                    rowMap.put("address", productAddress.get("attrValue"));
                }

                GenericValue productlongitude = EntityQuery.use(delegator).from("ProductAttribute").where("attrName", "longitude", "productId", productId).queryFirst();
                if (null != productlongitude) {
                    rowMap.put("longitude", productlongitude.get("attrValue"));
                }
                GenericValue productlatitude = EntityQuery.use(delegator).from("ProductAttribute").where("attrName", "latitude", "productId", productId).queryFirst();
                if (null != productlatitude) {
                    rowMap.put("latitude", productlatitude.get("attrValue"));
                }

                rowMap.put("productId", productId);

                rowMap.put("description", (String) gv.get("description"));

                rowMap.put("productName", (String) gv.get("productName"));

                rowMap.put("detailImageUrl", (String) gv.get("detailImageUrl"));

                rowMap.put("price", gv.get("price") + "");
                HashSet<String> fieldSet = new HashSet<String>();
                fieldSet.add("drObjectInfo");
                fieldSet.add("productId");
                EntityCondition findConditions3 = EntityCondition
                        .makeCondition("productId", EntityOperator.EQUALS, (String) gv.get("productId"));

                List<GenericValue> pictures = delegator.findList("ProductContentAndInfo",
                        findConditions3, fieldSet,
                        null, null, false);
                rowMap.put("morePicture", pictures);
                returnList.add(rowMap);

            }


        }
        resultMap.put("resourcesList", returnList);

        //总共有多少页码
        int countIndex = (resourceCount % viewSize);
        //viewIndex 当前页码


        if (resourceCount != 0 && resourceCount > viewSize) {
            resultMap.put("total", resourceCount % viewSize == 0 ? resourceCount / viewSize : resourceCount / viewSize + 1);
        } else {
            if (null == myContactListCountList || myContactListCountList.size() == 0) {
                resultMap.put("total", -1);
            } else {
                resultMap.put("total", 1);
            }

        }

        resultMap.put("from", viewIndex);


        resultMap.put("current_page", viewIndex + 1);
        resultMap.put("last_page", resourceCount);

        return resultMap;
    }


    /**
     * 查询绝对想要的资源列表
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryDesiredResources(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();

        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();

        String partyId = "admin";

        String resourceName = (String) context.get("resourceName");

        //查询朋友列表


        List<GenericValue> myFriendList = EntityQuery.use(delegator).from("PartyRelationship").where("partyIdTo", partyId, "partyRelationshipTypeId", "FRIEND").queryList();


        for (GenericValue gv : myFriendList) {

            Map<String, Object> rowMap = new HashMap<String, Object>();

            String friendPartyId = (String) gv.get("partyIdFrom");

            GenericValue shopingList = EntityQuery.use(delegator).from("ShoppingList").where("partyId", friendPartyId, "listName", resourceName).queryFirst();

            if (null != shopingList) {
                rowMap.put("desc", shopingList.get("description"));
                rowMap.put("time", shopingList.get("createdStamp"));
                returnList.add(rowMap);
            }

        }


        resultMap.put("resourcesList", returnList);

        return resultMap;
    }


    /**
     * Query My Product
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryMyProduct(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));
        String unioId = (String) context.get("partyId");
        String isDiscontinuation = (String) context.get("isDiscontinuation");
        // 0:查询未下架的正常资源。   1:查已下架的资源  (默认0)
        if (UtilValidate.isEmpty(isDiscontinuation)) {
            isDiscontinuation = "0";
        }


//        System.out.println("*OPENID = " + openId);
//
        //TODO FIX ME
//        GenericValue partyIdentification = EntityQuery.use(delegator).from("PartyIdentification").where("idValue", unioId, "partyIdentificationTypeId", "WX_UNIO_ID").queryFirst();
        GenericValue partyIdentification = EntityQuery.use(delegator).from("PartyIdentification").where("idValue", unioId, "partyIdentificationTypeId", "WX_MINIPRO_OPEN_ID").queryFirst();

        String partyId = "NA";

        if (UtilValidate.isNotEmpty(partyIdentification)) {
            partyId = (String) partyIdentification.get("partyId");
        }
        System.out.println("*partyId = " + partyId);

        String productCategoryId = "NA";


        //查看了我的资源列表，清空temp
        GenericValue forwardChainFactTemp = EntityQuery.use(delegator).from("YpForwardChainFactTemp").where(
                "partyIdTo", partyId).queryFirst();

        if (null != forwardChainFactTemp) {
//             forwardChainFactTemp.remove();
        }


        List<GenericValue> myResourceList = null;

        List<Map<String, Object>> resourceMapList = new ArrayList<Map<String, Object>>();

        //查我的目录
        GenericValue rodCatalogRole = EntityQuery.use(delegator).from("ProdCatalogRole").where("partyId", partyId, "roleTypeId", "ADMIN").queryFirst();

        EntityFindOptions findOptions = new EntityFindOptions();
//        findOptions.setFetchSize(0);
//        findOptions.setMaxRows(4);
        //Select Fields
        Set<String> fieldSet = new HashSet<String>();
        fieldSet.add("productId");
        fieldSet.add("description");
        fieldSet.add("productStoreId");
        fieldSet.add("productName");
        fieldSet.add("detailImageUrl");
        fieldSet.add("createdDate");
        fieldSet.add("price");
        fieldSet.add("productCategoryId");
        fieldSet.add("payToPartyId");

        if (rodCatalogRole != null) {
            String prodCatalogId = (String) rodCatalogRole.get("prodCatalogId");
            //根据目录拿关联的分类Id
            GenericValue prodCatalogCategory = EntityQuery.use(delegator).from("ProdCatalogCategory").where("prodCatalogId", prodCatalogId, "prodCatalogCategoryTypeId", "PCCT_PURCH_ALLW").queryFirst();
            //得到分类Id
            productCategoryId = (String) prodCatalogCategory.get("productCategoryId");
            //findConditions
            EntityCondition findConditions = EntityCondition
                    .makeCondition(UtilMisc.toMap("productCategoryId", productCategoryId));
            EntityCondition findConditions2 = null;

            if (isDiscontinuation.equals("0")) {
                findConditions2 = EntityCondition.makeCondition("salesDiscontinuationDate", EntityOperator.EQUALS, GenericEntity.NULL_FIELD);
            } else {
                findConditions2 = EntityCondition.makeCondition("salesDiscontinuationDate", EntityOperator.NOT_EQUAL, GenericEntity.NULL_FIELD);
            }


            EntityConditionList<EntityCondition> listConditions = EntityCondition
                    .makeCondition(findConditions, findConditions2);

            //Query My Resource
            myResourceList = delegator.findList("ProductAndCategoryMember",
                    listConditions, fieldSet,
                    UtilMisc.toList("-createdDate"), findOptions, false);

            if (null != myResourceList && myResourceList.size() > 0) {
                for (GenericValue gv : myResourceList) {
                    Map<String, Object> rowMap = new HashMap<String, Object>();

                    rowMap.put("productName", (String) gv.get("productName"));
                    rowMap.put("productId", (String) gv.get("productId"));
                    rowMap.put("productStoreId", (String) gv.get("productStoreId"));
                    rowMap.put("detailImageUrl", (String) gv.get("detailImageUrl"));
                    rowMap.put("createdDate", gv.get("createdDate"));
                    rowMap.put("price", gv.get("price"));
                    rowMap.put("productCategoryId", (String) gv.get("productCategoryId"));
                    rowMap.put("payToPartyId", (String) gv.get("payToPartyId"));
                    rowMap.put("description", (String) gv.get("description"));


                    fieldSet = new HashSet<String>();
                    fieldSet.add("drObjectInfo");
                    fieldSet.add("productId");
                    fieldSet.add("contentId");
                    EntityCondition findConditions3 = EntityCondition
                            .makeCondition("productId", EntityOperator.EQUALS, (String) gv.get("productId"));
                    List<GenericValue> pictures = delegator.findList("ProductContentAndInfo",
                            findConditions3, fieldSet,
                            null, null, false);
                    List<Map<String, Object>> morePicture = new ArrayList<Map<String, Object>>();
                    Map<String, Object> rowPicMapFirst = new HashMap<String, Object>();
                    rowPicMapFirst.put("drObjectInfo", (String) gv.get("detailImageUrl"));
                    rowPicMapFirst.put("contentId", "308561217_784838898");
                    morePicture.add(rowPicMapFirst);
                    if (null != pictures && pictures.size() > 0) {

                        for (GenericValue gvPic : pictures) {
                            Map<String, Object> rowPicMap = gvPic.getAllFields();
                            morePicture.add(rowPicMap);
                        }
                    }
                    rowMap.put("morePicture", morePicture);

                    //获得库存信息 getInventoryAvailableByFacility
                    Map<String, Object> getInventoryAvailableByFacilityMap = dispatcher.runSync("getInventoryAvailableByFacility", UtilMisc.toMap("userLogin", admin,
                            "facilityId", gv.get("productStoreId"), "productId", gv.get("productId")));
                    if (ServiceUtil.isSuccess(getInventoryAvailableByFacilityMap)) {
                        rowMap.put("quantityOnHandTotal", getInventoryAvailableByFacilityMap.get("quantityOnHandTotal"));
                        rowMap.put("availableToPromiseTotal", getInventoryAvailableByFacilityMap.get("availableToPromiseTotal"));
                    }

                    resourceMapList.add(rowMap);
                }
            }


            //  ProductContentAndInfo
            //EntityQuery.use(delegator).from("ProductAndCategoryMember").where("productCategoryId",productCategoryId).queryList();
        }


        resultMap.put("productList", resourceMapList);

        resultMap.put("nowPartyId", partyId);

        GenericValue userLogin = EntityQuery.use(delegator).from("UserLogin").where(UtilMisc.toMap("partyId", partyId)).queryFirst();
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
        String token = signer.sign(claims);
        resultMap.put("tarjeta", token);


//        resultMap.put("productCategoryId", productCategoryId);

        GenericValue forwardChainFact = EntityQuery.use(delegator).from("YpForwardChainFact").where(
                "basePartyId", partyId).queryFirst();

        if (null == forwardChainFact) {

            String fromPartyId = "NO_PARTY";
            String basePartyId = partyId;
            String workEffortId = "NA";
            String partyIdTo = "NO_PARTY";
            Map<String, String> userInfo = queryPersonBaseInfo(delegator, partyId);
            dispatcher.runSync("inForwardChainFact", UtilMisc.toMap(
                    "userLogin", admin,
                    "partyIdFrom", fromPartyId,
                    "partyIdTo", partyIdTo,
                    "workEffortId", workEffortId,
                    "basePartyId", basePartyId,
                    "firstName", userInfo.get("firstName"),
                    "objectInfo", userInfo.get("headPortrait"),
                    "createDate", new Timestamp(new Date().getTime())));
        }
        GenericValue myForwardChainFactTemp = EntityQuery.use(delegator).from("YpForwardChainFactTemp").where(
                "partyIdTo", partyId).queryFirst();

        // 更新我的软连接
        if (null != myForwardChainFactTemp) {
            myForwardChainFactTemp.set("basePartyId", partyId);
            myForwardChainFactTemp.store();
            Debug.logInfo("update ... " + myForwardChainFactTemp, module);
        }
        return resultMap;
    }


    public static Map<String, Object> queryMyPostalAddress(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();

        String openId = (String) context.get("openId");

        System.out.println("*OPENID = " + openId);

        GenericValue partyIdentification = EntityQuery.use(delegator).from("PartyIdentification").where("idValue", openId, "partyIdentificationTypeId", "WX_UNIO_ID").queryFirst();

        String partyId = "NA";

        if (UtilValidate.isNotEmpty(partyIdentification)) {
            partyId = (String) partyIdentification.get("partyId");
        }
        System.out.println("*partyId = " + partyId);


        Set<String> fieldSet = new HashSet<String>();
        fieldSet.add("contactMechId");
        fieldSet.add("partyId");
        fieldSet.add("address1");
        EntityCondition findConditions = EntityCondition
                .makeCondition(UtilMisc.toMap("partyId", partyId));

        //Query My Resource
        List<GenericValue> queryAddressList = delegator.findList("PartyAndPostalAddress",
                findConditions, fieldSet,
                UtilMisc.toList("-fromDate"), null, false);

        resultMap.put("postalAddress", queryAddressList);

        return resultMap;
    }


    /**
     * query MyCollect Product
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryMyCollectProduct(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();


        String openId = (String) context.get("openId");

        System.out.println("*OPENID = " + openId);

        GenericValue partyIdentification = EntityQuery.use(delegator).from("PartyIdentification").where("idValue", openId, "partyIdentificationTypeId", "WX_UNIO_ID").queryFirst();

        String partyId = "NA";

        if (UtilValidate.isNotEmpty(partyIdentification)) {
            partyId = (String) partyIdentification.get("partyId");
        }
        System.out.println("*partyId = " + partyId);


        List<GenericValue> partyMarkRoleList = EntityQuery.use(delegator).from("ProductRole").where("partyId", partyId, "roleTypeId", "PLACING_CUSTOMER").queryList();

        if (null != partyMarkRoleList) {


            for (GenericValue gv : partyMarkRoleList) {

                String productId = (String) gv.get("productId");

                Map<String, Object> rowMap = new HashMap<String, Object>();

                GenericValue product = delegator.findOne("Product", UtilMisc.toMap("productId", productId), false);


                Set<String> fieldSet = new HashSet<String>();
                fieldSet.add("productId");
                fieldSet.add("payToPartyId");

                EntityCondition findConditions = EntityCondition
                        .makeCondition(UtilMisc.toMap("productId", productId));

                GenericValue productDesc = delegator.findList("ProductAndCategoryMember",
                        findConditions, fieldSet,
                        null, null, false).get(0);
                GenericValue person = delegator.findOne("Person", UtilMisc.toMap("partyId", productDesc.get("payToPartyId")), false);


                List<GenericValue> contentsList =
                        EntityQuery.use(delegator).from("PartyContentAndDataResource").
                                where("partyId", productDesc.get("payToPartyId"), "partyContentTypeId", "LGOIMGURL").orderBy("-fromDate").queryPagedList(0, 999999).getData();


                GenericValue partyContent = null;
                if (null != contentsList && contentsList.size() > 0) {
                    partyContent = contentsList.get(0);
                }

                if (UtilValidate.isNotEmpty(partyContent)) {

                    rowMap.put("headPortrait",
                            partyContent.getString("objectInfo"));
                } else {
                    rowMap.put("headPortrait",
                            "https://personerp.oss-cn-hangzhou.aliyuncs.com/datas/images/defaultHead.png");
                }


                rowMap.put("payToPartyId", productDesc.get("payToPartyId"));

                rowMap.put("payToPartyName", person.get("firstName"));

                rowMap.put("partyId", partyId);

                rowMap.put("productId", productId);

                rowMap.put("internalName", product.get("internalName"));

                rowMap.put("productName", product.get("productName"));

                rowMap.put("detailImageUrl", product.get("detailImageUrl"));

                GenericValue productPrice = EntityQuery.use(delegator).from("ProductPrice").where("productId", productId).queryFirst();

                rowMap.put("price", productPrice.get("price"));

                returnList.add(rowMap);
            }

        }

        resultMap.put("collectList", returnList);

        return resultMap;
    }


    /**
     * queryOrderDetailFromWeChat
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryOrderDetailFromWeChat(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        String orderId = (String) context.get("orderId");

        //Order BaseInfo
        Map<String, Object> rowMap = new HashMap<String, Object>();
        //Express Info
        Map<String, Object> orderExpressInfo = new HashMap<String, Object>();

        GenericValue orderHeaderItemAndRoles = EntityQuery.use(delegator).from("OrderHeaderItemAndRoles").where("orderId", orderId, "roleTypeId", "SHIP_TO_CUSTOMER").queryFirst();

        if (null != orderHeaderItemAndRoles) {

            rowMap = orderHeaderItemAndRoles.getAllFields();

            GenericValue orderPaymentPrefAndPayment = EntityQuery.use(delegator).from("OrderPaymentPrefAndPayment").where("orderId", orderId).queryFirst();

            if (null != orderPaymentPrefAndPayment) {
                String orderPaymentPrefAndPaymentstatusId = (String) orderPaymentPrefAndPayment.get("statusId");

                if (orderPaymentPrefAndPaymentstatusId.equals("PAYMENT_RECEIVED")) {
                    rowMap.put("orderPayStatus", "已付款");
                    rowMap.put("payStatusCode", "1");
                } else {
                    rowMap.put("payStatusCode", "0");
                    rowMap.put("orderPayStatus", "待付款");
                }
            } else {

                rowMap.put("orderPayStatus", "待付款");

            }

            String productStoreId = (String) orderHeaderItemAndRoles.get("productStoreId");

            String productId = (String) orderHeaderItemAndRoles.get("productId");

            GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), false);

            GenericValue product = delegator.findOne("Product", UtilMisc.toMap("productId", productId), false);

            rowMap.put("productName", "" + product.get("productName"));

            rowMap.put("detailImageUrl", (String) product.get("detailImageUrl"));

            String payToPartyId = (String) productStore.get("payToPartyId");

            rowMap.put("payToPartyId", payToPartyId);


            Map<String, String> personInfoMap = null;

            Map<String, String> personAddressInfoMap = null;


            personInfoMap = queryPersonBaseInfo(delegator, payToPartyId);

            personAddressInfoMap = queryPersonAddressInfo(delegator, (String) rowMap.get("partyId"));

            rowMap.put("personInfoMap", personInfoMap);

            rowMap.put("personAddressInfoMap", personAddressInfoMap);

        }

        GenericValue order = delegator.findOne("OrderHeader", UtilMisc.toMap("orderId", orderId), false);
        if (null != order && null != order.get("internalCode")) {
            rowMap.put("internalCode", order.get("internalCode"));
        }


        //TODO QUERY orderExpressInfo
//        if(null != order && null != order.get("internalCode")){
//
//
//        Map<String,Object> queryExpressInfoMap = dispatcher.runSync("queryExpressInfo",UtilMisc.toMap("userLogin",admin,"code",order.get("internalCode")));
//        List<JSONObject> expressInfos = null;
//        if (ServiceUtil.isSuccess(queryExpressInfoMap)) {
//            expressInfos = (List<JSONObject>) queryExpressInfoMap.get("expressInfos");
//        }
//
//        rowMap.put("orderExpressInfo",expressInfos);
//        rowMap.put("orderExpressName",queryExpressInfoMap.get("name"));
//        }
        GenericValue orderHeaderAndShipGroups = EntityQuery.use(delegator).from("OrderHeaderAndShipGroups").
                where("orderId", orderId).queryFirst();
        rowMap.put("personAddressInfoMap", orderHeaderAndShipGroups);


        DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String createdDate = sdf.format(order.get("orderDate"));
        rowMap.put("createdDate", createdDate);

        //查询卖家提供的联系电话
        GenericValue telecomNumber = EntityUtil.getFirst(
                EntityQuery.use(delegator).from("TelecomNumberAndPartyView").where(UtilMisc.toMap("partyId", rowMap.get("payToPartyId"), "contactMechPurposeTypeId", "PHONE_MOBILE", "contactMechTypeId", "TELECOM_NUMBER")).queryList());
        if (UtilValidate.isNotEmpty(telecomNumber)) {
            rowMap.put("contactNumber", telecomNumber.getString("contactNumber"));
        } else {
            rowMap.put("contactNumber", null);
        }

        List<GenericValue> productFeatureAndAppls = EntityQuery.use(delegator).from("ProductFeatureAndAppl").where("productId", rowMap.get("productId")).queryList();
        List<String> featuresList = new ArrayList<String>();
        if (null != productFeatureAndAppls) {
            for (GenericValue gv2 : productFeatureAndAppls) {
                String productFeatureTypeId = (String) gv2.get("productFeatureTypeId");
                String description = (String) gv2.get("description");
                String compDesc = "";
                if (productFeatureTypeId.equals("SIZE")) {
                    compDesc = "尺寸:" + description;
                }
                if (productFeatureTypeId.equals("COLOR")) {
                    compDesc = "颜色:" + description;
                }
                featuresList.add(compDesc);
            }
        }
        rowMap.put("featuresList", featuresList);

        resultMap.put("orderDetail", rowMap);


        return resultMap;
    }


    /**
     * 小程序查销售单
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryMyResourceOrderFromWeChat(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();


        String unioId = (String) context.get("openId");


        GenericValue partyIdentification = EntityQuery.use(delegator).from("PartyIdentification").where("idValue", unioId, "partyIdentificationTypeId", "WX_MINIPRO_OPEN_ID").queryFirst();
        String partyId = "NA";

        if (UtilValidate.isNotEmpty(partyIdentification)) {
            partyId = (String) partyIdentification.get("partyId");
        }
        List<Map<String, Object>> myResourceOrderList = new ArrayList<Map<String, Object>>();
        GenericValue nowUserLogin = EntityQuery.use(delegator).from("UserLogin").where("partyId", partyId).queryFirst();


        //是否是从App端的查询
        String area = (String) context.get("area");


        String orderStatus = (String) context.get("orderStatus");


        Set<String> fieldSet = new HashSet<String>();
        fieldSet.add("orderId");
        fieldSet.add("partyId");
        fieldSet.add("statusId");
        fieldSet.add("currencyUom");
        fieldSet.add("grandTotal");
        fieldSet.add("internalCode");
        fieldSet.add("roleTypeId");
        fieldSet.add("orderDate");
        fieldSet.add("productStoreId");
        fieldSet.add("isViewed");


        EntityCondition roleTypeCondition = EntityCondition.makeCondition(UtilMisc.toMap("roleTypeId", "SALES_REP"));

        //定死素然
        EntityCondition payToPartyIdCondition = EntityCondition.makeCondition(UtilMisc.toMap("partyId", partyId));


        EntityCondition listConditions2 = null;

        //这种情况就是要查取消的订单
        String isCancelled = (String) context.get("isCancelled");
        if (null != orderStatus && orderStatus.equals("CANCEL")) {
            isCancelled = "1";
        }
        if ("ORDER_CANCELLED".equals(orderStatus)) {
            isCancelled = "1";
        }

        //如果isCancelled 为1  则查询取消的订单。
        if (!UtilValidate.isEmpty(isCancelled) && isCancelled.equals("1")) {
            EntityCondition statusConditions = EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, "ORDER_CANCELLED");
            EntityCondition genericCondition = EntityCondition.makeCondition(roleTypeCondition, EntityOperator.AND, payToPartyIdCondition);
            listConditions2 = EntityCondition.makeCondition(genericCondition, EntityOperator.AND, statusConditions);
        } else {
//            EntityCondition genericCondition = EntityCondition.makeCondition(roleTypeCondition, EntityOperator.AND, payToPartyIdCondition);
//            listConditions2 = EntityCondition.makeCondition(genericCondition);
            EntityCondition statusConditions = EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, orderStatus);
            EntityCondition genericCondition = EntityCondition.makeCondition(roleTypeCondition, EntityOperator.AND, payToPartyIdCondition);
            EntityCondition genericCondition2 = EntityCondition.makeCondition(genericCondition, EntityOperator.AND, statusConditions);
//                listConditions2 = EntityCondition.makeCondition(genericCondition2, EntityOperator.AND, orderCancelCondition);
            listConditions2 = EntityCondition.makeCondition(genericCondition2);
        }


        String queryEntity = "OrderHeaderItemAndRoles";
        queryEntity = "OrderHeaderAndRoles";


        List<GenericValue> queryMyResourceOrderList = null;

        if (null != orderStatus && orderStatus.equals("SHIPMENT")) {

            EntityCondition orderStatusCondition = EntityCondition.makeCondition(UtilMisc.toMap("statusId", "ORDER_COMPLETED"));

            EntityCondition listConditions3 = EntityCondition
                    .makeCondition(listConditions2, EntityOperator.AND, orderStatusCondition);

            queryMyResourceOrderList = delegator.findList(queryEntity,
                    listConditions3, fieldSet,
                    UtilMisc.toList("-orderDate"), null, false);

        } else {
            queryMyResourceOrderList = delegator.findList(queryEntity,
                    listConditions2, fieldSet,
                    UtilMisc.toList("-orderDate"), null, false);

        }

        GenericValue hiddenOrderPref = EntityQuery.use(delegator).from("UserPreference").where("userLoginId", nowUserLogin.getString("userLoginId"), "userPrefTypeId", "HIDDEN_ORDER").queryFirst();
        String[] hiddens = null;
        List<String> hiddenList = null;
        //说明该用户有需要隐藏的订单
        if (null != hiddenOrderPref) {
            String userPrefValue = hiddenOrderPref.getString("userPrefValue");
            if (userPrefValue.indexOf(",") > 0) {
                hiddens = userPrefValue.split(",");
            } else {
                hiddens = new String[]{userPrefValue};
            }
            hiddenList = Arrays.asList(hiddens);
        }

        if (null != queryMyResourceOrderList && queryMyResourceOrderList.size() > 0) {

            for (GenericValue gv : queryMyResourceOrderList) {

                Map<String, Object> rowMap = new HashMap<String, Object>();
                rowMap = gv.getAllFields();


                GenericValue orderHeaderAndShipGroupMaps = EntityQuery.use(delegator).from("OrderHeaderAndShipGroups").
                        where("orderId", gv.get("orderId")).queryFirst();
                rowMap.put("orderHeaderAndShipGroups", orderHeaderAndShipGroupMaps);

                String statusId = (String) gv.get("statusId");
                Map<String, Object> calcOrderTotal = dispatcher.runSync("getOrderAvailableReturnedTotal",
                        UtilMisc.toMap("orderId", rowMap.get("orderId")));
//                if(!statusId.equals("ORDER_CANCELLED")){    }
                BigDecimal availableTotal = (BigDecimal) calcOrderTotal.get("availableReturnTotal");
                int r = availableTotal.compareTo(BigDecimal.ZERO); //和0，Zero比较
                //小于
                if (r == -1 || r == 0) {
                    rowMap.put("grandTotal", "0");
                } else {
                    rowMap.put("grandTotal", calcOrderTotal.get("availableReturnTotal") + "");
                }

                //隐藏
                if (null != hiddenList) {
                    if (hiddenList.contains(rowMap.get("orderId"))) {
                        continue;
                    }
                }
                GenericValue orderNote = EntityQuery.use(delegator).from("OrderHeaderNoteView").
                        where("orderId", gv.get("orderId")).queryFirst();
                if (null != orderNote) {
                    rowMap.put("orderNote", orderNote.getAllFields());
                }

                List<Map<String, Object>> orderItemList = new ArrayList<Map<String, Object>>();
                List<GenericValue> orderItems = EntityQuery.use(delegator).from("OrderItem").where("orderId", rowMap.get("orderId")).queryList();
                for (GenericValue items : orderItems) {
                    Map<String, Object> itemMap = new HashMap<String, Object>();
                    String productId = (String) items.get("productId");
                    String quantity = items.get("quantity") + "";
                    BigDecimal quantityBdc = (BigDecimal) items.get("quantity");
                    BigDecimal unitPrice = (BigDecimal) items.get("unitPrice");
                    itemMap.put("unitPrice", unitPrice.intValue());

                    itemMap.put("quantity", quantityBdc.intValue());
                    itemMap.put("productId", productId);
                    List<GenericValue> productFeatureAndAppls = EntityQuery.use(delegator).from("ProductFeatureAndAppl").where("productId", productId).queryList();
                    List<String> featuresList = new ArrayList<String>();
                    if (null != productFeatureAndAppls) {
                        for (GenericValue gv2 : productFeatureAndAppls) {
                            String productFeatureTypeId = (String) gv2.get("productFeatureTypeId");
                            String description = (String) gv2.get("description");
                            String compDesc = "";
                            if (productFeatureTypeId.equals("SIZE")) {
                                compDesc = "尺寸:" + description;
                            }
                            if (productFeatureTypeId.equals("COLOR")) {
                                compDesc = "颜色:" + description;
                            }
                            featuresList.add(compDesc);
                        }
                    }
                    itemMap.put("featuresList", featuresList);
                    GenericValue product = delegator.findOne("Product", UtilMisc.toMap("productId", productId), false);

                    itemMap.put("productName", "" + product.get("productName"));
                    fieldSet = new HashSet<String>();
                    fieldSet.add("drObjectInfo");
                    fieldSet.add("productId");
                    fieldSet.add("productContentTypeId");
                    //有单品图就拿单品图,否则就拿首图
                    EntityCondition genericProductConditions = EntityCondition.makeCondition("productId", EntityOperator.EQUALS, productId);
                    EntityCondition singleTypeConditions = EntityCondition.makeCondition("productContentTypeId", EntityOperator.EQUALS, "SINGLE_PRODUCT_IMAGE");
                    EntityCondition singleCondition = EntityCondition.makeCondition(singleTypeConditions, EntityOperator.AND, genericProductConditions);
                    List<GenericValue> singlePictures = delegator.findList("ProductContentAndInfo", singleCondition, fieldSet,
                            null, null, false);
                    if (singlePictures != null && singlePictures.size() > 0) {
                        itemMap.put("detailImageUrl", singlePictures.get(0).get("drObjectInfo") + "");
                    } else {
                        itemMap.put("detailImageUrl", (String) product.get("detailImageUrl"));
                    }

                    orderItemList.add(itemMap);
                }


                rowMap.put("orderItemList", orderItemList);


                Timestamp createdDateTp = (Timestamp) gv.get("orderDate");

                rowMap.put("orderDate", dateToStr(createdDateTp, "yyyy-MM-dd HH:mm:ss"));

                String productStoreId = (String) gv.get("productStoreId");


                GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), false);

                String payToPartyId = (String) productStore.get("payToPartyId");

                rowMap.put("payToPartyId", payToPartyId);

                //查询店铺配置
                GenericValue queryAppConfig =
                        EntityQuery.use(delegator).from("PartyStoreAppConfig").where(
                                "productStoreId", productStoreId).queryFirst();
                String appServiceType = "2C";
                if (null != queryAppConfig) {
                    appServiceType = queryAppConfig.getString("appServiceType");
                }
                // 查询卖家付款二维码。
                if (appServiceType.toUpperCase().equals("2C")) {
                    GenericValue media = EntityQuery.use(delegator).from("PartyAttribute").where("partyId", payToPartyId, "attrName", "media_id").queryFirst();
                    if (null != media) {
                        rowMap.put("media_id", media.getString("attrValue"));
                    }
                }


                rowMap.put("statusId", UtilProperties.getMessage("PersonManagerUiLabels.xml", statusId, locale));

                String payFromPartyId = (String) rowMap.get("partyId");

                Map<String, String> personInfoMap = null;
                Map<String, String> personAddressInfoMap = null;

                GenericValue custOrderInfo = EntityQuery.use(delegator).from("OrderHeaderItemAndRoles").where("orderId", rowMap.get("orderId"), "roleTypeId", "SHIP_TO_CUSTOMER").queryFirst();

                personInfoMap = queryPersonBaseInfo(delegator, (String) custOrderInfo.get("partyId"));
                personAddressInfoMap = queryPersonAddressInfo(delegator, (String) custOrderInfo.get("partyId"));
                rowMap.put("realPartyId", custOrderInfo.get("partyId"));

                rowMap.put("userPartyId", partyId);

                rowMap.put("custPersonInfoMap", personInfoMap);
                rowMap.put("salesPersonInfoMap", queryPersonBaseInfo(delegator, payToPartyId));

                rowMap.put("personAddressInfoMap", personAddressInfoMap);


                GenericValue orderPaymentPrefAndPayment = EntityQuery.use(delegator).from("OrderPaymentPreference").where("orderId", gv.get("orderId")).orderBy("-createdStamp").queryFirst();

//	2018-04-04 14:27:49.0

                GenericValue payment = EntityQuery.use(delegator).from("Payment").where("partyIdTo", payToPartyId, "partyIdFrom", (String) custOrderInfo.get("partyId"), "comments", gv.get("orderId")).queryFirst();


                if (null != orderPaymentPrefAndPayment) {

                    String orderPaymentPrefAndPaymentstatusId = (String) orderPaymentPrefAndPayment.get("statusId");

                    if (orderPaymentPrefAndPaymentstatusId.equals("PAYMENT_RECEIVED")) {
                        rowMap.put("orderPayStatus", "已付款");
                        rowMap.put("payStatusCode", "1");
                    } else {
                        rowMap.put("payStatusCode", "0");
                        rowMap.put("orderPayStatus", "代付款");
                    }

                } else {
                    rowMap.put("payStatusCode", "0");
                    rowMap.put("orderPayStatus", "待付款");
                }
                GenericValue orderShipment = EntityQuery.use(delegator).from("OrderShipment").where("orderId", gv.get("orderId")).queryFirst();
                GenericValue orderItemShip = EntityQuery.use(delegator).from("OrderItemShipGroup").where("orderId", gv.get("orderId")).queryFirst();
                //理论上有这行数据,就肯定货运了
                if (null != orderShipment) {
                    rowMap.put("orderShipment", "待收货");
                    String trackingNumber = (String) orderItemShip.get("trackingNumber");
                    //说明是快递发货
                    if (null != trackingNumber) {
                        rowMap.put("internalCode", "" + trackingNumber);
                    } else {
                        rowMap.put("internalCode", "商家自配送");
                    }
                    if (rowMap.get("orderPayStatus").equals("已付款")) {
                        rowMap.put("orderCompleted", "已完成");
                    }
                } else {
                    rowMap.put("orderShipment", "待发货");
                }

                //不查询已收款的订单时,直接放入
                if (null != orderStatus && !orderStatus.equals("PAYMENT")) {
                    myResourceOrderList.add(rowMap);
                }
                if (null != orderStatus && orderStatus.equals("PAYMENT")) {
                    if (!rowMap.get("orderPayStatus").equals("未付款")) {
                        myResourceOrderList.add(rowMap);
                    }
                }
                //已取消的情况
                if (statusId.equals("ORDER_CANCELLED")) {
                    rowMap.put("orderPayStatus", "已取消");
                }
            }
        }

        resultMap.put("queryMyResourceOrderList", myResourceOrderList);
        return resultMap;
    }


    /**
     * query Order FromWeChat 采购单
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryOrderListFromWeChat(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();

        String orderStatusId = (String) context.get("orderStatus");
        String openId = (String) context.get("openId");

        System.out.println("*OPENID = " + openId);

        //暂时改成wx mini open id
//        GenericValue partyIdentification = EntityQuery.use(delegator).from("PartyIdentification").where("idValue", openId, "partyIdentificationTypeId", "WX_UNIO_ID").queryFirst();
        GenericValue partyIdentification = EntityQuery.use(delegator).from("PartyIdentification").where("idValue", openId, "partyIdentificationTypeId", "WX_MINIPRO_OPEN_ID").queryFirst();

        String partyId = "NA";

        if (UtilValidate.isNotEmpty(partyIdentification)) {
            partyId = (String) partyIdentification.get("partyId");
        }
        List<Map<String, Object>> orderList = new ArrayList<Map<String, Object>>();

        System.out.println("partyId ==  " + partyId);
        GenericValue nowUserLogin = EntityQuery.use(delegator).from("UserLogin").where("partyId", partyId).queryFirst();

        Set<String> fieldSet = new HashSet<String>();
        fieldSet.add("orderId");
        fieldSet.add("partyId");
        fieldSet.add("statusId");
        fieldSet.add("currencyUom");
        fieldSet.add("grandTotal");
//        fieldSet.add("productId");
//        fieldSet.add("quantity");
//        fieldSet.add("unitPrice");
        fieldSet.add("roleTypeId");
        fieldSet.add("orderDate");
        fieldSet.add("productStoreId");
//        fieldSet.add("payToPartyId");

        EntityCondition findConditions3 = EntityCondition.makeCondition("roleTypeId", EntityOperator.EQUALS, "BILL_TO_CUSTOMER");
//        EntityCondition orderCancelCondition = EntityCondition.makeCondition("statusId", EntityOperator.NOT_EQUAL, "ORDER_CANCELLED");
        EntityCondition findConditions = EntityCondition
                .makeCondition("partyId", EntityOperator.EQUALS, partyId);


        System.out.println("orderStatusId  ==   ? " + orderStatusId);
        EntityCondition listConditions2 = null;

        String findShipment = null;


        String isCancelled = (String) context.get("isCancelled");
        if (null != orderStatusId && orderStatusId.equals("CANCEL")) {
            isCancelled = "1";
        }
        if ("ORDER_CANCELLED".equals(orderStatusId)) {
            isCancelled = "1";
        }
        //如果isCancelled 为1  则查询取消的订单。
        if (!UtilValidate.isEmpty(isCancelled) && isCancelled.equals("1")) {
            EntityCondition statusConditions = EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, "ORDER_CANCELLED");
            EntityCondition genericCondition = EntityCondition.makeCondition(findConditions3, EntityOperator.AND, findConditions);
            listConditions2 = EntityCondition.makeCondition(genericCondition, EntityOperator.AND, statusConditions);
        } else {
            if (null != orderStatusId && orderStatusId.equals("SHIPMENT")) {
                findShipment = "SHIPMENT";

            } else {
                EntityCondition statusConditions = EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, orderStatusId);
                EntityCondition genericCondition = EntityCondition.makeCondition(findConditions3, EntityOperator.AND, findConditions);
                EntityCondition genericCondition2 = EntityCondition.makeCondition(genericCondition, EntityOperator.AND, statusConditions);
//                listConditions2 = EntityCondition.makeCondition(genericCondition2, EntityOperator.AND, orderCancelCondition);
                listConditions2 = EntityCondition.makeCondition(genericCondition2);
            }

        }


        List<GenericValue> queryMyResourceOrderList = null;


        String queryEntity = "OrderHeaderItemAndRoles";
        queryEntity = "OrderHeaderAndRoles";
        if (findShipment != null) {
            EntityCondition orderStatusCondition = EntityCondition.makeCondition(UtilMisc.toMap("statusId", "ORDER_COMPLETED"));
            EntityCondition genericCondition = EntityCondition.makeCondition(findConditions3, EntityOperator.AND, findConditions);
            EntityCondition listConditions3 = EntityCondition
                    .makeCondition(genericCondition, EntityOperator.AND, orderStatusCondition);
            //说明查已发货的
            queryMyResourceOrderList = delegator.findList(queryEntity,
                    listConditions3, fieldSet,
                    UtilMisc.toList("-orderDate"), null, false);
        } else {

            queryMyResourceOrderList = delegator.findList(queryEntity,
                    listConditions2, fieldSet,
                    UtilMisc.toList("-orderDate"), null, false);
        }

        GenericValue hiddenOrderPref = EntityQuery.use(delegator).from("UserPreference").where("userLoginId", nowUserLogin.getString("userLoginId"), "userPrefTypeId", "HIDDEN_ORDER").queryFirst();
        String[] hiddens = null;
        List<String> hiddenList = null;
        //说明该用户有需要隐藏的订单
        if (null != hiddenOrderPref) {
            String userPrefValue = hiddenOrderPref.getString("userPrefValue");
            if (userPrefValue.indexOf(",") > 0) {
                hiddens = userPrefValue.split(",");
            } else {
                hiddens = new String[]{userPrefValue};
            }
            hiddenList = Arrays.asList(hiddens);
        }
        Debug.logInfo("->hiddenList:" + hiddenList, module);
        if (null != queryMyResourceOrderList && queryMyResourceOrderList.size() > 0) {

            for (GenericValue gv : queryMyResourceOrderList) {

                Map<String, Object> rowMap = new HashMap<String, Object>();

                rowMap = gv.getAllFields();

                GenericValue orderHeaderAndShipGroupMap = EntityQuery.use(delegator).from("OrderHeaderAndShipGroups").
                        where("orderId", gv.get("orderId")).queryFirst();
                rowMap.put("orderHeaderAndShipGroups", orderHeaderAndShipGroupMap);

                String statusId = (String) gv.get("statusId");

                Map<String, Object> calcOrderTotal = dispatcher.runSync("getOrderAvailableReturnedTotal",
                        UtilMisc.toMap("orderId", rowMap.get("orderId")));

//                if(!statusId.equals("ORDER_CANCELLED")){}
                BigDecimal availableTotal = (BigDecimal) calcOrderTotal.get("availableReturnTotal");
                int r = availableTotal.compareTo(BigDecimal.ZERO); //和0，Zero比较
                //小于
                if (r == -1 || r == 0) {
                    rowMap.put("grandTotal", "0");
                } else {
                    rowMap.put("grandTotal", calcOrderTotal.get("availableReturnTotal") + "");
                }
                GenericValue orderNote = EntityQuery.use(delegator).from("OrderHeaderNoteView").
                        where("orderId", gv.get("orderId")).queryFirst();
                if (null != orderNote) {
                    rowMap.put("orderNote", orderNote.getAllFields());
                }

                //OrderReadHelper.calcOrderAdjustments(orderHeaderAdjustments, orderSubTotal, true, false, false)

                //grandTotal 要算订单调整项目


                //隐藏
                if (null != hiddenList) {
                    if (hiddenList.contains(rowMap.get("orderId"))) {
                        continue;
                    }
                }
                Timestamp createdDateTp = (Timestamp) gv.get("orderDate");

                rowMap.put("orderDate", dateToStr(createdDateTp, "yyyy-MM-dd HH:mm:ss"));
                Date date1 = createdDateTp;
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(date1.getTime());
//                    cal.add(Calendar.HOUR_OF_DAY, daysTillCancel);


                cal.add(Calendar.HOUR_OF_DAY, 4);
                Date cancelDate = cal.getTime();
                Date nowDate = new Date();

                long interval = (cancelDate.getTime() - nowDate.getTime()) / 1000;

                rowMap.put("cancelDate", cancelDate);
                rowMap.put("cancelDateStamp", cancelDate.getTime());
                rowMap.put("nowDateStamp", nowDate.getTime());
                if (interval > 0) {
                    long hours = (interval % (60 * 60 * 24)) / (60 * 60);
                    long minutes = (interval % (60 * 60)) / 60;
                    long seconds = interval % 60;
                    String dateTimes = "";
                    if (hours > 0) {
                        dateTimes = hours + "小时" + minutes + "分钟";
//                                + seconds + "秒";
                    } else if (minutes > 0) {
                        dateTimes = minutes + "分钟";
//                                + seconds + "秒";
                    } else {
                        dateTimes = seconds + "秒";
                    }
                    rowMap.put("interval", dateTimes);
                }

                String productStoreId = (String) gv.get("productStoreId");

                List<Map<String, Object>> orderItemList = new ArrayList<Map<String, Object>>();
                List<GenericValue> orderItems = EntityQuery.use(delegator).from("OrderItem").where("orderId", rowMap.get("orderId")).queryList();
                for (GenericValue items : orderItems) {
                    Map<String, Object> itemMap = new HashMap<String, Object>();
                    String productId = (String) items.get("productId");
                    String quantity = items.get("quantity") + "";
                    BigDecimal quantityBdc = (BigDecimal) items.get("quantity");
                    itemMap.put("quantity", quantityBdc.intValue());

                    BigDecimal unitPrice = (BigDecimal) items.get("unitPrice");
                    itemMap.put("unitPrice", unitPrice.intValue());

                    itemMap.put("productId", productId);
                    List<GenericValue> productFeatureAndAppls = EntityQuery.use(delegator).from("ProductFeatureAndAppl").where("productId", productId).queryList();
                    List<String> featuresList = new ArrayList<String>();
                    if (null != productFeatureAndAppls) {
                        for (GenericValue gv2 : productFeatureAndAppls) {
                            String productFeatureTypeId = (String) gv2.get("productFeatureTypeId");
                            String description = (String) gv2.get("description");
                            String compDesc = "";
                            if (productFeatureTypeId.equals("SIZE")) {
                                compDesc = "尺寸:" + description;
                            }
                            if (productFeatureTypeId.equals("COLOR")) {
                                compDesc = "颜色:" + description;
                            }
                            featuresList.add(compDesc);
                        }
                    }
                    itemMap.put("featuresList", featuresList);
                    GenericValue product = delegator.findOne("Product", UtilMisc.toMap("productId", productId), false);
                    EntityCondition genericProductConditions = EntityCondition.makeCondition("productId", EntityOperator.EQUALS, productId);
                    itemMap.put("productName", "" + product.get("productName"));
//有单品图就拿单品图,否则就拿首图
                    EntityCondition singleTypeConditions = EntityCondition.makeCondition("productContentTypeId", EntityOperator.EQUALS, "SINGLE_PRODUCT_IMAGE");
                    EntityCondition singleCondition = EntityCondition.makeCondition(singleTypeConditions, EntityOperator.AND, genericProductConditions);
                    fieldSet = new HashSet<String>();
                    fieldSet.add("drObjectInfo");
                    fieldSet.add("productId");
                    fieldSet.add("productContentTypeId");
                    List<GenericValue> singlePictures = delegator.findList("ProductContentAndInfo", singleCondition, fieldSet,
                            null, null, false);
                    if (singlePictures != null && singlePictures.size() > 0) {
                        itemMap.put("detailImageUrl", singlePictures.get(0).get("drObjectInfo") + "");
                    } else {
                        itemMap.put("detailImageUrl", (String) product.get("detailImageUrl"));
                    }


                    orderItemList.add(itemMap);
                }


                rowMap.put("orderItemList", orderItemList);


                GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), false);


                String payToPartyId = (String) productStore.get("payToPartyId");


                GenericValue salesRep = EntityQuery.use(delegator).from("OrderRole").where("orderId", gv.get("orderId"), "roleTypeId", "SALES_REP").queryFirst();


                rowMap.put("payToPartyId", payToPartyId);

                //查询店铺配置
                GenericValue queryAppConfig =
                        EntityQuery.use(delegator).from("PartyStoreAppConfig").where(
                                "productStoreId", productStoreId).queryFirst();
                String appServiceType = "2C";
                if (null != queryAppConfig) {
                    appServiceType = queryAppConfig.getString("appServiceType");
                }
                // 查询卖家付款二维码。
                if (appServiceType.toUpperCase().equals("2C")) {
                    GenericValue media = EntityQuery.use(delegator).from("PartyAttribute").where("partyId", payToPartyId, "attrName", "media_id").queryFirst();
                    if (null != media) {
                        rowMap.put("media_id", media.getString("attrValue"));
                    }
                }

//                GenericValue wxPayQrCodes =
//                        EntityQuery.use(delegator).from("PartyContentAndDataResource").
//                                where("partyId", payToPartyId, "partyContentTypeId", "WECHATQRCODE").orderBy("-fromDate").queryFirst();
//
//                if (null != wxPayQrCodes) {
//                    rowMap.put("weChatPayQrCode", wxPayQrCodes.getString("objectInfo"));
//                }


                //区分订单状态
                if (statusId.toLowerCase().indexOf("comp") > 0) {
                    rowMap.put("orderStatusCode", "1");
                } else {
                    rowMap.put("orderStatusCode", "0");
                }
                System.out.println("orderStatusCode = " + rowMap.get("orderStatusCode"));

                rowMap.put("statusId", UtilProperties.getMessage("PersonManagerUiLabels.xml", statusId, locale));


                String payFromPartyId = (String) rowMap.get("partyId");

                Map<String, String> personInfoMap = null;

                Map<String, String> personAddressInfoMap = null;

                if (null != salesRep) {
                    String salesRepId = (String) salesRep.get("partyId");
                    //查询的是销售代表
                    rowMap.put("salesPersonInfoMap", queryPersonBaseInfo(delegator, salesRepId));
                }

//                rowMap.put("salesPersonInfoMap", queryPersonBaseInfo(delegator, payToPartyId));
                rowMap.put("custPersonInfoMap", queryPersonBaseInfo(delegator, payFromPartyId));
                if (payToPartyId.equals(partyId)) {
                    personAddressInfoMap = queryPersonAddressInfo(delegator, payFromPartyId);
                }
                if (!payToPartyId.equals(partyId)) {
                    personAddressInfoMap = queryPersonAddressInfo(delegator, payToPartyId);
                }


                rowMap.put("custPartyId", payFromPartyId);
                rowMap.put("salesPartyId", payToPartyId);

                rowMap.put("userPartyId", partyId);

                rowMap.put("personInfoMap", personInfoMap);

                rowMap.put("personAddressInfoMap", personAddressInfoMap);






                GenericValue orderPaymentPrefAndPayment = EntityQuery.use(delegator).from("OrderPaymentPreference").where("orderId", gv.get("orderId")).queryFirst();

                GenericValue payment = EntityQuery.use(delegator).from("Payment").where("partyIdTo", payToPartyId, "partyIdFrom", payFromPartyId, "comments", rowMap.get("orderId")).queryFirst();
                if (null != orderPaymentPrefAndPayment) {
                    String orderPaymentPrefAndPaymentstatusId = (String) orderPaymentPrefAndPayment.get("statusId");

                    if (orderPaymentPrefAndPaymentstatusId.equals("PAYMENT_RECEIVED")) {
                        rowMap.put("orderPayStatus", "已付款");
                        rowMap.put("payStatusCode", "1");
                        rowMap.put("interval", null);
                    } else {
                        rowMap.put("payStatusCode", "0");
                        rowMap.put("orderPayStatus", "待付款");
                    }

                } else {
                    rowMap.put("payStatusCode", "0");
                    rowMap.put("orderPayStatus", "待付款");

                }

//                if(!statusId.equals("ORDER_SENT")){
//                    rowMap.put("orderShipment","未发货");
//                }else{
//                    rowMap.put("orderShipment","已发货");
//                    if(rowMap.get("orderPayStatus").equals("已收款")){
//                        rowMap.put("orderCompleted","已完成");
//                    }
//                }
                GenericValue orderShipment = EntityQuery.use(delegator).from("OrderShipment").where("orderId", gv.get("orderId")).queryFirst();
                GenericValue orderItemShip = EntityQuery.use(delegator).from("OrderItemShipGroup").where("orderId", gv.get("orderId")).queryFirst();
                //理论上有这行数据,就肯定货运了
                if (null != orderShipment) {
                    rowMap.put("orderShipment", "待收货");
                    String trackingNumber = (String) orderItemShip.get("trackingNumber");
                    //说明是快递发货
                    if (null != trackingNumber) {
                        rowMap.put("internalCode", "" + trackingNumber);
                    } else {
                        rowMap.put("internalCode", "商家自配送");
                    }
                    if (rowMap.get("orderPayStatus").equals("已付款")) {
                        rowMap.put("orderCompleted", "已完成");
                    }
                } else {
                    rowMap.put("orderShipment", "待发货");
                }
                //不查询已收款的订单时,直接放入
                if (null != orderStatusId && !orderStatusId.equals("PAYMENT")) {
                    orderList.add(rowMap);
                }
                if (null != orderStatusId && orderStatusId.equals("PAYMENT")) {
                    if (!rowMap.get("orderPayStatus").equals("待付款")) {
                        orderList.add(rowMap);
                    }
                }
                if (UtilValidate.isEmpty(orderStatusId)) {
                    orderList.add(rowMap);
                }
                if (statusId.equals("ORDER_CANCELLED")) {
                    rowMap.put("orderPayStatus", "已取消");
                }

            }
        }


        resultMap.put("orderList", orderList);

        return resultMap;
    }
}
