/*
Copyright 2018-2019 Szymon Perlicki

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
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.StructDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import parser.DataTypes;
import parser.LogError;
import sml.data.list.List;
import sml.data.string.MontyString;
import sml.data.tuple.Tuple;
import sml.math.Pow;

import java.math.BigInteger;
import java.util.HashMap;

class OperatorOverloading {
    private final static HashMap<String, Class<?>> builtInTypes = new HashMap<>();

    static {
        builtInTypes.put("List", List.class);
        builtInTypes.put("Function", FunctionDeclarationNode.class);
        builtInTypes.put("Integer", null);
        builtInTypes.put("String", MontyString.class);
        builtInTypes.put("Boolean", Boolean.class);
        builtInTypes.put("Float", Double.class);
        builtInTypes.put("Tuple", Tuple.class);

    }

    private static boolean isInstance(Object value, String name) {
        if (name.equals("Integer"))
            return value instanceof Integer || value instanceof BigInteger;
        return builtInTypes.get(name).isInstance(value);
    }

    static Object additionOperator(Object leftValue, Object rightValue, DataTypes type, String fileName, int line) {
        switch (type) {
            case INTEGER:
                var leftInt = (int) leftValue;
                var rightInt = (int) rightValue;
                try {
                    return Math.addExact(leftInt, rightInt);
                } catch (ArithmeticException e) {
                    return BigInteger.valueOf(leftInt).add(BigInteger.valueOf(rightInt));
                }
            case BIG_INTEGER:
                return ((BigInteger) leftValue).add(((BigInteger) rightValue));
            case FLOAT:
                return (double) leftValue + (double) rightValue;
            case BOOLEAN:
                new LogError("Can't add booleans.", fileName, line);
            case OBJECT:
                return overloadOperator(leftValue, rightValue, "$add", 2, fileName, line);
            case NOTHING:
                new LogError("Can't add Nothing.", fileName, line);
            default:
                return null;
        }
    }

    static Object andOperator(Object leftValue, Object rightValue, DataTypes type, String fileName, int line) {
        switch (type) {
            case INTEGER:
                return (int) leftValue & (int) rightValue;
            case BIG_INTEGER:
                return ((BigInteger) leftValue).and((BigInteger) rightValue);
            case FLOAT:
                new LogError("Can't do and operation with " + type.toString().toLowerCase() + "s", fileName,
                        line);
            case OBJECT:
                return overloadOperator(leftValue, rightValue, "$and", 2, fileName, line);
            case NOTHING:
                new LogError("Can't do and operation with Nothing.", fileName, line);
            default:
                return null;
        }
    }

    static Object assignmentAdditionOperator(Object leftValue, Object rightValue, DataTypes type, String fileName, int line) {
        if (type.equals(DataTypes.OBJECT))
            return overloadOperator(leftValue, rightValue, "$a_add", 2, fileName, line);
        var variable = VariableDeclarationNode.toMe(leftValue, fileName, line);
        switch (type) {
            case INTEGER:
            case BIG_INTEGER:
            case FLOAT:
                variable.setValue(additionOperator(variable.getValue(), rightValue, type, fileName, line), fileName,
                        line);
                return variable.getValue();
            case BOOLEAN:
                new LogError("Can't add booleans.", fileName, line);
            case NOTHING:
                new LogError("Can't add Nothing.", fileName, line);
            default:
                return null;
        }
    }

    static Object assignmentAndOperator(Object leftValue, Object rightValue, DataTypes type, String fileName, int line) {
        if (type.equals(DataTypes.OBJECT))
            return overloadOperator(leftValue, rightValue, "$a_and", 2, fileName, line);
        var variable = VariableDeclarationNode.toMe(leftValue, fileName, line);

        switch (type) {
            case INTEGER:
            case BIG_INTEGER:
            case FLOAT:
                variable.setValue(andOperator(variable.getValue(), rightValue, type, fileName, line), fileName, line);
                return variable.getValue();
            case NOTHING:
                new LogError("Can't do and operation with Nothing.", fileName, line);
            default:
                return null;
        }
    }

    static Object assignmentDivisionOperator(Object leftValue, Object rightValue, DataTypes type, String fileName, int line) {
        if (type.equals(DataTypes.OBJECT))
            return overloadOperator(leftValue, rightValue, "$a_div", 2, fileName, line);

        var variable = VariableDeclarationNode.toMe(leftValue, fileName, line);
        switch (type) {
            case INTEGER:
            case BIG_INTEGER:
            case FLOAT:
                variable.setValue(divisionOperator(variable.getValue(), rightValue, type, fileName, line), fileName,
                        line);
                return variable.getValue();
            case NOTHING:
                new LogError("Can't divide Nothing.", fileName, line);
            default:
                return null;
        }
    }

    static Object assignmentMultiplicationOperator(Object leftValue, Object rightValue, DataTypes type, String fileName, int line) {
        if (type.equals(DataTypes.OBJECT))
            return overloadOperator(leftValue, rightValue, "$a_mul", 2, fileName, line);
        var variable = VariableDeclarationNode.toMe(leftValue, fileName, line);

        switch (type) {
            case INTEGER:
            case BIG_INTEGER:
            case FLOAT:
                variable.setValue(multiplicationOperator(variable.getValue(), rightValue, type, fileName, line), fileName,
                        line);
                return variable.getValue();
            case NOTHING:
                new LogError("Can't multiply Nothing.", fileName, line);
            default:
                return null;
        }
    }

    static Object assignmentOperator(Object leftValue, Object rightValue, String fileName, int line) {
        if (!(leftValue instanceof VariableDeclarationNode) && leftValue instanceof Tuple && rightValue instanceof Tuple) {
            var leftTuple = (Tuple) leftValue;
            var rightTuple = (Tuple) rightValue;
            if (leftTuple.length() == rightTuple.length())
                for (int i = 0; i < leftTuple.length(); i++)
                    assignmentOperator(leftTuple.justGet(i), rightTuple.get(i),fileName,line);
            return leftTuple;
        }
        var variable = VariableDeclarationNode.toMe(leftValue, fileName, line);
        variable.setValue(rightValue, fileName, line);
        return variable.getValue();
    }

    static Object assignmentOrOperator(Object leftValue, Object rightValue, DataTypes type, String fileName, int line) {
        if (type.equals(DataTypes.OBJECT))
            return overloadOperator(leftValue, rightValue, "$a_or", 2, fileName, line);
        var variable = VariableDeclarationNode.toMe(leftValue, fileName, line);
        switch (type) {
            case INTEGER:
            case BIG_INTEGER:
            case FLOAT:
                variable.setValue(orOperator(variable.getValue(), rightValue, type, fileName, line), fileName, line);
                return variable.getValue();
            case NOTHING:
                new LogError("Can't do or operation with Nothing.", fileName, line);
            default:
                return null;
        }
    }

    static Object assignmentPowerOperator(Object leftValue, Object rightValue, DataTypes type, String fileName, int line) {
        if (type.equals(DataTypes.OBJECT))
            return overloadOperator(leftValue, rightValue, "$a_pow", 2, fileName, line);
        var variable = VariableDeclarationNode.toMe(leftValue, fileName, line);
        switch (type) {
            case INTEGER:
            case BIG_INTEGER:
            case FLOAT:
                variable.setValue(powerOperator(variable.getValue(), rightValue, type, fileName, line), fileName, line);
                return variable.getValue();
            case NOTHING:
                new LogError("Can't do or operation with Nothing.", fileName, line);
            default:
                return null;
        }
    }

    static Object assignmentShiftLeftOperator(Object leftValue, Object rightValue, DataTypes type, String fileName, int line) {
        if (type.equals(DataTypes.OBJECT))
            return overloadOperator(leftValue, rightValue, "$a_shl", 2, fileName, line);
        var variable = VariableDeclarationNode.toMe(leftValue, fileName, line);
        switch (type) {
            case INTEGER:
            case BIG_INTEGER:
            case FLOAT:
                variable.setValue(shiftLeftOperator(variable.getValue(), rightValue, type, fileName, line), fileName,
                        line);
                return variable.getValue();
            case NOTHING:
                new LogError("Can't shift left Nothing.", fileName, line);
            default:
                return null;
        }
    }

    static Object assignmentShiftRightOperator(Object leftValue, Object rightValue, DataTypes type, String fileName, int line) {
        if (type.equals(DataTypes.OBJECT))
            return overloadOperator(leftValue, rightValue, "$a_shr", 2, fileName, line);
        var variable = VariableDeclarationNode.toMe(leftValue, fileName, line);
        switch (type) {
            case INTEGER:
            case BIG_INTEGER:
            case FLOAT:
                variable.setValue(shiftRightOperator(variable.getValue(), rightValue, type, fileName, line), fileName,
                        line);
                return variable.getValue();
            case NOTHING:
                new LogError("Can't shift right Nothing.", fileName, line);
            default:
                return null;
        }
    }

    static Object assignmentSubtractionOperator(Object leftValue, Object rightValue, DataTypes type, String fileName, int line) {
        if (type.equals(DataTypes.OBJECT))
            return overloadOperator(leftValue, rightValue, "$a_sub", 2, fileName, line);
        var variable = VariableDeclarationNode.toMe(leftValue, fileName, line);
        switch (type) {
            case INTEGER:
            case BIG_INTEGER:
            case FLOAT:
                variable.setValue(subtractionOperator(variable.getValue(), rightValue, type, fileName, line), fileName,
                        line);
                return variable.getValue();
            case NOTHING:
                new LogError("Can't subtract Nothing.", fileName, line);
            default:
                return null;
        }
    }

    static Object assignmentXorOperator(Object leftValue, Object rightValue, DataTypes type, String fileName, int line) {
        if (type.equals(DataTypes.OBJECT))
            return overloadOperator(leftValue, rightValue, "$a_xor", 2, fileName, line);
        var variable = VariableDeclarationNode.toMe(leftValue, fileName, line);
        switch (type) {
            case INTEGER:
            case BIG_INTEGER:
            case FLOAT:
                variable.setValue(xorOperator(variable.getValue(), rightValue, type, fileName, line), fileName, line);
                return variable.getValue();
            case NOTHING:
                new LogError("Can't xor Nothing.", fileName, line);
            default:
                return null;
        }
    }

    static Object booleanAndOperator(boolean leftValue, OperationNode right, String fileName, int line) {
        if (!leftValue)
            return false;
        var rightValue = right.run();
        if (!(rightValue instanceof Boolean))
            if (rightValue instanceof StructDeclarationNode)
                return andOperator(true, rightValue, DataTypes.OBJECT, fileName, line);
            else
                new LogError(
                        "Type mismatch:\tboolean and " + DataTypes.getDataType(rightValue).toString().toLowerCase(),
                        fileName, line);
        return rightValue;
    }

    static Object booleanOrOperator(boolean leftValue, OperationNode right, String fileName, int line) {
        if (leftValue)
            return true;
        var rightValue = right.run();
        if (!(rightValue instanceof Boolean))
            if (rightValue instanceof StructDeclarationNode)
                return orOperator(false, rightValue, DataTypes.OBJECT, fileName, line);
            else
                new LogError(
                        "Type mismatch:\tboolean and " + DataTypes.getDataType(rightValue).toString().toLowerCase(),
                        fileName, line);
        return rightValue;
    }

    static Object divisionOperator(Object leftValue, Object rightValue, DataTypes type, String fileName, int line) {
        switch (type) {
            case INTEGER:
                return (int) leftValue / (int) rightValue;
            case BIG_INTEGER:
                if (rightValue.equals(BigInteger.ZERO))
                    new LogError("Can't divide by zero.", fileName, line);
                return ((BigInteger) leftValue).divide(((BigInteger) rightValue));
            case FLOAT:
                return (double) leftValue / (double) rightValue;
            case BOOLEAN:
                new LogError("Can't divide " + type.toString().toLowerCase() + "s", fileName, line);
            case OBJECT:
                return overloadOperator(leftValue, rightValue, "$div", 2, fileName, line);
            case NOTHING:
                new LogError("Can't divide Nothing.", fileName, line);
            default:
                return null;
        }
    }

    static Object dotOperator(Object leftValue, OperationNode rightValue, DataTypes type, String fileName, int line) {
        if (type.equals(DataTypes.OBJECT)) {
            return rightValue.runWithParent((StructDeclarationNode) leftValue, false);
        } else
            new LogError("Can't get attributes from simple values.", fileName, line);
        return null;

    }

    static Object equalsOperator(Object leftValue, Object rightValue, DataTypes type, String fileName, int line) {
        if (type.equals(DataTypes.OBJECT))
            return overloadOperator(leftValue, rightValue, "$eq", 2, fileName, line);

        return leftValue.equals(rightValue);
    }


    static Object greaterEqualsOperator(Object leftValue, Object rightValue, DataTypes type, String fileName, int line) {
        switch (type) {
            case INTEGER:
                return (int) leftValue >= (int) rightValue;
            case BIG_INTEGER:
                return ((BigInteger) leftValue).compareTo((BigInteger) rightValue) >= 0;
            case FLOAT:
                return (double) leftValue >= (double) rightValue;
            case BOOLEAN:
                new LogError("Can't compare booleans", fileName, line);
            case OBJECT:
                return overloadOperator(leftValue, rightValue, "$ge", 2, fileName, line);
            case NOTHING:
                new LogError("Can't compare Nothing.", fileName, line);

            default:
                return null;
        }
    }

    static Object greaterOperator(Object leftValue, Object rightValue, DataTypes type, String fileName, int line) {
        switch (type) {
            case INTEGER:
                return (int) leftValue > (int) rightValue;
            case BIG_INTEGER:
                return ((BigInteger) leftValue).compareTo((BigInteger) rightValue) > 0;
            case FLOAT:
                return (double) leftValue > (double) rightValue;
            case BOOLEAN:
                new LogError("Can't compare booleans", fileName, line);
            case OBJECT:
                return overloadOperator(leftValue, rightValue, "$gt", 2, fileName, line);
            case NOTHING:
                new LogError("Can't compare Nothing.", fileName, line);
            default:
                return null;
        }
    }

    static Object instanceOfOperator(Object leftValue, Object rightValue, DataTypes type, Block parent, String fileName, int line) {
        var rightName = ((IdentifierNode) rightValue).getName();

        if (builtInTypes.containsKey(rightName))
            return isInstance(leftValue, rightName);
        if (!parent.hasStructure(rightName))
            new LogError("There isn't any data type with name " + rightName, fileName, line);
        if (type.equals(DataTypes.OBJECT)) {
            return parent.getStructure(rightName, fileName, line)
                    .instanceOfMe((StructDeclarationNode) leftValue);
        }

        return false;
    }

    static Object lowerEqualsOperator(Object leftValue, Object rightValue, DataTypes type, String fileName, int line) {
        switch (type) {
            case INTEGER:
                return (int) leftValue <= (int) rightValue;
            case BIG_INTEGER:
                return ((BigInteger) leftValue).compareTo((BigInteger) rightValue) <= 0;
            case FLOAT:
                return (double) leftValue <= (double) rightValue;
            case BOOLEAN:
                new LogError("Can't compare booleans", fileName, line);
            case OBJECT:
                return overloadOperator(leftValue, rightValue, "$le", 2, fileName, line);
            case NOTHING:
                new LogError("Can't compare Nothing.", fileName, line);

            default:
                return null;
        }
    }

    static Object lowerOperator(Object leftValue, Object rightValue, DataTypes type, String fileName, int line) {
        switch (type) {
            case INTEGER:
                return (int) leftValue < (int) rightValue;
            case BIG_INTEGER:
                return ((BigInteger) leftValue).compareTo((BigInteger) rightValue) < 0;
            case FLOAT:
                return (double) leftValue < (double) rightValue;
            case BOOLEAN:
                new LogError("Can't compare booleans", fileName, line);
            case OBJECT:
                return overloadOperator(leftValue, rightValue, "$lt", 2, fileName, line);
            case NOTHING:
                new LogError("Can't compare Nothing.", fileName, line);
            default:
                return null;
        }
    }

    static Object moduloOperator(Object leftValue, Object rightValue, DataTypes type, String fileName, int line) {
        switch (type) {
            case INTEGER:
                return (int) leftValue % (int) rightValue;
            case BIG_INTEGER:
                var rightI = (BigInteger) rightValue;
                if (rightI.equals(BigInteger.ZERO))
                    return BigInteger.ZERO;
                return ((BigInteger) leftValue).remainder(rightI);
            case FLOAT:
                return (double) leftValue % (double) rightValue;
            case BOOLEAN:
                new LogError("Can't do modulo operation with booleans", fileName, line);
            case OBJECT:
                return overloadOperator(leftValue, rightValue, "$mod", 2, fileName, line);
            case NOTHING:
                new LogError("Can't do modulo operation with Nothing.", fileName, line);
            default:
                return null;
        }
    }

    static Object multiplicationOperator(Object leftValue, Object rightValue, DataTypes type, String fileName, int line) {
        switch (type) {
            case INTEGER:
                var leftInt = (int) leftValue;
                var rightInt = (int) rightValue;
                try {
                    return Math.multiplyExact(leftInt, rightInt);
                } catch (ArithmeticException e) {
                    return BigInteger.valueOf(leftInt).multiply(BigInteger.valueOf(rightInt));
                }
            case BIG_INTEGER:
                return ((BigInteger) leftValue).multiply(((BigInteger) rightValue));
            case FLOAT:
                return (double) leftValue * (double) rightValue;
            case BOOLEAN:
                new LogError("Can't multiply booleans", fileName, line);
            case OBJECT:
                return overloadOperator(leftValue, rightValue, "$mul", 2, fileName, line);
            case NOTHING:
                new LogError("Void hasn't got any value:\t" + leftValue + " " + rightValue + " *", fileName,
                        line);
            default:
                return null;
        }
    }

    static Object negationOperator(Object value, DataTypes type, String fileName, int line) {
        switch (type) {
            case INTEGER:
                return -(int) value;
            case BIG_INTEGER:
                return BigInteger.ZERO.subtract((BigInteger) value);
            case FLOAT:
                return -(double) value;
            case BOOLEAN:
                return !(boolean) value;
            case OBJECT:
                return overloadOperator(null, value, "$not", 1, fileName, line);
            case NOTHING:
                new LogError("Void hasn't got any value:\t" + value + " !", fileName, line);
            default:
                return null;
        }
    }

    static Object notEqualsOperator(Object leftValue, Object rightValue, DataTypes type, String fileName, int line) {
        if (type.equals(DataTypes.OBJECT))
            return overloadOperator(leftValue, rightValue, "$neq", 2, fileName, line);
        return !leftValue.equals(rightValue);
    }

    static Object orOperator(Object leftValue, Object rightValue, DataTypes type, String fileName, int line) {
        switch (type) {
            case INTEGER:
                return (int) leftValue | (int) rightValue;
            case BIG_INTEGER:
                return ((BigInteger) leftValue).or((BigInteger) rightValue);
            case BOOLEAN:
                return ((Boolean) leftValue) || ((Boolean) rightValue);
            case FLOAT:
                new LogError("Can't do or operation with " + type.toString().toLowerCase() + "s", fileName,
                        line);
            case OBJECT:
                return overloadOperator(leftValue, rightValue, "$or", 2, fileName, line);
            case NOTHING:
                new LogError("Void hasn't got any value:\t" + leftValue + " " + rightValue + " |", fileName,
                        line);
            default:
                return null;
        }
    }

    static Object powerOperator(Object leftValue, Object rightValue, DataTypes type, String fileName, int line) {
        switch (type) {
            case INTEGER:
            case BIG_INTEGER:
                return Pow.powerForIntegers(leftValue, rightValue, fileName, line);
            case BOOLEAN:
                return ((Boolean) leftValue) || ((Boolean) rightValue);
            case FLOAT:
                return Math.pow((double) leftValue, (double) rightValue);
            case OBJECT:
                return overloadOperator(leftValue, rightValue, "$pow", 2, fileName, line);
            case NOTHING:
                new LogError("Void hasn't got any value:\t" + leftValue + " " + rightValue + " |", fileName,
                        line);
            default:
                return null;
        }
    }

    private static Object overloadOperator(Object leftValue, Object rightValue, String nameOfFunction,
                                           int numberOfParameters, String fileName, int line) {
        var arguments = new Tuple(leftValue, rightValue);
        leftValue = OperationNode.getVariableValue(leftValue);
        if (leftValue instanceof StructDeclarationNode) {
            var struct = (StructDeclarationNode) leftValue;
            if (struct.hasFunction(nameOfFunction)) {
                var operator = struct.getFunction(nameOfFunction, fileName, line);
                if (operator.getParametersLength() == numberOfParameters)
                    return operator.call(arguments, fileName, line);
            }
        }
        if (rightValue instanceof StructDeclarationNode) {
            var struct = (StructDeclarationNode) rightValue;
            var name = "$r_" + nameOfFunction.substring(1);
            if (struct.hasFunction(name)) {
                var operator = struct.getFunction(name, fileName, line);
                if (operator.getParametersLength() == numberOfParameters)
                    return operator.call(arguments, fileName, line);
            }
        }
        new LogError(
                "Can't do any operations besides assignment and comparison with \"any\" data type if there isn't any overload.",
                fileName, line);
        return null;
    }

    static Object shiftLeftOperator(Object leftValue, Object rightValue, DataTypes type, String fileName, int line) {
        switch (type) {
            case INTEGER:
                return BigInteger.valueOf((int) leftValue).shiftLeft((int) rightValue);
            case BIG_INTEGER:
                return ((BigInteger) leftValue).shiftLeft(((BigInteger) rightValue).intValue());
            case FLOAT:
            case BOOLEAN:
                new LogError("Can't shift left " + type.toString().toLowerCase() + "s", fileName, line);
            case OBJECT:
                return overloadOperator(leftValue, rightValue, "$shl", 2, fileName, line);
            case NOTHING:
                new LogError("Can't shift left Nothing.", fileName, line);
            default:
                return null;
        }
    }

    static Object shiftRightOperator(Object leftValue, Object rightValue, DataTypes type, String fileName, int line) {
        switch (type) {
            case INTEGER:
                return (int) leftValue >> (int) rightValue;
            case BIG_INTEGER:
                return ((BigInteger) leftValue).shiftRight(((BigInteger) rightValue).intValue());
            case FLOAT:
            case BOOLEAN:
                new LogError("Can't shift right " + type.toString().toLowerCase() + "s", fileName, line);
            case OBJECT:
                return overloadOperator(leftValue, rightValue, "$shr", 2, fileName, line);
            case NOTHING:
                new LogError("Can't shift right Nothing.", fileName, line);
            default:
                return null;
        }
    }

    static Object subtractionOperator(Object leftValue, Object rightValue, DataTypes type, String fileName, int line) {
        switch (type) {
            case INTEGER:
                var leftInt = (int) leftValue;
                var rightInt = (int) rightValue;
                try {
                    return Math.subtractExact(leftInt, rightInt);
                } catch (ArithmeticException e) {
                    return BigInteger.valueOf(leftInt).multiply(BigInteger.valueOf(rightInt));
                }
            case BIG_INTEGER:
                return ((BigInteger) leftValue).subtract(((BigInteger) rightValue));
            case FLOAT:
                return (double) leftValue - (double) rightValue;
            case BOOLEAN:
                new LogError("Can't subtract booleans", fileName, line);
            case OBJECT:
                return overloadOperator(leftValue, rightValue, "$sub", 2, fileName, line);
            case NOTHING:
                new LogError("Can't subtract Nothing.", fileName, line);
            default:
                return null;
        }
    }

    static Object xorOperator(Object leftValue, Object rightValue, DataTypes type, String fileName, int line) {
        switch (type) {
            case INTEGER:
                return (int) leftValue ^ (int) rightValue;
            case BIG_INTEGER:
                return ((BigInteger) leftValue).xor((BigInteger) rightValue);
            case FLOAT:
            case BOOLEAN:
                new LogError("Can't do xor operation with " + type.toString().toLowerCase() + "s",
                        fileName, line);
            case OBJECT:
                return overloadOperator(leftValue, rightValue, "$xor", 2, fileName, line);
            case NOTHING:
                new LogError("Can't do xor operation with Nothing.", fileName, line);
            default:
                return null;
        }
    }

    static Object assignmentModuloOperator(Object leftValue, Object rightValue, DataTypes type, String fileName, int line) {
        if (type.equals(DataTypes.OBJECT))
            return overloadOperator(leftValue, rightValue, "$a_mod", 2, fileName, line);

        var variable = VariableDeclarationNode.toMe(leftValue, fileName, line);
        switch (type) {
            case INTEGER:
            case BIG_INTEGER:
            case FLOAT:
                variable.setValue(moduloOperator(variable.getValue(), rightValue, type, fileName, line), fileName,
                        line);
                return variable.getValue();
            case NOTHING:
                new LogError("Can't divide Nothing.", fileName, line);
            default:
                return null;
        }
    }
}
