package main.java.com.banfftech.platformmanager;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.ofbiz.base.util.HttpRequestFileUpload;
import org.apache.ofbiz.entity.transaction.TransactionUtil;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
import org.apache.ofbiz.base.util.UtilDateTime;

import org.apache.ofbiz.entity.model.ModelEntity;
import main.java.com.banfftech.personmanager.PersonManagerServices;
import main.java.com.banfftech.platformmanager.oss.OSSUnit;
import main.java.com.banfftech.platformmanager.util.TestExcel;
import net.sf.json.JSONObject;
import main.java.com.banfftech.platformmanager.constant.PeConstant;
import main.java.com.banfftech.platformmanager.util.EmojiHandler;
import main.java.com.banfftech.platformmanager.util.UtilTools;
import main.java.com.banfftech.platformmanager.wechat.AccessToken;
import main.java.com.banfftech.platformmanager.wechat.WeChatUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
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

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.rmi.CORBA.Util;
import javax.servlet.http.HttpServletRequest;

import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.service.GenericServiceException;

import org.apache.ofbiz.entity.transaction.GenericTransactionException;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.entity.GenericEntity;

import javax.servlet.http.HttpServletResponse;

import org.apache.ofbiz.entity.condition.EntityOperator;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import org.apache.ofbiz.base.util.UtilHttp;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.birt.chart.extension.datafeed.GanttEntry;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;

import static main.java.com.banfftech.personmanager.PersonManagerQueryServices.module;
import static main.java.com.banfftech.personmanager.PersonManagerQueryServices.queryPersonBaseInfo;
import static main.java.com.banfftech.platformmanager.wechat.WeChatUtil.getAccessToken;


/**
 * Created by Administrator on 2017/9/12.
 */
public class PlatformManagerServices {

    public final static String module = PlatformManagerServices.class.getName();

    public static final String resourceUiLabels = "PlatformManagerUiLabels.xml";


    private static String url = null;
    private static String appkey = null;
    private static String secret = null;
    private static String smsFreeSignName = null;
    private static String smsTemplateCode = null;



    public static Map<String, Object> createProductFeatureInertPk(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException{

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");

        String productFeatureId = (String) context.get("productFeatureId");
        String productFeatureCategoryId = (String) context.get("productFeatureCategoryId");
        String productFeatureTypeId = (String) context.get("productFeatureTypeId");
        String description = (String) context.get("description");

        GenericValue newProductFeture = delegator.makeValue("ProductFeature", UtilMisc.toMap("productFeatureId",productFeatureId,"productFeatureCategoryId", productFeatureCategoryId, "productFeatureTypeId", productFeatureTypeId, "description", description));
        newProductFeture.create();

        return ServiceUtil.returnSuccess();
    }



    /**
     * Get Login Captcha
     *
     * @param dctx
     * @param context
     * @return
     */
    public static Map<String, Object> getLoginCaptcha(DispatchContext dctx, Map<String, Object> context) {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        String teleNumber = (String) context.get("teleNumber");

        String smsType = (String) context.get("smsType");// LOGIN 或 REGISTER
        java.sql.Timestamp nowTimestamp = org.apache.ofbiz.base.util.UtilDateTime.nowTimestamp();
        int validTime = Integer.valueOf(EntityUtilProperties.getPropertyValue("pe", "sms.validTime", "900", delegator));
        int intervalTime = Integer
                .valueOf(EntityUtilProperties.getPropertyValue("pe", "sms.intervalTime", "20", delegator));
        boolean sendSMS = false;

        // Find SmsData
        EntityCondition captchaCondition = EntityCondition.makeCondition(
                EntityCondition.makeCondition("teleNumber", EntityOperator.EQUALS, teleNumber),
                EntityUtil.getFilterByDateExpr(), EntityCondition.makeCondition("isValid", EntityOperator.EQUALS, "N"));

        GenericValue sms = null;
        try {
            sms = EntityUtil.getFirst(delegator.findList("SmsValidateCode", captchaCondition, null,
                    UtilMisc.toList("-" + ModelEntity.CREATE_STAMP_FIELD), null, false));
        } catch (GenericEntityException e) {
            // TODO Sms-Ga EXCEPTION
        }

        if (UtilValidate.isEmpty(sms)) {
            sendSMS = true;
        } else {
            org.apache.ofbiz.base.util.Debug.logInfo("The user tel:[" + teleNumber + "]  verfiy code["
                    + sms.getString("captcha") + "], check the interval time , if we'll send new code", module);
            // 如果已有未验证的记录存在，则检查是否过了再次重发的时间间隔，没过就忽略本次请求
            if (org.apache.ofbiz.base.util.UtilDateTime.nowTimestamp().after(org.apache.ofbiz.base.util.UtilDateTime
                    .adjustTimestamp((java.sql.Timestamp) sms.get("fromDate"), Calendar.SECOND, intervalTime))) {
                sms.set("thruDate", nowTimestamp);
                try {
                    sms.store();
                } catch (GenericEntityException e) {

                    // return
                    // ServiceUtil.returnError("CloudCardInternalServiceError"));
                }
                org.apache.ofbiz.base.util.Debug.logInfo("The user tel:[" + teleNumber + "]  will get new verfiy code!",
                        module);
                sendSMS = true;
            }
        }

        if (sendSMS) {
            // 生成验证码
            String captcha = org.apache.ofbiz.base.util.UtilFormatOut
                    .padString(String.valueOf(Math.round((Math.random() * 10e6))), 6, false, '0');
            Map<String, Object> smsValidateCodeMap = UtilMisc.toMap();
            smsValidateCodeMap.put("teleNumber", teleNumber);
            smsValidateCodeMap.put("captcha", captcha);
            smsValidateCodeMap.put("smsType", smsType);
            smsValidateCodeMap.put("isValid", "N");
            smsValidateCodeMap.put("fromDate", nowTimestamp);
            smsValidateCodeMap.put("thruDate",
                    org.apache.ofbiz.base.util.UtilDateTime.adjustTimestamp(nowTimestamp, Calendar.SECOND, validTime));
            try {
                GenericValue smstGV = delegator.makeValue("SmsValidateCode", smsValidateCodeMap);
                smstGV.create();
            } catch (GenericEntityException e) {

                // return ServiceUtil.returnError(PeSendFailedError"));
            }

            // Send Message
            context.put("phone", teleNumber);
            context.put("code", captcha);
            context.put("product", "友评");
            sendTelMessage(dctx, context);
        }
        Map<String, Object> result = ServiceUtil.returnSuccess();
        Map<String, Object> inputMap = new HashMap<String, Object>();

        result.put("resultMap", inputMap);
        return result;
    }


    /**
     * Get Sms Property
     *
     * @param delegator
     */
    public static void getSmsProperty(Delegator delegator) {
        url = EntityUtilProperties.getPropertyValue("pe", "sms.url", delegator);
        appkey = EntityUtilProperties.getPropertyValue("pe", "sms.appkey", delegator);
        secret = EntityUtilProperties.getPropertyValue("pe", "sms.secret", delegator);
        smsFreeSignName = EntityUtilProperties.getPropertyValue("pe", "sms.smsFreeSignName", delegator);
        smsTemplateCode = EntityUtilProperties.getPropertyValue("pe", "sms.smsTemplateCode", delegator);
    }

    /**
     * Send Message
     *
     * @param dctx
     * @param context
     * @return
     */
    public static Map<String, Object> sendTelMessage(DispatchContext dctx, Map<String, Object> context) {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();

        // Scope Param
        Locale locale = (Locale) context.get("locale");
        String phone = (String) context.get("phone");
        String code = (String) context.get("code");
        String product = (String) context.get("product");
        String smsTemplateCode = (String) context.get("smsTemplateCode");

        if (null == smsTemplateCode || smsTemplateCode.trim().equals("")) {
            smsTemplateCode = "SMS_53800008";
        }

        // Initial Message Config
        getSmsProperty(delegator);

        // 暂时先写死
//		TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", "23654770",
//				"9c58a5fa366e2aabd8a62363c4c228c6");
        TaobaoClient client = new DefaultTaobaoClient("http://gw.api.taobao.com/router/rest", "23691976",
                "6d86dc0f2c5b3eb893142db3bbcaf2d2");

        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        req.setExtend("");
        req.setSmsType("normal");
        //req.setSmsFreeSignName(smsFreeSignName);
        req.setSmsFreeSignName("友评");

        String json = "{\"code\":\"" + code + "\",\"product\":\"" + "友评" + "\"" + "}";
        org.apache.ofbiz.base.util.Debug.logInfo(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> JSON=" + json, module);
        req.setSmsParamString(json);
        req.setRecNum(phone);
        //req.setSmsTemplateCode(smsTemplateCode);
        req.setSmsTemplateCode(smsTemplateCode);

        AlibabaAliqinFcSmsNumSendResponse rsp = null;
        try {
            rsp = client.execute(req);
        } catch (ApiException e) {

        }
        if (rsp != null && !rsp.isSuccess()) {
            org.apache.ofbiz.base.util.Debug
                    .logInfo("something wrong when send the short message, response body:" + rsp.getBody(), module);
        }

        // Service Foot
        Map<String, Object> result = ServiceUtil.returnSuccess();
        Map<String, Object> inputMap = new HashMap<String, Object>();

        result.put("resultMap", inputMap);
        return result;
    }


    // 导入exlSKU
    public static String ProductUploadImport(HttpServletRequest request, HttpServletResponse response) throws IOException, FileUploadException, InvalidFormatException, GenericEntityException, GenericServiceException {

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
        HttpSession session = request.getSession();
        GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");

        FileItem fileItem = getFileItem(request);
        String fileName = fileItem.getName();
        List<String[]> excelList = excelToList(fileItem);

        GenericValue admin = EntityQuery.use(delegator).from("UserLogin").where("userLoginId", "admin").queryFirst();
        try {

            String prodCatalogId = "ANKORAU_RETAIL";
            GenericValue prodCatalogCategory = EntityQuery.use(delegator).from("ProdCatalogCategory").where("prodCatalogId", prodCatalogId).queryFirst();
            String productCategoryId = (String) prodCatalogCategory.get("productCategoryId");
            GenericValue productStoreCatalog = EntityQuery.use(delegator).from("ProductStoreCatalog").where("prodCatalogId", prodCatalogId).queryFirst();
            String productStoreId = (String) productStoreCatalog.get("productStoreId");
            GenericValue productStore = EntityQuery.use(delegator).from("ProductStore").where("productStoreId", productStoreId).queryFirst();

            for (int i = 0; i < excelList.size(); i++) {
                TransactionUtil.setTransactionTimeout(100000);
                TransactionUtil.begin();
                //品牌-商品名称-款号-色号-颜色说明-尺码-吊牌价-详细尺寸-商品编码-商品描述-条码SKU-特别提醒-洗涤方式-上市年份+季节-组别-备注

                String[] excelRow = excelList.get(i);

                String payToPartyId = (String) productStore.get("payToPartyId");
                String brandName = excelRow[1];
                String productVirtualId = excelRow[2];
                String internalName = excelRow[1];
                String colorId = excelRow[3];
                String colorDesc = excelRow[4];

                String sizeId = excelRow[5];
                String sizeDesc = excelRow[7];
                String listPrice = excelRow[6];
                String productId = excelRow[8];
                String ean = excelRow[10];

                String otherDesc = excelRow[12];
                String keyword = excelRow[14];


                //导入的表存在虚拟产品Id
                if (UtilValidate.isNotEmpty(productVirtualId)) {
                    GenericValue productVirtual = EntityQuery.use(delegator).from("Product").where("productId", productVirtualId).queryFirst();
                    if (UtilValidate.isEmpty(productVirtual)) {
                        GenericValue newVirtualProduct = delegator.makeValue("Product", UtilMisc.toMap("productId", productVirtualId));
                        newVirtualProduct.set("productTypeId", "FINISHED_GOOD");
                        newVirtualProduct.set("description", otherDesc);
                        newVirtualProduct.set("comments", keyword);
                        //默认图片
                        newVirtualProduct.set("detailImageUrl", "https://personerp.oss-cn-hangzhou.aliyuncs.com/datas/serviceSales/DEFAULT_PRODUCT.jpg");
                        if (UtilValidate.isNotEmpty(internalName)) {
                            newVirtualProduct.set("internalName", internalName);
                            newVirtualProduct.set("productName", internalName);
                        }
                        newVirtualProduct.set("isVirtual", "Y");
                        newVirtualProduct.set("isVariant", "N");
                        newVirtualProduct.create();
                    }
                    //创建变形产品
                    GenericValue productVariant = delegator.findOne("Product", UtilMisc.toMap("productId", productId), false);
                    if (UtilValidate.isEmpty(productVariant)) {
                        GenericValue newVariantProduct = delegator.makeValue("Product", UtilMisc.toMap("productId", productId));
                        newVariantProduct.set("productTypeId", "FINISHED_GOOD");
                        newVariantProduct.set("description", otherDesc);
                        newVariantProduct.set("comments", keyword);
                        //默认图片
                        newVariantProduct.set("detailImageUrl", "https://personerp.oss-cn-hangzhou.aliyuncs.com/datas/serviceSales/DEFAULT_PRODUCT.jpg");
                        if (UtilValidate.isNotEmpty(internalName)) {
                            newVariantProduct.set("internalName", internalName);
                            newVariantProduct.set("productName", internalName);
                        }
                        newVariantProduct.set("isVirtual", "N");
                        newVariantProduct.set("isVariant", "Y");
                        newVariantProduct.set("virtualVariantMethodEnum", "VV_FEATURETREE");
                        newVariantProduct.create();

                    }
                    //创建产品关联
                    GenericValue productAssoc = EntityQuery.use(delegator).from("ProductAssoc").where("productId", productVirtualId, "productIdTo", productId, "productAssocTypeId", "PRODUCT_VARIANT").queryFirst();
                    if (UtilValidate.isEmpty(productAssoc)) {
                        GenericValue newAssoc = delegator.makeValue("ProductAssoc", UtilMisc.toMap("productId", productVirtualId, "productIdTo", productId, "productAssocTypeId", "PRODUCT_VARIANT", "fromDate", UtilDateTime.nowTimestamp()));
                        newAssoc.create();
                    }

                    //创建吊牌价
                    if (UtilValidate.isNotEmpty(listPrice)) {
                        GenericValue newProductVariantPrice = delegator.makeValue("ProductPrice", UtilMisc.toMap("productId", productId, "productPriceTypeId", "LIST_PRICE", "productPricePurposeId", "PURCHASE", "currencyUomId", "CNY", "productStoreGroupId", "_NA_", "fromDate", UtilDateTime.nowTimestamp()));
                        newProductVariantPrice.set("price", new BigDecimal(listPrice));
                        newProductVariantPrice.create();
                    }

                    //创建条形码
                    if (UtilValidate.isNotEmpty(ean)) {
                        GenericValue goodIdentification = EntityQuery.use(delegator).from("GoodIdentification").where("productId", productId, "goodIdentificationTypeId", "EAN").queryFirst();
                        if (UtilValidate.isEmpty(goodIdentification)) {
                            GenericValue newGoodIdentification = delegator.makeValue("GoodIdentification", UtilMisc.toMap("productId", productId, "goodIdentificationTypeId", "EAN"));
                            newGoodIdentification.set("idValue", ean);
                            newGoodIdentification.create();
                        }
                    }


                    //dispatcher.runSync("createProductKeyword",UtilMisc.toMap("userLogin",admin,"productId",productId,"keywordTypeId","KWT_KEYWORD","keyword",keyword));


                    //找到仓库
                    GenericValue facility = EntityQuery.use(delegator).from("Facility").where("ownerPartyId", payToPartyId).queryFirst();

                    //为产品创建库存量
                    Map<String, Object> receiveInventoryProductIn = UtilMisc.toMap("userLogin", userLogin,
                            "facilityId", (String) facility.get("facilityId"),
                            "inventoryItemTypeId", PeConstant.DEFAULT_INV_ITEM,
                            "productId", productId,
                            "description ", "卖家发布产品时的录入库存",
                            "quantityAccepted", new BigDecimal("15"),
                            "quantityRejected", BigDecimal.ZERO,
                            "unitCost", new BigDecimal(listPrice),
                            "ownerPartyId", payToPartyId,
                            "partyId", payToPartyId,
                            "uomId", PeConstant.DEFAULT_CURRENCY_UOM_ID,
                            "currencyUomId", PeConstant.DEFAULT_CURRENCY_UOM_ID);

                    Map<String, Object> receiveInventoryProductOut = dispatcher.runSync("receiveInventoryProduct", receiveInventoryProductIn
                    );
                    if (!ServiceUtil.isSuccess(receiveInventoryProductOut)) {
                        Debug.logError("*Mother Fuck Receive Inventory Product Error:" + receiveInventoryProductOut, module);
                        //return receiveInventoryProductOut;
                        return "error";
                    }

                    //SKU关联分类
                    Map<String, Object> addProductToCategoryInMap = new HashMap<String, Object>();
                    addProductToCategoryInMap.put("userLogin", admin);
                    addProductToCategoryInMap.put("productId", productId);
                    addProductToCategoryInMap.put("productCategoryId", productCategoryId);
                    Map<String, Object> addProductToCategoryServiceResultMap = dispatcher.runSync("addProductToCategory", addProductToCategoryInMap);
                    if (!ServiceUtil.isSuccess(addProductToCategoryServiceResultMap)) {
                        Debug.logError("*Mother Fuck added Product To Category Error:" + addProductToCategoryServiceResultMap, module);
                        // return addProductToCategoryServiceResultMap;
                        return "error";
                    }

//                    //虚拟产品关联分类

                    GenericValue productCategoryMember = EntityQuery.use(delegator).from("ProductCategoryMember").where("productId", productVirtualId,"productCategoryId",productCategoryId).queryFirst();

                    if (UtilValidate.isEmpty(productCategoryMember)) {
                        GenericValue newProductCategoryMember = delegator.makeValue("ProductCategoryMember", UtilMisc.toMap("productId", productVirtualId,"productCategoryId",productCategoryId,"fromDate", UtilDateTime.nowTimestamp()));
                        newProductCategoryMember.create();
                    }


//TODO 开始搞特征 -------------------------------------------------------------------------------------------------------------------------


                    //创建颜色特征
                    if (UtilValidate.isNotEmpty(colorId)) {


                        GenericValue productColorFeature = EntityQuery.use(delegator).from("ProductFeature").where("idCode",colorDesc, "productFeatureTypeId", "COLOR", "productFeatureCategoryId", "PRODUCT_COLOR").queryFirst();
                        String featureId = "";
                        //没找到这个特征
                        if (!UtilValidate.isNotEmpty(productColorFeature)) {
                            //创建该特征
                            Map<String, Object> createProductFetureMap = dispatcher.runSync("createProductFeatureInertPk", UtilMisc.toMap("idCode",colorDesc,"productFeatureId","COLOR_" + colorId, "productFeatureCategoryId", "PRODUCT_COLOR", "productFeatureTypeId", "COLOR", "description", colorDesc));
                            featureId = (String) createProductFetureMap.get("productFeatureId");
//                            GenericValue newProductFeture = delegator.makeValue("ProductFeature", UtilMisc.toMap("productFeatureId","COLOR_" + colorId,"productFeatureCategoryId", "PRODUCT_COLOR", "productFeatureTypeId", "COLOR", "description", colorDesc));
//                            newProductFeture.create();
                        } else {
                            featureId = (String) productColorFeature.get("featureId");
                        }


                        //创建 虚拟 颜色特征
                        GenericValue productVirtualColorFeatureAppl = EntityQuery.use(delegator).from("ProductFeatureAppl").where("productId", productVirtualId, "productFeatureId", featureId).queryFirst();

                        if (UtilValidate.isEmpty(productVirtualColorFeatureAppl)) {
                            GenericValue newVirtualProductColorFeatureAppl = delegator.makeValue("ProductFeatureAppl", UtilMisc.toMap("productId", productVirtualId, "productFeatureId", featureId, "fromDate", UtilDateTime.nowTimestamp()));
                            newVirtualProductColorFeatureAppl.set("productFeatureApplTypeId", "SELECTABLE_FEATURE");
                            newVirtualProductColorFeatureAppl.create();
                        }
                        //创建 变形 颜色特征
                        GenericValue productVariantColorFeatureAppl = EntityQuery.use(delegator).from("ProductFeatureAppl").where("productId", productId, "productFeatureId", featureId).queryFirst();
                        if (UtilValidate.isEmpty(productVariantColorFeatureAppl)) {
                            GenericValue newVariantProductColorFeatureAppl = delegator.makeValue("ProductFeatureAppl", UtilMisc.toMap("productId", productId, "productFeatureId", featureId, "fromDate", UtilDateTime.nowTimestamp()));
                            newVariantProductColorFeatureAppl.set("productFeatureApplTypeId", "STANDARD_FEATURE");
                            newVariantProductColorFeatureAppl.create();
                        }
                        //颜色特征模块结束
                    }


                    //创建尺码特征
                    if (UtilValidate.isNotEmpty(sizeId)) {

                        GenericValue productColorFeature = EntityQuery.use(delegator).from("ProductFeature").where("idCode",sizeDesc, "productFeatureTypeId", "SIZE", "productFeatureCategoryId", "PRODUCT_SIZE").queryFirst();
                        String featureId = "";
                        //没找到这个特征
                        if (!UtilValidate.isNotEmpty(productColorFeature)) {
                            //创建该特征
                            Map<String, Object> createProductFetureMap = dispatcher.runSync("createProductFeatureInertPk", UtilMisc.toMap("productFeatureId","SIZE_" + sizeId,  "productFeatureCategoryId", "PRODUCT_SIZE", "productFeatureTypeId", "SIZE", "description", sizeId,"idCode",sizeDesc));
                            featureId = (String) createProductFetureMap.get("productFeatureId");
//                            GenericValue newProductFeture = delegator.makeValue("ProductFeature", UtilMisc.toMap("productFeatureId","SIZE_" + sizeId,  "productFeatureCategoryId", "PRODUCT_SIZE", "productFeatureTypeId", "SIZE", "description", sizeId,"idCode",sizeDesc));
//                            newProductFeture.create();
                        } else {
                            featureId = (String) productColorFeature.get("featureId");
                        }


                        //创建 虚拟 尺码特征
                        GenericValue productVirtualSizeFeatureAppl = EntityQuery.use(delegator).from("ProductFeatureAppl").where("productId", productVirtualId, "productFeatureId", featureId).queryFirst();
                        if (UtilValidate.isEmpty(productVirtualSizeFeatureAppl)) {
                            GenericValue newVirtualProductSizeFeatureAppl = delegator.makeValue("ProductFeatureAppl", UtilMisc.toMap("productId", productVirtualId, "productFeatureId", featureId, "fromDate", UtilDateTime.nowTimestamp()));
                            newVirtualProductSizeFeatureAppl.set("productFeatureApplTypeId", "SELECTABLE_FEATURE");
                            newVirtualProductSizeFeatureAppl.create();
                        }
                        //创建 变形 尺码特征
                        GenericValue productVariantSizeFeatureAppl = EntityQuery.use(delegator).from("ProductFeatureAppl").where("productId", productId, "productFeatureId", featureId).queryFirst();

                        if (UtilValidate.isEmpty(productVariantSizeFeatureAppl)) {
                            GenericValue newVariantProductSizeFeatureAppl = delegator.makeValue("ProductFeatureAppl", UtilMisc.toMap("productId", productId, "productFeatureId", featureId, "fromDate", UtilDateTime.nowTimestamp()));
                            newVariantProductSizeFeatureAppl.set("productFeatureApplTypeId", "STANDARD_FEATURE");
                            newVariantProductSizeFeatureAppl.create();
                        }


                        //尺码特征结束
                    }


                    //判断是否存在导入的虚拟产品ID结构结束

                }
                TransactionUtil.commit();
                //循环结束
            }

        } catch (Exception e) {
            try {
                TransactionUtil.rollback();
            } catch (GenericTransactionException e1) {
                e1.printStackTrace();
            }
            Debug.logError(e, e.getMessage(), module);
            request.setAttribute("_ERROR_MESSAGE_", e.getMessage());
            return "error";
        }
        return "success";
    }


    //得到fileItem
    public static FileItem getFileItem(HttpServletRequest request) throws FileUploadException, IOException {
        FileItem fileItem = null;
        DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
        diskFileItemFactory.setSizeThreshold(4096);
        ServletFileUpload uploadFileServlet = new ServletFileUpload(diskFileItemFactory);
        // -1代表无限制
        uploadFileServlet.setSizeMax(-1);
        uploadFileServlet.setHeaderEncoding("utf-8");
        List<FileItem> fileItems = uploadFileServlet.parseRequest(request);
        for (FileItem fi : fileItems) {
            if (!fi.isFormField()) {
                fileItem = fi;
            }
        }
        return fileItem;
    }


    @SuppressWarnings("unused")
    //将Excel转换成List
    public static List<String[]> excelToList(FileItem fileItem) throws IOException, InvalidFormatException {
        InputStream is = new ByteArrayInputStream(fileItem.get());

        //XSSFWorkbook workbook = new XSSFWorkbook(is);
        Workbook wb = WorkbookFactory.create(is);
        // 得到exl表对象
        //XSSFSheet sheet = wb.getSheetAt(0);

        Sheet sheet = wb.getSheetAt(0);

        List<String[]> excelList = new ArrayList<String[]>();
        // 初始值
        int firstRowIndex = sheet.getFirstRowNum();
        // 最大值
        int lastRowNum = sheet.getLastRowNum();
        // 从1开始去掉表头
        for (int i = 1; i <= lastRowNum; i++) {
            // 获得一列
            Row row = sheet.getRow(i);
            if (row != null) {
                //
                int tdLength = row.getLastCellNum();
                String[] excelRow = new String[tdLength];

                for (int j = 0; j <= tdLength; j++) {
                    // 当前列值
                    Cell cell = row.getCell(j);
                    if (cell == null) {
                        break;
                    }
                    if ("".equals(cell.toString().trim())) {
                        break;
                    }
                    excelRow[j] = cell.toString().trim();
                }
                excelList.add(excelRow);
            }
        }
        return excelList;
    }


    /**
     * importProductToComp(商户导入产品)
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> importProductToComp(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException, Exception {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();

        Delegator delegator = dispatcher.getDelegator();

        Locale locale = (Locale) context.get("locale");

        Map<String, Object> result = ServiceUtil.returnSuccess();

        TestExcel.showExcel();

        return result;
    }

    /**
     * cleanSessionMessage
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> cleanSessionMessage(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();

        Delegator delegator = dispatcher.getDelegator();

        Locale locale = (Locale) context.get("locale");

        Map<String, Object> result = ServiceUtil.returnSuccess();

        GenericValue userLogin = (GenericValue) context.get("userLogin");

        String partyId = (String) userLogin.get("partyId");

        // Admin Do Run Service


        String partyIdTo = (String) context.get("partyIdTo");

        EntityCondition findConditions3 = EntityCondition
                .makeCondition(UtilMisc.toMap("partyIdTo", partyIdTo));


        EntityCondition findConditions4 = EntityCondition
                .makeCondition(UtilMisc.toMap("partyIdFrom", partyIdTo));

        EntityCondition listConditions2 = EntityCondition
                .makeCondition(findConditions3, EntityOperator.OR, findConditions4);

        EntityCondition findConditions = EntityCondition
                .makeCondition(UtilMisc.toMap("partyIdTo", partyId));


        EntityCondition findConditions2 = EntityCondition
                .makeCondition(UtilMisc.toMap("partyIdFrom", partyId));

        EntityCondition listConditions = EntityCondition
                .makeCondition(findConditions, EntityOperator.OR, findConditions2);

        EntityCondition findConditions5 = EntityCondition
                .makeCondition("badge", EntityOperator.EQUALS, "true");

        EntityCondition listBigConditions = EntityCondition
                .makeCondition(listConditions, listConditions2, findConditions5);
        Set<String> fieldSet = new HashSet<String>();
        fieldSet.add("message");
        fieldSet.add("partyIdFrom");
        fieldSet.add("partyIdTo");
        fieldSet.add("objectId");
        fieldSet.add("messageId");
        fieldSet.add("fromDate");

        fieldSet.add("messageLogTypeId");
        List<GenericValue> queryMessageList = delegator.findList("MessageLog",
                listBigConditions, fieldSet,
                null, null, false);

        for (GenericValue gv : queryMessageList) {
            gv.set("badge", "false");
            gv.store();

        }


        return result;
    }


    /**
     * 推送 服务类型
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> pushMessage(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();

        Delegator delegator = dispatcher.getDelegator();

        Locale locale = (Locale) context.get("locale");

        Map<String, Object> result = ServiceUtil.returnSuccess();

        GenericValue userLogin = (GenericValue) context.get("userLogin");


        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        String text = (String) context.get("text");


        String tarjeta = (String) context.get("tarjeta");

        String objectId = (String) context.get("objectId");

        String partyIdTo = (String) context.get("partyIdTo");

        String partyIdFrom = (String) context.get("partyIdFrom");

        Map<String, Object> pushWeChatMessageInfoMap = new HashMap<String, Object>();
        Map<String, Object> createMessageLogMap = new HashMap<String, Object>();

        //推送的不是图片,只要普通推送
        PersonManagerServices.pushMsgBase(objectId, partyIdFrom, partyIdTo, delegator, dispatcher, userLogin, text, pushWeChatMessageInfoMap, admin, createMessageLogMap, "TEXT");


        return result;
    }

    /**
     * createSimpleCarrierShipmentMethod(单独创建系统货运方式)
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> createSimpleCarrierShipmentMethod(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();

        Delegator delegator = dispatcher.getDelegator();

        Locale locale = (Locale) context.get("locale");

        Map<String, Object> result = ServiceUtil.returnSuccess();

        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));


        String carrierCode = (String) context.get("carrierCode");

        String name = (String) context.get("name");

        String code = (String) context.get("code");

        //Create PartyGroup
        dispatcher.runSync("createPartyGroup", UtilMisc.toMap("userLogin", admin, "partyId", carrierCode, "groupName", name));
        //Create Role
        Map<String, Object> createPartyRoleMap = UtilMisc.toMap("userLogin", admin, "partyId", carrierCode,
                "roleTypeId", "CARRIER");

        dispatcher.runSync("createPartyRole", createPartyRoleMap);

        //Create CarrierShipmentMethod
        dispatcher.runSync("createCarrierShipmentMethod", UtilMisc.toMap("userLogin", admin, "carrierServiceCode", code, "partyId", carrierCode, "roleTypeId", "CARRIER", "shipmentMethodTypeId", "EXPRESS", "sequenceNumber", new Long("10")));

        return result;
    }


    /**
     * 订单状态变更推送
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> pushOrderStatusInfo(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> result = ServiceUtil.returnSuccess();


        String openId = (String) context.get("openId");
        String orderId = (String) context.get("orderId");
        String date = (String) context.get("date");

        String payToPartyId = (String) context.get("payToPartyId");
        String tarjeta = (String) context.get("tarjeta");
        String messageInfo = (String) context.get("messageInfo");

        String jumpUrl = (String) context.get("jumpUrl");

        GenericValue orderHeader = delegator.findOne("OrderHeader", UtilMisc.toMap("orderId", orderId), false);


        String orderStatus = UtilProperties.getMessage(resourceUiLabels, orderHeader.get("statusId") + "", locale);


        // 发送模版消息
        AccessToken accessToken = getAccessToken(PeConstant.WECHAT_GZ_APP_ID, PeConstant.ACCESS_KEY_SECRET);
        String URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
        String url = URL.replace("ACCESS_TOKEN", accessToken.getToken());

        JSONObject jsobj1 = new JSONObject();
        JSONObject jsobj2 = new JSONObject();
        JSONObject jsobj3 = new JSONObject();
        JSONObject jsobj4 = new JSONObject();
        JSONObject jsobj5 = new JSONObject();
        JSONObject jsobj6 = new JSONObject();
        JSONObject jsobj7 = new JSONObject();
        JSONObject jsobj8 = new JSONObject();
        JSONObject jsobjminipro = new JSONObject();
//        jsobjminipro.put("appid","wx119831dae45a3f3f");
//        jsobjminipro.put("pagepath","pages/index/");
//        jsobj1.put("miniprogram",jsobjminipro);
//        String url2 = "http://www.yo-pe.com:3400/WebManager/control/miniChat?" +
//                "orderId="+orderId+"&payToPartyId=" +
//                ""+payToPartyId+"&tarjeta="+tarjeta+"&payToPartyHead="+personInfoMap.get("headPortrait")+"&payToPartyFirstName="+personInfoMap.get("firstName");
//        System.out.println("*============================================================URL = " + url2);


        jsobj1.put("touser", openId);
        jsobj1.put("template_id", "akqWpgJdI14Hm6vaisBd_-UfkzIInu_P-8l4FaNCHkU");
        jsobj1.put("url", jumpUrl);


        jsobjminipro.put("appid", "wx299644ef4c9afbde");
        jsobjminipro.put("pagepath", "pages/orderDetail/orderDetail?unioId=" + openId + "&orderId=" + orderId);
        jsobj1.put("miniprogram", jsobjminipro);

        jsobj3.put("value", "订单状态更新啦!");
        jsobj3.put("color", "#173177");
        jsobj2.put("first", jsobj3);

        jsobj4.put("value", orderId);
        jsobj4.put("color", "#173177");
        jsobj2.put("keyword1", jsobj4);


        if (orderStatus.toLowerCase().equals("created")) {
            orderStatus = "订单已创建";
            // messageInfo= personInfoMap.get("firstName")+"正在处理您的订单";
        } else {
            orderStatus = "订单已发货";
            // messageInfo= "物流公司:"+"物流单号:";
        }


        jsobj5.put("value", orderStatus);
        jsobj5.put("color", "#173177");
        jsobj2.put("keyword2", jsobj5);

        jsobj6.put("value", messageInfo);
        jsobj6.put("color", "#173177");
        jsobj2.put("keyword3", jsobj6);

        jsobj7.put("value", date);
        jsobj7.put("color", "#173177");
        jsobj2.put("keyword4", jsobj7);

        jsobj8.put("value", "友评平台竭诚为您服务^_^");
        jsobj8.put("color", "#173177");
        jsobj2.put("remark", jsobj8);

        jsobj1.put("data", jsobj2);


        WeChatUtil.PostSendMsg(jsobj1, url);

        return result;
    }


    /**
     * pushMiniProgramMessageInfo 推送小程序信息
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> pushMiniProgramMessageInfo(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> result = ServiceUtil.returnSuccess();
        System.out.println("*pushMiniProgramMessageInfo============================================================");

        String openId = (String) context.get("openId");
        String formid = (String) context.get("formId");


        // 发送模版消息
        AccessToken accessToken = getAccessToken(PeConstant.WECHAT_MINI_PROGRAM_APP_ID, PeConstant.WECHAT_MINI_PROGRAM_APP_SECRET_ID);
        String URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
        String url = URL.replace("ACCESS_TOKEN", accessToken.getToken());
        System.out.println("token = " + accessToken.getToken());
        System.out.println("URL = " + URL);
        JSONObject jsobj1 = new JSONObject();
        JSONObject jsobj2 = new JSONObject();
        JSONObject jsobj3 = new JSONObject();
        JSONObject jsobj4 = new JSONObject();
        JSONObject jsobj5 = new JSONObject();
        JSONObject jsobj6 = new JSONObject();
        JSONObject jsobj7 = new JSONObject();
        JSONObject bigJson = new JSONObject();

//        订单号
//        {{keyword1.DATA}}
//        金额
//        {{keyword2.DATA}}
//        商品名称
//        {{keyword3.DATA}}
//        订单内容
//        {{keyword4.DATA}}
//        用户姓名
//        {{keyword5.DATA}}
//        订单状态
//        {{keyword6.DATA}}

        jsobj1.put("value", "123456");
        jsobj3.put("value", "123456");
        jsobj4.put("value", "123456");
        jsobj5.put("value", "123456");
        jsobj6.put("value", "123456");
        jsobj7.put("value", "123456");


        jsobj2.put("keyword1", jsobj1);
        jsobj2.put("keyword2", jsobj3);
        jsobj2.put("keyword3", jsobj4);
        jsobj2.put("keyword4", jsobj5);
        jsobj2.put("keyword5", jsobj6);
        jsobj2.put("keyword6", jsobj7);


        bigJson.put("data", jsobj2);
        bigJson.put("touser", openId);
        bigJson.put("template_id", "cRmXGHl1f0BHKn8KPe62Y7XQmP5QM3cxQLP6B9HgzRI");
        bigJson.put("page", "pages/order/order");
        bigJson.put("form_id", formid);
        //  bigJson.put("emphasis_keyword","keyword1.DATA");

        WeChatUtil.PostSendMsg(bigJson, url);

        return result;
    }

    /**
     * Push WeChat MessageInfo
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> pushWeChatMessageInfo(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> result = ServiceUtil.returnSuccess();
        System.out.println("*pushWeChatMessageInfo============================================================");
        String partyIdFrom = (String) context.get("partyIdFrom");
        String openId = (String) context.get("openId");
        String productId = (String) context.get("productId");
        String date = (String) context.get("date");
        String message = (String) context.get("message");
        String firstName = (String) context.get("firstName");
        String payToPartyId = (String) context.get("payToPartyId");
        String tarjeta = (String) context.get("tarjeta");
        String jumpUrl = (String) context.get("url");


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
        String url2 = "";
        if (jumpUrl != null && !jumpUrl.trim().equals("")) {
            url2 = jumpUrl;
        } else {
            url2 = "http://www.yo-pe.com:3400/WebManager/control/miniChat?" +
                    "productId=" + productId + "&payToPartyId=" +
                    "" + payToPartyId + "&tarjeta=" + tarjeta + "&payToPartyHead=" + personInfoMap.get("headPortrait") + "&payToPartyFirstName=" + personInfoMap.get("firstName");
            System.out.println("*============================================================URL = " + url2);
        }


        jsobj1.put("touser", openId);
        jsobj1.put("template_id", "aFCzhfNrWb0GsEr0ZCVuijLPAQ6cPzPedORxyKHBzbs");
        //增加SPM逻辑
        jsobj1.put("url", "https://www.yo-pe.com/pejump/" + partyIdFrom + "/" + partyIdFrom + "111" + "/" + payToPartyId + "/" + productId + "/NA");

        JSONObject jsobjminipro = new JSONObject();
        jsobjminipro.put("appid", "wx299644ef4c9afbde");
        jsobjminipro.put("pagepath", "pages/chatView/chatView?username=" + payToPartyId + "&password=" + payToPartyId + "111&payToPartyId=" + partyIdFrom + "&productId=" + productId);
        jsobj1.put("miniprogram", jsobjminipro);

        System.out.println("pages/chatView/chatView?username=" + payToPartyId + "&password=" + payToPartyId + "111&payToPartyId=" + partyIdFrom + "&productId=" + productId);


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

        return result;
    }


    /**
     * send Message
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> sendMessage(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        GenericValue userLogin = (GenericValue) context.get("userLogin");

        String partyIdFrom = (String) userLogin.get("partyId");
        String partyIdTo = (String) context.get("partyIdTo");
        String message = (String) context.get("message");
        String orderId = (String) context.get("orderId");

        if (partyIdFrom.equals(partyIdTo)) {
            //TODO IF EQUALS , PARTY ID TO  = CUSTOMER
            GenericValue orderMap = EntityQuery.use(delegator).from("OrderHeaderItemAndRoles").where("orderId", orderId).queryFirst();
            partyIdTo = (String) orderMap.get("partyId");
        }


        Map<String, Object> createMessageLogMap = new HashMap<String, Object>();

        createMessageLogMap.put("partyIdFrom", partyIdFrom);
        createMessageLogMap.put("messageId", delegator.getNextSeqId("MessageLog"));
        createMessageLogMap.put("partyIdTo", partyIdTo);
        createMessageLogMap.put("message", message);
        createMessageLogMap.put("fromDate", org.apache.ofbiz.base.util.UtilDateTime.nowTimestamp());
        createMessageLogMap.put("messageLogTypeId", "TEXT");
        GenericValue msg = delegator.makeValue("MessageLog", createMessageLogMap);
        msg.create();


        // 查询registrationID
        EntityCondition pConditions = EntityCondition.makeCondition("partyId", partyIdTo);
        List<EntityCondition> devTypeExprs = new ArrayList<EntityCondition>();
        devTypeExprs.add(EntityCondition.makeCondition("partyIdentificationTypeId", "JPUSH_ANDROID"));
        devTypeExprs.add(EntityCondition.makeCondition("partyIdentificationTypeId", "JPUSH_IOS"));
        EntityCondition devCondition = EntityCondition.makeCondition(devTypeExprs, EntityOperator.OR);
        pConditions = EntityCondition.makeCondition(pConditions, devCondition);

        List<GenericValue> partyIdentifications = delegator.findList("PartyIdentification", pConditions, null, UtilMisc.toList("-createdStamp"), null, false);


        GenericValue partyIdentification = (GenericValue) partyIdentifications.get(0);
        String regId = (String) partyIdentification.getString("idValue");
        String partyIdentificationTypeId = (String) partyIdentification.get("partyIdentificationTypeId");
        Map<String, Object> result = ServiceUtil.returnSuccess();
        result.put("regId", regId);
        GenericValue person = delegator.findOne("Person", UtilMisc.toMap("partyId", partyIdFrom), false);
        result.put("partyIdFrom", person.get("firstName") + ":" + message);
        result.put("partyIdTo", partyIdFrom);
        result.put("deviceType", partyIdentificationTypeId);

        result.put("message", "message:" + partyIdTo + ":" + partyIdFrom + ":" + orderId + "");

        return result;
    }


    /**
     * sendAppAnd WeChatMessage
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> sendAppAndWeChatMessage(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> result = ServiceUtil.returnSuccess();

        String partyIdFrom = (String) context.get("partyIdFrom");
        String partyIdTo = (String) context.get("partyIdTo");
        String message = (String) context.get("message");
        String orderId = (String) context.get("orderId");


        if (partyIdFrom.equals(partyIdTo)) {
//            //TODO IF EQUALS , PARTY ID TO  = CUSTOMER
//            GenericValue orderMap = EntityQuery.use(delegator).from("OrderHeaderItemAndRoles").where("orderId",orderId).queryFirst();
//            partyIdTo = (String) orderMap.get("partyId");
            return result;
        }


        Map<String, Object> createMessageLogMap = new HashMap<String, Object>();

        createMessageLogMap.put("partyIdFrom", partyIdFrom);
        createMessageLogMap.put("messageId", delegator.getNextSeqId("MessageLog"));
        createMessageLogMap.put("partyIdTo", partyIdTo);
        createMessageLogMap.put("message", message);
        createMessageLogMap.put("fromDate", org.apache.ofbiz.base.util.UtilDateTime.nowTimestamp());

        createMessageLogMap.put("messageLogTypeId", "TEXT");

        GenericValue msg = delegator.makeValue("MessageLog", createMessageLogMap);
        msg.create();


        // 查询registrationID
        EntityCondition pConditions = EntityCondition.makeCondition("partyId", partyIdTo);
        List<EntityCondition> devTypeExprs = new ArrayList<EntityCondition>();
        devTypeExprs.add(EntityCondition.makeCondition("partyIdentificationTypeId", "JPUSH_ANDROID"));
        devTypeExprs.add(EntityCondition.makeCondition("partyIdentificationTypeId", "JPUSH_IOS"));
        EntityCondition devCondition = EntityCondition.makeCondition(devTypeExprs, EntityOperator.OR);
        pConditions = EntityCondition.makeCondition(pConditions, devCondition);

        List<GenericValue> partyIdentifications = delegator.findList("PartyIdentification", pConditions, null, UtilMisc.toList("-createdStamp"), null, false);


        GenericValue partyIdentification = (GenericValue) partyIdentifications.get(0);
        String regId = (String) partyIdentification.getString("idValue");
        String partyIdentificationTypeId = (String) partyIdentification.get("partyIdentificationTypeId");

        result.put("regId", regId);
        GenericValue person = delegator.findOne("Person", UtilMisc.toMap("partyId", partyIdFrom), false);
        result.put("partyIdFrom", person.get("firstName") + ":" + message);
        result.put("partyIdTo", partyIdFrom);
        result.put("deviceType", partyIdentificationTypeId);

        result.put("message", "message:" + partyIdTo + ":" + partyIdFrom + ":" + orderId + "");


        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));


        List<GenericValue> partyIdentificationList = EntityQuery.use(delegator).from("PartyIdentification").where("partyId", partyIdTo, "partyIdentificationTypeId", "WX_GZ_OPEN_ID").queryList();

        if (null != partyIdentificationList && partyIdentificationList.size() > 0) {

            String openId = (String) partyIdentificationList.get(0).get("idValue");

            Map<String, Object> pushWeChatMessageInfoMap = new HashMap<String, Object>();


            pushWeChatMessageInfoMap.put("message", message);

            pushWeChatMessageInfoMap.put("userLogin", admin);

            System.out.println("*PUSH WE CHAT GONG ZHONG PLATFORM !!!!!!!!!!!!!!!!!!!!!!!");

            Date date = new Date();

            SimpleDateFormat formatter;

            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String pushDate = "" + formatter.format(date);

            pushWeChatMessageInfoMap.put("date", pushDate);

            pushWeChatMessageInfoMap.put("openId", openId);

            pushWeChatMessageInfoMap.put("productId", "");

            pushWeChatMessageInfoMap.put("payToPartyId", partyIdTo);

            pushWeChatMessageInfoMap.put("url", "");

            //推微信
            dispatcher.runSync("pushWeChatMessageInfo", pushWeChatMessageInfoMap);

        }


        // 不用Ecs 推送App
        Map<String, Object> pushNotifOrMessageInfoMap = new HashMap<String, Object>();
        pushNotifOrMessageInfoMap.put("message", message);
        pushNotifOrMessageInfoMap.put("productId", "10000");
        pushNotifOrMessageInfoMap.put("content", "");
        pushNotifOrMessageInfoMap.put("deviceType", partyIdentificationTypeId);
        pushNotifOrMessageInfoMap.put("objectId", "10000");
        pushNotifOrMessageInfoMap.put("regId", regId);
        pushNotifOrMessageInfoMap.put("sendType", "one");


        GenericValue userLogin = EntityQuery.use(delegator).from("UserLogin").where("partyId", partyIdFrom, "enabled", "Y").queryFirst();

        pushNotifOrMessageInfoMap.put("userLogin", userLogin);

        dispatcher.runSync("pushNotifOrMessage", pushNotifOrMessageInfoMap);


        return result;
    }


}
