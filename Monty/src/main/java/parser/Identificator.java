/*
Copyright 2018 Szymon Perlicki

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package parser;

import java.util.List;
import java.util.regex.Pattern;

import lexer.Token;
import lexer.TokenTypes;

public abstract class Identificator {
	public static final Pattern importRegex = Pattern.compile("^IMPORT_KEYWORD [A-Z_]+ (DOT [A-Z_]+ )*$");
	public static final List<TokenTypes> dataTypesKeywords = List.of(TokenTypes.INTEGER_KEYWORD,
			TokenTypes.FLOAT_KEYWORD, TokenTypes.BOOLEAN_KEYWORD, TokenTypes.STRING_KEYWORD, TokenTypes.VOID_KEYWORD,
			TokenTypes.ANY_KEYWORD, TokenTypes.ARRAY_KEYWORD, TokenTypes.LIST_KEYWORD, TokenTypes.STACK_KEYWORD);

	public static boolean isElseStatement(List<Token> tokens) {
		if (!tokens.get(0).getType().equals(TokenTypes.ELSE_KEYWORD))
			return false;
		if (tokens.size() > 1 && !isIfStatement(tokens.subList(1, tokens.size())))
			new LogError("Expected if statement or nothing after \"else\" keyword:\t" + Tokens.getText(tokens),
					tokens.get(tokens.size() > 1 ? 1 : 0));
		return true;

	}

	public static boolean isEndKeyword(List<Token> tokens) {
		if (!tokens.get(0).getType().equals(TokenTypes.END_KEYWORD))
			return false;
		if (tokens.size() > 1)
			new LogError("Nothing expected after \"end\" keyword:\t" + Tokens.getText(tokens), tokens.get(1));
		return true;
	}

	public static boolean isExpression(List<Token> tokens) {
		Token last = null;
		int i = 0;
		for (Token token : tokens) {
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
					new LogError("Unexpected bracket:\t" + Tokens.getText(tokens.subList(0, i + 1)), token);
				if (last.getType().equals(TokenTypes.COMMA))
					new LogError("Unexpected comma before bracket:\t" + Tokens.getText(tokens.subList(0, i + 1)),
							token);
				break;
			case COMMA:
				if (last == null)
					new LogError("Unexpected comma:\t" + Tokens.getText(tokens.subList(0, i + 1)), token);
				if ((last.getType().equals(TokenTypes.BRACKET) && last.getText().equals("(")))
					new LogError("Unexpected bracket before comma:\t" + Tokens.getText(tokens.subList(0, i + 1)),
							token);
				break;
			case DOT:
				if (last == null || (last != null && !last.getType().equals(TokenTypes.IDENTIFIER)))
					new LogError("Unexpected dot:\t" + Tokens.getText(tokens.subList(0, i + 1)), token);
				break;
			default:
				return false;
			}
			last = token;
			i++;
		}
		return true;

	}

	public static boolean isFunctionDeclaration(List<Token> tokens) {
		var isFirstTokenFuncKeyword = tokens.get(0).getType().equals(TokenTypes.FUNC_KEYWORD);
		TokenTypes secondTokenType = null;
		if (tokens.size() >= 2)
			secondTokenType = tokens.get(1).getType();
		var isSecondTokenDataTypeKeyword = tokens.size() > 1 && dataTypesKeywords.contains(secondTokenType);
		var isThirdTokenIdentifier = tokens.size() > 2 && tokens.get(2).getType().equals(TokenTypes.IDENTIFIER);
		if (!isFirstTokenFuncKeyword)
			return false;
		if (!isSecondTokenDataTypeKeyword)
			new LogError("Expected data type declaration after \"func\" keyword:\t"
					+ Tokens.getText(tokens.subList(1, tokens.size())), tokens.get(1));
		if (!isThirdTokenIdentifier)
			new LogError("Expected identifier after data type declaration keyword:\t"
					+ Tokens.getText(tokens.subList(2, tokens.size())), tokens.get(2));
		var last = (TokenTypes) null;
		var isLastTokenDataTypeDeclaration = false;
		for (int i = 3; i < tokens.size(); i++) {
			var t = tokens.get(i);
			if (dataTypesKeywords.contains(t.getType()))
				isLastTokenDataTypeDeclaration = true;
			else if (t.getType().equals(TokenTypes.IDENTIFIER)) {
				if (!isLastTokenDataTypeDeclaration)
					new LogError("Expected data type declaration before identifer:\t"
							+ Tokens.getText(tokens.subList(i, tokens.size())), t);
				isLastTokenDataTypeDeclaration = false;
			} else if (t.getType().equals(TokenTypes.COMMA)) {
				if (!(last.equals(TokenTypes.IDENTIFIER) && i + 1 < tokens.size()))
					new LogError("Unexpected comma:\t" + Tokens.getText(tokens.subList(i, tokens.size())), t);
				isLastTokenDataTypeDeclaration = false;
			} else
				new LogError("Unexpected token:\t" + t.getText(), t);

			last = t.getType();
		}
		return true;

	}

	public static boolean isIfStatement(List<Token> tokens) {
		if (!tokens.get(0).getType().equals(TokenTypes.IF_KEYWORD))
			return false;
		if (tokens.size() == 1 || !isExpression(tokens.subList(1, tokens.size())))
			new LogError("Expected expression after \"if\" keyword:\t" + Tokens.getText(tokens),
					tokens.get(tokens.size() > 1 ? 1 : 0));
		return true;

	}

	public static boolean isImport(List<Token> tokens) {
		return importRegex.matcher(Tokens.getTypesToString(tokens)).matches();
	}

	public static boolean isReturnStatement(List<Token> tokens) {
		var isFirstTokenReturnKeyword = tokens.get(0).getType().equals(TokenTypes.RETURN_KEYWORD);

		if (!isFirstTokenReturnKeyword)
			return false;
		if (tokens.size() == 1 || !isExpression(tokens.subList(1, tokens.size())))
			new LogError("Expected expression after \"return\" keyword:\t" + Tokens.getText(tokens),
					tokens.get(tokens.size() > 1 ? 1 : 0));
		return true;
	}

	public static boolean isVariableDeclaration(List<Token> tokens) {
		var firstTokenType = tokens.get(0).getType();
		var isFirstTokenStaticOrDynamicKeyword = firstTokenType.equals(TokenTypes.STATIC_KEYWORD)
				|| firstTokenType.equals(TokenTypes.DYNAMIC_KEYWORD);
		var tokensSize = tokens.size();
		TokenTypes secondTokenType = null;

		if (tokensSize >= 2)
			secondTokenType = tokens.get(1).getType();

		var isSecondTokenTypeDataTypeKeyword = secondTokenType != null && dataTypesKeywords.contains(secondTokenType);
		if (!isFirstTokenStaticOrDynamicKeyword)
			return false;
		if (!isSecondTokenTypeDataTypeKeyword)
			new LogError("Expected data type declaration after  \"dynamic\" or \"static\" keyword:\t"
					+ Tokens.getText(tokens), tokens.get(1));
		if (tokensSize == 2)
			new LogError("Expected expression after data type declaration:\t" + Tokens.getText(tokens), tokens.get(1));
		if (!tokens.get(2).getType().equals(TokenTypes.IDENTIFIER))
			new LogError("Expected identifier after data type declaration:\t" + Tokens.getText(tokens), tokens.get(2));
		var expression = tokens.subList(2, tokens.size());
		if (!isExpression(expression))
			new LogError("Wrong expression after data type declaration:\t" + Tokens.getText(expression), tokens.get(2));

		return true;
	}

	public static boolean isWhileStatement(List<Token> tokens) {
		if (!tokens.get(0).getType().equals(TokenTypes.WHILE_KEYWORD))
			return false;
		if (tokens.size() == 1 || !isExpression(tokens.subList(1, tokens.size())))
			new LogError("Expected expression after \"while\" keyword:\t" + Tokens.getText(tokens),
					tokens.get(tokens.size() > 1 ? 1 : 0));
		return true;

	}

	public static boolean isDoWhileStatement(List<Token> tokens) {
		if (!tokens.get(0).getType().equals(TokenTypes.DO_KEYWORD))
			return false;
		if (tokens.size() == 1 || !tokens.get(1).getType().equals(TokenTypes.WHILE_KEYWORD))
			new LogError("Expected \"while\" keyword after \"do\" keyword:\t" + Tokens.getText(tokens),
					tokens.get(tokens.size() > 1 ? 1 : 0));
		if (!isExpression(tokens.subList(2, tokens.size())))
			new LogError("Expected expression after \"do\" keyword:\t" + Tokens.getText(tokens), tokens.get(2));
		return true;

	}

	public static boolean isForStatement(List<Token> tokens) {
		if (!tokens.get(0).getType().equals(TokenTypes.FOR_KEYWORD))
			return false;

		if (tokens.size() == 1 || !tokens.get(1).getType().equals(TokenTypes.IDENTIFIER))
			new LogError("Expected identifier after \"for\" keyword:\t" + Tokens.getText(tokens),
					tokens.get(tokens.size() > 1 ? 1 : 0));
		if (tokens.size() == 2 || !tokens.get(2).getType().equals(TokenTypes.IN_KEYWORD))
			new LogError("Expected \"in\" keyword after \"for\" keyword:\t" + Tokens.getText(tokens),
					tokens.get(tokens.size() > 2 ? 2 : 1));
		if (tokens.size() == 3 || !isExpression(tokens.subList(3, tokens.size() - 1)))
			new LogError("Expected expression after \"in\" keyword:\t" + Tokens.getText(tokens),
					tokens.get(tokens.size() > 3 ? 3 : 2));
		return true;
	}

	public static boolean isChangeToStatement(List<Token> tokens) {
		var tokensSize = tokens.size();
		if (!tokens.get(0).getType().equals(TokenTypes.CHANGE_KEYWORD))
			return false;
		if (tokensSize == 1 || !tokens.get(1).getType().equals(TokenTypes.IDENTIFIER))
			new LogError("Expected identifier after \"change\" keyword:\t" + Tokens.getText(tokens),
					tokens.get(tokens.size() > 1 ? 1 : 0));
		if (tokensSize == 2 || !tokens.get(2).getType().equals(TokenTypes.TO_KEYWORD))
			new LogError("Expected \"to\" keyword after identifier:\t" + Tokens.getText(tokens),
					tokens.get(tokens.size() > 2 ? 2 : 1));
		if (tokensSize == 3 | !dataTypesKeywords.contains(tokens.get(3).getType()))
			new LogError("Expected data type declaration after \"to\" keyword\t" + Tokens.getText(tokens),
					tokens.get(tokens.size() > 3 ? 3 : 2));
		return true;
	}

	public static boolean isThreadStatement(List<Token> tokens) {
		if (!tokens.get(0).getType().equals(TokenTypes.THREAD_KEYWORD))
			return false;
		if (tokens.size() == 1 || !isExpression(tokens.subList(1, tokens.size())))
			new LogError("Expected expression after \"thread\" keyword:\t" + Tokens.getText(tokens),
					tokens.get(tokens.size() > 1 ? 1 : 0));

		return true;
	}

	public static boolean isBreakStatement(List<Token> tokens) {
		if (!tokens.get(0).getType().equals(TokenTypes.BREAK_KEYWORD))
			return false;
		if (tokens.size() > 1)
			new LogError("Nothing expected after \"break\" keyword", tokens.get(1));
		return true;

	}

	public static boolean isContinueStatement(List<Token> tokens) {
		if (!tokens.get(0).getType().equals(TokenTypes.CONTINUE_KEYWORD))
			return false;
		if (tokens.size() > 1)
			new LogError("Nothing expected after \"continue\" keyword", tokens.get(1));
		return true;

	}

}
