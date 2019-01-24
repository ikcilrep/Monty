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

import ast.declarations.VariableDeclarationNode;
import parser.DataTypes;
import parser.LogError;
import sml.data.array.Array;
import sml.data.list.List;
import sml.data.stack.Stack;

public class OperatorOverloading {

	public static Object additionOperator(Object leftValue, Object rightValue, Object operator, DataTypes type,
			String fileName, int line) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).add(((BigInteger) rightValue));
		case FLOAT:
			return ((BigDecimal) leftValue).add(((BigDecimal) rightValue));
		case STRING:
			return leftValue.toString() + rightValue.toString();
		case BOOLEAN:
			new LogError("Can't add booleans:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString(), fileName, line);
		case ARRAY:
			return ((Array) leftValue).copy().append(rightValue);
		case LIST:
			return ((List) leftValue).copy().append(rightValue);
		case STACK:
			return ((Stack) leftValue).copy().push(rightValue);
		case ANY:
			new LogError("Can't do any operations with \"any\" data type", fileName, line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString(), fileName, line);
		default:
			return null;
		}
	}

	public static Object andOperator(Object leftValue, Object rightValue, Object operator, DataTypes type,
			String fileName, int line) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).and((BigInteger) rightValue);
		case BOOLEAN:
			return ((Boolean) leftValue) && ((Boolean) rightValue);
		case FLOAT:
		case STRING:
		case ARRAY:
		case LIST:
		case STACK:
			new LogError("Can't do and operation with " + type.toString().toLowerCase() + "s:\t " + leftValue.toString()
					+ " " + rightValue.toString() + " " + operator.toString(), fileName, line);
		case ANY:
			new LogError("Can't do any operations with \"any\" data type", fileName, line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString(), fileName, line);
		default:
			return null;
		}
	}

	public static Object assignmentAdditionOperator(Object leftValue, Object rightValue, Object operator,
			DataTypes type, String fileName, int line) {
		var variable = ((VariableDeclarationNode) leftValue);
		switch (type) {
		case INTEGER:
		case FLOAT:
		case STRING:
		case ARRAY:
		case LIST:
		case STACK:
			variable.setValue(additionOperator(variable.getValue(), rightValue, operator, type, fileName, line));
			return variable.getValue();
		case BOOLEAN:
			new LogError("Can't add booleans:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString(), fileName, line);
		case ANY:
			new LogError("Can't do any operations with \"any\" data type", fileName, line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString(), fileName, line);
		default:
			return null;
		}
	}

	public static Object assignmentAndOperator(Object leftValue, Object rightValue, Object operator, DataTypes type,
			String fileName, int line) {
		var variable = ((VariableDeclarationNode) leftValue);
		switch (type) {
		case INTEGER:
		case FLOAT:
		case STRING:
		case ARRAY:
		case LIST:
		case STACK:
			variable.setValue(andOperator(variable.getValue(), rightValue, operator, type, fileName, line));
			return variable.getValue();
		case ANY:
			new LogError("Can't do any operations with \"any\" data type", fileName, line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString(), fileName, line);
		default:
			return null;
		}
	}

	public static Object assignmentDivisionOperator(Object leftValue, Object rightValue, Object operator,
			DataTypes type, String fileName, int line) {
		var variable = ((VariableDeclarationNode) leftValue);
		switch (type) {
		case INTEGER:
		case FLOAT:
		case STRING:
		case ARRAY:
		case LIST:
		case STACK:
			variable.setValue(divisionOperator(variable.getValue(), rightValue, operator, type, fileName, line));
			return variable.getValue();
		case ANY:
			new LogError("Can't do any operations with \"any\" data type", fileName, line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString(), fileName, line);
		default:
			return null;
		}
	}

	public static Object assignmentMultiplicationOperator(Object leftValue, Object rightValue, Object operator,
			DataTypes type, String fileName, int line) {
		var variable = ((VariableDeclarationNode) leftValue);
		switch (type) {
		case INTEGER:
		case FLOAT:
		case STRING:
		case ARRAY:
		case LIST:
		case STACK:
			variable.setValue(multiplicationOperator(variable.getValue(), rightValue, operator, type, fileName, line));
			return variable.getValue();
		case ANY:
			new LogError("Can't do any operations with \"any\" data type", fileName, line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString(), fileName, line);
		default:
			return null;
		}
	}

	public static Object assignmentOperator(Object leftValue, Object rightValue, Object operator, DataTypes type,
			String fileName, int line) {
		var variable = ((VariableDeclarationNode) leftValue);
		switch (type) {
		case INTEGER:
		case FLOAT:
		case STRING:
		case BOOLEAN:
		case LIST:
		case ARRAY:
		case STACK:
		case ANY:
			variable.setValue(rightValue);
			return variable.getValue();
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString(), fileName, line);
		default:
			return null;
		}
	}

	public static Object assignmentOrOperator(Object leftValue, Object rightValue, Object operator, DataTypes type,
			String fileName, int line) {
		var variable = ((VariableDeclarationNode) leftValue);
		switch (type) {
		case INTEGER:
		case FLOAT:
		case STRING:
		case ARRAY:
		case LIST:
		case STACK:
			variable.setValue(orOperator(variable.getValue(), rightValue, operator, type, fileName, line));
			return variable.getValue();
		case ANY:
			new LogError("Can't do any operations with \"any\" data type", fileName, line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString(), fileName, line);
		default:
			return null;
		}
	}

	public static Object assignmentShiftLeftOperator(Object leftValue, Object rightValue, Object operator,
			DataTypes type, String fileName, int line) {
		var variable = ((VariableDeclarationNode) leftValue);
		switch (type) {
		case INTEGER:
		case FLOAT:
		case STRING:
		case ARRAY:
		case LIST:
		case STACK:
			variable.setValue(shiftLeftOperator(variable.getValue(), rightValue, operator, type, fileName, line));
			return variable.getValue();
		case ANY:
			new LogError("Can't do any operations with \"any\" data type", fileName, line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString(), fileName, line);
		default:
			return null;
		}
	}

	public static Object assignmentShiftRightOperator(Object leftValue, Object rightValue, Object operator,
			DataTypes type, String fileName, int line) {
		var variable = ((VariableDeclarationNode) leftValue);
		switch (type) {
		case INTEGER:
		case FLOAT:
		case STRING:
		case ARRAY:
		case LIST:
		case STACK:
			variable.setValue(shiftRightOperator(variable.getValue(), rightValue, operator, type, fileName, line));
			return variable.getValue();
		case ANY:
			new LogError("Can't do any operations with \"any\" data type", fileName, line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString(), fileName, line);
		default:
			return null;
		}
	}

	public static Object assignmentSubtractionOperator(Object leftValue, Object rightValue, Object operator,
			DataTypes type, String fileName, int line) {
		var variable = ((VariableDeclarationNode) leftValue);
		switch (type) {
		case INTEGER:
		case FLOAT:
		case STRING:
		case ARRAY:
		case LIST:
		case STACK:
			variable.setValue(subtractionOperator(variable.getValue(), rightValue, operator, type, fileName, line));
			return variable.getValue();
		case ANY:
			new LogError("Can't do any operations with \"any\" data type", fileName, line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString(), fileName, line);
		default:
			return null;
		}
	}

	public static Object assignmentXorOperator(Object leftValue, Object rightValue, Object operator, DataTypes type,
			String fileName, int line) {
		var variable = ((VariableDeclarationNode) leftValue);
		switch (type) {
		case INTEGER:
		case FLOAT:
		case STRING:
		case ARRAY:
		case LIST:
		case STACK:
			variable.setValue(xorOperator(variable.getValue(), rightValue, operator, type, fileName, line));
			return variable.getValue();
		case ANY:
			new LogError("Can't do any operations with \"any\" data type", fileName, line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString(), fileName, line);
		default:
			return null;
		}
	}

	public static Object divisionOperator(Object leftValue, Object rightValue, Object operator, DataTypes type,
			String fileName, int line) {

		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).divide(((BigInteger) rightValue));
		case FLOAT:
			return ((BigDecimal) leftValue).divide(((BigDecimal) rightValue));
		case STRING:
		case BOOLEAN:
		case ARRAY:
		case LIST:
		case STACK:
			new LogError("Can't divide " + type.toString().toLowerCase() + "s:\t" + leftValue.toString() + " "
					+ rightValue.toString() + " " + operator.toString(), fileName, line);
		case ANY:
			new LogError("Can't do any operations with \"any\" data type", fileName, line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString(), fileName, line);
		default:
			return null;
		}
	}

	public static Object equalsOperator(Object leftValue, Object rightValue, Object operator, DataTypes type,
			String fileName, int line) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).compareTo((BigInteger) rightValue) == 0;
		case FLOAT:
			return ((BigDecimal) leftValue).compareTo((BigDecimal) rightValue) == 0;
		case BOOLEAN:
			return ((Boolean) leftValue).compareTo((Boolean) rightValue) == 0;
		case STRING:
		case LIST:
		case STACK:
		case ARRAY:
			return leftValue.equals(rightValue);
		case ANY:
			new LogError("Can't do any operations with \"any\" data type", fileName, line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString(), fileName, line);
		default:
			return null;
		}
	}

	public static Object greaterEqualsOperator(Object leftValue, Object rightValue, Object operator, DataTypes type,
			String fileName, int line) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).compareTo((BigInteger) rightValue) >= 0;
		case FLOAT:
			return ((BigDecimal) leftValue).compareTo((BigDecimal) rightValue) >= 0;
		case STRING:
		case BOOLEAN:
		case ARRAY:
		case LIST:
		case STACK:
			new LogError(
					"Can't do greater-equals operation with " + type.toString().toLowerCase() + "s:\t"
							+ leftValue.toString() + " " + rightValue.toString() + " " + operator.toString(),
					fileName, line);
		case ANY:
			new LogError("Can't do any operations with \"any\" data type", fileName, line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString(), fileName, line);
		default:
			return null;
		}
	}

	public static Object greaterOperator(Object leftValue, Object rightValue, Object operator, DataTypes type,
			String fileName, int line) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).compareTo((BigInteger) rightValue) > 0;
		case FLOAT:
			return ((BigDecimal) leftValue).compareTo((BigDecimal) rightValue) > 0;
		case STRING:
		case BOOLEAN:
		case ARRAY:
		case LIST:
		case STACK:
			new LogError("Can't do greater operation with " + type.toString().toLowerCase() + "s:\t"
					+ leftValue.toString() + " " + rightValue.toString() + " " + operator.toString(), fileName, line);
		case ANY:
			new LogError("Can't do any operations with \"any\" data type", fileName, line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString(), fileName, line);
		default:
			return null;
		}
	}

	public static Object lowerEqualsOperator(Object leftValue, Object rightValue, Object operator, DataTypes type,
			String fileName, int line) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).compareTo((BigInteger) rightValue) <= 0;
		case FLOAT:
			return ((BigDecimal) leftValue).compareTo((BigDecimal) rightValue) <= 0;
		case STRING:
		case BOOLEAN:
		case ARRAY:
		case LIST:
		case STACK:
			new LogError(
					"Can't do lower-equals operation with " + type.toString().toLowerCase() + "s:\t"
							+ leftValue.toString() + " " + rightValue.toString() + " " + operator.toString(),
					fileName, line);
		case ANY:
			new LogError("Can't do any operations with \"any\" data type", fileName, line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString(), fileName, line);
		default:
			return null;
		}
	}

	public static Object lowerOperator(Object leftValue, Object rightValue, Object operator, DataTypes type,
			String fileName, int line) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).compareTo((BigInteger) rightValue) < 0;
		case FLOAT:
			return ((BigDecimal) leftValue).compareTo((BigDecimal) rightValue) < 0;
		case STRING:
		case BOOLEAN:
		case ARRAY:
		case LIST:
		case STACK:
			new LogError("Can't do lower operation with " + type.toString().toLowerCase() + "s:\t"
					+ leftValue.toString() + " " + rightValue.toString() + " " + operator.toString(), fileName, line);
		case ANY:
			new LogError("Can't do any operations with \"any\" data type", fileName, line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString(), fileName, line);
		default:
			return null;
		}
	}

	public static Object moduloOperator(Object leftValue, Object rightValue, Object operator, DataTypes type,
			String fileName, int line) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).remainder((BigInteger) rightValue);
		case FLOAT:
			return ((BigDecimal) leftValue).remainder((BigDecimal) rightValue);
		case STRING:
		case BOOLEAN:
		case ARRAY:
		case LIST:
		case STACK:
			new LogError("Can't do modulo operation on " + type.toString().toLowerCase() + "s:\t" + leftValue.toString()
					+ " " + rightValue.toString() + " " + operator.toString(), fileName, line);
		case ANY:
			new LogError("Can't do any operations with \"any\" data type", fileName, line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString(), fileName, line);
		default:
			return null;
		}
	}

	public static Object multiplicationOperator(Object leftValue, Object rightValue, Object operator, DataTypes type,
			String fileName, int line) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).multiply(((BigInteger) rightValue));
		case FLOAT:
			return ((BigDecimal) leftValue).multiply(((BigDecimal) rightValue));
		case STRING:
		case BOOLEAN:
		case ARRAY:
		case LIST:
			new LogError("Can't multiply " + type.toString().toLowerCase() + "s:\t" + leftValue.toString() + " "
					+ rightValue.toString() + " " + operator.toString(), fileName, line);
		case ANY:
			new LogError("Can't do any operations with \"any\" data type", fileName, line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString(), fileName, line);
		default:
			return null;
		}
	}


	public static Object negationOperator(Object leftValue, Object rightValue, Object operator, DataTypes type,
			String fileName, int line) {
		switch (type) {
		case INTEGER:
			return BigInteger.ZERO.subtract((BigInteger) rightValue);
		case FLOAT:
			return BigDecimal.ZERO.subtract((BigDecimal) rightValue);
		case STRING:
			return reverse(rightValue.toString());
		case BOOLEAN:
			return !(boolean) rightValue;
		case ARRAY:
			return ((Array) rightValue).reversed();
		case LIST:
			return ((List) rightValue).reversed();
		case STACK:
			return ((Stack) rightValue).reversed();
		case ANY:
			new LogError("Can't do any operations with \"any\" data type", fileName, line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString(), fileName,
					line);
		default:
			return null;
		}
	}

	public static Object notEqualsOperator(Object leftValue, Object rightValue, Object operator, DataTypes type,
			String fileName, int line) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).compareTo((BigInteger) rightValue) != 0;
		case FLOAT:
			return ((BigDecimal) leftValue).compareTo((BigDecimal) rightValue) != 0;
		case BOOLEAN:
			return ((Boolean) leftValue).compareTo((Boolean) rightValue) != 0;
		case STRING:
		case LIST:
		case STACK:
		case ARRAY:
			return !leftValue.equals(rightValue);
		case ANY:
			new LogError("Can't do any operations with \"any\" data type", fileName, line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString(), fileName, line);
		default:
			return null;
		}
	}

	public static Object orOperator(Object leftValue, Object rightValue, Object operator, DataTypes type,
			String fileName, int line) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).or((BigInteger) rightValue);
		case BOOLEAN:
			return ((Boolean) leftValue) || ((Boolean) rightValue);
		case FLOAT:
		case STRING:
		case ARRAY:
		case LIST:
		case STACK:
			new LogError("Can't do or operation with " + type.toString().toLowerCase() + "s:\t " + leftValue.toString()
					+ " " + rightValue.toString() + " " + operator.toString(), fileName, line);
		case ANY:
			new LogError("Can't do any operations with \"any\" data type", fileName, line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString(), fileName, line);
		default:
			return null;
		}
	}

	private static String reverse(String str) {
		var result = new StringBuilder(str.length());
		var string = str.toString();
		for (int j = string.length() - 1; j >= 0; j--)
			result.append(string.charAt(j));
		return result.toString();
	}


	public static Object shiftLeftOperator(Object leftValue, Object rightValue, Object operator, DataTypes type,
			String fileName, int line) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).shiftLeft(((BigInteger) rightValue).intValue());
		case STRING:
			var str = leftValue.toString();
			return str.substring(0, str.length() - ((BigInteger) rightValue).intValue());
		case ARRAY:
			var arr = ((Array) leftValue);
			return arr.subarray(0, arr.length() - ((BigInteger) rightValue).intValue());
		case LIST:
			var lst = ((List) leftValue);
			return lst.sublist(0, lst.length() - ((BigInteger) rightValue).intValue());
		case STACK:
			var stack = ((Stack) leftValue).copy();
			var value = ((BigInteger) rightValue).intValue();
			for (int i = 0; i < value; i++) {
				stack.pop();
			}
			return stack;
		case FLOAT:
		case BOOLEAN:
			new LogError("Can't shift left " + type.toString().toLowerCase() + "s:\t " + leftValue.toString() + " "
					+ rightValue.toString() + " " + operator.toString(), fileName, line);
		case ANY:
			new LogError("Can't do any operations with \"any\" data type", fileName, line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString(), fileName, line);
		default:
			return null;
		}
	}


	public static Object shiftRightOperator(Object leftValue, Object rightValue, Object operator, DataTypes type,
			String fileName, int line) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).shiftRight(((BigInteger) rightValue).intValue());
		case FLOAT:
		case STRING:
		case BOOLEAN:
			new LogError("Can't shift right " + type.toString().toLowerCase() + "s:\t " + leftValue.toString() + " "
					+ rightValue.toString() + " " + operator.toString(), fileName, line);
		case STACK:
			var stack = ((Stack) leftValue).copy().reversed();
			var value = ((BigInteger) rightValue).intValue();
			for (int i = 0; i < value; i++)
				stack.pop();
			return stack.reversed();
		case ARRAY:
			var arr = ((Array) leftValue);
			return arr.copy().subarray(((BigInteger) rightValue).intValue(), arr.length());
		case LIST:
			var lst = ((List) leftValue);
			return lst.copy().sublist(((BigInteger) rightValue).intValue(), lst.length());
		case ANY:
			new LogError("Can't do any operations with \"any\" data type", fileName, line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString(), fileName, line);
		default:
			return null;
		}
	}

	public static Object subtractionOperator(Object leftValue, Object rightValue, Object operator, DataTypes type,
			String fileName, int line) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).subtract(((BigInteger) rightValue));
		case FLOAT:
			return ((BigDecimal) leftValue).subtract(((BigDecimal) rightValue));
		case STRING:
		case BOOLEAN:
		case ARRAY:
		case LIST:
		case STACK:
			new LogError("Can't subtract " + type.toString().toLowerCase() + "s:\t" + leftValue.toString() + " "
					+ rightValue.toString() + " " + operator.toString(), fileName, line);
		case ANY:
			new LogError("Can't do any operations with \"any\" data type", fileName, line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString(), fileName, line);
		default:
			return null;
		}
	}

	public static Object xorOperator(Object leftValue, Object rightValue, Object operator, DataTypes type,
			String fileName, int line) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).xor((BigInteger) rightValue);
		case FLOAT:
		case STRING:
		case BOOLEAN:
		case ARRAY:
		case LIST:
		case STACK:
			new LogError("Can't do xor operation with " + type.toString().toLowerCase() + "s:\t " + leftValue.toString()
					+ " " + rightValue.toString() + " " + operator.toString(), fileName, line);
		case ANY:
			new LogError("Can't do any operations with \"any\" data type", fileName, line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString(), fileName, line);
		default:
			return null;
		}
	}
}
