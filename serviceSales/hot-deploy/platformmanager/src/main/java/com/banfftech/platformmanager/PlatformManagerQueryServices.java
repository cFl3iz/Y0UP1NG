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
                .makeCondition(listConditions,listConditions2);

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
            if(fromParty.equals(partyIdTo)){
                continue;
            }
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

        resultMap.put("partyId",partyIdTo);

        String partyIdFrom = (String) context.get("partyIdFrom");
        String objectId = (String) context.get("objectId");

        String bizType = (String) context.get("bizType");

        Set<String> fieldSet = new HashSet<String>();

        // App 内 用户点击阅读了消息列表
        String click  = (String) context.get("click");


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
        if (bizType != null & bizType.equals("findOne")) {
            queryMessageLogList = delegator.findList("MessageLog",
                    listBigConditions, fieldSet,
                    UtilMisc.toList("-fromDate"), null, false);
        }else{
            queryMessageLogList = delegator.findList("MessageLog",
                    listBigConditions, fieldSet,
                    UtilMisc.toList("-fromDate"), null, false);
        }


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


        List<String> directList = new ArrayList<String>();

//        beforeMap.put("objectId", "");
//        beforeMap.put("from", "");
//        beforeMap.put("to", "");


        //第一步,安装字典
//        for (int i = 0; i < queryMessageLogList.size(); i++) {
//            GenericValue gv = queryMessageLogList.get(i);
//            String fromParty = (String) gv.get("partyIdFrom");
//            String toParty = (String) gv.get("partyIdTo");
//            Map<String, Object> rowMap = new HashMap<String, Object>();
//            rowMap.put("fromParty",fromParty);
//            rowMap.put("toParty",toParty);
//
//        }


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
            }else{
                //绝对不拿自己的
                if(fromParty.equals(partyIdTo)){
                    user = queryPersonBaseInfo(delegator, toParty);
                }else{
                    user = queryPersonBaseInfo(delegator, fromParty);
                }

            if(directList.size()>0 && directList.contains(fromParty+"|"+toParty) || directList.size()>0 && directList.contains(toParty+"|"+fromParty)){
                continue;
            }else{
                directList.add(fromParty+"|"+toParty);
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
            rowMap.put("objectId",gvObjectId);

//            if (bizType != null & bizType.equals("findOne")) {
//                returnList.add(rowMap);
//            } else {
//
//
//                if (
//                        gvObjectId.equals(beforeMap.get("objectId"))
//                                && fromParty.equals(beforeMap.get("from"))
//                                && partyIdTo.equals(beforeMap.get("to")) ||
//                                gvObjectId.equals(beforeMap.get("objectId"))
//                                        && fromParty.equals(beforeMap.get("to"))
//                                        && partyIdTo.equals(beforeMap.get("from"))
//                        ) {
//
//                } else {
//                    returnList.add(rowMap);
//                }
//            }

            returnList.add(rowMap);
//            beforeMap.put("from", fromParty);
//            beforeMap.put("to", partyIdTo);
//            beforeMap.put("objectId", (String) gv.get("objectId"));

        }
        //badge
        int count = 0;
        for(Map<String, Object> mp : returnList){
            String to    =  (String)  mp.get("toParty");
            String from  =  (String)  mp.get("fromParty");
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
                    .makeCondition(listConditions, listConditions2,findConditions5);

            List<GenericValue> queryMessageList = delegator.findList("MessageLog",
                    listBigConditions, fieldSet,
                    null, null, false);
            if(bizType != null & bizType.equals("findOne") && click !=null & click.equals("click")){
                for(GenericValue gv : queryMessageList){
                    gv.set("badge","false");
                    gv.store();

                }
            }else{
                count += queryMessageList.size();
                mp.put("badge",queryMessageList.size());
            }

        }











        // 查询registrationID
//        EntityCondition pConditions = EntityCondition.makeCondition("partyId", partyIdTo);
//        List<EntityCondition> devTypeExprs = new ArrayList<EntityCondition>();
//        devTypeExprs.add(EntityCondition.makeCondition("partyIdentificationTypeId", "JPUSH_ANDROID"));
//        devTypeExprs.add(EntityCondition.makeCondition("partyIdentificationTypeId", "JPUSH_IOS"));
//        EntityCondition devCondition = EntityCondition.makeCondition(devTypeExprs, EntityOperator.OR);
//        pConditions = EntityCondition.makeCondition(pConditions, devCondition);
//
//        List<GenericValue> partyIdentifications =  delegator.findList("PartyIdentification", pConditions, null, UtilMisc.toList("-createdStamp"), null, false);
//
//
//
//        if(null != partyIdentifications && partyIdentifications.size()>0) {
//
//
//            GenericValue partyIdentification = (GenericValue) partyIdentifications.get(0);
//            String jpushId = (String) partyIdentification.getString("idValue");
//            String partyIdentificationTypeId = (String) partyIdentification.get("partyIdentificationTypeId");
//            System.out.println("===========================================" + "badge="+count+""+"|message="+"message"+"|content="+"badge"+"|regId="+jpushId+"|deviceType:"+partyIdentificationTypeId+"|sendType="+""+"objectId="+"");
//            dispatcher.runSync("pushNotifOrMessage",UtilMisc.toMap("userLogin",userLogin,"badge",count+"","message","message","content","badge","regId",jpushId,"deviceType",partyIdentificationTypeId,"sendType","","objectId",""));
//
//        }



        resultMap.put("messages", returnList);

        return resultMap;
    }
}
