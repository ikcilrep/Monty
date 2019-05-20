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

package parser.parsing;

import ast.Block;
import ast.Operator;
import ast.expressions.IdentifierNode;
import ast.expressions.OperationNode;
import ast.expressions.Promise;
import lexer.Token;
import parser.DataTypes;
import parser.LogError;
import parser.Recognizer;
import parser.Tokens;
import sml.data.string.MontyString;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

class ExpressionParser {
    /*
     * Parses list of tokens to abstract syntax tree.
     */
    private static LinkedList<OperationNode> lists;

    private static OperationNode parse(Block parent, ArrayList<Token> tokens, Stack<OperationNode> stack,
                                       IntegerHolder i) {
        if (i.i < tokens.size()) {
            var token = tokens.get(i.i);
            OperationNode node;
            switch (token.getType()) {
                case OPERATOR: // If token is operator
                    var tokenText = token.getText();
                    node = new OperationNode(Operator.toOperator(tokenText), parent, token.getFileName(), token.getLine());
                    if (stack.isEmpty())
                        new LogError("There isn't right operand", token);
                    node.setRight(stack.pop());

                    if (!Recognizer.isUnaryOperator(tokenText)) {
                        if (stack.isEmpty())
                            new LogError("There isn't left operand", token);
                        node.setLeft(stack.pop());
                    }
                    break;
                case FUNCTION:
                    node = new OperationNode(new IdentifierNode(token.getText(), true), parent, token.getFileName(), token.getLine());
                    if (stack.isEmpty())
                        new LogError("There isn't right operand", token);
                    node.setRight(stack.pop());
                    break;
                case IDENTIFIER: // If token is identifier
                    node = new OperationNode(new IdentifierNode(token.getText(), false), parent, token.getFileName(), token.getLine());
                    break;
                case OPENING_SQUARE_BRACKET:
                    node = lists.poll();
                    break;
                case EMPTY_TUPLE:
                    node = new OperationNode(Promise.EMPTY_TUPLE, parent, token.getFileName(), token.getLine());
                    break;
                default:
                    node = new OperationNode(toDataType(token, Tokens.getDataType(token.getType())), parent, token.getFileName(), token.getLine());
                    break;
            }
            stack.push(node);
            i.i++;
            return parse(parent, tokens, stack, i);
        }
        if (stack.size() != 1)
            new LogError("Ambiguous result for operation.", tokens.get(0));
        return stack.pop();
    }


    static OperationNode parseInfix(Block parent, ArrayList<Token> tokens) {
        return parseInfix(parent, tokens, 0, tokens.size());
    }

    static OperationNode parseInfix(Block parent, ArrayList<Token> tokens, int start) {
        return parseInfix(parent, tokens, start, tokens.size());
    }

    static OperationNode parseInfix(Block parent, ArrayList<Token> tokens, int start, int end) {
        return parse(parent, Converter.infixToSuffix(tokens, parent, start, end), new Stack<>(), new IntegerHolder(0));
    }


    static void setLists(LinkedList<OperationNode> lists) {
        ExpressionParser.lists = lists;
    }

    private static Object toDataType(Token token, DataTypes dataType) {
        // Returns values with proper data type.
        var literal = token.getText();
        if (dataType == null)
            new LogError("Unexpected token \"" + literal + "\"", token);


        switch (dataType) {
            case INTEGER:
                try {
                    return Integer.parseInt(literal);
                } catch (NumberFormatException e) {
                    return new BigInteger(literal);
                }
            case FLOAT:
                try {
                    return Double.parseDouble(literal);
                } catch (NumberFormatException e) {
                    return new LogError("Float overflow.", token);
                }
            case BOOLEAN:
                return Boolean.parseBoolean(literal);
            case OBJECT:
                return new MontyString(literal);
            default:
                return new LogError("There isn't constant of " + dataType.toString().toLowerCase());
        }
    }

}