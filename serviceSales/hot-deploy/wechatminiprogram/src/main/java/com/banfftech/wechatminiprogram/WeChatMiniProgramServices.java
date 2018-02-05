package main.java.com.banfftech.wechatminiprogram;

import main.java.com.banfftech.personmanager.PersonManagerQueryServices;
import main.java.com.banfftech.platformmanager.constant.PeConstant;
import main.java.com.banfftech.platformmanager.wechat.AccessToken;
import main.java.com.banfftech.platformmanager.wechat.WeChatUtil;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.base.util.UtilHttp;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import sun.net.www.content.text.Generic;
import org.apache.ofbiz.base.util.Debug;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static main.java.com.banfftech.personmanager.PersonManagerQueryServices.queryPersonBaseInfo;
import static main.java.com.banfftech.platformmanager.util.HttpHelper.sendGet;
import static main.java.com.banfftech.platformmanager.wechat.WeChatUtil.getAccessToken;

/**
 * Created by S on 2017/11/20.
 */
public class WeChatMiniProgramServices {


    public final static String module = WeChatMiniProgramServices.class.getName();



    public static String getRandomActionName(int number){

        switch(number){
            default:
                return "也搜索过相同 'aaaaa'";
            case 1:
                return "有你想要'sadsad'";
            case 2:
                return "是专家!,关于这方面 'asdsadsad'";
            case 3:
                return "的朋友沈寅麟关注过 'sadsad'";
            case 4:
                return "的朋友王玉亮购买过 'ABAB'";
            case 5:
                return "的朋友冯浩分享过 'AAAC'";
            case 6:
                return "的同学李玉峰在提供 'BBB'";
            case 7:
                return "的邻居董梦洁提供 'AAA'";


        }

    }


    /**
     * wechat ReleaseResource
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> wechatReleaseResource(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();

        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        BigDecimal quantityTotal = new BigDecimal("99999999");
        String unioId = (String) context.get("unioId");
        String productName = (String) context.get("productName");
        String filePath = (String) context.get("filePath");
        String description = (String) context.get("description");
        //默认的价格是0
        BigDecimal price = BigDecimal.ZERO;
        GenericValue partyIdentification = EntityQuery.use(delegator).from("PartyIdentification").where("idValue", unioId, "partyIdentificationTypeId", "WX_UNIO_ID").queryFirst();
        String partyId = "NA";

        if (UtilValidate.isNotEmpty(partyIdentification)) {
            partyId = (String) partyIdentification.get("partyId");
        }


        GenericValue userLogin =EntityQuery.use(delegator).from("UserLogin").where("partyId", partyId).queryFirst();



        //创建产品
        Map<String, Object> createProductInMap = new HashMap<String, Object>();
        createProductInMap.put("userLogin", admin);
        long ctm = System.currentTimeMillis();
        createProductInMap.put("internalName", partyId + "_" + ctm);
        createProductInMap.put("productName", productName);
        createProductInMap.put("productTypeId", PeConstant.PRODUCT_TYPE_ID);
        createProductInMap.put("description",description );

        createProductInMap.put("smallImageUrl",filePath +"?x-oss-process=image/resize,m_pad,h_50,w_50");
        createProductInMap.put("detailImageUrl", filePath);
        //调用服务创建产品(资源)
        Map<String, Object> createProductOutMap = dispatcher.runSync("createProduct", createProductInMap);

        if (!ServiceUtil.isSuccess(createProductOutMap)) {
            Debug.logError("*Mother Fuck Create Product OutMap Error:" + createProductOutMap, module);
            return createProductOutMap;
        }

        String productId = (String) createProductOutMap.get("productId");

        //创建产品价格(默认所有变形产品都和虚拟产品价格一致)
        Map<String, Object> createProductPriceInMap = new HashMap<String, Object>();
        createProductPriceInMap.put("userLogin", admin);
        createProductPriceInMap.put("productId", productId);
        createProductPriceInMap.put("currencyUomId", PeConstant.DEFAULT_CURRENCY_UOM_ID);
        createProductPriceInMap.put("price", price);
        createProductPriceInMap.put("productPricePurposeId", PeConstant.PRODUCT_PRICE_DEFAULT_PURPOSE);
        createProductPriceInMap.put("productPriceTypeId", PeConstant.PRODUCT_PRICE_DEFAULT_TYPE_ID);
        createProductPriceInMap.put("productStoreGroupId", PeConstant.NA);
        Map<String,Object> createProductPriceServiceResultMap = dispatcher.runSync("createProductPrice", createProductPriceInMap);
        if (!ServiceUtil.isSuccess(createProductPriceServiceResultMap)) {
            Debug.logError("*Mother Fuck Create Product Price Error:"+createProductPriceServiceResultMap, module);
              return createProductPriceServiceResultMap;

        }

        GenericValue prodCatalogRole =  EntityQuery.use(delegator).from("ProdCatalogRole").where("partyId", partyId,"roleTypeId","ADMIN").queryFirst();

        System.out.println("prodCatalogRole=>>"+prodCatalogRole);

        //产品关联分类
        Map<String, Object> addProductToCategoryInMap = new HashMap<String, Object>();
        addProductToCategoryInMap.put("userLogin", admin);
        addProductToCategoryInMap.put("productId", productId);
        addProductToCategoryInMap.put("productCategoryId", prodCatalogRole.get("productCategoryId"));
        Map<String,Object> addProductToCategoryServiceResultMap = dispatcher.runSync("addProductToCategory", addProductToCategoryInMap);
        if (!ServiceUtil.isSuccess(addProductToCategoryServiceResultMap)) {
            Debug.logError("*Mother Fuck added Product To Category Error:"+addProductToCategoryServiceResultMap, module);
           return addProductToCategoryServiceResultMap;

        }

        //找到仓库
        GenericValue facility = EntityQuery.use(delegator).from("Facility").where("ownerPartyId", partyId).queryFirst();

        //为产品创建库存量
        Map<String, Object> receiveInventoryProductIn = UtilMisc.toMap("userLogin", userLogin,
                "facilityId", (String) facility.get("facilityId"),
                "inventoryItemTypeId", PeConstant.DEFAULT_INV_ITEM,
                "productId", productId,
                "description ", "卖家发布产品时的录入库存",
                "quantityAccepted", quantityTotal,
                "quantityRejected", BigDecimal.ZERO,
                "unitCost", price,
                "ownerPartyId", partyId,
                "partyId", partyId,
                "uomId", PeConstant.DEFAULT_CURRENCY_UOM_ID,
                "currencyUomId", PeConstant.DEFAULT_CURRENCY_UOM_ID);

        Map<String, Object> receiveInventoryProductOut = dispatcher.runSync("receiveInventoryProduct", receiveInventoryProductIn
        );
        if (!ServiceUtil.isSuccess(receiveInventoryProductOut)) {
            Debug.logError("*Mother Fuck Receive Inventory Product Error:"+receiveInventoryProductOut, module);
             return receiveInventoryProductOut;

        }

        //  dispatcher.runSync("createProductAttribute",UtilMisc.toMap("userLogin",admin,"productId",productId,"attrName","quantityAccepted","attrValue",quantityTotal+""));


        //给产品增加用户角色
        Map<String, Object> addProductRoleServiceResoutMap = dispatcher.runSync("addProductRole", UtilMisc.toMap("userLogin", admin, "productId", productId, "partyId", partyId, "roleTypeId", "ADMIN"));
        if (!ServiceUtil.isSuccess(addProductRoleServiceResoutMap)) {
            Debug.logError("*Mother Fuck Added ProductRoleService  Error:"+addProductRoleServiceResoutMap, module);
              return addProductRoleServiceResoutMap;

        }




        return resultMap;
    }




    /**
     * autoCreate FriendRelation
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> autoCreateFriendRelation(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();

        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        String resourcesName = (String) context.get("resourcesName");

        if(null!=resourcesName){
            resourcesName=resourcesName.replaceAll("\"", "");
        }

        Date date = new Date();

        SimpleDateFormat formatter;

        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String pushDate = "" + formatter.format(date);




        String partyIdFrom   = "admin";
        String openId        = "oeLJkxAP6eivcZkflpPiDFNg";
        String productId     = "10000";

        String message     = resourcesName;
        String firstName     = "提醒";
        String payToPartyId  = "10000";
        String tarjeta       = "123";
        String jumpUrl   = null;




        Map<String,String> personInfoMap =  queryPersonBaseInfo(delegator,payToPartyId);


        // 发送模版消息
        AccessToken accessToken = getAccessToken(PeConstant.WECHAT_GZ_APP_ID,PeConstant.ACCESS_KEY_SECRET);
        String URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
        String url = URL.replace("ACCESS_TOKEN", accessToken.getToken());

        JSONObject jsobj1 = new JSONObject();
        JSONObject jsobj2 = new JSONObject();
        JSONObject jsobj3 = new JSONObject();
        JSONObject jsobj4 = new JSONObject();
        JSONObject jsobj5 = new JSONObject();



        jsobj1.put("touser",openId);
        jsobj1.put("template_id","aFCzhfNrWb0GsEr0ZCVuijLPAQ6cPzPedORxyKHBzbs");
        jsobj1.put("url","https://www.yo-pe.com/pejump/"+partyIdFrom+"/"+partyIdFrom+"111"+"/"+payToPartyId+"/"+productId);

        JSONObject jsobjminipro = new JSONObject();
        jsobjminipro.put("appid","wx299644ef4c9afbde");
        jsobjminipro.put("pagepath","pages/home/home");
        jsobj1.put("miniprogram",jsobjminipro);


        jsobj3.put("value", firstName+"给您发了一条消息");
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






        String friends = (String) context.get("friends");

        if (friends != null) {

            String[] friendArray = friends.split(",");

            for (String name : friendArray) {

                String nextUserName = name.substring(name.lastIndexOf("\":\"") + 3, name.lastIndexOf("\""));


                GenericValue person =  EntityQuery.use(delegator).from("Person").where("firstName",nextUserName).queryFirst();


                String nextPartyId ="";

                if(null == person){
                //先创建人员
                 Map<String, Object> createPartyInMap = UtilMisc.toMap("userLogin", admin, "nickname",nextUserName,
                 "firstName", nextUserName, "lastName", " ", "gender", "M");
                  Map<String, Object> createPerson = dispatcher.runSync("createUpdatePerson", createPartyInMap);
                  nextPartyId = (String) createPerson.get("partyId");
                }else{
                    nextPartyId = (String) person.get("partyId");
                }


                GenericValue relation =  EntityQuery.use(delegator).from("PartyRelationship").where("partyIdFrom",nextPartyId).queryFirst();

                if(relation == null){
                //创建人员关系
                Map<String,Object> createPartyRelationMap  = new HashMap<String, Object>();
                createPartyRelationMap.put("userLogin",admin);
                createPartyRelationMap.put("partyIdFrom",nextPartyId);
                createPartyRelationMap.put("partyIdTo","admin");
                createPartyRelationMap.put("partyRelationshipTypeId","FRIEND");
                dispatcher.runSync("createPartyRelationship",createPartyRelationMap);
                }

                int max=7;
                int min=1;

                Random random = new Random();
                int randomNumber = random.nextInt(max)%(max-min+1) + min;
                //创建购物记录
                GenericValue shopingList = EntityQuery.use(delegator).from("ShoppingList").where("partyId",nextPartyId,"listName",resourcesName).queryFirst();

                if(null == shopingList){


                Map<String,Object> createShoppingListMap  = new HashMap<String, Object>();
                createShoppingListMap.put("userLogin",admin);
                createShoppingListMap.put("listName",resourcesName);
                createShoppingListMap.put("description",nextUserName+ getRandomActionName(randomNumber));
                createShoppingListMap.put("shoppingListTypeId","SLT_FREQ_PURCHASES");
                createShoppingListMap.put("isPublic","Y");
                createShoppingListMap.put("partyId",nextPartyId);

                dispatcher.runSync("createShoppingList",createShoppingListMap);
                }

            }


        } else {
            return resultMap;
        }

        resultMap.put("resourcesName",resourcesName);
        return resultMap;
    }


    /**
     * jscode2session
     *
     * @param request
     * @param response
     * @return
     * @throws GenericServiceException
     * @throws GenericEntityException
     */
    public static String jscode2session(HttpServletRequest request, HttpServletResponse response)
            throws GenericServiceException, GenericEntityException {

        // Servlet Head

        Locale locale = UtilHttp.getLocale(request);

        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");

        Delegator delegator = (Delegator) request.getAttribute("delegator");

        HttpSession session = request.getSession();

        String code = (String) request.getParameter("code");


        String responseStr2 = sendGet(PeConstant.WECHAT_MINI_PROGRAM_SNS_PATH,
                "appid=" + PeConstant.WECHAT_MINI_PROGRAM_APP_ID +
                        "&secret=" + PeConstant.WECHAT_MINI_PROGRAM_APP_SECRET_ID +
                        "&js_code=" + code + "&grant_type=authorization_code");

        JSONObject jsonMap2 = JSONObject.fromObject(responseStr2);

        String unionid = (String) jsonMap2.get("unionid");

        System.out.println("*MiniProgramm-jscode2session jsonMap2 = " + jsonMap2);
        System.out.println("*unionid = " + unionid);
        request.setAttribute("unionid", unionid);

        return "success";
    }

}
