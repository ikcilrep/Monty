package monty;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import lexer.LexerConfig;
import lexer.MontyToken;
import parser.parsing.Parser;


public class Main {
	public static void main(String[] args) throws FileNotFoundException {
		BufferedReader br = null;
		var code = new StringBuilder();
		try {
			br = new BufferedReader(new FileReader(Main.class.getResource("Examples/sample.mt").getPath()));
			String line;
			while ((line = br.readLine()) != null) {
				code.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		var lb = LexerConfig.getLexer(code.toString());

		List<MontyToken> tokens = lb.getAllTokens();
		Parser.parse(tokens).run();
	}
}
