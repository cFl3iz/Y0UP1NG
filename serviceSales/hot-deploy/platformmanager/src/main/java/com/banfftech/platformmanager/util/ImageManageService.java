package main.java.com.banfftech.platformmanager.util;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ofbiz.entity.util.EntityFindOptions;
import org.apache.ofbiz.entity.condition.EntityConditionList;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilDateTime;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.base.util.UtilTimer;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityUtil;
import org.apache.ofbiz.product.imagemanagement.ImageManagementServices;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;
import org.apache.ofbiz.entity.condition.EntityOperator;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import org.apache.ofbiz.entity.util.EntityQuery;


import org.apache.ofbiz.product.imagemanagement.ImageManagementServices;

/**
 * Created by S on 2018/5/9.
 */
public class ImageManageService {
    private static final String KEY = UtilProperties.getPropertyValue("aliyunoss.properties", "KEY");
    private static final String SECRET = UtilProperties.getPropertyValue("aliyunoss.properties", "SECRET");
    private static final String ENDPOINT = UtilProperties.getPropertyValue("aliyunoss.properties", "ENDPOINT"); //change it internal address on deploying
    private static final String BUCKET_NAME = UtilProperties.getPropertyValue("aliyunoss.properties", "BUCKET_NAME");
    private static final String ANKORAU_ROOT = UtilProperties.getPropertyValue("aliyunoss.properties", "ANKORAU_ROOT");
    private static final String IMG_DOMAIN_PREFIX = UtilProperties.getPropertyValue("aliyunoss.properties", "IMG_DOMAIN_PREFIX");
    private static final String ANKORAU_SINGLE_PATH = UtilProperties.getPropertyValue("aliyunoss.properties", "ANKORAU_SINGLE_PATH");
    private static final String ANKORAU_DETAIL_PATH = UtilProperties.getPropertyValue("aliyunoss.properties", "ANKORAU_DETAIL_PATH");
    private static final String ANKORAU_MATCH_PATH = UtilProperties.getPropertyValue("aliyunoss.properties", "ANKORAU_MATCH_PATH");

    public final static String module = ImageManageService.class.getName();

    /**
     * 初始化临时表
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     * @throws UnsupportedEncodingException
     */
    public static Map<String, Object> initialAllSkuPictureTemp(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException, UnsupportedEncodingException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));


        //单品图列表
        List<Map<String, String>> singleList = new ArrayList<Map<String, String>>();
        //产品详情图列表
        List<Map<String, String>> detailList = new ArrayList<Map<String, String>>();
        //产品模特图列表
        List<Map<String, String>> matchList = new ArrayList<Map<String, String>>();

        OSSClient client = new OSSClient(ENDPOINT, KEY, SECRET);
        // String path = new String(ANKORAU_PATH.getBytes("ISO-8859-1"),"GBK");
        String single_path = ANKORAU_SINGLE_PATH;
        String detail_path = ANKORAU_DETAIL_PATH;
        String match_path = ANKORAU_MATCH_PATH;
        // 构造ListObjectsRequest请求
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest(BUCKET_NAME, single_path, null, "/", 999);
        ObjectListing listing = client.listObjects(listObjectsRequest);
        // 遍历所有Object
        for (OSSObjectSummary objectSummary : listing.getObjectSummaries()) {
            if (objectSummary.getKey().indexOf(".") > -1) {
                String fileName = getFileName(single_path, objectSummary.getKey());
                fileName = fileName.substring(0, fileName.lastIndexOf(".jpg"));
                if (fileName.indexOf("W") > -1) {
                    fileName = fileName.replaceAll("W", "");
                }
                String skuId = "";


                GenericValue prodImg = delegator.findOne("ProductImagesTemp", UtilMisc.toMap("skuId", fileName, "contentType", "ZUCZUG_IMAGES_TYPE"), false);
                if (prodImg == null) {
                    GenericValue prodImgTemp = delegator.makeValue("ProductImagesTemp", UtilMisc.toMap("skuId", fileName, "contentType", "SINGLE_PRODUCT_IMAGE", "path", getFileUrl(objectSummary.getKey())));
                    prodImgTemp.create();
                } else {
                    prodImg.set("path", getFileUrl(objectSummary.getKey()));
                    prodImg.store();
                }


//                Map<String,String> map = new HashMap<String, String>();
//                map.put("fileName", getFileName(single_path,objectSummary.getKey()));
//                map.put("fileUrl",  getFileUrl(objectSummary.getKey()));
//                singleList.add(map);
            }
        }

        listObjectsRequest = new ListObjectsRequest(BUCKET_NAME, detail_path, null, "/", 999);
        listing = client.listObjects(listObjectsRequest);
        // 遍历所有Object
        for (OSSObjectSummary objectSummary : listing.getObjectSummaries()) {
            if (objectSummary.getKey().indexOf(".") > -1) {
                String fileName = getFileName(detail_path, objectSummary.getKey());
                fileName = fileName.substring(0, fileName.lastIndexOf(".jpg"));
                if (fileName.indexOf("W") > -1) {
                    fileName = fileName.replaceAll("W", "");
                }
                GenericValue prodImg = delegator.findOne("ProductImagesTemp", UtilMisc.toMap("skuId", fileName, "contentType", "DETAIL_PRODUCT_IMAGE"), false);
                if (prodImg == null) {
                    GenericValue prodImgTemp = delegator.makeValue("ProductImagesTemp", UtilMisc.toMap("skuId", fileName, "contentType", "DETAIL_PRODUCT_IMAGE", "path", getFileUrl(objectSummary.getKey())));
                    prodImgTemp.create();
                } else {
                    prodImg.set("path", getFileUrl(objectSummary.getKey()));
                    prodImg.store();
                }
//                Map<String,String> map = new HashMap<String, String>();
//                map.put("fileName", getFileName(single_path,objectSummary.getKey()));
//                map.put("fileUrl",  getFileUrl(objectSummary.getKey()));
//                detailList.add(map);
            }
        }


        listObjectsRequest = new ListObjectsRequest(BUCKET_NAME, match_path, null, "/", 999);
        listing = client.listObjects(listObjectsRequest);
        // 遍历所有Object
        for (OSSObjectSummary objectSummary : listing.getObjectSummaries()) {
            if (objectSummary.getKey().indexOf(".") > -1) {
                String fileName = getFileName(match_path, objectSummary.getKey());
                fileName = fileName.substring(0, fileName.lastIndexOf(".jpg"));
                if (fileName.indexOf("W") > -1) {
                    fileName = fileName.replaceAll("W", "");
                }
                GenericValue prodImg = delegator.findOne("ProductImagesTemp", UtilMisc.toMap("skuId", fileName, "contentType", "MATCH_PRODUCT_IMAGE"), false);
                if (prodImg == null) {
                    GenericValue prodImgTemp = delegator.makeValue("ProductImagesTemp", UtilMisc.toMap("skuId", fileName, "contentType", "MATCH_PRODUCT_IMAGE", "path", getFileUrl(objectSummary.getKey())));
                    prodImgTemp.create();
                } else {
                    prodImg.set("path", getFileUrl(objectSummary.getKey()));
                    prodImg.store();
                }

            }
        }


        return resultMap;
    }

    /**
     * 初始化所有Sku的图片
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> initialAllSkuPicture(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException, UnsupportedEncodingException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));
        String prodCatalogId = (String) context.get("prodCatalogId");

        GenericValue prodCatalogCategory = EntityQuery.use(delegator).from("ProdCatalogCategory").where("prodCatalogId", prodCatalogId).queryFirst();
        String productCategoryId = (String) prodCatalogCategory.get("productCategoryId");

        List<String> orderBy = UtilMisc.toList("-createdDate");
        List<GenericValue> myContactListPage = null;
        myContactListPage = EntityQuery.use(delegator).from("ProductCategoryMemberAndProdDetail").
                where("productCategoryId", productCategoryId, "isVirtual", "N").orderBy(orderBy).queryList();

        if (null != myContactListPage) {
            for (GenericValue gv : myContactListPage) {
                //自己就是sku
                String skuId = (String) gv.get("productId");
                //取款号+色号
                skuId = skuId.substring(0, skuId.lastIndexOf("-"));

                EntityCondition matchConditions = EntityCondition.makeCondition("skuId", EntityOperator.LIKE, skuId + "%");
                EntityCondition matchConditions3 = EntityCondition.makeCondition("contentType", EntityOperator.EQUALS, "MATCH_PRODUCT_IMAGE");
                EntityCondition matchCondition = EntityCondition.makeCondition(matchConditions, EntityOperator.AND, matchConditions3);
                List<GenericValue> productMatchPictures = delegator.findList("ProductImagesTemp",
                        matchCondition, null,
                        null, null, false);
//                EntityQuery.use(delegator).from("ProductImagesTemp").where("skuId",skuId,"contentType","MATCH_PRODUCT_IMAGE").queryList();
                if (null != productMatchPictures && productMatchPictures.size() > 0) {
                    //更新产品首图
                    for (GenericValue matchPicture : productMatchPictures) {
                        String path = matchPicture.getString("path");
                        if (productMatchPictures.size() == 1) {
                            setProductEntityImageField(delegator, (String) gv.get("productId"), path);
                        } else {
                            String rowSingId = matchPicture.getString("skuId");
                            if (rowSingId.indexOf("P") > -1) {

                                setProductEntityImageField(delegator, (String) gv.get("productId"), path);
                            }
                        }
                        String contentId = createNewContentForImage(dctx, path, admin);
                        if (null == EntityQuery.use(delegator).from("ProductContent").where("contentId", contentId, "productId", (String) gv.get("productId"), "productContentTypeId", "MATCH_PRODUCT_IMAGE").queryFirst()) {

                            Map<String, Object> productContentCtx = new HashMap<String, Object>();
                            productContentCtx.put("productId", (String) gv.get("productId"));
                            productContentCtx.put("productContentTypeId", "MATCH_PRODUCT_IMAGE");
                            productContentCtx.put("fromDate", UtilDateTime.nowTimestamp());
                            productContentCtx.put("userLogin", admin);
//                        productContentCtx.put("sequenceNum", Long.parseLong(sequenceNum));
                            productContentCtx.put("contentId", contentId);
                            productContentCtx.put("statusId", "IM_PENDING");
                            dispatcher.runSync("createProductContent", productContentCtx);
                        }
                    }

                }


                //更新细节图
                EntityCondition detailConditions = EntityCondition.makeCondition("skuId", EntityOperator.LIKE, skuId + "%");
                EntityCondition detailConditions3 = EntityCondition.makeCondition("contentType", EntityOperator.EQUALS, "DETAIL_PRODUCT_IMAGE");
                EntityCondition detailCondition = EntityCondition.makeCondition(detailConditions, EntityOperator.AND, detailConditions3);
                List<GenericValue> productDetailPictures = delegator.findList("ProductImagesTemp",
                        detailCondition, null,
                        null, null, false);
                if (null != productDetailPictures && productDetailPictures.size() > 0) {
                    for (GenericValue detailPicture : productDetailPictures) {
                        String path = detailPicture.getString("path");
                        String contentId = createNewContentForImage(dctx, path, admin);
                        if (null == EntityQuery.use(delegator).from("ProductContent").where("contentId", contentId, "productId", (String) gv.get("productId"), "productContentTypeId", "DETAIL_PRODUCT_IMAGE").queryFirst()) {

                            Map<String, Object> productContentCtx = new HashMap<String, Object>();
                            productContentCtx.put("productId", (String) gv.get("productId"));
                            productContentCtx.put("productContentTypeId", "DETAIL_PRODUCT_IMAGE");
                            productContentCtx.put("fromDate", UtilDateTime.nowTimestamp());
                            productContentCtx.put("userLogin", admin);
                            productContentCtx.put("contentId", contentId);
                            productContentCtx.put("statusId", "IM_PENDING");
                            dispatcher.runSync("createProductContent", productContentCtx);
                        }
                    }
                }


                EntityCondition singleConditions = EntityCondition.makeCondition("skuId", EntityOperator.LIKE, skuId + "%");
                EntityCondition singleConditions3 = EntityCondition.makeCondition("contentType", EntityOperator.EQUALS, "SINGLE_PRODUCT_IMAGE");
                EntityCondition singleCondition = EntityCondition.makeCondition(singleConditions, EntityOperator.AND, singleConditions3);
                List<GenericValue> singleDetailPictures = delegator.findList("ProductImagesTemp",
                        singleCondition, null,
                        null, null, false);
                if (null != singleDetailPictures && singleDetailPictures.size() > 0) {
                    for (GenericValue singlePicture : singleDetailPictures) {
                        String path = singlePicture.getString("path");
                        String contentId = createNewContentForImage(dctx, path, admin);
                        if (null == EntityQuery.use(delegator).from("ProductContent").where("contentId", contentId, "productId", (String) gv.get("productId"), "productContentTypeId", "SINGLE_PRODUCT_IMAGE").queryFirst()) {
                            Map<String, Object> productContentCtx = new HashMap<String, Object>();
                            productContentCtx.put("productId", (String) gv.get("productId"));
                            productContentCtx.put("productContentTypeId", "SINGLE_PRODUCT_IMAGE");
                            productContentCtx.put("fromDate", UtilDateTime.nowTimestamp());
                            productContentCtx.put("userLogin", admin);
                            productContentCtx.put("contentId", contentId);
                            productContentCtx.put("statusId", "IM_PENDING");
                            dispatcher.runSync("createProductContent", productContentCtx);
                        }

                    }
                }

            }
        }


        return resultMap;
    }


    /**
     * 为图片创建一个新的content
     *
     * @param ctx
     * @param context
     * @return
     * @author sven
     */
    private static String createNewContentForImage(DispatchContext ctx, String objectInfo, GenericValue userLogin) {
        LocalDispatcher dispatcher = ctx.getDispatcher();
        Delegator delegator = ctx.getDelegator();
        String contentId = null;

        try {
            //先根据路径去dataResource里面找，如果找到了，就直接返回，避免重复
            GenericValue dr = EntityUtil.getFirst(EntityQuery.use(delegator).from("DataResource").where("objectInfo", objectInfo).queryList());
            if (UtilValidate.isNotEmpty(dr)) {
                GenericValue content = EntityUtil.getFirst(EntityQuery.use(delegator).from("Content").where("dataResourceId", dr.getString("dataResourceId")).queryList());
                return content.getString("contentId");
            }

            Map<String, Object> contentCtx = new HashMap<String, Object>();
            contentCtx.put("contentTypeId", "DOCUMENT");
            contentCtx.put("userLogin", userLogin);
            Map<String, Object> contentResult = new HashMap<String, Object>();
            contentResult = dispatcher.runSync("createContent", contentCtx);
            contentId = (String) contentResult.get("contentId");

            //获取MIME TYPE
            String fileContentType = "";
            if (objectInfo.lastIndexOf(".") > -1) {
                String fileExtension = objectInfo.substring(objectInfo.lastIndexOf(".") + 1);
                GenericValue fileExt = delegator.findOne("FileExtension", true, UtilMisc.toMap("fileExtensionId", fileExtension));
                if (UtilValidate.isNotEmpty(fileExt)) {
                    fileContentType = fileExt.getString("mimeTypeId");
                }
            }
            ImageManagementServices.createContentAndDataResource(ctx, userLogin, "", objectInfo, contentId, fileContentType);
        } catch (GenericServiceException e) {
            Debug.log("创建图片的Content失败");
            return null;
        } catch (GenericEntityException e) {
            e.printStackTrace();
            return null;
        }
        return contentId;
    }


    /**
     * 把第一个单品图放到Product字段中去
     *
     * @param delegator
     * @param variantProductId
     */
    public static void setProductEntityImageField(Delegator delegator, String variantProductId, String objectInfo) {
        try {
            GenericValue product = delegator.findOne("Product", false, UtilMisc.toMap("productId", variantProductId));
            if (UtilValidate.isNotEmpty(product)) {
                String smallSize = UtilProperties.getPropertyValue("aliyunoss", "OSS_SMALL_IMAGE_SIZE");
                String mediumSize = UtilProperties.getPropertyValue("aliyunoss", "OSS_MEDIUM_IMAGE_SIZE");
                String largeSize = UtilProperties.getPropertyValue("aliyunoss", "OSS_LARGE_IMAGE_SIZE");
                String detailSize = UtilProperties.getPropertyValue("aliyunoss", "OSS_DETAIL_IMAGE_SIZE");

                product.set("smallImageUrl", objectInfo + smallSize);
                product.set("mediumImageUrl", objectInfo + mediumSize);
                product.set("largeImageUrl", objectInfo + largeSize);
                product.set("detailImageUrl", objectInfo + detailSize);
                product.set("originalImageUrl", objectInfo);
                product.store();
            }
        } catch (GenericEntityException e) {
            e.printStackTrace();
        }
    }


    /**
     * 保存商品的搭配图片
     *
     * @param ctx
     * @param context
     * @return
     * @author sven
     */
    public static Map<String, Object> saveMatchImages(DispatchContext ctx, Map<String, Object> context) {
        Map<String, Object> result = ServiceUtil.returnSuccess("操作成功");
//        String productCategoryId = (String) context.get("productCategoryId");
//        String contentId = (String) context.get("contentId");
//        String sequenceNum = (String) context.get("sequenceNum");
//        String objectInfo = (String) context.get("objectInfo");
//        String prodCatContentTypeId = "CATEGORY_IMAGE_URL";
//        GenericValue userLogin = (GenericValue) context.get("userLogin");
//        LocalDispatcher dispatcher = ctx.getDispatcher();
//        Delegator delegator = ctx.getDelegator();
//        String successMessage = null;
//        try {
//            if(UtilValidate.isEmpty(contentId)){
//                GenericValue partyRole = delegator.findOne("PartyRole", false, UtilMisc.toMap("partyId",userLogin.getString("partyId"),"roleTypeId","IMAGEAPPROVER"));
//                if(UtilValidate.isEmpty(partyRole)){
//                    return ServiceUtil.returnError("您没有管理图片的权限");
//                }
//
//                contentId = createNewContentForImage(ctx,context);
//                if(UtilValidate.isNotEmpty(contentId)){
//                    //为避免一个Content关联了多次而fromDate不一样，先删除原来的
//                    if(UtilValidate.isEmpty(delegator.findByAnd("ProductCategoryContent", UtilMisc.toMap("productCategoryId",productCategoryId,"prodCatContentTypeId",prodCatContentTypeId,"contentId",contentId)))){
//                        GenericValue productCategoryContent = delegator.makeValue("ProductCategoryContent");
//                        productCategoryContent.set("productCategoryId", productCategoryId);
//                        productCategoryContent.set("contentId", contentId);
//                        productCategoryContent.set("prodCatContentTypeId", prodCatContentTypeId);
//                        productCategoryContent.set("fromDate", UtilDateTime.nowTimestamp());
//                        productCategoryContent.set("sequenceNum", Long.parseLong(sequenceNum));
//                        productCategoryContent.create();
//                        approvalImageContent(contentId,dispatcher,userLogin);
//                    }else{
//                        successMessage= "搭配图中有重复图片，已经忽略";
//                    }
//
//                }
//            }else{
//                //如果已经有图片了，只更新他的sequenceNum
//                GenericValue productContent = EntityUtil.getFirst(delegator.findByAnd("ProductCategoryContent", UtilMisc.toMap("productCategoryId",productCategoryId,"prodCatContentTypeId",prodCatContentTypeId,"contentId",contentId)));
//                if(UtilValidate.isNotEmpty(productContent)){
//                    productContent.set("sequenceNum", Long.parseLong(sequenceNum));
//                }
//                productContent.store();
//            }
//        } catch (NumberFormatException e) {
//            e.printStackTrace();
//        } catch (GenericEntityException e) {
//            e.printStackTrace();
//        }
//        result.put("productCategoryId", productCategoryId);
//        if(UtilValidate.isNotEmpty(successMessage)){
//            result.put("successMessage", successMessage);
//        }
        return result;
    }


    /**
     * 根据产品编号组装oss的路径名称
     *
     * @param productId  虚拟商品编号
     * @param colorId    颜色的featureId
     * @param imgType    图片类型，比如单品图，细节图，搭配图等等
     * @param delegator
     * @param dispatcher
     * @return //季、波段、系列、款号、颜色、图片类型
     * @author sven
     */
    private static String assemblingOSSPath(String virtualProductId, String colorId, String imgType, Delegator delegator, LocalDispatcher dispatcher) {
        String schemeStr = null;
        try {
            //检查这个颜色是否有4码
            String productId = ZuczugProductUtils.checkProductMiddleSize(virtualProductId, colorId, delegator, dispatcher);

            String colorIdCode = "";
            if (UtilValidate.isNotEmpty(colorId)) {
                colorIdCode = delegator.findOne("ProductFeature", true, UtilMisc.toMap("productFeatureId", colorId)).getString("idCode");
            }

            //获取组别等信息
            Map<String, String> groupInfo = ZuczugProductUtils.getVariantGroupCategorys(delegator, productId);

            String seasonName = "";//季
            if (UtilValidate.isNotEmpty(groupInfo.get("seasonId"))) {
                seasonName = delegator.findOne("ProductCategory", false, UtilMisc.toMap("productCategoryId", groupInfo.get("seasonId"))).getString("categoryName");
            }

            String seriesName = "";//系列
            if (UtilValidate.isNotEmpty(groupInfo.get("seriesId"))) {
                seriesName = delegator.findOne("ProductCategory", false, UtilMisc.toMap("productCategoryId", groupInfo.get("seriesId"))).getString("categoryName");
            }

            String waveName = "";//波段
            GenericValue waveView = EntityUtil.getFirst(EntityQuery.use(delegator).from("ProductCategoryAndMember").where("productId", virtualProductId, "productCategoryTypeId", "WAVE").queryList());
            if (UtilValidate.isNotEmpty(waveView)) {
                waveName = waveView.getString("categoryName"); //波段
            }

            schemeStr = UtilProperties.getPropertyValue("aliyunoss", "OSS_PATH_SCHEME");
            schemeStr = schemeStr.replace("${SEASON}", seasonName)
                    .replace("${SERIES}", seriesName)
                    .replace("${WAVE}", waveName)
                    .replace("${VIRTUAL_ID}", virtualProductId)
                    .replace("${COLOR_ID}", colorIdCode)
                    .replace("${IMG_TYPE}", imgType == null ? "" : imgType);

        } catch (GenericEntityException e) {
            e.printStackTrace();
        }

        return schemeStr;
    }

    /**
     * 根据产品名称从oss载入图片列表
     *
     * @return
     * @author sven
     */
    public static String loadOSSImageListWithProduct(HttpServletRequest request, HttpServletResponse response) {
        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
        Delegator delegator = (Delegator) request.getAttribute("delegator");

        OSSClient client = new OSSClient(ENDPOINT, KEY, SECRET);
        String virtualProductId = request.getParameter("productId");
        String colorId = request.getParameter("colorId");
        String imgType = request.getParameter("imgType");

        String prefix = "";

        if(imgType.equals("singleImages")){
            prefix = ANKORAU_SINGLE_PATH;
        }

        if(imgType.equals("detailImages")){
            prefix =   ANKORAU_DETAIL_PATH;
        }
        if(imgType.equals("matchImages")){
            prefix =   ANKORAU_MATCH_PATH;
        }


        //获取目录

                //assemblingOSSPath(virtualProductId, colorId, imgType, delegator, dispatcher);

        // 构造ListObjectsRequest请求
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest(BUCKET_NAME, prefix, null, "/", 999);

        ObjectListing listing = client.listObjects(listObjectsRequest);
        List<Map<String, String>> imgList = new ArrayList<Map<String, String>>();

        // 遍历所有Object
        for (OSSObjectSummary objectSummary : listing.getObjectSummaries()) {
            if (objectSummary.getKey().indexOf(".") > -1) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("fileName", getFileName(prefix, objectSummary.getKey()));
                 map.put("fileUrl", getFileUrl(objectSummary.getKey()));
                imgList.add(map);
            }
        }

        request.setAttribute("imgList", imgList);
        return "success";
    }

    /**
     * 获取文件名
     *
     * @param prefix
     * @param objKey
     * @return
     */
    private static String getFileName(String prefix, String objKey) {
        return objKey.replace(prefix, "");
    }

    /**
     * 获取文件的地址
     *
     * @param objKey
     * @return
     */
    private static String getFileUrl(String objKey) {
        return IMG_DOMAIN_PREFIX + "/" + objKey;
    }
}
