package lexer;

import lexer.lexerbuilder.LexerBuilder;

public class LexerConfig {
	public static LexerBuilder<MontyToken> getLexer(String code) {
		var lb = new LexerBuilder<MontyToken>(code);
		lb.setCommentChar('\'', '`');
		lb.setKeyword("var", new MontyToken(TokenTypes.VAR_KEYWORD));
		lb.setKeyword("end", new MontyToken(TokenTypes.END_KEYWORD));
		lb.setKeyword("func", new MontyToken(TokenTypes.FUNC_KEYWORD));
		lb.setKeyword("return", new MontyToken(TokenTypes.RETURN_KEYWORD));
		lb.setKeyword("print", new MontyToken(TokenTypes.PRINT_KEYWORD));
		lb.setKeyword("int", new MontyToken(TokenTypes.INTEGER_KEYWORD));
		lb.setKeyword("float", new MontyToken(TokenTypes.FLOAT_KEYWORD));
		lb.setKeyword("string", new MontyToken(TokenTypes.STRING_KEYWORD));
		lb.setKeyword("boolean", new MontyToken(TokenTypes.BOOLEAN_KEYWORD));
		lb.setOnAssignmentOperator(new MontyToken(TokenTypes.OPERATOR));
		lb.setOnBinaryOperator(new MontyToken(TokenTypes.OPERATOR));
		lb.setOnComparisonOperator(new MontyToken(TokenTypes.OPERATOR));
		lb.setOnLogicalOperator(new MontyToken(TokenTypes.OPERATOR));
		lb.setOnComma(new MontyToken(TokenTypes.COMMA));
		lb.setOnBracket(new MontyToken(TokenTypes.BRACKET));
		lb.setOnIdentifier(new MontyToken(TokenTypes.IDENTIFIER));
		lb.setOnSemicolon(new MontyToken(TokenTypes.SEMICOLON));
		lb.setOnFloatLiteral(new MontyToken(TokenTypes.FLOAT_LITERAL));
		lb.setOnIntegerLiteral(new MontyToken(TokenTypes.INTEGER_LITERAL));
		lb.setOnStringLiteral(new MontyToken(TokenTypes.STRING_LITERAL));
		lb.setKeyword("true", new MontyToken(TokenTypes.BOOLEAN_LITERAL));
		lb.setKeyword("false", new MontyToken(TokenTypes.BOOLEAN_LITERAL));
		lb.setOnIdentifier(new MontyToken(TokenTypes.IDENTIFIER));
		return lb;
	}
}
