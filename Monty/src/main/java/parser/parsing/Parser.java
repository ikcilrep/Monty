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

import java.util.HashMap;

import ast.Block;
import ast.NodeTypes;
import ast.statements.IfStatementNode;
import lexer.OptimizedTokensArray;
import lexer.Token;
import lexer.TokenTypes;
import monty.Importing;
import monty.Library;
import parser.Identificator;
import parser.LogError;

public class Parser {
	public static HashMap<String, Library> libraries = new HashMap<>();

	public static Block parse(OptimizedTokensArray tokens) {
		var tokensBeforeSemicolon = new OptimizedTokensArray();
		var block = new Block(null);
		for (Token token : tokens) {
			if (token.getType().equals(TokenTypes.SEMICOLON)) {
				if (tokensBeforeSemicolon.length() == 0)
					continue;
				if (Identificator.isExpression(tokensBeforeSemicolon)) {
					AdderToBlock.addExpression(block, tokensBeforeSemicolon);
				} else if (Identificator.isVariableDeclaration(tokensBeforeSemicolon)) {
					AdderToBlock.addVariableDeclaration(block, tokensBeforeSemicolon);
				} else if (Identificator.isReturnStatement(tokensBeforeSemicolon)) {
					AdderToBlock.addReturnStatement(block, tokensBeforeSemicolon);
				} else if (Identificator.isFunctionDeclaration(tokensBeforeSemicolon)) {
					block = AdderToBlock.addFunctionDeclaration(block, tokensBeforeSemicolon);
				} else if (Identificator.isStructDeclaration(tokensBeforeSemicolon)) {
					block = AdderToBlock.addStructDeclaration(block, tokensBeforeSemicolon);
				} else if (Identificator.isIfStatement(tokensBeforeSemicolon)) {
					block = AdderToBlock.addIfStatement(block, tokensBeforeSemicolon);
				} else if (Identificator.isElseStatement(tokensBeforeSemicolon)) {
					block = AdderToBlock.addElseStatement(block, tokensBeforeSemicolon);
					if (tokensBeforeSemicolon.length() > 1) {
						block = AdderToBlock.addIfStatement(block,
								tokensBeforeSemicolon.subarray(1, tokensBeforeSemicolon.length()));
						((IfStatementNode) block).setInElse(true);
					}
				} else if (Identificator.isWhileStatement(tokensBeforeSemicolon)) {
					block = AdderToBlock.addWhileStatement(block, tokensBeforeSemicolon);
				} else if (Identificator.isDoWhileStatement(tokensBeforeSemicolon)) {
					block = AdderToBlock.addDoWhileStatement(block, tokensBeforeSemicolon);
				} else if (Identificator.isImport(tokensBeforeSemicolon)) {
					Importing.importFile(block, tokensBeforeSemicolon);
				} else if (Identificator.isJar(tokensBeforeSemicolon)) {
					Importing.addJarLibrary(tokensBeforeSemicolon);
				} else if (Identificator.isChangeToStatement(tokensBeforeSemicolon)) {
					AdderToBlock.addChangeToStatement(block, tokensBeforeSemicolon);
				} else if (Identificator.isBreakStatement(tokensBeforeSemicolon)) {
					AdderToBlock.addBreakStatement(block, tokensBeforeSemicolon.get(0).getFileName(),
							tokensBeforeSemicolon.get(0).getLine());
				} else if (Identificator.isContinueStatement(tokensBeforeSemicolon)) {
					AdderToBlock.addContinueStatement(block, tokensBeforeSemicolon.get(0).getFileName(),
							tokensBeforeSemicolon.get(0).getLine());
				} else if (Identificator.isForStatement(tokensBeforeSemicolon)) {
					block = AdderToBlock.addForStatement(block, tokensBeforeSemicolon);
				} else if (Identificator.isEndKeyword(tokensBeforeSemicolon)) {
					var parent = block.getParent();
					if (parent == null)
						new LogError("Nothing to end!", tokensBeforeSemicolon.get(0));
					if (block.getNodeType() == NodeTypes.IF_STATEMENT && ((IfStatementNode) block).isInElse()
							&& parent.getNodeType() == NodeTypes.ELSE_BLOCK)
						block = parent;
					block = block.getParent();
				}
				tokensBeforeSemicolon.clear();
			} else
				tokensBeforeSemicolon.append(token);
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
