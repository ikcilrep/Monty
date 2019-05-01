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
import ast.declarations.CustomFunctionDeclarationNode;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.StructDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.FunctionCallNode;
import ast.expressions.OperationNode;
import ast.statements.*;
import lexer.OptimizedTokensArray;
import lexer.TokenTypes;
import parser.LogError;

abstract class AdderToBlock {
    static void addBreakStatement(Block block, String fileName, int line) {
        block.addChild(new BreakStatementNode(fileName, line));
    }

    static void addContinueStatement(Block block, String fileName, int line) {
        block.addChild(new ContinueStatementNode(fileName, line));
    }

    static Block addDoWhileStatement(Block block, OptimizedTokensArray tokens) {
        var whileStatement = new WhileStatementNode(ExpressionParser.parseInfix(block, tokens, 2),
                tokens.get(0).getFileName(), tokens.get(0).getLine(), true, block);
        block.addChild(whileStatement);
        return whileStatement;

    }

    static Block addElseStatement(Block block, OptimizedTokensArray tokens) {
        var elseBlock = new Block(block.getParent());
        elseBlock.setFileName(tokens.get(0).getFileName());
        elseBlock.setLine(tokens.get(0).getLine());
        elseBlock.setParent(block.getParent());
        if (block instanceof IfStatementNode)
            ((IfStatementNode) block).setElseBody(elseBlock);
        else
            new LogError("Unexpected \"else\" keyword.", tokens.get(0));

        return elseBlock;
    }

    static void addExpression(Block block, OptimizedTokensArray tokensBeforeSemicolon) {
        block.addChild(ExpressionParser.parseInfix(block, tokensBeforeSemicolon));
    }

    private static void addExpression(Block block, OptimizedTokensArray tokensBeforeSemicolon, int start) {
        block.addChild(ExpressionParser.parseInfix(block, tokensBeforeSemicolon, start));
    }

    static Block addForStatement(Block block, OptimizedTokensArray tokens) {
        var variableName = tokens.get(1).getText();
        var forStatement = new ForStatementNode(variableName, ExpressionParser.parseInfix(block, tokens, 3),
                tokens.get(0).getFileName(), tokens.get(0).getLine(), block);
        block.addChild(forStatement);
        return forStatement;
    }

    static Block addFunctionDeclaration(Block block, OptimizedTokensArray tokens) {
        var functionName = tokens.get(1).getText();
        var function = new CustomFunctionDeclarationNode(functionName);
        if (Character.isUpperCase(functionName.charAt(0)))
            new LogError("Function name " + functionName + " can't start with upper case.", tokens.get(2));
        parseFunctionsParameters(2, tokens, function);
        function.setBody(new Block(block));
        block.addFunction(function, tokens.get(1));
        return function.getBody();
    }

    static Block addIfStatement(Block block, OptimizedTokensArray tokens, boolean isInElse) {
        var ifStatement = new IfStatementNode(block, ExpressionParser.parseInfix(block, tokens, 1),
                tokens.get(0).getFileName(), tokens.get(0).getLine(), isInElse);
        ifStatement.setInElse(isInElse);
        block.addChild(ifStatement);
        return ifStatement;

    }

    static void addReturnStatement(Block block, OptimizedTokensArray tokens) {
        OperationNode expression;
        if (tokens.length() > 1)
            expression = ExpressionParser.parseInfix(block, tokens, 1);
        else
            expression = new OperationNode(new FunctionCallNode("nothing"), block);
        block.addChild(new ReturnStatementNode(expression, tokens.get(0).getFileName(), tokens.get(0).getLine()));
    }

    static Block addStructDeclaration(Block block, OptimizedTokensArray tokens) {
        var name = tokens.get(1).getText();
        if (!Character.isUpperCase(name.charAt(0)))
            new LogError("Struct name have to start with upper case.", tokens.get(0));
        var struct = new StructDeclarationNode(block, name);
        struct.addNewStruct(block, tokens.get(0));
        return struct;
    }

    static void addVariableDeclaration(Block block, OptimizedTokensArray tokens) {
        var name = tokens.get(1).getText();
        var isConst = Character.isUpperCase(name.charAt(0));
        var variable = new VariableDeclarationNode(name);
        block.addVariable(variable, tokens.get(0));
        if (tokens.length() > 2)
            addExpression(block, tokens, 1);
        else if (isConst)
            new LogError("Const value must be declared at the same time as whole variable.", tokens.get(1));
        variable.setConst(isConst);
    }

    static Block addWhileStatement(Block block, OptimizedTokensArray tokens) {
        var whileStatement = new WhileStatementNode(ExpressionParser.parseInfix(block, tokens, 1),
                tokens.get(0).getFileName(), tokens.get(0).getLine(), false, block);
        block.addChild(whileStatement);
        return whileStatement;

    }

    private static void parseFunctionsParameters(int start, OptimizedTokensArray tokens,
                                                 FunctionDeclarationNode function) {
        String name = null;
        for (int i = start; i < tokens.length(); i++) {
            var tokenType = tokens.get(i).getType();
            var isTokenTypeEqualsComma = tokens.get(i).getType().equals(TokenTypes.COMMA);
            if (tokenType.equals(TokenTypes.IDENTIFIER))
                name = tokens.get(i).getText();
            if (isTokenTypeEqualsComma || i + 1 >= tokens.length())
                function.addParameter(name);
        }
    }
}
