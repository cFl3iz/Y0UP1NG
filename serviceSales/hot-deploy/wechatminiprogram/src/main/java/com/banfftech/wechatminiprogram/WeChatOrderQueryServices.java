package main.java.com.banfftech.wechatminiprogram;

import main.java.com.banfftech.personmanager.PersonManagerQueryServices;
import main.java.com.banfftech.platformmanager.constant.PeConstant;
import org.apache.ofbiz.entity.GenericEntity;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.entity.util.EntityUtil;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.util.EntityFindOptions;
import org.apache.ofbiz.entity.condition.EntityConditionList;
import org.apache.ofbiz.entity.condition.EntityOperator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.geom.GeneralPath;
import java.util.*;

/**
 * Created by S on 2017/11/20.
 */
public class WeChatOrderQueryServices {


    public final static String module = WeChatOrderQueryServices.class.getName();


    /**
     * query MyCollect Product
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryMyCollectProduct(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();


        String openId = (String) context.get("openId");

        System.out.println("*OPENID = " + openId);

        GenericValue partyIdentification = EntityQuery.use(delegator).from("PartyIdentification").where("idValue", openId, "partyIdentificationTypeId", "WX_UNIO_ID").queryFirst();

        String partyId = "NA";

        if (UtilValidate.isNotEmpty(partyIdentification)) {
            partyId = (String) partyIdentification.get("partyId");
        }
        System.out.println("*partyId = " + partyId);


        List<GenericValue> partyMarkRoleList = EntityQuery.use(delegator).from("ProductRole").where("partyId", partyId, "roleTypeId", "PLACING_CUSTOMER").queryList();

        if (null != partyMarkRoleList) {


            for (GenericValue gv : partyMarkRoleList) {

                String productId  = (String) gv.get("productId");

                Map<String,Object> rowMap =  new HashMap<String, Object>();

                GenericValue product = delegator.findOne("Product",UtilMisc.toMap("productId",productId),false);

                rowMap.put("productId",productId);
                rowMap.put("internalName",product.get("internalName"));
                rowMap.put("productName",product.get("productName"));
                rowMap.put("detailImageUrl",product.get("detailImageUrl"));

                GenericValue productPrice =EntityQuery.use(delegator).from("ProductPrice").where("productId",productId).queryFirst();

                rowMap.put("price",productPrice.get("price"));

                returnList.add(rowMap);
            }

        }

        resultMap.put("collectList", returnList);

        return resultMap;
    }


    /**
     * query Order FromWeChat
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryOrderListFromWeChat(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();


        String openId = (String) context.get("openId");

        System.out.println("*OPENID = " + openId);

        GenericValue partyIdentification = EntityQuery.use(delegator).from("PartyIdentification").where("idValue", openId, "partyIdentificationTypeId", "WX_UNIO_ID").queryFirst();

        String partyId = "NA";

        if (UtilValidate.isNotEmpty(partyIdentification)) {
            partyId = (String) partyIdentification.get("partyId");
        }
        System.out.println("*partyId = " + partyId);
        Set<String> fieldSet = new HashSet<String>();
        fieldSet.add("orderId");
        fieldSet.add("partyId");
        fieldSet.add("statusId");
        fieldSet.add("currencyUom");
        fieldSet.add("grandTotal");
        fieldSet.add("productId");
        fieldSet.add("quantity");
        fieldSet.add("unitPrice");
        fieldSet.add("roleTypeId");
        fieldSet.add("orderDate");
        fieldSet.add("productStoreId");
        fieldSet.add("payToPartyId");


        EntityCondition findConditions3 = EntityCondition
                .makeCondition(UtilMisc.toMap("roleTypeId", "BILL_TO_CUSTOMER"));

        EntityCondition findConditions = EntityCondition
                .makeCondition(UtilMisc.toMap("partyId", partyId));


        EntityCondition findConditions2 = EntityCondition
                .makeCondition(UtilMisc.toMap("payToPartyId", partyId));

        EntityCondition listConditions = EntityCondition
                .makeCondition(findConditions, EntityOperator.OR, findConditions2);

        EntityCondition listConditions2 = EntityCondition
                .makeCondition(findConditions3, EntityOperator.AND, listConditions);


        List<GenericValue> queryMyResourceOrderList = delegator.findList("OrderHeaderItemAndRoles",
                listConditions2, fieldSet,
                UtilMisc.toList("-orderDate"), null, false);
        System.out.println("*queryMyResourceOrderList = " + queryMyResourceOrderList);
        if (null != queryMyResourceOrderList && queryMyResourceOrderList.size() > 0) {

            for (GenericValue gv : queryMyResourceOrderList) {
                Map<String, Object> rowMap = new HashMap<String, Object>();
                rowMap = gv.getAllFields();
                String productStoreId = (String) gv.get("productStoreId");
                String productId = (String) gv.get("productId");
                GenericValue product = delegator.findOne("Product", UtilMisc.toMap("productId", productId), false);
                GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), false);
                rowMap.put("productName", "" + product.get("productName"));
                rowMap.put("detailImageUrl", (String) product.get("detailImageUrl"));
                String payToPartyId = (String) productStore.get("payToPartyId");
                rowMap.put("payToPartyId", payToPartyId);
                String statusId = (String) gv.get("statusId");
                rowMap.put("statusId", UtilProperties.getMessage("PersonManagerUiLabels.xml", statusId, locale));
                String payFromPartyId = (String) rowMap.get("partyId");

                Map<String, String> personInfoMap = null;

                Map<String, String> personAddressInfoMap = null;

                //说明这笔订单我是卖家,查买家头像信息
                if (payToPartyId.equals(partyId)) {
                    personInfoMap = PersonManagerQueryServices.queryPersonBaseInfo(delegator, payFromPartyId);
                    personAddressInfoMap = PersonManagerQueryServices.queryPersonAddressInfo(delegator, payFromPartyId);
                }
                //说明这笔单我是买家,查卖家头像信息
                if (!payToPartyId.equals(partyId)) {
                    personInfoMap = PersonManagerQueryServices.queryPersonBaseInfo(delegator, payToPartyId);
                    personAddressInfoMap = PersonManagerQueryServices.queryPersonAddressInfo(delegator, payToPartyId);
                }
                rowMap.put("userPartyId", partyId);
                rowMap.put("personInfoMap", personInfoMap);
                rowMap.put("personAddressInfoMap", personAddressInfoMap);

                returnList.add(rowMap);
            }
        }


        resultMap.put("orderList", returnList);

        return resultMap;
    }
}
