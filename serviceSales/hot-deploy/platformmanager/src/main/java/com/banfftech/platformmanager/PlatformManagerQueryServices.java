package main.java.com.banfftech.platformmanager;


import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.entity.condition.EntityConditionList;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;
import org.eclipse.birt.chart.extension.datafeed.GanttEntry;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static main.java.com.banfftech.personmanager.PersonManagerQueryServices.queryPersonBaseInfo;


/**
 * Created by Administrator on 2017/9/12.
 */
public class PlatformManagerQueryServices {

    public final static String module = PlatformManagerQueryServices.class.getName();


    public static Map<String, Object> loadMaiJiaMessage(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();


        //Scope Param
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyIdTo = (String) userLogin.get("partyId");
        String partyIdFrom = (String) context.get("partyIdFrom");
        String objectId = (String) context.get("objectId");

        Set<String> fieldSet = new HashSet<String>();

        fieldSet.add("message");
        fieldSet.add("partyIdFrom");
        fieldSet.add("partyIdTo");
        fieldSet.add("objectId");
        fieldSet.add("messageId");
        fieldSet.add("fromDate");

//        if (UtilValidate.isNotEmpty(objectId)) {
//
//        }

        EntityCondition findConditions = EntityCondition
                .makeCondition(UtilMisc.toMap("partyIdTo", partyIdTo));


        EntityCondition findConditions2 = EntityCondition
                .makeCondition(UtilMisc.toMap("partyIdFrom", partyIdFrom));

        EntityCondition listConditions = EntityCondition
                .makeCondition(findConditions, EntityOperator.OR, findConditions2);


        EntityConditionList<EntityCondition> listBigConditions = EntityCondition
                .makeCondition(listConditions);

        List<GenericValue> queryMessageLogList = null;

        queryMessageLogList = delegator.findList("MessageLog",
                listBigConditions, fieldSet,
                UtilMisc.toList("fromDate"), null, false);


        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();

        for (GenericValue gv : queryMessageLogList) {
            Map<String, Object> rowMap = new HashMap<String, Object>();
            Map<String, Object> userMap = new HashMap<String, Object>();
            String fromParty = (String) gv.get("partyIdFrom");
            String toParty = (String) gv.get("partyIdTo");
            String messageId = (String) gv.get("messageId");
            String message = (String) gv.get("message");
            String tsStr = "";
            DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            try {
                //方法一
                tsStr = sdf.format(gv.get("fromDate"));
            } catch (Exception e) {
            }

            rowMap.put("messageId", messageId);
            rowMap.put("text", message);
            rowMap.put("messageTime", tsStr);

//            GenericValue person = delegator.findOne("Person",UtilMisc.toMap("partyId",toParty),false);
            Map<String, String> user = queryPersonBaseInfo(delegator, toParty);
            userMap.put("toPartyId", toParty);
            userMap.put("name", user.get("firstName"));
            userMap.put("avatar", user.get("headPortrait"));
            rowMap.put("user", userMap);
            returnList.add(rowMap);
        }


        resultMap.put("messages", returnList);

        return resultMap;
    }


    /**
     * Load Message Log
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> loadMessage(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();


        //Scope Param
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyIdTo = (String) userLogin.get("partyId");
        String partyIdFrom = (String) context.get("partyIdFrom");
        String objectId = (String) context.get("objectId");

        Set<String> fieldSet = new HashSet<String>();

        // 区分作用域 WebChat 还是 App 查询列用途
        String bizType = (String) context.get("bizType");

        fieldSet.add("message");
        fieldSet.add("partyIdFrom");
        fieldSet.add("partyIdTo");
        fieldSet.add("objectId");
        fieldSet.add("messageId");
        fieldSet.add("fromDate");


        EntityCondition findConditions3 = EntityCondition
                .makeCondition(UtilMisc.toMap("partyIdTo", partyIdTo));


        EntityCondition findConditions4 = EntityCondition
                .makeCondition(UtilMisc.toMap("partyIdFrom", partyIdTo));

        EntityCondition listConditions2 = EntityCondition
                .makeCondition(findConditions3, EntityOperator.OR, findConditions4);


        EntityConditionList<EntityCondition> listBigConditions = null;

        if (UtilValidate.isNotEmpty(partyIdFrom)) {
            EntityCondition findConditions = EntityCondition
                    .makeCondition(UtilMisc.toMap("partyIdTo", partyIdFrom));


            EntityCondition findConditions2 = EntityCondition
                    .makeCondition(UtilMisc.toMap("partyIdFrom", partyIdFrom));

            EntityCondition listConditions = EntityCondition
                    .makeCondition(findConditions, EntityOperator.OR, findConditions2);
            listBigConditions = EntityCondition
                    .makeCondition(listConditions, listConditions2);
        } else {
            listBigConditions = EntityCondition
                    .makeCondition(listConditions2);
        }


        List<GenericValue> queryMessageLogList = null;
        if (UtilValidate.isNotEmpty(bizType) && bizType.equals("webChat")) {
            queryMessageLogList = delegator.findList("MessageLog",
                    listBigConditions, fieldSet,
                    UtilMisc.toList("fromDate"), null, false);
        } else {
            queryMessageLogList = delegator.findList("MessageLogView",
                    listBigConditions, fieldSet,
                    UtilMisc.toList("-fromDate"), null, false);
        }


        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();

        for (GenericValue gv : queryMessageLogList) {
            Map<String, Object> rowMap = new HashMap<String, Object>();
            Map<String, Object> userMap = new HashMap<String, Object>();
            String fromParty = (String) gv.get("partyIdFrom");
            String toParty = (String) gv.get("partyIdTo");
            String messageId = (String) gv.get("messageId");
            String message = (String) gv.get("message");
            String tsStr = "";
            DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            try {
                //方法一
                tsStr = sdf.format(gv.get("fromDate"));
            } catch (Exception e) {
            }

            rowMap.put("messageId", messageId);
            rowMap.put("text", message);
            rowMap.put("messageTime", tsStr);


            Map<String, String> user = null;

            if (!partyIdTo.equals(fromParty)) {
                user = queryPersonBaseInfo(delegator, toParty);


            } else {

                user = queryPersonBaseInfo(delegator, fromParty);

            }
            //此处拿的是from
            userMap.put("toPartyId",  fromParty);

            userMap.put("name", user.get("firstName"));
            userMap.put("avatar", user.get("headPortrait"));
            rowMap.put("user", userMap);
            returnList.add(rowMap);
        }


        resultMap.put("messages", returnList);

        return resultMap;
    }


    /**
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> loadAllMessage(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();


        //Scope Param
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyIdTo = (String) userLogin.get("partyId");
        String partyIdFrom = (String) context.get("partyIdFrom");
        String objectId = (String) context.get("objectId");

        String bizType = (String) context.get("bizType");

        Set<String> fieldSet = new HashSet<String>();

        // 区分作用域 WebChat 还是 App 查询列用途

        fieldSet.add("message");
        fieldSet.add("partyIdFrom");
        fieldSet.add("partyIdTo");
        fieldSet.add("objectId");
        fieldSet.add("messageId");
        fieldSet.add("fromDate");


        EntityCondition findConditions3 = EntityCondition
                .makeCondition(UtilMisc.toMap("partyIdTo", partyIdTo));


        EntityCondition findConditions4 = EntityCondition
                .makeCondition(UtilMisc.toMap("partyIdFrom", partyIdTo));

        EntityCondition listConditions2 = EntityCondition
                .makeCondition(findConditions3, EntityOperator.OR, findConditions4);


        EntityConditionList<EntityCondition> listBigConditions = null;

        if (UtilValidate.isNotEmpty(partyIdFrom)) {
            EntityCondition findConditions = EntityCondition
                    .makeCondition(UtilMisc.toMap("partyIdTo", partyIdFrom));


            EntityCondition findConditions2 = EntityCondition
                    .makeCondition(UtilMisc.toMap("partyIdFrom", partyIdFrom));

            EntityCondition listConditions = EntityCondition
                    .makeCondition(findConditions, EntityOperator.OR, findConditions2);
            listBigConditions = EntityCondition
                    .makeCondition(listConditions, listConditions2);
        } else {
            listBigConditions = EntityCondition
                    .makeCondition(listConditions2);
        }


        List<GenericValue> queryMessageLogList = null;

        queryMessageLogList = delegator.findList("MessageLog",
                listBigConditions, fieldSet,
                UtilMisc.toList("-fromDate"), null, false);

//        if (UtilValidate.isNotEmpty(bizType) && bizType.equals("webChat")) {
//            queryMessageLogList = delegator.findList("MessageLog",
//                    listBigConditions, fieldSet,
//                    UtilMisc.toList("-fromDate"), null, false);
//        } else {}
//            queryMessageLogList = delegator.findList("MessageLogView",
//                    listBigConditions, fieldSet,
//                    UtilMisc.toList("-fromDate"), null, false);


        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();


        //-----------------------------------------------------------------------------


        Map<String, String> beforeMap = new HashMap<String, String>();

        beforeMap.put("objectId", "");
        beforeMap.put("from", "");
        beforeMap.put("to", "");


        for (int i = 0; i < queryMessageLogList.size(); i++) {

            GenericValue gv = queryMessageLogList.get(i);

            Map<String, Object> rowMap = new HashMap<String, Object>();

            Map<String, Object> userMap = new HashMap<String, Object>();


            String gvObjectId = (String) gv.get("objectId");

            String fromParty = (String) gv.get("partyIdFrom");

            String toParty = (String) gv.get("partyIdTo");


            Map<String, String> user = null;

            if (partyIdTo.equals(fromParty)) {
                user = queryPersonBaseInfo(delegator, toParty);
                userMap.put("_id", toParty);
            } else {
                user = queryPersonBaseInfo(delegator, fromParty);
                userMap.put("_id", fromParty);
            }




            userMap.put("name", user.get("firstName"));

            userMap.put("avatar", user.get("headPortrait"));

            rowMap.put("user", userMap);


            String messageId = (String) gv.get("messageId");

            String message = (String) gv.get("message");

            String tsStr = "";

            DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

            try {
                //方法一
                tsStr = sdf.format(gv.get("fromDate"));
            } catch (Exception e) {
            }

            rowMap.put("_id", messageId);
            rowMap.put("text", message);
            rowMap.put("createdAt", tsStr);


            if (bizType != null & bizType.equals("findOne")) {
                returnList.add(rowMap);
            } else {


                if (

                                  fromParty.equals(beforeMap.get("from"))
                                && partyIdTo.equals(beforeMap.get("to")) ||

                                          fromParty.equals(beforeMap.get("to"))
                                        && partyIdTo.equals(beforeMap.get("from"))
                        ) {
                } else {
                    returnList.add(rowMap);
                }
            }
            beforeMap.put("from", fromParty);
            beforeMap.put("to", partyIdTo);
            beforeMap.put("objectId", (String) gv.get("objectId"));

        }


        resultMap.put("messages", returnList);

        return resultMap;
    }
}
