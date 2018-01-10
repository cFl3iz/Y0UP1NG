package main.java.com.banfftech.platformmanager;


import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.entity.util.EntityQuery;
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

    public static final String resourceUiLabels = "PlatformManagerUiLabels.xml";

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
        fieldSet.add("messageLogTypeId");
//        if (UtilValidate.isNotEmpty(objectId)) {
//
//        }

        EntityCondition findConditions = EntityCondition
                .makeCondition(UtilMisc.toMap("partyIdTo", partyIdTo));


        EntityCondition findConditions2 = EntityCondition
                .makeCondition(UtilMisc.toMap("partyIdFrom", partyIdTo));

        EntityCondition listConditions = EntityCondition
                .makeCondition(findConditions, EntityOperator.OR, findConditions2);


        EntityCondition findConditions3 = EntityCondition
                .makeCondition(UtilMisc.toMap("partyIdTo", partyIdFrom));


        EntityCondition findConditions4 = EntityCondition
                .makeCondition(UtilMisc.toMap("partyIdFrom", partyIdFrom));

        EntityCondition listConditions2 = EntityCondition
                .makeCondition(findConditions3, EntityOperator.OR, findConditions4);


        EntityConditionList<EntityCondition> listBigConditions = EntityCondition
                .makeCondition(listConditions, listConditions2);

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
            if (fromParty.equals(partyIdTo)) {
                continue;
            }
            String messageId = (String) gv.get("messageId");
            String message = (String) gv.get("message");
            String tsStr = "";

            String messageLogTypeId = (String) gv.get("messageLogTypeId");
            rowMap.put("messageLogTypeId", messageLogTypeId);
            if(UtilValidate.isNotEmpty(messageLogTypeId) && messageLogTypeId.equals("LOCATION") && message!=null && !message.trim().equals("")){
                rowMap.put("latitude", message.substring(message.indexOf("tude\":")+6,message.indexOf(",\"lo")));
                rowMap.put("longitude", message.substring(message.indexOf("longitude\":") + 11, message.lastIndexOf("}")));
            }

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

        fieldSet.add("messageLogTypeId");
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
            String messageLogTypeId = (String) gv.get("messageLogTypeId");
            rowMap.put("messageLogTypeId", messageLogTypeId);
            if(UtilValidate.isNotEmpty(messageLogTypeId) && messageLogTypeId.equals("LOCATION") && message!=null && !message.trim().equals("")){
                rowMap.put("latitude", message.substring(message.indexOf("tude\":")+6,message.indexOf(",\"lo")));
                rowMap.put("longitude", message.substring(message.indexOf("longitude\":") + 11, message.lastIndexOf("}")));
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
            userMap.put("toPartyId", fromParty);

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

        resultMap.put("partyId", partyIdTo);

        String partyIdFrom = (String) context.get("partyIdFrom");

        String objectId = (String) context.get("objectId");

        String productId = (String) context.get("productId");

        String bizType = (String) context.get("bizType");

        Set<String> fieldSet = new HashSet<String>();

        // App 内 用户点击阅读了消息列表
        String click = (String) context.get("click");


        // 区分作用域 WebChat 还是 App 查询列用途

        fieldSet.add("message");
        fieldSet.add("partyIdFrom");
        fieldSet.add("partyIdTo");
        fieldSet.add("objectId");
        fieldSet.add("messageId");
        fieldSet.add("fromDate");

        fieldSet.add("messageLogTypeId");


        EntityCondition objectIdCondition = EntityCondition
                .makeCondition(UtilMisc.toMap("objectId", productId));

        EntityCondition findConditionsPartyIdTo = EntityCondition
                .makeCondition(UtilMisc.toMap("partyIdTo", partyIdTo));


        EntityCondition findConditionsPartyIdFrom = EntityCondition
                .makeCondition(UtilMisc.toMap("partyIdFrom", partyIdTo));


        EntityCondition findConditions3 = null;
        EntityCondition findConditions4 = null;
        if(UtilValidate.isNotEmpty(productId)){


          findConditions3 = EntityCondition
                .makeCondition(findConditionsPartyIdTo, EntityOperator.AND, objectIdCondition);

          findConditions4 = EntityCondition
                .makeCondition(findConditionsPartyIdFrom, EntityOperator.AND, objectIdCondition);
        }else{

             findConditions3 = EntityCondition
                    .makeCondition(findConditionsPartyIdTo);

              findConditions4 = EntityCondition
                    .makeCondition(findConditionsPartyIdFrom);

        }
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
        if (bizType != null & bizType.equals("findOne")) {
            queryMessageLogList = delegator.findList("MessageLog",
                    listBigConditions, fieldSet,
                    UtilMisc.toList("-fromDate"), null, false);
        } else {
            queryMessageLogList = delegator.findList("MessageLog",
                    listBigConditions, fieldSet,
                    UtilMisc.toList("-fromDate"), null, false);
        }




        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();


        //-----------------------------------------------------------------------------


        List<String> directList = new ArrayList<String>();



        for (int i = 0; i < queryMessageLogList.size(); i++) {

            GenericValue gv = queryMessageLogList.get(i);

            Map<String, Object> rowMap = new HashMap<String, Object>();

            Map<String, Object> dMap = new HashMap<String, Object>();

            Map<String, Object> userMap = new HashMap<String, Object>();


            String gvObjectId = (String) gv.get("objectId");

            String fromParty = (String) gv.get("partyIdFrom");

            Map<String, String> user = null;


            String toParty = (String) gv.get("partyIdTo");
            if (bizType != null & bizType.equals("findOne")) {
                user = queryPersonBaseInfo(delegator, fromParty);
            } else {
                //绝对不拿自己的
                if (fromParty.equals(partyIdTo)) {
                    user = queryPersonBaseInfo(delegator, toParty);
                    userMap.put("realPartyId",toParty);
                } else {
                    user = queryPersonBaseInfo(delegator, fromParty);
                    userMap.put("realPartyId",fromParty);
                }

                if (directList.size() > 0 && directList.contains(fromParty + "|" + toParty) || directList.size() > 0 && directList.contains(toParty + "|" + fromParty)) {
                    continue;
                } else {
                    directList.add(fromParty + "|" + toParty);
                }
            }


            userMap.put("_id", toParty);

            userMap.put("toParty", toParty);
            userMap.put("fromParty", fromParty);

            rowMap.put("toParty", toParty);
            rowMap.put("fromParty", fromParty);


            userMap.put("name", user.get("firstName"));

            userMap.put("avatar", user.get("headPortrait"));

            rowMap.put("user", userMap);


            String messageId = (String) gv.get("messageId");

            String message = (String) gv.get("message");
            String messageLogTypeId = (String) gv.get("messageLogTypeId");
            rowMap.put("messageLogTypeId", messageLogTypeId);
            if(UtilValidate.isNotEmpty(messageLogTypeId) && messageLogTypeId.equals("LOCATION") && message!=null && !message.trim().equals("")){
                rowMap.put("latitude", message.substring(message.indexOf("tude\":")+6,message.indexOf(",\"lo")));
                rowMap.put("longitude", message.substring(message.indexOf("longitude\":") + 11, message.lastIndexOf("}")));
            }
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
            rowMap.put("objectId", gvObjectId);



            returnList.add(rowMap);

        }
        //badge
        int count = 0;
        for (Map<String, Object> mp : returnList) {
            String to = (String) mp.get("toParty");
            String from = (String) mp.get("fromParty");
            String realPartyId = (String) mp.get("realPartyId");
            findConditions3 = EntityCondition
                    .makeCondition(UtilMisc.toMap("partyIdTo", to));


            findConditions4 = EntityCondition
                    .makeCondition(UtilMisc.toMap("partyIdFrom", to));

            listConditions2 = EntityCondition
                    .makeCondition(findConditions3, EntityOperator.OR, findConditions4);

            EntityCondition findConditions = EntityCondition
                    .makeCondition(UtilMisc.toMap("partyIdTo", from));


            EntityCondition findConditions2 = EntityCondition
                    .makeCondition(UtilMisc.toMap("partyIdFrom", from));

            EntityCondition listConditions = EntityCondition
                    .makeCondition(findConditions, EntityOperator.OR, findConditions2);

            EntityCondition findConditions5 = EntityCondition
                    .makeCondition("badge", EntityOperator.EQUALS, "true");

            listBigConditions = EntityCondition
                    .makeCondition(listConditions, listConditions2, findConditions5);

            List<GenericValue> queryMessageList = delegator.findList("MessageLog",
                    listBigConditions, fieldSet,
                    null, null, false);
            if (bizType != null & bizType.equals("findOne") && click != null & click.equals("click")) {
                for (GenericValue gv : queryMessageList) {
                    gv.set("badge", "false");
                    gv.store();

                }
            } else {
                count += queryMessageList.size();
                mp.put("badge", queryMessageList.size());
            }





            String relationStr = "";
            System.out.println("===========================================================================>");
            System.out.println("PartyIdTo="+userLogin.get("partyId"));
            System.out.println("PartyIdFrom="+realPartyId);

            //查客户关系
            List<GenericValue> partyRelationship = EntityQuery.use(delegator).from("PartyRelationship").where("partyIdTo",userLogin.get("partyId"),"partyIdFrom",realPartyId).queryList();
            System.out.println("partyRelationship="+partyRelationship.size());
            if(partyRelationship!=null && partyRelationship.size()>0){
                for(int index = 0 ; index < partyRelationship.size(); index++ ){
                    GenericValue gv = partyRelationship.get(index);
                    String relation = (String) gv.get("partyRelationshipTypeId");
                    System.out.println("relation="+relation);
                    relationStr += UtilProperties.getMessage(resourceUiLabels,relation, locale)+",";
                    System.out.println("relationStr="+relationStr);
                }
            }else{
                relationStr = "潜在客户";
            }
            System.out.println("<===========================================================================");
            mp.put("custRelation",relationStr);
        }




        resultMap.put("messages", returnList);

        return resultMap;
    }
}
