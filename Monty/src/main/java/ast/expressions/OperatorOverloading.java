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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;

import ast.declarations.StructDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import parser.DataTypes;
import parser.LogError;

public final class OperatorOverloading {
	private static ArrayList<OperationNode> arguments;
	static {
		arguments = new ArrayList<>();
		arguments.add(new OperationNode(null, null));
		arguments.add(new OperationNode(null, null));
	}

	private final static Object overloadOperator(Object leftValue, Object rightValue, String nameOfFunction,
			byte numberOfParameters, String fileName, int line) {
		Object[] values = { leftValue, rightValue };
		arguments.get(0).setOperand(new ConstantNode(leftValue, DataTypes.ANY));
		arguments.get(1).setOperand(new ConstantNode(rightValue, DataTypes.ANY));
		for (Object value : values)
			if (value instanceof StructDeclarationNode) {
				var struct = (StructDeclarationNode) value;
				if (struct.hasFunction(nameOfFunction)) {
					var operator = struct.getFunction(nameOfFunction);
					if (!operator.getType().equals(DataTypes.VOID)
							&& operator.getParameters().size() == numberOfParameters)
						return operator.call(arguments, fileName, line);
				}
			}
		new LogError(
				"Can't do any operations besides assignment and comparison with \"any\" data type if there isn't any overload.",
				fileName, line);
		return null;
	}

	private static String reverse(String str) {
		var result = new StringBuilder(str.length());
		var string = str;
		for (int j = string.length() - 1; j >= 0; j--)
			result.append(string.charAt(j));
		return result.toString();
	}

	public final static Object additionOperator(Object leftValue, Object rightValue, DataTypes type, String fileName,
			int line) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).add(((BigInteger) rightValue));
		case REAL:
			return ((BigDecimal) leftValue).add(((BigDecimal) rightValue));
		case STRING:
			return (String) leftValue + (String) rightValue;
		case BOOLEAN:
			new LogError("Can't add booleans:\t" + leftValue + " " + rightValue + " +", fileName, line);
		case ANY:
			return overloadOperator(leftValue, rightValue, "$add", (byte) 2, fileName, line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue + " " + rightValue + " +", fileName, line);
		default:
			return null;
		}
	}

	public final static Object andOperator(Object leftValue, Object rightValue, DataTypes type, String fileName,
			int line) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).and((BigInteger) rightValue);
		case BOOLEAN:
			return ((Boolean) leftValue) && ((Boolean) rightValue);
		case REAL:
		case STRING:
			new LogError("Can't do and operation with " + type.toString().toLowerCase() + "s:\t " + leftValue + " "
					+ rightValue + " &", fileName, line);
		case ANY:
			return overloadOperator(leftValue, rightValue, "$and", (byte) 2, fileName, line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue + " " + rightValue + " &", fileName, line);
		default:
			return null;
		}
	}

	public final static Object divisionOperator(Object leftValue, Object rightValue, DataTypes type, String fileName,
			int line) {
		switch (type) {
		case INTEGER:
			if (rightValue.equals(BigInteger.ZERO))
				new LogError("Can't divide by zero.", fileName, line);
			return ((BigInteger) leftValue).divide(((BigInteger) rightValue));
		case REAL:
			if (((BigDecimal) rightValue).compareTo(BigDecimal.ZERO) == 0)
				new LogError("Can't divide by zero.", fileName, line);
			try {
				return ((BigDecimal) leftValue).divide(((BigDecimal) rightValue));
			} catch (ArithmeticException e) {
				return ((BigDecimal) leftValue).divide(((BigDecimal) rightValue), ((BigDecimal) rightValue).intValue(),
						RoundingMode.HALF_UP);
			}
		case STRING:
		case BOOLEAN:
			new LogError("Can't divide " + type.toString().toLowerCase() + "s:\t" + leftValue + " " + rightValue + " /",
					fileName, line);
		case ANY:
			return overloadOperator(leftValue, rightValue, "$div", (byte) 2, fileName, line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue + " " + rightValue + " /", fileName, line);
		default:
			return null;
		}
	}

	public final static Object equalsOperator(Object leftValue, Object rightValue, DataTypes type, String fileName,
			int line) {
		switch (type) {
		case REAL:
			return ((BigDecimal) leftValue).compareTo((BigDecimal) rightValue) == 0;
		case INTEGER:
		case BOOLEAN:
		case STRING:
		case ANY:
			return leftValue.equals(rightValue);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue + " " + rightValue + " ==", fileName, line);
		default:
			return null;
		}
	}

	public final static Object greaterEqualsOperator(Object leftValue, Object rightValue, DataTypes type,
			String fileName, int line) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).compareTo((BigInteger) rightValue) >= 0;
		case REAL:
			return ((BigDecimal) leftValue).compareTo((BigDecimal) rightValue) >= 0;
		case STRING:
		case BOOLEAN:
			new LogError("Can't do greater-equals operation with " + type.toString().toLowerCase() + "s:\t" + leftValue
					+ " " + rightValue + " >=", fileName, line);
		case ANY:
			return overloadOperator(leftValue, rightValue, "$ge", (byte) 2, fileName, line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue + " " + rightValue + " >=", fileName, line);
		default:
			return null;
		}
	}

	public final static Object greaterOperator(Object leftValue, Object rightValue, DataTypes type, String fileName,
			int line) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).compareTo((BigInteger) rightValue) > 0;
		case REAL:
			return ((BigDecimal) leftValue).compareTo((BigDecimal) rightValue) > 0;
		case STRING:
		case BOOLEAN:
			new LogError("Can't do greater operation with " + type.toString().toLowerCase() + "s:\t" + leftValue + " "
					+ rightValue + " >", fileName, line);
		case ANY:
			return overloadOperator(leftValue, rightValue, "$gt", (byte) 2, fileName, line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue + " " + rightValue + " >", fileName, line);
		default:
			return null;
		}
	}

	public final static Object lowerEqualsOperator(Object leftValue, Object rightValue, DataTypes type, String fileName,
			int line) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).compareTo((BigInteger) rightValue) <= 0;
		case REAL:
			return ((BigDecimal) leftValue).compareTo((BigDecimal) rightValue) <= 0;
		case STRING:
		case BOOLEAN:
			new LogError("Can't do lower-equals operation with " + type.toString().toLowerCase() + "s:\t" + leftValue
					+ " " + rightValue + " <=", fileName, line);
		case ANY:
			return overloadOperator(leftValue, rightValue, "$le", (byte) 2, fileName, line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue + " " + rightValue + " <=", fileName, line);
		default:
			return null;
		}
	}

	public final static Object lowerOperator(Object leftValue, Object rightValue, DataTypes type, String fileName,
			int line) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).compareTo((BigInteger) rightValue) < 0;
		case REAL:
			return ((BigDecimal) leftValue).compareTo((BigDecimal) rightValue) < 0;
		case STRING:
		case BOOLEAN:
			new LogError("Can't do lower operation with " + type.toString().toLowerCase() + "s:\t" + leftValue + " "
					+ rightValue + " <", fileName, line);
		case ANY:
			return overloadOperator(leftValue, rightValue, "$lt", (byte) 2, fileName, line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue + " " + rightValue + " <", fileName, line);
		default:
			return null;
		}
	}

	public final static Object moduloOperator(Object leftValue, Object rightValue, DataTypes type, String fileName,
			int line) {
		switch (type) {
		case INTEGER:
			var rightI = (BigInteger) rightValue;
			if (rightI.equals(BigInteger.ZERO))
				return BigInteger.ZERO;
			return ((BigInteger) leftValue).remainder(rightI);
		case REAL:
			var rightF = (BigDecimal) rightValue;
			if (rightF.equals(BigDecimal.ZERO))
				return BigDecimal.ZERO;
			return ((BigDecimal) leftValue).remainder(rightF);
		case STRING:
		case BOOLEAN:
			new LogError("Can't do modulo operation on " + type.toString().toLowerCase() + "s:\t" + leftValue + " "
					+ rightValue + " %", fileName, line);
		case ANY:
			return overloadOperator(leftValue, rightValue, "$mod", (byte) 2, fileName, line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue + " " + rightValue + " %", fileName, line);
		default:
			return null;
		}
	}

	public final static Object multiplicationOperator(Object leftValue, Object rightValue, DataTypes type,
			String fileName, int line) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).multiply(((BigInteger) rightValue));
		case REAL:
			return ((BigDecimal) leftValue).multiply(((BigDecimal) rightValue));
		case STRING:
		case BOOLEAN:
			new LogError(
					"Can't multiply " + type.toString().toLowerCase() + "s:\t" + leftValue + " " + rightValue + " *",
					fileName, line);
		case ANY:
			return overloadOperator(leftValue, rightValue, "$mul", (byte) 2, fileName, line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue + " " + rightValue + " *", fileName, line);
		default:
			return null;
		}
	}

	public final static Object negationOperator(Object value, DataTypes type, String fileName, int line) {
		switch (type) {
		case INTEGER:
			return BigInteger.ZERO.subtract((BigInteger) value);
		case REAL:
			return BigDecimal.ZERO.subtract((BigDecimal) value);
		case STRING:
			return reverse((String) value);
		case BOOLEAN:
			return !(boolean) value;
		case ANY:
			return overloadOperator(null, value, "$not", (byte) 1, fileName, line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + value + " !", fileName, line);
		default:
			return null;
		}
	}

	public final static Object notEqualsOperator(Object leftValue, Object rightValue, DataTypes type, String fileName,
			int line) {
		switch (type) {
		case REAL:
			return ((BigDecimal) leftValue).compareTo((BigDecimal) rightValue) != 0;
		case INTEGER:
		case BOOLEAN:
		case ANY:
		case STRING:
			return !leftValue.equals(rightValue);
		case VOID:
			new LogError("Void hasn't got any value:\t" + rightValue + " !=", fileName, line);
		default:
			return null;
		}
	}

	public final static Object orOperator(Object leftValue, Object rightValue, DataTypes type, String fileName,
			int line) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).or((BigInteger) rightValue);
		case BOOLEAN:
			return ((Boolean) leftValue) || ((Boolean) rightValue);
		case REAL:
		case STRING:
			new LogError("Can't do or operation with " + type.toString().toLowerCase() + "s:\t " + leftValue + " "
					+ rightValue + " |", fileName, line);
		case ANY:
			return overloadOperator(leftValue, rightValue, "$or", (byte) 2, fileName, line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue + " " + rightValue + " |", fileName, line);
		default:
			return null;
		}
	}

	public final static Object shiftLeftOperator(Object leftValue, Object rightValue, DataTypes type, String fileName,
			int line) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).shiftLeft(((BigInteger) rightValue).intValue());
		case STRING:
			var str = (String) leftValue;
			return str.substring(0, str.length() - ((BigInteger) rightValue).intValue());
		case REAL:
		case BOOLEAN:
			new LogError("Can't shift left " + type.toString().toLowerCase() + "s:\t " + leftValue + " " + rightValue
					+ " <<", fileName, line);
		case ANY:
			return overloadOperator(leftValue, rightValue, "$shl", (byte) 2, fileName, line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue + " " + rightValue + " <<", fileName, line);
		default:
			return null;
		}
	}

	public final static Object shiftRightOperator(Object leftValue, Object rightValue, DataTypes type, String fileName,
			int line) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).shiftRight(((BigInteger) rightValue).intValue());
		case REAL:
		case STRING:
		case BOOLEAN:
			new LogError("Can't shift right " + type.toString().toLowerCase() + "s:\t " + leftValue + " " + rightValue
					+ " >>", fileName, line);
		case ANY:
			return overloadOperator(leftValue, rightValue, "$shr", (byte) 2, fileName, line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue + " " + rightValue + " >>", fileName, line);
		default:
			return null;
		}
	}

	public final static Object subtractionOperator(Object leftValue, Object rightValue, DataTypes type, String fileName,
			int line) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).subtract(((BigInteger) rightValue));
		case REAL:
			return ((BigDecimal) leftValue).subtract(((BigDecimal) rightValue));
		case STRING:
		case BOOLEAN:
			new LogError(
					"Can't subtract " + type.toString().toLowerCase() + "s:\t" + leftValue + " " + rightValue + " -",
					fileName, line);
		case ANY:
			return overloadOperator(leftValue, rightValue, "$sub", (byte) 2, fileName, line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue + " " + rightValue + " -", fileName, line);
		default:
			return null;
		}
	}

	public final static Object xorOperator(Object leftValue, Object rightValue, DataTypes type, String fileName,
			int line) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).xor((BigInteger) rightValue);
		case REAL:
		case STRING:
		case BOOLEAN:
			new LogError("Can't do xor operation with " + type.toString().toString().toLowerCase() + "s:\t " + leftValue
					+ " " + rightValue + " ^", fileName, line);
		case ANY:
			return overloadOperator(leftValue, rightValue, "$xor", (byte) 2, fileName, line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue + " " + rightValue + " ^", fileName, line);
		default:
			return null;
		}
	}

	public final static Object assignmentAdditionOperator(Object leftValue, Object rightValue, DataTypes type,
			String fileName, int line) {
		var variable = VariableDeclarationNode.toMe(leftValue, fileName, line);
		switch (type) {
		case INTEGER:
		case REAL:
		case STRING:
			variable.setValue(additionOperator(variable.getValue(), rightValue, type, fileName, line), fileName, line);
			return variable.getValue();
		case BOOLEAN:
			new LogError("Can't add booleans:\t" + variable.getName() + " " + rightValue + " +=", fileName, line);
		case ANY:
			return overloadOperator(leftValue, rightValue, "$a_add", (byte) 2, fileName, line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + variable.getName() + " " + rightValue + " +=", fileName,
					line);
		default:
			return null;
		}
	}

	public final static Object assignmentAndOperator(Object leftValue, Object rightValue, DataTypes type,
			String fileName, int line) {
		var variable = VariableDeclarationNode.toMe(leftValue, fileName, line);
		switch (type) {
		case INTEGER:
		case REAL:
		case STRING:
			variable.setValue(andOperator(variable.getValue(), rightValue, type, fileName, line), fileName, line);
			return variable.getValue();
		case ANY:
			return overloadOperator(leftValue, rightValue, "$a_and", (byte) 2, fileName, line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + variable.getName() + " " + rightValue + " &=", fileName,
					line);
		default:
			return null;
		}
	}

	public final static Object assignmentDivisionOperator(Object leftValue, Object rightValue, DataTypes type,
			String fileName, int line) {
		var variable = VariableDeclarationNode.toMe(leftValue, fileName, line);
		switch (type) {
		case INTEGER:
		case REAL:
		case STRING:
			variable.setValue(divisionOperator(variable.getValue(), rightValue, type, fileName, line), fileName, line);
			return variable.getValue();
		case ANY:
			return overloadOperator(leftValue, rightValue, "$a_div", (byte) 2, fileName, line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + variable.getName() + " " + rightValue + " /=", fileName,
					line);
		default:
			return null;
		}
	}

	public final static Object assignmentMultiplicationOperator(Object leftValue, Object rightValue, DataTypes type,
			String fileName, int line) {
		var variable = VariableDeclarationNode.toMe(leftValue, fileName, line);
		switch (type) {
		case INTEGER:
		case REAL:
		case STRING:
			variable.setValue(multiplicationOperator(variable.getValue(), rightValue, type, fileName, line), fileName,
					line);
			return variable.getValue();
		case ANY:
			return overloadOperator(leftValue, rightValue, "$a_mul", (byte) 2, fileName, line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + variable.getName() + " " + rightValue + " *=", fileName,
					line);
		default:
			return null;
		}
	}

	public final static Object assignmentOperator(Object leftValue, Object rightValue, DataTypes type, String fileName,
			int line) {
		var variable = VariableDeclarationNode.toMe(leftValue, fileName, line);
		switch (type) {
		case INTEGER:
		case REAL:
		case STRING:
		case BOOLEAN:
		case ANY:
			variable.setValue(rightValue, fileName, line);
			return variable.getValue();
		case VOID:
			new LogError("Void hasn't got any value:\t" + variable.getName() + " " + rightValue + " =", fileName, line);
		default:
			return null;
		}
	}

	public final static Object assignmentOrOperator(Object leftValue, Object rightValue, DataTypes type,
			String fileName, int line) {
		var variable = VariableDeclarationNode.toMe(leftValue, fileName, line);
		switch (type) {
		case INTEGER:
		case REAL:
		case STRING:
			variable.setValue(orOperator(variable.getValue(), rightValue, type, fileName, line), fileName, line);
			return variable.getValue();
		case ANY:
			return overloadOperator(leftValue, rightValue, "$a_or", (byte) 2, fileName, line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + variable.getName() + " " + rightValue + " |=", fileName,
					line);
		default:
			return null;
		}
	}

	public final static Object assignmentShiftLeftOperator(Object leftValue, Object rightValue, DataTypes type,
			String fileName, int line) {
		var variable = VariableDeclarationNode.toMe(leftValue, fileName, line);
		switch (type) {
		case INTEGER:
		case REAL:
		case STRING:
			variable.setValue(shiftLeftOperator(variable.getValue(), rightValue, type, fileName, line), fileName, line);
			return variable.getValue();
		case ANY:
			return overloadOperator(leftValue, rightValue, "$a_shl", (byte) 2, fileName, line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + variable.getName() + " " + rightValue + " <<=", fileName,
					line);
		default:
			return null;
		}
	}

	public final static Object assignmentShiftRightOperator(Object leftValue, Object rightValue, DataTypes type,
			String fileName, int line) {
		var variable = VariableDeclarationNode.toMe(leftValue, fileName, line);
		switch (type) {
		case INTEGER:
		case REAL:
		case STRING:
			variable.setValue(shiftRightOperator(variable.getValue(), rightValue, type, fileName, line), fileName,
					line);
			return variable.getValue();
		case ANY:
			return overloadOperator(leftValue, rightValue, "$a_shr", (byte) 2, fileName, line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + variable.getName() + " " + rightValue + " >>=", fileName,
					line);
		default:
			return null;
		}
	}

	public final static Object assignmentSubtractionOperator(Object leftValue, Object rightValue, DataTypes type,
			String fileName, int line) {
		var variable = VariableDeclarationNode.toMe(leftValue, fileName, line);
		switch (type) {
		case INTEGER:
		case REAL:
		case STRING:
			variable.setValue(subtractionOperator(variable.getValue(), rightValue, type, fileName, line), fileName,
					line);
			return variable.getValue();
		case ANY:
			return overloadOperator(leftValue, rightValue, "$a_sub", (byte) 2, fileName, line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + variable.getName() + " " + rightValue + " -=", fileName,
					line);
		default:
			return null;
		}
	}

	public final static Object assignmentXorOperator(Object leftValue, Object rightValue, DataTypes type,
			String fileName, int line) {
		var variable = VariableDeclarationNode.toMe(leftValue, fileName, line);
		switch (type) {
		case INTEGER:
		case REAL:
		case STRING:
			variable.setValue(xorOperator(variable.getValue(), rightValue, type, fileName, line), fileName, line);
			return variable.getValue();
		case ANY:
			return overloadOperator(leftValue, rightValue, "$a_xor", (byte) 2, fileName, line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + variable.getName() + " " + rightValue + " ^=", fileName,
					line);
		default:
			return null;
		}
	}
}
