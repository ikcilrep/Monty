package sml.functional.iterable;

import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;
import lexer.Lexer;
import monty.IOBlocks;
import parser.DataTypes;
import parser.parsing.Parser;

public final class Map extends FunctionDeclarationNode {
	private static Block code;
	static {
		code = Parser.parse(Lexer.lex("any list = [Nothing] * length(iterable);int i = 0;for x in iterable;"
				+ "list.set(i,function.call(x));i += 1;end;return Iterable(list);", "Map.java"));
		code.addFunction(IOBlocks.length);
		code.addFunction(IOBlocks.list);
		code.addFunction(IOBlocks.iterable);
		code.addVariable(IOBlocks.nothing);
	}

	public Map() {
		super("map", DataTypes.ANY);
		setBody(code);
		addParameter("iterable", DataTypes.ANY);
		addParameter("function", DataTypes.ANY);
	}

	@Override
	public Iterable call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		return (Iterable) getBody().run();
	}

}
