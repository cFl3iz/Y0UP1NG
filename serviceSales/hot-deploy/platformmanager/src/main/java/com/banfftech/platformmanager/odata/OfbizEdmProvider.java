package main.java.com.banfftech.platformmanager.odata;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.*;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.model.ModelEntity;
import org.apache.ofbiz.entity.model.ModelField;
import org.apache.ofbiz.entity.model.ModelReader;
import org.apache.ofbiz.entity.model.ModelViewEntity;
import org.apache.ofbiz.entity.model.ModelRelation;
import org.apache.ofbiz.entity.model.ModelKeyMap;

import org.apache.ofbiz.base.util.UtilValidate;
import java.util.*;

public class OfbizEdmProvider extends CsdlAbstractEdmProvider {

	public static final String module = OfbizEdmProvider.class.getName();
	// Service Namespace
	public static final String NAMESPACE = "org.ofbiz";

	// EDM Container
	public static final String CONTAINER_NAME = "Container";
	public static final FullQualifiedName CONTAINER = new FullQualifiedName(NAMESPACE, CONTAINER_NAME);

	// public static final String ET_INVOICE_NAME = "Invoice";
	// public static final FullQualifiedName ET_INVOICE_FQN = new FullQualifiedName(NAMESPACE, ET_INVOICE_NAME);

	// Entity Set Names
	// public static final String ES_INVOICES_NAME = "Invoices";
	private Set<String> entityNames = new HashSet<String>();

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
	private Delegator delegator;

	public OfbizEdmProvider(Delegator delegator) {
		super();
		this.delegator = delegator;
		ModelReader reader = delegator.getModelReader();
		Set<String> packageFilterSet = new HashSet<String>();
		packageFilterSet.add(NAMESPACE);
		Map<String, TreeSet<String>> packageEntities = null;
		try {
			// packageEntities = reader.getEntitiesByPackage(packageFilterSet, null);
			packageEntities = reader.getEntitiesByPackage(null, null);
			// Set<String> entityNameSet = packageEntities.get(NAMESPACE);
			Collection collectionEntityNameSet = packageEntities.values();
			Iterator it = collectionEntityNameSet.iterator();
			while (it.hasNext()) {
				Set<String> entityNameSet = (Set) it.next();
				for (String entityName: entityNameSet) {
					try {
						ModelEntity modelEntity = reader.getModelEntity(entityName);
						/***** 是否要包括view？ ************************
						 if (modelEntity instanceof ModelViewEntity) {
						 continue;
						 }
						 ****** 结束，是否要包括view？ ******************/
						Debug.logInfo("------------------- adding entity -- " + entityName, module);
						entityNames.add(entityName);
					} catch (GenericEntityException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (GenericEntityException e) {
			e.printStackTrace();
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
			// Debug.logInfo("got some ---------------------fieldName = " + fieldName + ", fieldType = " + fieldType, module);
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
				// Debug.logInfo("got some relations type = " + modelRelation.getType(), module);
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
				/***** 结束，处理反向关系，odata好像必须要两个entity都有互相的relation，也就是odata里的partner *************/
				FullQualifiedName fullQualifiedName = new FullQualifiedName(NAMESPACE, relEntityName);
				CsdlNavigationProperty navProp = new CsdlNavigationProperty();
				if (modelRelation.getType().equals("one") || modelRelation.getType().equals("one-nofk")) { // 如果是多对一，也就是简单外键关系
					String fieldName = null;

//					Iterator<ModelKeyMap> keyMapIterator = modelRelation.getKeyMapsIterator();
//
//					while (keyMapIterator.hasNext()) {
//						ModelKeyMap modelKeyMap = keyMapIterator.next();
//						fieldName = modelKeyMap.getFieldName();
//					}
					// OFbiz 16 Method
					for(ModelKeyMap rowKeyMap : modelRelation.getKeyMaps() ){
						fieldName = rowKeyMap.getFieldName();
					}

					navProp = navProp.setName(relEntityName)
							.setType(fullQualifiedName)
							.setPartner(entityName)
							.setNullable(true);
				} else {
					navProp = navProp.setName(relEntityName)
							.setType(fullQualifiedName)
							.setPartner(entityName)
							.setCollection(true);
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
		 // this method is called for one of the EntityTypes that are configured in the
		 // Schema
		 if (entityTypeName.equals(ET_INVOICE_FQN)) {

		 // create EntityType properties
		 CsdlProperty id = new CsdlProperty().setName("invoiceId")
		 .setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		 CsdlProperty name = new CsdlProperty().setName("partyIdFrom")
		 .setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
		 CsdlProperty description = new CsdlProperty().setName("description")
		 .setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

		 // create CsdlPropertyRef for Key element
		 CsdlPropertyRef propertyRef = new CsdlPropertyRef();
		 propertyRef.setName("invoiceId");

		 // configure EntityType
		 CsdlEntityType entityType = new CsdlEntityType();
		 entityType.setName(ET_INVOICE_NAME);
		 entityType.setProperties(Arrays.asList(id, name, description));
		 entityType.setKey(Collections.singletonList(propertyRef));

		 return entityType;
		 }

		 return null;
		 ****/
	}

	public CsdlEntitySet getEntitySet(FullQualifiedName entityContainer, String entitySetName) {

		String entityName = entitySetName.substring(0, entitySetName.length() - 1);
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
					CsdlNavigationPropertyBinding navPropBinding = new CsdlNavigationPropertyBinding();
					// Debug.logInfo("------------- seting binding path = " + relEntityName + ", and target = " + relEntityName + "s", module);
					navPropBinding.setPath(relEntityName); // the path from entity type to navigation property
					navPropBinding.setTarget(relEntityName + "s"); //target entitySet, where the nav prop points to
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

		// create EntitySets
		List<CsdlEntitySet> entitySets = new ArrayList<CsdlEntitySet>();
		for (String entityName: entityNames) {
			entitySets.add(getEntitySet(CONTAINER, entityName + "s"));
		}

		// create EntityContainer
		CsdlEntityContainer entityContainer = new CsdlEntityContainer();
		entityContainer.setName(CONTAINER_NAME);
		entityContainer.setEntitySets(entitySets);

		return entityContainer;
	}

	public List<CsdlSchema> getSchemas() {

		// create Schema
		CsdlSchema schema = new CsdlSchema();
		schema.setNamespace(NAMESPACE);

		// add EntityTypes
		List<CsdlEntityType> entityTypes = new ArrayList<CsdlEntityType>();
		for (String entityName: entityNames) {
			FullQualifiedName fullQualifiedName = new FullQualifiedName(NAMESPACE, entityName);
			entityTypes.add(getEntityType(fullQualifiedName));
		}
		schema.setEntityTypes(entityTypes);

		// add EntityContainer
		schema.setEntityContainer(getEntityContainer());

		// finally
		List<CsdlSchema> schemas = new ArrayList<CsdlSchema>();
		schemas.add(schema);

		return schemas;
	}

	public CsdlEntityContainerInfo getEntityContainerInfo(FullQualifiedName entityContainerName) {

		// This method is invoked when displaying the Service Document at e.g.
		// http://localhost:8080/DemoService/DemoService.svc
		if (entityContainerName == null || entityContainerName.equals(CONTAINER)) {
			CsdlEntityContainerInfo entityContainerInfo = new CsdlEntityContainerInfo();
			entityContainerInfo.setContainerName(CONTAINER);
			return entityContainerInfo;
		}

		return null;
	}
}
