package parser.parsing;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ast.Block;
import lexer.LexerConfig;
import lexer.MontyToken;
import lexer.TokenTypes;
import monty.FileIO;
import monty.Main;
import parser.Identificator;
import parser.MontyException;
import parser.Tokens;

public class Parser {

	public static Block parse(List<MontyToken> tokens) {
		var tokensBeforeSemicolon = new ArrayList<MontyToken>();
		var block = new Block(null);
		for (MontyToken token : tokens) {
			if (token.getType().equals(TokenTypes.SEMICOLON)) {
				if (tokensBeforeSemicolon.size() == 0)
					continue;
				if (Identificator.isExpression(tokensBeforeSemicolon)) {
					AdderToBlock.addExpression(block, tokensBeforeSemicolon);
				} else if (Identificator.isPrintStatement(tokensBeforeSemicolon)) {
					AdderToBlock.addPrintStatement(block, tokensBeforeSemicolon);
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
					var partOfPath = Tokens.getText(tokensBeforeSemicolon.subList(1, tokensBeforeSemicolon.size()));
					var path = new File(Main.path).getParent() + File.separatorChar
							+ partOfPath.substring(0, partOfPath.length() - 1).replace('.', File.separatorChar) + ".mt";
					var text = FileIO.readFile(new File(path).getAbsolutePath());
					var importedTokens = LexerConfig.getLexer(text).getAllTokens();
					var parsed = Parser.parse(importedTokens);
					block.concat(parsed);
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
		AdderToBlock.addStdlib(block);
		return block;

	}
}
