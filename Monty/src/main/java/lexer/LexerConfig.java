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
package lexer;

import lexer.lexerbuilder.LexerBuilder;

public class LexerConfig {
	public static LexerBuilder<MontyToken> getLexer(String code) {
		var lb = new LexerBuilder<MontyToken>(code);

		lb.setKeyword("var", new MontyToken(TokenTypes.VAR_KEYWORD));
		lb.setKeyword("if", new MontyToken(TokenTypes.IF_KEYWORD));
		lb.setKeyword("else", new MontyToken(TokenTypes.ELSE_KEYWORD));
		lb.setKeyword("end", new MontyToken(TokenTypes.END_KEYWORD));
		lb.setKeyword("func", new MontyToken(TokenTypes.FUNC_KEYWORD));
		lb.setKeyword("return", new MontyToken(TokenTypes.RETURN_KEYWORD));
		lb.setKeyword("print", new MontyToken(TokenTypes.PRINT_KEYWORD));
		lb.setKeyword("println", new MontyToken(TokenTypes.PRINT_KEYWORD));
		lb.setKeyword("int", new MontyToken(TokenTypes.INTEGER_KEYWORD));
		lb.setKeyword("void", new MontyToken(TokenTypes.VOID_KEYWORD));
		lb.setKeyword("float", new MontyToken(TokenTypes.FLOAT_KEYWORD));
		lb.setKeyword("string", new MontyToken(TokenTypes.STRING_KEYWORD));
		lb.setKeyword("boolean", new MontyToken(TokenTypes.BOOLEAN_KEYWORD));
		lb.setKeyword("array", new MontyToken(TokenTypes.ARRAY_KEYWORD));
		lb.setKeyword("any", new MontyToken(TokenTypes.ANY_KEYWORD));
		lb.setKeyword("is", new MontyToken(TokenTypes.IS_KEYWORD));
		lb.setKeyword("while", new MontyToken(TokenTypes.WHILE_KEYWORD));
		lb.setKeyword("import", new MontyToken(TokenTypes.IMPORT_KEYWORD));
		lb.setKeyword("dynamic", new MontyToken(TokenTypes.DYNAMIC_KEYWORD));
		lb.setKeyword("static", new MontyToken(TokenTypes.STATIC_KEYWORD));
		lb.setKeyword("change", new MontyToken(TokenTypes.CHANGE_KEYWORD));
		lb.setKeyword("to", new MontyToken(TokenTypes.TO_KEYWORD));

		lb.setOnIdentifier(new MontyToken(TokenTypes.IDENTIFIER));

		lb.setCommentChar('\'', '`');

		lb.setOnAssignmentOperator(new MontyToken(TokenTypes.OPERATOR));
		lb.setOnBinaryOperator(new MontyToken(TokenTypes.OPERATOR));
		lb.setOnComparisonOperator(new MontyToken(TokenTypes.OPERATOR));
		lb.setOnLogicalOperator(new MontyToken(TokenTypes.OPERATOR));

		lb.setOnComma(new MontyToken(TokenTypes.COMMA));
		lb.setOnDot(new MontyToken(TokenTypes.DOT));
		lb.setOnBracket(new MontyToken(TokenTypes.BRACKET));
		lb.setOnSquareBracket(new MontyToken(TokenTypes.SQUARE_BRACKET));
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
