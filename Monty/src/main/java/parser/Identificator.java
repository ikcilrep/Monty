package parser;

import java.util.List;

import lexer.MontyToken;
import lexer.TokenTypes;

public abstract class Identificator {
	public static boolean isExpression(List<MontyToken> tokens) {
		MontyToken last = null;
		for (MontyToken token : tokens) {
			switch (token.getType()) {
			case OPERATOR:
				if (last.getType().equals(TokenTypes.COMMA) || last.equals(null))
					return false;
				break;
			case INTEGER_LITERAL:
			case FLOAT_LITERAL:
			case BOOLEAN_LITERAL:
			case STRING_LITERAL:
			case IDENTIFIER:
				break;
			case BRACKET:
				if (last.getType().equals(TokenTypes.COMMA) || last.equals(null))
					new MontyException("Unexpected comma before bracket:\t" + Tokens.getText(tokens));
				break;
			case COMMA:
				if ((last.getType().equals(TokenTypes.BRACKET) && last.getText().equals("(")) || last.equals(null))
					new MontyException("Unexpected bracket before comma:\t" + Tokens.getText(tokens));
				break;
			default:
				return false;
			}
			last = token;
		}
		return true;

	}

	public static boolean isPrintStatement(List<MontyToken> tokens) {
		var isFirstTokenPrintKeyword = tokens.get(0).getType().equals(TokenTypes.PRINT_KEYWORD);
		if ((tokens.size() == 1 && isFirstTokenPrintKeyword)
				|| (tokens.size() > 1 && isFirstTokenPrintKeyword && !isExpression(tokens.subList(1, tokens.size()))))
			new MontyException("Expected expression after print keyword:\t" + Tokens.getText(tokens));
		if (!isFirstTokenPrintKeyword)
			return false;

		return true;
	}

	public static boolean isReturnStatement(List<MontyToken> tokens) {
		var isFirstTokenReturnKeyword = tokens.get(0).getType().equals(TokenTypes.RETURN_KEYWORD);

		if (!isFirstTokenReturnKeyword)
			return false;
		if (isFirstTokenReturnKeyword && tokens.size() > 1 && !isExpression(tokens.subList(1, tokens.size())))
			new MontyException("Wrong expression after return keyword:\t" + Tokens.getText(tokens));
		return true;
	}
}
