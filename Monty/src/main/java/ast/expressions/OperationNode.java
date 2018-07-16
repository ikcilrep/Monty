package ast.expressions;

import ast.Block;
import ast.Node;
import ast.NodeTypes;
import ast.declarations.VariableDeclarationNode;
import parser.DataTypes;
import parser.MontyException;

public class OperationNode extends ExpressionNode {
	private Object operand;
	private OperationNode left = null;
	private OperationNode right = null;
	private Block parent = null;

	public OperationNode(Object operand, Block parent) {
		this.operand = operand;
		this.nodeType = NodeTypes.OPERATION;
	}

	public OperationNode getLeftOperand() {
		return left;
	}

	public OperationNode getRightOperand() {
		return right;
	}

	public Object getOperand() {
		return operand;
	}

	public void setLeftOperand(OperationNode left) {
		this.left = left;
	}

	public void setRightOperand(OperationNode right) {
		this.right = right;
	}

	public Block getParent() {
		return parent;
	}

	public Object run(DataTypes dataType) {
		if (!getOperand().getClass().equals(String.class))
			return getOperand();
		Object a = getLeftOperand().run(dataType);
		Object b = getRightOperand().run(dataType);

		return calculate(a, b, getOperand(), dataType);

	}

	private Object getLiteral(Object expression, DataTypes dataType) {
		switch (((Node) expression).getNodeType()) {
		case VARIABLE:
			return parent.getVariableByName(((VariableNode) expression).getName());
		case FUNCTION_CALL:
			var functionToCall = ((FunctionCallNode) expression);
			var function = parent.getFunctionByName(functionToCall.getName());
			return toDataType(
					new ConstantNode(function.call(functionToCall.getArguments()).toString(), function.getType()),
					dataType);
		case CONSTANT:
			var cn = ((ConstantNode) expression);
			return toDataType(cn, dataType);
		default:
			return null;
		}
	}

	private Object calculate(Object a, Object b, Object operator, DataTypes dataType) {
		Object leftValue = getLiteral(a, dataType);
		Object rightValue = getLiteral(a, dataType);
		if (!operator.toString().contains("="))
			if (((Node) a).getNodeType().equals(NodeTypes.VARIABLE)) {
				var variable = ((VariableDeclarationNode) a);
				rightValue = toDataType(new ConstantNode(variable.getValue().toString(), variable.getType()), dataType);
			}
		if (((Node) b).getNodeType().equals(NodeTypes.VARIABLE)) {
			var variable = ((VariableDeclarationNode) b);
			rightValue = toDataType(new ConstantNode(variable.getValue().toString(), variable.getType()), dataType);
		}
		switch (operator.toString()) {
		case "+":
			switch (dataType) {
			case INTEGER:
				return ((int) leftValue) + ((int) rightValue);
			case FLOAT:
				return ((float) leftValue) + ((float) rightValue);
			case STRING:
				return leftValue.toString() + rightValue.toString();
			case BOOLEAN:
				new MontyException("Can't add booleans:\t" + leftValue.toString() + " " + rightValue.toString() + " "
						+ operator.toString());
			}
		case "-":
			switch (dataType) {
			case INTEGER:
				return ((int) leftValue) - ((int) rightValue);
			case FLOAT:
				return ((float) leftValue) - ((float) rightValue);
			case STRING:
				new MontyException("Can't subtract strings:\t\"" + leftValue.toString() + "\" \""
						+ rightValue.toString() + "\" " + operator.toString());
			case BOOLEAN:
				new MontyException("Can't subtract booleans:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			}
		case "*":
			switch (dataType) {
			case INTEGER:
				return ((int) leftValue) * ((int) rightValue);
			case FLOAT:
				return ((float) leftValue) * ((float) rightValue);
			case STRING:
				new MontyException("Can't multiply strings:\t\"" + leftValue.toString() + "\" \""
						+ rightValue.toString() + "\" " + operator.toString());
			case BOOLEAN:
				new MontyException("Can't multiply booleans:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			}
		case "/":
			switch (dataType) {
			case INTEGER:
				return ((int) leftValue) / ((int) rightValue);
			case FLOAT:
				return ((float) leftValue) / ((float) rightValue);
			case STRING:
				new MontyException("Can't divide strings:\t\"" + leftValue.toString() + "\" \"" + rightValue.toString()
						+ "\" " + operator.toString());
			case BOOLEAN:
				new MontyException("Can't divide booleans:\t" + leftValue.toString() + " " + rightValue.toString() + " "
						+ operator.toString());
			}
		case "<<":
			switch (dataType) {
			case INTEGER:
				return ((int) leftValue) / ((int) rightValue);
			case FLOAT:
				new MontyException("Can't shift left floats:\t\"" + leftValue.toString() + "\" \""
						+ rightValue.toString() + "\" " + operator.toString());
			case STRING:
				new MontyException("Can't shift left strings:\t\"" + leftValue.toString() + "\" \""
						+ rightValue.toString() + "\" " + operator.toString());
			case BOOLEAN:
				new MontyException("Can't shift left booleans:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			}
		case ">>":
			switch (dataType) {
			case INTEGER:
				return ((int) leftValue) >> ((int) rightValue);
			case FLOAT:
				new MontyException("Can't shift right floats:\t\"" + leftValue.toString() + "\" \""
						+ rightValue.toString() + "\" " + operator.toString());
			case STRING:
				new MontyException("Can't shift right strings:\t\"" + leftValue.toString() + "\" \""
						+ rightValue.toString() + "\" " + operator.toString());
			case BOOLEAN:
				new MontyException("Can't shift right booleans:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			}
		case "^":
			switch (dataType) {
			case INTEGER:
				return ((int) leftValue) ^ ((int) rightValue);
			case FLOAT:
				new MontyException("Can't xor floats:\t\"" + leftValue.toString() + "\" \"" + rightValue.toString()
						+ "\" " + operator.toString());
			case STRING:
				new MontyException("Can't xor strings:\t\"" + leftValue.toString() + "\" \"" + rightValue.toString()
						+ "\" " + operator.toString());
			case BOOLEAN:
				new MontyException("Can't xor booleans:\t" + leftValue.toString() + " " + rightValue.toString() + " "
						+ operator.toString());
			}
		case "&":
			switch (dataType) {
			case INTEGER:
				return ((int) leftValue) & ((int) rightValue);
			case FLOAT:
				new MontyException("Can't and floats:\t\"" + leftValue.toString() + "\" \"" + rightValue.toString()
						+ "\" " + operator.toString());
			case STRING:
				new MontyException("Can't and strings:\t\"" + leftValue.toString() + "\" \"" + rightValue.toString()
						+ "\" " + operator.toString());
			case BOOLEAN:
				new MontyException("Can't and booleans:\t" + leftValue.toString() + " " + rightValue.toString() + " "
						+ operator.toString());
			}
		case "|":
			switch (dataType) {
			case INTEGER:
				return ((int) leftValue) ^ ((int) rightValue);
			case FLOAT:
				new MontyException("Can't or floats:\t\"" + leftValue.toString() + "\" \"" + rightValue.toString()
						+ "\" " + operator.toString());
			case STRING:
				new MontyException("Can't or strings:\t\"" + leftValue.toString() + "\" \"" + rightValue.toString()
						+ "\" " + operator.toString());
			case BOOLEAN:
				new MontyException("Can't or booleans:\t" + leftValue.toString() + " " + rightValue.toString() + " "
						+ operator.toString());
			}
		case "&&":
			switch (dataType) {
			case INTEGER:
				new MontyException("Can't and integers:\t\"" + leftValue.toString() + "\" \"" + rightValue.toString()
						+ "\" " + operator.toString());
			case FLOAT:
				new MontyException("Can't and floats:\t\"" + leftValue.toString() + "\" \"" + rightValue.toString()
						+ "\" " + operator.toString());
			case STRING:
				new MontyException("Can't and strings:\t\"" + leftValue.toString() + "\" \"" + rightValue.toString()
						+ "\" " + operator.toString());
			case BOOLEAN:
				return ((boolean) leftValue) && ((boolean) rightValue);
			}
		case "||":
			switch (dataType) {
			case INTEGER:
				new MontyException("Can't or integers:\t\"" + leftValue.toString() + "\" \"" + rightValue.toString()
						+ "\" " + operator.toString());
			case FLOAT:
				new MontyException("Can't or floats:\t\"" + leftValue.toString() + "\" \"" + rightValue.toString()
						+ "\" " + operator.toString());
			case STRING:
				new MontyException("Can't or strings:\t\"" + leftValue.toString() + "\" \"" + rightValue.toString()
						+ "\" " + operator.toString());
			case BOOLEAN:
				return ((boolean) leftValue) || ((boolean) rightValue);

			}
		case "=":
			switch (dataType) {
			case INTEGER:
			case FLOAT:
			case STRING:
			case BOOLEAN:
				((VariableDeclarationNode)leftValue).setValue(rightValue);
				return rightValue;
			}
		}
		return operator;
	}

	private static Object toDataType(ConstantNode literal, DataTypes dataType) {
		var value = literal.getValue();
		switch (dataType) {
		case INTEGER:
			switch (literal.getType()) {
			case INTEGER:
				return Integer.parseInt(value);
			case FLOAT:
				return (int) Float.parseFloat(value);
			case BOOLEAN:
				if (value.equals("true"))
					return 1;
				else
					return 0;
			case STRING:
				if (value.matches("[+-]?[0-9]+"))
					return Integer.parseInt(value);
				else if (value.matches("[+-]?[0-9]+\\.[0-9]+"))
					return (int) Float.parseFloat(value);
				else if (value.equals("true") || value.equals("false"))
					return toDataType(new ConstantNode(value, DataTypes.BOOLEAN), DataTypes.INTEGER);
				else if (value.length() == 1)
					return Character.getNumericValue(value.charAt(0));
				else
					new MontyException("Value \"" + value + "\" can't be casted to integer.");
			default:
				break;
			}
			break;
		case FLOAT:
			switch (literal.getType()) {
			case INTEGER:
				return (float) Integer.parseInt(value);
			case FLOAT:
				return Float.parseFloat(value);
			case BOOLEAN:
				if (value.equals("true"))
					return 1f;
				else
					return 0f;
			case STRING:
				if (value.matches("[+-]?[0-9]+"))
					return (float) Integer.parseInt(value);
				else if (value.matches("[+-]?[0-9]+\\.[0-9]+"))
					return Float.parseFloat(value);
				else if (value.equals("true") || value.equals("false"))
					return toDataType(new ConstantNode(value, DataTypes.BOOLEAN), DataTypes.FLOAT);
				else if (value.length() == 1)
					return (float) Character.getNumericValue(value.charAt(0));
				else
					new MontyException("Value \"" + value + "\" can't be casted to float.");
			default:
				break;
			}
			break;
		case BOOLEAN:
			switch (literal.getType()) {
			case INTEGER:
				if (Integer.parseInt(value) > 0)
					return true;
				else
					return false;
			case FLOAT:
				if (Float.parseFloat(value) > 0f)
					return true;
				else
					return false;
			case BOOLEAN:
				return value.equals("true");

			case STRING:
				if (value.matches("[+-]?[0-9]+"))
					return toDataType(new ConstantNode(value, DataTypes.INTEGER), DataTypes.BOOLEAN);
				else if (value.matches("[+-]?[0-9]+\\.[0-9]+"))
					return toDataType(new ConstantNode(value, DataTypes.FLOAT), DataTypes.BOOLEAN);
				else if (value.equals("true"))
					return 1;
				else if (value.equals("false"))
					return 0;
				else if (value.length() == 1)
					return Character.getNumericValue(value.charAt(0)) > 1;
				else
					new MontyException("Value \"" + value + "\" can't be casted to boolean.");
			default:
				break;
			}
			break;
		case STRING:
			return value;
		default:
			return literal;
		}
		return dataType;
	}
}
