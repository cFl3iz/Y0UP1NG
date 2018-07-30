package main.java.com.banfftech.platformmanager.odata.ofbiz;

import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import org.apache.olingo.commons.api.data.ContextURL;
import org.apache.olingo.commons.api.data.ContextURL.Suffix;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
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
import org.apache.olingo.server.api.serializer.EntitySerializerOptions;
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
import org.apache.olingo.server.api.uri.queryoption.OrderByOption;
import org.apache.olingo.server.api.uri.queryoption.SkipOption;
import org.apache.olingo.server.api.uri.queryoption.TopOption;
import org.apache.olingo.server.api.uri.queryoption.ExpandOption;
import org.apache.olingo.server.api.uri.queryoption.SelectOption;

import org.apache.ofbiz.entity.Delegator;
// import org.apache.ofbiz.widget.tree.ModelTree.ModelNode.Link;

public class OfbizCollectionProcessor implements EntityCollectionProcessor {

	public static final String module = OfbizCollectionProcessor.class.getName();
	public static final int	MAX_ROWS = 10000;
	public static final int DAYS_BEFORE = -100;
	private OData odata;
	private ServiceMetadata serviceMetadata;
	private Delegator delegator;
	private Storage storage;

	public OfbizCollectionProcessor(Delegator delegator) {
		super();
		this.delegator = delegator;
		this.storage = new Storage(delegator);
	}

	@Override
	public void init(OData odata, ServiceMetadata serviceMetadata) {
		this.odata = odata;
		this.serviceMetadata = serviceMetadata;
	}

	@Override
	public void readEntityCollection(ODataRequest request, ODataResponse response, UriInfo uriInfo,
			ContentType responseFormat) throws ODataApplicationException, ODataLibraryException {
		EdmEntitySet responseEdmEntitySet = null; // for building ContextURL
		EntityCollection responseEntityCollection = null; // for the response body
		// 1st we have retrieve the requested EntitySet from the uriInfo object
		// (representation of the parsed service URI)
		List<UriResource> resourceParts = uriInfo.getUriResourceParts();
		int segmentCount = resourceParts.size();
		UriResourceEntitySet uriResourceEntitySet = (UriResourceEntitySet) resourceParts.get(0); // first segment is the EntitySet
		EdmEntitySet startEdmEntitySet = uriResourceEntitySet.getEntitySet();


		/******** $expand option ***********/
		SelectOption selectOption = uriInfo.getSelectOption();
 		ExpandOption expandOption = uriInfo.getExpandOption();
		/******** End $expand option ***********/


		if (segmentCount == 1) {
			responseEdmEntitySet = startEdmEntitySet; // first (and only) entitySet
			// 2nd: fetch the data from backend for this requested EntitySetName
			// it has to be delivered as EntitySet object
			FilterOption filterOption = uriInfo.getFilterOption();
			TopOption topOption = uriInfo.getTopOption();
			OrderByOption orderByOption = uriInfo.getOrderByOption();
			responseEntityCollection = storage.readEntitySetData(startEdmEntitySet, filterOption, topOption, orderByOption,expandOption);
		} else if (segmentCount == 2) {
			UriResource lastSegment = resourceParts.get(1); // don't support more complex URIs
			if(lastSegment instanceof UriResourceNavigation) {
				UriResourceNavigation uriResourceNavigation = (UriResourceNavigation)lastSegment;
				EdmNavigationProperty edmNavigationProperty = uriResourceNavigation.getProperty();
				responseEdmEntitySet = Util.getNavigationTargetEntitySet(startEdmEntitySet, edmNavigationProperty);
				List<UriParameter> keyPredicates = uriResourceEntitySet.getKeyPredicates();
				responseEntityCollection = storage.getRelatedEntityCollection(startEdmEntitySet, keyPredicates, edmNavigationProperty);
			}
		} else{ // this would be the case for e.g. Products(1)/Category/Products
		    throw new ODataApplicationException("Not supported", HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(),Locale.ROOT);
		}
		
		List<Entity> entityList = responseEntityCollection.getEntities();
		CountOption countOption = uriInfo.getCountOption();
		if (countOption != null) {
			boolean isCount = countOption.getValue();
			if (isCount) {
				responseEntityCollection.setCount(entityList.size());
			}
		}
		// handle $skip
		SkipOption skipOption = uriInfo.getSkipOption();
		if (skipOption != null) {
			int skipNumber = skipOption.getValue();
			if (skipNumber >= 0) {
				if (skipNumber <= entityList.size()) {
					entityList = entityList.subList(skipNumber, entityList.size());
				} else {
					// The client skipped all entities
					entityList.clear();
				}
			} else {
				throw new ODataApplicationException("Invalid value for $skip", HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ROOT);
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
				.contextURL(contextUrl).expand(expandOption).build();
		SerializerResult serializerResult = serializer.entityCollection(serviceMetadata, edmEntityType, responseEntityCollection,
				opts);
		InputStream serializedContent = serializerResult.getContent();


		// Finally: configure the response object: set the body, headers and status code
		response.setContent(serializedContent);
		response.setStatusCode(HttpStatusCode.OK.getStatusCode());
		response.setHeader(HttpHeader.CONTENT_TYPE, responseFormat.toContentTypeString());
	}

}
