package monty;

import java.io.FileNotFoundException;
import java.util.List;
import lexer.LexerConfig;
import lexer.MontyToken;
import parser.parsing.Parser;


public class Main {
	public static String[] argv = null;
 	public static void main(String[] args) throws FileNotFoundException {
 		argv = args;
		var lb = LexerConfig.getLexer(FileIO.readFile(Main.class.getResource("Examples/fibonacci.mt").getPath()));

		List<MontyToken> tokens = lb.getAllTokens();
		Parser.parse(tokens).run();
	}
}
