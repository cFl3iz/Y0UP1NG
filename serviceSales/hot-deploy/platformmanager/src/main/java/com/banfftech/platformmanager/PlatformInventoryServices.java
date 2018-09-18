package main.java.com.banfftech.platformmanager;

import net.sf.json.JSONArray;
import main.java.com.banfftech.personmanager.PersonManagerServices;
import main.java.com.banfftech.platformmanager.constant.PeConstant;
import main.java.com.banfftech.platformmanager.util.ExportExcelFile;
import main.java.com.banfftech.platformmanager.util.HttpHelper;
import main.java.com.banfftech.platformmanager.util.TestExcel;
import main.java.com.banfftech.platformmanager.wechat.WeChatUtil;
import net.sf.json.JSONObject;
import org.apache.commons.fileupload.FileItem;
import org.apache.ofbiz.base.util.Debug;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.ofbiz.base.util.*;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.entity.model.ModelEntity;
import org.apache.ofbiz.entity.transaction.GenericTransactionException;
import org.apache.ofbiz.entity.transaction.TransactionUtil;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.entity.util.EntityUtil;
import org.apache.ofbiz.entity.util.EntityUtilProperties;
import org.apache.ofbiz.order.shoppingcart.ShoppingCart;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;
//import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by S on 2017/9/12.
 */
public class PlatformInventoryServices {

    public final static String module = PlatformInventoryServices.class.getName();

    public static final String resourceUiLabels = "PlatformManagerUiLabels.xml";

    /**
     * 同步当前热卖的产品库存
     * @author S
     * @param ctx
     * @param context
     * @return
     */
    //报文格式
//    {
//        login.password = 1 qazZAQ !, orderList =[{
//            "OMSOrderId":"10205763501", "externalId":"160447566023138359", "grandTotal":
//            "1185.0000", "paymentMethodTypeId":"EXT_ALIPAY", "needInvoic":"0", "nickName":"王紧张小姐历险记", "toName":
//            "多金", "postalCode":"000000", "phoneNumber":"13028903446", "stateProvince":"辽宁省", "city":"沈阳市", "county":
//            "和平区", "address1":"沈水湾街道青年大街新世界工地黄楼", "internalNote":"", "noteInfo":"", "orderDate":
//            "2018-06-12 23:50:05", "paymentDate":"2018-06-12 23:50:05", "invoiceAmount":"0", "invoiceTitle":
//            "", "invoiceContent":"", "shippingAmount":"0.0000", "orderItems":[{
//                "orderItemSeqId":"791595", "productName":"条纹双层布折领短上衣", "productId":"B161TS01-92-2", "quantity":
//                "1", "unitPrice":"674.22"
//            },{
//                "orderItemSeqId":"791597", "productName":"条纹双层布短裤", "productId":"B161PA17-92-27", "quantity":
//                "1", "unitPrice":"510.78"
//            }]}],login.username = omsapiaccount
//    }

    public synchronized static Map<String, Object> syncZuczugInventory(DispatchContext ctx, Map<String, ? extends Object> context) throws GenericEntityException, GenericServiceException{

        Delegator delegator = ctx.getDelegator();
        LocalDispatcher dispatcher = ctx.getDispatcher();
        Map<String, Object> returnResult = ServiceUtil.returnSuccess();


        String productStoreId = (String) context.get("productStoreId");
        if(null==productStoreId){
            productStoreId = "ZUCZUGSTORE";
        }
        //店铺线上产品分类目录
//        GenericValue productStoreOnlineCategory = EntityQuery.use(delegator).from("ProductStoreOnlineCategory").where("productStoreId", "ZUCZUGSTORE").queryFirst();
        GenericValue productStoreOnlineCategory = EntityQuery.use(delegator).from("ProductStoreOnlineCategory").where("productStoreId",productStoreId).queryFirst();

        EntityCondition findCondition = EntityCondition.makeCondition(
                UtilMisc.toMap("productCategoryId", productStoreOnlineCategory.getString("productCategoryId")));
        Set<String> fieldSet = new HashSet<String>();
//        fieldSet.add("productCategoryId");
        fieldSet.add("productId");

        // Find ProductCategoryVariantMember
        List<GenericValue> productCategoryVariantMembers = delegator.findList("ProductCategoryVariantMember",
                findCondition, fieldSet,
                null, null, false);
        Map<String,String> keyMap = new HashMap<String, String>();
        List<Map<String,Object>> toFormatList = new ArrayList<Map<String, Object>>();
        if(null!=productCategoryVariantMembers&& productCategoryVariantMembers.size()>0){
            for(GenericValue gv : productCategoryVariantMembers){
                String productId = gv.getString("productId");
                Map<String,Object> rowMap = new HashMap<String, Object>();
                if(!keyMap.containsKey(productId)){
                    rowMap.put("productId",productId);
                    keyMap.put(productId,null);
                    toFormatList.add(rowMap);
                }
            }
        }
        //查询当前店铺需要同步的场所列表
        List<GenericValue> productStoreOnlineFacility = EntityQuery.use(delegator).from("ProductStoreOnlineFacilityInv").where(
                "productStoreId",productStoreId).queryList();
        List<Map<String,Object>> facilityList = null;
        if(null != productStoreOnlineFacility && productStoreOnlineFacility.size()>0){
                facilityList = new ArrayList<Map<String,Object>>();
                for(GenericValue onlineFacility : productStoreOnlineFacility){
                    Map<String,Object> rowMap = new HashMap<String, Object>();
                    String facilityId = onlineFacility.getString("facilityId");
                    rowMap.put("facilityId",facilityId);
                    facilityList.add(rowMap);
                }
        }



        if (null != toFormatList && toFormatList.size()>0) {

            JSONObject json = new JSONObject();
            json.put("login.username","omsapiaccount");
            json.put("login.password","1qazZAQ!");
            json.put("skus",toFormatList);
            json.put("facilitys",facilityList);
            String strSku = json.getString("skus");
            String strFacilitys = null;
            if(null != facilityList){
               strFacilitys = json.getString("facilitys");
            }

            Debug.logInfo("本次同步仓库:"+strFacilitys,module);

            //准备发送报文给长宁获取库存数据
            String postResult = HttpHelper.sendPost("http://114.215.180.140:9191/zuczugopen/control/ypSyncOfInventory",
                    "login.username=omsapiaccount&login.password=1qazZAQ!&skus=" + strSku+"&facilitys="+strFacilitys);


            if(UtilValidate.isNotEmpty(postResult)){
                JSONObject returnJson = JSONObject.fromObject(postResult);
//                Debug.logInfo("returnJson:"+returnJson,module);
                String resultMsg = (String) returnJson.get("responseMessage");
//                Debug.logInfo("resultMsg:"+resultMsg,module);
                if(resultMsg.trim().equals("success")){
                    JSONObject data = (JSONObject) returnJson.get("data");
                    JSONArray invList = (JSONArray) data.get("skusInvList");
                    if(null!=invList){
                        doSyncIven(invList, delegator, dispatcher);
                    }
                }
            }

//            Debug.logInfo("PostResult:"+postResult,module);
        }

        return returnResult;
    }

    /**
     * 更新库存
     * @param invList
     * @param delegator
     * @param dispatcher
     */
    private static void doSyncIven(JSONArray invList, Delegator delegator, LocalDispatcher dispatcher)throws GenericEntityException, GenericServiceException {
        GenericValue admin = EntityQuery.use(delegator).from("UserLogin").where("userLoginId", "admin").queryFirst();
        for(int i = 0 ; i < invList.size();i++){
            JSONObject rowInv = invList.getJSONObject(i);
            String skuId = "" + rowInv.keys().next();

            JSONObject invJson = (JSONObject) rowInv.get(skuId);
            String quantityOnHandTotalStr = "" + invJson.get("quantityOnHandTotal");
            String availableToPromiseTotalStr = "" + invJson.get("availableToPromiseTotal");
            Debug.logInfo("*product:"+skuId,module);
            Debug.logInfo("*quantityOnHandTotal:"+quantityOnHandTotalStr,module);
            Debug.logInfo("*availableToPromiseTotal:"+availableToPromiseTotalStr,module);

            BigDecimal quantityOnHandTotalZuczug = new BigDecimal(quantityOnHandTotalStr);
            BigDecimal availableToPromiseTotalZuczug = new BigDecimal(availableToPromiseTotalStr);

            if((quantityOnHandTotalZuczug.compareTo(BigDecimal.ZERO) <= 0)){
                quantityOnHandTotalZuczug = BigDecimal.ZERO;
            }
            if((availableToPromiseTotalZuczug.compareTo(BigDecimal.ZERO) <= 0)){
                availableToPromiseTotalZuczug = BigDecimal.ZERO;
            }

            //开始搞库存
            GenericValue category = EntityQuery.use(delegator).from("ProductAndCategoryMember").where("productId", skuId).queryFirst();
            GenericValue productPrice = EntityQuery.use(delegator).from("ProductPrice").where("productId", skuId).queryFirst();
            String productStoreId = category.getString("productStoreId");
            GenericValue store = EntityQuery.use(delegator).from("ProductStore").where("productStoreId", productStoreId).queryFirst();
            String inventoryFacilityId = store.getString("inventoryFacilityId");
            //获得库存信息 getInventoryAvailableByFacility
            Map<String, Object> getInventoryAvailableByFacilityMap = dispatcher.runSync("getInventoryAvailableByFacility", UtilMisc.toMap("userLogin", admin,
                    "facilityId", inventoryFacilityId, "productId", skuId));
            BigDecimal quantityOnHandTotal = (BigDecimal) getInventoryAvailableByFacilityMap.get("quantityOnHandTotal");
            BigDecimal availableToPromiseTotal = (BigDecimal) getInventoryAvailableByFacilityMap.get("availableToPromiseTotal");




            GenericValue productInventoryItem = EntityQuery.use(delegator).from("ProductInventoryItem").where("productId", skuId).queryFirst();
            String inventoryItemId = (String) productInventoryItem.get("inventoryItemId");
//          -1,表示bigdemical小于bigdemical2；
//           0,表示bigdemical等于bigdemical2；
//           1,表示bigdemical大于bigdemical2；

            Map<String, Object> createInventoryItemDetailMap = new HashMap<String, Object>();
            createInventoryItemDetailMap.put("userLogin", admin);
            createInventoryItemDetailMap.put("inventoryItemId", inventoryItemId);


            Debug.logInfo("*update resource availableToPromiseTotal = " + availableToPromiseTotal, module);
            Debug.logInfo("*update resource availableToPromiseTotal.compareTo(quantity)>0 = " + (availableToPromiseTotal.compareTo(availableToPromiseTotalZuczug) > 0), module);
            Debug.logInfo("*update resource quantityOnHandTotal.compareTo(quantity)>0 = " + (quantityOnHandTotal.compareTo(quantityOnHandTotalZuczug) > 0), module);

            //todo mark 2018 09 15 if <zuczug no update
            //说明现库存比要设置的库存大,需要做差异减法
            if (availableToPromiseTotal.compareTo(availableToPromiseTotalZuczug) > 0) {
                int availableToPromiseTotalInt = availableToPromiseTotal.intValue();
                int quantityInt = availableToPromiseTotalZuczug.intValue();
                Debug.logInfo("*update resource quantityInt Diff =   " + quantityInt, module);
                Debug.logInfo("*update resource availableToPromiseTotalInt =   " + availableToPromiseTotalInt, module);

                //createInventoryItemDetailMap.put("availableToPromiseDiff", new BigDecimal("-" + (availableToPromiseTotalInt - quantityInt)));
                //createInventoryItemDetailMap.put("unitCost", productPrice.get("price"));
                //createInventoryItemDetailMap = null;
            }
            //说明现库存比要设置的库存小,需要做差异加法
            if (availableToPromiseTotal.compareTo(availableToPromiseTotalZuczug) < 0) {
                int availableToPromiseTotalInt = availableToPromiseTotal.intValue();
                int quantityInt = availableToPromiseTotalZuczug.intValue();
                Debug.logInfo("*update resource quantityInt Diff =   " + quantityInt, module);
                Debug.logInfo("*update resource availableToPromiseTotalInt =   " + availableToPromiseTotalInt, module);
                createInventoryItemDetailMap.put("availableToPromiseDiff", new BigDecimal("" + (quantityInt - availableToPromiseTotalInt)));
                createInventoryItemDetailMap.put("unitCost", productPrice.get("price"));
                //一模一样的库存我还差异个屁?
                if (availableToPromiseTotal.compareTo(availableToPromiseTotalZuczug) == 0) {

                } else {
                    //3.2 Do create
                    Map<String, Object> createInventoryItemDetailOutMap = dispatcher.runSync("createInventoryItemDetail", createInventoryItemDetailMap);

                }
            }


            //再来一遍
            //说明现库存比要设置的库存大,需要做差异减法
            if (quantityOnHandTotal.compareTo(quantityOnHandTotalZuczug) > 0) {
                int quantityOnHandTotalInt = quantityOnHandTotal.intValue();
                int quantityInt = quantityOnHandTotalZuczug.intValue();
                Debug.logInfo("*update resource quantityInt Diff =   " + quantityInt, module);
                Debug.logInfo("*update resource availableToPromiseTotalInt =   " + quantityOnHandTotalInt, module);
                createInventoryItemDetailMap.put("accountingQuantityDiff", new BigDecimal("-" + (quantityOnHandTotalInt - quantityInt)));
                createInventoryItemDetailMap.put("quantityOnHandDiff", new BigDecimal("-" + (quantityOnHandTotalInt - quantityInt)));
                createInventoryItemDetailMap.put("unitCost", productPrice.get("price"));
            }
            //说明现库存比要设置的库存小,需要做差异加法
            if (quantityOnHandTotal.compareTo(quantityOnHandTotalZuczug) < 0) {
                int quantityOnHandTotalInt = quantityOnHandTotal.intValue();
                int quantityInt = quantityOnHandTotalZuczug.intValue();
                Debug.logInfo("*update resource quantityInt Diff =   " + quantityInt, module);
                Debug.logInfo("*update resource quantityOnHandTotalInt =   " + quantityOnHandTotalInt, module);
                createInventoryItemDetailMap.put("accountingQuantityDiff", new BigDecimal("" + (quantityInt - quantityOnHandTotalInt)));
                createInventoryItemDetailMap.put("quantityOnHandDiff", new BigDecimal("" + (quantityInt - quantityOnHandTotalInt)));
                createInventoryItemDetailMap.put("unitCost", productPrice.get("price"));
            }
            //一模一样的库存我还差异个屁?
            if (quantityOnHandTotal.compareTo(quantityOnHandTotalZuczug) == 0) {

            } else {
                //3.2 Do create
                Map<String, Object> createInventoryItemDetailOutMap = dispatcher.runSync("createInventoryItemDetail", createInventoryItemDetailMap);

            }

        }
    }

}
