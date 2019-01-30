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
import ast.RunnableNode;
import ast.declarations.StructDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import parser.DataTypes;
import parser.LogError;

public class OperationNode extends ExpressionNode implements RunnableNode{

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
			return OperatorOverloading.additionOperator(leftValue, rightValue, operator, type, getFileName(),
					getLine());
		case "-":
			return OperatorOverloading.subtractionOperator(leftValue, rightValue, operator, type, getFileName(),
					getLine());
		case "*":
			return OperatorOverloading.multiplicationOperator(leftValue, rightValue, operator, type, getFileName(),
					getLine());
		case "/":
			return OperatorOverloading.divisionOperator(leftValue, rightValue, operator, type, getFileName(),
					getLine());
		case "%":
			return OperatorOverloading.moduloOperator(leftValue, rightValue, operator, type, getFileName(), getLine());
		case "!":
			return OperatorOverloading.negationOperator(leftValue, rightValue, operator, type, getFileName(),
					getLine());
		case "<<":
			return OperatorOverloading.shiftLeftOperator(leftValue, rightValue, operator, type, getFileName(),
					getLine());
		case ">>":
			return OperatorOverloading.shiftRightOperator(leftValue, rightValue, operator, type, getFileName(),
					getLine());
		case "^":
			return OperatorOverloading.xorOperator(leftValue, rightValue, operator, type, getFileName(), getLine());
		case "&":
			return OperatorOverloading.andOperator(leftValue, rightValue, operator, type, getFileName(), getLine());
		case "|":
			return OperatorOverloading.orOperator(leftValue, rightValue, operator, type, getFileName(), getLine());
		case "==":
			return OperatorOverloading.equalsOperator(leftValue, rightValue, operator, type, getFileName(), getLine());
		case ">":
			return OperatorOverloading.greaterOperator(leftValue, rightValue, operator, type, getFileName(), getLine());
		case "<":
			return OperatorOverloading.lowerOperator(leftValue, rightValue, operator, type, getFileName(), getLine());
		case "<=":
			return OperatorOverloading.lowerEqualsOperator(leftValue, rightValue, operator, type, getFileName(),
					getLine());
		case ">=":
			return OperatorOverloading.greaterEqualsOperator(leftValue, rightValue, operator, type, getFileName(),
					getLine());
		case "!=":
			return OperatorOverloading.notEqualsOperator(leftValue, rightValue, operator, type, getFileName(),
					getLine());
		case "=":
			return OperatorOverloading.assignmentOperator(leftValue, rightValue, operator, type, getFileName(),
					getLine());
		case "+=":
			return OperatorOverloading.assignmentAdditionOperator(leftValue, rightValue, operator, type, getFileName(),
					getLine());
		case "-=":
			return OperatorOverloading.assignmentSubtractionOperator(leftValue, rightValue, operator, type,
					getFileName(), getLine());
		case "*=":
			return OperatorOverloading.assignmentMultiplicationOperator(leftValue, rightValue, operator, type,
					getFileName(), getLine());
		case "/=":
			return OperatorOverloading.assignmentDivisionOperator(leftValue, rightValue, operator, type, getFileName(),
					getLine());
		case "<<=":
			return OperatorOverloading.assignmentShiftLeftOperator(leftValue, rightValue, operator, type, getFileName(),
					getLine());
		case ">>=":
			return OperatorOverloading.assignmentShiftRightOperator(leftValue, rightValue, operator, type,
					getFileName(), getLine());
		case "^=":
			return OperatorOverloading.assignmentXorOperator(leftValue, rightValue, operator, type, getFileName(),
					getLine());
		case "&=":
			return OperatorOverloading.assignmentAndOperator(leftValue, rightValue, operator, type, getFileName(),
					getLine());
		case "|=":
			return OperatorOverloading.assignmentOrOperator(leftValue, rightValue, operator, type, getFileName(),
					getLine());
		}
		return null;
	}

	private DataTypes getDataType(Object expression) {
		// Returns data type of expression.
		if (expression instanceof Node && ((Node) expression).getNodeType().equals(NodeTypes.VARIABLE_DECLARATION))
			return ((VariableDeclarationNode) expression).getType();
		return DataTypes.getDataType(expression);
	}

	public OperationNode getLeftOperand() {
		return left;
	}

	private Object getLiteral(Object expression, Block parent) {
		// Returns value of expression.
		if (expression instanceof Node)
			switch (((Node) expression).getNodeType()) {
			case VARIABLE:
				var variableCall = (VariableNode) expression;
				var nextV = variableCall.getNext();
				var variable = parent.getVariableByName(variableCall.getName(), variableCall.getFileName(),
						variableCall.getLine());
				if (nextV != null) {
					var variableValue = variable.getValue();
					if (variableValue instanceof StructDeclarationNode)
						return getLiteral(nextV.getOperand(), (StructDeclarationNode) variableValue);
					else
						new LogError("Can't get attributes from simple data type", variableCall.getFileName(),
								variableCall.getLine());
				}
				return variable;
			case FUNCTION_CALL:
				var functionToCall = ((FunctionCallNode) expression);
				var function = parent.getFunctionByName(functionToCall.getName(), functionToCall.getFileName(),
						functionToCall.getLine());
				var functionCallValue = function.call(functionToCall.getArguments(), functionToCall.getFileName(),
						functionToCall.getLine());
				var nextF = functionToCall.getNext();
				if (nextF != null)
					if (functionCallValue instanceof StructDeclarationNode)
						return getLiteral(nextF.getOperand(), (StructDeclarationNode) functionCallValue);
					else
						new LogError("Can't get attributes from simple data type", functionToCall.getFileName(),
								functionToCall.getLine());

				return functionCallValue;
			case CONSTANT:
				var cn = ((ConstantNode) expression);
				return cn.getValue();
			default:
				return null;
			}
		else
			return expression;
	}

	private Object getLiteral(Object expression) {
		return getLiteral(expression, parent);
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

	@Override
	public Object run() {
		// Returns calculated value.
		var operand = getOperand();
		if (!(operand instanceof String)) {
			var literal = getLiteral(operand);
			if (literal instanceof VariableDeclarationNode) {
				return ((VariableDeclarationNode) literal).getValue();
			} else {
				return literal;
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
		if (!(getOperand() instanceof String))
			return getOperand();
		Object a = getLeftOperand().solve();
		Object b = getRightOperand().solve();
		Object operator = getOperand();
		var isComparison = operator.toString().equals("==") || operator.toString().equals("!=")
				|| operator.toString().equals("<=") || operator.toString().equals(">=")
				|| operator.toString().equals(">") || operator.toString().equals("<");
		Object leftValue = getLiteral(a);
		Object rightValue = getLiteral(b);
		DataTypes leftType = getDataType(leftValue);
		DataTypes rightType = getDataType(rightValue);
		if (!leftType.equals(rightType))
			new LogError("Type mismatch:\t" + leftType + " and " + rightType, getFileName(), getLine());

		if (!operator.toString().contains("=") || isComparison)
			if (leftValue instanceof VariableDeclarationNode)
				leftValue = ((VariableDeclarationNode) leftValue).getValue();
		if (rightValue instanceof VariableDeclarationNode)
			rightValue = ((VariableDeclarationNode) rightValue).getValue();
		return calculate(leftValue, rightValue, getOperand(), leftType);

	}

	public void setParent(Block parent) {
		this.parent = parent;
	}
}