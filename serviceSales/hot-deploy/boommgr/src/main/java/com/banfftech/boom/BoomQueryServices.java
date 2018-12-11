package main.java.com.banfftech.boom;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import main.java.com.banfftech.boom.bean.MailInfo;
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
import org.apache.ofbiz.base.util.collections.PagedList;

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
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.omg.CORBA.portable.Delegate;
import sun.net.www.content.text.Generic;
import sun.security.krb5.Config;

import static main.java.com.banfftech.personmanager.PersonManagerQueryServices.module;
import static main.java.com.banfftech.personmanager.PersonManagerQueryServices.queryBomPersonBaseInfo;
import static main.java.com.banfftech.personmanager.PersonManagerQueryServices.queryPersonBaseInfo;

/**
 * Created by S on 2018/8/29.
 */
public class BoomQueryServices {


    public static final String resourceUiLabels = "CommonEntityLabels.xml";


    /**
     * query DeliveryPlanItemByDate(按时间查询出库)
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryDeliveryPlanItemByDate(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = userLogin.getString("partyId");
        //筛选时间起
        EntityCondition dateConditionStart = null;
        //筛选时间止
        EntityCondition dateConditionEnd = null;
        Date date = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();

        String startDate = (String) context.get("startDate");
        String endDate = (String) context.get("endDate");
        String mail = (String) context.get("mail");

        Map<String,Object> myGroup = getMyGroup(delegator, partyId);
//        String partyGroupId = "13390";
         String partyGroupId = (String) myGroup.get("partyId");
        List<Map<String, Object>> dataArrayList = new ArrayList<Map<String, Object>>();

        EntityCondition findConditions = EntityCondition.makeCondition("payToParty", EntityOperator.EQUALS, partyGroupId);
        EntityCondition findConditions2 = EntityCondition.makeCondition("planId", EntityOperator.LIKE, partyGroupId+"/"+"%");
        EntityCondition genericCondition = EntityCondition.makeCondition(findConditions, EntityOperator.AND, findConditions2);
        Debug.logInfo("->>>>startDate:"+startDate,module);
        Debug.logInfo("->>>>endDate:"+endDate,module);

        dateConditionStart = EntityCondition
                .makeCondition("fromDate",EntityOperator.GREATER_THAN_EQUAL_TO,Timestamp.valueOf(startDate));
        dateConditionEnd = EntityCondition
                .makeCondition("fromDate",EntityOperator.LESS_THAN_EQUAL_TO,Timestamp.valueOf(endDate));

        EntityCondition dateCondition = EntityCondition.makeCondition(dateConditionStart, EntityOperator.AND, dateConditionEnd);

        EntityCondition allCondition = EntityCondition.makeCondition(genericCondition, EntityOperator.AND, dateCondition);
        List<GenericValue> dpList = delegator.findList("DeliveryPlansItem",
                allCondition, null,
                null, null, false);

        Map<String,Object> allProductMap = new HashMap<String, Object>();
        DateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        XSSFWorkbook wb = new XSSFWorkbook(); // --->创建了一个excel文件
        long tm = System.currentTimeMillis();
        String fileName = "test-send" + "-" + sdf2.format(tm);

        XSSFSheet sheet = wb.createSheet(fileName); // --->创建了一个工作簿
        XSSFRow row = sheet.createRow(0); // --->创建一行
        Map<String,Object> allChanelMap = new HashMap<String, Object>();
        EntityCondition findChanelCondition = EntityCondition.makeCondition("enumCode", EntityOperator.LIKE, partyGroupId+"%");
        List<GenericValue> chanels = EntityQuery.use(delegator).from("Enumeration").where(findChanelCondition).queryList();

        if(null != chanels && chanels.size()>0){
            for (GenericValue gv : chanels) {
                if(!allChanelMap.containsKey(gv.getString("enumId"))){
                    GenericValue chanel = EntityQuery.use(delegator).from("Enumeration").where("enumId",gv.getString("enumId")).queryFirst();
                    allChanelMap.put(gv.getString("enumId"), chanel.getString("description"));
                }
            }
        }
        Map<String,Object> channelProductCountMap = new HashMap<String, Object>();
        int tableHeadIndex = 0;
        row.createCell(tableHeadIndex).setCellValue("");
        tableHeadIndex+=1;
        if (dpList.size() > 0) {
            for (GenericValue gv : dpList) {
                String outQuantity = gv.getString("outQuantity") ;
                String enumId = gv.getString("enumId") ;
                //绘制表头
                if(!allProductMap.containsKey(gv.getString("productId")) && UtilValidate.isNotEmpty(outQuantity)){
                    int intOutQuantity = Integer.parseInt(outQuantity);
                    if(intOutQuantity>0 && allChanelMap.containsKey(enumId)){
                        allProductMap.put(gv.getString("productId"),gv.getString("productName"));
                        row.createCell(tableHeadIndex).setCellValue(gv.getString("productName"));
                        tableHeadIndex++;
                    }
                }


                String productId = gv.getString("productId") ;
                int outQ;
                if(outQuantity == null || outQuantity.trim().toLowerCase().equals("null")){
                    outQ =0;
                }else{
                    outQ = Integer.parseInt(outQuantity);
                }
                if(!channelProductCountMap.containsKey(enumId+"-"+productId)){
                    channelProductCountMap.put(enumId+"-"+productId,outQ);
                }else{
                    int exsitQty = Integer.parseInt(channelProductCountMap.get(enumId+"-"+productId)+"");
                    channelProductCountMap.put(enumId+"-"+productId,outQ+exsitQty);
                }
            }
        }

        int rowCount = 1;
        String beforeEnumId = null;
        Map<String,Object> isExistChannel = new HashMap<String, Object>();
        if(allChanelMap.size()>0){
            int i = 1;
            for (Map.Entry<String, Object> m : allChanelMap.entrySet()) {
                int j = 0;
                String channelKey = m.getKey();
                String channelName = "" + m.getValue();
                XSSFRow nRow = sheet.createRow(i);
                nRow.createCell(j).setCellValue(channelName);

                for (Map.Entry<String, Object> p : allProductMap.entrySet()) {
                    String productId = p.getKey();
                    if(channelProductCountMap.containsKey(channelKey + "-" + productId)){
                        String outQuantity = ""+channelProductCountMap.get(channelKey + "-" + productId);
                        int outQuantityInt = Integer.parseInt(outQuantity);
                            nRow.createCell(j+1).setCellValue((Integer)outQuantityInt);
                        j++;
                    }else{
                        nRow.createCell(j+1).setCellValue(Integer.parseInt("0"));
                        j++;
                    }
                }
                i++;
            }
        }
        sheet.setColumnWidth(0, 20 * 256);  //设置列宽，20个字符宽
        sheet.setColumnWidth(1, 20 * 256);  //设置列宽，20个字符宽
        sheet.setColumnWidth(2, 25 * 256);  //设置列宽，20个字符宽
        sheet.setColumnWidth(3, 25 * 256);  //设置列宽，20个字符宽
        sheet.setColumnWidth(4, 25 * 256);  //设置列宽，20个字符宽
        sheet.setColumnWidth(5, 25 * 256);  //设置列宽，20个字符宽
        sheet.setColumnWidth(6, 25 * 256);  //设置列宽，20个字符宽
        sheet.setColumnWidth(7, 25 * 256);  //设置列宽，20个字符宽
        sheet.setColumnWidth(8, 25 * 256);  //设置列宽，20个字符宽
        sheet.setColumnWidth(9, 25 * 256);  //设置列宽，20个字符宽
        sheet.setColumnWidth(10, 25 * 256);  //设置列宽，20个字符宽
        sheet.setColumnWidth(11, 25 * 256);  //设置列宽，20个字符宽
        sheet.setColumnWidth(12, 25 * 256);  //设置列宽，20个字符宽


        fileName += ".xlsx";
        try {
           FileOutputStream fout = new FileOutputStream("/tmp/"+fileName);
//            FileOutputStream fout = new FileOutputStream("D:\\"+fileName);
            wb.write(fout);
            fout.close();
            wb.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

//        // TODO 先不发送,看看效果
//        String path = ExportExcelFile.exportExcelMapToQiNiu(dataArrayList, excelTitle, mapKeys, "test-send" + "-" + sdf2.format(tm));
        List<File> attachments = new ArrayList<File>();
        File affix = new File("/tmp/"+fileName);
        attachments.add(affix);

        String mailContent = "";
        mailContent = "-----导出-----" + "<br/>";


        List<Map<String, Object>> excels = new ArrayList<Map<String, Object>>();

        MailInfo mailInfo = new MailInfo();
        mailInfo.setMailServerHost("smtp.exmail.qq.com"); // 邮箱服务器
        mailInfo.setMailServerPort("465");
        //不要验证
        mailInfo.setValidate(true);
        // 以下是发送方信息
        mailInfo.setUserName("yinlin.shen@banff-tech.com");
        mailInfo.setPassword("woaizhu131");// 您的邮箱密码
        mailInfo.setFromAddress("yinlin.shen@banff-tech.com");
        // 以下是接收方信息
        mailInfo.setToAddress(mail);
        mailInfo.setSubject("业务数据导出");
        mailInfo.setContent(mailContent);







        mailInfo.setAttachments(attachments);

        mailInfo.setContentType("text/html");//HTML格式：text/html，纯文本格式：text/plain
        // 这个类主要来发送邮件
        MailSender.sendMail(mailInfo);//发送邮件




        resultMap.put("dpList",dpList);
        return resultMap;
    }






    public static Map<String, Object> queryKeyWordBox(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException  {
        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> result = ServiceUtil.returnSuccess();
        Locale locale = (Locale) context.get("locale");

        String name = (String) context.get("name");
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        //当事人标识
        String partyId = (String) userLogin.get("partyId");
        Map<String, Object> myGroup = getMyGroup(delegator, partyId);
        String partyGroupId = (String) myGroup.get("partyId");


//        delegator.makeValue("KeyWordBox",
//                UtilMisc.toMap("kId",(String) delegator.getNextSeqId("KeyWordBox"),"entityId",partyGroupId, "name",name,"fromDate", org.apache.ofbiz.base.util.UtilDateTime.nowTimestamp()
//                ));

        List<Map<String,Object>> returnList = new ArrayList<Map<String, Object>>();

        List<GenericValue> keyWordBoxList = EntityQuery.use(delegator).from("KeyWordBox").where("entityId",partyGroupId).queryList();
        if(null!=keyWordBoxList&& keyWordBoxList.size()>0){
            for(GenericValue gv : keyWordBoxList){
                Map<String,Object> rowMap = new HashMap<String, Object>();
                rowMap = gv.getAllFields();
                String keyWordName = gv.getString("name");
                Long rowCount = EntityQuery.use(delegator).from("ProductAndRoleAndKeyWord").where("keyword",keyWordName).queryCount();

                rowMap.put("keywordCount",rowCount);
                returnList.add(rowMap);
            }
        }



        result.put("keyWordList",returnList);

        return result;
    }


    public static Map<String, Object> queryPermisionJson(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();

        GenericValue permJson = EntityQuery.use(delegator).from("PermisionJson").where( ).queryFirst();


        if(null!=permJson){
            resultMap.put("data",permJson.getString("data"));
        }

        return resultMap;
    }
    /**
     * Query InventoryWorkList
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryInventoryWorkList(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String loginPartyId = userLogin.getString("partyId");

        Map<String,Object> myGroup = getMyGroup(delegator, loginPartyId);
        String partyGroupId = (String) myGroup.get("partyId");


        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String viewIndexStr = (String) context.get("viewIndex");
        String viewSizeStr = (String) context.get("viewSize");
        String workEffortTypeId = (String) context.get("workEffortTypeId");


        //Default Value
        Long count = 0l;
        int viewSize = 10;
        int viewIndex = 0;
        if (UtilValidate.isNotEmpty(viewSizeStr)) {
            viewSize = Integer.parseInt(viewSizeStr);
        }
        if (UtilValidate.isNotEmpty(viewIndexStr)) {
            viewIndex = Integer.parseInt(viewIndexStr);
        }



        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();

        // Query ProductCategoryImage
        List<String> orderBy = UtilMisc.toList("-createdDate");
        PagedList<GenericValue> workerPageList = null;

        Set<String> typeSet = new HashSet<String>();
        typeSet.add("OUT_WORKER" );
        typeSet.add("PUT_WORKER" );
        typeSet.add("SET_WORKER" );
        EntityCondition typeCondi = EntityCondition
                .makeCondition("workEffortTypeId",EntityOperator.IN, typeSet);

        if(UtilValidate.isEmpty(workEffortTypeId)||workEffortTypeId.toLowerCase().equals("all")){
            workerPageList = EntityQuery.use(delegator).from("WorkEffort").
                    where(typeCondi).orderBy(orderBy).queryPagedList(viewIndex, viewSize);
            count = EntityQuery.use(delegator).from("WorkEffort").where(typeCondi).queryCount();
        }else{
            workerPageList = EntityQuery.use(delegator).from("WorkEffort").
                    where("workEffortTypeId", workEffortTypeId).orderBy(orderBy).queryPagedList(viewIndex, viewSize);
            count = EntityQuery.use(delegator).from("WorkEffort").where("workEffortTypeId", workEffortTypeId).queryCount();
        }

        List<GenericValue> workerList = workerPageList.getData();


        if (null!= workerList && workerList.size() > 0) {
            for (GenericValue gv : workerList) {

                Map<String, Object> rowMap = new HashMap<String, Object>();

                rowMap.put("date", sdf.format(gv.get("createdDate")));

                String workEffortId = gv.getString("workEffortId");

                //找到工人
                GenericValue worker = EntityQuery.use(delegator).from("WorkEffortPartyAssignment").where("workEffortId", workEffortId).queryFirst();
                if(null== worker){
                    continue;
                }
                Debug.logInfo("row-worker:"+worker,module);

                String partyId = worker.getString("partyId");


                Map<String,Object> rowGroup = getMyGroup(delegator, partyId);
                String rowGroupId = (String) rowGroup.get("partyId");

                if(!rowGroupId.equals(partyGroupId)){
                    continue;
                }


                String rowWorkEffortTypeId = gv.getString("workEffortTypeId");



                String typeStr = "入";

                if(rowWorkEffortTypeId.equals("OUT_WORKER")){
                    typeStr = "出";
                }
                if(rowWorkEffortTypeId.equals("SET_WORKER")){
                    typeStr = "盘";
                }



                rowMap.put("actionName",typeStr);

                rowMap.put("workEffortId",workEffortId);




                //找到产品
                GenericValue goodProd = EntityQuery.use(delegator).from("WorkEffortGoodStandard").where("workEffortId", workEffortId).queryFirst();


                String productId = goodProd.getString("productId");

                GenericValue product = EntityQuery.use(delegator).from("Product").where("productId", productId).queryFirst();

                GenericValue supplierProduct = EntityQuery.use(delegator).from("SupplierProduct").where("productId", productId).queryFirst();

                rowMap.put("vender","无");

                if(supplierProduct!=null){
                    GenericValue supplierInfo = EntityQuery.use(delegator).from("PartyGroup").where("partyId", supplierProduct.getString("partyId")).queryFirst();
                    if(supplierInfo!=null){
                        rowMap.put("vender",supplierInfo.getString("groupName"));
                    }
                }

                rowMap.put("productName",product.getString("productName"));



                rowMap.put("workerInfo", queryPersonBaseInfo(delegator, partyId));

                String primaryProductCategoryId = product.getString("primaryProductCategoryId");

                GenericValue productCategory = EntityQuery.use(delegator).from("ProductCategory").where("productCategoryId", primaryProductCategoryId).queryFirst();

                rowMap.put("categoryName",productCategory==null?"未知":productCategory.getString("categoryName"));


                GenericValue inventoryItemDetail = EntityQuery.use(delegator).from("InventoryItemDetail").where("workEffortId", workEffortId).queryFirst();

                if(inventoryItemDetail==null){
                    continue;
                }else{
                    rowMap.put("quantityOnHandDiff",inventoryItemDetail.get("quantityOnHandDiff"));
                }

                String inventoryItemId = inventoryItemDetail.getString("inventoryItemId");

                String inventoryItemDetailSeqId = inventoryItemDetail.getString("inventoryItemDetailSeqId");




                rowMap.put("inventoryItemDetailSeqId",inventoryItemDetailSeqId);
                GenericValue outWorker = EntityQuery.use(delegator).from("WorkEffort").where("locationDesc",inventoryItemDetailSeqId).queryFirst();
                rowMap.put("status","可用");
                if(null!=outWorker){
                    rowMap.put("status", "不可用");
                }



                returnList.add(rowMap);
            }
        }


        resultMap.put("inventoryWorkerList", returnList);
        resultMap.put("count", count.toString());

        return resultMap;
    }
    /**
     * queryWorkLogs
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryInventoryDetails(DispatchContext dctx, Map<String, Object> context)
            throws GenericEntityException, GenericServiceException {

        // Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Locale locale = (Locale) context.get("locale");
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String loginPartyId = userLogin.getString("partyId");
        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();


        String   groupPersons  =  (String) context.get("groupPersons");
        String   dateRange  =  (String) context.get("dateRange");






        Set<String> partySet = new HashSet<String>();

        // 人的筛选维度
        EntityCondition partyCondition = null;
        if(UtilValidate.isNotEmpty(groupPersons)){
            for(String containParty :groupPersons.split(",")){
                partySet.add(containParty);
            }
            partyCondition = EntityCondition
                    .makeCondition("partyId",EntityOperator.IN, partySet);
        }

        //筛选时间起
        EntityCondition dateConditionStart = null;
        //筛选时间止
        EntityCondition dateConditionEnd = null;
        Date date = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        String workEffortTypeId = (String) context.get("workEffortTypeId");
        if(UtilValidate.isNotEmpty(dateRange)){

            calendar.setTime(date);
            if(dateRange.equals("MONTH")){
                calendar.add(Calendar.MONTH, -1);
                dateConditionStart = EntityCondition
                        .makeCondition("fromDate",EntityOperator.GREATER_THAN_EQUAL_TO, Timestamp.valueOf(sdf.format(calendar.getTime())));

            }if(dateRange.equals("THREE_MONTH")){
                calendar.add(Calendar.MONTH, -3);
                dateConditionStart = EntityCondition
                        .makeCondition("fromDate",EntityOperator.GREATER_THAN_EQUAL_TO, Timestamp.valueOf(sdf.format(calendar.getTime())));


            }if(dateRange.equals("SIX_MONTH")){
                calendar.add(Calendar.MONTH, -6);
                dateConditionStart = EntityCondition
                        .makeCondition("fromDate",EntityOperator.GREATER_THAN_EQUAL_TO, Timestamp.valueOf(sdf.format(calendar.getTime())));


            }if(dateRange.equals("YEAR")){
                calendar.add(Calendar.YEAR, -1);
                dateConditionStart = EntityCondition
                        .makeCondition("fromDate",EntityOperator.GREATER_THAN_EQUAL_TO, Timestamp.valueOf(sdf.format(calendar.getTime())));

            }

        }else{
            calendar.add(Calendar.WEEK_OF_YEAR, -1);
            dateConditionStart = EntityCondition
                    .makeCondition("fromDate",EntityOperator.GREATER_THAN_EQUAL_TO, Timestamp.valueOf(sdf.format(calendar.getTime())));

        }
        dateConditionEnd = EntityCondition
                .makeCondition("fromDate",EntityOperator.LESS_THAN_EQUAL_TO,Timestamp.valueOf(sdf.format(date)));

        if(UtilValidate.isNotEmpty(dateRange)&&dateRange.indexOf("/")>0){
            String start  = dateRange.substring(0,dateRange.indexOf("/"));
            String end   =  dateRange.substring(dateRange.indexOf("/")+1);
            dateConditionStart = EntityCondition
                    .makeCondition("fromDate",EntityOperator.GREATER_THAN_EQUAL_TO, Timestamp.valueOf(start));

            dateConditionEnd = EntityCondition
                    .makeCondition("fromDate",EntityOperator.LESS_THAN_EQUAL_TO,Timestamp.valueOf(end));
        }





        EntityCondition genericStatusCondition = EntityCondition
                .makeCondition("currentStatusId",EntityOperator.EQUALS,"CAL_IN_PLANNING");

        //时间的起与止
        EntityCondition dateConditions = EntityCondition
                .makeCondition(dateConditionStart,EntityOperator.AND,dateConditionEnd);

        EntityCondition dateAndStatusConditions = EntityCondition
                .makeCondition(dateConditions,EntityOperator.AND,genericStatusCondition);


        EntityCondition genericTypeCondition = EntityCondition
                .makeCondition("workEffortTypeId",EntityOperator.EQUALS,"TASK");
        if(workEffortTypeId==null||workEffortTypeId.toLowerCase().equals("all")){
            genericTypeCondition = EntityCondition
                    .makeCondition();
        }
        EntityCondition bigCondi = EntityCondition
                .makeCondition(genericTypeCondition,EntityOperator.AND,dateAndStatusConditions);


        EntityCondition bigConditionAndPartyCondition = null;
        if(partyCondition!=null){
            bigConditionAndPartyCondition =
                    EntityCondition.makeCondition(bigCondi, EntityOperator.AND, partyCondition);

        }

        Map<String,String> keyMap = new HashMap<String, String>();



        List<String> orderBy = new ArrayList<String>();
        orderBy.add("-fromDate");
        List<GenericValue> workEffortList = delegator.findList("WorkEffortPartyAssignmentAll", bigConditionAndPartyCondition!=null?bigConditionAndPartyCondition:bigCondi , null,
                orderBy, null, false);

        if(workEffortList.size()>0){
            for(GenericValue gv : workEffortList){
                Map<String,String> partyKeyMap = new HashMap<String, String>();
                Map<String,Object> rowMap = new HashMap<String, Object>();

                String workEffortId = gv.getString("workEffortId");
                GenericValue originator= EntityQuery.use(delegator).from("WorkEffortPartyAssignment").where("workEffortId",workEffortId,"roleTypeId","ORIGINATOR").queryFirst();
                List<GenericValue> workers= EntityQuery.use(delegator).from("WorkEffortPartyAssignment").where("workEffortId",workEffortId,"roleTypeId","WORKER").queryList();

                String originatorPartyId  = originator.getString("partyId");
                rowMap.put("originatorInfo",queryPersonBaseInfo(delegator,originatorPartyId));
                List<Map<String,String>> workerRowList = new ArrayList<Map<String, String>>();
                if(workers!=null){
                    for(GenericValue rowWork : workers){
                        String partyRowWork = rowWork.getString("partyId");
                        if(!partyKeyMap.containsKey(partyRowWork)){
                            workerRowList.add(queryPersonBaseInfo(delegator,partyRowWork));
                            partyKeyMap.put(partyRowWork,null);
                        }
                    }
                }


                GenericValue workEffortNoteAndData= EntityQuery.use(delegator).from("WorkEffortNoteAndData").where("workEffortId",workEffortId).queryFirst();


                rowMap.put("workEffortId",workEffortId);
                rowMap.put("groupPersons",workerRowList);
                rowMap.put("description",gv.getString("workEffortName"));
                rowMap.put("date",sdf.format(gv.get("fromDate")));
//                rowMap.put("location",getLocationFromPosition(workEffortNoteAndData.getString("noteInfo")));



                List<GenericValue> contents= EntityQuery.use(delegator).from("WorkEffortAndDataResource").where("workEffortId",workEffortId).queryList();

                rowMap.put("contents",contents);

                if(!keyMap.containsKey(workEffortId)){
                    returnList.add(rowMap);
                    keyMap.put(workEffortId,"");
                }
            }
        }

        resultMap.put("inventoryDetails",returnList);
        return resultMap;
    }

    public static Map<String, Object> queryPartyInfo(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        String partyId = (String) context.get("partyId");
        resultMap.put("partyInfo",queryPersonBaseInfo(delegator,partyId));

        return resultMap;
    }

    /**
     * 查询搜索历史
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryHistory(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = userLogin.getString("partyId");
        List<GenericValue> historyList = EntityQuery.use(delegator).from("QueryHistory").where(
                "partyId", partyId).queryList();

        resultMap.put("historyList",historyList);

        return resultMap;
    }

    /**
     * queryDeliveryPlan
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryDeliveryPlan(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = userLogin.getString("partyId");
        String selectDate = (String) context.get("selectDate");



        Map<String,Object> myGroup = getMyGroup(delegator, partyId);
        String partyGroupId = (String) myGroup.get("partyId");

        Set<String> fieldSet = new HashSet<String>();
        fieldSet.add("planId");


        EntityCondition findConditions = EntityCondition.makeCondition("payToParty", EntityOperator.EQUALS, partyGroupId);
        EntityCondition findConditions2 = EntityCondition.makeCondition("planId", EntityOperator.EQUALS, partyGroupId+"/"+selectDate);
        EntityCondition genericCondition = EntityCondition.makeCondition(findConditions, EntityOperator.AND, findConditions2);


        List<GenericValue> planDataList = EntityQuery.use(delegator).from("DeliveryPlansItem").where(
                genericCondition).queryList();
        // 是否有历史数据
        List<GenericValue> historyData = delegator.findList("DeliveryPlans",
                findConditions2, fieldSet,
                null, null, false);
        if( historyData !=null && historyData.size()>0){
            resultMap.put("histroyData",historyData);
        }else{
            resultMap.put("histroyData",null);
        }

        Map<String,Object> channelMap = new HashMap<String, Object>();
        List<Map<String,Object>> returnList = new ArrayList<Map<String, Object>>();
        Debug.logInfo("planDataList:"+planDataList,module);
        String beforeChannel = null;
        int count = 0;
        if(planDataList.size()>0){
            for(GenericValue gv : planDataList){
                Map<String,Object> rowMap =new HashMap<String, Object>();
                rowMap = gv.getAllFields();

                int quantity =  Integer.parseInt(gv.getString("quantity").equals("null")?"0":gv.getString("quantity"));
                count+=quantity;
                returnList.add(rowMap);
            }
        }

//        if(null!= planDataList){
//
//
//
//            for(int i = 0 ; i < planDataList.size();i++){
//                GenericValue gv = (GenericValue) planDataList.get(i);
//                Map<String,Object> rowProduct = gv.getAllFields();
//                List<Map<String,Object>> rowProductList = new ArrayList<Map<String, Object>>();
//                Map<String,Object> rowChannelMap = new HashMap<String, Object>();
//
//                String channelId = (String) gv.get("enumId");
//
//                Debug.logInfo("channelId:"+channelId,module);
//                Debug.logInfo("before channelId:"+beforeChannel,module);
//                Debug.logInfo("before channelId:"+beforeChannel,module);
//                if(null == beforeChannel){
//
//                    rowProductList.add(rowProduct);
//
//                    rowChannelMap.put("enumId", channelId);
//                    rowChannelMap.put("enumDescription",channelId);
//                    rowChannelMap.put("productList",rowProductList);
//
//                    channelMap.put(channelId,rowChannelMap);
//                }
//
//                if(null!=beforeChannel&& beforeChannel.equals(channelId)){
//                    rowChannelMap = (HashMap<String, Object>) channelMap.get(channelId);
//                    rowProductList = (ArrayList<Map<String, Object>>) rowChannelMap.get("productList");
//                    rowProductList.add(rowProduct);
//                    rowChannelMap.put("productList",rowProductList);
//                    channelMap.put(channelId,rowChannelMap);
//                }
//
//
//                if(null != beforeChannel && !beforeChannel.equals(channelId)){
//
//                    if(channelMap.get(channelId)==null){
//                    rowProductList.add(rowProduct);
//                    rowChannelMap.put("enumId", channelId);
//                    rowChannelMap.put("enumDescription",channelId);
//                    rowChannelMap.put("productList",rowProductList);
//                    channelMap.put(channelId,rowChannelMap);
//                    channelMap =  new HashMap<String, Object>();
//                    }else{
//                        rowChannelMap = (HashMap<String, Object>) channelMap.get(channelId);
//                        rowProductList = (ArrayList<Map<String, Object>>) rowChannelMap.get("productList");
//                        rowProductList.add(rowProduct);
//                        rowChannelMap.put("productList",rowProductList);
//                        channelMap.put(channelId,rowChannelMap);
//                    }
//                }
//
//                beforeChannel = channelId;
//            }
//        }
        resultMap.put("dayPlans",returnList);
        resultMap.put("selectDate",selectDate);
        resultMap.put("count",count+"");


        return resultMap;
    }


    public static Map<String, Object> productionTemp(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = userLogin.getString("partyId");
        GenericValue relation = EntityQuery.use(delegator).from("PartyRelationship").where(
                "partyIdFrom", partyId, "partyRelationshipTypeId", "OWNER" ).queryFirst();

        String partyGroupId = relation.getString("partyIdTo");

        GenericValue facility =  EntityQuery.use(delegator).from("Facility").where(
                "ownerPartyId", partyGroupId ).queryFirst();

        String facilityId = facility.getString("facilityId");

        String type = (String) context.get("type");

        resultMap.put("productList",EntityQuery.use(delegator).from("ProductionTemp").where(
                "facilityId", facilityId ,"type",type).queryList());

        return resultMap;
    }


    /**
     * 我的同事
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryMyColleagues(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = userLogin.getString("partyId");

        List<Map<String, String>> returnList = new ArrayList<Map<String, String>>();

        Map<String, Object> myGroup = getMyGroup(delegator, partyId);
        String partyGroupId = (String) myGroup.get("partyId");


        List<GenericValue> colleaguesList = EntityQuery.use(delegator).from("PartyRelationship").where(
                "partyIdFrom", partyGroupId, "partyRelationshipTypeId", "EMPLOYMENT" ).queryList();


        if(colleaguesList.size()>0){
            for(GenericValue gv : colleaguesList){
                    String partyIdTo  = gv.getString("partyIdTo");
                   Map<String,String> personInfo =    queryPersonBaseInfo(delegator,partyIdTo);
                personInfo.put("partyId",partyIdTo);
                returnList.add(personInfo);
            }
        }

        resultMap.put("colleaguesList",returnList);
        return resultMap;
    }



    /**
     * queryMyPurchaseOrderList
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryMyPurchaseOrderList(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = userLogin.getString("partyId");

        List<Map<String,Object>> returnList = new ArrayList<Map<String, Object>>();


        Map<String,Object> myGroup = getMyGroup(delegator,partyId);
        String partyGroupId = (String) myGroup.get("partyId");

        Set<String> fieldSet = new HashSet<String>();
        fieldSet.add("orderId");
        fieldSet.add("partyId");
        fieldSet.add("statusId");
        fieldSet.add("currencyUom");
        fieldSet.add("grandTotal");
        fieldSet.add("productId");
        fieldSet.add("quantity");
        fieldSet.add("unitPrice");
        fieldSet.add("isViewed");
        fieldSet.add("roleTypeId");
        fieldSet.add("orderDate");
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        EntityCondition findConditions = EntityCondition.makeCondition("roleTypeId", EntityOperator.EQUALS, "BILL_TO_CUSTOMER");
        EntityCondition findConditions2 = EntityCondition.makeCondition("partyId", EntityOperator.EQUALS, partyGroupId);
        EntityCondition genericCondition = EntityCondition.makeCondition(findConditions, EntityOperator.AND, findConditions2);
        List<GenericValue> queryOrderList = delegator.findList("PurchaseOrderHeaderItemAndRoles",
                genericCondition, fieldSet,
                UtilMisc.toList("orderDate DESC"), null, false);
        if(queryOrderList.size()>0){
            for(GenericValue order : queryOrderList){
                Map<String, Object> rowMap = new HashMap<String, Object>();

                rowMap = order.getAllFields();
//isViewed
                String productId = (String) order.get("productId");
                String isViewed  = (String) order.get("isViewed");
                GenericValue product = delegator.findOne("Product", UtilMisc.toMap("productId", productId), false);

                rowMap.put("isViewed",isViewed);

                rowMap.put("productName", "" + product.get("productName"));

                rowMap.put("detailImageUrl", (String) product.get("detailImageUrl"));

                String statusId = (String) order.get("statusId");

                if(statusId.equals("ORDER_APPROVED") && null!=isViewed&&isViewed.equals("Y")){
                    rowMap.put("statusId","ORDER_VIEWED");
                }

                String orderId = order.getString("orderId");


                GenericValue orderNote = EntityQuery.use(delegator).from("OrderHeaderNoteView").
                        where("orderId", orderId,"noteName", "供应商行为", "noteInfo", "ACCEP").queryFirst();
                if(null!=orderNote && !statusId.equals("ORDER_COMPLETED")){
                    rowMap.put("statusId","ACCEP");
                }
                GenericValue orderNote2 = EntityQuery.use(delegator).from("OrderHeaderNoteView").
                        where("orderId", orderId,"noteName", "供应商行为", "noteInfo", "FAHUO").queryFirst();
                if(null!=orderNote2 && !statusId.equals("ORDER_COMPLETED")){
                    rowMap.put("statusId","FAHUO");
                }

                GenericValue custOrderRole = EntityQuery.use(delegator).from("OrderRole").where("orderId", orderId, "roleTypeId", "SHIP_FROM_VENDOR").queryFirst();

                String payToPartyId =custOrderRole.getString("partyId");

                rowMap.put("salesPersonInfoMap", queryBomPersonBaseInfo(delegator, payToPartyId,partyGroupId));

                rowMap.put("custPersonInfoMap", myGroup);


                rowMap.put("partyGroupId", payToPartyId);





                String uomId = product.getString("quantityUomId");
                GenericValue uom =  EntityQuery.use(delegator).from("Uom").where(
                        "uomId", uomId).queryFirst();
                String uomDescription = uom.getString("description");
                String cndescription = UtilProperties.getMessage(resourceUiLabels, "Uom.description." + uomId, new Locale("zh"));
                rowMap.put("uomDescription",cndescription.indexOf("Uom.description")>-1?uomDescription:cndescription);

                rowMap.put("orderDate", sdf.format((Timestamp)order.get("orderDate")));




                returnList.add(rowMap);
            }
        }


        resultMap.put("orderList",returnList);
//        resultMap.put("partyGroupId",partyGroupId);
        return resultMap;
    }


    /**
     * quickQueryCount
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> quickQueryCount(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = userLogin.getString("partyId");

        Map<String,Object> myGroup = getMyGroup(delegator,partyId);
        String partyGroupId = (String) myGroup.get("partyId");
        partyId= partyGroupId;
        Long supplierCount = EntityQuery.use(delegator).from("PartyRelationship").where(
                "partyIdFrom", partyId, "roleTypeIdTo", "LEAD", "partyRelationshipTypeId", "LEAD_OWNER" ).queryCount();
        Long rawMaterialsCount = EntityQuery.use(delegator).from("ProductAndRole").where(
                "partyId", partyId, "roleTypeId", "ADMIN", "productTypeId", "RAW_MATERIAL").queryCount();
        Long finishGoodCount = EntityQuery.use(delegator).from("ProductAndRole").where(
                "partyId", partyId, "roleTypeId", "ADMIN", "productTypeId", "FINISHED_GOOD").queryCount();
        List<String> types = new ArrayList<String>();
        types.add("ORDER_CREATED");
        types.add("ORDER_APPROVED");
        EntityCondition findConditions = EntityCondition.makeCondition("roleTypeId", EntityOperator.EQUALS, "SHIP_FROM_VENDOR");
        EntityCondition findConditions2 = EntityCondition.makeCondition("partyId", EntityOperator.EQUALS, partyId);
        EntityCondition genericCondition = EntityCondition.makeCondition(findConditions, EntityOperator.AND, findConditions2);
        EntityCondition statusConditions = EntityCondition.makeCondition("statusId", EntityOperator.NOT_IN, types);
        EntityCondition genericCondition2 = EntityCondition.makeCondition(genericCondition, EntityOperator.AND, statusConditions);
        Long salesOrderCount = EntityQuery.use(delegator).from("PurchaseOrderHeaderItemAndRoles").where(
                genericCondition2).queryCount();
        EntityCondition findConditionsCust = EntityCondition.makeCondition("roleTypeId", EntityOperator.EQUALS, "BILL_TO_CUSTOMER");
        EntityCondition genericCondition3 = EntityCondition.makeCondition(findConditionsCust, EntityOperator.AND, findConditions2);
        Long purchaseOrderCount = EntityQuery.use(delegator).from("PurchaseOrderHeaderItemAndRoles").where(
                genericCondition3).queryCount();


        //生产计划

        GenericValue facility =  EntityQuery.use(delegator).from("Facility").where(
                "ownerPartyId", partyId ).queryFirst();
        String facilityId = facility.getString("facilityId");
        Long productionsCount = EntityQuery.use(delegator).from("WorkEffortAndGoods").where(
                "workEffortTypeId", "PROD_ORDER_HEADER", "facilityId", facilityId).queryCount();




        Map<String,Object> queryCountMap = new HashMap<String, Object>();
        queryCountMap.put("supplierCount",supplierCount.intValue());
        queryCountMap.put("rawMaterialsCount",rawMaterialsCount.intValue());
        queryCountMap.put("finishGoodCount",finishGoodCount.intValue());
        queryCountMap.put("salesOrderCount",salesOrderCount.intValue());
        queryCountMap.put("purchaseOrderCount",purchaseOrderCount.intValue());
        queryCountMap.put("productionsCount",productionsCount.intValue());

        resultMap.put("queryCountMap",queryCountMap);
        return resultMap;
    }


    /**
     * 我的销售订单?
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryMySalesOrderList(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = userLogin.getString("partyId");


        Map<String,Object> myGroup = getMyGroup(delegator,partyId);
        String partyGroupId = (String) myGroup.get("partyId");


        List<Map<String,Object>> returnList = new ArrayList<Map<String, Object>>();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        Set<String> fieldSet = new HashSet<String>();
        fieldSet.add("orderId");
        fieldSet.add("partyId");
        fieldSet.add("statusId");
        fieldSet.add("currencyUom");
        fieldSet.add("grandTotal");
        fieldSet.add("productId");
        fieldSet.add("quantity");
        fieldSet.add("unitPrice");
        fieldSet.add("isViewed");
        fieldSet.add("roleTypeId");
        fieldSet.add("orderDate");

                List<String> types = new ArrayList<String>();
        types.add("ORDER_CREATED");
        types.add("ORDER_APPROVED");

//        EntityCondition findConditions = EntityCondition
//                .makeCondition("uomTypeId", EntityOperator.NOT_IN, types);

        EntityCondition findConditions = EntityCondition.makeCondition("roleTypeId", EntityOperator.EQUALS, "SHIP_FROM_VENDOR");
        EntityCondition findConditions2 = EntityCondition.makeCondition("partyId", EntityOperator.EQUALS, partyGroupId);
        EntityCondition genericCondition = EntityCondition.makeCondition(findConditions, EntityOperator.AND, findConditions2);

        EntityCondition statusConditions = EntityCondition.makeCondition("statusId", EntityOperator.NOT_IN,types);

        EntityCondition genericCondition2 = EntityCondition.makeCondition(genericCondition, EntityOperator.AND, statusConditions);


        List<GenericValue> queryOrderList = delegator.findList("PurchaseOrderHeaderItemAndRoles",
                genericCondition2, fieldSet,
                UtilMisc.toList("orderDate DESC"), null, false);
        if(queryOrderList.size()>0){
            for(GenericValue order : queryOrderList){
                Map<String, Object> rowMap = new HashMap<String, Object>();

                rowMap = order.getAllFields();

                String productId = (String) order.get("productId");
                String isViewed = (String) order.get("isViewed");

                GenericValue product = delegator.findOne("Product", UtilMisc.toMap("productId", productId), false);

                rowMap.put("productName", "" + product.get("productName"));
                rowMap.put("isViewed",isViewed);

                rowMap.put("detailImageUrl", (String) product.get("detailImageUrl"));

                String statusId = (String) order.get("statusId");


                if(statusId.equals("ORDER_APPROVED") && null!=isViewed&&isViewed.equals("Y")){
                    rowMap.put("statusId","ORDER_VIEWED");
                }

                String orderId = order.getString("orderId");

                GenericValue orderNote = EntityQuery.use(delegator).from("OrderHeaderNoteView").
                        where("orderId", orderId,"noteName", "供应商行为", "noteInfo", "ACCEP").queryFirst();
                if(null!=orderNote && !statusId.equals("ORDER_COMPLETED")){
                    rowMap.put("statusId","ACCEP");
                }
                GenericValue orderNote2 = EntityQuery.use(delegator).from("OrderHeaderNoteView").
                        where("orderId", orderId,"noteName", "供应商行为", "noteInfo", "FAHUO").queryFirst();
                if(null!=orderNote2 && !statusId.equals("ORDER_COMPLETED")){
                    rowMap.put("statusId","FAHUO");
                }


                GenericValue custOrderRole = EntityQuery.use(delegator).from("OrderRole").where("orderId", orderId, "roleTypeId", "BILL_TO_CUSTOMER").queryFirst();

                String custId =custOrderRole.getString("partyId");

                rowMap.put("salesPersonInfoMap", queryBomPersonBaseInfo(delegator, partyId,partyGroupId));

                rowMap.put("custPersonInfoMap", queryBomPersonBaseInfo(delegator, custId, partyGroupId));
                String uomId = product.getString("quantityUomId");
                GenericValue uom =  EntityQuery.use(delegator).from("Uom").where(
                        "uomId", uomId).queryFirst();
                String uomDescription = uom.getString("description");
                String cndescription = UtilProperties.getMessage(resourceUiLabels, "Uom.description." + uomId, new Locale("zh"));
                rowMap.put("uomDescription",cndescription.indexOf("Uom.description")>-1?uomDescription:cndescription);
                rowMap.put("orderDate", sdf.format(order.get("orderDate")));
                returnList.add(rowMap);
            }
        }


        resultMap.put("orderList",returnList);
        return resultMap;
    }

    /**
     * queryProductionRouting
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryProductionRouting(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = userLogin.getString("partyId");

        Map<String,Object> myGroup = getMyGroup(delegator,partyId);
        String partyGroupId = (String) myGroup.get("partyId");


//        GenericValue relation = EntityQuery.use(delegator).from("PartyRelationship").where(
//                "partyIdFrom", partyId, "partyRelationshipTypeId", "OWNER" ).queryFirst();
//
//        String partyGroupId = relation.getString("partyIdTo");

        GenericValue facility =  EntityQuery.use(delegator).from("Facility").where(
                "ownerPartyId", partyGroupId ).queryFirst();

        String facilityId = facility.getString("facilityId");




        List<GenericValue> productionList = EntityQuery.use(delegator).from("WorkEffortAndGoods").where(
                "workEffortTypeId", "PROD_ORDER_HEADER","facilityId",facilityId).orderBy("-createdDate").queryList();



        List<Map<String,Object>> returnList = new ArrayList<Map<String, Object>>();


        String workEffortName = null;
        String workEffortId   = null;
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String beforeWorkEffort = null;




        if(productionList.size()>0){
            for(GenericValue gv : productionList){

                Map<String,Object> rowMap = new HashMap<String, Object>();
                List<Map<String,Object>> supplierProductList = new ArrayList<Map<String, Object>>();

                workEffortId = gv.getString("workEffortId");

                String productId = gv.getString("productId");
                GenericValue parentProduct = delegator.findOne("Product", false, UtilMisc.toMap("productId",productId));
                String productionProductName = parentProduct.getString("productName");
                String detailImageUrl =   parentProduct.getString("detailImageUrl");
                String statusId  = gv.getString("statusId");
                String currentStatusId  = gv.getString("currentStatusId");

                rowMap.put("statusId",statusId);

                rowMap.put("currentStatusId",currentStatusId);
                rowMap.put("workEffortId",workEffortId);

                String sdfDate = sdf.format(gv.get("createdDate"));
                //生产数量
                Double estimatedQuantity = (Double) gv.get("estimatedQuantity");
                BigDecimal quantityToProduce = (BigDecimal) gv.get("quantityToProduce");

                rowMap.put("productionDate",sdfDate);

                rowMap.put("estimatedQuantity",estimatedQuantity);
                rowMap.put("quantityToProduce",quantityToProduce);

                rowMap.put("workEffortName", "生产["+productionProductName+"] X ("+estimatedQuantity+") ");
                rowMap.put("productName",productionProductName);
                rowMap.put("detailImageUrl",detailImageUrl);

                GenericValue workEffort = EntityQuery.use(delegator).from("WorkEffort").where(
                        "workEffortId",workEffortId).queryFirst();
                List<GenericValue> childWorkEfforts  = workEffort.getRelated("ChildWorkEffort", UtilMisc.toMap("workEffortTypeId", "PROD_ORDER_TASK"), UtilMisc.toList("priority"), false);
                GenericValue  childWorkEffort = childWorkEfforts.get(0);
                String childWorkEffortId = childWorkEffort.getString("workEffortId");

                List<GenericValue> childWorkEffortGoods = EntityQuery.use(delegator).from("WorkEffortAndGoods").where(
                        "workEffortId",childWorkEffortId).orderBy("-createdDate").queryList();
                if(childWorkEffortGoods.size()>0){
                    for(GenericValue childGoods : childWorkEffortGoods){

                        Map<String,Object> rowProductMap = new HashMap<String, Object>();
                        String childProductId = childGoods.getString("productId");
                        GenericValue childProduct = delegator.findOne("Product", false, UtilMisc.toMap("productId",childProductId));
                        Double childEstimatedQuantity = (Double) childGoods.get("estimatedQuantity");
                        detailImageUrl =   childProduct.getString("detailImageUrl");

                        rowProductMap.put("compProductId",childProductId);
                        rowProductMap.put("childWorkEffortId",childWorkEffortId);
                        rowProductMap.put("productName",childProduct.getString("productName"));
                        rowProductMap.put("estimatedQuantity",childEstimatedQuantity);
                        rowProductMap.put("detailImageUrl",detailImageUrl);


                        List<GenericValue>  suppliers = EntityQuery.use(delegator).from("SupplierProduct").where(
                                "productId", childProductId).queryList();
                        List<Map<String,Object>> supplierList = new ArrayList<Map<String, Object>>();
                        if(suppliers.size()>0){
                            for(GenericValue supplier : suppliers){
                                Map<String,Object> rowSupplier = new HashMap<String, Object>();

                                String supplierPartyId = supplier.getString("partyId");

                                Map<String,String> supplierInfo =  queryBomPersonBaseInfo(delegator, supplierPartyId, partyGroupId);
                                rowSupplier.put("name",supplierInfo.get("aliasCompanyName")+"-"+supplierInfo.get("aliasName") );
                                rowSupplier.put("supplierInfo", supplierInfo);
                                rowSupplier.put("partyId",supplierPartyId);


                                supplierList.add(rowSupplier);
                            }
                        }

                        rowProductMap.put("supplierList",supplierList);





                        String uomId = childProduct.getString("quantityUomId");
                        GenericValue uom =  EntityQuery.use(delegator).from("Uom").where(
                                "uomId", uomId).queryFirst();
                        if(uom!=null){
                            String uomDescription = uom.getString("description");
                            String cndescription = UtilProperties.getMessage(resourceUiLabels, "Uom.description." + uomId, new Locale("zh"));
                            rowProductMap.put("uomDescription",cndescription.indexOf("Uom.description")>-1?uomDescription:cndescription);
                        }else{
                            rowProductMap.put("uomDescription","");
                        }



                        supplierProductList.add(rowProductMap);
                    }
                }



                rowMap.put("supplierProductList",supplierProductList);

                returnList.add(rowMap);
            }
        }


        resultMap.put("productionRunList",returnList);
        return resultMap;
    }


    public static Map<String,Object> getMyGroup (Delegator delegator,String partyId) throws GenericEntityException{

        Map<String,Object> goupMap = new HashMap<String, Object>();

        GenericValue relation = EntityQuery.use(delegator).from("PartyRelationship").where(
                "partyIdTo", partyId, "partyRelationshipTypeId", "EMPLOYMENT" ).queryFirst();
        if(null!=relation){


        String partyGroupId = relation.getString("partyIdFrom");

        GenericValue partyGroup = EntityQuery.use(delegator).from("PartyGroup").where(
                "partyId", partyGroupId).queryFirst();

        goupMap = partyGroup.getAllFields();
        }else{
            return null;
        }
        return goupMap;
    }

    /**
     * queryMyInfo
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryMyInfo(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = userLogin.getString("partyId");

        Map<String,Object> returnMap =new HashMap<String, Object>();

        GenericValue teleContact = EntityQuery.use(delegator).from("TelecomNumberAndPartyView").where("partyId", partyId).queryFirst();


        if (null != teleContact) {
            String contactNumber = (String) teleContact.get("contactNumber");
            returnMap.put("contactTel", contactNumber);
        }


//        GenericValue relation = EntityQuery.use(delegator).from("PartyRelationship").where(
//                "partyIdFrom", partyId, "partyRelationshipTypeId", "OWNER" ).queryFirst();
//
//        String partyGroupId = relation.getString("partyIdTo");
//
//        GenericValue partyGroup = EntityQuery.use(delegator).from("PartyGroup").where(
//                "partyId", partyGroupId).queryFirst();

        Map<String,Object> myGroup = getMyGroup(delegator,partyId);
        String partyGroupId = (String) myGroup.get("partyId");

        String groupName = (String) myGroup.get("groupName");

        returnMap.put("partyGroupId",partyGroupId);
        returnMap.put("groupName",groupName);
        returnMap.put("roleType","OWNER");

        resultMap.put("userInfo",returnMap);
        return resultMap;
    }




    /**
     * Query MyOrderList
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryMyOrderList(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = userLogin.getString("partyId");

        String orderTypeId = (String) context.get("orderTypeId");


        List<GenericValue> orderList = EntityQuery.use(delegator).from("ProductAndRole").where(
                "partyId", partyId, "roleTypeId", "ADMIN", "productTypeId", "RAW_MATERIAL").orderBy("-fromDate").queryList();

        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");



        return resultMap;
    }

    /**
     * 查询原辅料
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryMyRawMaterials(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = userLogin.getString("partyId");
        String productName = (String) context.get("productName");

        Map<String,Object> myGroup = getMyGroup(delegator,partyId);
        String partyGroupId = (String) myGroup.get("partyId");


        List<GenericValue> productList = EntityQuery.use(delegator).from("ProductAndRole").where(
                "partyId", partyGroupId, "roleTypeId", "ADMIN","productTypeId","RAW_MATERIAL").orderBy("-fromDate").queryList();

        List<Map<String,Object>> returnList = new ArrayList<Map<String, Object>>();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if(productList.size()>0){
            for(GenericValue gv : productList){
                Map<String,Object> rowMap = new HashMap<String, Object>();
                String productId = gv.getString("productId");
                rowMap.put("productId",productId);


                rowMap.put("productName",gv.getString("productName"));
                rowMap.put("imagePath",gv.getString("detailImageUrl"));
                try{
                rowMap.put("createdDate",sdf.format(gv.get("fromDate")));
                }catch (Exception e){

                }
                    String uomId = gv.getString("quantityUomId");
                rowMap.put("quantityUomId",uomId);

                GenericValue uom =  EntityQuery.use(delegator).from("Uom").where(
                        "uomId", uomId).queryFirst();
                String uomDescription = "";
                if(null!= uom && uom.get("description")!=null ){
                    uomDescription = uom.getString("description");
                }




                //kucun
                String payToPartyId = (String) gv.get("partyId");
                GenericValue facility = EntityQuery.use(delegator).from("Facility").where("ownerPartyId", payToPartyId).queryFirst();
                String originFacilityId = (String) facility.get("facilityId");
                Map<String, Object> getInventoryAvailableByFacilityMap = dispatcher.runSync("getInventoryAvailableByFacility", UtilMisc.toMap("userLogin", admin,
                        "facilityId", originFacilityId, "productId", productId));
                if (ServiceUtil.isSuccess(getInventoryAvailableByFacilityMap)) {
                    rowMap.put("quantityOnHandTotal", getInventoryAvailableByFacilityMap.get("quantityOnHandTotal"));
                    rowMap.put("availableToPromiseTotal", getInventoryAvailableByFacilityMap.get("availableToPromiseTotal"));
                }




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


                        Map<String,String> supplierInfo =  queryBomPersonBaseInfo(delegator, supplierPartyId, partyGroupId);
                        rowSupplier.put("name",supplierInfo.get("aliasCompanyName")+"-"+supplierInfo.get("aliasName") );
                        rowSupplier.put("supplierInfo", supplierInfo);
                        rowSupplier.put("partyId",supplierPartyId);
//                        GenericValue partyGroup =  EntityQuery.use(delegator).from("PartyGroup").where(
//                                "partyId", supplierPartyId).queryFirst();
//                        //说明该线索人还没登录验证过
//                        if(null ==partyGroup){
//                            GenericValue person =  EntityQuery.use(delegator).from("Person").where(
//                                    "partyId", supplierPartyId).queryFirst();
//                            rowSupplier.put("partyId",supplierPartyId);
//                            rowSupplier.put("name",person.getString("lastName")+person.getString("firstName"));
//                        }else{
//                            rowSupplier.put("partyId",supplierPartyId);
//                            rowSupplier.put("name",partyGroup.getString("groupName"));
//                        }

                        supplierList.add(rowSupplier);
                    }
                }

                rowMap.put("supplierList",supplierList);

                returnList.add(rowMap);
            }
        }

        resultMap.put("rawMaterialsList",returnList);


//        if(returnList!=null && returnList.size()>0 && productName!=null){
//            // query history
//            String qhId = delegator.getNextSeqId("QueryHistory");
//            GenericValue newHistory = delegator.makeValue("QueryHistory", UtilMisc.toMap("qhId",qhId,"partyId",partyId,"name",productName));
//            newHistory.create();
//        }

        return resultMap;
    }


    /**
     * queryMyFinishedGood
     * @param dctx
     * @param context
     * @return
     * @throws GenericEntityException
     * @throws GenericServiceException
     */
    public static Map<String, Object> queryMyFinishedGood(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        // Admin Do Run Service
        GenericValue admin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "admin"));

        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = userLogin.getString("partyId");

        String productName = (String) context.get("productName");

        String keyWord = (String) context.get("keyWord");


        Map<String,String> pkMap = new HashMap<String, String>();



        String viewIndexStr = (String) context.get("viewIndex");
        String viewSizeStr = (String) context.get("viewSize");


        //Default Value
        Long count = 0l;
        int viewSize = 10;
        int viewIndex = 0;
        if (UtilValidate.isNotEmpty(viewSizeStr)) {
            viewSize = Integer.parseInt(viewSizeStr);
        }
        if (UtilValidate.isNotEmpty(viewIndexStr)) {
            viewIndex = Integer.parseInt(viewIndexStr);
        }
        PagedList<GenericValue> productList = null;





        Map<String,Object> myGroup = getMyGroup(delegator,partyId);
        String partyGroupId = (String) myGroup.get("partyId");
        partyId = partyGroupId;


        GenericValue groupFacility = EntityQuery.use(delegator).from("Facility").where(
                "ownerPartyId", partyGroupId).queryFirst();
        String groupFacilityId = groupFacility.getString("facilityId");

//        List<GenericValue> productList = null;
        Debug.logInfo("query成品:"+productName,module);


        EntityCondition findConditionsRole = EntityCondition
                .makeCondition("roleTypeId", "ADMIN");
        EntityCondition findConditionsType = EntityCondition
                .makeCondition("productTypeId","FINISHED_GOOD");

        EntityCondition findConditionsNameAndRole = EntityCondition
                .makeCondition(findConditionsType, EntityOperator.AND, findConditionsRole);


        EntityCondition findConditionsParty = EntityCondition
                .makeCondition("partyId", partyId);

        EntityCondition findConditionsTypeRoleAndParty = EntityCondition
                .makeCondition(findConditionsNameAndRole, EntityOperator.AND, findConditionsParty);


        if(productName!=null && !productName.trim().equals("") && !productName.equals("null")){

            EntityCondition findConditionsName = EntityCondition
                    .makeCondition("productName", EntityOperator.LIKE, "%" + productName + "%");
            EntityCondition findConditionsTypeRoleAndPartyAndName = EntityCondition
                    .makeCondition(findConditionsTypeRoleAndParty, EntityOperator.AND, findConditionsName);
            if(keyWord!=null && !keyWord.equals("") && !keyWord.equals("null")){
                EntityCondition findConditionsKeyWord = EntityCondition
                        .makeCondition("keyword", EntityOperator.LIKE, "%"+keyWord+"%");
                EntityCondition findConditionsTypeRoleAndPartyAndNameAndKey = EntityCondition
                        .makeCondition(findConditionsTypeRoleAndPartyAndName, EntityOperator.AND, findConditionsKeyWord);
//                productList = delegator.findList("ProductAndRoleAndKeyWord",
//                        findConditionsTypeRoleAndPartyAndNameAndKey, null, null, null, true);
                productList = EntityQuery.use(delegator).from("ProductAndRoleAndKeyWord").where(
                        findConditionsTypeRoleAndPartyAndNameAndKey).orderBy( "sequenceNum").queryPagedList(viewIndex, viewSize);
            }else{
//                productList = delegator.findList("ProductAndRole",
//                        findConditionsTypeRoleAndPartyAndName, null, null, null, true);
                 productList = EntityQuery.use(delegator).from("ProductAndRole").where(
                         findConditionsTypeRoleAndPartyAndName).orderBy( "sequenceNum").queryPagedList(viewIndex, viewSize);
            }

        }else{
            if(keyWord!=null && !keyWord.equals("") && !keyWord.equals("null")){
                EntityCondition findConditionsKeyWord = EntityCondition
                        .makeCondition("keyword", EntityOperator.LIKE, "%"+keyWord+"%");
                EntityCondition findConditionsTypeRoleAndPartyAndNameAndKey = EntityCondition
                        .makeCondition(findConditionsTypeRoleAndParty, EntityOperator.AND, findConditionsKeyWord);
//                productList = delegator.findList("ProductAndRoleAndKeyWord",
//                        findConditionsTypeRoleAndPartyAndNameAndKey, null, null, null, true);
                productList = EntityQuery.use(delegator).from("ProductAndRoleAndKeyWord").where(
                        findConditionsTypeRoleAndPartyAndNameAndKey).orderBy( "sequenceNum").queryPagedList(viewIndex, viewSize);
            }else{
                productList = EntityQuery.use(delegator).from("ProductAndRole").where(
                        findConditionsTypeRoleAndParty).orderBy( "sequenceNum").queryPagedList(viewIndex, viewSize);
            }

        }



        Debug.logInfo("queryMyFinishedGood:"+partyGroupId,module);
        Debug.logInfo("~~~~~~productList:"+productList,module);
        Debug.logInfo("productListS:"+productList.getData().size(),module);
        List<Map<String,Object>> returnList = new ArrayList<Map<String, Object>>();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if(productList!=null){
            for(GenericValue gv : productList.getData()){
                Map<String,Object> rowMap = new HashMap<String, Object>();

                String productId = gv.getString("productId");
                Long rowCount = EntityQuery.use(delegator).from("ProductKeyword").where("keyword",keyWord,"productId",productId).queryCount();
                if(rowCount==0 && keyWord!=null && !keyWord.equals("") && !keyWord.equals("null")){
                    Debug.logInfo("rowCount:"+rowCount+"|keyWord:"+keyWord,module);
                    continue;
                }
                if(pkMap.containsKey(productId)){
                    Debug.logInfo("pkMap.containsKey(productId):"+(pkMap.containsKey(productId))+"|productId:"+productId,module);
                    continue;
                }
                pkMap.put(productId,null);
                List<GenericValue> tags =  EntityQuery.use(delegator).from("ProductKeyword").where(
                        "productId", productId,"keywordTypeId","KWT_TAG").queryList();

                rowMap.put("keyWordList",tags);

                String uomParentId = gv.getString("quantityUomId");


                GenericValue uomParent =  EntityQuery.use(delegator).from("Uom").where(
                        "uomId", uomParentId).queryFirst();
                if(null!=uomParent){
                    String uomDescription = uomParent.getString("description");
                    rowMap.put("description",uomDescription);
                    rowMap.put("uomId",uomParentId);
                    String cndescription = UtilProperties.getMessage(resourceUiLabels, "Uom.description." + uomParentId, new Locale("zh"));
                    rowMap.put("uomDescription",cndescription.indexOf("Uom.description")>-1?uomDescription:cndescription);
                }



                rowMap.put("productId",productId);

                rowMap.put("productName",gv.getString("productName"));
                rowMap.put("imagePath",gv.getString("detailImageUrl"));

                rowMap.put("createdDate",sdf.format(gv.get("fromDate")));
                rowMap.put("sequenceNum", gv.get("sequenceNum") );



                //kucun
                String payToPartyId = (String) gv.get("partyId");

                Map<String, Object> getInventoryAvailableByFacilityMap = dispatcher.runSync("getInventoryAvailableByFacility", UtilMisc.toMap("userLogin", admin,
                        "facilityId", groupFacilityId, "productId", productId));
                if (ServiceUtil.isSuccess(getInventoryAvailableByFacilityMap)) {
                    rowMap.put("quantityOnHandTotal", getInventoryAvailableByFacilityMap.get("quantityOnHandTotal"));
                    rowMap.put("availableToPromiseTotal", getInventoryAvailableByFacilityMap.get("availableToPromiseTotal"));
                }





                List<GenericValue> manufComponents  = EntityQuery.use(delegator).from("ProductAssoc").where(
                        "productId", productId,"productAssocTypeId","MANUF_COMPONENT").queryList();


                List<Map<String,Object>> manuList =new ArrayList<Map<String, Object>>();
                if(null!=manufComponents&&manufComponents.size()>0){
                    for(GenericValue row : manufComponents){
                        Map<String,Object> rowComponent = new HashMap<String, Object>();
                        String rowProductId = row.getString("productIdTo");
                        BigDecimal quantity  = (BigDecimal)row.get("quantity");
                        rowComponent.put("productId",rowProductId);
                        rowComponent.put("quantity",quantity.intValue());
                        GenericValue rowProd = EntityQuery.use(delegator).from("Product").where(
                                "productId", rowProductId).queryFirst();
                        rowComponent.put("productName",rowProd.getString("productName"));
                        rowComponent.put("imagePath",rowProd.getString("detailImageUrl"));
                        rowComponent.put("fromDate",sdf.format(row.get("fromDate")));


                        String uomId = rowProd.getString("quantityUomId");


                         GenericValue uom =  EntityQuery.use(delegator).from("Uom").where(
                                 "uomId", uomId).queryFirst();
                        if(null!=uom){
                        String uomDescription = uom.getString("description");
                        rowComponent.put("manufComponentDescription",uomDescription);
                        String cndescription = UtilProperties.getMessage(resourceUiLabels, "Uom.description." + uomId, new Locale("zh"));
                        rowComponent.put("manufComponentZhDescription",cndescription.indexOf("Uom.description")>-1?uomDescription:cndescription);
                        }



                        manuList.add(rowComponent);
                    }
                }
                rowMap.put("manuList",manuList);

                returnList.add(rowMap);
            }
        }

        if(returnList!=null && returnList.size()>0 && productName!=null&&!productName.equals("null")&&!productName.equals("")){
            // query history
            GenericValue queryHistory =  EntityQuery.use(delegator).from("QueryHistory").where(
                    "partyId",userLogin.getString("partyId"),"name",productName).queryFirst();
            if(null == queryHistory){
                String qhId = delegator.getNextSeqId("QueryHistory");
                GenericValue newHistory = delegator.makeValue("QueryHistory", UtilMisc.toMap("qhId",qhId,"partyId",userLogin.getString("partyId"),"name",productName));
                newHistory.create();
            }

        }

        resultMap.put("finishedGoodList",returnList);
        return resultMap;
    }


    public static Map<String, Object> queryChannelList(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();



        Locale locale = (Locale) context.get("locale");

        GenericValue userLogin = (GenericValue) context.get("userLogin");
        String partyId = userLogin.getString("partyId");
        Map<String, Object> myGroup = getMyGroup(delegator, partyId);
        String partyGroupId = (String) myGroup.get("partyId");

        EntityCondition findConditions = EntityCondition
                .makeCondition("enumTypeId", EntityOperator.EQUALS, "CN_SALES_CHANNEL");


        EntityCondition findConditions2 = EntityCondition.makeCondition("enumCode", EntityOperator.LIKE, partyGroupId+"%");
        EntityCondition genericCondition = EntityCondition.makeCondition(findConditions, EntityOperator.AND, findConditions2);


        List<GenericValue> enumeration = EntityQuery.use(delegator).from("Enumeration").where(genericCondition).orderBy(
                "sequenceId").queryList();

        List<Map<String,Object>> returnList = new ArrayList<Map<String, Object>>();

//        for(GenericValue gv : enumeration){
//            Map<String,Object> rowMap = gv.getAllFields();
//            returnList.add(rowMap);
//        }


        resultMap.put("channelList",enumeration);
        return resultMap;
    }

    public static Map<String, Object> queryQuantityUom(DispatchContext dctx, Map<String, Object> context) throws GenericEntityException, GenericServiceException {

        //Service Head
        LocalDispatcher dispatcher = dctx.getDispatcher();
        Delegator delegator = dispatcher.getDelegator();
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();


//        List<String> types = new ArrayList<String>();
//        types.add("CURRENCY_MEASURE");
//
//        EntityCondition findConditions = EntityCondition
//                .makeCondition("uomTypeId", EntityOperator.NOT_IN, types);
        EntityCondition findConditions = EntityCondition
                .makeCondition("uomTypeId", EntityOperator.EQUALS, "BOM_MEASURE");

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

        Map<String,Object> myGroup = getMyGroup(delegator,partyId);
        String partyGroupId = (String) myGroup.get("partyId");

        List<Map<String,Object>> returnList = new ArrayList<Map<String, Object>>();

        List<GenericValue> relationList = EntityQuery.use(delegator).from("PartyRelationshipAndContactMechDetail").where(
                "partyIdFrom", partyGroupId, "roleTypeIdTo", "LEAD","partyRelationshipTypeId","LEAD_OWNER").orderBy("-fromDate").queryList();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String beforePartyId = null;
        Debug.logInfo("relationList:"+relationList,module);
        if(relationList.size()>0){
              for(GenericValue gv : relationList){
                Map<String,Object> rowMap = new HashMap<String, Object>();
                String partyIdTo = gv.getString("partyIdTo");

                  String contactMechTypeId = gv.getString("contactMechTypeId");

                Debug.logInfo("PartyRelationshipAndContactMechDetail=>:"+gv,module);


                if(beforePartyId!=null && beforePartyId.equals(partyIdTo)){
                    rowMap = returnList.get(returnList.size()-1);
                    String tnContactNumber = gv.getString("tnContactNumber");
                    if(null!=tnContactNumber){
                        rowMap.put("tnContactNumber", gv.getString("tnContactNumber"));
                    }
                    returnList.remove(returnList.size()-1);
                    returnList.add(rowMap);
                    continue;
                }

                  if(beforePartyId!=null   && !beforePartyId.equals(partyIdTo)){
                      if(contactMechTypeId.equals("TELECOM_NUMBER")){
                          String tnContactNumber = gv.getString("tnContactNumber");
                          if(null!=tnContactNumber){
                              rowMap.put("tnContactNumber", gv.getString("tnContactNumber"));
                          }
                      }
                  }


                Map<String,String> supplierInfo =  queryBomPersonBaseInfo(delegator, partyIdTo, partyGroupId);
                 rowMap.put("name",supplierInfo.get("aliasCompanyName")+"-"+supplierInfo.get("aliasName") );
//                  if(partyId.equals(partyIdTo)){
//                      rowMap.put("name","自有仓库"+"-"+supplierInfo.get("aliasName") );
//                  }
                rowMap.put("supplierInfo",supplierInfo);
                rowMap.put("partyId",partyIdTo);
                  String paAddress1 =  gv.getString("paAddress1");
                  if(null!=paAddress1){
                      rowMap.put("paAddress1",paAddress1);
                  }
//                rowMap.put("tel",supplierInfo.get("userLoginId"));
                rowMap.put("avatar",supplierInfo.get("headPortrait"));
                rowMap.put("orderSize","0");
                rowMap.put("fromDate",sdf.format(gv.get("fromDate")));
                  if (UtilValidate.isEmpty(rowMap.get("tnContactNumber"))){
                      rowMap.put("tnContactNumber", supplierInfo.get("contactNumber"));
                  }
                returnList.add(rowMap);
                  beforePartyId=partyIdTo;
            }
        }

      resultMap.put("supplierList",returnList);
        return resultMap;
    }


}
