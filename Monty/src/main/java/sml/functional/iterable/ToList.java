package sml.functional.iterable;

import java.util.ArrayList;

import ast.expressions.OperationNode;
import lexer.Lexer;
import monty.IOBlocks;
import parser.DataTypes;
import parser.parsing.Parser;
import sml.data.Method;

final class ToList extends Method<Iterable> {

	public ToList(Iterable parent) {
		super(parent, "toList", DataTypes.ANY);
		var body = Parser.parse(Lexer.lex(
				"any result [](Nothing) length(This) * =;int i 0 =;for x in This;result.set(i, x);i 1 +=;end;return result;",
				"ToArray.java"));
		setBody(body);
		body.setParent(parent);
		body.addFunction(IOBlocks.length);
		body.addFunction(IOBlocks.list);
		body.addVariable(IOBlocks.nothing);
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		return getBody().run();
	}

}
