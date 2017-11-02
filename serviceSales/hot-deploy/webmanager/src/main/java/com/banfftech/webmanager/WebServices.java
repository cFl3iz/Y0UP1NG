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
            throws GenericServiceException {

        // Servlet Head

        Locale locale = UtilHttp.getLocale(request);

        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");

        Delegator delegator = (Delegator) request.getAttribute("delegator");

        HttpSession session = request.getSession();

        String code = (String) request.getParameter("code");

        String productId = (String) request.getParameter("productId");


        Map<String,Object> accessMap =  wu.getWeChatAccess(code);


        request.setAttribute("productId",productId);
        request.setAttribute("accessToken",accessMap.get("accessToken"));
        request.setAttribute("openId",accessMap.get("openId"));
        request.setAttribute("code",code);


        //TODO  RunSync getWeChatUserInfo Service  To Get UnioId

        return "success";
    }



}
