package main.java.com.banfftech.personmanager;

import main.java.com.banfftech.platformmanager.constant.PeConstant;
import org.apache.ofbiz.entity.GenericEntity;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilProperties;
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

        //Query My Resource
        List<GenericValue> queryMyResourceOrderList = delegator.findList("OrderHeaderItemAndRoles",
                listConditions, fieldSet,
                UtilMisc.toList("-orderDate"), null, false);


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
//            EntityCondition findConditions2 = EntityCondition
//                    .makeCondition(UtilMisc.toMap("productTypeId", "SERVICE"));
//            EntityCondition findConditions3 = EntityCondition
//                    .makeCondition("productName", EntityOperator.NOT_EQUAL, GenericEntity.NULL_FIELD);


            //Query My Resource
            myResourceList = delegator.findList("ProductAndCategoryMember",
                    findConditions, fieldSet,
                    UtilMisc.toList("-createdDate"), findOptions, false);
            //EntityQuery.use(delegator).from("ProductAndCategoryMember").where("productCategoryId",productCategoryId).queryList();
        }


        resultMap.put("myResourceList", myResourceList);
        resultMap.put("productCategoryId", productCategoryId);

        return resultMap;
    }


}
