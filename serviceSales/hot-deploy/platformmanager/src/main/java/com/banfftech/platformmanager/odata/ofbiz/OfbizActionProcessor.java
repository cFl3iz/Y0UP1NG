package main.java.com.banfftech.platformmanager.odata.ofbiz;

import org.apache.olingo.commons.api.data.Parameter;
import org.apache.olingo.commons.api.edm.EdmAction;
import org.apache.olingo.commons.api.format.ContentType;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.*;
import org.apache.olingo.server.api.deserializer.ODataDeserializer;
import org.apache.olingo.server.api.processor.ActionVoidProcessor;
import org.apache.olingo.server.api.uri.UriInfo;
import org.apache.olingo.server.api.uri.UriResourceAction;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;

import java.util.Locale;
import java.util.Map;

public class OfbizActionProcessor implements ActionVoidProcessor {

	public static final String module = OfbizActionProcessor.class.getName();
	private OData odata;
	private ServiceMetadata serviceMetadata;
	private LocalDispatcher dispatcher;
	private Delegator delegator;
	private Storage storage;

	public OfbizActionProcessor(Delegator delegator, LocalDispatcher dispatcher, Storage storage) {
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
	public void processActionVoid(ODataRequest request, ODataResponse response, UriInfo uriInfo, ContentType requestFormat)
			throws ODataApplicationException, ODataLibraryException {
		Debug.logInfo("------------------------------------------------------------ in readFunctionImportInternal", module);

	    // 1st Get the action from the resource path
	    final EdmAction edmAction = ((UriResourceAction) uriInfo.asUriInfoResource().getUriResourceParts()
	                                                                                .get(0)).getAction();
	    final UriResourceAction uriResourceAction = (UriResourceAction) uriInfo.asUriInfoResource().getUriResourceParts().get(0);

	    // 2nd Deserialize the parameter
	    // In our case there is only one action. So we can be sure that parameter "Amount" has been provided by the client
	    if (requestFormat == null) {
	      throw new ODataApplicationException("The content type has not been set in the request.",
	          HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ROOT);
	    }
	    
	    final ODataDeserializer deserializer = odata.createDeserializer(requestFormat);
	    final Map<String, Parameter> actionParameters = deserializer.actionParameters(request.getBody(), edmAction)
	        .getActionParameters();
	    Debug.logInfo("-------------------------- edmAction.getName() = " + edmAction.getName(), module);
	    // final Parameter parameterAmount = actionParameter.get(DemoEdmProvider.PARAMETER_AMOUNT);

		try {
			storage.processActionVoid(uriResourceAction, actionParameters);
		} catch (GenericServiceException e) {
			e.printStackTrace();
		} catch (GenericEntityException e) {
			e.printStackTrace();
		}

	    response.setStatusCode(HttpStatusCode.NO_CONTENT.getStatusCode());
	}

}
