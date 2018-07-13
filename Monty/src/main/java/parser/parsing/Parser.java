package parser.parsing;

import java.util.ArrayList;
import java.util.List;
import ast.Node;
import lexer.MontyToken;
import lexer.TokenTypes;
import parser.Identificator;
import parser.Tokens;

public class Parser {

	public static Node parse(List<MontyToken> tokens) {
		var tokensBeforeSemicolon = new ArrayList<MontyToken>();
		for (MontyToken token : tokens) {
			if (token.getType().equals(TokenTypes.SEMICOLON)) {
				if (Identificator.isExpression(tokensBeforeSemicolon))
					System.out.println("EXPRESSION!");
				else if (Identificator.isPrintStatement(tokensBeforeSemicolon))
					System.out.println("PRINT STATEMENT!");
				else if (Identificator.isReturnStatement(tokensBeforeSemicolon))
					System.out.println("RETURN STATEMENT!");
				tokensBeforeSemicolon.clear();
			} else
				tokensBeforeSemicolon.add(token);
		}
		return null;

	}
}
