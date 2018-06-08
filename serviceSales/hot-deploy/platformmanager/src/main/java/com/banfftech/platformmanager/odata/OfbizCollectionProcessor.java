package main.java.com.banfftech.platformmanager.odata;

import org.apache.olingo.commons.api.data.*;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.format.ContentType;
import org.apache.olingo.commons.api.http.HttpHeader;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.*;
import org.apache.olingo.server.api.processor.EntityCollectionProcessor;
import org.apache.olingo.server.api.serializer.EntityCollectionSerializerOptions;
import org.apache.olingo.server.api.serializer.ODataSerializer;
import org.apache.olingo.server.api.serializer.SerializerResult;
import org.apache.olingo.server.api.uri.UriInfo;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourceEntitySet;
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

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class OfbizCollectionProcessor implements EntityCollectionProcessor {

	public static final String module = DemoServlet.class.getName();
	public static final int	MAX_ROWS = 10000;
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
		// 1st we have retrieve the requested EntitySet from the uriInfo object
		// (representation of the parsed service URI)
		List<UriResource> resourcePaths = uriInfo.getUriResourceParts();
		UriResourceEntitySet uriResourceEntitySet = (UriResourceEntitySet) resourcePaths.get(0); // in our example, the
																									// first segment is
																									// the EntitySet
		EdmEntitySet edmEntitySet = uriResourceEntitySet.getEntitySet();

		// 2nd: fetch the data from backend for this requested EntitySetName
		// it has to be delivered as EntitySet object
		EntityCollection entitySet = storage.readEntitySetData(edmEntitySet);
		entitySet = getData(edmEntitySet);

		EntityCollection returnEntityCollection = new EntityCollection();
		List<Entity> entityList = entitySet.getEntities();
		CountOption countOption = uriInfo.getCountOption();
		if (countOption != null) {
			boolean isCount = countOption.getValue();
			if (isCount) {
				returnEntityCollection.setCount(entityList.size());
			}
		}
		FilterOption filterOption = uriInfo.getFilterOption();
		if (filterOption != null) {
			Expression filterExpression = filterOption.getExpression();
			try {
				Iterator<Entity> entityIterator = entityList.iterator();

				// Evaluate the expression for each entity
				// If the expression is evaluated to "true", keep the entity otherwise remove it
				// from
				// the entityList
				while (entityIterator.hasNext()) {
					// To evaluate the the expression, create an instance of the Filter Expression
					// Visitor and pass the current entity to the constructor
					Entity currentEntity = entityIterator.next();
					FilterExpressionVisitor expressionVisitor = new FilterExpressionVisitor(currentEntity);

					// Evaluating the expression
					Object visitorResult = filterExpression.accept(expressionVisitor);
					// The result of the filter expression must be of type Edm.Boolean
					if (visitorResult instanceof Boolean) {
						if (!Boolean.TRUE.equals(visitorResult)) {
							// The expression evaluated to false (or null), so we have to remove the
							// currentEntity from entityList
							entityIterator.remove();
						}
					} else {
						throw new ODataApplicationException("A filter expression must evaulate to type Edm.Boolean",
								HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ENGLISH);
					}
				} // End while
			} catch (ExpressionVisitException e) {
				throw new ODataApplicationException("Exception in filter evaluation",
						HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), Locale.ENGLISH);
			}

		}

		// 3rd: create a serializer based on the requested format (json)
		ODataSerializer serializer = odata.createSerializer(responseFormat);

		// 4th: Now serialize the content: transform from the EntitySet object to
		// InputStream
		EdmEntityType edmEntityType = edmEntitySet.getEntityType();
		ContextURL contextUrl = ContextURL.with().entitySet(edmEntitySet).build();

		final String id = request.getRawBaseUri() + "/" + edmEntitySet.getName();
		EntityCollectionSerializerOptions opts = EntityCollectionSerializerOptions.with().id(id).count(countOption)
				.contextURL(contextUrl).build();
		SerializerResult serializerResult = serializer.entityCollection(serviceMetadata, edmEntityType, entitySet,
				opts);
		InputStream serializedContent = serializerResult.getContent();

		// Finally: configure the response object: set the body, headers and status code
		response.setContent(serializedContent);
		response.setStatusCode(HttpStatusCode.OK.getStatusCode());
		response.setHeader(HttpHeader.CONTENT_TYPE, responseFormat.toContentTypeString());
	}

	/*****************/
	private EntityCollection getData(EdmEntitySet edmEntitySet) {

		EdmEntityType edmEntityType = edmEntitySet.getEntityType();
		String entityName = edmEntityType.getName();
		Debug.logInfo("adding entity property for .... " + entityName, module);
		List<GenericValue> genericValues = null;
		try {
			// genericValues = delegator.findByAnd(entityName, UtilMisc.toMap("partyIdFrom", "HANGZHOU_AGENT"));
			EntityFindOptions efo = new EntityFindOptions();
			efo.setMaxRows(MAX_ROWS);
			List<String> orderBy = UtilMisc.toList("-lastUpdatedStamp");
			if (entityName.equals("OrderHeader")) {
				genericValues = orderHeaderFindList(entityName, orderBy, efo);
			} else {
				genericValues = delegator.findList(entityName, null, null, orderBy, efo, false);
			}
		} catch (GenericEntityException e) {
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
	/**
	 * @throws GenericEntityException **********/

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
