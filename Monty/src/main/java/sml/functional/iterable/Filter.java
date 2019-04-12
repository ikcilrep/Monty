package sml.functional.iterable;

import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;
import lexer.Lexer;
import monty.IOBlocks;
import parser.DataTypes;
import parser.parsing.Parser;

public final class Filter extends FunctionDeclarationNode {
	private static Block code;
	static {
		code = Parser.parse(Lexer
				.lex("any array [A]().setLength(length(iterable)) =;int i 0 =;for x in iterable;if function.call(x);"
						+ "array.set(i,x);i 1 +=;end;end;return Iterable(array.setLength(i));", "Filter.java"));
		code.addFunction(IOBlocks.length);
		code.addFunction(IOBlocks.array);
		code.addFunction(IOBlocks.iterable);
	}

	public Filter() {
		super("filter", DataTypes.ANY);
		setBody(code);
		addParameter("iterable", DataTypes.ANY);
		addParameter("function", DataTypes.ANY);
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		return getBody().run();
	}

}
