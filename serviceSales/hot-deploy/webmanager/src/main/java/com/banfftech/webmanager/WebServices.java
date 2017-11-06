package main.java.com.banfftech.webmanager;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

import main.java.com.banfftech.platformmanager.constant.PeConstant;
import main.java.com.banfftech.platformmanager.util.UtilTools;
import main.java.com.banfftech.platformmanager.wechat.WeChatUtil;
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

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.entity.GenericEntity;

import javax.servlet.http.HttpServletResponse;

import org.apache.ofbiz.entity.condition.EntityOperator;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import org.apache.ofbiz.base.util.UtilHttp;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;


import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;

import main.java.com.banfftech.platformmanager.oss.OSSUnit;



/**
 * Created by S on 2017/11/2.
 */
public class WebServices {

    public final static String module = WebServices.class.getName();

    public static final String resourceError = "PlatformManagerErrorUiLabels.xml";

    public static WeChatUtil wu = new WeChatUtil();

    /**
     * AccessWeChat From -BuyProduct
     * @param request
     * @param response
     * @return
     * @throws GenericServiceException
     */
    public static String accessWeChatFromBuyProduct(HttpServletRequest request, HttpServletResponse response)
            throws GenericServiceException,GenericEntityException {

        // Servlet Head

        Locale locale = UtilHttp.getLocale(request);

        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");

        Delegator delegator = (Delegator) request.getAttribute("delegator");

        HttpSession session = request.getSession();

        String code = (String) request.getParameter("code");

        String prodCatalogId = (String) request.getParameter("prodCatalogId");

        String productId = (String) request.getParameter("productId");

        String payToPartyId = (String) request.getParameter("payToPartyId");

        String productStoreId = (String) request.getParameter("productStoreId");

        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        GenericValue userLogin = null;

        Map<String,Object> accessMap =  wu.getWeChatAccess(code);



        Map<String,Object> resultMap = dispatcher.runSync("getWeChatUserInfo",UtilMisc.toMap("userLogin",admin,"code",code,"openId",accessMap.get("openId"),"accessToken",accessMap.get("accessToken")));

        Map<String,String> userInfoMap = (Map<String,String>) resultMap.get("weChatUserInfo");

        System.out.println("==================================================userInfoMap= " + userInfoMap);

        String unioId =  (String) userInfoMap.get("unionid");



        List<GenericValue> partyIdentificationList = EntityQuery.use(delegator).from("PartyIdentification").where("idValue", unioId).queryList();

        String   partyId ="";




        if(partyIdentificationList.size()>1){

            partyId =  (String) partyIdentificationList.get(partyIdentificationList.size()-1).get("partyId");
            userLogin = EntityQuery.use(delegator).from("UserLogin").where("partyId", partyId, "enabled", "Y").queryFirst();
        }else{

            if(null != partyIdentificationList && partyIdentificationList.size()>0 && partyIdentificationList.get(0) != null){

                partyId =(String) partyIdentificationList.get(0).get("partyId");
                userLogin = EntityQuery.use(delegator).from("UserLogin").where("partyId", partyId, "enabled", "Y").queryFirst();
            }else{

                Debug.logInfo("*CREATE NEW USER", module);
                //立即注册
                Map<String, Object> createPeUserMap = new HashMap<String, Object>();
                createPeUserMap.put("tel",delegator.getNextSeqId("UserLogin")+"");
                createPeUserMap.put("userLogin", admin);
                createPeUserMap.put("uuid", "");
                Map<String, Object> serviceResultMap = dispatcher.runSync("createPeUser", createPeUserMap);
                String newUserLoginId = (String) serviceResultMap.get("userLoginId");
                userLogin = EntityQuery.use(delegator).from("UserLogin").where("userLoginId", newUserLoginId, "enabled", "Y").queryFirst();
                partyId = (String)userLogin.get("partyId");
                main.java.com.banfftech.platformmanager.common.PlatformLoginWorker.createNewWeChatPerson(admin,partyId,delegator,unioId,userInfoMap,userLogin,dispatcher);
            }
        }

        GenericValue priductPrice =  EntityQuery.use(delegator).from("ProductPrice").where("productId", productId, "productPriceTypeId",PeConstant.PRODUCT_PRICE_DEFAULT_TYPE_ID,"productPricePurposeId",PeConstant.PRODUCT_PRICE_DEFAULT_PURPOSE,"currencyUomId", PeConstant.DEFAULT_CURRENCY_UOM_ID).queryFirst();

        // placeResourceOrder
        Map<String,Object> placeResourceOrderInMap = new HashMap<String, Object>();
        placeResourceOrderInMap.put("userLogin",userLogin);
        placeResourceOrderInMap.put("productId",productId);
        placeResourceOrderInMap.put("prodCatalogId",prodCatalogId);
        placeResourceOrderInMap.put("price",priductPrice.get("price")+"");
        placeResourceOrderInMap.put("productStoreId",productStoreId);
        placeResourceOrderInMap.put("payToPartyId",payToPartyId);


        Map<String,Object> placeResourceOrderOutMap =  dispatcher.runSync("placeResourceOrder",placeResourceOrderInMap);


        request.setAttribute("productId",productId);
        request.setAttribute("orderId",placeResourceOrderOutMap.get("orderId"));
        return "success";
    }



}
