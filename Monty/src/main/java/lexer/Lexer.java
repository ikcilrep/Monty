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

package lexer;

import org.apache.commons.text.StringEscapeUtils;
import parser.LogError;

import java.util.ArrayList;
import java.util.Set;

public final class Lexer {
    private final static Set<Character> OPERATORS_PARTS = Set.of('+', '-', '*', '/', '!', '<', '>', '=', '|', '&', '%',
            '^', '.', ',');
    private final static Set<String> OPERATORS = Set.of(".", "!", "+", "-", "*", "**", "/", "<", ">", "&", "|", "^", "=",
            "<<", ">>", "!=", "+=", "-=", "*=", "/=", "<=", ">=", "&=", "|=", "^=", "==", "<<=", ">>=", "%", "%=", "**=",
            ",", "->");

    private final static Set<TokenTypes> hasValue = Set.of(TokenTypes.BOOLEAN_LITERAL, TokenTypes.INTEGER_LITERAL,
            TokenTypes.STRING_LITERAL, TokenTypes.FLOAT_LITERAL, TokenTypes.IDENTIFIER, TokenTypes.CLOSING_BRACKET,
            TokenTypes.CLOSING_SQUARE_BRACKET);

    private static ArrayList<Token> identifierOrKeyword(String code, String fileName, int line,
                                                        ArrayList<Token> tokens, int i) {
        var tokenText = new StringBuilder(String.valueOf(code.charAt(i)));
        char c;
        while (++i < code.length() && Character.isJavaIdentifierPart(c = code.charAt(i)))
            tokenText.append(c);
        tokens.add(new Token(keywordOrIdentifierToTokenType(tokenText.toString()), tokenText.toString(), fileName, line));
        return lex(code, fileName, line, tokens, i);
    }

    private static TokenTypes getSpecialTokenType(char tokenText) {
        switch (tokenText) {
            case '(':
                return TokenTypes.OPENING_BRACKET;
            case ')':
                return TokenTypes.CLOSING_BRACKET;
            case '[':
                return TokenTypes.OPENING_SQUARE_BRACKET;
            case ']':
                return TokenTypes.CLOSING_SQUARE_BRACKET;
            default:
                return TokenTypes.SEMICOLON;
        }

    }

    private static TokenTypes keywordOrIdentifierToTokenType(String tokenText) {
        switch (tokenText) {
            case "if":
                return TokenTypes.IF_KEYWORD;
            case "else":
                return TokenTypes.ELSE_KEYWORD;
            case "end":
                return TokenTypes.END_KEYWORD;
            case "func":
                return TokenTypes.FUNC_KEYWORD;
            case "struct":
                return TokenTypes.STRUCT_KEYWORD;
            case "return":
                return TokenTypes.RETURN_KEYWORD;
            case "for":
                return TokenTypes.FOR_KEYWORD;
            case "while":
                return TokenTypes.WHILE_KEYWORD;
            case "in":
                return TokenTypes.IN_KEYWORD;
            case "break":
                return TokenTypes.BREAK_KEYWORD;
            case "continue":
                return TokenTypes.CONTINUE_KEYWORD;
            case "import":
                return TokenTypes.IMPORT_KEYWORD;
            case "do":
                return TokenTypes.DO_KEYWORD;
            case "true":
            case "false":
                return TokenTypes.BOOLEAN_LITERAL;
            case "instanceof":
                return TokenTypes.OPERATOR;
            case "namespace":
                return TokenTypes.NAMESPACE_KEYWORD;
            default:
                return TokenTypes.IDENTIFIER;
        }
    }

    public static ArrayList<Token> lex(String code, String path) {
        return lex(code, path, 1, new ArrayList<>(), 0);
    }

    public static ArrayList<Token> lex(String code, String path, int line) {
        return lex(code, path, 1, new ArrayList<>(), line);
    }

    public static ArrayList<Token> lex(String code, String fileName, int line, ArrayList<Token> tokens,
                                       int i) {
        var isInComment = false;
        for (; i < code.length(); i++) {
            char c = code.charAt(i);
            if (c == '\n') {
                line++;
                isInComment = false;
            }
            if (!isInComment) {
                if (c == '#')
                    isInComment = true;
                if (c == ';' || c == '(' || c == ')' || c == '[' || c == ']')
                    tokens.add(new Token(getSpecialTokenType(c), c + "", fileName, line));
                if (c == '\"')
                    return stringLiteral(code, fileName, line, tokens, i);
                if (Character.isDigit(c) || ((c == '+' || c == '-')
                        && (tokens.size() == 0 || !hasValue.contains(tokens.get(tokens.size() - 1).getType())
                        && (i + 1 < code.length() && Character.isDigit(code.charAt(i + 1))))))
                    return number(code, fileName, line, tokens, i);
                if (OPERATORS_PARTS.contains(c))
                    return operator(code, fileName, line, tokens, i);
                if (Character.isJavaIdentifierStart(c))
                    return identifierOrKeyword(code, fileName, line, tokens, i);
            }
        }
        tokens.trimToSize();
        return tokens;
    }

    private static ArrayList<Token> number(String code, String fileName, int line,
                                           ArrayList<Token> tokens, int i) {
        var tokenText = new StringBuilder(String.valueOf(code.charAt(i)));
        while (++i < code.length() && Character.isDigit(code.charAt(i)))
            tokenText.append(code.charAt(i));
        if (i + 1 < code.length() && code.charAt(i) == '.' && Character.isDigit(code.charAt(i + 1)))
            return realLiteral(code, tokenText.toString() + '.', fileName, line, tokens, i);
        tokens.add(new Token(TokenTypes.INTEGER_LITERAL, tokenText.toString(), fileName, line));
        return lex(code, fileName, line, tokens, i);
    }

    private static ArrayList<Token> operator(String code, String fileName, int line,
                                             ArrayList<Token> tokens, int i) {
        var tokenText = new StringBuilder(String.valueOf(code.charAt(i)));
        while (++i < code.length() && OPERATORS_PARTS.contains(code.charAt(i)))
            tokenText.append(code.charAt(i));
        Token token = new Token(operatorToTokenType(tokenText.toString(), fileName, line), tokenText.toString(), fileName, line);
        tokens.add(token);
        return lex(code, fileName, line, tokens, i);
    }

    private static TokenTypes operatorToTokenType(String tokenText, String fileName, int line) {
        if (tokenText.equals("=>"))
            return TokenTypes.ARROW;
        if (OPERATORS.contains(tokenText))
            return TokenTypes.OPERATOR;
        new LogError("Unknown operator:\t" + tokenText, fileName, line);
        return null;
    }

    private static ArrayList<Token> realLiteral(String code, String integer, String fileName, int line,
                                                ArrayList<Token> tokens, int i) {
        var tokenText = new StringBuilder(integer);
        while (++i < code.length() && Character.isDigit(code.charAt(i)))
            tokenText.append(code.charAt(i));
        tokens.add(new Token(TokenTypes.FLOAT_LITERAL, tokenText.toString(), fileName, line));
        return lex(code, fileName, line, tokens, i);
    }

    private static ArrayList<Token> stringLiteral(String code, String fileName, int line,
                                                  ArrayList<Token> tokens, int i) {
        var tokenText = new StringBuilder();
        try {
            while (code.charAt(++i - 1) == '\\' || code.charAt(i) != '\"')
                tokenText.append(code.charAt(i));
        } catch (IndexOutOfBoundsException e) {
            new LogError("String wasn't closed", fileName, line);
        }
        Token token = new Token(TokenTypes.STRING_LITERAL, StringEscapeUtils.unescapeJava(tokenText.toString()), fileName, line);
        tokens.add(token);
        return lex(code, fileName, line, tokens, i + 1);
    }
}
