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
     * accessWeChatFromBuyStory
     * @param request
     * @param response
     * @return
     * @throws GenericServiceException
     * @throws GenericEntityException
     */
    public static String accessWeChatFromBuyStory(HttpServletRequest request, HttpServletResponse response)
            throws GenericServiceException,GenericEntityException {

        // Servlet Head

        Locale locale = UtilHttp.getLocale(request);

        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");

        Delegator delegator = (Delegator) request.getAttribute("delegator");

        HttpSession session = request.getSession();

        String code = (String) request.getParameter("code");

        String productId = (String) request.getParameter("productId");

        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        Map<String,Object> loginServiceResultMap = dispatcher.runSync("weChatAppWebLogin",
                UtilMisc.toMap("userLogin",admin,"code",code));

        String tarjeta = (String) loginServiceResultMap.get("tarjeta");

        String subscribe = (String) loginServiceResultMap.get("subscribe");

        String partyId   = (String) loginServiceResultMap.get("partyId");

        request.setAttribute("productId",productId);

        request.setAttribute("partyId",partyId);

        request.setAttribute("tarjeta",tarjeta);

        request.setAttribute("subscribe",subscribe);

        return "success";
    }


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

        String productId = (String) request.getParameter("productId");

        String payToPartyId = (String) request.getParameter("payToPartyId");

        request.setAttribute("returnPayToPartyId",payToPartyId);

        String spm      = (String) request.getParameter("spm");

        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        Map<String,Object> loginServiceResultMap = dispatcher.runSync("weChatAppWebLogin",
                UtilMisc.toMap("userLogin",admin,"code",code));

        String tarjeta = (String) loginServiceResultMap.get("tarjeta");
        String subscribe = (String) loginServiceResultMap.get("subscribe");
        String nowPersonName = (String) loginServiceResultMap.get("nowPersonName");
//        GenericValue priductPrice =  EntityQuery.use(delegator).from("ProductPrice").where("productId", productId, "productPriceTypeId",PeConstant.PRODUCT_PRICE_DEFAULT_TYPE_ID,"productPricePurposeId",PeConstant.PRODUCT_PRICE_DEFAULT_PURPOSE,"currencyUomId", PeConstant.DEFAULT_CURRENCY_UOM_ID).queryFirst();
//
//        // placeResourceOrder
//        Map<String,Object> placeResourceOrderInMap = new HashMap<String, Object>();
//        placeResourceOrderInMap.put("userLogin",userLogin);
//        placeResourceOrderInMap.put("productId",productId);
//        placeResourceOrderInMap.put("prodCatalogId",prodCatalogId);
//        placeResourceOrderInMap.put("price",priductPrice.get("price")+"");
//        placeResourceOrderInMap.put("productStoreId",productStoreId);
//        placeResourceOrderInMap.put("payToPartyId",payToPartyId);
//
//
//        Map<String,Object> placeResourceOrderOutMap =  dispatcher.runSync("placeResourceOrder",placeResourceOrderInMap);


        request.setAttribute("productId",productId);

        request.setAttribute("tarjeta",tarjeta);

        request.setAttribute("subscribe",subscribe);
        request.setAttribute("nowPersonName",nowPersonName);

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> SPM = " +spm +" spmlength =" + spm.length());

//        if(null != spm && spm.length()>=5){
//            String sharePartyId  = spm.substring(1,5);
//            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> sharePartyId = " +sharePartyId);
//            GenericValue person = delegator.findOne("Person",UtilMisc.toMap("partyId",sharePartyId),false);
//            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> firstName = " +person.get("firstName"));
//            request.setAttribute("spm",person.get("firstName"));
//        }else{
//            request.setAttribute("spm",spm);
//        }


        request.setAttribute("spm",spm);

        return "success";
    }










}
