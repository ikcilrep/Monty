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

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.StructDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import sml.data.returning.VoidType;

import java.math.BigInteger;

public enum DataTypes {
    BOOLEAN, INTEGER, BIG_INTEGER, FLOAT, NOTHING, OBJECT;
    public final static BigInteger INT_MAX = BigInteger.valueOf(Integer.MAX_VALUE);
    public final static BigInteger INT_MIN = BigInteger.valueOf(Integer.MIN_VALUE);

    public static DataTypes getDataType(Object value) {
        if (value instanceof VoidType)
            return NOTHING;
        if (value instanceof BigInteger)
            return BIG_INTEGER;
        if (value instanceof Integer)
            return INTEGER;
        if (value instanceof Double)
            return FLOAT;
        if (value instanceof Boolean)
            return BOOLEAN;
        if (value instanceof Block || value instanceof FunctionDeclarationNode)
            return OBJECT;
        if (value instanceof VariableDeclarationNode)
            return getDataType(((VariableDeclarationNode) value).getValue());
        return null;

    }

    public static int getAndCheckSmallInteger(Object _index, String nameOfNumber, String fileName, int line) {
        if (_index instanceof Integer)
            return (int) _index;
        if (_index instanceof BigInteger) {
            var bigIndex = (BigInteger) _index;
            if (bigIndex.compareTo(DataTypes.INT_MAX) > 0)
                new LogError(nameOfNumber + " have to be less or equals 2^31-1.", fileName, line);
            if (bigIndex.compareTo(DataTypes.INT_MIN) < 0)
                new LogError(nameOfNumber + " have to be greater or equals -2^31.", fileName, line);
            return bigIndex.intValue();
        }
        new LogError(nameOfNumber + " have to be integer.", fileName, line);
        return -1;
    }

}
