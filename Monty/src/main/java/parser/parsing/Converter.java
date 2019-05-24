package parser.parsing;

import ast.Block;
import ast.expressions.IdentifierNode;
import ast.expressions.OperationNode;
import ast.expressions.Promise;
import lexer.Token;
import lexer.TokenTypes;
import parser.LogError;

import java.util.*;

class Converter {


    private final static Token JUST_OPERATOR = new Token(TokenTypes.OPERATOR, "", null, -1);
    private final static HashMap<String, Integer> PRECEDENCES_OF_OPERATORS;
    private final static Set<String> RIGHT_ASSOCIATIVE_OPERATORS = Set.of("=", "+=", "-=", "*=", "/=", "%=", "&=", "^=", "|=", "<<=",
            ">>=", "**", "**=");
    private final static Set<String> NOT_ASSOCIATIVE_OPERATORS = Set.of("<", "<=", ">=", ">", "instanceof");
    private final static IdentifierNode LIST_CALL = new IdentifierNode("List", true);

    static {
        PRECEDENCES_OF_OPERATORS = new HashMap<>();
        PRECEDENCES_OF_OPERATORS.put("", 18);
        PRECEDENCES_OF_OPERATORS.put(".", 17);
        PRECEDENCES_OF_OPERATORS.put("!", 16);
        PRECEDENCES_OF_OPERATORS.put("**", 15);
        PRECEDENCES_OF_OPERATORS.put("*", 14);
        PRECEDENCES_OF_OPERATORS.put("/", 14);
        PRECEDENCES_OF_OPERATORS.put("%", 14);
        PRECEDENCES_OF_OPERATORS.put("+", 13);
        PRECEDENCES_OF_OPERATORS.put("-", 13);
        PRECEDENCES_OF_OPERATORS.put("<<", 10);
        PRECEDENCES_OF_OPERATORS.put(">>", 10);
        PRECEDENCES_OF_OPERATORS.put("<", 9);
        PRECEDENCES_OF_OPERATORS.put("<=", 9);
        PRECEDENCES_OF_OPERATORS.put(">", 9);
        PRECEDENCES_OF_OPERATORS.put(">=", 9);
        PRECEDENCES_OF_OPERATORS.put("instanceof", 9);
        PRECEDENCES_OF_OPERATORS.put("==", 8);
        PRECEDENCES_OF_OPERATORS.put("!=", 8);
        PRECEDENCES_OF_OPERATORS.put("&", 7);
        PRECEDENCES_OF_OPERATORS.put("^", 6);
        PRECEDENCES_OF_OPERATORS.put("|", 5);
        PRECEDENCES_OF_OPERATORS.put(",", 1);
        PRECEDENCES_OF_OPERATORS.put("=", 0);
        PRECEDENCES_OF_OPERATORS.put("+=", 0);
        PRECEDENCES_OF_OPERATORS.put("-=", 0);
        PRECEDENCES_OF_OPERATORS.put("*=", 0);
        PRECEDENCES_OF_OPERATORS.put("/=", 0);
        PRECEDENCES_OF_OPERATORS.put("%=", 0);
        PRECEDENCES_OF_OPERATORS.put("&=", 0);
        PRECEDENCES_OF_OPERATORS.put("^=", 0);
        PRECEDENCES_OF_OPERATORS.put("|=", 0);
        PRECEDENCES_OF_OPERATORS.put("<<=", 0);
        PRECEDENCES_OF_OPERATORS.put(">>=", 0);
        PRECEDENCES_OF_OPERATORS.put("**=", 0);
    }

    private static int getPrecedence(Token token) {
        return PRECEDENCES_OF_OPERATORS.get(token.getText());
    }

    private static boolean shouldPopFromOperatorStackToOutputQueue(Token actualToken, Token tokenAtTheTop) {
        var topType = tokenAtTheTop.getType();
        if (topType.equals(TokenTypes.OPENING_BRACKET))
            return false;
        if (topType.equals(TokenTypes.FUNCTION))
            return true;
        var actualPrecedence = getPrecedence(actualToken);
        var topPrecedence = getPrecedence(tokenAtTheTop);
        return topPrecedence > actualPrecedence
                || (topPrecedence ==  actualPrecedence && isLeftAssociative(tokenAtTheTop));
    }

    static ArrayList<Token> infixToSuffix(ArrayList<Token> tokens, Block parent, int start, int end) {
        var outputQueue = new ArrayList<Token>();
        var operatorStack = new Stack<Token>();
        var lists = new LinkedList<OperationNode>();
        for (var i = new IntegerHolder(start); i.i < end; i.i++) {
            var token = tokens.get(i.i);
            var type = token.getType();
            switch (type) {
                case OPERATOR:
                    while (!operatorStack.isEmpty() &&
                            shouldPopFromOperatorStackToOutputQueue(token,operatorStack.peek()))
                        outputQueue.add(operatorStack.pop());
                    operatorStack.push(token);
                    break;
                case OPENING_SQUARE_BRACKET:
                    lists.add(parseList(tokens, parent, i));
                    outputQueue.add(token);
                    break;
                case CLOSING_BRACKET:
                    try {
                        while (!operatorStack.peek().getType().equals(TokenTypes.OPENING_BRACKET))
                            outputQueue.add(operatorStack.pop());
                    } catch (EmptyStackException e) {
                        new LogError("Mismatched brackets.", token);
                    }
                    if (tokens.get(i.i-1).getType().equals(TokenTypes.OPENING_BRACKET))
                        outputQueue.add(new Token(TokenTypes.EMPTY_TUPLE, "", token.getFileName(), token.getLine()));
                    operatorStack.pop();
                    operatorStack.push(JUST_OPERATOR);

                    break;
                case OPENING_BRACKET:
                    operatorStack.push(token);
                    break;
                case IDENTIFIER:
                    if (i.i + 1 < tokens.size()) {
                        var nextType = tokens.get(i.i + 1).getType();
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
        }
        while (!operatorStack.empty())
            outputQueue.add(operatorStack.pop());
        ExpressionParser.setLists(lists);
        return outputQueue;
    }

    private static boolean isLeftAssociative(Token token) {
        var operator = token.getText();
        return !(RIGHT_ASSOCIATIVE_OPERATORS.contains(operator) || NOT_ASSOCIATIVE_OPERATORS.contains(operator));
    }

    private static OperationNode parseList(ArrayList<Token> tokens, Block parent, IntegerHolder i) {
        var token = tokens.get(i.i);
        var list = new OperationNode(LIST_CALL, parent, token.getFileName(), token.getLine());
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
        if (counter - 1 > i.i) {
            list.setRight(ExpressionParser.parseInfix(parent, tokens, i.i, counter - 1));
            i.i = counter - 1;
        } else
            list.setRight(new OperationNode(Promise.EMPTY_TUPLE, parent));
        return list;

    }
}
