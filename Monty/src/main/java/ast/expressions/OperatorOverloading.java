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

import ast.declarations.VariableDeclarationNode;
import parser.DataTypes;
import parser.LogError;

public final class OperatorOverloading {

	public final static Object additionOperator(Object leftValue, Object rightValue, Object operator, DataTypes type,
			String fileName, int line) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).add(((BigInteger) rightValue));
		case REAL:
			return ((BigDecimal) leftValue).add(((BigDecimal) rightValue));
		case STRING:
			return leftValue.toString() + rightValue.toString();
		case BOOLEAN:
			new LogError("Can't add booleans:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString(), fileName, line);
		case ANY:
			new LogError("Can't do any operations besides assignment and comparison with \"any\" data type", fileName,
					line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString(), fileName, line);
		default:
			return null;
		}
	}

	public final static Object andOperator(Object leftValue, Object rightValue, Object operator, DataTypes type,
			String fileName, int line) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).and((BigInteger) rightValue);
		case BOOLEAN:
			return ((Boolean) leftValue) && ((Boolean) rightValue);
		case REAL:
		case STRING:
			new LogError("Can't do and operation with " + type.toString().toLowerCase() + "s:\t " + leftValue.toString()
					+ " " + rightValue.toString() + " " + operator.toString(), fileName, line);
		case ANY:
			new LogError("Can't do any operations besides assignment and comparison with \"any\" data type", fileName,
					line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString(), fileName, line);
		default:
			return null;
		}
	}

	public final static Object assignmentAdditionOperator(Object leftValue, Object rightValue, Object operator,
			DataTypes type, String fileName, int line) {
		var variable = ((VariableDeclarationNode) leftValue);
		switch (type) {
		case INTEGER:
		case REAL:
		case STRING:
			variable.setValue(additionOperator(variable.getValue(), rightValue, operator, type, fileName, line));
			return variable.getValue();
		case BOOLEAN:
			new LogError("Can't add booleans:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString(), fileName, line);
		case ANY:
			new LogError("Can't do any operations besides assignment and comparison with \"any\" data type", fileName,
					line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString(), fileName, line);
		default:
			return null;
		}
	}

	public final static Object assignmentAndOperator(Object leftValue, Object rightValue, Object operator,
			DataTypes type, String fileName, int line) {
		var variable = ((VariableDeclarationNode) leftValue);
		switch (type) {
		case INTEGER:
		case REAL:
		case STRING:
			variable.setValue(andOperator(variable.getValue(), rightValue, operator, type, fileName, line));
			return variable.getValue();
		case ANY:
			new LogError("Can't do any operations besides assignment and comparison with \"any\" data type", fileName,
					line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString(), fileName, line);
		default:
			return null;
		}
	}

	public final static Object assignmentDivisionOperator(Object leftValue, Object rightValue, Object operator,
			DataTypes type, String fileName, int line) {
		var variable = ((VariableDeclarationNode) leftValue);
		switch (type) {
		case INTEGER:
		case REAL:
		case STRING:
			variable.setValue(divisionOperator(variable.getValue(), rightValue, operator, type, fileName, line));
			return variable.getValue();
		case ANY:
			new LogError("Can't do any operations besides assignment and comparison with \"any\" data type", fileName,
					line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString(), fileName, line);
		default:
			return null;
		}
	}

	public final static Object assignmentMultiplicationOperator(Object leftValue, Object rightValue, Object operator,
			DataTypes type, String fileName, int line) {
		var variable = ((VariableDeclarationNode) leftValue);
		switch (type) {
		case INTEGER:
		case REAL:
		case STRING:
			variable.setValue(multiplicationOperator(variable.getValue(), rightValue, operator, type, fileName, line));
			return variable.getValue();
		case ANY:
			new LogError("Can't do any operations besides assignment and comparison with \"any\" data type", fileName,
					line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString(), fileName, line);
		default:
			return null;
		}
	}

	public final static Object assignmentOperator(Object leftValue, Object rightValue, Object operator, DataTypes type,
			String fileName, int line) {
		var variable = ((VariableDeclarationNode) leftValue);
		switch (type) {
		case INTEGER:
		case REAL:
		case STRING:
		case BOOLEAN:
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

	public final static Object assignmentOrOperator(Object leftValue, Object rightValue, Object operator,
			DataTypes type, String fileName, int line) {
		var variable = ((VariableDeclarationNode) leftValue);
		switch (type) {
		case INTEGER:
		case REAL:
		case STRING:
			variable.setValue(orOperator(variable.getValue(), rightValue, operator, type, fileName, line));
			return variable.getValue();
		case ANY:
			new LogError("Can't do any operations besides assignment and comparison with \"any\" data type", fileName,
					line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString(), fileName, line);
		default:
			return null;
		}
	}

	public final static Object assignmentShiftLeftOperator(Object leftValue, Object rightValue, Object operator,
			DataTypes type, String fileName, int line) {
		var variable = ((VariableDeclarationNode) leftValue);
		switch (type) {
		case INTEGER:
		case REAL:
		case STRING:
			variable.setValue(shiftLeftOperator(variable.getValue(), rightValue, operator, type, fileName, line));
			return variable.getValue();
		case ANY:
			new LogError("Can't do any operations besides assignment and comparison with \"any\" data type", fileName,
					line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString(), fileName, line);
		default:
			return null;
		}
	}

	public final static Object assignmentShiftRightOperator(Object leftValue, Object rightValue, Object operator,
			DataTypes type, String fileName, int line) {
		var variable = ((VariableDeclarationNode) leftValue);
		switch (type) {
		case INTEGER:
		case REAL:
		case STRING:
			variable.setValue(shiftRightOperator(variable.getValue(), rightValue, operator, type, fileName, line));
			return variable.getValue();
		case ANY:
			new LogError("Can't do any operations besides assignment and comparison with \"any\" data type", fileName,
					line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString(), fileName, line);
		default:
			return null;
		}
	}

	public final static Object assignmentSubtractionOperator(Object leftValue, Object rightValue, Object operator,
			DataTypes type, String fileName, int line) {
		var variable = ((VariableDeclarationNode) leftValue);
		switch (type) {
		case INTEGER:
		case REAL:
		case STRING:
			variable.setValue(subtractionOperator(variable.getValue(), rightValue, operator, type, fileName, line));
			return variable.getValue();
		case ANY:
			new LogError("Can't do any operations besides assignment and comparison with \"any\" data type", fileName,
					line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString(), fileName, line);
		default:
			return null;
		}
	}

	public final static Object assignmentXorOperator(Object leftValue, Object rightValue, Object operator,
			DataTypes type, String fileName, int line) {
		var variable = ((VariableDeclarationNode) leftValue);
		switch (type) {
		case INTEGER:
		case REAL:
		case STRING:
			variable.setValue(xorOperator(variable.getValue(), rightValue, operator, type, fileName, line));
			return variable.getValue();
		case ANY:
			new LogError("Can't do any operations besides assignment and comparison with \"any\" data type", fileName,
					line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString(), fileName, line);
		default:
			return null;
		}
	}

	public final static Object divisionOperator(Object leftValue, Object rightValue, Object operator, DataTypes type,
			String fileName, int line) {
		switch (type) {
		case INTEGER:
			if (rightValue.equals(BigInteger.ZERO))
				new LogError("Can't divide by zero", fileName, line);
			return ((BigInteger) leftValue).divide(((BigInteger) rightValue));
		case REAL:
			if (((BigDecimal)rightValue).compareTo(BigDecimal.ZERO) == 0)
				new LogError("Can't divide by zero", fileName, line);
			return ((BigDecimal) leftValue).divide(((BigDecimal) rightValue), 100, RoundingMode.HALF_UP);
		case STRING:
		case BOOLEAN:
			new LogError("Can't divide " + type.toString().toLowerCase() + "s:\t" + leftValue.toString() + " "
					+ rightValue.toString() + " " + operator.toString(), fileName, line);
		case ANY:
			new LogError("Can't do any operations besides assignment and comparison with \"any\" data type", fileName,
					line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString(), fileName, line);
		default:
			return null;
		}
	}

	public final static Object equalsOperator(Object leftValue, Object rightValue, Object operator, DataTypes type,
			String fileName, int line) {
		switch (type) {
		case REAL:
			return ((BigDecimal) leftValue).compareTo((BigDecimal) rightValue) == 0;
		case INTEGER:
		case BOOLEAN:
		case STRING:
		case ANY:
			return leftValue.equals(rightValue);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString(), fileName, line);
		default:
			return null;
		}
	}

	public final static Object greaterEqualsOperator(Object leftValue, Object rightValue, Object operator,
			DataTypes type, String fileName, int line) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).compareTo((BigInteger) rightValue) >= 0;
		case REAL:
			return ((BigDecimal) leftValue).compareTo((BigDecimal) rightValue) >= 0;
		case STRING:
		case BOOLEAN:
			new LogError(
					"Can't do greater-equals operation with " + type.toString().toLowerCase() + "s:\t"
							+ leftValue.toString() + " " + rightValue.toString() + " " + operator.toString(),
					fileName, line);
		case ANY:
			new LogError("Can't do any operations besides assignment and comparison with \"any\" data type", fileName,
					line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString(), fileName, line);
		default:
			return null;
		}
	}

	public final static Object greaterOperator(Object leftValue, Object rightValue, Object operator, DataTypes type,
			String fileName, int line) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).compareTo((BigInteger) rightValue) > 0;
		case REAL:
			return ((BigDecimal) leftValue).compareTo((BigDecimal) rightValue) > 0;
		case STRING:
		case BOOLEAN:
			new LogError("Can't do greater operation with " + type.toString().toLowerCase() + "s:\t"
					+ leftValue.toString() + " " + rightValue.toString() + " " + operator.toString(), fileName, line);
		case ANY:
			new LogError("Can't do any operations besides assignment and comparison with \"any\" data type", fileName,
					line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString(), fileName, line);
		default:
			return null;
		}
	}

	public final static Object lowerEqualsOperator(Object leftValue, Object rightValue, Object operator, DataTypes type,
			String fileName, int line) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).compareTo((BigInteger) rightValue) <= 0;
		case REAL:
			return ((BigDecimal) leftValue).compareTo((BigDecimal) rightValue) <= 0;
		case STRING:
		case BOOLEAN:
			new LogError(
					"Can't do lower-equals operation with " + type.toString().toLowerCase() + "s:\t"
							+ leftValue.toString() + " " + rightValue.toString() + " " + operator.toString(),
					fileName, line);
		case ANY:
			new LogError("Can't do any operations besides assignment and comparison with \"any\" data type", fileName,
					line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString(), fileName, line);
		default:
			return null;
		}
	}

	public final static Object lowerOperator(Object leftValue, Object rightValue, Object operator, DataTypes type,
			String fileName, int line) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).compareTo((BigInteger) rightValue) < 0;
		case REAL:
			return ((BigDecimal) leftValue).compareTo((BigDecimal) rightValue) < 0;
		case STRING:
		case BOOLEAN:
			new LogError("Can't do lower operation with " + type.toString().toLowerCase() + "s:\t"
					+ leftValue.toString() + " " + rightValue.toString() + " " + operator.toString(), fileName, line);
		case ANY:
			new LogError("Can't do any operations besides assignment and comparison with \"any\" data type", fileName,
					line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString(), fileName, line);
		default:
			return null;
		}
	}

	public final static Object moduloOperator(Object leftValue, Object rightValue, Object operator, DataTypes type,
			String fileName, int line) {
		switch (type) {
		case INTEGER:
			var rightI = (BigInteger) rightValue;
			if (rightI.equals(BigInteger.ZERO))
				return BigInteger.ZERO;
			return ((BigInteger) leftValue).mod(rightI);
		case REAL:
			var rightF = (BigDecimal) rightValue;
			if (rightF.equals(BigDecimal.ZERO))
				return BigDecimal.ZERO;
			return ((BigDecimal) leftValue).remainder(rightF);
		case STRING:
		case BOOLEAN:
			new LogError("Can't do modulo operation on " + type.toString().toLowerCase() + "s:\t" + leftValue.toString()
					+ " " + rightValue.toString() + " " + operator.toString(), fileName, line);
		case ANY:
			new LogError("Can't do any operations besides assignment and comparison with \"any\" data type", fileName,
					line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString(), fileName, line);
		default:
			return null;
		}
	}

	public final static Object multiplicationOperator(Object leftValue, Object rightValue, Object operator,
			DataTypes type, String fileName, int line) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).multiply(((BigInteger) rightValue));
		case REAL:
			return ((BigDecimal) leftValue).multiply(((BigDecimal) rightValue));
		case STRING:
		case BOOLEAN:
			new LogError("Can't multiply " + type.toString().toLowerCase() + "s:\t" + leftValue.toString() + " "
					+ rightValue.toString() + " " + operator.toString(), fileName, line);
		case ANY:
			new LogError("Can't do any operations besides assignment and comparison with \"any\" data type", fileName,
					line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString(), fileName, line);
		default:
			return null;
		}
	}

	public final static Object negationOperator(Object leftValue, Object rightValue, Object operator, DataTypes type,
			String fileName, int line) {
		switch (type) {
		case INTEGER:
			return BigInteger.ZERO.subtract((BigInteger) rightValue);
		case REAL:
			return BigDecimal.ZERO.subtract((BigDecimal) rightValue);
		case STRING:
			return reverse(rightValue.toString());
		case BOOLEAN:
			return !(boolean) rightValue;
		case ANY:
			new LogError("Can't do any operations besides assignment and comparison with \"any\" data type", fileName,
					line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString(), fileName,
					line);
		default:
			return null;
		}
	}

	public final static Object notEqualsOperator(Object leftValue, Object rightValue, Object operator, DataTypes type,
			String fileName, int line) {
		switch (type) {
		case REAL:
			return ((BigDecimal) leftValue).compareTo((BigDecimal) rightValue) != 0;
		case INTEGER:
		case BOOLEAN:
		case STRING:
		case ANY:
			return !leftValue.equals(rightValue);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString(), fileName, line);
		default:
			return null;
		}
	}

	public final static Object orOperator(Object leftValue, Object rightValue, Object operator, DataTypes type,
			String fileName, int line) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).or((BigInteger) rightValue);
		case BOOLEAN:
			return ((Boolean) leftValue) || ((Boolean) rightValue);
		case REAL:
		case STRING:
			new LogError("Can't do or operation with " + type.toString().toLowerCase() + "s:\t " + leftValue.toString()
					+ " " + rightValue.toString() + " " + operator.toString(), fileName, line);
		case ANY:
			new LogError("Can't do any operations besides assignment and comparison with \"any\" data type", fileName,
					line);
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

	public final static Object shiftLeftOperator(Object leftValue, Object rightValue, Object operator, DataTypes type,
			String fileName, int line) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).shiftLeft(((BigInteger) rightValue).intValue());
		case STRING:
			var str = leftValue.toString();
			return str.substring(0, str.length() - ((BigInteger) rightValue).intValue());
		case REAL:
		case BOOLEAN:
			new LogError("Can't shift left " + type.toString().toLowerCase() + "s:\t " + leftValue.toString() + " "
					+ rightValue.toString() + " " + operator.toString(), fileName, line);
		case ANY:
			new LogError("Can't do any operations besides assignment and comparison with \"any\" data type", fileName,
					line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString(), fileName, line);
		default:
			return null;
		}
	}

	public final static Object shiftRightOperator(Object leftValue, Object rightValue, Object operator, DataTypes type,
			String fileName, int line) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).shiftRight(((BigInteger) rightValue).intValue());
		case REAL:
		case STRING:
		case BOOLEAN:
			new LogError("Can't shift right " + type.toString().toLowerCase() + "s:\t " + leftValue.toString() + " "
					+ rightValue.toString() + " " + operator.toString(), fileName, line);
		case ANY:
			new LogError("Can't do any operations besides assignment and comparison with \"any\" data type", fileName,
					line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString(), fileName, line);
		default:
			return null;
		}
	}

	public final static Object subtractionOperator(Object leftValue, Object rightValue, Object operator, DataTypes type,
			String fileName, int line) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).subtract(((BigInteger) rightValue));
		case REAL:
			return ((BigDecimal) leftValue).subtract(((BigDecimal) rightValue));
		case STRING:
		case BOOLEAN:
			new LogError("Can't subtract " + type.toString().toLowerCase() + "s:\t" + leftValue.toString() + " "
					+ rightValue.toString() + " " + operator.toString(), fileName, line);
		case ANY:
			new LogError("Can't do any operations besides assignment and comparison with \"any\" data type", fileName,
					line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString(), fileName, line);
		default:
			return null;
		}
	}

	public final static Object xorOperator(Object leftValue, Object rightValue, Object operator, DataTypes type,
			String fileName, int line) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).xor((BigInteger) rightValue);
		case REAL:
		case STRING:
		case BOOLEAN:
			new LogError("Can't do xor operation with " + type.toString().toLowerCase() + "s:\t " + leftValue.toString()
					+ " " + rightValue.toString() + " " + operator.toString(), fileName, line);
		case ANY:
			new LogError("Can't do any operations besides assignment and comparison with \"any\" data type", fileName,
					line);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString(), fileName, line);
		default:
			return null;
		}
	}
}
