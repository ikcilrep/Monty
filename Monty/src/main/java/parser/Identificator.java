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

import lexer.OptimizedTokensArray;
import lexer.Token;
import lexer.TokenTypes;

import java.util.regex.Pattern;

public abstract class Identificator {
    private static final Pattern IMPORT_REGEX = Pattern.compile("^[A-Z_]+ (DOT [A-Z_]+ )*$");

    public static boolean isBreakStatement(OptimizedTokensArray tokens) {
        if (!tokens.get(0).getType().equals(TokenTypes.BREAK_KEYWORD))
            return false;
        if (tokens.length() > 1)
            new LogError("Nothing expected after \"break\" keyword", tokens.get(1));
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
        if (!isExpression(tokens, 2, tokens.length()))
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

    public static boolean isExpression(OptimizedTokensArray tokensBeforeSemicolon, int start, int end) {
        Token last = null;
        for (; start < tokensBeforeSemicolon.length(); start++) {
            var token = tokensBeforeSemicolon.get(start);
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
                case OPENING_BRACKET:
                case CLOSING_BRACKET:
                case OPENING_SQUARE_BRACKET:
                case CLOSING_SQUARE_BRACKET:
                    break;
                case COMMA:
                    if (last != null){
                        if (last.getType().equals(TokenTypes.OPENING_BRACKET))
                            new LogError("Unexpected bracket before comma.", token);
                    } else
                        new LogError("Unexpected comma.", token);

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
        if (tokens.length() == 3 || !isExpression(tokens, 3, tokens.length() - 1))
            new LogError("Expected expression after \"in\" keyword.", tokens.get(tokens.length() > 3 ? 3 : 2));
        return true;
    }

    public static boolean isFunctionDeclaration(OptimizedTokensArray tokensBeforeSemicolon) {
        var isFirstTokenFuncKeyword = tokensBeforeSemicolon.get(0).getType().equals(TokenTypes.FUNC_KEYWORD);
        var isSecondTokenIdentifier = tokensBeforeSemicolon.length() > 1
                && tokensBeforeSemicolon.get(1).getType().equals(TokenTypes.IDENTIFIER);
        if (!isFirstTokenFuncKeyword)
            return false;
        if (!isSecondTokenIdentifier)
            new LogError("Expected identifier after \"func\" keyword.", tokensBeforeSemicolon.get(1));
        TokenTypes last = null;
        for (int i = 2; i < tokensBeforeSemicolon.length(); i++) {
            var t = tokensBeforeSemicolon.get(i);
            if (t.getType().equals(TokenTypes.COMMA)) {
                if (!((last == null ||  last.equals(TokenTypes.IDENTIFIER)) && i + 1 < tokensBeforeSemicolon.length()))
                    new LogError("Unexpected comma.", t);
            } else if (!t.getType().equals(TokenTypes.IDENTIFIER))
                new LogError("Unexpected token:\t\"" + t.getText() + "\"", t);

            last = t.getType();
        }
        return true;

    }

    public static boolean isIfStatement(OptimizedTokensArray tokens) {
        if (!tokens.get(0).getType().equals(TokenTypes.IF_KEYWORD))
            return false;
        if (tokens.length() == 1 || !isExpression(tokens, 1, tokens.length()))
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
                && !isExpression(tokensBeforeSemicolon, 1, tokensBeforeSemicolon.length()))
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
        if (!tokensBeforeSemicolon.get(0).getType().equals(TokenTypes.VAR_KEYWORD))
            return false;
        if (tokensBeforeSemicolon.length() == 1
                || !tokensBeforeSemicolon.get(1).getType().equals(TokenTypes.IDENTIFIER))
            new LogError("Expected identifier after \"var\" keyword.", tokensBeforeSemicolon.get(1));
        if (tokensBeforeSemicolon.length() > 2
                && !isExpression(tokensBeforeSemicolon, 1, tokensBeforeSemicolon.length()))
            new LogError("Expected expression after \"var\" keyword.", tokensBeforeSemicolon.get(1));

        return true;
    }

    public static boolean isWhileStatement(OptimizedTokensArray tokens) {
        if (!tokens.get(0).getType().equals(TokenTypes.WHILE_KEYWORD))
            return false;
        if (tokens.length() == 1 || !isExpression(tokens, 1, tokens.length()))
            new LogError("Expected expression after \"while\" keyword.", tokens.get(tokens.length() > 1 ? 1 : 0));
        return true;

    }

}
