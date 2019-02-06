package sml.data;

import java.util.ArrayList;

import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;
import lexer.Lexer;
import parser.DataTypes;
import parser.parsing.Parser;

public final class Length extends FunctionDeclarationNode {

	public Length() {
		super("length", DataTypes.INTEGER);
		addParameter("iterable", DataTypes.ANY);
		setBody(Parser.parse(Lexer.lex("int counter;for _ in iterable;counter 1+=;end;return counter;", "Length.java")));
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		return getBody().run();
	}

}
	