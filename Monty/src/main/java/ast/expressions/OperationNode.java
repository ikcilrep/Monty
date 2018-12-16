/*
Copyright 2018 Szymon Perlicki

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package ast.expressions;

import ast.Block;
import ast.Node;
import ast.NodeTypes;
import ast.declarations.VariableDeclarationNode;
import parser.DataTypes;
import parser.MontyException;
import ast.expressions.OperatorOverloading;

public class OperationNode extends ExpressionNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2708343059798065830L;
	private OperationNode left = null;
	private Object operand;
	private Block parent = null;

	private OperationNode right = null;

	public OperationNode(Object operand, Block parent) {
		this.operand = operand;
		this.nodeType = NodeTypes.OPERATION;
		this.parent = parent;
	}

	private Object calculate(Object leftValue, Object rightValue, Object operator, DataTypes type) {
		// Calculates the result of math operation.

		switch (operator.toString()) {
		case "+":
			return OperatorOverloading.additionOperator(leftValue, rightValue, operator, type);
		case "-":
			return OperatorOverloading.subtractionOperator(leftValue, rightValue, operator, type);
		case "*":
			return OperatorOverloading.multiplicationOperator(leftValue, rightValue, operator, type);
		case "/":
			return OperatorOverloading.divisionOperator(leftValue, rightValue, operator, type);
		case "%":
			return OperatorOverloading.moduloOperator(leftValue, rightValue, operator, type);
		case "!":
			return OperatorOverloading.negationOperator(leftValue, rightValue, operator, type);
		case "<<":
			return OperatorOverloading.shiftLeftOperator(leftValue, rightValue, operator, type);
		case ">>":
			return OperatorOverloading.shiftRightOperator(leftValue, rightValue, operator, type);
		case "^":
			return OperatorOverloading.xorOperator(leftValue, rightValue, operator, type);
		case "&":
			return OperatorOverloading.andOperator(leftValue, rightValue, operator, type);
		case "|":
			return OperatorOverloading.orOperator(leftValue, rightValue, operator, type);
		case "==":
			return OperatorOverloading.equalsOperator(leftValue, rightValue, operator, type);
		case ">":
			return OperatorOverloading.greaterOperator(leftValue, rightValue, operator, type);
		case "<":
			return OperatorOverloading.lowerOperator(leftValue, rightValue, operator, type);
		case "<=":
			return OperatorOverloading.lowerEqualsOperator(leftValue, rightValue, operator, type);
		case ">=":
			return OperatorOverloading.greaterEqualsOperator(leftValue, rightValue, operator, type);
		case "!=":
			return OperatorOverloading.notEqualsOperator(leftValue, rightValue, operator, type);
		case "=":
			return OperatorOverloading.assignmentOperator(leftValue, rightValue, operator, type);
		case "+=":
			return OperatorOverloading.assignmentAdditionOperator(leftValue, rightValue, operator, type);
		case "-=":
			return OperatorOverloading.assignmentSubtractionOperator(leftValue, rightValue, operator, type);
		case "*=":
			return OperatorOverloading.assignmentMultiplicationOperator(leftValue, rightValue, operator, type);
		case "/=":
			return OperatorOverloading.assignmentDivisionOperator(leftValue, rightValue, operator, type);
		case "<<=":
			return OperatorOverloading.assignmentShiftLeftOperator(leftValue, rightValue, operator, type);
		case ">>=":
			return OperatorOverloading.assignmentShiftRightOperator(leftValue, rightValue, operator, type);
		case "^=":
			return OperatorOverloading.assignmentXorOperator(leftValue, rightValue, operator, type);
		case "&=":
			return OperatorOverloading.assignmentAndOperator(leftValue, rightValue, operator, type);
		case "|=":
			return OperatorOverloading.assignmentOrOperator(leftValue, rightValue, operator, type);
		}
		return null;
	}

	private DataTypes getDataType(Object expression) {
		// Returns data type of expression.
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

		return DataTypes.getDataType(expression);
	}

	public OperationNode getLeftOperand() {
		return left;
	}

	private Object getLiteral(Object expression) {
		// Returns value of expression.
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
		// Returns calculated value.
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
		Object operator = getOperand();
		var isComparison = operator.toString().equals("==") || operator.toString().equals("!=")
				|| operator.toString().equals("<=") || operator.toString().equals(">=")
				|| operator.toString().equals(">") || operator.toString().equals("<");
		Object leftValue = null;
		Object rightValue = null;
		DataTypes type = getDataType(a);
		// If type isn't array and type of a and b aren't equals.
		if (!(type.equals(DataTypes.ARRAY) || type.equals(DataTypes.LIST) || type.equals(DataTypes.STACK) || type.equals(getDataType(b))))
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
		return calculate(leftValue, rightValue, getOperand(), type);

	}
}
