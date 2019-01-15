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

package parser;

import lexer.Token;
import lexer.TokenTypes;
import sml.data.array.Array;

public class Tokens {
	public static DataTypes getDataType(TokenTypes type) {
		switch (type) {
		case INTEGER_LITERAL:
		case INT_KEYWORD:
			return DataTypes.INTEGER;
		case FLOAT_LITERAL:
		case FLOAT_KEYWORD:
			return DataTypes.FLOAT;
		case BOOLEAN_LITERAL:
		case BOOLEAN_KEYWORD:
			return DataTypes.BOOLEAN;
		case STRING_LITERAL:
		case STRING_KEYWORD:
			return DataTypes.STRING;
		case ARRAY_KEYWORD:
			return DataTypes.ARRAY;
		case STACK_KEYWORD:
			return DataTypes.STACK;
		case LIST_KEYWORD:
			return DataTypes.LIST;
		case ANY_KEYWORD:
			return DataTypes.ANY;
		case VOID_KEYWORD:
			return DataTypes.VOID;
		default:
			return null;
		}
	}

	public static String getText(Array<Token> array) {
		var result = new StringBuilder();
		Token next = null;
		int i = 0;
		for (Token token : array) {
			result.append(token.getText());
			if (i + 1 < array.length())
				next = array.get(i + 1);
			if (!(token.getType().equals(TokenTypes.BRACKET) || token.getType().equals(TokenTypes.COMMA)
					|| token.getType().equals(TokenTypes.DOT) || next == null
					|| next.getType().equals(TokenTypes.BRACKET) || next.getType().equals(TokenTypes.COMMA)
					|| next.getType().equals(TokenTypes.DOT)))
				result.append(' ');
			i++;
			next = null;
		}
		return result.toString();
	}

	public static String getTypesToString(Array<Token> array) {
		var result = new StringBuilder();
		for (Token token : array) {
			result.append(token.getType());
			result.append(' ');
		}
		return result.toString();
	}
}
