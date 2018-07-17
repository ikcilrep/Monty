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
import lexer.MontyToken;
import lexer.TokenTypes;
import parser.DataTypes;
import parser.MontyException;
import parser.Tokens;

public class ExpressionParser {
	public static List<ArrayList<MontyToken>> split(TokenTypes splitOnIt, List<MontyToken> list) {
		ArrayList<ArrayList<MontyToken>> newList = new ArrayList<>();
		newList.add(new ArrayList<MontyToken>());
		int i = 0;
		int bracketCounter = 1;
		for (MontyToken t : list) {
			if (t.getType().equals(TokenTypes.BRACKET))
				bracketCounter++;
			if (t.getType().equals(splitOnIt) && bracketCounter % 2 != 0) {
				newList.add(new ArrayList<MontyToken>());
				i++;
			} else
				newList.get(i).add(t);
		}
		return newList;
	}

	public static Object toDataType(String literal, DataTypes dataType) {
		switch (dataType) {
		case INTEGER:
			return new BigInteger(literal);
		case FLOAT:
			return Float.parseFloat(literal);
		case STRING:
			return literal;
		case BOOLEAN:
			return Boolean.parseBoolean(literal);
		}
		return dataType;
	}

	/*
	 * Parses list of tokens to abstract syntax tree.
	 */
	public static OperationNode parse(Block parent, List<MontyToken> tokens) {
		var stack = new Stack<OperationNode>();
		for (int i = 0; i < tokens.size(); i++) {
			MontyToken token = tokens.get(i);
			var node = (OperationNode) null;
			switch (token.getType()) {
			case OPERATOR: // If token is operator
				node = new OperationNode(token.getText(), parent);
				if (!token.getText().equals("!"))
					((OperationNode) node).setRightOperand(stack.pop());
				((OperationNode) node).setLeftOperand(stack.pop());
				break;
			case IDENTIFIER: // If token is identifier
				if (i + 1 < tokens.size() && tokens.get(i + 1).getType().equals(TokenTypes.BRACKET)) {
					int j = 0;
					int openBracketCounter = 1;
					int closeBracketCounter = 0;
					for (j = i + 2; openBracketCounter > closeBracketCounter; j++) {
						if (j >= tokens.size()) {
							if (openBracketCounter > closeBracketCounter)
								new MontyException("Expected closing bracket:\t"
										+ Tokens.getText(tokens.subList(i + 1, tokens.size())));
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
					for (List<MontyToken> ts : split(TokenTypes.COMMA, tokens.subList(i + 2, j - 1))) {
						if (ts.size() == 0)
							break;
						function.addArgument(parse(parent, ts));
					}
					node = new OperationNode(function, parent);
					i = j - 1;
				} else
					node = new OperationNode(new VariableNode(token.getText()), parent);
				break;
			default:
				var dataType = Tokens.getDataType(token.getType());
				node = new OperationNode(new ConstantNode(toDataType(token.getText(), dataType), dataType), parent);
				break;
			}
			stack.push(node);
		}

		if (stack.size() != 1) {
			System.out.println(stack);
			new MontyException("Ambiguous result for this operation:\t" + Tokens.getText(tokens));
		}
		return stack.pop();
	}

}
