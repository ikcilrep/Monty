package sml.functional.iterable;

import java.util.ArrayList;

import ast.Block;
import ast.expressions.OperationNode;
import lexer.Lexer;
import monty.IOBlocks;
import parser.DataTypes;
import parser.parsing.Parser;
import sml.data.Method;

final class Get extends Method<Iterable> {
	private static Block code;
	static {
		code = Parser.parse(Lexer.lex(
				"int counter;for x in This;if counter i ==;return x;end;counter 1 +=;end;logError(\"Iterable doesn't have \" i \" element\" + +);",
				"Get.java"));
		code.addFunction(IOBlocks.logError);
		code.addFunction(IOBlocks.length);

	}

	public Get(Iterable parent) {
		super(parent, "get", DataTypes.ANY);
		code.setParent(parent);
		setBody(code);
		addParameter("i", DataTypes.INTEGER);
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		return getBody().run();
	}

}
