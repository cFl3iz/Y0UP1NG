package main.java.com.banfftech.personmanager;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

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
import sun.net.www.content.text.Generic;
import sun.security.krb5.Config;

import static main.java.com.banfftech.personmanager.PersonManagerQueryServices.queryPersonBaseInfo;
import static main.java.com.banfftech.platformmanager.wechat.WeChatUtil.getAccessToken;

/**
 * Created by S on 2017/9/12.
 */
public class PersonManagerServices {


    public final static String module = PersonManagerServices.class.getName();

    public static final String resourceUiLabels = "PlatformManagerUiLabels.xml";

    public static final String resourceError = "PlatformManagerErrorUiLabels.xml";

    public static final String resource_error = "OrderErrorUiLabels";

    /**
     * PartyRelationShipENUM
     */
    public enum relationType {

        C2CRSS, CONTACT;

        public static relationType getRelationType(String relationType) {

            return valueOf(relationType.toUpperCase());

        }
    }


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
     * doRemoveProductFromCategory
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     * @throws Exception
     */
    public static Map<String, Object> doRemoveProductFromCategory(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException, Exception {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();

        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        String productId = (String) context.get("productId");

        GenericValue productAndCategoryMember = EntityQuery.use(delegator).from("ProductAndCategoryMember").where(UtilMisc.toMap("productId", productId)).queryFirst();

        if (null != productAndCategoryMember) {


            Map<String, Object> removeProductFromCategoryInMap = new HashMap<String, Object>();
            removeProductFromCategoryInMap.put("userLogin", admin);
            removeProductFromCategoryInMap.put("productId", productId);
            removeProductFromCategoryInMap.put("productCategoryId", productAndCategoryMember.get("productCategoryId"));
            removeProductFromCategoryInMap.put("fromDate", productAndCategoryMember.get("fromDate"));

            Map<String, Object> removeProductFromCategoryOutMap = dispatcher.runSync("removeProductFromCategory", removeProductFromCategoryInMap);

            if (ServiceUtil.isError(removeProductFromCategoryOutMap)) {
                return removeProductFromCategoryOutMap;
            }
        }

        return resultMap;
    }


    /**
     * recoveResource
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     * @throws Exception
     */
    public static Map<String, Object> recoveResource(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException, Exception {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();


        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        String productId = (String) context.get("productId");


        Map<String, Object> updateProductInMap = new HashMap<String, Object>();
        updateProductInMap.put("userLogin", admin);
        updateProductInMap.put("productId", productId);
        updateProductInMap.put("salesDiscontinuationDate", null);

        Map<String, Object> updateProductOutMap = dispatcher.runSync("updateProduct", updateProductInMap);

        if (ServiceUtil.isError(updateProductOutMap)) {
            return updateProductOutMap;
        }

        return resultMap;
    }

    /**
     * receivedBProductInformation
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     * @throws Exception
     */
    public static Map<String, Object> receivedBProductInformation(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException, Exception {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        GenericValue userLogin = null;
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));
        String partyId = (String) context.get("partyId");
        if (!UtilValidate.isEmpty(partyId)) {
            userLogin = EntityQuery.use(delegator).from("UserLogin").where(UtilMisc.toMap("partyId", partyId)).queryFirst();
        } else {
            userLogin = (GenericValue) context.get("userLogin");
        }




        // 当前收到引用的当事人
        String receivePartyId = (String) userLogin.get("partyId");
        // 来自引用当事人
        String spm = (String) context.get("partyIdFrom");
        // 资源主
        String payToPartyId = (String) context.get("payToPartyId");
        // 资源ID
        String productId = (String) context.get("productId");
        // 来自上层
        String beforePartyId = (String) context.get("beforePartyId");

        GenericValue workEffortAndProductAndParty = null;

        // 自己不用记录自己打开了自己转发给别人的链接
        if (beforePartyId.equals(receivePartyId)) {
            return resultMap;
        }

        // 发起转发者打开了分享,无意义。
        if (spm.equals(receivePartyId)) {
            return resultMap;
        }

        System.out.println("->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("spm=" + spm);
        System.out.println("receivePartyId=" + receivePartyId);
        System.out.println("payToPartyId=" + payToPartyId);
        System.out.println("partyId=" + partyId);

        // 就是第一层
        String workEffortId = "";
        Debug.logInfo("beforePartyId=" + beforePartyId + "|spm=" + spm, module);
        if (beforePartyId.trim().equals(spm.trim())) {

            workEffortAndProductAndParty = EntityQuery.use(delegator).from("WorkEffortAndProductAndPartyReFerrer").where(UtilMisc.toMap("productId", productId, "partyId", spm, "description", productId + spm)).queryFirst();
//        GenericValue isExsitsReferrer = EntityQuery.use(delegator).from("WorkEffortAndProductAndPartyReFerrer").where(UtilMisc.toMap("productId", productId, "partyId", , "description", productId + spm)).queryFirst();
            if (null == workEffortAndProductAndParty) {
                return resultMap;
            }

            workEffortId = (String) workEffortAndProductAndParty.get("workEffortId");
            System.out.println("workEffortId=" + workEffortId);
        } else {
            //不是第一层的人,以现在这曾为准
            workEffortAndProductAndParty = EntityQuery.use(delegator).from("WorkEffortAndProductAndPartyReFerrer").where(UtilMisc.toMap("productId", productId, "partyId", beforePartyId, "description", productId + beforePartyId)).queryFirst();
            workEffortId = (String) workEffortAndProductAndParty.get("workEffortId");
        }
        GenericValue workEffortAndProductAndPartyAddressee = EntityQuery.use(delegator).from("WorkEffortAndProductAndPartyAddressee").where(UtilMisc.toMap("productId", productId, "partyId", receivePartyId, "workEffortId", workEffortId)).queryFirst();
        //已经记录过了
        if (workEffortAndProductAndPartyAddressee != null) {
            return resultMap;
        }

        Map<String, Object> createAddresseeMap = UtilMisc.toMap("userLogin", admin, "partyId", receivePartyId,
                "roleTypeId", "ADDRESSEE", "statusId", "PRTYASGN_ASSIGNED", "workEffortId", workEffortId);
        Map<String, Object> createAddresseeResultMap = dispatcher.runSync("assignPartyToWorkEffort", createAddresseeMap);
        if (!ServiceUtil.isSuccess(createAddresseeResultMap)) {
            Debug.logInfo("*createAddresseeMap Fail:" + createAddresseeMap, module);
            return createAddresseeResultMap;
        }

        return resultMap;
    }

    /**
     * 收到资源引用
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     * @throws Exception
     */
    public static Map<String, Object> receivedInformation(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException, Exception {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        GenericValue userLogin = null;

        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        String partyId = (String) context.get("partyId");
        if (!UtilValidate.isEmpty(partyId)) {
            userLogin = EntityQuery.use(delegator).from("UserLogin").where(UtilMisc.toMap("partyId", partyId)).queryFirst();
        } else {
            userLogin = (GenericValue) context.get("userLogin");
        }
        // 当前收到引用的当事人
        String receivePartyId = (String) userLogin.get("partyId");
        // 来自引用当事人
        String spm = (String) context.get("spm");
        // 资源主
        String payToPartyId = (String) context.get("payToPartyId");
        // 资源ID
        String productId = (String) context.get("productId");

        GenericValue workEffortAndProductAndParty = null;


        // 卖家本人打开了分享,无意义。
        if (payToPartyId.equals(receivePartyId)) {
            return resultMap;
        }

        System.out.println("->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("spm=" + spm);
        System.out.println("receivePartyId=" + receivePartyId);
        System.out.println("payToPartyId=" + payToPartyId);
        System.out.println("partyId=" + partyId);

        // 说明上层引用就是资源主
        if (UtilValidate.isEmpty(spm)) {
            workEffortAndProductAndParty = EntityQuery.use(delegator).from("WorkEffortAndProductAndParty").where(UtilMisc.toMap("productId", productId, "partyId", payToPartyId, "description", productId + payToPartyId)).queryFirst();
        } else {
            // 说明上层引用不是资源主
            workEffortAndProductAndParty = EntityQuery.use(delegator).from("WorkEffortAndProductAndPartyReFerrer").where(UtilMisc.toMap("productId", productId, "partyId", spm, "description", productId + spm)).queryFirst();
        }

        if (null == workEffortAndProductAndParty) {
            return resultMap;
        }

        String workEffortId = (String) workEffortAndProductAndParty.get("workEffortId");
        System.out.println("workEffortId=" + workEffortId);

        GenericValue workEffortAndProductAndPartyAddressee = EntityQuery.use(delegator).from("WorkEffortAndProductAndPartyAddressee").where(UtilMisc.toMap("productId", productId, "partyId", receivePartyId, "workEffortId", workEffortId)).queryFirst();
        //已经记录过了
        if (workEffortAndProductAndPartyAddressee != null) {
            return resultMap;
        }

        Map<String, Object> createAddresseeMap = UtilMisc.toMap("userLogin", admin, "partyId", receivePartyId,
                "roleTypeId", "ADDRESSEE", "statusId", "PRTYASGN_ASSIGNED", "workEffortId", workEffortId);
        Map<String, Object> createAddresseeResultMap = dispatcher.runSync("assignPartyToWorkEffort", createAddresseeMap);
        if (!ServiceUtil.isSuccess(createAddresseeResultMap)) {
            Debug.logInfo("*createAddresseeMap Fail:" + createAddresseeMap, module);
            return createAddresseeResultMap;
        }

        return resultMap;
    }


    /**
     * 新转发发起
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     * @throws Exception
     */
    public static Map<String, Object> shareBProductInformation(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException, Exception {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        GenericValue userLogin = null;
        //当事人Id
        String partyId = (String) context.get("partyId");
        //销售代表Id
        String partyIdFrom = (String) context.get("partyIdFrom");
        if (!UtilValidate.isEmpty(partyId)) {
            userLogin = EntityQuery.use(delegator).from("UserLogin").where(UtilMisc.toMap("partyId", partyId)).queryFirst();
        } else {
            userLogin = (GenericValue) context.get("userLogin");
        }


        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));
        Map<String, Object> createWorkEffortMap = new HashMap<String, Object>();

        // 当前引用当事人
        String sharePartyIdFrom = (String) userLogin.get("partyId");
        // 资源主 不存在这个概念
        String payToPartyId = (String) context.get("payToPartyId");
        // 资源ID
        String productId = (String) context.get("productId");


        GenericValue product = delegator.findOne("Product", UtilMisc.toMap("productId", productId), false);
        if (null == product) {
            ServiceUtil.returnError("*Product Not Found");
        }
        // 资源名称
        String productName = (String) product.get("productName");

        // 当前用户是否是销售代表
        boolean isSalesRep = false;
        // 当前用户是否创建过这个记录,这个条件基于是销售代表
        boolean isCreated = true;

        GenericValue role = EntityQuery.use(delegator).from("ProductStoreRole").where("productStoreId", "ZUCZUGSTORE", "partyId", partyId, "roleTypeId", "SALES_REP").queryFirst();

        if (role != null) {
            isSalesRep = !isSalesRep;
        }

        //当事人是销售代表
        if (isSalesRep) {
            GenericValue isExsits = EntityQuery.use(delegator).from("WorkEffortAndProductAndPartySalesRep").where("productId", productId, "roleTypeId", "SALES_REP", "partyId", sharePartyIdFrom).queryFirst();
            if (null == isExsits) {
                String workEffortId = "";
                isCreated = false;
                //注意这个desc 很重要,起到了真正意义上的标识作用
                // 创建转发引用WorkEffort
                createWorkEffortMap = UtilMisc.toMap("userLogin", userLogin, "currentStatusId", "CAL_IN_PLANNING",
                        "workEffortName", "引用:" + productName, "workEffortTypeId", "EVENT", "description", productId + sharePartyIdFrom,
                        "actualStartDate", org.apache.ofbiz.base.util.UtilDateTime.nowTimestamp(), "percentComplete", new Long(1));
                Map<String, Object> serviceResultByCreateWorkEffortMap = dispatcher.runSync("createWorkEffort",
                        createWorkEffortMap);
                if (!ServiceUtil.isSuccess(serviceResultByCreateWorkEffortMap)) {
                    Debug.logInfo("*Create WorkEffort Fail:" + createWorkEffortMap, module);
                    return serviceResultByCreateWorkEffortMap;
                }
                workEffortId = (String) serviceResultByCreateWorkEffortMap.get("workEffortId");


                //SHIP_FROM_VENDOR
                // 增加资源主角色对于引用
                Map<String, Object> createShipFromVendorMap = UtilMisc.toMap("userLogin", admin, "partyId", payToPartyId,
                        "roleTypeId", "SHIP_FROM_VENDOR", "statusId", "PRTYASGN_ASSIGNED", "workEffortId", workEffortId);
                Map<String, Object> createAdminAssignPartyResultMap = dispatcher.runSync("assignPartyToWorkEffort", createShipFromVendorMap);
                if (!ServiceUtil.isSuccess(createAdminAssignPartyResultMap)) {
                    Debug.logInfo("*assignPartyToWorkEffort Fail:" + createShipFromVendorMap, module);
                    return createAdminAssignPartyResultMap;
                }

                //REFERRER
                //增加当前转发者对于转发引用的关联角色
                Map<String, Object> createReferrerMap = UtilMisc.toMap("userLogin", admin, "partyId", sharePartyIdFrom,
                        "roleTypeId", "REFERRER", "statusId", "PRTYASGN_ASSIGNED", "workEffortId", workEffortId);
                Map<String, Object> createReferrerResultMap = dispatcher.runSync("assignPartyToWorkEffort", createReferrerMap);
                if (!ServiceUtil.isSuccess(createReferrerResultMap)) {
                    Debug.logInfo("*create Referrer Map Fail:" + createReferrerMap, module);
                    return createReferrerResultMap;
                }

                // 把引用转发关联上产品
                Map<String, Object> createWorkEffortGoodStandardMap = UtilMisc.toMap("userLogin", admin, "statusId", "WEGS_CREATED",
                        "workEffortGoodStdTypeId", "GENERAL_SALES", "workEffortId", workEffortId, "productId", productId);
                Map<String, Object> createWorkEffortGoodStandardResultMap = dispatcher.runSync("createWorkEffortGoodStandard", createWorkEffortGoodStandardMap);
                if (!ServiceUtil.isSuccess(createWorkEffortGoodStandardResultMap)) {
                    Debug.logInfo("*Create WorkEffortGoodStandard Fail:" + createWorkEffortGoodStandardMap, module);
                    return createWorkEffortGoodStandardResultMap;
                }

                workEffortId = (String) serviceResultByCreateWorkEffortMap.get("workEffortId");


                //增加销售代表
                createReferrerMap = UtilMisc.toMap("userLogin", admin, "partyId", sharePartyIdFrom,
                        "roleTypeId", "SALES_REP", "statusId", "PRTYASGN_ASSIGNED", "workEffortId", workEffortId);
                createReferrerResultMap = dispatcher.runSync("assignPartyToWorkEffort", createReferrerMap);
                if (!ServiceUtil.isSuccess(createReferrerResultMap)) {
                    Debug.logInfo("*create Referrer Map Fail:" + createReferrerMap, module);
                    return createReferrerResultMap;
                }

            } else {

            }
        } else {

            Debug.logInfo("->当事人不是销售代表", module);
            Debug.logInfo("->以转发人的角度去看有没有转发过这个分享数据:" + "productId" + productId + "partyId" + sharePartyIdFrom + "description:", productId + sharePartyIdFrom, module);

            // 当事人不是销售代表
            // TODO FIXME 当事人不是销售代表也要创建自己的转发链条的
            // 以转发人的角度去看有没有转发过这个分享数据?
            GenericValue workEffortAndProductAndPartyReFerrer = EntityQuery.use(delegator).from("WorkEffortAndProductAndPartyReFerrer").where(UtilMisc.toMap("productId", productId, "partyId", sharePartyIdFrom)).queryFirst();
            // 是否存在自己的转发链条
            GenericValue isExsits = EntityQuery.use(delegator).from("WorkEffortAndProductAndPartyReFerrer").where(UtilMisc.toMap("productId", productId, "partyId", sharePartyIdFrom, "description", productId + sharePartyIdFrom)).queryFirst();

            // 已转发过,则增加转发次数,否则正常转发。
            if (null != workEffortAndProductAndPartyReFerrer) {
                long percentComplete = new Long(0);
                if (workEffortAndProductAndPartyReFerrer.get("percentComplete") != null) {
                    percentComplete = (long) workEffortAndProductAndPartyReFerrer.get("percentComplete");
                }
                String updateWorkEffortId = (String) workEffortAndProductAndPartyReFerrer.get("workEffortId");
                dispatcher.runAsync("updateWorkEffort", UtilMisc.toMap("userLogin", admin, "workEffortId", updateWorkEffortId, "percentComplete", percentComplete + 1));
            } else {
                //还没转发过,先找销售代表的那行记录拿workEffortId
                GenericValue salesRepWorkEffort = EntityQuery.use(delegator).from("WorkEffortAndProductAndPartySalesRep").where("productId", productId, "roleTypeId", "SALES_REP", "description", productId + partyIdFrom).queryFirst();
                Map<String, Object> createReferrerMap = UtilMisc.toMap("userLogin", admin, "partyId", sharePartyIdFrom,
                        "roleTypeId", "REFERRER", "statusId", "PRTYASGN_ASSIGNED", "workEffortId", salesRepWorkEffort.getString("workEffortId"));
                Map<String, Object> createReferrerResultMap = dispatcher.runSync("assignPartyToWorkEffort", createReferrerMap);
                if (!ServiceUtil.isSuccess(createReferrerResultMap)) {
                    Debug.logInfo("*create Referrer Map Fail:" + createReferrerMap, module);
                    return createReferrerResultMap;
                }

            }

            //没有自己的转发链条? 一次转发链条拥有 当前转发人、销售代表、 产品等主要特征
            GenericValue isExsitsSalesRep = null;
            if(null!= isExsits){
                String exsitWorkEffortId = isExsits.getString("workEffortId");
              isExsitsSalesRep =   EntityQuery.use(delegator).from("WorkEffortAndProductAndPartySalesRep").where("workEffortId",exsitWorkEffortId, "roleTypeId", "SALES_REP", "partyId", partyIdFrom).queryFirst();

            }
         if (null == isExsits && null == isExsitsSalesRep) {
                createWorkEffortMap = UtilMisc.toMap("userLogin", userLogin, "currentStatusId", "CAL_IN_PLANNING",
                        "workEffortName", "引用:" + productName, "workEffortTypeId", "EVENT", "description", productId + sharePartyIdFrom,
                        "actualStartDate", org.apache.ofbiz.base.util.UtilDateTime.nowTimestamp(), "percentComplete", new Long(1));
                Map<String, Object> serviceResultByCreateWorkEffortMap = dispatcher.runSync("createWorkEffort",
                        createWorkEffortMap);
                if (!ServiceUtil.isSuccess(serviceResultByCreateWorkEffortMap)) {
                    Debug.logInfo("*Create WorkEffort Fail:" + createWorkEffortMap, module);
                    return serviceResultByCreateWorkEffortMap;
                }
                String workEffortId = (String) serviceResultByCreateWorkEffortMap.get("workEffortId");

                //增加销售代表
                Map<String, Object> createReferrerMap = UtilMisc.toMap("userLogin", admin, "partyId", partyIdFrom,
                        "roleTypeId", "SALES_REP", "statusId", "PRTYASGN_ASSIGNED", "workEffortId", workEffortId);
                Map<String, Object> createReferrerResultMap = dispatcher.runSync("assignPartyToWorkEffort", createReferrerMap);
                if (!ServiceUtil.isSuccess(createReferrerResultMap)) {
                    Debug.logInfo("*create Referrer Map Fail:" + createReferrerMap, module);
                    return createReferrerResultMap;
                }

                // 把引用转发关联上产品
                Map<String, Object> createWorkEffortGoodStandardMap = UtilMisc.toMap("userLogin", admin, "statusId", "WEGS_CREATED",
                        "workEffortGoodStdTypeId", "GENERAL_SALES", "workEffortId", workEffortId, "productId", productId);
                Map<String, Object> createWorkEffortGoodStandardResultMap = dispatcher.runSync("createWorkEffortGoodStandard", createWorkEffortGoodStandardMap);
                if (!ServiceUtil.isSuccess(createWorkEffortGoodStandardResultMap)) {
                    Debug.logInfo("*Create WorkEffortGoodStandard Fail:" + createWorkEffortGoodStandardMap, module);
                    return createWorkEffortGoodStandardResultMap;
                }

                //增加当前转发者对于转发引用的关联角色
                createReferrerMap = UtilMisc.toMap("userLogin", admin, "partyId", sharePartyIdFrom,
                        "roleTypeId", "REFERRER", "statusId", "PRTYASGN_ASSIGNED", "workEffortId", workEffortId);
                createReferrerResultMap = dispatcher.runSync("assignPartyToWorkEffort", createReferrerMap);
                if (!ServiceUtil.isSuccess(createReferrerResultMap)) {
                    Debug.logInfo("*create Referrer Map Fail:" + createReferrerMap, module);
                    return createReferrerResultMap;
                }


            }

        }


        return resultMap;
    }


    /**
     * 转发-> 资源主的第一次发送资源到微信也是转发。转发就是引用。
     * 资源主转发自己的资源只有一条workEffort数据。
     * 每个人的转发都只有一次数据 不会出现重复。但这条数据将会递增接收人。
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     * @throws Exception
     */
    public static Map<String, Object> shareInformation(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException, Exception {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        GenericValue userLogin = null;
        String partyId = (String) context.get("partyId");
        if (!UtilValidate.isEmpty(partyId)) {
            userLogin = EntityQuery.use(delegator).from("UserLogin").where(UtilMisc.toMap("partyId", partyId)).queryFirst();
        } else {
            userLogin = (GenericValue) context.get("userLogin");
        }

        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));
        Map<String, Object> createWorkEffortMap = new HashMap<String, Object>();

        // 当前引用当事人
        String sharePartyIdFrom = (String) userLogin.get("partyId");
        // 资源主
        String payToPartyId = (String) context.get("payToPartyId");
        // 资源ID
        String productId = (String) context.get("productId");

        // 以资源主的角度去看有没有发布过这个分享数据
        GenericValue workEffortAndProductAndParty = EntityQuery.use(delegator).from("WorkEffortAndProductAndParty").where(UtilMisc.toMap("productId", productId, "partyId", payToPartyId, "description", productId + payToPartyId)).queryFirst();

        // 已分享过,则不再记录了
        if (null != workEffortAndProductAndParty && sharePartyIdFrom.equals(payToPartyId)) {
            Debug.logInfo("->Work Effort AndProductAndParty Is Exsits!<-", module);
            return resultMap;
        }

        // 以转发人的角度去看有没有转发过这个分享数据?
        GenericValue workEffortAndProductAndPartyReFerrer =
                EntityQuery.use(delegator).from("WorkEffortAndProductAndPartyReFerrer").where(UtilMisc.toMap("productId", productId, "partyId", sharePartyIdFrom, "description", productId + sharePartyIdFrom)).queryFirst();

        // 已转发过,则增加转发次数,否则正常转发。
        if (null != workEffortAndProductAndPartyReFerrer) {
            long percentComplete = new Long(0);
            if (workEffortAndProductAndPartyReFerrer.get("percentComplete") != null) {
                percentComplete = (long) workEffortAndProductAndPartyReFerrer.get("percentComplete");
            }

            String updateWorkEffortId = (String) workEffortAndProductAndPartyReFerrer.get("workEffortId");
            dispatcher.runAsync("updateWorkEffort", UtilMisc.toMap("userLogin", admin, "workEffortId", updateWorkEffortId, "percentComplete", percentComplete + 1));

        }


        GenericValue product = delegator.findOne("Product", UtilMisc.toMap("productId", productId), false);
        if (null == product) {
            ServiceUtil.returnError("*Product Not Found");
        }
        // 资源名称
        String productName = (String) product.get("productName");


        //注意这个desc 很重要,起到了真正意义上的标识作用
        // 创建转发引用WorkEffort
        createWorkEffortMap = UtilMisc.toMap("userLogin", userLogin, "currentStatusId", "CAL_IN_PLANNING",
                "workEffortName", "引用:" + productName, "workEffortTypeId", "EVENT", "description", productId + sharePartyIdFrom,
                "actualStartDate", org.apache.ofbiz.base.util.UtilDateTime.nowTimestamp(), "percentComplete", new Long(1));
        Map<String, Object> serviceResultByCreateWorkEffortMap = dispatcher.runSync("createWorkEffort",
                createWorkEffortMap);

        if (!ServiceUtil.isSuccess(serviceResultByCreateWorkEffortMap)) {
            Debug.logInfo("*Create WorkEffort Fail:" + createWorkEffortMap, module);
            return serviceResultByCreateWorkEffortMap;
        }

        String workEffortId = (String) serviceResultByCreateWorkEffortMap.get("workEffortId");


        //SHIP_FROM_VENDOR
        // 增加资源主角色对于引用
        Map<String, Object> createShipFromVendorMap = UtilMisc.toMap("userLogin", admin, "partyId", payToPartyId,
                "roleTypeId", "SHIP_FROM_VENDOR", "statusId", "PRTYASGN_ASSIGNED", "workEffortId", workEffortId);
        Map<String, Object> createAdminAssignPartyResultMap = dispatcher.runSync("assignPartyToWorkEffort", createShipFromVendorMap);
        if (!ServiceUtil.isSuccess(createAdminAssignPartyResultMap)) {
            Debug.logInfo("*assignPartyToWorkEffort Fail:" + createShipFromVendorMap, module);
            return createAdminAssignPartyResultMap;
        }

        //REFERRER
        //增加当前转发者对于转发引用的关联角色
        Map<String, Object> createReferrerMap = UtilMisc.toMap("userLogin", admin, "partyId", sharePartyIdFrom,
                "roleTypeId", "REFERRER", "statusId", "PRTYASGN_ASSIGNED", "workEffortId", workEffortId);
        Map<String, Object> createReferrerResultMap = dispatcher.runSync("assignPartyToWorkEffort", createReferrerMap);
        if (!ServiceUtil.isSuccess(createReferrerResultMap)) {
            Debug.logInfo("*create Referrer Map Fail:" + createReferrerMap, module);
            return createReferrerResultMap;
        }

        // 把引用转发关联上产品
        Map<String, Object> createWorkEffortGoodStandardMap = UtilMisc.toMap("userLogin", admin, "statusId", "WEGS_CREATED",
                "workEffortGoodStdTypeId", "GENERAL_SALES", "workEffortId", workEffortId, "productId", productId);
        Map<String, Object> createWorkEffortGoodStandardResultMap = dispatcher.runSync("createWorkEffortGoodStandard", createWorkEffortGoodStandardMap);
        if (!ServiceUtil.isSuccess(createWorkEffortGoodStandardResultMap)) {
            Debug.logInfo("*Create WorkEffortGoodStandard Fail:" + createWorkEffortGoodStandardMap, module);
            return createWorkEffortGoodStandardResultMap;
        }

        if (!sharePartyIdFrom.equals(payToPartyId)) {


            GenericValue sQueryProduct = EntityQuery.use(delegator).from("Product").where("productId", productId).queryFirst();

            GenericValue person = EntityQuery.use(delegator).from("Person").where("partyId", sharePartyIdFrom).queryFirst();


            Map<String, Object> createMessageLogMap = new HashMap<String, Object>();

            createMessageLogMap.put("partyIdFrom", partyId);

            createMessageLogMap.put("message", person.get("firstName") + "帮您转发了[" + sQueryProduct.get("productName") + "]");

            createMessageLogMap.put("messageId", delegator.getNextSeqId("MessageLog"));

            createMessageLogMap.put("partyIdTo", payToPartyId);

            createMessageLogMap.put("badge", "CHECK");

            createMessageLogMap.put("messageLogTypeId", "SYSTEM");

            createMessageLogMap.put("objectId", productId);


            createMessageLogMap.put("fromDate", org.apache.ofbiz.base.util.UtilDateTime.nowTimestamp());

            GenericValue msg = delegator.makeValue("MessageLog", createMessageLogMap);

            msg.create();

            //推送先不用ECA
            // 查询registrationID
            EntityCondition pConditions = EntityCondition.makeCondition("partyId", payToPartyId);
            List<EntityCondition> devTypeExprs = new ArrayList<EntityCondition>();
            devTypeExprs.add(EntityCondition.makeCondition("partyIdentificationTypeId", "JPUSH_ANDROID"));
            devTypeExprs.add(EntityCondition.makeCondition("partyIdentificationTypeId", "JPUSH_IOS"));
            EntityCondition devCondition = EntityCondition.makeCondition(devTypeExprs, EntityOperator.OR);
            pConditions = EntityCondition.makeCondition(pConditions, devCondition);
            List<GenericValue> partyIdentifications = delegator.findList("PartyIdentification", pConditions, null, UtilMisc.toList("-createdStamp"), null, false);

            if (null != partyIdentifications && partyIdentifications.size() > 0) {

                GenericValue partyIdentification = (GenericValue) partyIdentifications.get(0);
                String jpushId = (String) partyIdentification.getString("idValue");
                String partyIdentificationTypeId = (String) partyIdentification.get("partyIdentificationTypeId");
                String type = "JPUSH_IOS";
                if (partyIdentificationTypeId != null && partyIdentificationTypeId.toLowerCase().indexOf("android") > 0) {
                    type = "JPUSH_ANDROID";
                }
                try {
                    dispatcher.runSync("pushNotifOrMessage", UtilMisc.toMap("userLogin", admin, "productId", productId, "message", "order", "content", person.get("firstName") + "帮您转发了[" + sQueryProduct.get("productName") + "]", "regId", jpushId, "deviceType", partyIdentificationTypeId, "sendType", type, "objectId", productId));
                } catch (GenericServiceException e1) {
                    Debug.logError(e1.getMessage(), module);
//                return ServiceUtil.returnError(UtilProperties.getMessage(resourceError, "JPushError", locale));
                }

            }
        }
        return resultMap;
    }

    /**
     * updateWeChatResource(小程序)
     *
     * @param request
     * @param response
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     * @throws Exception
     */
    public static String updateWeChatResource(HttpServletRequest request, HttpServletResponse response) throws GenericEntityException, GenericServiceException, Exception {

        // Service Head
        Locale locale = UtilHttp.getLocale(request);

        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");

        Delegator delegator = (Delegator) request.getAttribute("delegator");

        HttpSession session = request.getSession();


        GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");

        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));


        String unioId = (String) request.getParameter("unioId");

        GenericValue partyIdentification = EntityQuery.use(delegator).from("PartyIdentification").where("idValue", unioId, "partyIdentificationTypeId", "WX_UNIO_ID").queryFirst();

        String tel = (String) request.getParameter("tel");

        String partyId = (String) partyIdentification.get("partyId");
        String productId = (String) request.getParameter("productId");
        String description = (String) request.getParameter("description");
        String productName = (String) request.getParameter("productName");
        String productPriceStr = (String) request.getParameter("price");
        String quantityTotaStr = (String) request.getParameter("kuCun");

        BigDecimal quantity = new BigDecimal(quantityTotaStr);
        BigDecimal price = new BigDecimal(productPriceStr);

        String filePaths = (String) request.getParameter("filePath");


        // 更新产品的服务Map
        Map<String, Object> updateProduct = UtilMisc.toMap("userLogin", admin, "productId", productId, "productName", productName, "description", description);


        GenericValue productPriceEntity = EntityQuery.use(delegator).from("ProductPrice").where("productId", productId).queryFirst();
        productPriceEntity.set("price", price);
        productPriceEntity.store();

        GenericValue productEntity = EntityQuery.use(delegator).from("Product").where("productId", productId).queryFirst();

        String detailImageUrl = (String) productEntity.get("detailImageUrl");


        //3. Create Inventory Item ..

        //3.1 Get Now InventoryItem Quantity
        GenericValue facility = EntityQuery.use(delegator).from("Facility").where("ownerPartyId", partyId).queryFirst();
        String originFacilityId = (String) facility.get("facilityId");
        Map<String, Object> getInventoryAvailableByFacilityMap = dispatcher.runSync("getInventoryAvailableByFacility", UtilMisc.toMap("userLogin", admin,
                "facilityId", originFacilityId, "productId", productId));
        if (!ServiceUtil.isSuccess(getInventoryAvailableByFacilityMap)) {
            return "error";
        }
        BigDecimal quantityOnHandTotal = (BigDecimal) getInventoryAvailableByFacilityMap.get("quantityOnHandTotal");
        BigDecimal availableToPromiseTotal = (BigDecimal) getInventoryAvailableByFacilityMap.get("availableToPromiseTotal");


        GenericValue productInventoryItem = EntityQuery.use(delegator).from("ProductInventoryItem").where("productId", productId).queryFirst();
        String inventoryItemId = (String) productInventoryItem.get("inventoryItemId");
//          -1,表示bigdemical小于bigdemical2；
//           0,表示bigdemical等于bigdemical2；
//           1,表示bigdemical大于bigdemical2；

        Map<String, Object> createInventoryItemDetailMap = new HashMap<String, Object>();
        createInventoryItemDetailMap.put("userLogin", admin);
        createInventoryItemDetailMap.put("inventoryItemId", inventoryItemId);


        Debug.logInfo("*update resource availableToPromiseTotal = " + availableToPromiseTotal, module);
        Debug.logInfo("*update resource quantity = " + quantity, module);
        Debug.logInfo("*update resource availableToPromiseTotal.compareTo(quantity)>0 = " + (availableToPromiseTotal.compareTo(quantity) > 0), module);

        //说明现库存比要设置的库存大,需要做差异减法
        if (availableToPromiseTotal.compareTo(quantity) > 0) {
            int availableToPromiseTotalInt = availableToPromiseTotal.intValue();
            int quantityInt = quantity.intValue();
            Debug.logInfo("*update resource quantityInt Diff =   " + quantityInt, module);
            Debug.logInfo("*update resource availableToPromiseTotalInt =   " + availableToPromiseTotalInt, module);

            createInventoryItemDetailMap.put("accountingQuantityDiff", new BigDecimal("-" + (availableToPromiseTotalInt - quantityInt)));
            createInventoryItemDetailMap.put("availableToPromiseDiff", new BigDecimal("-" + (availableToPromiseTotalInt - quantityInt)));
            createInventoryItemDetailMap.put("quantityOnHandDiff", new BigDecimal("-" + (availableToPromiseTotalInt - quantityInt)));
        }
        //说明现库存比要设置的库存小,需要做差异加法
        if (availableToPromiseTotal.compareTo(quantity) < 0) {
            int availableToPromiseTotalInt = availableToPromiseTotal.intValue();
            int quantityInt = quantity.intValue();
            Debug.logInfo("*update resource quantityInt Diff =   " + quantityInt, module);
            Debug.logInfo("*update resource availableToPromiseTotalInt =   " + availableToPromiseTotalInt, module);
            createInventoryItemDetailMap.put("accountingQuantityDiff", new BigDecimal("" + (quantityInt - availableToPromiseTotalInt)));
            createInventoryItemDetailMap.put("availableToPromiseDiff", new BigDecimal("" + (quantityInt - availableToPromiseTotalInt)));
            createInventoryItemDetailMap.put("quantityOnHandDiff", new BigDecimal("" + (quantityInt - availableToPromiseTotalInt)));
        }
        //一模一样的库存我还差异个屁?
        if (availableToPromiseTotal.compareTo(quantity) == 0) {

        }


        //3.2 Do create
        Map<String, Object> createInventoryItemDetailOutMap = dispatcher.runSync("createInventoryItemDetail", createInventoryItemDetailMap);

        if (!ServiceUtil.isSuccess(createInventoryItemDetailOutMap)) {
            return "error";
        }
        String[] filePathsArray = null;
        if (filePaths != null) {
            filePathsArray = filePaths.split(",");
        }

        GenericValue findProduct = EntityQuery.use(delegator).from("Product").where("productId", productId).queryFirst();

        //开始传图
        if (null != filePathsArray && filePathsArray.length > 0) {
            for (int i = 0; i < filePathsArray.length; i++) {
                System.out.println("->File Path = " + filePathsArray[i]);
                //说明首图被删除了
                if (findProduct.get("detailImageUrl") == null || (findProduct.get("detailImageUrl") + "").trim().equals("") || (findProduct.get("detailImageUrl") + "").length() < 40) {
                    System.out.println("detaillength = " + (findProduct.get("detailImageUrl") + "").length());
                    if (i == 0) {
                        updateProduct.put("smallImageUrl", "http://" + filePathsArray[i] + "?x-oss-process=image/resize,m_pad,h_50,w_50");
                        updateProduct.put("detailImageUrl", "http://" + filePathsArray[i]);
                    }
                    if (i > 0) {
                        //创建产品内容和数据资源附图
                        createProductContentAndDataResource(delegator, dispatcher, admin, productId, "", filePathsArray[i], i);
                    }
                } else {

                    //只追加为附图
                    createProductContentAndDataResource(delegator, dispatcher, admin, productId, "", filePathsArray[i], i);

                }
            }
        }

        //Update Product
        Map<String, Object> serviceResultMap = dispatcher.runSync("updateProduct", updateProduct);

        if (!ServiceUtil.isSuccess(serviceResultMap)) {
            return "error";
        }
        return "success";
    }


    /**
     * Update Resource Bis Info
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     * @throws Exception
     */
    public static String updateResourceInfo(HttpServletRequest request, HttpServletResponse response) throws GenericEntityException, GenericServiceException, Exception {

        // Service Head
        Locale locale = UtilHttp.getLocale(request);

        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");

        Delegator delegator = (Delegator) request.getAttribute("delegator");

        HttpSession session = request.getSession();


        GenericValue userLogin = (GenericValue) session.getAttribute("userLogin");
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        String partyId = (String) userLogin.get("partyId");
        String productId = (String) request.getParameter("productId");
        String description = (String) request.getParameter("description");
        String productName = (String) request.getParameter("productName");
        String productPriceStr = (String) request.getParameter("productPrice");


        String quantityTotaStr = (String) request.getParameter("quantityTotal");

        BigDecimal quantity = new BigDecimal(quantityTotaStr);
        BigDecimal price = new BigDecimal(productPriceStr);

        //1.Update Product
        Map<String, Object> serviceResultMap = dispatcher.runSync("updateProduct", UtilMisc.toMap("userLogin", userLogin
                , "productId", productId, "productName", productName, "description", description));

        if (!ServiceUtil.isSuccess(serviceResultMap)) {
            return "error";
        }
        //2. Update ProductPrice
        //TODO 不再使用原生服务更新产品价格
//       serviceResultMap = dispatcher.runSync("updateProductPrice",UtilMisc.toMap("userLogin",userLogin
//                ,"currencyUomId",PeConstant.DEFAULT_CURRENCY_UOM_ID,"fromDate",org.apache.ofbiz.base.util.UtilDateTime.nowTimestamp(),
//               "price",new BigDecimal(productPrice),
//               "productPricePurposeId","PURCHASE",
//               "productPriceTypeId","DEFAULT_PRICE",
//               "productStoreGroupId","_NA_",
//               "productId",productId));
//
//        if (!ServiceUtil.isSuccess(serviceResultMap)) {
//            return serviceResultMap;
//        }
        GenericValue productPriceEntity = EntityQuery.use(delegator).from("ProductPrice").where("productId", productId).queryFirst();
        productPriceEntity.set("price", price);
        productPriceEntity.store();

        GenericValue productEntity = EntityQuery.use(delegator).from("Product").where("productId", productId).queryFirst();

        String detailImageUrl = (String) productEntity.get("detailImageUrl");


        //3. Create Inventory Item ..

        //3.1 Get Now InventoryItem Quantity
        GenericValue facility = EntityQuery.use(delegator).from("Facility").where("ownerPartyId", partyId).queryFirst();
        String originFacilityId = (String) facility.get("facilityId");
        Map<String, Object> getInventoryAvailableByFacilityMap = dispatcher.runSync("getInventoryAvailableByFacility", UtilMisc.toMap("userLogin", admin,
                "facilityId", originFacilityId, "productId", productId));
        if (!ServiceUtil.isSuccess(getInventoryAvailableByFacilityMap)) {
            return "error";
        }
        BigDecimal quantityOnHandTotal = (BigDecimal) getInventoryAvailableByFacilityMap.get("quantityOnHandTotal");
        BigDecimal availableToPromiseTotal = (BigDecimal) getInventoryAvailableByFacilityMap.get("availableToPromiseTotal");


        GenericValue productInventoryItem = EntityQuery.use(delegator).from("ProductInventoryItem").where("productId", productId).queryFirst();
        String inventoryItemId = (String) productInventoryItem.get("inventoryItemId");
//          -1,表示bigdemical小于bigdemical2；
//           0,表示bigdemical等于bigdemical2；
//           1,表示bigdemical大于bigdemical2；

        Map<String, Object> createInventoryItemDetailMap = new HashMap<String, Object>();
        createInventoryItemDetailMap.put("userLogin", admin);
        createInventoryItemDetailMap.put("inventoryItemId", inventoryItemId);


        Debug.logInfo("*update resource availableToPromiseTotal = " + availableToPromiseTotal, module);
        Debug.logInfo("*update resource quantity = " + quantity, module);
        Debug.logInfo("*update resource availableToPromiseTotal.compareTo(quantity)>0 = " + (availableToPromiseTotal.compareTo(quantity) > 0), module);

        //说明现库存比要设置的库存大,需要做差异减法
        if (availableToPromiseTotal.compareTo(quantity) > 0) {
            int availableToPromiseTotalInt = availableToPromiseTotal.intValue();
            int quantityInt = quantity.intValue();
            Debug.logInfo("*update resource quantityInt Diff =   " + quantityInt, module);
            Debug.logInfo("*update resource availableToPromiseTotalInt =   " + availableToPromiseTotalInt, module);

            createInventoryItemDetailMap.put("accountingQuantityDiff", new BigDecimal("-" + (availableToPromiseTotalInt - quantityInt)));
            createInventoryItemDetailMap.put("availableToPromiseDiff", new BigDecimal("-" + (availableToPromiseTotalInt - quantityInt)));
            createInventoryItemDetailMap.put("quantityOnHandDiff", new BigDecimal("-" + (availableToPromiseTotalInt - quantityInt)));
        }
        //说明现库存比要设置的库存小,需要做差异加法
        if (availableToPromiseTotal.compareTo(quantity) < 0) {
            int availableToPromiseTotalInt = availableToPromiseTotal.intValue();
            int quantityInt = quantity.intValue();
            Debug.logInfo("*update resource quantityInt Diff =   " + quantityInt, module);
            Debug.logInfo("*update resource availableToPromiseTotalInt =   " + availableToPromiseTotalInt, module);
            createInventoryItemDetailMap.put("accountingQuantityDiff", new BigDecimal("" + (quantityInt - availableToPromiseTotalInt)));
            createInventoryItemDetailMap.put("availableToPromiseDiff", new BigDecimal("" + (quantityInt - availableToPromiseTotalInt)));
            createInventoryItemDetailMap.put("quantityOnHandDiff", new BigDecimal("" + (quantityInt - availableToPromiseTotalInt)));
        }
        //一模一样的库存我还差异个屁?
        if (availableToPromiseTotal.compareTo(quantity) == 0) {

        }


        //3.2 Do create
        Map<String, Object> createInventoryItemDetailOutMap = dispatcher.runSync("createInventoryItemDetail", createInventoryItemDetailMap);

        if (!ServiceUtil.isSuccess(createInventoryItemDetailOutMap)) {
            return "error";
        }


        try {
            //上传图片到Oss
            ServletFileUpload dfu = new ServletFileUpload(new DiskFileItemFactory(10240, null));
            List<FileItem> items = dfu.parseRequest(request);
            int itemSize = 0;
            int index = 0;
            if (null != items) {
                itemSize = items.size();
                //循环上传请求中的所有文件
                for (FileItem item : items) {
                    InputStream in = item.getInputStream();
                    String fileName = item.getName();

                    long tm = System.currentTimeMillis();
                    String pictureKey = OSSUnit.uploadObject2OSS(in, item.getName(), OSSUnit.getOSSClient(), null,
                            "personerp", PeConstant.PRODUCT_OSS_PATH, tm);
                    if (pictureKey != null && !pictureKey.equals("")) {
                        if (detailImageUrl == null || detailImageUrl.equals("")) {
                            //说明首图没了，先弄首图
                            detailImageUrl = PeConstant.OSS_PATH + PeConstant.PRODUCT_OSS_PATH + tm + fileName.substring(fileName.indexOf("."));
                            productEntity.set("smallImageUrl", PeConstant.OSS_PATH + PeConstant.PRODUCT_OSS_PATH + tm + fileName.substring(fileName.indexOf(".")) + "?x-oss-process=image/resize,m_pad,h_50,w_50");
                            productEntity.set("detailImageUrl", PeConstant.OSS_PATH + PeConstant.PRODUCT_OSS_PATH + tm + fileName.substring(fileName.indexOf(".")));
                            productEntity.store();
                        } else {
                            createProductContentAndDataResource(delegator, dispatcher, admin, productId, "", "https://personerp.oss-cn-hangzhou.aliyuncs.com/" + PeConstant.PRODUCT_OSS_PATH + tm + fileName.substring(fileName.indexOf(".")), index);

                        }
                    }

                    index++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "success";
    }


    /**
     * recruitingPartyToParty
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     * @throws Exception
     */
    public static Map<String, Object> recruitingPartyToParty(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException, Exception {

        // Service Head
        Locale locale = (Locale) context.get("locale");
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));
        String openId = (String) context.get("openId");
        String userName = (String) context.get("userName");
        String storeId = (String) context.get("storeId");
        String captcha = (String) context.get("captcha");
        String teleNumber = (String) context.get("teleNumber");


        GenericValue smsValidateCode = EntityQuery.use(delegator).from("SmsValidateCode").where("teleNumber", teleNumber, "isValid", "N").queryFirst();
        if (null != smsValidateCode && smsValidateCode.get("captcha").equals(captcha)) {
            smsValidateCode.set("isValid", "Y");
            smsValidateCode.store();
        } else {
            resultMap.put("message", "验证码错误!");
            return resultMap;
        }


        GenericValue partyIdentification = EntityQuery.use(delegator).from("PartyIdentification").where("idValue", openId).queryFirst();
        if (partyIdentification == null) {
            return ServiceUtil.returnError(UtilProperties.getMessage(resourceError, "NotFoundOpenId", locale));
        }

        GenericValue storeRole = EntityQuery.use(delegator).from("ProductStoreRoleAndStoreDetail").where("partyId", partyIdentification.get("partyId"), "roleTypeId", "SALES_REP").queryFirst();

        if (null == storeRole) {
            Map<String, Object> createProductStoreRoleOut = dispatcher.runSync("createProductStoreRole", UtilMisc.toMap("userLogin", admin, "partyId", partyIdentification.get("partyId"), "productStoreId", storeId, "roleTypeId", "SALES_REP"));
            if (!ServiceUtil.isSuccess(createProductStoreRoleOut)) {
                return createProductStoreRoleOut;
            }
            resultMap.put("message", "加入成功!");
        } else {
            resultMap.put("message", "已经是该店铺销售代表!");
        }


        GenericValue person = EntityQuery.use(delegator).from("Person").where("partyId", partyIdentification.get("partyId")).queryFirst();

        person.set("nickname", userName);

        person.store();

        return resultMap;
    }


    /**
     * 用户的联系信息
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     * @throws Exception
     */
    public static Map<String, Object> getUserContactInfo(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException, Exception {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        String unioId = (String) context.get("unioId");

        Debug.logInfo("*getUserContactInfo =>", module);
        GenericValue partyIdentification = EntityQuery.use(delegator).from("PartyIdentification").where("idValue", unioId, "partyIdentificationTypeId", "WX_UNIO_ID").queryFirst();

        String partyId = "NA";
        Map<String, Object> userContactInfo = new HashMap<String, Object>();

        if (UtilValidate.isNotEmpty(partyIdentification)) {
            partyId = (String) partyIdentification.get("partyId");


            //查询联系号码
            GenericValue teleContact = EntityQuery.use(delegator).from("TelecomNumberAndPartyView").where("partyId", partyId).queryFirst();

            if (null != teleContact) {
                String contactNumber = (String) teleContact.get("contactNumber");
                userContactInfo.put("contactTel", contactNumber);
            }
            //查询邮政地址,目的:收货地址
            Set<String> fieldSet = new HashSet<String>();
            fieldSet.add("contactMechId");
            fieldSet.add("partyId");
            fieldSet.add("address1");
            EntityCondition findConditions = EntityCondition
                    .makeCondition(UtilMisc.toMap("partyId", partyId));
            List<GenericValue> queryAddress = delegator.findList("PartyAndPostalAddress",
                    findConditions, fieldSet,
                    UtilMisc.toList("-fromDate"), null, false);

            String address1 = null;

            if (queryAddress != null & queryAddress.size() > 0) {
                GenericValue address = queryAddress.get(0);
                address1 = (String) address.get("address1");
                userContactInfo.put("address", address1);
            }
        }
        resultMap.put("contactInfo", userContactInfo);
        return resultMap;
    }


    /**
     * 对产品的评论
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     * @throws Exception
     */
    public static Map<String, Object> tuCao(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException, Exception {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));
        GenericValue userLogin = (GenericValue) context.get("userLogin");

        Debug.logInfo("*tuCaoProduct => Now UserLoing:" + userLogin, module);

        String userLoginId = (String) userLogin.get("userLoginId");
        String productId = (String) context.get("productId");
        String text = (String) context.get("text");


        Map<String, Object> createProductContentMap = dispatcher.runSync("createSimpleTextContentForProduct",
                UtilMisc.toMap("userLogin", userLogin, "productContentTypeId", "TUCAO", "productId", productId, "text", text, "createdByUserLogin", userLoginId));

        if (ServiceUtil.isError(createProductContentMap)) {
            return createProductContentMap;
        }

        return resultMap;
    }


    /**
     * createPartyToPartyRelation
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     * @throws Exception
     */
    public static Map<String, Object> createPartyToPartyRelation(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException, Exception {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        String partyIdFrom = (String) context.get("partyIdFrom");
        String partyIdTo = (String) context.get("partyIdTo");
        String relationShipType = (String) context.get("relationShipType");


        Long dataCount = EntityQuery.use(delegator).from("PartyRelationship").where("partyIdFrom", partyIdTo, "partyIdTo", partyIdFrom, "partyRelationshipTypeId", relationShipType).queryCount();
        Map<String, Object> createPartyRelationshipInMap = new HashMap<String, Object>();
        if (0 == dataCount) {
            createPartyRelationshipInMap.put("userLogin", admin);
            createPartyRelationshipInMap.put("partyIdFrom", partyIdTo);
            createPartyRelationshipInMap.put("partyIdTo", partyIdFrom);
            createPartyRelationshipInMap.put("partyRelationshipTypeId", relationShipType);
            Map<String, Object> createPartyRelationshipOutMap = dispatcher.runSync("createPartyRelationship", createPartyRelationshipInMap);
            if (ServiceUtil.isError(createPartyRelationshipOutMap)) {
                return createPartyRelationshipOutMap;
            }
        }
        dataCount = EntityQuery.use(delegator).from("PartyRelationship").where("partyIdFrom", partyIdFrom, "partyIdTo", partyIdTo, "partyRelationshipTypeId", relationShipType).queryCount();
        if (0 == dataCount) {
            createPartyRelationshipInMap = new HashMap<String, Object>();
            createPartyRelationshipInMap.put("userLogin", admin);
            createPartyRelationshipInMap.put("partyIdFrom", partyIdFrom);
            createPartyRelationshipInMap.put("partyIdTo", partyIdTo);
            createPartyRelationshipInMap.put("partyRelationshipTypeId", relationShipType);
            Map<String, Object> createPartyRelationshipOutMap = dispatcher.runSync("createPartyRelationship", createPartyRelationshipInMap);

            if (ServiceUtil.isError(createPartyRelationshipOutMap)) {
                return createPartyRelationshipOutMap;
            }
        }
        return resultMap;
    }


    //TODO ADD CODE

    /**
     * 响应客户请求并创建报价单
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     * @throws Exception
     */
    public static Map<String, Object> returnFormAndCreateQuoteFromCustRequest(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException, Exception {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        GenericValue userLogin = (GenericValue) context.get("userLogin");

        String partyId = (String) context.get("partyId");
        String formField = (String) context.get("formField");
        String price = (String) context.get("price");
        String custPartyId = (String) context.get("custPartyId");
        String custRequestId = (String) context.get("custRequestId");

        //创建产品报价给客户询价请求
        Map<String, Object> createQuoteMap = dispatcher.runSync("createQuoteFromCustRequest", UtilMisc.toMap("userLogin", admin, "quoteTypeId", "PRODUCT_QUOTE",
                "custRequestId", custRequestId));


        return resultMap;
    }


    /**
     * 创建客户请求
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     * @throws Exception
     */
    public static Map<String, Object> createCustRequestFromWeb(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException, Exception {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));


        String partyId = (String) context.get("partyId");
        String payToPartyId = (String) context.get("payToPartyId");
        String productId = (String) context.get("productId");
        String selectFeatures = (String) context.get("selectFeatures");
        String markText = (String) context.get("markText");
        if (null != markText) {
            markText = "备注:" + markText;
        }
        GenericValue product = delegator.findOne("Product", UtilMisc.toMap("productId", productId), false);
        HashSet<String> fieldSet = new HashSet<String>();
        fieldSet.add("productId");
        fieldSet.add("description");

        Set<String> descriptionSet = new HashSet<String>();

        String selectFeaturesList[] = selectFeatures.split(",");

        EntityCondition findConditions2 = EntityCondition
                .makeCondition("productId", EntityOperator.NOT_EQUAL, productId);

        String feature = "我选的特征:";

        for (String str : selectFeaturesList) {
            String rowStr = str.substring(str.indexOf("_") + 1);
            String type = str.substring(0, str.indexOf("_"));
            String getI18N = UtilProperties.getMessage(resourceUiLabels, "ProductFeatureType.description." + type, new Locale("zh"));
            if (getI18N.equals("ProductFeatureType.description." + type)) {
                GenericValue featureType = EntityQuery.use(delegator).from("UserPreferenceProductFeatures").where("productFeatureTypeId", type).queryFirst();

                feature += featureType.get("description") + "要" + rowStr + "的,";
            } else {
                feature += getI18N + "要" + rowStr + "的,";
            }

            descriptionSet.add(rowStr);
        }

        EntityCondition findConditions = EntityCondition
                .makeCondition("description", EntityOperator.IN, descriptionSet);

        EntityCondition findConditions3 = EntityCondition
                .makeCondition(findConditions, EntityOperator.AND, findConditions2);


        List<GenericValue> productFeatureAndAppl = EntityQuery.use(delegator).from("ProductFeatureAndAppl").where(findConditions3).distinct().queryList();
        // delegator.findList("ProductFeatureAndAppl", findConditions3, fieldSet, null, null, false);
        if (null != productFeatureAndAppl && productFeatureAndAppl.size() > 0) {
//            for(GenericValue gv : productFeatureAndAppl){
//                String
//            }
//            GenericValue productAppl = (GenericValue)productFeatureAndAppl.get(0);
//            System.out.println("");
        }
        String productStoreId = "";
        if (null != payToPartyId) {
            GenericValue store = EntityQuery.use(delegator).from("ProductStore").where(UtilMisc.toMap("payToPartyId", payToPartyId)).queryFirst();
            productStoreId = (String) store.get("productStoreId");
        }


        Map<String, Object> createCustRequest = dispatcher.runSync("createCustRequest",
                UtilMisc.toMap("userLogin", admin,
                        "custRequestTypeId", "RF_QUOTE",
                        "fromPartyId", partyId,
                        "description", markText,
                        "maximumAmountUomId", "CNY",
                        "productStoreId", productStoreId,
                        "salesChannelEnumId", "WEB_SALES_CHANNEL",
                        "currencyUomId", "CNY",
                        "internalComment", "",
                        "reason", markText,
                        "story", feature,
                        "productId", productId,
                        "custRequestName", "资源询价=>" + product.get("productName"),
                        "quantity", BigDecimal.ONE));
        String custRequestId = (String) createCustRequest.get("custRequestId");
        //发出请求的会员
        Map<String, Object> createCustRequestPartyRequesterMap = dispatcher.runSync("createCustRequestParty",
                UtilMisc.toMap("userLogin", admin,
                        "custRequestId", custRequestId,
                        "partyId", partyId,
                        "roleTypeId", "REQ_REQUESTER"));

        //请求接受者
        Map<String, Object> createCustRequestPartyTakerMap = dispatcher.runSync("createCustRequestParty",
                UtilMisc.toMap("userLogin", admin,
                        "custRequestId", custRequestId,
                        "partyId", payToPartyId,
                        "roleTypeId", "REQ_TAKER"));

        //推送一条消息
        pushMsgBase(productId, partyId, payToPartyId, delegator, dispatcher, admin, "我要询个价,你的这个资源:" + product.get("productName") + "。" + feature + "。" + markText, new HashMap<String, Object>(), admin, new HashMap<String, Object>(), "TEXT");


        return resultMap;
    }


    /**
     * 小程序创建的询价
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     * @throws Exception
     */
    public static Map<String, Object> createCustRequestFromMiniApp(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException, Exception {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));


        String unioId = (String) context.get("unioId");
        GenericValue partyIdentification = EntityQuery.use(delegator).from("PartyIdentification").where("idValue", unioId, "partyIdentificationTypeId", "WX_UNIO_ID").queryFirst();
        String partyId = "NA";
        if (UtilValidate.isNotEmpty(partyIdentification)) {
            partyId = (String) partyIdentification.get("partyId");
        }
        String payToPartyId = (String) context.get("payToPartyId");
        String productId = (String) context.get("productId");

        String markText = "没有备注";

        GenericValue product = delegator.findOne("Product", UtilMisc.toMap("productId", productId), false);


        String feature = "";


        String productStoreId = "";
        if (null != payToPartyId) {
            GenericValue store = EntityQuery.use(delegator).from("ProductStore").where(UtilMisc.toMap("payToPartyId", payToPartyId)).queryFirst();
            productStoreId = (String) store.get("productStoreId");
        }


        Map<String, Object> createCustRequest = dispatcher.runSync("createCustRequest",
                UtilMisc.toMap("userLogin", admin,
                        "custRequestTypeId", "RF_QUOTE",
                        "fromPartyId", partyId,
                        "description", markText,
                        "maximumAmountUomId", "CNY",
                        "productStoreId", productStoreId,
                        "salesChannelEnumId", "WEB_SALES_CHANNEL",
                        "currencyUomId", "CNY",
                        "internalComment", "",
                        "reason", markText,
                        "story", feature,
                        "productId", productId,
                        "custRequestName", "资源询价=>" + product.get("productName"),
                        "quantity", BigDecimal.ONE));
        String custRequestId = (String) createCustRequest.get("custRequestId");
        //发出请求的会员
        Map<String, Object> createCustRequestPartyRequesterMap = dispatcher.runSync("createCustRequestParty",
                UtilMisc.toMap("userLogin", admin,
                        "custRequestId", custRequestId,
                        "partyId", partyId,
                        "roleTypeId", "REQ_REQUESTER"));

        //请求接受者
        Map<String, Object> createCustRequestPartyTakerMap = dispatcher.runSync("createCustRequestParty",
                UtilMisc.toMap("userLogin", admin,
                        "custRequestId", custRequestId,
                        "partyId", payToPartyId,
                        "roleTypeId", "REQ_TAKER"));

        //推送一条消息
        pushMsgBase(productId, partyId, payToPartyId, delegator, dispatcher, admin, "我要询个价,你的这个资源:" + product.get("productName") + "。" + feature + "。" + markText, new HashMap<String, Object>(), admin, new HashMap<String, Object>(), "TEXT");


        return resultMap;
    }


    /**
     * add Distributing Leaflets (增加转发记录)
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     * @throws Exception
     */
    public static Map<String, Object> addDistributingLeaflets(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException, Exception {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();


        String sellerPartyId = (String) context.get("sellerPartyId");
        String buyerPartyId = (String) context.get("buyerPartyId");
        String workerPartyId = (String) context.get("workerPartyId");
        String productId = (String) context.get("productId");

        String temp = workerPartyId.substring(1);
        String partyIdTo = "";
        if (temp.indexOf(",") > 0) {
            partyIdTo = temp.substring(0, temp.indexOf(","));
        } else {
            partyIdTo = temp.substring(0);
        }

        System.out.println("============================>worker=" + partyIdTo + "|sellerPartyId=" + sellerPartyId + "buyerPartyId=>" + buyerPartyId);


        if (partyIdTo != null && buyerPartyId != null && partyIdTo.equals(buyerPartyId)) {
            return resultMap;
        }

        GenericValue person = delegator.findOne("Person", UtilMisc.toMap("partyId", partyIdTo), false);


        Map<String, Object> createDistributingLeafletsMap = new HashMap<String, Object>();

        createDistributingLeafletsMap.put("sellerPartyId", sellerPartyId);

        createDistributingLeafletsMap.put("buyerPartyId", buyerPartyId);

        createDistributingLeafletsMap.put("workerPartyId", partyIdTo);

        createDistributingLeafletsMap.put("productId", productId);

        createDistributingLeafletsMap.put("workerName", person.get("firstName"));

        GenericValue findDistributingLeaflets = EntityQuery.use(delegator).from("DistributingLeaflets").where(createDistributingLeafletsMap).queryFirst();


        //查客户关系
        GenericValue partyRelationship = EntityQuery.use(delegator).from("PartyRelationship").where("partyIdTo", sellerPartyId, "partyIdFrom", buyerPartyId, "roleTypeIdTo", "SHIP_FROM_VENDOR", "roleTypeIdFrom", "BILL_TO_CUSTOMER").queryFirst();

        if (null == findDistributingLeaflets && partyRelationship == null) {

            createDistributingLeafletsMap.put("DLId", delegator.getNextSeqId("DistributingLeaflets"));

            System.out.println("*createDistributingLeafletsData :" + createDistributingLeafletsMap);

            GenericValue distributingLeaflets = delegator.makeValue("DistributingLeaflets", createDistributingLeafletsMap);

            distributingLeaflets.create();
        } else {
            System.out.println("*Exsits DistributingLeaflets Data : " + createDistributingLeafletsMap);
        }


        return resultMap;
    }


    /**
     * doAddPartyRelation
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     * @throws Exception
     */
    public static Map<String, Object> doAddPartyRelation(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException, Exception {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();


        Delegator delegator = dispatcher.getDelegator();
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));
        String partyId = (String) context.get("partyId");

        String spm = (String) context.get("spm");


        Map<String, Object> resultMap = ServiceUtil.returnSuccess();

        if (spm == null || partyId == null) {
            return resultMap;
        }


        System.out.println(">>>>>>>>>>>>>>>>>>>>>>doAddPartyRelation>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        String temp = spm.substring(1);
        String partyIdTo = "";
        if (temp.indexOf(",") > 0) {
            partyIdTo = temp.substring(0, temp.indexOf(","));
        } else {
            partyIdTo = temp.substring(0);
        }

        if (partyId.equals(partyIdTo)) {
            return resultMap;
        }

        String partyIdFrom = partyId;
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>partyIdTo=" + partyIdTo);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>partyIdFrom=" + partyIdFrom);
        PersonManagerServices.createRelationCONTACT(delegator, dispatcher, admin, partyIdTo, partyIdFrom);

        return resultMap;
    }


    /**
     * addProductRole
     *
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
        if (productId == null || partyId == null) {
            return resultMap;
        }
        // 客户对于产品的角色

        GenericValue productRoleCust = EntityQuery.use(delegator).from("ProductRole").where("partyId", partyId, "roleTypeId", roleTypeId, "productId", productId).queryFirst();

        System.out.println("*ProductRole Compoment ------------------->");
        System.out.println("*ProductRole =" + roleTypeId);
        System.out.println("*ProductId =" + productId);
        System.out.println("*PartyId =" + partyId);
        System.out.println("*HisRole ? =" + (productRoleCust == null));
        System.out.println("*ProductRole Compoment <-------------------");
        if (null == productRoleCust) {
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

                System.out.println("*partyIdentificationList:" + partyIdentificationList);
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

                    pushWeChatMessageInfoMap.put("url", "http://www.yo-pe.com:3400/WebManager/control/shareProduct?productId=" + productId);

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


        Map<String, Object> serviceResultMap = dispatcher.runSync("createPayment", UtilMisc.toMap("statusId", "PMNT_RECEIVED", "paymentMethodId", paymentMethod.get("paymentMethodId"), "userLogin", admin, "partyIdTo", payToPartyId, "amount", orderHeader.get("grandTotal"), "partyIdFrom", partyId, "paymentTypeId", PeConstant.CUSTOMER_PAYMENT, "currencyUomId", PeConstant.DEFAULT_CURRENCY_UOM_ID, "comments", orderId));

        String paymentId = (String) serviceResultMap.get("paymentId");

        System.out.println("*create payment over | paymentid = " + paymentId);

        if (!ServiceUtil.isSuccess(serviceResultMap)) {

            return serviceResultMap;

        }

        //createOrderPaymentPreference
        Map<String, Object> createOrderPaymentPreferenceMap = dispatcher.runSync("createOrderPaymentPreference", UtilMisc.toMap("statusId", "PMNT_RECEIVED", "paymentMethodId", paymentMethod.get("paymentMethodId"), "userLogin", admin, "maxAmount", orderHeader.get("grandTotal"), "orderId", orderId));

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

        resultMap.put("paymentId", paymentId);
        return resultMap;
    }


    /**
     * TestCreatePeOrder(Demo)
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     * @throws Exception
     */
    public static Map<String, Object> createPeOrder(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException, Exception {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();

        // ProductID
        String productId = (String) context.get("productId");
        //产品的分类ID
        String productCategoryId = (String) context.get("productCategoryId");
        String prodCatalogId = (String) context.get("prodCatalogId");
        //下单的当事人,创建服务会检查他有没有创建权限等。
        String partyId = (String) context.get("partyId");
        //产品仓库
        String originFacilityId = (String) context.get("originFacilityId");
        //产品店铺
        String productStoreId = (String) context.get("productStoreId");
        //销售代表PartyId
        String salesRepPartyId = (String) context.get("salesRepPartyId");
        //供应商
        String billFromVendorPartyId = (String) context.get("billFromVendorPartyId");
        // Quantity  Amount
        BigDecimal quantity = (BigDecimal) context.get("quantity");

        //最终客户、收货客户、意向客户等客户当事人
        String billToCustomerPartyId, endUserCustomerPartyId, placingCustomerPartyId, shipToCustomerPartyId = partyId;
        //发货人就是供应商
        String supplierPartyId, shipFromVendorPartyId = billFromVendorPartyId;

        //StoreOrderMap
        Map<String, Object> createOrderServiceIn = new HashMap<String, Object>();

        //Order Items
        List<GenericValue> orderItemList = new ArrayList<GenericValue>();

        GenericValue product = EntityQuery.use(delegator).from("ProductAndPriceView").where("productId", productId).queryFirst();
        GenericValue itemProduct = delegator.makeValue("OrderItem", UtilMisc.toMap());

        itemProduct.set("productId", productId);
        itemProduct.set("isModifiedPrice", "N");
        itemProduct.set("shipBeforeDate", null);
        itemProduct.set("productCategoryId", productCategoryId);
        // Unit Price = List Price
        itemProduct.set("unitListPrice", context.get("grandTotal"));
        itemProduct.set("shoppingListId", null);
        itemProduct.set("cancelBackOrderDate", null);
        // Desc To Order Item List
        itemProduct.set("itemDescription", product.get("productName"));
        itemProduct.set("selectedAmount", BigDecimal.ZERO);
        itemProduct.set("orderItemTypeId", PeConstant.ORDER_ITEM_TYPE);
        itemProduct.set("orderItemSeqId", "00001");
        itemProduct.set("unitPrice", product.get("price"));
        itemProduct.set("quantity", quantity);
        itemProduct.set("comments", null);
        itemProduct.set("statusId", PeConstant.ORDER_ITEM_APPROVED_STATUS_ID);
        itemProduct.set("quoteItemSeqId", null);
        itemProduct.set("externalId", null);
        itemProduct.set("supplierProductId", null);
        itemProduct.set("prodCatalogId", prodCatalogId);

        orderItemList.add(itemProduct);

        createOrderServiceIn.put("currencyUom", PeConstant.DEFAULT_CURRENCY_UOM_ID);
        createOrderServiceIn.put("orderName", "FROM_" + partyId + "_TO_" + billFromVendorPartyId + "_BUY_" + productId);
        createOrderServiceIn.put("orderItems", orderItemList);
        createOrderServiceIn.put("orderTypeId", PeConstant.SALES_ORDER);
        createOrderServiceIn.put("partyId", partyId);
        createOrderServiceIn.put("billFromVendorPartyId", billFromVendorPartyId);
        createOrderServiceIn.put("productStoreId", productStoreId);
        createOrderServiceIn.put("originFacilityId", originFacilityId);

        createOrderServiceIn.put("billToCustomerPartyId", partyId);
        createOrderServiceIn.put("endUserCustomerPartyId", partyId);
        createOrderServiceIn.put("placingCustomerPartyId", partyId);
        createOrderServiceIn.put("shipToCustomerPartyId", partyId);

        createOrderServiceIn.put("supplierPartyId", billFromVendorPartyId);
        createOrderServiceIn.put("shipFromVendorPartyId", billFromVendorPartyId);
        createOrderServiceIn.put("userLogin", admin);


        //Do Run Service
        Map<String, Object> createOrderOut = dispatcher.runSync("storeOrder", createOrderServiceIn);

        if (!ServiceUtil.isSuccess(createOrderOut)) {
            return createOrderOut;
        }

        String orderId = (String) createOrderOut.get("orderId");


        //销售代表角色
        if (null != salesRepPartyId) {
            dispatcher.runSync("addOrderRole", UtilMisc.toMap("userLogin", admin, "orderId", orderId, "roleTypeId", "SALES_REP", "partyId", salesRepPartyId));
        }

        resultMap.put("orderId", orderId);

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

        GenericValue orderItem = EntityQuery.use(delegator).from("OrderItem").where("orderId", orderId).queryFirst();
        String productId = (String) orderItem.get("productId");
        GenericValue product = EntityQuery.use(delegator).from("Product").where("productId", productId).queryFirst();

        GenericValue orderCust = EntityQuery.use(delegator).from("OrderRole").where("orderId", orderId, "roleTypeId", "SHIP_TO_CUSTOMER").queryFirst();
        GenericValue orderSales = EntityQuery.use(delegator).from("OrderRole").where("orderId", orderId, "roleTypeId", "BILL_FROM_VENDOR").queryFirst();

        String payFromPartyId = (String) orderCust.get("partyId");
        String payToPartyId = (String) orderSales.get("partyId");
        GenericValue userLogin = EntityQuery.use(delegator).from("UserLogin").where("partyId", payFromPartyId).queryFirst();

        EntityCondition pConditions = EntityCondition.makeCondition("partyId", payFromPartyId);

        List<GenericValue> partyIdentifications = delegator.findList("PartyIdentification", pConditions, null, UtilMisc.toList("-createdStamp"), null, false);


        if (null != partyIdentifications && partyIdentifications.size() > 0) {
            GenericValue partyIdentification = (GenericValue) partyIdentifications.get(0);
            String jpushId = (String) partyIdentification.getString("idValue");
            String partyIdentificationTypeId = (String) partyIdentification.get("partyIdentificationTypeId");
            dispatcher.runSync("pushNotifOrMessage", UtilMisc.toMap("userLogin", admin, "message", "order", "content", "订单:" + orderId + "[" + product.get("productName") + "]" + "已被卖家取消。", "regId", jpushId, "deviceType", partyIdentificationTypeId, "sendType", "", "objectId", orderId));
        }

        //推送微信
        Map<String, Object> pushWeChatMessageInfoMap = new HashMap<String, Object>();
        //   pushMsgBase(orderId,payToPartyId ,payFromPartyId , delegator, dispatcher, userLogin, "订单:+" + orderId  +"["+product.get("productName")+"]已被卖家取消。", pushWeChatMessageInfoMap, admin, new HashMap<String, Object>(), "TEXT");


        List<GenericValue> partyIdentificationList = EntityQuery.use(delegator).from("PartyIdentification").where("partyId", payFromPartyId, "partyIdentificationTypeId", "WX_GZ_OPEN_ID").queryList();


        if (null != partyIdentificationList && partyIdentificationList.size() > 0) {


            System.out.println("*PUSH WE CHAT GONG ZHONG PLATFORM !!!!!!!!!!!!!!!!!!!!!!!");

            Date date = new Date();

            SimpleDateFormat formatter;

            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String pushDate = "" + formatter.format(date);

            pushWeChatMessageInfoMap.put("date", pushDate);

            String openId = (String) partyIdentificationList.get(0).get("idValue");

            pushWeChatMessageInfoMap.put("openId", openId);

            pushWeChatMessageInfoMap.put("productId", productId);
            pushWeChatMessageInfoMap.put("firstName", "友评");
            pushWeChatMessageInfoMap.put("message", "订单:" + orderId + "[" + product.get("productName") + "]" + "已被卖家取消。");

            pushWeChatMessageInfoMap.put("payToPartyId", payToPartyId);

            //推微信
            dispatcher.runSync("pushWeChatMessageInfo", pushWeChatMessageInfoMap);

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
        String partyId = (String) context.get("partyId");
        if (null != partyId) {
            //小程序确认收款
            userLogin = EntityQuery.use(delegator).from("UserLogin").where("partyId", partyId).queryFirst();
        } else {
            partyId = (String) userLogin.get("partyId");
        }


        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        Map<String, Object> resultMap = ServiceUtil.returnSuccess();

        String orderId = (String) context.get("orderId");
        GenericValue orderItem = EntityQuery.use(delegator).from("OrderItem").where("orderId", orderId).queryFirst();
        String productId = (String) orderItem.get("productId");
        GenericValue product = EntityQuery.use(delegator).from("Product").where("productId", productId).queryFirst();

        GenericValue orderHeader = EntityQuery.use(delegator).from("OrderHeader").where("orderId", orderId).queryFirst();


        Map<String, Object> createOrderPaymentPreferenceMap = new HashMap<String, Object>();
        createOrderPaymentPreferenceMap.put("userLogin", admin);
        createOrderPaymentPreferenceMap.put("orderId", orderId);
        createOrderPaymentPreferenceMap.put("createdByUserLogin", userLogin.get("userLoginId"));
        createOrderPaymentPreferenceMap.put("maxAmount", orderHeader.get("grandTotal"));
        createOrderPaymentPreferenceMap.put("overflowFlag", "N");
        createOrderPaymentPreferenceMap.put("paymentMethodTypeId", "EXT_WXPAY");
        // createOrderPaymentPreferenceMap.put("paymentMethodTypeId","EXT_COD");
        createOrderPaymentPreferenceMap.put("presentFlag", "N");
        createOrderPaymentPreferenceMap.put("statusId", "PAYMENT_RECEIVED");
        createOrderPaymentPreferenceMap.put("swipedFlag", "N");

        Map<String, Object> createOrderPaymentPreferenceOutMap =
                dispatcher.runSync("createOrderPaymentPreference", createOrderPaymentPreferenceMap);
        if (!ServiceUtil.isSuccess(createOrderPaymentPreferenceOutMap)) {
            return createOrderPaymentPreferenceOutMap;
        }

//        //找到买家
        GenericValue orderCust = EntityQuery.use(delegator).from("OrderRole").where("orderId", orderId, "roleTypeId", "SHIP_TO_CUSTOMER").queryFirst();

        String payFromPartyId = (String) orderCust.get("partyId");


//
//
//        GenericValue payFromUserLogin = EntityQuery.use(delegator).from("UserLogin").where("partyId", payFromPartyId).queryFirst();
//        //卖家确定自己已经收到钱了
//        Map<String, Object> createPaymentResult = dispatcher.runSync("createPaymentFromCust", UtilMisc.toMap("userLogin", payFromUserLogin, "payToPartyId", partyId, "orderId", orderId));
//

        //查找订单支付Id
        // GenericValue orderPaymentPrefAndPayment = EntityQuery.use(delegator).from("OrderPaymentPrefAndPayment").where("orderId", orderId).queryFirst();

        //这种情况下说明是先付钱,后发货的。


        //所以卖家应找到客户的付款 并且将状态改为'已收到'

        // GenericValue payment = EntityQuery.use(delegator).from("Payment").where("paymentTypeId", PeConstant.CUSTOMER_PAYMENT, "partyIdFrom", payFromPartyId, "partyIdTo", partyId).queryFirst();

//        String paymentId = (String) createPaymentResult.get("paymentId");
//
//        System.out.println("====================================== Payment Id =  " + paymentId);
//
//        Map<String, Object> setPaymentStatusMap = dispatcher.runSync("setPaymentStatus", UtilMisc.toMap("userLogin", userLogin, "paymentId", paymentId, "statusId", "PMNT_RECEIVED"));
//
//        if (!ServiceUtil.isSuccess(setPaymentStatusMap)) {
//            return setPaymentStatusMap;
//        }
//

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

        //应用收款支付.....
        receiveOfflinePayment("EXT_WXPAY", orderHeader.get("grandTotal").toString(), orderId, payFromPartyId, locale, delegator, dispatcher, userLogin);

//        Map<String, Object> updateOrderStatusInMap = new HashMap<String, Object>();
//        updateOrderStatusInMap.put("userLogin", admin);
//        updateOrderStatusInMap.put("orderId", orderId);
//        updateOrderStatusInMap.put("statusId", PeConstant.ORDER_APPROVED_STATUS_ID);
//        Map<String, Object> updateOrderStatusOutMap = dispatcher.runSync("changeOrderStatus", updateOrderStatusInMap);

        Map<String, Object> pushWeChatMessageInfoMap = new HashMap<String, Object>();
        //推送告知买家

        EntityCondition pConditions = EntityCondition.makeCondition("partyId", payFromPartyId);

        List<GenericValue> partyIdentifications = delegator.findList("PartyIdentification", pConditions, null, UtilMisc.toList("-createdStamp"), null, false);


        if (null != partyIdentifications && partyIdentifications.size() > 0) {
            GenericValue partyIdentification = (GenericValue) partyIdentifications.get(0);
            String jpushId = (String) partyIdentification.getString("idValue");
            String partyIdentificationTypeId = (String) partyIdentification.get("partyIdentificationTypeId");
            dispatcher.runSync("pushNotifOrMessage", UtilMisc.toMap("userLogin", admin, "message", "order", "content", "资源:" + product.get("productName") + "的卖家已确认货款到账!", "regId", jpushId, "deviceType", partyIdentificationTypeId, "sendType", "", "objectId", orderId));
        }


        //推送微信

        pushMsgBase(orderId, partyId, payFromPartyId, delegator, dispatcher, userLogin, "订单:+" + orderId + "|" + product.get("productName") + "的卖家已经确认货款到账!", pushWeChatMessageInfoMap, admin, new HashMap<String, Object>(), "TEXT");


        return resultMap;
    }


    public static String receiveOfflinePayment(String paymentMethodStr, String paymentAmountStr, String orderId, String partyId, Locale locale, Delegator delegator, LocalDispatcher dispatcher, GenericValue userLogin) {


        // get the order header & payment preferences
        GenericValue orderHeader = null;
        try {
            orderHeader = EntityQuery.use(delegator).from("OrderHeader").where("orderId", orderId).queryOne();
        } catch (GenericEntityException e) {
            Debug.logError(e, "Problems reading order header from datasource.", module);

        }

        BigDecimal grandTotal = BigDecimal.ZERO;
        if (orderHeader != null) {
            grandTotal = orderHeader.getBigDecimal("grandTotal");
        }

        // get the payment types to receive
        List<GenericValue> paymentMethodTypes = null;

        try {
            paymentMethodTypes = EntityQuery.use(delegator).from("PaymentMethodType").where(EntityCondition.makeCondition("paymentMethodTypeId", EntityOperator.EQUALS, "EXT_WXPAY")).queryList();
        } catch (GenericEntityException e) {
            Debug.logError(e, "Problems getting payment types", module);

        }
        Debug.logInfo("1616:paymentMethodTypes=" + paymentMethodTypes, module);
        if (paymentMethodTypes == null) {
            // request.setAttribute("_ERROR_MESSAGE_", UtilProperties.getMessage(resource_error, "OrderProblemsWithPaymentTypeLookup", locale));
            return "error";
        }

        // get the payment methods to receive
        List<GenericValue> paymentMethods = null;
        try {
            paymentMethods = EntityQuery.use(delegator).from("PaymentMethod").where("partyId", partyId).queryList();
        } catch (GenericEntityException e) {
            Debug.logError(e, "Problems getting payment methods", module);
            // request.setAttribute("_ERROR_MESSAGE_", UtilProperties.getMessage(resource_error, "OrderProblemsWithPaymentMethodLookup", locale));
            return "error";
        }

        GenericValue placingCustomer = null;
        try {
            placingCustomer = EntityQuery.use(delegator).from("OrderRole").where("orderId", orderId, "roleTypeId", "PLACING_CUSTOMER").queryFirst();
        } catch (GenericEntityException e) {
            Debug.logError(e, "Problems looking up order payment preferences", module);
            //    request.setAttribute("_ERROR_MESSAGE_", UtilProperties.getMessage(resource_error, "OrderErrorProcessingOfflinePayments", locale));
            return "error";
        }
        Debug.logInfo("1616:paymentMethods=" + paymentMethods, module);
        for (GenericValue paymentMethod : paymentMethods) {
            String paymentMethodId = paymentMethod.getString("paymentMethodId");
            String paymentMethodAmountStr = "0";
            if (paymentMethodId.equals(paymentMethodStr)) {
                paymentMethodAmountStr = paymentAmountStr;
            }

            //request.getParameter(paymentMethodId + "_amount");
            String paymentMethodReference = "";
            //request.getParameter(paymentMethodId + "_reference");
            if (UtilValidate.isNotEmpty(paymentMethodAmountStr)) {
                BigDecimal paymentMethodAmount = BigDecimal.ZERO;
                try {
                    paymentMethodAmount = (BigDecimal) ObjectType.simpleTypeConvert(paymentMethodAmountStr, "BigDecimal", null, locale);
                    Debug.logInfo("1616:paymentMethodAmount=" + paymentMethodAmount, module);
                } catch (GeneralException e) {
                    //    request.setAttribute("_ERROR_MESSAGE_", UtilProperties.getMessage(resource_error, "OrderProblemsPaymentParsingAmount", locale));
                    return "error";
                }
                Debug.logInfo("1616:paymentMethodAmount.compareTo(BigDecimal.ZERO) > 0=" + (paymentMethodAmount.compareTo(BigDecimal.ZERO) > 0), module);
                if (paymentMethodAmount.compareTo(BigDecimal.ZERO) > 0) {
                    // create a payment, payment reference and payment appl record, when not exist yet.
                    Map<String, Object> results = null;
                    try {
                        results = dispatcher.runSync("createPaymentFromOrder",
                                UtilMisc.toMap("orderId", orderId,
                                        "paymentMethodId", paymentMethodId,
                                        "paymentRefNum", paymentMethodReference,
                                        "comments", "Payment received offline and manually entered.",
                                        "userLogin", userLogin));
                    } catch (GenericServiceException e) {
                        Debug.logError(e, "Failed to execute service createPaymentFromOrder", module);
                        //         request.setAttribute("_ERROR_MESSAGE_", e.getMessage());
                        return "error";
                    }
                    Debug.logInfo("1616:results == null=" + (results), module);

                    if ((results == null) || (results.get(ModelService.RESPONSE_MESSAGE).equals(ModelService.RESPOND_ERROR))) {
                        Debug.logError((String) results.get(ModelService.ERROR_MESSAGE), module);
                        //   request.setAttribute("_ERROR_MESSAGE_", results.get(ModelService.ERROR_MESSAGE));
                        return "error";
                    }
                }
                //不用你再批准了谢谢
//                OrderChangeHelper.approveOrder(dispatcher, userLogin, orderId);

                //谁让你返回的
                //return "success";
            }
        }

        List<GenericValue> toBeStored = new LinkedList<GenericValue>();
        Debug.logInfo("1616:paymentMethodTypes=" + paymentMethodTypes, module);
        for (GenericValue paymentMethodType : paymentMethodTypes) {
            String paymentMethodTypeId = paymentMethodType.getString("paymentMethodTypeId");
            Debug.logInfo("1616:paymentMethodTypeId=" + paymentMethodTypeId, module);
            Debug.logInfo("1616:paymentMethodStr=" + paymentMethodStr, module);

            String amountStr = "0";
            Debug.logInfo("1616:paymentMethodTypeId.equals(paymentMethodStr)=" + (paymentMethodTypeId.equals(paymentMethodStr)), module);
            if (paymentMethodTypeId.equals(paymentMethodStr)) {
                amountStr = paymentAmountStr;
            }
            Debug.logInfo("1616:amountStr=" + amountStr, module);

            //request.getParameter(paymentMethodTypeId + "_amount");
            String paymentReference = "";
            //request.getParameter(paymentMethodTypeId + "_reference");
            if (UtilValidate.isNotEmpty(amountStr)) {
                BigDecimal paymentTypeAmount = BigDecimal.ZERO;
                try {
                    paymentTypeAmount = (BigDecimal) ObjectType.simpleTypeConvert(amountStr, "BigDecimal", null, locale);
                } catch (GeneralException e) {
                    //       request.setAttribute("_ERROR_MESSAGE_", UtilProperties.getMessage(resource_error, "OrderProblemsPaymentParsingAmount", locale));
                    return "error";
                }
                Debug.logInfo("1616:paymentTypeAmount=" + paymentTypeAmount, module);

                Debug.logInfo("1616:paymentTypeAmount.compareTo(BigDecimal.ZERO) > 0=" + (paymentTypeAmount.compareTo(BigDecimal.ZERO) > 0), module);

                if (paymentTypeAmount.compareTo(BigDecimal.ZERO) > 0) {
                    // create the OrderPaymentPreference
                    // TODO: this should be done with a service
                    Map<String, String> prefFields = UtilMisc.<String, String>toMap("orderPaymentPreferenceId", delegator.getNextSeqId("OrderPaymentPreference"));
                    GenericValue paymentPreference = delegator.makeValue("OrderPaymentPreference", prefFields);
                    paymentPreference.set("paymentMethodTypeId", paymentMethodType.getString("paymentMethodTypeId"));
                    paymentPreference.set("maxAmount", paymentTypeAmount);
                    paymentPreference.set("statusId", "PAYMENT_RECEIVED");
                    paymentPreference.set("orderId", orderId);
                    paymentPreference.set("createdDate", UtilDateTime.nowTimestamp());
                    if (userLogin != null) {
                        paymentPreference.set("createdByUserLogin", userLogin.getString("userLoginId"));
                    }

                    try {
                        Debug.logInfo("1616:create a new OrderPaymentPreference", module);
                        delegator.create(paymentPreference);
                    } catch (GenericEntityException ex) {
                        Debug.logError(ex, "Cannot create a new OrderPaymentPreference", module);
                        //          request.setAttribute("_ERROR_MESSAGE_", ex.getMessage());
                        return "error";
                    }

                    // create a payment record
                    Map<String, Object> results = null;
                    try {
                        System.out.println("paymentRefNum=" + paymentReference);
                        System.out.println("paymentPreference.orderPaymentPreferenceId=" + paymentPreference.get("orderPaymentPreferenceId"));
                        System.out.println("paymentPreference.orderPaymentPreferenceId=" + paymentPreference.get("orderPaymentPreferenceId"));
                        System.out.println("paymentFromId=" + placingCustomer.getString("partyId"));
                        results = dispatcher.runSync("createPaymentFromPreference", UtilMisc.toMap("userLogin", userLogin,
                                "orderPaymentPreferenceId", paymentPreference.get("orderPaymentPreferenceId"), "paymentRefNum", paymentReference,
                                "paymentFromId", placingCustomer.getString("partyId"), "comments", "Payment received offline and manually entered."));
                    } catch (GenericServiceException e) {
                        Debug.logError(e, "Failed to execute service createPaymentFromPreference", module);
                        //          request.setAttribute("_ERROR_MESSAGE_", e.getMessage());
                        return "error";
                    }

                    if ((results == null) || (results.get(ModelService.RESPONSE_MESSAGE).equals(ModelService.RESPOND_ERROR))) {
                        Debug.logError((String) results.get(ModelService.ERROR_MESSAGE), module);
                        //        request.setAttribute("_ERROR_MESSAGE_", results.get(ModelService.ERROR_MESSAGE));
                        return "error";
                    }
                }
            }
        }

        // get the current payment prefs
        GenericValue offlineValue = null;
        List<GenericValue> currentPrefs = null;
        BigDecimal paymentTally = BigDecimal.ZERO;
        try {
            EntityConditionList<EntityExpr> ecl = EntityCondition.makeCondition(UtilMisc.toList(
                            EntityCondition.makeCondition("orderId", EntityOperator.EQUALS, orderId),
                            EntityCondition.makeCondition("statusId", EntityOperator.NOT_EQUAL, "PAYMENT_CANCELLED")),
                    EntityOperator.AND);
            currentPrefs = EntityQuery.use(delegator).from("OrderPaymentPreference").where(ecl).queryList();
        } catch (GenericEntityException e) {
            Debug.logError(e, "ERROR: Unable to get existing payment preferences from order", module);
        }
        if (UtilValidate.isNotEmpty(currentPrefs)) {
            for (GenericValue cp : currentPrefs) {
                String paymentMethodType = cp.getString("paymentMethodTypeId");
                if ("EXT_OFFLINE".equals(paymentMethodType)) {
                    offlineValue = cp;
                } else {
                    BigDecimal cpAmt = cp.getBigDecimal("maxAmount");
                    if (cpAmt != null) {
                        paymentTally = paymentTally.add(cpAmt);
                    }
                }
            }
        }

        // now finish up
        boolean okayToApprove = false;
        if (paymentTally.compareTo(grandTotal) >= 0) {
            // cancel the offline preference
            okayToApprove = true;
            if (offlineValue != null) {
                offlineValue.set("statusId", "PAYMENT_CANCELLED");
                toBeStored.add(offlineValue);
            }
        }

        // store the status changes and the newly created payment preferences and payments
        // TODO: updating order payment preference should be done with a service
        try {
            delegator.storeAll(toBeStored);
        } catch (GenericEntityException e) {
            Debug.logError(e, "Problems storing payment information", module);
            //      request.setAttribute("_ERROR_MESSAGE_", UtilProperties.getMessage(resource_error, "OrderProblemStoringReceivedPaymentInformation", locale));
            return "error";
        }

        if (okayToApprove) {
            // update the status of the order and items 不用再批准了谢谢
            // OrderChangeHelper.approveOrder(dispatcher, userLogin, orderId);
        }

        return "success";
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
        net.sf.json.JSONObject result = null;
        String entityStr = null;

        HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
        //   System.out.println(response.toString());
        //获取response的body

        try {
            entityStr = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }
        net.sf.json.JSONObject jsonMap2 = net.sf.json.JSONObject.fromObject(entityStr);
        Debug.logInfo("*QueryExpressInfo:" + jsonMap2, module);
        JSONArray list = null;
        try {
            result = net.sf.json.JSONObject.fromObject(jsonMap2.get("result") + "");
            Debug.logInfo("*QueryExpressInfo=" + result, module);

            type = (String) result.get("type");
            Debug.logInfo("*QueryExpressInfo=" + type, module);

            list = (JSONArray) result.get("list");
            Debug.logInfo("*list=" + list, module);
        } catch (Exception e) {
            Debug.logInfo("--" + UtilProperties.getMessage(resourceError, "ExpressInfoNotFound", locale), module);
            Debug.logInfo(e, module);
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
     * selectPartyPostalAddress2Order
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> selectPartyPostalAddress2Order(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException {
        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();

        Delegator delegator = dispatcher.getDelegator();

        Locale locale = (Locale) context.get("locale");


        GenericValue userLogin = (GenericValue) context.get("userLogin");

        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        Map<String, Object> resultMap = ServiceUtil.returnSuccess();

        String orderId = (String) context.get("orderId");
        String subscribe = (String) context.get("subscribe");

        resultMap.put("subscribe", subscribe);

        String contactMechId = (String) context.get("contactMechId");


        Map<String, Object> updateShipGroupShipInfoOutMap = dispatcher.runSync("updateShipGroupShipInfo", UtilMisc.toMap(
                "userLogin", userLogin, "orderId", orderId,
                "contactMechId", contactMechId, "shipmentMethod", "EXPRESS@" + "SHUNFENG_EXPRESS", "shipGroupSeqId", "00001"));

        if (!ServiceUtil.isSuccess(updateShipGroupShipInfoOutMap)) {
            return updateShipGroupShipInfoOutMap;
        }


        return resultMap;
    }

    /**
     * 删除产品的图片
     *
     * @param dctx
     * @param context
     * @return
     * @throws IOException
     * @throws GenericEntityException
     * @throws GenericServiceException
     * @throws InterruptedException
     */
    public static Map<String, Object> removeResourcePicture(DispatchContext dctx, Map<String, ? extends Object> context)
            throws IOException, GenericEntityException, GenericServiceException, InterruptedException {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dctx.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> inputMap = new HashMap<String, Object>();
        Map<String, Object> result = ServiceUtil.returnSuccess();

        // Scope Param

        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));


        // contentId
        String contentId = (String) context.get("contentId");
        String productId = (String) context.get("productId");

        //说明删除的是详情图
        if (contentId != null && contentId.equals("308561217_784838898")) {

            GenericValue product = EntityQuery.use(delegator).from("Product").where("productId", productId).queryFirst();
            product.set("detailImageUrl", "");
            product.store();


        } else {


            GenericValue contentAndDataResource = EntityQuery.use(delegator).from("ProductContentAndInfo").where("productId", productId, "contentId", contentId).queryFirst();


            // Update Content
            try {
                Map<String, Object> serviceInputMap = UtilMisc.toMap("userLogin", admin, "contentId", contentId,
                        "fromDate", contentAndDataResource.get("fromDate"), "productContentTypeId", "ADDITIONAL_OTHER",
                        "productId", productId);

                dispatcher.runSync("removeProductContent", serviceInputMap);

                String dataResourceId = (String) contentAndDataResource.get("dataResourceId");

                GenericValue dataResource = EntityQuery.use(delegator).from("DataResource")
                        .where("dataResourceId", dataResourceId).queryOne();

                String pid = (String) dataResource.get("dataResourceName");

                OSSUnit.deleteFile(OSSUnit.getOSSClient(), "personerp", "datas/product_img/", pid);

            } catch (Exception e) {
                inputMap.put("resultMsg", UtilProperties.getMessage("PersonManagerERRORLabels", "DELETEERROR", locale));
            }


        }
        result.put("resultMap", inputMap);
        return result;
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

        // Sudo
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();


        String code = (String) context.get("code");
        String orderId = (String) context.get("orderId");
        String partyId = (String) context.get("partyId");
        //小程序确认收款
        if (null != partyId) {
            userLogin = EntityQuery.use(delegator).from("UserLogin").where("partyId", partyId).queryFirst();
        } else {
            partyId = (String) userLogin.get("partyId");
        }
        String carrierCode = (String) context.get("carrierCode");
        String name = (String) context.get("name");
        String shipmentMethodId = "";
        String contactMechId = "";
        String sinceTheSend = (String) context.get("sinceTheSend");


        GenericValue orderCust = EntityQuery.use(delegator).from("OrderRole").where("orderId", orderId, "roleTypeId", "SHIP_TO_CUSTOMER").queryFirst();


        GenericValue postalAddress = EntityUtil.getFirst(
                EntityQuery.use(delegator).from("PartyContactMechPurpose").where(UtilMisc.toMap("partyId", orderCust.get("partyId"), "contactMechPurposeTypeId", "SHIPPING_LOCATION")).queryList());

        if (null == postalAddress) {
        } else {
            contactMechId = (String) postalAddress.get("contactMechId");
        }

        GenericValue store = EntityQuery.use(delegator).from("ProductStore").where(UtilMisc.toMap("payToPartyId", partyId)).queryFirst();

        String productStoreId = (String) store.get("productStoreId");
        if (null != sinceTheSend && sinceTheSend.equals("1")) {
            //卖家自发货,不用物流单位

            GenericValue orderHeader = delegator.findOne("OrderHeader", UtilMisc.toMap("orderId", orderId), false);
            String stautsId = (String) orderHeader.get("statusId");

            if (stautsId.equals("ORDER_APPROVED")) {
                Map<String, Object> changeOrderStatusOutMap = dispatcher.runSync("changeOrderStatus", UtilMisc.toMap(
                        "userLogin", admin, "orderId", orderId, "statusId", "ORDER_SENT",
                        "changeReason", "订单发送", "setItemStatus", "Y"));

                if (!ServiceUtil.isSuccess(changeOrderStatusOutMap)) {
                    return changeOrderStatusOutMap;
                }
            } else {
                Map<String, Object> changeOrderStatusOutMap = dispatcher.runSync("changeOrderStatus", UtilMisc.toMap(
                        "userLogin", admin, "orderId", orderId, "statusId", "ORDER_APPROVED",
                        "changeReason", "订单批准", "setItemStatus", "Y"));

                if (!ServiceUtil.isSuccess(changeOrderStatusOutMap)) {
                    return changeOrderStatusOutMap;
                }
                changeOrderStatusOutMap = dispatcher.runSync("changeOrderStatus", UtilMisc.toMap(
                        "userLogin", admin, "orderId", orderId, "statusId", "ORDER_SENT",
                        "changeReason", "订单发送", "setItemStatus", "Y"));

                if (!ServiceUtil.isSuccess(changeOrderStatusOutMap)) {
                    return changeOrderStatusOutMap;
                }
            }

            GenericValue order = delegator.findOne("OrderHeader", UtilMisc.toMap("orderId", orderId), false);
            order.set("internalCode", "卖家自配送");
            order.store();


        } else {


            //查询店铺是否拥有该货运方式

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


            GenericValue orderHeader = delegator.findOne("OrderHeader", UtilMisc.toMap("orderId", orderId), false);
            String stautsId = (String) orderHeader.get("statusId");

            if (stautsId.equals("ORDER_APPROVED")) {
                Map<String, Object> changeOrderStatusOutMap = dispatcher.runSync("changeOrderStatus", UtilMisc.toMap(
                        "userLogin", userLogin, "orderId", orderId, "statusId", "ORDER_SENT",
                        "changeReason", "订单发送", "setItemStatus", "Y"));

                if (!ServiceUtil.isSuccess(changeOrderStatusOutMap)) {
                    return changeOrderStatusOutMap;
                }
            } else {
                Map<String, Object> changeOrderStatusOutMap = dispatcher.runSync("changeOrderStatus", UtilMisc.toMap(
                        "userLogin", userLogin, "orderId", orderId, "statusId", "ORDER_APPROVED",
                        "changeReason", "订单批准", "setItemStatus", "Y"));

                if (!ServiceUtil.isSuccess(changeOrderStatusOutMap)) {
                    return changeOrderStatusOutMap;
                }
                changeOrderStatusOutMap = dispatcher.runSync("changeOrderStatus", UtilMisc.toMap(
                        "userLogin", userLogin, "orderId", orderId, "statusId", "ORDER_SENT",
                        "changeReason", "订单发送", "setItemStatus", "Y"));

                if (!ServiceUtil.isSuccess(changeOrderStatusOutMap)) {
                    return changeOrderStatusOutMap;
                }
            }

            GenericValue order = delegator.findOne("OrderHeader", UtilMisc.toMap("orderId", orderId), false);
            order.set("internalCode", code);
            order.store();
        }

        //推送给微信用户

        List<GenericValue> partyIdentificationList = EntityQuery.use(delegator).from("PartyIdentification").where("partyId", orderCust.get("partyId"), "partyIdentificationTypeId", "WX_GZ_OPEN_ID").queryList();
        GenericValue orderItem = EntityQuery.use(delegator).from("OrderItem").where("orderId", orderId).queryFirst();
        String productId = (String) orderItem.get("productId");
        GenericValue product = EntityQuery.use(delegator).from("Product").where("productId", productId).queryFirst();


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
            if (null != sinceTheSend && sinceTheSend.equals("1")) {
                //自配送
                pushWeChatMessageInfoMap.put("messageInfo", "您购买的(" + product.get("productName") + ")我发货了," + "由我亲自给您配送!");

            } else {
                pushWeChatMessageInfoMap.put("messageInfo", "您购买的(" + product.get("productName") + ")我已发货" + ",物流公司是" + name + "。物流单号:" + (code == null ? "暂无详细" : code));
            }


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


            pushWeChatMessageInfoMap.put("jumpUrl", "http://www.yo-pe.com:3400/WebManager/control/myOrderDetail?orderId=" + orderId + "&tarjeta=" + signer.sign(claims));
            //推微信订单状态
            dispatcher.runSync("pushOrderStatusInfo", pushWeChatMessageInfoMap);
        }


        Map<String, Object> createMessageLogMap = new HashMap<String, Object>();

        createMessageLogMap.put("partyIdFrom", partyId);


        if (null != sinceTheSend && sinceTheSend.equals("1")) {
            //自配送
            createMessageLogMap.put("message", "您购买的(" + product.get("productName") + ")我发货了," + "由我亲自给您配送!");

        } else {
            createMessageLogMap.put("message", "您购买的(" + product.get("productName") + ")我已发货" + ",物流公司是" + name + "。物流单号:" + (code == null ? "暂无详细" : code));
        }
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
     * createPersonPartyPostalAddressFromMiniprogram
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> createPersonPartyPostalAddressFromMiniprogram(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();

        Delegator delegator = dispatcher.getDelegator();

        Locale locale = (Locale) context.get("locale");


        GenericValue userLogin = (GenericValue) context.get("userLogin");

        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        Map<String, Object> resultMap = ServiceUtil.returnSuccess();

        String tarjeta = (String) context.get("tarjeta");
        String orderId = (String) context.get("orderId");

        String partyId = (String) userLogin.get("partyId");

        String userName = (String) context.get("userName");
        String postalCode = (String) context.get("postalCode");
        String provinceName = (String) context.get("provinceName");
        String cityName = (String) context.get("cityName");
        String countyName = (String) context.get("countyName");
        String detailInfo = (String) context.get("detailInfo");
        String nationalCode = (String) context.get("nationalCode");
        String telNumber = (String) context.get("telNumber");
        //没有给订单号的情况下只是增加地址
        System.out.println("ORDER_ID=" + orderId);
        if (orderId.toUpperCase().equals("NA")) {
            // 货运目的地址
            String contactMechPurposeTypeId = "SHIPPING_LOCATION";
            Map<String, Object> createPartyPostalAddressOutMap = dispatcher.runSync("createPartyPostalAddress",
                    UtilMisc.toMap("userLogin", admin, "toName", userName, "partyId", partyId, "countryGeoId", PeConstant.DEFAULT_GEO_COUNTRY, "city", PeConstant.DEFAULT_CITY_GEO_COUNTRY, "address1", provinceName + " " + cityName + " " + countyName + " " + detailInfo, "postalCode", postalCode,
                            "contactMechPurposeTypeId", contactMechPurposeTypeId, "comments", telNumber));
            String contactMechId = (String) createPartyPostalAddressOutMap.get("contactMechId");
            if (!ServiceUtil.isSuccess(createPartyPostalAddressOutMap)) {
                return createPartyPostalAddressOutMap;
            }
            return resultMap;
        }

        resultMap.put("orderId", orderId);
        resultMap.put("tarjeta", tarjeta);


        List<GenericValue> partyAndPostalAddress = EntityQuery.use(delegator).from("PartyAndPostalAddress").where("address1", provinceName + " " + cityName + " " + countyName + " " + detailInfo).queryList();

        String contactMechId = "";

        if (null == partyAndPostalAddress || partyAndPostalAddress.size() == 0) {
            // 货运目的地址
            String contactMechPurposeTypeId = "SHIPPING_LOCATION";
            Map<String, Object> createPartyPostalAddressOutMap = dispatcher.runSync("createPartyPostalAddress",
                    UtilMisc.toMap("userLogin", admin, "toName", userName, "partyId", partyId, "countryGeoId", PeConstant.DEFAULT_GEO_COUNTRY, "city", PeConstant.DEFAULT_CITY_GEO_COUNTRY, "address1", provinceName + " " + cityName + " " + countyName + " " + detailInfo, "postalCode", postalCode,
                            "contactMechPurposeTypeId", contactMechPurposeTypeId, "comments", telNumber));
            contactMechId = (String) createPartyPostalAddressOutMap.get("contactMechId");
            if (!ServiceUtil.isSuccess(createPartyPostalAddressOutMap)) {
                return createPartyPostalAddressOutMap;
            }
        } else {
            GenericValue contactAddress = (GenericValue) partyAndPostalAddress.get(0);
            contactMechId = (String) contactAddress.get("contactMechId");
        }

        //更新一下订单的货运地址
        Map<String, Object> updateShipGroupShipInfoOutMap = dispatcher.runSync("updateShipGroupShipInfo", UtilMisc.toMap(
                "userLogin", userLogin, "orderId", orderId,
                "contactMechId", contactMechId, "shipmentMethod", "EXPRESS@" + "SHUNFENG_EXPRESS", "shipGroupSeqId", "00001"));

        if (!ServiceUtil.isSuccess(updateShipGroupShipInfoOutMap)) {
            return updateShipGroupShipInfoOutMap;
        }

//        if(null!=telNumber && !telNumber.trim().equals("")){
//            // 创建联系
//            Map<String, Object> inputTelecom = UtilMisc.toMap();
//            inputTelecom.put("partyId", partyId);
//            inputTelecom.put("contactNumber", telNumber);
//            inputTelecom.put("contactMechTypeId", "TELECOM_NUMBER");
//            inputTelecom.put("contactMechPurposeTypeId", "PHONE_MOBILE");
//            inputTelecom.put("userLogin", admin);
//            Map<String, Object> createTelecom = null;
//            try {
//                createTelecom = dispatcher.runSync("createPartyTelecomNumber", inputTelecom);
//            } catch (GenericServiceException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }

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

        //订单id
        String orderId = (String) context.get("orderId");

        resultMap.put("orderId", orderId);

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

        List<GenericValue> partyAndPostalAddress = EntityQuery.use(delegator).from("PartyAndPostalAddress").where("address1", address1 + " " + address2).queryList();
        if (null == partyAndPostalAddress || partyAndPostalAddress.size() == 0) {
            // 货运目的地址
            String contactMechPurposeTypeId = "SHIPPING_LOCATION";
            Map<String, Object> createPartyPostalAddressOutMap = dispatcher.runSync("createPartyPostalAddress",
                    UtilMisc.toMap("userLogin", admin, "toName", firstName, "partyId", partyId, "countryGeoId", PeConstant.DEFAULT_GEO_COUNTRY, "city", PeConstant.DEFAULT_CITY_GEO_COUNTRY, "address1", address1 + " " + address2, "postalCode", PeConstant.DEFAULT_POST_CODE,
                            "contactMechPurposeTypeId", contactMechPurposeTypeId));
            String contactMechId = (String) createPartyPostalAddressOutMap.get("contactMechId");
            if (!ServiceUtil.isSuccess(createPartyPostalAddressOutMap)) {
                return createPartyPostalAddressOutMap;
            }
        }
//        //寄送账单地址
//        dispatcher.runAsync("createPartyContactMechPurpose",
//                UtilMisc.toMap("userLogin", admin, "contactMechId", contactMechId,
//                        "contactMechPurposeTypeId", "BILLING_LOCATION"));
//
//        //通信地址
//        dispatcher.runAsync("createPartyContactMechPurpose",
//                UtilMisc.toMap("userLogin", admin, "contactMechId", contactMechId,
//                        "contactMechPurposeTypeId", "GENERAL_LOCATION"));
//
//        //首选地址
//        dispatcher.runAsync("createPartyContactMechPurpose",
//                UtilMisc.toMap("userLogin", admin, "contactMechId", contactMechId,
//                        "contactMechPurposeTypeId", "PRIMARY_LOCATION"));


        //更新一下订单的货运地址
        //updateShipGroupShipInfo
//        Map<String, Object> updateShipGroupShipInfoOutMap = dispatcher.runSync("updateShipGroupShipInfo", UtilMisc.toMap(
//                "userLogin", userLogin, "orderId", orderId,
//                "contactMechId", contactMechId, "shipmentMethod", "EXPRESS@" + "SHUNFENG_EXPRESS", "shipGroupSeqId", "00001"));
//
//        if (!ServiceUtil.isSuccess(updateShipGroupShipInfoOutMap)) {
//            return updateShipGroupShipInfoOutMap;
//        }

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

        Debug.logInfo("* markOrOutMarkProduct = productId=" + productId, module);
        Debug.logInfo("* userLogin = userLogin=" + userLogin, module);

        String markIt = (String) context.get("markIt");

        String partyId = (String) userLogin.get("partyId");

        Long placingCustCount = EntityQuery.use(delegator).from("ProductRole").where("roleTypeId", "PLACING_CUSTOMER", "productId", productId, "partyId", partyId).queryCount();

        //  if (markIt.equals("true")) {
        Debug.logInfo("* placingCustCount =" + placingCustCount, module);
        if (placingCustCount <= 0) {
            Debug.logInfo("* placingCustCount <= 0 =" + (placingCustCount <= 0), module);
            Long custCount = EntityQuery.use(delegator).from("ProductRole").where("roleTypeId", "CUSTOMER", "productId", productId).queryCount();
            //此处如果对这个产品已经有客户角色,不再增加潜在客户角色 ,
            // 2018-3-1 不用如此痛苦.
            //   if (custCount == null || custCount <= 0) {}
            dispatcher.runSync("addPartyToProduct", UtilMisc.toMap("userLogin", admin, "partyId", partyId, "productId", productId, "roleTypeId", "PLACING_CUSTOMER"));

        } else {
            if (placingCustCount > 0) {
                Debug.logInfo("* placingCustCount " + (placingCustCount), module);
                GenericValue partyMarkRole = EntityQuery.use(delegator).from("ProductRole").where("partyId", partyId, "productId", productId, "roleTypeId", "PLACING_CUSTOMER").queryFirst();
                dispatcher.runSync("removePartyFromProduct", UtilMisc.toMap("userLogin", admin, "partyId", partyId, "productId", productId, "roleTypeId", "PLACING_CUSTOMER", "fromDate", partyMarkRole.get("fromDate")));
            }
        }
        //     } else {

        //       }


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
     * 点赞了
     *
     * @param request
     * @param response
     * @return
     * @throws GenericServiceException
     * @throws GenericEntityException
     */
    public static String likeResource(HttpServletRequest request, HttpServletResponse response) throws GenericServiceException, GenericEntityException {

        // Servlet Head

        Locale locale = UtilHttp.getLocale(request);

        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");

        Delegator delegator = (Delegator) request.getAttribute("delegator");

        HttpSession session = request.getSession();

        String productId = (String) request.getParameter("productId");

        String unioId = (String) request.getParameter("unioId");

        String text = (String) request.getParameter("text");

        Debug.logInfo("*likeResource unioId = " + unioId, module);
        Debug.logInfo("*likeResource productId = " + productId, module);


        GenericValue partyIdentification = EntityQuery.use(delegator).from("PartyIdentification").where("idValue", unioId, "partyIdentificationTypeId", "WX_UNIO_ID").queryFirst();

        String partyId = "NA";

        if (UtilValidate.isNotEmpty(partyIdentification)) {
            partyId = (String) partyIdentification.get("partyId");
        }

        GenericValue partyUserLogin = EntityQuery.use(delegator).from("UserLogin").where("partyId", partyId).queryFirst();


        Map<String, Object> markOutMap = dispatcher.runSync("markOrOutMarkProduct", UtilMisc.toMap("productId", productId, "userLogin", partyUserLogin));


        Debug.logInfo("*likeResource markOutMap = " + markOutMap, module);

        return "success";
    }

    /**
     * 对产品的吐槽
     *
     * @param request
     * @param response
     * @return
     * @throws GenericServiceException
     * @throws GenericEntityException
     */
    public static String tuCaoProduct(HttpServletRequest request, HttpServletResponse response) throws GenericServiceException, GenericEntityException {

        // Servlet Head

        Locale locale = UtilHttp.getLocale(request);

        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");

        Delegator delegator = (Delegator) request.getAttribute("delegator");

        HttpSession session = request.getSession();

        String productId = (String) request.getParameter("productId");

        String unioId = (String) request.getParameter("unioId");

        String text = (String) request.getParameter("text");

        GenericValue partyIdentification = EntityQuery.use(delegator).from("PartyIdentification").where("idValue", unioId, "partyIdentificationTypeId", "WX_UNIO_ID").queryFirst();

        String partyId = "NA";

        if (UtilValidate.isNotEmpty(partyIdentification)) {
            partyId = (String) partyIdentification.get("partyId");
        }

        GenericValue partyUserLogin = EntityQuery.use(delegator).from("UserLogin").where("partyId", partyId).queryFirst();

        Map<String, Object> tuCaoResultMap = dispatcher.runSync("tuCao", UtilMisc.toMap("userLogin", partyUserLogin, "text", text, "productId", productId));

        if (ServiceUtil.isError(tuCaoResultMap)) {
            return "error";
        }

        return "success";
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

        String text = (String) request.getParameter("text");

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
        pushWeChatMessageInfoMap.put("partyIdFrom", partyIdFrom);
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

        Map<String, Object> doJpushMap = new HashMap<String, Object>();

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
            doJpushMap.put("userLogin", admin);
            doJpushMap.put("productId", objectId);
            doJpushMap.put("badge", badege_str);
            doJpushMap.put("message", "message");
            doJpushMap.put("content", text);
            doJpushMap.put("regId", jpushId);
            doJpushMap.put("deviceType", partyIdentificationTypeId);
            doJpushMap.put("sendType", "");
            doJpushMap.put("objectId", partyIdFrom);

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
        if (null != partyIdentifications && partyIdentifications.size() > 0) {
            try {
                System.out.println("*push notif Or Message !!!!!!!!!!!!!!!!!");
                dispatcher.runSync("pushNotifOrMessage", doJpushMap);
            } catch (GenericServiceException e1) {
                Debug.logError(e1.getMessage(), module);
                return "error";
            }
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

        //    GenericValue userLogin = (GenericValue) context.get("userLogin");
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
     *
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
        System.out.println("*messageId=" + messageId);
        GenericValue messageLog = EntityQuery.use(delegator).from("MessageLog").where("messageId", messageId).queryFirst();
        messageLog.set("message", " " + messageLog.get("message") + "");
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
    public static void createProductContentAndDataResource(Delegator delegator, LocalDispatcher dispatcher, GenericValue admin, String productId, String description, String dataInfo, int count) throws GenericServiceException {

        // Create Content
        //    String contentTypeId = "ADDITIONAL_IMAGE_" + count;
        String contentTypeId = "ADDITIONAL_OTHER";
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
     * 发布资源(产品)
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

        //当事人标识
        String partyId = (String) userLogin.get("partyId");
        //AdminRunService
        GenericValue admin = delegator.findOne("UserLogin", UtilMisc.toMap("userLoginId", "admin"), false);
        //分类Id
        String productCategoryId = (String) request.getParameter("productCategoryId");
        //价格
        String productPrice = (String) request.getParameter("productPrice");
        //产品名称
        String productName = (String) request.getParameter("productName");
        //封面图
        String defaultImageUrl = (String) request.getParameter("defaultImageUrl");
        //库存量
        String quantityTotalStr = (String) request.getParameter("quantityTotal");
        //默认库存
        BigDecimal quantityTotal = new BigDecimal("99999999");
        //产品特征数组
        String productFeature = (String) request.getParameter("productFeatures");
        //产品说明
        String description = (String) request.getParameter("description");

        if (!UtilValidate.isEmpty(quantityTotalStr)) {
            quantityTotal = new BigDecimal(quantityTotalStr);
        }

        //默认的价格是0
        BigDecimal price = BigDecimal.ZERO;
        System.out.println(">>>>>>>>>>>>>>>>>>>>>productPrice=" + productPrice);
        try {
            if (!UtilValidate.isEmpty(productPrice) && !productPrice.trim().equals("")) {
                price = new BigDecimal(productPrice);
            }
        } catch (Exception e) {
            price = BigDecimal.ZERO;
        }

        //不分梨用户过来需要先给他创建店铺和目录
        if (null == productCategoryId) {
            productCategoryId = createPersonStoreAndCatalogAndCategory(locale, admin, delegator, dispatcher, partyId);

        }


        //创建产品
        Map<String, Object> createProductInMap = new HashMap<String, Object>();
        createProductInMap.put("userLogin", admin);
        long ctm = System.currentTimeMillis();
        createProductInMap.put("internalName", partyId + "_" + ctm);
        createProductInMap.put("productName", productName);
        createProductInMap.put("productTypeId", PeConstant.PRODUCT_TYPE_ID);
        createProductInMap.put("description", description);

        //产品ID
        String productId = "";

        if (!UtilValidate.isEmpty(defaultImageUrl)) {
            createProductInMap.put("smallImageUrl", defaultImageUrl);
            createProductInMap.put("detailImageUrl", defaultImageUrl);
        } else {

            try {
                //上传图片到Oss
                ServletFileUpload dfu = new ServletFileUpload(new DiskFileItemFactory(10240, null));
                List<FileItem> items = dfu.parseRequest(request);
                int itemSize = 0;
                int index = 0;
                if (null != items) {
                    itemSize = items.size();
                    //循环上传请求中的所有文件
                    for (FileItem item : items) {
                        InputStream in = item.getInputStream();
                        String fileName = item.getName();
                        if (!UtilValidate.isEmpty(fileName) && index == 0) {
                            long tm = System.currentTimeMillis();
                            String pictureKey = OSSUnit.uploadObject2OSS(in, item.getName(), OSSUnit.getOSSClient(), null,
                                    "personerp", PeConstant.PRODUCT_OSS_PATH, tm);
                            if (pictureKey != null && !pictureKey.equals("")) {
                                createProductInMap.put("smallImageUrl", PeConstant.OSS_PATH + PeConstant.PRODUCT_OSS_PATH + tm + fileName.substring(fileName.indexOf(".")) + "?x-oss-process=image/resize,m_pad,h_50,w_50");
                                createProductInMap.put("detailImageUrl", PeConstant.OSS_PATH + PeConstant.PRODUCT_OSS_PATH + tm + fileName.substring(fileName.indexOf(".")));
                                //调用服务创建产品(资源)
                                Map<String, Object> createProductOutMap = dispatcher.runSync("createProduct", createProductInMap);

                                if (!ServiceUtil.isSuccess(createProductOutMap)) {
                                    Debug.logError("*Mother Fuck Create Product OutMap Error:" + createProductOutMap, module);
                                    //   return createProductOutMap;
                                    return "error";
                                }
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


        //创建产品价格(默认所有变形产品都和虚拟产品价格一致)
        Map<String, Object> createProductPriceInMap = new HashMap<String, Object>();
        createProductPriceInMap.put("userLogin", admin);
        createProductPriceInMap.put("productId", productId);
        createProductPriceInMap.put("currencyUomId", PeConstant.DEFAULT_CURRENCY_UOM_ID);
        createProductPriceInMap.put("price", price);
        createProductPriceInMap.put("productPricePurposeId", PeConstant.PRODUCT_PRICE_DEFAULT_PURPOSE);
        createProductPriceInMap.put("productPriceTypeId", PeConstant.PRODUCT_PRICE_DEFAULT_TYPE_ID);
        createProductPriceInMap.put("productStoreGroupId", PeConstant.NA);
        Map<String, Object> createProductPriceServiceResultMap = dispatcher.runSync("createProductPrice", createProductPriceInMap);
        if (!ServiceUtil.isSuccess(createProductPriceServiceResultMap)) {
            Debug.logError("*Mother Fuck Create Product Price Error:" + createProductPriceServiceResultMap, module);
            // return createProductPriceServiceResultMap;
            return "error";
        }

        //产品关联分类
        Map<String, Object> addProductToCategoryInMap = new HashMap<String, Object>();
        addProductToCategoryInMap.put("userLogin", admin);
        addProductToCategoryInMap.put("productId", productId);
        addProductToCategoryInMap.put("productCategoryId", productCategoryId);
        Map<String, Object> addProductToCategoryServiceResultMap = dispatcher.runSync("addProductToCategory", addProductToCategoryInMap);
        if (!ServiceUtil.isSuccess(addProductToCategoryServiceResultMap)) {
            Debug.logError("*Mother Fuck added Product To Category Error:" + addProductToCategoryServiceResultMap, module);
            // return addProductToCategoryServiceResultMap;
            return "error";
        }

        //找到仓库
        GenericValue facility = EntityQuery.use(delegator).from("Facility").where("ownerPartyId", partyId).queryFirst();

        //为产品创建库存量
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
        if (!ServiceUtil.isSuccess(receiveInventoryProductOut)) {
            Debug.logError("*Mother Fuck Receive Inventory Product Error:" + receiveInventoryProductOut, module);
            //return receiveInventoryProductOut;
            return "error";
        }

        //  dispatcher.runSync("createProductAttribute",UtilMisc.toMap("userLogin",admin,"productId",productId,"attrName","quantityAccepted","attrValue",quantityTotal+""));


        //给产品增加用户角色
        Map<String, Object> addProductRoleServiceResoutMap = dispatcher.runSync("addProductRole", UtilMisc.toMap("userLogin", admin, "productId", productId, "partyId", partyId, "roleTypeId", "ADMIN"));
        if (!ServiceUtil.isSuccess(addProductRoleServiceResoutMap)) {
            Debug.logError("*Mother Fuck Added ProductRoleService  Error:" + addProductRoleServiceResoutMap, module);
            //  return addProductRoleServiceResoutMap;
            return "error";
        }


        //是一个虚拟产品而且要变形
        if (productFeature != null) {
            Debug.logInfo("*[" + productId + "] Is a Virtual Product ! -------------------------------------------------------------- productFeature = ---------- " + productFeature, module);
            //更新产品为虚拟产品
            Map<String, Object> updateProductServiceResultMap = dispatcher.runSync("updateProduct", UtilMisc.toMap("userLogin", admin, "productId", productId, "isVirtual", "Y", "virtualVariantMethodEnum", "VV_FEATURETREE"));
            if (!ServiceUtil.isSuccess(updateProductServiceResultMap)) {
                Debug.logError("*Mother Fuck updateProductService  Error:" + updateProductServiceResultMap, module);
                //  return updateProductServiceResultMap;
                return "error";
            }
            //给产品创建特征组
            String productFeatureCategoryId = "";
            Map<String, Object> createProductFeatureCategoryResultMap = dispatcher.runSync("createProductFeatureCategory", UtilMisc.toMap("userLogin", admin, "description", productId + "_" + productName + "的特征组"));
            if (!ServiceUtil.isSuccess(createProductFeatureCategoryResultMap)) {
                Debug.logError("*Mother Fuck createProductFeatureCategory  Error:" + createProductFeatureCategoryResultMap, module);
                //  return updateProductServiceResultMap;
                return "error";
            } else {
                productFeatureCategoryId = (String) createProductFeatureCategoryResultMap.get("productFeatureCategoryId");
            }

            Debug.logInfo("*CreateProductFeatureCategory |productFeatureCategoryId=" + productFeatureCategoryId, module);


            JSONArray myJsonArray = JSONArray.fromObject(productFeature);

            //用于存储变形产品的组合,理论上有多少行就有多少个组合出的变形产品
            List<Map<String, Object>> quickAddVariantList = new ArrayList<Map<String, Object>>();

            List<String> quickAddVariantStrList = new ArrayList<String>();

            //创建特征or选择数据中的特征

            for (int index = 0; index < myJsonArray.size(); index++) {

                JSONArray myJsonArray2 = (JSONArray) myJsonArray.get(index);

                net.sf.json.JSONObject feature = (net.sf.json.JSONObject) myJsonArray2.get(0);

                String optionTitle = (String) feature.get("optionTitle");


                String productFeatureTypeId = (String) feature.get("productFeatureTypeId");


                Debug.logInfo("* >>> productFeatureTypeId =" + productFeatureTypeId, module);

                //如果 productFeatureTypeId 是空 ,说明不是从数据库选择出来的已有类型。需要创建!
                if (UtilValidate.isEmpty(productFeatureTypeId)) {
                    //创建新的特征类型
                    Map<String, Object> createFeatureTypeResultMap = dispatcher.runSync("createProductFeatureType", UtilMisc.toMap("userLogin", admin, "description", optionTitle, "productFeatureTypeId", delegator.getNextSeqId("ProductFeatureType")));
                    if (!ServiceUtil.isSuccess(createFeatureTypeResultMap)) {
                        Debug.logError("*Mother Fuck createProductFeatureType  Error:" + createFeatureTypeResultMap, module);
                        return "error";
                    }
                    productFeatureTypeId = (String) createFeatureTypeResultMap.get("productFeatureTypeId");
                    //把这个特征类型给到当前用户的偏好设置
                    Map<String, Object> setUserPreferenceResultMap = dispatcher.runSync("setUserPreference", UtilMisc.toMap("userLogin", admin, "userPrefLoginId", userLogin.get("userLoginId"), "userPrefTypeId", productFeatureTypeId, "userPrefGroupTypeId", "PRODUCT_FEATURES", "userPrefValue", optionTitle));
                    if (!ServiceUtil.isSuccess(setUserPreferenceResultMap)) {
                        Debug.logError("*Mother Fuck setUserPreferenceResultMap  Error:" + setUserPreferenceResultMap, module);
                        return "error";
                    }
                }

                JSONArray optionList = (JSONArray) feature.get("optionList");

                if (optionList.size() > 0) {

                    Debug.logInfo("* >>>optionListIndex>>> =" + optionList, module);
                    for (int optionListIndex = 0; optionListIndex < optionList.size(); optionListIndex++) {
                        String quickAddVariantStr = "";
                        //Create Product Feature Attribute
                        net.sf.json.JSONObject optionList2 = (net.sf.json.JSONObject) optionList.get(optionListIndex);
                        String optionValue = (String) optionList2.get("value");
                        //创建特征
                        Map<String, Object> createProductFetureMap = dispatcher.runSync("createProductFeature", UtilMisc.toMap("userLogin", admin, "productFeatureCategoryId", productFeatureCategoryId, "productFeatureTypeId", productFeatureTypeId, "description", optionValue));

                        String featureId = (String) createProductFetureMap.get("productFeatureId");
                        Debug.logInfo("*featureId:" + featureId, module);
                        //建立产品与特征的关联
                        Map<String, Object> applyFeatureToProductMap = dispatcher.runSync("applyFeatureToProduct", UtilMisc.toMap("userLogin", admin,
                                "productFeatureId", featureId, "productId", productId, "productFeatureApplTypeId", "SELECTABLE_FEATURE"));
                        if (!ServiceUtil.isSuccess(applyFeatureToProductMap)) {
                            Debug.logError("*Mother Fuck applyFeatureToProduct  Error:" + applyFeatureToProductMap, module);
                            return "error";
                        }
                        quickAddVariantStr = optionTitle + "|" + optionValue + "," + featureId;
                        quickAddVariantStrList.add(quickAddVariantStr);
//                        Map<String,Object> quickAddMap = new HashMap<String, Object>();
//                        quickAddMap.put(optionValue,featureId);
//                        quickAddVariantList.add(quickAddMap);

                    }
                }
            }
            Debug.logInfo("*quickAddVariantStrList:" + quickAddVariantStrList, module);
            quickAddVariantMethod(quickAddVariantStrList, dispatcher, userLogin, productId);
        }


        request.setAttribute("productId", productId);

        return "success";

    }

    /**
     * 快速创建变形产品
     *
     * @param quickAddVariantStrList
     * @param dispatcher
     * @param userLogin
     * @param productId
     * @throws GenericServiceException
     */
    private static void quickAddVariantMethod(List<String> quickAddVariantStrList, LocalDispatcher dispatcher, GenericValue userLogin, String productId) throws GenericServiceException {

        //:[颜色|红,10070, 颜色|黄,10071, 尺寸|180,10072, 尺寸|170,10073]
        //这是一个防止重复添加的Map
        Map<String, String> noReAddMap = new HashMap<String, String>();
        Debug.logInfo("*FUCK LOGIC START ====================================================================================>:", module);
        Long sequenceNum = new Long(10);
        for (String rowStr : quickAddVariantStrList) {
            String nowFeatureName = rowStr.substring(0, rowStr.indexOf("|"));
            String nowFeatureValue = rowStr.substring(rowStr.indexOf("|") + 1, rowStr.indexOf(","));
            String nowFeatureTypeId = rowStr.substring(rowStr.indexOf(",") + 1, rowStr.length());
            Debug.logInfo("*nowFeatureName:" + nowFeatureName, module);
            Debug.logInfo("*nowFeatureValue:" + nowFeatureValue, module);
            Debug.logInfo("*nowFeatureTypeId:" + nowFeatureTypeId, module);

            for (String innerRowStr : quickAddVariantStrList) {
                String innerNowFeatureName = innerRowStr.substring(0, innerRowStr.indexOf("|"));
                String innerNowFeatureValue = innerRowStr.substring(innerRowStr.indexOf("|") + 1, innerRowStr.indexOf(","));
                String innerNowFeatureTypeId = innerRowStr.substring(innerRowStr.indexOf(",") + 1, innerRowStr.length());
                Debug.logInfo("*innerNowFeatureName:" + innerNowFeatureName, module);
                Debug.logInfo("*innerNowFeatureValue:" + innerNowFeatureValue, module);
                Debug.logInfo("*innerNowFeatureTypeId:" + innerNowFeatureTypeId, module);
                //是一个类型则跳过,只创建不同类型的组合产品
                if (nowFeatureName.equals(innerNowFeatureName)) {
                    continue;
                } else {
                    //创建变形产品
                    String isExsitsStr = nowFeatureTypeId + innerNowFeatureTypeId;
                    Debug.logInfo("*isExsitsStr:" + isExsitsStr, module);
                    Debug.logInfo("*noReAddMap:" + noReAddMap, module);
                    Debug.logInfo("*noReAddMap.containsKey(isExsitsStr):" + noReAddMap.containsKey(isExsitsStr), module);
                    //这个组合是否已经产生变形产品?
                    if (noReAddMap.containsKey(isExsitsStr) || noReAddMap.containsKey(innerNowFeatureTypeId + nowFeatureTypeId)) {
                        continue;
                    } else {
                        noReAddMap.put(isExsitsStr, "");
                        Map<String, Object> quickAddVariantMap = dispatcher.runSync("quickAddVariant", UtilMisc.toMap("userLogin", userLogin, "productId", productId, "productFeatureIds", "|" + nowFeatureTypeId + "|" + innerNowFeatureTypeId, "productVariantId", productId + "_" + sequenceNum, "sequenceNum", sequenceNum));
                        Debug.logInfo("*quickAddVariantMap:" + UtilMisc.toMap("userLogin", userLogin, "productId", productId, "productFeatureIds", "|" + nowFeatureTypeId + "|" + innerNowFeatureTypeId, "productVariantId", productId + "_" + sequenceNum, "sequenceNum", sequenceNum), module);
                        if (!ServiceUtil.isSuccess(quickAddVariantMap)) {
                            Debug.logError("*Mother Fuck quick Add Variant Error:" + quickAddVariantMap, module);
                        }
                        sequenceNum += 10;
                    }
                }

            }
        }
        Debug.logInfo("*FUCK LOGIC END ====================================================================================>:", module);


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
        GenericValue userLogin = (GenericValue) context.get("userLogin");

        String partyRelationshipTypeId = (String) context.get("partyRelationshipTypeId");

        Map<String, Object> serviceResultMap = null;
        //指定了创建什么类型
        if (null != partyRelationshipTypeId) {
            partyIdFrom = (String) userLogin.get("partyId");
            GenericValue queryIsExsitsRelation = EntityQuery.use(delegator).from("PartyRelationshipAndType").where(
                    UtilMisc.toMap("parentTypeId", "INTERPERSONAL", "partyIdTo", partyIdTo, "partyIdFrom",
                            partyIdFrom, "partyRelationshipTypeId", partyRelationshipTypeId)).queryFirst();


            //说明这个关系已经存在 不必再次添加了
            if (queryIsExsitsRelation == null) {
                Map<String, Object> createPartyRelationshipInMap = new HashMap<String, Object>();
                createPartyRelationshipInMap.put("roleTypeIdFrom", "ADMIN");
                createPartyRelationshipInMap.put("roleTypeIdTo", "ADMIN");
//                createPartyRelationshipInMap.put("userLogin", admin);
                createPartyRelationshipInMap.put("partyIdFrom", partyIdFrom);
                createPartyRelationshipInMap.put("partyIdTo", partyIdTo);
                createPartyRelationshipInMap.put("partyRelationshipTypeId", partyRelationshipTypeId);
                createPartyRelationshipInMap.put("fromDate", org.apache.ofbiz.base.util.UtilDateTime.nowTimestamp());

//                Map<String, Object> createPartyRelationshipOutMap = dispatcher.runSync("createPartyRelationship", createPartyRelationshipInMap);
//
//                if (ServiceUtil.isError(createPartyRelationshipOutMap)) {
//                    return createPartyRelationshipOutMap;
//                }

                GenericValue partyRelationship = delegator.makeValue("PartyRelationship", createPartyRelationshipInMap);

                partyRelationship.create();

            }

        } else {
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

        if (!partyIdFrom.equals(partyIdTo)) {

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
    private static Map<String, Object> createRelationCONTACT(Delegator delegator, LocalDispatcher dispatcher, GenericValue admin, String partyIdTo, String partyIdFrom) throws GenericServiceException, GenericEntityException {


        if (!partyIdFrom.equals(partyIdTo)) {


            String partyRelationshipTypeId = "";
            // Create Supplier Relation
            partyRelationshipTypeId = PeConstant.CONTACT;
            GenericValue isExsitsRela = EntityQuery.use(delegator).from("PartyRelationship").where("partyIdFrom", partyIdTo, "partyIdTo", partyIdFrom, "partyRelationshipTypeId", partyRelationshipTypeId).queryFirst();

            Map<String, Object> createPartyRelationshipInMap = new HashMap<String, Object>();
            createPartyRelationshipInMap.put("userLogin", admin);
            createPartyRelationshipInMap.put("partyIdFrom", partyIdTo);
            createPartyRelationshipInMap.put("partyIdTo", partyIdFrom);
            createPartyRelationshipInMap.put("partyRelationshipTypeId", partyRelationshipTypeId);
            Map<String, Object> createPartyRelationshipOutMap = null;
            if (null == isExsitsRela) {
                createPartyRelationshipOutMap = dispatcher.runSync("createPartyRelationship", createPartyRelationshipInMap);

                if (ServiceUtil.isError(createPartyRelationshipOutMap)) {
                    return createPartyRelationshipOutMap;
                }
            }
            partyRelationshipTypeId = PeConstant.CONTACT;
            createPartyRelationshipInMap = new HashMap<String, Object>();

            isExsitsRela = EntityQuery.use(delegator).from("PartyRelationship").where("partyIdFrom", partyIdFrom, "partyIdTo", partyIdTo, "partyRelationshipTypeId", partyRelationshipTypeId).queryFirst();

            createPartyRelationshipInMap.put("userLogin", admin);
            createPartyRelationshipInMap.put("partyIdFrom", partyIdFrom);
            createPartyRelationshipInMap.put("partyIdTo", partyIdTo);
            createPartyRelationshipInMap.put("partyRelationshipTypeId", partyRelationshipTypeId);
            if (isExsitsRela == null) {
                createPartyRelationshipOutMap = dispatcher.runSync("createPartyRelationship", createPartyRelationshipInMap);
            }
            if (ServiceUtil.isError(createPartyRelationshipOutMap)) {
                return createPartyRelationshipOutMap;
            }

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
     * peSentOrder
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> peSentOrder(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();

        // Sudo
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String code = (String) context.get("code");
        String orderId = (String) context.get("orderId");
        String partyId = (String) context.get("partyId");
        //小程序确认收款
        if (null != partyId) {
            userLogin = EntityQuery.use(delegator).from("UserLogin").where("partyId", partyId).queryFirst();
        } else {
            partyId = (String) userLogin.get("partyId");
        }
        String carrierCode = (String) context.get("carrierCode");
        String name = (String) context.get("name");
        String shipmentMethodId = "";
        String contactMechId = "";
        String sinceTheSend = (String) context.get("sinceTheSend");

        GenericValue orderSales = EntityQuery.use(delegator).from("OrderRole").where("orderId", orderId, "roleTypeId", "BILL_FROM_VENDOR").queryFirst();

        GenericValue facility = EntityQuery.use(delegator).from("Facility").where("ownerPartyId", orderSales.get("partyId")).queryFirst();

        Map<String, Object> quickShipmentOut = dispatcher.runSync("quickShipEntireOrder", UtilMisc.toMap(
                "userLogin", admin,
                "orderId", orderId,
                "originFacilityId", facility.get("facilityId")));

        if (!ServiceUtil.isSuccess(quickShipmentOut)) {
            return quickShipmentOut;
        }

        //更新货运信息。不管是不是卖家自配送。承运人都是卖家。只不过如果是快递发货，会增加tc

        //Update ShipmentRouteSegment
        // 暂不适用服务更新 dispatcher.runSync("updateShipmentRouteSegment","");

        GenericValue orderShipment = EntityQuery.use(delegator).from("OrderShipment").where("orderId", orderId).queryFirst();
        String shipmentId = (String) orderShipment.get("shipmentId");
        GenericValue shipmentRouteSegment = EntityQuery.use(delegator).from("ShipmentRouteSegment").where("shipmentId", shipmentId).queryFirst();
        shipmentRouteSegment.set("carrierPartyId", orderSales.get("partyId"));
        shipmentRouteSegment.store();
        //Update OrderItemShipGroup
        // 暂不适用服务更新 dispatcher.runSync("updateOrderItemShipGroup","");
        GenericValue orderItemShipGroup = EntityQuery.use(delegator).from("OrderItemShipGroup").where("orderId", orderId).queryFirst();
        orderItemShipGroup.set("carrierPartyId", orderSales.get("partyId"));
        if (sinceTheSend != null && sinceTheSend.equals("1")) {
            orderItemShipGroup.set("trackingNumber", code);
        }
        orderItemShipGroup.store();


        GenericValue orderCust = EntityQuery.use(delegator).from("OrderRole").where("orderId", orderId, "roleTypeId", "SHIP_TO_CUSTOMER").queryFirst();
        GenericValue partyIdentification = EntityQuery.use(delegator).from("PartyIdentification").where("partyId", orderCust.get("partyId"), "partyIdentificationTypeId", "WX_GZ_OPEN_ID").queryFirst();
        GenericValue orderItem = EntityQuery.use(delegator).from("OrderItem").where("orderId", orderId).queryFirst();
        String productId = (String) orderItem.get("productId");
        GenericValue product = EntityQuery.use(delegator).from("Product").where("productId", productId).queryFirst();

        //推送买家
        if (null != partyIdentification) {

            Map<String, Object> pushWeChatMessageInfoMap = new HashMap<String, Object>();


            System.out.println("*PUSH WE CHAT GONG ZHONG PLATFORM !!!!!!!!!!!!!!!!!!!!!!!");

            pushWeChatMessageInfoMap.put("payToPartyId", partyId);

            Date date = new Date();

            SimpleDateFormat formatter;

            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String pushDate = "" + formatter.format(date);

            pushWeChatMessageInfoMap.put("date", pushDate);


            String openId = (String) partyIdentification.get("idValue");

            pushWeChatMessageInfoMap.put("openId", openId);

            pushWeChatMessageInfoMap.put("orderId", orderId);
            if (null != sinceTheSend && sinceTheSend.equals("1")) {
                //自配送
                pushWeChatMessageInfoMap.put("messageInfo", "您购买的(" + product.get("productName") + ")我发货了," + "由我亲自给您配送!");

            } else {
                pushWeChatMessageInfoMap.put("messageInfo", "您购买的(" + product.get("productName") + ")我已发货" + ",物流公司是" + name + "。物流单号:" + (code == null ? "暂无详细" : code));
            }


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
            pushWeChatMessageInfoMap.put("jumpUrl", "http://www.yo-pe.com:3400/WebManager/control/myOrderDetail?orderId=" + orderId + "&tarjeta=" + signer.sign(claims));
            //推微信订单状态
            dispatcher.runSync("pushOrderStatusInfo", pushWeChatMessageInfoMap);
        }


        return resultMap;
    }


    /**
     * 资源下单
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> placeResourceOrder(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = (String) userLogin.get("partyId");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();

        // Sudo
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));
        // StoreId
        String productStoreId = (String) context.get("productStoreId");
        // 销售代表
        String salesRepId = (String) context.get("salesRepId");
        // Amount
        String amount_str = (String) context.get("amount");
        // PayTo
        String payToPartyId = (String) context.get("payToPartyId");
        // ProductID
        String productId = (String) context.get("productId");
        // ProductPrice
        String price = (String) context.get("price");
        // CatalogId
        String prodCatalogId = (String) context.get("prodCatalogId");
        // 订单备注
        String orderReMark = (String) context.get("orderReMark");

        String salesRepPartyId = null;

        if(salesRepId==null || UtilValidate.isEmpty(salesRepId)){
            salesRepId="ZUCZUG";
        }

        salesRepPartyId = salesRepId;
//        if(null!=salesRepId){
//            GenericValue partyIdentification = EntityQuery.use(delegator).from("PartyIdentification").where("idValue", salesRepId, "partyIdentificationTypeId", "WX_MINIPRO_OPEN_ID").queryFirst();
//            salesRepPartyId = (String) partyIdentification.get("partyId");
//        }

        Debug.logInfo("*PlaceResourceOrder|productStoreId=" + productStoreId, module);
        Debug.logInfo("*PlaceResourceOrder|amount_str=" + amount_str, module);
        Debug.logInfo("*PlaceResourceOrder|payToPartyId=" + payToPartyId, module);
        Debug.logInfo("*PlaceResourceOrder|productId=" + productId, module);
        Debug.logInfo("*PlaceResourceOrder|prodCatalogId=" + prodCatalogId, module);


        BigDecimal subTotal = BigDecimal.ZERO;
        BigDecimal grandTotal = BigDecimal.ZERO;
        BigDecimal amount = BigDecimal.ONE;


        if (!UtilValidate.isEmpty(amount_str)) {
            amount = new BigDecimal(amount_str);
        }


        if (!UtilValidate.isEmpty(price)) {
            grandTotal = subTotal = new BigDecimal(price);
        }


        GenericValue facility = EntityQuery.use(delegator).from("Facility").where("ownerPartyId", payToPartyId).queryFirst();
        GenericValue category = EntityQuery.use(delegator).from("ProductCategoryAndMember").where("productId", productId).queryFirst();
        Debug.logInfo("*placeResourceOrder ownerPartyID = " + payToPartyId, module);
        String originFacilityId = (String) facility.get("facilityId");

        Map<String, Object> doCreateOrderIn = new HashMap<String, Object>();
        doCreateOrderIn.put("userLogin", admin);
        doCreateOrderIn.put("productId", productId);
        doCreateOrderIn.put("salesRepPartyId", salesRepPartyId);
        doCreateOrderIn.put("productCategoryId", category.get("productCategoryId"));
        doCreateOrderIn.put("prodCatalogId", prodCatalogId);
        doCreateOrderIn.put("productStoreId", productStoreId);
        doCreateOrderIn.put("partyId", partyId);
        doCreateOrderIn.put("billFromVendorPartyId", payToPartyId);
        doCreateOrderIn.put("originFacilityId", originFacilityId);
        doCreateOrderIn.put("quantity", amount);
        doCreateOrderIn.put("grandTotal", grandTotal);
        Map<String, Object> doCreateOrderOut = dispatcher.runSync("createPeOrder", doCreateOrderIn);

        if (!ServiceUtil.isSuccess(doCreateOrderOut)) {
            return doCreateOrderOut;
        }

        String orderId = (String) doCreateOrderOut.get("orderId");

        // 如果有备注
        if (!UtilValidate.isEmpty(orderReMark)) {
            Map<String, Object> createOrderNoteOut = dispatcher.runSync("createOrderNote", UtilMisc.toMap("userLogin", admin, "orderId", orderId, "noteName", "买家备注", "note", orderReMark, "internalNote", "N"));
            if (!ServiceUtil.isSuccess(createOrderNoteOut)) {
                return createOrderNoteOut;
            }
        }


        //给订单项创建
//        Map<String, Object> createOrderItemShipGroupInMap = new HashMap<String, Object>();
//        createOrderItemShipGroupInMap.put("userLogin", userLogin);
//        createOrderItemShipGroupInMap.put("orderId", orderId);
//        createOrderItemShipGroupInMap.put("facilityId", (String) facility.get("facilityId"));
//        createOrderItemShipGroupInMap.put("carrierPartyId", "SHUNFENG_EXPRESS");
//        createOrderItemShipGroupInMap.put("shipmentMethodTypeId", "EXPRESS");
//        Map<String, Object> createOrderItemShipGroupOut = dispatcher.runSync("createOrderItemShipGroup", createOrderItemShipGroupInMap);
//        if (ServiceUtil.isError(createOrderItemShipGroupOut)) {
//            return createOrderItemShipGroupOut;
//        }


        GenericValue teleContact = EntityQuery.use(delegator).from("TelecomNumberAndPartyView").where("partyId", payToPartyId).queryFirst();

        if (null != teleContact) {
            String contactNumber = (String) teleContact.get("contactNumber");
            resultMap.put("contactTel", contactNumber);
        }


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


        //推送给微信用户(买家)

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

            pushWeChatMessageInfoMap.put("jumpUrl", "http://www.yo-pe.com:3400/WebManager/control/myOrder");

            Map<String, String> personInfoMap = queryPersonBaseInfo(delegator, payToPartyId);

            pushWeChatMessageInfoMap.put("messageInfo", personInfoMap.get("firstName") + "正在处理您的订单");
            //推微信订单状态
            dispatcher.runSync("pushOrderStatusInfo", pushWeChatMessageInfoMap);
        }
        GenericValue queryProduct = EntityQuery.use(delegator).from("Product").where("productId", productId).queryFirst();

        //推送给微信用户(卖家)

        GenericValue partySalesIdentification = EntityQuery.use(delegator).from("PartyIdentification").where("partyId", payToPartyId, "partyIdentificationTypeId", "WX_GZ_OPEN_ID").queryFirst();


        if (null != partySalesIdentification) {

            Map<String, Object> pushWeChatMessageInfoMap = new HashMap<String, Object>();

            //销售代表的
            pushWeChatMessageInfoMap.put("payToPartyId", salesRepPartyId);
//            pushWeChatMessageInfoMap.put("payToPartyId", payToPartyId);

            Date date = new Date();

            SimpleDateFormat formatter;

            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String pushDate = "" + formatter.format(date);

            pushWeChatMessageInfoMap.put("date", pushDate);


            String openId = (String) partySalesIdentification.get("idValue");

            pushWeChatMessageInfoMap.put("openId", openId);

            pushWeChatMessageInfoMap.put("orderId", orderId);


            pushWeChatMessageInfoMap.put("jumpUrl", "http://www.yo-pe.com:3400/WebManager/control/myOrder");

            Map<String, String> personInfoMap = queryPersonBaseInfo(delegator, partyId);

            pushWeChatMessageInfoMap.put("messageInfo", personInfoMap.get("firstName") + "购买了" + amount_str + "件" + queryProduct.get("productName"));
            //推微信订单状态
            dispatcher.runSync("pushOrderStatusInfo", pushWeChatMessageInfoMap);
        }

        //买家就是卖家的情况直接返回
        if (partyId.equals(payToPartyId)) {
            return resultMap;
        }

        GenericValue productRole = EntityQuery.use(delegator).from("ProductRole").where("partyId", partyId, "roleTypeId", "PLACING_CUSTOMER", "productId", productId).queryFirst();
        //如果这个客户已经是产品的意向客户,取消这个角色,并且给予 '客户'角色
        if (productRole != null && productRole.get("partyId") != null) {
            System.out.println("productRole=" + productRole);
            GenericValue partyMarkRole = EntityQuery.use(delegator).from("ProductRole").where("partyId", partyId, "productId", productId, "roleTypeId", "PLACING_CUSTOMER").queryFirst();
            dispatcher.runSync("removePartyFromProduct", UtilMisc.toMap("userLogin", admin, "partyId", partyId, "productId", productId, "roleTypeId", "PLACING_CUSTOMER", "fromDate", partyMarkRole.get("fromDate")));
        }

        //授予客户角色
        GenericValue productRoleCust = EntityQuery.use(delegator).from("ProductRole").where("partyId", partyId, "roleTypeId", PeConstant.PRODUCT_CUSTOMER, "productId", productId).queryFirst();
        if (null == productRoleCust) {
            dispatcher.runSync("addPartyToProduct", UtilMisc.toMap("userLogin", admin, "partyId", partyId, "productId", context.get("productId"), "roleTypeId", PeConstant.PRODUCT_CUSTOMER));
        }


        GenericValue sQueryProduct = EntityQuery.use(delegator).from("Product").where("productId", productId).queryFirst();


        Map<String, Object> createMessageLogMap = new HashMap<String, Object>();

        createMessageLogMap.put("partyIdFrom", partyId);

        createMessageLogMap.put("message", maiJiaName + " 购买了" + amount_str + "件(" + sQueryProduct.get("productName") + ")。");

        createMessageLogMap.put("messageId", delegator.getNextSeqId("MessageLog"));

//        createMessageLogMap.put("partyIdTo", payToPartyId);
        createMessageLogMap.put("partyIdTo", salesRepPartyId);

        createMessageLogMap.put("badge", "CHECK");

        createMessageLogMap.put("messageLogTypeId", "ORDER");

        createMessageLogMap.put("objectId", productId);


        createMessageLogMap.put("fromDate", org.apache.ofbiz.base.util.UtilDateTime.nowTimestamp());

        GenericValue msg = delegator.makeValue("MessageLog", createMessageLogMap);

        msg.create();


        //  auto approved

        Map<String, Object> changeOrderStatusOutMap = dispatcher.runSync("changeOrderStatus",
                UtilMisc.toMap("userLogin", admin,
                        "orderId", orderId,
                        "statusId", PeConstant.ORDER_APPROVED_STATUS_ID));


        // check inventory quantity
        Map<String, Object> getInventoryAvailableByFacilityMap = dispatcher.runSync("getInventoryAvailableByFacility", UtilMisc.toMap("userLogin", admin,
                "facilityId", originFacilityId, "productId", productId));
        if (!ServiceUtil.isSuccess(getInventoryAvailableByFacilityMap)) {
            return getInventoryAvailableByFacilityMap;
        }
        BigDecimal quantityOnHandTotal = (BigDecimal) getInventoryAvailableByFacilityMap.get("quantityOnHandTotal");
        BigDecimal availableToPromiseTotal = (BigDecimal) getInventoryAvailableByFacilityMap.get("availableToPromiseTotal");


        //推卖家
        if (null != partyIdentifications && partyIdentifications.size() > 0) {

            GenericValue partyIdentification = (GenericValue) partyIdentifications.get(0);
            String jpushId = (String) partyIdentification.getString("idValue");
            String partyIdentificationTypeId = (String) partyIdentification.get("partyIdentificationTypeId");
            String type = "JPUSH_IOS";
            if (partyIdentificationTypeId != null && partyIdentificationTypeId.toLowerCase().indexOf("android") > 0) {
                type = "JPUSH_ANDROID";
            }
            try {
                dispatcher.runSync("pushNotifOrMessage", UtilMisc.toMap("userLogin", admin, "productId", productId, "message", "order", "content", maiJiaName + "购买" + amount.toString() + "件(" + sQueryProduct.get("productName") + ")点我查看!", "regId", jpushId, "deviceType", partyIdentificationTypeId, "sendType", type, "objectId", orderId));
                if (availableToPromiseTotal.compareTo(BigDecimal.ZERO) == 0) {
                    // 没库存了
                    dispatcher.runSync("pushNotifOrMessage", UtilMisc.toMap("userLogin", admin, "productId", productId, "message", "order", "content", "库存清空提醒:资源(" + sQueryProduct.get("productName") + "),您承诺的库存" + quantityOnHandTotal + "件已售空。", "regId", jpushId, "deviceType", partyIdentificationTypeId, "sendType", type, "objectId", orderId));
                }
            } catch (GenericServiceException e1) {
                Debug.logError(e1.getMessage(), module);
            }

        }


        resultMap.put("partyIdFrom", partyId);
        resultMap.put("partyIdTo", payToPartyId);
        resultMap.put("relationEnum", "C2CRSS");
        resultMap.put("orderId", orderId);

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

        // 创建会员店铺及目录和分类
        createPersonStoreAndCatalogAndCategory(locale, admin, delegator, dispatcher, partyId);


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

        // 销售代表角色
        partyMarkRole = EntityQuery.use(delegator).from("PartyRole").where("partyId", partyId, "roleTypeId", "SALES_REP").queryFirst();
        if (null == partyMarkRole) {
            Map<String, Object> createPartySALES_REPRoleMap = UtilMisc.toMap("userLogin", admin, "partyId", partyId,
                    "roleTypeId", "SALES_REP");
            dispatcher.runSync("createPartyRole", createPartySALES_REPRoleMap);
        }


        // 创建当事人


        // 创建当事人税务机关
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
        //2018-03-18 卖家本身可能就是承运人...
        GenericValue partyRoleCarrier = EntityQuery.use(delegator).from("PartyRole").where("partyId", partyId, "roleTypeId", "CARRIER").queryFirst();
        if (null == partyRoleCarrier) {
            Map<String, Object> createPartyCarrierRoleMap = UtilMisc.toMap("userLogin", admin, "partyId", partyId,
                    "roleTypeId", "CARRIER");
            dispatcher.runSync("createPartyRole", createPartyCarrierRoleMap);
        }


        //授予当事人询价角色
        GenericValue partyRoleRequester = EntityQuery.use(delegator).from("PartyRole").where("partyId", partyId, "roleTypeId", "REQ_REQUESTER").queryFirst();
        if (null == partyRoleRequester) {
            Map<String, Object> createPartyRoleMap = UtilMisc.toMap("userLogin", admin, "partyId", partyId,
                    "roleTypeId", "REQ_REQUESTER");
            dispatcher.runSync("createPartyRole", createPartyRoleMap);
        }
        //授予当事人询价接受角色
        GenericValue partyRoleTaker = EntityQuery.use(delegator).from("PartyRole").where("partyId", partyId, "roleTypeId", "REQ_TAKER").queryFirst();
        if (null == partyRoleTaker) {
            Map<String, Object> createPartyRoleMap = UtilMisc.toMap("userLogin", admin, "partyId", partyId,
                    "roleTypeId", "REQ_TAKER");
            dispatcher.runSync("createPartyRole", createPartyRoleMap);
        }


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

        // 引用者角色
        GenericValue partyReferrerRole = EntityQuery.use(delegator).from("PartyRole").where("partyId", partyId, "roleTypeId", "REFERRER").queryFirst();
        if (null == partyReferrerRole) {
            Map<String, Object> createPartyReferrerRoleMap = UtilMisc.toMap("userLogin", admin, "partyId", partyId,
                    "roleTypeId", "REFERRER");
            dispatcher.runSync("createPartyRole", createPartyReferrerRoleMap);
        }
        // 收信人角色
        GenericValue partyAddresseeRole = EntityQuery.use(delegator).from("PartyRole").where("partyId", partyId, "roleTypeId", "ADDRESSEE").queryFirst();
        if (null == partyAddresseeRole) {
            Map<String, Object> partyAddresseeRoleMap = UtilMisc.toMap("userLogin", admin, "partyId", partyId,
                    "roleTypeId", "ADDRESSEE");
            dispatcher.runSync("createPartyRole", partyAddresseeRoleMap);
        }


        //内部团体角色
        GenericValue partyInternalOrganizatioRole = EntityQuery.use(delegator).from("PartyRole").where("partyId", partyId, "roleTypeId", "INTERNAL_ORGANIZATIO").queryFirst();
        if (null == partyInternalOrganizatioRole) {
            Map<String, Object> partyInternalOrganizatioRoleMap = UtilMisc.toMap("userLogin", admin, "partyId", partyId,
                    "roleTypeId", "INTERNAL_ORGANIZATIO");
            dispatcher.runSync("createPartyRole", partyInternalOrganizatioRoleMap);
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

        // 创建自己送货的货运方法
        dispatcher.runSync("createCarrierShipmentMethod", UtilMisc.toMap("userLogin", admin, "partyId", partyId, "roleTypeId", "CARRIER", "shipmentMethodTypeId", "EXPRESS"));


        //店铺关联货运方法顺丰
        Map<String, Object> createProductStoreShipMethMap = dispatcher.runSync("createProductStoreShipMeth", UtilMisc.toMap("userLogin", admin,
                "partyId", partyId,
                "productStoreId", productStoreId,
                "productStoreShipMethId", "10000",
                "roleTypeId", "CARRIER",
                "shipmentMethodTypeId", "EXPRESS",
                "partyId", "SHUNFENG_EXPRESS",
                "allowUspsAddr", "Y",
                "requireUspsAddr", "N",
                "allowCompanyAddr", "Y",
                "requireCompanyAddr", "N"
                , "includeNoChargeItems", "Y"));

        // 店铺关联货运方法，自己
        dispatcher.runSync("createProductStoreShipMeth", UtilMisc.toMap("userLogin", admin,
                "partyId", partyId,
                "productStoreId", productStoreId,
                "productStoreShipMethId", "10000",
                "roleTypeId", "CARRIER",
                "shipmentMethodTypeId", "EXPRESS",
                "partyId", partyId,
                "allowUspsAddr", "Y",
                "requireUspsAddr", "N",
                "allowCompanyAddr", "Y",
                "requireCompanyAddr", "N"
                , "includeNoChargeItems", "Y"));


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


        //createPartyAcctgPreference

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
                    "defaultCurrencyUomId", PeConstant.DEFAULT_CURRENCY_UOM_ID, "reserveInventory", "Y", "storeName", storeName, "payToPartyId", partyId, "inventoryFacilityId", inventoryFacilityId));
            if (!ServiceUtil.isSuccess(createProductStoreOutMap)) {
                return createProductStoreOutMap;
            }
            personStoreId = (String) createProductStoreOutMap.get("productStoreId");


            //createProductStorePaymentSetting

            //微信支付设置
            Map<String, Object> createProductStorePaymentSettingOutMap = dispatcher.runSync("createProductStorePaymentSetting", UtilMisc.toMap("userLogin", admin,
                    "productStoreId", personStoreId, "applyToAllProducts", "Y", "paymentMethodTypeId", "EXT_WXPAY", "paymentServiceTypeEnumId", "PRDS_PAY_AUTH", "paymentService", "genericPaymentService"));
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
