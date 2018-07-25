package monty;

import java.io.FileNotFoundException;
import java.util.List;
import lexer.LexerConfig;
import lexer.MontyToken;
import parser.parsing.Parser;

public class Main {
	public static String[] argv = null;
	public static String path;

	public static void main(String[] args) throws FileNotFoundException {
		argv = args;
		path = Main.class.getResource("Examples/xorEncryption.mt").getPath();
		// path = args[0];
		var lb = LexerConfig.getLexer(FileIO.readFile(path));

		List<MontyToken> tokens = lb.getAllTokens();
		Importing.setLibraries();
		var block = Parser.parse(tokens);
		block.addFunction(new sml.data.returning.Nothing());
		block.run();
	}
}
