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

import lexer.Token;
import lexer.TokenTypes;

import java.util.ArrayList;
import java.util.regex.Pattern;

public abstract class Identificator {
    private static final Pattern IMPORT_REGEX = Pattern.compile("^[A-Z_]+ (DOT [A-Z_]+ )*$");

    public static boolean isBreakStatement(ArrayList<Token> tokens) {
        if (!tokens.get(0).getType().equals(TokenTypes.BREAK_KEYWORD))
            return false;
        if (tokens.size() > 1)
            new LogError("Nothing expected after \"break\" keyword", tokens.get(1));
        return true;

    }

    public static boolean isContinueStatement(ArrayList<Token> tokens) {
        if (!tokens.get(0).getType().equals(TokenTypes.CONTINUE_KEYWORD))
            return false;
        if (tokens.size() > 1)
            new LogError("Nothing expected after \"continue\" keyword", tokens.get(1));
        return true;

    }

    public static boolean isDoWhileStatement(ArrayList<Token> tokens) {
        if (!tokens.get(0).getType().equals(TokenTypes.DO_KEYWORD))
            return false;
        if (tokens.size() == 1 || !tokens.get(1).getType().equals(TokenTypes.WHILE_KEYWORD))
            new LogError("Expected \"while\" keyword after \"do\" keyword.", tokens.get(tokens.size() > 1 ? 1 : 0));
        if (!isExpression(tokens, 2, tokens.size()))
            new LogError("Expected expression after \"do\" keyword.", tokens.get(2));
        return true;

    }

    public static boolean isElseStatement(ArrayList<Token> tokens) {
        if (!tokens.get(0).getType().equals(TokenTypes.ELSE_KEYWORD))
            return false;
        if (tokens.size() > 1 && !isIfStatement((ArrayList<Token>) tokens.subList(1, tokens.size())))
            new LogError("Expected if statement or nothing after \"else\" keyword.",
                    tokens.get(tokens.size() > 1 ? 1 : 0));
        return true;

    }

    public static boolean isEndKeyword(ArrayList<Token> tokens) {
        if (!tokens.get(0).getType().equals(TokenTypes.END_KEYWORD))
            return false;
        if (tokens.size() > 1)
            new LogError("Nothing expected after \"end\" keyword.", tokens.get(1));
        return true;
    }

    public static boolean isExpression(ArrayList<Token> tokens, int start, int end) {
        for (; start < tokens.size(); start++) {
            var token = tokens.get(start);
            switch (token.getType()) {
                case OPERATOR:
                case INTEGER_LITERAL:
                case REAL_LITERAL:
                case BOOLEAN_LITERAL:
                case STRING_LITERAL:
                case IDENTIFIER:
                case FUNCTION:
                case OPENING_BRACKET:
                case CLOSING_BRACKET:
                case OPENING_SQUARE_BRACKET:
                case CLOSING_SQUARE_BRACKET:
                case DOT:
                    break;
                default:
                    return false;
            }
        }

        return true;

    }

    public static boolean isForStatement(ArrayList<Token> tokens) {
        if (!tokens.get(0).getType().equals(TokenTypes.FOR_KEYWORD))
            return false;

        if (tokens.size() == 1 || !tokens.get(1).getType().equals(TokenTypes.IDENTIFIER))
            new LogError("Expected identifier after \"for\" keyword.", tokens.get(tokens.size() > 1 ? 1 : 0));
        if (tokens.size() == 2 || !tokens.get(2).getType().equals(TokenTypes.IN_KEYWORD))
            new LogError("Expected \"in\" keyword after \"for\" keyword.", tokens.get(tokens.size() > 2 ? 2 : 1));
        if (tokens.size() == 3 || !isExpression(tokens, 3, tokens.size() - 1))
            new LogError("Expected expression after \"in\" keyword.", tokens.get(tokens.size() > 3 ? 3 : 2));
        return true;
    }

    public static boolean isFunctionDeclaration(ArrayList<Token> tokens) {
        var isFirstTokenFuncKeyword = tokens.get(0).getType().equals(TokenTypes.FUNC_KEYWORD);
        var isSecondTokenIdentifier = tokens.size() > 1
                && tokens.get(1).getType().equals(TokenTypes.IDENTIFIER);
        if (!isFirstTokenFuncKeyword)
            return false;
        if (!isSecondTokenIdentifier)
            new LogError("Expected identifier after \"func\" keyword.", tokens.get(1));
        TokenTypes last = null;
        for (int i = 2; i < tokens.size(); i++) {
            var token = tokens.get(i);
            if (isComma(token)) {
                if (!((last == null || last.equals(TokenTypes.IDENTIFIER)) && i + 1 < tokens.size()))
                    new LogError("Unexpected comma.", token);
            } else if (!token.getType().equals(TokenTypes.IDENTIFIER))
                new LogError("Unexpected token:\t\"" + token.getText() + "\"", token);

            last = token.getType();
        }
        return true;

    }
    public static boolean isComma(Token token) {
        return token.getType().equals(TokenTypes.OPERATOR) && token.getText().equals(",");
    }
    public static boolean isIfStatement(ArrayList<Token> tokens) {
        if (!tokens.get(0).getType().equals(TokenTypes.IF_KEYWORD))
            return false;
        if (tokens.size() == 1 || !isExpression(tokens, 1, tokens.size()))
            new LogError("Expected expression after \"if\" keyword.", tokens.get(tokens.size() > 1 ? 1 : 0));
        return true;

    }

    public static boolean isImport(ArrayList<Token> tokens) {
        return tokens.get(0).getType().equals(TokenTypes.IMPORT_KEYWORD)
                && IMPORT_REGEX.matcher(Tokens.getTypesToString(tokens.subList(1, tokens.size()))).matches();
    }

    public static boolean isJar(ArrayList<Token> tokens) {
        return tokens.get(0).getType().equals(TokenTypes.JAR_KEYWORD)
                && IMPORT_REGEX.matcher(Tokens.getTypesToString(tokens.subList(1, tokens.size()))).matches();
    }

    public static boolean isReturnStatement(ArrayList<Token> tokens) {
        var isFirstTokenReturnKeyword = tokens.get(0).getType().equals(TokenTypes.RETURN_KEYWORD);

        if (!isFirstTokenReturnKeyword)
            return false;
        if (tokens.size() > 1
                && !isExpression(tokens, 1, tokens.size()))
            new LogError("Expected expression or nothing after \"return\" keyword.",
                    tokens.get(tokens.size() > 1 ? 1 : 0));
        return true;
    }

    public static boolean isStructDeclaration(ArrayList<Token> tokens) {
        if (!tokens.get(0).getType().equals(TokenTypes.STRUCT_KEYWORD))
            return false;
        if (tokens.size() == 1 || !tokens.get(1).getType().equals(TokenTypes.IDENTIFIER))
            new LogError("Expected name after \"struct\" keyword.", tokens.get(tokens.size() > 1 ? 1 : 0));

        return true;
    }

    public static boolean isVariableDeclaration(ArrayList<Token> tokens) {
        if (!tokens.get(0).getType().equals(TokenTypes.VAR_KEYWORD))
            return false;
        if (tokens.size() == 1
                || !tokens.get(1).getType().equals(TokenTypes.IDENTIFIER))
            new LogError("Expected identifier after \"var\" keyword.", tokens.get(1));
        if (tokens.size() > 2
                && !isExpression(tokens, 1, tokens.size()))
            new LogError("Expected expression after \"var\" keyword.", tokens.get(1));

        return true;
    }

    public static boolean isWhileStatement(ArrayList<Token> tokens) {
        if (!tokens.get(0).getType().equals(TokenTypes.WHILE_KEYWORD))
            return false;
        if (tokens.size() == 1 || !isExpression(tokens, 1, tokens.size()))
            new LogError("Expected expression after \"while\" keyword.", tokens.get(tokens.size() > 1 ? 1 : 0));
        return true;

    }

}
