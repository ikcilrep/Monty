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

import java.util.Set;
import java.util.regex.Pattern;

import lexer.OptimizedTokensArray;
import lexer.Token;
import lexer.TokenTypes;

public abstract class Identificator {
	public static final Pattern IMPORT_REGEX = Pattern.compile("^[A-Z_]+ (DOT [A-Z_]+ )*$");
	public static final Set<TokenTypes> DATA_TYPES_KEYWORDS = Set.of(TokenTypes.INT_KEYWORD, TokenTypes.REAL_KEYWORD,
			TokenTypes.BOOLEAN_KEYWORD, TokenTypes.STRING_KEYWORD, TokenTypes.VOID_KEYWORD, TokenTypes.ANY_KEYWORD);

	public static boolean isBreakStatement(OptimizedTokensArray tokens) {
		if (!tokens.get(0).getType().equals(TokenTypes.BREAK_KEYWORD))
			return false;
		if (tokens.length() > 1)
			new LogError("Nothing expected after \"break\" keyword", tokens.get(1));
		return true;

	}

	public static boolean isChangeToStatement(OptimizedTokensArray tokens) {
		var tokensSize = tokens.length();
		if (!tokens.get(0).getType().equals(TokenTypes.CHANGE_KEYWORD))
			return false;
		if (tokensSize == 1 || !tokens.get(1).getType().equals(TokenTypes.IDENTIFIER))
			new LogError("Expected identifier after \"change\" keyword.", tokens.get(tokens.length() > 1 ? 1 : 0));
		if (tokensSize == 2 || !tokens.get(2).getType().equals(TokenTypes.TO_KEYWORD))
			new LogError("Expected \"to\" keyword after identifier.", tokens.get(tokens.length() > 2 ? 2 : 1));
		if (tokensSize == 3 | !DATA_TYPES_KEYWORDS.contains(tokens.get(3).getType()))
			new LogError("Expected data type declaration after \"to\" keyword.",
					tokens.get(tokens.length() > 3 ? 3 : 2));
		return true;
	}

	public static boolean isContinueStatement(OptimizedTokensArray tokens) {
		if (!tokens.get(0).getType().equals(TokenTypes.CONTINUE_KEYWORD))
			return false;
		if (tokens.length() > 1)
			new LogError("Nothing expected after \"continue\" keyword", tokens.get(1));
		return true;

	}

	public static boolean isDoWhileStatement(OptimizedTokensArray tokens) {
		if (!tokens.get(0).getType().equals(TokenTypes.DO_KEYWORD))
			return false;
		if (tokens.length() == 1 || !tokens.get(1).getType().equals(TokenTypes.WHILE_KEYWORD))
			new LogError("Expected \"while\" keyword after \"do\" keyword.", tokens.get(tokens.length() > 1 ? 1 : 0));
		if (!isExpression(tokens.subarray(2, tokens.length())))
			new LogError("Expected expression after \"do\" keyword.", tokens.get(2));
		return true;

	}

	public static boolean isElseStatement(OptimizedTokensArray tokens) {
		if (!tokens.get(0).getType().equals(TokenTypes.ELSE_KEYWORD))
			return false;
		if (tokens.length() > 1 && !isIfStatement(tokens.subarray(1, tokens.length())))
			new LogError("Expected if statement or nothing after \"else\" keyword.",
					tokens.get(tokens.length() > 1 ? 1 : 0));
		return true;

	}

	public static boolean isEndKeyword(OptimizedTokensArray tokens) {
		if (!tokens.get(0).getType().equals(TokenTypes.END_KEYWORD))
			return false;
		if (tokens.length() > 1)
			new LogError("Nothing expected after \"end\" keyword.", tokens.get(1));
		return true;
	}

	public static boolean isExpression(OptimizedTokensArray tokensBeforeSemicolon) {
		Token last = null;
		for (Token token : tokensBeforeSemicolon) {
			switch (token.getType()) {
			case OPERATOR:
				if (last == null || last.getType().equals(TokenTypes.COMMA))
					return false;
				break;
			case INTEGER_LITERAL:
			case REAL_LITERAL:
			case BOOLEAN_LITERAL:
			case STRING_LITERAL:
			case IDENTIFIER:
				break;
			case OPENING_BRACKET:
			case CLOSING_BRACKET:
				if (last == null)
					new LogError("Unexpected bracket.", token);
				if (last.getType().equals(TokenTypes.COMMA))
					new LogError("Unexpected comma before bracket.", token);
				break;
			case COMMA:
				if (last == null)
					new LogError("Unexpected comma.", token);
				if (last.getType().equals(TokenTypes.OPENING_BRACKET))
					new LogError("Unexpected bracket before comma.", token);
				break;
			case DOT:
				if (last == null || (!last.getType().equals(TokenTypes.IDENTIFIER)
						&& !last.getType().equals(TokenTypes.CLOSING_BRACKET)))
					new LogError("Unexpected dot.", token);
				break;
			default:
				return false;
			}
			last = token;
		}
		return true;

	}

	public static boolean isForStatement(OptimizedTokensArray tokens) {
		if (!tokens.get(0).getType().equals(TokenTypes.FOR_KEYWORD))
			return false;

		if (tokens.length() == 1 || !tokens.get(1).getType().equals(TokenTypes.IDENTIFIER))
			new LogError("Expected identifier after \"for\" keyword.", tokens.get(tokens.length() > 1 ? 1 : 0));
		if (tokens.length() == 2 || !tokens.get(2).getType().equals(TokenTypes.IN_KEYWORD))
			new LogError("Expected \"in\" keyword after \"for\" keyword.", tokens.get(tokens.length() > 2 ? 2 : 1));
		if (tokens.length() == 3 || !isExpression(tokens.subarray(3, tokens.length() - 1)))
			new LogError("Expected expression after \"in\" keyword.", tokens.get(tokens.length() > 3 ? 3 : 2));
		return true;
	}

	public static boolean isFunctionDeclaration(OptimizedTokensArray tokensBeforeSemicolon) {
		var isFirstTokenFuncKeyword = tokensBeforeSemicolon.get(0).getType().equals(TokenTypes.FUNC_KEYWORD);
		TokenTypes secondTokenType = null;
		if (tokensBeforeSemicolon.length() >= 2)
			secondTokenType = tokensBeforeSemicolon.get(1).getType();
		var isSecondTokenDataTypeKeyword = tokensBeforeSemicolon.length() > 1
				&& DATA_TYPES_KEYWORDS.contains(secondTokenType);
		var isThirdTokenIdentifier = tokensBeforeSemicolon.length() > 2
				&& tokensBeforeSemicolon.get(2).getType().equals(TokenTypes.IDENTIFIER);
		if (!isFirstTokenFuncKeyword)
			return false;
		if (!isSecondTokenDataTypeKeyword)
			new LogError("Expected data type declaration after \"func\" keyword.", tokensBeforeSemicolon.get(1));
		if (!isThirdTokenIdentifier)
			new LogError("Expected identifier after data type declaration keyword.", tokensBeforeSemicolon.get(2));
		TokenTypes last = null;
		var isLastTokenDataTypeDeclaration = false;
		for (int i = 3; i < tokensBeforeSemicolon.length(); i++) {
			var t = tokensBeforeSemicolon.get(i);
			if (DATA_TYPES_KEYWORDS.contains(t.getType()))
				isLastTokenDataTypeDeclaration = true;
			else if (t.getType().equals(TokenTypes.IDENTIFIER)) {
				if (!isLastTokenDataTypeDeclaration)
					new LogError("Expected data type declaration before identifer.", t);
				isLastTokenDataTypeDeclaration = false;
			} else if (t.getType().equals(TokenTypes.COMMA)) {
				if (!(last.equals(TokenTypes.IDENTIFIER) && i + 1 < tokensBeforeSemicolon.length()))
					new LogError("Unexpected comma.", t);
				isLastTokenDataTypeDeclaration = false;
			} else
				new LogError("Unexpected token.", t);

			last = t.getType();
		}
		return true;

	}

	public static boolean isIfStatement(OptimizedTokensArray tokens) {
		if (!tokens.get(0).getType().equals(TokenTypes.IF_KEYWORD))
			return false;
		if (tokens.length() == 1 || !isExpression(tokens.subarray(1, tokens.length())))
			new LogError("Expected expression after \"if\" keyword.", tokens.get(tokens.length() > 1 ? 1 : 0));
		return true;

	}

	public static boolean isImport(OptimizedTokensArray tokens) {
		return tokens.get(0).getType().equals(TokenTypes.IMPORT_KEYWORD)
				&& IMPORT_REGEX.matcher(Tokens.getTypesToString(tokens.subarray(1, tokens.length()))).matches();
	}

	public static boolean isJar(OptimizedTokensArray tokens) {
		return tokens.get(0).getType().equals(TokenTypes.JAR_KEYWORD)
				&& IMPORT_REGEX.matcher(Tokens.getTypesToString(tokens.subarray(1, tokens.length()))).matches();
	}

	public static boolean isReturnStatement(OptimizedTokensArray tokensBeforeSemicolon) {
		var isFirstTokenReturnKeyword = tokensBeforeSemicolon.get(0).getType().equals(TokenTypes.RETURN_KEYWORD);

		if (!isFirstTokenReturnKeyword)
			return false;
		if (tokensBeforeSemicolon.length() > 1
				&& !isExpression(tokensBeforeSemicolon.subarray(1, tokensBeforeSemicolon.length())))
			new LogError("Expected expression or nothing after \"return\" keyword.",
					tokensBeforeSemicolon.get(tokensBeforeSemicolon.length() > 1 ? 1 : 0));
		return true;
	}

	public static boolean isStructDeclaration(OptimizedTokensArray tokens) {
		if (!tokens.get(0).getType().equals(TokenTypes.STRUCT_KEYWORD))
			return false;
		if (tokens.length() == 1 || !tokens.get(1).getType().equals(TokenTypes.IDENTIFIER))
			new LogError("Expected name after \"struct\" keyword.", tokens.get(tokens.length() > 1 ? 1 : 0));

		return true;
	}

	public static boolean isVariableDeclaration(OptimizedTokensArray tokensBeforeSemicolon) {
		var firstTokenType = tokensBeforeSemicolon.get(0).getType();
		var isFirstTokenDataTypeKeyword = DATA_TYPES_KEYWORDS.contains(firstTokenType);
		var isFirstTokenDynamicKeyword = firstTokenType.equals(TokenTypes.DYNAMIC_KEYWORD);
		var tokensSize = tokensBeforeSemicolon.length();
		TokenTypes secondTokenType = null;

		if (tokensSize >= 2)
			secondTokenType = tokensBeforeSemicolon.get(1).getType();

		if (!(isFirstTokenDataTypeKeyword || isFirstTokenDynamicKeyword))
			return false;
		int n = 1;
		if (isFirstTokenDynamicKeyword) {
			if (!(secondTokenType != null && DATA_TYPES_KEYWORDS.contains(secondTokenType)) || tokensSize == 2)
				new LogError("Expected data type declaration after \"dynamic\" keyword.");
			n++;

		}
		if (!tokensBeforeSemicolon.get(n).getType().equals(TokenTypes.IDENTIFIER))
			new LogError("Expected identifier after data type declaration.", tokensBeforeSemicolon.get(n));

		if (tokensSize > n + 1) {
			var expression = tokensBeforeSemicolon.subarray(n, tokensBeforeSemicolon.length());
			if (!isExpression(expression))
				new LogError("Wrong expression after data type declaration.", tokensBeforeSemicolon.get(n));

		}
		return true;
	}

	public static boolean isWhileStatement(OptimizedTokensArray tokens) {
		if (!tokens.get(0).getType().equals(TokenTypes.WHILE_KEYWORD))
			return false;
		if (tokens.length() == 1 || !isExpression(tokens.subarray(1, tokens.length())))
			new LogError("Expected expression after \"while\" keyword.", tokens.get(tokens.length() > 1 ? 1 : 0));
		return true;

	}

}
