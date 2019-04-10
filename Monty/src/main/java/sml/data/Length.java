package sml.data;

import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;
import lexer.Lexer;
import parser.DataTypes;
import parser.parsing.Parser;

public final class Length extends FunctionDeclarationNode {
	private final static Block code = Parser
			.parse(Lexer.lex("int counter;for _ in iterable;counter 1+=;end;return counter;", "Length.java"));

	public Length() {
		super("length", DataTypes.INTEGER);
		setBody(code);
		addParameter("iterable", DataTypes.ANY);
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		return getBody().run();
	}

}
