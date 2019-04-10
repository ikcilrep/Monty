package sml.functional.iterable;

import java.util.ArrayList;

import ast.expressions.OperationNode;
import lexer.Lexer;
import parser.DataTypes;
import parser.parsing.Parser;
import sml.data.Length;
import sml.data.Method;
import sml.errors.LogError;

final class Get extends Method<Iterable> {

	public Get(Iterable parent) {
		super(parent,"get", DataTypes.ANY);
		var body = Parser.parse(Lexer.lex(
				"int counter;for x in This;if counter i ==;return x;end;counter 1 +=;end;logError(\"Iterable doesn't have \" i \" element\" + +);",
				"Get.java"));
		setBody(body);
		body.setParent(parent);
		body.addFunction(new LogError());
		body.addFunction(new Length());
		addParameter("i", DataTypes.INTEGER);
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		return getBody().run();
	}

}
