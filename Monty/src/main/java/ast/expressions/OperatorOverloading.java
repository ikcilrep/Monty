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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

import ast.Block;
import ast.declarations.StructDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import parser.DataTypes;
import parser.LogError;
import sml.data.list.List;
import sml.data.string.StringStruct;
import sml.iterations.range.Range;
import sml.math.MathStruct;

public final class OperatorOverloading {
	public static ArrayList<OperationNode> arguments;
	private static HashMap<String, Class<? extends StructDeclarationNode>> builtInTypes = new HashMap<>();
	static {
		arguments = new ArrayList<>();
		arguments.add(new OperationNode(null, null));
		arguments.add(new OperationNode(null, null));
		builtInTypes.put("List", List.class);
		builtInTypes.put("Range", Range.class);
		builtInTypes.put("Iterable", sml.functional.iterable.Iterable.class);

	}

	private static int temporaryLine;
	private static String temporaryFileName;

	public static void setTemporary(String fileName, int line) {
		temporaryFileName = fileName;
		temporaryLine = line;
	}

	public static int getTemporaryLine() {
		return temporaryLine;
	}

	public static String getTemporaryFileName() {
		return temporaryFileName;
	}

	public final static Object overloadOperator(Object leftValue, Object rightValue, String nameOfFunction,
			int numberOfParameters, boolean isAssignment) {
		arguments.get(0).setOperand(new ConstantNode(leftValue));
		arguments.get(1).setOperand(new ConstantNode(rightValue));
		if (leftValue instanceof StructDeclarationNode) {
			var struct = (StructDeclarationNode) leftValue;
			if (struct.hasFunction(nameOfFunction)) {
				var operator = struct.getFunction(nameOfFunction);
				if (operator.getParameters().size() == numberOfParameters)
					return operator.call(arguments, temporaryFileName, temporaryLine);
			}
		} else if (!isAssignment && rightValue instanceof StructDeclarationNode) {
			var t = arguments.get(0);
			arguments.set(0, arguments.get(1));
			arguments.set(1, t);
			var struct = (StructDeclarationNode) rightValue;
			if (struct.hasFunction(nameOfFunction)) {
				var operator = struct.getFunction(nameOfFunction);
				if (operator.getParameters().size() == numberOfParameters)
					return operator.call(arguments, temporaryFileName, temporaryLine);
			}
		}
		new LogError(
				"Can't do any operations besides assignment and comparison with \"any\" data type if there isn't any overload.",
				temporaryFileName, temporaryLine);
		return null;
	}

	private static String reverse(String str) {
		var result = new StringBuilder(str.length());
		var string = str;
		for (int j = string.length() - 1; j >= 0; j--)
			result.append(string.charAt(j));
		return result.toString();
	}

	public final static Object additionOperator(Object leftValue, Object rightValue, DataTypes type) {
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
		case STRING:
			return (String) leftValue + (String) rightValue;
		case BOOLEAN:
			new LogError("Can't add booleans.", temporaryFileName, temporaryLine);
		case ANY:
			return overloadOperator(leftValue, rightValue, "$add", 2, false);
		case VOID:
			new LogError("Can't add Nothing.", temporaryFileName, temporaryLine);
		default:
			return null;
		}
	}

	public final static Object andOperator(Object leftValue, Object rightValue, DataTypes type) {
		switch (type) {
		case INTEGER:
			return (int) leftValue & (int) rightValue;
		case BIG_INTEGER:
			return ((BigInteger) leftValue).and((BigInteger) rightValue);
		case BOOLEAN:
			return ((Boolean) leftValue) && ((Boolean) rightValue);
		case FLOAT:
		case STRING:
			new LogError("Can't do and operation with " + type.toString().toLowerCase() + "s", temporaryFileName,
					temporaryLine);
		case ANY:
			return overloadOperator(leftValue, rightValue, "$and", 2, false);
		case VOID:
			new LogError("Can't do and operation with Nothing.", temporaryFileName, temporaryLine);
		default:
			return null;
		}
	}

	public final static Object divisionOperator(Object leftValue, Object rightValue, DataTypes type) {
		switch (type) {
		case INTEGER:
			return (int) leftValue / (int) rightValue;
		case BIG_INTEGER:
			if (rightValue.equals(BigInteger.ZERO))
				new LogError("Can't divide by zero.", temporaryFileName, temporaryLine);
			return ((BigInteger) leftValue).divide(((BigInteger) rightValue));
		case FLOAT:
			return (double) leftValue / (double) rightValue;
		case STRING:
		case BOOLEAN:
			new LogError("Can't divide " + type.toString().toLowerCase() + "s", temporaryFileName, temporaryLine);
		case ANY:
			return overloadOperator(leftValue, rightValue, "$div", 2, false);
		case VOID:
			new LogError("Can't divide Nothing.", temporaryFileName, temporaryLine);
		default:
			return null;
		}
	}

	public final static Object dotOperator(Object leftValue, Object rightValue, DataTypes type, Block parent) {
		if (type.equals(DataTypes.ANY))
			return OperationNode.getLiteral(rightValue, (StructDeclarationNode) leftValue, temporaryFileName,
					temporaryLine);
		if (rightValue instanceof FunctionCallNode) {
			var function = (FunctionCallNode) rightValue;
			var arguments = new ArrayList<OperationNode>();
			arguments.add(new OperationNode(new ConstantNode(leftValue), parent));
			arguments.addAll(function.getArguments());
			switch (type) {
			case INTEGER:
			case BIG_INTEGER:
			case FLOAT:
				return MathStruct.getStruct().getFunction(function.getName(), temporaryFileName, temporaryLine)
						.call(arguments, temporaryFileName, temporaryLine);
			case STRING:
				return StringStruct.getStruct().getFunction(function.getName(), temporaryFileName, temporaryLine)
						.call(arguments, temporaryFileName, temporaryLine);
			case VOID:
				new LogError("Can't get attributes or methods from Nothing.", temporaryFileName, temporaryLine);
			case BOOLEAN:
				new LogError("Can't get attributes or methods from boolean.", temporaryFileName, temporaryLine);
			default:
				return null;
			}
		} else {
			new LogError("Can't get attributes from simple values.", temporaryFileName, temporaryLine);
		}
		return null;

	}

	public final static Object equalsOperator(Object leftValue, Object rightValue) {
		return leftValue.equals(rightValue);
	}

	public final static Object greaterEqualsOperator(Object leftValue, Object rightValue, DataTypes type) {
		switch (type) {
		case INTEGER:
			return (int) leftValue >= (int) rightValue;
		case BIG_INTEGER:
			return ((BigInteger) leftValue).compareTo((BigInteger) rightValue) >= 0;
		case FLOAT:
			return (double) leftValue >= (double) rightValue;
		case STRING:
		case BOOLEAN:
			new LogError("Can't compare" + type.toString().toLowerCase() + "s", temporaryFileName, temporaryLine);
		case ANY:
			return overloadOperator(leftValue, rightValue, "$ge", 2, false);
		case VOID:
			new LogError("Can't compare Nothing.", temporaryFileName, temporaryLine);

		default:
			return null;
		}
	}

	public final static Object greaterOperator(Object leftValue, Object rightValue, DataTypes type) {
		switch (type) {
		case INTEGER:
			return (int) leftValue > (int) rightValue;
		case BIG_INTEGER:
			return ((BigInteger) leftValue).compareTo((BigInteger) rightValue) > 0;
		case FLOAT:
			return (double) leftValue > (double) rightValue;
		case STRING:
		case BOOLEAN:
			new LogError("Can't compare" + type.toString().toLowerCase() + "s", temporaryFileName, temporaryLine);
		case ANY:
			return overloadOperator(leftValue, rightValue, "$gt", 2, false);
		case VOID:
			new LogError("Can't compare Nothing.", temporaryFileName, temporaryLine);
		default:
			return null;
		}
	}

	public final static Object instanceOfOperator(Object leftValue, Object rightValue, DataTypes type, Block parent) {
		var rightName = ((VariableNode) rightValue).getName();
		switch (rightName) {
		case "Integer":
			return type.equals(DataTypes.BIG_INTEGER) || type.equals(DataTypes.INTEGER);
		case "Float":
			return type.equals(DataTypes.FLOAT);
		case "String":
			return type.equals(DataTypes.STRING);
		case "Boolean":
			return type.equals(DataTypes.BOOLEAN);
		case "Nothing":
			return type.equals(DataTypes.VOID);
		default:
			if (builtInTypes.containsKey(rightName))
				return builtInTypes.get(rightName).isInstance(leftValue);
			if (type.equals(DataTypes.ANY))
				return parent.getStructure(rightName, temporaryFileName, temporaryLine)
						.instanceOfMe((StructDeclarationNode) leftValue);
			break;
		}

		return null;
	}

	public final static Object lowerEqualsOperator(Object leftValue, Object rightValue, DataTypes type) {
		switch (type) {
		case INTEGER:
			return (int) leftValue <= (int) rightValue;
		case BIG_INTEGER:
			return ((BigInteger) leftValue).compareTo((BigInteger) rightValue) <= 0;
		case FLOAT:
			return (double) leftValue <= (double) rightValue;
		case STRING:
		case BOOLEAN:
			new LogError("Can't compare" + type.toString().toLowerCase() + "s", temporaryFileName, temporaryLine);
		case ANY:
			return overloadOperator(leftValue, rightValue, "$le", 2, false);
		case VOID:
			new LogError("Can't compare Nothing.", temporaryFileName, temporaryLine);

		default:
			return null;
		}
	}

	public final static Object lowerOperator(Object leftValue, Object rightValue, DataTypes type) {
		switch (type) {
		case INTEGER:
			return (int) leftValue < (int) rightValue;
		case BIG_INTEGER:
			return ((BigInteger) leftValue).compareTo((BigInteger) rightValue) < 0;
		case FLOAT:
			return (double) leftValue < (double) rightValue;
		case STRING:
		case BOOLEAN:
			new LogError("Can't compare" + type.toString().toLowerCase() + "s", temporaryFileName, temporaryLine);
		case ANY:
			return overloadOperator(leftValue, rightValue, "$lt", 2, false);
		case VOID:
			new LogError("Can't compare Nothing.", temporaryFileName, temporaryLine);
		default:
			return null;
		}
	}

	public final static Object moduloOperator(Object leftValue, Object rightValue, DataTypes type) {
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
		case STRING:
		case BOOLEAN:
			new LogError("Can't do modulo operation with " + type.toString().toLowerCase() + "s", temporaryFileName,
					temporaryLine);
		case ANY:
			return overloadOperator(leftValue, rightValue, "$mod", 2, false);
		case VOID:
			new LogError("Can't do modulo operation with Nothing.", temporaryFileName, temporaryLine);
		default:
			return null;
		}
	}

	public final static Object multiplicationOperator(Object leftValue, Object rightValue, DataTypes type) {
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
		case STRING:
		case BOOLEAN:
			new LogError("Can't multiply " + type.toString().toLowerCase() + "s", temporaryFileName, temporaryLine);
		case ANY:
			return overloadOperator(leftValue, rightValue, "$mul", 2, false);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue + " " + rightValue + " *", temporaryFileName,
					temporaryLine);
		default:
			return null;
		}
	}

	public final static Object negationOperator(Object value, DataTypes type) {
		switch (type) {
		case INTEGER:
			return -(int) value;
		case BIG_INTEGER:
			return BigInteger.ZERO.subtract((BigInteger) value);
		case FLOAT:
			return -(double) value;
		case STRING:
			return reverse((String) value);
		case BOOLEAN:
			return !(boolean) value;
		case ANY:
			return overloadOperator(null, value, "$not", 1, false);
		case VOID:
			new LogError("Void hasn't got any value:\t" + value + " !", temporaryFileName, temporaryLine);
		default:
			return null;
		}
	}

	public final static Object notEqualsOperator(Object leftValue, Object rightValue) {
		return !leftValue.equals(rightValue);
	}

	public final static Object orOperator(Object leftValue, Object rightValue, DataTypes type) {
		switch (type) {
		case INTEGER:
			var leftInt = (int) leftValue;
			var rightInt = (int) rightValue;
			try {
				return leftInt | rightInt;
			} catch (ArithmeticException e) {
				return BigInteger.valueOf(leftInt).or(BigInteger.valueOf(rightInt));
			}
		case BIG_INTEGER:
			return ((BigInteger) leftValue).or((BigInteger) rightValue);
		case BOOLEAN:
			return ((Boolean) leftValue) || ((Boolean) rightValue);
		case FLOAT:
		case STRING:
			new LogError("Can't do or operation with " + type.toString().toLowerCase() + "s", temporaryFileName,
					temporaryLine);
		case ANY:
			return overloadOperator(leftValue, rightValue, "$or", 2, false);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue + " " + rightValue + " |", temporaryFileName,
					temporaryLine);
		default:
			return null;
		}
	}

	public final static Object shiftLeftOperator(Object leftValue, Object rightValue, DataTypes type) {
		switch (type) {
		case INTEGER:
			return BigInteger.valueOf((int) leftValue).shiftLeft((int) rightValue);
		case BIG_INTEGER:
			return ((BigInteger) leftValue).shiftLeft(((BigInteger) rightValue).intValue());
		case STRING:
			var str = (String) leftValue;
			return str.substring(0, str.length() - ((BigInteger) rightValue).intValue());
		case FLOAT:
		case BOOLEAN:
			new LogError("Can't shift left " + type.toString().toLowerCase() + "s", temporaryFileName, temporaryLine);
		case ANY:
			return overloadOperator(leftValue, rightValue, "$shl", 2, false);
		case VOID:
			new LogError("Can't shift left Nothing.", temporaryFileName, temporaryLine);
		default:
			return null;
		}
	}

	public final static Object shiftRightOperator(Object leftValue, Object rightValue, DataTypes type) {
		switch (type) {
		case INTEGER:
			return (int) leftValue >> (int) rightValue;
		case BIG_INTEGER:
			return ((BigInteger) leftValue).shiftRight(((BigInteger) rightValue).intValue());
		case FLOAT:
		case STRING:
		case BOOLEAN:
			new LogError("Can't shift right " + type.toString().toLowerCase() + "s", temporaryFileName, temporaryLine);
		case ANY:
			return overloadOperator(leftValue, rightValue, "$shr", 2, false);
		case VOID:
			new LogError("Can't shift right Nothing.", temporaryFileName, temporaryLine);
		default:
			return null;
		}
	}

	public final static Object subtractionOperator(Object leftValue, Object rightValue, DataTypes type) {
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
		case STRING:
		case BOOLEAN:
			new LogError("Can't subtract " + type.toString().toLowerCase() + "s", temporaryFileName, temporaryLine);
		case ANY:
			return overloadOperator(leftValue, rightValue, "$sub", 2, false);
		case VOID:
			new LogError("Can't subtract Nothing.", temporaryFileName, temporaryLine);
		default:
			return null;
		}
	}

	public final static Object xorOperator(Object leftValue, Object rightValue, DataTypes type) {
		switch (type) {
		case INTEGER:
			return (int) leftValue ^ (int) rightValue;
		case BIG_INTEGER:
			return ((BigInteger) leftValue).xor((BigInteger) rightValue);
		case FLOAT:
		case STRING:
		case BOOLEAN:
			new LogError("Can't do xor operation with " + type.toString().toString().toLowerCase() + "s",
					temporaryFileName, temporaryLine);
		case ANY:
			return overloadOperator(leftValue, rightValue, "$xor", 2, false);
		case VOID:
			new LogError("Can't do xor operation with Nothing.", temporaryFileName, temporaryLine);
		default:
			return null;
		}
	}

	public final static Object assignmentAdditionOperator(Object leftValue, Object rightValue, DataTypes type) {
		var variable = VariableDeclarationNode.toMe(leftValue, temporaryFileName, temporaryLine);
		switch (type) {
		case INTEGER:
		case BIG_INTEGER:
		case FLOAT:
		case STRING:
			variable.setValue(additionOperator(variable.getValue(), rightValue, type), temporaryFileName,
					temporaryLine);
			return variable.getValue();
		case BOOLEAN:
			new LogError("Can't add booleans.", temporaryFileName, temporaryLine);
		case ANY:
			return overloadOperator(variable.getValue(), rightValue, "$a_add", 2, true);
		case VOID:
			new LogError("Can't add Nothing.", temporaryFileName, temporaryLine);
		default:
			return null;
		}
	}

	public final static Object assignmentAndOperator(Object leftValue, Object rightValue, DataTypes type) {
		var variable = VariableDeclarationNode.toMe(leftValue, temporaryFileName, temporaryLine);
		switch (type) {
		case INTEGER:
		case BIG_INTEGER:
		case FLOAT:
		case STRING:
			variable.setValue(andOperator(variable.getValue(), rightValue, type), temporaryFileName, temporaryLine);
			return variable.getValue();
		case ANY:
			return overloadOperator(variable.getValue(), rightValue, "$a_and", 2, true);
		case VOID:
			new LogError("Can't do and operation with Nothing.", temporaryFileName, temporaryLine);
		default:
			return null;
		}
	}

	public final static Object assignmentDivisionOperator(Object leftValue, Object rightValue, DataTypes type) {
		var variable = VariableDeclarationNode.toMe(leftValue, temporaryFileName, temporaryLine);
		switch (type) {
		case INTEGER:
		case BIG_INTEGER:
		case FLOAT:
		case STRING:
			variable.setValue(divisionOperator(variable.getValue(), rightValue, type), temporaryFileName,
					temporaryLine);
			return variable.getValue();
		case ANY:
			return overloadOperator(variable.getValue(), rightValue, "$a_div", 2, true);
		case VOID:
			new LogError("Can't divide Nothing.", temporaryFileName, temporaryLine);
		default:
			return null;
		}
	}

	public final static Object assignmentMultiplicationOperator(Object leftValue, Object rightValue, DataTypes type) {
		var variable = VariableDeclarationNode.toMe(leftValue, temporaryFileName, temporaryLine);
		switch (type) {
		case INTEGER:
		case BIG_INTEGER:
		case FLOAT:
		case STRING:
			variable.setValue(multiplicationOperator(variable.getValue(), rightValue, type), temporaryFileName,
					temporaryLine);
			return variable.getValue();
		case ANY:
			return overloadOperator(variable.getValue(), rightValue, "$a_mul", 2, true);
		case VOID:
			new LogError("Can't multiply Nothing.", temporaryFileName, temporaryLine);
		default:
			return null;
		}
	}

	public final static Object assignmentOperator(Object leftValue, Object rightValue, DataTypes type) {
		var variable = VariableDeclarationNode.toMe(leftValue, temporaryFileName, temporaryLine);
		switch (type) {
		case INTEGER:
		case BIG_INTEGER:
		case FLOAT:
		case STRING:
		case BOOLEAN:
		case ANY:
		case VOID:
			variable.setValue(rightValue, temporaryFileName, temporaryLine);
			return variable.getValue();
		default:
			return null;
		}
	}

	public final static Object assignmentOrOperator(Object leftValue, Object rightValue, DataTypes type) {
		var variable = VariableDeclarationNode.toMe(leftValue, temporaryFileName, temporaryLine);
		switch (type) {
		case INTEGER:
		case BIG_INTEGER:
		case FLOAT:
		case STRING:
			variable.setValue(orOperator(variable.getValue(), rightValue, type), temporaryFileName, temporaryLine);
			return variable.getValue();
		case ANY:
			return overloadOperator(variable.getValue(), rightValue, "$a_or", 2, true);
		case VOID:
			new LogError("Can't do or operation with Nothing.", temporaryFileName, temporaryLine);
		default:
			return null;
		}
	}

	public final static Object assignmentShiftLeftOperator(Object leftValue, Object rightValue, DataTypes type) {
		var variable = VariableDeclarationNode.toMe(leftValue, temporaryFileName, temporaryLine);
		switch (type) {
		case INTEGER:
		case BIG_INTEGER:
		case FLOAT:
		case STRING:
			variable.setValue(shiftLeftOperator(variable.getValue(), rightValue, type), temporaryFileName,
					temporaryLine);
			return variable.getValue();
		case ANY:
			return overloadOperator(variable.getValue(), rightValue, "$a_shl", 2, true);
		case VOID:
			new LogError("Can't shift left Nothing.", temporaryFileName, temporaryLine);
		default:
			return null;
		}
	}

	public final static Object assignmentShiftRightOperator(Object leftValue, Object rightValue, DataTypes type) {
		var variable = VariableDeclarationNode.toMe(leftValue, temporaryFileName, temporaryLine);
		switch (type) {
		case INTEGER:
		case BIG_INTEGER:
		case FLOAT:
		case STRING:
			variable.setValue(shiftRightOperator(variable.getValue(), rightValue, type), temporaryFileName,
					temporaryLine);
			return variable.getValue();
		case ANY:
			return overloadOperator(variable.getValue(), rightValue, "$a_shr", 2, true);
		case VOID:
			new LogError("Can't shift right Nothing.", temporaryFileName, temporaryLine);
		default:
			return null;
		}
	}

	public final static Object assignmentSubtractionOperator(Object leftValue, Object rightValue, DataTypes type) {
		var variable = VariableDeclarationNode.toMe(leftValue, temporaryFileName, temporaryLine);
		switch (type) {
		case INTEGER:
		case BIG_INTEGER:
		case FLOAT:
		case STRING:
			variable.setValue(subtractionOperator(variable.getValue(), rightValue, type), temporaryFileName,
					temporaryLine);
			return variable.getValue();
		case ANY:
			return overloadOperator(variable.getValue(), rightValue, "$a_sub", 2, true);
		case VOID:
			new LogError("Can't subtract Nothing.", temporaryFileName, temporaryLine);
		default:
			return null;
		}
	}

	public final static Object assignmentXorOperator(Object leftValue, Object rightValue, DataTypes type) {
		var variable = VariableDeclarationNode.toMe(leftValue, temporaryFileName, temporaryLine);
		switch (type) {
		case INTEGER:
		case BIG_INTEGER:
		case FLOAT:
		case STRING:
			variable.setValue(xorOperator(variable.getValue(), rightValue, type), temporaryFileName, temporaryLine);
			return variable.getValue();
		case ANY:
			return overloadOperator(variable.getValue(), rightValue, "$a_xor", 2, true);
		case VOID:
			new LogError("Can't xor Nothing.", temporaryFileName, temporaryLine);
		default:
			return null;
		}
	}
}
