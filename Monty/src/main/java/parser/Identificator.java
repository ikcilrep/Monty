package parser;

import java.util.List;
import java.util.regex.Pattern;

import lexer.MontyToken;
import lexer.TokenTypes;

public abstract class Identificator {
	public static final Pattern importRegex = Pattern.compile("^IMPORT_KEYWORD [A-Z_]+ (DOT [A-Z_]+ )*$");

	public static boolean isElseStatement(List<MontyToken> tokens) {
		if (!tokens.get(0).getType().equals(TokenTypes.ELSE_KEYWORD))
			return false;
		if (tokens.size() > 1 && !isIfStatement(tokens.subList(1, tokens.size())))
			new MontyException("Expected if statement or nothing after \"else\" keyword:\t" + Tokens.getText(tokens));
		return true;

	}

	public static boolean isEndKeyword(List<MontyToken> tokens) {
		if (!tokens.get(0).getType().equals(TokenTypes.END_KEYWORD))
			return false;
		if (tokens.size() > 1)
			new MontyException("Nothing expected after \"end\" keyword:\t" + Tokens.getText(tokens));
		return true;
	}

	public static boolean isExpression(List<MontyToken> tokens) {
		MontyToken last = null;
		int i = 0;
		for (MontyToken token : tokens) {
			switch (token.getType()) {
			case OPERATOR:
				if (last == null || last.getType().equals(TokenTypes.COMMA))
					return false;
				break;
			case INTEGER_LITERAL:
			case FLOAT_LITERAL:
			case BOOLEAN_LITERAL:
			case STRING_LITERAL:
			case IDENTIFIER:
				break;
			case BRACKET:
				if (last == null)
					new MontyException("Unexpected bracket:\t" + Tokens.getText(tokens.subList(0, i + 1)));
				if (last.getType().equals(TokenTypes.COMMA))
					new MontyException("Unexpected comma before bracket:\t" + Tokens.getText(tokens.subList(0, i + 1)));
				break;
			case COMMA:
				if (last == null)
					new MontyException("Unexpected comma:\t" + Tokens.getText(tokens.subList(0, i + 1)));
				if ((last.getType().equals(TokenTypes.BRACKET) && last.getText().equals("(")))
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

	public static boolean isFunctionDeclaration(List<MontyToken> tokens) {
		var isFirstTokenFuncKeyword = tokens.get(0).getType().equals(TokenTypes.FUNC_KEYWORD);
		TokenTypes secondTokenType = null;
		if (tokens.size() >= 2)
			secondTokenType = tokens.get(1).getType();
		var isSecondTokenDataTypeKeyword = tokens.size() > 1 && (secondTokenType.equals(TokenTypes.INTEGER_KEYWORD)
				|| secondTokenType.equals(TokenTypes.FLOAT_KEYWORD) || secondTokenType.equals(TokenTypes.STRING_KEYWORD)
				|| secondTokenType.equals(TokenTypes.BOOLEAN_KEYWORD) || secondTokenType.equals(TokenTypes.VOID_KEYWORD)
				|| secondTokenType.equals(TokenTypes.ARRAY_KEYWORD) || secondTokenType.equals(TokenTypes.ANY_KEYWORD));
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
			case ARRAY_KEYWORD:
			case ANY_KEYWORD:
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

	public static boolean isIfStatement(List<MontyToken> tokens) {
		if (!tokens.get(0).getType().equals(TokenTypes.IF_KEYWORD))
			return false;
		if (tokens.size() == 1)
			new MontyException("Expected expression after \"if\" keyword:\t" + Tokens.getText(tokens));
		if (!isExpression(tokens.subList(1, tokens.size())))
			new MontyException("Wrong expression after \"if\" keyword:\t" + Tokens.getText(tokens));
		return true;

	}

	public static boolean isImport(List<MontyToken> tokens) {
		return importRegex.matcher(Tokens.getTypesToString(tokens)).matches();
	}

	public static boolean isPrintStatement(List<MontyToken> tokens) {
		if (!tokens.get(0).getType().equals(TokenTypes.PRINT_KEYWORD))
			return false;
		if (tokens.size() == 1)
			new MontyException("Expected expression after \"print\" keyword:\t" + Tokens.getText(tokens));
		var expression = tokens.subList(1, tokens.size());
		if (!isExpression(tokens.subList(1, tokens.size())))
			new MontyException("Wrong expression after \"print\" keyword:\t" + Tokens.getText(expression));
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
		var tokensSize = tokens.size();
		TokenTypes secondTokenType = null;
		TokenTypes thirdTokenType = null;

		if (tokensSize >= 2)
			secondTokenType = tokens.get(1).getType();
		if (tokensSize >= 3)
			thirdTokenType = tokens.get(2).getType();

		var isSecondTokenTypeDynamicOrStaticKeyword = secondTokenType != null
				&& (secondTokenType.equals(TokenTypes.DYNAMIC_KEYWORD)
						|| secondTokenType.equals(TokenTypes.STATIC_KEYWORD));
		var isThirdTokenDataTypeKeyword = thirdTokenType != null && (thirdTokenType.equals(TokenTypes.INTEGER_KEYWORD)
				|| thirdTokenType.equals(TokenTypes.FLOAT_KEYWORD) || thirdTokenType.equals(TokenTypes.STRING_KEYWORD)
				|| thirdTokenType.equals(TokenTypes.BOOLEAN_KEYWORD)
				|| thirdTokenType.equals(TokenTypes.ARRAY_KEYWORD));
		if (!isFirstTokenVarKeyword)
			return false;
		if (!isSecondTokenTypeDynamicOrStaticKeyword)
			new MontyException(
					"Expected  \"dynamic\" or \"static\" keyword after \"var\" keyword:\t" + Tokens.getText(tokens));
		if (!isThirdTokenDataTypeKeyword)
			new MontyException("Expected data type declaration after \"dynamic\" or \"static\" keyword:\t"
					+ Tokens.getText(tokens));
		if (tokensSize == 3)
			new MontyException("Expected expression after data type declaration:\t" + Tokens.getText(tokens));
		if (!tokens.get(3).getType().equals(TokenTypes.IDENTIFIER))
			new MontyException("Expected identifier after data type declaration:\t" + Tokens.getText(tokens));
		var expression = tokens.subList(3, tokens.size());
		if (!isExpression(expression))
			new MontyException("Wrong expression after data type declaration:\t" + Tokens.getText(expression));

		return true;
	}

	public static boolean isWhileStatement(List<MontyToken> tokens) {
		if (!tokens.get(0).getType().equals(TokenTypes.WHILE_KEYWORD))
			return false;
		if (tokens.size() == 1)
			new MontyException("Expected expression after \"while\" keyword:\t" + Tokens.getText(tokens));
		if (!isExpression(tokens.subList(1, tokens.size())))
			new MontyException("Wrong expression after \"while\" keyword:\t" + Tokens.getText(tokens));
		return true;

	}

	public static boolean isChangeToStatement(List<MontyToken> tokens) {
		var tokensSize = tokens.size();
		if (!tokens.get(0).getType().equals(TokenTypes.CHANGE_KEYWORD))
			return false;
		if (!(tokensSize >= 2 && tokens.get(1).getType().equals(TokenTypes.IDENTIFIER)))
			new MontyException("Expected identifier after \"change\" keyword:\t" + Tokens.getText(tokens));
		if (!(tokensSize >= 3 && tokens.get(2).getType().equals(TokenTypes.TO_KEYWORD)))
			new MontyException("Expected \"to\" keyword after identifier:\t" + Tokens.getText(tokens));
		if (tokensSize >= 4) {
			TokenTypes fourthTokenType = tokens.get(3).getType();
			if (!((tokens.get(3).getType().equals(TokenTypes.INTEGER_KEYWORD)
					|| fourthTokenType.equals(TokenTypes.FLOAT_KEYWORD)
					|| fourthTokenType.equals(TokenTypes.BOOLEAN_KEYWORD)
					|| fourthTokenType.equals(TokenTypes.STRING_KEYWORD)
					|| fourthTokenType.equals(TokenTypes.ARRAY_KEYWORD))))
				new MontyException("Expected data type declaration after \"to\" keyword\t" + Tokens.getText(tokens));
		}
		return true;
	}
}
