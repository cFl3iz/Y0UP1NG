package main.java.com.banfftech.platformmanager.odata;

import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.ex.ODataRuntimeException;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.UriParameter;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Storage {
	public static final String module = Storage.class.getName();

    private List<Entity> invoiceList;

    public Storage(Delegator delegator) {
    		invoiceList = new ArrayList<Entity>();
        initSampleData(delegator);
    }

    /* PUBLIC FACADE */

    public EntityCollection readEntitySetData(EdmEntitySet edmEntitySet)throws ODataApplicationException{

        // actually, this is only required if we have more than one Entity Sets
        if(edmEntitySet.getName().equals(DemoEdmProvider.ES_INVOICES_NAME)){
            return getInvoices();
        }

        return null;
    }

    public Entity readEntityData(EdmEntitySet edmEntitySet, List<UriParameter> keyParams) throws ODataApplicationException{

        EdmEntityType edmEntityType = edmEntitySet.getEntityType();

        // actually, this is only required if we have more than one Entity Type
        if(edmEntityType.getName().equals(DemoEdmProvider.ET_INVOICE_NAME)){
        		Debug.logInfo("--------------- readEntityData", module);
            return getInvoice(edmEntityType, keyParams);
        }

        return null;
    }



    /*  INTERNAL */

    private EntityCollection getInvoices(){
        EntityCollection retEntitySet = new EntityCollection();

        for(Entity productEntity : this.invoiceList){
            retEntitySet.getEntities().add(productEntity);
        }

        return retEntitySet;
    }


    private Entity getInvoice(EdmEntityType edmEntityType, List<UriParameter> keyParams) throws ODataApplicationException{

        // the list of entities at runtime
        EntityCollection entitySet = getInvoices();

        /*  generic approach  to find the requested entity */
        Entity requestedEntity = Util.findEntity(edmEntityType, entitySet, keyParams);

        if(requestedEntity == null){
            // this variable is null if our data doesn't contain an entity for the requested key
            // Throw suitable exception
            throw new ODataApplicationException("Entity for requested key doesn't exist",
                                       HttpStatusCode.NOT_FOUND.getStatusCode(), Locale.ENGLISH);
        }

        return requestedEntity;
     }

     /* HELPER */
     private void initSampleData(Delegator delegator){
 		List<GenericValue> invoices = null;
 		try {
 			invoices = EntityQuery.use(delegator).from("Product").where().queryList();

//                    delegator.findByAnd("Invoice", UtilMisc.toMap("partyIdFrom", "HANGZHOU_AGENT"));
 		} catch (GenericEntityException e) {
 			e.printStackTrace();
 		}
 		// EntityCollection invoiceCollection = new EntityCollection();
 		// check for which EdmEntitySet the data is requested
		// invoiceList = invoiceCollection.getEntities();
		for (GenericValue invoice : invoices) {
			final Entity e1 = new Entity().addProperty(new Property(null, "invoiceId", ValueType.PRIMITIVE, invoice.getString("invoiceId")))
					.addProperty(new Property(null, "partyIdFrom", ValueType.PRIMITIVE, invoice.getString("partyIdFrom")))
					.addProperty(new Property(null, "description", ValueType.PRIMITIVE, invoice.getString("description")));
			e1.setId(createId("Invoices", invoice.getString("invoiceId")));
			invoiceList.add(e1);
			
		}

    }

    private URI createId(String entitySetName, Object id) {
        try {
            return new URI(entitySetName + "('" + String.valueOf(id) + "')");
        } catch (URISyntaxException e) {
            throw new ODataRuntimeException("Unable to create id for entity: " + entitySetName, e);
        }
    }
}
