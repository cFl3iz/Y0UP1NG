package main.java.com.banfftech.platformmanager;


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



        EntityCondition findCondition = EntityCondition.makeCondition(
                UtilMisc.toMap("productCategoryId", "ANKORAU_RETAIL_ROOT"));
        Set<String> fieldSet = new HashSet<String>();
//        fieldSet.add("productCategoryId");
        fieldSet.add("productId");

        // Find ProductCategoryVariantMember
        List<GenericValue> productCategoryVariantMembers = delegator.findList("ProductCategoryVariantMember",
                findCondition, fieldSet,
                null, null, false);

        if (null != productCategoryVariantMembers) {

            JSONObject json = new JSONObject();
            json.put("login.username","omsapiaccount");
            json.put("login.password","1qazZAQ!");
            Debug.logInfo("productCategoryVariantMembers:"+productCategoryVariantMembers,module);
            json.put("skus",productCategoryVariantMembers);
            String strSku = json.getString("skus");
            //准备发送报文给长宁获取库存数据
            String postResult = HttpHelper.sendPost("http://121.199.20.78:9191/zuczugopen/control/ypSyncOfInventory",
                    "login.username=omsapiaccount&login.password=1qazZAQ!&skus="+strSku);

            //String postResult = WeChatUtil.PostSendMsg(json, "http://121.199.20.78:9191/zuczugopen/control/ypSyncOfInventory");

            Debug.logInfo("PostResult:"+postResult,module);
        }

        return returnResult;
    }

}
