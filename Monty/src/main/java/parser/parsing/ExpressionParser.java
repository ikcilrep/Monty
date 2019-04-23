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

import java.math.BigInteger;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

import ast.Block;
import ast.expressions.ConstantNode;
import ast.expressions.FunctionCallNode;
import ast.expressions.OperationNode;
import ast.expressions.VariableNode;
import lexer.OptimizedTokensArray;
import lexer.Token;
import parser.DataTypes;
import parser.LogError;
import parser.Tokens;
import sml.data.string.StringStruct;

public class ExpressionParser {
	/*
	 * Parses list of tokens to abstract syntax tree.
	 */
	private static LinkedList<FunctionCallNode> functions;
	private static LinkedList<FunctionCallNode> lists;

	private final static HashMap<String, ConstantNode> LITERALS = new HashMap<>();

	private final static HashMap<String, ConstantNode> STRING_LITERALS = new HashMap<>();
	private final static OperationNode parse(Block parent, OptimizedTokensArray tokens) {
		return parse(parent, tokens, new Stack<>(), new IntegerHolder(0));
	}

	private final static OperationNode parse(Block parent, OptimizedTokensArray tokens, Stack<OperationNode> stack,
			IntegerHolder i) {
		if (i.i < tokens.length()) {
			var token = tokens.get(i.i);
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
				return recParseIdentifier(parent, tokens, stack, i);
			case OPENING_SQUARE_BRACKET:
				return recParseList(parent, tokens, stack, i);
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
			return parse(parent, tokens, stack, i);
		}
		if (stack.size() != 1)
			new LogError("Ambiguous result for operation.", tokens.get(0));
		return stack.pop();
	}

	private final static OperationNode parseFunction(Block parent, OptimizedTokensArray tokens, IntegerHolder i) {
		var token = tokens.get(i.i);
		var function = functions.poll();
		var node = new OperationNode(function, parent);
		node.setFileName(token.getFileName());
		node.setLine(token.getLine());
		return node;
	}

	private final static OperationNode parseIdentifier(Block parent, OptimizedTokensArray array, IntegerHolder i) {
		if (array.get(i.i).isFunction())
			return parseFunction(parent, array, i);
		return parseVariable(parent, array, i);
	}

	public final static OperationNode parseInfix(Block parent, OptimizedTokensArray tokens) {
		return parse(parent, Converter.infixToSuffix(tokens, parent));
	}

	public final static OperationNode parseInfix(Block parent, OptimizedTokensArray tokens, int start) {
		return parse(parent, Converter.infixToSuffix(tokens, parent), new Stack<>(), new IntegerHolder(start));
	}

	private final static OperationNode parseVariable(Block parent, OptimizedTokensArray array, IntegerHolder i) {
		var token = array.get(i.i);
		var variable = new VariableNode(token.getText());
		var node = new OperationNode(variable, parent);
		node.setFileName(token.getFileName());
		node.setLine(token.getLine());
		return node;
	}

	private final static OperationNode recParseIdentifier(Block parent, OptimizedTokensArray tokens,
			Stack<OperationNode> stack, IntegerHolder i) {
		stack.push(parseIdentifier(parent, tokens, i));
		i.i++;
		return parse(parent, tokens, stack, i);
	}

	private final static OperationNode recParseList(Block parent, OptimizedTokensArray tokens,
			Stack<OperationNode> stack, IntegerHolder i) {
		stack.push(new OperationNode(lists.poll(), parent));
		i.i++;
		return parse(parent, tokens, stack, i);
	}

	public static void setFunctions(LinkedList<FunctionCallNode> functions) {
		ExpressionParser.functions = functions;
	}

	public static void setLists(LinkedList<FunctionCallNode> lists) {
		ExpressionParser.lists = lists;
	}

	private final static ConstantNode toDataType(Token token, DataTypes dataType) {
		// Returns values with proper data type.
		var literal = token.getText();
		if (dataType == null)
			new LogError("Unexpected token \"" + literal + "\"", token);
		if (dataType.equals(DataTypes.ANY))
			if (STRING_LITERALS.containsKey(literal))
				return STRING_LITERALS.get(literal);
			else {
				var newStringLiteral = new ConstantNode(new StringStruct(literal));
				STRING_LITERALS.put(literal, newStringLiteral);
				return newStringLiteral;
			}
		if (LITERALS.containsKey(literal))
			return LITERALS.get(literal);
		Object valueOfDataType = null;
		switch (dataType) {
		case INTEGER:
			try {
				valueOfDataType = Integer.parseInt(literal);
			} catch (NumberFormatException e) {
				valueOfDataType = new BigInteger(literal);
			}
			break;
		case FLOAT:
			try {
				valueOfDataType = Double.parseDouble(literal);
			} catch (NumberFormatException e) {
				new LogError("Float overflow.", token);
			}
			break;
		case BOOLEAN:
			valueOfDataType = Boolean.parseBoolean(literal);
			break;
		default:
			new LogError("There isn't constant of " + dataType.toString().toLowerCase());
		}
		var newLiteral = new ConstantNode(valueOfDataType);
		LITERALS.put(literal, newLiteral);
		return newLiteral;
	}

}