package sml.functional.iterable;

import java.util.ArrayList;

import ast.expressions.OperationNode;
import lexer.Lexer;
import parser.DataTypes;
import parser.parsing.Parser;
import sml.data.Length;
import sml.data.Method;
import sml.data.array.NewArray;

public class ToArray extends Method<Iterable> {

	public ToArray(Iterable parent) {
		super(parent, "toArray", DataTypes.ANY);
		var body = Parser.parse(Lexer.lex(
				"any result [A]().setLength(length(This)) =;int i 0 =;for x in This;result.set(i, x);i 1 +=;end;return result;",
				"ToArray.java"));
		setBody(body);
		body.setParent(parent);
		body.addFunction(new Length());
		body.addFunction(new NewArray());
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		return getBody().run();
	}

}
