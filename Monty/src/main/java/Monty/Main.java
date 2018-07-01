package Monty;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import AST.OperationNode;
import AST.VariableDeclarationNode;
import AST.VariableNode;
import LanguageBuilder.LexerBuilder;
import Lexer.MontyToken;
import Lexer.TokenTypes;
import Parser.DataTypes;

public class Main {
	public static void main(String[] args) throws FileNotFoundException {
		BufferedReader br = null;
		var code = new StringBuilder();
		try {
			br = new BufferedReader(new FileReader("/home/ikcilrep/sample.mt"));
			String line;
			while ((line = br.readLine()) != null) {
				code.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		var lb = new LexerBuilder<MontyToken>(code.toString());
		lb.setCommentChar('#', '\n');
		lb.setKeyword("var", new MontyToken(TokenTypes.VAR_KEYWORD));
		lb.setKeyword("int", new MontyToken(TokenTypes.INTEGER_KEYWORD));
		lb.setKeyword("float", new MontyToken(TokenTypes.FLOAT_KEYWORD));
		lb.setKeyword("string", new MontyToken(TokenTypes.STRING_KEYWORD));
		lb.setKeyword("boolean", new MontyToken(TokenTypes.BOOLEAN_KEYWORD));
		lb.setOnAssignmentOperator(new MontyToken(TokenTypes.OPERATOR));
		lb.setOnBinaryOperator(new MontyToken(TokenTypes.OPERATOR));
		lb.setOnComparisonOperator(new MontyToken(TokenTypes.OPERATOR));
		lb.setOnLogicalOperator(new MontyToken(TokenTypes.OPERATOR));
		lb.setOnComma(new MontyToken(TokenTypes.COMMA));
		lb.setOnIdentifier(new MontyToken(TokenTypes.IDENTIFIER));

		lb.setOnFloatLiteral(new MontyToken(TokenTypes.FLOAT_LITERAL));
		lb.setOnIntegerLiteral(new MontyToken(TokenTypes.INTEGER_LITERAL));
		lb.setOnStringLiteral(new MontyToken(TokenTypes.STRING_LITERAL));
		lb.setKeyword("true", new MontyToken(TokenTypes.BOOLEAN_LITERAL));
		lb.setKeyword("false", new MontyToken(TokenTypes.BOOLEAN_LITERAL));

		List<MontyToken> tokens = lb.getAllTokens();
		System.out.println(0 % 2);
		var on = (VariableDeclarationNode) (Parser.VariableDeclarationParser.parse(tokens.subList(1, tokens.size())));
		System.out.println("Name: " + on.getName() + "\nType: " + on.getType());
	}
}
