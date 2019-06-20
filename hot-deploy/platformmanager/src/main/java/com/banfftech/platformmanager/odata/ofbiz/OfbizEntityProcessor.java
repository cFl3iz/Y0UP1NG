package com.banfftech.platformmanager.odata.ofbiz;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.olingo.commons.api.data.ContextURL;
import org.apache.olingo.commons.api.data.ContextURL.Suffix;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.format.ContentType;
import org.apache.olingo.commons.api.http.HttpHeader;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.ODataLibraryException;
import org.apache.olingo.server.api.ODataRequest;
import org.apache.olingo.server.api.ODataResponse;
import org.apache.olingo.server.api.ServiceMetadata;
import org.apache.olingo.server.api.deserializer.DeserializerResult;
import org.apache.olingo.server.api.deserializer.ODataDeserializer;
import org.apache.olingo.server.api.processor.EntityProcessor;
import org.apache.olingo.server.api.serializer.EntitySerializerOptions;
import org.apache.olingo.server.api.serializer.ODataSerializer;
import org.apache.olingo.server.api.serializer.SerializerException;
import org.apache.olingo.server.api.serializer.SerializerResult;
import org.apache.olingo.server.api.uri.UriInfo;
import org.apache.olingo.server.api.uri.UriParameter;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourceEntitySet;
import org.apache.olingo.server.api.uri.UriResourceFunction;
import org.apache.olingo.server.api.uri.queryoption.ExpandOption;
import org.apache.olingo.server.api.uri.queryoption.SelectOption;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.model.ModelEntity;
import org.apache.ofbiz.entity.model.ModelField;
import org.apache.ofbiz.entity.model.ModelReader;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.entity.util.EntityQuery;

public class OfbizEntityProcessor implements EntityProcessor {

	public static final String module = OfbizEntityProcessor.class.getName();
	private OData odata;
	private ServiceMetadata serviceMetadata;
	private LocalDispatcher dispatcher;
	private Delegator delegator;
	private Storage storage;

	public OfbizEntityProcessor(Delegator delegator, LocalDispatcher dispatcher, Storage storage) {
		super();
		this.delegator = delegator;
		this.dispatcher = dispatcher;
		this.storage = storage;
	}

	@Override
	public void init(OData odata, ServiceMetadata serviceMetatdata) {
		this.odata = odata;
		this.serviceMetadata = serviceMetadata;
	}

	@Override
	public void createEntity(ODataRequest request, ODataResponse response, UriInfo uriInfo, ContentType requestFormat,
			ContentType responseFormat) throws ODataApplicationException, ODataLibraryException {
		// 1. Retrieve the entity type from the URI
		EdmEntitySet edmEntitySet = Util.getEdmEntitySet(uriInfo);
		EdmEntityType edmEntityType = edmEntitySet.getEntityType();

		// 2. create the data in backend
		// 2.1. retrieve the payload from the POST request for the entity to create and
		// deserialize it
		InputStream requestInputStream = request.getBody();
		ODataDeserializer deserializer = this.odata.createDeserializer(requestFormat);
		DeserializerResult result = deserializer.entity(requestInputStream, edmEntityType);
		Entity requestEntity = result.getEntity();
		// 2.2 do the creation in backend, which returns the newly created entity
		Entity createdEntity = storage.createEntityData(edmEntitySet, requestEntity);
		if (createdEntity == null) {
			throw new ODataApplicationException("use creation service instead",
					HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ROOT);
		}

		// 3. serialize the response (we have to return the created entity)
		ContextURL contextUrl = ContextURL.with().entitySet(edmEntitySet).build();
		EntitySerializerOptions options = EntitySerializerOptions.with().contextURL(contextUrl).build(); // expand and
																											// select
																											// currently
																											// not
																											// supported

		ODataSerializer serializer = this.odata.createSerializer(responseFormat);
		SerializerResult serializedResponse = serializer.entity(serviceMetadata, edmEntityType, createdEntity, options);

		// 4. configure the response object
		response.setContent(serializedResponse.getContent());
		response.setStatusCode(HttpStatusCode.CREATED.getStatusCode());
		response.setHeader(HttpHeader.CONTENT_TYPE, responseFormat.toContentTypeString());
	}

	@Override
	public void deleteEntity(ODataRequest arg0, ODataResponse arg1, UriInfo arg2)
			throws ODataApplicationException, ODataLibraryException {
		// TODO Auto-generated method stub

	}

	@Override
	public void readEntity(ODataRequest request, ODataResponse response, UriInfo uriInfo, ContentType responseFormat)
			throws ODataApplicationException, ODataLibraryException {
		// The sample service supports only functions imports and entity sets.
		// We do not care about bound functions and composable functions.

		UriResource uriResource = uriInfo.getUriResourceParts().get(0);

		if (uriResource instanceof UriResourceEntitySet) {
			readEntityInternal(request, response, uriInfo, responseFormat);
		} else if (uriResource instanceof UriResourceFunction) {
			readFunctionImportInternal(request, response, uriInfo, responseFormat);
		} else {
			throw new ODataApplicationException("Only EntitySet is supported",
					HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ENGLISH);
		}
	}

	private void readEntityInternal(ODataRequest request, ODataResponse response, UriInfo uriInfo,
			ContentType responseFormat) throws ODataApplicationException, SerializerException {
		// 1. retrieve the Entity Type
		List<UriResource> resourcePaths = uriInfo.getUriResourceParts();
		// Note: only in our example we can assume that the first segment is the
		// EntitySet
		UriResourceEntitySet uriResourceEntitySet = (UriResourceEntitySet) resourcePaths.get(0);
		EdmEntitySet edmEntitySet = uriResourceEntitySet.getEntitySet();

		// 2. retrieve the data from backend
		List<UriParameter> keyPredicates = uriResourceEntitySet.getKeyPredicates();
		// Entity entity = storage.readEntityData(edmEntitySet, keyPredicates);
		/**/
		GenericValue genericValue = null;
		genericValue = getGenericValue(edmEntitySet, keyPredicates);
		Entity entity = getData(edmEntitySet, genericValue);
		/**/
		// Entity entity = storage.readEntityData(edmEntitySet, keyPredicates);

		SelectOption selectOption = uriInfo.getSelectOption();
		/******** $expand option ***********/
		ExpandOption expandOption = uriInfo.getExpandOption();
		storage.addExpandOption(expandOption, entity, genericValue);
		/******** End $expand option ***********/

		// 3. serialize
		EdmEntityType entityType = edmEntitySet.getEntityType();
		String selectList = odata.createUriHelper().buildContextURLSelectList(entityType, expandOption, selectOption);
		InputStream entityStream = null;
		try {
			ContextURL contextUrl = ContextURL.with().entitySet(edmEntitySet).selectList(selectList)
					.suffix(Suffix.ENTITY).build();
			// expand and select currently not supported
			EntitySerializerOptions options = EntitySerializerOptions.with().contextURL(contextUrl).select(selectOption)
					.expand(expandOption).build();

			ODataSerializer serializer = odata.createSerializer(responseFormat);
			entityType.getName();
			entity.getType();
			options.getContextURL().getKeyPath();
			// serviceMetadata.toString();
			SerializerResult serializerResult = serializer.entity(serviceMetadata, entityType, entity, options);
			entityStream = serializerResult.getContent();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 4. configure the response object
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

	private void readFunctionImportInternal(final ODataRequest request, final ODataResponse response,
			final UriInfo uriInfo, final ContentType responseFormat)
			throws ODataApplicationException, SerializerException {

		// 1st step: Analyze the URI and fetch the entity returned by the function
		// import
		// Function Imports are always the first segment of the resource path
		final UriResource firstSegment = uriInfo.getUriResourceParts().get(0);

		if (!(firstSegment instanceof UriResourceFunction)) {
			throw new ODataApplicationException("Not implemented", HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(),
					Locale.ENGLISH);
		}

		final UriResourceFunction uriResourceFunction = (UriResourceFunction) firstSegment;
		// final Entity entity = storage.readFunctionImportEntity(uriResourceFunction, serviceMetadata);
		Entity entity = null;
		try {
			entity = storage.readFunctionImportEntity(uriResourceFunction);
		} catch (GenericServiceException e) {
			e.printStackTrace();
		}

		if (entity == null) {
			throw new ODataApplicationException("Nothing found.", HttpStatusCode.NOT_FOUND.getStatusCode(),
					Locale.ROOT);
		}

		// 2nd step: Serialize the response entity
		final EdmEntityType edmEntityType = (EdmEntityType) uriResourceFunction.getFunction().getReturnType().getType();
		final ContextURL contextURL = ContextURL.with().type(edmEntityType).build();
		final EntitySerializerOptions opts = EntitySerializerOptions.with().contextURL(contextURL).build();
		final ODataSerializer serializer = odata.createSerializer(responseFormat);
		final SerializerResult serializerResult = serializer.entity(serviceMetadata, edmEntityType, entity, opts);

		// 3rd configure the response object
		response.setContent(serializerResult.getContent());
		response.setStatusCode(HttpStatusCode.OK.getStatusCode());
		response.setHeader(HttpHeader.CONTENT_TYPE, responseFormat.toContentTypeString());
	}

	@Override
	public void updateEntity(ODataRequest arg0, ODataResponse arg1, UriInfo arg2, ContentType arg3, ContentType arg4)
			throws ODataApplicationException, ODataLibraryException {
		// TODO Auto-generated method stub

	}

	private GenericValue getGenericValue(EdmEntitySet edmEntitySet, List<UriParameter> keyPredicates) {
		GenericValue genericValue = null;
		Map<String, Object> pk = new HashMap<String, Object>();
		for (UriParameter keyPredicate : keyPredicates) {
			String regexp = "\'";
			String keyText = keyPredicate.getText();
			keyText = keyText.replaceAll(regexp, "");
			Debug.logInfo("==================== param name = " + keyPredicate.getName() + ", value = " + keyText,
					module);
			pk.put(keyPredicate.getName(), keyText);
		}
		String entityName = edmEntitySet.getEntityType().getName();
		Debug.logInfo("==================== begin to get entity from delegator, entityName = " + entityName, module);
		// GenericValue genericValue = delegator.findByPrimaryKey(entityName, pk);
		try {
			genericValue = EntityQuery.use(delegator).from(entityName).where(pk).queryFirst();
//					delegator.findByPrimaryKey(entityName, pk);
		} catch (GenericEntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return genericValue;
	}

	private Entity getData(EdmEntitySet edmEntitySet, GenericValue genericValue) {
		Debug.logInfo("---------------------- getData, "
				+ edmEntitySet.getEntityType().getFullQualifiedName().getFullQualifiedNameAsString(), module);
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
		for (UriParameter keyPredicate : keyPredicates) {
			String regexp = "\'";
			String keyText = keyPredicate.getText();
			keyText = keyText.replaceAll(regexp, "");
			Debug.logInfo("==================== param name = " + keyPredicate.getName() + ", value = " + keyText,
					module);
			pk.put(keyPredicate.getName(), keyText);
		}
		String entityName = edmEntitySet.getEntityType().getName();
		try {
			Debug.logInfo("==================== begin to get entity from delegator, entityName = " + entityName,
					module);
			// GenericValue genericValue = delegator.findByPrimaryKey(entityName, pk);
			genericValue = EntityQuery.use(delegator).from(entityName).where(pk).queryFirst();
//					delegator.findByPrimaryKey(entityName, pk);
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
