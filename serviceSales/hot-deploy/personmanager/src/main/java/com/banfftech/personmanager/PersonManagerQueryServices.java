package main.java.com.banfftech.personmanager;

import main.java.com.banfftech.platformmanager.constant.PeConstant;
import org.apache.ofbiz.entity.GenericEntity;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.entity.util.EntityUtil;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.util.EntityFindOptions;
import org.apache.ofbiz.entity.condition.EntityConditionList;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.eclipse.birt.chart.extension.datafeed.GanttEntry;

import java.util.*;

/**
 * Created by S on 2017/9/12.
 */
public class PersonManagerQueryServices {

    public final static String module = PersonManagerQueryServices.class.getName();

    public static final String resourceError = "PlatformManagerErrorUiLabels.xml";


    /**
     * Query MyRelationShip
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryMyRelationShip(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();


        //Scope Param
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = (String) userLogin.get("partyId");

        List<Map<String,Object>> relationList = new ArrayList<Map<String, Object>>();

        Map<String,Object> relationMap = new HashMap<String, Object>();



        Set<String> fieldSet = new HashSet<String>();
        fieldSet.add("partyIdFrom");
        fieldSet.add("partyIdTo");
        fieldSet.add("roleTypeIdFrom");
        fieldSet.add("roleTypeIdTo");
        fieldSet.add("partyRelationshipTypeId");


        EntityCondition findConditions = EntityCondition
                .makeCondition(UtilMisc.toMap("partyIdFrom", partyId));


        //Query My Resource
        List<GenericValue> queryMyRelationListList = delegator.findList("PartyRelationship",
                findConditions, fieldSet,
                UtilMisc.toList("-createdStamp"), null, false);


        if(null != queryMyRelationListList && queryMyRelationListList.size()>0){

            for(GenericValue gv : queryMyRelationListList){
                Map<String,Object> rowMap = new HashMap<String, Object>();
                rowMap = gv.getAllFields();
                String partyIdTo = (String) gv.get("partyIdTo");
                String partyRelationshipTypeId = (String) gv.get("partyRelationshipTypeId");


                if(!relationMap.containsKey(partyIdTo)){

                    relationMap.put(partyIdTo,"");
                GenericValue person = delegator.findOne("Person",UtilMisc.toMap("partyId",partyIdTo),false);
                rowMap.put("firstName",(String) person.get("firstName"));
                 List<GenericValue> contentsList =
                            EntityQuery.use(delegator).from("PartyContentAndDataResource").
                                    where("partyId",partyIdTo, "partyContentTypeId", "LGOIMGURL").orderBy("-fromDate").queryPagedList(0,999999).getData();
                 GenericValue findPartyContent = null;
                 if(null != contentsList && contentsList.size()>0){
                        findPartyContent = contentsList.get(0);
                 }

                 if (null != findPartyContent && null != findPartyContent.get("objectInfo")) {
                     rowMap.put("headPortrait",
                                (String) findPartyContent.get("objectInfo"));
                 } else {
                     rowMap.put("headPortrait",
                                "https://personerp.oss-cn-hangzhou.aliyuncs.com/datas/images/defaultHead.png");
                  }

                rowMap.put("partyRelationshipTypeId",UtilProperties.getMessage("PersonManagerUiLabels.xml", partyRelationshipTypeId, locale));
                relationList.add(rowMap);
                }
            }
        }

        resultMap.put("relationList", relationList);

        return resultMap;
    }








    /**
     * Query MyResource Order
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryMyResourceOrder(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();


        //Scope Param
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = (String) userLogin.get("partyId");

        List<Map<String,Object>> myResourceOrderList = new ArrayList<Map<String, Object>>();




        Set<String> fieldSet = new HashSet<String>();
        fieldSet.add("orderId");
        fieldSet.add("partyId");
        fieldSet.add("statusId");
        fieldSet.add("currencyUom");
        fieldSet.add("grandTotal");
        fieldSet.add("productId");
        fieldSet.add("quantity");
        fieldSet.add("unitPrice");
        fieldSet.add("orderDate");
        fieldSet.add("productStoreId");



        EntityCondition findConditions = EntityCondition
                .makeCondition(UtilMisc.toMap("partyId", partyId));

        EntityCondition findConditions2 = EntityCondition
                .makeCondition(UtilMisc.toMap("roleTypeId", "BILL_TO_CUSTOMER"));

        EntityConditionList<EntityCondition> listConditions = EntityCondition
                .makeCondition(findConditions, findConditions2);

//        EntityCondition findSalesConditions = EntityCondition
//                .makeCondition(UtilMisc.toMap("payToPartyId", partyId));
//
//        EntityCondition findSalesConditions2 = EntityCondition
//                .makeCondition(UtilMisc.toMap("roleTypeId", "SHIP_FROM_VENDOR"));
//
//        EntityConditionList<EntityCondition> listSalesConditions = EntityCondition
//                .makeCondition(findSalesConditions, findSalesConditions2);


        List<GenericValue> queryMyResourceOrderList = delegator.findList("OrderHeaderItemAndRoles",
                listConditions, fieldSet,
                UtilMisc.toList("-orderDate"), null, false);
//        List<GenericValue> queryMySalesResourceOrderList = delegator.findList("OrderHeaderItemAndRoles",
//                listSalesConditions, fieldSet,
//                UtilMisc.toList("-orderDate"), null, false);

        if(null != queryMyResourceOrderList && queryMyResourceOrderList.size()>0){

            for(GenericValue gv : queryMyResourceOrderList){
                Map<String,Object> rowMap = new HashMap<String, Object>();
                rowMap = gv.getAllFields();
                String productStoreId = (String) gv.get("productStoreId");
                String productId = (String) gv.get("productId");
                GenericValue product = delegator.findOne("Product",UtilMisc.toMap("productId",productId),false);
                GenericValue productStore = delegator.findOne("ProductStore",UtilMisc.toMap("productStoreId",productStoreId),false);
                rowMap.put("productName",""+product.get("productName"));
                rowMap.put("detailImageUrl",(String)product.get("detailImageUrl"));
                rowMap.put("payToPartyId",(String)productStore.get("payToPartyId"));
                String statusId = (String) gv.get("statusId");
                rowMap.put("statusId",UtilProperties.getMessage("PersonManagerUiLabels.xml", statusId, locale));
                myResourceOrderList.add(rowMap);
            }
        }


//        if(null != queryMySalesResourceOrderList && queryMySalesResourceOrderList.size()>0){
//
//            for(GenericValue gv : queryMySalesResourceOrderList){
//                Map<String,Object> rowMap = new HashMap<String, Object>();
//                rowMap = gv.getAllFields();
//                String productStoreId = (String) gv.get("productStoreId");
//                String productId = (String) gv.get("productId");
//                GenericValue product = delegator.findOne("Product",UtilMisc.toMap("productId",productId),false);
//                GenericValue productStore = delegator.findOne("ProductStore",UtilMisc.toMap("productStoreId",productStoreId),false);
//                rowMap.put("productName",""+product.get("productName"));
//                rowMap.put("detailImageUrl",(String)product.get("detailImageUrl"));
//                rowMap.put("partyId",(String)gv.get("partyId"));
//                String statusId = (String) gv.get("statusId");
//                rowMap.put("statusId",UtilProperties.getMessage("PersonManagerUiLabels.xml", statusId, locale));
//                myResourceOrderList.add(rowMap);
//            }
//        }

        resultMap.put("queryMyResourceOrderList", myResourceOrderList);

        return resultMap;
    }


    /**
     * 查询资源详情
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryResourceDetail(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();


        //Scope Param
        GenericValue userLogin = (GenericValue) context.get("userLogin");

        String productId = (String) context.get("productId");

        Set<String> fieldSet = new HashSet<String>();
        fieldSet.add("productId");
        fieldSet.add("productName");
        fieldSet.add("productStoreId");
        fieldSet.add("detailImageUrl");
        fieldSet.add("createdDate");
        fieldSet.add("salesDiscontinuationDate");
        fieldSet.add("price");
        fieldSet.add("prodCatalogId");
        fieldSet.add("payToPartyId");
        //    Map<String,Object> productMap = EntityQuery.use(delegator).from("Product").where("productId",productId).queryFirst().getAllFields();

        EntityCondition findConditions = EntityCondition
                .makeCondition(UtilMisc.toMap("productId", productId));

        GenericValue product = delegator.findList("ProductAndCategoryMember",
                findConditions, fieldSet,
                null, null, false).get(0);
        Map<String, Object> resourceDetail = product.getAllFields();
        GenericValue person = delegator.findOne("Person", UtilMisc.toMap("partyId", resourceDetail.get("payToPartyId")), false);
        if(person!=null){
            resourceDetail.put("firstName",(String) person.get("firstName"));
        }





        Set<String> orderFieldSet = new HashSet<String>();
        fieldSet.add("orderId");
        fieldSet.add("partyId");
        fieldSet.add("productId");



        EntityCondition findOrderConditions = EntityCondition
                .makeCondition(UtilMisc.toMap("productId", productId));



        //Query My Resource
        List<GenericValue> queryMyResourceOrderList = delegator.findList("OrderHeaderItemAndRoles",
                findOrderConditions, orderFieldSet,
                UtilMisc.toList("-orderDate"), null, false);

        List<Map<String,Object>> partyOrderList = new ArrayList<Map<String, Object>>();
        if(queryMyResourceOrderList!=null && queryMyResourceOrderList.size()>0){
            for(GenericValue order : queryMyResourceOrderList){
                Map<String,Object> rowMap = new HashMap<String, Object>();
                String partyId = (String) order.get("partyId");
                GenericValue orderPerson = delegator.findOne("Person", UtilMisc.toMap("partyId", order.get("partyId")), false);
                if(person!=null){
                    rowMap.put("firstName",(String) orderPerson.get("firstName"));
                }
                partyOrderList.add(rowMap);
            }
        }
        resourceDetail.put("partyBuyOrder",partyOrderList);

        resultMap.put("resourceDetail", resourceDetail);

        return resultMap;
    }


    /**
     * 按照维度查询
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryDimensionResource(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();


        //Scope Param
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = (String) userLogin.get("partyId");
        String productCategoryId = "";
        List<Map<String,Object>> returnList = new ArrayList<Map<String, Object>>();
        List<GenericValue> dimensionResourceList = null;

        //查我的目录
        GenericValue rodCatalogRole = EntityQuery.use(delegator).from("ProdCatalogRole").where("partyId", partyId, "roleTypeId", "ADMIN").queryFirst();

//        EntityFindOptions findOptions = new EntityFindOptions();
//        findOptions.setFetchSize(0);
//        findOptions.setMaxRows(4);
        //Select Fields
        Set<String> fieldSet = new HashSet<String>();
        fieldSet.add("productId");
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
                    .makeCondition("productCategoryId", EntityOperator.NOT_EQUAL, productCategoryId);

            EntityCondition findConditions2 = EntityCondition
                    .makeCondition(UtilMisc.toMap("productTypeId", "SERVICE"));
            EntityCondition findConditions3 = EntityCondition
                    .makeCondition("productName", EntityOperator.NOT_EQUAL, GenericEntity.NULL_FIELD.toString());
            EntityCondition findConditions4 = EntityCondition
                    .makeCondition("salesDiscontinuationDate", EntityOperator.EQUALS, GenericEntity.NULL_FIELD);


            EntityConditionList<EntityCondition> listConditions = EntityCondition
                    .makeCondition(findConditions, findConditions2, findConditions3);


            //Query My Resource
            dimensionResourceList = delegator.findList("ProductAndCategoryMember",
                    listConditions, fieldSet,
                    UtilMisc.toList("-createdDate"), null, false);
            //EntityQuery.use(delegator).from("ProductAndCategoryMember").where("productCategoryId",productCategoryId).queryList();

            if(null != dimensionResourceList && dimensionResourceList.size()>0){
                for(GenericValue gv : dimensionResourceList){
                    Map<String,Object> rowMap = new HashMap<String, Object>();
                    rowMap = gv.getAllFields();
                    String payToPartyId = (String)gv.get("payToPartyId");
                    GenericValue person = delegator.findOne("Person", UtilMisc.toMap("partyId", payToPartyId), false);
                    if(person!=null){
                        rowMap.put("firstName",(String) person.get("firstName"));
                    }

                    returnList.add(rowMap);
                }
            }
        }





        resultMap.put("dimensionResourceList", returnList);

        return resultMap;
    }


    /**
     * queryPersonInfo
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     */
    public static Map<String, Object> queryPersonInfo(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException {


        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        Map<String, Object> inputMap = UtilMisc.toMap();

        String partyId = (String) userLogin.get("partyId");



        GenericValue person = delegator.findOne("Person", false, UtilMisc.toMap("partyId", partyId));


        GenericValue party = delegator.findOne("Party", UtilMisc.toMap("partyId", partyId), false);



        inputMap.put("personName",(String) person.get("firstName"));
        //获取性别
        String gender = "";
        if (UtilValidate.isNotEmpty(person.get("gender"))) {
            // gender = "M".equals(person.get("gender")) ? "男" : "女";
            inputMap.put("gender", person.get("gender"));
        } else {
            inputMap.put("gender", "NA");
        }

        //获取电话号码
        GenericValue telecomNumber = EntityUtil.getFirst(
                EntityQuery.use(delegator).from("TelecomNumberAndPartyView").where(UtilMisc.toMap("partyId", partyId, "contactMechPurposeTypeId", "PHONE_MOBILE", "contactMechTypeId", "TELECOM_NUMBER")).queryList());
        if (UtilValidate.isNotEmpty(telecomNumber)){
            inputMap.put("contactNumber", telecomNumber.getString("contactNumber"));
        }
        //获取email
        GenericValue emailAddress = EntityUtil.getFirst(
                EntityQuery.use(delegator).from("EmailAndPartyView").where(UtilMisc.toMap("partyId", partyId, "contactMechPurposeTypeId", "PRIMARY_EMAIL", "contactMechTypeId", "EMAIL_ADDRESS")).queryList());
        if (UtilValidate.isNotEmpty(emailAddress)) inputMap.put("email", emailAddress.getString("infoString"));
        //获取地址
        GenericValue postalAddress = EntityUtil.getFirst(
                EntityQuery.use(delegator).from("PostalAddressAndPartyView").where(UtilMisc.toMap("partyId", partyId, "contactMechPurposeTypeId", "PRIMARY_LOCATION", "contactMechTypeId", "POSTAL_ADDRESS")).queryList());
        if (UtilValidate.isNotEmpty(postalAddress))
            inputMap.put("contactAddress", "" + postalAddress.get("geoName") + " " + postalAddress.get("city") + " " + postalAddress.get("address2") + " " + postalAddress.get("address1"));
        if (UtilValidate.isNotEmpty(emailAddress)) inputMap.put("email", emailAddress.getString("infoString"));


        List<GenericValue> contentsList =
                EntityQuery.use(delegator).from("PartyContentAndDataResource").
                        where("partyId", partyId, "partyContentTypeId", "LGOIMGURL").orderBy("-fromDate").queryPagedList(0,999999).getData();


        GenericValue partyContent = null;
        if(null != contentsList && contentsList.size()>0){
            partyContent = contentsList.get(0);
        }

        if (UtilValidate.isNotEmpty(partyContent)) {

            String contentId = partyContent.getString("contentId");
            inputMap.put("headPortrait",
                    partyContent.getString("objectInfo"));
        } else {
            inputMap.put("headPortrait",
                    "https://personerp.oss-cn-hangzhou.aliyuncs.com/datas/images/defaultHead.png");
        }


        List<GenericValue> partyIdentificationList = EntityQuery.use(delegator).from("PartyIdentification").where("partyId", partyId, "partyIdentificationTypeId", "WX_OPEN_ID").queryList();


        if (null != partyIdentificationList && partyIdentificationList.size() > 0) {
            String openId = (String) partyIdentificationList.get(0).get("idValue");

            inputMap.put("openId", partyIdentificationList.get(0).get("idValue"));
        } else {
            inputMap.put("openId", "NA");
        }


        resultMap.put("userInfo", inputMap);
        return resultMap;
    }





    /**
     * 查询我的资源(包含自己发布的和从别处购买的)
     *
     * @param dctx
     * @param context
     * @return
     */
    public static Map<String, Object> queryMyResource(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();


        //Scope Param
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = (String) userLogin.get("partyId");
        String productCategoryId = "NA";
        List<GenericValue> myResourceList = null;


        //查我的目录
        GenericValue rodCatalogRole = EntityQuery.use(delegator).from("ProdCatalogRole").where("partyId", partyId, "roleTypeId", "ADMIN").queryFirst();

        EntityFindOptions findOptions = new EntityFindOptions();
        findOptions.setFetchSize(0);
        findOptions.setMaxRows(4);
        //Select Fields
        Set<String> fieldSet = new HashSet<String>();
        fieldSet.add("productId");
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
            EntityCondition findConditions2 = EntityCondition
                    .makeCondition("salesDiscontinuationDate", EntityOperator.EQUALS, GenericEntity.NULL_FIELD);



            EntityConditionList<EntityCondition> listConditions = EntityCondition
                    .makeCondition(findConditions, findConditions2);

            //Query My Resource
            myResourceList = delegator.findList("ProductAndCategoryMember",
                    listConditions, fieldSet,
                    UtilMisc.toList("-createdDate"), findOptions, false);
            //EntityQuery.use(delegator).from("ProductAndCategoryMember").where("productCategoryId",productCategoryId).queryList();
        }


        resultMap.put("myResourceList", myResourceList);
        resultMap.put("productCategoryId", productCategoryId);

        return resultMap;
    }


}
