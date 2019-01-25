package monty;

import ast.Block;
import lexer.Lexer;
import lexer.OptimizedTokensArray;
import parser.parsing.Parser;

public class IOBlocks {

	public static Block readBlockFromFile(String path) {
		var tokens = Lexer.lex(FileIO.readFile(path), path, 1, new OptimizedTokensArray(), 0);
		var block = Parser.parse(tokens);
		block.getFunctions().put("nothing", new sml.data.returning.Nothing());
		return block;
	}

}
