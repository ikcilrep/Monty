package parser.parsing;

import ast.Block;
import ast.expressions.FunctionCallNode;
import ast.expressions.OperationNode;
import lexer.OptimizedTokensArray;
import lexer.Token;
import lexer.TokenTypes;
import parser.LogError;

import java.util.*;

class Converter {
    private static HashMap<String, Integer> precedence;
    private static Set<String> rightAssociative = Set.of("=", "+=", "-=", "*=", "/=", "%=", "&=", "^=", "|=", "<<=",
            ">>=", "**", "**=");
    private static Set<String> notAssociative = Set.of("<", "<=", ">=", ">", "instanceof");

    static {
        precedence = new HashMap<>();
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

    }

    static OptimizedTokensArray infixToSuffix(OptimizedTokensArray tokens, Block parent) {
        var outputQueue = new OptimizedTokensArray();
        var operatorStack = new Stack<Token>();
        var functions = new LinkedList<FunctionCallNode>();
        var lists = new LinkedList<FunctionCallNode>();
        var wasLastIdentifier = false;
        for (var i = new IntegerHolder(0); i.i < tokens.length(); i.i++) {
            var token = tokens.get(i.i);
            var type = token.getType();
            switch (type) {
                case OPERATOR:
                    if (!operatorStack.empty()) {
                        var top = operatorStack.peek();
                        if (!top.getType().equals(TokenTypes.OPENING_BRACKET)) {
                            var operatorAtTheTop = top.getText();
                            int topPrecedence = precedence.get(operatorAtTheTop);
                            int thisPrecedence = precedence.get(token.getText());
                            while (topPrecedence > thisPrecedence
                                    || (topPrecedence == thisPrecedence && isLeftAssociative(operatorAtTheTop))) {
                                outputQueue.append(operatorStack.pop());
                                if (operatorStack.empty())
                                    break;
                                top = operatorStack.peek();
                                if (top.getType().equals(TokenTypes.OPENING_BRACKET))
                                    break;
                                operatorAtTheTop = top.getText();
                                topPrecedence = precedence.get(top.getText());
                                thisPrecedence = precedence.get(token.getText());
                            }
                        }
                    }
                    operatorStack.push(token);
                    break;
                case OPENING_SQUARE_BRACKET:
                    lists.add(parseList(tokens, parent, i));
                    outputQueue.append(token);
                    break;
                case OPENING_BRACKET:
                    if (wasLastIdentifier) {
                        var last = outputQueue.get(outputQueue.length() - 1);
                        last.setFunction(true);
                        functions.add(parseFunction(last.getText(), tokens, parent, i));
                    } else
                        operatorStack.push(token);
                    break;
                case CLOSING_BRACKET:
                    while (!operatorStack.peek().getType().equals(TokenTypes.OPENING_BRACKET))
                        try {
                            outputQueue.append(operatorStack.pop());
                        } catch (EmptyStackException e) {
                            new LogError("Mismatched brackets.", token);
                        }
                    operatorStack.pop();
                    break;
                default:
                    outputQueue.append(token);
                    break;
            }
            wasLastIdentifier = type.equals(TokenTypes.IDENTIFIER);
        }
        while (!operatorStack.empty())
            outputQueue.append(operatorStack.pop());
        ExpressionParser.setFunctions(functions);
        ExpressionParser.setLists(lists);
        return outputQueue;
    }

    private static boolean isLeftAssociative(String operator) {
        return !(rightAssociative.contains(operator) || notAssociative.contains(operator));
    }

    private static ArrayList<OperationNode> parseExpressionsSeparatedByComma(OptimizedTokensArray tokens,
                                                                                   Block parent, IntegerHolder i, boolean isList) {
        var result = new ArrayList<OperationNode>();
        var expression = new OptimizedTokensArray();
        var type = tokens.get(i.i).getType();
        short a = 0;
        short b = 1;
        if (isList) {
            a = 1;
            b = 0;
        }
        for (short[] brackets = {b, a}; brackets[a] > 0; i.i++) {
            var token = tokens.get(i.i);
            try {
                type = token.getType();
            } catch (NullPointerException e) {
                new LogError("Unclosed bracket.", tokens.get(i.i - 1));
            }

            switch (type) {
                case OPENING_BRACKET:
                    brackets[0]++;
                    break;
                case CLOSING_BRACKET:
                    brackets[0]--;
                    break;
                case OPENING_SQUARE_BRACKET:
                    brackets[1]++;
                    break;
                case CLOSING_SQUARE_BRACKET:
                    brackets[1]--;
                    break;
                default:
                    break;
            }

            if (brackets[b] == 0 && ((type.equals(TokenTypes.COMMA) && brackets[a] == 1) || brackets[a] == 0)) {
                if (expression.length() == 0)
                    if (brackets[a] == 1)
                        new LogError("Unexpected comma.", tokens.get(0));
                    else
                        continue;
                result.add(ExpressionParser.parseInfix(parent, expression));
                expression.clear();
            } else
                expression.append(token);
        }
        i.i--;
        return result;
    }

    private static FunctionCallNode parseFunction(String name, OptimizedTokensArray tokens, Block parent,
                                                  IntegerHolder i) {
        i.i++;
        var function = new FunctionCallNode(name);
        function.setArguments(parseExpressionsSeparatedByComma(tokens, parent, i, false));
        return function;
    }

    private static FunctionCallNode parseList(OptimizedTokensArray tokens, Block parent, IntegerHolder i) {
        var function = new FunctionCallNode("List");
        i.i++;
        function.setArguments(parseExpressionsSeparatedByComma(tokens, parent, i, true));
        return function;
    }
}
