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
import lexer.Token;
import lexer.TokenTypes;
import parser.LogError;
import parser.Recognizer;

import java.util.ArrayList;

public final class Parser {
    private static Block endAndReturnParent(Block block, String errorMessage,Token token) {
        var parent = block.getParent();
        if (parent == null)
            new LogError(errorMessage, token);
        return block.getParent();
    }
    public static Block parse(ArrayList<Token> tokens) {
        var tokensBeforeSemicolon = new ArrayList<Token>();
        var block = new Block(null);
        var mainBlock = block;
        for (Token token : tokens) {
            if (token.getType().equals(TokenTypes.SEMICOLON)) {
                if (tokensBeforeSemicolon.size() == 0)
                    continue;
                if (Recognizer.isReturnStatement(tokensBeforeSemicolon)) {
                    AdderToBlock.addReturnStatement(block, tokensBeforeSemicolon);
                } else if (Recognizer.isFunctionDeclaration(tokensBeforeSemicolon)) {
                    block = AdderToBlock.addFunctionDeclaration(block, tokensBeforeSemicolon);
                } else if (Recognizer.isStructDeclaration(tokensBeforeSemicolon)) {
                    block = AdderToBlock.addStructDeclaration(block, tokensBeforeSemicolon);
                } else if (Recognizer.isIfStatement(tokensBeforeSemicolon)) {
                    block = AdderToBlock.addIfStatement(block, tokensBeforeSemicolon, false);
                } else if (Recognizer.isElseStatement(tokensBeforeSemicolon)) {
                    block = AdderToBlock.addElseStatement(block, tokensBeforeSemicolon);
                    if (tokensBeforeSemicolon.size() > 1) {
                        block = AdderToBlock.addIfStatement(block, (ArrayList<Token>)
                                tokensBeforeSemicolon.subList(1, tokensBeforeSemicolon.size()), true);
                    }
                } else if (Recognizer.isWhileStatement(tokensBeforeSemicolon)) {
                    block = AdderToBlock.addWhileStatement(block, tokensBeforeSemicolon);
                } else if (Recognizer.isDoWhileStatement(tokensBeforeSemicolon)) {
                    block = AdderToBlock.addDoWhileStatement(block, tokensBeforeSemicolon);
                } else if (Recognizer.isImportStatement(tokensBeforeSemicolon)) {
                    AdderToBlock.addImportStatement(block, tokensBeforeSemicolon);
                } else if (Recognizer.isBreakStatement(tokensBeforeSemicolon)) {
                    AdderToBlock.addBreakStatement(block, tokensBeforeSemicolon.get(0).getFileName(),
                            tokensBeforeSemicolon.get(0).getLine());
                } else if (Recognizer.isContinueStatement(tokensBeforeSemicolon)) {
                    AdderToBlock.addContinueStatement(block, tokensBeforeSemicolon.get(0).getFileName(),
                            tokensBeforeSemicolon.get(0).getLine());
                } else if (Recognizer.isForStatement(tokensBeforeSemicolon)) {
                    block = AdderToBlock.addForStatement(block, tokensBeforeSemicolon);
                } else if (Recognizer.isNamespaceDeclaration(tokensBeforeSemicolon)) {
                    block = AdderToBlock.addNamespace(block, tokensBeforeSemicolon);
                } else if (Recognizer.isArrowStatement(tokensBeforeSemicolon)) {
                    AdderToBlock.addReturnStatement(block, tokensBeforeSemicolon);
                    block = endAndReturnParent(block,"Arrow statement returns value and ends block," +
                            " but there is nothing to end!", tokens.get(0));
                } else if (Recognizer.isEndKeyword(tokensBeforeSemicolon))
                    block = endAndReturnParent(block,"There is nothing to end!", tokens.get(0));
                else if (Recognizer.isExpression(tokensBeforeSemicolon, 0, tokensBeforeSemicolon.size())) {
                    AdderToBlock.addExpression(block, tokensBeforeSemicolon);
                }
                tokensBeforeSemicolon.clear();
            } else
                tokensBeforeSemicolon.add(token);
        }
        return mainBlock;

    }
}
