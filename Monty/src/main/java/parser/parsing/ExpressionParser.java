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

package parser.parsing;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import ast.Block;
import ast.expressions.ConstantNode;
import ast.expressions.FunctionCallNode;
import ast.expressions.OperationNode;
import ast.expressions.VariableNode;
import lexer.Token;
import lexer.TokenTypes;
import parser.DataTypes;
import parser.Identificator;
import parser.LogError;
import parser.Tokens;

public class ExpressionParser {
	private static List<ArrayList<Token>> splitArguments(List<Token> list) {
		// Splits function arguments into two dimensional array.
		ArrayList<ArrayList<Token>> newList = new ArrayList<>();
		newList.add(new ArrayList<Token>());
		int i = 0;
		int openBracketCounter = 1;
		int closeBracketCounter = 0;
		for (Token t : list) {
			if (t.getText().equals("("))
				openBracketCounter++;
			else if (t.getText().equals(")"))
				closeBracketCounter++;
			// If every pair of bracket except last is closed and actual token type is comma
			// adds new arguments.
			if (t.getType().equals(TokenTypes.COMMA) && openBracketCounter - 1 == closeBracketCounter) {
				newList.add(new ArrayList<Token>());
				i++;
			} else
				newList.get(i).add(t);
		}
		return newList;
	}

	private static Object toDataType(String literal, DataTypes dataType) {
		// Returns values with proper data type.
		switch (dataType) {
		case INTEGER:
			return new BigInteger(literal);
		case FLOAT:
			return Float.parseFloat(literal);
		case STRING:
			return literal;
		case BOOLEAN:
			return Boolean.parseBoolean(literal);
		default:
			new LogError("There isn't constant of " + dataType.toString().toLowerCase());
		}
		return dataType;
	}

	/*
	 * Parses list of tokens to abstract syntax tree.
	 */
	public static OperationNode parse(Block parent, List<Token> tokens) {
		var stack = new Stack<OperationNode>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			OperationNode node = null;
			switch (token.getType()) {
			case OPERATOR: // If token is operator
				node = new OperationNode(token.getText(), parent);
				if (!token.getText().equals("!")) {
					if (stack.isEmpty())
						new LogError("There isn't right operand", token);
					node.setRightOperand(stack.pop());
				}
				if (stack.isEmpty())
					new LogError("There isn't left operand", token);
				node.setLeftOperand(stack.pop());
				break;
			case IDENTIFIER: // If token is identifier
				if (i + 1 < tokens.size() && tokens.get(i + 1).getType().equals(TokenTypes.BRACKET)) {
					int j = 0;
					int openBracketCounter = 1;
					int closeBracketCounter = 0;
					// Separates function arguments from identifier.
					for (j = i + 2; openBracketCounter > closeBracketCounter; j++) {
						if (j >= tokens.size()) {
							if (openBracketCounter > closeBracketCounter)
								new LogError("Expected closing bracket:\t"
										+ Tokens.getText(tokens.subList(i + 1, tokens.size())), token);
							break;
						}
						switch (tokens.get(j).getText()) {
						case "(":
							openBracketCounter++;
							break;
						case ")":
							closeBracketCounter++;
							break;
						default:
							break;
						}
					}
					var function = new FunctionCallNode(token.getText());
					var splited = splitArguments(tokens.subList(i + 2, j - 1));
					// Adds parsed function arguments to object.
					for (List<Token> ts : splited) {
						// If there isn't any arguments breaks loop.
						if (ts.size() == 0)
							break;
						if (!Identificator.isExpression(ts))
							new LogError("Expected expression as function " + token.getText()
									+ " call's argument:\t" + Tokens.getText(ts), token);
						function.addArgument(parse(parent, ts));
					}
					node = new OperationNode(function, parent);
					// I is after function arguments.
					i = j - 1;
				} else
					node = new OperationNode(new VariableNode(token.getText()), parent);
				break;
			default:
				// Otherwise token in expression can be only constant.
				var dataType = Tokens.getDataType(token.getType());

				node = new OperationNode(new ConstantNode(toDataType(token.getText(), dataType), dataType), parent);
				break;
			}
			stack.push(node);
		}
		if (stack.size() != 1)
			new LogError("Ambiguous result for this operation:\t" + Tokens.getText(tokens), tokens.get(0));

		return stack.pop();
	}

}
