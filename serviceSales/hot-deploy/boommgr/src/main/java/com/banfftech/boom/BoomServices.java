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
        return partyId;
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


                userLogin = PersonManagerServices.justCreatePartyPersonUserLogin(delegator, dispatcher, name, nickName);

                String partyId = (String) userLogin.getString("partyId");

                // 角色
                dispatcher.runSync("createPartyRole",
                        UtilMisc.toMap("userLogin", admin, "partyId", partyId, "roleTypeId", "OWNER"));


                String groupId = createGroup(delegator, dispatcher, admin, organizationName, "");


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










                //  邮政地址
                String contactMechPurposeTypeId = "POSTAL_ADDRESS";
                Map<String, Object> createPartyPostalAddressOutMap = dispatcher.runSync("createPartyPostalAddress",
                        UtilMisc.toMap("userLogin", admin, "toName", name, "partyId", partyId, "countryGeoId", PeConstant.DEFAULT_GEO_COUNTRY, "city",city, "address1", province + "-" +country,"address2",city, "postalCode", PeConstant.DEFAULT_POST_CODE
                                ));
                String contactMechId = (String) createPartyPostalAddressOutMap.get("contactMechId");
                if (!ServiceUtil.isSuccess(createPartyPostalAddressOutMap)) {
                    return createPartyPostalAddressOutMap;
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

        Map<String, Object> createProductOutMap = dispatcher.runSync("createProduct", createProductInMap);
        if (ServiceUtil.isError(createProductOutMap)) {
            return createProductOutMap;
        }

        String productId = (String) createProductOutMap.get("productId");

        if (rawMaterials != null && rawMaterials.length() > 2) {
            for (String rowProduct : rawMaterials.split(",")) {
                String productIdFrom = rowProduct.substring(0, rowProduct.indexOf(":"));
                String count = rowProduct.substring(rowProduct.indexOf(":") + 1);
                dispatcher.runSync("createProductAssoc", UtilMisc.toMap("userLogin", admin, "productIdTo", productId, "productId", productIdFrom
                        , "quantity", new BigDecimal(count), "productAssocTypeId", "MANUF_COMPONENT", "fromDate", org.apache.ofbiz.base.util.UtilDateTime.nowTimestamp()));
            }
        }


        dispatcher.runSync("addProductRole", UtilMisc.toMap("userLogin", admin, "roleTypeId", "ADMIN", "productId", productId, "partyId", partyId));

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
    public static Map<String, Object> createMySupplier(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

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

        String provinceName = (String) context.get("provinceName");
        String cityName = (String) context.get("cityName");
        String countyName = (String) context.get("countyName");
        String detailInfo = (String) context.get("detailInfo");



        boolean isExsitsRole = false;

        GenericValue exsitsUser = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", supplierTel));

        String supplierPartyId = "";

        if (null == exsitsUser) {

            supplierPartyId = createSupplier(delegator, dispatcher, admin, supplierName, "无");

            // Create Party Block
            int random = (int) (Math.random() * 1000000 + 1);
            delegator.createOrStore(delegator.makeValue("Person",
                    UtilMisc.toMap("nickname", "#" + random,
                            "firstName", supplierName, "lastName", " ", "gender", "M", "partyId", supplierPartyId)));


            // Create UserLogin Block
            Map<String, Object> createUserLoginInMap = UtilMisc.toMap("userLogin", admin, "userLoginId",
                    supplierTel, "partyId", supplierPartyId, "currentPassword", "ofbiz",
                    "currentPasswordVerify", "ofbiz", "enabled", "Y");
            Map<String, Object> createUserLogin = dispatcher.runSync("createUserLogin", createUserLoginInMap);

        } else {
            supplierPartyId = exsitsUser.getString("partyId");
            GenericValue hasRelation = EntityQuery.use(delegator).from("PartyRelationship").where(
                    "partyIdFrom", partyId, "partyIdTo", supplierPartyId, "roleTypeIdTo", "SUPPLIER", "roleTypeIdFrom", "CUSTOMER").queryFirst();
            if(null != hasRelation){
                isExsitsRole = !isExsitsRole;

            }
        }


        if (false == isExsitsRole) {


            Map<String, Object> createPartyRelationshipInMap = new HashMap<String, Object>();
            createPartyRelationshipInMap.put("roleTypeIdFrom", "CUSTOMER");
            createPartyRelationshipInMap.put("roleTypeIdTo", "SUPPLIER");
            createPartyRelationshipInMap.put("userLogin", admin);
            createPartyRelationshipInMap.put("partyIdFrom", partyId);
            createPartyRelationshipInMap.put("partyIdTo", supplierPartyId);
            createPartyRelationshipInMap.put("partyRelationshipTypeId", PeConstant.SUPPLIER);
            Map<String, Object> createPartyRelationshipOutMap = dispatcher.runSync("createPartyRelationship", createPartyRelationshipInMap);



            //  邮政地址"address1", province + "-" +country,"address2",city
            String contactMechPurposeTypeId = "POSTAL_ADDRESS";
            Map<String, Object> createPartyPostalAddressOutMap = dispatcher.runSync("createPartyPostalAddress",
                    UtilMisc.toMap("userLogin", admin, "attnName",partyId,"toName", supplierName, "partyId", supplierPartyId, "countryGeoId", PeConstant.DEFAULT_GEO_COUNTRY,
                            "city",cityName, "address1", provinceName+"-"+countyName+"-"+cityName+"-"+detailInfo ,"postalCode", PeConstant.DEFAULT_POST_CODE ));
            String contactMechId = (String) createPartyPostalAddressOutMap.get("contactMechId");
            if (!ServiceUtil.isSuccess(createPartyPostalAddressOutMap)) {
                return createPartyPostalAddressOutMap;
            }

            resultMap.put("supplierInfo", null);

            if (ServiceUtil.isError(createPartyRelationshipOutMap)) {
                return createPartyRelationshipOutMap;
            }
        } else {
            resultMap.put("supplierInfo", queryPersonBaseInfo(delegator, supplierPartyId));
        }


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
        return partyId;
    }


}
