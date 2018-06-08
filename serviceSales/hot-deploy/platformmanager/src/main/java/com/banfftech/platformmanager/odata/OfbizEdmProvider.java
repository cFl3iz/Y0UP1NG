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

import java.util.*;

public class OfbizEdmProvider extends CsdlAbstractEdmProvider {

	public static final String module = OfbizEdmProvider.class.getName();
	// Service Namespace
	public static final String NAMESPACE = "org.apache.ofbiz.order.order";

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
		FIELDMAP.put("numeric", EdmPrimitiveTypeKind.Int64);
		FIELDMAP.put("floating-point", EdmPrimitiveTypeKind.Double);
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
			packageEntities = reader.getEntitiesByPackage(packageFilterSet, null);
			Set<String> entityNameSet = packageEntities.get(NAMESPACE);
			for (String entityName: entityNameSet) {
				try {
					ModelEntity modelEntity = reader.getModelEntity(entityName);
					if (modelEntity instanceof ModelViewEntity) {
						continue;
					}
					Debug.logInfo("------------------- adding entity -- " + entityName, module);
					entityNames.add(entityName);
				} catch (GenericEntityException e) {
					e.printStackTrace();
				}
			}
		} catch (GenericEntityException e) {
			e.printStackTrace();
		}
	}

	public CsdlEntityType getEntityType(FullQualifiedName entityTypeName) {

		String entityName = entityTypeName.getName(); // Such as Invoice
		Debug.logInfo("entityName = " + entityName, module);
		ModelReader reader = delegator.getModelReader();
		ModelEntity modelEntity;
		try {
			modelEntity = reader.getModelEntity(entityName);
		} catch (GenericEntityException e) {
			e.printStackTrace();
			return null;
		}
		Iterator<ModelField> pksIterator = modelEntity.getPksIterator();
		List<CsdlPropertyRef> propertyRefs = new ArrayList<CsdlPropertyRef>();
		while (pksIterator.hasNext()) {
			ModelField field = pksIterator.next();
			String fieldName = field.getName();
			Debug.logInfo("pk field = " + fieldName, module);
			CsdlPropertyRef propertyRef = new CsdlPropertyRef();
			propertyRef.setName(fieldName);
			propertyRefs.add(propertyRef);
		}
		Iterator<ModelField> fieldIterator = modelEntity.getFieldsIterator();
		List<CsdlProperty> propertyList = new ArrayList<CsdlProperty>();
		while (fieldIterator.hasNext()) {
		    ModelField field = fieldIterator.next();
		    String fieldName = field.getName();
		    String fieldType = field.getType();
		    CsdlProperty csdlProperty = new CsdlProperty().setName(fieldName)
					.setType(FIELDMAP.get(fieldType).getFullQualifiedName());
		    propertyList.add(csdlProperty);
		}
		CsdlEntityType entityType = new CsdlEntityType();
		entityType.setName(entityName);
		entityType.setProperties(propertyList);
		entityType.setKey(propertyRefs);
		
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
		Debug.logInfo("in getEntitySet, entityName = " + entityName, module);
		if (entityContainer.equals(CONTAINER)) {
			CsdlEntitySet entitySet = new CsdlEntitySet();
			entitySet.setName(entitySetName);
			entitySet.setType(new FullQualifiedName(NAMESPACE, entityName));

			return entitySet;
		}

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
