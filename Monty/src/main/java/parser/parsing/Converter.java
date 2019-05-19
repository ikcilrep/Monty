package parser.parsing;

import ast.Block;
import ast.expressions.IdentifierNode;
import ast.expressions.OperationNode;
import lexer.Token;
import lexer.TokenTypes;
import parser.LogError;
import sml.Sml;
import sml.data.tuple.Tuple;

import java.util.*;

class Converter {


    private final static Token EMPTY_OPERATOR = new Token(TokenTypes.OPERATOR, "", null, -1);
    private final static HashMap<String, Integer> precedence;
    private final static Set<String> rightAssociative = Set.of("=", "+=", "-=", "*=", "/=", "%=", "&=", "^=", "|=", "<<=",
            ">>=", "**", "**=");
    private final static Set<String> notAssociative = Set.of("<", "<=", ">=", ">", "instanceof");
    private final static IdentifierNode LIST_CALL = new IdentifierNode("List", true);
    static {
        precedence = new HashMap<>();
        precedence.put("", 18);
        precedence.put(".", 17);
        precedence.put("!", 16);
        precedence.put("**", 15);
        precedence.put("*", 14);
        precedence.put("/", 14);
        precedence.put("%", 14);
        precedence.put("+", 13);
        precedence.put("-", 13);
        precedence.put("<<", 10);
        precedence.put(">>", 10);
        precedence.put("<", 9);
        precedence.put("<=", 9);
        precedence.put(">", 9);
        precedence.put(">=", 9);
        precedence.put("instanceof", 9);
        precedence.put("==", 8);
        precedence.put("!=", 8);
        precedence.put("&", 7);
        precedence.put("^", 6);
        precedence.put("|", 5);
        precedence.put("=", 1);
        precedence.put("+=", 1);
        precedence.put("-=", 1);
        precedence.put("*=", 1);
        precedence.put("/=", 1);
        precedence.put("%=", 1);
        precedence.put("&=", 1);
        precedence.put("^=", 1);
        precedence.put("|=", 1);
        precedence.put("<<=", 1);
        precedence.put(">>=", 1);
        precedence.put("**=", 1);
        precedence.put(",", 0);


    }
    private static int getPrecedence(Token token) {
        return token.getType().equals(TokenTypes.FUNCTION) ? 100 :precedence.get(token.getText());
    }

    static ArrayList<Token> infixToSuffix(ArrayList<Token> tokens, Block parent, int start, int end) {
        var outputQueue = new ArrayList<Token>();
        var operatorStack = new Stack<Token>();
        var lists = new LinkedList<OperationNode>();
        var wasLastOpeningBracket = false;
        for (var i = new IntegerHolder(start); i.i < end; i.i++) {
            var token = tokens.get(i.i);
            var type = token.getType();
            switch (type) {
                case OPERATOR:
                    if (!operatorStack.empty()) {
                        var top = operatorStack.peek();
                        if (!(top.getType().equals(TokenTypes.OPENING_BRACKET))) {
                            int topPrecedence = getPrecedence(top);
                            int thisPrecedence = getPrecedence(token);
                            while (topPrecedence > thisPrecedence
                                    || (topPrecedence == thisPrecedence && isLeftAssociative(top))) {
                                outputQueue.add(operatorStack.pop());
                                if (operatorStack.empty())
                                    break;
                                top = operatorStack.peek();
                                var topType = top.getType();
                                if (topType.equals(TokenTypes.OPENING_BRACKET))
                                    break;
                                topPrecedence =  getPrecedence(top);
                                thisPrecedence = getPrecedence(token);
                            }
                        }
                    }
                    operatorStack.push(token);
                    break;
                case OPENING_SQUARE_BRACKET:
                    lists.add(parseList(tokens, parent, i));
                    outputQueue.add(token);
                    break;
                case CLOSING_BRACKET:
                    if (wasLastOpeningBracket)
                        outputQueue.add(new Token(TokenTypes.EMPTY_TUPLE, "", token.getFileName(), token.getLine()));
                    else
                        try {
                            while (!operatorStack.peek().getType().equals(TokenTypes.OPENING_BRACKET))
                                outputQueue.add(operatorStack.pop());
                        } catch (EmptyStackException e) {
                            new LogError("Mismatched brackets.", token);
                        }
                    operatorStack.pop();
                    operatorStack.push(EMPTY_OPERATOR);

                    break;
                case OPENING_BRACKET:
                    operatorStack.push(token);
                    break;
                case IDENTIFIER:
                    if (i.i + 1 < tokens.size()) {
                        var nextType =tokens.get(i.i + 1).getType();
                        if (!(nextType.equals(TokenTypes.OPERATOR) || nextType.equals(TokenTypes.CLOSING_BRACKET)
                                || nextType.equals(TokenTypes.CLOSING_SQUARE_BRACKET))) {
                            token.setType(TokenTypes.FUNCTION);
                            operatorStack.push(token);
                            break;
                        }
                    }
                    outputQueue.add(token);
                    break;
                default:
                    outputQueue.add(token);
                    break;
            }
            wasLastOpeningBracket = type.equals(TokenTypes.OPENING_BRACKET);

        }
        while (!operatorStack.empty())
            outputQueue.add(operatorStack.pop());
        ExpressionParser.setLists(lists);
        return outputQueue;
    }

    private static boolean isLeftAssociative(Token token) {
        var operator = token.getText();
        return !(rightAssociative.contains(operator) || notAssociative.contains(operator));
    }

    private static OperationNode parseList(ArrayList<Token> tokens, Block parent, IntegerHolder i) {
        var list = new OperationNode(LIST_CALL, parent);
        var token = tokens.get(i.i);
        token.setFileName(token.getFileName());
        token.setLine(token.getLine());
        i.i++;
        var openedBrackets = 1;
        var counter = i.i;
        while (openedBrackets > 0) {
            var tokenType = tokens.get(counter).getType();
            switch (tokenType) {
                case OPENING_SQUARE_BRACKET:
                    openedBrackets++;
                    break;
                case CLOSING_SQUARE_BRACKET:
                    openedBrackets--;
                    break;
                    default:
                        break;
            }
            counter++;
        }
        if (counter -1 > i.i) {
            list.setRight(ExpressionParser.parseInfix(parent, tokens,i.i, counter-1));
            i.i = counter - 1;
        } else
            list.setRight(new OperationNode(new Tuple(),parent));
        return list;

    }
}
