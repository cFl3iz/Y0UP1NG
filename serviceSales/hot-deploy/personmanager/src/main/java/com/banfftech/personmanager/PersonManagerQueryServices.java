package main.java.com.banfftech.personmanager;

import main.java.com.banfftech.platformmanager.constant.PeConstant;
import org.apache.ofbiz.entity.GenericEntity;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.entity.util.EntityUtil;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import net.sf.json.JSONObject;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.condition.EntityOperator;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.geom.GeneralPath;
import java.util.*;

import main.java.com.banfftech.platformmanager.util.GZIP;



/**
 * Created by S on 2017/9/12.
 */
public class PersonManagerQueryServices {

    public final static String module = PersonManagerQueryServices.class.getName();

    public static final String resourceError = "PlatformManagerErrorUiLabels.xml";

    public static final String resourceUiLabels = "PlatformManagerUiLabels.xml";

    /**
     * Query ConsumerInfo
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryConsumerInfo(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();

        Delegator delegator = dispatcher.getDelegator();

        Locale locale = (Locale) context.get("locale");

        Map<String, Object> resultMap = ServiceUtil.returnSuccess();

        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();

        //Scope Param
        GenericValue userLogin = (GenericValue) context.get("userLogin");

        String partyId = (String) userLogin.get("partyId");

        String realPartyId = (String) context.get("realPartyId");

        resultMap.put("realPartyId",realPartyId);

        String productCategoryId  = "";


//        List<GenericValue> custProductRole = EntityQuery.use(delegator).from("ProductRole").where(UtilMisc.toMap("productId", productId,"contactMechTypeId", "POSTAL_ADDRESS")).queryList();


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
//            EntityCondition findConditions2 = EntityCondition
//                    .makeCondition("salesDiscontinuationDate", EntityOperator.EQUALS, GenericEntity.NULL_FIELD);
//
//
//            EntityConditionList<EntityCondition> listConditions = EntityCondition
//                    .makeCondition(findConditions, findConditions2);

            //Query My Resource
            List<GenericValue> myResourceList = delegator.findList("ProductAndCategoryMember",
                    findConditions, fieldSet,
                    UtilMisc.toList("-createdDate"), findOptions, false);

            if(null != myResourceList && myResourceList.size()>0){
                for(GenericValue gv :myResourceList){
                    Map<String,Object> rowMap = new HashMap<String, Object>();

                    String productId = (String)gv.get("productId");

                    rowMap.put("productName",(String)gv.get("productName"));
                    rowMap.put("productId",productId);
                    rowMap.put("productStoreId",(String)gv.get("productStoreId"));
                    rowMap.put("detailImageUrl",(String)gv.get("detailImageUrl"));
                    rowMap.put("createdDate",gv.get("createdDate"));
                    rowMap.put("price",gv.get("price"));
                    rowMap.put("productCategoryId",(String)gv.get("productCategoryId"));
                    rowMap.put("payToPartyId",(String)gv.get("payToPartyId"));
                    rowMap.put("description",(String)gv.get("description"));


                    fieldSet = new HashSet<String>();

                    fieldSet.add("drObjectInfo");

                    fieldSet.add("productId");

                    EntityCondition findConditions3 = EntityCondition
                            .makeCondition("productId", EntityOperator.EQUALS,productId );

                    GenericValue  custProductRole = EntityQuery.use(delegator).from("ProductRole").where("productId",productId,"partyId",realPartyId).queryFirst();
                    if(custProductRole!=null){
                        String custProductRoleStr =  UtilProperties.getMessage(resourceUiLabels,custProductRole.get("roleTypeId") +"", locale);
                        rowMap.put("productPartyRole",custProductRoleStr);
                        returnList.add(rowMap);
                        GenericValue  isConfirm = EntityQuery.use(delegator).from("MessageLog").where("objectId",productId,"partyIdFrom",partyId,"partyIdTo",realPartyId,"badge", "CHECK","message", " 已收到您的货款! ").queryFirst();
                        if(isConfirm!=null){
                            rowMap.put("isConfirmPay","TRUE");
                        }else{
                            rowMap.put("isConfirmPay","FALSE");
                        }
                    }


                }
            }


            //  ProductContentAndInfo

        }












        List<Map<String,Object>> orderList = new ArrayList<Map<String, Object>>();




        Set<String> fieldOrderSet = new HashSet<String>();
        fieldOrderSet.add("orderId");
        fieldOrderSet.add("partyId");
        fieldOrderSet.add("statusId");
        fieldOrderSet.add("currencyUom");
        fieldOrderSet.add("grandTotal");
        fieldOrderSet.add("productId");
        fieldOrderSet.add("quantity");
        fieldOrderSet.add("unitPrice");
        fieldOrderSet.add("roleTypeId");
        fieldOrderSet.add("orderDate");
        fieldOrderSet.add("productStoreId");
        fieldOrderSet.add("payToPartyId");

        EntityCondition findConditions3 = EntityCondition
                .makeCondition(UtilMisc.toMap("roleTypeId", "BILL_TO_CUSTOMER"));

        EntityCondition findConditions = EntityCondition
                .makeCondition(UtilMisc.toMap("partyId", realPartyId));


        EntityCondition findConditions2 = EntityCondition
                .makeCondition(UtilMisc.toMap("payToPartyId",partyId));

        EntityCondition listConditions = EntityCondition
                .makeCondition(findConditions,EntityOperator.AND,findConditions2);

        EntityCondition listConditions2 = EntityCondition
                .makeCondition(findConditions3,EntityOperator.AND,listConditions);


        List<GenericValue> queryMyResourceOrderList = delegator.findList("OrderHeaderItemAndRoles",
                listConditions2, fieldOrderSet,
                UtilMisc.toList("-orderDate"), null, false);




        if(null != queryMyResourceOrderList && queryMyResourceOrderList.size()>0){

            for(GenericValue gv : queryMyResourceOrderList){

                Map<String,Object> rowMap = new HashMap<String, Object>();

                rowMap = gv.getAllFields();



                String productStoreId = (String) gv.get("productStoreId");

                String productId = (String) gv.get("productId");

                GenericValue productStore = delegator.findOne("ProductStore",UtilMisc.toMap("productStoreId",productStoreId),false);

                GenericValue product = delegator.findOne("Product",UtilMisc.toMap("productId",productId),false);

                rowMap.put("productName",""+product.get("productName"));

                rowMap.put("detailImageUrl",(String)product.get("detailImageUrl"));

                String payToPartyId = (String)productStore.get("payToPartyId");

                rowMap.put("payToPartyId",payToPartyId);

                String statusId = (String) gv.get("statusId");

                //区分订单状态
                if(statusId.toLowerCase().indexOf("comp")>0){
                    rowMap.put("orderStatusCode","1");
                }else{
                    rowMap.put("orderStatusCode","0");
                }

                System.out.println("orderStatusCode = " + rowMap.get("orderStatusCode"));

                rowMap.put("statusId",UtilProperties.getMessage("PersonManagerUiLabels.xml", statusId, locale));



                String payFromPartyId = (String) rowMap.get("partyId");




                GenericValue orderPaymentPrefAndPayment = EntityQuery.use(delegator).from("OrderPaymentPrefAndPayment").where("orderId",gv.get("orderId")).queryFirst();

                GenericValue payment = EntityQuery.use(delegator).from("Payment").where("partyIdTo",payToPartyId,"partyIdFrom",payFromPartyId,"comments",rowMap.get("orderId")).queryFirst();
                GenericValue  isConfirm = EntityQuery.use(delegator).from("MessageLog").where("objectId",productId,"partyIdFrom",partyId,"partyIdTo",payFromPartyId,"badge", "CHECK","message", " 已收到您的货款! ").queryFirst();
                if(isConfirm!=null){
                    rowMap.put("isConfirmPay","TRUE");
                }else{
                    rowMap.put("isConfirmPay","FALSE");
                }
                orderList.add(rowMap);


            }
        }

        String relationStr = "";

        List<GenericValue> partyRelationship = EntityQuery.use(delegator).from("PartyRelationship").where("partyIdTo",realPartyId,"partyIdFrom",partyId).queryList();

        if(partyRelationship!=null && partyRelationship.size()>0){
            for(int index = 0 ; index < partyRelationship.size(); index++ ){
                 GenericValue gv = partyRelationship.get(index);
                 String relation = (String) gv.get("partyRelationshipTypeId");
                 relationStr += UtilProperties.getMessage(resourceUiLabels,relation, locale)+",";
            }
        }else{
            relationStr = "潜在客户";
        }

        resultMap.put("orderList", orderList);
        resultMap.put("partyRelation",relationStr);
        resultMap.put("queryConsumerInfoList",returnList);

        return resultMap;
    }











    /**
     * Query PostalAddress
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryPostalAddress(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();


        //Scope Param
        GenericValue userLogin = (GenericValue) context.get("userLogin");

        String partyId = (String) userLogin.get("partyId");


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

        resultMap.put("postalAddress",queryAddressList);

        return resultMap;
    }








    /**
     * Query ProductRole
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryProductRole(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();


        //Scope Param
        GenericValue userLogin = (GenericValue) context.get("userLogin");

        String partyId = (String) userLogin.get("partyId");

        String productId = (String) context.get("productId");

        GenericValue partyMarkRole = EntityQuery.use(delegator).from("ProductRole").where("partyId", partyId,"productId",productId, "roleTypeId", "PLACING_CUSTOMER").queryFirst();


        if(null != partyMarkRole){
            resultMap.put("mark","true");
        }

        return resultMap;
    }




    /**
     * Query LocalRoster
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryLocalRoster(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
        StringBuffer rosterBuffer = new StringBuffer();

        //Scope Param
        GenericValue userLogin = (GenericValue) context.get("userLogin");

        String partyId = (String) userLogin.get("partyId");


        // Default 2 Dimension
        rosterBuffer = queryDimensionRelationShip(partyId, delegator);


//        GZIP.compress()
        try {
            resultMap.put("roster",rosterBuffer.toString());
        }catch (Exception e){

        }
        return resultMap;
    }

    /**
     * Query PartyRelationShip
     * @param partyId
     * @param delegator
     * @return
     */
    private static StringBuffer queryDimensionRelationShip(String partyId, Delegator delegator ) throws GenericEntityException, GenericServiceException {

        StringBuffer rosterBuffer = new StringBuffer();

        Set<String> fieldSet = new HashSet<String>();

        fieldSet.add("partyIdTo");


        EntityCondition findConditions = EntityCondition
                .makeCondition(UtilMisc.toMap("partyIdFrom", partyId));


        EntityCondition findConditions2 = EntityCondition
                .makeCondition(UtilMisc.toMap("partyRelationshipTypeId", PeConstant.CONTACT));

        EntityConditionList<EntityCondition> listConditions = EntityCondition
                .makeCondition(findConditions, findConditions2);

        // 1D
        List<GenericValue> myRelationListList = delegator.findList("PartyRelationship",
                listConditions, fieldSet,
                null, null, false);


        Map<String,Object> dimensionMap = new HashMap<String, Object>();


        for(int i = 0 ; i  < myRelationListList.size();i++){
            GenericValue gv = myRelationListList.get(i);
            if(i+1==myRelationListList.size()){
                rosterBuffer.append((String)gv.get("partyIdTo"));
            }else{
                rosterBuffer.append((String)gv.get("partyIdTo")+",");
            }

            dimensionMap.put(gv.get("partyIdTo")+"","");
        }


        rosterBuffer.append("。");
        // 1D OVER

        rosterBuffer.append(forQueryDimensionRelationShip(myRelationListList, delegator,dimensionMap,partyId));
        // 2D 3D OVER

        return rosterBuffer;
    }

    /**
     * ForEach Dimension
     * @param myRelationListList
     * @param delegator
     * @return
     */
    private static String  forQueryDimensionRelationShip(List<GenericValue> relationList, Delegator delegator,Map<String,Object> dimensionMap,String userPartyId) throws GenericEntityException, GenericServiceException {

        Map<String,Object> twoDimensionMap = new HashMap<String, Object>();

        StringBuffer sb = new StringBuffer();

       for(int i =0; i < relationList.size();i++){
           GenericValue gv = relationList.get(i);
           String partyIdTo = (String) gv.get("partyIdTo");



           Set<String> fieldSet = new HashSet<String>();

           fieldSet.add("partyIdTo");


           EntityCondition findConditions = EntityCondition
                   .makeCondition(UtilMisc.toMap("partyIdFrom", partyIdTo));


           EntityCondition findConditions2 = EntityCondition
                   .makeCondition(UtilMisc.toMap("partyRelationshipTypeId", PeConstant.CONTACT));

           EntityConditionList<EntityCondition> listConditions = EntityCondition
                   .makeCondition(findConditions, findConditions2);

           // 1D
           List<GenericValue> myRelationListList = delegator.findList("PartyRelationship",
                   listConditions, fieldSet,
                   null, null, false);





           for(int y = 0 ; y< myRelationListList.size();y++){
               GenericValue gv2 = myRelationListList.get(y);
               String partyIdToTo = (String)gv2.get("partyIdTo");

                   if(i+1 == relationList.size() && y +1 == myRelationListList.size()){

                     if(!dimensionMap.containsKey(partyIdToTo) && !userPartyId.equals(partyIdToTo) && !twoDimensionMap.containsKey(partyIdToTo)) {
                         sb.append(partyIdToTo);
                         twoDimensionMap.put(partyIdToTo,"");
                      }


                   }else{
                       if(!dimensionMap.containsKey(partyIdToTo) && !userPartyId.equals(partyIdToTo)  && !twoDimensionMap.containsKey(partyIdToTo)) {
                           sb.append(partyIdToTo + ",");
                           twoDimensionMap.put(partyIdToTo,"");
                       }
                   }


           }
       }


        return sb.toString();
    }


    /**
     * Query More Resource
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryMoreResource(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        List<Map<String,Object>> returnList = new ArrayList<Map<String, Object>>();


        //Scope Param
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId     = (String) userLogin.get("partyId");
        String productId   = (String) context.get("productId");
        String productName = (String) context.get("productName");
        String roster      = (String) context.get("roster");

//        if(UtilValidate.isNotEmpty(roster)){
//            try {
//                roster = GZIP.unCompress(roster);
//            }catch (Exception e) {
//               //TODO Add Catch
//            }
//        }

        System.out.println("roster = " + roster);

        String [] rosterArray = roster.split("。");

        List<String> rosters = doSplitRoster(rosterArray);




        Set<String> fieldSet = new HashSet<String>();
        fieldSet.add("productId");
        fieldSet.add("productStoreId");
        fieldSet.add("productName");
        fieldSet.add("detailImageUrl");
        fieldSet.add("createdDate");
        fieldSet.add("price");
        fieldSet.add("productCategoryId");
        fieldSet.add("payToPartyId");


        EntityCondition findConditions = EntityCondition
                .makeCondition("productName",EntityOperator.LIKE,productName);

        EntityCondition findConditions2 = EntityCondition
                .makeCondition("payToPartyId",EntityOperator.IN,rosters);

        EntityCondition findConditions3 = EntityCondition
                .makeCondition("salesDiscontinuationDate", EntityOperator.EQUALS, GenericEntity.NULL_FIELD);

        EntityConditionList<EntityCondition> listConditions = EntityCondition
                .makeCondition(findConditions, findConditions2,findConditions3);


        if(UtilValidate.isNotEmpty(productId)){
            findConditions = EntityCondition
                    .makeCondition(UtilMisc.toMap("productId",productId));
        }





        //Query My Resource
        List<GenericValue> queryResourceList = delegator.findList("ProductAndCategoryMember",
                listConditions, fieldSet,
                UtilMisc.toList("-createdDate"), null, false);





        if(null != queryResourceList && queryResourceList.size()>0){
            for(GenericValue gv : queryResourceList){
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




        resultMap.put("resourceList", returnList);

        return resultMap;
    }

    /**
     * 循环分割名单
     * @param rosterArray
     * @return
     */
    private static List<String> doSplitRoster(String[] rosterArray) {
        List<String> returnList = new ArrayList<String>();

        String [] oneArray = rosterArray[0].split(",");


        for(int i = 0 ; i < oneArray.length; i ++){
                String partyId = oneArray[i];
                returnList.add(partyId);
        }
        if(rosterArray.length > 1 && rosterArray[1].indexOf(",")>0){
            String [] twoArray = rosterArray[1].split(",");
            for(int i = 0 ; i < twoArray.length; i ++){
                String partyId = twoArray[i];
                returnList.add(partyId);
            }
        }


        return returnList;
    }


         /**
         * Ajax Query Resource
         * @param request
         * @param response
         * @return
         * @throws GenericServiceException
         * @throws GenericEntityException
         */
    public static String ajaxQueryResource(HttpServletRequest request, HttpServletResponse response)
            throws GenericServiceException, GenericEntityException {

        // Servlet Head


        Delegator delegator = (Delegator) request.getAttribute("delegator");

        String productName = (String) request.getParameter("productName");


        Set<String> fieldSet = new HashSet<String>();
        fieldSet.add("productId");
        fieldSet.add("productName");


        EntityCondition findConditions = EntityCondition
                .makeCondition("productName",EntityOperator.LIKE,"%"+productName+"%");


        //Query My Resource
        List<GenericValue> queryResourceList = delegator.findList("Product",
                findConditions, fieldSet,
                UtilMisc.toList("-createdStamp"), null, true);


        request.setAttribute("queryResourceList",queryResourceList);

        return "success";
    }




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
     * Query MyOrders Detail
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryMyOrdersDetail(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();

        Delegator delegator = dispatcher.getDelegator();

        Locale locale = (Locale) context.get("locale");

        Map<String, Object> resultMap = ServiceUtil.returnSuccess();

        //Scope Param
        GenericValue userLogin = (GenericValue) context.get("userLogin");

        String partyId = (String) userLogin.get("partyId");

        String orderId = (String) context.get("orderId");

        //Order BaseInfo
        Map<String,Object> rowMap = new HashMap<String, Object>();
        //Express Info
        Map<String,Object> orderExpressInfo = new HashMap<String, Object>();

        GenericValue  orderHeaderItemAndRoles  = EntityQuery.use(delegator).from("OrderHeaderItemAndRoles").where("orderId", orderId).queryFirst();

        if(null != orderHeaderItemAndRoles){

            rowMap = orderHeaderItemAndRoles.getAllFields();

            GenericValue orderPaymentPrefAndPayment = EntityQuery.use(delegator).from("OrderPaymentPrefAndPayment").where("orderId",orderId).queryFirst();

            if(null != orderPaymentPrefAndPayment){

                String statusId = (String) orderPaymentPrefAndPayment.get("statusId");

                if(statusId.toUpperCase().indexOf("RECEIVED")>0){

                    rowMap.put("orderPayStatus","已付款");

                }else{

                    rowMap.put("orderPayStatus","未付款");

                }
            }else{

                rowMap.put("orderPayStatus","未付款");

            }

            String productStoreId = (String) orderHeaderItemAndRoles.get("productStoreId");

            String productId = (String) orderHeaderItemAndRoles.get("productId");

            GenericValue productStore = delegator.findOne("ProductStore",UtilMisc.toMap("productStoreId",productStoreId),false);

            GenericValue product = delegator.findOne("Product",UtilMisc.toMap("productId",productId),false);

            rowMap.put("productName",""+product.get("productName"));

            rowMap.put("detailImageUrl",(String)product.get("detailImageUrl"));

            String payToPartyId = (String)productStore.get("payToPartyId");

            rowMap.put("payToPartyId",payToPartyId);


            Map<String,String> personInfoMap = null;

            Map<String,String> personAddressInfoMap = null;


            personInfoMap =  queryPersonBaseInfo(delegator,payToPartyId);

            personAddressInfoMap = queryPersonAddressInfo(delegator,partyId);

            rowMap.put("personInfoMap",personInfoMap);

            rowMap.put("personAddressInfoMap",personAddressInfoMap);

        }

        GenericValue order =  delegator.findOne("OrderHeader", UtilMisc.toMap("orderId", orderId),false);

        //TODO QUERY orderExpressInfo
        Map<String,Object> queryExpressInfoMap = dispatcher.runSync("queryExpressInfo",UtilMisc.toMap("userLogin",userLogin,"code",order.get("internalCode")));
        List<JSONObject> expressInfos = null;
        if (ServiceUtil.isSuccess(queryExpressInfoMap)) {
          expressInfos = (List<JSONObject>) queryExpressInfoMap.get("expressInfos");
        }


        resultMap.put("orderInfo",rowMap);
        resultMap.put("orderExpressInfo",expressInfos);
        resultMap.put("orderExpressName",queryExpressInfoMap.get("name"));


        return resultMap;
    }


    /**
     * Query MyOrders
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryMyOrders(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();


        //Scope Param
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = (String) userLogin.get("partyId");
        List<Map<String,Object>> orderList = new ArrayList<Map<String, Object>>();




        Set<String> fieldSet = new HashSet<String>();
        fieldSet.add("orderId");
        fieldSet.add("partyId");
        fieldSet.add("statusId");
        fieldSet.add("currencyUom");
        fieldSet.add("grandTotal");
        fieldSet.add("productId");
        fieldSet.add("quantity");
        fieldSet.add("unitPrice");
        fieldSet.add("roleTypeId");
        fieldSet.add("orderDate");
        fieldSet.add("productStoreId");
        fieldSet.add("payToPartyId");

        EntityCondition findConditions3 = EntityCondition
                .makeCondition("roleTypeId",EntityOperator.EQUALS,"BILL_TO_CUSTOMER");

        EntityCondition findConditions = EntityCondition
                .makeCondition("partyId" ,EntityOperator.EQUALS,partyId);

        EntityCondition listConditions2 = EntityCondition
                .makeCondition(findConditions3,EntityOperator.AND,findConditions);


        List<GenericValue> queryMyResourceOrderList = delegator.findList("OrderHeaderItemAndRoles",
                listConditions2, fieldSet,
                UtilMisc.toList("-orderDate"), null, false);


        if(null != queryMyResourceOrderList && queryMyResourceOrderList.size()>0){

            for(GenericValue gv : queryMyResourceOrderList){

                Map<String,Object> rowMap = new HashMap<String, Object>();

                rowMap = gv.getAllFields();



                String productStoreId = (String) gv.get("productStoreId");

                String productId = (String) gv.get("productId");

                GenericValue productStore = delegator.findOne("ProductStore",UtilMisc.toMap("productStoreId",productStoreId),false);

                GenericValue product = delegator.findOne("Product",UtilMisc.toMap("productId",productId),false);

                rowMap.put("productName",""+product.get("productName"));

                rowMap.put("detailImageUrl",(String)product.get("detailImageUrl"));

                String payToPartyId = (String)productStore.get("payToPartyId");

                rowMap.put("payToPartyId",payToPartyId);

                String statusId = (String) gv.get("statusId");

                //区分订单状态
                if(statusId.toLowerCase().indexOf("comp")>0){
                    rowMap.put("orderStatusCode","1");
                }else{
                    rowMap.put("orderStatusCode","0");
                }
                System.out.println("orderStatusCode = " + rowMap.get("orderStatusCode"));

                rowMap.put("statusId",UtilProperties.getMessage("PersonManagerUiLabels.xml", statusId, locale));



                String payFromPartyId = (String) rowMap.get("partyId");

                Map<String,String> personInfoMap = null;

                Map<String,String> personAddressInfoMap = null;

                //说明这笔订单我是卖家,查买家头像信息
                if(payToPartyId.equals(partyId)){

                    personInfoMap =  queryPersonBaseInfo(delegator,payFromPartyId);

                    personAddressInfoMap = queryPersonAddressInfo(delegator,payFromPartyId);

                    rowMap.put("realPartyId",payFromPartyId);

                }
                //说明这笔单我是买家,查卖家头像信息
                if(!payToPartyId.equals(partyId)){

                    personInfoMap = queryPersonBaseInfo(delegator,payToPartyId);

                    personAddressInfoMap = queryPersonAddressInfo(delegator,payToPartyId);

                    rowMap.put("realPartyId",payToPartyId);
                }

                rowMap.put("userPartyId",partyId);

                rowMap.put("personInfoMap",personInfoMap);

                rowMap.put("personAddressInfoMap",personAddressInfoMap);


                GenericValue orderPaymentPrefAndPayment = EntityQuery.use(delegator).from("OrderPaymentPrefAndPayment").where("orderId",gv.get("orderId")).queryFirst();

                GenericValue payment = EntityQuery.use(delegator).from("Payment").where("partyIdTo",payToPartyId,"partyIdFrom",payFromPartyId,"comments",rowMap.get("orderId")).queryFirst();

                if(null != orderPaymentPrefAndPayment){

                    String orderPaymentPrefAndPaymentstatusId = (String) orderPaymentPrefAndPayment.get("statusId");

                    if(orderPaymentPrefAndPaymentstatusId.toUpperCase().indexOf("RECEIVED")>0){

                        rowMap.put("orderPayStatus","已确认收款");
                        rowMap.put("payStatusCode","1");
                    }else{
                        rowMap.put("payStatusCode","0");
                        rowMap.put("orderPayStatus","买家已付款");
                    }
                }else{
                    rowMap.put("payStatusCode","0");
                    rowMap.put("orderPayStatus","未付款");
                    if(null!=payment){
                        String paymentStatusId = (String) payment.get("statusId");
                        if(paymentStatusId.toUpperCase().indexOf("RECEIVED")>0){
                            rowMap.put("orderPayStatus","已确认收款");
                            rowMap.put("payStatusCode","1");
                        }
                        if(paymentStatusId.toUpperCase().indexOf("NOT_PAID")>0){
                            rowMap.put("orderPayStatus","买家已付款");
                            rowMap.put("payStatusCode","1");
                        }

                    }else{
                        rowMap.put("payStatusCode","0");
                        rowMap.put("orderPayStatus","未付款");
                    }

                }



                orderList.add(rowMap);
            }
        }


        resultMap.put("orderList", orderList);

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
        fieldSet.add("roleTypeId");
        fieldSet.add("orderDate");
        fieldSet.add("productStoreId");
        fieldSet.add("payToPartyId");



        EntityCondition findConditions3 = EntityCondition
                .makeCondition(UtilMisc.toMap("roleTypeId", "BILL_TO_CUSTOMER"));

        EntityCondition findConditions = EntityCondition
                .makeCondition(UtilMisc.toMap("partyId", partyId));


                EntityCondition findConditions2 = EntityCondition
                .makeCondition(UtilMisc.toMap("payToPartyId",partyId));

        EntityCondition listConditions = EntityCondition
                .makeCondition(findConditions,EntityOperator.OR,findConditions2);

        EntityCondition listConditions2 = EntityCondition
                .makeCondition(findConditions3,EntityOperator.AND,listConditions);


        List<GenericValue> queryMyResourceOrderList = delegator.findList("OrderHeaderItemAndRoles",
                listConditions2, fieldSet,
                UtilMisc.toList("-orderDate"), null, false);

        if(null != queryMyResourceOrderList && queryMyResourceOrderList.size()>0){

            for(GenericValue gv : queryMyResourceOrderList){
                Map<String,Object> rowMap = new HashMap<String, Object>();
                rowMap = gv.getAllFields();






                String productStoreId = (String) gv.get("productStoreId");
                String productId = (String) gv.get("productId");

                GenericValue productStore = delegator.findOne("ProductStore",UtilMisc.toMap("productStoreId",productStoreId),false);
                GenericValue product = delegator.findOne("Product",UtilMisc.toMap("productId",productId),false);
                rowMap.put("productName",""+product.get("productName"));
                rowMap.put("detailImageUrl",(String)product.get("detailImageUrl"));
                String payToPartyId = (String)productStore.get("payToPartyId");
                rowMap.put("payToPartyId",payToPartyId);
                String statusId = (String) gv.get("statusId");
                rowMap.put("statusId",UtilProperties.getMessage("PersonManagerUiLabels.xml", statusId, locale));
                String payFromPartyId = (String) rowMap.get("partyId");

                Map<String,String> personInfoMap = null;
                Map<String,String> personAddressInfoMap = null;

                //说明这笔订单我是卖家,查买家头像信息
                if(payToPartyId.equals(partyId)){
                    personInfoMap =  queryPersonBaseInfo(delegator,payFromPartyId);
                    personAddressInfoMap = queryPersonAddressInfo(delegator,payFromPartyId);
                    rowMap.put("realPartyId",payFromPartyId);
                }
                //说明这笔单我是买家,查卖家头像信息
                if(!payToPartyId.equals(partyId)){
                    personInfoMap = queryPersonBaseInfo(delegator,payToPartyId);
                    personAddressInfoMap = queryPersonAddressInfo(delegator,payToPartyId);
                    rowMap.put("realPartyId",payToPartyId);
                }
                rowMap.put("userPartyId",partyId);

                rowMap.put("personInfoMap",personInfoMap);
                rowMap.put("personAddressInfoMap",personAddressInfoMap);




                GenericValue orderPaymentPrefAndPayment = EntityQuery.use(delegator).from("OrderPaymentPrefAndPayment").where("orderId",gv.get("orderId")).queryFirst();

                GenericValue payment = EntityQuery.use(delegator).from("Payment").where("partyIdTo",payToPartyId,"partyIdFrom",payFromPartyId,"comments",gv.get("orderId")).queryFirst();

                if(null != orderPaymentPrefAndPayment){

                    String orderPaymentPrefAndPaymentstatusId = (String) orderPaymentPrefAndPayment.get("statusId");

                    if(orderPaymentPrefAndPaymentstatusId.toUpperCase().indexOf("RECEIVED")>0){

                        rowMap.put("orderPayStatus","已确认收款");
                        rowMap.put("payStatusCode","1");
                    }else{
                        rowMap.put("payStatusCode","0");
                        rowMap.put("orderPayStatus","买家已付款");
                    }
                }else{
                    rowMap.put("payStatusCode","0");
                    rowMap.put("orderPayStatus","未付款");
                    if(null!=payment){
                        String paymentStatusId = (String) payment.get("statusId");
                        if(paymentStatusId.toUpperCase().indexOf("RECEIVED")>0){
                            rowMap.put("orderPayStatus","已确认收款");
                            rowMap.put("payStatusCode","1");
                        }
                        if(paymentStatusId.toUpperCase().indexOf("NOT_PAID")>0){
                            rowMap.put("orderPayStatus","买家已付款");
                            rowMap.put("payStatusCode","1");
                        }

                    }else{
                        rowMap.put("payStatusCode","0");
                        rowMap.put("orderPayStatus","未付款");
                    }

                }

                myResourceOrderList.add(rowMap);
            }
        }


        resultMap.put("queryMyResourceOrderList", myResourceOrderList);

        return resultMap;
    }


    /**
     * 查询卖家地址
     * @param delegator
     * @param payFromPartyId
     * @return
     */
    public static Map<String, String> queryPersonAddressInfo(Delegator delegator, String partyId)throws GenericEntityException {

        Map<String, String> personMap = new HashMap<String, String>();

        GenericValue postalAddress = EntityUtil.getFirst(
                EntityQuery.use(delegator).from("PartyAndPostalAddress").where(UtilMisc.toMap("partyId", partyId,"contactMechTypeId", "POSTAL_ADDRESS")).queryList());
        if (UtilValidate.isNotEmpty(postalAddress))
            personMap.put("contactAddress", "" + postalAddress.get("address1"));

        return personMap;
    }


    /**
     * QueryPersonBaseInfo
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryPersonBaseInfo(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();


        System.out.println(" payToPartyFirstName = "+(String) context.get("payToPartyFirstName"));

        resultMap.put("tarjeta", (String) context.get("tarjeta"));
        resultMap.put("productId",(String) context.get("productId"));
        resultMap.put("payToPartyId",(String) context.get("payToPartyId"));
        resultMap.put("payToPartyFirstName",(String) context.get("payToPartyFirstName"));
        resultMap.put("payToPartyHead",(String) context.get("payToPartyHead"));


        //Scope Param
        GenericValue userLogin = (GenericValue) context.get("userLogin");

        String partyId = (String) userLogin.get("partyId");

        resultMap.put("partyId",partyId);

        Map<String, String> personInfo = new HashMap<String, String>();

        personInfo.put("partyId",partyId);

        GenericValue person = delegator.findOne("Person", UtilMisc.toMap("partyId", partyId), false);

        if (person != null) {

            List<GenericValue> contentsList =
                    EntityQuery.use(delegator).from("PartyContentAndDataResource").
                            where("partyId", partyId, "partyContentTypeId", "LGOIMGURL").orderBy("-fromDate").queryPagedList(0, 999999).getData();


            GenericValue partyContent = null;
            if (null != contentsList && contentsList.size() > 0) {
                partyContent = contentsList.get(0);
            }

            if (UtilValidate.isNotEmpty(partyContent)) {
                String contentId = partyContent.getString("contentId");
                personInfo.put("headPortrait",
                        partyContent.getString("objectInfo"));
            } else {
                personInfo.put("headPortrait",
                        "https://personerp.oss-cn-hangzhou.aliyuncs.com/datas/images/defaultHead.png");
            }
            personInfo.put("firstName", (String) person.get("firstName"));


        }

        resultMap.put("personInfo",personInfo);

        return resultMap;
    }


    /**
     * QueryPersonBaseInfo-WeChat
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryPersonBaseInfoByChat(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        resultMap.put("tarjeta", (String) context.get("tarjeta"));
        resultMap.put("productId",(String) context.get("productId"));
        resultMap.put("payToPartyId",(String) context.get("payToPartyId"));
        resultMap.put("payToPartyFirstName",(String) context.get("payToPartyFirstName"));
        resultMap.put("payToPartyHead",(String) context.get("payToPartyHead"));


        //Scope Param
        GenericValue userLogin = (GenericValue) context.get("userLogin");

        String partyId = (String) userLogin.get("partyId");

        resultMap.put("partyId",partyId);

        Map<String, String> personInfo = new HashMap<String, String>();

        personInfo.put("partyId",partyId);

        GenericValue person = delegator.findOne("Person", UtilMisc.toMap("partyId", partyId), false);

        if (person != null) {

            List<GenericValue> contentsList =
                    EntityQuery.use(delegator).from("PartyContentAndDataResource").
                            where("partyId", partyId, "partyContentTypeId", "LGOIMGURL").orderBy("-fromDate").queryPagedList(0, 999999).getData();


            GenericValue partyContent = null;
            if (null != contentsList && contentsList.size() > 0) {
                partyContent = contentsList.get(0);
            }

            if (UtilValidate.isNotEmpty(partyContent)) {
                String contentId = partyContent.getString("contentId");
                personInfo.put("headPortrait",
                        partyContent.getString("objectInfo"));
            } else {
                personInfo.put("headPortrait",
                        "https://personerp.oss-cn-hangzhou.aliyuncs.com/datas/images/defaultHead.png");
            }
            personInfo.put("firstName", (String) person.get("firstName"));


        }

        resultMap.put("personInfo",personInfo);

        //自己就是卖家的情况下无需再增加角色关系
        if(partyId.equals(context.get("payToPartyId")+"")){
            return resultMap;
        }

        //是否已经是产品的意向客户
        GenericValue productRole = EntityQuery.use(delegator).from("ProductRole").where("productId",context.get("productId"),"partyId",partyId, "roleTypeId", "PLACING_CUSTOMER").queryFirst();
        //如果还不是,就给一个意向客户到产品
        if (!UtilValidate.isNotEmpty(productRole)) {
            dispatcher.runSync("addPartyToProduct", UtilMisc.toMap("userLogin", admin, "partyId", partyId, "productId", context.get("productId"), "roleTypeId", "PLACING_CUSTOMER"));
        }


//已注释掉,将客户做成店铺客户的角色逻辑
        //找店铺Id
//        GenericValue productStoreRole = EntityQuery.use(delegator).from("ProductStoreRole").where("partyId",context.get("payToPartyId") , "roleTypeId", "ADMIN").queryFirst();
//        String productStoreId = (String) productStoreRole.get("productStoreId");
//        GenericValue custStoreRole = EntityQuery.use(delegator).from("ProductStoreRole").where("partyId",partyId,"productStoreId",productStoreId).queryFirst();
//        if (!UtilValidate.isNotEmpty(custStoreRole)) {
//            //什么角色都没的时候,默认给一个潜在客户角色
//            Map<String, Object> createProductStoreRoleOutMap =   dispatcher.runSync("createProductStoreRole", UtilMisc.toMap("userLogin", userLogin, "partyId", partyId, "productStoreId", productStoreId, "roleTypeId", PeConstant.DEFAULT_PRODUCT_STORE_CUST_ROLE));
//            if (!ServiceUtil.isSuccess(createProductStoreRoleOutMap)) {
//                return createProductStoreRoleOutMap;
//            }
//        }

        return resultMap;
    }


    /**
     * 查头像和名称
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String,String> queryPersonBaseInfo(Delegator delegator,String partyId)throws GenericEntityException, GenericServiceException {

        Map<String, String> personInfo = new HashMap<String, String>();

        GenericValue person = delegator.findOne("Person", UtilMisc.toMap("partyId", partyId), false);

        if (person != null) {

            List<GenericValue> contentsList =
                    EntityQuery.use(delegator).from("PartyContentAndDataResource").
                            where("partyId", partyId, "partyContentTypeId", "LGOIMGURL").orderBy("-fromDate").queryPagedList(0, 999999).getData();


            GenericValue partyContent = null;
            if (null != contentsList && contentsList.size() > 0) {
                partyContent = contentsList.get(0);
            }

            if (UtilValidate.isNotEmpty(partyContent)) {
                String contentId = partyContent.getString("contentId");
                personInfo.put("headPortrait",
                        partyContent.getString("objectInfo"));
            } else {
                personInfo.put("headPortrait",
                        "https://personerp.oss-cn-hangzhou.aliyuncs.com/datas/images/defaultHead.png");
            }
            personInfo.put("firstName", (String) person.get("firstName"));


        }
        return personInfo;
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

//        String userLoginPartyId = (String) userLogin.get("partyId");
        if(null != userLogin){
        System.out.println("*queryResourceDetail user login = " + userLogin);
        }
        String productId = (String) context.get("productId");

        Set<String> fieldSet = new HashSet<String>();
        fieldSet.add("productId");
        fieldSet.add("productName");
        fieldSet.add("productStoreId");
        fieldSet.add("createdDate");
        fieldSet.add("salesDiscontinuationDate");
        fieldSet.add("price");
        fieldSet.add("detailImageUrl");
        fieldSet.add("prodCatalogId");
        fieldSet.add("payToPartyId");
        fieldSet.add("description");

        EntityCondition findConditions = EntityCondition
                .makeCondition(UtilMisc.toMap("productId", productId));

        GenericValue product = delegator.findList("ProductAndCategoryMember",
                findConditions, fieldSet,
                null, null, false).get(0);
        Map<String, Object> resourceDetail = product.getAllFields();
        GenericValue person = delegator.findOne("Person", UtilMisc.toMap("partyId", resourceDetail.get("payToPartyId")), false);
        if(person!=null){


            List<GenericValue> contentsList =
                    EntityQuery.use(delegator).from("PartyContentAndDataResource").
                            where("partyId", resourceDetail.get("payToPartyId"), "partyContentTypeId", "LGOIMGURL").orderBy("-fromDate").queryPagedList(0,999999).getData();


            GenericValue partyContent = null;
            if(null != contentsList && contentsList.size()>0){
                partyContent = contentsList.get(0);
            }

            if (UtilValidate.isNotEmpty(partyContent)) {

                String contentId = partyContent.getString("contentId");
                resourceDetail.put("headPortrait",
                        partyContent.getString("objectInfo"));
            } else {
                resourceDetail.put("headPortrait",
                        "https://personerp.oss-cn-hangzhou.aliyuncs.com/datas/images/defaultHead.png");
            }
            resourceDetail.put("firstName",(String) person.get("firstName"));

            //PartyNoteView
            GenericValue partyNoteView = EntityQuery.use(delegator).from("PartyNoteView").where("targetPartyId", resourceDetail.get("payToPartyId")).queryFirst();
            if(UtilValidate.isNotEmpty(partyNoteView)){
                resourceDetail.put("partyNote",partyNoteView.get("noteInfo"));
            }else{
                resourceDetail.put("partyNote","这位卖家还未设置个人说明...");
            }

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
            resourceDetail.put("orderId",queryMyResourceOrderList.get(0).get("orderId"));
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


        // Query Product More Images & Text
        List<Map<String,Object>> productMoreDetails = new ArrayList<Map<String, Object>>();
        resourceDetail.put("productMoreDetails",productMoreDetails);




        fieldSet = new HashSet<String>();
        fieldSet.add("drObjectInfo");
        fieldSet.add("productId");
        EntityCondition findConditions3 = EntityCondition
                .makeCondition("productId", EntityOperator.EQUALS, productId);
        List<GenericValue> pictures =  delegator.findList("ProductContentAndInfo",
                findConditions3, fieldSet,
                null, null, false);


        Long custCount = EntityQuery.use(delegator).from("ProductRole").where("productId",productId,"roleTypeId",PeConstant.PRODUCT_CUSTOMER).queryCount();
        Long placingCount = EntityQuery.use(delegator).from("ProductRole").where("productId",productId,"roleTypeId","PLACING_CUSTOMER").queryCount();

        resourceDetail.put("custCount",custCount);
        resourceDetail.put("placingCount",placingCount);

        resourceDetail.put("morePicture",pictures);
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
                    .makeCondition(findConditions, findConditions2, findConditions3,findConditions4);


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
                EntityQuery.use(delegator).from("PostalAddressAndPartyView").where(UtilMisc.toMap("partyId", partyId, "contactMechPurposeTypeId", "SHIPPING_LOCATION", "contactMechTypeId", "POSTAL_ADDRESS")).queryList());
        if (UtilValidate.isNotEmpty(postalAddress))
            inputMap.put("contactAddress", "" + postalAddress.get("geoName") + " " + postalAddress.get("city")  + " " + postalAddress.get("address1"));
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


        List<GenericValue> partyIdentificationList = EntityQuery.use(delegator).from("PartyIdentification").where("partyId", partyId, "partyIdentificationTypeId", "WX_UNIO_ID").queryList();


        if (null != partyIdentificationList && partyIdentificationList.size() > 0) {
            String openId = (String) partyIdentificationList.get(0).get("idValue");

            inputMap.put("openId", partyIdentificationList.get(0).get("idValue"));
        } else {
            inputMap.put("openId", "NA");
        }

//        List<GenericValue> paymentMethodList = EntityQuery.use(delegator).from("PaymentMethod").where("partyId", partyId, "paymentMethodTypeId", "MEDIATION_PAY").queryList();
//
//        if(null != paymentMethodList && paymentMethodList.size()>0){
//            inputMap.put("paymentMethodList",paymentMethodList);
//        }else{
//            inputMap.put("paymentMethodList","NA");
//        }


        //Query Qr Code

        List<GenericValue> aliPayQrCodes =
                EntityQuery.use(delegator).from("PartyContentAndDataResource").
                        where("partyId", partyId, "partyContentTypeId", "ALIQRCODE").orderBy("-fromDate").queryPagedList(0,999999).getData();


        GenericValue aliPayQrCode = null;

        if(null != aliPayQrCodes && aliPayQrCodes.size()>0){
            aliPayQrCode = aliPayQrCodes.get(0);
            inputMap.put("aliPayQrCode",aliPayQrCode.getString("objectInfo"));
        }

        List<GenericValue> weChatQrCodes =
                EntityQuery.use(delegator).from("PartyContentAndDataResource").
                        where("partyId", partyId, "partyContentTypeId", "WECHATQRCODE").orderBy("-fromDate").queryPagedList(0,999999).getData();


        GenericValue weChatQrCode = null;

        if(null != weChatQrCodes && weChatQrCodes.size()>0){
            weChatQrCode = weChatQrCodes.get(0);
            inputMap.put("weChatQrCode",weChatQrCode.getString("objectInfo"));
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

        List<Map<String,Object>> resourceMapList = new ArrayList<Map<String, Object>>();

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
            EntityCondition findConditions2 = EntityCondition
                    .makeCondition("salesDiscontinuationDate", EntityOperator.EQUALS, GenericEntity.NULL_FIELD);



            EntityConditionList<EntityCondition> listConditions = EntityCondition
                    .makeCondition(findConditions, findConditions2);

            //Query My Resource
            myResourceList = delegator.findList("ProductAndCategoryMember",
                    listConditions, fieldSet,
                    UtilMisc.toList("-createdDate"), findOptions, false);

            if(null != myResourceList && myResourceList.size()>0){
                for(GenericValue gv :myResourceList){
                    Map<String,Object> rowMap = new HashMap<String, Object>();

                    rowMap.put("productName",(String)gv.get("productName"));
                    rowMap.put("productId",(String)gv.get("productId"));
                    rowMap.put("productStoreId",(String)gv.get("productStoreId"));
                    rowMap.put("detailImageUrl",(String)gv.get("detailImageUrl"));
                    rowMap.put("createdDate",gv.get("createdDate"));
                    rowMap.put("price",gv.get("price"));
                    rowMap.put("productCategoryId",(String)gv.get("productCategoryId"));
                    rowMap.put("payToPartyId",(String)gv.get("payToPartyId"));
                    rowMap.put("description",(String)gv.get("description"));


                    fieldSet = new HashSet<String>();
                    fieldSet.add("drObjectInfo");
                    fieldSet.add("productId");
                    EntityCondition findConditions3 = EntityCondition
                            .makeCondition("productId", EntityOperator.EQUALS,(String)gv.get("productId") );
                    List<GenericValue> pictures =  delegator.findList("ProductContentAndInfo",
                            findConditions3, fieldSet,
                            null, null, false);
                    rowMap.put("morePicture",pictures);

                    resourceMapList.add(rowMap);
                }
            }


          //  ProductContentAndInfo
            //EntityQuery.use(delegator).from("ProductAndCategoryMember").where("productCategoryId",productCategoryId).queryList();
        }


        resultMap.put("myResourceList", resourceMapList);
        resultMap.put("productCategoryId", productCategoryId);

        return resultMap;
    }


}
