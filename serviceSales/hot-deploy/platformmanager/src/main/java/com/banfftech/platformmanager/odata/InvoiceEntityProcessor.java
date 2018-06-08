package main.java.com.banfftech.platformmanager.odata;

import org.apache.olingo.commons.api.data.ContextURL;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.format.ContentType;
import org.apache.olingo.commons.api.http.HttpHeader;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.*;
import org.apache.olingo.server.api.processor.EntityProcessor;
import org.apache.olingo.server.api.serializer.EntitySerializerOptions;
import org.apache.olingo.server.api.serializer.ODataSerializer;
import org.apache.olingo.server.api.serializer.SerializerResult;
import org.apache.olingo.server.api.uri.UriInfo;
import org.apache.olingo.server.api.uri.UriParameter;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourceEntitySet;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.model.ModelEntity;
import org.apache.ofbiz.entity.model.ModelField;
import org.apache.ofbiz.entity.model.ModelReader;

import org.apache.ofbiz.entity.util.EntityQuery;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
	    // 1. retrieve the Entity Type
	    List<UriResource> resourcePaths = uriInfo.getUriResourceParts();
	    // Note: only in our example we can assume that the first segment is the EntitySet
	    UriResourceEntitySet uriResourceEntitySet = (UriResourceEntitySet) resourcePaths.get(0);
	    EdmEntitySet edmEntitySet = uriResourceEntitySet.getEntitySet();

	    // 2. retrieve the data from backend
	    List<UriParameter> keyPredicates = uriResourceEntitySet.getKeyPredicates();
	    // Entity entity = storage.readEntityData(edmEntitySet, keyPredicates);
	    Entity entity = getData(edmEntitySet, keyPredicates);

	    // 3. serialize
	    EdmEntityType entityType = edmEntitySet.getEntityType();

	    ContextURL contextUrl = ContextURL.with().entitySet(edmEntitySet).build();
	    // expand and select currently not supported
	    EntitySerializerOptions options = EntitySerializerOptions.with().contextURL(contextUrl).build();

	    ODataSerializer serializer = odata.createSerializer(responseFormat);
	    SerializerResult serializerResult = serializer.entity(serviceMetadata, entityType, entity, options);
	    InputStream entityStream = serializerResult.getContent();

	    //4. configure the response object
	    response.setContent(entityStream);
	    response.setStatusCode(HttpStatusCode.OK.getStatusCode());
	    response.setHeader(HttpHeader.CONTENT_TYPE, responseFormat.toContentTypeString());
	}

	@Override
	public void updateEntity(ODataRequest arg0, ODataResponse arg1, UriInfo arg2, ContentType arg3, ContentType arg4)
			throws ODataApplicationException, ODataLibraryException {
		// TODO Auto-generated method stub

	}
	
	private Entity getData(EdmEntitySet edmEntitySet, List<UriParameter> keyPredicates) {
//		Map<String, Object> pk = new HashMap<String, Object>();
//		for (UriParameter keyPredicate: keyPredicates) {
//            String regexp = "\'";
//            String keyText = keyPredicate.getText();
//            keyText = keyText.replaceAll(regexp, "");
//			Debug.logInfo("==================== param name = " + keyPredicate.getName() + ", value = " + keyText, module);
//			pk.put(keyPredicate.getName(), keyText);
//		}
//		String entityName = edmEntitySet.getEntityType().getName();
//		try {
//			Debug.logInfo("==================== begin to get entity from delegator, entityName = " + entityName, module);
//			GenericValue genericValue = delegator.findByPrimaryKey(entityName, pk);
//			/************* use genericValue **************/
//			Entity e1 = new Entity();
//			ModelReader reader = delegator.getModelReader();
//			ModelEntity modelEntity;
//			try {
//				modelEntity = reader.getModelEntity(entityName);
//			} catch (GenericEntityException e) {
//				e.printStackTrace();
//				return null;
//			}
//			Iterator<ModelField> fieldIterator = modelEntity.getFieldsIterator();
//			while (fieldIterator.hasNext()) {
//				ModelField field = fieldIterator.next();
//				String fieldName = field.getName();
//				String fieldType = field.getType();
//				// Debug.logInfo("adding entity property .... " + fieldName, module);
//				e1 = e1.addProperty(new Property(null, fieldName, ValueType.PRIMITIVE, genericValue.get(fieldName)));
//			}
//			e1.setId(Util.createId(entityName + "s", genericValue));
//			return e1;
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		}
		return null;
	}

}
