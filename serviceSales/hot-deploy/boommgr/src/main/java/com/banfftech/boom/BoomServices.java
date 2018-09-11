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

import static main.java.com.banfftech.personmanager.PersonManagerQueryServices.queryPersonBaseInfo;
import static main.java.com.banfftech.platformmanager.common.PlatformLoginWorker.getToken;
import static main.java.com.banfftech.platformmanager.wechat.WeChatUtil.getAccessToken;
import static main.java.com.banfftech.wechatminiprogram.WeChatMiniProgramServices.updateProductBizData;
import static main.java.com.banfftech.wechatminiprogram.WeChatMiniProgramServices.updateProductBizDataFromOrder;

/**
 * Created by S on 2018/8/29.
 */
public class BoomServices {


    public final static String module = BoomServices.class.getName();

    private static String createGroup(Delegator delegator, LocalDispatcher dispatcher, GenericValue admin, String vender, String venderLocal) throws GenericEntityException, GenericServiceException {
        String partyId = "";
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


        return partyId;
    }


    /**
     * create PurchaseOrder
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

        //下单的当事人,创建服务会检查他有没有创建权限等。
        String partyId = (String) userLogin.get("partyId");

        if(null!= itemArray){
            for(String rowStr : itemArray.split(",")){
                String productId = rowStr.substring(0,rowStr.indexOf(":"));
                String quantityStr = rowStr.substring(rowStr.indexOf(":")+1,rowStr.lastIndexOf(":"));
                String supplierPartyId = rowStr.substring(rowStr.lastIndexOf(":")+1,rowStr.indexOf("/"));
                String beiZhu    = rowStr.substring(rowStr.indexOf("/")+1);

                //供应商产品仓库暂无
//                String originFacilityId = (String) context.get("originFacilityId");

                //供应商
                String billFromVendorPartyId = supplierPartyId;
                // Quantity  Amount
                BigDecimal quantity = new BigDecimal(quantityStr);

                //最终客户、收货客户、意向客户等客户当事人
                String billToCustomerPartyId, endUserCustomerPartyId, placingCustomerPartyId, shipToCustomerPartyId = partyId;
                //发货人就是供应商
                String   shipFromVendorPartyId = billFromVendorPartyId;

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
            }
        }







        dispatcher.runSync("changeProductionRunStatus",UtilMisc.toMap("userLogin",admin,"productionRunId",workEffortId,"statusId","PRUN_DOC_PRINTED"));
//        String orderId = (String) createOrderOut.get("orderId");



        return result;
    }



    /**
     * 企业Bom注册
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

            if (checkCaptcha || captcha.equals("123456") ) {


                // Check is Exsit Contact Number ?..
                List<GenericValue> teleContact = EntityQuery.use(delegator).from("TelecomNumberAndPartyAndRelationshipView").where(
                        "contactNumber", tel,"roleTypeIdTo","LEAD","partyRelationshipTypeId","LEAD_OWNER","contactMechTypeId","TELECOM_NUMBER").queryList();


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



                String groupId = createGroup(delegator, dispatcher, admin, organizationName, "");

                dispatcher.runSync("createPartyAcctgPreference",UtilMisc.toMap("userLogin", admin
                ,"partyId",groupId,"baseCurrencyUomId","CNY"));


                // Create Emp to PartyGroup From Lead
                if(teleContact.size()>0){
                    for(GenericValue row:teleContact){
                        Map<String, Object> createPartyRelationshipInMap = new HashMap<String, Object>();
                        createPartyRelationshipInMap.put("userLogin", admin);
                        //createPartyRelationship to Group



                        GenericValue partyRole = EntityQuery.use(delegator).from("PartyRole").where("partyId", row.getString("partyId"), "roleTypeId", "ACCOUNT_LEAD").queryFirst();
                        if (null == partyRole) {
                            dispatcher.runSync("createPartyRole",
                                    UtilMisc.toMap("userLogin", admin, "partyId", row.getString("partyId"), "roleTypeId", "ACCOUNT_LEAD"));
                        }

                        createPartyRelationshipInMap.put("roleTypeIdTo", "LEAD");
                        createPartyRelationshipInMap.put("roleTypeIdFrom", "ACCOUNT_LEAD");
                        createPartyRelationshipInMap.put("partyRelationshipTypeId", "EMPLOYMENT");
                        createPartyRelationshipInMap.put("partyIdTo",groupId);
                        createPartyRelationshipInMap.put("partyIdFrom",row.getString("partyId"));

                        Map<String, Object> createPartyRelationshipOutMap = dispatcher.runSync("createPartyRelationship", createPartyRelationshipInMap);
                        if (ServiceUtil.isError(createPartyRelationshipOutMap)) {
                            return createPartyRelationshipOutMap;
                        }
                    }
                }


                Map<String, Object> createPartyRelationshipInMap = new HashMap<String, Object>();

                createPartyRelationshipInMap.put("userLogin", admin);
                createPartyRelationshipInMap.put("roleTypeIdTo", "_NA_");
                createPartyRelationshipInMap.put("roleTypeIdFrom", "_NA_");
                createPartyRelationshipInMap.put("partyIdFrom", partyId);
                createPartyRelationshipInMap.put("partyIdTo", groupId);
                createPartyRelationshipInMap.put("partyRelationshipTypeId", "OWNER");
                Map<String, Object> createPartyRelationshipOutMap = dispatcher.runSync("createPartyRelationship", createPartyRelationshipInMap);
                if (ServiceUtil.isError(createPartyRelationshipOutMap)) {
                    return createPartyRelationshipOutMap;
                }


                // 创建联系电话
                Map<String, Object> inputTelecom = UtilMisc.toMap();
                inputTelecom.put("partyId", partyId);
                inputTelecom.put("contactNumber", tel);
                inputTelecom.put("contactMechTypeId", "TELECOM_NUMBER");
                inputTelecom.put("contactMechPurposeTypeId", "PHONE_MOBILE");
                inputTelecom.put("userLogin", admin);
                Map<String, Object> createTelecom = dispatcher.runSync("createPartyTelecomNumber", inputTelecom);

                Map<String,String> userInfoMap = new HashMap<String, String>();
                userInfoMap.put("nickname",nickName);
                userInfoMap.put("sex",gender);
                userInfoMap.put("language",language);
                userInfoMap.put("headimgurl",avatarUrl);
                main.java.com.banfftech.platformmanager.common.PlatformLoginWorker.updatePersonAndIdentificationLanguage(admin, partyId, delegator, openId, userInfoMap, userLogin, dispatcher);




                if(city!=null && !city.equals("") && province!=null){


                //  邮政地址
                String contactMechPurposeTypeId = "POSTAL_ADDRESS";
                Map<String, Object> createPartyPostalAddressOutMap = dispatcher.runSync("createPartyPostalAddress",
                        UtilMisc.toMap("userLogin", admin, "toName", name, "partyId", partyId, "countryGeoId", PeConstant.DEFAULT_GEO_COUNTRY, "city",city, "address1", province + "-" +country,"address2",city, "postalCode", PeConstant.DEFAULT_POST_CODE
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




                result.put("tarjeta", getToken(userLogin.getString("userLoginId"), delegator));
                result.put("partyId", partyId);
                result.put("userInfo", PersonManagerQueryServices.queryPersonBaseInfo(delegator,partyId));
            } else {
                return ServiceUtil.returnError("check fail");
            }
        }


        return result;
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
            return result;
        }

        result.put("tarjeta", null);
        result.put("userInfo", null);
        result.put("partyId", null);

        return result;
    }


    /**
     * createProductionRouting
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

        GenericValue relation = EntityQuery.use(delegator).from("PartyRelationship").where(
                "partyIdFrom", partyId, "partyRelationshipTypeId", "OWNER" ).queryFirst();

        String partyGroupId = relation.getString("partyIdTo");

//        createProductionRun
        GenericValue facility =  EntityQuery.use(delegator).from("Facility").where(
                "ownerPartyId", partyGroupId  ).queryFirst();
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

        dispatcher.runSync("createRequirement",UtilMisc.toMap("userLogin",userLogin,"requirementTypeId","INTERNAL_REQUIREMENT"
        ,"facilityId",facilityId,"productId",productId,"statusId","REQ_APPROVED","requirementStartDate",org.apache.ofbiz.base.util.UtilDateTime.nowTimestamp(),
                "quantity",new BigDecimal(quantity)));

        return resultMap;
    }





    /**
     * createMyFinishedGood
     *
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
//        String quantityUomId = (String) context.get("quantityUomId");
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
//        createProductInMap.put("quantityUomId", quantityUomId);


        GenericValue relation = EntityQuery.use(delegator).from("PartyRelationship").where(
                "partyIdFrom", partyId, "partyRelationshipTypeId", "OWNER" ).queryFirst();

        String partyGroupId = relation.getString("partyIdTo");


        GenericValue facility =  EntityQuery.use(delegator).from("Facility").where(
                "ownerPartyId", partyGroupId  ).queryFirst();
        String facilityId = facility.getString("facilityId");
    createProductInMap.put("facilityId", facilityId );

        Map<String, Object> createProductOutMap = dispatcher.runSync("createProduct", createProductInMap);
        if (ServiceUtil.isError(createProductOutMap)) {
            return createProductOutMap;
        }






        String productId = (String) createProductOutMap.get("productId");


        dispatcher.runSync("createProductFacility",UtilMisc.toMap("userLogin",userLogin,
                "productId",productId,"facilityId",facilityId,"minimumStock",BigDecimal.ZERO,"reorderQuantity",new BigDecimal("10000"),"daysToShip",new Long(10)));


        if (rawMaterials != null && rawMaterials.length() > 2) {
            for (String rowProduct : rawMaterials.split(",")) {
                String productIdFrom = rowProduct.substring(0, rowProduct.indexOf(":"));
                String count = rowProduct.substring(rowProduct.indexOf(":") + 1);
                dispatcher.runSync("createProductAssoc", UtilMisc.toMap("userLogin", admin, "productIdTo",productIdFrom , "productId", productId
                        , "quantity", new BigDecimal(count), "productAssocTypeId", "MANUF_COMPONENT", "fromDate", org.apache.ofbiz.base.util.UtilDateTime.nowTimestamp()));
            }
        }


        dispatcher.runSync("addProductRole", UtilMisc.toMap("userLogin", admin, "roleTypeId", "ADMIN", "productId", productId, "partyId", partyId));
        dispatcher.runSync("createCostComponent", UtilMisc.toMap("userLogin", admin,"costComponentTypeId","GEN_COST","costUomId","CNY", "productId", productId, "partyId", partyId));



        return resultMap;
    }


    /**
     * empJoinPartyGroup
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
        String empId        = (String) context.get("empId");

        Map<String, Object> createPartyRelationshipInMap = new HashMap<String, Object>();
        createPartyRelationshipInMap.put("userLogin", admin);
        createPartyRelationshipInMap.put("roleTypeIdTo", "_NA_");
        createPartyRelationshipInMap.put("roleTypeIdFrom",roleTypeIdFrom);
        createPartyRelationshipInMap.put("partyRelationshipTypeId", "EMPLOYMENT");
        createPartyRelationshipInMap.put("partyIdTo",partyGroupId);
        createPartyRelationshipInMap.put("partyIdFrom",empId);

        Map<String, Object> createPartyRelationshipOutMap = dispatcher.runSync("createPartyRelationship", createPartyRelationshipInMap);
        if (ServiceUtil.isError(createPartyRelationshipOutMap)) {
            return createPartyRelationshipOutMap;
        }

        return resultMap;
    }

    /**
     * createRawMaterials
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
//
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = userLogin.getString("partyId");

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
        createProductInMap.put("detailImageUrl", imagePath);
        createProductInMap.put("smallImageUrl", imagePath);
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



        dispatcher.runSync("addProductRole", UtilMisc.toMap("userLogin", admin, "roleTypeId", "ADMIN", "productId", productId, "partyId", partyId));

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

        String supplierName = (String) context.get("supplierName");
        String supplierTel = (String) context.get("supplierTel");

        String firstName = supplierName;
        String lastName = " ";

        if(supplierName.length()>=4){
            firstName = supplierName.substring(0,2);
            lastName = supplierName.substring(2+1);
        }
        if(supplierName.length()<=3){
            lastName = supplierName.substring(0,1);
            firstName = supplierName.substring(1);
        }

        //是否已经添加过

        Long hasData = EntityQuery.use(delegator).from("PartyRelationshipAndContactMechDetail").where(
                "partyIdFrom", partyId, "roleTypeIdTo", "LEAD","partyRelationshipTypeId","LEAD_OWNER","tnContactNumber",supplierTel).queryCount();
        if(hasData>0){
            return ServiceUtil.returnError("已经存在的线索");
        }



        String provinceName = (String) context.get("provinceName");
        String cityName = (String) context.get("cityName");
        String countyName = (String) context.get("countyName");
        String detailInfo = (String) context.get("detailInfo");


        Map<String,Object> createLeadMap = new HashMap<String, Object>();

        createLeadMap.put("userLogin",userLogin);
        createLeadMap.put("firstName",firstName );
        createLeadMap.put("lastName",lastName );
        createLeadMap.put("countryGeoId","CHN");
        createLeadMap.put("city",cityName==null?"":cityName);
        if(null != countyName && null != provinceName && null != detailInfo){
        createLeadMap.put("address1",countyName+" "+provinceName+" "+cityName+" "+detailInfo);
        }
        createLeadMap.put("countryCode","86");
        createLeadMap.put("postalCode","200000");
        createLeadMap.put("contactNumber",supplierTel);


        Map<String, Object> createLeadOutMap = dispatcher.runSync("createLead", createLeadMap);



            if (ServiceUtil.isError(createLeadOutMap)) {
                return createLeadOutMap;
            }
        String resultPartyId = (String) createLeadOutMap.get("partyId");


//        boolean isExsitsRole = false;
//
//        GenericValue exsitsUser = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", supplierTel));
//
//        String supplierPartyId = "";
//
//        if (null == exsitsUser) {
//
//            supplierPartyId = createSupplier(delegator, dispatcher, admin, supplierName, "无");
//
//            // Create Party Block
//            int random = (int) (Math.random() * 1000000 + 1);
//            delegator.createOrStore(delegator.makeValue("Person",
//                    UtilMisc.toMap("nickname", "#" + random,
//                            "firstName", supplierName, "lastName", " ", "gender", "M", "partyId", supplierPartyId)));
//
//
//            // Create UserLogin Block
//            Map<String, Object> createUserLoginInMap = UtilMisc.toMap("userLogin", admin, "userLoginId",
//                    supplierTel, "partyId", supplierPartyId, "currentPassword", "ofbiz",
//                    "currentPasswordVerify", "ofbiz", "enabled", "Y");
//            Map<String, Object> createUserLogin = dispatcher.runSync("createUserLogin", createUserLoginInMap);
//
//        } else {
//            supplierPartyId = exsitsUser.getString("partyId");
//            GenericValue hasRelation = EntityQuery.use(delegator).from("PartyRelationship").where(
//                    "partyIdFrom", partyId, "partyIdTo", supplierPartyId, "roleTypeIdTo", "SUPPLIER", "roleTypeIdFrom", "CUSTOMER").queryFirst();
//            if(null != hasRelation){
//                isExsitsRole = !isExsitsRole;
//
//            }
//        }
//
//
//        if (false == isExsitsRole) {
//
//
//            Map<String, Object> createPartyRelationshipInMap = new HashMap<String, Object>();
//            createPartyRelationshipInMap.put("roleTypeIdFrom", "CUSTOMER");
//            createPartyRelationshipInMap.put("roleTypeIdTo", "SUPPLIER");
//            createPartyRelationshipInMap.put("userLogin", admin);
//            createPartyRelationshipInMap.put("partyIdFrom", partyId);
//            createPartyRelationshipInMap.put("partyIdTo", supplierPartyId);
//            createPartyRelationshipInMap.put("partyRelationshipTypeId", PeConstant.SUPPLIER);
//            Map<String, Object> createPartyRelationshipOutMap = dispatcher.runSync("createPartyRelationship", createPartyRelationshipInMap);
//
//
//
//            //  邮政地址"address1", province + "-" +country,"address2",city
//            String contactMechPurposeTypeId = "POSTAL_ADDRESS";
//            Map<String, Object> createPartyPostalAddressOutMap = dispatcher.runSync("createPartyPostalAddress",
//                    UtilMisc.toMap("userLogin", admin, "attnName",partyId,"toName", supplierName, "partyId", supplierPartyId, "countryGeoId", PeConstant.DEFAULT_GEO_COUNTRY,
//                            "city",cityName, "address1", provinceName+"-"+countyName+"-"+cityName+"-"+detailInfo ,"postalCode", PeConstant.DEFAULT_POST_CODE ));
//            String contactMechId = (String) createPartyPostalAddressOutMap.get("contactMechId");
//            if (!ServiceUtil.isSuccess(createPartyPostalAddressOutMap)) {
//                return createPartyPostalAddressOutMap;
//            }
//
//            resultMap.put("supplierInfo", null);
//
//            if (ServiceUtil.isError(createPartyRelationshipOutMap)) {
//                return createPartyRelationshipOutMap;
//            }
//        } else {
//            resultMap.put("supplierInfo", queryPersonBaseInfo(delegator, supplierPartyId));
//        }



        dispatcher.runSync("createPartyRole",
                UtilMisc.toMap("userLogin", admin, "partyId", resultPartyId, "roleTypeId", "SUPPLIER"));

        resultMap.put("leadInfo",UtilMisc.toMap("partyId",resultPartyId));
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
