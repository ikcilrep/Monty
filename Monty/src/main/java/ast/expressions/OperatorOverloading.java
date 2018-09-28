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

import ast.declarations.VariableDeclarationNode;
import parser.MontyException;
import sml.data.array.Array;
import sml.data.list.List;
import parser.DataTypes;

public class OperatorOverloading {
	private static String reverse(String str) {
		var result = new StringBuilder(str.length());
		var string = str.toString();
		for (int j = string.length() - 1; j >= 0; j--)
			result.append(string.charAt(j));
		return result.toString();
	}

	public static Object additionOperator(Object leftValue, Object rightValue, Object operator, DataTypes type) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).add(((BigInteger) rightValue));
		case FLOAT:
			return ((Float) leftValue) + ((Float) rightValue);
		case STRING:
			return leftValue.toString() + rightValue.toString();
		case BOOLEAN:
			new MontyException("Can't add booleans:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		case ARRAY:
			return ((Array) leftValue).copy().append(rightValue);
		case LIST:
			return ((List) leftValue).copy().append(rightValue);
		case ANY:
			new MontyException("Can't do any operations with \"any\" data type");
		case VOID:
			new MontyException("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		default:
			return null;
		}
	}

	public static Object subtractionOperator(Object leftValue, Object rightValue, Object operator, DataTypes type) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).subtract(((BigInteger) rightValue));
		case FLOAT:
			return ((Float) leftValue) - ((Float) rightValue);
		case STRING:
			new MontyException("Can't subtract strings:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		case BOOLEAN:
			new MontyException("Can't subtract booleans:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		case ARRAY:
			new MontyException("Can't subtract array:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		case LIST:
			new MontyException("Can't subtract list:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		case ANY:
			new MontyException("Can't do any operations with \"any\" data type");
		case VOID:
			new MontyException("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		default:
			return null;
		}
	}

	public static Object multiplicationOperator(Object leftValue, Object rightValue, Object operator, DataTypes type) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).multiply(((BigInteger) rightValue));
		case FLOAT:
			return ((Float) leftValue) * ((Float) rightValue);
		case STRING:
			new MontyException("Can't multiply strings:\t\"" + leftValue.toString() + "\" \"" + rightValue.toString()
					+ "\" " + operator.toString());
		case BOOLEAN:
			new MontyException("Can't multiply booleans:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		case ARRAY:
			new MontyException("Can't multiply array:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		case LIST:
			new MontyException("Can't multiply list:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		case ANY:
			new MontyException("Can't do any operations with \"any\" data type");
		case VOID:
			new MontyException("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		default:
			return null;
		}
	}

	public static Object divisionOperator(Object leftValue, Object rightValue, Object operator, DataTypes type) {

		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).divide(((BigInteger) rightValue));
		case FLOAT:
			return ((Float) leftValue) / ((Float) rightValue);
		case STRING:
			new MontyException("Can't divide strings:\t\"" + leftValue.toString() + "\" \"" + rightValue.toString()
					+ "\" " + operator.toString());
		case BOOLEAN:
			new MontyException("Can't divide booleans:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		case ARRAY:
			new MontyException("Can't divide array:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		case LIST:
			new MontyException("Can't divide list:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		case ANY:
			new MontyException("Can't do any operations with \"any\" data type");
		case VOID:
			new MontyException("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		default:
			return null;
		}
	}

	public static Object moduloOperator(Object leftValue, Object rightValue, Object operator, DataTypes type) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).mod(((BigInteger) rightValue));
		case FLOAT:
			return ((Float) leftValue) % ((Float) rightValue);
		case STRING:
			new MontyException("Can't modulo strings:\t\"" + leftValue.toString() + "\" \"" + rightValue.toString()
					+ "\" " + operator.toString());
		case BOOLEAN:
			new MontyException("Can't modulo booleans:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		case ARRAY:
			new MontyException("Can't modulo array:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		case LIST:
			new MontyException("Can't modulo list:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		case ANY:
			new MontyException("Can't do any operations with \"any\" data type");
		case VOID:
			new MontyException("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		default:
			return null;
		}
	}

	public static Object negationOperator(Object leftValue, Object rightValue, Object operator, DataTypes type) {
		switch (type) {
		case INTEGER:
			return BigInteger.valueOf(0).subtract((BigInteger) rightValue);
		case FLOAT:
			return 0 - (Float) rightValue;
		case STRING:
			return reverse(leftValue.toString());
		case BOOLEAN:
			return !(boolean) rightValue;
		case ARRAY:
			return ((Array) rightValue).reversed();
		case LIST:
			return ((List) rightValue).reversed();
		case ANY:
			new MontyException("Can't do any operations with \"any\" data type");
		case VOID:
			new MontyException("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString());
		default:
			return null;
		}
	}

	public static Object shiftLeftOperator(Object leftValue, Object rightValue, Object operator, DataTypes type) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).shiftLeft(((BigInteger) rightValue).intValue());
		case FLOAT:
			new MontyException("Can't shift left Floats:\t " + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		case STRING:
			new MontyException("Can't shift left strings:\t\"" + leftValue.toString() + "\" \"" + rightValue.toString()
					+ "\" " + operator.toString());
		case BOOLEAN:
			new MontyException("Can't shift left booleans:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		case ARRAY:
			var arr = ((Array) leftValue);
			return arr.subarray(0, arr.length() - ((BigInteger) rightValue).intValue());
		case LIST:
			var lst = ((List) leftValue);
			return lst.sublist(0, lst.length() - ((BigInteger) rightValue).intValue());
		case ANY:
			new MontyException("Can't do any operations with \"any\" data type");
		case VOID:
			new MontyException("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		default:
			return null;
		}
	}

	public static Object shiftRightOperator(Object leftValue, Object rightValue, Object operator, DataTypes type) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).shiftRight(((BigInteger) rightValue).intValue());
		case FLOAT:
			new MontyException("Can't shift right Floats:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		case STRING:
			new MontyException("Can't shift right strings:\t\"" + leftValue.toString() + "\" \"" + rightValue.toString()
					+ "\" " + operator.toString());
		case BOOLEAN:
			new MontyException("Can't shift right booleans:\t" + leftValue.toString() + " " + rightValue.toString()
					+ " " + operator.toString());
		case ARRAY:
			var arr = ((Array) leftValue);
			return arr.copy().subarray(((BigInteger) rightValue).intValue(), arr.length());
		case LIST:
			var lst = ((List) leftValue);
			return lst.copy().sublist(((BigInteger) rightValue).intValue(), lst.length());
		case ANY:
			new MontyException("Can't do any operations with \"any\" data type");
		case VOID:
			new MontyException("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		default:
			return null;
		}
	}

	public static Object xorOperator(Object leftValue, Object rightValue, Object operator, DataTypes type) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).xor((BigInteger) rightValue);
		case FLOAT:
			new MontyException("Can't do \"xor\" operation with Floats:\t" + leftValue.toString() + " "
					+ rightValue.toString() + " " + operator.toString());
		case STRING:
			new MontyException("Can't do \"xor\" operation with strings:\t\"" + leftValue.toString() + "\" \""
					+ rightValue.toString() + "\" " + operator.toString());
		case BOOLEAN:
			new MontyException("Can't do \"xor\" operation with booleans:\t" + leftValue.toString() + " "
					+ rightValue.toString() + " " + operator.toString());
		case ARRAY:
			new MontyException("Can't do \"xor\" operation with array:\t" + leftValue.toString() + " "
					+ rightValue.toString() + " " + operator.toString());
		case LIST:
			new MontyException("Can't do \"xor\" operation with list:\t" + leftValue.toString() + " "
					+ rightValue.toString() + " " + operator.toString());
		case ANY:
			new MontyException("Can't do any operations with \"any\" data type");
		case VOID:
			new MontyException("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		default:
			return null;
		}
	}

	public static Object andOperator(Object leftValue, Object rightValue, Object operator, DataTypes type) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).and((BigInteger) rightValue);
		case FLOAT:
			new MontyException("Can't do \"and\" operation with Floats:\t\"" + leftValue.toString() + "\" \""
					+ rightValue.toString() + "\" " + operator.toString());
		case STRING:
			new MontyException("Can't do \"and\" operation with strings:\t\"" + leftValue.toString() + "\" \""
					+ rightValue.toString() + "\" " + operator.toString());
		case BOOLEAN:
			return ((Boolean) leftValue) && ((Boolean) rightValue);
		case ARRAY:
			new MontyException("Can't do \"and\" operation with array:\t" + leftValue.toString() + " "
					+ rightValue.toString() + " " + operator.toString());
		case LIST:
			new MontyException("Can't do \"and\" operation with list:\t" + leftValue.toString() + " "
					+ rightValue.toString() + " " + operator.toString());
		case ANY:
			new MontyException("Can't do any operations with \"any\" data type");
		case VOID:
			new MontyException("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		default:
			return null;
		}
	}

	public static Object orOperator(Object leftValue, Object rightValue, Object operator, DataTypes type) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).or((BigInteger) rightValue);
		case FLOAT:
			new MontyException("Can't do \"or\" operation with Floats:\t" + leftValue.toString() + " "
					+ rightValue.toString() + " " + operator.toString());
		case STRING:
			new MontyException("Can't do \"or\" operation with strings:\t\"" + leftValue.toString() + "\" \""
					+ rightValue.toString() + "\" " + operator.toString());
		case BOOLEAN:
			return ((Boolean) leftValue) || ((Boolean) rightValue);
		case ARRAY:
			new MontyException("Can't do \"or\" operation with array:\t" + leftValue.toString() + " "
					+ rightValue.toString() + " " + operator.toString());
		case LIST:
			new MontyException("Can't do \"or\" operation with list:\t" + leftValue.toString() + " "
					+ rightValue.toString() + " " + operator.toString());
		case ANY:
			new MontyException("Can't do any operations with \"any\" data type");
		case VOID:
			new MontyException("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		default:
			return null;
		}
	}

	public static Object equalsOperator(Object leftValue, Object rightValue, Object operator, DataTypes type) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).compareTo((BigInteger) rightValue) == 0;
		case FLOAT:
			return ((Float) leftValue).compareTo((Float) rightValue) == 0;
		case BOOLEAN:
			return ((Boolean) leftValue).compareTo((Boolean) rightValue) == 0;
		case STRING:
		case LIST:
		case ARRAY:
			return leftValue.equals(rightValue);
		case ANY:
			new MontyException("Can't do any operations with \"any\" data type");
		case VOID:
			new MontyException("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		default:
			return null;
		}
	}

	public static Object greaterOperator(Object leftValue, Object rightValue, Object operator, DataTypes type) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).compareTo((BigInteger) rightValue) > 0;
		case FLOAT:
			return ((Float) leftValue).compareTo((Float) rightValue) > 0;
		case STRING:
			new MontyException("One string can't be greater than other string:\t" + leftValue.toString() + " "
					+ rightValue.toString() + " " + operator.toString());
		case BOOLEAN:
			new MontyException("One boolean can't be greater than other boolean:\t" + leftValue.toString() + " "
					+ rightValue.toString() + " " + operator.toString());
		case ARRAY:
			new MontyException("Array hasn't got any value to compare:\t" + leftValue.toString() + " "
					+ rightValue.toString() + " " + operator.toString());
		case LIST:
			new MontyException("List hasn't got any value to compare:\t" + leftValue.toString() + " "
					+ rightValue.toString() + " " + operator.toString());
		case ANY:
			new MontyException("Can't do any operations with \"any\" data type");
		case VOID:
			new MontyException("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		default:
			return null;
		}
	}

	public static Object lowerOperator(Object leftValue, Object rightValue, Object operator, DataTypes type) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).compareTo((BigInteger) rightValue) < 0;
		case FLOAT:
			return ((Float) leftValue).compareTo((Float) rightValue) < 0;
		case STRING:
			new MontyException("One string can't be lower than other string:\t" + leftValue.toString() + " "
					+ rightValue.toString() + " " + operator.toString());
		case BOOLEAN:
			new MontyException("One boolean can't be lower than other boolean:\t" + leftValue.toString() + " "
					+ rightValue.toString() + " " + operator.toString());
		case ARRAY:
			new MontyException("Array hasn't got any value to compare:\t" + leftValue.toString() + " "
					+ rightValue.toString() + " " + operator.toString());
		case LIST:
			new MontyException("List hasn't got any value to compare:\t" + leftValue.toString() + " "
					+ rightValue.toString() + " " + operator.toString());
		case ANY:
			new MontyException("Can't do any operations with \"any\" data type");
		case VOID:
			new MontyException("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		default:
			return null;
		}
	}

	public static Object greaterEqualsOperator(Object leftValue, Object rightValue, Object operator, DataTypes type) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).compareTo((BigInteger) rightValue) >= 0;
		case FLOAT:
			return ((Float) leftValue).compareTo((Float) rightValue) >= 0;
		case STRING:
			new MontyException("One string can't be greater than other string:\t" + leftValue.toString() + " "
					+ rightValue.toString() + " " + operator.toString());
		case BOOLEAN:
			new MontyException("One boolean can't be greater than other boolean:\t" + leftValue.toString() + " "
					+ rightValue.toString() + " " + operator.toString());
		case ARRAY:
			new MontyException("Array hasn't got any value to compare:\t" + leftValue.toString() + " "
					+ rightValue.toString() + " " + operator.toString());
		case LIST:
			new MontyException("List hasn't got any value to compare:\t" + leftValue.toString() + " "
					+ rightValue.toString() + " " + operator.toString());
		case ANY:
			new MontyException("Can't do any operations with \"any\" data type");
		case VOID:
			new MontyException("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		default:
			return null;
		}
	}

	public static Object lowerEqualsOperator(Object leftValue, Object rightValue, Object operator, DataTypes type) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).compareTo((BigInteger) rightValue) <= 0;
		case FLOAT:
			return ((Float) leftValue).compareTo((Float) rightValue) <= 0;
		case STRING:
			new MontyException("One string can't be lower than other string:\t" + leftValue.toString() + " "
					+ rightValue.toString() + " " + operator.toString());
		case BOOLEAN:
			new MontyException("One boolean can't be lower than other boolean:\t" + leftValue.toString() + " "
					+ rightValue.toString() + " " + operator.toString());
		case ARRAY:
			new MontyException("Array hasn't got any value to compare:\t" + leftValue.toString() + " "
					+ rightValue.toString() + " " + operator.toString());
		case LIST:
			new MontyException("List hasn't got any value to compare:\t" + leftValue.toString() + " "
					+ rightValue.toString() + " " + operator.toString());
		case ANY:
			new MontyException("Can't do any operations with \"any\" data type");
		case VOID:
			new MontyException("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		default:
			return null;
		}
	}

	public static Object notEqualsOperator(Object leftValue, Object rightValue, Object operator, DataTypes type) {
		switch (type) {
		case INTEGER:
			return ((BigInteger) leftValue).compareTo((BigInteger) rightValue) != 0;
		case FLOAT:
			return ((Float) leftValue).compareTo((Float) rightValue) != 0;
		case STRING:
			new MontyException("One string can't be lower than other string:\t" + leftValue.toString() + " "
					+ rightValue.toString() + " " + operator.toString());
		case BOOLEAN:
			new MontyException("One boolean can't be lower than other boolean:\t" + leftValue.toString() + " "
					+ rightValue.toString() + " " + operator.toString());
		case ARRAY:
			new MontyException("Array hasn't got any value to compare:\t" + leftValue.toString() + " "
					+ rightValue.toString() + " " + operator.toString());
		case LIST:
			new MontyException("List hasn't got any value to compare:\t" + leftValue.toString() + " "
					+ rightValue.toString() + " " + operator.toString());
		case ANY:
			new MontyException("Can't do any operations with \"any\" data type");
		case VOID:
			new MontyException("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		default:
			return null;
		}
	}

	public static Object assignmentOperator(Object leftValue, Object rightValue, Object operator, DataTypes type) {
		var variable = ((VariableDeclarationNode) leftValue);
		switch (type) {
		case INTEGER:
		case FLOAT:
		case STRING:
		case BOOLEAN:
		case LIST:
		case ARRAY:
			variable.setValue(rightValue);
			return variable.getValue();
		case ANY:
			new MontyException("Can't do any operations with \"any\" data type");
		case VOID:
			new MontyException("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		default:
			return null;
		}
	}

	public static Object assignmentAdditionOperator(Object leftValue, Object rightValue, Object operator,
			DataTypes type) {
		var variable = ((VariableDeclarationNode) leftValue);
		switch (type) {
		case INTEGER:
			variable.setValue(((BigInteger) variable.getValue()).add((BigInteger) rightValue));
			return variable.getValue();
		case FLOAT:
			variable.setValue((Float) variable.getValue() + (Float) rightValue);
			return variable.getValue();
		case STRING:
			variable.setValue(variable.getValue().toString() + rightValue.toString());
			return variable.getValue();
		case BOOLEAN:
			new MontyException("Can't add booleans:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		case ARRAY:
			var array = (Array) variable.getValue();
			array.append(rightValue);
			return variable.getValue();
		case LIST:
			var list = (List) variable.getValue();
			list.append(rightValue);
			return variable.getValue();
		case ANY:
			new MontyException("Can't do any operations with \"any\" data type");
		case VOID:
			new MontyException("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		default:
			return null;
		}
	}

	public static Object assignmentSubtractionOperator(Object leftValue, Object rightValue, Object operator,
			DataTypes type) {
		var variable = ((VariableDeclarationNode) leftValue);
		switch (type) {
		case INTEGER:
			variable.setValue(((BigInteger) variable.getValue()).subtract((BigInteger) rightValue));
			return variable.getValue();
		case FLOAT:
			variable.setValue((Float) variable.getValue() - (Float) rightValue);
			return variable.getValue();
		case STRING:
			new MontyException("Can't subtract strings:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		case BOOLEAN:
			new MontyException("Can't subtract booleans:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		case ARRAY:
			new MontyException("Can't subtract array:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		case LIST:
			new MontyException("Can't subtract list:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		case ANY:
			new MontyException("Can't do any operations with \"any\" data type");
		case VOID:
			new MontyException("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		default:
			return null;
		}
	}

	public static Object assignmentMultiplicationOperator(Object leftValue, Object rightValue, Object operator,
			DataTypes type) {
		var variable = ((VariableDeclarationNode) leftValue);
		switch (type) {
		case INTEGER:
			variable.setValue(((BigInteger) variable.getValue()).multiply((BigInteger) rightValue));
			return variable.getValue();
		case FLOAT:
			variable.setValue((Float) variable.getValue() * (Float) rightValue);
			return variable.getValue();
		case STRING:
			new MontyException("Can't multiply strings:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		case BOOLEAN:
			new MontyException("Can't multiply booleans:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		case ARRAY:
			new MontyException("Can't multiply array:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		case LIST:
			new MontyException("Can't multiply list:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		case ANY:
			new MontyException("Can't do any operations with \"any\" data type");
		case VOID:
			new MontyException("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		default:
			return null;
		}
	}

	public static Object assignmentDivisionOperator(Object leftValue, Object rightValue, Object operator,
			DataTypes type) {
		var variable = ((VariableDeclarationNode) leftValue);
		switch (type) {
		case INTEGER:
			variable.setValue(((BigInteger) variable.getValue()).divide((BigInteger) rightValue));
			return variable.getValue();
		case FLOAT:
			variable.setValue((Float) variable.getValue() / (Float) rightValue);
			return variable.getValue();
		case STRING:
			new MontyException("Can't divide strings:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		case BOOLEAN:
			new MontyException("Can't divide booleans:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		case ARRAY:
			new MontyException("Can't divide array:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		case LIST:
			new MontyException("Can't divide list:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		case ANY:
			new MontyException("Can't do any operations with \"any\" data type");
		case VOID:
			new MontyException("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		default:
			return null;
		}
	}

	public static Object assignmentShiftLeftOperator(Object leftValue, Object rightValue, Object operator,
			DataTypes type) {
		switch (type) {
		case INTEGER:
			var variable = ((VariableDeclarationNode) leftValue);
			variable.setValue(((BigInteger) variable.getValue()).shiftLeft(((BigInteger) rightValue).intValue()));
			return variable.getValue();
		case FLOAT:
			new MontyException("Can't shift left Floats:\t " + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		case STRING:
			new MontyException("Can't shift left strings:\t\"" + leftValue.toString() + "\" \"" + rightValue.toString()
					+ "\" " + operator.toString());
		case BOOLEAN:
			new MontyException("Can't shift left booleans:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		case ARRAY:
			var arr = ((Array) leftValue);
			return arr.subarray(0, arr.length() - ((BigInteger) rightValue).intValue());
		case LIST:
			var lst = ((List) leftValue);
			return lst.sublist(0, lst.length() - ((BigInteger) rightValue).intValue());
		case ANY:
			new MontyException("Can't do any operations with \"any\" data type");
		case VOID:
			new MontyException("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		default:
			return null;
		}
	}

	public static Object assignmentShiftRightOperator(Object leftValue, Object rightValue, Object operator,
			DataTypes type) {
		switch (type) {
		case INTEGER:
			var variable = ((VariableDeclarationNode) leftValue);
			variable.setValue(((BigInteger) variable.getValue()).shiftRight(((BigInteger) rightValue).intValue()));
			return variable.getValue();
		case FLOAT:
			new MontyException("Can't shift right Floats:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		case STRING:
			new MontyException("Can't shift right strings:\t\"" + leftValue.toString() + "\" \"" + rightValue.toString()
					+ "\" " + operator.toString());
		case BOOLEAN:
			new MontyException("Can't shift right booleans:\t" + leftValue.toString() + " " + rightValue.toString()
					+ " " + operator.toString());
		case ARRAY:
			var arr = ((Array) leftValue);
			return arr.subarray(((BigInteger) rightValue).intValue(), arr.length());
		case LIST:
			var lst = ((List) leftValue);
			return lst.sublist(((BigInteger) rightValue).intValue(), lst.length());
		case ANY:
			new MontyException("Can't do any operations with \"any\" data type");
		case VOID:
			new MontyException("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		default:
			return null;
		}
	}

	public static Object assignmentXorOperator(Object leftValue, Object rightValue, Object operator, DataTypes type) {
		switch (type) {
		case INTEGER:
			var variable = ((VariableDeclarationNode) leftValue);
			variable.setValue(((BigInteger) variable.getValue()).xor((BigInteger) rightValue));
			return variable.getValue();
		case FLOAT:
			new MontyException("Can't do \"xor\" operation with Floats:\t" + leftValue.toString() + " "
					+ rightValue.toString() + " " + operator.toString());
		case STRING:
			new MontyException("Can't do \"xor\" operation with strings:\t\"" + leftValue.toString() + "\" \""
					+ rightValue.toString() + "\" " + operator.toString());
		case BOOLEAN:
			new MontyException("Can't do \"xor\" operation with booleans:\t" + leftValue.toString() + " "
					+ rightValue.toString() + " " + operator.toString());
		case ARRAY:
			new MontyException("Can't do \"xor\" operation with array:\t" + leftValue.toString() + " "
					+ rightValue.toString() + " " + operator.toString());
		case LIST:
			new MontyException("Can't do \"xor\" operation with list:\t" + leftValue.toString() + " "
					+ rightValue.toString() + " " + operator.toString());
		case ANY:
			new MontyException("Can't do any operations with \"any\" data type");
		case VOID:
			new MontyException("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		default:
			return null;
		}
	}

	public static Object assignmentAndOperator(Object leftValue, Object rightValue, Object operator, DataTypes type) {
		switch (type) {
		case INTEGER:
			var variable = ((VariableDeclarationNode) leftValue);
			variable.setValue(((BigInteger) variable.getValue()).and(((BigInteger) rightValue)));
			return variable.getValue();
		case FLOAT:
			new MontyException("Can't do \"and\" operation with Floats:\t\"" + leftValue.toString() + "\" \""
					+ rightValue.toString() + "\" " + operator.toString());
		case STRING:
			new MontyException("Can't do \"and\" operation with strings:\t\"" + leftValue.toString() + "\" \""
					+ rightValue.toString() + "\" " + operator.toString());
		case BOOLEAN:
			new MontyException("Can't do \"and\" operation with booleans:\t" + leftValue.toString() + " "
					+ rightValue.toString() + " " + operator.toString());
		case ARRAY:
			new MontyException("Can't do \"and\" operation with array:\t" + leftValue.toString() + " "
					+ rightValue.toString() + " " + operator.toString());
		case LIST:
			new MontyException("Can't do \"and\" operation with list:\t" + leftValue.toString() + " "
					+ rightValue.toString() + " " + operator.toString());
		case ANY:
			new MontyException("Can't do any operations with \"any\" data type");
		case VOID:
			new MontyException("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		default:
			return null;
		}
	}

	public static Object assignmentOrOperator(Object leftValue, Object rightValue, Object operator, DataTypes type) {
		switch (type) {
		case INTEGER:
			var variable = ((VariableDeclarationNode) leftValue);
			variable.setValue(((BigInteger) variable.getValue()).or(((BigInteger) rightValue)));
			return variable.getValue();
		case FLOAT:
			new MontyException("Can't do \"or\" operation with Floats:\t" + leftValue.toString() + " "
					+ rightValue.toString() + " " + operator.toString());
		case STRING:
			new MontyException("Can't do \"or\" operation with strings:\t\"" + leftValue.toString() + "\" \""
					+ rightValue.toString() + "\" " + operator.toString());
		case BOOLEAN:
			new MontyException("Can't do \"or\" operation with booleans:\t" + leftValue.toString() + " "
					+ rightValue.toString() + " " + operator.toString());
		case ARRAY:
			new MontyException("Can't do \"or\" operation with array:\t" + leftValue.toString() + " "
					+ rightValue.toString() + " " + operator.toString());
		case LIST:
			new MontyException("Can't do \"or\" operation with list:\t" + leftValue.toString() + " "
					+ rightValue.toString() + " " + operator.toString());
		case ANY:
			new MontyException("Can't do any operations with \"any\" data type");
		case VOID:
			new MontyException("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString() + " "
					+ operator.toString());
		default:
			return null;
		}
	}
}
