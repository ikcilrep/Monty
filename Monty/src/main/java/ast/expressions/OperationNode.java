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
		this.parent = parent;
	}

	private Object calculate(Object a, Object b, Object operator) {
		var isComparison = operator.toString().equals("==") || operator.toString().equals("!=")
				|| operator.toString().equals("<=") || operator.toString().equals(">=")
				|| operator.toString().equals(">") || operator.toString().equals("<");
		Object leftValue = null;
		Object rightValue = null;
		DataTypes type = getDataType(a);
		if (!type.equals(getDataType(b)))
			new MontyException("Type mismatch:\t" + type + " and " + getDataType(b));
		leftValue = getLiteral(a);
		rightValue = getLiteral(b);
		if (!operator.toString().contains("=") || (operator.toString().contains("=") && isComparison))
			if (a instanceof Node && ((Node) a).getNodeType().equals(NodeTypes.VARIABLE)) {
				var variable = parent.getVariableByName(((VariableNode) a).getName());
				leftValue = variable.getValue();

			}
		if (b instanceof Node && ((Node) b).getNodeType().equals(NodeTypes.VARIABLE)) {
			var variable = parent.getVariableByName(((VariableNode) b).getName());
			rightValue = variable.getValue();
		}
		switch (operator.toString()) {
		case "+":
			switch (type) {
			case INTEGER:
				return ((Integer) leftValue) + ((Integer) rightValue);
			case FLOAT:
				return ((Float) leftValue) + ((Float) rightValue);
			case STRING:
				return leftValue.toString() + rightValue.toString();
			case BOOLEAN:
				new MontyException("Can't add booleans:\t" + leftValue.toString() + " " + rightValue.toString() + " "
						+ operator.toString());
			}
		case "-":
			switch (type) {
			case INTEGER:
				return ((Integer) leftValue) - ((Integer) rightValue);
			case FLOAT:
				return ((Float) leftValue) - ((Float) rightValue);
			case STRING:
				new MontyException("Can't subtract strings:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			case BOOLEAN:
				new MontyException("Can't subtract booleans:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			}
		case "*":
			switch (type) {
			case INTEGER:
				return ((Integer) leftValue) * ((Integer) rightValue);
			case FLOAT:
				return ((Float) leftValue) * ((Float) rightValue);
			case STRING:
				new MontyException("Can't multiply strings:\t\"" + leftValue.toString() + "\" \""
						+ rightValue.toString() + "\" " + operator.toString());
			case BOOLEAN:
				new MontyException("Can't multiply booleans:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			}
		case "/":
			switch (type) {
			case INTEGER:
				return ((Integer) leftValue) / ((Integer) rightValue);
			case FLOAT:
				return ((Float) leftValue) / ((Float) rightValue);
			case STRING:
				new MontyException("Can't divide strings:\t\"" + leftValue.toString() + "\" \"" + rightValue.toString()
						+ "\" " + operator.toString());
			case BOOLEAN:
				new MontyException("Can't divide booleans:\t" + leftValue.toString() + " " + rightValue.toString() + " "
						+ operator.toString());
			}
		case "%":
			switch (type) {
			case INTEGER:
				return (Integer) leftValue % (Integer) rightValue;
			case FLOAT:
				return ((Float) leftValue) % ((Float) rightValue);
			case STRING:
				new MontyException("Can't modulo strings:\t\"" + leftValue.toString() + "\" \"" + rightValue.toString()
						+ "\" " + operator.toString());
			case BOOLEAN:
				new MontyException("Can't modulo booleans:\t" + leftValue.toString() + " " + rightValue.toString() + " "
						+ operator.toString());
			}
		case "!":
			switch (type) {
			case INTEGER:
				return 0 - (Integer) rightValue;
			case FLOAT:
				return 0 - (Float) rightValue;
			case STRING:
				new MontyException("There isn't opposite of \"" + rightValue.toString() + "\".");
			case BOOLEAN:
				return !(boolean) rightValue;
			}
		case "<<":
			switch (type) {
			case INTEGER:
				return ((Integer) leftValue) << ((Integer) rightValue);
			case FLOAT:
				new MontyException("Can't shift left Floats:\t " + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			case STRING:
				new MontyException("Can't shift left strings:\t\"" + leftValue.toString() + "\" \""
						+ rightValue.toString() + "\" " + operator.toString());
			case BOOLEAN:
				new MontyException("Can't shift left booleans:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			}
		case ">>":
			switch (type) {
			case INTEGER:
				return ((Integer) leftValue) >> ((Integer) rightValue);
			case FLOAT:
				new MontyException("Can't shift right Floats:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			case STRING:
				new MontyException("Can't shift right strings:\t\"" + leftValue.toString() + "\" \""
						+ rightValue.toString() + "\" " + operator.toString());
			case BOOLEAN:
				new MontyException("Can't shift right booleans:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			}
		case "^":
			switch (type) {
			case INTEGER:
				return ((Integer) leftValue) ^ ((Integer) rightValue);
			case FLOAT:
				new MontyException("Can't xor Floats:\t" + leftValue.toString() + " " + rightValue.toString() + " "
						+ operator.toString());
			case STRING:
				new MontyException("Can't xor strings:\t\"" + leftValue.toString() + "\" \"" + rightValue.toString()
						+ "\" " + operator.toString());
			case BOOLEAN:
				new MontyException("Can't xor booleans:\t" + leftValue.toString() + " " + rightValue.toString() + " "
						+ operator.toString());
			}
		case "&":
			switch (type) {
			case INTEGER:
				return ((Integer) leftValue) & ((Integer) rightValue);
			case FLOAT:
				new MontyException("Can't and Floats:\t\"" + leftValue.toString() + "\" \"" + rightValue.toString()
						+ "\" " + operator.toString());
			case STRING:
				new MontyException("Can't and strings:\t\"" + leftValue.toString() + "\" \"" + rightValue.toString()
						+ "\" " + operator.toString());
			case BOOLEAN:
				new MontyException("Can't and booleans:\t" + leftValue.toString() + " " + rightValue.toString() + " "
						+ operator.toString());
			}
		case "|":
			switch (type) {
			case INTEGER:
				return ((Integer) leftValue) | ((Integer) rightValue);
			case FLOAT:
				new MontyException("Can't or Floats:\t" + leftValue.toString() + " " + rightValue.toString() + " "
						+ operator.toString());
			case STRING:
				new MontyException("Can't or strings:\t\"" + leftValue.toString() + "\" \"" + rightValue.toString()
						+ "\" " + operator.toString());
			case BOOLEAN:
				new MontyException("Can't or booleans:\t" + leftValue.toString() + " " + rightValue.toString() + " "
						+ operator.toString());
			}
		case "&&":
			switch (type) {
			case INTEGER:
				new MontyException("Can't and Integeregers:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			case FLOAT:
				new MontyException("Can't and Floats:\t" + leftValue.toString() + " " + rightValue.toString() + " "
						+ operator.toString());
			case STRING:
				new MontyException("Can't and strings:\t\"" + leftValue.toString() + "\" \"" + rightValue.toString()
						+ "\" " + operator.toString());
			case BOOLEAN:
				return ((boolean) leftValue) && ((boolean) rightValue);
			}
		case "||":
			switch (type) {
			case INTEGER:
				new MontyException("Can't or Integeregers:\t" + leftValue.toString() + " " + rightValue.toString()
						+ "\" " + operator.toString());
			case FLOAT:
				new MontyException("Can't or Floats:\t" + leftValue.toString() + " " + rightValue.toString() + " "
						+ operator.toString());
			case STRING:
				new MontyException("Can't or strings:\t\"" + leftValue.toString() + "\" \"" + rightValue.toString()
						+ "\" " + operator.toString());
			case BOOLEAN:
				return ((boolean) leftValue) || ((boolean) rightValue);
			}
		case "==":
			switch (type) {
			case INTEGER:
				return (Integer) leftValue == (Integer) rightValue;
			case FLOAT:
				return (Float) leftValue == (Float) rightValue;
			case BOOLEAN:
				return (boolean) leftValue == (boolean) rightValue;
			case STRING:
				return leftValue.equals(rightValue);
			}
		case ">":
			switch (type) {
			case INTEGER:
				return (Integer) leftValue > (Integer) rightValue;
			case FLOAT:
				return (Float) leftValue > (Float) rightValue;
			case STRING:
				new MontyException("One string can't be greater than other string:\t" + leftValue.toString() + " "
						+ rightValue.toString() + " " + operator.toString());
			case BOOLEAN:
				new MontyException("One boolean can't be greater than other string:\t" + leftValue.toString() + " "
						+ rightValue.toString() + " " + operator.toString());
			}
		case "<":
			switch (type) {
			case INTEGER:
				return (Integer) leftValue < (Integer) rightValue;
			case FLOAT:
				return (Float) leftValue < (Float) rightValue;
			case STRING:
				new MontyException("One string can't be lower than other string:\t" + leftValue.toString() + " "
						+ rightValue.toString() + " " + operator.toString());
			case BOOLEAN:
				new MontyException("One boolean can't be lower than other string:\t" + leftValue.toString() + " "
						+ rightValue.toString() + " " + operator.toString());
			}
		case "<=":
			switch (type) {
			case INTEGER:
				return (Integer) leftValue <= (Integer) rightValue;
			case FLOAT:
				return (Float) leftValue <= (Float) rightValue;
			case STRING:
				new MontyException("One string can't be lower than other string:\t" + leftValue.toString() + " "
						+ rightValue.toString() + " " + operator.toString());
			case BOOLEAN:
				new MontyException("One boolean can't be lower than other string:\t" + leftValue.toString() + " "
						+ rightValue.toString() + " " + operator.toString());
			}
		case ">=":
			switch (type) {
			case INTEGER:
				return (Integer) leftValue >= (Integer) rightValue;
			case FLOAT:
				return (Float) leftValue >= (Float) rightValue;
			case STRING:
				new MontyException("One string can't be greater than other string:\t" + leftValue.toString() + " "
						+ rightValue.toString() + " " + operator.toString());
			case BOOLEAN:
				new MontyException("One boolean can't be greater than other string:\t" + leftValue.toString() + " "
						+ rightValue.toString() + " " + operator.toString());
			}
		case "!=":
			switch (type) {
			case INTEGER:
				return (Integer) leftValue != (Integer) rightValue;
			case FLOAT:
				return (Float) leftValue != (Float) rightValue;
			case BOOLEAN:
				return (boolean) leftValue != (boolean) rightValue;
			case STRING:
				return !leftValue.equals(rightValue);
			}
		case "=":
			var variable = ((VariableDeclarationNode) leftValue);
			switch (type) {
			case INTEGER:
			case FLOAT:
			case STRING:
			case BOOLEAN:
				variable.setValue(rightValue);
				return variable.getValue();
			}
		case "+=":
			variable = ((VariableDeclarationNode) leftValue);
			switch (type) {
			case INTEGER:
				variable.setValue((Integer) variable.getValue() + (Integer) rightValue);
				return variable.getValue();
			case FLOAT:
				variable.setValue((Float) variable.getValue() + (Float) rightValue);
				return variable.getValue();
			case STRING:
				variable.setValue(variable.getValue().toString() + rightValue.toString());
				return variable.getValue();
			case BOOLEAN:
				new MontyException("Can't add booleans:\t" + leftValue.toString() + " " + rightValue.toString() + " "
						+ operator.toString());
				return null;
			}
		case "-=":
			variable = ((VariableDeclarationNode) leftValue);
			switch (type) {
			case INTEGER:
				variable.setValue((Integer) variable.getValue() - (Integer) rightValue);
				return variable.getValue();
			case FLOAT:
				variable.setValue((Float) variable.getValue() - (Float) rightValue);
				return variable.getValue();
			case STRING:
				new MontyException("Can't subtract strings:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			case BOOLEAN:
				new MontyException("Can't subtract booleans:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			}
		case "*=":
			variable = ((VariableDeclarationNode) leftValue);
			switch (type) {
			case INTEGER:
				variable.setValue((Integer) variable.getValue() * (Integer) rightValue);
				return variable.getValue();
			case FLOAT:
				variable.setValue((Float) variable.getValue() * (Float) rightValue);
				return variable.getValue();
			case STRING:
				new MontyException("Can't multiply strings:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			case BOOLEAN:
				new MontyException("Can't multiply booleans:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			}
		case "/=":
			variable = ((VariableDeclarationNode) leftValue);
			switch (type) {
			case INTEGER:
				variable.setValue((Integer) variable.getValue() / (Integer) rightValue);
				return variable.getValue();
			case FLOAT:
				variable.setValue((Float) variable.getValue() / (Float) rightValue);
				return variable.getValue();
			case STRING:
				new MontyException("Can't divide strings:\t" + leftValue.toString() + " " + rightValue.toString() + " "
						+ operator.toString());
			case BOOLEAN:
				new MontyException("Can't divide booleans:\t" + leftValue.toString() + " " + rightValue.toString() + " "
						+ operator.toString());
			}
		case "<<=":
			switch (type) {
			case INTEGER:
				variable = ((VariableDeclarationNode) leftValue);
				variable.setValue((Integer) variable.getValue() << (Integer) rightValue);
				return variable.getValue();
			case FLOAT:
				new MontyException("Can't shift left Floats:\t " + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			case STRING:
				new MontyException("Can't shift left strings:\t\"" + leftValue.toString() + "\" \""
						+ rightValue.toString() + "\" " + operator.toString());
			case BOOLEAN:
				new MontyException("Can't shift left booleans:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			}
		case ">>=":
			switch (type) {
			case INTEGER:
				variable = ((VariableDeclarationNode) leftValue);
				variable.setValue((Integer) variable.getValue() >> (Integer) rightValue);
				return variable.getValue();
			case FLOAT:
				new MontyException("Can't shift right Floats:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			case STRING:
				new MontyException("Can't shift right strings:\t\"" + leftValue.toString() + "\" \""
						+ rightValue.toString() + "\" " + operator.toString());
			case BOOLEAN:
				new MontyException("Can't shift right booleans:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			}
		case "^=":
			switch (type) {
			case INTEGER:
				variable = ((VariableDeclarationNode) leftValue);
				variable.setValue((Integer) variable.getValue() ^ (Integer) rightValue);
				return variable.getValue();
			case FLOAT:
				new MontyException("Can't xor Floats:\t" + leftValue.toString() + " " + rightValue.toString() + " "
						+ operator.toString());
			case STRING:
				new MontyException("Can't xor strings:\t\"" + leftValue.toString() + "\" \"" + rightValue.toString()
						+ "\" " + operator.toString());
			case BOOLEAN:
				new MontyException("Can't xor booleans:\t" + leftValue.toString() + " " + rightValue.toString() + " "
						+ operator.toString());
			}
		case "&=":
			switch (type) {
			case INTEGER:
				variable = ((VariableDeclarationNode) leftValue);
				variable.setValue((Integer) variable.getValue() & (Integer) rightValue);
				return variable.getValue();
			case FLOAT:
				new MontyException("Can't and Floats:\t\"" + leftValue.toString() + "\" \"" + rightValue.toString()
						+ "\" " + operator.toString());
			case STRING:
				new MontyException("Can't and strings:\t\"" + leftValue.toString() + "\" \"" + rightValue.toString()
						+ "\" " + operator.toString());
			case BOOLEAN:
				new MontyException("Can't and booleans:\t" + leftValue.toString() + " " + rightValue.toString() + " "
						+ operator.toString());
			}
		case "|=":
			switch (type) {
			case INTEGER:
				variable = ((VariableDeclarationNode) leftValue);
				variable.setValue((Integer) variable.getValue() | (Integer) rightValue);
				return variable.getValue();
			case FLOAT:
				new MontyException("Can't or Floats:\t" + leftValue.toString() + " " + rightValue.toString() + " "
						+ operator.toString());
			case STRING:
				new MontyException("Can't or strings:\t\"" + leftValue.toString() + "\" \"" + rightValue.toString()
						+ "\" " + operator.toString());
			case BOOLEAN:
				new MontyException("Can't or booleans:\t" + leftValue.toString() + " " + rightValue.toString() + " "
						+ operator.toString());
			}
		}
		return operator;
	}

	public OperationNode getLeftOperand() {
		return left;
	}

	private Object getLiteral(Object expression) {
		if (expression instanceof Node)
			switch (((Node) expression).getNodeType()) {
			case VARIABLE:
				return parent.getVariableByName(((VariableNode) expression).getName());
			case FUNCTION_CALL:
				var functionToCall = ((FunctionCallNode) expression);
				var function = parent.getFunctionByName(functionToCall.getName());
				return function.call(functionToCall.getArguments());
			case CONSTANT:
				var cn = ((ConstantNode) expression);
				return cn.getValue();
			default:
				return null;
			}
		else
			return expression;
	}

	private DataTypes getDataType(Object expression) {
		if (expression instanceof Node)
			switch (((Node) expression).getNodeType()) {
			case VARIABLE:
				return parent.getVariableByName(((VariableNode) expression).getName()).getType();
			case FUNCTION_CALL:
				var functionCall = ((FunctionCallNode) expression);
				var function = parent.getFunctionByName(functionCall.getName());
				return function.getType();
			case CONSTANT:
				var cn = ((ConstantNode) expression);
				return cn.getType();
			default:
				return null;
			}
		else if (expression instanceof String)
			return DataTypes.STRING;
		else if (expression instanceof Integer)
			return DataTypes.INTEGER;
		else if (expression instanceof Float)
			return DataTypes.FLOAT;
		else if (expression instanceof Boolean)
			return DataTypes.BOOLEAN;
		return null;
	}

	public Object getOperand() {
		return operand;
	}

	public Block getParent() {
		return parent;
	}

	public OperationNode getRightOperand() {
		return right;
	}

	public Object run() {
		if (!getOperand().getClass().equals(String.class)) {
			var operand = getOperand();
			var castedOperand = (Node) operand;
			if (castedOperand.getNodeType().equals(NodeTypes.VARIABLE)) {
				var variable = ((VariableDeclarationNode) getLiteral(operand));
				return variable.getValue();
			} else {
				return getLiteral(operand);
			}
		}
		return solve();
	}

	public void setLeftOperand(OperationNode left) {
		this.left = left;
	}

	public void setRightOperand(OperationNode right) {
		this.right = right;
	}

	private Object solve() {
		if (!getOperand().getClass().equals(String.class))
			return getOperand();
		Object a = getLeftOperand().solve();
		Object b = getRightOperand().solve();
		return calculate(a, b, getOperand());

	}
}
