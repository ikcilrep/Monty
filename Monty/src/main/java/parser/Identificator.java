package parser;

import java.util.List;

import lexer.MontyToken;
import lexer.TokenTypes;

public abstract class Identificator {
	public static boolean isExpression(List<MontyToken> tokens) {
		MontyToken last = null;
		int i = 0;
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
					new MontyException("Unexpected comma before bracket:\t" + Tokens.getText(tokens.subList(0, i + 1)));
				break;
			case COMMA:
				if ((last.getType().equals(TokenTypes.BRACKET) && last.getText().equals("(")) || last.equals(null))
					new MontyException("Unexpected bracket before comma:\t" + Tokens.getText(tokens.subList(0, i + 1)));
				break;
			default:
				return false;
			}
			last = token;
			i++;
		}
		return true;

	}

	public static boolean isPrintStatement(List<MontyToken> tokens) {
		var isFirstTokenPrintKeyword = tokens.get(0).getType().equals(TokenTypes.PRINT_KEYWORD);
		if (!isFirstTokenPrintKeyword)
			return false;
		if (tokens.size() == 1 && isFirstTokenPrintKeyword)
			new MontyException("Expected expression after \"print\" keyword:\t" + Tokens.getText(tokens));
		if (tokens.size() > 1) {
			var expression = tokens.subList(1, tokens.size());
			if (isFirstTokenPrintKeyword && !isExpression(expression))
				new MontyException("Wrong expression after \"print\" keyword:\t" + Tokens.getText(expression));
		}
		return true;
	}

	public static boolean isReturnStatement(List<MontyToken> tokens) {
		var isFirstTokenReturnKeyword = tokens.get(0).getType().equals(TokenTypes.RETURN_KEYWORD);

		if (!isFirstTokenReturnKeyword)
			return false;
		if (tokens.size() > 1 && !isExpression(tokens.subList(1, tokens.size())))
			new MontyException("Wrong expression after return keyword:\t" + Tokens.getText(tokens));
		return true;
	}

	public static boolean isVariableDeclaration(List<MontyToken> tokens) {
		var isFirstTokenVarKeyword = tokens.get(0).getType().equals(TokenTypes.VAR_KEYWORD);
		var isSecondTokenDataTypeKeyword = tokens.size() > 1
				&& (tokens.get(1).getType().equals(TokenTypes.INTEGER_KEYWORD)
						|| tokens.get(1).getType().equals(TokenTypes.FLOAT_KEYWORD)
						|| tokens.get(1).getType().equals(TokenTypes.STRING_KEYWORD)
						|| tokens.get(1).getType().equals(TokenTypes.BOOLEAN_KEYWORD));
		if (!isFirstTokenVarKeyword)
			return false;
		if (!isSecondTokenDataTypeKeyword)
			new MontyException("Expected data type declaration after \"var\" keyword:\t" + Tokens.getText(tokens));
		if (isSecondTokenDataTypeKeyword && tokens.size() == 2)
			new MontyException("Expected expression after data type declaration:\t" + Tokens.getText(tokens));
		var expression = tokens.subList(2, tokens.size());
		if (!isExpression(expression))
			new MontyException("Wrong expression after data type declaration:\t" + Tokens.getText(expression));
		if (!tokens.get(2).getType().equals(TokenTypes.IDENTIFIER))
			new MontyException("Expected identifier after data type declaration:\t" + Tokens.getText(expression));

		return true;
	}

	public static boolean isFunctionDeclaration(List<MontyToken> tokens) {
		var isFirstTokenFuncKeyword = tokens.get(0).getType().equals(TokenTypes.FUNC_KEYWORD);
		var isSecondTokenDataTypeKeyword = tokens.size() > 1
				&& (tokens.get(1).getType().equals(TokenTypes.INTEGER_KEYWORD)
						|| tokens.get(1).getType().equals(TokenTypes.FLOAT_KEYWORD)
						|| tokens.get(1).getType().equals(TokenTypes.STRING_KEYWORD)
						|| tokens.get(1).getType().equals(TokenTypes.BOOLEAN_KEYWORD));
		var isThirdTokenIdentifier = tokens.size() > 2 && tokens.get(2).getType().equals(TokenTypes.IDENTIFIER);
		if (!isFirstTokenFuncKeyword)
			return false;
		if (!isSecondTokenDataTypeKeyword)
			new MontyException("Expected data type declaration after \"func\" keyword:\t"
					+ Tokens.getText(tokens.subList(1, tokens.size())));
		if (!isThirdTokenIdentifier)
			new MontyException("Expected identifier after data type declaration keyword:\t"
					+ Tokens.getText(tokens.subList(2, tokens.size())));
		var last = (TokenTypes) null;
		var isLastTokenDataTypeDeclaration = false;
		for (int i = 3; i < tokens.size(); i++) {
			var t = tokens.get(i);
			switch (t.getType()) {
			case INTEGER_KEYWORD:
			case FLOAT_KEYWORD:
			case STRING_KEYWORD:
			case BOOLEAN_KEYWORD:
				isLastTokenDataTypeDeclaration = true;
				break;
			case IDENTIFIER:
				if (!isLastTokenDataTypeDeclaration)
					new MontyException("Expected data type declaration before identifer:\t"
							+ Tokens.getText(tokens.subList(i, tokens.size())));
				isLastTokenDataTypeDeclaration = false;
				break;
			case COMMA:
				if (!(last.equals(TokenTypes.IDENTIFIER) && i + 1 < tokens.size()))
					new MontyException("Unexpected comma:\t" + Tokens.getText(tokens.subList(i, tokens.size())));
				isLastTokenDataTypeDeclaration = false;
				break;
			default:
				new MontyException("Unexpected token:\t" + t.getText());
			}
			last = t.getType();
		}
		return true;

	}

	public static boolean isEndKeyword(List<MontyToken> tokens) {
		if (!tokens.get(0).getType().equals(TokenTypes.END_KEYWORD))
			return false;
		if (tokens.size() > 1)
			new MontyException("Nothing expected after \"end\" keyword:\t" + Tokens.getText(tokens));
		return true;
	}

	public static boolean isIfStatement(List<MontyToken> tokens) {
		if (!tokens.get(0).getType().equals(TokenTypes.IF_KEYWORD))
			return false;
		if (tokens.size() == 1)
			new MontyException("Expected expression after \"if\" keyword:\t" + Tokens.getText(tokens));
		if (!isExpression(tokens.subList(1, tokens.size())))
			new MontyException("Wrong expression after \"if\" keyword:\t" + Tokens.getText(tokens));
		return true;

	}
}
