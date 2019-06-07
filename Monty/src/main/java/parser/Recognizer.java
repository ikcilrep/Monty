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

import ast.Operator;
import lexer.Token;
import lexer.TokenTypes;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Set;

public abstract class Recognizer {
    private static final Set<String> UNARY_OPERATORS = Set.of("!", "");

    public static boolean isUnaryOperator(String operator) {
        return UNARY_OPERATORS.contains(operator);
    }

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
        if (tokens.size() > 1 && !isIfStatement(tokens, 1))
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
        for (; start < end; start++) {
            var token = tokens.get(start);
            switch (token.getType()) {
                case OPERATOR:
                case INTEGER_LITERAL:
                case FLOAT_LITERAL:
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

    public static RecognizedFunctionPrototype isFunctionDeclaration(ArrayList<Token> tokens) {
        var isFirstTokenFuncKeyword = tokens.get(0).getType().equals(TokenTypes.FUNC_KEYWORD);
        var isSecondTokenIdentifier = tokens.size() > 1
                && tokens.get(1).getType().equals(TokenTypes.IDENTIFIER);
        if (!isFirstTokenFuncKeyword)
            return new RecognizedFunctionPrototype();
        if (!isSecondTokenIdentifier)
            new LogError("Expected identifier after \"func\" keyword.", tokens.get(1));
        for (int i = 2; i < tokens.size(); i++) {
            var token = tokens.get(i);
            var tokenType = token.getType();
            var tokenText = token.getText();
            if (!tokenType.equals(TokenTypes.IDENTIFIER)) {
                if (tokenType.equals(TokenTypes.OPERATOR) && Operator.toOperator(tokenText) == Operator.LAMBDA) {
                    if (!(i+1 < tokens.size() &&  isExpression(tokens, i+1 ,tokens.size())))
                        new LogError("Expected expression after arrow operator.",token);
                    return new RecognizedFunctionPrototype(i,true);
                } else
                new LogError("Unexpected token:\t\"" + tokenText + "\"", token);
            }
        }
        return new RecognizedFunctionPrototype(tokens.size(),false);

    }

    public static boolean isIfStatement(ArrayList<Token> tokens) {
        return isIfStatement(tokens, 0);

    }

    private static boolean isIfStatement(ArrayList<Token> tokens, int start) {
        if (!tokens.get(start).getType().equals(TokenTypes.IF_KEYWORD))
            return false;
        var length = start + 1;
        if (tokens.size() == length || !isExpression(tokens, length, tokens.size()))
            new LogError("Expected expression after \"if\" keyword.", tokens.get(tokens.size() > length ? length : start));
        return true;

    }

    public static boolean isImportStatement(ArrayList<Token> tokens) {
        if (!tokens.get(0).getType().equals(TokenTypes.IMPORT_KEYWORD))
            return false;
        if (tokens.size() == 1 || !tokens.get(1).getType().equals(TokenTypes.STRING_LITERAL))
            new LogError("Expected string after \"import\" keyword, got " + tokens.get(1).getText(), tokens.get(1));

        if (tokens.size() == 2 || !tokens.get(2).getType().equals(TokenTypes.IN_KEYWORD))
            new LogError("Expected \"in\" keyword after location, got " + tokens.get(2).getText(), tokens.get(2));

        if (tokens.size() == 3 || !tokens.get(3).getType().equals(TokenTypes.IDENTIFIER))
            new LogError("Expected identifier after \"in\" keyword, got " + tokens.get(2).getText(), tokens.get(2));

        return true;
    }

    public static boolean isNamespaceDeclaration(ArrayList<Token> tokens) {
        if (!tokens.get(0).getType().equals(TokenTypes.NAMESPACE_KEYWORD))
            return false;
        if (tokens.size() == 1 || !tokens.get(1).getType().equals(TokenTypes.IDENTIFIER))
            new LogError("Expected identifier after \"in\" keyword, got " + tokens.get(1).getText(), tokens.get(2));
        return true;
    }

    public static boolean isReturnStatement(ArrayList<Token> tokens) {
        var isFirstTokenReturnKeyword = tokens.get(0).getType().equals(TokenTypes.RETURN_KEYWORD);

        if (!isFirstTokenReturnKeyword)
            return false;
        if (tokens.size() > 1
                && !isExpression(tokens, 1, tokens.size()))
            new LogError("Expected expression or NOTHING after \"return\" keyword.",
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


    public static boolean isWhileStatement(ArrayList<Token> tokens) {
        if (!tokens.get(0).getType().equals(TokenTypes.WHILE_KEYWORD))
            return false;
        if (tokens.size() == 1 || !isExpression(tokens, 1, tokens.size()))
            new LogError("Expected expression after \"while\" keyword.", tokens.get(tokens.size() > 1 ? 1 : 0));
        return true;

    }

}
