package main.java.com.banfftech.boom;


import java.math.BigDecimal;
import java.text.DateFormat;
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

/**
 * Created by S on 2018/8/29.
 */
public class BoomQueryServices {


    public static final String resourceUiLabels = "CommonEntityLabels.xml";



    public static Map<String, Object> queryMyRawMaterials(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = userLogin.getString("partyId");

        List<GenericValue> productList = EntityQuery.use(delegator).from("ProductRole").where(
                "partyId", partyId, "roleTypeId", "ADMIN").orderBy("-fromDate").queryList();

        List<Map<String,Object>> returnList = new ArrayList<Map<String, Object>>();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if(productList.size()>0){
            for(GenericValue gv : productList){
                Map<String,Object> rowMap = new HashMap<String, Object>();
                String productId = gv.getString("productId");
                rowMap.put("productId",productId);
                GenericValue product =  EntityQuery.use(delegator).from("Product").where(
                        "productId", productId).queryFirst();

                rowMap.put("productName",product.getString("productName"));
                rowMap.put("imagePath",product.getString("detailImageUrl"));
                rowMap.put("createdDate",sdf.format(product.get("createdDate")));
                String uomId = product.getString("quantityUomId");
                rowMap.put("quantityUomId",uomId);

                GenericValue uom =  EntityQuery.use(delegator).from("Uom").where(
                        "uomId", uomId).queryFirst();

                String uomDescription = uom.getString("description");
                rowMap.put("description",uomDescription);

                String cndescription = UtilProperties.getMessage(resourceUiLabels, "Uom.description." + uomId, new Locale("zh"));
                rowMap.put("uomDescription",cndescription.indexOf("Uom.description")>-1?uomDescription:cndescription);


                List<GenericValue> suppliers = EntityQuery.use(delegator).from("SupplierProduct").where(
                        "productId", productId).queryList();

                List<Map<String,Object>> supplierList = new ArrayList<Map<String, Object>>();
                if(null!=suppliers&&suppliers.size()>0){
                    for(GenericValue supplier : suppliers){
                        Map<String,Object> rowSupplier = new HashMap<String, Object>();
                        String supplierPartyId = supplier.getString("partyId");
                        GenericValue partyGroup =  EntityQuery.use(delegator).from("PartyGroup").where(
                                "partyId", supplierPartyId).queryFirst();
                        rowSupplier.put("supplierId",supplierPartyId);
                        rowSupplier.put("groupName",partyGroup.getString("groupName"));
                        supplierList.add(rowSupplier);
                    }
                }
                rowMap.put("supplierList",supplierList);

                returnList.add(rowMap);
            }
        }

        resultMap.put("rawMaterialsList",returnList);
        return resultMap;
    }


    public static Map<String, Object> queryQuantityUom(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();


        List<String> types = new ArrayList<String>();
        types.add("CURRENCY_MEASURE");

        EntityCondition findConditions = EntityCondition
                .makeCondition("uomTypeId", EntityOperator.NOT_IN, types);


        List<GenericValue> uomList = EntityQuery.use(delegator).from("Uom").where(findConditions).orderBy("-createdStamp").queryList();

        List<Map<String,Object>> returnList = new ArrayList<Map<String, Object>>();

            for(GenericValue gv : uomList){
                Map<String,Object> rowMap = new HashMap<String, Object>();
                String uomId = gv.getString("uomId");

                String abbreviation = gv.getString("abbreviation");

                rowMap.put("uomId",uomId);
                String description = gv.getString("description");
                rowMap.put("description",description);

                String cndescription = UtilProperties.getMessage(resourceUiLabels, "Uom.description." + uomId, new Locale("zh"));
                rowMap.put("zh_description",cndescription.indexOf("Uom.description")>-1?description:cndescription);
                rowMap.put("abbreviation",abbreviation);


                returnList.add(rowMap);
            }


        resultMap.put("uomList",returnList);
        return resultMap;
    }


    /**
     * queryMySupplierList
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryMySupplierList(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = userLogin.getString("partyId");

        List<GenericValue> relationList = EntityQuery.use(delegator).from("PartyRelationship").where(
                "partyIdFrom", partyId, "roleTypeIdFrom", "CUSTOMER").orderBy("-fromDate").queryList();

        List<Map<String,Object>> returnList = new ArrayList<Map<String, Object>>();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if(relationList.size()>0){
            for(GenericValue gv : relationList){
                Map<String,Object> rowMap = new HashMap<String, Object>();
                String partyIdTo = gv.getString("partyIdTo");
                Map<String,String> supplierInfo =  queryPersonBaseInfo(delegator,partyIdTo);
                rowMap.put("name",supplierInfo.get("firstName"));
                rowMap.put("partyId",partyIdTo);
                rowMap.put("tel",supplierInfo.get("userLoginId"));
                rowMap.put("avatar",supplierInfo.get("headPortrait"));
                rowMap.put("orderSize","0");
                rowMap.put("fromDate",sdf.format(gv.get("fromDate")));
                returnList.add(rowMap);
            }
        }

        resultMap.put("supplierList",returnList);
        return resultMap;
    }


}
