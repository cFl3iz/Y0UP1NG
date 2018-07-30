package main.java.com.banfftech.platformmanager.odata.ofbiz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.olingo.commons.api.Constants;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.data.Link;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.edm.EdmNavigationProperty;
import org.apache.olingo.commons.api.edm.EdmProperty;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.UriInfoResource;
import org.apache.olingo.server.api.uri.UriParameter;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourceNavigation;
import org.apache.olingo.server.api.uri.UriResourcePrimitiveProperty;
import org.apache.olingo.server.api.uri.queryoption.ExpandItem;
import org.apache.olingo.server.api.uri.queryoption.ExpandOption;
import org.apache.olingo.server.api.uri.queryoption.FilterOption;
import org.apache.olingo.server.api.uri.queryoption.OrderByItem;
import org.apache.olingo.server.api.uri.queryoption.OrderByOption;
import org.apache.olingo.server.api.uri.queryoption.TopOption;
import org.apache.olingo.server.api.uri.queryoption.expression.Expression;
import org.apache.olingo.server.api.uri.queryoption.expression.ExpressionVisitException;
import org.apache.olingo.server.api.uri.queryoption.expression.Member;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.model.ModelEntity;
import org.apache.ofbiz.entity.model.ModelField;
import org.apache.ofbiz.entity.model.ModelReader;
import org.apache.ofbiz.entity.util.EntityFindOptions;

public class Storage {
	public static final String module = Storage.class.getName();
	public static final int	MAX_ROWS = 10000;
	public static final int DAYS_BEFORE = -100;

    private Delegator delegator;

    public Storage(Delegator delegator) {
    		this.delegator = delegator;
    }

    /* PUBLIC FACADE */

	public EntityCollection readEntitySetData(EdmEntitySet edmEntitySet, FilterOption filterOption, TopOption topOption, OrderByOption orderByOption,ExpandOption expandOption) {

		EdmEntityType edmEntityType = edmEntitySet.getEntityType();
		String entityName = edmEntityType.getName();
		List<GenericValue> genericValues = null;
		try {
			EntityFindOptions efo = new EntityFindOptions();
			if (topOption != null) {
				int topValue = topOption.getValue();
				efo.setMaxRows(topValue);
			} else {
				efo.setMaxRows(MAX_ROWS);
			}
			EntityCondition entityCondition = null;
			if (filterOption != null) {
				Expression filterExpression = filterOption.getExpression();
				OfbizExpressionVisitor expressionVisitor = new OfbizExpressionVisitor();
				entityCondition = (EntityCondition) filterExpression.accept(expressionVisitor);
			}
			// List<String> orderBy = UtilMisc.toList("-lastUpdatedStamp");
			List<String> orderBy = new ArrayList<String>();
			if (orderByOption != null) {
				List<OrderByItem> orderItemList = orderByOption.getOrders();
				for (OrderByItem orderByItem : orderItemList) {
					Expression expression = orderByItem.getExpression();
					if (expression instanceof Member) {
						UriInfoResource resourcePath = ((Member) expression).getResourcePath();
						UriResource uriResource = resourcePath.getUriResourceParts().get(0);
						if (uriResource instanceof UriResourcePrimitiveProperty) {
							EdmProperty edmProperty = ((UriResourcePrimitiveProperty) uriResource).getProperty();
							final String sortPropertyName = edmProperty.getName();
							if (orderByItem.isDescending()) {
								orderBy.add("-" + sortPropertyName);
							} else {
								orderBy.add(sortPropertyName);
							}
						}
					}
				}
			}
			genericValues = delegator.findList(entityName, entityCondition, null, orderBy, efo, false);
		} catch (GenericEntityException e) {
			e.printStackTrace();
		} catch (ExpressionVisitException e) {
			e.printStackTrace();
		} catch (ODataApplicationException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		EntityCollection entityCollection = new EntityCollection();
		List<Entity> entityList = entityCollection.getEntities();
		for (GenericValue genericValue : genericValues) {
			Entity rowEntity = genericValueToEntity(edmEntityType, genericValue);
			Debug.logInfo("before rowEntity:"+rowEntity,module);
			addExpandOption(expandOption, rowEntity, genericValue);
			entityList.add(rowEntity);
			Debug.logInfo("after rowEntity:"+rowEntity,module);
		}

		return entityCollection;
	}

    public Entity readEntityData(EdmEntitySet edmEntitySet, List<UriParameter> keyParams) throws ODataApplicationException{

        EdmEntityType edmEntityType = edmEntitySet.getEntityType();
        GenericValue genericValue = getGenericValue(edmEntitySet, keyParams);
        return genericValueToEntity(edmEntityType, genericValue);

    }

	public EntityCollection getRelatedEntityCollection(EdmEntitySet edmEntitySet, List<UriParameter> keyParams, EdmNavigationProperty edmNavigationProperty) {
		GenericValue originGenericValue = getGenericValue(edmEntitySet, keyParams);
		String relationName = edmNavigationProperty.getName();
		EdmEntityType edmEntityType = edmNavigationProperty.getType();

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
			entityList.add(genericValueToEntity(edmEntityType, genericValue));
		}
		return entityCollection;
	}
	
	private Entity genericValueToEntity(EdmEntityType edmEntityType, GenericValue genericValue) {
		String entityName = genericValue.getEntityName();
		try {
			Entity e1 = new Entity();
			e1.setType(edmEntityType.getFullQualifiedName().getFullQualifiedNameAsString());
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
				e1 = e1.addProperty(new Property(null, fieldName, ValueType.PRIMITIVE, genericValue.get(fieldName)));
			}
			e1.setId(Util.createId(entityName + "s", genericValue));
			return e1;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private GenericValue getGenericValue(EdmEntitySet edmEntitySet, List<UriParameter> keyPredicates) {
		GenericValue genericValue = null;
		Map<String, Object> pk = new HashMap<String, Object>();
		for (UriParameter keyPredicate: keyPredicates) {
            String regexp = "\'";
            String keyText = keyPredicate.getText();
            keyText = keyText.replaceAll(regexp, "");
			pk.put(keyPredicate.getName(), keyText);
		}
		String entityName = edmEntitySet.getEntityType().getName();
		try {
			genericValue = delegator.findOne(entityName, pk,false);
		} catch (GenericEntityException e) {
			e.printStackTrace();
		}
		return genericValue;
	}

	private Map<Entity, GenericValue> getRelatedEntityMaps(GenericValue originGenericValue, EdmNavigationProperty edmNavigationProperty, boolean isCollection) {
		Map<Entity, GenericValue> resultMap = new HashMap<Entity, GenericValue>();
		Debug.logInfo("------------------ entering in OfbizCollectionProcessor.getRelatedEntityCollection", module);
		String relationName = edmNavigationProperty.getName();
		EdmEntityType edmEntityType = edmNavigationProperty.getType();

		String entityName = edmEntityType.getName();
		Debug.logInfo("adding entity property for relation entity .... " + entityName, module);
		List<GenericValue> genericValues = null;
		try {
			if (isCollection) {
				genericValues = originGenericValue.getRelated(relationName);
			} else {
				GenericValue genericValue = originGenericValue.getRelatedOne(relationName);
				genericValues = new ArrayList<GenericValue>();
				genericValues.add(genericValue);
			}
		} catch (GenericEntityException e) {
			e.printStackTrace();
		}
		// EntityCollection entityCollection = new EntityCollection();
		// check for which EdmEntitySet the data is requested
		// List<Entity> entityList = entityCollection.getEntities();
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
			// entityList.add(e1);
			resultMap.put(e1, genericValue);
		}
		
		return resultMap;
	}

	private EntityCollection getRelatedEntityCollection(GenericValue originGenericValue, EdmNavigationProperty edmNavigationProperty) {

		Map<Entity, GenericValue> relatedMaps = this.getRelatedEntityMaps(originGenericValue, edmNavigationProperty, true);
		EntityCollection entityCollection = new EntityCollection();
		// check for which EdmEntitySet the data is requested
		List<Entity> entityList = entityCollection.getEntities();
		Set<Entity> entitySet = relatedMaps.keySet();
		entityList.addAll(entitySet);
		return entityCollection;
	}

	public void addExpandOption(ExpandOption expandOption, Entity entity, GenericValue genericValue) {
		EdmNavigationProperty edmNavigationProperty = null;
		if (expandOption != null) {
			List<ExpandItem> expandItems = expandOption.getExpandItems();
			for (ExpandItem expandItem : expandItems) {
				// ExpandItem expandItem = expandOption.getExpandItems().get(0);
				if (expandItem.isStar()) { // will expand all the navigation items
					// TODO
				} else {
					UriResource uriResource = expandItem.getResourcePath().getUriResourceParts().get(0);
					Debug.logInfo("===============" + uriResource.getSegmentValue(), module);
					if(uriResource instanceof UriResourceNavigation) {
					    edmNavigationProperty = ((UriResourceNavigation) uriResource).getProperty();
						Debug.logInfo("=============== edmNavigationProperty = " + edmNavigationProperty.getName(), module);
					}
				}
				if (edmNavigationProperty != null) {
					String navPropName = edmNavigationProperty.getName();
					Debug.logInfo("=============== navPropName = " + navPropName, module);
					if (edmNavigationProperty.isCollection()) {
						// EntityCollection expandEntityCollection = getRelatedEntityCollection(genericValue, edmNavigationProperty);
						Map<Entity, GenericValue> expandEntityMaps = getRelatedEntityMaps(genericValue, edmNavigationProperty, true);
						EntityCollection expandEntityCollection = getEntityCollectionFromEGMap(expandEntityMaps);
						/********* expand里可能还带有expand，是个层层嵌套的结构 ********************/
						ExpandOption nestedExpandOption = expandItem.getExpandOption(); // expand nested in expand
						// 对expandEntityCollection里对每一个entity都要做expand处理
						List<Entity> expandEntities = expandEntityCollection.getEntities();
						for (Entity expandEntity : expandEntities) {
							GenericValue expandGenericValue = expandEntityMaps.get(expandEntity);
							this.addExpandOption(nestedExpandOption, expandEntity, expandGenericValue);
						}
						/********* end: expand里可能还带有expand，是个层层嵌套的结构 ********************/
						
						Link link = new Link();
						link.setTitle(navPropName);
						link.setType(Constants.ENTITY_NAVIGATION_LINK_TYPE);
						link.setRel(Constants.NS_ASSOCIATION_LINK_REL + navPropName);
						link.setInlineEntitySet(expandEntityCollection);
						// link.setHref(expandEntityCollection.getId().toASCIIString());
						entity.getNavigationLinks().add(link);
					} else {
						Map<Entity, GenericValue> expandEntityMaps = getRelatedEntityMaps(genericValue, edmNavigationProperty, false);
						Entity expandEntity = getEntityFromEGMap(expandEntityMaps);
						/********* expand里可能还带有expand，是个层层嵌套的结构 ********************/
						ExpandOption nestedExpandOption = expandItem.getExpandOption(); // expand nested in expand
						GenericValue expandGenericValue = expandEntityMaps.get(expandEntity);
						this.addExpandOption(nestedExpandOption, expandEntity, expandGenericValue);
						/********* end: expand里可能还带有expand，是个层层嵌套的结构 ********************/
						
						Link link = new Link();
						link.setTitle(navPropName);
						link.setType(Constants.ENTITY_NAVIGATION_LINK_TYPE);
						link.setRel(Constants.NS_ASSOCIATION_LINK_REL + navPropName);
						link.setInlineEntity(expandEntity);
						// link.setHref(expandEntityCollection.getId().toASCIIString());
						entity.getNavigationLinks().add(link);
					}
				}
			}
		}
	}

	private Entity getEntityFromEGMap(Map<Entity, GenericValue> entityMaps) {
		Set<Entity> entitySet = entityMaps.keySet();
		return entitySet.iterator().next();
	}

	private EntityCollection getEntityCollectionFromEGMap(Map<Entity, GenericValue> entityMaps) {
		EntityCollection entityCollection = new EntityCollection();
		// check for which EdmEntitySet the data is requested
		List<Entity> entityList = entityCollection.getEntities();
		Set<Entity> entitySet = entityMaps.keySet();
		entityList.addAll(entitySet);
		return entityCollection;
	}
}
