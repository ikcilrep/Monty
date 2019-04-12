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
	public static ArrayList<OperationNode> arguments;
	static {
		arguments = new ArrayList<>();
		arguments.add(new OperationNode(null, null));
		arguments.add(new OperationNode(null, null));
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
		arguments.get(0).setOperand(new ConstantNode(leftValue, DataTypes.ANY));
		arguments.get(1).setOperand(new ConstantNode(rightValue, DataTypes.ANY));
		if (leftValue instanceof StructDeclarationNode) {
			var struct = (StructDeclarationNode) leftValue;
			if (struct.hasFunction(nameOfFunction)) {
				var operator = struct.getFunction(nameOfFunction);
				if (!operator.getType().equals(DataTypes.VOID) && operator.getParameters().size() == numberOfParameters)
					return operator.call(arguments, temporaryFileName, temporaryLine);
			}
		} else if (!isAssignment && rightValue instanceof StructDeclarationNode) {
			var t = arguments.get(0);
			arguments.set(0, arguments.get(1));
			arguments.set(1, t);
			var struct = (StructDeclarationNode) rightValue;
			if (struct.hasFunction(nameOfFunction)) {
				var operator = struct.getFunction(nameOfFunction);
				if (!operator.getType().equals(DataTypes.VOID) && operator.getParameters().size() == numberOfParameters)
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
			return ((BigInteger) leftValue).add(((BigInteger) rightValue));
		case REAL:
			return ((BigDecimal) leftValue).add(((BigDecimal) rightValue));
		case STRING:
			return (String) leftValue + (String) rightValue;
		case BOOLEAN:
			new LogError("Can't add booleans:\t" + leftValue + " " + rightValue + " +", temporaryFileName,
					temporaryLine);
		case ANY:
			return overloadOperator(leftValue, rightValue, "$add", 2, false);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue + " " + rightValue + " +", temporaryFileName,
					temporaryLine);
		default:
			return null;
		}
	}

	public final static Object andOperator(Object leftValue, Object rightValue, DataTypes type) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).and((BigInteger) rightValue);
		case BOOLEAN:
			return ((Boolean) leftValue) && ((Boolean) rightValue);
		case REAL:
		case STRING:
			new LogError("Can't do and operation with " + type.toString().toLowerCase() + "s:\t " + leftValue + " "
					+ rightValue + " &", temporaryFileName, temporaryLine);
		case ANY:
			return overloadOperator(leftValue, rightValue, "$and", 2, false);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue + " " + rightValue + " &", temporaryFileName,
					temporaryLine);
		default:
			return null;
		}
	}

	public final static Object divisionOperator(Object leftValue, Object rightValue, DataTypes type) {
		switch (type) {
		case INTEGER:
			if (rightValue.equals(BigInteger.ZERO))
				new LogError("Can't divide by zero.", temporaryFileName, temporaryLine);
			return ((BigInteger) leftValue).divide(((BigInteger) rightValue));
		case REAL:
			if (((BigDecimal) rightValue).compareTo(BigDecimal.ZERO) == 0)
				new LogError("Can't divide by zero.", temporaryFileName, temporaryLine);
			try {
				return ((BigDecimal) leftValue).divide(((BigDecimal) rightValue));
			} catch (ArithmeticException e) {
				return ((BigDecimal) leftValue).divide(((BigDecimal) rightValue), ((BigDecimal) rightValue).intValue(),
						RoundingMode.HALF_UP);
			}
		case STRING:
		case BOOLEAN:
			new LogError("Can't divide " + type.toString().toLowerCase() + "s:\t" + leftValue + " " + rightValue + " /",
					temporaryFileName, temporaryLine);
		case ANY:
			return overloadOperator(leftValue, rightValue, "$div", 2, false);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue + " " + rightValue + " /", temporaryFileName,
					temporaryLine);
		default:
			return null;
		}
	}

	public final static Object equalsOperator(Object leftValue, Object rightValue, DataTypes type) {
		switch (type) {
		case REAL:
			return ((BigDecimal) leftValue).compareTo((BigDecimal) rightValue) == 0;
		case INTEGER:
		case BOOLEAN:
		case STRING:
		case ANY:
			return leftValue.equals(rightValue);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue + " " + rightValue + " ==", temporaryFileName,
					temporaryLine);
		default:
			return null;
		}
	}

	public final static Object greaterEqualsOperator(Object leftValue, Object rightValue, DataTypes type) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).compareTo((BigInteger) rightValue) >= 0;
		case REAL:
			return ((BigDecimal) leftValue).compareTo((BigDecimal) rightValue) >= 0;
		case STRING:
		case BOOLEAN:
			new LogError("Can't do greater-equals operation with " + type.toString().toLowerCase() + "s:\t" + leftValue
					+ " " + rightValue + " >=", temporaryFileName, temporaryLine);
		case ANY:
			return overloadOperator(leftValue, rightValue, "$ge", 2, false);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue + " " + rightValue + " >=", temporaryFileName,
					temporaryLine);
		default:
			return null;
		}
	}

	public final static Object greaterOperator(Object leftValue, Object rightValue, DataTypes type) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).compareTo((BigInteger) rightValue) > 0;
		case REAL:
			return ((BigDecimal) leftValue).compareTo((BigDecimal) rightValue) > 0;
		case STRING:
		case BOOLEAN:
			new LogError("Can't do greater operation with " + type.toString().toLowerCase() + "s:\t" + leftValue + " "
					+ rightValue + " >", temporaryFileName, temporaryLine);
		case ANY:
			return overloadOperator(leftValue, rightValue, "$gt", 2, false);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue + " " + rightValue + " >", temporaryFileName,
					temporaryLine);
		default:
			return null;
		}
	}

	public final static Object lowerEqualsOperator(Object leftValue, Object rightValue, DataTypes type) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).compareTo((BigInteger) rightValue) <= 0;
		case REAL:
			return ((BigDecimal) leftValue).compareTo((BigDecimal) rightValue) <= 0;
		case STRING:
		case BOOLEAN:
			new LogError("Can't do lower-equals operation with " + type.toString().toLowerCase() + "s:\t" + leftValue
					+ " " + rightValue + " <=", temporaryFileName, temporaryLine);
		case ANY:
			return overloadOperator(leftValue, rightValue, "$le", 2, false);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue + " " + rightValue + " <=", temporaryFileName,
					temporaryLine);
		default:
			return null;
		}
	}

	public final static Object lowerOperator(Object leftValue, Object rightValue, DataTypes type) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).compareTo((BigInteger) rightValue) < 0;
		case REAL:
			return ((BigDecimal) leftValue).compareTo((BigDecimal) rightValue) < 0;
		case STRING:
		case BOOLEAN:
			new LogError("Can't do lower operation with " + type.toString().toLowerCase() + "s:\t" + leftValue + " "
					+ rightValue + " <", temporaryFileName, temporaryLine);
		case ANY:
			return overloadOperator(leftValue, rightValue, "$lt", 2, false);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue + " " + rightValue + " <", temporaryFileName,
					temporaryLine);
		default:
			return null;
		}
	}

	public final static Object moduloOperator(Object leftValue, Object rightValue, DataTypes type) {
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
					+ rightValue + " %", temporaryFileName, temporaryLine);
		case ANY:
			return overloadOperator(leftValue, rightValue, "$mod", 2, false);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue + " " + rightValue + " %", temporaryFileName,
					temporaryLine);
		default:
			return null;
		}
	}

	public final static Object multiplicationOperator(Object leftValue, Object rightValue, DataTypes type) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).multiply(((BigInteger) rightValue));
		case REAL:
			return ((BigDecimal) leftValue).multiply(((BigDecimal) rightValue));
		case STRING:
		case BOOLEAN:
			new LogError(
					"Can't multiply " + type.toString().toLowerCase() + "s:\t" + leftValue + " " + rightValue + " *",
					temporaryFileName, temporaryLine);
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
			return BigInteger.ZERO.subtract((BigInteger) value);
		case REAL:
			return BigDecimal.ZERO.subtract((BigDecimal) value);
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

	public final static Object notEqualsOperator(Object leftValue, Object rightValue, DataTypes type) {
		switch (type) {
		case REAL:
			return ((BigDecimal) leftValue).compareTo((BigDecimal) rightValue) != 0;
		case INTEGER:
		case BOOLEAN:
		case ANY:
		case STRING:
			return !leftValue.equals(rightValue);
		case VOID:
			new LogError("Void hasn't got any value:\t" + rightValue + " !=", temporaryFileName, temporaryLine);
		default:
			return null;
		}
	}

	public final static Object orOperator(Object leftValue, Object rightValue, DataTypes type) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).or((BigInteger) rightValue);
		case BOOLEAN:
			return ((Boolean) leftValue) || ((Boolean) rightValue);
		case REAL:
		case STRING:
			new LogError("Can't do or operation with " + type.toString().toLowerCase() + "s:\t " + leftValue + " "
					+ rightValue + " |", temporaryFileName, temporaryLine);
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
			return ((BigInteger) leftValue).shiftLeft(((BigInteger) rightValue).intValue());
		case STRING:
			var str = (String) leftValue;
			return str.substring(0, str.length() - ((BigInteger) rightValue).intValue());
		case REAL:
		case BOOLEAN:
			new LogError("Can't shift left " + type.toString().toLowerCase() + "s:\t " + leftValue + " " + rightValue
					+ " <<", temporaryFileName, temporaryLine);
		case ANY:
			return overloadOperator(leftValue, rightValue, "$shl", 2, false);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue + " " + rightValue + " <<", temporaryFileName,
					temporaryLine);
		default:
			return null;
		}
	}

	public final static Object shiftRightOperator(Object leftValue, Object rightValue, DataTypes type) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).shiftRight(((BigInteger) rightValue).intValue());
		case REAL:
		case STRING:
		case BOOLEAN:
			new LogError("Can't shift right " + type.toString().toLowerCase() + "s:\t " + leftValue + " " + rightValue
					+ " >>", temporaryFileName, temporaryLine);
		case ANY:
			return overloadOperator(leftValue, rightValue, "$shr", 2, false);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue + " " + rightValue + " >>", temporaryFileName,
					temporaryLine);
		default:
			return null;
		}
	}

	public final static Object subtractionOperator(Object leftValue, Object rightValue, DataTypes type) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).subtract(((BigInteger) rightValue));
		case REAL:
			return ((BigDecimal) leftValue).subtract(((BigDecimal) rightValue));
		case STRING:
		case BOOLEAN:
			new LogError(
					"Can't subtract " + type.toString().toLowerCase() + "s:\t" + leftValue + " " + rightValue + " -",
					temporaryFileName, temporaryLine);
		case ANY:
			return overloadOperator(leftValue, rightValue, "$sub", 2, false);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue + " " + rightValue + " -", temporaryFileName,
					temporaryLine);
		default:
			return null;
		}
	}

	public final static Object xorOperator(Object leftValue, Object rightValue, DataTypes type) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).xor((BigInteger) rightValue);
		case REAL:
		case STRING:
		case BOOLEAN:
			new LogError("Can't do xor operation with " + type.toString().toString().toLowerCase() + "s:\t " + leftValue
					+ " " + rightValue + " ^", temporaryFileName, temporaryLine);
		case ANY:
			return overloadOperator(leftValue, rightValue, "$xor", 2, false);
		case VOID:
			new LogError("Void hasn't got any value:\t" + leftValue + " " + rightValue + " ^", temporaryFileName,
					temporaryLine);
		default:
			return null;
		}
	}

	public final static Object assignmentAdditionOperator(Object leftValue, Object rightValue, DataTypes type) {
		var variable = VariableDeclarationNode.toMe(leftValue, temporaryFileName, temporaryLine);
		switch (type) {
		case INTEGER:
		case REAL:
		case STRING:
			variable.setValue(additionOperator(variable.getValue(), rightValue, type), temporaryFileName,
					temporaryLine);
			return variable.getValue();
		case BOOLEAN:
			new LogError("Can't add booleans:\t" + variable.getName() + " " + rightValue + " +=", temporaryFileName,
					temporaryLine);
		case ANY:
			return overloadOperator(variable.getValue(), rightValue, "$a_add", 2, true);
		case VOID:
			new LogError("Void hasn't got any value:\t" + variable.getName() + " " + rightValue + " +=",
					temporaryFileName, temporaryLine);
		default:
			return null;
		}
	}

	public final static Object assignmentAndOperator(Object leftValue, Object rightValue, DataTypes type) {
		var variable = VariableDeclarationNode.toMe(leftValue, temporaryFileName, temporaryLine);
		switch (type) {
		case INTEGER:
		case REAL:
		case STRING:
			variable.setValue(andOperator(variable.getValue(), rightValue, type), temporaryFileName, temporaryLine);
			return variable.getValue();
		case ANY:
			return overloadOperator(variable.getValue(), rightValue, "$a_and", 2, true);
		case VOID:
			new LogError("Void hasn't got any value:\t" + variable.getName() + " " + rightValue + " &=",
					temporaryFileName, temporaryLine);
		default:
			return null;
		}
	}

	public final static Object assignmentDivisionOperator(Object leftValue, Object rightValue, DataTypes type) {
		var variable = VariableDeclarationNode.toMe(leftValue, temporaryFileName, temporaryLine);
		switch (type) {
		case INTEGER:
		case REAL:
		case STRING:
			variable.setValue(divisionOperator(variable.getValue(), rightValue, type), temporaryFileName,
					temporaryLine);
			return variable.getValue();
		case ANY:
			return overloadOperator(variable.getValue(), rightValue, "$a_div", 2, true);
		case VOID:
			new LogError("Void hasn't got any value:\t" + variable.getName() + " " + rightValue + " /=",
					temporaryFileName, temporaryLine);
		default:
			return null;
		}
	}

	public final static Object assignmentMultiplicationOperator(Object leftValue, Object rightValue, DataTypes type) {
		var variable = VariableDeclarationNode.toMe(leftValue, temporaryFileName, temporaryLine);
		switch (type) {
		case INTEGER:
		case REAL:
		case STRING:
			variable.setValue(multiplicationOperator(variable.getValue(), rightValue, type), temporaryFileName,
					temporaryLine);
			return variable.getValue();
		case ANY:
			return overloadOperator(variable.getValue(), rightValue, "$a_mul", 2, true);
		case VOID:
			new LogError("Void hasn't got any value:\t" + variable.getName() + " " + rightValue + " *=",
					temporaryFileName, temporaryLine);
		default:
			return null;
		}
	}

	public final static Object assignmentOperator(Object leftValue, Object rightValue, DataTypes type) {
		var variable = VariableDeclarationNode.toMe(leftValue, temporaryFileName, temporaryLine);
		switch (type) {
		case INTEGER:
		case REAL:
		case STRING:
		case BOOLEAN:
		case ANY:
			variable.setValue(rightValue, temporaryFileName, temporaryLine);
			return variable.getValue();
		case VOID:
			new LogError("Void hasn't got any value:\t" + variable.getName() + " " + rightValue + " =",
					temporaryFileName, temporaryLine);
		default:
			return null;
		}
	}

	public final static Object assignmentOrOperator(Object leftValue, Object rightValue, DataTypes type) {
		var variable = VariableDeclarationNode.toMe(leftValue, temporaryFileName, temporaryLine);
		switch (type) {
		case INTEGER:
		case REAL:
		case STRING:
			variable.setValue(orOperator(variable.getValue(), rightValue, type), temporaryFileName, temporaryLine);
			return variable.getValue();
		case ANY:
			return overloadOperator(variable.getValue(), rightValue, "$a_or", 2, true);
		case VOID:
			new LogError("Void hasn't got any value:\t" + variable.getName() + " " + rightValue + " |=",
					temporaryFileName, temporaryLine);
		default:
			return null;
		}
	}

	public final static Object assignmentShiftLeftOperator(Object leftValue, Object rightValue, DataTypes type) {
		var variable = VariableDeclarationNode.toMe(leftValue, temporaryFileName, temporaryLine);
		switch (type) {
		case INTEGER:
		case REAL:
		case STRING:
			variable.setValue(shiftLeftOperator(variable.getValue(), rightValue, type), temporaryFileName,
					temporaryLine);
			return variable.getValue();
		case ANY:
			return overloadOperator(variable.getValue(), rightValue, "$a_shl", 2, true);
		case VOID:
			new LogError("Void hasn't got any value:\t" + variable.getName() + " " + rightValue + " <<=",
					temporaryFileName, temporaryLine);
		default:
			return null;
		}
	}

	public final static Object assignmentShiftRightOperator(Object leftValue, Object rightValue, DataTypes type) {
		var variable = VariableDeclarationNode.toMe(leftValue, temporaryFileName, temporaryLine);
		switch (type) {
		case INTEGER:
		case REAL:
		case STRING:
			variable.setValue(shiftRightOperator(variable.getValue(), rightValue, type), temporaryFileName,
					temporaryLine);
			return variable.getValue();
		case ANY:
			return overloadOperator(variable.getValue(), rightValue, "$a_shr", 2, true);
		case VOID:
			new LogError("Void hasn't got any value:\t" + variable.getName() + " " + rightValue + " >>=",
					temporaryFileName, temporaryLine);
		default:
			return null;
		}
	}

	public final static Object assignmentSubtractionOperator(Object leftValue, Object rightValue, DataTypes type) {
		var variable = VariableDeclarationNode.toMe(leftValue, temporaryFileName, temporaryLine);
		switch (type) {
		case INTEGER:
		case REAL:
		case STRING:
			variable.setValue(subtractionOperator(variable.getValue(), rightValue, type), temporaryFileName,
					temporaryLine);
			return variable.getValue();
		case ANY:
			return overloadOperator(variable.getValue(), rightValue, "$a_sub", 2, true);
		case VOID:
			new LogError("Void hasn't got any value:\t" + variable.getName() + " " + rightValue + " -=",
					temporaryFileName, temporaryLine);
		default:
			return null;
		}
	}

	public final static Object assignmentXorOperator(Object leftValue, Object rightValue, DataTypes type) {
		var variable = VariableDeclarationNode.toMe(leftValue, temporaryFileName, temporaryLine);
		switch (type) {
		case INTEGER:
		case REAL:
		case STRING:
			variable.setValue(xorOperator(variable.getValue(), rightValue, type), temporaryFileName, temporaryLine);
			return variable.getValue();
		case ANY:
			return overloadOperator(variable.getValue(), rightValue, "$a_xor", 2, true);
		case VOID:
			new LogError("Void hasn't got any value:\t" + variable.getName() + " " + rightValue + " ^=",
					temporaryFileName, temporaryLine);
		default:
			return null;
		}
	}
}
