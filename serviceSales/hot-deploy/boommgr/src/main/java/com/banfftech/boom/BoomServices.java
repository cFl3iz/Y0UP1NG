package main.java.com.banfftech.boom;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

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

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.rmi.CORBA.Util;
import javax.servlet.http.HttpServletRequest;

import org.apache.ofbiz.entity.util.EntityUtilProperties;
import com.auth0.jwt.JWTExpiredException;
import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;
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

//import net.sf.json.JSONObject;
import org.apache.ofbiz.service.ModelService;
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
import static main.java.com.banfftech.platformmanager.wechat.WeChatUtil.getAccessToken;
import static main.java.com.banfftech.wechatminiprogram.WeChatMiniProgramServices.updateProductBizData;
import static main.java.com.banfftech.wechatminiprogram.WeChatMiniProgramServices.updateProductBizDataFromOrder;
/**
 * Created by S on 2018/8/29.
 */
public class BoomServices {


    public final static String module = BoomServices.class.getName();

    /**
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

        GenericValue exsitsUser = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", supplierTel));
        String supplierPartyId = "";
        if( null == exsitsUser){

          supplierPartyId = createSupplier(delegator,dispatcher,admin,supplierName,"无");

        // Create Party Block
        int random = (int) (Math.random() * 1000000 + 1);
        Map<String, Object> createPartyInMap = UtilMisc.toMap("userLogin", admin, "nickname", "#" + random,
                "firstName", supplierName, "lastName", " ", "gender", "M","partyId",supplierPartyId);
        Map<String, Object> createPerson = dispatcher.runSync("createUpdatePerson", createPartyInMap);
//        supplierPartyId = (String) createPerson.get("partyId");


        // Create UserLogin Block
        Map<String, Object> createUserLoginInMap = UtilMisc.toMap("userLogin", admin, "userLoginId",
                supplierTel, "partyId", supplierPartyId, "currentPassword", "ofbiz",
                "currentPasswordVerify", "ofbiz", "enabled", "Y");
        Map<String, Object> createUserLogin = dispatcher.runSync("createUserLogin", createUserLoginInMap);

        }else{
            supplierPartyId=exsitsUser.getString("partyId");
        }



        Map<String, Object> createPartyRelationshipInMap = new HashMap<String, Object>();
        createPartyRelationshipInMap.put("roleTypeIdFrom", "CUSTOMER");
        createPartyRelationshipInMap.put("roleTypeIdTo","SUPPLIER");
        createPartyRelationshipInMap.put("userLogin", admin);
        createPartyRelationshipInMap.put("partyIdFrom",partyId );
        createPartyRelationshipInMap.put("partyIdTo", supplierPartyId);
        createPartyRelationshipInMap.put("partyRelationshipTypeId", PeConstant.SUPPLIER);
        Map<String, Object> createPartyRelationshipOutMap = dispatcher.runSync("createPartyRelationship", createPartyRelationshipInMap);

        if (ServiceUtil.isError(createPartyRelationshipOutMap)) {
            return createPartyRelationshipOutMap;
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
