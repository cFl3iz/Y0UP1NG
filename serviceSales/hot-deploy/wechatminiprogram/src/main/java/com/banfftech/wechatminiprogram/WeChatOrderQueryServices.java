package main.java.com.banfftech.wechatminiprogram;
import net.sf.json.JSONObject;
import main.java.com.banfftech.personmanager.PersonManagerQueryServices;
import main.java.com.banfftech.platformmanager.constant.PeConstant;
import org.apache.ofbiz.entity.GenericEntity;
import org.apache.ofbiz.base.util.collections.PagedList;
import org.apache.ofbiz.base.util.Debug;
import java.sql.Timestamp;
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
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.util.EntityFindOptions;
import org.apache.ofbiz.entity.condition.EntityConditionList;
import org.apache.ofbiz.entity.condition.EntityOperator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.geom.GeneralPath;
import java.util.*;

import static main.java.com.banfftech.personmanager.PersonManagerQueryServices.queryPersonAddressInfo;
import static main.java.com.banfftech.personmanager.PersonManagerQueryServices.queryPersonBaseInfo;
import static main.java.com.banfftech.platformmanager.util.UtilTools.dateToStr;

/**
 * Created by S on 2017/11/20.
 */
public class WeChatOrderQueryServices {


    public final static String module = WeChatOrderQueryServices.class.getName();


    /**
     * 查询好友的资源列表
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
        if(viewIndexStr!=null){
            viewIndex = Integer.parseInt(viewIndexStr);
        }

        int viewSize = 3;
        int lowIndex = 0;
        int highIndex = 0;
        int resourceCount  = 0;

        GenericValue partyIdentification = EntityQuery.use(delegator).from("PartyIdentification").where("idValue", openId, "partyIdentificationTypeId", "WX_UNIO_ID").queryFirst();
        String partyId = "NA";

        if (UtilValidate.isNotEmpty(partyIdentification)) {
            partyId = (String) partyIdentification.get("partyId");
        }


        //查询联系人列表
        List<String> orderBy = UtilMisc.toList("-createdDate");
        PagedList<GenericValue> myContactListPage = null;
        myContactListPage = EntityQuery.use(delegator).from("PartyContactResources").
                where("partyIdTo", partyId, "partyRelationshipTypeId", PeConstant.CONTACT, "roleTypeId", "ADMIN","salesDiscontinuationDate","").orderBy(orderBy)
                .distinct()
                .queryPagedList(viewIndex, viewSize);

        List<GenericValue> myContactList = myContactListPage.getData();



        List<GenericValue> myContactListCountList   = EntityQuery.use(delegator).from("PartyContactResources").
                where("partyIdTo", partyId, "partyRelationshipTypeId", PeConstant.CONTACT, "roleTypeId", "ADMIN","salesDiscontinuationDate","")
                .orderBy(orderBy)
                .distinct()
                .queryList();
        resourceCount = myContactListCountList.size();

        lowIndex = myContactListPage.getStartIndex();
        highIndex = myContactListPage.getEndIndex();

        if(null != myContactList){


            for(GenericValue gv : myContactList){

                Map<String,Object> rowMap = new HashMap<String, Object>();

                String contactPartyId = (String) gv.get("partyIdFrom");

                if(partyId.equals(contactPartyId)){
                    continue;
                }

                Map<String,String> userInfoMap =  queryPersonBaseInfo(delegator,contactPartyId);

                Timestamp createdDateTp = (Timestamp) gv.get("createdDate");

                rowMap.put("created",dateToStr(createdDateTp,"yyyy-MM-dd HH:mm:ss"));

                rowMap.put("partyId",partyId);

                rowMap.put("user",userInfoMap);

                rowMap.put("contactPartyId",contactPartyId);

                String productId = (String) gv.get("productId");

                GenericValue productAddress = EntityQuery.use(delegator).from("ProductAttribute").where("attrName","address","productId", productId).queryFirst();
                if(null!=productAddress){
                    rowMap.put("address", productAddress.get("attrValue"));
                }

                GenericValue productlongitude = EntityQuery.use(delegator).from("ProductAttribute").where("attrName","longitude","productId", productId).queryFirst();
                if(null!=productlongitude) {
                    rowMap.put("longitude", productlongitude.get("attrValue"));
                }
                GenericValue productlatitude = EntityQuery.use(delegator).from("ProductAttribute").where("attrName","latitude","productId", productId).queryFirst();
                if(null!=productlatitude) {
                    rowMap.put("latitude", productlatitude.get("attrValue"));
                }

                rowMap.put("productId",productId);

                rowMap.put("description",(String) gv.get("description"));

                rowMap.put("productName",(String) gv.get("productName"));

                rowMap.put("detailImageUrl",(String) gv.get("detailImageUrl"));

                rowMap.put("price",gv.get("price") + "");
                HashSet<String> fieldSet = new HashSet<String>();
                fieldSet.add("drObjectInfo");
                fieldSet.add("productId");
                EntityCondition findConditions3 = EntityCondition
                        .makeCondition("productId", EntityOperator.EQUALS,(String)gv.get("productId") );

                List<GenericValue> pictures =  delegator.findList("ProductContentAndInfo",
                        findConditions3, fieldSet,
                        null, null, false);
                rowMap.put("morePicture",pictures);
                returnList.add(rowMap);

            }


        }
        resultMap.put("resourcesList",returnList);

        //总共有多少页码
        int countIndex = (resourceCount%viewSize);
        //viewIndex 当前页码


        if(resourceCount!=0 && resourceCount>viewSize){
            resultMap.put("total",resourceCount%viewSize == 0 ? resourceCount / viewSize : resourceCount / viewSize+1 );
        }else{
            if(null == myContactListCountList || myContactListCountList.size() == 0){
                resultMap.put("total",-1);
            }else{
                resultMap.put("total",1);
            }

        }

        resultMap.put("from",viewIndex);



        resultMap.put("current_page",viewIndex+1);
        resultMap.put("last_page",resourceCount);

        return  resultMap;
    }


    /**
     * 查询绝对想要的资源列表
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


        List<GenericValue> myFriendList = EntityQuery.use(delegator).from("PartyRelationship").where("partyIdTo", partyId,"partyRelationshipTypeId","FRIEND").queryList();


        for(GenericValue gv : myFriendList){

            Map<String,Object> rowMap = new HashMap<String, Object>();

            String friendPartyId = (String) gv.get("partyIdFrom");

            GenericValue shopingList = EntityQuery.use(delegator).from("ShoppingList").where("partyId",friendPartyId,"listName",resourceName).queryFirst();

            if(null != shopingList){
                rowMap.put("desc",shopingList.get("description"));
                rowMap.put("time",shopingList.get("createdStamp"));
                returnList.add(rowMap);
            }

        }


        resultMap.put("resourcesList",returnList);

        return  resultMap;
    }



    /**
     * Query My Product
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

        String unioId = (String) context.get("partyId");

//        System.out.println("*OPENID = " + openId);
//
        GenericValue partyIdentification = EntityQuery.use(delegator).from("PartyIdentification").where("idValue", unioId, "partyIdentificationTypeId", "WX_UNIO_ID").queryFirst();

        String partyId = "NA";

        if (UtilValidate.isNotEmpty(partyIdentification)) {
            partyId = (String) partyIdentification.get("partyId");
        }
        System.out.println("*partyId = " + partyId);

        String productCategoryId = "NA";

        List<GenericValue> myResourceList = null;

        List<Map<String,Object>> resourceMapList = new ArrayList<Map<String, Object>>();

        //查我的目录
        GenericValue rodCatalogRole =  EntityQuery.use(delegator).from("ProdCatalogRole").where("partyId", partyId, "roleTypeId", "ADMIN").queryFirst();

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


        resultMap.put("productList", resourceMapList);
//        resultMap.put("productCategoryId", productCategoryId);


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

        resultMap.put("postalAddress",queryAddressList);

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

                String productId  = (String) gv.get("productId");

                Map<String,Object> rowMap =  new HashMap<String, Object>();

                GenericValue product = delegator.findOne("Product",UtilMisc.toMap("productId",productId),false);


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
                                where("partyId", productDesc.get("payToPartyId"), "partyContentTypeId", "LGOIMGURL").orderBy("-fromDate").queryPagedList(0,999999).getData();


                GenericValue partyContent = null;
                if(null != contentsList && contentsList.size()>0){
                    partyContent = contentsList.get(0);
                }

                if (UtilValidate.isNotEmpty(partyContent)) {

                    rowMap.put("headPortrait",
                            partyContent.getString("objectInfo"));
                } else {
                    rowMap.put("headPortrait",
                            "https://personerp.oss-cn-hangzhou.aliyuncs.com/datas/images/defaultHead.png");
                }




                rowMap.put("payToPartyId",productDesc.get("payToPartyId"));

                rowMap.put("payToPartyName",person.get("firstName"));

                rowMap.put("partyId",partyId);

                rowMap.put("productId",productId);

                rowMap.put("internalName",product.get("internalName"));

                rowMap.put("productName",product.get("productName"));

                rowMap.put("detailImageUrl",product.get("detailImageUrl"));

                GenericValue productPrice =EntityQuery.use(delegator).from("ProductPrice").where("productId",productId).queryFirst();

                rowMap.put("price",productPrice.get("price"));

                returnList.add(rowMap);
            }

        }

        resultMap.put("collectList", returnList);

        return resultMap;
    }


    /**
     * queryOrderDetailFromWeChat
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
        Map<String,Object> rowMap = new HashMap<String, Object>();
        //Express Info
        Map<String,Object> orderExpressInfo = new HashMap<String, Object>();

        GenericValue  orderHeaderItemAndRoles  = EntityQuery.use(delegator).from("OrderHeaderItemAndRoles").where("orderId", orderId,"roleTypeId","SHIP_TO_CUSTOMER").queryFirst();

        if(null != orderHeaderItemAndRoles){

            rowMap = orderHeaderItemAndRoles.getAllFields();

            GenericValue orderPaymentPrefAndPayment = EntityQuery.use(delegator).from("OrderPaymentPrefAndPayment").where("orderId",orderId).queryFirst();

            if(null != orderPaymentPrefAndPayment){
                rowMap.put("orderPayStatus","已付款");
//                String statusId = (String) orderPaymentPrefAndPayment.get("statusId");
//
//                if(statusId.toUpperCase().indexOf("RECEIVED")>0){
//
//
//
//                }else{
//
//                    rowMap.put("orderPayStatus","未付款");
//
//                }
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

            personAddressInfoMap = queryPersonAddressInfo(delegator,(String) rowMap.get("partyId"));

            rowMap.put("personInfoMap",personInfoMap);

            rowMap.put("personAddressInfoMap",personAddressInfoMap);

        }

        GenericValue order =  delegator.findOne("OrderHeader", UtilMisc.toMap("orderId", orderId),false);
        if(null != order && null != order.get("internalCode")) {
            rowMap.put("internalCode",order.get("internalCode"));
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
        resultMap.put("orderDetail",rowMap);


        return resultMap;
    }



    public static Map<String, Object> queryMyResourceOrderFromWeChat(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();


        String unioId = (String) context.get("openId");

        System.out.println("->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>unioId = " + unioId);
//
        GenericValue partyIdentification = EntityQuery.use(delegator).from("PartyIdentification").where("idValue", unioId, "partyIdentificationTypeId", "WX_UNIO_ID").queryFirst();

        String partyId = "NA";

        if (UtilValidate.isNotEmpty(partyIdentification)) {
            partyId = (String) partyIdentification.get("partyId");
        }
        System.out.println("*partyId = " + partyId);

        List<Map<String, Object>> myResourceOrderList = new ArrayList<Map<String, Object>>();


        //是否是从App端的查询
        String area =null;


        String orderStatus = (String) context.get("orderStatus");


        Set<String> fieldSet = new HashSet<String>();
        fieldSet.add("orderId");
        fieldSet.add("partyId");
        fieldSet.add("statusId");
        fieldSet.add("currencyUom");
        fieldSet.add("grandTotal");
        fieldSet.add("productId");
        fieldSet.add("quantity");
        fieldSet.add("unitPrice");
        fieldSet.add("internalCode");

        fieldSet.add("roleTypeId");
        fieldSet.add("orderDate");
        fieldSet.add("productStoreId");
        fieldSet.add("payToPartyId");


        EntityCondition roleTypeCondition = EntityCondition
                .makeCondition(UtilMisc.toMap("roleTypeId", "BILL_FROM_VENDOR"));

        EntityCondition payToPartyIdCondition = EntityCondition
                .makeCondition(UtilMisc.toMap("payToPartyId", partyId));



        EntityCondition genericCondition = EntityCondition.makeCondition(roleTypeCondition, EntityOperator.AND, payToPartyIdCondition);




        List<GenericValue> queryMyResourceOrderList = null;

        if (null != orderStatus && orderStatus.equals("SHIPMENT")) {

            EntityCondition orderStatusCondition = EntityCondition.makeCondition(UtilMisc.toMap("statusId", "ORDER_SENT"));

            EntityCondition listConditions3 = EntityCondition
                    .makeCondition(genericCondition, EntityOperator.AND, orderStatusCondition);

            queryMyResourceOrderList = delegator.findList("OrderHeaderItemAndRoles",
                    listConditions3, fieldSet,
                    UtilMisc.toList("-orderDate"), null, false);

        } else {
            queryMyResourceOrderList = delegator.findList("OrderHeaderItemAndRoles",
                    genericCondition, fieldSet,
                    UtilMisc.toList("-orderDate"), null, false);

        }

        System.out.println("-----------------------------------------------------------------------------------> queryMyResourceOrderList = " + queryMyResourceOrderList);
        System.out.println("-----------------------------------------------------------------------------------> queryMyResourceOrderList");

        if (null != queryMyResourceOrderList && queryMyResourceOrderList.size() > 0) {

            for (GenericValue gv : queryMyResourceOrderList) {

                Map<String, Object> rowMap = new HashMap<String, Object>();
                rowMap = gv.getAllFields();


                String productStoreId = (String) gv.get("productStoreId");
                String productId = (String) gv.get("productId");

                GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), false);
                GenericValue product = delegator.findOne("Product", UtilMisc.toMap("productId", productId), false);
                rowMap.put("productName", "" + product.get("productName"));
                rowMap.put("detailImageUrl", (String) product.get("detailImageUrl"));
                String payToPartyId = (String) productStore.get("payToPartyId");
                if (null != area && area.equals("app") && !payToPartyId.equals(partyId)) {
                    continue;
                }

                rowMap.put("payToPartyId", payToPartyId);
                String statusId = (String) gv.get("statusId");




                rowMap.put("statusId", UtilProperties.getMessage("PersonManagerUiLabels.xml", statusId, locale));


                String payFromPartyId = (String) rowMap.get("partyId");

                Map<String, String> personInfoMap = null;
                Map<String, String> personAddressInfoMap = null;

                //说明这笔订单我是卖家,查买家头像信息
                //   if(payToPartyId.equals(partyId)){   }
                GenericValue custOrderInfo = EntityQuery.use(delegator).from("OrderHeaderItemAndRoles").where("orderId", rowMap.get("orderId"), "roleTypeId", "SHIP_TO_CUSTOMER").queryFirst();

                personInfoMap = queryPersonBaseInfo(delegator, (String) custOrderInfo.get("partyId"));
                personAddressInfoMap = queryPersonAddressInfo(delegator, (String) custOrderInfo.get("partyId"));
                rowMap.put("realPartyId", custOrderInfo.get("partyId"));

                rowMap.put("userPartyId", partyId);

                rowMap.put("personInfoMap", personInfoMap);
                rowMap.put("personAddressInfoMap", personAddressInfoMap);


                System.out.println("orderId=" + gv.get("orderId"));
                System.out.println("payToPartyId=" + payToPartyId);
                System.out.println("payFromPartyId=" + (String) custOrderInfo.get("partyId"));

                GenericValue orderPaymentPrefAndPayment = EntityQuery.use(delegator).from("OrderPaymentPreference").where("orderId", gv.get("orderId")).queryFirst();


                GenericValue payment = EntityQuery.use(delegator).from("Payment").where("partyIdTo", payToPartyId, "partyIdFrom", (String) custOrderInfo.get("partyId"), "comments", gv.get("orderId")).queryFirst();

                System.out.println("=============================================================");
                System.out.println("orderPaymentPrefAndPayme=" + orderPaymentPrefAndPayment);
                System.out.println("payment=" + payment);
                System.out.println("=============================================================");


                if (null != orderPaymentPrefAndPayment) {

                    rowMap.put("orderPayStatus", "已收款");
                    rowMap.put("payStatusCode", "1");

                } else {


                    if (null != payment) {
                        String paymentStatusId = (String) payment.get("statusId");
                        if (paymentStatusId != null) {

                            rowMap.put("orderPayStatus", "已收款");

                            rowMap.put("payStatusCode", "1");

                        }
                    } else {

                        rowMap.put("payStatusCode", "0");
                        rowMap.put("orderPayStatus", "未收款");
                    }

                }
                if(!statusId.equals("ORDER_SENT")){
                    rowMap.put("orderShipment","未发货");
                }else{
                    rowMap.put("orderShipment","已发货");
                    if(rowMap.get("orderPayStatus").equals("已收款")){
                        rowMap.put("orderCompleted","已完成");
                    }
                }


                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> RowMap" + rowMap);
                //不查询已收款的订单时,直接放入
                if (null != orderStatus && !orderStatus.equals("PAYMENT")) {
                    myResourceOrderList.add(rowMap);
                }
                if (null != orderStatus && orderStatus.equals("PAYMENT")) {
                    if (!rowMap.get("orderPayStatus").equals("未收款")) {
                        myResourceOrderList.add(rowMap);
                    }
                }
                if(null==orderStatus){
                    myResourceOrderList.add(rowMap);
                }
            }
        }

//        resultMap.put("orderStatus", orderStatus);

        resultMap.put("queryMyResourceOrderList", myResourceOrderList);

        return resultMap;
    }


    /**
     * query Order FromWeChat
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


        String openId = (String) context.get("openId");

        System.out.println("*OPENID = " + openId);

        GenericValue partyIdentification = EntityQuery.use(delegator).from("PartyIdentification").where("idValue", openId, "partyIdentificationTypeId", "WX_UNIO_ID").queryFirst();

        String partyId = "NA";

        if (UtilValidate.isNotEmpty(partyIdentification)) {
            partyId = (String) partyIdentification.get("partyId");
        }
        System.out.println("*partyId = " + partyId);
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


//        EntityCondition findConditions3 = EntityCondition
//                .makeCondition(UtilMisc.toMap("roleTypeId", "BILL_TO_CUSTOMER"));
//
//        EntityCondition findConditions = EntityCondition
//                .makeCondition(UtilMisc.toMap("partyId", partyId));
//
//
//        EntityCondition findConditions2 = EntityCondition
//                .makeCondition(UtilMisc.toMap("payToPartyId", partyId));
//
//        EntityCondition listConditions = EntityCondition
//                .makeCondition(findConditions, EntityOperator.OR, findConditions2);
//
//        EntityCondition listConditions2 = EntityCondition
//                .makeCondition(findConditions3, EntityOperator.AND, listConditions);

        //只查我的采购订单

        EntityCondition roleTypeCondition  = EntityCondition
                .makeCondition(UtilMisc.toMap("roleTypeId", "BILL_TO_CUSTOMER"));

        EntityCondition payToPartyIdCondition = EntityCondition
                .makeCondition(UtilMisc.toMap("partyId",partyId));


        EntityCondition listConditions2 = EntityCondition
                .makeCondition(roleTypeCondition,EntityOperator.AND,payToPartyIdCondition);
        List<GenericValue> queryMyResourceOrderList = delegator.findList("OrderHeaderItemAndRoles",
                listConditions2, fieldSet,
                UtilMisc.toList("-orderDate"), null, false);
        System.out.println("*queryMyResourceOrderList = " + queryMyResourceOrderList);
        if (null != queryMyResourceOrderList && queryMyResourceOrderList.size() > 0) {
            for (GenericValue gv : queryMyResourceOrderList) {
                Map<String, Object> rowMap = new HashMap<String, Object>();
                rowMap = gv.getAllFields();
                String productStoreId = (String) gv.get("productStoreId");
                String productId = (String) gv.get("productId");
                GenericValue product = delegator.findOne("Product", UtilMisc.toMap("productId", productId), false);
                GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), false);
                rowMap.put("productName", "" + product.get("productName"));
                rowMap.put("detailImageUrl", (String) product.get("detailImageUrl"));
                String payToPartyId = (String) productStore.get("payToPartyId");
                rowMap.put("payToPartyId", payToPartyId);





                GenericValue orderPaymentPrefAndPayment = EntityQuery.use(delegator).from("OrderPaymentPreference").where("orderId", rowMap.get("orderId")).queryFirst();


                String statusId = (String) gv.get("statusId");

                if (null != orderPaymentPrefAndPayment) {

                    rowMap.put("orderPayStatus", "已收款");
                    rowMap.put("payStatusCode", "1");

                } else {
                    rowMap.put("orderPayStatus", "未付款");
                }

                if(statusId.equals("ORDER_SENT")){
                    rowMap.put("orderShipment","已发货");
                    if(null!=rowMap.get("orderPayStatus")&&rowMap.get("orderPayStatus").equals("已收款")){
                        rowMap.put("orderCompleted","已完成");
                    }
                }else{
                    rowMap.put("orderShipment","未发货");
                }




                //String statusId = (String) gv.get("statusId");
//                rowMap.put("statusId", UtilProperties.getMessage("PersonManagerUiLabels.xml", statusId, locale));
                String payFromPartyId = (String) rowMap.get("partyId");

                Map<String, String> personInfoMap = null;

                Map<String, String> personAddressInfoMap = null;

                //说明这笔订单我是卖家,查买家头像信息
                if (payToPartyId.equals(partyId)) {
                    personInfoMap = queryPersonBaseInfo(delegator, payFromPartyId);
                    personAddressInfoMap = queryPersonAddressInfo(delegator, payFromPartyId);
                }
                //说明这笔单我是买家,查卖家头像信息
                if (!payToPartyId.equals(partyId)) {
                    personInfoMap = queryPersonBaseInfo(delegator, payToPartyId);
                    personAddressInfoMap = queryPersonAddressInfo(delegator, payToPartyId);
                }
                rowMap.put("userPartyId", partyId);
                rowMap.put("personInfoMap", personInfoMap);
                rowMap.put("personAddressInfoMap", personAddressInfoMap);

                returnList.add(rowMap);
            }
        }


        resultMap.put("orderList", returnList);

        return resultMap;
    }
}
