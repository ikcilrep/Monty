package parser.parsing;

import java.util.ArrayList;
import java.util.List;

import ast.Block;
import ast.Node;
import lexer.MontyToken;
import lexer.TokenTypes;
import parser.AdderToBlock;
import parser.Identificator;

public class Parser {

	public static Node parse(List<MontyToken> tokens) {
		var tokensBeforeSemicolon = new ArrayList<MontyToken>();
		var block = new Block(null);
		for (MontyToken token : tokens) {
			if (token.getType().equals(TokenTypes.SEMICOLON)) {
				if (Identificator.isExpression(tokensBeforeSemicolon)) {
					System.out.println("EXPRESSION!");
					AdderToBlock.addExpression(block, tokensBeforeSemicolon);
				} else if (Identificator.isPrintStatement(tokensBeforeSemicolon)) {
					System.out.println("PRINT STATEMENT!");
					AdderToBlock.addPrintStatement(block, tokensBeforeSemicolon);
				} else if (Identificator.isVariableDeclaration(tokensBeforeSemicolon)) {
					System.out.println("VARIABLE DECLARATION!");
					AdderToBlock.addVariableDeclaration(block, tokens);
				} else if (Identificator.isReturnStatement(tokensBeforeSemicolon)) {
					System.out.println("RETURN STATEMENT!");
					AdderToBlock.addReturnStatement(block, tokensBeforeSemicolon);
				}
				tokensBeforeSemicolon.clear();
			} else
				tokensBeforeSemicolon.add(token);
		}
		return null;

	}
}
