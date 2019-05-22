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
import monty.Importing;
import parser.LogError;
import parser.Recognizer;

import java.util.ArrayList;

public final class Parser {
    public static Block parse(ArrayList<Token> tokens) {
        var tokensBeforeSemicolon = new ArrayList<Token>();
        var block = new Block(null);
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
                } else if (Recognizer.isImport(tokensBeforeSemicolon)) {
                    Importing.importFile(block, tokensBeforeSemicolon);
                } else if (Recognizer.isBreakStatement(tokensBeforeSemicolon)) {
                    AdderToBlock.addBreakStatement(block, tokensBeforeSemicolon.get(0).getFileName(),
                            tokensBeforeSemicolon.get(0).getLine());
                } else if (Recognizer.isContinueStatement(tokensBeforeSemicolon)) {
                    AdderToBlock.addContinueStatement(block, tokensBeforeSemicolon.get(0).getFileName(),
                            tokensBeforeSemicolon.get(0).getLine());
                } else if (Recognizer.isForStatement(tokensBeforeSemicolon)) {
                    block = AdderToBlock.addForStatement(block, tokensBeforeSemicolon);
                } else if (Recognizer.isEndKeyword(tokensBeforeSemicolon)) {
                    var parent = block.getParent();
                    if (parent == null)
                        new LogError("Nothing to end!", tokensBeforeSemicolon.get(0));
                    block = block.getParent();
                } else if (Recognizer.isExpression(tokensBeforeSemicolon, 0, tokensBeforeSemicolon.size())) {
                    AdderToBlock.addExpression(block, tokensBeforeSemicolon);
                }
                tokensBeforeSemicolon.clear();
            } else
                tokensBeforeSemicolon.add(token);
        }
        while (true) {
            var parent = block.getParent();
            if (parent == null)
                break;
            block = parent;
        }
        return block;

    }
}
