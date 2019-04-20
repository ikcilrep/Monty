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

import java.math.BigInteger;

import ast.Block;
import ast.NodeWithParent;
import ast.declarations.VariableDeclarationNode;
import parser.DataTypes;
import parser.LogError;
import sml.casts.ToFloat;

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

	private final Object calculate(Object leftValue, Object rightValue, String operator, DataTypes type) {
		// Calculates the result of math operation.
		switch (operator) {
		case "+":
			return OperatorOverloading.additionOperator(leftValue, rightValue, type);
		case "-":
			return OperatorOverloading.subtractionOperator(leftValue, rightValue, type);
		case "*":
			return OperatorOverloading.multiplicationOperator(leftValue, rightValue, type);
		case "/":
			return OperatorOverloading.divisionOperator(leftValue, rightValue, type);
		case "%":
			return OperatorOverloading.moduloOperator(leftValue, rightValue, type);
		case "!":
			return OperatorOverloading.negationOperator(rightValue, type);
		case "<<":
			return OperatorOverloading.shiftLeftOperator(leftValue, rightValue, type);
		case ">>":
			return OperatorOverloading.shiftRightOperator(leftValue, rightValue, type);
		case "^":
			return OperatorOverloading.xorOperator(leftValue, rightValue, type);
		case "&":
			return OperatorOverloading.andOperator(leftValue, rightValue, type);
		case "|":
			return OperatorOverloading.orOperator(leftValue, rightValue, type);
		case "==":
			return OperatorOverloading.equalsOperator(leftValue, rightValue);
		case ">":
			return OperatorOverloading.greaterOperator(leftValue, rightValue, type);
		case "<":
			return OperatorOverloading.lowerOperator(leftValue, rightValue, type);
		case "<=":
			return OperatorOverloading.lowerEqualsOperator(leftValue, rightValue, type);
		case ">=":
			return OperatorOverloading.greaterEqualsOperator(leftValue, rightValue, type);
		case "!=":
			return OperatorOverloading.notEqualsOperator(leftValue, rightValue);
		case "=":
			return OperatorOverloading.assignmentOperator(leftValue, rightValue, type);
		case "+=":
			return OperatorOverloading.assignmentAdditionOperator(leftValue, rightValue, type);
		case "-=":
			return OperatorOverloading.assignmentSubtractionOperator(leftValue, rightValue, type);
		case "*=":
			return OperatorOverloading.assignmentMultiplicationOperator(leftValue, rightValue, type);
		case "/=":
			return OperatorOverloading.assignmentDivisionOperator(leftValue, rightValue, type);
		case "<<=":
			return OperatorOverloading.assignmentShiftLeftOperator(leftValue, rightValue, type);
		case ">>=":
			return OperatorOverloading.assignmentShiftRightOperator(leftValue, rightValue, type);
		case "^=":
			return OperatorOverloading.assignmentXorOperator(leftValue, rightValue, type);
		case "&=":
			return OperatorOverloading.assignmentAndOperator(leftValue, rightValue, type);
		case "|=":
			return OperatorOverloading.assignmentOrOperator(leftValue, rightValue, type);
		}
		return null;
	}

	private final Object getLiteral(Object expression, String fileName, int line) {
		return getLiteral(expression, parent, fileName, line);
	}

	final static Object getLiteral(Object expression, Block parent, String fileName, int line) {
		// Returns value of expression.
		if (expression instanceof VariableNode)
			return parent.getVariable(((VariableNode) expression).getName(), fileName, line);
		else if (expression instanceof FunctionCallNode) {
			var functionToCall = ((FunctionCallNode) expression);
			return parent.getFunction(functionToCall.getName(), fileName, line).call(functionToCall.getArguments(),
					fileName, line);
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
			if (literal instanceof VariableDeclarationNode)
				return ((VariableDeclarationNode) literal).getValue();
			else
				return literal;
		}
		var value = solve();
		if (value instanceof VariableDeclarationNode)
			return ((VariableDeclarationNode) value).getValue();
		return value;
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
			for (var argument : arguments)
				argument.setParent(parent);
		}
	}

	public final void setRightOperand(OperationNode right) {
		this.right = right;
	}

	private final Object solve() {
		if (!(operand instanceof String))
			return operand;
		OperatorOverloading.setTemporary(fileName, line);

		var operator = (String) operand;
		var isComparison = operator.equals("==") || operator.equals("!=") || operator.equals("<=")
				|| operator.equals(">=") || operator.equals(">") || operator.equals("<");

		var isNotAssignment = !operator.contains("=") || isComparison;
		var a = getLeft().solve();

		var leftValue = getLiteral(a, left.fileName, left.line);
		if (isNotAssignment && leftValue instanceof VariableDeclarationNode)
			leftValue = ((VariableDeclarationNode) leftValue).getValue();

		var leftType = DataTypes.getDataType(leftValue);
		if (leftType != null && leftType.equals(DataTypes.BOOLEAN)) {
			if (operator.equals("&"))
				return OperatorOverloading.booleanAndOperator((boolean) leftValue, right);
			else if (operator.equals("|"))
				return OperatorOverloading.booleanOrOperator((boolean) leftValue, right);
		}
		var b = getRight().solve();

		if (operator.equals(".")) {
			if (!(b instanceof NamedExpression))
				new LogError("Variable or function can only be getted from struct.", fileName, line);
			return OperatorOverloading.dotOperator(leftValue, b, leftType, parent);
		} else if (operator.equals("instanceof")) {
			if (!(b instanceof NamedExpression))
				new LogError("Right value have to be type name.", fileName, line);
			return OperatorOverloading.instanceOfOperator(leftValue, b, leftType, parent);
		}

		var rightValue = getLiteral(b, right.fileName, right.line);

		if (rightValue instanceof VariableDeclarationNode)
			rightValue = ((VariableDeclarationNode) rightValue).getValue();
		var rightType = DataTypes.getDataType(rightValue);
		if (!isNotAssignment && (leftType == null || leftType == DataTypes.VOID))
			leftType = rightType;
		if (!(operator.equals("==") || operator.equals("!="))) {
			if (!leftType.equals(rightType)) {
				switch (leftType) {
				case INTEGER:
					switch (rightType) {
					case FLOAT:
						leftType = DataTypes.FLOAT;
						leftValue = Double.valueOf((int) leftValue);
						break;
					case BIG_INTEGER:
						leftType = DataTypes.BIG_INTEGER;
						if (isNotAssignment)
							leftValue = BigInteger.valueOf((int) leftValue);
						else {
							var variable = (VariableDeclarationNode) leftValue;
							variable.setValue(BigInteger.valueOf((int) variable.getValue()), fileName, line);
						}
						break;
					default:
						break;
					}
				case BIG_INTEGER:
					switch (rightType) {
					case FLOAT:
						leftType = DataTypes.FLOAT;
						if (isNotAssignment) {
							leftValue = ToFloat.toReal(leftValue, fileName, line);
						} else {
							var variable = (VariableDeclarationNode) leftValue;
							variable.setValue(ToFloat.toReal(variable.getValue(), fileName, line));
						}
						break;
					case INTEGER:
						rightType = DataTypes.BIG_INTEGER;
						rightValue = BigInteger.valueOf((int) rightValue);
						break;
					default:
						break;
					}
					break;
				case FLOAT:
					switch (rightType) {
					case BIG_INTEGER:
						rightType = DataTypes.FLOAT;
						rightValue = ((BigInteger) leftValue).doubleValue();
						break;
					case INTEGER:
						rightType = DataTypes.FLOAT;
						rightValue = Double.valueOf((int) rightValue);
						break;
					default:
						break;
					}
					break;
				case STRING:
					if (operator.contains("+")) {
						rightType = DataTypes.STRING;
						rightValue = rightValue.toString();
					}
					break;
				case ANY:
					rightType = DataTypes.ANY;
					break;
				default:
					break;
				}
			}
			if (!leftType.equals(rightType)) {
				if (rightType.equals(DataTypes.ANY))
					leftType = DataTypes.ANY;
				else if (isNotAssignment && operator.equals("+") && rightType.equals(DataTypes.STRING)) {
					leftType = DataTypes.STRING;
					leftValue = leftValue.toString();
				} else {
					System.out.println(operator);
					System.out.println(leftValue);
					System.out.println(rightValue);
					new LogError("Type mismatch:\t" + leftType.toString().toLowerCase() + " and "
							+ rightType.toString().toLowerCase(), fileName, line);}
			}
		} else if (!leftType.equals(rightType))
			if (leftType.equals(DataTypes.INTEGER) && rightType.equals(DataTypes.BIG_INTEGER)) {
				leftValue = BigInteger.valueOf((int) leftValue);
				leftType = DataTypes.BIG_INTEGER;
			} else if (rightType.equals(DataTypes.INTEGER) && leftType.equals(DataTypes.BIG_INTEGER)) {
				rightValue = BigInteger.valueOf((int) rightValue);
				rightType = DataTypes.BIG_INTEGER;
			}

		return calculate(leftValue, rightValue, operator, leftType);

	}

	public void setOperand(Object operand) {
		this.operand = operand;
	}

	@Override
	public final OperationNode copy() {
		try {
			var copied = (OperationNode) clone();
			var operand = getOperand();
			if (operand instanceof FunctionCallNode)
				copied.setOperand(((FunctionCallNode) operand).copy());
			var right = getRight();
			if (right != null)
				copied.setRight(right.copy());
			var left = getLeft();
			if (left != null)
				copied.setLeft(left.copy());

			return copied;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	public OperationNode getLeft() {
		return left;
	}

	public void setLeft(OperationNode left) {
		this.left = left;
	}

	public OperationNode getRight() {
		return right;
	}

	public void setRight(OperationNode right) {
		this.right = right;
	}
}