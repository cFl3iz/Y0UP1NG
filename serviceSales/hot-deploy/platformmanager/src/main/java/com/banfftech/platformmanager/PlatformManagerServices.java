package main.java.com.banfftech.platformmanager;


import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

import main.java.com.banfftech.platformmanager.constant.PeConstant;
import main.java.com.banfftech.platformmanager.util.UtilTools;
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
import org.eclipse.birt.chart.extension.datafeed.GanttEntry;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;



/**
 * Created by Administrator on 2017/9/12.
 */
public class PlatformManagerServices {

    public final static String module = PlatformManagerServices.class.getName();


    /**
     * send Message
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

        if(partyIdFrom.equals(partyIdTo)){
            //TODO IF EQUALS , PARTY ID TO  = CUSTOMER
            GenericValue orderMap = EntityQuery.use(delegator).from("OrderHeaderItemAndRoles").where("orderId",orderId).queryFirst();
            partyIdTo = (String) orderMap.get("partyId");
        }


        Map<String,Object> createMessageLogMap = new HashMap<String, Object>();

        createMessageLogMap.put("partyIdFrom",partyIdFrom);
        createMessageLogMap.put("messageId", delegator.getNextSeqId("MessageLog"));
        createMessageLogMap.put("partyIdTo",partyIdTo);
        createMessageLogMap.put("message",message);
        createMessageLogMap.put("fromDate",org.apache.ofbiz.base.util.UtilDateTime.nowTimestamp());

        GenericValue msg = delegator.makeValue("MessageLog", createMessageLogMap);
        msg.create();




        // 查询registrationID
        EntityCondition pConditions = EntityCondition.makeCondition("partyId", partyIdTo);
        List<EntityCondition> devTypeExprs = new ArrayList<EntityCondition>();
        devTypeExprs.add(EntityCondition.makeCondition("partyIdentificationTypeId", "JPUSH_ANDROID"));
        devTypeExprs.add(EntityCondition.makeCondition("partyIdentificationTypeId", "JPUSH_IOS"));
        EntityCondition devCondition = EntityCondition.makeCondition(devTypeExprs, EntityOperator.OR);
        pConditions = EntityCondition.makeCondition(pConditions, devCondition);

        List<GenericValue> partyIdentifications =  delegator.findList("PartyIdentification", pConditions, null, UtilMisc.toList("-createdStamp"), null, false);
        GenericValue  partyIdentification = (GenericValue) partyIdentifications.get(0);
        String regId = (String) partyIdentification.getString("idValue");
        String partyIdentificationTypeId = (String) partyIdentification.get("partyIdentificationTypeId");
        Map<String, Object> result = ServiceUtil.returnSuccess();
        result.put("regId", regId);
        GenericValue person = delegator.findOne("Person",UtilMisc.toMap("partyId",partyIdFrom),false);
        result.put("partyIdFrom",person.get("firstName") + ":"+message);
        result.put("partyIdTo",partyIdFrom);
        result.put("deviceType",partyIdentificationTypeId);

        result.put("message","message:" + partyIdTo + ":" + partyIdFrom+":"+orderId+"");

        return result;
    }




}
