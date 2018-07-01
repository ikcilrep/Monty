package Parser;

import java.util.List;

import Lexer.MontyToken;

public class Tokens {
	public static String getText(List<MontyToken> tokens) {
		var result = new StringBuilder();
		for (MontyToken token : tokens) {
			result.append(token.getText());
			result.append(' ');
		}
		return result.toString();
	}
}
