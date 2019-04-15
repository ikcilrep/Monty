package parser.parsing;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.Stack;

import ast.Block;
import ast.expressions.FunctionCallNode;
import ast.expressions.OperationNode;
import lexer.OptimizedTokensArray;
import lexer.Token;
import lexer.TokenTypes;
import parser.LogError;

public class Converter {
	private static HashMap<String, Integer> precedence;
	private static Set<String> rightAssociative = Set.of("=", "+=", "-=", "*=", "/=", "%=", "&=", "^=", "|=", "<<=",
			">>=");
	static {
		precedence = new HashMap<>();
		precedence.put(".", 16);
		precedence.put("!", 15);
		precedence.put("*", 14);
		precedence.put("/", 14);
		precedence.put("%", 14);
		precedence.put("+", 13);
		precedence.put("-", 13);
		precedence.put("<<", 10);
		precedence.put(">>", 10);
		precedence.put("<", 9);
		precedence.put("<=", 9);
		precedence.put(">", 9);
		precedence.put(">=", 9);
		precedence.put("==", 8);
		precedence.put("!=", 8);
		precedence.put("&", 7);
		precedence.put("^", 6);
		precedence.put("|", 5);
		precedence.put("=", 1);
		precedence.put("+=", 1);
		precedence.put("-=", 1);
		precedence.put("*=", 1);
		precedence.put("/=", 1);
		precedence.put("%=", 1);
		precedence.put("&=", 1);
		precedence.put("^=", 1);
		precedence.put("|=", 1);
		precedence.put("<<=", 1);
		precedence.put(">>=", 1);

	}

	public static OptimizedTokensArray infixToSuffix(OptimizedTokensArray tokens, Block parent) {
		var outputQueue = new OptimizedTokensArray();
		var operatorStack = new Stack<Token>();
		var functions = new LinkedList<FunctionCallNode>();
		var wasLastIdentifier = false;
		for (var i = new IntegerHolder(0); i.i < tokens.length(); i.i++) {
			var token = tokens.get(i.i);
			var type = token.getType();
			switch (type) {
			case OPERATOR:
				if (!operatorStack.empty()) {
					var top = operatorStack.peek();
					if (!top.getType().equals(TokenTypes.OPENING_BRACKET)) {
						var topText = top.getText();
						int topPrecedence = precedence.get(topText);
						int thisPrecedence = precedence.get(token.getText());
						while (topPrecedence > thisPrecedence
								|| (topPrecedence == thisPrecedence && !rightAssociative.contains(topText))) {
							outputQueue.append(operatorStack.pop());
							if (operatorStack.empty())
								break;
							top = operatorStack.peek();
							if (top.getType().equals(TokenTypes.OPENING_BRACKET))
								break;
							topText = top.getText();
							topPrecedence = precedence.get(top.getText());
							thisPrecedence = precedence.get(token.getText());
						}
					}
				}
				operatorStack.push(token);
				break;
			case OPENING_BRACKET:
				if (wasLastIdentifier) {
					var last = outputQueue.get(outputQueue.length() - 1);
					last.setFunction(true);
					i.i++;
					functions.add(parseFunction(last.getText(), tokens, parent, i));
				} else
					operatorStack.push(token);
				break;
			case CLOSING_BRACKET:
				while (!operatorStack.peek().getType().equals(TokenTypes.OPENING_BRACKET))
					try {
						outputQueue.append(operatorStack.pop());
					} catch (EmptyStackException e) {
						new LogError("Mismatched brackets.", token);
					}
				operatorStack.pop();
				break;
			default:
				outputQueue.append(token);
				break;
			}
			wasLastIdentifier = type.equals(TokenTypes.IDENTIFIER);
		}
		while (!operatorStack.empty())
			outputQueue.append(operatorStack.pop());
		ExpressionParser.setFunctions(functions);
		return outputQueue;
	}

	private final static FunctionCallNode parseFunction(String name, OptimizedTokensArray tokens, Block parent,
			IntegerHolder i) {
		var function = new FunctionCallNode(name);
		function.setArguments(parseExpressionsSeparatedByComma(TokenTypes.OPENING_BRACKET, TokenTypes.CLOSING_BRACKET,
				tokens, parent, i));
		return function;
	}

	private final static ArrayList<OperationNode> parseExpressionsSeparatedByComma(TokenTypes open, TokenTypes close,
			OptimizedTokensArray tokens, Block parent, IntegerHolder i) {
		var result = new ArrayList<OperationNode>();
		var expression = new OptimizedTokensArray();
		int opened = 1;
		for (; opened > 0; i.i++) {
			var token = tokens.get(i.i);
			var type = token.getType();
			if (type.equals(open))
				opened++;
			else if (type.equals(close))
				opened--;
			// If every pair of open and close except last is closed and actual token type
			// is comma
			// appends new expression.
			if ((type.equals(TokenTypes.COMMA) && opened == 1) || opened == 0) {
				if (expression.length() == 0)
					if (opened == 1)
						new LogError("Unexpected comma.", tokens.get(0));
					else
						continue;
				result.add(ExpressionParser.parseInfix(parent, expression));
				expression.clear();
			} else
				expression.append(token);
		}
		i.i--;
		return result;
	}
}
