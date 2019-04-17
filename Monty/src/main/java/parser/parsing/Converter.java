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
	private static Set<String> notAssociative = Set.of("<", "<=", ">=", ">");

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
		var lists = new LinkedList<FunctionCallNode>();
		var wasLastIdentifier = false;
		for (var i = new IntegerHolder(0); i.i < tokens.length(); i.i++) {
			var token = tokens.get(i.i);
			var type = token.getType();
			switch (type) {
			case OPERATOR:
				if (!operatorStack.empty()) {
					var top = operatorStack.peek();
					if (!top.getType().equals(TokenTypes.OPENING_BRACKET)) {
						var operatorAtTheTop = top.getText();
						int topPrecedence = precedence.get(operatorAtTheTop);
						int thisPrecedence = precedence.get(token.getText());
						while (topPrecedence > thisPrecedence
								|| (topPrecedence == thisPrecedence && isLeftAssociative(operatorAtTheTop))) {
							outputQueue.append(operatorStack.pop());
							if (operatorStack.empty())
								break;
							top = operatorStack.peek();
							if (top.getType().equals(TokenTypes.OPENING_BRACKET))
								break;
							operatorAtTheTop = top.getText();
							topPrecedence = precedence.get(top.getText());
							thisPrecedence = precedence.get(token.getText());
						}
					}
				}
				operatorStack.push(token);
				break;
			case OPENING_SQUARE_BRACKET:
				lists.add(parseList(tokens, parent, i));
				outputQueue.append(token);
				break;
			case OPENING_BRACKET:
				if (wasLastIdentifier) {
					var last = outputQueue.get(outputQueue.length() - 1);
					last.setFunction(true);
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
		ExpressionParser.setLists(lists);
		return outputQueue;
	}

	private final static boolean isLeftAssociative(String operator) {
		return !(rightAssociative.contains(operator) || notAssociative.contains(operator));
	}

	private final static FunctionCallNode parseFunction(String name, OptimizedTokensArray tokens, Block parent,
			IntegerHolder i) {
		var function = new FunctionCallNode(name);
		function.setArguments(parseExpressionsSeparatedByComma(tokens, parent, i));
		return function;
	}

	private final static FunctionCallNode parseList(OptimizedTokensArray tokens, Block parent, IntegerHolder i) {
		var function = new FunctionCallNode("List");
		function.setArguments(parseExpressionsSeparatedByComma(tokens, parent, i));
		return function;
	}

	private final static ArrayList<OperationNode> parseExpressionsSeparatedByComma(OptimizedTokensArray tokens,
			Block parent, IntegerHolder i) {
		var result = new ArrayList<OperationNode>();
		var expression = new OptimizedTokensArray();
		var type = tokens.get(i.i).getType();
		int openedBrackets = 0, openedSquareBrackets = 0;
		if (type.equals(TokenTypes.OPENING_BRACKET))
			openedBrackets++;
		else if (type.equals(TokenTypes.OPENING_SQUARE_BRACKET))
			openedSquareBrackets++;
		i.i++;
		for (; openedBrackets > 0 || openedSquareBrackets > 0; i.i++) {
			var token = tokens.get(i.i);
			try {
				type = token.getType();
			} catch (NullPointerException e) {
				new LogError("Unclosed bracket.", tokens.get(i.i - 1));
			}

			if (type.equals(TokenTypes.OPENING_BRACKET))
				openedBrackets++;
			else if (type.equals(TokenTypes.OPENING_SQUARE_BRACKET))
				openedSquareBrackets++;
			else if (type.equals(TokenTypes.CLOSING_BRACKET))
				openedBrackets--;
			else if (type.equals(TokenTypes.CLOSING_SQUARE_BRACKET))
				openedSquareBrackets--;
			// If every pair of open and close except last is closed and actual token type
			// is comma
			// appends new expression.
			var sum = openedBrackets + openedSquareBrackets;
			if ((type.equals(TokenTypes.COMMA) && sum == 1) || sum == 0) {
				if (expression.length() == 0)
					if (sum == 1)
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
