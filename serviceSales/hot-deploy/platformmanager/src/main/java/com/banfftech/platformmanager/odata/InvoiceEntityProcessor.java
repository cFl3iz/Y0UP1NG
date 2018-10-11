package main.java.com.banfftech.platformmanager.odata;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.olingo.commons.api.Constants;
import org.apache.olingo.commons.api.data.ContextURL;
import org.apache.olingo.commons.api.data.ContextURL.Suffix;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.data.Link;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.edm.EdmNavigationProperty;
import org.apache.olingo.commons.api.format.ContentType;
import org.apache.olingo.commons.api.http.HttpHeader;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.ODataLibraryException;
import org.apache.olingo.server.api.ODataRequest;
import org.apache.olingo.server.api.ODataResponse;
import org.apache.olingo.server.api.ServiceMetadata;
import org.apache.olingo.server.api.processor.EntityProcessor;
import org.apache.olingo.server.api.serializer.EntitySerializerOptions;
import org.apache.olingo.server.api.serializer.ODataSerializer;
import org.apache.olingo.server.api.serializer.SerializerResult;
import org.apache.olingo.server.api.uri.UriInfo;
import org.apache.olingo.server.api.uri.UriParameter;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourceEntitySet;
import org.apache.olingo.server.api.uri.UriResourceNavigation;
import org.apache.olingo.server.api.uri.queryoption.ExpandItem;
import org.apache.olingo.server.api.uri.queryoption.ExpandOption;
import org.apache.olingo.server.api.uri.queryoption.SelectOption;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.model.ModelEntity;
import org.apache.ofbiz.entity.model.ModelField;
import org.apache.ofbiz.entity.model.ModelReader;

public class InvoiceEntityProcessor implements EntityProcessor {

	public static final String module = InvoiceEntityProcessor.class.getName();
	private OData odata;
	private ServiceMetadata serviceMetadata;
	private Delegator delegator;
	private Storage storage;



	public InvoiceEntityProcessor(Storage storage) {
		super();
		this.storage = storage;
		//this.storage = new Storage(delegator);
	}

	public InvoiceEntityProcessor(Storage storage, Delegator delegator) {
		super();
		this.storage = storage;
		this.delegator = delegator;
	}

	public InvoiceEntityProcessor(Delegator delegator) {
		super();
		this.delegator = delegator;
	}

	@Override
	public void init(OData odata, ServiceMetadata serviceMetatdata) {
		this.odata = odata;
		this.serviceMetadata = serviceMetadata;
	}

	@Override
	public void createEntity(ODataRequest arg0, ODataResponse arg1, UriInfo arg2, ContentType arg3, ContentType arg4)
			throws ODataApplicationException, ODataLibraryException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteEntity(ODataRequest arg0, ODataResponse arg1, UriInfo arg2)
			throws ODataApplicationException, ODataLibraryException {
		// TODO Auto-generated method stub

	}

	@Override
	public void readEntity(ODataRequest request, ODataResponse response, UriInfo uriInfo, ContentType responseFormat)
			throws ODataApplicationException, ODataLibraryException {
		Debug.logInfo("-------------------------------------------- entering InvoiceEntityProcessor.readEntity ", module);
	    // 1. retrieve the Entity Type
	    List<UriResource> resourcePaths = uriInfo.getUriResourceParts();
	    // Note: only in our example we can assume that the first segment is the EntitySet
	    UriResourceEntitySet uriResourceEntitySet = (UriResourceEntitySet) resourcePaths.get(0);
	    EdmEntitySet edmEntitySet = uriResourceEntitySet.getEntitySet();

	    // 2. retrieve the data from backend
	    List<UriParameter> keyPredicates = uriResourceEntitySet.getKeyPredicates();
	    // Entity entity = storage.readEntityData(edmEntitySet, keyPredicates);
	    GenericValue genericValue = null;
	    genericValue = getGenericValue(edmEntitySet, keyPredicates);
	    // Entity entity = getData(edmEntitySet, keyPredicates, genericValue);
	    Entity entity = getData(edmEntitySet, genericValue);
		Debug.logInfo("-------------------------------------------- after getData ", module);

		SelectOption selectOption = uriInfo.getSelectOption();
		/******** $expand option ***********/
		EdmNavigationProperty edmNavigationProperty = null;
		ExpandOption expandOption = uriInfo.getExpandOption();
		if (expandOption != null) {
			// List<ExpandItem> expandItems = expandOption.getExpandItems();
			ExpandItem expandItem = expandOption.getExpandItems().get(0);
			Debug.logInfo("-------------------------------------------- entering check expand option ", module);
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
				EdmEntityType expandEdmEntityType = edmNavigationProperty.getType();
				String navPropName = edmNavigationProperty.getName();
				Debug.logInfo("=============== navPropName = " + navPropName, module);
				EntityCollection expandEntityCollection = gerRelatedEntities(genericValue, expandEdmEntityType);

				Debug.logInfo("=============== begin process link 1", module);
				Link link = new Link();
				link.setTitle(navPropName);
				Debug.logInfo("=============== begin process link 2", module);
				link.setType(Constants.ENTITY_NAVIGATION_LINK_TYPE);
				link.setRel(Constants.NS_ASSOCIATION_LINK_REL + navPropName);
				Debug.logInfo("=============== begin process link 3", module);
				link.setInlineEntitySet(expandEntityCollection);
				Debug.logInfo("=============== begin process link 4", module);
				// link.setHref(expandEntityCollection.getId().toASCIIString());
				Debug.logInfo("=============== begin process link 5", module);
				entity.getNavigationLinks().add(link);
			}
		}
		/******** End $expand option ***********/

		Debug.logInfo("-------------------------------------------- 1", module);
		// 3. serialize
	    EdmEntityType entityType = edmEntitySet.getEntityType();
	    String selectList = odata.createUriHelper().buildContextURLSelectList(entityType, expandOption, selectOption);
	    InputStream entityStream = null;
		try {
		    Debug.logInfo("-------------------------------------------- 2", module);
		    ContextURL contextUrl = ContextURL.with().entitySet(edmEntitySet)
		    			.selectList(selectList)
					.suffix(Suffix.ENTITY).build();
		    // expand and select currently not supported
			Debug.logInfo("-------------------------------------------- 3", module);
		    EntitySerializerOptions options = EntitySerializerOptions.with()
		    			.contextURL(contextUrl)
		    			.select(selectOption)
		    			.expand(expandOption)
		    			.build();

			Debug.logInfo("-------------------------------------------- 4", module);
		    ODataSerializer serializer = odata.createSerializer(responseFormat);
		    entityType.getName();
		    entity.getType();
		    options.getContextURL().getKeyPath();
		    // serviceMetadata.toString();
			Debug.logInfo("-------------------------------------------- 5 " + entityType.getName() + ", " + entity.getType(), module);
		    SerializerResult serializerResult = serializer.entity(serviceMetadata, entityType, entity, options);
			Debug.logInfo("-------------------------------------------- 6", module);
		    entityStream = serializerResult.getContent();
		} catch (Exception e) {
			e.printStackTrace();
		}

		Debug.logInfo("-------------------------------------------- 7", module);
	    //4. configure the response object
	    response.setContent(entityStream);
	    response.setStatusCode(HttpStatusCode.OK.getStatusCode());
	    response.setHeader(HttpHeader.CONTENT_TYPE, responseFormat.toContentTypeString());
	}

	private EntityCollection gerRelatedEntities(GenericValue originGenericValue, EdmEntityType expandEdmEntityType) {
		Debug.logInfo("------------------ entering in InvoiceEntityProcessor.gerRelatedEntities", module);
		if (originGenericValue == null) {
			Debug.logInfo("================================= originGenericValue is null ", module);
		}
		String entityName = expandEdmEntityType.getName();
		Debug.logInfo("adding entity property for relation entity .... " + entityName, module);
		List<GenericValue> genericValues = null;
		try {
			genericValues = originGenericValue.getRelated(entityName);
		} catch (GenericEntityException e) {
			e.printStackTrace();
		}
		EntityCollection entityCollection = new EntityCollection();
		// check for which EdmEntitySet the data is requested
		List<Entity> entityList = entityCollection.getEntities();
		for (GenericValue genericValue : genericValues) {
			Debug.logInfo(genericValue.toString(), module);
			Entity e1 = new Entity();
			e1.setType(expandEdmEntityType.getFullQualifiedName().getFullQualifiedNameAsString());
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
			entityList.add(e1);

		}
		return entityCollection;
	}

	@Override
	public void updateEntity(ODataRequest arg0, ODataResponse arg1, UriInfo arg2, ContentType arg3, ContentType arg4)
			throws ODataApplicationException, ODataLibraryException {
		// TODO Auto-generated method stub

	}

	private GenericValue getGenericValue(EdmEntitySet edmEntitySet, List<UriParameter> keyPredicates) {
		GenericValue genericValue = null;
		Map<String, Object> pk = new HashMap<String, Object>();
		for (UriParameter keyPredicate: keyPredicates) {
            String regexp = "\'";
            String keyText = keyPredicate.getText();
            keyText = keyText.replaceAll(regexp, "");
			Debug.logInfo("==================== param name = " + keyPredicate.getName() + ", value = " + keyText, module);
			pk.put(keyPredicate.getName(), keyText);
		}
		String entityName = edmEntitySet.getEntityType().getName();
		Debug.logInfo("==================== begin to get entity from delegator, entityName = " + entityName, module);
		// GenericValue genericValue = delegator.findByPrimaryKey(entityName, pk);
//		try {
//			genericValue = null;
//		} catch (GenericEntityException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return genericValue;
	}

	private Entity getData(EdmEntitySet edmEntitySet, GenericValue genericValue) {
		Debug.logInfo("---------------------- getData, " + edmEntitySet.getEntityType().getFullQualifiedName().getFullQualifiedNameAsString(), module);
		String entityName = genericValue.getEntityName();
		try {
			Entity e1 = new Entity();
			e1.setType(edmEntitySet.getEntityType().getFullQualifiedName().getFullQualifiedNameAsString());
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
				// Debug.logInfo("adding entity property .... " + fieldName, module);
				e1 = e1.addProperty(new Property(null, fieldName, ValueType.PRIMITIVE, genericValue.get(fieldName)));
			}
			e1.setId(Util.createId(entityName + "s", genericValue));
			return e1;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private Entity getData(EdmEntitySet edmEntitySet, List<UriParameter> keyPredicates, GenericValue genericValue) {
		Map<String, Object> pk = new HashMap<String, Object>();
		for (UriParameter keyPredicate: keyPredicates) {
            String regexp = "\'";
            String keyText = keyPredicate.getText();
            keyText = keyText.replaceAll(regexp, "");
			Debug.logInfo("==================== param name = " + keyPredicate.getName() + ", value = " + keyText, module);
			pk.put(keyPredicate.getName(), keyText);
		}
		String entityName = edmEntitySet.getEntityType().getName();
		try {
			Debug.logInfo("==================== begin to get entity from delegator, entityName = " + entityName, module);
			// GenericValue genericValue = delegator.findByPrimaryKey(entityName, pk);
			genericValue = null;
			/************* use genericValue **************/
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
				// Debug.logInfo("adding entity property .... " + fieldName, module);
				e1 = e1.addProperty(new Property(null, fieldName, ValueType.PRIMITIVE, genericValue.get(fieldName)));
			}
			e1.setId(Util.createId(entityName + "s", genericValue));
			return e1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
