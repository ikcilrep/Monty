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

import java.util.List;

public class Tokens {
    public static DataTypes getDataType(TokenTypes type) {
        switch (type) {
            case INTEGER_LITERAL:
                return DataTypes.INTEGER;
            case REAL_LITERAL:
                return DataTypes.FLOAT;
            case BOOLEAN_LITERAL:
                return DataTypes.BOOLEAN;
            case STRING_LITERAL:
                return DataTypes.OBJECT;
            default:
                return null;
        }
    }

    public static String getText(List<Token> array) {
        var result = new StringBuilder();
        for (Token token : array)
            result.append(token.getText());
        return result.toString();
    }

    static String getTypesToString(List<Token> array) {
        var result = new StringBuilder();
        for (Token token : array) {
            if (token.getType().equals(TokenTypes.OPERATOR) && token.getText().equals("."))
                result.append("DOT");
            else
                result.append(token.getType());

            result.append(' ');
        }
        return result.toString();
    }
}
