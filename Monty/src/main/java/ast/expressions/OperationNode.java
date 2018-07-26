package ast.expressions;
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
import java.math.BigInteger;

import ast.Block;
import ast.Node;
import ast.NodeTypes;
import ast.declarations.VariableDeclarationNode;
import parser.DataTypes;
import parser.MontyException;
import sml.data.array.Array;

public class OperationNode extends ExpressionNode {

	private Object operand;
	private OperationNode left = null;
	private OperationNode right = null;

	private Block parent = null;

	public OperationNode(Object operand, Block parent) {
		this.operand = operand;
		this.nodeType = NodeTypes.OPERATION;
		this.parent = parent;
	}

	private Object calculate(Object a, Object b, Object operator) {
		var isComparison = operator.toString().equals("==") || operator.toString().equals("!=")
				|| operator.toString().equals("<=") || operator.toString().equals(">=")
				|| operator.toString().equals(">") || operator.toString().equals("<");
		Object leftValue = null;
		Object rightValue = null;
		DataTypes type = getDataType(a);
		if (!(type.equals(DataTypes.ARRAY) || type.equals(getDataType(b))))
			new MontyException("Type mismatch:\t" + type + " and " + getDataType(b));
		leftValue = getLiteral(a);
		rightValue = getLiteral(b);

		if (!operator.toString().contains("=") || (operator.toString().contains("=") && isComparison))
			if (a instanceof Node && ((Node) a).getNodeType().equals(NodeTypes.VARIABLE)) {
				var variable = parent.getVariableByName(((VariableNode) a).getName());
				leftValue = variable.getValue();

			}
		if (b instanceof Node && ((Node) b).getNodeType().equals(NodeTypes.VARIABLE)) {
			var variable = parent.getVariableByName(((VariableNode) b).getName());
			rightValue = variable.getValue();
		}

		switch (operator.toString()) {
		case "+":
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
			case ANY:
				new MontyException("Can't do any operations with \"any\" data type");
			case VOID:
				new MontyException("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			}
		case "-":
			switch (type) {
			case INTEGER:
				return ((BigInteger) leftValue).subtract(((BigInteger) rightValue));
			case FLOAT:
				return ((Float) leftValue) - ((Float) rightValue);
			case STRING:
				new MontyException("Can't subtract strings:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			case BOOLEAN:
				new MontyException("Can't subtract booleans:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			case ARRAY:
				new MontyException("Can't subtract array:\t" + leftValue.toString() + " " + rightValue.toString() + " "
						+ operator.toString());
			case ANY:
				new MontyException("Can't do any operations with \"any\" data type");
			case VOID:
				new MontyException("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			}
		case "*":
			switch (type) {
			case INTEGER:
				return ((BigInteger) leftValue).multiply(((BigInteger) rightValue));
			case FLOAT:
				return ((Float) leftValue) * ((Float) rightValue);
			case STRING:
				new MontyException("Can't multiply strings:\t\"" + leftValue.toString() + "\" \""
						+ rightValue.toString() + "\" " + operator.toString());
			case BOOLEAN:
				new MontyException("Can't multiply booleans:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			case ARRAY:
				new MontyException("Can't multiply array:\t" + leftValue.toString() + " " + rightValue.toString() + " "
						+ operator.toString());
			case ANY:
				new MontyException("Can't do any operations with \"any\" data type");
			case VOID:
				new MontyException("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			}
		case "/":
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
				new MontyException("Can't multiply array:\t" + leftValue.toString() + " " + rightValue.toString() + " "
						+ operator.toString());
			case ANY:
				new MontyException("Can't do any operations with \"any\" data type");
			case VOID:
				new MontyException("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			}
		case "%":
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
				new MontyException("Can't multiply array:\t" + leftValue.toString() + " " + rightValue.toString() + " "
						+ operator.toString());
			case ANY:
				new MontyException("Can't do any operations with \"any\" data type");
			case VOID:
				new MontyException("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			}
		case "!":
			switch (type) {
			case INTEGER:
				return BigInteger.valueOf(0).subtract((BigInteger) rightValue);
			case FLOAT:
				return 0 - (Float) rightValue;
			case STRING:
				var result = new StringBuilder();
				var string = rightValue.toString();
				for (int j = string.length() - 1, i = 0; j >= 0; j--, i++)
					result.append(string.charAt(i));
				return result.toString();
			case BOOLEAN:
				return !(boolean) rightValue;
			case ARRAY:
				var resultArray = new Array();
				var array = (Array) rightValue;
				for (int j = array.length() - 1, i = 0; j >= 0; j--, i++)
					resultArray.append(array.get(i));
				return resultArray;
			case ANY:
				new MontyException("Can't do any operations with \"any\" data type");
			case VOID:
				new MontyException("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			}
		case "<<":
			switch (type) {
			case INTEGER:
				return ((BigInteger) leftValue).shiftLeft(((BigInteger) rightValue).intValue());
			case FLOAT:
				new MontyException("Can't shift left Floats:\t " + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			case STRING:
				new MontyException("Can't shift left strings:\t\"" + leftValue.toString() + "\" \""
						+ rightValue.toString() + "\" " + operator.toString());
			case BOOLEAN:
				new MontyException("Can't shift left booleans:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			case ARRAY:
				new MontyException("Can't shift left array:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			case ANY:
				new MontyException("Can't do any operations with \"any\" data type");
			case VOID:
				new MontyException("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			}
		case ">>":
			switch (type) {
			case INTEGER:
				return ((BigInteger) leftValue).shiftRight(((BigInteger) rightValue).intValue());
			case FLOAT:
				new MontyException("Can't shift right Floats:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			case STRING:
				new MontyException("Can't shift right strings:\t\"" + leftValue.toString() + "\" \""
						+ rightValue.toString() + "\" " + operator.toString());
			case BOOLEAN:
				new MontyException("Can't shift right booleans:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			case ARRAY:
				new MontyException("Can't shift right array:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			case ANY:
				new MontyException("Can't do any operations with \"any\" data type");
			case VOID:
				new MontyException("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			}
		case "^":
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
			case ANY:
				new MontyException("Can't do any operations with \"any\" data type");
			case VOID:
				new MontyException("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			}
		case "&":
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
				new MontyException("Can't do \"and\" operation with booleans:\t" + leftValue.toString() + " "
						+ rightValue.toString() + " " + operator.toString());
			case ARRAY:
				new MontyException("Can't do \"and\" operation with array:\t" + leftValue.toString() + " "
						+ rightValue.toString() + " " + operator.toString());
			case ANY:
				new MontyException("Can't do any operations with \"any\" data type");
			case VOID:
				new MontyException("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			}
		case "|":
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
				new MontyException("Can't do \"or\" operation with booleans:\t" + leftValue.toString() + " "
						+ rightValue.toString() + " " + operator.toString());
			case ARRAY:
				new MontyException("Can't do \"or\" operation with array:\t" + leftValue.toString() + " "
						+ rightValue.toString() + " " + operator.toString());
			case ANY:
				new MontyException("Can't do any operations with \"any\" data type");
			case VOID:
				new MontyException("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			}
		case "&&":
			switch (type) {
			case INTEGER:
				new MontyException("Can't do \"and\" operation with Integeregers:\t" + leftValue.toString() + " "
						+ rightValue.toString() + " " + operator.toString());
			case FLOAT:
				new MontyException("Can't do \"and\" operation with Floats:\t" + leftValue.toString() + " "
						+ rightValue.toString() + " " + operator.toString());
			case STRING:
				new MontyException("Can't do \"and\" operation with strings:\t\"" + leftValue.toString() + "\" \""
						+ rightValue.toString() + "\" " + operator.toString());
			case BOOLEAN:
				return ((boolean) leftValue) && ((boolean) rightValue);
			case ARRAY:
				new MontyException("Can't do \"and\" operation with array:\t" + leftValue.toString() + " "
						+ rightValue.toString() + " " + operator.toString());
			case ANY:
				new MontyException("Can't do any operations with \"any\" data type");
			case VOID:
				new MontyException("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			}
		case "||":
			switch (type) {
			case INTEGER:
				new MontyException("Can't do \"or\" operation with Integeregers:\t" + leftValue.toString() + " "
						+ rightValue.toString() + "\" " + operator.toString());
			case FLOAT:
				new MontyException("Can't do \"or\" operation with Floats:\t" + leftValue.toString() + " "
						+ rightValue.toString() + " " + operator.toString());
			case STRING:
				new MontyException("Can't do \"or\" operation with strings:\t\"" + leftValue.toString() + "\" \""
						+ rightValue.toString() + "\" " + operator.toString());
			case BOOLEAN:
				return ((boolean) leftValue) || ((boolean) rightValue);
			case ARRAY:
				new MontyException("Can't do \"or\" operation with array:\t" + leftValue.toString() + " "
						+ rightValue.toString() + " " + operator.toString());
			case ANY:
				new MontyException("Can't do any operations with \"any\" data type");
			case VOID:
				new MontyException("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			}
		case "==":
			switch (type) {
			case INTEGER:
				return ((BigInteger) leftValue).compareTo((BigInteger) rightValue) == 0;
			case FLOAT:
				return ((Float) leftValue).compareTo((Float) rightValue) == 0;
			case BOOLEAN:
				return ((Boolean) leftValue).compareTo((Boolean) rightValue) == 0;
			case STRING:
				return leftValue.equals(rightValue);
			case ARRAY:
				return leftValue.equals(rightValue);
			case ANY:
				new MontyException("Can't do any operations with \"any\" data type");
			case VOID:
				new MontyException("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			}
		case ">":
			switch (type) {
			case INTEGER:
				return ((BigInteger) leftValue).compareTo((BigInteger) rightValue) > 0;
			case FLOAT:
				return ((Float) leftValue).compareTo((Float) rightValue) > 0;
			case STRING:
				new MontyException("One string can't be greater than other string:\t" + leftValue.toString() + " "
						+ rightValue.toString() + " " + operator.toString());
			case BOOLEAN:
				new MontyException("One boolean can't be greater than other string:\t" + leftValue.toString() + " "
						+ rightValue.toString() + " " + operator.toString());
			case ARRAY:
				new MontyException("Array hasn't got any value to compare:\t" + leftValue.toString() + " "
						+ rightValue.toString() + " " + operator.toString());
			case ANY:
				new MontyException("Can't do any operations with \"any\" data type");
			case VOID:
				new MontyException("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			}
		case "<":
			switch (type) {
			case INTEGER:
				return ((BigInteger) leftValue).compareTo((BigInteger) rightValue) < 0;
			case FLOAT:
				return ((Float) leftValue).compareTo((Float) rightValue) < 0;
			case STRING:
				new MontyException("One string can't be lower than other string:\t" + leftValue.toString() + " "
						+ rightValue.toString() + " " + operator.toString());
			case BOOLEAN:
				new MontyException("One boolean can't be lower than other string:\t" + leftValue.toString() + " "
						+ rightValue.toString() + " " + operator.toString());
			case ARRAY:
				new MontyException("Array hasn't got any value to compare:\t" + leftValue.toString() + " "
						+ rightValue.toString() + " " + operator.toString());
			case ANY:
				new MontyException("Can't do any operations with \"any\" data type");
			case VOID:
				new MontyException("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			}
		case "<=":
			switch (type) {
			case INTEGER:
				return (((BigInteger) leftValue).compareTo((BigInteger) rightValue)) <= 0;
			case FLOAT:
				return ((Float) leftValue).compareTo((Float) rightValue) <= 0;
			case STRING:
				new MontyException("One string can't be lower than other string:\t" + leftValue.toString() + " "
						+ rightValue.toString() + " " + operator.toString());
			case BOOLEAN:
				new MontyException("One boolean can't be lower than other string:\t" + leftValue.toString() + " "
						+ rightValue.toString() + " " + operator.toString());
			case ARRAY:
				new MontyException("Array hasn't got any value to compare:\t" + leftValue.toString() + " "
						+ rightValue.toString() + " " + operator.toString());
			case ANY:
				new MontyException("Can't do any operations with \"any\" data type");
			case VOID:
				new MontyException("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			}
		case ">=":
			switch (type) {
			case INTEGER:
				return ((BigInteger) leftValue).compareTo((BigInteger) rightValue) >= 0;
			case FLOAT:
				return ((Float) leftValue).compareTo((Float) rightValue) >= 0;
			case STRING:
				new MontyException("One string can't be greater than other string:\t" + leftValue.toString() + " "
						+ rightValue.toString() + " " + operator.toString());
			case BOOLEAN:
				new MontyException("One boolean can't be greater than other string:\t" + leftValue.toString() + " "
						+ rightValue.toString() + " " + operator.toString());
			case ARRAY:
				new MontyException("Array hasn't got any value to compare:\t" + leftValue.toString() + " "
						+ rightValue.toString() + " " + operator.toString());
			case ANY:
				new MontyException("Can't do any operations with \"any\" data type");
			case VOID:
				new MontyException("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			}
		case "!=":
			switch (type) {
			case INTEGER:
				return ((BigInteger) leftValue).compareTo((BigInteger) rightValue) != 0;
			case FLOAT:
				return ((Float) leftValue).compareTo((Float) rightValue) != 0;
			case BOOLEAN:
				return ((Boolean) leftValue).compareTo((Boolean) rightValue) != 0;
			case ARRAY:
			case STRING:
				return !leftValue.equals(rightValue);
			case ANY:
				new MontyException("Can't do any operations with \"any\" data type");
			case VOID:
				new MontyException("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			}
		case "=":
			var variable = ((VariableDeclarationNode) leftValue);
			switch (type) {
			case INTEGER:
			case FLOAT:
			case STRING:
			case BOOLEAN:
			case ARRAY:
				variable.setValue(rightValue);
				return variable.getValue();
			case ANY:
				new MontyException("Can't do any operations with \"any\" data type");
			case VOID:
				new MontyException("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			}
		case "+=":
			variable = ((VariableDeclarationNode) leftValue);
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
			case ANY:
				new MontyException("Can't do any operations with \"any\" data type");
			case VOID:
				new MontyException("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
				return null;
			}
		case "-=":
			variable = ((VariableDeclarationNode) leftValue);
			switch (type) {
			case INTEGER:
				variable.setValue(((BigInteger) variable.getValue()).subtract((BigInteger) rightValue));
				return variable.getValue();
			case FLOAT:
				variable.setValue((Float) variable.getValue() - (Float) rightValue);
				return variable.getValue();
			case STRING:
				new MontyException("Can't subtract strings:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			case BOOLEAN:
				new MontyException("Can't subtract booleans:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			case ARRAY:
				new MontyException("Can't subtract array:\t" + leftValue.toString() + " " + rightValue.toString() + " "
						+ operator.toString());
			case ANY:
				new MontyException("Can't do any operations with \"any\" data type");
			case VOID:
				new MontyException("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			}
		case "*=":
			variable = ((VariableDeclarationNode) leftValue);
			switch (type) {
			case INTEGER:
				variable.setValue(((BigInteger) variable.getValue()).multiply((BigInteger) rightValue));
				return variable.getValue();
			case FLOAT:
				variable.setValue((Float) variable.getValue() * (Float) rightValue);
				return variable.getValue();
			case STRING:
				new MontyException("Can't multiply strings:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			case BOOLEAN:
				new MontyException("Can't multiply booleans:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			case ARRAY:
				new MontyException("Can't multiply array:\t" + leftValue.toString() + " " + rightValue.toString() + " "
						+ operator.toString());
			case ANY:
				new MontyException("Can't do any operations with \"any\" data type");
			case VOID:
				new MontyException("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			}
		case "/=":
			variable = ((VariableDeclarationNode) leftValue);
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
			case ANY:
				new MontyException("Can't do any operations with \"any\" data type");
			case VOID:
				new MontyException("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			}
		case "<<=":
			switch (type) {
			case INTEGER:
				variable = ((VariableDeclarationNode) leftValue);
				variable.setValue(((BigInteger) variable.getValue()).shiftLeft(((BigInteger) rightValue).intValue()));
				return variable.getValue();
			case FLOAT:
				new MontyException("Can't shift left Floats:\t " + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			case STRING:
				new MontyException("Can't shift left strings:\t\"" + leftValue.toString() + "\" \""
						+ rightValue.toString() + "\" " + operator.toString());
			case BOOLEAN:
				new MontyException("Can't shift left booleans:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			case ARRAY:
				new MontyException("Can't shift left array:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			case ANY:
				new MontyException("Can't do any operations with \"any\" data type");
			case VOID:
				new MontyException("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			}
		case ">>=":
			switch (type) {
			case INTEGER:
				variable = ((VariableDeclarationNode) leftValue);
				variable.setValue(((BigInteger) variable.getValue()).shiftRight(((BigInteger) rightValue).intValue()));
				return variable.getValue();
			case FLOAT:
				new MontyException("Can't shift right Floats:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			case STRING:
				new MontyException("Can't shift right strings:\t\"" + leftValue.toString() + "\" \""
						+ rightValue.toString() + "\" " + operator.toString());
			case BOOLEAN:
				new MontyException("Can't shift right booleans:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			case ARRAY:
				new MontyException("Can't shift right array:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			case ANY:
				new MontyException("Can't do any operations with \"any\" data type");
			case VOID:
				new MontyException("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			}
		case "^=":
			switch (type) {
			case INTEGER:
				variable = ((VariableDeclarationNode) leftValue);
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
			case ANY:
				new MontyException("Can't do any operations with \"any\" data type");
			case VOID:
				new MontyException("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			}
		case "&=":
			switch (type) {
			case INTEGER:
				variable = ((VariableDeclarationNode) leftValue);
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
			case ANY:
				new MontyException("Can't do any operations with \"any\" data type");
			case VOID:
				new MontyException("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			}
		case "|=":
			switch (type) {
			case INTEGER:
				variable = ((VariableDeclarationNode) leftValue);
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
			case ANY:
				new MontyException("Can't do any operations with \"any\" data type");
			case VOID:
				new MontyException("Void hasn't got any value:\t" + leftValue.toString() + " " + rightValue.toString()
						+ " " + operator.toString());
			}
		}
		return operator;
	}

	public OperationNode getLeftOperand() {
		return left;
	}

	private Object getLiteral(Object expression) {
		if (expression instanceof Node)
			switch (((Node) expression).getNodeType()) {
			case VARIABLE:
				return parent.getVariableByName(((VariableNode) expression).getName());
			case FUNCTION_CALL:
				var functionToCall = ((FunctionCallNode) expression);
				var function = parent.getFunctionByName(functionToCall.getName());
				return function.call(functionToCall.getArguments());
			case CONSTANT:
				var cn = ((ConstantNode) expression);
				return cn.getValue();
			default:
				return null;
			}
		else
			return expression;
	}

	private DataTypes getDataType(Object expression) {
		if (expression instanceof Node)
			switch (((Node) expression).getNodeType()) {
			case VARIABLE:
				return parent.getVariableByName(((VariableNode) expression).getName()).getType();
			case FUNCTION_CALL:
				var functionCall = ((FunctionCallNode) expression);
				var function = parent.getFunctionByName(functionCall.getName());
				return function.getType();
			case CONSTANT:
				var cn = ((ConstantNode) expression);
				return cn.getType();
			default:
				return null;
			}

		return DataTypes.getDataType(expression);
	}

	public Object getOperand() {
		return operand;
	}

	public Block getParent() {
		return parent;
	}

	public OperationNode getRightOperand() {
		return right;
	}

	public Object run() {
		if (!getOperand().getClass().equals(String.class)) {
			var operand = getOperand();
			var castedOperand = (Node) operand;
			if (castedOperand.getNodeType().equals(NodeTypes.VARIABLE)) {
				var variable = ((VariableDeclarationNode) getLiteral(operand));
				return variable.getValue();
			} else {
				return getLiteral(operand);
			}
		}
		return solve();
	}

	public void setLeftOperand(OperationNode left) {
		this.left = left;
	}

	public void setRightOperand(OperationNode right) {
		this.right = right;
	}

	private Object solve() {
		if (!getOperand().getClass().equals(String.class))
			return getOperand();
		Object a = getLeftOperand().solve();
		Object b = getRightOperand().solve();
		return calculate(a, b, getOperand());

	}
}
