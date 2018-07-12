package parser;

import java.util.List;

import lexer.MontyToken;

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
