package main.java.com.banfftech.platformmanager.odata.ofbiz;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.olingo.commons.api.edm.EdmEnumType;
import org.apache.olingo.commons.api.edm.EdmType;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.commons.core.edm.primitivetype.EdmDate;
import org.apache.olingo.commons.core.edm.primitivetype.EdmString;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourcePrimitiveProperty;
import org.apache.olingo.server.api.uri.queryoption.expression.BinaryOperatorKind;
import org.apache.olingo.server.api.uri.queryoption.expression.Expression;
import org.apache.olingo.server.api.uri.queryoption.expression.ExpressionVisitException;
import org.apache.olingo.server.api.uri.queryoption.expression.ExpressionVisitor;
import org.apache.olingo.server.api.uri.queryoption.expression.Literal;
import org.apache.olingo.server.api.uri.queryoption.expression.Member;
import org.apache.olingo.server.api.uri.queryoption.expression.MethodKind;
import org.apache.olingo.server.api.uri.queryoption.expression.UnaryOperatorKind;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilDateTime;
import org.apache.ofbiz.entity.condition.EntityComparisonOperator;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityExpr;
import org.apache.ofbiz.entity.condition.EntityJoinOperator;
import org.apache.ofbiz.entity.condition.EntityOperator;

public class OfbizExpressionVisitor implements ExpressionVisitor<Object> {
	public static final String module = OfbizExpressionVisitor.class.getName();

	public final static Map<BinaryOperatorKind, EntityComparisonOperator> COMPARISONOPERATORMAP = new HashMap<BinaryOperatorKind, EntityComparisonOperator>();
	static {
		COMPARISONOPERATORMAP.put(BinaryOperatorKind.EQ, EntityOperator.EQUALS);
		COMPARISONOPERATORMAP.put(BinaryOperatorKind.GE, EntityOperator.GREATER_THAN_EQUAL_TO);
		COMPARISONOPERATORMAP.put(BinaryOperatorKind.GT, EntityOperator.GREATER_THAN);
		COMPARISONOPERATORMAP.put(BinaryOperatorKind.LE, EntityOperator.LESS_THAN_EQUAL_TO);
		COMPARISONOPERATORMAP.put(BinaryOperatorKind.LT, EntityOperator.LESS_THAN);
		COMPARISONOPERATORMAP.put(BinaryOperatorKind.HAS, EntityOperator.LIKE);
	};
	public final static Map<BinaryOperatorKind, EntityJoinOperator> JOINOPERATORMAP = new HashMap<BinaryOperatorKind, EntityJoinOperator>();
	static {
		JOINOPERATORMAP.put(BinaryOperatorKind.AND, EntityOperator.AND);
		JOINOPERATORMAP.put(BinaryOperatorKind.OR, EntityOperator.OR);
	};

	@Override
	public Object visitAlias(String arg0) throws ExpressionVisitException, ODataApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitBinaryOperator(BinaryOperatorKind operator, Object left, Object right)
			throws ExpressionVisitException, ODataApplicationException {
		// Binary Operators are split up in three different kinds. Up to the kind of the
		// operator it can be applied to different types
		// - Arithmetic operations like add, minus, modulo, etc. are allowed on numeric
		// types like Edm.Int32
		// - Logical operations are allowed on numeric types and also Edm.String
		// - Boolean operations like and, or are allowed on Edm.Boolean
		// A detailed explanation can be found in OData Version 4.0 Part 2: URL
		// Conventions

		Debug.logInfo("------------------- Entering OfbizExpressionVisitor.visitBinaryOperator", module);
		Debug.logInfo("------------------- operator = " + operator, module);
		Debug.logInfo("------------------- left = " + left, module);
		Debug.logInfo("------------------- right = " + right, module);
		EntityExpr entityExpr;
		if (left instanceof EntityCondition) {
			EntityJoinOperator joinOperator = JOINOPERATORMAP.get(operator);
			List<EntityCondition> exprs = new ArrayList<EntityCondition>();
			exprs.add((EntityCondition) left);
			exprs.add((EntityCondition) right);
			return EntityCondition.makeCondition(exprs, joinOperator);
		} else if (left instanceof String){
			EntityComparisonOperator comparisonOperator = COMPARISONOPERATORMAP.get(operator);
			// List<EntityExpr> exprs = new ArrayList<EntityExpr>();
			return EntityCondition.makeCondition(left, comparisonOperator, right);
		} else {
			throw new ODataApplicationException("Binary operation " + operator.name() + " is not implemented",
					HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ENGLISH);
		}
	}

	@Override
	public Object visitEnum(EdmEnumType arg0, List<String> arg1)
			throws ExpressionVisitException, ODataApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitLambdaExpression(String arg0, String arg1, Expression arg2)
			throws ExpressionVisitException, ODataApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitLambdaReference(String arg0) throws ExpressionVisitException, ODataApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitLiteral(Literal literal) throws ExpressionVisitException, ODataApplicationException {
		// To keep this tutorial simple, our filter expression visitor supports only
		// Edm.Int32 and Edm.String
		// In real world scenarios it can be difficult to guess the type of an literal.
		// We can be sure, that the literal is a valid OData literal because the URI
		// Parser checks
		// the lexicographical structure

		Debug.logInfo("------------------- Entering OfbizExpressionVisitor.visitLiteral", module);
		// String literals start and end with an single quotation mark
		String literalAsString = literal.getText();
		if (literal.getType() instanceof EdmString) {
			String stringLiteral = "";
			if (literal.getText().length() > 2) {
				stringLiteral = literalAsString.substring(1, literalAsString.length() - 1);
			}
			Debug.logInfo("------------------- return = " + stringLiteral, module);
			return stringLiteral;
		} else if (literal.getType() instanceof EdmDate) {
			String stringLiteral = literal.getText();
			Timestamp timeStamp = Util.dateStringToTimestamp(stringLiteral);
			Debug.logInfo("------------------- return = " + timeStamp, module);
			return timeStamp;
		} else {
			// Try to convert the literal into an Java Integer
			try {
				Debug.logInfo("------------------- return = " + Integer.parseInt(literalAsString), module);
				return Integer.parseInt(literalAsString);
			} catch (NumberFormatException e) {
				throw new ODataApplicationException("Only Edm.Int32 and Edm.String literals are implemented",
						HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ENGLISH);
			}
		}
	}

	@Override
	public Object visitMember(Member member) throws ExpressionVisitException, ODataApplicationException {
		Debug.logInfo("------------------- Entering OfbizExpressionVisitor.visitMember", module);
		// To keeps things simple, this tutorial allows only primitive properties.
		// We have faith that the java type of Edm.Int32 is Integer
		final List<UriResource> uriResourceParts = member.getResourcePath().getUriResourceParts();

		// Make sure that the resource path of the property contains only a single
		// segment and a
		// primitive property has been addressed. We can be sure, that the property
		// exists because
		// the UriParser checks if the property has been defined in service metadata
		// document.

		if (uriResourceParts.size() == 1 && uriResourceParts.get(0) instanceof UriResourcePrimitiveProperty) {
			UriResourcePrimitiveProperty uriResourceProperty = (UriResourcePrimitiveProperty) uriResourceParts.get(0);
			Debug.logInfo("------------------- return = " + uriResourceProperty.getProperty().getName(), module);
			return uriResourceProperty.getProperty().getName();
		} else {
			// The OData specification allows in addition complex properties and navigation
			// properties with a target cardinality 0..1 or 1.
			// This means any combination can occur e.g. Supplier/Address/City
			// -> Navigation properties Supplier
			// -> Complex Property Address
			// -> Primitive Property City
			// For such cases the resource path returns a list of UriResourceParts
			throw new ODataApplicationException("Only primitive properties are implemented in filter expressions",
					HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ENGLISH);
		}
	}

	@Override
	public Object visitMethodCall(MethodKind methodCall, List<Object> parameters)
			throws ExpressionVisitException, ODataApplicationException {
		// To keep this tutorial small and simple, we implement only one method call
		// contains(String, String) -> Boolean
		if(methodCall == MethodKind.CONTAINS) {
			if(parameters.get(0) instanceof String && parameters.get(1) instanceof String) {
				String valueParam1 = (String) parameters.get(0);
				String valueParam2 = (String) parameters.get(1);
				return EntityCondition.makeCondition(valueParam1, EntityOperator.LIKE, "%" + valueParam2 + "%");
			} else {
				throw new ODataApplicationException("Contains needs two parametes of type Edm.String",
						HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ENGLISH);
			}
		} else {
			throw new ODataApplicationException("Method call " + methodCall + " not implemented",
					HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ENGLISH);
		}
	}

	@Override
	public Object visitTypeLiteral(EdmType arg0) throws ExpressionVisitException, ODataApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitUnaryOperator(UnaryOperatorKind arg0, Object arg1)
			throws ExpressionVisitException, ODataApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	private Object evaluateBooleanOperation(BinaryOperatorKind operator, Object left, Object right)
			throws ODataApplicationException {
		Debug.logInfo("------------------- Entering evaluateBooleanOperation", module);

		// First check that both operands are of type Boolean
		if (left instanceof Boolean && right instanceof Boolean) {
			Boolean valueLeft = (Boolean) left;
			Boolean valueRight = (Boolean) right;

			// Than calculate the result value
			if (operator == BinaryOperatorKind.AND) {
				return valueLeft && valueRight;
			} else {
				// OR
				return valueLeft || valueRight;
			}
		} else {
			throw new ODataApplicationException("Boolean operations needs two numeric operands",
					HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ENGLISH);
		}
	}

	private Object evaluateComparisonOperation(BinaryOperatorKind operator, Object left, Object right)
			throws ODataApplicationException {

		// Debug.logInfo("------------------- Entering evaluateComparisonOperation",
		// module);
		// All types in our tutorial supports all logical operations, but we have to
		// make sure that
		// the types are equal
		if (left.getClass().equals(right.getClass())) {
			// Luckily all used types String, Boolean and also Integer support the interface
			// Comparable
			int result;
			if (left instanceof Integer) {
				result = ((Comparable<Integer>) (Integer) left).compareTo((Integer) right);
			} else if (left instanceof String) {
				result = ((Comparable<String>) (String) left).compareTo((String) right);
			} else if (left instanceof Boolean) {
				result = ((Comparable<Boolean>) (Boolean) left).compareTo((Boolean) right);
			} else {
				throw new ODataApplicationException("Class " + left.getClass().getCanonicalName() + " not expected",
						HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), Locale.ENGLISH);
			}

			if (operator == BinaryOperatorKind.EQ) {
				return result == 0;
			} else if (operator == BinaryOperatorKind.NE) {
				return result != 0;
			} else if (operator == BinaryOperatorKind.GE) {
				return result >= 0;
			} else if (operator == BinaryOperatorKind.GT) {
				return result > 0;
			} else if (operator == BinaryOperatorKind.LE) {
				return result <= 0;
			} else {
				// BinaryOperatorKind.LT
				return result < 0;
			}

		} else {
			throw new ODataApplicationException("Comparison needs two equal types",
					HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ENGLISH);
		}
	}

	private Object evaluateArithmeticOperation(BinaryOperatorKind operator, Object left, Object right)
			throws ODataApplicationException {
		Debug.logInfo("------------------- Entering evaluateArithmeticOperation", module);

		// First check if the type of both operands is numerical
		if (left instanceof Integer && right instanceof Integer) {
			Integer valueLeft = (Integer) left;
			Integer valueRight = (Integer) right;

			// Than calculate the result value
			if (operator == BinaryOperatorKind.ADD) {
				return valueLeft + valueRight;
			} else if (operator == BinaryOperatorKind.SUB) {
				return valueLeft - valueRight;
			} else if (operator == BinaryOperatorKind.MUL) {
				return valueLeft * valueRight;
			} else if (operator == BinaryOperatorKind.DIV) {
				return valueLeft / valueRight;
			} else {
				// BinaryOperatorKind,MOD
				return valueLeft % valueRight;
			}
		} else {
			throw new ODataApplicationException("Arithmetic operations needs two numeric operands",
					HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ENGLISH);
		}
	}

}
