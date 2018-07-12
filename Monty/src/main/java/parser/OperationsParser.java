package parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import ast.expressions.ExpressionNode;
//import ast.expressions.FunctionCallNode;
import ast.expressions.OperationNode;
import ast.expressions.VariableNode;
import lexer.MontyToken;
import lexer.TokenTypes;
import parser.exceptions.AmbiguousResultException;
import parser.exceptions.UnknownLiteralException;

public class OperationsParser {

	public static Object toDataType(DataTypes dataType, MontyToken token) {
		switch (dataType) {
		case INTEGER:
			switch (token.getType()) {
			case INTEGER_LITERAL:
				return Integer.parseInt(token.getText());
			case FLOAT_LITERAL:
				return (int) Float.parseFloat(token.getText());
			case BOOLEAN_LITERAL:
				if (token.getText().equals("true"))
					return 1;
				else if (token.getText().equals("false"))
					return 0;
			case STRING_LITERAL:
				if (token.getText().matches("[+-]?[0-9]+"))
					return Integer.parseInt(token.getText());
				else
					new UnknownLiteralException("Unknown literal for this type: " + token.getText() + '.');

			default:
				new UnknownLiteralException("Unknown literal for this type: " + token.getText() + '.');
			}
		case FLOAT:
			switch (token.getType()) {
			case INTEGER_LITERAL:
				return (float) Integer.parseInt(token.getText());
			case FLOAT_LITERAL:
				return Float.parseFloat(token.getText());
			case BOOLEAN_LITERAL:
				if (token.getText().equals("true"))
					return 1.0;
				else if (token.getText().equals("false"))
					return 0.0;
			case STRING_LITERAL:
				if (token.getText().matches("[+-]?[0-9]+\\.[0-9]+"))
					return Float.parseFloat(token.getText());
				else
					new UnknownLiteralException("Unknown literal for this type: " + token.getText() + '.');
			default:
				new UnknownLiteralException("Unknown literal for this type: " + token.getText() + '.');
			}
		case STRING:
			return token.getText();
		case BOOLEAN:
			switch (token.getType()) {
			case INTEGER_LITERAL:
				if (Integer.parseInt(token.getText()) > 0)
					return true;
				else
					return false;
			case FLOAT_LITERAL:
				if (Float.parseFloat(token.getText()) > 0.0)
					return true;
				else
					return false;
			case BOOLEAN_LITERAL:
				if (token.getText().equals("true"))
					return true;
				else if (token.getText().equals("false"))
					return false;
			case STRING_LITERAL:
				if (token.getText().equals("true"))
					return true;
				else if (token.getText().equals("false"))
					return false;
			default:
				new UnknownLiteralException("Unknown literal for this type: " + token.getText() + '.');
			}
		default:
			new UnknownLiteralException("Unknown literal: " + token.getText() + '.');

		}
		return null;
	}

	public static List<ArrayList<MontyToken>> split(TokenTypes splitOnIt, List<MontyToken> list) {
		ArrayList<ArrayList<MontyToken>> newList = new ArrayList<>();
		int i = 0;
		for (MontyToken t : list) {
			if (t.getText().equals(splitOnIt)) {
				newList.add(new ArrayList<MontyToken>());
				i++;
			} else
				newList.get(i).add(t);

		}
		return newList;
	}

	/*
	 * Parses list of tokens to abstract syntax tree.
	 */
	public static ExpressionNode parse(List<MontyToken> operations, DataTypes dataType) {
		var stack = new Stack<OperationNode>();

		for (int i = 0; i < operations.size(); i++) {
			MontyToken token = operations.get(i);
			var node = new OperationNode(token.getText());
			switch (token.getType()) {
			case OPERATOR: // If token is operator
				if (!token.getText().equals("!"))
					node.setRightOperand(stack.pop());
				node.setLeftOperand(stack.pop());
				break;
			case IDENTIFIER: // If token is identifier
				/*
				 * if (operations.get(++i).equals(TokenTypes.BRACKET)) { var arguments = new
				 * ArrayList<ExpressionNode>(); int j; for (j = ++i;
				 * !operations.get(j).equals(TokenTypes.BRACKET); ++j, ++i); for
				 * (List<MontyToken> splited : split(TokenTypes.COMMA,operations.subList(i, j)))
				 * { arguments.add(parse(splited, dataType)); } node = new OperationNode(new
				 * FunctionCallNode(name)) } else
				 */
				node = new OperationNode(new VariableNode(token.getText()));
				break;
			default:
				break;
			}
			stack.push(node);
			i++;
		}
		if (stack.size() != 1)
			new AmbiguousResultException("Ambiguous result for this operation: " + Tokens.getText(operations) + '.');
		return stack.pop();
	}
}
