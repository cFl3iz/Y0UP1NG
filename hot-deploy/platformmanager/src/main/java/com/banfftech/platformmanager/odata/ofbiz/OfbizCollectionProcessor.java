package com.banfftech.platformmanager.odata.ofbiz;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

import org.apache.olingo.commons.api.data.ContextURL;
import org.apache.olingo.commons.api.data.ContextURL.Suffix;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.edm.EdmNavigationProperty;
import org.apache.olingo.commons.api.edm.EdmPrimitiveType;
import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
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
import org.apache.olingo.server.api.serializer.PrimitiveSerializerOptions;
import org.apache.olingo.server.api.serializer.SerializerException;
import org.apache.olingo.server.api.serializer.SerializerResult;
import org.apache.olingo.server.api.uri.UriInfo;
import org.apache.olingo.server.api.uri.UriParameter;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourceEntitySet;
import org.apache.olingo.server.api.uri.UriResourceNavigation;
import org.apache.olingo.server.api.uri.queryoption.ApplyItem;
import org.apache.olingo.server.api.uri.queryoption.ApplyOption;
import org.apache.olingo.server.api.uri.queryoption.CountOption;
import org.apache.olingo.server.api.uri.queryoption.ExpandOption;
import org.apache.olingo.server.api.uri.queryoption.FilterOption;
import org.apache.olingo.server.api.uri.queryoption.OrderByOption;
import org.apache.olingo.server.api.uri.queryoption.SelectOption;
import org.apache.olingo.server.api.uri.queryoption.SkipOption;
import org.apache.olingo.server.api.uri.queryoption.TopOption;
import org.apache.olingo.server.api.uri.queryoption.apply.Aggregate;
import org.apache.olingo.server.api.uri.queryoption.apply.AggregateExpression;
import org.apache.olingo.server.api.uri.queryoption.apply.AggregateExpression.StandardMethod;
import org.apache.olingo.server.api.uri.queryoption.expression.Expression;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.entity.Delegator;
// import org.apache.ofbiz.widget.tree.ModelTree.ModelNode.Link;
import org.apache.ofbiz.service.LocalDispatcher;

public class OfbizCollectionProcessor implements EntityCollectionProcessor {

	public static final String module = OfbizCollectionProcessor.class.getName();
	public static final int	MAX_ROWS = 10000;
	public static final int DAYS_BEFORE = -100;
	private OData odata;
	private ServiceMetadata serviceMetadata;
	private Delegator delegator;
	private LocalDispatcher dispatcher;
	private Storage storage;

	public OfbizCollectionProcessor(Delegator delegator, LocalDispatcher dispatcher, Storage storage) {
		super();
		this.delegator = delegator;
		this.dispatcher = dispatcher;
		this.storage = storage;
	}

	@Override
	public void init(OData odata, ServiceMetadata serviceMetadata) {
		Debug.logInfo("+++++++++++++++++++++++++ entering into init()", module);
		this.odata = odata;
		this.serviceMetadata = serviceMetadata;
	}

	@Override
	public void readEntityCollection(ODataRequest request, ODataResponse response, UriInfo uriInfo,
			ContentType responseFormat) throws ODataApplicationException, ODataLibraryException {
		Debug.logInfo("+++++++++++++++++++++++++ entering into readEntityCollection()", module);

		EdmEntitySet responseEdmEntitySet = null; // for building ContextURL
		EntityCollection responseEntityCollection = null; // for the response body

		// 1st we have retrieve the requested EntitySet from the uriInfo object
		// (representation of the parsed service URI)
		List<UriResource> resourceParts = uriInfo.getUriResourceParts();
		int segmentCount = resourceParts.size();
	    if(!(resourceParts.get(0) instanceof UriResourceEntitySet)) {
	    		Debug.logInfo("====================== in readEntityCollection, it's not UriResourceEntitySet", module);
	    		return;
	    }
		UriResourceEntitySet uriResourceEntitySet = (UriResourceEntitySet) resourceParts.get(0); // first segment is the EntitySet
		EdmEntitySet startEdmEntitySet = uriResourceEntitySet.getEntitySet();


		/******** $expand option ***********/
		SelectOption selectOption = uriInfo.getSelectOption();

		ExpandOption expandOption = uriInfo.getExpandOption();
		/******** End $expand option ***********/

		/******** $skip option ***********/
		SkipOption skipOption = uriInfo.getSkipOption();
		/******** End $skip option ***********/

		ApplyOption applyOption = uriInfo.getApplyOption();
		if (applyOption != null) {
			readEntityCollectionWithApply(request, response, uriInfo, startEdmEntitySet, applyOption, responseFormat);
			return;
		}
		if (segmentCount == 1) {
			Debug.logInfo("======== it's one segment", module);
			responseEdmEntitySet = startEdmEntitySet; // first (and only) entitySet
			// 2nd: fetch the data from backend for this requested EntitySetName
			// it has to be delivered as EntitySet object
			FilterOption filterOption = uriInfo.getFilterOption();
			TopOption topOption = uriInfo.getTopOption();
			OrderByOption orderByOption = uriInfo.getOrderByOption();
			// add expandOption
			responseEntityCollection = storage.readEntitySetData(startEdmEntitySet, filterOption,skipOption, topOption, orderByOption,expandOption,selectOption);
		} else if (segmentCount == 2) {
			Debug.logInfo("======== it's two segments", module);
			UriResource lastSegment = resourceParts.get(1); // don't support more complex URIs
			if(lastSegment instanceof UriResourceNavigation) {
				Debug.logInfo("======== last segment is UriResourceNavigation", module);
				UriResourceNavigation uriResourceNavigation = (UriResourceNavigation)lastSegment;
				EdmNavigationProperty edmNavigationProperty = uriResourceNavigation.getProperty();
				responseEdmEntitySet = Util.getNavigationTargetEntitySet(startEdmEntitySet, edmNavigationProperty);
				List<UriParameter> keyPredicates = uriResourceEntitySet.getKeyPredicates();
				// /OrderHeaders('TMALLCO162373')/OrderRole?$filter=roleTypeId eq 'BILL_TO_CUSTOMER'&$skip=0&$top=100
				// responseEntityCollection = storage.getRelatedEntityCollection(startEdmEntitySet, keyPredicates, edmNavigationProperty);
				FilterOption filterOption = uriInfo.getFilterOption();
				TopOption topOption = uriInfo.getTopOption();
				OrderByOption orderByOption = uriInfo.getOrderByOption();
				responseEntityCollection = storage.getRelatedEntityCollection(keyPredicates, edmNavigationProperty, filterOption,skipOption, topOption, orderByOption,expandOption,selectOption);
			}
		} else{ // this would be the case for e.g. Products(1)/Category/Products
		    throw new ODataApplicationException("Not supported", HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(),Locale.ROOT);
		}
		
		List<Entity> entityList = responseEntityCollection.getEntities();
		CountOption countOption = uriInfo.getCountOption();
		boolean isCount = false;
		if (countOption != null) {
			 isCount = countOption.getValue();
			if (isCount) {
				responseEntityCollection.setCount(entityList.size());
			}
		}

		// 3rd: create a serializer based on the requested format (json)
		ODataSerializer serializer = odata.createSerializer(responseFormat);

		// 4th: Now serialize the content: transform from the EntitySet object to
		// InputStream
		EdmEntityType edmEntityType = responseEdmEntitySet.getEntityType();
		String selectList = odata.createUriHelper().buildContextURLSelectList(edmEntityType, expandOption, selectOption);
		ContextURL contextUrl = ContextURL.with().entitySet(responseEdmEntitySet).selectList(selectList).suffix(
				Suffix.ENTITY).build();

		final String id = request.getRawBaseUri() + "/" + responseEdmEntitySet.getName();
		// expand and select currently not supported
		EntityCollectionSerializerOptions opts = EntityCollectionSerializerOptions.with().id(id).count(countOption)
				.contextURL(contextUrl).expand(expandOption).select(selectOption).build();

		SerializerResult serializerResult = serializer.entityCollection(serviceMetadata, edmEntityType, responseEntityCollection,
				opts);
		InputStream serializedContent = serializerResult.getContent();




		// Finally: configure the response object: set the body, headers and status code
		response.setContent(serializedContent);
		response.setStatusCode(HttpStatusCode.OK.getStatusCode());
		response.setHeader(HttpHeader.CONTENT_TYPE, responseFormat.toContentTypeString());
	}

	private void readEntityCollectionWithApply(ODataRequest request, ODataResponse response, UriInfo uriInfo,
			EdmEntitySet edmEntitySet, ApplyOption applyOption, ContentType responseFormat) throws SerializerException {
		Debug.logInfo("============================ get into apply option", module);
		List<ApplyItem> applyItems = applyOption.getApplyItems();
		BigDecimal sumValue = BigDecimal.ZERO; // 这个变量定义不太好，搞得好像进入这里一定是sum似的。不过目前先解决sum吧
		EdmPrimitiveType edmPrimitiveType = null;
		String fieldName = null;
		for (ApplyItem applyItem : applyItems) { // 不清楚什么情况下会有多个ApplyItem，先这样写吧。
			Debug.logInfo("============================ get into apply item " + applyItem.getKind(), module);
			if (applyItem.getKind().equals(ApplyItem.Kind.AGGREGATE)) {
				Aggregate aggregate = (Aggregate) applyItem;
				List<AggregateExpression> aggregateExpressions = aggregate.getExpressions();
				for (AggregateExpression aggregateExpression : aggregateExpressions) {
					String alias = aggregateExpression.getAlias(); // aggregate(grandTotal with sum as total), 就是这个total
					Debug.logInfo("================================= aggregateExpression = " + aggregateExpression.getAlias(), module);
					Debug.logInfo("================================= standardMethod = " + aggregateExpression.getStandardMethod().name(), module);
					StandardMethod standardMethod = aggregateExpression.getStandardMethod(); // aggregate(grandTotal with sum as total), 就是这个sum
					List<UriResource> uriResources = aggregateExpression.getPath();
					for (UriResource uriResource : uriResources) {
						Debug.logInfo("================================= uriResource.getSegmentValue = " + uriResource.getSegmentValue(), module);
						Debug.logInfo("================================= uriResource.getKind = " + uriResource.getKind().name(), module);
					}
					Debug.logInfo("================================= aggregateExpression.getExpression = " + aggregateExpression.getExpression(), module);
					Expression expression = aggregateExpression.getExpression(); // aggregate(grandTotal with sum as total), 就是这个grandTotal
					Debug.logInfo("================================= aggregateExpression.getExpression = " + aggregateExpression.getExpression().getClass().getName(), module);
					// FullQualifiedName fullQualifiedCustomMethod = aggregateExpression.getCustomMethod();
					// Debug.logInfo("================================= fullQualifiedCustomMethod = " + fullQualifiedCustomMethod.getFullQualifiedNameAsString(), module);
					FilterOption filterOption = uriInfo.getFilterOption();
					// add expandOption
					String expressionName = expression.toString(); // 如果是OrderHeader对象的grandTotal，则这里的值会是[grandTotal]
					fieldName = expressionName.substring(1, expressionName.length() - 1);
					sumValue = storage.entitySetDataApplySum(edmEntitySet, filterOption, standardMethod, expression);
					edmPrimitiveType = (EdmPrimitiveType) edmEntitySet.getEntityType().getProperty(fieldName).getType();
				}
			}
		}
		Property property = new Property(null, fieldName, ValueType.PRIMITIVE, sumValue);
		final ContextURL contextURL = ContextURL.with().type(edmPrimitiveType).build();
		final PrimitiveSerializerOptions opts = PrimitiveSerializerOptions.with().contextURL(contextURL).build();
		final ODataSerializer serializer = odata.createSerializer(responseFormat);
		final SerializerResult serializerResult = serializer.primitive(serviceMetadata, edmPrimitiveType, property, opts);

		// 3rd configure the response object
		response.setContent(serializerResult.getContent());
		response.setStatusCode(HttpStatusCode.OK.getStatusCode());
		response.setHeader(HttpHeader.CONTENT_TYPE, responseFormat.toContentTypeString());
	}
}
