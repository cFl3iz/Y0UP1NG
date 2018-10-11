package main.java.com.banfftech.platformmanager.odata.ofbiz;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import main.java.com.banfftech.platformmanager.util.UtilTools;
import org.apache.olingo.commons.api.Constants;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.data.Link;
import org.apache.olingo.commons.api.data.Parameter;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.edm.EdmNavigationProperty;
import org.apache.olingo.commons.api.edm.EdmProperty;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.UriInfoResource;
import org.apache.olingo.server.api.uri.UriParameter;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourceAction;
import org.apache.olingo.server.api.uri.UriResourceFunction;
import org.apache.olingo.server.api.uri.UriResourceNavigation;
import org.apache.olingo.server.api.uri.UriResourcePrimitiveProperty;
import org.apache.olingo.server.api.uri.queryoption.ExpandItem;
import org.apache.olingo.server.api.uri.queryoption.ExpandOption;
import org.apache.olingo.server.api.uri.queryoption.FilterOption;
import org.apache.olingo.server.api.uri.queryoption.OrderByItem;
import org.apache.olingo.server.api.uri.queryoption.OrderByOption;
import org.apache.olingo.server.api.uri.queryoption.SelectOption;
import org.apache.olingo.server.api.uri.queryoption.SkipOption;
import org.apache.olingo.server.api.uri.queryoption.TopOption;
import org.apache.olingo.server.api.uri.queryoption.apply.AggregateExpression.StandardMethod;
import org.apache.olingo.server.api.uri.queryoption.expression.Expression;
import org.apache.olingo.server.api.uri.queryoption.expression.ExpressionVisitException;
import org.apache.olingo.server.api.uri.queryoption.expression.Member;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilDateTime;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.entity.model.ModelEntity;
import org.apache.ofbiz.entity.model.ModelField;
import org.apache.ofbiz.entity.model.ModelReader;
import org.apache.ofbiz.entity.util.EntityFindOptions;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ModelParam;
import org.apache.ofbiz.service.ModelService;
import org.apache.ofbiz.entity.util.EntityQuery;

public class Storage {
	public static final String module = Storage.class.getName();
	public static final int	MAX_ROWS = 10000;
	public static final int DAYS_BEFORE = -100;

    private Delegator delegator;
    private LocalDispatcher dispatcher;

    public Storage(Delegator delegator) {
    		this.delegator = delegator;
    }

    public Storage(Delegator delegator, LocalDispatcher dispatcher) {
		this.delegator = delegator;
		this.dispatcher = dispatcher;
    }
    
    /* PUBLIC FACADE */
	public EntityCollection readEntitySetData(EdmEntitySet edmEntitySet, FilterOption filterOption,SkipOption skipOption, TopOption topOption, OrderByOption orderByOption,ExpandOption expandOption,SelectOption selectOption) {
		EdmEntityType edmEntityType = edmEntitySet.getEntityType();
		return readEntitySetData(edmEntityType, filterOption, null, skipOption, topOption, orderByOption, expandOption, selectOption);
	}
	public EntityCollection readEntitySetData(EdmEntityType edmEntityType, FilterOption filterOption, Map<String, Object> extraOption, SkipOption skipOption, TopOption topOption, OrderByOption orderByOption,ExpandOption expandOption,SelectOption selectOption) {
		String entityName = edmEntityType.getName();
		List<GenericValue> genericValues = null;

		int skipValue = 0;

		if (skipOption != null) {
			int skipNumber = skipOption.getValue();
			if (skipNumber >= 0) {
				skipValue = skipNumber;
			}
		}
		int topValue = 0;
		if (topOption != null && topOption.getValue()>0) {
			topValue = topOption.getValue();
		}

		try {
			EntityFindOptions efo = new EntityFindOptions();
			if (topValue != 0){
				efo.setMaxRows(topValue+skipValue);
			} else {
				efo.setMaxRows(MAX_ROWS);
			}

			EntityCondition entityCondition = null;
			if (filterOption != null) {
				Expression filterExpression = filterOption.getExpression();
				OfbizExpressionVisitor expressionVisitor = new OfbizExpressionVisitor();
				entityCondition = (EntityCondition) filterExpression.accept(expressionVisitor);
			}
			if (extraOption != null) {
				EntityCondition extraCondition = EntityCondition.makeCondition(extraOption);
				if (entityCondition == null) {
					entityCondition = extraCondition;
				} else {
					List<EntityCondition> exprs = new ArrayList<EntityCondition>();
					exprs.add(extraCondition);
					exprs.add(entityCondition);
					entityCondition = EntityCondition.makeCondition(exprs, EntityOperator.AND);
				}
			}
			// List<String> orderBy = UtilMisc.toList("-lastUpdatedStamp");
			List<String> orderBy = new ArrayList<String>();
			if (orderByOption != null) {
				List<OrderByItem> orderItemList = orderByOption.getOrders();
				for (OrderByItem orderByItem : orderItemList) {
					Expression expression = orderByItem.getExpression();
					if (expression instanceof Member) {
						UriInfoResource resourcePath = ((Member) expression).getResourcePath();
						UriResource uriResource = resourcePath.getUriResourceParts().get(0);
						if (uriResource instanceof UriResourcePrimitiveProperty) {
							EdmProperty edmProperty = ((UriResourcePrimitiveProperty) uriResource).getProperty();
							final String sortPropertyName = edmProperty.getName();
							if (orderByItem.isDescending()) {
								orderBy.add("-" + sortPropertyName);
							} else {
								orderBy.add(sortPropertyName);
							}
						}
					}
				}
			}
			Set<String> fieldsToSelect = null;
			if(selectOption!=null){
				fieldsToSelect = new HashSet<String>();
				for(String selectFieldName : selectOption.getText().split(",")){
					fieldsToSelect.add(selectFieldName);
				}
			}

            genericValues = delegator.findList(entityName, entityCondition, fieldsToSelect, orderBy, efo, false);

			genericValues = UtilTools.getListPaging(genericValues, skipValue, topValue);

		} catch (GenericEntityException e) {
			e.printStackTrace();
		} catch (ExpressionVisitException e) {
			e.printStackTrace();
		} catch (ODataApplicationException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}



		EntityCollection entityCollection = new EntityCollection();
		List<Entity> entityList = entityCollection.getEntities();
		if(genericValues!=null){
			for (GenericValue genericValue : genericValues) {
				Entity rowEntity = genericValueToEntity(edmEntityType, genericValue);
				// Add To EntitySet
				addExpandOption(expandOption, rowEntity, genericValue);
				entityList.add(rowEntity);
			}
		}


		return entityCollection;
		
	}

	public Entity readEntityData(EdmEntitySet edmEntitySet, List<UriParameter> keyParams) throws ODataApplicationException{

        EdmEntityType edmEntityType = edmEntitySet.getEntityType();
        GenericValue genericValue = getGenericValue(edmEntitySet, keyParams);
        return genericValueToEntity(edmEntityType, genericValue);

    }

	public EntityCollection getRelatedEntityCollection(EdmEntitySet edmEntitySet, List<UriParameter> keyParams, EdmNavigationProperty edmNavigationProperty) {
		Debug.logInfo("---------------In getRelatedEntityCollection, edmEntitySet is " + edmEntitySet.getName(), module);
		GenericValue originGenericValue = getGenericValue(edmEntitySet, keyParams);
		String relationName = edmNavigationProperty.getName();
		Debug.logInfo("---------------In relationName is " + relationName, module);
		EdmEntityType edmEntityType = edmNavigationProperty.getType();

		List<GenericValue> genericValues = null;
		try {
			genericValues = originGenericValue.getRelated(relationName);
		} catch (GenericEntityException e) {
			e.printStackTrace();
		}
		EntityCollection entityCollection = new EntityCollection();
		// check for which EdmEntitySet the data is requested
		List<Entity> entityList = entityCollection.getEntities();
		for (GenericValue genericValue : genericValues) {
			entityList.add(genericValueToEntity(edmEntityType, genericValue));
		}
		return entityCollection;
	}

	// add query options, filterOption, skipOption ... to getRelatedEntityCollection
	public EntityCollection getRelatedEntityCollection(List<UriParameter> keyParams, EdmNavigationProperty edmNavigationProperty,
			FilterOption filterOption, SkipOption skipOption, TopOption topOption, OrderByOption orderByOption,
			ExpandOption expandOption, SelectOption selectOption) {

		Debug.logInfo("---------------In new getRelatedEntityCollection", module);
		Map<String, Object> pk = uriParametersToMap(keyParams);
		EdmEntityType edmEntityType = edmNavigationProperty.getType();
		return readEntitySetData(edmEntityType, filterOption, pk, skipOption, topOption, orderByOption, expandOption, selectOption);
	}
	
	public static Map<String, Object> uriParametersToMap(List<UriParameter> keyParams) {
		Map<String, Object> pk = new HashMap<String, Object>();
		for (UriParameter keyPredicate: keyParams) {
            String regexp = "\'";
            String keyText = keyPredicate.getText();
            keyText = keyText.replaceAll(regexp, "");
            Debug.logInfo("UriParameter name = " + keyPredicate.getName() + ", value = " + keyText, module);
			pk.put(keyPredicate.getName(), keyText);
		}
		return pk;
	}

	private Entity genericValueToEntity(EdmEntityType edmEntityType, GenericValue genericValue) {
		String entityName = genericValue.getEntityName();
		try {
			Entity e1 = new Entity();
			e1.setType(edmEntityType.getFullQualifiedName().getFullQualifiedNameAsString());
			ModelReader reader = delegator.getModelReader();
			ModelEntity modelEntity;
			try {
				modelEntity = reader.getModelEntity(entityName);
			} catch (GenericEntityException e) {
				e.printStackTrace();
				return null;
			}
			Iterator<ModelField> fieldIterator = modelEntity.getFieldsIterator();
			while (fieldIterator.hasNext()) {
				ModelField field = fieldIterator.next();
				String fieldName = field.getName();
				e1 = e1.addProperty(new Property(null, fieldName, ValueType.PRIMITIVE, genericValue.get(fieldName)));
			}
			e1.setId(Util.createId(entityName + "s", genericValue));
			return e1;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private GenericValue getGenericValue(EdmEntitySet edmEntitySet, List<UriParameter> keyPredicates) {
		GenericValue genericValue = null;
		Map<String, Object> pk = new HashMap<String, Object>();
		for (UriParameter keyPredicate: keyPredicates) {
            String regexp = "\'";
            String keyText = keyPredicate.getText();
            keyText = keyText.replaceAll(regexp, "");
            Debug.logInfo("UriParameter name = " + keyPredicate.getName() + ", value = " + keyText, module);
			pk.put(keyPredicate.getName(), keyText);
		}
		String entityName = edmEntitySet.getEntityType().getName();
		try {
			genericValue = EntityQuery.use(delegator).from(entityName).where(pk).queryFirst();
					//delegator.findByPrimaryKey(entityName, pk);
		} catch (GenericEntityException e) {
			e.printStackTrace();
		}
		return genericValue;
	}


	private List<GenericValue> getGenericValues(EdmEntityType edmEntityType, List<UriParameter> keyPredicates) {
		List<GenericValue> genericValues = null;
		Map<String, Object> pk = new HashMap<String, Object>();
		for (UriParameter keyPredicate: keyPredicates) {
            String regexp = "\'";
            String keyText = keyPredicate.getText();
            keyText = keyText.replaceAll(regexp, "");
            Debug.logInfo("UriParameter name = " + keyPredicate.getName() + ", value = " + keyText, module);
			pk.put(keyPredicate.getName(), keyText);
		}
		String entityName = edmEntityType.getName();
		try {
			genericValues = EntityQuery.use(delegator).from(entityName).where(pk).queryList();
//					delegator.findByAnd(entityName, pk);
		} catch (GenericEntityException e) {
			e.printStackTrace();
		}
		return genericValues;
	}
	private Map<Entity, GenericValue> getRelatedEntityMaps(GenericValue originGenericValue, EdmNavigationProperty edmNavigationProperty, boolean isCollection) {
		Map<Entity, GenericValue> resultMap = new HashMap<Entity, GenericValue>();
		Debug.logInfo("------------------ entering in OfbizCollectionProcessor.getRelatedEntityCollection", module);
		String relationName = edmNavigationProperty.getName();
		EdmEntityType edmEntityType = edmNavigationProperty.getType();

		String entityName = edmEntityType.getName();
		Debug.logInfo("adding entity property for relation entity .... " + entityName, module);
		List<GenericValue> genericValues = null;
		try {
			if (isCollection) {
				genericValues = originGenericValue.getRelated(relationName);
			} else {
				GenericValue genericValue = originGenericValue.getRelatedOne(relationName);
				genericValues = new ArrayList<GenericValue>();
				genericValues.add(genericValue);
			}
		} catch (GenericEntityException e) {
			e.printStackTrace();
		}
		// EntityCollection entityCollection = new EntityCollection();
		// check for which EdmEntitySet the data is requested
		// List<Entity> entityList = entityCollection.getEntities();
        if (genericValues.size() > 0 && genericValues.get(0) != null) {
			for (GenericValue genericValue : genericValues) {
				Entity e1 = new Entity();
				ModelReader reader = delegator.getModelReader();
				ModelEntity modelEntity;
				try {
					modelEntity = reader.getModelEntity(entityName);
				} catch (GenericEntityException e) {
					e.printStackTrace();
					return null;
				}
				Iterator<ModelField> fieldIterator = modelEntity.getFieldsIterator();
				while (fieldIterator.hasNext()) {
					ModelField field = fieldIterator.next();
					String fieldName = field.getName();
					String fieldType = field.getType();
					Debug.logInfo("adding entity property .... " + fieldName, module);
					e1 = e1.addProperty(new Property(null, fieldName, ValueType.PRIMITIVE, genericValue.get(fieldName)));
				}
				e1.setId(Util.createId(entityName + "s", genericValue));
				// entityList.add(e1);
				resultMap.put(e1, genericValue);
			}
		}
		return resultMap;
	}

	private EntityCollection getRelatedEntityCollection(GenericValue originGenericValue, EdmNavigationProperty edmNavigationProperty) {

		Map<Entity, GenericValue> relatedMaps = this.getRelatedEntityMaps(originGenericValue, edmNavigationProperty, true);
		EntityCollection entityCollection = new EntityCollection();
		// check for which EdmEntitySet the data is requested
		List<Entity> entityList = entityCollection.getEntities();
		Set<Entity> entitySet = relatedMaps.keySet();
		entityList.addAll(entitySet);
		return entityCollection;
	}

	public void addExpandOption(ExpandOption expandOption, Entity entity, GenericValue genericValue) {
		EdmNavigationProperty edmNavigationProperty = null;
		if (expandOption != null) {
			List<ExpandItem> expandItems = expandOption.getExpandItems();
			for (ExpandItem expandItem : expandItems) {
				// ExpandItem expandItem = expandOption.getExpandItems().get(0);
				if (expandItem.isStar()) { // will expand all the navigation items
					// TODO
				} else {
					UriResource uriResource = expandItem.getResourcePath().getUriResourceParts().get(0);
					Debug.logInfo("===============" + uriResource.getSegmentValue(), module);
					if(uriResource instanceof UriResourceNavigation) {
					    edmNavigationProperty = ((UriResourceNavigation) uriResource).getProperty();
						Debug.logInfo("=============== edmNavigationProperty = " + edmNavigationProperty.getName(), module);
					}
				}
				if (edmNavigationProperty != null) {
					String navPropName = edmNavigationProperty.getName();
					Debug.logInfo("=============== navPropName = " + navPropName, module);
					if (edmNavigationProperty.isCollection()) {
						// EntityCollection expandEntityCollection = getRelatedEntityCollection(genericValue, edmNavigationProperty);
						Map<Entity, GenericValue> expandEntityMaps = getRelatedEntityMaps(genericValue, edmNavigationProperty, true);
						EntityCollection expandEntityCollection = getEntityCollectionFromEGMap(expandEntityMaps);
						/********* expand里可能还带有expand，是个层层嵌套的结构 ********************/
						ExpandOption nestedExpandOption = expandItem.getExpandOption(); // expand nested in expand
						// 对expandEntityCollection里对每一个entity都要做expand处理
						List<Entity> expandEntities = expandEntityCollection.getEntities();
						for (Entity expandEntity : expandEntities) {
							GenericValue expandGenericValue = expandEntityMaps.get(expandEntity);
							this.addExpandOption(nestedExpandOption, expandEntity, expandGenericValue);
						}
						/********* end: expand里可能还带有expand，是个层层嵌套的结构 ********************/

						Link link = new Link();
						link.setTitle(navPropName);
						link.setType(Constants.ENTITY_NAVIGATION_LINK_TYPE);
						link.setRel(Constants.NS_ASSOCIATION_LINK_REL + navPropName);
						link.setInlineEntitySet(expandEntityCollection);
						// link.setHref(expandEntityCollection.getId().toASCIIString());
						entity.getNavigationLinks().add(link);
					} else {
						Map<Entity, GenericValue> expandEntityMaps = getRelatedEntityMaps(genericValue, edmNavigationProperty, false);
						Entity expandEntity = getEntityFromEGMap(expandEntityMaps);
						if (null != expandEntity) {
							/********* expand里可能还带有expand，是个层层嵌套的结构 ********************/
							ExpandOption nestedExpandOption = expandItem.getExpandOption(); // expand nested in expand
							GenericValue expandGenericValue = expandEntityMaps.get(expandEntity);
							this.addExpandOption(nestedExpandOption, expandEntity, expandGenericValue);
							/********* end: expand里可能还带有expand，是个层层嵌套的结构 ********************/

							Link link = new Link();
							link.setTitle(navPropName);
							link.setType(Constants.ENTITY_NAVIGATION_LINK_TYPE);
							link.setRel(Constants.NS_ASSOCIATION_LINK_REL + navPropName);
							link.setInlineEntity(expandEntity);
							// link.setHref(expandEntityCollection.getId().toASCIIString());
							entity.getNavigationLinks().add(link);
						}
					}
				}
			}
		}
	}

	private Entity getEntityFromEGMap(Map<Entity, GenericValue> entityMaps) {
		Set<Entity> entitySet = entityMaps.keySet();
		return entitySet.iterator().hasNext() ? entitySet.iterator().next() : null;
	}

	private EntityCollection getEntityCollectionFromEGMap(Map<Entity, GenericValue> entityMaps) {
		EntityCollection entityCollection = new EntityCollection();
		// check for which EdmEntitySet the data is requested
		List<Entity> entityList = entityCollection.getEntities();
		Set<Entity> entitySet = entityMaps.keySet();
		entityList.addAll(entitySet);
		return entityCollection;
	}

	public Entity createEntityData(EdmEntitySet edmEntitySet, org.apache.olingo.commons.api.data.Entity entityToCreate) {
		try {
			
			/********** 获取系统所有的service **************************************************/
			Set<String> serviceNames = dispatcher.getDispatchContext().getAllServiceNames();
			
			EdmEntityType edmEntityType = edmEntitySet.getEntityType();
			if (serviceNames.contains("create" + edmEntityType.getName())) { // ofbiz存在创建这个对象的service，那就建议用户调用service，不要直接创建
				Debug.logInfo(edmEntityType.getName() + " exists creation service", module);
				return null;
			}
			Map<String, Object> fieldMap = convertFieldMap(entityToCreate);
			GenericValue newGenericValue = delegator.makeValue(edmEntityType.getName(), fieldMap);
			newGenericValue.create();
		} catch (Exception e) {
			e.printStackTrace();
		}

	    return entityToCreate;
	}

	private Map<String, Object> convertFieldMap(org.apache.olingo.commons.api.data.Entity entityToCreate) {
		Map<String, Object> fieldMap = new HashMap<String, Object>();
		List<Property> properties = entityToCreate.getProperties();
		for (Property property : properties) {
			fieldMap.put(property.getName(), property.getValue().toString());
		}
		return fieldMap;
	}

	public Property readFunctionImportPrimitive(UriResourceFunction uriResourceFunction) throws GenericServiceException, GenericEntityException {
		Debug.logInfo("------------------------------------------------------------ in storage.readFunctionImportPrimitive", module);
		String functionName = uriResourceFunction.getFunctionImport().getName(); // functionName就是ofbiz的serviceName
		ModelService modelService = dispatcher.getDispatchContext().getModelService(functionName);
		final List<UriParameter> parameters = uriResourceFunction.getParameters();
	    Map<String, Object> serviceParameters = new HashMap<String, Object>();
        String regexp = "\'";
	    for (UriParameter parameter: parameters) {
	    		String paramName = parameter.getName();
	    		ModelParam modelParam = modelService.getParam(paramName);
	    		String paramType = modelParam.getType();
	    		String valueString = parameter.getText();
	    		Debug.logInfo("------- type = " + paramType + ", value = " + valueString, module);
	    		if (valueString == null || valueString.equals("null")) {
	    			continue;
	    		}
	    		if (paramType.equals("String")) {
	    			valueString = valueString.replaceAll(regexp, "");
	    			serviceParameters.put(paramName, valueString);
	    		} else if (paramType.equals("BigDecimal") || paramType.equals("java.math.BigDecimal")) {
	    			serviceParameters.put(paramName, new BigDecimal(valueString));
	    		} else if (paramType.equals("Timestamp") || paramType.equals("java.sql.Timestamp")) {
	    			valueString = valueString.replaceAll(regexp, "");
	    			serviceParameters.put(paramName, Timestamp.valueOf(valueString + " 00:00:00")); // 目前只发现odata支持日期，所以要把日期转换成Timestamp的格式
	    		} else if (paramType.equals("Double")) {
	    			serviceParameters.put(paramName, new Double(valueString));
	    		} else if (paramType.equals("Long")) {
	    			serviceParameters.put(paramName, new Long(valueString));
	    		} else { // 理论上，代码不会运行到这里，否则，上面到if就应该多加几个数据类型判断
	    			Debug.logInfo("================================ NO !!!!!!!!!!!!!!!!!!!!!!", module);
	    			serviceParameters.put(parameter.getName(), valueString);
	    		}
	    }
	    /********************* 先暂时写死system，今后需要从http session里获取userLogin *****************************/
//	    GenericValue userLogin = delegator.findByPrimaryKey("UserLogin", UtilMisc.toMap("userLoginId", "system"));
		GenericValue userLogin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "system"));
		serviceParameters.put("userLogin", userLogin);
	    /******************************************************************************************************/
	    Map<String, Object> result = dispatcher.runSync(functionName, serviceParameters);
		List<String> outParamNames = modelService.getParameterNames("OUT", true, false);
		// 运行到这里的service，都是有且只有一个输出参数
		String outParamName = outParamNames.get(0);
		Object outValue = result.get(outParamName);
		if (outValue != null) {
			return new Property(null, outParamName, ValueType.PRIMITIVE, outValue);
		}
		return null;
	}

	public Entity readFunctionImportEntity(UriResourceFunction uriResourceFunction) throws GenericServiceException {
		String functionName = uriResourceFunction.getFunctionImport().getName(); // functionName就是ofbiz的serviceName
		ModelService modelService = dispatcher.getDispatchContext().getModelService(functionName);
		final List<UriParameter> parameters = uriResourceFunction.getParameters();
	    Map<String, Object> serviceParameters = new HashMap<String, Object>();
        String regexp = "\'";
	    for (UriParameter parameter: parameters) {
	    		String paramName = parameter.getName();
	    		ModelParam modelParam = modelService.getParam(paramName);
	    		String paramType = modelParam.getType();
	    		String valueString = parameter.getText();
	    		if (paramType.equals("String")) {
	    			valueString = valueString.replaceAll(regexp, "");
	    			serviceParameters.put(paramName, valueString);
	    		} else if (paramType.equals("BigDecimal") || paramType.equals("java.math.BigDecimal")) {
	    			serviceParameters.put(paramName, new BigDecimal(valueString));
	    		} else if (paramType.equals("Timestamp") || paramType.equals("java.sql.Timestamp")) {
	    			serviceParameters.put(paramName, Timestamp.valueOf(valueString));
	    		} else if (paramType.equals("Double")) {
	    			serviceParameters.put(paramName, new Double(valueString));
	    		} else if (paramType.equals("Long")) {
	    			serviceParameters.put(paramName, new Long(valueString));
	    		} else { // 理论上，代码不会运行到这里，否则，上面到if就应该多加几个数据类型判断
	    			serviceParameters.put(parameter.getName(), valueString);
	    		}
	    }
	    Map<String, Object> result = dispatcher.runSync(functionName, serviceParameters);
		List<String> outParamNames = modelService.getParameterNames("OUT", true, false);
		// 运行到这里的service，都是有且只有一个输出参数
		String outParamName = outParamNames.get(0);
		GenericValue outValue = (GenericValue) result.get(outParamName);
		if (outValue != null) {
			EdmEntityType edmEntityType = uriResourceFunction.getFunctionImport().getReturnedEntitySet().getEntityType();
			return genericValueToEntity(edmEntityType, outValue);
		}
		return null;
	}

	public void processActionVoid(UriResourceAction uriResourceAction, Map<String, Parameter> actionParameters) throws GenericServiceException, GenericEntityException {
		Debug.logInfo("------------------------------------------------------------ in storage.processActionVoid", module);
		String actionName = uriResourceAction.getActionImport().getName(); // functionName就是ofbiz的serviceName
		ModelService modelService = dispatcher.getDispatchContext().getModelService(actionName);
		// final List<UriParameter> parameters = uriResourceAction.getParameters();
		Set<String> actionParameterNames = actionParameters.keySet();
	    Map<String, Object> serviceParameters = new HashMap<String, Object>();
	    for (String actionParameterName: actionParameterNames) {
	    		ModelParam modelParam = modelService.getParam(actionParameterName);
	    		String paramType = modelParam.getType();
	    		Parameter parameter = actionParameters.get(actionParameterName);
	    		
	    		Object value = parameter.getValue();
	    		if (value == null) {
		    		Debug.logInfo("================================ get null for " + actionParameterName, module);
		    		continue;
	    		}
	    		if (parameter.isCollection()) { // 参数是List
	    			List values = parameter.asCollection();
	    			serviceParameters.put(actionParameterName, values);
	    			for (Object valueitem:values) {
	    				Debug.logInfo("=================== valueitem = " + valueitem + ", java type = " + valueitem.getClass().getName() , module);
	    			}
	    		} else { // 参数不是List
		    		String edmType = parameter.getType();
		    		Debug.logInfo("------- modelType = " + paramType + ", edmType = " + parameter.getType() + ", java type = " + value.getClass().getName() + ", value = " + value, module);
		    		if (paramType.equals("Timestamp") || paramType.equals("java.sql.Timestamp")) {
		    			Timestamp theTimestamp = UtilDateTime.getTimestamp(((Calendar) value).getTimeInMillis());
		    			serviceParameters.put(actionParameterName, theTimestamp);
		    		} else if (edmType.equals("Edm.Double")) {
		    			serviceParameters.put(actionParameterName, new BigDecimal((Double) value));
		    		} else if (edmType.equals("Edm.Int64")) {
		    			serviceParameters.put(actionParameterName, new BigDecimal((Long) value));
		    		} else {
		    			serviceParameters.put(actionParameterName, value);
		    		}
	    		}
	    }
	    /********************* 先暂时写死system，今后需要从http session里获取userLogin *****************************/
//	    GenericValue userLogin = delegator.findByPrimaryKey("UserLogin", UtilMisc.toMap("userLoginId", "system"));
		GenericValue userLogin = delegator.findOne("UserLogin", false, UtilMisc.toMap("userLoginId", "system"));
	       serviceParameters.put("userLogin", userLogin);
	    /******************************************************************************************************/
	    	Map<String, Object> result = dispatcher.runSync(actionName, serviceParameters);
		// List<String> outParamNames = modelService.getParameterNames("OUT", true, false);
	}

	public BigDecimal entitySetDataApplySum(EdmEntitySet edmEntitySet, FilterOption filterOption,
			StandardMethod standardMethod, Expression expression) {
		String entityName = edmEntitySet.getEntityType().getName();
		List<GenericValue> genericValues = null;
		EntityFindOptions efo = new EntityFindOptions();
		efo.setMaxRows(MAX_ROWS);

		try {
			EntityCondition entityCondition = null;
			if (filterOption != null) {
				Expression filterExpression = filterOption.getExpression();
				OfbizExpressionVisitor expressionVisitor = new OfbizExpressionVisitor();
				entityCondition = (EntityCondition) filterExpression.accept(expressionVisitor);
			}

            genericValues = delegator.findList(entityName, entityCondition, null, null, efo, false);
		} catch (GenericEntityException e) {
			e.printStackTrace();
		} catch (ExpressionVisitException e) {
			e.printStackTrace();
		} catch (ODataApplicationException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String expressionName = expression.toString(); // 如果是OrderHeader对象的grandTotal，则这里的值会是[grandTotal]
		String fieldName = expressionName.substring(1, expressionName.length() - 1);
		Debug.logInfo("----------------------------- got fieldName = " + fieldName, module);
		BigDecimal sumValue = BigDecimal.ZERO;
		for (GenericValue genericValue : genericValues) {
			sumValue = genericValue.getBigDecimal(fieldName).add(sumValue);
		}


		return sumValue;
	}
}
