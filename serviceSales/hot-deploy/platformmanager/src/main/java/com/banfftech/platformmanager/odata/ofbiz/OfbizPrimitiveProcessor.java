package main.java.com.banfftech.platformmanager.odata.ofbiz;

import org.apache.olingo.commons.api.data.ContextURL;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.edm.EdmPrimitiveType;
import org.apache.olingo.commons.api.format.ContentType;
import org.apache.olingo.commons.api.http.HttpHeader;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.*;
import org.apache.olingo.server.api.processor.PrimitiveProcessor;
import org.apache.olingo.server.api.serializer.ODataSerializer;
import org.apache.olingo.server.api.serializer.PrimitiveSerializerOptions;
import org.apache.olingo.server.api.serializer.SerializerException;
import org.apache.olingo.server.api.serializer.SerializerResult;
import org.apache.olingo.server.api.uri.UriInfo;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourceFunction;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;

import java.util.Locale;

public class OfbizPrimitiveProcessor implements PrimitiveProcessor {

	public static final String module = OfbizPrimitiveProcessor.class.getName();
	private OData odata;
	private ServiceMetadata serviceMetadata;
	private LocalDispatcher dispatcher;
	private Delegator delegator;
	private Storage storage;

	public OfbizPrimitiveProcessor(Delegator delegator, LocalDispatcher dispatcher) {
		super();
		this.delegator = delegator;
		this.storage = new Storage(delegator, dispatcher);
	}

	@Override
	public void init(OData odata, ServiceMetadata serviceMetatdata) {
		this.odata = odata;
		this.serviceMetadata = serviceMetadata;
	}

	@Override
	public void deletePrimitive(ODataRequest arg0, ODataResponse arg1, UriInfo arg2)
			throws ODataApplicationException, ODataLibraryException {
		throw new ODataApplicationException("Not supported.", HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ROOT);
	}

	@Override
	public void readPrimitive(ODataRequest request, ODataResponse response, UriInfo uriInfo, ContentType responseFormat)
			throws ODataApplicationException, ODataLibraryException {
		Debug.logInfo("------------------------------------------------------------ in readPrimitive", module);
		UriResource uriResource = uriInfo.getUriResourceParts().get(0);

		if (uriResource instanceof UriResourceFunction) {
			readFunctionImportInternal(request, response, uriInfo, responseFormat);
		} else {
			throw new ODataApplicationException("Only EntitySet is supported",
					HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ENGLISH);
		}
	}

	@Override
	public void updatePrimitive(ODataRequest arg0, ODataResponse arg1, UriInfo arg2, ContentType arg3, ContentType arg4)
			throws ODataApplicationException, ODataLibraryException {
		throw new ODataApplicationException("Not supported.", HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ROOT);
	}

	private void readFunctionImportInternal(final ODataRequest request, final ODataResponse response,
			final UriInfo uriInfo, final ContentType responseFormat)
			throws ODataApplicationException, SerializerException {
		Debug.logInfo("------------------------------------------------------------ in readFunctionImportInternal", module);

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
		Property property = null;
		try {
			property = storage.readFunctionImportPrimitive(uriResourceFunction);
		} catch (GenericServiceException e) {
			e.printStackTrace();
		} catch (GenericEntityException e) {
			e.printStackTrace();
		}

		if (property == null) {
			throw new ODataApplicationException("Nothing found.", HttpStatusCode.NOT_FOUND.getStatusCode(),
					Locale.ROOT);
		}

		// 2nd step: Serialize the response entity
		final EdmPrimitiveType edmPrimitiveType = (EdmPrimitiveType) uriResourceFunction.getFunction().getReturnType().getType();
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
