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
import ast.declarations.VariableDeclarationNode;
import parser.DataTypes;
import parser.LogError;
import sml.casts.ToFloat;
import sml.casts.ToInt;

import java.math.BigInteger;

public final class OperationNode extends NodeWithParent implements Cloneable {

    static Object getLiteral(Object expression, Block parent, String fileName, int line) {
        // Returns value of expression.
        if (expression instanceof VariableNode)
            return parent.getVariableOrFunction(((VariableNode) expression).getName(), fileName, line);
        else if (expression instanceof FunctionCallNode) {
            var functionToCall = ((FunctionCallNode) expression);
            return parent.getFunction(functionToCall.getName(), fileName, line).call(functionToCall.getArguments(),
                    fileName, line);
        } else if (expression instanceof ConstantNode)
            return ((ConstantNode) expression).getValue();
        return expression;
    }

    private OperationNode left = null;

    private Object operand;

    private Block parent = null;

    private OperationNode right = null;

    public OperationNode(Object operand, Block parent) {
        setOperand(operand);
        setParent(parent);
    }

    private Object calculate(Object leftValue, Object rightValue, String operator, DataTypes type) {
        // Calculates the result of math operation.
        switch (operator) {
            case "+":
                return OperatorOverloading.additionOperator(leftValue, rightValue, type);
            case "-":
                return OperatorOverloading.subtractionOperator(leftValue, rightValue, type);
            case "*":
                return OperatorOverloading.multiplicationOperator(leftValue, rightValue, type);
            case "**":
                return OperatorOverloading.powerOperator(leftValue, rightValue, type);
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
            case "**=":
                return OperatorOverloading.assignmentPowerOperator(leftValue, rightValue, type);
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

    private OperationNode getLeft() {
        return left;
    }

    private Object getLiteral(Object expression, String fileName, int line) {
        return getLiteral(expression, parent, fileName, line);
    }

    public Object getOperand() {
        return operand;
    }

    public final Block getParent() {
        return parent;
    }

    private OperationNode getRight() {
        return right;
    }

    @Override
    public final Object run() {
        // Returns calculated value.
        if (!(operand instanceof String)) {
            OperatorOverloading.setTemporary(getFileName(), getLine());
            var literal = getLiteral(operand, getFileName(), getLine());
            if (literal instanceof VariableDeclarationNode)
                return ((VariableDeclarationNode) literal).getValue();
            else
                return literal;
        }
        return solve();
    }

    private void setLeft(OperationNode left) {
        this.left = left;
    }

    public final void setLeftOperand(OperationNode left) {
        this.left = left;
    }

    void setOperand(Object operand) {
        this.operand = operand;
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

    private void setRight(OperationNode right) {
        this.right = right;
    }

    public final void setRightOperand(OperationNode right) {
        this.right = right;
    }

    private Object solve() {
        var operand = getOperand();
        if (!(operand instanceof String))
            return operand;
        OperatorOverloading.setTemporary(getFileName(), getLine());

        var operator = (String) operand;
        var isComparison = operator.equals("==") || operator.equals("!=") || operator.equals("<=")
                || operator.equals(">=") || operator.equals(">") || operator.equals("<");

        var isAssignment = operator.contains("=") && !isComparison;
        var a = getLeft().solve();

        var leftValue = getLiteral(a, left.getFileName(), left.getLine());
        if (!isAssignment && leftValue instanceof VariableDeclarationNode)
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
                new LogError("Variable or function can only be got from struct.", getFileName(), getLine());
            return OperatorOverloading.dotOperator(leftValue, b, leftType, parent);
        } else if (operator.equals("instanceof")) {
            if (!(b instanceof NamedExpression))
                new LogError("Right value have to be type name.", getFileName(), getLine());
            return OperatorOverloading.instanceOfOperator(leftValue, b, leftType, parent);
        }

        var rightValue = getLiteral(b, right.getFileName(), right.getLine());

        if (rightValue instanceof VariableDeclarationNode)
            rightValue = ((VariableDeclarationNode) rightValue).getValue();
        var rightType = DataTypes.getDataType(rightValue);
        if (operator.equals("="))
            leftType = rightType;
        if (!(operator.equals("==") || operator.equals("!="))) {
            if (!leftType.equals(rightType)) {
                switch (leftType) {
                    case INTEGER:
                        switch (rightType) {
                            case FLOAT:
                                leftType = DataTypes.FLOAT;
                                if (isAssignment)
                                    ToFloat.fromSmallIntVariable((VariableDeclarationNode) leftValue, getFileName(), getLine());
                                else
                                    leftValue = ToFloat.fromInt((int) leftValue);
                                break;
                            case BIG_INTEGER:
                                leftType = DataTypes.BIG_INTEGER;
                                if (isAssignment)
                                    ToInt.fromSmallIntVariable((VariableDeclarationNode) leftValue, getFileName(), getLine());
                                else
                                    leftValue = ToInt.fromSmallInt((int) leftValue);
                                break;
                            default:
                                break;
                        }
                        break;
                    case BIG_INTEGER:
                        switch (rightType) {
                            case FLOAT:
                                leftType = DataTypes.FLOAT;
                                if (!isAssignment)
                                    ToFloat.fromBigIntVariable((VariableDeclarationNode) leftValue, getFileName(), getLine());
                                else
                                    leftValue = ToFloat.fromInt((BigInteger) leftValue);
                                break;
                            case INTEGER:
                                rightType = DataTypes.BIG_INTEGER;
                                rightValue = ToInt.fromSmallInt((int) rightValue);
                                break;
                            default:
                                break;
                        }
                        break;
                    case FLOAT:
                        switch (rightType) {
                            case BIG_INTEGER:
                                rightType = DataTypes.FLOAT;
                                rightValue = ToFloat.fromInt((BigInteger) rightValue);
                                break;
                            case INTEGER:
                                rightType = DataTypes.FLOAT;
                                rightValue = ToFloat.fromInt((int) rightValue);
                                break;
                            default:
                                break;
                        }
                        break;
                    case OBJECT:
                        rightType = DataTypes.OBJECT;
                        break;
                    default:
                        break;
                }
            }
            if (!leftType.equals(rightType)) {
                if (rightType.equals(DataTypes.OBJECT))
                    leftType = DataTypes.OBJECT;
                else
                    new LogError("Type mismatch:\t" + leftType.toString().toLowerCase() + " and "
                            + rightType.toString().toLowerCase(), getFileName(), getLine());

            }
        } else if (!leftType.equals(rightType))
            if (leftType.equals(DataTypes.INTEGER) && rightType.equals(DataTypes.BIG_INTEGER)) {
                leftValue = BigInteger.valueOf((int) leftValue);
                leftType = DataTypes.BIG_INTEGER;
            } else if (rightType.equals(DataTypes.INTEGER) && leftType.equals(DataTypes.BIG_INTEGER))
                rightValue = BigInteger.valueOf((int) rightValue);


        return calculate(leftValue, rightValue, operator, leftType);

    }
}