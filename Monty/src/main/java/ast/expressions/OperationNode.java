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
import sml.casts.ToReal;

public final class OperationNode extends NodeWithParent implements Cloneable {

	private OperationNode left = null;
	private Object operand;
	public Object getOperand() {
		return operand;
	}

	private Block parent = null;

	private OperationNode right = null;

	public OperationNode(Object operand, Block parent) {
		this.operand = operand;
		this.parent = parent;
	}

	private final Object calculate(Object leftValue, Object rightValue, Object operator, DataTypes type) {
		// Calculates the result of math operation.
		OperatorOverloading.setTemporary(fileName, line);
		switch (operator.toString()) {
		case "+":
			return OperatorOverloading.additionOperator(leftValue, rightValue, type);
		case "-":
			return OperatorOverloading.subtractionOperator(leftValue, rightValue, type);
		case "*":
			return OperatorOverloading.multiplicationOperator(leftValue, rightValue, type);
		case "/":
			return OperatorOverloading.divisionOperator(leftValue, rightValue, type );
		case "%":
			return OperatorOverloading.moduloOperator(leftValue, rightValue, type);
		case "!":
			return OperatorOverloading.negationOperator(rightValue, type);
		case "<<":
			return OperatorOverloading.shiftLeftOperator(leftValue, rightValue, type);
		case ">>":
			return OperatorOverloading.shiftRightOperator(leftValue, rightValue, type);
		case "^":
			return OperatorOverloading.xorOperator(leftValue, rightValue, type );
		case "&":
			return OperatorOverloading.andOperator(leftValue, rightValue, type );
		case "|":
			return OperatorOverloading.orOperator(leftValue, rightValue, type);
		case "==":
			return OperatorOverloading.equalsOperator(leftValue, rightValue, type);
		case ">":
			return OperatorOverloading.greaterOperator(leftValue, rightValue, type);
		case "<":
			return OperatorOverloading.lowerOperator(leftValue, rightValue, type);
		case "<=":
			return OperatorOverloading.lowerEqualsOperator(leftValue, rightValue, type);
		case ">=":
			return OperatorOverloading.greaterEqualsOperator(leftValue, rightValue, type);
		case "!=":
			return OperatorOverloading.notEqualsOperator(leftValue, rightValue, type);
		case "=":
			return OperatorOverloading.assignmentOperator(leftValue, rightValue, type);
		case "+=":
			return OperatorOverloading.assignmentAdditionOperator(leftValue, rightValue, type
					);
		case "-=":
			return OperatorOverloading.assignmentSubtractionOperator(leftValue, rightValue, type
					);
		case "*=":
			return OperatorOverloading.assignmentMultiplicationOperator(leftValue, rightValue, type
					);
		case "/=":
			return OperatorOverloading.assignmentDivisionOperator(leftValue, rightValue, type
					);
		case "<<=":
			return OperatorOverloading.assignmentShiftLeftOperator(leftValue, rightValue, type
					);
		case ">>=":
			return OperatorOverloading.assignmentShiftRightOperator(leftValue, rightValue, type
					);
		case "^=":
			return OperatorOverloading.assignmentXorOperator(leftValue, rightValue, type);
		case "&=":
			return OperatorOverloading.assignmentAndOperator(leftValue, rightValue, type);
		case "|=":
			return OperatorOverloading.assignmentOrOperator(leftValue, rightValue, type);
		}
		return null;
	}

	private final DataTypes getDataType(Object expression) {
		// Returns data type of expression.
		if (expression instanceof VariableDeclarationNode)
			return ((VariableDeclarationNode) expression).getType();
		return DataTypes.getDataType(expression);
	}

	private final Object getLiteral(Object expression, String fileName, int line) {
		return getLiteral(expression, parent, fileName, line);
	}

	private final Object getLiteral(Object expression, Block parent, String fileName, int line) {
		// Returns value of expression.
		if (expression instanceof VariableNode) {
			var variableCall = (VariableNode) expression;
			var nextV = variableCall.getNext();
			var variable = parent.getVariable(variableCall.getName(), fileName, line);

			if (nextV != null) {
				var variableValue = variable.getValue();
				if (variableValue instanceof StructDeclarationNode)
					return getLiteral(nextV.operand, (StructDeclarationNode) variableValue, nextV.fileName, nextV.line);
				else
					new LogError("Can't get attributes from simple data type.", fileName, line);
			}
			return variable;
		} else if (expression instanceof FunctionCallNode) {
			var functionToCall = ((FunctionCallNode) expression);
			var function = parent.getFunction(functionToCall.getName(), fileName, line);
			var functionCallValue = function.call(functionToCall.getArguments(), fileName, line);
			var nextF = functionToCall.getNext();
			if (nextF != null)
				if (functionCallValue instanceof StructDeclarationNode)
					return getLiteral(nextF.operand, (StructDeclarationNode) functionCallValue, nextF.fileName,
							nextF.line);
				else
					new LogError("Can't get attributes from simple data type.", fileName, line);
			return functionCallValue;
		} else if (expression instanceof ConstantNode)
			return ((ConstantNode) expression).getValue();
		return expression;
	}

	public final Block getParent() {
		return parent;
	}

	@Override
	public final Object run() {
		// Returns calculated value.
		if (!(operand instanceof String)) {
			var literal = getLiteral(operand, fileName, line);
			if (literal instanceof VariableDeclarationNode) {
				return ((VariableDeclarationNode) literal).getValue();
			} else {
				return literal;
			}
		}
		return solve();
	}

	public final void setLeftOperand(OperationNode left) {
		this.left = left;
	}

	@Override
	public final void setParent(Block parent) {
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
		}
		if (operand instanceof StructContainer) {
			var next = ((StructContainer) operand).getNext();
			if (next != null)
				next.setParent(parent);
		}
	}

	public final void setRightOperand(OperationNode right) {
		this.right = right;
	}

	private final Object solve() {
		if (!(operand instanceof String))
			return operand;
		Object a = left.solve();
		Object b = right.solve();
		Object operator = operand;
		var isComparison = operator.toString().equals("==") || operator.toString().equals("!=")
				|| operator.toString().equals("<=") || operator.toString().equals(">=")
				|| operator.toString().equals(">") || operator.toString().equals("<");
		Object leftValue = getLiteral(a, left.fileName, left.line);
		Object rightValue = getLiteral(b, right.fileName, right.line);
		DataTypes leftType = getDataType(leftValue);
		DataTypes rightType = getDataType(rightValue);
		var isNotAssignment = !operator.toString().contains("=") || isComparison;
		if (isNotAssignment)
			if (leftValue instanceof VariableDeclarationNode)
				leftValue = ((VariableDeclarationNode) leftValue).getValue();
		if (rightValue instanceof VariableDeclarationNode)
			rightValue = ((VariableDeclarationNode) rightValue).getValue();

		if (leftType.equals(DataTypes.ANY) && !(leftValue instanceof StructDeclarationNode)
				&& !(leftValue instanceof VariableDeclarationNode))
			leftType = DataTypes.getDataType(leftValue);
		if (rightType.equals(DataTypes.ANY) && !(rightValue instanceof StructDeclarationNode))
			rightType = DataTypes.getDataType(rightValue);

		if (isNotAssignment && (leftType.equals(DataTypes.INTEGER) && rightType.equals(DataTypes.REAL))) {
			leftType = DataTypes.REAL;
			leftValue = ToReal.toReal(leftValue, fileName, line);
		} else if (isNotAssignment && operator.equals("+")
				&& (rightType.equals(DataTypes.STRING) && !rightType.equals(leftType))) {
			leftType = DataTypes.STRING;
			leftValue = leftValue.toString();
		} else if (leftType.equals(DataTypes.REAL) && rightType.equals(DataTypes.INTEGER)) {
			rightType = DataTypes.REAL;
			rightValue = ToReal.toReal(rightValue, fileName, line);
		} else if (operator.toString().contains("+") && leftType.equals(DataTypes.STRING)
				&& !leftType.equals(rightType)) {
			rightType = DataTypes.STRING;
			rightValue = rightValue.toString();
		} else if (!isNotAssignment && leftType.equals(DataTypes.ANY) && !leftType.equals(rightType))
			rightType = DataTypes.ANY;
		else if (rightType.equals(DataTypes.ANY) && !leftType.equals(rightType))
			leftType = DataTypes.ANY;
		if (!leftType.equals(rightType))
			new LogError("Type mismatch:\t" + leftType + " and " + rightType, fileName, line);

		return calculate(leftValue, rightValue, operand, leftType);

	}

	public void setOperand(Object operand) {
		this.operand = operand;
	}

	@Override
	public final OperationNode copy() {
		try {
			return (OperationNode) clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
}