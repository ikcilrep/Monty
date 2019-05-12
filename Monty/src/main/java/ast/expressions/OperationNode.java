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
import ast.Operator;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import parser.DataTypes;
import parser.LogError;
import sml.casts.ToFloat;
import sml.casts.ToInt;
import sml.data.tuple.Tuple;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import static ast.Operator.*;

public final class OperationNode extends NodeWithParent {
    private OperationNode left;

    public Object getOperand() {
        return operand;
    }

    private final Object operand;
    private Block parent;

    public OperationNode getRight() {
        return right;
    }

    private OperationNode right;
    public OperationNode(Object operand, Block parent) {
        this.operand = operand;
        this.parent = parent;
    }

    private OperationNode(Object operand, Block parent, String fileName, int line) {
        this.operand = operand;
        this.parent = parent;
        setFileName(fileName);
        setLine(line);
    }

    static Object getVariableValue(Object canBeVariable) {
        if (canBeVariable instanceof  VariableDeclarationNode)
            return ((VariableDeclarationNode) canBeVariable).getValue();
        return canBeVariable;
    }

    private static Object getLiteral(Object expression, Block parent,
                                     boolean doesGetValueFromVariable, String fileName, int line) {
        // Returns value of expression.
        if (expression instanceof IdentifierNode) {
            var variableNode = ((IdentifierNode) expression);
            if (variableNode.isFunctionCall())
                return parent.getFunction(variableNode.getName(), fileName, line);
            var variableOrFunction = parent.getVariableOrFunction(((IdentifierNode) expression).getName(), fileName, line);
            if (doesGetValueFromVariable && variableOrFunction instanceof VariableDeclarationNode)
                return ((VariableDeclarationNode) variableOrFunction).getValue();
            return variableOrFunction;
        }
        return expression;
    }

    private Object calculate(Object leftValue, Object rightValue, Operator operator, DataTypes type, String fileName, int line) {
        // Calculates the result of math operation.
        switch (operator) {
            case ADDITION:
                return OperatorOverloading.additionOperator(leftValue, rightValue, type, fileName, line);
            case SUBTRACTION:
                return OperatorOverloading.subtractionOperator(leftValue, rightValue, type, fileName, line);
            case MULTIPLICATION:
                return OperatorOverloading.multiplicationOperator(leftValue, rightValue, type, fileName, line);
            case POWER:
                return OperatorOverloading.powerOperator(leftValue, rightValue, type, fileName, line);
            case DIVISION:
                return OperatorOverloading.divisionOperator(leftValue, rightValue, type, fileName, line);
            case MODULO:
                return OperatorOverloading.moduloOperator(leftValue, rightValue, type, fileName, line);
            case SHIFT_LEFT:
                return OperatorOverloading.shiftLeftOperator(leftValue, rightValue, type, fileName, line);
            case SHIFT_RIGHT:
                return OperatorOverloading.shiftRightOperator(leftValue, rightValue, type, fileName, line);
            case XOR:
                return OperatorOverloading.xorOperator(leftValue, rightValue, type, fileName, line);
            case AND:
                return OperatorOverloading.andOperator(leftValue, rightValue, type, fileName, line);
            case OR:
                return OperatorOverloading.orOperator(leftValue, rightValue, type, fileName, line);
            case EQUALS:
                return OperatorOverloading.equalsOperator(leftValue, rightValue, type, fileName, line);
            case GREATER_THAN:
                return OperatorOverloading.greaterOperator(leftValue, rightValue, type, fileName, line);
            case LESS_THAN:
                return OperatorOverloading.lowerOperator(leftValue, rightValue, type, fileName, line);
            case LESS_EQUALS:
                return OperatorOverloading.lowerEqualsOperator(leftValue, rightValue, type, fileName, line);
            case GREATER_EQUALS:
                return OperatorOverloading.greaterEqualsOperator(leftValue, rightValue, type, fileName, line);
            case NOT_EQUALS:
                return OperatorOverloading.notEqualsOperator(leftValue, rightValue, type, fileName, line);
            case ASSIGNMENT:
                return OperatorOverloading.assignmentOperator(leftValue, rightValue, type, fileName, line);
            case ASSIGNMENT_ADDITION:
                return OperatorOverloading.assignmentAdditionOperator(leftValue, rightValue, type, fileName, line);
            case ASSIGNMENT_SUBTRACTION:
                return OperatorOverloading.assignmentSubtractionOperator(leftValue, rightValue, type, fileName, line);
            case ASSIGNMENT_POWER:
                return OperatorOverloading.assignmentPowerOperator(leftValue, rightValue, type, fileName, line);
            case ASSIGNMENT_MULTIPLICATION:
                return OperatorOverloading.assignmentMultiplicationOperator(leftValue, rightValue, type, fileName, line);
            case ASSIGNMENT_DIVISION:
                return OperatorOverloading.assignmentDivisionOperator(leftValue, rightValue, type, fileName, line);
            case ASSIGNMENT_MODULO:
                return OperatorOverloading.assignmentModuloOperator(leftValue, rightValue, type, fileName, line);
            case ASSIGNMENT_SHIFT_LEFT:
                return OperatorOverloading.assignmentShiftLeftOperator(leftValue, rightValue, type, fileName, line);
            case ASSIGNMENT_SHIFT_RIGHT:
                return OperatorOverloading.assignmentShiftRightOperator(leftValue, rightValue, type, fileName, line);
            case ASSIGNMENT_XOR:
                return OperatorOverloading.assignmentXorOperator(leftValue, rightValue, type, fileName, line);
            case ASSIGNMENT_AND:
                return OperatorOverloading.assignmentAndOperator(leftValue, rightValue, type, fileName, line);
            case ASSIGNMENT_OR:
                return OperatorOverloading.assignmentOrOperator(leftValue, rightValue, type, fileName, line);
        }
        return null;
    }
    private Object calculate(Object value, DataTypes type, String fileName, int line) {
        return OperatorOverloading.negationOperator(value, type, fileName, line);
    }

    @Override
    public final OperationNode copy() {
        var copied = new OperationNode(operand, parent, getFileName(), getLine());
        if (right != null)
            copied.right = right.copy();
        if (left != null)
            copied.left = left.copy();
        return copied;
    }



    @Override
    public final void setParent(Block parent) {
        this.parent = parent;
        if (left != null)
            left.setParent(parent);
        if (right != null)
            right.setParent(parent);
    }


    final Object runWithParent(Block parent, boolean doesGetVariableValue) {
        // Returns calculated value.
        var fileName = getFileName();
        var line = getLine();
        Object result;
        if (!(operand instanceof Operator || operand instanceof IdentifierNode)) {
            result = operand;
        }  else
            result = solve(parent);
        result = getLiteral(result,parent, doesGetVariableValue, fileName, line);
        if (result instanceof LinkedList)
            return new Tuple((LinkedList) result);
        else if (result instanceof VariableDeclarationNode && doesGetVariableValue)
            return ((VariableDeclarationNode) result).getValue();
        return result;
    }
    private Object runWithParent(Block parent) {
        // Returns calculated value.
        return runWithParent(parent,true);
    }
    @Override
    public final Object run() {
        return runWithParent(parent);
    }


    public final void setLeft(OperationNode left) {
        this.left = left;
    }

    public final void setRight(OperationNode right) {
        this.right = right;
    }
    private Object solveUnaryOperator(Block parent, String fileName, int line) {
        var value = getLiteral(right.solve(parent),parent, true, right.getFileName(), right.getLine());
        return calculate(value, DataTypes.getDataType(value), fileName, line);
    }
    @SuppressWarnings("unchecked")
    private Object solveBinaryOperator(Block parent, Operator operator) {
        var fileName = getFileName();
        var line = getLine();
        var isAssignment = operator.isAssignment();
        var isDot = operator.equals(DOT);
        var leftFileName = left.getFileName();
        var leftLine = left.getLine();
        var leftValue = getLiteral(left.solve(this.parent),this.parent, !isAssignment, leftFileName, leftLine);

        if (operator.equals(COMMA)) {
            var rightValue = getLiteral(right.solve(parent), parent,true, leftFileName, leftLine);
            if (leftValue instanceof LinkedList) {
                ((LinkedList<Object>) leftValue).add(rightValue);
                return leftValue;
            }
            if (rightValue instanceof LinkedList) {
                ((LinkedList<Object>) rightValue).addFirst(leftValue);
                return rightValue;
            }
            return new LinkedList<>(List.of(leftValue,rightValue));
        }

        if (leftValue instanceof LinkedList)
            return new Tuple((LinkedList) leftValue);

        var leftType = DataTypes.getDataType(leftValue);
        if (leftType != null && leftType.equals(DataTypes.BOOLEAN)) {
            if (operator.equals(AND))
                return OperatorOverloading.booleanAndOperator((boolean) leftValue, right, fileName, line);
            else if (operator.equals(OR))
                return OperatorOverloading.booleanOrOperator((boolean) leftValue, right, fileName, line);
        }



        if (isDot) {
            if (!(right.operand instanceof IdentifierNode))
                new LogError("Variable or function can only be got from struct.", fileName, line);
            return OperatorOverloading.dotOperator(leftValue, right, leftType,fileName, line);
        }

        var b = right.solve(parent);

        if (operator.equals(INSTANCE_OF)) {
            if (!(b instanceof IdentifierNode))
                new LogError("Right value have to be type name.", fileName, line);
            return OperatorOverloading.instanceOfOperator(leftValue, b, leftType, parent,fileName,line);
        }
        var rightValue = getLiteral(b,parent, true, right.getFileName(), right.getLine());
        if (rightValue instanceof LinkedList)
            return new Tuple(((LinkedList) rightValue));
        var rightType = DataTypes.getDataType(rightValue);


        if (operator.equals(ASSIGNMENT)) {
            leftType = rightType;
        }
        if (!(operator.equals(EQUALS) || operator.equals(NOT_EQUALS))) {
            if (!leftType.equals(rightType)) {
                switch (leftType) {
                    case INTEGER:
                        switch (rightType) {
                            case FLOAT:
                                leftType = DataTypes.FLOAT;
                                if (isAssignment)
                                    ToFloat.fromSmallIntVariable((VariableDeclarationNode) leftValue, fileName, line);
                                else
                                    leftValue = ToFloat.fromInt((int) leftValue);
                                break;
                            case BIG_INTEGER:
                                leftType = DataTypes.BIG_INTEGER;
                                if (isAssignment)
                                    ToInt.fromSmallIntVariable((VariableDeclarationNode) leftValue, fileName, line);
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
                                    ToFloat.fromBigIntVariable((VariableDeclarationNode) leftValue, fileName, line);
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
                            + rightType.toString().toLowerCase(), fileName, line);

            }
        } else if (!leftType.equals(rightType))
            if (leftType.equals(DataTypes.INTEGER) && rightType.equals(DataTypes.BIG_INTEGER)) {
                leftValue = BigInteger.valueOf((int) leftValue);
                leftType = DataTypes.BIG_INTEGER;
            } else if (rightType.equals(DataTypes.INTEGER) && leftType.equals(DataTypes.BIG_INTEGER))
                rightValue = BigInteger.valueOf((int) rightValue);


        return calculate(leftValue, rightValue, operator, leftType, fileName, line);
    }

    public static Tuple argumentsToTuple(Object arguments) {
        if (arguments instanceof Tuple)
            return (Tuple)arguments;
        return new Tuple(arguments);
    }

    private Object solveFunction(Block parent) {
        var fileName = getFileName();
        var line = getLine();
        var value = getLiteral(operand, parent, true, fileName, line);

        if (value instanceof FunctionDeclarationNode) {
            if (right.left != null && right.left.operand.equals(JUST))  {
                var newNode = new OperationNode(right.operand, right.parent, getFileName(), getLine());
                newNode.left =  new OperationNode(operand, this.parent, getFileName(), getLine());
                newNode.left.right = right.left;
                newNode.right = right.right;
                return newNode.solve(parent);
            }
            return ((FunctionDeclarationNode) value).call(argumentsToTuple(right.run()), fileName, line);
        }
        return value;
    }


    private Object solve(Block parent) {
        if (operand instanceof IdentifierNode && ((IdentifierNode) operand).isFunctionCall())
            return solveFunction(parent);
        else if (operand.equals(JUST))
            return right.runWithParent(parent);
        else if (!(operand instanceof Operator))
            return operand;
        var operator = (Operator) operand;

        if (operator.equals(NEGATION))
            return solveUnaryOperator(parent, getFileName(), getLine());
        return solveBinaryOperator(parent,operator);
    }
}