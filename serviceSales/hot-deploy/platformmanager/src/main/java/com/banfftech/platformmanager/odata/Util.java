package main.java.com.banfftech.platformmanager.odata;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.edm.EdmBindingTarget;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.edm.EdmNavigationProperty;
import org.apache.olingo.commons.api.edm.EdmPrimitiveType;
import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeException;
import org.apache.olingo.commons.api.edm.EdmProperty;
import org.apache.olingo.commons.api.edm.EdmSingleton;
import org.apache.olingo.commons.api.edm.EdmType;
import org.apache.olingo.commons.api.ex.ODataRuntimeException;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.UriInfoResource;
import org.apache.olingo.server.api.uri.UriParameter;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourceEntitySet;
import org.apache.olingo.server.api.uri.UriResourceNavigation;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilDateTime;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.model.ModelEntity;

public class Util {

	public static final String module = Util.class.getName();
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public static EdmEntitySet getEdmEntitySet(UriInfoResource uriInfo) throws ODataApplicationException {

        List<UriResource> resourcePaths = uriInfo.getUriResourceParts();
         // To get the entity set we have to interpret all URI segments
        if (!(resourcePaths.get(0) instanceof UriResourceEntitySet)) {
            throw new ODataApplicationException("Invalid resource type for first segment.",
                                    HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(),Locale.ENGLISH);
        }

        UriResourceEntitySet uriResource = (UriResourceEntitySet) resourcePaths.get(0);

        return uriResource.getEntitySet();
    }

    public static Entity findEntity(EdmEntityType edmEntityType,
                                    EntityCollection rt_entitySet, List<UriParameter> keyParams)
                                    throws ODataApplicationException {

        List<Entity> entityList = rt_entitySet.getEntities();

        // loop over all entities in order to find that one that matches all keys in request
        // an example could be e.g. contacts(ContactID=1, CompanyID=1)
        for(Entity rt_entity : entityList){
            boolean foundEntity = entityMatchesAllKeys(edmEntityType, rt_entity, keyParams);
            if(foundEntity){
                return rt_entity;
            }
        }

        return null;
    }


    public static boolean entityMatchesAllKeys(EdmEntityType edmEntityType, Entity rt_entity,  List<UriParameter> keyParams)
                                                throws ODataApplicationException {

        // loop over all keys
        for (final UriParameter key : keyParams) {
            // key
            String keyName = key.getName();
            String keyText = key.getText();
            String regexp = "\'";
            keyText = keyText.replaceAll(regexp, "");
            Debug.logInfo("keyName = " + keyName + ", keyText = " + keyText, module);

            // Edm: we need this info for the comparison below
            EdmProperty edmKeyProperty = (EdmProperty )edmEntityType.getProperty(keyName);
            Boolean isNullable = edmKeyProperty.isNullable();
            Integer maxLength = edmKeyProperty.getMaxLength();
            Integer precision = edmKeyProperty.getPrecision();
            Boolean isUnicode = edmKeyProperty.isUnicode();
            Integer scale = edmKeyProperty.getScale();
            // get the EdmType in order to compare
            EdmType edmType = edmKeyProperty.getType();
            // Key properties must be instance of primitive type
            EdmPrimitiveType edmPrimitiveType = (EdmPrimitiveType)edmType;

            // Runtime data: the value of the current entity
            Object valueObject = rt_entity.getProperty(keyName).getValue(); // null-check is done in FWK

            // now need to compare the valueObject with the keyText String
            // this is done using the type.valueToString //
            String valueAsString = null;
            try {
                valueAsString = edmPrimitiveType.valueToString(valueObject, isNullable, maxLength,
                                                                precision, scale, isUnicode);
            } catch (EdmPrimitiveTypeException e) {
                throw new ODataApplicationException("Failed to retrieve String value",
                                             HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(),Locale.ENGLISH, e);
            }

            if (valueAsString == null){
                return false;
            }

            boolean matches = valueAsString.equals(keyText);
            if(!matches){
                // if any of the key properties is not found in the entity, we don't need to search further
                Debug.logInfo("valueAsString = " + valueAsString + ", keyText = " + keyText, module);
                return false;
            }
        }

        return true;
    }

	public static URI createId(String entitySetName, GenericValue genericValue) {
		String entityId;
		ModelEntity modelEntity = genericValue.getModelEntity();
		List<String> pkNames = modelEntity.getPkFieldNames();
		if (pkNames.size() == 1) {
			entityId = "'" + genericValue.getString(pkNames.get(0)) + "'";
		} else {
			StringBuffer sb = new StringBuffer();
			int i = 0;
			for (String pkName: pkNames) {
				String value = genericValue.getString(pkName);
				sb.append(pkName).append("='").append(value).append("'");
				i++;
				if (pkNames.size() > i) {
					sb.append(",");
				}
			}
			entityId = sb.toString();
		}
		String uriEntity = entitySetName + "(" + entityId + ")";
		try {
			return new URI(uriEntity);
		} catch (URISyntaxException e) {
			throw new ODataRuntimeException("Unable to create id for entity: " + entitySetName, e);
		}
	}
	
	public static Timestamp dateStringToTimestamp(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        try {
			Date parsedDate = sdf.parse(dateString);
			return UtilDateTime.toTimestamp(parsedDate);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}

	}
	
	public static EdmEntitySet getNavigationTargetEntitySet(final UriInfoResource uriInfo) throws ODataApplicationException {

	    EdmEntitySet entitySet;
	    final List<UriResource> resourcePaths = uriInfo.getUriResourceParts();

	    // First must be entity set (hence function imports are not supported here).
	    if (resourcePaths.get(0) instanceof UriResourceEntitySet) {
	        entitySet = ((UriResourceEntitySet) resourcePaths.get(0)).getEntitySet();
	    } else {
	        throw new ODataApplicationException("Invalid resource type.",
	                HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ROOT);
	    }

	    int navigationCount = 0;
	    while (entitySet != null
	        && ++navigationCount < resourcePaths.size()
	        && resourcePaths.get(navigationCount) instanceof UriResourceNavigation) {
	        final UriResourceNavigation uriResourceNavigation = (UriResourceNavigation) resourcePaths.get(navigationCount);
	        final EdmBindingTarget target = entitySet.getRelatedBindingTarget(uriResourceNavigation.getProperty().getName());
	        if (target instanceof EdmEntitySet) {
	            entitySet = (EdmEntitySet) target;
	        } else {
	            throw new ODataApplicationException("Singletons not supported", HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(),
	                                                 Locale.ROOT);
	        }
	    }

	    return entitySet;
	}

	public static EdmEntitySet getNavigationTargetEntitySet(EdmEntitySet startEdmEntitySet, EdmNavigationProperty edmNavigationProperty) throws ODataApplicationException {

		String navPropName = edmNavigationProperty.getName();
		Debug.logInfo("================== navPropName = " + navPropName, module);
		EdmBindingTarget edmBindingTarget = startEdmEntitySet.getRelatedBindingTarget(navPropName);
		if (edmBindingTarget == null) {
			Debug.logInfo("================== target is null", module);
		}
		if(edmBindingTarget instanceof EdmEntitySet){
			Debug.logInfo("================== target is " + edmBindingTarget.getName(), module);
			return (EdmEntitySet) edmBindingTarget;
		}
		throw new ODataApplicationException("Singletons not supported", HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(),
                Locale.ROOT);
	}
}
