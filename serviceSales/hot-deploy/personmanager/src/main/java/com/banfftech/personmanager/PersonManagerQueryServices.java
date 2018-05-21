package main.java.com.banfftech.personmanager;

import main.java.com.banfftech.platformmanager.constant.PeConstant;
import main.java.com.banfftech.platformmanager.util.AesCbcUtil;
import main.java.com.banfftech.platformmanager.util.Base64Util;
import main.java.com.banfftech.platformmanager.util.UtilTools;
import org.apache.ofbiz.entity.GenericEntity;
import org.apache.ofbiz.base.util.Debug;

import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.entity.util.EntityUtil;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.util.EntityUtilProperties;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.base.util.collections.PagedList;
import com.auth0.jwt.JWTExpiredException;
import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;
import net.sf.json.JSONObject;
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
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.eclipse.birt.chart.extension.datafeed.GanttEntry;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.geom.GeneralPath;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.Timestamp;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import main.java.com.banfftech.platformmanager.util.GZIP;
import sun.net.www.content.text.Generic;

import static main.java.com.banfftech.platformmanager.util.HttpHelper.sendGet;
import static main.java.com.banfftech.platformmanager.util.UtilTools.dateToStr;


/**
 * Created by S on 2017/9/12.
 */
public class PersonManagerQueryServices {

    public final static String module = PersonManagerQueryServices.class.getName();

    public static final String resourceError = "PlatformManagerErrorUiLabels.xml";

    public static final String resourceUiLabels = "PlatformManagerUiLabels.xml";

    /**
     * QueryCustRequestList
     *
     * @param request
     * @param response
     * @return
     * @throws GenericServiceException
     * @throws GenericEntityException
     */
    public static String webQueryCustRequestList(HttpServletRequest request, HttpServletResponse response)
            throws GenericServiceException, GenericEntityException {

        // Servlet Head

        Delegator delegator = (Delegator) request.getAttribute("delegator");
        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");

        String requestProductId = (String) request.getParameter("productId");

//        //指定查产品客户请求列表
//        if(UtilValidate.isNotEmpty(requestProductId)){
//
//        }

        String openId = (String) request.getParameter("unioId");
        GenericValue partyIdentification = EntityQuery.use(delegator).from("PartyIdentification").where("idValue", openId, "partyIdentificationTypeId", "WX_UNIO_ID").queryFirst();
        String partyId = "NA";

        if (UtilValidate.isNotEmpty(partyIdentification)) {
            partyId = (String) partyIdentification.get("partyId");
        }

        GenericValue userLogin = EntityQuery.use(delegator).from("UserLogin").where(UtilMisc.toMap("partyId", partyId)).queryFirst();

        Map<String, Object> serviceResultMap = dispatcher.runSync("queryCustRequestList", UtilMisc.toMap("userLogin", userLogin, "productId", requestProductId));
        List<GenericValue> custRequestList = null;
        if (ServiceUtil.isSuccess(serviceResultMap) && null != serviceResultMap.get("custRequestList")) {
            custRequestList = (List<GenericValue>) serviceResultMap.get("custRequestList");
        }


        request.setAttribute("custRequestList", custRequestList);

        return "success";
    }


    /**
     * Query ForwardChain FirstLines 'New Logic'
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     * @throws Exception
     * @author S
     */
    public Map<String, Object> queryForwardChainFirstLines(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException, Exception {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();

        Delegator delegator = dispatcher.getDelegator();

        Locale locale = (Locale) context.get("locale");

        Map<String, Object> resultMap = ServiceUtil.returnSuccess();

        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();

        GenericValue userLogin = (GenericValue) context.get("userLogin");
        // 当前行的basePartyId
        String addresseePartyId =     (String) context.get("addresseePartyId");
        String rowWorkEffortId  =     (String) context.get("rowWorkEffortId");

        if (userLogin == null) {
            Debug.logError("User Token Not Found...", module);
            return ServiceUtil.returnError(UtilProperties.getMessage(resourceError, "InternalServiceError", locale));
        }


        if (!UtilValidate.isNotEmpty(addresseePartyId)) {

            String basePartyId = userLogin.getString("partyId");

            List<GenericValue> forwardChainMainLines = EntityQuery.use(delegator).from("FatherWorkEffortPartyReFerrer").where(
                    UtilMisc.toMap("partyId", basePartyId)).queryList();

            if (null != forwardChainMainLines && forwardChainMainLines.size() > 0) {
                for (GenericValue rowWorkEffort : forwardChainMainLines) {
                    String fatherWorkEffortId = rowWorkEffort.getString("fatherWorkEffortId");
                    //是一条子链吗? 首行只查主链路
                    if (EntityQuery.use(delegator).from("WorkEffortAssoc").where(
                            UtilMisc.toMap("workEffortIdTo", fatherWorkEffortId)).queryFirst() != null) {
                        continue;
                    }

                    List<GenericValue> firstShareLines = EntityQuery.use(delegator).from("FatherWorkEffortPartyAddressee").where(UtilMisc.toMap("fatherWorkEffortId", fatherWorkEffortId)).queryList();
                    Debug.logInfo("*First Addressee Lines = " + firstShareLines, module);
                    // 有人点开过
                    if (null != firstShareLines && firstShareLines.size() > 0) {
                        for (GenericValue gv : firstShareLines) {
                            Map<String, Object> rowMap = new HashMap<String, Object>();
                            String rowPartyId = (String) gv.get("partyId");
                            String childWorkEffortId = (String) gv.get("subWorkEffortId");
                            rowMap.put("addresseePartyId", rowPartyId);
                            rowMap.put("workEffortId", childWorkEffortId);
                            rowMap.put("user", queryPersonBaseInfo(delegator, rowPartyId));
                            //他转发过多少次
                            List<GenericValue> workEffortAndSubWorkEffortPartyReFerrer = EntityQuery.use(delegator).from("WorkEffortAssoc").where(
                                    UtilMisc.toMap("workEffortIdFrom", childWorkEffortId)).queryList();
                            rowMap.put("addressCount",workEffortAndSubWorkEffortPartyReFerrer.size());
                            returnList.add(rowMap);
                        }
                    }

                }
            }
        }else{
            //以父链Id查子链
            List<GenericValue> workEffortAndSubWorkEffortPartyReFerrer = EntityQuery.use(delegator).from("WorkEffortAndSubWorkEffortPartyReFerrer").where(
                    UtilMisc.toMap("fatherWorkEffortId", rowWorkEffortId,"partyId",addresseePartyId)).queryList();
            if(null!=workEffortAndSubWorkEffortPartyReFerrer && workEffortAndSubWorkEffortPartyReFerrer.size()>0){
                //查出多少行转发子链
                for(GenericValue gv:workEffortAndSubWorkEffortPartyReFerrer){
                    String innerRowWorkEffortId = gv.getString("workEffortId");
                    //子链中又有多少点开的人
                    List<GenericValue> firstAddresseeLines = EntityQuery.use(delegator).from("WorkEffortAndPartyAdressee").where(UtilMisc.toMap("workEffortId", innerRowWorkEffortId)).queryList();
                    Debug.logInfo("*First Addressee Lines = " + firstAddresseeLines, module);
                    if(null!=firstAddresseeLines && firstAddresseeLines.size()>0){
                        for(GenericValue innerAddress :firstAddresseeLines){
                            String childRowWorkEffortId = innerAddress.getString("workEffortId");
                            Map<String, Object> rowMap = new HashMap<String, Object>();
                            String rowPartyId = (String) innerAddress.get("partyId");
                            rowMap.put("addresseePartyId", rowPartyId);
                            rowMap.put("workEffortId", childRowWorkEffortId);
                            rowMap.put("user", queryPersonBaseInfo(delegator, rowPartyId));
                            returnList.add(rowMap);
                        }
                    }

                }
            }


        }
        resultMap.put("firstShareLines",returnList);
        return resultMap;
    }


    /**
     * 查询分享链条(商户销售代表链)
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     * @throws Exception
     */
    public Map<String, Object> queryBProductShareFirstLines(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException, Exception {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();

        Delegator delegator = dispatcher.getDelegator();

        Locale locale = (Locale) context.get("locale");

        Map<String, Object> resultMap = ServiceUtil.returnSuccess();


        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();

        String productId = (String) context.get("productId");

        GenericValue userLogin = (GenericValue) context.get("userLogin");

        //其实是销售代表 ID

        String payToPartyId = (String) userLogin.get("partyId");

        String sharePartyId = (String) context.get("sharePartyId");


        // 以资源主的角度去找他对于这个产品作为引用人的数据。
        GenericValue workEffort = null;
        // 在第一行的基础上找下一行数据。
        if (sharePartyId != null && !UtilValidate.isEmpty(sharePartyId) && !sharePartyId.equals("null")) {
            List<GenericValue> workEfforts = EntityQuery.use(delegator).from("WorkEffortAndProductAndPartyReFerrer").where(UtilMisc.toMap("productId", productId, "partyId", sharePartyId, "description", productId + payToPartyId + sharePartyId)).queryList();
            for (GenericValue workEffortRow : workEfforts) {
                String workEffortRowId = workEffortRow.getString("workEffortId");
                GenericValue notSalesRep = EntityQuery.use(delegator).from("WorkEffortAndProductAndPartyReFerrer").where(UtilMisc.toMap("partyId", payToPartyId, "workEffortId", workEffortRowId)).queryFirst();
                if (notSalesRep == null) {
                    workEffort = workEffortRow;
                }
            }
        } else {
            //查首行数据
            Map<String, Object> queryMap = UtilMisc.toMap("productId", productId, "partyId", payToPartyId, "description", productId + payToPartyId);
            Debug.logInfo("查首行数据queryMap=" + queryMap, module);
            workEffort = EntityQuery.use(delegator).from("WorkEffortAndProductAndPartySalesRep").where(queryMap).queryFirst();
        }

        Debug.logInfo("payToPartyId=" + payToPartyId, module);
        Debug.logInfo("sharePartyId=" + sharePartyId, module);
        Debug.logInfo("productId=" + productId, module);
        Debug.logInfo("workEffort=" + workEffort, module);

        if (null != workEffort) {


            String workEffortId = (String) workEffort.get("workEffortId");
            // 找作为addressee的人的列表。
            List<GenericValue> firstShareLines = EntityQuery.use(delegator).from("WorkEffortAndProductAndPartyAddressee").where(UtilMisc.toMap("productId", productId, "workEffortId", workEffortId)).queryList();
            Debug.logInfo("firstShareLines=" + firstShareLines, module);

            // 有人点开过
            if (null != firstShareLines && firstShareLines.size() > 0) {
                for (GenericValue gv : firstShareLines) {

                    Map<String, Object> rowMap = new HashMap<String, Object>();
                    String rowPartyId = (String) gv.get("partyId");

                    rowMap.put("rowParty", rowPartyId);
                    rowMap.put("workEffortId", workEffortId);

                    rowMap.put("user", queryPersonBaseInfo(delegator, rowPartyId));
//                    GenericValue updateWorkEffort = delegator.findOne("WorkEffort",UtilMisc.toMap("workEffortId",workEffortId),false);
//                    String shareCount    =  updateWorkEffort.getString("shareCount");
//                    String addressCount  =  updateWorkEffort.getString("addressCount");

//                    rowMap.put("shareCount",shareCount);
//                    rowMap.put("addressCount",addressCount);

                    // 查询此人分享了多少次
                    GenericValue shareCountWorker = EntityQuery.use(delegator).from("WorkEffortAndProductAndPartyReFerrer").where(UtilMisc.toMap("productId", productId, "partyId", rowPartyId, "description", productId + payToPartyId + rowPartyId)).queryFirst();

                    String shareCount = "0";

                    if (null != shareCountWorker && null != shareCountWorker.get("percentComplete")) {
                        shareCount = shareCountWorker.get("percentComplete") + "";
                    }

                    rowMap.put("shareCount", shareCount);

                    GenericValue nowShareWorkEffort = EntityQuery.use(delegator).from("WorkEffortAndProductAndPartyReFerrer").where(UtilMisc.toMap("productId", productId, "partyId", rowPartyId, "description", productId + payToPartyId + rowPartyId)).queryFirst();
                    if (nowShareWorkEffort != null) {
                        String nowShareWorkEffortId = nowShareWorkEffort.getString("workEffortId");
                        Long xiaJiRenShu = EntityQuery.use(delegator).from("WorkEffortAndProductAndPartyAddressee").where(UtilMisc.toMap("productId", productId, "workEffortId", nowShareWorkEffortId)).queryCount();
                        rowMap.put("addressCount", xiaJiRenShu);
                    } else {
                        rowMap.put("addressCount", "0");
                    }

                    rowMap.put("shareCount", shareCount);
                    EntityCondition findConditions = null;
                    if (productId.indexOf("-") > 0) {
                        findConditions = EntityCondition.makeCondition("productId", EntityOperator.LIKE, "%" + productId.substring(0, productId.indexOf("-")) + "%");
                    } else {
                        findConditions = EntityCondition.makeCondition("productId", EntityOperator.EQUALS, productId);

                    }
                    EntityCondition findConditions2 = EntityCondition.makeCondition("roleTypeId", EntityOperator.EQUALS, "END_USER_CUSTOMER");
                    EntityCondition findConditions3 = EntityCondition.makeCondition("partyId", EntityOperator.EQUALS, rowPartyId);
                    EntityCondition genericCondition = EntityCondition.makeCondition(findConditions, EntityOperator.AND, findConditions2);
                    EntityCondition findConditions4 = EntityCondition.makeCondition(genericCondition, EntityOperator.AND, findConditions3);
                    List<GenericValue> queryMyResourceOrderList = delegator.findList("OrderHeaderItemAndRoles",
                            findConditions4, null,
                            null, null, false);
                    //只有真的付过钱,才算订单
                    if (queryMyResourceOrderList != null && queryMyResourceOrderList.size() > 0) {
                        for (GenericValue gvOrder : queryMyResourceOrderList) {
                            GenericValue orderPaymentPrefAndPayment = EntityQuery.use(delegator).from("OrderPaymentPreference").where("orderId", gvOrder.get("orderId")).orderBy("-createdStamp").queryFirst();
                            if (null != orderPaymentPrefAndPayment) {
                                String orderPaymentPrefAndPaymentstatusId = (String) orderPaymentPrefAndPayment.get("statusId");
                                if (orderPaymentPrefAndPaymentstatusId.equals("PAYMENT_RECEIVED")) {
                                    rowMap.put("buyCount", "1");
                                    break;
                                }
                            }
                        }
                    }


                    returnList.add(rowMap);
                }
            }

        } else {
            // 还没转发出去过
        }


        // TODO FIX ME .  FUCK METHOD NO NO NO
        //集合冒泡,数据量如果大了再封装出去做插入排
        for (int i = 0; i < returnList.size(); i++) {
            Map<String, Object> gvMap = returnList.get(i);
            int gvXiaJiRenShu = Integer.parseInt(gvMap.get("addressCount") + "");
            // 从第i+1为开始循环数组
            for (int j = i + 1; j < returnList.size(); j++) {
                Map<String, Object> gvMap2 = returnList.get(j);
                int gv2XiaJiRenShu = Integer.parseInt(gvMap2.get("addressCount") + "");
                // 如果前一位比后一位小，那么就将两个数字调换
                // 这里是按降序排列
                // 如果你想按升序排列只要改变符号即可
                if (gvXiaJiRenShu < gv2XiaJiRenShu) {
                    Map<String, Object> r = returnList.get(i);
                    returnList.set(i, returnList.get(j));
                    returnList.set(j, r);
                }
            }
        }


        resultMap.put("firstShareLines", returnList);

        return resultMap;
    }


    /**
     * 查询分享链条
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     * @throws Exception
     */
    public Map<String, Object> queryProductShareFirstLines(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException, Exception {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();

        Delegator delegator = dispatcher.getDelegator();

        Locale locale = (Locale) context.get("locale");

        Map<String, Object> resultMap = ServiceUtil.returnSuccess();


        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();

        String productId = (String) context.get("productId");

        String payToPartyId = (String) context.get("payToPartyId");

        String sharePartyId = (String) context.get("sharePartyId");

        // 以资源主的角度去找他对于这个产品作为引用人的数据。
        GenericValue workEffort = null;
        // 在第一行的基础上找下一行数据。
        if (null != sharePartyId && !sharePartyId.trim().equals("")) {
            workEffort = EntityQuery.use(delegator).from("WorkEffortAndProductAndPartyReFerrer").where(UtilMisc.toMap("productId", productId, "partyId", sharePartyId, "description", productId + sharePartyId)).queryFirst();
        } else {
            //查首行数据
            workEffort = EntityQuery.use(delegator).from("WorkEffortAndProductAndPartyReFerrer").where(UtilMisc.toMap("productId", productId, "partyId", payToPartyId, "description", productId + payToPartyId)).queryFirst();
        }
        if (null != workEffort) {


            String workEffortId = (String) workEffort.get("workEffortId");
            // 找作为addressee的人的列表。
            List<GenericValue> firstShareLines = EntityQuery.use(delegator).from("WorkEffortAndProductAndPartyAddressee").where(UtilMisc.toMap("productId", productId, "workEffortId", workEffortId)).queryList();
            // 有人点开过
            if (null != firstShareLines && firstShareLines.size() > 0) {
                for (GenericValue gv : firstShareLines) {

                    Map<String, Object> rowMap = new HashMap<String, Object>();
                    String rowPartyId = (String) gv.get("partyId");

                    rowMap.put("rowParty", rowPartyId);

                    rowMap.put("user", queryPersonBaseInfo(delegator, rowPartyId));

                    // 查询此人分享了多少次
                    GenericValue shareCountWorker = EntityQuery.use(delegator).from("WorkEffortAndProductAndPartyReFerrer").where(UtilMisc.toMap("productId", productId, "partyId", rowPartyId, "description", productId + rowPartyId)).queryFirst();

                    String shareCount = "0";

                    if (null != shareCountWorker && null != shareCountWorker.get("percentComplete")) {
                        shareCount = shareCountWorker.get("percentComplete") + "";
                    }

                    rowMap.put("shareCount", shareCount);


                    returnList.add(rowMap);
                }
            }

        } else {
            // 还没转发出去过
        }

        resultMap.put("firstShareLines", returnList);

        return resultMap;
    }


    /**
     * genericPaymentService
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     * @throws Exception
     */
    public Map<String, Object> genericPaymentService(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException, Exception {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();

        Delegator delegator = dispatcher.getDelegator();

        Locale locale = (Locale) context.get("locale");

        Map<String, Object> resultMap = ServiceUtil.returnSuccess();

        return resultMap;
    }


    /**
     * getTel FromEncryptedData
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     * @throws ParseException
     */
    public Map<String, Object> getTelFromEncryptedData(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException, Exception {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();

        Delegator delegator = dispatcher.getDelegator();

        Locale locale = (Locale) context.get("locale");

        Map<String, Object> resultMap = ServiceUtil.returnSuccess();


        String code = (String) context.get("code");

        String iv = (String) context.get("iv");

        String encryptedData = (String) context.get("encryptedData");


        String responseStr2 = sendGet(PeConstant.WECHAT_MINI_PROGRAM_SNS_PATH,
                "appid=" + PeConstant.WECHAT_MINI_PROGRAM_APP_ID +
                        "&secret=" + PeConstant.WECHAT_MINI_PROGRAM_APP_SECRET_ID +
                        "&js_code=" + code + "&grant_type=authorization_code");

        JSONObject jsonMap2 = JSONObject.fromObject(responseStr2);
        String session_key = "" + jsonMap2.get("session_key");
        System.out.println("===========================>");
        System.out.println("JSON MAP2=" + jsonMap2);
        System.out.println("iv=" + iv);
        System.out.println("encryptedData=" + encryptedData);
        System.out.println("===========================>");
        // UtilTools.decrypt(Base64.decodeBase64(session_key),Base64.decodeBase64(iv),Base64.decodeBase64(encryptedData));
//        byte[] dataByte = Base64Util.decode(encryptedData);
//        // 加密秘钥
//        byte[] keyByte = Base64Util.decode(session_key);
//        // 偏移量
//        byte[] ivByte = Base64Util.decode(iv);
        //TODO FIX ME  偏移量错误
        JSONObject json = getUserInfo(session_key, iv, encryptedData);
        //decrypt(keyByte,ivByte,dataByte);

//        System.out.println("JSON DATA - > " +json);
        resultMap.put("tel", "15000035538");

        return resultMap;
    }

    public static byte[] decrypt(byte[] key, byte[] iv, byte[] encData)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        return cipher.doFinal(encData);
    }

    /**
     * 解密用户敏感数据获取用户信息
     *
     * @param sessionKey    数据进行加密签名的密钥
     * @param encryptedData 包括敏感数据在内的完整用户信息的加密数据
     * @param iv            加密算法的初始向量
     */
    public JSONObject getUserInfo(String encryptedData, String sessionKey, String iv) throws UnsupportedEncodingException, InvalidAlgorithmParameterException, InvalidKeyException, InvalidParameterSpecException, BadPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, NoSuchPaddingException {
        // 被加密的数据
        byte[] dataByte = Base64Util.decode(encryptedData);
        // 加密秘钥
        byte[] keyByte = Base64Util.decode(sessionKey);
        // 偏移量
        byte[] ivByte = Base64Util.decode(iv);

        // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
        int base = 16;
        if (keyByte.length % base != 0) {
            int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
            byte[] temp = new byte[groups * base];
            Arrays.fill(temp, (byte) 0);
            System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
            keyByte = temp;
        }
        // 初始化
        Security.addProvider(new BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
        AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
        parameters.init(new IvParameterSpec(ivByte));
        cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
        byte[] resultByte = cipher.doFinal(dataByte);
        if (null != resultByte && resultByte.length > 0) {
            String result = new String(resultByte, "UTF-8");
            return JSONObject.fromObject(result);
        }

        return null;
    }


    /**
     * Query CustRequestList
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryCustRequestList(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException, ParseException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();

        Delegator delegator = dispatcher.getDelegator();

        Locale locale = (Locale) context.get("locale");

        Map<String, Object> resultMap = ServiceUtil.returnSuccess();

        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();


        String reqProductId = (String) context.get("productId");

        String partyId = "";

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> reqProductId =" + reqProductId);

        GenericValue userLogin = null;

        if (UtilValidate.isNotEmpty(reqProductId)) {
            GenericValue productAdmin = EntityQuery.use(delegator).from("ProductRole").where("productId", reqProductId, "roleTypeId", "ADMIN").queryFirst();
            partyId = (String) productAdmin.get("partyId");

        } else {
            userLogin = (GenericValue) context.get("userLogin");
            partyId = (String) userLogin.get("partyId");
        }


        String roleTypeId = (String) context.get("roleTypeId");

        String viewIndexStr = (String) context.get("viewIndex");

        //String viewSize  = (String) context.get("viewSize");


        int viewIndex = 0;

        if (viewIndexStr != null) {
            viewIndex = Integer.parseInt(viewIndexStr);
        }

        int viewSize = 999;

        // Default 'REQ_REQUESTER'
        if (!UtilValidate.isNotEmpty(roleTypeId)) {
            roleTypeId = "REQ_REQUESTER";
        }

        if (UtilValidate.isNotEmpty(viewIndexStr)) {
            viewIndex = Integer.parseInt(viewIndexStr);
        }
//查请求接受者的请求列表,当产品id不为空的时候
        if (UtilValidate.isNotEmpty(reqProductId)) {
            roleTypeId = "REQ_TAKER";
        }

        List<String> orderBy = UtilMisc.toList("-createdDate");

        PagedList<GenericValue> custRequestAndRolePage = null;


        custRequestAndRolePage = EntityQuery.use(delegator).from("CustRequestAndRole").
                where("partyId", partyId, "roleTypeId", roleTypeId).orderBy(orderBy)
                .distinct()
                .queryPagedList(viewIndex, viewSize);


        List<GenericValue> custRequestAndRoleList = custRequestAndRolePage.getData();

        System.out.println("?>>>>>>>>>>>>==> custRequestAndRoleListSize = " + custRequestAndRoleList.size());

        if (null != custRequestAndRoleList && custRequestAndRoleList.size() > 0) {
            for (GenericValue gv : custRequestAndRoleList) {
                Map<String, Object> rowMap = new HashMap<String, Object>();
                String custRequestName = (String) gv.get("custRequestName");
                rowMap.put("custRequestName", custRequestName);
                String description = (String) gv.get("description");
                rowMap.put("description", description);
                DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String createdDate = sdf.format(gv.get("createdDate"));
                java.util.Date date = sdf.parse(createdDate);
                long s1 = date.getTime();//将时间转为毫秒
                long s2 = System.currentTimeMillis();//得到当前的毫秒
                long dayago = (s2 - s1) / 1000 / 60;
                if (dayago > 0) {
                    rowMap.put("createdDate", dayago + "分钟前");
                } else {
                    rowMap.put("createdDate", "刚刚");
                }


                String requestPartyId = (String) gv.get("partyId");
                rowMap.put("requestPartyId", requestPartyId);
                String custRequestId = (String) gv.get("custRequestId");
                rowMap.put("custRequestId", custRequestId);

                GenericValue custRequestItem = null;
                String productId = "";


                if (UtilValidate.isNotEmpty(reqProductId)) {
                    custRequestItem = EntityQuery.use(delegator).from("CustRequestItem").where(UtilMisc.toMap("custRequestId", custRequestId)).queryFirst();
                    if (null == custRequestItem) {
                        continue;
                    } else {
                        productId = (String) custRequestItem.get("productId");
                        System.out.println(">>> Row productId =" + productId + " req productId =" + reqProductId);
                    }

                    if (productId.equals(reqProductId)) {
                        GenericValue custRole = EntityQuery.use(delegator).from("CustRequestAndRole").where("custRequestId", custRequestId, "roleTypeId", "REQ_REQUESTER").queryFirst();
                        rowMap.put("user", queryPersonBaseInfo(delegator, (String) custRole.get("partyId")));
                        returnList.add(rowMap);
                    }

                } else {
                    custRequestItem = EntityQuery.use(delegator).from("CustRequestItem").where(UtilMisc.toMap("custRequestId", custRequestId)).queryFirst();
                    productId = (String) custRequestItem.get("productId");
                    GenericValue product = EntityQuery.use(delegator).from("Product").where(UtilMisc.toMap("productId", productId)).queryFirst();
                    rowMap.put("productName", product.get("productName"));
                    rowMap.put("detailImageUrl", product.get("detailImageUrl"));
                    GenericValue productPrice = EntityQuery.use(delegator).from("ProductPrice").where(UtilMisc.toMap("productId", productId)).queryFirst();
                    rowMap.put("price", productPrice.get("price"));
                    returnList.add(rowMap);
                }


                System.out.println(">>>rowMap=" + rowMap);

            }
        }


        resultMap.put("custRequestList", returnList);

        return resultMap;
    }


    /**
     * queryProduct Detail
     *
     * @param request
     * @param response
     * @return
     * @throws GenericServiceException
     * @throws GenericEntityException
     */
    public static String queryProductDetail(HttpServletRequest request, HttpServletResponse response)
            throws GenericServiceException, GenericEntityException {

        // Servlet Head

        Delegator delegator = (Delegator) request.getAttribute("delegator");

        String productId = (String) request.getParameter("productId");

        String nowPartyId = (String) request.getParameter("partyId");


        GenericValue userLogin = EntityQuery.use(delegator).from("UserLogin").where(UtilMisc.toMap("partyId", nowPartyId)).queryFirst();
        LocalDispatcher dispatcher = (LocalDispatcher) request.getAttribute("dispatcher");
        //有效时间
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
        claims.put("user", userLogin.get("userLoginId"));
        claims.put("delegatorName", delegator.getDelegatorName());
        claims.put("exp", exp);
        claims.put("iat", iat);
        String token = signer.sign(claims);


        Set<String> fieldSet = new HashSet<String>();
        fieldSet.add("productId");
        fieldSet.add("productName");
        fieldSet.add("productStoreId");
        fieldSet.add("createdDate");
        fieldSet.add("salesDiscontinuationDate");
        fieldSet.add("price");
        fieldSet.add("detailImageUrl");
        fieldSet.add("prodCatalogId");
        fieldSet.add("payToPartyId");
        fieldSet.add("description");

        EntityCondition findConditions = EntityCondition
                .makeCondition(UtilMisc.toMap("productId", productId));

        GenericValue product = delegator.findList("ProductAndCategoryMember",
                findConditions, fieldSet,
                null, null, false).get(0);


        Map<String, Object> resourceDetail = product.getAllFields();
        if (product != null && product.get("salesDiscontinuationDate") == null) {
            resourceDetail.put("salesDiscontinuationDate", "");
        }

//        GenericValue person = delegator.findOne("Person", UtilMisc.toMap("partyId", resourceDetail.get("payToPartyId")), false);
//
//        if(person!=null){
//
//
//            List<GenericValue> contentsList =
//                    EntityQuery.use(delegator).from("PartyContentAndDataResource").
//                            where("partyId", resourceDetail.get("payToPartyId"), "partyContentTypeId", "LGOIMGURL").orderBy("-fromDate").queryPagedList(0,999999).getData();
//
//
//            GenericValue partyContent = null;
//            if(null != contentsList && contentsList.size()>0){
//                partyContent = contentsList.get(0);
//            }
//
//            if (UtilValidate.isNotEmpty(partyContent)) {
//
//                String contentId = partyContent.getString("contentId");
//                resourceDetail.put("headPortrait",
//                        partyContent.getString("objectInfo"));
//            } else {
//                resourceDetail.put("headPortrait",
//                        "https://personerp.oss-cn-hangzhou.aliyuncs.com/datas/images/defaultHead.png");
//            }
//            resourceDetail.put("firstName",(String) person.get("firstName"));
//
//            //PartyNoteView
//            GenericValue partyNoteView = EntityQuery.use(delegator).from("PartyNoteView").where("targetPartyId", resourceDetail.get("payToPartyId")).queryFirst();
//            if(UtilValidate.isNotEmpty(partyNoteView)){
//                resourceDetail.put("partyNote",partyNoteView.get("noteInfo"));
//            }else{
//                resourceDetail.put("partyNote","这位卖家还未设置个人说明...");
//            }
//
//        }


        Set<String> orderFieldSet = new HashSet<String>();
        fieldSet.add("orderId");
        fieldSet.add("partyId");
        fieldSet.add("productId");


        EntityCondition findOrderConditions = EntityCondition
                .makeCondition(UtilMisc.toMap("productId", productId));


        //Query My Resource
        List<GenericValue> queryMyResourceOrderList = delegator.findList("OrderHeaderItemAndRoles",
                findOrderConditions, orderFieldSet,
                UtilMisc.toList("-orderDate"), null, false);

        List<Map<String, Object>> partyOrderList = new ArrayList<Map<String, Object>>();
        if (queryMyResourceOrderList != null && queryMyResourceOrderList.size() > 0) {
            resourceDetail.put("orderId", queryMyResourceOrderList.get(0).get("orderId"));
            for (GenericValue order : queryMyResourceOrderList) {
                Map<String, Object> rowMap = new HashMap<String, Object>();

                String partyId = (String) order.get("partyId");
                GenericValue orderPerson = delegator.findOne("Person", UtilMisc.toMap("partyId", order.get("partyId")), false);
//                if(person!=null){
//                    rowMap.put("firstName",(String) orderPerson.get("firstName"));
//                }
                partyOrderList.add(rowMap);
            }
        }

        resourceDetail.put("partyBuyOrder", partyOrderList);


        // Query Product More Images & Text
        List<Map<String, Object>> productMoreDetails = new ArrayList<Map<String, Object>>();
        resourceDetail.put("productMoreDetails", productMoreDetails);


        fieldSet = new HashSet<String>();

        fieldSet.add("drObjectInfo");

        fieldSet.add("productId");

        EntityCondition findConditions3 = EntityCondition
                .makeCondition("productId", EntityOperator.EQUALS, productId);

        List<GenericValue> pictures = delegator.findList("ProductContentAndInfo",
                findConditions3, fieldSet,
                null, null, false);


        Long custCount = EntityQuery.use(delegator).from("ProductRole").where("productId", productId, "roleTypeId", PeConstant.PRODUCT_CUSTOMER).queryCount();

        Long placingCount = EntityQuery.use(delegator).from("ProductRole").where("productId", productId, "roleTypeId", "PLACING_CUSTOMER").queryCount();

        resourceDetail.put("custCount", custCount);

        resourceDetail.put("placingCount", placingCount);

        resourceDetail.put("morePicture", pictures);


//        GenericValue inventoryItem = EntityQuery.use(delegator).from("InventoryItem").where("productId", productId).queryFirst();
//
//        if(null!=inventoryItem){
//            resourceDetail.put("availableToPromiseTotal",inventoryItem.get("availableToPromiseTotal"));
//            resourceDetail.put("quantityOnHandTotal",inventoryItem.get("quantityOnHandTotal"));
//            resourceDetail.put("accountingQuantityTotal",inventoryItem.get("accountingQuantityTotal"));
//        }
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));
        String payToPartyId = (String) product.get("payToPartyId");
        GenericValue facility = EntityQuery.use(delegator).from("Facility").where("ownerPartyId", payToPartyId).queryFirst();
        String originFacilityId = (String) facility.get("facilityId");
        Map<String, Object> getInventoryAvailableByFacilityMap = dispatcher.runSync("getInventoryAvailableByFacility", UtilMisc.toMap("userLogin", admin,
                "facilityId", originFacilityId, "productId", productId));
        if (ServiceUtil.isSuccess(getInventoryAvailableByFacilityMap)) {
            resourceDetail.put("quantityOnHandTotal", getInventoryAvailableByFacilityMap.get("quantityOnHandTotal"));
            resourceDetail.put("availableToPromiseTotal", getInventoryAvailableByFacilityMap.get("availableToPromiseTotal"));
        }


        request.setAttribute("resourceDetail", resourceDetail);

        request.setAttribute("tarjeta", token);

        return "success";
    }


    /**
     * 查询聊天记录
     *
     * @param request
     * @param response
     * @return
     * @throws GenericServiceException
     * @throws GenericEntityException
     */
    public static String loadChatMessage(HttpServletRequest request, HttpServletResponse response)
            throws GenericServiceException, GenericEntityException {

        // Servlet Head

        Delegator delegator = (Delegator) request.getAttribute("delegator");


        // Admin Do Run Service


        String objectId = (String) request.getParameter("objectId");

        String partyIdTo = (String) request.getParameter("partyIdTo");

        String partyIdFrom = (String) request.getParameter("partyIdFrom");


        System.out.println("IN QUERY PARAM = objectId = " + objectId);
        System.out.println("IN QUERY PARAM = partyIdTo = " + partyIdTo);
        System.out.println("IN QUERY PARAM = partyIdFrom = " + partyIdFrom);

        Set<String> fieldSet = new HashSet<String>();

        // 区分作用域 WebChat 还是 App 查询列用途
        String bizType = "webChat";

        fieldSet.add("messageId");
        fieldSet.add("message");
        fieldSet.add("partyIdFrom");
        fieldSet.add("partyIdTo");
        fieldSet.add("objectId");
        fieldSet.add("messageId");
        fieldSet.add("fromDate");
        fieldSet.add("messageLogTypeId");

//        EntityCondition findConditions3 = EntityCondition
//                .makeCondition(UtilMisc.toMap("partyIdTo", partyIdTo));
//
//
//        EntityCondition findConditions4 = EntityCondition
//                .makeCondition(UtilMisc.toMap("partyIdFrom", partyIdTo));
//
//        EntityCondition listConditions2 = EntityCondition
//                .makeCondition(findConditions3, EntityOperator.OR, findConditions4);
//
//
//        EntityConditionList<EntityCondition> listBigConditions = null;
//
//        if (UtilValidate.isNotEmpty(partyIdFrom)) {
//            EntityCondition findConditions = EntityCondition
//                    .makeCondition(UtilMisc.toMap("partyIdTo", partyIdFrom));
//
//
//            EntityCondition findConditions2 = EntityCondition
//                    .makeCondition(UtilMisc.toMap("partyIdFrom", partyIdFrom));
//
//            EntityCondition listConditions = EntityCondition
//                    .makeCondition(findConditions, EntityOperator.OR, findConditions2);
//            listBigConditions = EntityCondition
//                    .makeCondition(listConditions, listConditions2);
//        } else {
//            listBigConditions = EntityCondition
//                    .makeCondition(listConditions2);
//        }
//
//
//        List<GenericValue> queryMessageLogList = null;
//
//        if (UtilValidate.isNotEmpty(bizType) && bizType.equals("webChat")) {
//            queryMessageLogList = delegator.findList("MessageLog",
//                    listBigConditions, fieldSet,
//                    UtilMisc.toList("fromDate"), null, false);
//        } else {
//            queryMessageLogList = delegator.findList("MessageLogView",
//                    listBigConditions, fieldSet,
//                    UtilMisc.toList("-fromDate"), null, false);
//        }


        List<GenericValue> queryMessageLogList = EntityQuery.use(delegator).from("MessageLog").where("partyIdTo", partyIdTo).orderBy("-fromDate").queryList();

        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();

        for (GenericValue gv : queryMessageLogList) {

            Map<String, Object> rowMap = new HashMap<String, Object>();

            Map<String, Object> userMap = new HashMap<String, Object>();

            String fromParty = (String) gv.get("partyIdFrom");
            String toParty = (String) gv.get("partyIdTo");
            String message = (String) gv.get("message");

            String tsStr = "";

            DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

            try {
                //方法一

                tsStr = sdf.format(gv.get("fromDate"));

            } catch (Exception e) {

            }

            String messageLogTypeId = (String) gv.get("messageLogTypeId");

//            rowMap.put("messageLogTypeId", messageLogTypeId);

//            if(UtilValidate.isNotEmpty(messageLogTypeId) && messageLogTypeId.equals("LOCATION") && message!=null && !message.trim().equals("")){
//                rowMap.put("latitude", message.substring(message.indexOf("tude\":")+6,message.indexOf(",\"lo")));
//                rowMap.put("longitude", message.substring(message.indexOf("longitude\":") + 11, message.lastIndexOf("}")));
//            }
            String messageId = (String) gv.get("messageId");
//            rowMap.put("messageId", "checkAddress/" + messageId);
            rowMap.put("messageId", messageId);

            rowMap.put("content", message);


            rowMap.put("time", tsStr);
            rowMap.put("messageLogTypeId", messageLogTypeId);


            Map<String, String> user = null;

//            if (!partyIdTo.equals(fromParty)) {
//                user = queryPersonBaseInfo(delegator, toParty);
//
//            } else {
//                user = queryPersonBaseInfo(delegator, fromParty);
//            }
            user = queryPersonBaseInfo(delegator, fromParty);
            //此处拿的是from


            rowMap.put("avatar", user.get("headPortrait"));

            // it me
            if (partyIdFrom.equals(fromParty)) {
                rowMap.put("me", true);
            } else {
                rowMap.put("me", false);
            }

            rowMap.put("username", user.get("firstName"));

            returnList.add(rowMap);

        }

        request.setAttribute("messages", returnList);

        return "success";
    }


    /**
     * queryProductTuCaoList
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryProductTuCaoList(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();

        Delegator delegator = dispatcher.getDelegator();

        Locale locale = (Locale) context.get("locale");

        Map<String, Object> resultMap = ServiceUtil.returnSuccess();

        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();

        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        GenericValue userLogin = (GenericValue) context.get("userLogin");

        String productId = (String) context.get("productId");

        List<GenericValue> productContentAndInfos = EntityQuery.use(delegator).from("ProductContentAndInfo").where("productId", productId).queryList();

        System.out.println(">>>>>>>>>>>>>>productId=" + productId);
        System.out.println(">>>>>>>>>>>>>>productContentAndInfos=" + productContentAndInfos);

        if (productContentAndInfos.size() > 0) {
            for (GenericValue gv : productContentAndInfos) {

                Map<String, Object> rowMap = new HashMap<String, Object>();

                String contentId = (String) gv.get("contentId");

                GenericValue content = EntityQuery.use(delegator).from("Content").where("contentId", contentId).queryFirst();

                String dataResourceId = (String) content.get("dataResourceId");

                GenericValue electronicText = EntityQuery.use(delegator).from("ElectronicText").where("dataResourceId", dataResourceId).queryFirst();
                if (null != electronicText) {


                    DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    String tsStr = "";
                    try {

                        tsStr = sdf.format(electronicText.get("createdStamp"));

                    } catch (Exception e) {

                    }
                    rowMap.put("ts", tsStr);

                    rowMap.put("contentId", contentId);

                    rowMap.put("text", electronicText.get("textData"));

                    String createdByUserLogin = (String) gv.get("createdByUserLogin");

                    GenericValue findUserLogin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", createdByUserLogin));

                    String tuCaoPartyId = (String) findUserLogin.get("partyId");

                    rowMap.put("userInfo", queryPersonBaseInfo(delegator, tuCaoPartyId));

                    returnList.add(rowMap);
                }
            }
        }


        resultMap.put("tuCaoList", returnList);
        return resultMap;
    }


    /**
     * query PartyRequests
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryPartyRequests(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();

        Delegator delegator = dispatcher.getDelegator();

        Locale locale = (Locale) context.get("locale");

        Map<String, Object> resultMap = ServiceUtil.returnSuccess();

        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();

        GenericValue userLogin = (GenericValue) context.get("userLogin");

        String partyId = (String) userLogin.get("partyId");

        List<GenericValue> custRequestAndRoleList = EntityQuery.use(delegator).from("CustRequestAndRole").where("partyId", partyId, "roleTypeId", "REQ_TAKER").queryList();

        if (custRequestAndRoleList.size() > 0) {
            for (GenericValue gv : custRequestAndRoleList) {
                Map<String, Object> rowMap = new HashMap<String, Object>();
                String custRequestId = (String) gv.get("custRequestId");
                rowMap.put("custRequestId", custRequestId);
                GenericValue custRequester = EntityQuery.use(delegator).from("CustRequestAndRole").where("custRequestId", custRequestId, "roleTypeId", "REQ_REQUESTER").queryFirst();
                String custId = (String) custRequester.get("partyId");

                Map<String, String> customerInfo = queryPersonBaseInfo(delegator, custId);
                rowMap.put("custInfo", customerInfo);
                rowMap.put("custPartyId", custId);

                //TODO FIX
                GenericValue custRequest = EntityQuery.use(delegator).from("CustRequest").where("custRequestId", custRequestId).queryFirst();
                GenericValue custRequestItem = EntityQuery.use(delegator).from("CustRequestItem").where("custRequestId", custRequestId).queryFirst();
                String custRequestName = (String) custRequest.get("custRequestName");
                rowMap.put("custRequestName", custRequestName);


                String reason = (String) custRequest.get("reason");
                String story = (String) custRequestItem.get("story");
                String productId = (String) custRequestItem.get("productId");

                rowMap.put("reason", reason);
                rowMap.put("featureDesc", story);
                rowMap.put("productId", productId);

                GenericValue product = delegator.findOne("Product", UtilMisc.toMap("productId", productId), false);
                rowMap.put("productName", product.get("productName"));
                rowMap.put("productImage", product.get("smallImageUrl"));


                returnList.add(rowMap);
            }
        }

        resultMap.put("requestList", returnList);

        return resultMap;
    }

    /**
     * 查询用户产品特征类型的偏好设置
     *
     * @param dctx
     * @param context
     * @return ServiceResultMap
     * @throws GenericEntityException
     * @throws GenericServiceException
     * @author S
     */
    public static Map<String, Object> queryUserProductFeaturePreference(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();

        Delegator delegator = dispatcher.getDelegator();

        Locale locale = (Locale) context.get("locale");

        Map<String, Object> resultMap = ServiceUtil.returnSuccess();

        List<Map<String, Object>> productFeatureList = new ArrayList<Map<String, Object>>();

        GenericValue userLogin = (GenericValue) context.get("userLogin");

        String userLoginId = (String) userLogin.get("userLoginId");

        //查询系统预设的产品特征类型
        List<GenericValue> systemDefaultFeatureTypes = EntityQuery.use(delegator).from("ProductFeatureType").where().queryList();

        //查询用户偏好设置中保存的特征类型
        List<GenericValue> productFeaturePreferenceList = EntityQuery.use(delegator).from("UserPreferenceProductFeatures").where("userLoginId", userLoginId).queryList();

        //TODO FIX 别人的偏好设置不作为系统预设查出来


        if (null != productFeaturePreferenceList && productFeaturePreferenceList.size() > 0) {
            for (GenericValue gv : productFeaturePreferenceList) {
                Map<String, Object> rowMap = gv.getAllFields();
                productFeatureList.add(rowMap);
            }
        }

        //系统预设的FeatureType
        for (GenericValue gv : systemDefaultFeatureTypes) {
            Map<String, Object> rowMap = gv.getAllFields();


//            GenericValue userPreference= EntityQuery.use(delegator).from("UserPreference").where("userPrefTypeId",rowMap.get("productFeatureTypeId"),"userLoginId",userLoginId).queryFirst();
//            if(userPreference!=null){
//                continue;
//            }
            String getI18N = UtilProperties.getMessage(resourceUiLabels, "ProductFeatureType.description." + rowMap.get("productFeatureTypeId"), new Locale("zh"));

            //没有这个国际化配置,说明是新增的不是预设的
            if (getI18N.equals("ProductFeatureType.description." + rowMap.get("productFeatureTypeId"))) {
                continue;
            } else {
                rowMap.put("description", getI18N);
            }
            productFeatureList.add(rowMap);
        }


        resultMap.put("featureTypeList", productFeatureList);

        return resultMap;
    }


    /**
     * 查询产品的特征及特征属性
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryProductFeatures(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();

        Delegator delegator = dispatcher.getDelegator();

        Locale locale = (Locale) context.get("locale");

        Map<String, Object> resultMap = ServiceUtil.returnSuccess();

        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();

//        GenericValue userLogin = (GenericValue) context.get("userLogin");
//
//        String partyId = (String) userLogin.get("partyId");
        String productId = (String) context.get("productId");

        List<GenericValue> productFeatures = EntityQuery.use(delegator).from("ProductFeatureAndAppl").where("productId", productId).queryList();

        for (GenericValue gv : productFeatures) {

            Map<String, Object> rowMap = new HashMap<String, Object>();


            String productFeatureId = (String) gv.get("productFeatureId");

            String productFeatureTypeId = (String) gv.get("productFeatureTypeId");

            String description = (String) gv.get("description");


//            List<GenericValue> productFeaturesAttrs = EntityQuery.use(delegator).from("ProductFeatureApplAttr").where("productId", productId,"productFeatureId",productFeatureId).queryList();
//
//            List<Map<String,Object>> rowMapList = new ArrayList<Map<String, Object>>();
//
//            for(GenericValue gv2 : productFeaturesAttrs){
//                Map<String,Object> rowMaps = new HashMap<String, Object>();
//                rowMaps.put("optionValue",(String) gv2.get("attrValue"));
//                rowMapList.add(rowMaps);
//            }

            String getI18N = UtilProperties.getMessage(resourceUiLabels, "ProductFeatureType.description." + productFeatureTypeId, new Locale("zh"));
            if (getI18N.equals("ProductFeatureType.description." + productFeatureTypeId)) {
                GenericValue productFeatureType = delegator.findOne("ProductFeatureType", UtilMisc.toMap("productFeatureTypeId", productFeatureTypeId), false);
                rowMap.put((String) productFeatureType.get("description"), description);
            } else {
                //说明是预设的
                rowMap.put(getI18N, description);
            }


//
//            String attrName = (String) gv.get("attrName");
//
//            String attrValue = (String) gv.get("attrValue");
//
//            attrName = attrName.substring(0,attrName.indexOf("|"));
//
//            rowMap.put("attrName",attrName);
//
//            rowMap.put("attrValue",attrValue);
//
//            returnList.add(rowMap);
            returnList.add(rowMap);
        }

        List<Map<String, List<String>>> returnMapList = new ArrayList<Map<String, List<String>>>();


//                [{"颜色":"红色"},{"颜色":"白色"},{"颜色":"蓝色"},{"品牌":"彪马"},{"品牌":"kappa"},{"品牌":"尼克"}]


        List<String> keyList = new ArrayList<String>();
        Debug.logInfo("sbreturnList=" + returnList, module);
        for (Map<String, Object> rowMap : returnList) {

            Map<String, List<String>> rowsMap = new HashMap<String, List<String>>();
            for (String key : rowMap.keySet()) {
                Debug.logInfo("key=" + key, module);
                Debug.logInfo("keyList.contains(key)=" + keyList.contains(key), module);
                if (!keyList.contains(key)) {
                    keyList.add(key);
                    List<String> innerList = new ArrayList<String>();
                    innerList.add((String) rowMap.get(key));
                    rowsMap.put(key, innerList);
                    returnMapList.add(rowsMap);
                } else {
                    Debug.logInfo("else rowMap =" + rowMap, module);
                    Debug.logInfo("else rowsMap =" + rowsMap, module);


                    List<String> beforeList = null;
                    Debug.logInfo("returnMapList =" + returnMapList, module);
                    if (returnMapList != null && returnMapList.size() > 0) {
                        for (Map<String, List<String>> mapStringList : returnMapList) {
                            if (null != mapStringList.get(key)) {
                                beforeList = mapStringList.get(key);
                                break;
                            } else {
                                continue;
                            }
                        }
                    }
                    Debug.logInfo("beforeList =" + beforeList, module);
                    if (beforeList != null) {
                        beforeList.add((String) rowMap.get(key));
                        rowsMap.put(key, beforeList);
                        //  returnMapList.add(rowsMap);
                    }
                }
            }


        }

        Debug.logInfo("keyList =" + keyList, module);
        Debug.logInfo("returnMapList =" + returnMapList, module);
        resultMap.put("productFeaturesList", returnMapList);

        return resultMap;
    }


    public static Map<String, Object> queryProductFeatures2(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();

        Delegator delegator = dispatcher.getDelegator();

        Locale locale = (Locale) context.get("locale");

        Map<String, Object> resultMap = ServiceUtil.returnSuccess();

        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();

        String productId = (String) context.get("productId");

        List<GenericValue> productFeatures = EntityQuery.use(delegator).from("ProductFeatureAndAppl").where("productId", productId).queryList();

        for (GenericValue gv : productFeatures) {

            Map<String, Object> rowMap = new HashMap<String, Object>();
            String productFeatureId = (String) gv.get("productFeatureId");

            String productFeatureTypeId = (String) gv.get("productFeatureTypeId");

            String description = (String) gv.get("description");


            String getI18N = UtilProperties.getMessage(resourceUiLabels, "ProductFeatureType.description." + productFeatureTypeId, new Locale("zh"));
            if (getI18N.equals("ProductFeatureType.description." + productFeatureTypeId)) {
                GenericValue productFeatureType = delegator.findOne("ProductFeatureType", UtilMisc.toMap("productFeatureTypeId", productFeatureTypeId), false);
                rowMap.put("type", (String) productFeatureType.get("description"));
                rowMap.put("typeValue", description);
                rowMap.put("typeId", productFeatureTypeId);
            } else {
                //说明是预设的
                rowMap.put("typeId", productFeatureTypeId);
                rowMap.put("type", getI18N);
                rowMap.put("typeValue", description);
            }


            returnList.add(rowMap);
        }

        String nowKey = "";
        String beforeKey = "";
        String htmlBuilder = "";
        int rowCount = 1; //共几行计数
        int rowTypeCount = 1; //几种类型计数

        for (Map<String, Object> rowMap : returnList) {
            String keyValue = (String) rowMap.get("typeValue");
            String typeId = (String) rowMap.get("typeId");

            if (rowCount == 1) {
                htmlBuilder = "<div class=\"card_cont card1\">" + "<div class=\"card\">" +
                        "<p class=\"question\"><span>1</span>" + (String) rowMap.get("type") + "</p>\n" +
                        "<ul class=\"select\">";

            }
            nowKey = (String) rowMap.get("type");
            if (nowKey.equals(beforeKey) && rowCount != 1) {
                //说明是一个特种类型
                htmlBuilder += "<li>" +
                        "<input id=\"q" + rowTypeCount + "_" + rowCount + "\" type=\"radio\" name=\"r-group-" + rowTypeCount + "\" value=\"" + typeId + "_" + keyValue + "\" >" +
                        "<label for=\"q" + rowTypeCount + "_" + rowCount + "\">" + keyValue +
                        "</label>" + "</li>";
            }
            if (!nowKey.equals(beforeKey) && rowCount == 1) {
                //第一行数据
                htmlBuilder += "<li>" +
                        "<input id=\"q" + rowTypeCount + "_" + rowCount + "\" value=\"" + typeId + "_" + keyValue + "\" type=\"radio\" name=\"r-group-" + rowTypeCount + "\" >" +
                        "<label for=\"q" + rowTypeCount + "_" + rowCount + "\">" + keyValue +
                        "</label>" + "</li>";
            }

            if (!nowKey.equals(beforeKey) && rowCount != 1) {
                //更换特征类型
                rowTypeCount += 1;//类型计数+1
                //上个选项收尾
                htmlBuilder += "</ul></div></div> ";
                //新的选项
                htmlBuilder += "<div class=\"card_cont card" + rowTypeCount + "\">" + "<div class=\"card\">" +
                        "<p class=\"question\"><span>" + rowTypeCount + "</span>" + nowKey + "</p>\n" +
                        "<ul class=\"select\">";
                htmlBuilder += "<li>" +
                        "<input id=\"q" + rowTypeCount + "_" + rowCount + "\" value=\"" + typeId + "_" + keyValue + "\" type=\"radio\" name=\"r-group-" + rowTypeCount + "\" >" +
                        "<label for=\"q" + rowTypeCount + "_" + rowCount + "\">" + keyValue +
                        "</label>" + "</li>";
            }

            beforeKey = nowKey;
            rowCount += 1;
        }
        //收尾

        htmlBuilder += "</ul><br/><input id='markText' size=\"25\" style=\"width:255px;height=30px;\"/ placeholder=\"给卖家的留言..\" value=\"\"/>" +
                "<br/><br/><br/><br/><button style='width:100px;background-color: #e9686b;color:white;' onclick='javascript:commitCustRequest();'>提交</button></div></div>";
//        resultMap.put("productFeaturesList",returnList);
        resultMap.put("htmlBuilder", htmlBuilder);
        resultMap.put("rowTypeCount", rowTypeCount + "");
        return resultMap;
    }


    /**
     * query CustSalesReport
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryCustSalesReport(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();

        Delegator delegator = dispatcher.getDelegator();

        Locale locale = (Locale) context.get("locale");

        Map<String, Object> resultMap = ServiceUtil.returnSuccess();

        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();

        //Scope Param
        GenericValue userLogin = (GenericValue) context.get("userLogin");

        String partyId = (String) userLogin.get("partyId");


        String productId = (String) context.get("productId");


        List<GenericValue> custList = EntityQuery.use(delegator).from("ProductRole").where("roleTypeId", PeConstant.PRODUCT_CUSTOMER, "productId", productId).queryList();
        List<GenericValue> placingCustList = EntityQuery.use(delegator).from("ProductRole").where("roleTypeId", "PLACING_CUSTOMER", "productId", productId).queryList();
        List<GenericValue> visitorList = EntityQuery.use(delegator).from("ProductRole").where("roleTypeId", "VISITOR", "productId", productId).queryList();
        List<GenericValue> partnerList = EntityQuery.use(delegator).from("ProductRole").where("roleTypeId", "PARTNER", "productId", productId).queryList();

        // 客户、 潜在客户、 访问者 、 合作伙伴(转发者)
        resultMap.put("custList", custList.size() > 0 ? forEeachCustList(custList, delegator) : new ArrayList<Map<String, String>>());
        resultMap.put("placingCustList", placingCustList.size() > 0 ? forEeachCustList(placingCustList, delegator) : new ArrayList<Map<String, String>>());
        resultMap.put("visitorList", visitorList.size() > 0 ? forEeachCustList(visitorList, delegator) : new ArrayList<Map<String, String>>());
        resultMap.put("partnerList", partnerList.size() > 0 ? forEeachCustList(partnerList, delegator) : new ArrayList<Map<String, String>>());


        return resultMap;
    }

    private static List<Map<String, String>> forEeachCustList(List<GenericValue> custList, Delegator delegator) throws GenericEntityException, GenericServiceException {
        List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();

        for (GenericValue gv : custList) {
            String partyId = (String) gv.get("partyId");

            Map<String, String> userInfoMap = queryPersonBaseInfo(delegator, partyId);

            returnList.add(userInfoMap);
        }

        return returnList;
    }


    /**
     * Query ConsumerInfo
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryConsumerInfo(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();

        Delegator delegator = dispatcher.getDelegator();

        Locale locale = (Locale) context.get("locale");

        Map<String, Object> resultMap = ServiceUtil.returnSuccess();

        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();

        //Scope Param
        GenericValue userLogin = (GenericValue) context.get("userLogin");

        String partyId = (String) userLogin.get("partyId");

        String realPartyId = (String) context.get("realPartyId");

        String productId = (String) context.get("productId");

        resultMap.put("realPartyId", realPartyId);


        String relationStr = "";

        List<GenericValue> partyRelationship = EntityQuery.use(delegator).from("PartyRelationship").where("partyIdTo", realPartyId, "partyIdFrom", partyId).queryList();

        if (partyRelationship != null && partyRelationship.size() > 0) {
//            for(int index = 0 ; index < partyRelationship.size(); index++ ){
//                 GenericValue gv = partyRelationship.get(index);
//                 String relation = (String) gv.get("partyRelationshipTypeId");
//                 relationStr += UtilProperties.getMessage(resourceUiLabels,relation, locale)+",";
//            }
            relationStr = "客户";
        } else {
            relationStr = "潜在客户";
        }


        Set<String> fieldSet = new HashSet<String>();
        fieldSet.add("productId");
        fieldSet.add("productName");
        fieldSet.add("productStoreId");
        fieldSet.add("createdDate");
        fieldSet.add("salesDiscontinuationDate");
        fieldSet.add("price");
        fieldSet.add("detailImageUrl");
        fieldSet.add("prodCatalogId");
        fieldSet.add("payToPartyId");
        fieldSet.add("description");

        EntityCondition findConditions = EntityCondition
                .makeCondition(UtilMisc.toMap("productId", productId));

        GenericValue product = delegator.findList("ProductAndCategoryMember",
                findConditions, fieldSet,
                null, null, false).get(0);
        Map<String, Object> resourceDetail = product.getAllFields();

//        List<GenericValue> distributingLeafletsList = EntityQuery.use(delegator).from(
//                "DistributingLeaflets").where("sellerPartyId", (String) userLogin.get("partyId"), "buyerPartyId", realPartyId, "productId", productId).queryList();
//        if (null != distributingLeafletsList && distributingLeafletsList.size() > 0) {
//            resultMap.put("distributingLeaflets", distributingLeafletsList.get(distributingLeafletsList.size() - 1).getAllFields());
//        } else {
//            resultMap.put("distributingLeaflets", new HashMap<String, Object>());
//        }

        resultMap.put("partyRelation", relationStr);
        resultMap.put("resourceDetail", resourceDetail);

//        resultMap.put("queryConsumerInfoList",returnList);

        return resultMap;
    }


    /**
     * Query PostalAddress
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryPostalAddress(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();


        //Scope Param
        GenericValue userLogin = (GenericValue) context.get("userLogin");

        String partyId = (String) userLogin.get("partyId");


        Set<String> fieldSet = new HashSet<String>();
        fieldSet.add("contactMechId");
        fieldSet.add("partyId");
        fieldSet.add("address1");
        fieldSet.add("postalCode");
        fieldSet.add("toName");
        fieldSet.add("comments");
        EntityCondition findConditions = EntityCondition.makeCondition(UtilMisc.toMap("partyId", partyId));

        EntityCondition findConditions2 = EntityCondition.makeCondition("thruDate", EntityOperator.EQUALS, GenericEntity.NULL_FIELD);


        EntityConditionList<EntityCondition> listConditions = EntityCondition
                .makeCondition(findConditions, findConditions2);

        //Query My Resource
        List<GenericValue> queryAddressList = delegator.findList("PartyAndPostalAddress",
                listConditions, fieldSet,
                UtilMisc.toList("-fromDate"), null, false);

        resultMap.put("postalAddress", queryAddressList);

        //查询卖家提供的联系电话
//        GenericValue telecomNumber = EntityUtil.getFirst(
//                EntityQuery.use(delegator).from("TelecomNumberAndPartyView").where(UtilMisc.toMap("partyId", partyId, "contactMechPurposeTypeId", "PHONE_MOBILE", "contactMechTypeId", "TELECOM_NUMBER")).queryList());
//        if (UtilValidate.isNotEmpty(telecomNumber)) {
//            resultMap.put("teleNumber",telecomNumber.get("contactNumber") );
//        }else{
//            resultMap.put("teleNumber", "");
//        }

        return resultMap;
    }


    /**
     * Query ProductRole
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryProductRole(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();


        //Scope Param
        GenericValue userLogin = (GenericValue) context.get("userLogin");

        String partyId = (String) userLogin.get("partyId");

        String productId = (String) context.get("productId");

        GenericValue partyMarkRole = EntityQuery.use(delegator).from("ProductRole").where("partyId", partyId, "productId", productId, "roleTypeId", "PLACING_CUSTOMER").queryFirst();


        if (null != partyMarkRole) {
            resultMap.put("mark", "true");
        }

        return resultMap;
    }


    /**
     * Query LocalRoster
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryLocalRoster(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
        StringBuffer rosterBuffer = new StringBuffer();

        //Scope Param
        GenericValue userLogin = (GenericValue) context.get("userLogin");

        String partyId = (String) userLogin.get("partyId");


        // Default 2 Dimension
        rosterBuffer = queryDimensionRelationShip(partyId, delegator);


//        GZIP.compress()
        try {
            resultMap.put("roster", rosterBuffer.toString());
        } catch (Exception e) {

        }
        return resultMap;
    }

    /**
     * Query PartyRelationShip
     *
     * @param partyId
     * @param delegator
     * @return
     */
    private static StringBuffer queryDimensionRelationShip(String partyId, Delegator delegator) throws GenericEntityException, GenericServiceException {

        StringBuffer rosterBuffer = new StringBuffer();

        Set<String> fieldSet = new HashSet<String>();

        fieldSet.add("partyIdTo");


        EntityCondition findConditions = EntityCondition
                .makeCondition(UtilMisc.toMap("partyIdFrom", partyId));


        EntityCondition findConditions2 = EntityCondition
                .makeCondition(UtilMisc.toMap("partyRelationshipTypeId", PeConstant.CONTACT));

        EntityConditionList<EntityCondition> listConditions = EntityCondition
                .makeCondition(findConditions, findConditions2);

        // 1D
        List<GenericValue> myRelationListList = delegator.findList("PartyRelationship",
                listConditions, fieldSet,
                null, null, false);


        Map<String, Object> dimensionMap = new HashMap<String, Object>();


        for (int i = 0; i < myRelationListList.size(); i++) {
            GenericValue gv = myRelationListList.get(i);
            if (i + 1 == myRelationListList.size()) {
                rosterBuffer.append((String) gv.get("partyIdTo"));
            } else {
                rosterBuffer.append((String) gv.get("partyIdTo") + ",");
            }

            dimensionMap.put(gv.get("partyIdTo") + "", "");
        }


        rosterBuffer.append("。");
        // 1D OVER

        rosterBuffer.append(forQueryDimensionRelationShip(myRelationListList, delegator, dimensionMap, partyId));
        // 2D 3D OVER

        return rosterBuffer;
    }

    /**
     * ForEach Dimension
     *
     * @param myRelationListList
     * @param delegator
     * @return
     */
    private static String forQueryDimensionRelationShip(List<GenericValue> relationList, Delegator delegator, Map<String, Object> dimensionMap, String userPartyId) throws GenericEntityException, GenericServiceException {

        Map<String, Object> twoDimensionMap = new HashMap<String, Object>();

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < relationList.size(); i++) {
            GenericValue gv = relationList.get(i);
            String partyIdTo = (String) gv.get("partyIdTo");


            Set<String> fieldSet = new HashSet<String>();

            fieldSet.add("partyIdTo");


            EntityCondition findConditions = EntityCondition
                    .makeCondition(UtilMisc.toMap("partyIdFrom", partyIdTo));


            EntityCondition findConditions2 = EntityCondition
                    .makeCondition(UtilMisc.toMap("partyRelationshipTypeId", PeConstant.CONTACT));

            EntityConditionList<EntityCondition> listConditions = EntityCondition
                    .makeCondition(findConditions, findConditions2);

            // 1D
            List<GenericValue> myRelationListList = delegator.findList("PartyRelationship",
                    listConditions, fieldSet,
                    null, null, false);


            for (int y = 0; y < myRelationListList.size(); y++) {
                GenericValue gv2 = myRelationListList.get(y);
                String partyIdToTo = (String) gv2.get("partyIdTo");

                if (i + 1 == relationList.size() && y + 1 == myRelationListList.size()) {

                    if (!dimensionMap.containsKey(partyIdToTo) && !userPartyId.equals(partyIdToTo) && !twoDimensionMap.containsKey(partyIdToTo)) {
                        sb.append(partyIdToTo);
                        twoDimensionMap.put(partyIdToTo, "");
                    }


                } else {
                    if (!dimensionMap.containsKey(partyIdToTo) && !userPartyId.equals(partyIdToTo) && !twoDimensionMap.containsKey(partyIdToTo)) {
                        sb.append(partyIdToTo + ",");
                        twoDimensionMap.put(partyIdToTo, "");
                    }
                }


            }
        }


        return sb.toString();
    }


    /**
     * Query More Resource
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryMoreResource(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();


        //Scope Param
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = (String) userLogin.get("partyId");
        String productId = (String) context.get("productId");
        String productName = (String) context.get("productName");
        String roster = (String) context.get("roster");

//        if(UtilValidate.isNotEmpty(roster)){
//            try {
//                roster = GZIP.unCompress(roster);
//            }catch (Exception e) {
//               //TODO Add Catch
//            }
//        }

        System.out.println("roster = " + roster);

        String[] rosterArray = roster.split("。");

        List<String> rosters = doSplitRoster(rosterArray);


        Set<String> fieldSet = new HashSet<String>();
        fieldSet.add("productId");
        fieldSet.add("productStoreId");
        fieldSet.add("productName");
        fieldSet.add("detailImageUrl");
        fieldSet.add("createdDate");
        fieldSet.add("price");
        fieldSet.add("productCategoryId");
        fieldSet.add("payToPartyId");


        EntityCondition findConditions = EntityCondition
                .makeCondition("productName", EntityOperator.LIKE, productName);

        EntityCondition findConditions2 = EntityCondition
                .makeCondition("payToPartyId", EntityOperator.IN, rosters);

        EntityCondition findConditions3 = EntityCondition
                .makeCondition("salesDiscontinuationDate", EntityOperator.EQUALS, GenericEntity.NULL_FIELD);

        EntityConditionList<EntityCondition> listConditions = EntityCondition
                .makeCondition(findConditions, findConditions2, findConditions3);


        if (UtilValidate.isNotEmpty(productId)) {
            findConditions = EntityCondition
                    .makeCondition(UtilMisc.toMap("productId", productId));
        }


        //Query My Resource
        List<GenericValue> queryResourceList = delegator.findList("ProductAndCategoryMember",
                listConditions, fieldSet,
                UtilMisc.toList("-createdDate"), null, false);


        if (null != queryResourceList && queryResourceList.size() > 0) {
            for (GenericValue gv : queryResourceList) {
                Map<String, Object> rowMap = new HashMap<String, Object>();
                rowMap = gv.getAllFields();
                String payToPartyId = (String) gv.get("payToPartyId");
                GenericValue person = delegator.findOne("Person", UtilMisc.toMap("partyId", payToPartyId), false);
                if (person != null) {
                    rowMap.put("firstName", (String) person.get("firstName"));
                }
                returnList.add(rowMap);
            }
        }


        resultMap.put("resourceList", returnList);

        return resultMap;
    }

    /**
     * 循环分割名单
     *
     * @param rosterArray
     * @return
     */
    private static List<String> doSplitRoster(String[] rosterArray) {
        List<String> returnList = new ArrayList<String>();

        String[] oneArray = rosterArray[0].split(",");


        for (int i = 0; i < oneArray.length; i++) {
            String partyId = oneArray[i];
            returnList.add(partyId);
        }
        if (rosterArray.length > 1 && rosterArray[1].indexOf(",") > 0) {
            String[] twoArray = rosterArray[1].split(",");
            for (int i = 0; i < twoArray.length; i++) {
                String partyId = twoArray[i];
                returnList.add(partyId);
            }
        }


        return returnList;
    }


    /**
     * Ajax Query Resource
     *
     * @param request
     * @param response
     * @return
     * @throws GenericServiceException
     * @throws GenericEntityException
     */
    public static String ajaxQueryResource(HttpServletRequest request, HttpServletResponse response)
            throws GenericServiceException, GenericEntityException {

        // Servlet Head


        Delegator delegator = (Delegator) request.getAttribute("delegator");

        String productName = (String) request.getParameter("productName");


        Set<String> fieldSet = new HashSet<String>();
        fieldSet.add("productId");
        fieldSet.add("productName");


        EntityCondition findConditions = EntityCondition
                .makeCondition("productName", EntityOperator.LIKE, "%" + productName + "%");


        //Query My Resource
        List<GenericValue> queryResourceList = delegator.findList("Product",
                findConditions, fieldSet,
                UtilMisc.toList("-createdStamp"), null, true);


        request.setAttribute("queryResourceList", queryResourceList);

        return "success";
    }


    /**
     * Query MyRelationShip
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryMyRelationShip(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();


        //Scope Param
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = (String) userLogin.get("partyId");

        List<Map<String, Object>> relationList = new ArrayList<Map<String, Object>>();

        Map<String, Object> relationMap = new HashMap<String, Object>();


        Set<String> fieldSet = new HashSet<String>();
        fieldSet.add("partyIdFrom");
        fieldSet.add("partyIdTo");
        fieldSet.add("roleTypeIdFrom");
        fieldSet.add("roleTypeIdTo");
        fieldSet.add("partyRelationshipTypeId");


        EntityCondition findConditions = EntityCondition
                .makeCondition(UtilMisc.toMap("partyIdFrom", partyId));


        //Query My Resource
        List<GenericValue> queryMyRelationListList = delegator.findList("PartyRelationship",
                findConditions, fieldSet,
                UtilMisc.toList("-createdStamp"), null, false);


        if (null != queryMyRelationListList && queryMyRelationListList.size() > 0) {

            for (GenericValue gv : queryMyRelationListList) {
                Map<String, Object> rowMap = new HashMap<String, Object>();
                rowMap = gv.getAllFields();
                String partyIdTo = (String) gv.get("partyIdTo");
                String partyRelationshipTypeId = (String) gv.get("partyRelationshipTypeId");


                if (!relationMap.containsKey(partyIdTo)) {

                    relationMap.put(partyIdTo, "");
                    GenericValue person = delegator.findOne("Person", UtilMisc.toMap("partyId", partyIdTo), false);
                    rowMap.put("firstName", (String) person.get("firstName"));
                    List<GenericValue> contentsList =
                            EntityQuery.use(delegator).from("PartyContentAndDataResource").
                                    where("partyId", partyIdTo, "partyContentTypeId", "LGOIMGURL").orderBy("-fromDate").queryPagedList(0, 999999).getData();
                    GenericValue findPartyContent = null;
                    if (null != contentsList && contentsList.size() > 0) {
                        findPartyContent = contentsList.get(0);
                    }

                    if (null != findPartyContent && null != findPartyContent.get("objectInfo")) {
                        rowMap.put("headPortrait",
                                (String) findPartyContent.get("objectInfo"));
                    } else {
                        rowMap.put("headPortrait",
                                "https://personerp.oss-cn-hangzhou.aliyuncs.com/datas/images/defaultHead.png");
                    }

                    rowMap.put("partyRelationshipTypeId", UtilProperties.getMessage("PersonManagerUiLabels.xml", partyRelationshipTypeId, locale));
                    relationList.add(rowMap);
                }
            }
        }

        resultMap.put("relationList", relationList);

        return resultMap;
    }


    /**
     * Query MyOrders Detail
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryMyOrdersDetail(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();

        Delegator delegator = dispatcher.getDelegator();

        Locale locale = (Locale) context.get("locale");

        Map<String, Object> resultMap = ServiceUtil.returnSuccess();

        //Scope Param
        GenericValue userLogin = (GenericValue) context.get("userLogin");

        String partyId = (String) userLogin.get("partyId");

        String orderId = (String) context.get("orderId");

        //Order BaseInfo
        Map<String, Object> rowMap = new HashMap<String, Object>();
        //Express Info
        Map<String, Object> orderExpressInfo = new HashMap<String, Object>();

        GenericValue orderHeaderItemAndRoles = EntityQuery.use(delegator).from("OrderHeaderItemAndRoles").where("orderId", orderId).queryFirst();

        GenericValue custOrderRole = EntityQuery.use(delegator).from("OrderRole").where("orderId", orderId, "roleTypeId", "SHIP_TO_CUSTOMER").queryFirst();

        String custPartyId = (String) custOrderRole.get("partyId");

        if (null != orderHeaderItemAndRoles) {

            rowMap = orderHeaderItemAndRoles.getAllFields();

            GenericValue orderPaymentPrefAndPayment = EntityQuery.use(delegator).from("OrderPaymentPreference").where("orderId", orderId).queryFirst();


            Debug.logInfo("===================================> orderPaymentPrefAndPayment=" + orderPaymentPrefAndPayment, module);

            if (null != orderPaymentPrefAndPayment) {


                rowMap.put("orderPayStatus", "已收款");


            } else {

                rowMap.put("orderPayStatus", "未付款");


            }

            String productStoreId = (String) orderHeaderItemAndRoles.get("productStoreId");

            String productId = (String) orderHeaderItemAndRoles.get("productId");

            GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), false);

            GenericValue product = delegator.findOne("Product", UtilMisc.toMap("productId", productId), false);

            rowMap.put("productName", "" + product.get("productName"));

            rowMap.put("detailImageUrl", (String) product.get("detailImageUrl"));

            String payToPartyId = (String) productStore.get("payToPartyId");

            rowMap.put("payToPartyId", payToPartyId);


            Map<String, String> personInfoMap = null;

            Map<String, String> personAddressInfoMap = null;


            personInfoMap = queryPersonBaseInfo(delegator, custPartyId);

            personAddressInfoMap = queryPersonAddressInfoFromOrder(delegator, partyId, orderId);

            rowMap.put("custPersonInfoMap", personInfoMap);


//            GenericValue orderHeaderAndShipGroups =EntityQuery.use(delegator).from("OrderHeaderAndShipGroups").
//                    where("orderId", orderId).queryFirst();
//            if(null!=orderHeaderAndShipGroups && orderHeaderAndShipGroups.get("internalCode")!=null ){  }

            GenericValue orderShipment = EntityQuery.use(delegator).from("OrderShipment").where("orderId", orderId).queryFirst();
            GenericValue orderItemShip = EntityQuery.use(delegator).from("OrderItemShipGroup").where("orderId", orderId).queryFirst();
            //理论上有这行数据,就肯定货运了
            if (null != orderShipment) {
                rowMap.put("orderShipment", "待收货");
                String trackingNumber = (String) orderItemShip.get("trackingNumber");
                //说明是快递发货
                if (null != trackingNumber) {
                    rowMap.put("internalCode", trackingNumber);
                } else {
                    rowMap.put("internalCode", "商家自配送");
                }
                if (rowMap.get("orderPayStatus").equals("已付款")) {
                    rowMap.put("orderCompleted", "已完成");
                }
            } else {
                rowMap.put("orderShipment", "待发货");
            }

//            String internalCode = (String)orderHeaderAndShipGroups.get("internalCode");
//            if(internalCode.equals("卖家自配送")){
//                rowMap.put("internalCode", internalCode);
//            }else{
//                rowMap.put("internalCode", "快递:"+internalCode);
//            }
//                rowMap.put("personAddressInfoMap", orderHeaderAndShipGroups);
//            }else{
//                rowMap.put("internalCode", "未发货");
//            }


            rowMap.put("salesPersonInfoMap", queryPersonBaseInfo(delegator, payToPartyId));
        }
        GenericValue orderNote = EntityQuery.use(delegator).from("OrderHeaderNoteView").
                where("orderId", orderId).queryFirst();
        rowMap.put("orderNote", orderNote);

//        GenericValue order = delegator.findOne("OrderHeader", UtilMisc.toMap("orderId", orderId), false);
//        String stuatusId = (String)order.get("statusId");
//        if(!stuatusId.equals("ORDER_SENT")){
//            rowMap.put("orderShipment","未发货");
//        }else{
//            rowMap.put("orderShipment","已发货");
//            if(rowMap.get("orderPayStatus").equals("已收款")){
//                rowMap.put("orderCompleted","已完成");
//            }
//        }


        rowMap.put("custPartyId", custPartyId);
        resultMap.put("orderInfo", rowMap);
        resultMap.put("orderExpressInfo", null);


        return resultMap;
    }


    /**
     * Query MyOrders
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryMyOrders(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        String orderStatusId = (String) context.get("orderStatus");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();


        //Scope Param
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = (String) userLogin.get("partyId");
        List<Map<String, Object>> orderList = new ArrayList<Map<String, Object>>();

        System.out.println("partyId ==  " + partyId);


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
                .makeCondition("roleTypeId", EntityOperator.EQUALS, "BILL_TO_CUSTOMER");

        EntityCondition findConditions = EntityCondition
                .makeCondition("partyId", EntityOperator.EQUALS, partyId);


        System.out.println("orderStatusId  ==   ? " + orderStatusId);
        EntityCondition listConditions2 = null;
        EntityCondition listConditions3 = null;
        String isCancelled = (String) context.get("isCancelled");

        //如果isCancelled 为1  则查询取消的订单。
        if (!UtilValidate.isEmpty(isCancelled) && isCancelled.equals("1")) {
            EntityCondition statusConditions = EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, "ORDER_CANCELLED");
            EntityCondition genericCondition = EntityCondition.makeCondition(findConditions3, EntityOperator.AND, findConditions);
            listConditions2 = EntityCondition.makeCondition(genericCondition, EntityOperator.AND, statusConditions);
        } else {
            if (null != orderStatusId && orderStatusId.equals("SHIPMENT")) {
                EntityCondition orderStatusCondition = EntityCondition.makeCondition(UtilMisc.toMap("statusId", "ORDER_COMPLETED"));
                listConditions3 = EntityCondition
                        .makeCondition(listConditions2, EntityOperator.AND, orderStatusCondition);
            } else {
                listConditions2 = EntityCondition.makeCondition(findConditions3, EntityOperator.AND, findConditions);
            }

        }


        List<GenericValue> queryMyResourceOrderList = null;

        System.out.println("list condition3 == null ? " + (listConditions3 != null));

        if (listConditions3 != null) {
            //说明查已发货的
            queryMyResourceOrderList = delegator.findList("OrderHeaderItemAndRoles",
                    listConditions3, fieldSet,
                    UtilMisc.toList("-orderDate"), null, false);
        } else {
            System.out.println("in else ==  ");
            queryMyResourceOrderList = delegator.findList("OrderHeaderItemAndRoles",
                    listConditions2, fieldSet,
                    UtilMisc.toList("-orderDate"), null, false);
        }
        System.out.println("queryMyResourceOrderList ==  " + queryMyResourceOrderList);


        if (null != queryMyResourceOrderList && queryMyResourceOrderList.size() > 0) {

            for (GenericValue gv : queryMyResourceOrderList) {

                Map<String, Object> rowMap = new HashMap<String, Object>();

                rowMap = gv.getAllFields();


                String productStoreId = (String) gv.get("productStoreId");

                String productId = (String) gv.get("productId");

                GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), false);

                GenericValue product = delegator.findOne("Product", UtilMisc.toMap("productId", productId), false);

                rowMap.put("productName", "" + product.get("productName"));

                rowMap.put("detailImageUrl", (String) product.get("detailImageUrl"));

                String payToPartyId = (String) productStore.get("payToPartyId");

                rowMap.put("payToPartyId", payToPartyId);

                // 查询卖家付款二维码。
                GenericValue wxPayQrCodes =
                        EntityQuery.use(delegator).from("PartyContentAndDataResource").
                                where("partyId", payToPartyId, "partyContentTypeId", "WECHATQRCODE").orderBy("-fromDate").queryFirst();

                if (null != wxPayQrCodes) {
                    rowMap.put("weChatPayQrCode", wxPayQrCodes.getString("objectInfo"));
                }


                String statusId = (String) gv.get("statusId");

                //区分订单状态
                if (statusId.toLowerCase().indexOf("comp") > 0) {
                    rowMap.put("orderStatusCode", "1");
                } else {
                    rowMap.put("orderStatusCode", "0");
                }
                System.out.println("orderStatusCode = " + rowMap.get("orderStatusCode"));

                rowMap.put("statusId", UtilProperties.getMessage("PersonManagerUiLabels.xml", statusId, locale));


                String payFromPartyId = (String) rowMap.get("partyId");

                Map<String, String> personInfoMap = null;

                Map<String, String> personAddressInfoMap = null;

                //说明这笔订单我是卖家,查买家头像信息
                //2018-03-21 老金需要不管是销单还是采单  都要双方信息
//                if (payToPartyId.equals(partyId)) {
//
//                    personInfoMap = queryPersonBaseInfo(delegator, payFromPartyId);
//
//                    personAddressInfoMap = queryPersonAddressInfo(delegator, payFromPartyId);
//
//                    rowMap.put("realPartyId", payFromPartyId);
//
//                }
//                //说明这笔单我是买家,查卖家头像信息
//                if (!payToPartyId.equals(partyId)) {
//
//                    personInfoMap = queryPersonBaseInfo(delegator, payToPartyId);
//
//                    personAddressInfoMap = queryPersonAddressInfo(delegator, payToPartyId);
//
//                    rowMap.put("realPartyId", payToPartyId);
//                }


                rowMap.put("salesPersonInfoMap", queryPersonBaseInfo(delegator, payToPartyId));
                rowMap.put("custPersonInfoMap", queryPersonBaseInfo(delegator, payFromPartyId));
                if (payToPartyId.equals(partyId)) {
                    personAddressInfoMap = queryPersonAddressInfo(delegator, payFromPartyId);
                }
                if (!payToPartyId.equals(partyId)) {
                    personAddressInfoMap = queryPersonAddressInfo(delegator, payToPartyId);
                }


                rowMap.put("custPartyId", payFromPartyId);
                rowMap.put("salesPartyId", payToPartyId);

                rowMap.put("userPartyId", partyId);

                rowMap.put("personInfoMap", personInfoMap);

                rowMap.put("personAddressInfoMap", personAddressInfoMap);


                GenericValue orderPaymentPrefAndPayment = EntityQuery.use(delegator).from("OrderPaymentPreference").where("orderId", gv.get("orderId")).orderBy("-createdStamp").queryFirst();

                //     GenericValue payment = EntityQuery.use(delegator).from("Payment").where("partyIdTo", payToPartyId, "partyIdFrom", payFromPartyId, "comments", rowMap.get("orderId")).queryFirst();

                if (null != orderPaymentPrefAndPayment) {

                    String orderPaymentPrefAndPaymentstatusId = (String) orderPaymentPrefAndPayment.get("statusId");

                    if (orderPaymentPrefAndPaymentstatusId.equals("PAYMENT_RECEIVED")) {
                        rowMap.put("orderPayStatus", "已收款");
                        rowMap.put("payStatusCode", "1");
                    } else {
                        rowMap.put("payStatusCode", "0");
                        rowMap.put("orderPayStatus", "未付款");
                    }
                } else {
                    rowMap.put("payStatusCode", "0");
                    rowMap.put("orderPayStatus", "未付款");

                }
                GenericValue orderShipment = EntityQuery.use(delegator).from("OrderShipment").where("orderId", gv.get("orderId")).queryFirst();
                GenericValue orderItemShip = EntityQuery.use(delegator).from("OrderItemShipGroup").where("orderId", gv.get("orderId")).queryFirst();
                //理论上有这行数据,就肯定货运了
                if (null != orderShipment) {
                    rowMap.put("orderShipment", "待收货");
                    String trackingNumber = (String) orderItemShip.get("trackingNumber");
                    //说明是快递发货
                    if (null != trackingNumber) {
                        rowMap.put("internalCode", "" + trackingNumber);
                    } else {
                        rowMap.put("internalCode", "商家自配送");
                    }
                    if (rowMap.get("orderPayStatus").equals("已付款")) {
                        rowMap.put("orderCompleted", "已完成");
                    }
                } else {
                    rowMap.put("orderShipment", "代发货");
                }
//                if(!statusId.equals("ORDER_SENT")){
//                    rowMap.put("orderShipment","未发货");
//                }else{
//                    rowMap.put("orderShipment","已发货");
//                    if(rowMap.get("orderPayStatus").equals("已收款")){
//                        rowMap.put("orderCompleted","已完成");
//                    }
//                }

                //不查询已收款的订单时,直接放入
                if (null != orderStatusId && !orderStatusId.equals("PAYMENT")) {
                    orderList.add(rowMap);
                }
                if (null != orderStatusId && orderStatusId.equals("PAYMENT")) {
                    if (!rowMap.get("orderPayStatus").equals("未付款")) {
                        orderList.add(rowMap);
                    }
                }
                if (UtilValidate.isEmpty(orderStatusId)) {
                    orderList.add(rowMap);
                }

            }
        }


        resultMap.put("orderList", orderList);

        return resultMap;
    }


    /**
     * Query MyResource Order 销售单
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryMyResourceOrder(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();


        //Scope Param
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = (String) userLogin.get("partyId");

        List<Map<String, Object>> myResourceOrderList = new ArrayList<Map<String, Object>>();


        //是否是从App端的查询
        String area = (String) context.get("area");


        String orderStatus = (String) context.get("orderStatus");


        Set<String> fieldSet = new HashSet<String>();
        fieldSet.add("orderId");
        fieldSet.add("partyId");
        fieldSet.add("statusId");
        fieldSet.add("currencyUom");
        fieldSet.add("grandTotal");
        fieldSet.add("productId");
        fieldSet.add("quantity");
        fieldSet.add("unitPrice");
        fieldSet.add("internalCode");

        fieldSet.add("roleTypeId");
        fieldSet.add("orderDate");
        fieldSet.add("productStoreId");
        fieldSet.add("payToPartyId");


        EntityCondition roleTypeCondition = EntityCondition
                .makeCondition(UtilMisc.toMap("roleTypeId", "BILL_FROM_VENDOR"));

        EntityCondition payToPartyIdCondition = EntityCondition
                .makeCondition(UtilMisc.toMap("payToPartyId", partyId));


        EntityCondition listConditions2 = null;

        String isCancelled = (String) context.get("isCancelled");
        //如果isCancelled 为1  则查询取消的订单。
        if (!UtilValidate.isEmpty(isCancelled) && isCancelled.equals("1")) {
            EntityCondition statusConditions = EntityCondition.makeCondition("statusId", EntityOperator.EQUALS, "ORDER_CANCELLED");
            EntityCondition genericCondition = EntityCondition.makeCondition(roleTypeCondition, EntityOperator.AND, payToPartyIdCondition);
            listConditions2 = EntityCondition.makeCondition(genericCondition, EntityOperator.AND, statusConditions);
        } else {
            EntityCondition statusConditions = EntityCondition.makeCondition("statusId", EntityOperator.NOT_EQUAL, "ORDER_CANCELLED");
            EntityCondition genericCondition = EntityCondition.makeCondition(roleTypeCondition, EntityOperator.AND, payToPartyIdCondition);
            listConditions2 = EntityCondition.makeCondition(genericCondition, EntityOperator.AND, statusConditions);
        }

//        EntityCondition listConditions2 = EntityCondition
//                .makeCondition(roleTypeCondition, EntityOperator.AND, payToPartyIdCondition);


        List<GenericValue> queryMyResourceOrderList = null;

        if (null != orderStatus && orderStatus.equals("SHIPMENT")) {

            EntityCondition orderStatusCondition = EntityCondition.makeCondition(UtilMisc.toMap("statusId", "ORDER_COMPLETED"));

            EntityCondition listConditions3 = EntityCondition
                    .makeCondition(listConditions2, EntityOperator.AND, orderStatusCondition);

            queryMyResourceOrderList = delegator.findList("OrderHeaderItemAndRoles",
                    listConditions3, fieldSet,
                    UtilMisc.toList("-orderDate"), null, false);

        } else {
            queryMyResourceOrderList = delegator.findList("OrderHeaderItemAndRoles",
                    listConditions2, fieldSet,
                    UtilMisc.toList("-orderDate"), null, false);

        }


        if (null != queryMyResourceOrderList && queryMyResourceOrderList.size() > 0) {

            for (GenericValue gv : queryMyResourceOrderList) {

                Map<String, Object> rowMap = new HashMap<String, Object>();
                rowMap = gv.getAllFields();


                String productStoreId = (String) gv.get("productStoreId");
                String productId = (String) gv.get("productId");

                GenericValue productStore = delegator.findOne("ProductStore", UtilMisc.toMap("productStoreId", productStoreId), false);
                GenericValue product = delegator.findOne("Product", UtilMisc.toMap("productId", productId), false);
                rowMap.put("productName", "" + product.get("productName"));
                rowMap.put("detailImageUrl", (String) product.get("detailImageUrl"));
                String payToPartyId = (String) productStore.get("payToPartyId");
                if (null != area && area.equals("app") && !payToPartyId.equals(partyId)) {
                    continue;
                }

                rowMap.put("payToPartyId", payToPartyId);
                String statusId = (String) gv.get("statusId");


                rowMap.put("statusId", UtilProperties.getMessage("PersonManagerUiLabels.xml", statusId, locale));


                //有物流信息
//                if(statusId.equals("ORDER_COMPLETED")){
//                    Map<String,Object> queryExpressInfoMap = dispatcher.runSync("queryExpressInfo",UtilMisc.toMap("userLogin",userLogin,"code",rowMap.get("internalCode")));
//                    List<JSONObject> expressInfos = null;
//                    if (ServiceUtil.isSuccess(queryExpressInfoMap)) {
//                        expressInfos = (List<JSONObject>) queryExpressInfoMap.get("expressInfos");
//                        rowMap.put("expressInfos",expressInfos);
//                    }
//                }
                String payFromPartyId = (String) rowMap.get("partyId");

                Map<String, String> personInfoMap = null;
                Map<String, String> personAddressInfoMap = null;

                //说明这笔订单我是卖家,查买家头像信息
                //   if(payToPartyId.equals(partyId)){   }
                GenericValue custOrderInfo = EntityQuery.use(delegator).from("OrderHeaderItemAndRoles").where("orderId", rowMap.get("orderId"), "roleTypeId", "SHIP_TO_CUSTOMER").queryFirst();

                personInfoMap = queryPersonBaseInfo(delegator, (String) custOrderInfo.get("partyId"));
                personAddressInfoMap = queryPersonAddressInfo(delegator, (String) custOrderInfo.get("partyId"));
                rowMap.put("realPartyId", custOrderInfo.get("partyId"));

                //说明这笔单我是买家,查卖家头像信息
//                if(!payToPartyId.equals(partyId)){
//                    personInfoMap = queryPersonBaseInfo(delegator,payToPartyId);
//                    personAddressInfoMap = queryPersonAddressInfo(delegator,payToPartyId);
//                    rowMap.put("realPartyId",payToPartyId);
//                }
                rowMap.put("userPartyId", partyId);

                rowMap.put("custPersonInfoMap", personInfoMap);
                rowMap.put("salesPersonInfoMap", queryPersonBaseInfo(delegator, payToPartyId));

                rowMap.put("personAddressInfoMap", personAddressInfoMap);


                System.out.println("orderId=" + gv.get("orderId"));
                System.out.println("payToPartyId=" + payToPartyId);
                System.out.println("payFromPartyId=" + (String) custOrderInfo.get("partyId"));

                GenericValue orderPaymentPrefAndPayment = EntityQuery.use(delegator).from("OrderPaymentPreference").where("orderId", gv.get("orderId")).queryFirst();


                GenericValue payment = EntityQuery.use(delegator).from("Payment").where("partyIdTo", payToPartyId, "partyIdFrom", (String) custOrderInfo.get("partyId"), "comments", gv.get("orderId")).queryFirst();

                System.out.println("=============================================================");
                System.out.println("orderPaymentPrefAndPayme=" + orderPaymentPrefAndPayment);
                System.out.println("payment=" + payment);
                System.out.println("=============================================================");


                if (null != orderPaymentPrefAndPayment) {

                    rowMap.put("orderPayStatus", "已收款");
                    rowMap.put("payStatusCode", "1");

                } else {
                    rowMap.put("orderPayStatus", "未付款");
                    rowMap.put("payStatusCode", "1");

                }
//                if(!statusId.equals("ORDER_SENT")){
//                    rowMap.put("orderShipment","未发货");
//                }else{
//                    rowMap.put("orderShipment","已发货");
//                    if(rowMap.get("orderPayStatus").equals("已收款")){
//                        rowMap.put("orderCompleted","已完成");
//                    }
//                }

                GenericValue orderShipment = EntityQuery.use(delegator).from("OrderShipment").where("orderId", gv.get("orderId")).queryFirst();
                GenericValue orderItemShip = EntityQuery.use(delegator).from("OrderItemShipGroup").where("orderId", gv.get("orderId")).queryFirst();
                //理论上有这行数据,就肯定货运了
                if (null != orderShipment) {
                    rowMap.put("orderShipment", "待发货");
                    String trackingNumber = (String) orderItemShip.get("trackingNumber");
                    //说明是快递发货
                    if (null != trackingNumber) {
                        rowMap.put("internalCode", trackingNumber);
                    } else {
                        rowMap.put("internalCode", "商家自配送");
                    }
                    if (rowMap.get("orderPayStatus").equals("待付款")) {
                        rowMap.put("orderCompleted", "已完成");
                    }
                } else {
                    rowMap.put("orderShipment", "待发货");
                }

                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> RowMap" + rowMap);
                //不查询已收款的订单时,直接放入
                if (null != orderStatus && !orderStatus.equals("PAYMENT")) {
                    myResourceOrderList.add(rowMap);
                }
                if (null != orderStatus && orderStatus.equals("PAYMENT")) {
                    if (!rowMap.get("orderPayStatus").equals("未付款")) {
                        myResourceOrderList.add(rowMap);
                    }
                }
            }
        }

        resultMap.put("orderStatus", orderStatus);

        resultMap.put("queryMyResourceOrderList", myResourceOrderList);

        return resultMap;
    }


    /**
     * 查询卖家地址
     *
     * @param delegator
     * @param payFromPartyId
     * @return
     */
    public static Map<String, String> queryPersonAddressInfo(Delegator delegator, String partyId) throws GenericEntityException {

        Map<String, String> personMap = new HashMap<String, String>();
        //查这笔订单中买家的收货地址

        GenericValue postalAddress = EntityUtil.getFirst(
                EntityQuery.use(delegator).from("PartyAndPostalAddress").where(UtilMisc.toMap("partyId", partyId, "contactMechTypeId", "POSTAL_ADDRESS")).queryList());
        if (UtilValidate.isNotEmpty(postalAddress))
            personMap.put("contactAddress", "" + postalAddress.get("address1"));

        return personMap;
    }

    public static Map<String, String> queryPersonAddressInfoFromOrder(Delegator delegator, String partyId, String orderId) throws GenericEntityException {

        Map<String, String> personMap = new HashMap<String, String>();


        GenericValue postalAddress = EntityUtil.
                getFirst(EntityQuery.use(delegator).from("OrderHeaderAndShipGroups").where(UtilMisc.toMap("orderId", orderId)).queryList());
        if (UtilValidate.isNotEmpty(postalAddress)) {
            personMap.put("city", "" + postalAddress.get("city"));
            personMap.put("address", "" + postalAddress.get("address1"));
            personMap.put("postalCode", "" + postalAddress.get("postalCode"));
        }
        //查这笔订单中买家的收货地址

//        GenericValue postalAddress = EntityUtil.getFirst(
//                EntityQuery.use(delegator).from("PartyAndPostalAddress").where(UtilMisc.toMap("partyId", partyId, "contactMechTypeId", "POSTAL_ADDRESS")).queryList());
//        if (UtilValidate.isNotEmpty(postalAddress))
//            personMap.put("contactAddress", "" + postalAddress.get("address1"));

        return personMap;
    }

    /**
     * QueryPersonBaseInfo
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryPersonBaseInfo(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();


        System.out.println(" payToPartyFirstName = " + (String) context.get("payToPartyFirstName"));

        resultMap.put("tarjeta", (String) context.get("tarjeta"));
        resultMap.put("productId", (String) context.get("productId"));
        resultMap.put("payToPartyId", (String) context.get("payToPartyId"));
        resultMap.put("payToPartyFirstName", (String) context.get("payToPartyFirstName"));
        resultMap.put("payToPartyHead", (String) context.get("payToPartyHead"));


        //Scope Param
        GenericValue userLogin = (GenericValue) context.get("userLogin");

        String partyId = (String) userLogin.get("partyId");

        resultMap.put("partyId", partyId);

        Map<String, String> personInfo = new HashMap<String, String>();

        personInfo.put("partyId", partyId);

        GenericValue person = delegator.findOne("Person", UtilMisc.toMap("partyId", partyId), false);

        if (person != null) {

            List<GenericValue> contentsList =
                    EntityQuery.use(delegator).from("PartyContentAndDataResource").
                            where("partyId", partyId, "partyContentTypeId", "LGOIMGURL").orderBy("-fromDate").queryPagedList(0, 999999).getData();


            GenericValue partyContent = null;
            if (null != contentsList && contentsList.size() > 0) {
                partyContent = contentsList.get(0);
            }

            if (UtilValidate.isNotEmpty(partyContent)) {
                String contentId = partyContent.getString("contentId");
                personInfo.put("headPortrait",
                        partyContent.getString("objectInfo"));
            } else {
                personInfo.put("headPortrait",
                        "https://personerp.oss-cn-hangzhou.aliyuncs.com/datas/images/defaultHead.png");
            }
            personInfo.put("firstName", (String) person.get("firstName"));


        }

        resultMap.put("personInfo", personInfo);

        return resultMap;
    }


    /**
     * QueryPersonBaseInfo-WeChat
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryPersonBaseInfoByChat(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        resultMap.put("tarjeta", (String) context.get("tarjeta"));
        resultMap.put("productId", (String) context.get("productId"));
        resultMap.put("payToPartyId", (String) context.get("payToPartyId"));
        resultMap.put("payToPartyFirstName", (String) context.get("payToPartyFirstName"));
        resultMap.put("payToPartyHead", (String) context.get("payToPartyHead"));


        //Scope Param
        GenericValue userLogin = (GenericValue) context.get("userLogin");

        String partyId = (String) userLogin.get("partyId");

        resultMap.put("partyId", partyId);

        Map<String, String> personInfo = new HashMap<String, String>();

        personInfo.put("partyId", partyId);

        GenericValue person = delegator.findOne("Person", UtilMisc.toMap("partyId", partyId), false);

        if (person != null) {

            List<GenericValue> contentsList =
                    EntityQuery.use(delegator).from("PartyContentAndDataResource").
                            where("partyId", partyId, "partyContentTypeId", "LGOIMGURL").orderBy("-fromDate").queryPagedList(0, 999999).getData();


            GenericValue partyContent = null;
            if (null != contentsList && contentsList.size() > 0) {
                partyContent = contentsList.get(0);
            }

            if (UtilValidate.isNotEmpty(partyContent)) {
                String contentId = partyContent.getString("contentId");
                personInfo.put("headPortrait",
                        partyContent.getString("objectInfo"));
            } else {
                personInfo.put("headPortrait",
                        "https://personerp.oss-cn-hangzhou.aliyuncs.com/datas/images/defaultHead.png");
            }
            personInfo.put("firstName", (String) person.get("firstName"));


        }

        resultMap.put("personInfo", personInfo);

        //自己就是卖家的情况下无需再增加角色关系
        if (partyId.equals(context.get("payToPartyId") + "")) {
            return resultMap;
        }

        //是否已经是产品的意向客户
        GenericValue productRole = EntityQuery.use(delegator).from("ProductRole").where("productId", context.get("productId"), "partyId", partyId, "roleTypeId", "PLACING_CUSTOMER").queryFirst();
        //如果还不是,就给一个意向客户到产品
        if (!UtilValidate.isNotEmpty(productRole)) {
            dispatcher.runSync("addPartyToProduct", UtilMisc.toMap("userLogin", admin, "partyId", partyId, "productId", context.get("productId"), "roleTypeId", "PLACING_CUSTOMER"));
        }


//已注释掉,将客户做成店铺客户的角色逻辑
        //找店铺Id
//        GenericValue productStoreRole = EntityQuery.use(delegator).from("ProductStoreRole").where("partyId",context.get("payToPartyId") , "roleTypeId", "ADMIN").queryFirst();
//        String productStoreId = (String) productStoreRole.get("productStoreId");
//        GenericValue custStoreRole = EntityQuery.use(delegator).from("ProductStoreRole").where("partyId",partyId,"productStoreId",productStoreId).queryFirst();
//        if (!UtilValidate.isNotEmpty(custStoreRole)) {
//            //什么角色都没的时候,默认给一个潜在客户角色
//            Map<String, Object> createProductStoreRoleOutMap =   dispatcher.runSync("createProductStoreRole", UtilMisc.toMap("userLogin", userLogin, "partyId", partyId, "productStoreId", productStoreId, "roleTypeId", PeConstant.DEFAULT_PRODUCT_STORE_CUST_ROLE));
//            if (!ServiceUtil.isSuccess(createProductStoreRoleOutMap)) {
//                return createProductStoreRoleOutMap;
//            }
//        }

        return resultMap;
    }


    /**
     * 查头像和名称
     *
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, String> queryPersonBaseInfo(Delegator delegator, String partyId) throws GenericEntityException, GenericServiceException {

        Map<String, String> personInfo = new HashMap<String, String>();

        GenericValue person = delegator.findOne("Person", UtilMisc.toMap("partyId", partyId), false);

        if (person != null) {

            List<GenericValue> contentsList =
                    EntityQuery.use(delegator).from("PartyContentAndDataResource").
                            where("partyId", partyId, "partyContentTypeId", "LGOIMGURL").orderBy("-fromDate").queryPagedList(0, 999999).getData();


            GenericValue partyContent = null;
            if (null != contentsList && contentsList.size() > 0) {
                partyContent = contentsList.get(0);
            }

            if (UtilValidate.isNotEmpty(partyContent)) {
                String contentId = partyContent.getString("contentId");
                personInfo.put("headPortrait",
                        partyContent.getString("objectInfo"));
            } else {
                personInfo.put("headPortrait",
                        "https://personerp.oss-cn-hangzhou.aliyuncs.com/datas/images/defaultHead.png");
            }
            personInfo.put("firstName", (String) person.get("firstName"));


        }
        return personInfo;
    }


    /**
     * queryResourceDetailFrom PeMiniProgram
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryResourceDetailMiniProgram(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();

        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));
        //Scope Param
//        GenericValue userLogin = (GenericValue) context.get("userLogin");

        String nowPartyId = (String) context.get("partyId");
        String openId = (String) context.get("unioId");
        GenericValue partyIdentification = EntityQuery.use(delegator).from("PartyIdentification").where("idValue", openId, "partyIdentificationTypeId", "WX_UNIO_ID").queryFirst();
        String partyId = "NA";


        if (UtilValidate.isNotEmpty(partyIdentification)) {
            partyId = (String) partyIdentification.get("partyId");
        }


        System.out.println("openId=" + openId);
        System.out.println("partyId=" + partyId);

        resultMap.put("nowPartyId", partyId);

        GenericValue userLogin = EntityQuery.use(delegator).from("UserLogin").where(UtilMisc.toMap("partyId", partyId)).queryFirst();

        //有效时间
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
        claims.put("user", userLogin.get("userLoginId"));
        claims.put("delegatorName", delegator.getDelegatorName());
        claims.put("exp", exp);
        claims.put("iat", iat);
        String token = signer.sign(claims);

        String productId = (String) context.get("productId");
        resultMap.put("productId", productId);

        Set<String> fieldSet = new HashSet<String>();
        fieldSet.add("productId");
        fieldSet.add("productName");
        fieldSet.add("productStoreId");
        fieldSet.add("createdDate");
        fieldSet.add("salesDiscontinuationDate");
        fieldSet.add("price");
        fieldSet.add("detailImageUrl");
        fieldSet.add("prodCatalogId");
        fieldSet.add("payToPartyId");
        fieldSet.add("description");

        EntityCondition findConditions = EntityCondition
                .makeCondition(UtilMisc.toMap("productId", productId));

        GenericValue product = delegator.findList("ProductAndCategoryMember",
                findConditions, fieldSet,
                null, null, false).get(0);


        // Map<String, Object> resourceDetail = product.getAllFields();
        Map<String, Object> resourceDetail = new HashMap<String, Object>();

        resourceDetail.put("id", (String) product.get("productId"));

        resourceDetail.put("prodCatalogId", (String) product.get("prodCatalogId"));
        resourceDetail.put("productStoreId", (String) product.get("productStoreId"));

        resourceDetail.put("title", (String) product.get("productName"));
        resourceDetail.put("desc", (String) product.get("description"));
        resourceDetail.put("source", "龙熙的转发");
        resourceDetail.put("salesDiscontinuationDate", product.get("salesDiscontinuationDate"));
        resourceDetail.put("cover_url", (String) product.get("detailImageUrl"));
        String payToId = (String) product.get("payToPartyId");
        Map<String, String> userInfoMap = queryPersonBaseInfo(delegator, payToId);
        resourceDetail.put("user", userInfoMap);


        //异步记录访客与资源主建立联系关系
        if (!partyId.equals(payToId)) {
            dispatcher.runAsync("createPartyToPartyRelation", UtilMisc.toMap("userLogin", admin, "partyIdFrom", partyId, "partyIdTo", payToId, "relationShipType", PeConstant.CONTACT));
        }


        resourceDetail.put("payToId", payToId);

        resourceDetail.put("is_follow", "false");


        Set<String> orderFieldSet = new HashSet<String>();
        fieldSet.add("orderId");
        fieldSet.add("partyId");
        fieldSet.add("productId");


        EntityCondition findOrderConditions = EntityCondition
                .makeCondition(UtilMisc.toMap("productId", productId));
        EntityCondition findOrderConditions2 = EntityCondition
                .makeCondition(UtilMisc.toMap("roleTypeId", "BILL_TO_CUSTOMER"));

        EntityConditionList<EntityCondition> listOrderConditions = EntityCondition
                .makeCondition(findOrderConditions, findOrderConditions2);

        //Query My Resource
        List<GenericValue> queryMyResourceOrderList = delegator.findList("OrderHeaderItemAndRoles",
                listOrderConditions, orderFieldSet,
                UtilMisc.toList("-orderDate"), null, false);

        List<Map<String, Object>> partyOrderList = new ArrayList<Map<String, Object>>();
        if (queryMyResourceOrderList != null && queryMyResourceOrderList.size() > 0) {
            resourceDetail.put("orderId", queryMyResourceOrderList.get(0).get("orderId"));
            partyOrderList = doForEachGetBuyerFromRelation(queryMyResourceOrderList, delegator, nowPartyId);
        }
        //实际购买的人
        resourceDetail.put("likes", partyOrderList);


        // Query Product More Images & Text
        List<Map<String, Object>> productMoreDetails = new ArrayList<Map<String, Object>>();
        resourceDetail.put("productMoreDetails", productMoreDetails);


        fieldSet = new HashSet<String>();
        fieldSet.add("drObjectInfo");
        fieldSet.add("productId");
        fieldSet.add("contentId");
        EntityCondition findConditions3 = EntityCondition
                .makeCondition("productId", EntityOperator.EQUALS, productId);
        List<GenericValue> pictures = delegator.findList("ProductContentAndInfo",
                findConditions3, fieldSet,
                null, null, false);


        Long custCount = EntityQuery.use(delegator).from("ProductRole").where("productId", productId, "roleTypeId", PeConstant.PRODUCT_CUSTOMER).queryCount();
        Long placingCount = EntityQuery.use(delegator).from("ProductRole").where("productId", productId, "roleTypeId", "PLACING_CUSTOMER").queryCount();


        Long iLike = EntityQuery.use(delegator).from("ProductRole").where("productId", productId, "roleTypeId", "PLACING_CUSTOMER", "partyId", partyId).queryCount();
        if (iLike > 0) {
            resourceDetail.put("is_like", "true");
        } else {
            resourceDetail.put("is_like", "false");
        }


        resourceDetail.put("like_count", placingCount);
        resourceDetail.put("doc_class", UtilMisc.toMap("title", "其他", "desc", "其他"));
//        resourceDetail.put("placingCount",placingCount);

        resourceDetail.put("morePicture", pictures);
        resourceDetail.put("nowPartyId", partyId);


        Map<String, Object> queryTuCaoMap = dispatcher.runSync("queryProductTuCaoList", UtilMisc.toMap("userLogin", userLogin, "productId", productId));

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>> queryTuCaoMap= " + queryTuCaoMap);
        List<Map<String, Object>> tuCaoList = new ArrayList<Map<String, Object>>();
        if (null != queryTuCaoMap.get("tuCaoList")) {
            tuCaoList = (List<Map<String, Object>>) queryTuCaoMap.get("tuCaoList");
        }

        GenericValue productAddress = EntityQuery.use(delegator).from("ProductAttribute").where("attrName", "address", "productId", productId).queryFirst();
        if (null != productAddress) {
            resourceDetail.put("address", productAddress.get("attrValue"));
        }

        GenericValue productlongitude = EntityQuery.use(delegator).from("ProductAttribute").where("attrName", "longitude", "productId", productId).queryFirst();
        if (null != productlongitude) {
            resourceDetail.put("longitude", productlongitude.get("attrValue"));
        }
        GenericValue productlatitude = EntityQuery.use(delegator).from("ProductAttribute").where("attrName", "latitude", "productId", productId).queryFirst();
        if (null != productlatitude) {
            resourceDetail.put("latitude", productlatitude.get("attrValue"));
        }


        resourceDetail.put("tuCaoList", tuCaoList);


        //查询价格,如果有
        GenericValue productPrice = EntityQuery.use(delegator).from("ProductPrice").where(UtilMisc.toMap("productId", productId)).queryFirst();
        if (null != productPrice) {
            resourceDetail.put("price", productPrice.get("price"));
        }
        GenericValue facility = EntityQuery.use(delegator).from("Facility").where("ownerPartyId", payToId).queryFirst();
        String originFacilityId = (String) facility.get("facilityId");
        //获得库存信息 getInventoryAvailableByFacility
        Map<String, Object> getInventoryAvailableByFacilityMap = dispatcher.runSync("getInventoryAvailableByFacility", UtilMisc.toMap("userLogin", admin,
                "facilityId", originFacilityId, "productId", productId));
        if (ServiceUtil.isSuccess(getInventoryAvailableByFacilityMap)) {
            resourceDetail.put("quantityOnHandTotal", getInventoryAvailableByFacilityMap.get("quantityOnHandTotal"));
            resourceDetail.put("availableToPromiseTotal", getInventoryAvailableByFacilityMap.get("availableToPromiseTotal"));
        }


        //查询卖家提供的联系电话
        GenericValue telecomNumber = EntityUtil.getFirst(
                EntityQuery.use(delegator).from("TelecomNumberAndPartyView").where(UtilMisc.toMap("partyId", payToId, "contactMechPurposeTypeId", "PHONE_MOBILE", "contactMechTypeId", "TELECOM_NUMBER")).queryList());
        if (UtilValidate.isNotEmpty(telecomNumber)) {
            resourceDetail.put("contactNumber", telecomNumber.getString("contactNumber"));
        }


        //查询已有的人际关系列表和人际关系类型
        List<GenericValue> myPersonalRelations = EntityQuery.use(delegator).from("PartyRelationshipAndType").where(UtilMisc.toMap("parentTypeId", "INTERPERSONAL", "partyIdTo", payToId)).queryList();
        List<Map<String, Object>> returnRelationsList = new ArrayList<Map<String, Object>>();
        if (null != myPersonalRelations && myPersonalRelations.size() > 0) {
            for (GenericValue gv : myPersonalRelations) {
                Map<String, Object> rowRelationMap = new HashMap<String, Object>();
                String relationPartyId = (String) gv.get("partyIdFrom");
                Map<String, String> relationPartyMap = queryPersonBaseInfo(delegator, relationPartyId);
                rowRelationMap.put("relationDisc", relationPartyMap.get("firstName") + "是他的" + (String) gv.get("partyRelationshipName"));
                rowRelationMap.put("relationPartyAvatar", relationPartyMap.get("headPortrait"));
                returnRelationsList.add(rowRelationMap);
            }
        }

        //系统中能够被定义人际关系的类型列表,包含当前访问者可能已和资源主存在的关系类型
        List<GenericValue> personalRelationsType = EntityQuery.use(delegator).from("PartyRelationshipType").where(UtilMisc.toMap("parentTypeId", "INTERPERSONAL")).queryList();
        List<Map<String, Object>> returnPersonalRelationsTypeList = new ArrayList<Map<String, Object>>();
        if (null != personalRelationsType && personalRelationsType.size() > 0) {

            for (GenericValue typeRow : personalRelationsType) {
                Map<String, Object> rowPersonalMap = new HashMap<String, Object>();
                String partyRelationshipTypeId = (String) typeRow.get("partyRelationshipTypeId");
                String partyRelationshipName = (String) typeRow.get("partyRelationshipName");
//                String description = (String) typeRow.get("description");
                GenericValue queryIsExsitsRelation = EntityQuery.use(delegator).from("PartyRelationshipAndType").where(
                        UtilMisc.toMap("parentTypeId", "INTERPERSONAL", "partyIdTo", payToId, "partyIdFrom",
                                partyId, "partyRelationshipTypeId", partyRelationshipTypeId)).queryFirst();

                //说明这个关系已经存在 不必再次添加了
                if (queryIsExsitsRelation != null) {
                    rowPersonalMap.put("id", "0");
                } else {
                    rowPersonalMap.put("id", partyRelationshipTypeId);
                }

                rowPersonalMap.put("name", partyRelationshipName);
                rowPersonalMap.put("desc", "卖家是我的" + partyRelationshipName);
                returnPersonalRelationsTypeList.add(rowPersonalMap);
            }
        }


        //和资源主有关系的人都在这个列表
        resourceDetail.put("returnRelationsList", returnRelationsList);
        resourceDetail.put("returnPersonalRelationsTypeList", returnPersonalRelationsTypeList);


        resourceDetail.put("tarjeta", token);

        resultMap.put("resourceDetail", resourceDetail);

//        if(null != userLogin){
//            resultMap.put("partyId", (String) userLogin.get("partyId"));
//        }

        return resultMap;
    }

    /**
     * 查询资源详情
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryResourceDetail(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();


        //Scope Param
        GenericValue userLogin = (GenericValue) context.get("userLogin");

        String nowPartyId = (String) context.get("partyId");

        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));
        GenericValue nowPerson = delegator.findOne("Person", UtilMisc.toMap("partyId", nowPartyId), false);

        if (null != nowPerson) {
            resultMap.put("nowPersonName", (String) nowPerson.get("firstName"));
        }

        String productId = (String) context.get("productId");
        resultMap.put("productId", productId);

        Set<String> fieldSet = new HashSet<String>();
        fieldSet.add("productId");
        fieldSet.add("productName");
        fieldSet.add("productStoreId");
        fieldSet.add("createdDate");
        fieldSet.add("salesDiscontinuationDate");
        fieldSet.add("price");
        fieldSet.add("detailImageUrl");
        fieldSet.add("prodCatalogId");
        fieldSet.add("payToPartyId");
        fieldSet.add("description");

        EntityCondition findConditions = EntityCondition
                .makeCondition(UtilMisc.toMap("productId", productId));
        GenericValue product = null;
        List<GenericValue> products = delegator.findList("ProductAndCategoryMember",
                findConditions, fieldSet,
                null, null, false);

        if (products == null || products.size() == 0) {
            return resultMap;
        }
        product = products.get(0);
        Map<String, Object> resourceDetail = product.getAllFields();
        String payToId = (String) product.get("payToPartyId");
        String detailImageUrl = "";
        if (null != resourceDetail.get("detailImageUrl")) {
            detailImageUrl = (String) resourceDetail.get("detailImageUrl");
        }

        GenericValue person = delegator.findOne("Person", UtilMisc.toMap("partyId", resourceDetail.get("payToPartyId")), false);
        if (person != null) {
            List<GenericValue> contentsList =
                    EntityQuery.use(delegator).from("PartyContentAndDataResource").
                            where("partyId", resourceDetail.get("payToPartyId"), "partyContentTypeId", "LGOIMGURL").orderBy("-fromDate").queryPagedList(0, 999999).getData();


            GenericValue partyContent = null;
            if (null != contentsList && contentsList.size() > 0) {
                partyContent = contentsList.get(0);
            }

            if (UtilValidate.isNotEmpty(partyContent)) {

                String contentId = partyContent.getString("contentId");
                resourceDetail.put("headPortrait",
                        partyContent.getString("objectInfo"));
            } else {
                resourceDetail.put("headPortrait",
                        "https://personerp.oss-cn-hangzhou.aliyuncs.com/datas/images/defaultHead.png");
            }
            resourceDetail.put("firstName", (String) person.get("firstName"));

            //PartyNoteView
//            GenericValue partyNoteView = EntityQuery.use(delegator).from("PartyNoteView").where("targetPartyId", resourceDetail.get("payToPartyId")).queryFirst();
//            if (UtilValidate.isNotEmpty(partyNoteView)) {
//                resourceDetail.put("partyNote", partyNoteView.get("noteInfo"));
//            } else {
//                resourceDetail.put("partyNote", "这位卖家还未设置个人说明...");
//            }

        }


        Set<String> orderFieldSet = new HashSet<String>();
        fieldSet.add("orderId");
        fieldSet.add("partyId");
        fieldSet.add("productId");


        EntityCondition findOrderConditions = EntityCondition
                .makeCondition(UtilMisc.toMap("productId", productId));
        EntityCondition findOrderConditions2 = EntityCondition
                .makeCondition(UtilMisc.toMap("roleTypeId", "BILL_TO_CUSTOMER"));

        EntityConditionList<EntityCondition> listOrderConditions = EntityCondition
                .makeCondition(findOrderConditions, findOrderConditions2);

        //Query My Resource
        List<GenericValue> queryMyResourceOrderList = delegator.findList("OrderHeaderItemAndRoles",
                listOrderConditions, orderFieldSet,
                UtilMisc.toList("-orderDate"), null, false);

        List<Map<String, Object>> partyOrderList = new ArrayList<Map<String, Object>>();
        if (queryMyResourceOrderList != null && queryMyResourceOrderList.size() > 0) {
            resourceDetail.put("orderId", queryMyResourceOrderList.get(0).get("orderId"));
            partyOrderList = doForEachGetBuyerFromRelation(queryMyResourceOrderList, delegator, nowPartyId);

        }

        resourceDetail.put("partyBuyOrder", partyOrderList);


        // Query Product More Images & Text
        List<Map<String, Object>> productMoreDetails = new ArrayList<Map<String, Object>>();
        resourceDetail.put("productMoreDetails", productMoreDetails);


        fieldSet = new HashSet<String>();
        fieldSet.add("drObjectInfo");
        fieldSet.add("productId");
        fieldSet.add("contentId");
        EntityCondition findConditions3 = EntityCondition
                .makeCondition("productId", EntityOperator.EQUALS, productId);
        List<GenericValue> pictures = delegator.findList("ProductContentAndInfo",
                findConditions3, fieldSet,
                null, null, false);
        List<Map<String, Object>> picturesListParp = new ArrayList<Map<String, Object>>();

        Map<String, Object> firstMap = new HashMap<String, Object>();
        firstMap.put("drObjectInfo", detailImageUrl);
        //这是封面图,固定的contentId
        firstMap.put("contentId", "308561217_784838898");
        if (!detailImageUrl.trim().equals("")) {
            picturesListParp.add(firstMap);
        }

        if (null != pictures && pictures.size() > 0) {
            for (GenericValue gv : pictures) {
                Map<String, Object> rowMap = new HashMap<String, Object>();
                String drObjectInfo = "";
                if (null != gv.get("drObjectInfo")) {
                    drObjectInfo = (String) gv.get("drObjectInfo");
                    if (drObjectInfo.indexOf("http") < 0) {
                        drObjectInfo = "http://" + drObjectInfo;
                    }
                }
                rowMap.put("drObjectInfo", drObjectInfo);
                rowMap.put("contentId", gv.get("contentId"));
                picturesListParp.add(rowMap);
            }
        }
        Long custCount = EntityQuery.use(delegator).from("ProductRole").where("productId", productId, "roleTypeId", PeConstant.PRODUCT_CUSTOMER).queryCount();
        Long placingCount = EntityQuery.use(delegator).from("ProductRole").where("productId", productId, "roleTypeId", "PLACING_CUSTOMER").queryCount();

        resourceDetail.put("custCount", custCount);
        resourceDetail.put("placingCount", placingCount);
        resourceDetail.put("morePicture", picturesListParp);

        //查询 ProductVirtualAndVariantInfo  查看这个产品是否是虚拟产品 有没有变形产品
//        Map<String,Object> queryProductFeature = dispatcher.runSync("ProductVirtualAndVariantInfo",UtilMisc.toMap("userLogin",userLogin,"productId",productId));


//         List<GenericValue> productVirtualAndVariantInfoList = EntityQuery.use(delegator).from("ProductVirtualAndVariantInfo").where("productId",productId).queryList();

//        List<GenericValue> productFeatureAndApplList = EntityQuery.use(delegator).from("ProductFeatureAndAppl").where("productId", productId).queryList();


//        List<Map<String,Object>> productFeaturesList = (List<Map<String,Object>>) queryProductFeature.get("productFeaturesList");
//
//        String strProductFeaturesList = "<div class=\"pro-color\">";
//
//        for(Map<String,Object> mp : productFeaturesList){
//            Set set = mp.keySet();//得到所有map里面key的集合\
//            for(Iterator iter = set.iterator(); iter.hasNext();)//遍历
//            {
//                String key = (String)iter.next();
//                strProductFeaturesList += "<span class=\"part-note-msg\">";
//                strProductFeaturesList = strProductFeaturesList + key + "</span>";
//
//                List<String> innerList = (List<String>) mp.get(key);
//
//                strProductFeaturesList += "<p id=\"color\"><a  href=\"javaScript:selectFeature(this);\" onclick=\"selectFeature(this);\" title=\"noselected\" style=\"display:none;\">" +"</a></p>";
//
//                for(int i =0 ; i < innerList.size();i++){
//
//                     String rowKey = innerList.get(i);
//
//                     strProductFeaturesList = strProductFeaturesList + "<p id=\"color\">";
//                     strProductFeaturesList += "<a id=\""+key +"\" href=\"javaScript:selectFeature(this);\" onclick=\"selectFeature(this);\" title=\"noselected\" class=\"a-item J_ping\"   report-eventparam=\"   " + rowKey + "  \" > " + rowKey +"</a>";
//
//                    strProductFeaturesList += "</p>";
//                }
//
//            }
//
//        }
//        strProductFeaturesList += "</div>";
//
//        System.out.println("strProductFeaturesList="+strProductFeaturesList);


        // 查询卖家的联系二维码。
        GenericValue wxContactQrCodes =
                EntityQuery.use(delegator).from("PartyContentAndDataResource").
                        where("partyId", payToId, "partyContentTypeId", "WECHATCONTACTQRCODE").orderBy("-fromDate").queryFirst();


        if (null != wxContactQrCodes) {
            resourceDetail.put("weChatContactQrCode", wxContactQrCodes.getString("objectInfo"));
        }
        // 查询卖家付款二维码。
        GenericValue wxPayQrCodes =
                EntityQuery.use(delegator).from("PartyContentAndDataResource").
                        where("partyId", payToId, "partyContentTypeId", "WECHATQRCODE").orderBy("-fromDate").queryFirst();

        if (null != wxPayQrCodes) {
            resourceDetail.put("weChatPayQrCode", wxPayQrCodes.getString("objectInfo"));
        }


        GenericValue facility = EntityQuery.use(delegator).from("Facility").where("ownerPartyId", payToId).queryFirst();
        String originFacilityId = (String) facility.get("facilityId");
        //获得库存信息 getInventoryAvailableByFacility
        Map<String, Object> getInventoryAvailableByFacilityMap = dispatcher.runSync("getInventoryAvailableByFacility", UtilMisc.toMap("userLogin", admin,
                "facilityId", originFacilityId, "productId", productId));
        if (ServiceUtil.isSuccess(getInventoryAvailableByFacilityMap)) {
            resourceDetail.put("quantityOnHandTotal", getInventoryAvailableByFacilityMap.get("quantityOnHandTotal"));
            resourceDetail.put("availableToPromiseTotal", getInventoryAvailableByFacilityMap.get("availableToPromiseTotal"));
        }

        resultMap.put("resourceDetail", resourceDetail);
//      resultMap.put("productFeatureAndApplList", productFeatureAndApplList);

        if (null != userLogin) {
            resultMap.put("partyId", (String) userLogin.get("partyId"));
        }


        return resultMap;
    }

    private static List<Map<String, Object>> doForEachGetBuyerFromRelation(List<GenericValue> queryMyResourceOrderList, Delegator delegator, String partyId) throws GenericEntityException {

        List<Map<String, Object>> partyOrderList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> noContactList = new ArrayList<Map<String, Object>>();
        Map<String, String> names = new HashMap<String, String>();
        for (GenericValue order : queryMyResourceOrderList) {
            Map<String, Object> rowMap = new HashMap<String, Object>();
            rowMap.put("orderDate", order.get("orderDate"));
            String orderPartyId = (String) order.get("partyId");
            GenericValue orderPerson = delegator.findOne("Person", UtilMisc.toMap("partyId", orderPartyId), false);
            if (orderPerson != null) {
                rowMap.put("firstName", (String) orderPerson.get("firstName"));
            }
            //查头像
            List<GenericValue> contentsList =
                    EntityQuery.use(delegator).from("PartyContentAndDataResource").
                            where("partyId", orderPartyId, "partyContentTypeId", "LGOIMGURL").orderBy("-fromDate").queryPagedList(0, 999999).getData();


            GenericValue partyContent = null;
            if (null != contentsList && contentsList.size() > 0) {
                partyContent = contentsList.get(0);
            }

            if (UtilValidate.isNotEmpty(partyContent)) {


                rowMap.put("avatar",
                        partyContent.getString("objectInfo"));
            } else {
                rowMap.put("avatar",
                        "https://personerp.oss-cn-hangzhou.aliyuncs.com/datas/images/defaultHead.png");
            }

            GenericValue partyRelationship = EntityQuery.use(delegator).from("PartyRelationship").where("partyIdTo", orderPartyId, "partyIdFrom", partyId, "partyRelationshipTypeId", "CONTACT_REL").queryFirst();
            if (null != partyRelationship) {

                if (!names.containsKey((String) rowMap.get("firstName"))) {
                    names.put((String) rowMap.get("firstName"), "");
                    partyOrderList.add(rowMap);
                }

            } else {
                if (!names.containsKey((String) rowMap.get("firstName"))) {
                    names.put((String) rowMap.get("firstName"), "");
                    noContactList.add(rowMap);
                }
            }

        }

        if (noContactList.size() > 0) {
            for (int i = 0; i < noContactList.size(); i++) {
                Map<String, Object> rowMap = noContactList.get(i);
                partyOrderList.add(rowMap);
            }
        }

        return partyOrderList;
    }

    public static Map<String, Object> queryAppContactResourceList(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();

        GenericValue userLogin = (GenericValue) context.get("userLogin");

        String partyId = (String) userLogin.get("partyId");

        String viewIndexStr = (String) context.get("viewIndexStr");


        int viewIndex = 0;
        if (viewIndexStr != null) {
            viewIndex = Integer.parseInt(viewIndexStr);
        }

        int viewSize = 9999;
        int lowIndex = 0;
        int highIndex = 0;
        int resourceCount = 0;


        //查询联系人列表
        List<String> orderBy = UtilMisc.toList("-createdDate");
        PagedList<GenericValue> myContactListPage = null;
        myContactListPage = EntityQuery.use(delegator).from("PartyContactResources").
                where("partyIdTo", partyId, "partyRelationshipTypeId", PeConstant.CONTACT, "roleTypeId", "ADMIN").orderBy(orderBy)
                .distinct()
                .queryPagedList(viewIndex, viewSize);

        List<GenericValue> myContactList = myContactListPage.getData();


        List<GenericValue> myContactListCountList = EntityQuery.use(delegator).from("PartyContactResources").
                where("partyIdTo", partyId, "partyRelationshipTypeId", PeConstant.CONTACT, "roleTypeId", "ADMIN")
                .orderBy(orderBy)
                .distinct()
                .queryList();
        resourceCount = myContactListCountList.size();

        lowIndex = myContactListPage.getStartIndex();
        highIndex = myContactListPage.getEndIndex();


        if (null != myContactList) {


            for (GenericValue gv : myContactList) {

                Map<String, Object> rowMap = new HashMap<String, Object>();

                String contactPartyId = (String) gv.get("partyIdFrom");

                if (partyId.equals(contactPartyId) || gv.get("salesDiscontinuationDate") != null) {
                    continue;
                }

                Map<String, String> userInfoMap = queryPersonBaseInfo(delegator, contactPartyId);

                java.sql.Timestamp createdDateTp = (java.sql.Timestamp) gv.get("createdDate");

                rowMap.put("created", dateToStr(createdDateTp, "yyyy-MM-dd HH:mm:ss"));

                rowMap.put("partyId", partyId);

                rowMap.put("user", userInfoMap);

                rowMap.put("contactPartyId", contactPartyId);

                String productId = (String) gv.get("productId");

                GenericValue productAddress = EntityQuery.use(delegator).from("ProductAttribute").where("attrName", "address", "productId", productId).queryFirst();
                if (null != productAddress) {
                    rowMap.put("address", productAddress.get("attrValue"));
                }

                GenericValue productlongitude = EntityQuery.use(delegator).from("ProductAttribute").where("attrName", "longitude", "productId", productId).queryFirst();
                if (null != productlongitude) {
                    rowMap.put("longitude", productlongitude.get("attrValue"));
                }
                GenericValue productlatitude = EntityQuery.use(delegator).from("ProductAttribute").where("attrName", "latitude", "productId", productId).queryFirst();
                if (null != productlatitude) {
                    rowMap.put("latitude", productlatitude.get("attrValue"));
                }

                rowMap.put("productId", productId);

                rowMap.put("description", (String) gv.get("description"));

                rowMap.put("productName", (String) gv.get("productName"));

                rowMap.put("detailImageUrl", (String) gv.get("detailImageUrl"));

                rowMap.put("price", gv.get("price") + "");
                HashSet<String> fieldSet = new HashSet<String>();
                fieldSet.add("drObjectInfo");
                fieldSet.add("productId");
                EntityCondition findConditions3 = EntityCondition
                        .makeCondition("productId", EntityOperator.EQUALS, (String) gv.get("productId"));

                List<GenericValue> pictures = delegator.findList("ProductContentAndInfo",
                        findConditions3, fieldSet,
                        null, null, false);
//                List<Map<String,Object>> rowPicturesList = new ArrayList<Map<String, Object>>();
//                if(null!=pictures && pictures.size()>0){
//                    for(GenericValue picture : pictures){
//                        Map<String,Object> rowPicture = picture.getAllFields();
//                        rowPicture.put("drObjectInfo","http://"+rowPicture.get("drObjectInfo"));
//                        rowPicturesList.add(rowPicture);
//                    }
//                }

                rowMap.put("morePicture", pictures);

                returnList.add(rowMap);

            }


        }
        resultMap.put("resourcesList", returnList);

        //总共有多少页码
        int countIndex = (resourceCount % viewSize);
        //viewIndex 当前页码


        if (resourceCount != 0 && resourceCount > viewSize) {
            resultMap.put("total", resourceCount % viewSize == 0 ? resourceCount / viewSize : resourceCount / viewSize + 1);
        } else {
            if (null == myContactListCountList || myContactListCountList.size() == 0) {
                resultMap.put("total", -1);
            } else {
                resultMap.put("total", 1);
            }

        }

        resultMap.put("from", viewIndex);


        resultMap.put("current_page", viewIndex + 1);
        resultMap.put("last_page", resourceCount);

        return resultMap;
    }

    /**
     * 按照维度查询
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryDimensionResource(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();


        //Scope Param
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = (String) userLogin.get("partyId");
        String productCategoryId = "";
        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
        List<GenericValue> dimensionResourceList = null;

        //查我的目录
        GenericValue rodCatalogRole = EntityQuery.use(delegator).from("ProdCatalogRole").where("partyId", partyId, "roleTypeId", "ADMIN").queryFirst();

//        EntityFindOptions findOptions = new EntityFindOptions();
//        findOptions.setFetchSize(0);
//        findOptions.setMaxRows(4);
        //Select Fields
        Set<String> fieldSet = new HashSet<String>();
        fieldSet.add("productId");
        fieldSet.add("productStoreId");
        fieldSet.add("productName");
        fieldSet.add("detailImageUrl");
        fieldSet.add("createdDate");
        fieldSet.add("price");
        fieldSet.add("productCategoryId");
        fieldSet.add("payToPartyId");

        if (rodCatalogRole != null) {
            String prodCatalogId = (String) rodCatalogRole.get("prodCatalogId");
            //根据目录拿关联的分类Id
            GenericValue prodCatalogCategory = EntityQuery.use(delegator).from("ProdCatalogCategory").where("prodCatalogId", prodCatalogId, "prodCatalogCategoryTypeId", "PCCT_PURCH_ALLW").queryFirst();
            //得到分类Id
            productCategoryId = (String) prodCatalogCategory.get("productCategoryId");
            //findConditions
            EntityCondition findConditions = EntityCondition
                    .makeCondition("productCategoryId", EntityOperator.NOT_EQUAL, productCategoryId);

            EntityCondition findConditions2 = EntityCondition
                    .makeCondition(UtilMisc.toMap("productTypeId", "SERVICE"));
            EntityCondition findConditions3 = EntityCondition
                    .makeCondition("productName", EntityOperator.NOT_EQUAL, GenericEntity.NULL_FIELD.toString());
            EntityCondition findConditions4 = EntityCondition
                    .makeCondition("salesDiscontinuationDate", EntityOperator.EQUALS, GenericEntity.NULL_FIELD);


            EntityConditionList<EntityCondition> listConditions = EntityCondition
                    .makeCondition(findConditions, findConditions2, findConditions3, findConditions4);


            //Query My Resource
            dimensionResourceList = delegator.findList("ProductAndCategoryMember",
                    listConditions, fieldSet,
                    UtilMisc.toList("-createdDate"), null, false);
            //EntityQuery.use(delegator).from("ProductAndCategoryMember").where("productCategoryId",productCategoryId).queryList();

            if (null != dimensionResourceList && dimensionResourceList.size() > 0) {
                for (GenericValue gv : dimensionResourceList) {
                    Map<String, Object> rowMap = new HashMap<String, Object>();
                    rowMap = gv.getAllFields();
                    String payToPartyId = (String) gv.get("payToPartyId");
                    GenericValue person = delegator.findOne("Person", UtilMisc.toMap("partyId", payToPartyId), false);
                    if (person != null) {
                        rowMap.put("firstName", (String) person.get("firstName"));
                    }

                    returnList.add(rowMap);
                }
            }
        }


        resultMap.put("dimensionResourceList", returnList);

        return resultMap;
    }


    /**
     * queryPersonInfo
     *
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     */
    public static Map<String, Object> queryPersonInfo(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {


        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        Map<String, Object> inputMap = UtilMisc.toMap();
        String partyId = "";

        if (userLogin == null) {
            partyId = (String) context.get("partyId");
        } else {
            partyId = (String) userLogin.get("partyId");
        }


        GenericValue person = delegator.findOne("Person", false, UtilMisc.toMap("partyId", partyId));


        GenericValue party = delegator.findOne("Party", UtilMisc.toMap("partyId", partyId), false);


        inputMap.put("personName", (String) person.get("firstName"));
        //获取性别
        String gender = "";
        if (UtilValidate.isNotEmpty(person.get("gender"))) {
            // gender = "M".equals(person.get("gender")) ? "男" : "女";
            inputMap.put("gender", person.get("gender"));
        } else {
            inputMap.put("gender", "NA");
        }
        Map<String, Object> postalInfo = dispatcher.runSync("queryPostalAddress", UtilMisc.toMap(
                "userLogin", userLogin
        ));
        if (null != postalInfo.get("postalAddress")) {
            inputMap.put("postalInfo", postalInfo.get("postalAddress"));
        }


        //获取电话号码
//        GenericValue telecomNumber = EntityUtil.getFirst(
//                EntityQuery.use(delegator).from("TelecomNumberAndPartyView").where(UtilMisc.toMap("partyId", partyId, "contactMechPurposeTypeId", "PHONE_MOBILE", "contactMechTypeId", "TELECOM_NUMBER")).queryList());
//        if (UtilValidate.isNotEmpty(telecomNumber)) {
//            inputMap.put("contactNumber", telecomNumber.getString("contactNumber"));
//        }


//        //获取email
//        GenericValue emailAddress = EntityUtil.getFirst(
//                EntityQuery.use(delegator).from("EmailAndPartyView").where(UtilMisc.toMap("partyId", partyId, "contactMechPurposeTypeId", "PRIMARY_EMAIL", "contactMechTypeId", "EMAIL_ADDRESS")).queryList());
//        if (UtilValidate.isNotEmpty(emailAddress)) inputMap.put("email", emailAddress.getString("infoString"));
//        //获取地址
//        GenericValue postalAddress = EntityUtil.getFirst(
//                EntityQuery.use(delegator).from("PostalAddressAndPartyView").where(UtilMisc.toMap("partyId", partyId, "contactMechPurposeTypeId", "SHIPPING_LOCATION", "contactMechTypeId", "POSTAL_ADDRESS")).queryList());
//        if (UtilValidate.isNotEmpty(postalAddress))
//            inputMap.put("contactAddress", "" + postalAddress.get("geoName") + " " + postalAddress.get("city") + " " + postalAddress.get("address1"));
//        if (UtilValidate.isNotEmpty(emailAddress)) inputMap.put("email", emailAddress.getString("infoString"));


        List<GenericValue> contentsList =
                EntityQuery.use(delegator).from("PartyContentAndDataResource").
                        where("partyId", partyId, "partyContentTypeId", "LGOIMGURL").orderBy("-fromDate").queryPagedList(0, 999999).getData();


        GenericValue partyContent = null;
        if (null != contentsList && contentsList.size() > 0) {
            partyContent = contentsList.get(0);
        }

        if (UtilValidate.isNotEmpty(partyContent)) {

            String contentId = partyContent.getString("contentId");
            inputMap.put("headPortrait",
                    partyContent.getString("objectInfo"));
        } else {
            inputMap.put("headPortrait",
                    "https://personerp.oss-cn-hangzhou.aliyuncs.com/datas/images/defaultHead.png");
        }


        List<GenericValue> partyIdentificationList = EntityQuery.use(delegator).from("PartyIdentification").where("partyId", partyId, "partyIdentificationTypeId", "WX_UNIO_ID").queryList();


        if (null != partyIdentificationList && partyIdentificationList.size() > 0) {
            String openId = (String) partyIdentificationList.get(0).get("idValue");

            inputMap.put("openId", partyIdentificationList.get(0).get("idValue"));
        } else {
            inputMap.put("openId", "NA");
        }

//        List<GenericValue> paymentMethodList = EntityQuery.use(delegator).from("PaymentMethod").where("partyId", partyId, "paymentMethodTypeId", "MEDIATION_PAY").queryList();
//
//        if(null != paymentMethodList && paymentMethodList.size()>0){
//            inputMap.put("paymentMethodList",paymentMethodList);
//        }else{
//            inputMap.put("paymentMethodList","NA");
//        }


        //Query Qr Code 付款码信息 目前没有这个业务线注释掉

        GenericValue wxPayQrCodes =
                EntityQuery.use(delegator).from("PartyContentAndDataResource").
                        where("partyId", partyId, "partyContentTypeId", "WECHATQRCODE").orderBy("-fromDate").queryFirst();


        if (null != wxPayQrCodes) {
            inputMap.put("weChatPayQrCode", wxPayQrCodes.getString("objectInfo"));
        }
        GenericValue wxContactQrCodes =
                EntityQuery.use(delegator).from("PartyContentAndDataResource").
                        where("partyId", partyId, "partyContentTypeId", "WECHATCONTACTQRCODE").orderBy("-fromDate").queryFirst();


        if (null != wxContactQrCodes) {
            inputMap.put("weChatContactQrCode", wxContactQrCodes.getString("objectInfo"));
        }


//
//        List<GenericValue> weChatQrCodes =
//                EntityQuery.use(delegator).from("PartyContentAndDataResource").
//                        where("partyId", partyId, "partyContentTypeId", "WECHATQRCODE").orderBy("-fromDate").queryPagedList(0,999999).getData();
//
//
//        GenericValue weChatQrCode = null;
//
//        if(null != weChatQrCodes && weChatQrCodes.size()>0){
//            weChatQrCode = weChatQrCodes.get(0);
//            inputMap.put("weChatQrCode",weChatQrCode.getString("objectInfo"));
//        }


        resultMap.put("userInfo", inputMap);


        return resultMap;
    }


    /**
     * 查询我的资源(包含自己发布的和从别处购买的)
     *
     * @param dctx
     * @param context
     * @return
     */
    public static Map<String, Object> queryMyResource(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();


        //Scope Param
        GenericValue userLogin = (GenericValue) context.get("userLogin");

        String viewSizeStr = (String) context.get("viewSize");
        String viewIndexStr = (String) context.get("viewIndex");
        String isDiscontinuation = (String) context.get("isDiscontinuation");

        //Default Value
        int viewSize = 10;
        int viewIndex = 0;

        if (UtilValidate.isNotEmpty(viewSizeStr)) {
            viewSize = Integer.parseInt(viewSizeStr);
        }
        if (UtilValidate.isNotEmpty(viewIndexStr)) {
            viewIndex = Integer.parseInt(viewIndexStr);
        }
        // 0:查询未下架的正常资源。   1:查已下架的资源  (默认0)
        if (UtilValidate.isEmpty(isDiscontinuation)) {
            isDiscontinuation = "0";
        }

        String partyId = (String) userLogin.get("partyId");
        String productCategoryId = "NA";
        List<GenericValue> myResourceList = null;

        List<Map<String, Object>> resourceMapList = new ArrayList<Map<String, Object>>();

        //查我的目录
        GenericValue rodCatalogRole = EntityQuery.use(delegator).from("ProdCatalogRole").where("partyId", partyId, "roleTypeId", "ADMIN").queryFirst();

        EntityFindOptions findOptions = new EntityFindOptions();

        //findOptions.setFetchSize(viewIndex);

        findOptions.setMaxRows(viewSize);

        //Select Fields
        Set<String> fieldSet = new HashSet<String>();
        fieldSet.add("productId");
        fieldSet.add("description");
        fieldSet.add("productStoreId");
        fieldSet.add("productName");
        fieldSet.add("detailImageUrl");
        fieldSet.add("createdDate");
        fieldSet.add("price");
        fieldSet.add("productCategoryId");
        fieldSet.add("payToPartyId");

        if (rodCatalogRole != null) {
            String prodCatalogId = (String) rodCatalogRole.get("prodCatalogId");
            //根据目录拿关联的分类Id
            GenericValue prodCatalogCategory = EntityQuery.use(delegator).from("ProdCatalogCategory").where("prodCatalogId", prodCatalogId, "prodCatalogCategoryTypeId", "PCCT_PURCH_ALLW").queryFirst();
            //得到分类Id
            productCategoryId = (String) prodCatalogCategory.get("productCategoryId");
            //findConditions
            EntityCondition findConditions = EntityCondition
                    .makeCondition(UtilMisc.toMap("productCategoryId", productCategoryId));

            EntityCondition findConditions2 = null;

            if (isDiscontinuation.equals("0")) {
                findConditions2 = EntityCondition.makeCondition("salesDiscontinuationDate", EntityOperator.EQUALS, GenericEntity.NULL_FIELD);
            } else {
                findConditions2 = EntityCondition.makeCondition("salesDiscontinuationDate", EntityOperator.NOT_EQUAL, GenericEntity.NULL_FIELD);
            }

            EntityConditionList<EntityCondition> listConditions = EntityCondition
                    .makeCondition(findConditions, findConditions2);

            //Query My Resource
            myResourceList = delegator.findList("ProductAndCategoryMember",
                    listConditions, fieldSet,
                    UtilMisc.toList("-createdDate"), findOptions, false);

            if (null != myResourceList && myResourceList.size() > 0) {
                for (GenericValue gv : myResourceList) {
                    Map<String, Object> rowMap = new HashMap<String, Object>();


                    Long custCount = EntityQuery.use(delegator).from("ProductRole").where("roleTypeId", PeConstant.PRODUCT_CUSTOMER, "productId", (String) gv.get("productId")).queryCount();
                    Long placingCustCount = EntityQuery.use(delegator).from("ProductRole").where("roleTypeId", "PLACING_CUSTOMER", "productId", (String) gv.get("productId")).queryCount();
                    Long visitorCount = EntityQuery.use(delegator).from("ProductRole").where("roleTypeId", "VISITOR", "productId", (String) gv.get("productId")).queryCount();
                    Long partnerCount = EntityQuery.use(delegator).from("ProductRole").where("roleTypeId", "PARTNER", "productId", (String) gv.get("productId")).queryCount();

                    // 客户、 潜在客户、 访问者 、 合作伙伴(转发者)
                    rowMap.put("custCount", custCount);
                    rowMap.put("placingCustCount", placingCustCount);
                    rowMap.put("visitorCount", visitorCount);
                    rowMap.put("partnerCount", partnerCount);

//                    GenericValue productAttrQu = EntityQuery.use(delegator).from("ProductAttribute").where("attrName","quantityAccepted","productId", (String)gv.get("productId")).queryFirst();
//
//                    rowMap.put("kuCun",(String)productAttrQu.get("attrValue"));

                    rowMap.put("productName", (String) gv.get("productName"));
                    rowMap.put("productId", (String) gv.get("productId"));
                    rowMap.put("productStoreId", (String) gv.get("productStoreId"));
                    rowMap.put("detailImageUrl", (String) gv.get("detailImageUrl"));
                    rowMap.put("createdDate", gv.get("createdDate"));
                    rowMap.put("price", gv.get("price"));
                    rowMap.put("productCategoryId", (String) gv.get("productCategoryId"));
                    rowMap.put("payToPartyId", (String) gv.get("payToPartyId"));
                    rowMap.put("description", (String) gv.get("description"));


                    fieldSet = new HashSet<String>();
                    fieldSet.add("drObjectInfo");
                    fieldSet.add("productId");
                    EntityCondition findConditions3 = EntityCondition
                            .makeCondition("productId", EntityOperator.EQUALS, (String) gv.get("productId"));
                    List<GenericValue> pictures = delegator.findList("ProductContentAndInfo",
                            findConditions3, fieldSet,
                            null, null, false);
                    rowMap.put("morePicture", pictures);

                    resourceMapList.add(rowMap);
                }
            }


            //  ProductContentAndInfo
            //EntityQuery.use(delegator).from("ProductAndCategoryMember").where("productCategoryId",productCategoryId).queryList();
        }


        resultMap.put("myResourceList", resourceMapList);
        resultMap.put("productCategoryId", productCategoryId);

        return resultMap;
    }


}
