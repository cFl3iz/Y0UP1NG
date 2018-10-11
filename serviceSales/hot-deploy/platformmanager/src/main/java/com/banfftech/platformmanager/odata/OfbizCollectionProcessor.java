package main.java.com.banfftech.platformmanager.odata;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.olingo.commons.api.data.ContextURL;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
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
import org.apache.olingo.server.api.processor.EntityCollectionProcessor;
import org.apache.olingo.server.api.serializer.EntityCollectionSerializerOptions;
import org.apache.olingo.server.api.serializer.ODataSerializer;
import org.apache.olingo.server.api.serializer.SerializerResult;
import org.apache.olingo.server.api.uri.UriInfo;
import org.apache.olingo.server.api.uri.UriParameter;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourceEntitySet;
import org.apache.olingo.server.api.uri.UriResourceNavigation;
import org.apache.olingo.server.api.uri.queryoption.CountOption;
import org.apache.olingo.server.api.uri.queryoption.FilterOption;
import org.apache.olingo.server.api.uri.queryoption.expression.Expression;
import org.apache.olingo.server.api.uri.queryoption.expression.ExpressionVisitException;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilDateTime;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityConditionList;
import org.apache.ofbiz.entity.condition.EntityExpr;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.entity.model.ModelEntity;
import org.apache.ofbiz.entity.model.ModelField;
import org.apache.ofbiz.entity.model.ModelReader;
import org.apache.ofbiz.entity.util.EntityFindOptions;
import org.apache.ofbiz.entity.util.EntityQuery;
// import org.apache.ofbiz.widget.tree.ModelTree.ModelNode.Link;

public class OfbizCollectionProcessor implements EntityCollectionProcessor {

	public static final String module = DemoServlet.class.getName();
	public static final int	MAX_ROWS = 1000;
	public static final int DAYS_BEFORE = -100;
	private OData odata;
	private ServiceMetadata serviceMetadata;
	private Delegator delegator;
	private Storage storage;

	public OfbizCollectionProcessor(Storage storage) {
		super();
		this.storage = storage;
	}

	public OfbizCollectionProcessor(Storage storage, Delegator delegator) {
		super();
		this.storage = storage;
		this.delegator = delegator;
	}

	public OfbizCollectionProcessor(Delegator delegator) {
		super();
		this.delegator = delegator;
	}

	@Override
	public void init(OData odata, ServiceMetadata serviceMetadata) {
		this.odata = odata;
		this.serviceMetadata = serviceMetadata;
	}

	@Override
	public void readEntityCollection(ODataRequest request, ODataResponse response, UriInfo uriInfo,
			ContentType responseFormat) throws ODataApplicationException, ODataLibraryException {
		Debug.logInfo("================================================================ entering OfbizCollectionProcessor.readEntityCollection", module);
		EdmEntitySet responseEdmEntitySet = null; // for building ContextURL
		EntityCollection responseEntityCollection = null; // for the response body
		// 1st we have retrieve the requested EntitySet from the uriInfo object
		// (representation of the parsed service URI)
		List<UriResource> resourceParts = uriInfo.getUriResourceParts();
		int segmentCount = resourceParts.size();
		UriResourceEntitySet uriResourceEntitySet = (UriResourceEntitySet) resourceParts.get(0); // first segment is the EntitySet
		EdmEntitySet startEdmEntitySet = uriResourceEntitySet.getEntitySet();
		// EdmEntitySet edmEntitySet = uriResourceEntitySet.getEntitySet();
		
		// EntityCollection entitySet = null;
		if (segmentCount == 1) {
			responseEdmEntitySet = startEdmEntitySet; // first (and only) entitySet
			// 2nd: fetch the data from backend for this requested EntitySetName
			// it has to be delivered as EntitySet object
			FilterOption filterOption = uriInfo.getFilterOption();
			// EntityCollection entitySet = storage.readEntitySetData(edmEntitySet);
			responseEntityCollection = getData(startEdmEntitySet, filterOption);
		} else if (segmentCount == 2) {
			UriResource lastSegment = resourceParts.get(1); // don't support more complex URIs
			if(lastSegment instanceof UriResourceNavigation) {
				UriResourceNavigation uriResourceNavigation = (UriResourceNavigation)lastSegment;
				EdmNavigationProperty edmNavigationProperty = uriResourceNavigation.getProperty();
				EdmEntityType targetEntityType = edmNavigationProperty.getType();
				responseEdmEntitySet = Util.getNavigationTargetEntitySet(startEdmEntitySet, edmNavigationProperty);
				// responseEdmEntitySet = Util.getNavigationTargetEntitySet(uriInfo);
				// responseEdmEntitySet = Util.getNavigationTargetEntitySet(startEdmEntitySet, uriResourceNavigation);
				List<UriParameter> keyPredicates = uriResourceEntitySet.getKeyPredicates();
				// Entity sourceEntity = readEntityData(startEdmEntitySet, keyPredicates);
				GenericValue genericValue = readEntityData(startEdmEntitySet, keyPredicates);
				if (genericValue == null) {
		            throw new ODataApplicationException("Entity not found.", HttpStatusCode.NOT_FOUND.getStatusCode(), Locale.ROOT);
		        }
				responseEntityCollection = getRelatedEntityCollection(genericValue, edmNavigationProperty);
			}
		} else{ // this would be the case for e.g. Products(1)/Category/Products
		    throw new ODataApplicationException("Not supported", HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(),Locale.ROOT);
		}
		
		

		// EntityCollection returnEntityCollection = new EntityCollection();
		List<Entity> entityList = responseEntityCollection.getEntities();
		CountOption countOption = uriInfo.getCountOption();
		if (countOption != null) {
			boolean isCount = countOption.getValue();
			if (isCount) {
				responseEntityCollection.setCount(entityList.size());
			}
		}
		Debug.logInfo("===================== after 2nd", module);
		// 3rd: create a serializer based on the requested format (json)
		ODataSerializer serializer = odata.createSerializer(responseFormat);

		// 4th: Now serialize the content: transform from the EntitySet object to
		// InputStream
		EdmEntityType edmEntityType = responseEdmEntitySet.getEntityType();
		ContextURL contextUrl = ContextURL.with().entitySet(responseEdmEntitySet).build();

		final String id = request.getRawBaseUri() + "/" + responseEdmEntitySet.getName();
		EntityCollectionSerializerOptions opts = EntityCollectionSerializerOptions.with().id(id).count(countOption)
				.contextURL(contextUrl).build();
		SerializerResult serializerResult = serializer.entityCollection(serviceMetadata, edmEntityType, responseEntityCollection,
				opts);
		InputStream serializedContent = serializerResult.getContent();
		Debug.logInfo("===================== after 4th", module);

		// Finally: configure the response object: set the body, headers and status code
		response.setContent(serializedContent);
		response.setStatusCode(HttpStatusCode.OK.getStatusCode());
		response.setHeader(HttpHeader.CONTENT_TYPE, responseFormat.toContentTypeString());
	}

	/*****************/
	private EntityCollection getData(EdmEntitySet edmEntitySet, FilterOption filterOption) {

		Debug.logInfo("------------------ entering in OfbizCollectionProcessor.getData", module);
		EdmEntityType edmEntityType = edmEntitySet.getEntityType();
		String entityName = edmEntityType.getName();
		// Debug.logInfo("adding entity property for .... " + entityName, module);
		List<GenericValue> genericValues = null;
		try {
			// genericValues = delegator.findByAnd(entityName, UtilMisc.toMap("partyIdFrom", "HANGZHOU_AGENT"));
			EntityFindOptions efo = new EntityFindOptions();
			efo.setMaxRows(MAX_ROWS);
			EntityCondition entityCondition = null;
			if (filterOption != null) {
				Expression filterExpression = filterOption.getExpression();
				OfbizExpressionVisitor expressionVisitor = new OfbizExpressionVisitor();
				entityCondition = (EntityCondition) filterExpression.accept(expressionVisitor);
			}
			List<String> orderBy = UtilMisc.toList("-lastUpdatedStamp");
			genericValues = delegator.findList(entityName, entityCondition, null, orderBy, efo, false);
		} catch (GenericEntityException e) {
			e.printStackTrace();
		} catch (ExpressionVisitException e) {
			e.printStackTrace();
		} catch (ODataApplicationException e) {
			e.printStackTrace();
		}
		EntityCollection entityCollection = new EntityCollection();
		// check for which EdmEntitySet the data is requested
		List<Entity> entityList = entityCollection.getEntities();
		for (GenericValue genericValue : genericValues) {
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
			entityList.add(e1);

		}

		return entityCollection;
	}

	private GenericValue readEntityData(EdmEntitySet edmEntitySet, List<UriParameter> keyPredicates) {

		Debug.logInfo("------------------ entering in OfbizCollectionProcessor.readEntityData", module);
		EdmEntityType edmEntityType = edmEntitySet.getEntityType();
		String entityName = edmEntityType.getName();
		Debug.logInfo("====================== entity property for .... " + entityName, module);
		GenericValue genericValue = null;
		Map<String, Object> pkFields = new HashMap<String, Object>();
        String regexp = "\'";
		for (final UriParameter uriParameter : keyPredicates) {
			String keyName = uriParameter.getName();
			String keyText = uriParameter.getText();
            keyText = keyText.replaceAll(regexp, "");
			Debug.logInfo("====================== entity property for .... " + keyName + ", " + keyText, module);
			pkFields.put(keyName, keyText);
		}
		try {
			genericValue = EntityQuery.use(delegator).from(entityName).where(pkFields).queryFirst();
//					delegator.findByPrimaryKey(entityName, pkFields);
			if (genericValue == null) {
				Debug.logInfo("------------------ didn't find by primarykey", module);
			} else {
				Debug.logInfo("------------------ found by primarykey", module);
			}
			return genericValue;
		} catch (GenericEntityException e) {
			e.printStackTrace();
		}
		return null;
	}

	private EntityCollection getRelatedEntityCollection(GenericValue originGenericValue, EdmNavigationProperty edmNavigationProperty) {

		Debug.logInfo("------------------ entering in OfbizCollectionProcessor.getRelatedEntityCollection", module);
		String relationName = edmNavigationProperty.getName();
		EdmEntityType edmEntityType = edmNavigationProperty.getType();

		String entityName = edmEntityType.getName();
		Debug.logInfo("adding entity property for relation entity .... " + entityName, module);
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
			entityList.add(e1);

		}
		return entityCollection;
	}
	/*** @throws GenericEntityException **********/

	private List<GenericValue> orderHeaderFindList(String entityName, List<String> orderBy, EntityFindOptions efo) throws GenericEntityException {
		List<GenericValue> genericValues = null;
		Timestamp nowTimestamp = UtilDateTime.nowTimestamp();
		Timestamp beginTimestamp = UtilDateTime.addDaysToTimestamp(nowTimestamp, DAYS_BEFORE);
        EntityConditionList<EntityExpr> conditions = EntityCondition.makeCondition(UtilMisc.toList(
        			EntityCondition.makeCondition("lastUpdatedStamp", EntityOperator.GREATER_THAN_EQUAL_TO, beginTimestamp),
                EntityCondition.makeCondition("productStoreId", EntityOperator.EQUALS, "EC10")),
                EntityOperator.AND);
        
		genericValues = delegator.findList(entityName, conditions, null, orderBy, efo, false);
		return genericValues;
	}
}
