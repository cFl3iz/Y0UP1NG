package main.java.com.banfftech.platformmanager.odata;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.java.com.banfftech.platformmanager.odata.ofbiz.OfbizActionProcessor;
import main.java.com.banfftech.platformmanager.odata.ofbiz.OfbizPrimitiveProcessor;
import org.apache.olingo.commons.api.edmx.EdmxReference;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataHttpHandler;
import org.apache.olingo.server.api.ServiceMetadata;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.service.LocalDispatcher;

import main.java.com.banfftech.platformmanager.odata.ofbiz.OfbizEntityProcessor;
import main.java.com.banfftech.platformmanager.odata.ofbiz.OfbizCollectionProcessor;

public class DemoServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	public static final String module = DemoServlet.class.getName();

	protected void service(final HttpServletRequest req, final HttpServletResponse resp)
			throws ServletException, IOException {
		Delegator delegator = (Delegator) getServletContext().getAttribute("delegator");
		LocalDispatcher dispatcher = (LocalDispatcher) getServletContext().getAttribute("dispatcher");

		main.java.com.banfftech.platformmanager.odata.ofbiz.Storage storage = new main.java.com.banfftech.platformmanager.odata.ofbiz.Storage(delegator, dispatcher);
		// create odata handler and configure it with CsdlEdmProvider and Processor
		OData odata = OData.newInstance();
		// ServiceMetadata edm = odata.createServiceMetadata(new DemoEdmProvider(), new ArrayList<EdmxReference>());
		ServiceMetadata edm = odata.createServiceMetadata(new OfbizEdmProvider(delegator, dispatcher), new ArrayList<EdmxReference>());
		ODataHttpHandler handler = odata.createHandler(edm);
		handler.register(new OfbizCollectionProcessor(delegator, dispatcher, storage));
		handler.register(new OfbizEntityProcessor(delegator, dispatcher, storage));
		handler.register(new OfbizActionProcessor(delegator, dispatcher, storage));
		handler.register(new DemoBatchProcessor());
		handler.register(new OfbizPrimitiveProcessor(delegator, dispatcher, storage));

		// let the handler do the work
		handler.process(req, resp);
	}
}
