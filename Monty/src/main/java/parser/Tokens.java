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

import lexer.OptimizedTokensArray;
import lexer.Token;
import lexer.TokenTypes;

public class Tokens {
	public static DataTypes getDataType(TokenTypes type) {
		switch (type) {
		case INTEGER_LITERAL:
		case INT_KEYWORD:
			return DataTypes.INTEGER;
		case REAL_LITERAL:
		case REAL_KEYWORD:
			return DataTypes.REAL;
		case BOOLEAN_LITERAL:
		case BOOLEAN_KEYWORD:
			return DataTypes.BOOLEAN;
		case STRING_LITERAL:
		case STRING_KEYWORD:
			return DataTypes.STRING;
		case ANY_KEYWORD:
			return DataTypes.ANY;
		case VOID_KEYWORD:
			return DataTypes.VOID;
		default:
			return null;
		}
	}

	
	public static String getText(OptimizedTokensArray array) {
		var result = new StringBuilder();
		for (Token token : array)
			if (token.getType().equals(TokenTypes.DOT))
				result.append(".");
			else
				result.append(token.getText());
		return result.toString();
	}

	public static String getTypesToString(OptimizedTokensArray array) {
		var result = new StringBuilder();
		for (Token token : array) {
			result.append(token.getType());
			result.append(' ');
		}
		return result.toString();
	}
}
