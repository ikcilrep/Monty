package parser;

import java.util.List;

import lexer.MontyToken;
import lexer.TokenTypes;

public class Tokens {
	public static String getText(List<MontyToken> tokens) {
		var result = new StringBuilder();
		for (MontyToken token : tokens) {
			result.append(token.getText());
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
		default:
			return null;
		}
	}
}
