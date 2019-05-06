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
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import parser.DataTypes;
import parser.LogError;
import sml.casts.ToFloat;
import sml.casts.ToInt;
import sml.data.tuple.Tuple;

import java.awt.image.AreaAveragingScaleFilter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public final class OperationNode extends NodeWithParent implements Cloneable {
    private OperationNode left;
    private final Object operand;
    private Block parent;
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


            var variableOrFunction = parent.getVariableOrFunction(((IdentifierNode) expression).getName());
            if (doesGetValueFromVariable && variableOrFunction instanceof VariableDeclarationNode)
                return ((VariableDeclarationNode) variableOrFunction).getValue();
            return variableOrFunction;
        } else if (expression instanceof ConstantNode)
            return ((ConstantNode) expression).getValue();
        return expression;
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
        var copied = new OperationNode(operand, parent, getFileName(), getLine());
        if (right != null)
            copied.setRight(right.copy());
        if (left != null)
            copied.setLeft(left.copy());
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
        if (!(operand instanceof String || operand instanceof IdentifierNode)) {
            OperatorOverloading.setTemporary(fileName, line);
            result = operand;
        }  else
            result = solve(parent);
        result = getLiteral(result,parent, doesGetVariableValue, fileName, line);
        if (result instanceof ArrayList)
            return new Tuple(((ArrayList) result).toArray());
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


    private Object solve(Block parent) {
        var fileName = getFileName();
        var line = getLine();
        if (operand instanceof IdentifierNode && ((IdentifierNode) operand).isFunctionCall()) {
            var value = getLiteral(operand,parent, true, fileName, line);
            if (right == null)
                return value;

            if (value instanceof FunctionDeclarationNode) {

                    var arguments = right.runWithParent(parent);
                    if (!(arguments instanceof Tuple))
                        arguments = new Tuple(arguments);
                    return ((FunctionDeclarationNode) value).call((Tuple) arguments, fileName, line);
                    
            }
        }else if (operand.equals(""))
            return right.runWithParent(parent);
        else if (!(operand instanceof String))
            return operand;
        OperatorOverloading.setTemporary(fileName, line);

        var operator = (String) operand;
        var isComparison = operator.equals("==") || operator.equals("!=") || operator.equals("<=")
                || operator.equals(">=") || operator.equals(">") || operator.equals("<");

        var isAssignment = operator.contains("=") && !isComparison;
        var isDot = operator.equals(".");
        var leftFileName = left.getFileName();
        var leftLine = left.getLine();
        var leftValue = getLiteral(left.solve(parent),parent, !isAssignment, leftFileName, leftLine);


        if (operator.equals(",")) {
            var rightValue = getLiteral(right.solve(parent), parent,true, leftFileName, leftLine);
            if (leftValue instanceof ArrayList) {
                ((ArrayList<Object>) leftValue).add(rightValue);
                return leftValue;
            }
            if (rightValue instanceof ArrayList) {
                ((ArrayList<Object>) rightValue).add(0, leftValue);
                return rightValue;
            }
            return new ArrayList<>(List.of(leftValue,rightValue));
        }

        if (leftValue instanceof ArrayList)
            return new Tuple(((ArrayList) leftValue).toArray());

        var leftType = DataTypes.getDataType(leftValue);
        if (leftType != null && leftType.equals(DataTypes.BOOLEAN)) {
            if (operator.equals("&"))
                return OperatorOverloading.booleanAndOperator((boolean) leftValue, right);
            else if (operator.equals("|"))
                return OperatorOverloading.booleanOrOperator((boolean) leftValue, right);
        }



        if (isDot) {
            if (!(right.operand instanceof IdentifierNode))
                new LogError("Variable or function can only be got from struct.", fileName, line);
            return OperatorOverloading.dotOperator(leftValue, right, leftType);
        }
        var b = right.solve(parent);

        if (operator.equals("instanceof")) {
            if (!(b instanceof IdentifierNode))
                new LogError("Right value have to be type name.", fileName, line);
            return OperatorOverloading.instanceOfOperator(leftValue, b, leftType, parent);
        }

        var rightValue = getLiteral(b,parent, true, right.getFileName(), right.getLine());
        if (rightValue instanceof ArrayList)
            return new Tuple(((ArrayList) rightValue).toArray());
        var rightType = DataTypes.getDataType(rightValue);


        if (operator.equals("=")) {
            leftType = rightType;
        }
        if (!(operator.equals("==") || operator.equals("!="))) {
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


        return calculate(leftValue, rightValue, operator, leftType);

    }
}