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

package parser.parsing;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import ast.Block;
import ast.expressions.ConstantNode;
import ast.expressions.FunctionCallNode;
import ast.expressions.OperationNode;
import ast.expressions.StructContainer;
import ast.expressions.VariableNode;
import lexer.OptimizedTokensArray;
import lexer.Token;
import lexer.TokenTypes;
import parser.DataTypes;
import parser.Identificator;
import parser.LogError;
import parser.Tokens;

public class ExpressionParser {
	/*
	 * Parses list of tokens to abstract syntax tree.
	 */

	private final static HashMap<String, ConstantNode> literals = new HashMap<>();
	private final static HashMap<String, ConstantNode> stringLiterals = new HashMap<>();

	private final static boolean isFunction(OptimizedTokensArray array, int i) {
		return i + 1 < array.length() && array.get(i + 1).getType().equals(TokenTypes.OPENING_BRACKET);
	}

	public final static OperationNode parse(Block parent, OptimizedTokensArray array) {
		return parse(parent, array, new Stack<>(), new IntegerHolder());
	}

	public final static OperationNode parse(Block parent, OptimizedTokensArray array, Stack<OperationNode> stack,
			IntegerHolder i) {
		if (i.i < array.length()) {
			var token = array.get(i.i);
			OperationNode node = null;
			switch (token.getType()) {
			case OPERATOR: // If token is operator
				node = new OperationNode(token.getText(), parent);
				if (!token.getText().equals("!")) {
					if (stack.isEmpty())
						new LogError("There isn't right operand", token);
					node.setRightOperand(stack.pop());
				} else {
					node.setRightOperand(stack.peek());
				}
				if (stack.isEmpty())
					new LogError("There isn't left operand", token);
				node.setLeftOperand(stack.pop());
				break;
			case IDENTIFIER: // If token is identifier
				return recParseIdentifier(parent, array, stack, i);
			default:
				// Otherwise token in expression can be only constant.
				var dataType = Tokens.getDataType(token.getType());
				node = new OperationNode(toDataType(token, dataType), parent);
				break;
			}
			stack.push(node);
			node.setFileName(token.getFileName());
			node.setLine(token.getLine());
			i.i++;
			return parse(parent, array, stack, i);
		}
		if (stack.size() != 1)
			new LogError("Ambiguous result for operation.", array.get(0));
		return stack.pop();
	}

	private final static OperationNode parseAfterDot(Block parent, StructContainer structContainer,
			OptimizedTokensArray array, IntegerHolder i) {
		if (i.i + 1 < array.length() && array.get(i.i + 1).getType().equals(TokenTypes.DOT))
			if ((i.i += 2) < array.length() && array.get(i.i).getType().equals(TokenTypes.IDENTIFIER)) {
				var variableOrFunctionOperationNode = parseIdentifier(parent, array, i);
				structContainer.setNext(variableOrFunctionOperationNode);
				return variableOrFunctionOperationNode;
			} else
				new LogError("Expression after dot have to be function or variable.", array.get(i.i - 1));
		return null;
	}

	private final static OperationNode parseFunction(Block parent, OptimizedTokensArray array, IntegerHolder i) {
		var token = array.get(i.i);
		int j = 0;
		int openBracketCounter = 1;
		int closeBracketCounter = 0;
		// Separates function arguments from identifier.
		for (j = i.i + 2; openBracketCounter > closeBracketCounter; j++) {
			if (j >= array.length()) {
				if (openBracketCounter > closeBracketCounter)
					new LogError("Expected closing bracket.", token);
				break;
			}
			switch (array.get(j).getType()) {
			case OPENING_BRACKET:
				openBracketCounter++;
				break;
			case CLOSING_BRACKET:
				closeBracketCounter++;
				break;
			default:
				break;
			}

		}
		var function = new FunctionCallNode(token.getText());
		function.setFileName(token.getFileName());
		function.setLine(token.getLine());
		var splited = splitArguments(array.subarray(i.i + 2, j - 1));
		// Adds parsed function arguments to object.
		for (OptimizedTokensArray ts : splited) {
			// If there isn't any arguments breaks loop.
			if (ts.length() == 0)
				break;

			if (!Identificator.isExpression(ts))
				new LogError("Expected expression as function " + token.getText() + " call's argument.", token);
			function.addArgument(parse(parent, ts, new Stack<>(), new IntegerHolder()));
		}
		i.i = j - 1;
		parseAfterDot(parent, function, array, i);
		var node = new OperationNode(function, parent);
		node.setFileName(token.getFileName());
		node.setLine(token.getLine());
		return node;
	}

	private final static OperationNode parseIdentifier(Block parent, OptimizedTokensArray array, IntegerHolder i) {
		if (isFunction(array, i.i))
			return parseFunction(parent, array, i);
		return parseVariable(parent, array, i);
	}

	private final static OperationNode parseVariable(Block parent, OptimizedTokensArray array, IntegerHolder i) {
		var token = array.get(i.i);
		var variable = new VariableNode(token.getText());
		variable.setFileName(token.getFileName());
		variable.setLine(token.getLine());
		parseAfterDot(parent, variable, array, i);
		var node = new OperationNode(variable, parent);
		node.setFileName(token.getFileName());
		node.setLine(token.getLine());
		return node;
	}

	private final static OperationNode recParseIdentifier(Block parent, OptimizedTokensArray array,
			Stack<OperationNode> stack, IntegerHolder i) {
		stack.push(parseIdentifier(parent, array, i));
		i.i++;
		return parse(parent, array, stack, i);
	}

	private final static ArrayList<OptimizedTokensArray> splitArguments(OptimizedTokensArray array) {
		// Splits function arguments into two dimensional array.
		ArrayList<OptimizedTokensArray> newArray = new ArrayList<>();
		newArray.add(new OptimizedTokensArray());
		int i = 0;
		int openBracketCounter = 1;
		int closeBracketCounter = 0;
		for (Token t : array) {
			if (t.getType().equals(TokenTypes.OPENING_BRACKET))
				openBracketCounter++;
			else if (t.getType().equals(TokenTypes.CLOSING_BRACKET))
				closeBracketCounter++;
			// If every pair of bracket except last is closed and actual token type is comma
			// appends new arguments.
			if (t.getType().equals(TokenTypes.COMMA) && openBracketCounter - 1 == closeBracketCounter) {
				newArray.add(new OptimizedTokensArray());
				i++;
			} else {
				newArray.get(i).append(t);
			}
		}
		return newArray;
	}

	private final static ConstantNode toDataType(Token token, DataTypes dataType) {
		// Returns values with proper data type.
		var literal = token.getText();
		if (dataType == null)
			new LogError("Unexpected token \"" + literal + "\"", token);
		if (dataType.equals(DataTypes.STRING) && stringLiterals.containsKey(literal))
			return stringLiterals.get(literal);
		if (literals.containsKey(literal))
			return literals.get(literal);
		Object valueOfDataType = null;
		switch (dataType) {
		case INTEGER:
			valueOfDataType = new BigInteger(literal);
			break;
		case REAL:
			valueOfDataType = new BigDecimal(literal);
			break;
		case BOOLEAN:
			valueOfDataType = Boolean.parseBoolean(literal);
			break;
		case STRING:
			var newStringLiteral = new ConstantNode(literal, dataType);
			stringLiterals.put(literal, newStringLiteral);
			return newStringLiteral;
		default:
			new LogError("There isn't constant of " + dataType.toString().toLowerCase());
		}
		var newLiteral = new ConstantNode(valueOfDataType, dataType);
		literals.put(literal, newLiteral);
		return newLiteral;
	}

}