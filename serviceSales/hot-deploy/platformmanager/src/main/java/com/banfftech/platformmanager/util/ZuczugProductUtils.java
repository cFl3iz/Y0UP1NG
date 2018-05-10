package main.java.com.banfftech.platformmanager.util;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.GeneralException;
import org.apache.ofbiz.base.util.UtilDateTime;
import org.apache.ofbiz.base.util.UtilHttp;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityConditionList;
import org.apache.ofbiz.entity.condition.EntityExpr;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.entity.util.EntityUtil;
import org.apache.ofbiz.order.shoppingcart.CartItemModifyException;
import org.apache.ofbiz.order.shoppingcart.ShoppingCart;
import org.apache.ofbiz.order.shoppingcart.ShoppingCartEvents;
import org.apache.ofbiz.order.shoppingcart.ShoppingCartItem;
import org.apache.ofbiz.order.shoppingcart.WebShoppingCart;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ModelService;
import org.apache.ofbiz.service.ServiceUtil;
import org.apache.ofbiz.entity.util.EntityQuery;


public class ZuczugProductUtils {

	public static final String module = ZuczugProductUtils.class.getName();
	public static final String IS_CONFIRMED_ATTR = "isConfirmed";
	public static final String COLOR_CONFIRMED_VALUE = "Y";
	public static final String COLOR_NOT_CONFIRMED_VALUE = "N";
	public static final Map<String, String> INTERNAL_SIZE_MAPPING = new HashMap<String, String>();
	static {
		INTERNAL_SIZE_MAPPING.put("0", "SIZE0");
		INTERNAL_SIZE_MAPPING.put("2", "SIZE2");
		INTERNAL_SIZE_MAPPING.put("4", "SIZE4");
		INTERNAL_SIZE_MAPPING.put("6", "SIZE6");
		INTERNAL_SIZE_MAPPING.put("8", "SIZE8");
		INTERNAL_SIZE_MAPPING.put("25", "SIZE0");
		INTERNAL_SIZE_MAPPING.put("26", "SIZE2");
		INTERNAL_SIZE_MAPPING.put("27", "SIZE4");
		INTERNAL_SIZE_MAPPING.put("28", "SIZE6");
		INTERNAL_SIZE_MAPPING.put("29", "SIZE8");
		INTERNAL_SIZE_MAPPING.put("F", "SIZE4");
	}
	/**
	 * 检查某个商品是否有4码，如果没有就创建
	 * 现在是检查商品是否有中间码，4 或者 27
	 * 补充逻辑，如果商品是非虚拟非变型，直接返回他自己
	 * @author sven
	 * @param productId
	 * @param colorId
	 * @param delegator
	 * @param dispatcher
	 * @return
	 */
	public static String checkProductMiddleSize(String productId,String colorId,Delegator delegator,LocalDispatcher dispatcher){
		String skuId = "";
		try {
			GenericValue pro = delegator.findOne("Product", false, UtilMisc.toMap("productId",productId));
			if(UtilValidate.areEqual(pro.getString("isVirtual"), "N") && UtilValidate.areEqual(pro.getString("isVariant"), "N")){
				return productId;
			}
			GenericValue colorFeature = delegator.findOne("ProductFeature", true, UtilMisc.toMap("productFeatureId",colorId));
			if(UtilValidate.isEmpty(colorFeature)){
				return skuId;
			}
			String size4Id = productId+"-"+colorFeature.getString("idCode")+"-4";
			String size27Id = productId+"-"+colorFeature.getString("idCode")+"-27";
			String sizeFId = productId+"-"+colorFeature.getString("idCode")+"-F";
			String size37Id = productId+"-"+colorFeature.getString("idCode")+"-37";
			GenericValue product = delegator.findOne("Product", true, UtilMisc.toMap("productId",size4Id));
			GenericValue product27 = delegator.findOne("Product", true, UtilMisc.toMap("productId",size27Id));
			GenericValue productF = delegator.findOne("Product", true, UtilMisc.toMap("productId",sizeFId));
			GenericValue product37 = delegator.findOne("Product", true, UtilMisc.toMap("productId",size37Id));
			if(UtilValidate.isNotEmpty(product)){
				if(UtilValidate.isNotEmpty(EntityQuery.use(delegator).from("ProductAssoc").where("productId",productId,"productIdTo",size4Id,"productAssocTypeId","PRODUCT_VARIANT").queryList())){
					return size4Id;
				}
				Debug.log("productId:"+productId+"--color:"+colorId+",中间码存在，但是没有Assoc关联");
			}

			if(UtilValidate.isNotEmpty(product27)){
				if(UtilValidate.isNotEmpty(EntityQuery.use(delegator).from("ProductAssoc").where("productId",productId,"productIdTo",size27Id,"productAssocTypeId","PRODUCT_VARIANT").queryList() )){
					return size27Id;
				}
				Debug.log("productId:"+productId+"--color:"+colorId+",中间码存在，但是没有Assoc关联");
			}

			if(UtilValidate.isNotEmpty(product37)){
				if(UtilValidate.isNotEmpty(EntityQuery.use(delegator).from("ProductAssoc").where( "productId", productId,"productIdTo",size37Id,"productAssocTypeId","PRODUCT_VARIANT").queryList())){
					return size37Id;
				}
				Debug.log("productId:"+productId+"--color:"+colorId+",中间码存在，但是没有Assoc关联");
			}

			if(UtilValidate.isNotEmpty(productF)){
				if(UtilValidate.isNotEmpty(EntityQuery.use(delegator).from("ProductAssoc").where("productId",productId,"productIdTo",sizeFId,"productAssocTypeId","PRODUCT_VARIANT").queryList())) {
					return sizeFId;
				}
				Debug.log("productId:"+productId+"--color:"+colorId+",中间码存在，但是没有Assoc关联");
			}
			if(UtilValidate.isEmpty(skuId)){
				Debug.log("productId:"+productId+"--color:"+colorId+",没有中间码");
			}
		} catch (GenericEntityException e) {
			e.printStackTrace();
		}
		return skuId;
	}


	/**
	 * 查询一个商品的组别等信息，如果是变型就查询他的虚拟
	 * @author sven
	 * @param delegator
	 * @param productId
	 * @return （组别:groupId、系列:seriesId、子系列:subSeriesId、季:seasonId、波段:waveId）
	 */
	public static Map<String,String> getVariantGroupCategorys(Delegator delegator,String productId){
		Map<String,String> result = new HashMap<String, String>();

		try {
			GenericValue product=delegator.findOne("Product", true, UtilMisc.toMap("productId",productId));
			if(UtilValidate.isNotEmpty(product) && "Y".equals(product.getString("isVariant"))){//如果是变形商品，就查询他的虚拟
				GenericValue assoc = EntityUtil.getFirst(EntityQuery.use(delegator).from("ProductAssoc").where("productIdTo",productId,"productAssocTypeId","PRODUCT_VARIANT").queryList());
				if(UtilValidate.isNotEmpty(assoc)){
					productId = assoc.getString("productId");
				}
			}
			GenericValue group = EntityUtil.getFirst(EntityQuery.use(delegator).from("ProductCategoryAndMember").where("productId",productId,"productCategoryTypeId","GROUPNAME").queryList());
			if(UtilValidate.isNotEmpty(group)){
				result.put("groupId", group.getString("productCategoryId"));
				GenericValue pgv = EntityUtil.getFirst(EntityQuery.use(delegator).from("ProductCategoryGroupView").where("groupId",group.getString("productCategoryId")).queryList());
				if (UtilValidate.isNotEmpty(pgv)){
					result.put("seasonId", pgv.getString("seasonId"));
					result.put("seriesId", pgv.getString("seriesId"));
					result.put("subSeriesId", pgv.getString("subSeriesId"));
				}
			}

			GenericValue wave = EntityUtil.getFirst(EntityQuery.use(delegator).from("ProductCategoryAndMember").where("productId", productId,"productCategoryTypeId","WAVE").queryList());
			if(UtilValidate.isNotEmpty(wave)){
				result.put("waveId", wave.getString("productCategoryId"));
			}
		} catch (GenericEntityException e) {
			e.printStackTrace();
		}
		return result;
	}
}
