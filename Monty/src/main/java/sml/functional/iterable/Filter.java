package sml.functional.iterable;

import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;
import lexer.Lexer;
import monty.IOBlocks;
import parser.parsing.Parser;

public final class Filter extends FunctionDeclarationNode {
	private static Block code;
	static {
		code = Parser.parse(Lexer.lex("var list = [];var i = 0;for x in iterable;if function.call(x);"
				+ "list.add(x);i += 1;end;end;return Iterable(list);", "Filter.java"));
		code.addFunction(IOBlocks.list);
		code.addFunction(IOBlocks.iterable);
	}

	public Filter() {
		super("filter");
		setBody(code);
		addParameter("iterable");
		addParameter("function");
	}

	@Override
	public Iterable call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		return (Iterable) getBody().run();
	}

}
