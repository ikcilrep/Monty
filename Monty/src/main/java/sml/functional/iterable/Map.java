package sml.functional.iterable;

import java.util.ArrayList;

import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;
import lexer.Lexer;
import parser.DataTypes;
import parser.parsing.Parser;
import sml.data.Length;
import sml.data.array.NewArray;

public final class Map extends FunctionDeclarationNode {

	public Map() {
		super("map", DataTypes.ANY);

		var body = Parser.parse(Lexer.lex("any array [A]().setLength(length(iterable)) =;int i 0 =;for x in iterable;"
				+ "array.set(i,function.call(x));i 1 +=;end;return Iterable(array);", "Map.java"));
		setBody(body);
		body.addFunction(new Length());
		body.addFunction(new NewArray());
		body.addFunction(new NewIterable());

		addParameter("iterable", DataTypes.ANY);
		addParameter("function", DataTypes.ANY);
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		return getBody().run();
	}

}
