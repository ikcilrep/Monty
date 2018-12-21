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
	public static LexerBuilder getLexer(String code, String fileName) {
		var lb = new LexerBuilder(code, fileName);

		lb.setKeyword("if", new Token(TokenTypes.IF_KEYWORD));
		lb.setKeyword("else", new Token(TokenTypes.ELSE_KEYWORD));
		lb.setKeyword("end", new Token(TokenTypes.END_KEYWORD));
		lb.setKeyword("func", new Token(TokenTypes.FUNC_KEYWORD));
		lb.setKeyword("return", new Token(TokenTypes.RETURN_KEYWORD));
		lb.setKeyword("int", new Token(TokenTypes.INTEGER_KEYWORD));
		lb.setKeyword("void", new Token(TokenTypes.VOID_KEYWORD));
		lb.setKeyword("float", new Token(TokenTypes.FLOAT_KEYWORD));
		lb.setKeyword("string", new Token(TokenTypes.STRING_KEYWORD));
		lb.setKeyword("boolean", new Token(TokenTypes.BOOLEAN_KEYWORD));
		lb.setKeyword("array", new Token(TokenTypes.ARRAY_KEYWORD));
		lb.setKeyword("list", new Token(TokenTypes.LIST_KEYWORD));
		lb.setKeyword("stack", new Token(TokenTypes.STACK_KEYWORD));
		lb.setKeyword("any", new Token(TokenTypes.ANY_KEYWORD));
		lb.setKeyword("is", new Token(TokenTypes.IS_KEYWORD));
		lb.setKeyword("while", new Token(TokenTypes.WHILE_KEYWORD));
		lb.setKeyword("for", new Token(TokenTypes.FOR_KEYWORD));
		lb.setKeyword("in", new Token(TokenTypes.IN_KEYWORD));
		lb.setKeyword("break", new Token(TokenTypes.BREAK_KEYWORD));
		lb.setKeyword("continue", new Token(TokenTypes.CONTINUE_KEYWORD));

		lb.setKeyword("import", new Token(TokenTypes.IMPORT_KEYWORD));
		lb.setKeyword("dynamic", new Token(TokenTypes.DYNAMIC_KEYWORD));
		lb.setKeyword("static", new Token(TokenTypes.STATIC_KEYWORD));
		lb.setKeyword("change", new Token(TokenTypes.CHANGE_KEYWORD));
		lb.setKeyword("do", new Token(TokenTypes.DO_KEYWORD));

		lb.setKeyword("to", new Token(TokenTypes.TO_KEYWORD));
		lb.setKeyword("thread", new Token(TokenTypes.THREAD_KEYWORD));

		lb.setOnIdentifier(new Token(TokenTypes.IDENTIFIER));

		lb.setCommentChar('\'', '`');

		lb.setOnAssignmentOperator(new Token(TokenTypes.OPERATOR));
		lb.setOnBinaryOperator(new Token(TokenTypes.OPERATOR));
		lb.setOnComparisonOperator(new Token(TokenTypes.OPERATOR));
		lb.setOnLogicalOperator(new Token(TokenTypes.OPERATOR));

		lb.setOnComma(new Token(TokenTypes.COMMA));
		lb.setOnDot(new Token(TokenTypes.DOT));
		lb.setOnBracket(new Token(TokenTypes.BRACKET));
		lb.setOnSemicolon(new Token(TokenTypes.SEMICOLON));

		lb.setOnFloatLiteral(new Token(TokenTypes.FLOAT_LITERAL));
		lb.setOnIntegerLiteral(new Token(TokenTypes.INTEGER_LITERAL));
		lb.setOnStringLiteral(new Token(TokenTypes.STRING_LITERAL));
		lb.setKeyword("true", new Token(TokenTypes.BOOLEAN_LITERAL));
		lb.setKeyword("false", new Token(TokenTypes.BOOLEAN_LITERAL));
		lb.setOnIdentifier(new Token(TokenTypes.IDENTIFIER));
		return lb;
	}
}
