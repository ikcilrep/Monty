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
import ast.expressions.ConstantNode;
import ast.expressions.OperationNode;
import ast.expressions.IdentifierNode;
import lexer.Token;
import parser.DataTypes;
import parser.LogError;
import parser.Tokens;
import sml.Sml;
import sml.data.string.StringStruct;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

class ExpressionParser {
    private final static HashMap<String, ConstantNode> LITERALS = new HashMap<>();
    private final static HashMap<String, ConstantNode> STRING_LITERALS = new HashMap<>();
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
                    node = new OperationNode(token.getText(), parent);
                    if (stack.isEmpty())
                        new LogError("There isn't right operand", token);

                    if (token == Converter.EMPTY_OPERATOR || token.getText().equals("!"))
                        node.setRight(stack.peek());
                    else
                        node.setRight(stack.pop());

                    if (stack.isEmpty())
                        new LogError("There isn't left operand", token);
                    node.setLeft(stack.pop());
                    break;
                case FUNCTION:
                    node = new OperationNode(new IdentifierNode(token.getText(), true), parent);
                    if (stack.isEmpty())
                        new LogError("There isn't right operand", token);
                    node.setRight(stack.pop());
                    break;
                case IDENTIFIER: // If token is identifier
                    return recParseIdentifier(parent, tokens, stack, i);
                case OPENING_SQUARE_BRACKET:
                    return recParseList(parent, tokens, stack, i);
                case EMPTY_TUPLE:
                    return recParseEmptyTuple(parent, tokens, stack,i);
                default:
                    // Otherwise token in expression can be only constant.
                    var dataType = Tokens.getDataType(token.getType());
                    node = new OperationNode(toDataType(token, dataType), parent);
                    break;
            }
            stack.push(node);
            node.setFileName(token.getFileName());
            node.setLine(token.getLine());
            i.i++;
            return parse(parent, tokens, stack, i);
        }
        if (stack.size() != 1)
            new LogError("Ambiguous result for operation.", tokens.get(0));
        return stack.pop();
    }


    static OperationNode parseInfix(Block parent, ArrayList<Token> tokens) {
        return parseInfix(parent, tokens, 0);
    }

    static OperationNode parseInfix(Block parent, ArrayList<Token> tokens, int start) {
        return parse(parent, Converter.infixToSuffix(tokens, parent, start), new Stack<>(), new IntegerHolder(0));
    }

    private static OperationNode parseIdentifier(Block parent, ArrayList<Token> array, IntegerHolder i) {
        var token = array.get(i.i);
        var variable = new IdentifierNode(token.getText(), false);
        var node = new OperationNode(variable, parent);
        node.setFileName(token.getFileName());
        node.setLine(token.getLine());
        return node;
    }

    private static OperationNode recParseIdentifier(Block parent, ArrayList<Token> tokens,
                                                    Stack<OperationNode> stack, IntegerHolder i) {
        stack.push(parseIdentifier(parent, tokens, i));
        i.i++;
        return parse(parent, tokens, stack, i);
    }

    private static OperationNode recParseList(Block parent, ArrayList<Token> tokens,
                                              Stack<OperationNode> stack, IntegerHolder i) {
        stack.push(lists.poll());
        i.i++;
        return parse(parent, tokens, stack, i);
    }

    private static OperationNode recParseEmptyTuple(Block parent, ArrayList<Token> tokens,
                                              Stack<OperationNode> stack, IntegerHolder i) {
        stack.push(new OperationNode(new ConstantNode(Sml.EMPTY_ARGUMENT_LIST),parent));
        i.i++;
        return parse(parent, tokens, stack, i);
    }


    static void setLists(LinkedList<OperationNode> lists) {
        ExpressionParser.lists = lists;
    }

    private static ConstantNode toDataType(Token token, DataTypes dataType) {
        // Returns values with proper data type.
        var literal = token.getText();
        if (dataType != null) {
            if (dataType.equals(DataTypes.OBJECT))
                if (STRING_LITERALS.containsKey(literal))
                    return STRING_LITERALS.get(literal);
                else {
                    var newStringLiteral = new ConstantNode(new StringStruct(literal));
                    STRING_LITERALS.put(literal, newStringLiteral);
                    return newStringLiteral;
                }
        } else
            new LogError("Unexpected token \"" + literal + "\"", token);

        if (LITERALS.containsKey(literal))
            return LITERALS.get(literal);
        Object valueOfDataType = null;
        switch (dataType) {
            case INTEGER:
                try {
                    valueOfDataType = Integer.parseInt(literal);
                } catch (NumberFormatException e) {
                    valueOfDataType = new BigInteger(literal);
                }
                break;
            case FLOAT:
                try {
                    valueOfDataType = Double.parseDouble(literal);
                } catch (NumberFormatException e) {
                    new LogError("Float overflow.", token);
                }
                break;
            case BOOLEAN:
                valueOfDataType = Boolean.parseBoolean(literal);
                break;
            default:
                new LogError("There isn't constant of " + dataType.toString().toLowerCase());
        }
        var newLiteral = new ConstantNode(valueOfDataType);
        LITERALS.put(literal, newLiteral);
        return newLiteral;
    }

}