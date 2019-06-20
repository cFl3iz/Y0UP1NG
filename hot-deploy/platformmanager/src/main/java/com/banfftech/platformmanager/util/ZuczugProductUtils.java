package com.banfftech.platformmanager.util;

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

import org.apache.batik.dom.GenericEntity;
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
	public static String checkProductMiddleSize(String productId,String colorId,Delegator delegator,LocalDispatcher dispatcher)throws GenericEntityException{
		String skuId = "";
		String virId = productId;
		//0181BA04-44-F
		List<GenericValue> skus = EntityQuery.use(delegator).from("ProductAssoc").where("productId", productId).queryList();

		if (skus != null && skus.size() > 0) {
			for (GenericValue sku : skus) {
				GenericValue isExsitsColor = EntityQuery.use(delegator).from("ProductFeatureAndAppl").where("productId", sku.getString("productIdTo"), "productFeatureTypeId", "COLOR", "description", colorId).queryFirst();

				if (isExsitsColor != null ) {
					skuId = sku.getString("productIdTo");
					break;
				}
			}
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
