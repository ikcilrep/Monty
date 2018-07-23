package parser;

import java.util.List;

import lexer.MontyToken;
import lexer.TokenTypes;

public class Tokens {
	public static String getText(List<MontyToken> tokens) {
		var result = new StringBuilder();
		MontyToken next = null;
		int i = 0;
		for (MontyToken token : tokens) {
			result.append(token.getText());
			if (i + 1 < tokens.size())
				next = tokens.get(i + 1);
			if (!(token.getType().equals(TokenTypes.BRACKET) || token.getType().equals(TokenTypes.COMMA)
					|| token.getType().equals(TokenTypes.DOT) || next == null
					|| next.getType().equals(TokenTypes.BRACKET) || next.getType().equals(TokenTypes.COMMA)
					|| next.getType().equals(TokenTypes.DOT)))
				result.append(' ');
			i++;
			next = null;
		}
		return result.toString();
	}

	public static String getTypesToString(List<MontyToken> tokens) {
		var result = new StringBuilder();
		for (MontyToken token : tokens) {
			result.append(token.getType());
			result.append(' ');
		}
		return result.toString();
	}

	public static DataTypes getDataType(TokenTypes type) {
		switch (type) {
		case INTEGER_LITERAL:
		case INTEGER_KEYWORD:
			return DataTypes.INTEGER;
		case FLOAT_LITERAL:
		case FLOAT_KEYWORD:
			return DataTypes.FLOAT;
		case BOOLEAN_LITERAL:
		case BOOLEAN_KEYWORD:
			return DataTypes.BOOLEAN;
		case STRING_LITERAL:
		case STRING_KEYWORD:
			return DataTypes.STRING;
		case ARRAY_KEYWORD:
			return DataTypes.ARRAY;
		case ANY_KEYWORD:
			return DataTypes.ANY;
		case VOID_KEYWORD:
			return DataTypes.VOID;
		default:
			return null;
		}
	}
}
