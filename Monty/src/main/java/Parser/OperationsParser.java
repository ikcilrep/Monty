package Parser;

import java.util.List;
import java.util.Stack;

import AST.OperationNode;
import AST.VariableNode;
import Lexer.MontyToken;
import Parser.Exceptions.AmbiguousResultException;
import Parser.Exceptions.UnknownLiteralException;

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
	/*
	 *	Parses list of tokens to abstract syntax tree.
	 */
	public static OperationNode parse(List<MontyToken> operations, DataTypes dataType) {
		var stack = new Stack<OperationNode>();
		for (MontyToken token : operations) {
			var node = new OperationNode(token.getText());
			switch (token.getType()) {
			case OPERATOR: // If token is operator
				if (!token.getText().equals("!"))
					node.setRightOperand(stack.pop());
				node.setLeftOperand(stack.pop());
				break;
			case IDENTIFIER: // If token is identifier
				node = new OperationNode(new VariableNode(token.getText()));
				break;
			default:
				break;
			}
			stack.push(node);
		}
		if (stack.size() != 1)
			new AmbiguousResultException("Ambiguous result for this operation: " + Tokens.getText(operations)+'.');
		return stack.pop();
	}
}
