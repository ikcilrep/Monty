/*
Copyright 2018-2019 Szymon Perlicki

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
import sml.data.array.Array;

public abstract class Identificator {
	public static final Pattern importRegex = Pattern.compile("^[A-Z_]+ (DOT [A-Z_]+ )*$");
	public static final List<TokenTypes> dataTypesKeywords = List.of(TokenTypes.INT_KEYWORD,
			TokenTypes.FLOAT_KEYWORD, TokenTypes.BOOLEAN_KEYWORD, TokenTypes.STRING_KEYWORD, TokenTypes.VOID_KEYWORD,
			TokenTypes.ANY_KEYWORD, TokenTypes.ARRAY_KEYWORD, TokenTypes.LIST_KEYWORD, TokenTypes.STACK_KEYWORD);

	public static boolean isBreakStatement(Array<Token> tokens) {
		if (!tokens.get(0).getType().equals(TokenTypes.BREAK_KEYWORD))
			return false;
		if (tokens.length() > 1)
			new LogError("Nothing expected after \"break\" keyword", tokens.get(1));
		return true;

	}

	public static boolean isChangeToStatement(Array<Token> tokens) {
		var tokensSize = tokens.length();
		if (!tokens.get(0).getType().equals(TokenTypes.CHANGE_KEYWORD))
			return false;
		if (tokensSize == 1 || !tokens.get(1).getType().equals(TokenTypes.IDENTIFIER))
			new LogError("Expected identifier after \"change\" keyword:\t" + Tokens.getText(tokens),
					tokens.get(tokens.length() > 1 ? 1 : 0));
		if (tokensSize == 2 || !tokens.get(2).getType().equals(TokenTypes.TO_KEYWORD))
			new LogError("Expected \"to\" keyword after identifier:\t" + Tokens.getText(tokens),
					tokens.get(tokens.length() > 2 ? 2 : 1));
		if (tokensSize == 3 | !dataTypesKeywords.contains(tokens.get(3).getType()))
			new LogError("Expected data type declaration after \"to\" keyword\t" + Tokens.getText(tokens),
					tokens.get(tokens.length() > 3 ? 3 : 2));
		return true;
	}

	public static boolean isContinueStatement(Array<Token> tokens) {
		if (!tokens.get(0).getType().equals(TokenTypes.CONTINUE_KEYWORD))
			return false;
		if (tokens.length() > 1)
			new LogError("Nothing expected after \"continue\" keyword", tokens.get(1));
		return true;

	}

	public static boolean isDoWhileStatement(Array<Token> tokens) {
		if (!tokens.get(0).getType().equals(TokenTypes.DO_KEYWORD))
			return false;
		if (tokens.length() == 1 || !tokens.get(1).getType().equals(TokenTypes.WHILE_KEYWORD))
			new LogError("Expected \"while\" keyword after \"do\" keyword:\t" + Tokens.getText(tokens),
					tokens.get(tokens.length() > 1 ? 1 : 0));
		if (!isExpression(tokens.subarray(2, tokens.length())))
			new LogError("Expected expression after \"do\" keyword:\t" + Tokens.getText(tokens), tokens.get(2));
		return true;

	}

	public static boolean isElseStatement(Array<Token> tokens) {
		if (!tokens.get(0).getType().equals(TokenTypes.ELSE_KEYWORD))
			return false;
		if (tokens.length() > 1 && !isIfStatement(tokens.subarray(1, tokens.length())))
			new LogError("Expected if statement or nothing after \"else\" keyword:\t" + Tokens.getText(tokens),
					tokens.get(tokens.length() > 1 ? 1 : 0));
		return true;

	}

	public static boolean isEndKeyword(Array<Token> tokens) {
		if (!tokens.get(0).getType().equals(TokenTypes.END_KEYWORD))
			return false;
		if (tokens.length() > 1)
			new LogError("Nothing expected after \"end\" keyword:\t" + Tokens.getText(tokens), tokens.get(1));
		return true;
	}

	public static boolean isExpression(Array<Token> tokensBeforeSemicolon) {
		Token last = null;
		int i = 0;
		for (Token token : tokensBeforeSemicolon) {
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
					new LogError("Unexpected bracket:\t" + Tokens.getText(tokensBeforeSemicolon.subarray(0, i + 1)), token);
				if (last.getType().equals(TokenTypes.COMMA))
					new LogError("Unexpected comma before bracket:\t" + Tokens.getText(tokensBeforeSemicolon.subarray(0, i + 1)),
							token);
				break;
			case COMMA:
				if (last == null)
					new LogError("Unexpected comma:\t" + Tokens.getText(tokensBeforeSemicolon.subarray(0, i + 1)), token);
				if ((last.getType().equals(TokenTypes.BRACKET) && last.getText().equals("(")))
					new LogError("Unexpected bracket before comma:\t" + Tokens.getText(tokensBeforeSemicolon.subarray(0, i + 1)),
							token);
				break;
			case DOT:
				if (last == null || (!last.getType().equals(TokenTypes.IDENTIFIER) && !last.getText().equals(")")))
					new LogError("Unexpected dot:\t" + Tokens.getText(tokensBeforeSemicolon.subarray(0, i + 1)), token);
				break;
			default:
				return false;
			}
			last = token;
			i++;
		}
		return true;

	}

	public static boolean isForStatement(Array<Token> tokens) {
		if (!tokens.get(0).getType().equals(TokenTypes.FOR_KEYWORD))
			return false;

		if (tokens.length() == 1 || !tokens.get(1).getType().equals(TokenTypes.IDENTIFIER))
			new LogError("Expected identifier after \"for\" keyword:\t" + Tokens.getText(tokens),
					tokens.get(tokens.length() > 1 ? 1 : 0));
		if (tokens.length() == 2 || !tokens.get(2).getType().equals(TokenTypes.IN_KEYWORD))
			new LogError("Expected \"in\" keyword after \"for\" keyword:\t" + Tokens.getText(tokens),
					tokens.get(tokens.length() > 2 ? 2 : 1));
		if (tokens.length() == 3 || !isExpression(tokens.subarray(3, tokens.length() - 1)))
			new LogError("Expected expression after \"in\" keyword:\t" + Tokens.getText(tokens),
					tokens.get(tokens.length() > 3 ? 3 : 2));
		return true;
	}

	public static boolean isFunctionDeclaration(Array<Token> tokensBeforeSemicolon) {
		var isFirstTokenFuncKeyword = tokensBeforeSemicolon.get(0).getType().equals(TokenTypes.FUNC_KEYWORD);
		TokenTypes secondTokenType = null;
		if (tokensBeforeSemicolon.length() >= 2)
			secondTokenType = tokensBeforeSemicolon.get(1).getType();
		var isSecondTokenDataTypeKeyword = tokensBeforeSemicolon.length() > 1 && dataTypesKeywords.contains(secondTokenType);
		var isThirdTokenIdentifier = tokensBeforeSemicolon.length() > 2 && tokensBeforeSemicolon.get(2).getType().equals(TokenTypes.IDENTIFIER);
		if (!isFirstTokenFuncKeyword)
			return false;
		if (!isSecondTokenDataTypeKeyword)
			new LogError("Expected data type declaration after \"func\" keyword:\t"
					+ Tokens.getText(tokensBeforeSemicolon.subarray(1, tokensBeforeSemicolon.length())), tokensBeforeSemicolon.get(1));
		if (!isThirdTokenIdentifier)
			new LogError("Expected identifier after data type declaration keyword:\t"
					+ Tokens.getText(tokensBeforeSemicolon.subarray(2, tokensBeforeSemicolon.length())), tokensBeforeSemicolon.get(2));
		var last = (TokenTypes) null;
		var isLastTokenDataTypeDeclaration = false;
		for (int i = 3; i < tokensBeforeSemicolon.length(); i++) {
			var t = tokensBeforeSemicolon.get(i);
			if (dataTypesKeywords.contains(t.getType()))
				isLastTokenDataTypeDeclaration = true;
			else if (t.getType().equals(TokenTypes.IDENTIFIER)) {
				if (!isLastTokenDataTypeDeclaration)
					new LogError("Expected data type declaration before identifer:\t"
							+ Tokens.getText(tokensBeforeSemicolon.subarray(i, tokensBeforeSemicolon.length())), t);
				isLastTokenDataTypeDeclaration = false;
			} else if (t.getType().equals(TokenTypes.COMMA)) {
				if (!(last.equals(TokenTypes.IDENTIFIER) && i + 1 < tokensBeforeSemicolon.length()))
					new LogError("Unexpected comma:\t" + Tokens.getText(tokensBeforeSemicolon.subarray(i, tokensBeforeSemicolon.length())), t);
				isLastTokenDataTypeDeclaration = false;
			} else
				new LogError("Unexpected token:\t" + t.getText(), t);

			last = t.getType();
		}
		return true;

	}

	public static boolean isIfStatement(Array<Token> tokens) {
		if (!tokens.get(0).getType().equals(TokenTypes.IF_KEYWORD))
			return false;
		if (tokens.length() == 1 || !isExpression(tokens.subarray(1, tokens.length())))
			new LogError("Expected expression after \"if\" keyword:\t" + Tokens.getText(tokens),
					tokens.get(tokens.length() > 1 ? 1 : 0));
		return true;

	}

	public static boolean isImport(Array<Token> tokens) {
		return tokens.get(0).getType().equals(TokenTypes.IMPORT_KEYWORD)
				&& importRegex.matcher(Tokens.getTypesToString(tokens.subarray(1, tokens.length()))).matches();
	}

	public static boolean isJar(Array<Token> tokens) {
		return tokens.get(0).getType().equals(TokenTypes.JAR_KEYWORD)
				&& importRegex.matcher(Tokens.getTypesToString(tokens.subarray(1, tokens.length()))).matches();
	}

	public static boolean isReturnStatement(Array<Token> tokensBeforeSemicolon) {
		var isFirstTokenReturnKeyword = tokensBeforeSemicolon.get(0).getType().equals(TokenTypes.RETURN_KEYWORD);

		if (!isFirstTokenReturnKeyword)
			return false;
		if (tokensBeforeSemicolon.length() > 1 && !isExpression(tokensBeforeSemicolon.subarray(1, tokensBeforeSemicolon.length())))
			new LogError("Expected expression or nothing after \"return\" keyword:\t" + Tokens.getText(tokensBeforeSemicolon),
					tokensBeforeSemicolon.get(tokensBeforeSemicolon.length() > 1 ? 1 : 0));
		return true;
	}

	public static boolean isThreadStatement(Array<Token> tokens) {
		if (!tokens.get(0).getType().equals(TokenTypes.THREAD_KEYWORD))
			return false;
		if (tokens.length() == 1 || !isExpression(tokens.subarray(1, tokens.length())))
			new LogError("Expected expression after \"thread\" keyword:\t" + Tokens.getText(tokens),
					tokens.get(tokens.length() > 1 ? 1 : 0));

		return true;
	}

	public static boolean isVariableDeclaration(Array<Token> tokensBeforeSemicolon) {
		var firstTokenType = tokensBeforeSemicolon.get(0).getType();
		var isFirstTokenStaticOrDynamicKeyword = firstTokenType.equals(TokenTypes.STATIC_KEYWORD)
				|| firstTokenType.equals(TokenTypes.DYNAMIC_KEYWORD);
		var tokensSize = tokensBeforeSemicolon.length();
		TokenTypes secondTokenType = null;

		if (tokensSize >= 2)
			secondTokenType = tokensBeforeSemicolon.get(1).getType();

		var isSecondTokenTypeDataTypeKeyword = secondTokenType != null && dataTypesKeywords.contains(secondTokenType);
		if (!isFirstTokenStaticOrDynamicKeyword)
			return false;
		if (!isSecondTokenTypeDataTypeKeyword)
			new LogError("Expected data type declaration after  \"dynamic\" or \"static\" keyword:\t"
					+ Tokens.getText(tokensBeforeSemicolon), tokensBeforeSemicolon.get(1));
		if (tokensSize == 2)
			new LogError("Expected expression after data type declaration:\t" + Tokens.getText(tokensBeforeSemicolon), tokensBeforeSemicolon.get(1));
		if (!tokensBeforeSemicolon.get(2).getType().equals(TokenTypes.IDENTIFIER))
			new LogError("Expected identifier after data type declaration:\t" + Tokens.getText(tokensBeforeSemicolon), tokensBeforeSemicolon.get(2));
		if (tokensSize > 3) {
			var expression = tokensBeforeSemicolon.subarray(2, tokensBeforeSemicolon.length());
			if (!isExpression(expression))
				new LogError("Wrong expression after data type declaration:\t" + Tokens.getText(expression),
						tokensBeforeSemicolon.get(2));
		}
		return true;
	}
	
	public static boolean isStructDeclaration(Array<Token> tokens) {
		if (!tokens.get(0).getType().equals(TokenTypes.STRUCT_KEYWORD))
			return false;
		if (tokens.length() == 1 || !tokens.get(1).getType().equals(TokenTypes.IDENTIFIER))
			new LogError("Expected name after \"struct\" keyword:\t" + Tokens.getText(tokens),
					tokens.get(tokens.length() > 1 ? 1 : 0));

		return true;
	}
	public static boolean isWhileStatement(Array<Token> tokens) {
		if (!tokens.get(0).getType().equals(TokenTypes.WHILE_KEYWORD))
			return false;
		if (tokens.length() == 1 || !isExpression(tokens.subarray(1, tokens.length())))
			new LogError("Expected expression after \"while\" keyword:\t" + Tokens.getText(tokens),
					tokens.get(tokens.length() > 1 ? 1 : 0));
		return true;

	}

}
