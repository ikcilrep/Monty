package sml.functional.iterable;

import java.util.ArrayList;

import ast.Block;
import ast.expressions.OperationNode;
import lexer.Lexer;
import monty.IOBlocks;
import parser.DataTypes;
import parser.parsing.Parser;
import sml.data.Method;
import sml.data.list.List;

final class ToList extends Method<Iterable> {
	private static Block code;
	static {
		code = Parser.parse(Lexer.lex(
				"any result = [](Nothing) * length(This);int i = 0;for x in This;result.set(i, x);i += 1;end;return result;",
				"ToArray.java"));
		code.addFunction(IOBlocks.length);
		code.addFunction(IOBlocks.list);
		code.addVariable(IOBlocks.nothing);

	}

	public ToList(Iterable parent) {
		super(parent, "toList", DataTypes.ANY);
		setBody(code);
		code.setParent(parent);
	}

	@Override
	public List call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		return (List) getBody().run();
	}

}
