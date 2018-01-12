package main.java.com.banfftech.personmanager;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

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

import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;

import main.java.com.banfftech.platformmanager.oss.OSSUnit;


import net.sf.json.JSONArray;
import sun.net.www.content.text.Generic;
import sun.security.krb5.Config;

import static main.java.com.banfftech.personmanager.PersonManagerQueryServices.queryPersonBaseInfo;
import static main.java.com.banfftech.platformmanager.wechat.WeChatUtil.getAccessToken;

/**
 * Created by S on 2017/9/12.
 */
public class PersonManagerServices {


    public final static String module = PersonManagerServices.class.getName();


    public static final String resourceError = "PlatformManagerErrorUiLabels.xml";


    /**
     * PartyRelationShipENUM
     */
    public enum relationType {

        C2CRSS, CONTACT;

        public static relationType getRelationType(String relationType) {

            return valueOf(relationType.toUpperCase());

        }
    }


    /**
     *
     */
    public enum express {


        AAEWEB, CAE,
        ARAMEX,
        ND56,
        DHL,
        PEISI,
        DPEX,
        EFSPOST,
        DEXP,
        CHINZ56,
        EMS,
        QCKD,
        EWE,
        QFKD,
        FEDEX,
        APEX,
        FEDEXIN,
        RFD,
        PCA,
        SFC,
        TNT,
        STO,
        UPS,
        SFWL,
        ANJELEX,
        SHENGHUI,
        ANE,
        SDEX,
        ANEEX,
        SFEXPRESS,
        ANXINDA,
        SUNING,
        EES,
        SURE,
        HTKY,
        HOAU,
        BSKY,
        TTKDEX,
        FLYWAYEX,
        VANGEN,
        DTW, WANJIA, DEPPON, EWINSHINE, GCE, GZWENJIE, PHOENIXEXP, XBWL, FTD, XFEXPRESS, GSD, BROADASIA, GTO, YIEXPRESS, BLACKDOG, QEXPRESS, HENGLU, ETD, HYE, UC56, HQKY, CHINAPOST, JOUST, YFHEX, TMS, YTO, JIAJI, YADEX, JIAYI, YCGWL, KERRY, YFEXPRESS, HREX, YTEXPRESS, PEWKEE, YUNDA, JD, ZJS, KKE, ZMKMEX, JIUYESCM, COE, KYEXPRESS, CRE, FASTEXPRESS, ZTKY, BLUESKY, ZTO, LTS, LBEX, ZTO56, CNPL;

        public static express getExpressName(String express) {
            return valueOf(express.toUpperCase());
        }
    }


    /**
     * 返回快递中文名
     *
     * @param type
     * @return
     */
    private static String getExpressNameFromType(String type) {


        switch (express.getExpressName(type)) {

            case AAEWEB:
                return "AAE";

            case CAE:
                return "民航";

            case ARAMEX:
                return "Aramex";

            case ND56:
                return "能达";

            case DHL:
                return "DHL";

            case PEISI:
                return "配思航宇";

            case DPEX:
                return "DPEX";

            case EFSPOST:
                return "平安快递";

            case DEXP:
                return "D速";

            case CHINZ56:
                return "秦远物流";

            case EMS:
                return "EMS";

            case QCKD:
                return "全晨";

            case EWE:
                return "EWE";

            case QFKD:
                return "全峰";

            case FEDEX:
                return "联邦快递";

            case APEX:
                return "全一";

            case FEDEXIN:
                return "联邦快递-国际件";

            case RFD:
                return "如风达";

            case PCA:
                return "PCA";

            case SFC:
                return "三态";

            case TNT:
                return "TNT";

            case STO:
                return "申通";

            case UPS:
                return "UPS";

            case SFWL:
                return "盛丰";

            case ANJELEX:
                return "安捷";

            case SHENGHUI:
                return "盛辉";

            case ANE:
                return "安能";

            case SDEX:
                return "顺达快递";

            case ANEEX:
                return "安能快递";

            case SFEXPRESS:
                return "顺丰";

            case ANXINDA:
                return "安信达";

            case SUNING:
                return "苏宁";

            case EES:
                return "百福东方";

            case SURE:
                return "速尔";

            case HTKY:
                return "百世快递";

            case HOAU:
                return "天地华宇";

            case BSKY:
                return "百世快运";

            case TTKDEX:
                return "天天";

            case FLYWAYEX:
                return "程光";

            case VANGEN:
                return "万庚";

            case DTW:
                return "大田";

            case WANJIA:
                return "万家物流";

            case DEPPON:
                return "德邦";

            case EWINSHINE:
                return "万象";

            case GCE:
                return "飞洋";

            case GZWENJIE:
                return "文捷航空";

            case PHOENIXEXP:
                return "凤凰";

            case XBWL:
                return "新邦";

            case FTD:
                return "富腾达";

            case XFEXPRESS:
                return "信丰";

            case GSD:
                return "共速达";

            case BROADASIA:
                return "亚风";

            case GTO:
                return "国通";

            case YIEXPRESS:
                return "宜送";

            case BLACKDOG:
                return "黑狗";

            case QEXPRESS:
                return "易达通";

            case HENGLU:
                return "恒路";

            case ETD:
                return "易通达";

            case HYE:
                return "鸿远";

            case UC56:
                return "优速";

            case HQKY:
                return "华企";

            case CHINAPOST:
                return "邮政包裹";

            case JOUST:
                return "急先达";

            case YFHEX:
                return "原飞航";

            case TMS:
                return "加运美";

            case YTO:
                return "圆通";

            case JIAJI:
                return "佳吉";

            case YADEX:
                return "源安达";

            case JIAYI:
                return "佳怡";

            case YCGWL:
                return "远成";

            case KERRY:
                return "嘉里物流";

            case YFEXPRESS:
                return "越丰";

            case HREX:
                return "锦程快递";

            case YTEXPRESS:
                return "运通";

            case PEWKEE:
                return "晋越";

            case YUNDA:
                return "韵达";

            case JD:
                return "京东";

            case ZJS:
                return "宅急送";

            case KKE:
                return "京广";

            case ZMKMEX:
                return "芝麻开门";

            case JIUYESCM:
                return "九曳";

            case COE:
                return "中国东方";

            case KYEXPRESS:
                return "跨越";

            case CRE:
                return "中铁快运";

            case FASTEXPRESS:
                return "快捷";

            case ZTKY:
                return "中铁物流";

            case BLUESKY:
                return "蓝天";

            case ZTO:
                return "中通";

            case LTS:
                return "联昊通";
            case LBEX:
                return "龙邦";

            case ZTO56:
                return "中通快运";

            case CNPL:
                return "中邮";

        }


        return "未知";
    }


    /**
     * addProductRole
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     * @throws Exception
     */
    public static Map<String, Object> addProductRole(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException, Exception {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();


        Delegator delegator = dispatcher.getDelegator();
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));
        String partyId = (String) context.get("partyId");
        String roleTypeId = (String) context.get("roleTypeId");
        String productId = (String) context.get("productId");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        if(productId==null || partyId ==null){
            return resultMap;
        }
        // 客户对于产品的角色

        GenericValue productRoleCust =  EntityQuery.use(delegator).from("ProductRole").where("partyId", partyId, "roleTypeId", roleTypeId,"productId", productId).queryFirst();

        System.out.println("*ProductRole Compoment ------------------->");
        System.out.println("*ProductRole =" + roleTypeId);
        System.out.println("*ProductId =" + productId);
        System.out.println("*PartyId =" + partyId);
        System.out.println("*HisRole ? =" + (productRoleCust == null));
        System.out.println("*ProductRole Compoment <-------------------");
        if(null == productRoleCust){
            dispatcher.runSync("addPartyToProduct", UtilMisc.toMap("userLogin", admin, "partyId", partyId, "productId", productId, "roleTypeId", roleTypeId));
        }

        return resultMap;
    }



    /**
     * spread Product 推广产品
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     * @throws Exception
     */
    public static Map<String, Object> spreadProduct(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException, Exception {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();

        Delegator delegator = dispatcher.getDelegator();

        GenericValue userLogin = (GenericValue) context.get("userLogin");

        String partyId = (String) userLogin.get("partyId");


        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        Map<String, Object> resultMap = ServiceUtil.returnSuccess();

        String productId = (String) context.get("productId");

        String roleTypeId = (String) context.get("roleTypeId");

        String text = (String) context.get("text");




        List<GenericValue> partyList = EntityQuery.use(delegator).from("ProductRole").where("productId", productId, "roleTypeId", roleTypeId).queryList();




        if (null != partyList && partyList.size() > 0) {

            for (GenericValue partyGv : partyList) {
                String spreadPartyId = (String) partyGv.get("partyId");


                List<GenericValue> partyIdentificationList = EntityQuery.use(delegator).from("PartyIdentification").where("partyId", spreadPartyId, "partyIdentificationTypeId", "WX_GZ_OPEN_ID").queryList();

                System.out.println("*partyIdentificationList:" +partyIdentificationList);
                if (null != partyIdentificationList && partyIdentificationList.size() > 0) {

                    String openId = (String) partyIdentificationList.get(0).get("idValue");

                    Map<String, Object> pushWeChatMessageInfoMap = new HashMap<String, Object>();


                    pushWeChatMessageInfoMap.put("message", text);

                    pushWeChatMessageInfoMap.put("userLogin", admin);

                    System.out.println("*PUSH WE CHAT GONG ZHONG PLATFORM !!!!!!!!!!!!!!!!!!!!!!!");

                    Date date = new Date();

                    SimpleDateFormat formatter;

                    formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    String pushDate = "" + formatter.format(date);

                    pushWeChatMessageInfoMap.put("date", pushDate);

                    pushWeChatMessageInfoMap.put("openId", openId);

                    pushWeChatMessageInfoMap.put("productId", productId);

                    pushWeChatMessageInfoMap.put("payToPartyId", partyId);

                    pushWeChatMessageInfoMap.put("url", "http://www.lyndonspace.com:3400/WebManager/control/shareProduct?productId="+productId);

                    //推微信
                    dispatcher.runSync("pushWeChatMessageInfo", pushWeChatMessageInfoMap);


                }
            }
        }


        return resultMap;
    }


    /**
     * Confirm Payment(新卖家确认)
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     * @throws Exception
     */
    public static Map<String, Object> confirmPayment(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException, Exception {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();

        Delegator delegator = dispatcher.getDelegator();

        GenericValue userLogin = (GenericValue) context.get("userLogin");

        String partyId = (String) userLogin.get("partyId");


        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        Map<String, Object> resultMap = ServiceUtil.returnSuccess();

        String productId = (String) context.get("productId");

        String realPartyId = (String) context.get("realPartyId");

        List<GenericValue> partyIdentificationList = EntityQuery.use(delegator).from("PartyIdentification").where("partyId", realPartyId, "partyIdentificationTypeId", "WX_GZ_OPEN_ID").queryList();


        if (null != partyIdentificationList && partyIdentificationList.size() > 0) {

            String openId = (String) partyIdentificationList.get(0).get("idValue");

            Map<String, Object> pushWeChatMessageInfoMap = new HashMap<String, Object>();


            GenericValue person = delegator.findOne("Person", UtilMisc.toMap("partyId", partyId), false);


            if (person != null) {

            }

            pushWeChatMessageInfoMap.put("firstName", person.get("firstName"));

            pushWeChatMessageInfoMap.put("message", "卖家" + person.get("firstName") + "已经确认收到货款。");

            pushWeChatMessageInfoMap.put("userLogin", admin);

            System.out.println("*PUSH WE CHAT GONG ZHONG PLATFORM !!!!!!!!!!!!!!!!!!!!!!!");

            Date date = new Date();

            SimpleDateFormat formatter;

            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String pushDate = "" + formatter.format(date);

            pushWeChatMessageInfoMap.put("date", pushDate);

            pushWeChatMessageInfoMap.put("openId", openId);

            pushWeChatMessageInfoMap.put("productId", productId);

            pushWeChatMessageInfoMap.put("payToPartyId", partyId);

            //推微信
            dispatcher.runSync("pushWeChatMessageInfo", pushWeChatMessageInfoMap);


        }


        Map<String, Object> createMessageLogMap = new HashMap<String, Object>();

        createMessageLogMap.put("partyIdFrom", partyId);

        createMessageLogMap.put("message", " 已收到您的货款! ");

        createMessageLogMap.put("messageId", delegator.getNextSeqId("MessageLog"));

        createMessageLogMap.put("partyIdTo", realPartyId);

        createMessageLogMap.put("badge", "CHECK");

        createMessageLogMap.put("messageLogTypeId", "TEXT");

        createMessageLogMap.put("objectId", productId);


        createMessageLogMap.put("fromDate", org.apache.ofbiz.base.util.UtilDateTime.nowTimestamp());

        GenericValue msg = delegator.makeValue("MessageLog", createMessageLogMap);

        msg.create();


        return resultMap;
    }


    /**
     * create PaymentFromCust
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     * @throws Exception
     */
    public static Map<String, Object> createPaymentFromCust(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException, Exception {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();

        Delegator delegator = dispatcher.getDelegator();

        GenericValue userLogin = (GenericValue) context.get("userLogin");

        String partyId = (String) userLogin.get("partyId");


        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        Map<String, Object> resultMap = ServiceUtil.returnSuccess();

        String orderId = (String) context.get("orderId");

        String payToPartyId = (String) context.get("payToPartyId");


        GenericValue orderHeader = delegator.findOne("OrderHeader", UtilMisc.toMap("orderId", orderId), false);

        GenericValue paymentMethod = EntityQuery.use(delegator).from("PaymentMethod").where("partyId", partyId, "paymentMethodTypeId", "EXT_WXPAY").queryFirst();


        Map<String, Object> serviceResultMap = dispatcher.runSync("createPayment", UtilMisc.toMap("statusId","PMNT_RECEIVED","paymentMethodId", paymentMethod.get("paymentMethodId"), "userLogin", admin, "partyIdTo", payToPartyId, "amount", orderHeader.get("grandTotal"), "partyIdFrom", partyId, "paymentTypeId", PeConstant.CUSTOMER_PAYMENT, "currencyUomId", PeConstant.DEFAULT_CURRENCY_UOM_ID, "comments", orderId));

        String paymentId = (String) serviceResultMap.get("paymentId");

        System.out.println("*create payment over | paymentid = " + paymentId);

        if (!ServiceUtil.isSuccess(serviceResultMap)) {

            return serviceResultMap;

        }

        //createOrderPaymentPreference
        Map<String, Object> createOrderPaymentPreferenceMap = dispatcher.runSync("createOrderPaymentPreference", UtilMisc.toMap("statusId","PMNT_RECEIVED","paymentMethodId", paymentMethod.get("paymentMethodId"), "userLogin", admin, "maxAmount", orderHeader.get("grandTotal"), "orderId", orderId));

        if (!ServiceUtil.isSuccess(createOrderPaymentPreferenceMap)) {

            return createOrderPaymentPreferenceMap;

        }


//        EntityCondition pConditions = EntityCondition.makeCondition("partyId", payToPartyId);
//
//        List<GenericValue> partyIdentifications = delegator.findList("PartyIdentification", pConditions, null, UtilMisc.toList("-createdStamp"), null, false);
//
//        if (null != partyIdentifications && partyIdentifications.size() > 0) {
//
//            GenericValue partyIdentification = (GenericValue) partyIdentifications.get(0);
//
//            String jpushId = (String) partyIdentification.getString("idValue");
//
//            String partyIdentificationTypeId = (String) partyIdentification.get("partyIdentificationTypeId");
//
//           dispatcher.runSync("pushNotifOrMessage", UtilMisc.toMap("userLogin", admin, "message", "order", "content", "订单:+" + orderId + "的买家已完成微信支付,请查收确认!", "regId", jpushId, "deviceType", partyIdentificationTypeId, "sendType", "", "objectId", orderId));
//        }

        resultMap.put("paymentId",paymentId);
        return resultMap;
    }


    /**
     * Order Cancel
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     * @throws Exception
     */
    public static Map<String, Object> orderCancel(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException, Exception {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();

        Delegator delegator = dispatcher.getDelegator();


        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        Map<String, Object> resultMap = ServiceUtil.returnSuccess();

        String orderId = (String) context.get("orderId");

        String changeReason = context.get("changeReason") == null ? "default change" : (String) context.get("changeReason");

        Map<String, Object> changeOrderStatusMap =
                dispatcher.runSync("changeOrderStatus", UtilMisc.toMap("userLogin", admin, "orderId", orderId, "statusId", "ORDER_CANCELLED", "changeReason", changeReason));
        if (!ServiceUtil.isSuccess(changeOrderStatusMap)) {
            return changeOrderStatusMap;
        }


        return resultMap;
    }


    /**
     * Order Payment Received (已弃用)
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     * @throws Exception
     */
    public static Map<String, Object> orderPaymentReceived(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException, Exception {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();

        Delegator delegator = dispatcher.getDelegator();

        Locale locale = (Locale) context.get("locale");


        GenericValue userLogin = (GenericValue) context.get("userLogin");

        String partyId = (String) userLogin.get("partyId");

        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        Map<String, Object> resultMap = ServiceUtil.returnSuccess();

        String orderId = (String) context.get("orderId");


        //找到买家
        GenericValue orderCust = EntityQuery.use(delegator).from("OrderRole").where("orderId", orderId, "roleTypeId", "SHIP_TO_CUSTOMER").queryFirst();

        String payFromPartyId = (String) orderCust.get("partyId");


        GenericValue payFromUserLogin = EntityQuery.use(delegator).from("UserLogin").where("partyId",payFromPartyId).queryFirst();
              //卖家确定自己已经收到钱了
         Map<String,Object> createPaymentResult =  dispatcher.runSync("createPaymentFromCust",UtilMisc.toMap("userLogin",payFromUserLogin,"payToPartyId",partyId,"orderId",orderId));


        //查找订单支付Id
          // GenericValue orderPaymentPrefAndPayment = EntityQuery.use(delegator).from("OrderPaymentPrefAndPayment").where("orderId", orderId).queryFirst();

        //这种情况下说明是先付钱,后发货的。


            //所以卖家应找到客户的付款 并且将状态改为'已收到'

           // GenericValue payment = EntityQuery.use(delegator).from("Payment").where("paymentTypeId", PeConstant.CUSTOMER_PAYMENT, "partyIdFrom", payFromPartyId, "partyIdTo", partyId).queryFirst();

            String paymentId = (String) createPaymentResult.get("paymentId");

            System.out.println("====================================== Payment Id =  " + paymentId);

            Map<String, Object> setPaymentStatusMap = dispatcher.runSync("setPaymentStatus", UtilMisc.toMap("userLogin", userLogin, "paymentId", paymentId, "statusId", "PMNT_RECEIVED"));

            if (!ServiceUtil.isSuccess(setPaymentStatusMap)) {
                return setPaymentStatusMap;
            }




            //推送提醒买家

//        } else {
//
//
//            String orderPaymentPreferenceId = (String) orderPaymentPrefAndPayment.get("orderPaymentPreferenceId");
//
//            GenericValue orderHeader = EntityQuery.use(delegator).from("OrderHeader").where("orderId", orderId).queryFirst();
//
//            //找发票
 //           GenericValue orderItemBillingAndInvoiceAndItem = EntityQuery.use(delegator).from("OrderItemBillingAndInvoiceAndItem").where("orderId", orderId, "amount", orderHeader.get("grandTotal")).queryFirst();
//
//
//            //先将支付应用到发票
//
//            Map<String, Object> createPaymentApplicationMap = dispatcher.runSync("createPaymentApplication", UtilMisc.toMap(
//                    "userLogin", userLogin, "paymentId", orderPaymentPrefAndPayment.get("paymentId"), "invoiceId", orderItemBillingAndInvoiceAndItem.get("invoiceId"), "amountApplied", orderHeader.get("grandTotal")));
//
//            if (!ServiceUtil.isSuccess(createPaymentApplicationMap)) {
//                return createPaymentApplicationMap;
//            }
//
//
//            //确认这笔支付的状态
//
//            Map<String, Object> setPaymentStatusOutMap = dispatcher.runSync("setPaymentStatus", UtilMisc.toMap(
//                    "userLogin", userLogin, "paymentId", orderPaymentPrefAndPayment.get("paymentId"), "statusId", "PMNT_CONFIRMED"));
//
//            if (!ServiceUtil.isSuccess(setPaymentStatusOutMap)) {
//                return setPaymentStatusOutMap;
//            }
//
//
//            //更新订单支付信息
////        Map<String, Object> updateOrderPaymentPreferenceOutMap = dispatcher.runSync("updateOrderPaymentPreference", UtilMisc.toMap(
////                "userLogin", userLogin, "orderPaymentPreferenceId",orderPaymentPreferenceId,"statusId","PMNT_RECEIVED"));
////
////        if (!ServiceUtil.isSuccess(updateOrderPaymentPreferenceOutMap)) {
////            return updateOrderPaymentPreferenceOutMap;
////        }
//
//            //暂时使用卑劣方式
//            orderPaymentPrefAndPayment.set("statusId", "PMNT_RECEIVED");
//            orderPaymentPrefAndPayment.store();
//
//
//        }

        Map<String, Object> pushWeChatMessageInfoMap = new HashMap<String, Object>();
        //推送告知买家

        EntityCondition pConditions = EntityCondition.makeCondition("partyId", payFromPartyId);

        List<GenericValue> partyIdentifications = delegator.findList("PartyIdentification", pConditions, null, UtilMisc.toList("-createdStamp"), null, false);


        if (null != partyIdentifications && partyIdentifications.size() > 0) {
            GenericValue partyIdentification = (GenericValue) partyIdentifications.get(0);
            String jpushId = (String) partyIdentification.getString("idValue");
            String partyIdentificationTypeId = (String) partyIdentification.get("partyIdentificationTypeId");
            dispatcher.runSync("pushNotifOrMessage", UtilMisc.toMap("userLogin", admin, "message", "order", "content", "订单:+" + orderId + "的卖家已确认货款到账!", "regId", jpushId, "deviceType", partyIdentificationTypeId, "sendType", "", "objectId", orderId));
        }


        //推送微信

        pushMsgBase(orderId, partyId, payFromPartyId, delegator, dispatcher, userLogin, "订单:+" + orderId + "的卖家已经确认货款到账!", pushWeChatMessageInfoMap, admin, new HashMap<String, Object>(), "TEXT");


        return resultMap;
    }


    /**
     * setOrderPaymentStatus 买家确认已经付钱了
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     * @throws Exception
     */
    public static Map<String, Object> setOrderPaymentStatus(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException, Exception {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();

        Delegator delegator = dispatcher.getDelegator();

        Locale locale = (Locale) context.get("locale");


        GenericValue userLogin = (GenericValue) context.get("userLogin");

        String partyId = (String) userLogin.get("partyId");

        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        Map<String, Object> resultMap = ServiceUtil.returnSuccess();

        String paymentId = (String) context.get("paymentId");

        String payToPartyId = (String) context.get("payToPartyId");

        String orderId = (String) context.get("orderId");

        GenericValue paymentMethod = EntityQuery.use(delegator).from("PaymentMethod").where("partyId", partyId, "paymentMethodTypeId", "EXT_WXPAY").queryFirst();


        //添加买家的支付方法到该笔支付
        Map<String, Object> updatePaymentOutMap = dispatcher.runSync("updatePayment", UtilMisc.toMap(
                "userLogin", userLogin, "paymentId", paymentId, "paymentMethodId", paymentMethod.get("paymentMethodId")));

        if (!ServiceUtil.isSuccess(updatePaymentOutMap)) {
            return updatePaymentOutMap;
        }

        //确认这笔支付的状态

        Map<String, Object> setPaymentStatusOutMap = dispatcher.runSync("setPaymentStatus", UtilMisc.toMap(
                "userLogin", userLogin, "paymentId", paymentId, "statusId", "PMNT_RECEIVED"));

        if (!ServiceUtil.isSuccess(setPaymentStatusOutMap)) {
            return setPaymentStatusOutMap;
        }

        //推送告知卖家

//        EntityCondition pConditions = EntityCondition.makeCondition("partyId", payToPartyId);
//
//        List<GenericValue> partyIdentifications = delegator.findList("PartyIdentification", pConditions, null, UtilMisc.toList("-createdStamp"), null, false);
//
//
//        if (null != partyIdentifications && partyIdentifications.size() > 0) {
//            GenericValue partyIdentification = (GenericValue) partyIdentifications.get(0);
//            String jpushId = (String) partyIdentification.getString("idValue");
//            String partyIdentificationTypeId = (String) partyIdentification.get("partyIdentificationTypeId");
//            dispatcher.runSync("pushNotifOrMessage", UtilMisc.toMap("userLogin", admin, "message", "order", "content", "订单:+" + orderId + "的买家已完成微信支付,请查收确认!", "regId", jpushId, "deviceType", partyIdentificationTypeId, "sendType", "", "objectId", orderId));
//        }

        return resultMap;
    }


    /**
     * 查询快递信息
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryExpressInfo(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException, Exception {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();

        Delegator delegator = dispatcher.getDelegator();

        Locale locale = (Locale) context.get("locale");


        GenericValue userLogin = (GenericValue) context.get("userLogin");

        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        Map<String, Object> resultMap = ServiceUtil.returnSuccess();


        String code = (String) context.get("code");

        resultMap.put("expressCode", code);

        String host = PeConstant.ALI_EXPRESS_QUERY_API_HOST;
        String path = PeConstant.ALI_EXPRESS_QUERY_API_PATH;
        String method = "GET";
        String appcode = PeConstant.ALI_EXPRESS_QUERY_API_CODE;
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("number", code);
        //当Type为auto时,自动识别物流单位
        querys.put("type", "auto");
        List<JSONObject> strlist = null;
        String type = null;
        JSONObject result = null;
        String entityStr = null;

        HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
        //   System.out.println(response.toString());
        //获取response的body

        try {
            entityStr = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject jsonMap2 = JSONObject.fromObject(entityStr);
        Debug.logInfo("*QueryExpressInfo:" + jsonMap2, module);
        JSONArray list = null;
        try {
            result = (JSONObject) jsonMap2.get("result");
            type = (String) result.get("type");
            list = (JSONArray) result.get("list");
        } catch (Exception e) {
            Debug.logInfo("--" + UtilProperties.getMessage(resourceError, "ExpressInfoNotFound", locale), module);
            //return ServiceUtil.returnError(UtilProperties.getMessage(resourceError, "ExpressInfoNotFound", locale));
        }

        if (list != null) {
            strlist = (List) JSONArray.toList(list,
                    JSONObject.class);
            resultMap.put("expressInfos", strlist);
            resultMap.put("name", getExpressNameFromType(type));
            resultMap.put("carrierCode", type);
        } else {
            resultMap.put("name", "没有物流信息");
        }


        return resultMap;
    }


    /**
     * updateShipGroupShipInfoForWeChat发货
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> updateShipGroupShipInfoForWeChat(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();

        Delegator delegator = dispatcher.getDelegator();

        Locale locale = (Locale) context.get("locale");


        GenericValue userLogin = (GenericValue) context.get("userLogin");

        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        Map<String, Object> resultMap = ServiceUtil.returnSuccess();


        String code = (String) context.get("code");

        String orderId = (String) context.get("orderId");

        String partyId = (String) userLogin.get("partyId");

        String carrierCode = (String) context.get("carrierCode");

        String name = (String) context.get("name");

        String shipmentMethodId = "";

        String contactMechId = "";

        GenericValue orderCust = EntityQuery.use(delegator).from("OrderRole").where("orderId", orderId, "roleTypeId", "SHIP_TO_CUSTOMER").queryFirst();


        GenericValue postalAddress = EntityUtil.getFirst(
                EntityQuery.use(delegator).from("PartyContactMechPurpose").where(UtilMisc.toMap("partyId", orderCust.get("partyId"), "contactMechPurposeTypeId", "SHIPPING_LOCATION")).queryList());

        if (null == postalAddress) {

        } else {

            contactMechId = (String) postalAddress.get("contactMechId");
        }

        GenericValue store = EntityQuery.use(delegator).from("ProductStore").where(UtilMisc.toMap("payToPartyId", partyId)).queryFirst();

        String productStoreId = (String) store.get("productStoreId");

        //查询店铺是否拥有该货运方式
        //GenericValue productStoreShipmentMeth =  EntityQuery.use(delegator).from("ProductStoreShipmentMeth").where(UtilMisc.toMap("partyId", productStoreId)).queryFirst();

        EntityCondition findConditions = EntityCondition
                .makeCondition("partyId", EntityOperator.LIKE, "%" + name + "%");


        EntityCondition findConditions2 = EntityCondition
                .makeCondition(UtilMisc.toMap("productStoreId", productStoreId));

        EntityCondition listConditions = EntityCondition
                .makeCondition(findConditions, EntityOperator.AND, findConditions2);


        //QueryStoreShipmentMethList
        List<GenericValue> queryStoreShipmentMethList = delegator.findList("ProductStoreShipmentMeth",
                listConditions, null,
                UtilMisc.toList("-createdStamp"), null, true);

        if (queryStoreShipmentMethList != null && queryStoreShipmentMethList.size() > 0) {
            GenericValue shimentMethod = (GenericValue) queryStoreShipmentMethList.get(0);
            shipmentMethodId = (String) shimentMethod.get("partyId");
        } else {
            //查询系统中是否有此货运方法,如果没有新增。

            GenericValue party = delegator.findOne("PartyGroup", UtilMisc.toMap("partyId", carrierCode), false);

            if (party == null) {
                dispatcher.runSync("createSimpleCarrierShipmentMethod", UtilMisc.toMap("userLogin", admin, "name", name, "carrierCode", carrierCode, "code", code));
            }

            //给卖家店铺增加此货运方法

            dispatcher.runSync("createProductStoreShipMeth", UtilMisc.toMap("userLogin", admin,
                    "productStoreShipMethId", carrierCode, "productStoreId", productStoreId,
                    "shipmentMethodTypeId", "EXPRESS", "partyId", carrierCode, "roleTypeId", "CARRIER"));
            shipmentMethodId = carrierCode;
        }


        //将买家信息更新到订单货运
        Map<String, Object> updateShipGroupShipInfoOutMap = dispatcher.runSync("updateShipGroupShipInfo", UtilMisc.toMap(
                "userLogin", userLogin, "orderId", orderId,
                "contactMechId", contactMechId, "shipmentMethod", "EXPRESS@" + shipmentMethodId, "shipGroupSeqId", "00001"));

        if (!ServiceUtil.isSuccess(updateShipGroupShipInfoOutMap)) {
            return updateShipGroupShipInfoOutMap;
        }


        //更变订单状态

        Map<String, Object> changeOrderStatusOutMap = dispatcher.runSync("changeOrderStatus", UtilMisc.toMap(
                "userLogin", userLogin, "orderId", orderId, "statusId", "ORDER_APPROVED",
                "changeReason", "卖家确认物流信息", "setItemStatus", "Y"));

        if (!ServiceUtil.isSuccess(changeOrderStatusOutMap)) {
            return changeOrderStatusOutMap;
        }

        GenericValue order = delegator.findOne("OrderHeader", UtilMisc.toMap("orderId", orderId), false);

        order.set("internalCode", code);


        order.store();


        //推送给微信用户

        List<GenericValue> partyIdentificationList = EntityQuery.use(delegator).from("PartyIdentification").where("partyId", orderCust.get("partyId"), "partyIdentificationTypeId", "WX_GZ_OPEN_ID").queryList();


        if (null != partyIdentificationList && partyIdentificationList.size() > 0) {

            Map<String, Object> pushWeChatMessageInfoMap = new HashMap<String, Object>();


            System.out.println("*PUSH WE CHAT GONG ZHONG PLATFORM !!!!!!!!!!!!!!!!!!!!!!!");

            pushWeChatMessageInfoMap.put("payToPartyId", partyId);

            Date date = new Date();

            SimpleDateFormat formatter;

            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String pushDate = "" + formatter.format(date);

            pushWeChatMessageInfoMap.put("date", pushDate);


            String openId = (String) partyIdentificationList.get(0).get("idValue");

            pushWeChatMessageInfoMap.put("openId", openId);

            pushWeChatMessageInfoMap.put("orderId", orderId);

            pushWeChatMessageInfoMap.put("messageInfo", "物流公司:" + name + "物流单号:" + code);


            GenericValue toPartyUserLogin = EntityQuery.use(delegator).from("UserLogin").where("partyId", orderCust.get("partyId"), "enabled", "Y").queryFirst();

            String toPartyUserLoginId = (String) toPartyUserLogin.get("userLoginId");


            long expirationTime = Long.valueOf(EntityUtilProperties.getPropertyValue("pe", "tarjeta.expirationTime", "172800L", delegator));
            String iss = EntityUtilProperties.getPropertyValue("pe", "tarjeta.issuer", delegator);
            String tokenSecret = EntityUtilProperties.getPropertyValue("pe", "tarjeta.secret", delegator);
            //开始时间
            final long iat = System.currentTimeMillis() / 1000L; // issued at claim
            //到期时间
            final long exp = iat + expirationTime;
            //生成
            final JWTSigner signer = new JWTSigner(tokenSecret);
            final HashMap<String, Object> claims = new HashMap<String, Object>();
            claims.put("iss", iss);
            claims.put("user", toPartyUserLoginId);
            claims.put("delegatorName", delegator.getDelegatorName());
            claims.put("exp", exp);
            claims.put("iat", iat);









            pushWeChatMessageInfoMap.put("jumpUrl","http://www.lyndonspace.com:3400/WebManager/control/myOrderDetail?orderId="+orderId+"&tarjeta="+signer.sign(claims));
            //推微信订单状态
            dispatcher.runSync("pushOrderStatusInfo", pushWeChatMessageInfoMap);
        }


        GenericValue orderItem  =  EntityQuery.use(delegator).from("OrderItem").where("orderId",orderId).queryFirst();

        Map<String, Object> createMessageLogMap = new HashMap<String, Object>();

        createMessageLogMap.put("partyIdFrom", partyId);

        createMessageLogMap.put("message", "喂我告诉你,我已经发货了"+",物流公司是" + name + "!物流单号:" + code);

        createMessageLogMap.put("messageId", delegator.getNextSeqId("MessageLog"));

        createMessageLogMap.put("partyIdTo", orderCust.get("partyId"));

        createMessageLogMap.put("badge", "CHECK");

        createMessageLogMap.put("messageLogTypeId", "TEXT");

        createMessageLogMap.put("objectId", orderItem.get("productId"));


        createMessageLogMap.put("fromDate", org.apache.ofbiz.base.util.UtilDateTime.nowTimestamp());

        GenericValue msg = delegator.makeValue("MessageLog", createMessageLogMap);

        msg.create();


        return resultMap;
    }


    /**
     * createPerson PartyPostalAddress
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> createPersonPartyPostalAddress(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();

        Delegator delegator = dispatcher.getDelegator();

        Locale locale = (Locale) context.get("locale");


        GenericValue userLogin = (GenericValue) context.get("userLogin");

        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        Map<String, Object> resultMap = ServiceUtil.returnSuccess();

        String address1 = (String) context.get("address1");

        String address2 = (String) context.get("address2");

        String tarjeta = (String) context.get("tarjeta");

        String partyId = (String) userLogin.get("partyId");

        resultMap.put("tarjeta", tarjeta);


        //Create GEO
//        String geoDataSourceId = "GEOPT_";
//        Map<String, Object> createGeoPointOutMap = dispatcher.runSync("createGeoPoint", UtilMisc.toMap("userLogin", userLogin, "latitude", latitude,
//                "longitude", longitude, "information", address1, "dataSourceId", geoDataSourceId));
//        if (!ServiceUtil.isSuccess(createGeoPointOutMap)) {
//            return createGeoPointOutMap;
//        }
//
//        String geoPointId = (String) createGeoPointOutMap.get("geoPointId");
//        GenericValue partyGeoPoint = delegator.makeValue("PartyGeoPoint",
//                UtilMisc.toMap("partyId", cloudCardStroreId, "geoPointId", geoPointId, "fromDate", UtilDateTime.nowTimestamp()));
//        partyGeoPoint.create();
        GenericValue person = delegator.findOne("Person", UtilMisc.toMap("partyId", partyId), false);
        String firstName = (String) person.get("firstName");

        // 货运目的地址
        String contactMechPurposeTypeId = "SHIPPING_LOCATION";
        Map<String, Object> createPartyPostalAddressOutMap = dispatcher.runSync("createPartyPostalAddress",
                UtilMisc.toMap("userLogin", admin, "toName", firstName, "partyId", partyId, "countryGeoId", PeConstant.DEFAULT_GEO_COUNTRY, "city", PeConstant.DEFAULT_CITY_GEO_COUNTRY, "address1", address1 + " " + address2, "postalCode", PeConstant.DEFAULT_POST_CODE,
                        "contactMechPurposeTypeId", contactMechPurposeTypeId));
        String contactMechId = (String) createPartyPostalAddressOutMap.get("contactMechId");
        if (!ServiceUtil.isSuccess(createPartyPostalAddressOutMap)) {
            return createPartyPostalAddressOutMap;
        }

        //寄送账单地址
        dispatcher.runAsync("createPartyContactMechPurpose",
                UtilMisc.toMap("userLogin", admin, "contactMechId", contactMechId,
                        "contactMechPurposeTypeId", "BILLING_LOCATION"));

        //通信地址
        dispatcher.runAsync("createPartyContactMechPurpose",
                UtilMisc.toMap("userLogin", admin, "contactMechId", contactMechId,
                        "contactMechPurposeTypeId", "GENERAL_LOCATION"));

        //首选地址
        dispatcher.runAsync("createPartyContactMechPurpose",
                UtilMisc.toMap("userLogin", admin, "contactMechId", contactMechId,
                        "contactMechPurposeTypeId", "PRIMARY_LOCATION"));


        return resultMap;
    }


    /**
     * markOrOutMarkProduct
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> markOrOutMarkProduct(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();

        Delegator delegator = dispatcher.getDelegator();

        Locale locale = (Locale) context.get("locale");


        GenericValue userLogin = (GenericValue) context.get("userLogin");

        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        Map<String, Object> resultMap = ServiceUtil.returnSuccess();

        String productId = (String) context.get("productId");

        String markIt = (String) context.get("markIt");

        String partyId = (String) userLogin.get("partyId");

        Long placingCustCount =  EntityQuery.use(delegator).from("ProductRole").where("roleTypeId", "PLACING_CUSTOMER","productId", productId).queryCount();

        if (markIt.equals("true")) {

            if(placingCustCount==null || placingCustCount <= 0) {
                Long custCount =  EntityQuery.use(delegator).from("ProductRole").where("roleTypeId","CUSTOMER","productId", productId).queryCount();
                //此处如果对这个产品已经有客户角色,不再增加潜在客户角色
                if(custCount==null || custCount <= 0) {
                    dispatcher.runSync("addPartyToProduct", UtilMisc.toMap("userLogin", admin, "partyId", partyId, "productId", productId, "roleTypeId", "PLACING_CUSTOMER"));
                }
            }
        } else {
            GenericValue partyMarkRole = EntityQuery.use(delegator).from("ProductRole").where("partyId", partyId, "productId", productId, "roleTypeId", "PLACING_CUSTOMER").queryFirst();
            dispatcher.runSync("removePartyFromProduct", UtilMisc.toMap("userLogin", admin, "partyId", partyId, "productId", productId, "roleTypeId", "PLACING_CUSTOMER", "fromDate", partyMarkRole.get("fromDate")));
        }


        return resultMap;
    }


    public static boolean isUTF8(String key) {
        try {
            key.getBytes("utf-8");
            return true;
        } catch (UnsupportedEncodingException e) {
            return false;
        }
    }

    /**
     * pushMessage
     *
     * @param request
     * @param response
     * @return
     * @throws GenericServiceException
     * @throws GenericEntityException
     */
    public static String pushMessage(HttpServletRequest request, HttpServletResponse response)
            throws GenericServiceException, GenericEntityException {

        // Servlet Head

        Locale locale = UtilHttp.getLocale(request);

        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");

        Delegator delegator = (Delegator) request.getAttribute("delegator");

        HttpSession session = request.getSession();


        Map<String, Object> createMessageLogMap = new HashMap<String, Object>();

        Map<String, Object> pushWeChatMessageInfoMap = new HashMap<String, Object>();


        GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");


        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        String   text = (String) request.getParameter("text");

        System.out.println("########################################################################### text = " + text + "| is utf8 = " + isUTF8(text));

        String tarjeta = (String) request.getParameter("tarjeta");

        String objectId = (String) request.getParameter("objectId");

       // System.out.println("########################################################################### objectId = " + objectId);


        String partyIdTo = (String) request.getParameter("partyIdTo");


        System.out.println("########################################################################### partyIdTo = " + partyIdTo);

        String partyIdFrom = (String) request.getParameter("partyIdFrom");

        String messageLogTypeId = (String) request.getParameter("messageLogTypeId");

        //发送的是收款码?
        String pay_qr_code = (String) request.getParameter("pay_qr_code");

        //默认是文字类型
        if (UtilValidate.isEmpty(messageLogTypeId)) {
            messageLogTypeId = "TEXT";
        }


        if (!UtilValidate.isEmpty(messageLogTypeId) && messageLogTypeId.equals("IMAGE")) {

            try {

                ServletFileUpload dfu = new ServletFileUpload(new DiskFileItemFactory(10240, null));

                List<FileItem> items = dfu.parseRequest(request);


                int itemSize = 0;


                // if(null!=items){
                if (null != items) {

                    itemSize = items.size();


                    for (FileItem item : items) {


                        InputStream in = item.getInputStream();

                        String fileName = item.getName();


                        if (!UtilValidate.isEmpty(fileName)) {


                            long tm = System.currentTimeMillis();
                            String pictureKey = OSSUnit.uploadObject2OSS(in, item.getName(), OSSUnit.getOSSClient(), null,
                                    "personerp", PeConstant.CHAT_IMAGE_OSS_PATH, tm);

                            if (pictureKey != null && !pictureKey.equals("")) {
                                text = PeConstant.OSS_PATH + PeConstant.CHAT_IMAGE_OSS_PATH + tm + fileName.substring(fileName.indexOf("."));
                                pushMsgBase(objectId, partyIdFrom, partyIdTo, delegator, dispatcher, userLogin, text, pushWeChatMessageInfoMap, admin, createMessageLogMap, messageLogTypeId);
                            }
                        }

                    }
                }


            } catch (Exception e) {

                e.printStackTrace();

            }


        } else {
            //推送的不是图片,只要普通推送
            pushMsgBase(objectId, partyIdFrom, partyIdTo, delegator, dispatcher, userLogin, text, pushWeChatMessageInfoMap, admin, createMessageLogMap, messageLogTypeId);
        }

        //pay_qr_code 推送的是收款码,需要再推一次包含注入内容的确认信息。
//        if (!UtilValidate.isEmpty(pay_qr_code) && pay_qr_code.toLowerCase().equals("y") || !UtilValidate.isEmpty(pay_qr_code) && pay_qr_code.toLowerCase().equals("true")) {
//
//
//            System.out.println("*IN pay_qr_code Biz,Now System Double Push!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//
//
//            createMessageLogMap = new HashMap<String, Object>();
//
//
//            GenericValue orderPaymentPrefAndPayment = EntityQuery.use(delegator).from("OrderPaymentPrefAndPayment").where("orderId", objectId).queryFirst();
//
//
//            if (!UtilValidate.isEmpty(orderPaymentPrefAndPayment)) {
//                createMessageLogMap.put("message", "如果你已经付好了,请点击 <button id='setPaymentStatusBtn' class='button' style='font-size:17px;' onclick='setPaymentStatus(" + orderPaymentPrefAndPayment.get("paymentId") + "," + objectId + ");'>这个按钮</button> 通知我查收!");
//            } else {
//                createMessageLogMap.put("message", "如果你已经付好了,请点击 <button id='setPaymentStatusBtn' class='button' style='font-size:17px;' onclick='createPayment(" + objectId + ");'> 这个按钮 </button> 通知我查收!");
//            }
//
//
//            createMessageLogMap.put("partyIdFrom", "" + userLogin.get("partyId"));
//
//
//            createMessageLogMap.put("messageId", delegator.getNextSeqId("MessageLog"));
//
//            createMessageLogMap.put("partyIdTo", partyIdTo);
//
//
//            createMessageLogMap.put("badge", "false");
//
//            createMessageLogMap.put("messageLogTypeId", "TEXT");
//
//
//            if (!UtilValidate.isEmpty(objectId)) {
//                createMessageLogMap.put("objectId", objectId);
//            }
//
//            createMessageLogMap.put("fromDate", org.apache.ofbiz.base.util.UtilDateTime.nowTimestamp());
//
//            GenericValue msg = delegator.makeValue("MessageLog", createMessageLogMap);
//
//            msg.create();
//        }


        return "success";
    }


    /**
     * pushMsgBase
     *
     * @param objectId
     * @param partyIdFrom
     * @param partyIdTo
     * @param delegator
     * @param dispatcher
     * @param userLogin
     * @param text
     * @param pushWeChatMessageInfoMap
     * @param admin
     * @param createMessageLogMap
     * @return
     */
    public static String pushMsgBase(String objectId, String partyIdFrom, String partyIdTo, Delegator delegator, LocalDispatcher dispatcher,
                                     GenericValue userLogin, String text,
                                     Map<String, Object> pushWeChatMessageInfoMap, GenericValue admin, Map<String, Object> createMessageLogMap, String messageLogTypeId) throws GenericEntityException, GenericServiceException {
        pushWeChatMessageInfoMap.put("partyIdFrom",partyIdFrom);
        if (UtilValidate.isEmpty(partyIdFrom)) {
            partyIdFrom = (String) userLogin.get("partyId");
        }

        pushWeChatMessageInfoMap.put("userLogin", userLogin);

        pushWeChatMessageInfoMap.put("message", text);


        GenericValue toPartyUserLogin = EntityQuery.use(delegator).from("UserLogin").where("partyId", partyIdTo, "enabled", "Y").queryFirst();

        String toPartyUserLoginId = (String) toPartyUserLogin.get("userLoginId");


        long expirationTime = Long.valueOf(EntityUtilProperties.getPropertyValue("pe", "tarjeta.expirationTime", "172800L", delegator));
        String iss = EntityUtilProperties.getPropertyValue("pe", "tarjeta.issuer", delegator);
        String tokenSecret = EntityUtilProperties.getPropertyValue("pe", "tarjeta.secret", delegator);
        //开始时间
        final long iat = System.currentTimeMillis() / 1000L; // issued at claim
        //到期时间
        final long exp = iat + expirationTime;
        //生成
        final JWTSigner signer = new JWTSigner(tokenSecret);
        final HashMap<String, Object> claims = new HashMap<String, Object>();
        claims.put("iss", iss);
        claims.put("user", toPartyUserLoginId);
        claims.put("delegatorName", delegator.getDelegatorName());
        claims.put("exp", exp);
        claims.put("iat", iat);

        pushWeChatMessageInfoMap.put("tarjeta", signer.sign(claims));

        // 查询registrationID
        EntityCondition pConditions = EntityCondition.makeCondition("partyId", partyIdTo);
        List<EntityCondition> devTypeExprs = new ArrayList<EntityCondition>();
        devTypeExprs.add(EntityCondition.makeCondition("partyIdentificationTypeId", "JPUSH_ANDROID"));
        devTypeExprs.add(EntityCondition.makeCondition("partyIdentificationTypeId", "JPUSH_IOS"));
        EntityCondition devCondition = EntityCondition.makeCondition(devTypeExprs, EntityOperator.OR);
        pConditions = EntityCondition.makeCondition(pConditions, devCondition);

        List<GenericValue> partyIdentifications = delegator.findList("PartyIdentification", pConditions, null, UtilMisc.toList("-createdStamp"), null, false);

        GenericValue person = delegator.findOne("Person", UtilMisc.toMap("partyId", partyIdFrom), false);
        createMessageLogMap.put("message", text);
        if (person != null) {
            text = person.get("firstName") + ":" + text;
        }

        pushWeChatMessageInfoMap.put("firstName", person.get("firstName"));

        if (null != partyIdentifications && partyIdentifications.size() > 0) {


            GenericValue partyIdentification = (GenericValue) partyIdentifications.get(0);
            String jpushId = (String) partyIdentification.getString("idValue");
            String partyIdentificationTypeId = (String) partyIdentification.get("partyIdentificationTypeId");

            //查角标
            EntityCondition findValCondition = EntityCondition.makeCondition(
                    EntityCondition.makeCondition("badge", EntityOperator.EQUALS, "true"),
                    EntityCondition.makeCondition("partyIdTo", EntityOperator.EQUALS, partyIdTo));
            long count = delegator.findCountByCondition("MessageLog", findValCondition, null, null);

            String badege_str = count + "";

            try {
                dispatcher.runSync("pushNotifOrMessage", UtilMisc.toMap("userLogin", admin,"productId",objectId, "badge", badege_str, "message", "message", "content", text, "regId", jpushId, "deviceType", partyIdentificationTypeId, "sendType", "", "objectId", partyIdFrom));
            } catch (GenericServiceException e1) {
                Debug.logError(e1.getMessage(), module);
                return "error";
            }


        }


        if (!UtilValidate.isEmpty(partyIdFrom)) {
            createMessageLogMap.put("partyIdFrom", partyIdFrom);
        }

        createMessageLogMap.put("messageId", delegator.getNextSeqId("MessageLog"));

        createMessageLogMap.put("partyIdTo", partyIdTo);

        createMessageLogMap.put("badge", "true");

        createMessageLogMap.put("messageLogTypeId", messageLogTypeId);


        if (!UtilValidate.isEmpty(objectId)) {
            createMessageLogMap.put("objectId", objectId);
        }

        createMessageLogMap.put("fromDate", org.apache.ofbiz.base.util.UtilDateTime.nowTimestamp());

        GenericValue msg = delegator.makeValue("MessageLog", createMessageLogMap);

        msg.create();


        List<GenericValue> partyIdentificationList = EntityQuery.use(delegator).from("PartyIdentification").where("partyId", partyIdTo, "partyIdentificationTypeId", "WX_GZ_OPEN_ID").queryList();


        if (null != partyIdentificationList && partyIdentificationList.size() > 0) {
            //从产品找到卖家
            GenericValue payToParty = EntityQuery.use(delegator).from("ProductAndCategoryMember").where("productId", objectId).queryFirst();
            String payToPartyId = "";
            if (null != payToParty) {
                payToPartyId = (String) payToParty.get("payToPartyId");
            } else {
                //从订单找到卖家
                GenericValue orderFrom = EntityQuery.use(delegator).from("OrderRole").where("orderId", objectId, "roleTypeId", "BILL_FROM_VENDOR").queryFirst();

                payToPartyId = (String) orderFrom.get("partyId");
            }


            System.out.println("*PUSH WE CHAT GONG ZHONG PLATFORM !!!!!!!!!!!!!!!!!!!!!!!");

            Date date = new Date();

            SimpleDateFormat formatter;

            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String pushDate = "" + formatter.format(date);

            pushWeChatMessageInfoMap.put("date", pushDate);

            String openId = (String) partyIdentificationList.get(0).get("idValue");

            pushWeChatMessageInfoMap.put("openId", openId);

            pushWeChatMessageInfoMap.put("productId", objectId);

            pushWeChatMessageInfoMap.put("payToPartyId", partyIdTo);

            //推微信
            dispatcher.runSync("pushWeChatMessageInfo", pushWeChatMessageInfoMap);

        }
        return "success";
    }


    /**
     * push Message
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
//    public static Map<String, Object> pushMessage(DispatchContext dctx, Map<String, Object> context)
//            throws GenericEntityException, GenericServiceException {
//
//        // Service Head
//        LocalDispatcher dispatcher = dctx.getDispatcher();
//        Delegator delegator = dispatcher.getDelegator();
//        Locale locale = (Locale) context.get("locale");
//        Map<String,Object> createMessageLogMap = new HashMap<String, Object>();
//        Map<String,Object> pushWeChatMessageInfoMap = new HashMap<String, Object>();
//        GenericValue userLogin = (GenericValue) context.get("userLogin");
//        // Admin Do Run Service
//        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));
//
//        String text = (String) context.get("text");
//
//        String tarjeta       = (String) context.get("tarjeta");
//
//        String objectId = (String) context.get("objectId");
//
//        String partyIdTo = (String) context.get("partyIdTo");
//
//        String partyIdFrom = (String) context.get("partyIdFrom");
//
//        if(UtilValidate.isEmpty(partyIdFrom)){
//            partyIdFrom = (String) userLogin.get("partyId");
//        }
//
//        pushWeChatMessageInfoMap.put("userLogin",userLogin);
//
//        pushWeChatMessageInfoMap.put("message",text);
//
//
//        GenericValue toPartyUserLogin = EntityQuery.use(delegator).from("UserLogin").where("partyId", partyIdTo,"enabled","Y").queryFirst();
//
//        String toPartyUserLoginId = (String) toPartyUserLogin.get("userLoginId");
//
//
//
//        long expirationTime = Long.valueOf(EntityUtilProperties.getPropertyValue("pe", "tarjeta.expirationTime", "172800L", delegator));
//        String iss = EntityUtilProperties.getPropertyValue("pe", "tarjeta.issuer", delegator);
//        String tokenSecret = EntityUtilProperties.getPropertyValue("pe", "tarjeta.secret", delegator);
//        //开始时间
//        final long iat = System.currentTimeMillis() / 1000L; // issued at claim
//        //到期时间
//        final long exp = iat + expirationTime;
//        //生成
//        final JWTSigner signer = new JWTSigner(tokenSecret);
//        final HashMap<String, Object> claims = new HashMap<String, Object>();
//        claims.put("iss", iss);
//        claims.put("user", toPartyUserLoginId);
//        claims.put("delegatorName", delegator.getDelegatorName());
//        claims.put("exp", exp);
//        claims.put("iat", iat);
//
//        pushWeChatMessageInfoMap.put("tarjeta",signer.sign(claims));
//
//
//        System.out.println("========================================= partyIdTo = " +partyIdTo);
//
//        // 查询registrationID
//        EntityCondition pConditions = EntityCondition.makeCondition("partyId", partyIdTo);
//        List<EntityCondition> devTypeExprs = new ArrayList<EntityCondition>();
//        devTypeExprs.add(EntityCondition.makeCondition("partyIdentificationTypeId", "JPUSH_ANDROID"));
//        devTypeExprs.add(EntityCondition.makeCondition("partyIdentificationTypeId", "JPUSH_IOS"));
//        EntityCondition devCondition = EntityCondition.makeCondition(devTypeExprs, EntityOperator.OR);
//        pConditions = EntityCondition.makeCondition(pConditions, devCondition);
//
//        List<GenericValue> partyIdentifications =  delegator.findList("PartyIdentification", pConditions, null, UtilMisc.toList("-createdStamp"), null, false);
//
//        GenericValue person = delegator.findOne("Person",UtilMisc.toMap("partyId",partyIdFrom),false);
//        createMessageLogMap.put("message",text);
//        if(person!=null){
//            text = person.get("firstName")+":"+text;
//        }
//
//        pushWeChatMessageInfoMap.put("firstName",person.get("firstName"));
//
//        if(null != partyIdentifications && partyIdentifications.size()>0){
//
//
//            GenericValue  partyIdentification = (GenericValue) partyIdentifications.get(0);
//            String jpushId = (String) partyIdentification.getString("idValue");
//            String partyIdentificationTypeId = (String) partyIdentification.get("partyIdentificationTypeId");
//
//            //查角标
//            EntityCondition findValCondition = EntityCondition.makeCondition(
//                    EntityCondition.makeCondition("badge", EntityOperator.EQUALS, "true"),
//                    EntityCondition.makeCondition("partyIdTo", EntityOperator.EQUALS, partyIdTo));
//            long count = delegator.findCountByCondition("MessageLog", findValCondition, null, null);
//
//            String badege_str = count+"";
//
//            try{
//                dispatcher.runSync("pushNotifOrMessage",UtilMisc.toMap("userLogin",admin,"badge",badege_str,"message","message","content",text,"regId",jpushId,"deviceType",partyIdentificationTypeId,"sendType","","objectId",partyIdFrom));
//            } catch (GenericServiceException e1) {
//                Debug.logError(e1.getMessage(), module);
//                return ServiceUtil.returnError(UtilProperties.getMessage(resourceError, "JPushError", locale));
//            }
//
//
//        }
//
//
//
//
//        if(!UtilValidate.isEmpty(partyIdFrom)) {
//            createMessageLogMap.put("partyIdFrom", partyIdFrom);
//        }
//
//        createMessageLogMap.put("messageId", delegator.getNextSeqId("MessageLog"));
//
//        createMessageLogMap.put("partyIdTo",partyIdTo);
//
//        createMessageLogMap.put("badge","true");
//
//        if(!UtilValidate.isEmpty(objectId)){
//            createMessageLogMap.put("objectId",objectId);
//        }
//
//        createMessageLogMap.put("fromDate",org.apache.ofbiz.base.util.UtilDateTime.nowTimestamp());
//
//        GenericValue msg = delegator.makeValue("MessageLog", createMessageLogMap);
//
//        msg.create();
//
//
//
//
//
//
//
//
//        List<GenericValue> partyIdentificationList = EntityQuery.use(delegator).from("PartyIdentification").where("partyId", partyIdTo, "partyIdentificationTypeId", "WX_GZ_OPEN_ID").queryList();
//
//
//        if (null != partyIdentificationList && partyIdentificationList.size() > 0) {
//
//            GenericValue payToParty = EntityQuery.use(delegator).from("ProductAndCategoryMember").where("productId", objectId).queryFirst();
//
//            String payToPartyId = (String) payToParty.get("payToPartyId");
//
//            System.out.println("*PUSH WE CHAT GONG ZHONG PLATFORM !!!!!!!!!!!!!!!!!!!!!!!");
//
//            Date date = new Date();
//
//            SimpleDateFormat formatter;
//
//            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//            String pushDate = ""+formatter.format(date);
//
//            pushWeChatMessageInfoMap.put("date",pushDate);
//
//            String openId = (String) partyIdentificationList.get(0).get("idValue");
//
//            pushWeChatMessageInfoMap.put("openId",openId);
//
//            pushWeChatMessageInfoMap.put("productId",objectId);
//
//            pushWeChatMessageInfoMap.put("payToPartyId",payToPartyId);
//
//            //推微信
//            dispatcher.runSync("pushWeChatMessageInfo",pushWeChatMessageInfoMap);
//
//        } else {
//
//        }
//
//
//
//
//
//
//        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
//
//        return resultMap;
//    }


    /**
     * Affirm Order
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> affirmOrder(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");

        GenericValue userLogin = (GenericValue) context.get("userLogin");
        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        String orderId = (String) context.get("orderId");

        Map<String, Object> updateOrderStatusInMap = new HashMap<String, Object>();
        updateOrderStatusInMap.put("userLogin", admin);
        updateOrderStatusInMap.put("orderId", orderId);
        updateOrderStatusInMap.put("statusId", PeConstant.ORDER_APPROVED_STATUS_ID);

        Map<String, Object> updateOrderStatusOutMap = dispatcher.runSync("changeOrderStatus", updateOrderStatusInMap);

        if (ServiceUtil.isError(updateOrderStatusOutMap)) {
            return updateOrderStatusOutMap;
        }


        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        return resultMap;
    }


    /**
     * salesDiscontinuation
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> salesDiscontinuation(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");

        GenericValue userLogin = (GenericValue) context.get("userLogin");
        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        String productId = (String) context.get("productId");

        Map<String, Object> updateProductInMap = new HashMap<String, Object>();
        updateProductInMap.put("userLogin", admin);
        updateProductInMap.put("productId", productId);
        updateProductInMap.put("salesDiscontinuationDate", new Timestamp(new Date().getTime()));

        Map<String, Object> updateProductOutMap = dispatcher.runSync("updateProduct", updateProductInMap);

        if (ServiceUtil.isError(updateProductOutMap)) {
            return updateProductOutMap;
        }


        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        return resultMap;
    }


    /**
     * Matching Contact
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> matchingContact(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");

        GenericValue userLogin = (GenericValue) context.get("userLogin");

        String partyId = (String) userLogin.get("partyId");

        String contacts = (String) context.get("contacts");

        // Split
        String[] contactsArray = contacts.split(",");

        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        int count = 0;

        if (!UtilValidate.isEmpty(contactsArray)) {
            count = forSearchPeUsers(partyId, delegator, dispatcher, contactsArray, admin);
        }


        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        resultMap.put("count", count + "");
        return resultMap;
    }

    /**
     * Search User Is Exsits ?
     *
     * @param delegator
     * @param dispatcher
     * @param contactsArray
     * @param admin
     * @return
     */
    private static int forSearchPeUsers(String partyIdFrom, Delegator delegator, LocalDispatcher dispatcher, String[] contactsArray, GenericValue admin) throws GenericEntityException, GenericServiceException {

        int count = 0;

        for (int i = 0; i < contactsArray.length; i++) {
            Map<String, Object> rowMap = new HashMap<String, Object>();

            String tel = contactsArray[i].substring(contactsArray[i].indexOf(":") + 1).replace("-", "").replace("\"", "");
            tel = tel.replaceAll("]", "");

            String nickName = contactsArray[i].substring(1, contactsArray[i].indexOf(":"));
            GenericValue teleContact = EntityQuery.use(delegator).from("TelecomNumberAndPartyView").where("contactNumber", tel).queryFirst();


            if (null != teleContact) {
                String partyId = (String) teleContact.get("partyId");
                Map<String, Object> addPartyRelationShipInMap = new HashMap<String, Object>();
                addPartyRelationShipInMap.put("userLogin", admin);
                addPartyRelationShipInMap.put("partyIdFrom", partyId);
                addPartyRelationShipInMap.put("partyIdTo", partyIdFrom);
                addPartyRelationShipInMap.put("relationEnum", "CONTACT");
                dispatcher.runSync("addPartyRelationShip", addPartyRelationShipInMap);
                count++;
            }
            // createActivityMembers(userLogin, workEffortId, nickName, tel, dispatcher, delegator, false, null, null, null, null, null);
        }


        return count;
    }


    /**
     * checkAddress
     * @param request
     * @param response
     * @return
     * @throws GenericServiceException
     * @throws GenericEntityException
     */
    public static String checkAddress(HttpServletRequest request, HttpServletResponse response)
            throws GenericServiceException, GenericEntityException {

        // Servlet Head

        Locale locale = UtilHttp.getLocale(request);

        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");

        Delegator delegator = (Delegator) request.getAttribute("delegator");

        HttpSession session = request.getSession();
        // productId
        String messageId = (String) request.getParameter("messageId");
        System.out.println("*messageId="+messageId);
        GenericValue messageLog =  EntityQuery.use(delegator).from("MessageLog").where("messageId", messageId).queryFirst();
        messageLog.set("message"," "+ messageLog.get("message")+"");
        messageLog.store();
        return "success";
    }




    /**
     * checkSubscribe
     *
     * @param request
     * @param response
     * @return
     * @throws GenericServiceException
     * @throws GenericEntityException
     */
    public static String checkSubscribe(HttpServletRequest request, HttpServletResponse response)
            throws GenericServiceException, GenericEntityException {

        // Servlet Head

        Locale locale = UtilHttp.getLocale(request);

        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");

        Delegator delegator = (Delegator) request.getAttribute("delegator");

        HttpSession session = request.getSession();
        // productId
        String productId = (String) request.getParameter("productId");


        request.setAttribute("subscribe", "");
        return "success";
    }


    /**
     * Query Message
     *
     * @param request
     * @param response
     * @return
     * @throws GenericServiceException
     * @throws GenericEntityException
     */
    public static String queryMessage(HttpServletRequest request, HttpServletResponse response)
            throws GenericServiceException, GenericEntityException {

        // Servlet Head

        Locale locale = UtilHttp.getLocale(request);

        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");

        Delegator delegator = (Delegator) request.getAttribute("delegator");

        HttpSession session = request.getSession();

        GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");

        GenericValue admin = delegator.findOne("UserLogin", UtilMisc.toMap("userLoginId", "admin"), false);

        // productId
        String productId = (String) request.getParameter("productId");


        return "success";
    }


    /**
     * add Product Content
     *
     * @param request
     * @param response
     * @return
     * @throws GenericServiceException
     * @throws GenericEntityException
     */
    public static String addProductContent(HttpServletRequest request, HttpServletResponse response)
            throws GenericServiceException, GenericEntityException {

        // Servlet Head

        Locale locale = UtilHttp.getLocale(request);

        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");

        Delegator delegator = (Delegator) request.getAttribute("delegator");

        HttpSession session = request.getSession();

        GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");

        String partyId = (String) userLogin.get("partyId");


        GenericValue admin = delegator.findOne("UserLogin", UtilMisc.toMap("userLoginId", "admin"), false);

        // productId
        String productId = (String) request.getParameter("productId");


        String description = (String) request.getParameter("description");


        GenericValue product = delegator.findOne("Product", UtilMisc.toMap("productId", productId), false);

        product.set("description", description);

        product.store();

//        String alipayaccount = (String) request.getParameter("alipayaccount");
//
//        String wechatpayaccount = (String) request.getParameter("wechatpayaccount");
//
//
//        if (!UtilValidate.isEmpty(alipayaccount)) {
//            // Create Default Pay Method To Party
//            GenericValue newPayMethod = delegator.makeValue("PaymentMethod");
//            newPayMethod.set("paymentMethodId", delegator.getNextSeqId("PaymentMethod"));
//            newPayMethod.set("partyId", partyId);
//            newPayMethod.set("paymentMethodTypeId", "MEDIATION_PAY");
//            newPayMethod.set("description", "支付宝账号:"+alipayaccount);
//            newPayMethod.create();
//        }
//        if (!UtilValidate.isEmpty(wechatpayaccount)) {
//            GenericValue newPayMethod2 = delegator.makeValue("PaymentMethod");
//            newPayMethod2.set("paymentMethodId", delegator.getNextSeqId("PaymentMethod"));
//            newPayMethod2.set("partyId", partyId);
//            newPayMethod2.set("paymentMethodTypeId", "MEDIATION_PAY");
//            newPayMethod2.set("description", "微信账号:" + wechatpayaccount);
//            newPayMethod2.create();
//        }


        try {

            ServletFileUpload dfu = new ServletFileUpload(new DiskFileItemFactory(10240, null));

            List<FileItem> items = dfu.parseRequest(request);


            int itemSize = 0;


            // if(null!=items){
            if (null != items) {
                itemSize = items.size();

                int count = 1;

                for (FileItem item : items) {


                    InputStream in = item.getInputStream();

                    String fileName = item.getName();


                    if (!UtilValidate.isEmpty(fileName)) {


                        long tm = System.currentTimeMillis();
                        String pictureKey = OSSUnit.uploadObject2OSS(in, item.getName(), OSSUnit.getOSSClient(), null,
                                "personerp", PeConstant.PRODUCT_OSS_PATH, tm);

                        if (pictureKey != null && !pictureKey.equals("") && count <= 4) {
                            //创建产品内容和数据资源附图
                            createProductContentAndDataResource(delegator, dispatcher, admin, productId, "", "https://personerp.oss-cn-hangzhou.aliyuncs.com/" + PeConstant.PRODUCT_OSS_PATH + tm + fileName.substring(fileName.indexOf(".")), count);
                            count++;
                        }
                    }

                }
            }


        } catch (Exception e) {

            e.printStackTrace();

        }

        return "success";

    }

    /**
     * 创建产品内容及数据资源
     *
     * @param dispatcher
     * @param admin
     * @param productId
     * @param discription
     * @param dataInfo
     */
    private static void createProductContentAndDataResource(Delegator delegator, LocalDispatcher dispatcher, GenericValue admin, String productId, String description, String dataInfo, int count) throws GenericServiceException {

        // Create Content
        String contentTypeId = "ADDITIONAL_IMAGE_" + count;
        Map<String, Object> resultMap1 = dispatcher.runSync("createContent", UtilMisc.toMap("userLogin", admin));
        String contentId = (String) resultMap1.get("contentId");
        dispatcher.runSync("createProductContent", UtilMisc.toMap("userLogin", admin, "productContentTypeId", contentTypeId, "productId", productId, "contentId", contentId));

        // Create DataResource
        Map<String, Object> dataResourceCtx = new HashMap<String, Object>();
        dataResourceCtx.put("objectInfo", dataInfo);
        dataResourceCtx.put("dataResourceName", description);
        dataResourceCtx.put("userLogin", admin);
        dataResourceCtx.put("dataResourceTypeId", "SHORT_TEXT");
        dataResourceCtx.put("mimeTypeId", "text/html");
        Map<String, Object> dataResourceResult = new HashMap<String, Object>();
        try {
            dataResourceResult = dispatcher.runSync("createDataResource", dataResourceCtx);
        } catch (GenericServiceException e) {
            Debug.logError(e, module);

        }

        // Update Content
        Map<String, Object> contentCtx = new HashMap<String, Object>();
        contentCtx.put("contentId", contentId);
        contentCtx.put("dataResourceId", dataResourceResult.get("dataResourceId"));
        contentCtx.put("userLogin", admin);
        try {
            dispatcher.runSync("updateContent", contentCtx);
        } catch (GenericServiceException e) {
            Debug.logError(e, module);

        }

    }


    /**
     * release Resource
     *
     * @param request
     * @param response
     * @return
     * @throws GenericServiceException
     */
    public static String releaseResource(HttpServletRequest request, HttpServletResponse response)
            throws GenericServiceException, GenericEntityException {

        // Servlet Head

        Locale locale = UtilHttp.getLocale(request);

        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");

        Delegator delegator = (Delegator) request.getAttribute("delegator");

        HttpSession session = request.getSession();

        GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");

        String partyId = (String) userLogin.get("partyId");


        GenericValue admin = delegator.findOne("UserLogin", UtilMisc.toMap("userLoginId", "admin"), false);

        // 分类Id
        String productCategoryId = (String) request.getParameter("productCategoryId");

        // 价格
        String productPrice = (String) request.getParameter("productPrice");

        String productName = (String) request.getParameter("productName");

        String defaultImageUrl = (String) request.getParameter("defaultImageUrl");

        String quantityTotalStr = (String) request.getParameter("quantityTotal");

        BigDecimal quantityTotal = new BigDecimal("99999999");


        String description   = (String) request.getParameter("description");

        System.out.println("*description = " + description);

        if (!UtilValidate.isEmpty(quantityTotalStr)) {
            quantityTotal = new BigDecimal(quantityTotalStr);
        }


        // Default Price
        BigDecimal price = BigDecimal.ZERO;

        if (!UtilValidate.isEmpty(productPrice)) {
            price = new BigDecimal(productPrice);
        }

        // 不分梨用户
        if (null == productCategoryId) {
            productCategoryId = createPersonStoreAndCatalogAndCategory(locale, admin, delegator, dispatcher, partyId);

        }


        //创建产品Map
        Map<String, Object> createProductInMap = new HashMap<String, Object>();
        createProductInMap.put("userLogin", admin);
        long ctm = System.currentTimeMillis();
        createProductInMap.put("internalName", partyId + "_" + ctm);
        createProductInMap.put("productName", productName);
        createProductInMap.put("productTypeId", PeConstant.PRODUCT_TYPE_ID);
        createProductInMap.put("description",description );

        String productId = "";

        if (!UtilValidate.isEmpty(defaultImageUrl)) {
            createProductInMap.put("smallImageUrl", defaultImageUrl);
            createProductInMap.put("detailImageUrl", defaultImageUrl);
        } else {

            try {

                ServletFileUpload dfu = new ServletFileUpload(new DiskFileItemFactory(10240, null));

                List<FileItem> items = dfu.parseRequest(request);


                int itemSize = 0;
                int index = 0;

                // if(null!=items){
                if (null != items) {
                    itemSize = items.size();


                    for (FileItem item : items) {


                        InputStream in = item.getInputStream();

                        String fileName = item.getName();


                        if (!UtilValidate.isEmpty(fileName) && index ==0) {


                            long tm = System.currentTimeMillis();
                            String pictureKey = OSSUnit.uploadObject2OSS(in, item.getName(), OSSUnit.getOSSClient(), null,
                                    "personerp", PeConstant.PRODUCT_OSS_PATH, tm);

                            if (pictureKey != null && !pictureKey.equals("")) {


                                createProductInMap.put("smallImageUrl", PeConstant.OSS_PATH + PeConstant.PRODUCT_OSS_PATH + tm + fileName.substring(fileName.indexOf(".")) + "?x-oss-process=image/resize,m_pad,h_50,w_50");
                                createProductInMap.put("detailImageUrl", PeConstant.OSS_PATH + PeConstant.PRODUCT_OSS_PATH + tm + fileName.substring(fileName.indexOf(".")));
                                // Create Product
                                Map<String, Object> createProductOutMap = dispatcher.runSync("createProduct", createProductInMap);
                                productId = (String) createProductOutMap.get("productId");
                            }
                        }
                        if (!UtilValidate.isEmpty(fileName) && index > 0) {
                            long tm = System.currentTimeMillis();
                            String pictureKey = OSSUnit.uploadObject2OSS(in, item.getName(), OSSUnit.getOSSClient(), null,
                                    "personerp", PeConstant.PRODUCT_OSS_PATH, tm);

                            if (pictureKey != null && !pictureKey.equals("")) {


                                    //创建产品内容和数据资源附图
                                    createProductContentAndDataResource(delegator, dispatcher, admin, productId, "", "https://personerp.oss-cn-hangzhou.aliyuncs.com/" + PeConstant.PRODUCT_OSS_PATH + tm + fileName.substring(fileName.indexOf(".")), index);


                            }
                        }
                        index++;
                    }
                }


            } catch (Exception e) {

                e.printStackTrace();

            }


        }





        // Create Product Price
        Map<String, Object> createProductPriceInMap = new HashMap<String, Object>();
        createProductPriceInMap.put("userLogin", admin);
        createProductPriceInMap.put("productId", productId);
        createProductPriceInMap.put("currencyUomId", PeConstant.DEFAULT_CURRENCY_UOM_ID);
        createProductPriceInMap.put("price", price);
        createProductPriceInMap.put("productPricePurposeId", PeConstant.PRODUCT_PRICE_DEFAULT_PURPOSE);
        createProductPriceInMap.put("productPriceTypeId", PeConstant.PRODUCT_PRICE_DEFAULT_TYPE_ID);
        createProductPriceInMap.put("productStoreGroupId", PeConstant.NA);
        dispatcher.runSync("createProductPrice", createProductPriceInMap);


        // Add Product To Category 产品 关联 分类
        Map<String, Object> addProductToCategoryInMap = new HashMap<String, Object>();
        addProductToCategoryInMap.put("userLogin", admin);
        addProductToCategoryInMap.put("productId", productId);
        addProductToCategoryInMap.put("productCategoryId", productCategoryId);
        dispatcher.runSync("addProductToCategory", addProductToCategoryInMap);


        GenericValue facility = EntityQuery.use(delegator).from("Facility").where("ownerPartyId", partyId).queryFirst();

        // 为产品创建库存量
        Map<String, Object> receiveInventoryProductIn = UtilMisc.toMap("userLogin", userLogin,
                "facilityId", (String) facility.get("facilityId"),
                "inventoryItemTypeId", PeConstant.DEFAULT_INV_ITEM,
                "productId", productId,
                "description ", "卖家发布产品时的录入库存",
                "quantityAccepted", quantityTotal,
                "quantityRejected", BigDecimal.ZERO,
                "unitCost", price,
                "ownerPartyId", partyId,
                "partyId", partyId,
                "uomId", PeConstant.DEFAULT_CURRENCY_UOM_ID,
                "currencyUomId", PeConstant.DEFAULT_CURRENCY_UOM_ID);

        Map<String, Object> receiveInventoryProductOut = dispatcher.runSync("receiveInventoryProduct", receiveInventoryProductIn
        );

        dispatcher.runSync("createProductAttribute",UtilMisc.toMap("productId",productId,"attrName","quantityAccepted","attrValue",quantityTotal+""));

        //  System.out.println("********************************************createInventoryItemOut="+receiveInventoryProductOut);

        request.setAttribute("productId", productId);

        return "success";

    }


    /**
     * Add Party RelationShip
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> addPartyRelationShip(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        String partyIdFrom = (String) context.get("partyIdFrom");
        String partyIdTo = (String) context.get("partyIdTo");
        String relationEnum = (String) context.get("relationEnum");

        Map<String, Object> serviceResultMap = null;
        switch (relationType.getRelationType(relationEnum)) {
            case C2CRSS:
                serviceResultMap = createRelationC2CRSS(delegator, dispatcher, admin, partyIdFrom, partyIdTo);
                if (ServiceUtil.isError(serviceResultMap)) {
                    return serviceResultMap;
                }
            case CONTACT:
                serviceResultMap = createRelationCONTACT(delegator, dispatcher, admin, partyIdFrom, partyIdTo);
                if (ServiceUtil.isError(serviceResultMap)) {
                    return serviceResultMap;
                }
        }


        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        return resultMap;
    }

    /**
     * 建立C2C资源服务双方关系
     *
     * @param delegator
     * @param dispatcher
     * @param admin
     * @param partyIdFrom
     * @param partyIdTo
     */
    private static Map<String, Object> createRelationC2CRSS(Delegator delegator, LocalDispatcher dispatcher, GenericValue admin, String partyIdTo, String partyIdFrom) throws GenericServiceException {

        String partyRelationshipTypeId = "";
        // Create Supplier Relation
        partyRelationshipTypeId = PeConstant.SUPPLIER;
        String roleTypeIdFrom = "BILL_TO_CUSTOMER";
        String roleTypeIdTo = "SHIP_FROM_VENDOR";
        Map<String, Object> createPartyRelationshipInMap = new HashMap<String, Object>();
        createPartyRelationshipInMap.put("roleTypeIdFrom", roleTypeIdFrom);
        createPartyRelationshipInMap.put("roleTypeIdTo", roleTypeIdTo);
        createPartyRelationshipInMap.put("userLogin", admin);
        createPartyRelationshipInMap.put("partyIdFrom", partyIdTo);
        createPartyRelationshipInMap.put("partyIdTo", partyIdFrom);
        createPartyRelationshipInMap.put("partyRelationshipTypeId", partyRelationshipTypeId);
        Map<String, Object> createPartyRelationshipOutMap = dispatcher.runSync("createPartyRelationship", createPartyRelationshipInMap);

        if (ServiceUtil.isError(createPartyRelationshipOutMap)) {
            return createPartyRelationshipOutMap;
        }

        partyRelationshipTypeId = PeConstant.CUSTOMER;
        createPartyRelationshipInMap = new HashMap<String, Object>();

        createPartyRelationshipInMap.put("roleTypeIdFrom", roleTypeIdTo);
        createPartyRelationshipInMap.put("roleTypeIdTo", roleTypeIdFrom);

        createPartyRelationshipInMap.put("userLogin", admin);
        createPartyRelationshipInMap.put("partyIdFrom", partyIdFrom);
        createPartyRelationshipInMap.put("partyIdTo", partyIdTo);
        createPartyRelationshipInMap.put("partyRelationshipTypeId", partyRelationshipTypeId);
        createPartyRelationshipOutMap = dispatcher.runSync("createPartyRelationship", createPartyRelationshipInMap);

        if (ServiceUtil.isError(createPartyRelationshipOutMap)) {
            return createPartyRelationshipOutMap;
        }


        return ServiceUtil.returnSuccess();
    }


    /**
     * createRelationCONTACT
     *
     * @param delegator
     * @param dispatcher
     * @param admin
     * @param partyIdTo
     * @param partyIdFrom
     * @return
     * @throws GenericServiceException
     */
    private static Map<String, Object> createRelationCONTACT(Delegator delegator, LocalDispatcher dispatcher, GenericValue admin, String partyIdTo, String partyIdFrom) throws GenericServiceException {

        String partyRelationshipTypeId = "";
        // Create Supplier Relation
        partyRelationshipTypeId = PeConstant.CONTACT;

        Map<String, Object> createPartyRelationshipInMap = new HashMap<String, Object>();

        createPartyRelationshipInMap.put("userLogin", admin);
        createPartyRelationshipInMap.put("partyIdFrom", partyIdTo);
        createPartyRelationshipInMap.put("partyIdTo", partyIdFrom);
        createPartyRelationshipInMap.put("partyRelationshipTypeId", partyRelationshipTypeId);
        Map<String, Object> createPartyRelationshipOutMap = dispatcher.runSync("createPartyRelationship", createPartyRelationshipInMap);

        if (ServiceUtil.isError(createPartyRelationshipOutMap)) {
            return createPartyRelationshipOutMap;
        }

        partyRelationshipTypeId = PeConstant.CONTACT;
        createPartyRelationshipInMap = new HashMap<String, Object>();

        createPartyRelationshipInMap.put("userLogin", admin);
        createPartyRelationshipInMap.put("partyIdFrom", partyIdFrom);
        createPartyRelationshipInMap.put("partyIdTo", partyIdTo);
        createPartyRelationshipInMap.put("partyRelationshipTypeId", partyRelationshipTypeId);
        createPartyRelationshipOutMap = dispatcher.runSync("createPartyRelationship", createPartyRelationshipInMap);

        if (ServiceUtil.isError(createPartyRelationshipOutMap)) {
            return createPartyRelationshipOutMap;
        }


        return ServiceUtil.returnSuccess();
    }

    /**
     * Update PersonInfo
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
//    public static Map<String, Object> updatePersonInfo(DispatchContext dctx, Map<String, Object> context)
//            throws GenericEntityException, GenericServiceException {
//
//        // Service Head
//        LocalDispatcher dispatcher = dctx.getDispatcher();
//        Delegator delegator = dispatcher.getDelegator();
//        Locale locale = (Locale) context.get("locale");
//        GenericValue userLogin = (GenericValue) context.get("userLogin");
//
//
//
//        // Scope Param
//        String partyId = (String) userLogin.get("partyId");
//        String firstName = (String) context.get("firstName");
////        if(EmojiFilter.containsEmoji(firstName)){
////            //包含emoji表情
////            firstName = EmojiHandler.encodeJava(firstName);
////        }
//        String gender = (String) context.get("gender");
//
//        dispatcher.runSync("updatePerson",UtilMisc.toMap("userLogin",userLogin,
//                "partyId",partyId,"firstName",firstName,"lastName","NA","gender",gender));
//
//
//
//        // Service Foot
//        Map<String, Object> result = ServiceUtil.returnSuccess();
//
//        return result;
//    }


    /**
     * update Person PaymentQrCode
     *
     * @param request
     * @param response
     * @return
     * @throws GenericServiceException
     * @throws GenericEntityException
     */
    public static String updatePersonPaymentQrCode(HttpServletRequest request, HttpServletResponse response)
            throws GenericServiceException, GenericEntityException {

        // Servlet Head

        Locale locale = UtilHttp.getLocale(request);

        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");

        Delegator delegator = (Delegator) request.getAttribute("delegator");

        HttpSession session = request.getSession();

        GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");

        String partyId = (String) userLogin.get("partyId");

        //QR CODE
        String partyContentType = (String) request.getParameter("partyContentType");

        GenericValue admin = delegator.findOne("UserLogin", UtilMisc.toMap("userLoginId", "admin"), false);

        try {

            ServletFileUpload dfu = new ServletFileUpload(new DiskFileItemFactory(10240, null));

            List<FileItem> items = dfu.parseRequest(request);


            int itemSize = 0;

            if (null != items) {
                itemSize = items.size();
            }
            if (null != dfu && null != items) {


                for (FileItem item : items) {


                    InputStream in = item.getInputStream();

                    String fileName = item.getName();


                    if (!UtilValidate.isEmpty(fileName)) {

                        long tm = System.currentTimeMillis();

                        String pictureKey = OSSUnit.uploadObject2OSS(in, item.getName(), OSSUnit.getOSSClient(), null,
                                "personerp", PeConstant.DEFAULT_RR_CODE_DISK, tm);

                        if (pictureKey != null && !pictureKey.equals("")) {

                            createContentAndDataResource(partyId, delegator, admin, dispatcher, pictureKey, "https://personerp.oss-cn-hangzhou.aliyuncs.com/" + PeConstant.DEFAULT_RR_CODE_DISK + tm + fileName.substring(fileName.indexOf(".")), partyContentType);

                        }
                    }

                }
            }
        } catch (Exception e) {

            e.printStackTrace();

        }

        return "success";

    }


    /**
     * upload Head Portrait
     *
     * @param request
     * @param response
     * @return
     * @throws GenericServiceException
     */
    public static String updatePersonInfo(HttpServletRequest request, HttpServletResponse response)
            throws GenericServiceException, GenericEntityException {

        // Servlet Head

        Locale locale = UtilHttp.getLocale(request);

        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");

        Delegator delegator = (Delegator) request.getAttribute("delegator");

        HttpSession session = request.getSession();

        GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");

        String partyId = (String) userLogin.get("partyId");

        String firstName = (String) request.getParameter("firstName");

        String noteInfo = (String) request.getParameter("noteInfo");

        String gender = (String) request.getParameter("gender");

        if (!UtilValidate.isEmpty(gender)) {
            gender = "M";
        }


        GenericValue admin = delegator.findOne("UserLogin", UtilMisc.toMap("userLoginId", "admin"), false);


        if (!UtilValidate.isEmpty(noteInfo)) {
            dispatcher.runSync("createPartyNote", UtilMisc.toMap("userLogin", admin,
                    "partyId", partyId, "note", noteInfo, "noteName", "个人说明"));
        }

        dispatcher.runSync("updatePerson", UtilMisc.toMap("userLogin", admin,
                "partyId", partyId, "firstName", firstName, "lastName", "NA", "gender", gender));


        try {

            ServletFileUpload dfu = new ServletFileUpload(new DiskFileItemFactory(10240, null));

            List<FileItem> items = dfu.parseRequest(request);


            int itemSize = 0;

            if (null != items) {
                itemSize = items.size();
            }
            if (null != dfu && null != items) {


                for (FileItem item : items) {


                    InputStream in = item.getInputStream();

                    String fileName = item.getName();


                    if (!UtilValidate.isEmpty(fileName)) {


                        long tm = System.currentTimeMillis();
                        String pictureKey = OSSUnit.uploadObject2OSS(in, item.getName(), OSSUnit.getOSSClient(), null,
                                "personerp", PeConstant.DEFAULT_HEAD_DISK, tm);

                        if (pictureKey != null && !pictureKey.equals("")) {

                            createContentAndDataResource(partyId, delegator, admin, dispatcher, pictureKey, "https://personerp.oss-cn-hangzhou.aliyuncs.com/" + PeConstant.DEFAULT_HEAD_DISK + tm + fileName.substring(fileName.indexOf(".")), null);

                        }
                    }

                }
            }
        } catch (Exception e) {

            e.printStackTrace();

        }

        return "success";

    }


    /**
     * Create Content & DataResource &&  ASSOC Party
     *
     * @param delegator
     * @param userLogin
     * @param dispatcher
     * @param pictureKey
     * @return
     * @throws GenericServiceException
     * @throws GenericEntityException
     * @author S.Y.L
     */
    public static String createContentAndDataResource(String partyId, Delegator delegator, GenericValue userLogin, LocalDispatcher dispatcher, String pictureKey, String path, String partyContentType)
            throws GenericServiceException, GenericEntityException {
        //没有指定内容类型,默认是在传头像
        if (partyContentType == null) {
            partyContentType = "LGOIMGURL";
        }

        // 1.CREATE DATA RESOURCE
        Map<String, Object> createDataResourceMap = UtilMisc.toMap("userLogin", userLogin, "partyId", "admin",
                "dataResourceTypeId", "LOCAL_FILE", "dataCategoryId", "PERSONAL", "dataResourceName", pictureKey,
                "mimeTypeId", "image/jpeg", "isPublic", "Y", "dataTemplateTypeId", "NONE", "statusId", "CTNT_PUBLISHED",
                "objectInfo", path);


        Map<String, Object> serviceResultByDataResource = dispatcher.runSync("createDataResource",
                createDataResourceMap);
        String dataResourceId = (String) serviceResultByDataResource.get("dataResourceId");


        Map<String, Object> createContentMap = UtilMisc.toMap("userLogin", userLogin, "createdByUserLogin", userLogin.get("userLoginId"), "contentTypeId",
                "DOCUMENT", "statusId", "CTNT_PUBLISHED", "mimeTypeId", "image/png", "dataResourceId", dataResourceId);


        Map<String, Object> serviceResultByCreateContentMap = dispatcher.runSync("createContent", createContentMap);
        String contentId = (String) serviceResultByCreateContentMap.get("contentId");


        Map<String, Object> assocContentPartyMap = UtilMisc.toMap("userLogin", userLogin, "contentId", contentId, "partyId", partyId, "partyContentTypeId", partyContentType);
        dispatcher.runSync("createPartyContent", assocContentPartyMap);

        return null;
    }


    /**
     * Place Resource Order
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> placeResourceOrder(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = (String) userLogin.get("partyId");


        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));
        String productStoreId = (String) context.get("productStoreId");
        String amount_str = (String) context.get("amount");
        String payToPartyId = (String) context.get("payToPartyId");
        String productId = (String) context.get("productId");
        String price = (String) context.get("price");
        String prodCatalogId = (String) context.get("prodCatalogId");

        BigDecimal subTotal = BigDecimal.ZERO;
        BigDecimal grandTotal = BigDecimal.ZERO;
        BigDecimal amount = BigDecimal.ONE;


        if (!UtilValidate.isEmpty(amount_str)) {
            amount = new BigDecimal(amount_str);
        }


        if (!UtilValidate.isEmpty(price)) {
            grandTotal = subTotal = new BigDecimal(price);
        }

        GenericValue productAttrQu = EntityQuery.use(delegator).from("ProductAttribute").where("attrName","quantityAccepted","productId", productId).queryFirst();

        String qantStr = (String)productAttrQu.get("attrValue");

        int qant = Integer.parseInt(qantStr);

        if(qant <= 0){
            return ServiceUtil.returnSuccess();
        }else{
            qant = qant - 1;
            productAttrQu.set("attrValue",qant+"");
        }



        GenericValue facility = EntityQuery.use(delegator).from("Facility").where("ownerPartyId", partyId).queryFirst();
        String originFacilityId = (String) facility.get("facilityId");

        // Do Create OrderHeader
        Map<String, Object> createOrderHeaderInMap = new HashMap<String, Object>();
        createOrderHeaderInMap.put("userLogin", userLogin);
        createOrderHeaderInMap.put("productStoreId", productStoreId);

        createOrderHeaderInMap.put("originFacilityId", originFacilityId);


        createOrderHeaderInMap.put("salesChannelEnumId", "WEB_SALES_CHANNEL");
        createOrderHeaderInMap.put("currencyUom", PeConstant.DEFAULT_CURRENCY_UOM_ID);
        createOrderHeaderInMap.put("orderTypeId", PeConstant.SALES_ORDER);
        createOrderHeaderInMap.put("statusId", PeConstant.ORDER_CREATED_STATUS_ID);
        createOrderHeaderInMap.put("remainingSubTotal", subTotal);
        createOrderHeaderInMap.put("grandTotal", grandTotal);

        Map<String, Object> createOrderHeaderOutMap = dispatcher.runSync("createOrderHeader", createOrderHeaderInMap);

        if (ServiceUtil.isError(createOrderHeaderOutMap)) {
            return createOrderHeaderOutMap;
        }

        String orderId = (String) createOrderHeaderOutMap.get("orderId");


        // Add Product To Order Item

        Map<String, Object> appendOrderItemInMap = new HashMap<String, Object>();

        appendOrderItemInMap.put("userLogin", userLogin);
        appendOrderItemInMap.put("orderId", orderId);
        appendOrderItemInMap.put("productId", productId);
        appendOrderItemInMap.put("quantity", amount);
        appendOrderItemInMap.put("amount", grandTotal);
        appendOrderItemInMap.put("shipGroupSeqId", "00001");
        appendOrderItemInMap.put("prodCatalogId", prodCatalogId);
        appendOrderItemInMap.put("basePrice", grandTotal);
        appendOrderItemInMap.put("overridePrice", price);
        appendOrderItemInMap.put("calcTax", new Boolean("false"));

        //appendOrderItem

        Map<String, Object> appendOrderItemOutMap = null;
        try {
//            for(int i = 0 ; i < amount;i++){ }
            //TODO
            appendOrderItemOutMap = dispatcher.runSync("appendOrderItem", appendOrderItemInMap);

        } catch (GenericServiceException e1) {
            Debug.logError(e1.getMessage(), module);
            return ServiceUtil.returnError(UtilProperties.getMessage(resourceError, "ProductNoLongerForSale", locale));
        }


        //  接收账单的客户 到 订单
        Map<String, Object> addOrderRoleInMap = new HashMap<String, Object>();
        addOrderRoleInMap.put("userLogin", userLogin);
        addOrderRoleInMap.put("orderId", orderId);
        addOrderRoleInMap.put("partyId", partyId);
        addOrderRoleInMap.put("roleTypeId", "BILL_TO_CUSTOMER");
        Map<String, Object> addOrderRoleOutMap = dispatcher.runSync("addOrderRole", addOrderRoleInMap);

        if (ServiceUtil.isError(addOrderRoleOutMap)) {
            return addOrderRoleOutMap;
        }


        //  发出账单的厂家 到 订单
        Map<String, Object> addBILL_FROM_VENDORInMap = new HashMap<String, Object>();
        addBILL_FROM_VENDORInMap.put("userLogin", userLogin);
        addBILL_FROM_VENDORInMap.put("orderId", orderId);
        addBILL_FROM_VENDORInMap.put("partyId", payToPartyId);
        addBILL_FROM_VENDORInMap.put("roleTypeId", "BILL_FROM_VENDOR");
        Map<String, Object> addBILL_FROM_VENDOROutMap = dispatcher.runSync("addOrderRole", addBILL_FROM_VENDORInMap);

        if (ServiceUtil.isError(addBILL_FROM_VENDOROutMap)) {
            return addBILL_FROM_VENDOROutMap;
        }


        //  收货的客户 到 订单
        Map<String, Object> addSHIP_TO_CUSTOMERInMap = new HashMap<String, Object>();
        addSHIP_TO_CUSTOMERInMap.put("userLogin", userLogin);
        addSHIP_TO_CUSTOMERInMap.put("orderId", orderId);
        addSHIP_TO_CUSTOMERInMap.put("partyId", partyId);
        addSHIP_TO_CUSTOMERInMap.put("roleTypeId", "SHIP_TO_CUSTOMER");
        Map<String, Object> addSHIP_TO_CUSTOMEROutMap = dispatcher.runSync("addOrderRole", addSHIP_TO_CUSTOMERInMap);

        if (ServiceUtil.isError(addSHIP_TO_CUSTOMEROutMap)) {
            return addSHIP_TO_CUSTOMEROutMap;
        }


        //最关键的意向客户的角色
        GenericValue placRole =  EntityQuery.use(delegator).from("ProductRole").where("partyId",partyId,"roleTypeId", "PLACING_CUSTOMER","productId", productId).queryFirst();
        if(null == placRole){

        Map<String, Object> addPLACING_CUSTOMERInMap = new HashMap<String, Object>();
        addPLACING_CUSTOMERInMap.put("userLogin", userLogin);
        addPLACING_CUSTOMERInMap.put("orderId", orderId);
        addPLACING_CUSTOMERInMap.put("partyId", partyId);
       addPLACING_CUSTOMERInMap.put("roleTypeId", "PLACING_CUSTOMER");

        Map<String, Object> addPLACING_CUSTOMEROutMap = dispatcher.runSync("addOrderRole", addPLACING_CUSTOMERInMap);

        if (ServiceUtil.isError(addPLACING_CUSTOMEROutMap)) {
            return addPLACING_CUSTOMEROutMap;
        }


        }

        GenericValue teleContact = EntityQuery.use(delegator).from("TelecomNumberAndPartyView").where("partyId", payToPartyId).queryFirst();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        if (null != teleContact) {
            String contactNumber = (String) teleContact.get("contactNumber");
            resultMap.put("contactTel", contactNumber);
        }

        resultMap.put("partyIdFrom", partyId);
        resultMap.put("partyIdTo", payToPartyId);
        resultMap.put("relationEnum", "C2CRSS");
        resultMap.put("orderId", orderId);


        //推送先不用ECA
        // 查询registrationID
        EntityCondition pConditions = EntityCondition.makeCondition("partyId", payToPartyId);
        List<EntityCondition> devTypeExprs = new ArrayList<EntityCondition>();
        devTypeExprs.add(EntityCondition.makeCondition("partyIdentificationTypeId", "JPUSH_ANDROID"));
        devTypeExprs.add(EntityCondition.makeCondition("partyIdentificationTypeId", "JPUSH_IOS"));
        EntityCondition devCondition = EntityCondition.makeCondition(devTypeExprs, EntityOperator.OR);
        pConditions = EntityCondition.makeCondition(pConditions, devCondition);

        List<GenericValue> partyIdentifications = delegator.findList("PartyIdentification", pConditions, null, UtilMisc.toList("-createdStamp"), null, false);

        GenericValue person = delegator.findOne("Person", UtilMisc.toMap("partyId", partyId), false);
        String maiJiaName = (String) person.get("firstName");




        //推送给微信用户

        List<GenericValue> partyIdentificationList = EntityQuery.use(delegator).from("PartyIdentification").where("partyId", partyId, "partyIdentificationTypeId", "WX_GZ_OPEN_ID").queryList();


        if (null != partyIdentificationList && partyIdentificationList.size() > 0) {

            Map<String, Object> pushWeChatMessageInfoMap = new HashMap<String, Object>();


            System.out.println("*PUSH WE CHAT GONG ZHONG PLATFORM !!!!!!!!!!!!!!!!!!!!!!!");

            pushWeChatMessageInfoMap.put("payToPartyId", payToPartyId);

            Date date = new Date();

            SimpleDateFormat formatter;

            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String pushDate = "" + formatter.format(date);

            pushWeChatMessageInfoMap.put("date", pushDate);


            String openId = (String) partyIdentificationList.get(0).get("idValue");

            pushWeChatMessageInfoMap.put("openId", openId);

            pushWeChatMessageInfoMap.put("orderId", orderId);



            String toPartyUserLoginId = (String) userLogin.get("userLoginId");


            long expirationTime = Long.valueOf(EntityUtilProperties.getPropertyValue("pe", "tarjeta.expirationTime", "172800L", delegator));
            String iss = EntityUtilProperties.getPropertyValue("pe", "tarjeta.issuer", delegator);
            String tokenSecret = EntityUtilProperties.getPropertyValue("pe", "tarjeta.secret", delegator);
            //开始时间
            final long iat = System.currentTimeMillis() / 1000L; // issued at claim
            //到期时间
            final long exp = iat + expirationTime;
            //生成
            final JWTSigner signer = new JWTSigner(tokenSecret);
            final HashMap<String, Object> claims = new HashMap<String, Object>();
            claims.put("iss", iss);
            claims.put("user", toPartyUserLoginId);
            claims.put("delegatorName", delegator.getDelegatorName());
            claims.put("exp", exp);
            claims.put("iat", iat);






            pushWeChatMessageInfoMap.put("jumpUrl", "http://www.lyndonspace.com:3400/WebManager/control/myOrder?tarjeta="+ signer.sign(claims));

            Map<String, String> personInfoMap = queryPersonBaseInfo(delegator, payToPartyId);

            pushWeChatMessageInfoMap.put("messageInfo", personInfoMap.get("firstName") + "正在处理您的订单");
            //推微信订单状态
            dispatcher.runSync("pushOrderStatusInfo", pushWeChatMessageInfoMap);
        }


        //买家就是卖家的情况直接返回
        if (partyId.equals(payToPartyId)) {
            return resultMap;
        }
        GenericValue productRole = EntityQuery.use(delegator).from("ProductRole").where("partyId", partyId, "roleTypeId", "PLACING_CUSTOMER","productId", productId).queryFirst();
        //如果这个客户已经是产品的意向客户,取消这个角色,并且给予 '客户'角色
        if (productRole !=null && productRole.get("partyId")!=null) {
            System.out.println("productRole="+productRole);
            GenericValue partyMarkRole = EntityQuery.use(delegator).from("ProductRole").where("partyId", partyId, "productId", productId, "roleTypeId", "PLACING_CUSTOMER").queryFirst();
            dispatcher.runSync("removePartyFromProduct", UtilMisc.toMap("userLogin", admin, "partyId", partyId, "productId", productId, "roleTypeId", "PLACING_CUSTOMER", "fromDate", partyMarkRole.get("fromDate")));
        }
        //授予客户角色
        GenericValue productRoleCust =  EntityQuery.use(delegator).from("ProductRole").where("partyId", partyId, "roleTypeId", PeConstant.PRODUCT_CUSTOMER,"productId", productId).queryFirst();
        if(null == productRoleCust){
            dispatcher.runSync("addPartyToProduct", UtilMisc.toMap("userLogin", admin, "partyId", partyId, "productId", context.get("productId"), "roleTypeId", PeConstant.PRODUCT_CUSTOMER));
        }

        //已注释掉,将客户做成店铺客户的角色逻辑
//        GenericValue custStoreRole = EntityQuery.use(delegator).from("ProductStoreRole").where("partyId",partyId,"productStoreId",productStoreId,"roleTypeId", PeConstant.PRODUCT_STORE_CUST_ROLE).queryFirst();
//        if (!UtilValidate.isNotEmpty(custStoreRole)) {
//            //如果没有客户角色就创建一个客户角色
//            Map<String, Object> createProductStoreRoleOutMap =   dispatcher.runSync("createProductStoreRole", UtilMisc.toMap("userLogin", userLogin, "partyId", partyId, "productStoreId", productStoreId, "roleTypeId", PeConstant.PRODUCT_STORE_CUST_ROLE));
//            if (!ServiceUtil.isSuccess(createProductStoreRoleOutMap)) {
//                return createProductStoreRoleOutMap;
//            }
//        }

        Set<String> fieldSet = new HashSet<String>();
        fieldSet.add("contactMechId");
        fieldSet.add("partyId");
        fieldSet.add("address1");
        EntityCondition findConditions = EntityCondition
                .makeCondition(UtilMisc.toMap("partyId", partyId));

        //Query My Resource
        List<GenericValue> queryAddressList = delegator.findList("PartyAndPostalAddress",
                findConditions, fieldSet,
                UtilMisc.toList("-fromDate"), null, false);

        String address1 =null;
        if(queryAddressList!=null & queryAddressList.size()>0){
            GenericValue address = queryAddressList.get(0);
            address1 = (String) address.get("address1");
        }
        if(address1==null){
            dispatcher.runSync("pushMessage",UtilMisc.toMap("partyIdTo",partyId,"partyIdFrom",payToPartyId,"text","您好,我已收到您下的订单,但我没有您的收货地址,请直接发给我您的地址(若已更新收货地址,勿理会本条提示*_*)!","objectId",productId));
        }else{
            dispatcher.runSync("pushMessage",UtilMisc.toMap("partyIdTo",partyId,"partyIdFrom",payToPartyId,"text","您的订单收货地址:"+address1+",无误请点击→","objectId",productId));
        }



        //确认客户关系
//        Map<String,Object> createPartyRelationshipInMap = new HashMap<String, Object>();
//
//
//        createPartyRelationshipInMap.put("roleTypeIdTo", "SHIP_FROM_VENDOR");
//        createPartyRelationshipInMap.put("roleTypeIdFrom", "CUSTOMER");
//        createPartyRelationshipInMap.put("userLogin", admin);
//        createPartyRelationshipInMap.put("partyIdFrom", partyId);
//        createPartyRelationshipInMap.put("partyIdTo", payToPartyId);
//        createPartyRelationshipInMap.put("partyRelationshipTypeId", "CUSTOMER_REL");
//
//        dispatcher.runSync("createPartyRelationship", createPartyRelationshipInMap);



        //推卖家
        if (null != partyIdentifications && partyIdentifications.size() > 0) {

            GenericValue partyIdentification = (GenericValue) partyIdentifications.get(0);
            String jpushId = (String) partyIdentification.getString("idValue");
            String partyIdentificationTypeId = (String) partyIdentification.get("partyIdentificationTypeId");

            try {
                dispatcher.runSync("pushNotifOrMessage", UtilMisc.toMap("userLogin", admin, "productId",productId,"message", "order", "content", maiJiaName + "购买了您的产品!点我查看!", "regId", jpushId, "deviceType", partyIdentificationTypeId, "sendType", "", "objectId", orderId));
            } catch (GenericServiceException e1) {
                Debug.logError(e1.getMessage(), module);
                return ServiceUtil.returnError(UtilProperties.getMessage(resourceError, "JPushError", locale));
            }

        }
        return resultMap;
    }


    /**
     * Create User
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> createPeUser(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");

        String partyId, userLoginId = "";
        String uuid = (String) context.get("uuid");
        String tel = (String) context.get("tel");
        String openId = (String) context.get("openId");

        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));


        // Create Party Block
        int random = (int) (Math.random() * 1000000 + 1);
        Map<String, Object> createPartyInMap = UtilMisc.toMap("userLogin", admin, "nickname", "#" + random,
                "firstName", "#" + random, "lastName", " ", "gender", "M");
        Map<String, Object> createPerson = dispatcher.runSync("createUpdatePerson", createPartyInMap);
        partyId = (String) createPerson.get("partyId");


        // Create PartyIdentification Block
        if (!UtilValidate.isEmpty(uuid)) {
            Map<String, Object> createPartyIdentificationInMap = UtilMisc.toMap("userLogin", admin, "partyId",
                    partyId, "idValue", uuid, "partyIdentificationTypeId", "CARD_ID");
            dispatcher.runSync("createPartyIdentification", createPartyIdentificationInMap);
        }


        if (!UtilValidate.isEmpty(openId)) {
            Map<String, Object> createPartyIdentificationWxInMap = UtilMisc.toMap("userLogin", admin, "partyId",
                    partyId, "idValue", openId, "partyIdentificationTypeId", "WX_UNIO_ID");
            dispatcher.runSync("createPartyIdentification", createPartyIdentificationWxInMap);
        }


        // Create UserLogin Block
        Map<String, Object> createUserLoginInMap = UtilMisc.toMap("userLogin", admin, "userLoginId",
                tel, "partyId", partyId, "currentPassword", "ofbiz",
                "currentPasswordVerify", "ofbiz", "enabled", "Y");
        Map<String, Object> createUserLogin = dispatcher.runSync("createUserLogin", createUserLoginInMap);


        // Search New UserLogin
        userLoginId = (String) EntityQuery.use(delegator).from("UserLogin").where("partyId", partyId).queryFirst()
                .get("userLoginId");

        // Grant Permission Block
        Map<String, Object> userLoginSecurityGroupInMap = UtilMisc.toMap("userLogin", admin, "userLoginId",
                userLoginId, "groupId", "FULLADMIN");
        dispatcher.runSync("addUserLoginToSecurityGroup", userLoginSecurityGroupInMap);


        if (!UtilValidate.isEmpty(tel)) {
            // Create Party Telecom Number
            Map<String, Object> inputTelecom = UtilMisc.toMap();
            inputTelecom.put("partyId", partyId);
            inputTelecom.put("contactNumber", tel);
            inputTelecom.put("contactMechTypeId", "TELECOM_NUMBER");
            inputTelecom.put("contactMechPurposeTypeId", "PHONE_MOBILE");
            inputTelecom.put("userLogin", admin);
            Map<String, Object> createTelecom = null;
            createTelecom = dispatcher.runSync("createPartyTelecomNumber", inputTelecom);

        }

        createPersonStoreAndCatalogAndCategory(locale, admin, delegator, dispatcher, partyId);


        //创建当事人支付方法

        // Create Default Pay Method To Party
        GenericValue newPayMethod = delegator.makeValue("PaymentMethod");
        newPayMethod.set("paymentMethodId", delegator.getNextSeqId("PaymentMethod"));
        newPayMethod.set("partyId", partyId);
        newPayMethod.set("paymentMethodTypeId", "EXT_ALIPAY");
        newPayMethod.set("description", "支付宝");
        newPayMethod.create();


        GenericValue newPayMethod2 = delegator.makeValue("PaymentMethod");
        newPayMethod2.set("paymentMethodId", delegator.getNextSeqId("PaymentMethod"));
        newPayMethod2.set("partyId", partyId);
        newPayMethod2.set("paymentMethodTypeId", "EXT_WXPAY");
        newPayMethod2.set("description", "微信");
        newPayMethod2.create();


        // Create Party Role 授予当事人 意向客户 角色 用于mark product
        GenericValue partyMarkRole = EntityQuery.use(delegator).from("PartyRole").where("partyId", partyId, "roleTypeId", "PLACING_CUSTOMER").queryFirst();
        if (null == partyMarkRole) {
            Map<String, Object> createPartyMarkRoleMap = UtilMisc.toMap("userLogin", admin, "partyId", partyId,
                    "roleTypeId", "PLACING_CUSTOMER");
            dispatcher.runSync("createPartyRole", createPartyMarkRoleMap);
        }



        // Create Party Role 授予当事人 访问者 角色 用于mark product
         partyMarkRole = EntityQuery.use(delegator).from("PartyRole").where("partyId", partyId, "roleTypeId", "VISITOR").queryFirst();
        if (null == partyMarkRole) {
            Map<String, Object> createPartyMarkRoleMap = UtilMisc.toMap("userLogin", admin, "partyId", partyId,
                    "roleTypeId", "VISITOR");
            dispatcher.runSync("createPartyRole", createPartyMarkRoleMap);
        }
        // Create Party Role 授予当事人 合作伙伴 角色 用于mark product
        partyMarkRole = EntityQuery.use(delegator).from("PartyRole").where("partyId", partyId, "roleTypeId", "PARTNER").queryFirst();
        if (null == partyMarkRole) {
            Map<String, Object> createPartyMarkRoleMap = UtilMisc.toMap("userLogin", admin, "partyId", partyId,
                    "roleTypeId", "PARTNER");
            dispatcher.runSync("createPartyRole", createPartyMarkRoleMap);
        }




        //创建当事人税务机关

        Map<String, Object> createTaxAuthorityOutMap = dispatcher.runSync("createTaxAuthority",
                UtilMisc.toMap("userLogin", admin,
                        "includeTaxInPrice", "N",
                        "taxAuthGeoId", "CHN",
                        "taxAuthPartyId", partyId));

        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        resultMap.put("userLoginId", userLoginId);
        return resultMap;
    }


    /**
     * 创建分类创建店铺创建目录并且关联当事人
     *
     * @param admin
     * @param delegator
     * @param dispatcher
     * @param partyId
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static String createPersonStoreAndCatalogAndCategory(Locale locale, GenericValue admin, Delegator delegator, LocalDispatcher dispatcher, String partyId) throws GenericEntityException, GenericServiceException {

        // Create Party Role 授予当事人角色,如果没有
        GenericValue partyRole = EntityQuery.use(delegator).from("PartyRole").where("partyId", partyId, "roleTypeId", "ADMIN").queryFirst();
        if (null == partyRole) {
            Map<String, Object> createPartyRoleMap = UtilMisc.toMap("userLogin", admin, "partyId", partyId,
                    "roleTypeId", "ADMIN");
            dispatcher.runSync("createPartyRole", createPartyRoleMap);
        }


        // 接收账单的客户是否拥有
        GenericValue partyCustRole = EntityQuery.use(delegator).from("PartyRole").where("partyId", partyId, "roleTypeId", "BILL_TO_CUSTOMER").queryFirst();
        if (null == partyCustRole) {
            Map<String, Object> createCustPartyRoleMap = UtilMisc.toMap("userLogin", admin, "partyId", partyId,
                    "roleTypeId", "BILL_TO_CUSTOMER");
            dispatcher.runSync("createPartyRole", createCustPartyRoleMap);
        }

        // 收货的客户是否拥有
        GenericValue partyCustShipRole = EntityQuery.use(delegator).from("PartyRole").where("partyId", partyId, "roleTypeId", "SHIP_TO_CUSTOMER").queryFirst();
        if (null == partyCustShipRole) {
            Map<String, Object> createPartyCustShipRoleMap = UtilMisc.toMap("userLogin", admin, "partyId", partyId,
                    "roleTypeId", "SHIP_TO_CUSTOMER");
            dispatcher.runSync("createPartyRole", createPartyCustShipRoleMap);
        }

        //最终客户角色是否拥有
        GenericValue partyEndCustRole = EntityQuery.use(delegator).from("PartyRole").where("partyId", partyId, "roleTypeId", "END_USER_CUSTOMER").queryFirst();
        if (null == partyEndCustRole) {
            Map<String, Object> createPartyEndCustRoleMap = UtilMisc.toMap("userLogin", admin, "partyId", partyId,
                    "roleTypeId", "END_USER_CUSTOMER");
            dispatcher.runSync("createPartyRole", createPartyEndCustRoleMap);
        }


        GenericValue partyCUSTOMERRole = EntityQuery.use(delegator).from("PartyRole").where("partyId", partyId, "roleTypeId", "CUSTOMER").queryFirst();
        if (null == partyCUSTOMERRole) {
            Map<String, Object> createPartyCUSTOMERRoleMap = UtilMisc.toMap("userLogin", admin, "partyId", partyId,
                    "roleTypeId", "CUSTOMER");
            dispatcher.runSync("createPartyRole", createPartyCUSTOMERRoleMap);
        }


        // 发货厂家角色
        GenericValue partyVendorRole = EntityQuery.use(delegator).from("PartyRole").where("partyId", partyId, "roleTypeId", "SHIP_FROM_VENDOR").queryFirst();
        if (null == partyVendorRole) {
            Map<String, Object> createVendorPartyRoleMap = UtilMisc.toMap("userLogin", admin, "partyId", partyId,
                    "roleTypeId", "SHIP_FROM_VENDOR");
            dispatcher.runSync("createPartyRole", createVendorPartyRoleMap);
        }

        //发出账单的厂家
        GenericValue partyBillVendorRole = EntityQuery.use(delegator).from("PartyRole").where("partyId", partyId, "roleTypeId", "BILL_FROM_VENDOR").queryFirst();
        if (null == partyBillVendorRole) {
            Map<String, Object> createPartyBillVendorRoleMap = UtilMisc.toMap("userLogin", admin, "partyId", partyId,
                    "roleTypeId", "BILL_FROM_VENDOR");
            dispatcher.runSync("createPartyRole", createPartyBillVendorRoleMap);
        }


        //创建仓库

        Map<String, Object> createPersonFacilityOutMap = dispatcher.runSync("createPersonFacility", UtilMisc.toMap("userLogin", admin,
                "partyId", partyId));

        String facilityId = (String) createPersonFacilityOutMap.get("facilityId");


        // 创建店铺
        Map<String, Object> createPersonStoreOutMap = dispatcher.runSync("createPersonStore", UtilMisc.toMap("userLogin", admin,
                "partyId", partyId, "inventoryFacilityId", facilityId));

        String productStoreId = (String) createPersonStoreOutMap.get("storeId");


        // 关联店铺角色
        Map<String, Object> createProductStoreRoleOutMap = dispatcher.runSync("createProductStoreRole", UtilMisc.toMap("userLogin", admin,
                "partyId", partyId, "productStoreId", productStoreId, "roleTypeId", "ADMIN"));


        //店铺关联货运方法
        //EMS
//        Map<String, Object> createProductStoreShipEmsMethOutMap =
//                dispatcher.runSync("createProductStoreShipMeth", UtilMisc.toMap("userLogin", admin,
//                        "productStoreShipMethId", "CP_EMS", "productStoreId", productStoreId,
//                        "shipmentMethodTypeId", "EXPRESS", "partyId", "CHINAPOST", "roleTypeId", "CARRIER"));
//
//
//            dispatcher.runAsync("createProductStoreShipMeth", UtilMisc.toMap("userLogin", admin,
//                    "productStoreShipMethId", "CP_EMS", "productStoreId", productStoreId,
//                    "shipmentMethodTypeId", "EXPRESS", "partyId", "SHUNFENG_EXPRESS", "roleTypeId", "CARRIER"));


        // CreateProd Catalog 创建目录
        Map<String, Object> createProdCatalogInMap = new HashMap<String, Object>();
        createProdCatalogInMap.put("userLogin", admin);
        createProdCatalogInMap.put("catalogName", partyId);
        Map<String, Object> createProdCatalogOutMap = dispatcher.runSync("createProdCatalog", createProdCatalogInMap);
        String prodCatalogId = (String) createProdCatalogOutMap.get("prodCatalogId");

        // createProductCategory 创建我的分类
        Map<String, Object> createProductCategoryInMap = new HashMap<String, Object>();
        createProductCategoryInMap.put("userLogin", admin);
        createProductCategoryInMap.put("productCategoryTypeId", "CATALOG_CATEGORY");
        createProductCategoryInMap.put("categoryName", partyId);
        Map<String, Object> createProductCategoryOutMap = dispatcher.runSync("createProductCategory", createProductCategoryInMap);
        String productCategoryId = (String) createProductCategoryOutMap.get("productCategoryId");


        // Add ProdCatalog To Party 目录关联当事人
        Map<String, Object> addProdCatalogToPartyInMap = new HashMap<String, Object>();
        addProdCatalogToPartyInMap.put("userLogin", admin);
        addProdCatalogToPartyInMap.put("prodCatalogId", prodCatalogId);
        addProdCatalogToPartyInMap.put("partyId", partyId);
        addProdCatalogToPartyInMap.put("roleTypeId", "ADMIN");
        Map<String, Object> addProdCatalogToPartyOutMap = dispatcher.runSync("addProdCatalogToParty", addProdCatalogToPartyInMap);

        // Create Product Store Catalog 目录关联店铺
        Map<String, Object> createProductStoreCatalogInMap = new HashMap<String, Object>();
        createProductStoreCatalogInMap.put("userLogin", admin);
        createProductStoreCatalogInMap.put("prodCatalogId", prodCatalogId);
        createProductStoreCatalogInMap.put("productStoreId", productStoreId);
        Map<String, Object> createProductStoreCatalogOutMap = dispatcher.runSync("createProductStoreCatalog", createProductStoreCatalogInMap);

        // Add Product Category To ProdCatalog 目录关联分类
        Map<String, Object> addProductCategoryToProdCatalogInMap = new HashMap<String, Object>();
        addProductCategoryToProdCatalogInMap.put("userLogin", admin);
        addProductCategoryToProdCatalogInMap.put("prodCatalogCategoryTypeId", "PCCT_PURCH_ALLW");
        addProductCategoryToProdCatalogInMap.put("prodCatalogId", prodCatalogId);
        addProductCategoryToProdCatalogInMap.put("productCategoryId", productCategoryId);

        Map<String, Object> addProductCategoryToProdCatalogOutMap = dispatcher.runSync("addProductCategoryToProdCatalog", addProductCategoryToProdCatalogInMap);

        return productCategoryId;
    }


    /**
     * Create PE Person Store / Facility
     *
     * @param dctx
     * @param context
     * @return
     */
    public static Map<String, Object> createPersonStore(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));
        // 当事人
        String partyId = (String) context.get("partyId");

        String inventoryFacilityId = (String) context.get("inventoryFacilityId");

        // 商家名
        String storeName = partyId;

        // 返回的店铺Id
        String personStoreId = null;

        try {
            // Create Product Store
            Map<String, Object> createProductStoreOutMap = dispatcher.runSync("createProductStore", UtilMisc.toMap("userLogin", admin,
                    "defaultCurrencyUomId", PeConstant.DEFAULT_CURRENCY_UOM_ID, "storeName", storeName, "payToPartyId", partyId, "inventoryFacilityId", inventoryFacilityId));
            if (!ServiceUtil.isSuccess(createProductStoreOutMap)) {
                return createProductStoreOutMap;
            }
            personStoreId = (String) createProductStoreOutMap.get("productStoreId");


            //createProductStorePaymentSetting

            //微信支付设置
            Map<String, Object> createProductStorePaymentSettingOutMap = dispatcher.runSync("createProductStorePaymentSetting", UtilMisc.toMap("userLogin", admin,
                    "productStoreId", personStoreId, "applyToAllProducts", "Y", "paymentMethodTypeId", "EXT_WXPAY", "paymentServiceTypeEnumId", "PRDS_PAY_AUTH"));
            if (!ServiceUtil.isSuccess(createProductStorePaymentSettingOutMap)) {
                return createProductStorePaymentSettingOutMap;
            }

        } catch (GenericServiceException e1) {
            Debug.logError(e1.getMessage(), module);
            return ServiceUtil.returnError(UtilProperties.getMessage(resourceError, "InternalServiceError", locale));
        }


        // 信用评分
//            Map<String, Object> createPartyClassificationOutMap = dispatcher.runSync("createPartyClassification",
//                    UtilMisc.toMap("userLogin", userLogin, "partyId", personStoreId, "partyClassificationGroupId", level));
//            if (!ServiceUtil.isSuccess(createPartyClassificationOutMap)) {
//                return createPartyClassificationOutMap;
//            }


        Map<String, Object> result = ServiceUtil.returnSuccess();
        result.put("storeId", personStoreId);
        return result;
    }


    /**
     * createPersonFacility
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     */
    public static Map<String, Object> createPersonFacility(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));
        // 当事人
        String partyId = (String) context.get("partyId");

        String facilityId = null;

        GenericValue person = delegator.findOne("Person", false, UtilMisc.toMap("partyId", partyId));

        // 仓库名
        String facilityName = (String) person.get("firstName");

        try {
            // Create Facility
            Map<String, Object> createFacilityOutMap = dispatcher.runSync("createFacility", UtilMisc.toMap("userLogin", admin,
                    "ownerPartyId", partyId, "facilityTypeId", "WAREHOUSE", "facilityName", facilityName, "defaultInventoryItemTypeId", "NON_SERIAL_INV_ITEM"));
            if (!ServiceUtil.isSuccess(createFacilityOutMap)) {
                return createFacilityOutMap;
            }
            facilityId = (String) createFacilityOutMap.get("facilityId");


        } catch (GenericServiceException e1) {
            Debug.logError(e1.getMessage(), module);
            return ServiceUtil.returnError(UtilProperties.getMessage(resourceError, "InternalServiceError", locale));
        }


        Map<String, Object> result = ServiceUtil.returnSuccess();
        result.put("facilityId", facilityId);
        return result;
    }

}
