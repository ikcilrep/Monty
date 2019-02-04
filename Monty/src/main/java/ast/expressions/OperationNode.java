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
import ast.NodeWithParent;
import ast.declarations.StructDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import parser.DataTypes;
import parser.LogError;
import sml.casts.ToFloat;

public class OperationNode extends NodeWithParent implements Cloneable {

	private OperationNode left = null;
	private Object operand;
	private Block parent = null;

	private OperationNode right = null;

	public OperationNode(Object operand, Block parent) {
		this.operand = operand;
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
		if (expression instanceof VariableDeclarationNode)
			return ((VariableDeclarationNode) expression).getType();
		return DataTypes.getDataType(expression);
	}

	public OperationNode getLeftOperand() {
		return left;
	}

	private Object getLiteral(Object expression) {
		return getLiteral(expression, parent);
	}

	private Object getLiteral(Object expression, Block parent) {
		// Returns value of expression.
		if (expression instanceof VariableNode) {
			var variableCall = (VariableNode) expression;
			var nextV = variableCall.getNext();
			var variable = parent.getVariable(variableCall.getName(), variableCall.getFileName(),
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
		} else if (expression instanceof FunctionCallNode) {
			var functionToCall = ((FunctionCallNode) expression);
			var function = parent.getFunction(functionToCall.getName(), functionToCall.getFileName(),
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
		} else if (expression instanceof ConstantNode) {
			var cn = ((ConstantNode) expression);
			return cn.getValue();
		} else
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

	@Override
	public void setParent(Block parent) {
		this.parent = parent;
		if (left != null)
			left.setParent(parent);
		if (right != null)
			right.setParent(parent);
		if (operand instanceof FunctionCallNode) {
			var function = ((FunctionCallNode) operand);
			var arguments = function.getArguments();
			for (OperationNode argument : arguments)
				argument.setParent(parent);
			var next = function.getNext();
			if (next != null)
				next.setParent(parent);
		} else if (operand instanceof VariableNode) {
			var next = ((VariableNode) operand).getNext();
			if (next != null)
				next.setParent(parent);
		}
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
		var isNotAssignment = !operator.toString().contains("=") || isComparison;
		if (isNotAssignment)
			if (leftValue instanceof VariableDeclarationNode)
				leftValue = ((VariableDeclarationNode) leftValue).getValue();
		if (rightValue instanceof VariableDeclarationNode)
			rightValue = ((VariableDeclarationNode) rightValue).getValue();

		if (isNotAssignment && (leftType.equals(DataTypes.INTEGER) && rightType.equals(DataTypes.FLOAT))) {
			leftType = DataTypes.FLOAT;
			leftValue = ToFloat.toFloat(leftValue, getFileName(), getLine());
		} else if (isNotAssignment && (rightType.equals(DataTypes.STRING) && !rightType.equals(leftType))) {
			leftType = DataTypes.STRING;
			leftValue = leftValue.toString();
		} else if (leftType.equals(DataTypes.FLOAT) && rightType.equals(DataTypes.INTEGER)) {
			rightType = DataTypes.FLOAT;
			rightValue = ToFloat.toFloat(rightValue, getFileName(), getLine());
		} else if (leftType.equals(DataTypes.STRING) && !leftType.equals(rightType)) {
			rightType = DataTypes.STRING;
			rightValue = rightValue.toString();
		}

		if (!leftType.equals(rightType))
			new LogError("Type mismatch:\t" + leftType + " and " + rightType, getFileName(), getLine());

		return calculate(leftValue, rightValue, getOperand(), leftType);

	}

	public OperationNode copy() {
		try {
			return (OperationNode) clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
}