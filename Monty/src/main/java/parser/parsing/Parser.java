package parser.parsing;

/*
Copyright 2018 Szymon Perlicki

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ast.Block;
import lexer.MontyToken;
import lexer.TokenTypes;
import monty.Importing;
import monty.Library;
import parser.Identificator;
import parser.MontyException;

public class Parser {
	public static HashMap<String, Library> libraries;

	public static Block parse(List<MontyToken> tokens) {
		var tokensBeforeSemicolon = new ArrayList<MontyToken>();
		var block = new Block(null);
		for (MontyToken token : tokens) {
			if (token.getType().equals(TokenTypes.SEMICOLON)) {
				if (tokensBeforeSemicolon.size() == 0)
					continue;
				if (Identificator.isExpression(tokensBeforeSemicolon)) {
					AdderToBlock.addExpression(block, tokensBeforeSemicolon);
				} else if (Identificator.isVariableDeclaration(tokensBeforeSemicolon)) {
					AdderToBlock.addVariableDeclaration(block, tokensBeforeSemicolon);
				} else if (Identificator.isReturnStatement(tokensBeforeSemicolon)) {
					AdderToBlock.addReturnStatement(block, tokensBeforeSemicolon);
				} else if (Identificator.isFunctionDeclaration(tokensBeforeSemicolon)) {
					block = AdderToBlock.addFunctionDeclaration(block, tokensBeforeSemicolon);
				} else if (Identificator.isIfStatement(tokensBeforeSemicolon)) {
					block = AdderToBlock.addIfStatement(block, tokensBeforeSemicolon);
				} else if (Identificator.isElseStatement(tokensBeforeSemicolon)) {
					block = AdderToBlock.addElseStatement(block, tokensBeforeSemicolon);
					if (tokensBeforeSemicolon.size() > 1)
						block = AdderToBlock.addIfStatement(block,
								tokensBeforeSemicolon.subList(1, tokensBeforeSemicolon.size()));
				} else if (Identificator.isWhileStatement(tokensBeforeSemicolon)) {
					block = AdderToBlock.addWhileStatement(block, tokensBeforeSemicolon);
				} else if (Identificator.isImport(tokensBeforeSemicolon)) {
					Importing.importFile(block, tokensBeforeSemicolon);
				} else if (Identificator.isChangeToStatement(tokensBeforeSemicolon)) {
					AdderToBlock.addChangeToStatement(block, tokensBeforeSemicolon);
				} else if (Identificator.isEndKeyword(tokensBeforeSemicolon)) {
					var parent = block.getParent();
					if (parent == null)
						new MontyException("Nothing to end!");
					block = block.getParent();
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
