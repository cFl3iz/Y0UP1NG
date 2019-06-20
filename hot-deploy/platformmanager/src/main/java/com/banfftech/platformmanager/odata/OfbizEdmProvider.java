package com.banfftech.platformmanager.odata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlAbstractEdmProvider;
import org.apache.olingo.commons.api.edm.provider.CsdlAction;
import org.apache.olingo.commons.api.edm.provider.CsdlActionImport;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityContainer;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityContainerInfo;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
import org.apache.olingo.commons.api.edm.provider.CsdlFunction;
import org.apache.olingo.commons.api.edm.provider.CsdlFunctionImport;
import org.apache.olingo.commons.api.edm.provider.CsdlNavigationProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlNavigationPropertyBinding;
import org.apache.olingo.commons.api.edm.provider.CsdlParameter;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlPropertyRef;
import org.apache.olingo.commons.api.edm.provider.CsdlReturnType;
import org.apache.olingo.commons.api.edm.provider.CsdlSchema;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.model.ModelEntity;
import org.apache.ofbiz.entity.model.ModelField;
import org.apache.ofbiz.entity.model.ModelKeyMap;
import org.apache.ofbiz.entity.model.ModelReader;
import org.apache.ofbiz.entity.model.ModelRelation;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ModelParam;
import org.apache.ofbiz.service.ModelService;

public class OfbizEdmProvider extends CsdlAbstractEdmProvider {

	public static final String module = OfbizEdmProvider.class.getName();
	public static final String NAMESPACE = "org.ofbiz";
	public static final String CONTAINER_NAME = "Container";
	public static final FullQualifiedName CONTAINER = new FullQualifiedName(NAMESPACE, CONTAINER_NAME);
	public static final Set<String> entityNames = new HashSet<String>();
	public static final Map<String, FullQualifiedName> OFBIZ_SERVICE_MAP = new HashMap<String, FullQualifiedName>();
	public static Set<String> serviceNames = null;
	public static final Set<String> possibleActionNames = new HashSet<String>();
	public static final Set<String> actionNames = new HashSet<String>();
	public static final Set<String> functionNames = new HashSet<String>();

	public final static Map<String, EdmPrimitiveTypeKind> FIELDMAP = new HashMap<String, EdmPrimitiveTypeKind>();
	static {
		FIELDMAP.put("id", EdmPrimitiveTypeKind.String);
		FIELDMAP.put("id-ne", EdmPrimitiveTypeKind.String);
		FIELDMAP.put("id-long", EdmPrimitiveTypeKind.String);
		FIELDMAP.put("id-long-ne", EdmPrimitiveTypeKind.String);
		FIELDMAP.put("id-vlong", EdmPrimitiveTypeKind.String);
		FIELDMAP.put("id-vlong-ne", EdmPrimitiveTypeKind.String);
		FIELDMAP.put("comment", EdmPrimitiveTypeKind.String);
		FIELDMAP.put("description", EdmPrimitiveTypeKind.String);
		FIELDMAP.put("currency-precise", EdmPrimitiveTypeKind.Double);
		FIELDMAP.put("currency-amount", EdmPrimitiveTypeKind.Double);
		FIELDMAP.put("fixed-point", EdmPrimitiveTypeKind.Double);
		FIELDMAP.put("date-time", EdmPrimitiveTypeKind.Date);
		FIELDMAP.put("date", EdmPrimitiveTypeKind.Date);
		FIELDMAP.put("indicator", EdmPrimitiveTypeKind.String);
		FIELDMAP.put("floating-point", EdmPrimitiveTypeKind.Double);
		FIELDMAP.put("long-varchar", EdmPrimitiveTypeKind.String);
		FIELDMAP.put("short-varchar", EdmPrimitiveTypeKind.String);
		FIELDMAP.put("very-long", EdmPrimitiveTypeKind.String);
		FIELDMAP.put("very-short", EdmPrimitiveTypeKind.String);
		FIELDMAP.put("value", EdmPrimitiveTypeKind.String);
		FIELDMAP.put("name", EdmPrimitiveTypeKind.String);
		FIELDMAP.put("url", EdmPrimitiveTypeKind.String);
		FIELDMAP.put("numeric", EdmPrimitiveTypeKind.Int64);
		FIELDMAP.put("floating-point", EdmPrimitiveTypeKind.Double);
		FIELDMAP.put("blob", EdmPrimitiveTypeKind.Binary);
		FIELDMAP.put("byte-array", EdmPrimitiveTypeKind.Binary);
		FIELDMAP.put("object", EdmPrimitiveTypeKind.Binary);
		FIELDMAP.put("time", EdmPrimitiveTypeKind.TimeOfDay);
		FIELDMAP.put("email", EdmPrimitiveTypeKind.String);
		FIELDMAP.put("credit-card-number", EdmPrimitiveTypeKind.String);
		FIELDMAP.put("credit-card-date", EdmPrimitiveTypeKind.String);
		FIELDMAP.put("tel-number", EdmPrimitiveTypeKind.String);
	};
	public final static Map<String, EdmPrimitiveTypeKind> PARAM_TYPE_MAP = new HashMap<String, EdmPrimitiveTypeKind>();
	static {
		PARAM_TYPE_MAP.put("String", EdmPrimitiveTypeKind.String);
		PARAM_TYPE_MAP.put("java.lang.String", EdmPrimitiveTypeKind.String);
		PARAM_TYPE_MAP.put("BigDecimal", EdmPrimitiveTypeKind.Double);
		PARAM_TYPE_MAP.put("java.math.BigDecimal", EdmPrimitiveTypeKind.Double);
		PARAM_TYPE_MAP.put("java.sql.Timestamp", EdmPrimitiveTypeKind.Date);
		PARAM_TYPE_MAP.put("Timestamp", EdmPrimitiveTypeKind.Date);
		PARAM_TYPE_MAP.put("java.sql.Date", EdmPrimitiveTypeKind.Date);
		PARAM_TYPE_MAP.put("Long", EdmPrimitiveTypeKind.Int64);
		PARAM_TYPE_MAP.put("Double", EdmPrimitiveTypeKind.Double);
		PARAM_TYPE_MAP.put("Boolean", EdmPrimitiveTypeKind.Boolean);
		PARAM_TYPE_MAP.put("Integer", EdmPrimitiveTypeKind.Int64);
		PARAM_TYPE_MAP.put("java.sql.Time", EdmPrimitiveTypeKind.Date);
	}

	private Delegator delegator;
	private LocalDispatcher dispatcher;

	public OfbizEdmProvider(Delegator delegator, LocalDispatcher dispatcher) {
		super();
		this.delegator = delegator;
		this.dispatcher = dispatcher;
		if (entityNames.isEmpty()) {
			ModelReader reader = delegator.getModelReader();
			Map<String, TreeSet<String>> packageEntities = null;
			try {
				/********** 获取系统所有的entity **************************************************/
				packageEntities = reader.getEntitiesByPackage(null, null);
				Collection collectionEntityNameSet = packageEntities.values();
				Iterator it = collectionEntityNameSet.iterator();
				while (it.hasNext()) {
					Set<String> entityNameSet = (Set) it.next();
					entityNames.addAll(entityNameSet);
				}
			} catch (GenericEntityException e) {
				e.printStackTrace();
			}
		}
		if (serviceNames == null) {
			/********** 获取系统所有的service **************************************************/
			serviceNames = dispatcher.getDispatchContext().getAllServiceNames();
			for (String serviceName : serviceNames) {
				FullQualifiedName fullQualifiedServiceName = new FullQualifiedName(NAMESPACE, serviceName);
				OFBIZ_SERVICE_MAP.put(serviceName, fullQualifiedServiceName);
			}
		}
	}

	public CsdlEntityType getEntityType(FullQualifiedName entityTypeName) {

		String entityName = entityTypeName.getName(); // Such as Invoice
		// Debug.logInfo("entityName = " + entityName, module);
		ModelReader reader = delegator.getModelReader();
		ModelEntity modelEntity;
		try {
			modelEntity = reader.getModelEntity(entityName);
		} catch (GenericEntityException e) {
			e.printStackTrace();
			return null;
		}
		// 先添加主键
		Iterator<ModelField> pksIterator = modelEntity.getPksIterator();
		List<CsdlPropertyRef> propertyRefs = new ArrayList<CsdlPropertyRef>();
		while (pksIterator.hasNext()) {
			ModelField field = pksIterator.next();
			String fieldName = field.getName();
			// Debug.logInfo("pk field = " + fieldName, module);
			CsdlPropertyRef propertyRef = new CsdlPropertyRef();
			propertyRef.setName(fieldName);
			propertyRefs.add(propertyRef);
		}
		// 再添加其它字段
		Iterator<ModelField> fieldIterator = modelEntity.getFieldsIterator();
		List<CsdlProperty> propertyList = new ArrayList<CsdlProperty>();
		while (fieldIterator.hasNext()) {
			ModelField field = fieldIterator.next();
			String fieldName = field.getName();
			String fieldType = field.getType();
			// Debug.logInfo("got some ---------------------fieldName = " + fieldName + ",
			// fieldType = " + fieldType, module);
			CsdlProperty csdlProperty = new CsdlProperty().setName(fieldName)
					.setType(FIELDMAP.get(fieldType).getFullQualifiedName());
			propertyList.add(csdlProperty);
		}
		// 最后添加外键
		Iterator<ModelRelation> relationsIterator = modelEntity.getRelationsIterator();
		List<CsdlNavigationProperty> navPropList = new ArrayList<CsdlNavigationProperty>();
		if (relationsIterator != null) {
			while (relationsIterator.hasNext()) {
				// Debug.logInfo("got some relations from " + entityName, module);
				ModelRelation modelRelation = relationsIterator.next();
				// Debug.logInfo("got some relations type = " + modelRelation.getType(),
				// module);
				String relEntityName = modelRelation.getRelEntityName();
				if (entityName.equals(relEntityName)) { // 这种是父子关系，例如PartyIdentificationType.parentTypeId
					continue;
				}
				String relTitle = modelRelation.getTitle();
				/***** 处理反向关系，odata好像必须要两个entity都有互相的relation，也就是odata里的partner *************/
				boolean foundPartner = false;
				ModelEntity relEntity = null;
				try {
					relEntity = reader.getModelEntity(relEntityName);
				} catch (GenericEntityException e) {
					e.printStackTrace();
					return null;
				}
				Iterator<ModelRelation> partnerRelationsIterator = relEntity.getRelationsIterator();
				while (partnerRelationsIterator.hasNext()) {
					ModelRelation partnerModelRelation = partnerRelationsIterator.next();
					String partnerRelEntityName = partnerModelRelation.getRelEntityName();
					if (entityName.equals(partnerRelEntityName)) {
						foundPartner = true;
					}
				}
				if (!foundPartner) { // 没有partner，这个relation就不要体现在odata上了
					continue;
				}
				/*****
				 * 结束，处理反向关系，odata好像必须要两个entity都有互相的relation，也就是odata里的partner
				 *************/
				FullQualifiedName fullQualifiedName = new FullQualifiedName(NAMESPACE, relEntityName);
				CsdlNavigationProperty navProp = new CsdlNavigationProperty();
				
				String navPropName;
				if (modelRelation.getTitle() == null || modelRelation.getTitle().isEmpty()) {
					navPropName = relEntityName;
				} else {
					navPropName = modelRelation.getTitle() + relEntityName;
				}
				if (modelRelation.getType().equals("one") || modelRelation.getType().equals("one-nofk")) { // 如果是多对一，也就是简单外键关系
					//Iterator<ModelKeyMap> keyMapIterator = modelRelation.getKeyMapsIterator();
					String fieldName = null;
					for(ModelKeyMap modelKeyMap : modelRelation.getKeyMaps() ){
						fieldName = modelKeyMap.getFieldName();
					}
//					while (keyMapIterator.hasNext()) {
//						ModelKeyMap modelKeyMap = keyMapIterator.next();
//						fieldName = modelKeyMap.getFieldName();
//					}
					//navProp = navProp.setName(relEntityName).setType(fullQualifiedName).setPartner(entityName)
					//		.setNullable(true);
					//navProp = navProp.setName(navPropName).setType(fullQualifiedName).setPartner(entityName)
					//		.setNullable(true);
					navProp = navProp.setName(navPropName).setType(fullQualifiedName).setNullable(true);
				} else {
					//navProp = navProp.setName(relEntityName).setType(fullQualifiedName).setPartner(entityName)
					//		.setCollection(true);
					//navProp = navProp.setName(navPropName).setType(fullQualifiedName).setPartner(entityName)
					//		.setCollection(true);
					navProp = navProp.setName(navPropName).setType(fullQualifiedName).setCollection(true);
				}
				navPropList.add(navProp);
			}
		}

		CsdlEntityType entityType = new CsdlEntityType();
		entityType.setName(entityName);
		entityType.setProperties(propertyList);
		entityType.setKey(propertyRefs);
		if (UtilValidate.isNotEmpty(navPropList)) {
			entityType.setNavigationProperties(navPropList);
		}

		return entityType;

		/****
		 * // this method is called for one of the EntityTypes that are configured in
		 * the // Schema if (entityTypeName.equals(ET_INVOICE_FQN)) {
		 * 
		 * // create EntityType properties CsdlProperty id = new
		 * CsdlProperty().setName("invoiceId")
		 * .setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()); CsdlProperty
		 * name = new CsdlProperty().setName("partyIdFrom")
		 * .setType(EdmPrimitiveTypeKind.String.getFullQualifiedName()); CsdlProperty
		 * description = new CsdlProperty().setName("description")
		 * .setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		 * 
		 * // create CsdlPropertyRef for Key element CsdlPropertyRef propertyRef = new
		 * CsdlPropertyRef(); propertyRef.setName("invoiceId");
		 * 
		 * // configure EntityType CsdlEntityType entityType = new CsdlEntityType();
		 * entityType.setName(ET_INVOICE_NAME);
		 * entityType.setProperties(Arrays.asList(id, name, description));
		 * entityType.setKey(Collections.singletonList(propertyRef));
		 * 
		 * return entityType; }
		 * 
		 * return null;
		 ****/
	}

	public CsdlEntitySet getEntitySet(FullQualifiedName entityContainer, String entitySetName) {
		
		String entityName = entitySetName.substring(0, entitySetName.length() - 1);
		if (!entityNames.contains(entityName)) {
			return null;
		}
		// Debug.logInfo("in getEntitySet, entityName = " + entityName, module);
		if (entityContainer.equals(CONTAINER)) {
			// Debug.logInfo("got some ---------------------1 " + entityName, module);
			CsdlEntitySet entitySet = new CsdlEntitySet();
			// Debug.logInfo("got some ---------------------2 " + entityName, module);
			entitySet.setName(entitySetName);
			entitySet.setType(new FullQualifiedName(NAMESPACE, entityName));
			// Debug.logInfo("got some ---------------------3 " + entityName, module);

			/*********** 处理navigation *******************/
			ModelReader reader = delegator.getModelReader();
			ModelEntity modelEntity;
			try {
				modelEntity = reader.getModelEntity(entityName);
			} catch (GenericEntityException e) {
				e.printStackTrace();
				return null;
			}
			Iterator<ModelRelation> relationsIterator = modelEntity.getRelationsIterator();
			if (relationsIterator != null) {
				List<CsdlNavigationPropertyBinding> navPropBindingList = new ArrayList<CsdlNavigationPropertyBinding>();
				while (relationsIterator.hasNext()) {
					// Debug.logInfo("got some relations from " + entityName, module);
					ModelRelation modelRelation = relationsIterator.next();
					String relEntityName = modelRelation.getRelEntityName();
					String bindingPath;
					if (modelRelation.getTitle() == null) {
						bindingPath = relEntityName;
					} else {
						bindingPath = modelRelation.getTitle() + relEntityName;
					}
					CsdlNavigationPropertyBinding navPropBinding = new CsdlNavigationPropertyBinding();
					// Debug.logInfo("------------- seting binding path = " + relEntityName + ", and
					// target = " + relEntityName + "s", module);
					// navPropBinding.setPath(relEntityName); // the path from entity type to navigation property
					navPropBinding.setPath(bindingPath);
					navPropBinding.setTarget(relEntityName + "s"); // target entitySet, where the nav prop points to
					navPropBindingList.add(navPropBinding);
				}
				entitySet.setNavigationPropertyBindings(navPropBindingList);
			}
			/*********** 结束，处理navigation *******************/
			return entitySet;
		}
		// Debug.logInfo("got some ---------------------4 " + entityName, module);
		return null;
	}

	public CsdlEntityContainer getEntityContainer() {
		Debug.logInfo("+++++++++++++++++++++++++ entering into getEntityContainer()", module);
		try {
		// create EntitySets
		List<CsdlEntitySet> entitySets = new ArrayList<CsdlEntitySet>();
		for (String entityName : entityNames) {
			entitySets.add(getEntitySet(CONTAINER, entityName + "s"));
		}

		// Create function imports
		List<CsdlFunctionImport> functionImports = new ArrayList<CsdlFunctionImport>();
		Iterator<String> serviceNameIt = functionNames.iterator();
		while (serviceNameIt.hasNext()) {
			functionImports.add(getFunctionImport(CONTAINER, serviceNameIt.next()));
		}

		// Create action imports
		List<CsdlActionImport> actionImports = new ArrayList<CsdlActionImport>();
		Iterator<String> actionNameIt = actionNames.iterator();
		while (actionNameIt.hasNext()) {
			actionImports.add(getActionImport(CONTAINER, actionNameIt.next()));
		}

		// create EntityContainer
		CsdlEntityContainer entityContainer = new CsdlEntityContainer();
		entityContainer.setName(CONTAINER_NAME);
		entityContainer.setEntitySets(entitySets);
		entityContainer.setFunctionImports(functionImports);
		entityContainer.setActionImports(actionImports);
		

		return entityContainer;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<CsdlSchema> getSchemas() {
		Debug.logInfo("+++++++++++++++++++++++++ entering into getSchemas()", module);
		// create Schema
		CsdlSchema schema = new CsdlSchema();
		schema.setNamespace(NAMESPACE);

		// add EntityTypes
		List<CsdlEntityType> entityTypes = new ArrayList<CsdlEntityType>();
		for (String entityName : entityNames) {
			FullQualifiedName fullQualifiedName = new FullQualifiedName(NAMESPACE, entityName);
			entityTypes.add(getEntityType(fullQualifiedName));
		}
		schema.setEntityTypes(entityTypes);

		// add functions
		List<CsdlFunction> functions = new ArrayList<CsdlFunction>();
		Iterator<String> nameIt;
		if (functionNames.isEmpty()) { // 第一次运行，还不清楚哪些service可以用到odata的function，所以遍历所有service
			nameIt = serviceNames.iterator();
		} else {
			nameIt = functionNames.iterator();
		}
		// Iterator<String> nameIt = serviceNames.iterator();
		while (nameIt.hasNext()) {
			String serviceName = nameIt.next();
			FullQualifiedName fullQualifiedServiceName = OFBIZ_SERVICE_MAP.get(serviceName);
			List<CsdlFunction> serviceFunctions = getFunctions(fullQualifiedServiceName);
			if (serviceFunctions == null) {
				// Debug.logInfo("=================== not a valid odata function " +
				// serviceName, module);
				continue;
			}
			functions.addAll(serviceFunctions);
		}
		schema.setFunctions(functions);

		// add actions
		List<CsdlAction> actions = new ArrayList<CsdlAction>();
		Iterator<String> actionNameIt = possibleActionNames.iterator();
		while (actionNameIt.hasNext()) {
			String serviceName = actionNameIt.next();
			FullQualifiedName fullQualifiedServiceName = OFBIZ_SERVICE_MAP.get(serviceName);
			List<CsdlAction> serviceActions = getActions(fullQualifiedServiceName);
			if (serviceActions == null) {
				// Debug.logInfo("=================== not a valid odata action " + serviceName,
				// module);
				continue;
			}
			actions.addAll(serviceActions);
			actionNames.add(serviceName);
		}
		schema.setActions(actions);

		// add EntityContainer
		schema.setEntityContainer(getEntityContainer());

		// finally
		List<CsdlSchema> schemas = new ArrayList<CsdlSchema>();
		schemas.add(schema);
		return schemas;
	}

	public CsdlEntityContainerInfo getEntityContainerInfo(FullQualifiedName entityContainerName) {
		Debug.logInfo("+++++++++++++++++++++++++ entering into getEntityContainerInfo()", module);

		// This method is invoked when displaying the Service Document at e.g.
		// http://localhost:8080/DemoService/DemoService.svc
		if (entityContainerName == null || entityContainerName.equals(CONTAINER)) {
			CsdlEntityContainerInfo entityContainerInfo = new CsdlEntityContainerInfo();
			entityContainerInfo.setContainerName(CONTAINER);
			return entityContainerInfo;
		}

		return null;
	}

	@Override
	public List<CsdlFunction> getFunctions(final FullQualifiedName functionName) {
		// Debug.logInfo("-------------------------------------- entering getFunctions, functionname = " + functionName.getName(), module);
		String serviceName = functionName.getName();
		final List<CsdlFunction> functions = new ArrayList<CsdlFunction>();
		ModelService modelService;
		// 以下代码，if else太多，可能会造成看代码人的不适，请谨慎阅读！
		try {
			modelService = dispatcher.getDispatchContext().getModelService(serviceName);

			// 处理OUT参数
			List<String> outParamNames = modelService.getParameterNames("OUT", true, false);
			boolean hasValidReturn = false;
			EdmPrimitiveTypeKind returnEdmType = null;
			if (outParamNames.size() == 0) { // 没有返回参数，应该属于Action
				// Debug.logInfo("================ has no out param", module);
				if (!possibleActionNames.contains(serviceName)) {
					possibleActionNames.add(serviceName);
				}
				// OFBIZ_SERVICE_MAP.remove(serviceName);
				// serviceNames.remove(serviceName);
				return null;
			}
			if (outParamNames.size() == 1) { // 只有一个返回参数，符合Odata规范
				ModelParam modelParam = modelService.getParam(outParamNames.get(0));
				String type = modelParam.getType();
				if (type.equals("org.apache.ofbiz.entity.GenericValue")) { // GenericValue稍后处理，需要人工猜到底是什么entity
					hasValidReturn = true;
				}
				returnEdmType = PARAM_TYPE_MAP.get(type);
				if (returnEdmType != null) {
					hasValidReturn = true;
				}
				boolean isOptional = modelParam.isOptional();
				if (!hasValidReturn) {
					// OFBIZ_SERVICE_MAP.remove(serviceName);
					// serviceNames.remove(serviceName);
					Debug.logVerbose("================ serviceName = " + serviceName + ", outtype = " + type
							+ ", outname = " + outParamNames.get(0), module);
					return null;
				} else {
					functionNames.add(serviceName); // 符合odata规范，可以作为一个function
				}
			} else { // 有多个返回参数，不符合Odata规范，应该咋办？
				for (String outParamName : outParamNames) {
					ModelParam modelParam = modelService.getParam(outParamName);
					String type = modelParam.getType();
					returnEdmType = PARAM_TYPE_MAP.get(type);
					boolean isOptional = modelParam.isOptional();
				}
				// OFBIZ_SERVICE_MAP.remove(serviceName);
				// serviceNames.remove(serviceName);
				Debug.logVerbose("================ serviceName = " + serviceName + " has more than one out parameter", module);
				return null;
			}
			final CsdlReturnType returnType = new CsdlReturnType();
			if (returnEdmType != null) {
				// returnType.setCollection(true);
				returnType.setType(returnEdmType.getFullQualifiedName());
			} else { // 否则就是GenericValue，需要找到对应的entity
				FullQualifiedName fqn = getFullQualifiedNameByParamName(outParamNames.get(0));
				if (fqn != null) { // 虽然是GenericValue，但是还是有可能不属于任何ofbiz的entity
					returnType.setType(fqn);
				} else {
					return null;
				}
			}

			// 如果OUT这里都通过了，没有被return null弹回去，就继续处理IN参数
			List<String> paramNames = modelService.getParameterNames("IN", true, false);
			List<CsdlParameter> parameters = new ArrayList<CsdlParameter>();
			for (String paramName : paramNames) {
				ModelParam modelParam = modelService.getParam(paramName);
				FullQualifiedName fqn = getFullQualifiedNameByServiceParam(modelParam);
				if (fqn == null) {
					return null;
				}
				final CsdlParameter parameter = new CsdlParameter();
				parameter.setType(fqn);
				parameter.setName(paramName);
				boolean isOptional = modelParam.isOptional();
				parameter.setNullable(isOptional);
				if (modelParam.getType().equals("List")) {
					parameter.setCollection(true);
				}
				parameters.add(parameter);
			}

			final CsdlFunction function = new CsdlFunction();
			function.setName(serviceName).setParameters(parameters).setReturnType(returnType);
			functions.add(function);
		} catch (GenericServiceException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		// Debug.logInfo("================ serviceName = " + serviceName + " has
		// successfully parsed", module);
		return functions;
		/*****************************************************************************
		 * if (functionName.equals(FUNCTION_COUNT_CATEGORIES_FQN)) { // It is allowed to
		 * overload functions, so we have to provide a list of // functions for each
		 * function name
		 * 
		 * // Create the parameter for the function final CsdlParameter parameterAmount
		 * = new CsdlParameter(); parameterAmount.setName(PARAMETER_AMOUNT);
		 * parameterAmount.setNullable(false);
		 * parameterAmount.setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
		 * 
		 * // Create the return type of the function final CsdlReturnType returnType =
		 * new CsdlReturnType(); returnType.setCollection(true);
		 * returnType.setType(ET_CATEGORY_FQN);
		 * 
		 * // Create the function final CsdlFunction function = new CsdlFunction();
		 * function.setName(FUNCTION_COUNT_CATEGORIES_FQN.getName()).setParameters(Arrays.asList(parameterAmount))
		 * .setReturnType(returnType); functions.add(function);
		 * 
		 * return functions; } return null;
		 ***********************************************************************************/
	}

	private FullQualifiedName getFullQualifiedNameByParamName(String paramName) {
		String entityName = paramName;
		// 把首字符变成大写，因为所有的entityName，首字符都是大写
		if (Character.isLowerCase(paramName.charAt(0))) {
			entityName = (new StringBuilder()).append(Character.toUpperCase(paramName.charAt(0))).append(paramName.substring(1)).toString();
		}
		if (entityNames.contains(entityName)) {
			return new FullQualifiedName(NAMESPACE, entityName);
		} else {
			return null;
		}
	}

	@Override
	public CsdlFunctionImport getFunctionImport(FullQualifiedName entityContainer, String functionImportName) {
		//Debug.logInfo("--------------------------------------------entering getFunctionImport", module);
		if (entityContainer.equals(CONTAINER)) {
			FullQualifiedName fullQualifiedName = OFBIZ_SERVICE_MAP.get(functionImportName);
			// return new
			// CsdlFunctionImport().setName(functionImportName).setFunction(fullQualifiedName)
			// .setEntitySet(ES_CATEGORIES_NAME).setIncludeInServiceDocument(true);
			return new CsdlFunctionImport().setName(functionImportName).setFunction(fullQualifiedName)
					.setIncludeInServiceDocument(true);
		}

		return null;
	}

	@Override
	public List<CsdlAction> getActions(final FullQualifiedName actionName) {
		String serviceName = actionName.getName();
		final List<CsdlAction> actions = new ArrayList<CsdlAction>();
		ModelService modelService;
		try {
			// Debug.logInfo("================ serviceName = " + serviceName, module);
			modelService = dispatcher.getDispatchContext().getModelService(serviceName);

			// 处理IN参数
			List<String> paramNames = modelService.getParameterNames("IN", true, false);
			List<CsdlParameter> parameters = new ArrayList<CsdlParameter>();
			for (String paramName : paramNames) {
				ModelParam modelParam = modelService.getParam(paramName);
				FullQualifiedName fqn = getFullQualifiedNameByServiceParam(modelParam);
				if (fqn == null) {
					return null;
				}
				final CsdlParameter parameter = new CsdlParameter();
				parameter.setType(fqn);
				parameter.setName(paramName);
				boolean isOptional = modelParam.isOptional();
				parameter.setNullable(isOptional);
				if (modelParam.getType().equals("List")) {
					parameter.setCollection(true);
				}
				parameters.add(parameter);
			}

			final CsdlAction action = new CsdlAction();
			action.setName(serviceName).setParameters(parameters);
			actions.add(action);
		} catch (GenericServiceException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return actions;
	}

	@Override
	public CsdlActionImport getActionImport(FullQualifiedName entityContainer, String actionImportName) {
		// Debug.logInfo("----------------------------------------------
		// getActionImport", module);
		if (entityContainer.equals(CONTAINER) && actionNames.contains(actionImportName)) {
			FullQualifiedName fullQualifiedName = OFBIZ_SERVICE_MAP.get(actionImportName);
			// return new
			// CsdlFunctionImport().setName(functionImportName).setFunction(fullQualifiedName)
			// .setEntitySet(ES_CATEGORIES_NAME).setIncludeInServiceDocument(true);
			return new CsdlActionImport().setName(actionImportName).setAction(fullQualifiedName);
		}

		return null;
	}
	
	private FullQualifiedName getFullQualifiedNameByServiceParam(ModelParam modelParam) {
		String paramName = modelParam.getName();
		String type = modelParam.getType();
		EdmPrimitiveTypeKind edmType = PARAM_TYPE_MAP.get(type);
		if (edmType != null) { // 直接就是Primitive Type，也就是Integer，String之类的，简单，返回
			 return edmType.getFullQualifiedName();
		} else {
			/*****/
			if (type.equals("org.apache.ofbiz.entity.GenericValue")) { // 如果是GenericValue，就要检查一下，是不是ofbiz已经定义的entity
				return getFullQualifiedNameByParamName(paramName);// 参数是GenericValue，但是也有可能不属于任何entity，将会被抛弃，返回null！
			} else if (type.equals("List")) { // 传入参数是List，那就要搞清楚这个List里是什么类型的对象
				String modelParamDescription = modelParam.description; // ofbiz的service定义里，attribute的description里就是collection里的对象。这个不是ofbiz的标准，是我给出的狗血解决方案
				EdmPrimitiveTypeKind collectionEdmType = PARAM_TYPE_MAP.get(modelParamDescription);
				//Debug.logInfo("--------------- the field " + paramName + ", description " + modelParamDescription + ", serviceName " + serviceName, module);
				if (collectionEdmType != null) {
					return collectionEdmType.getFullQualifiedName();
				} else {
					if (entityNames.contains(modelParamDescription)) {
						return new FullQualifiedName(NAMESPACE, modelParamDescription);
					} else {
						return null;
					}
				}
			} else { // 传入参数是一个非Odata类型的参数，怎么办？抛弃吧！
				return null;
			}
		
		}
	}
}
