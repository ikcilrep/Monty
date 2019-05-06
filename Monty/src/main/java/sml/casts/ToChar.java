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

package sml.casts;

import ast.Block;
import parser.DataTypes;
import parser.LogError;
import sml.NativeFunctionDeclarationNode;
import sml.data.tuple.Tuple;

import java.math.BigInteger;

public final class ToChar extends NativeFunctionDeclarationNode {
    public ToChar() {
        super("toChar", new String[1]);
        setBody(new Block(null));
        addParameter("integer");
    }

    @Override
    public String call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        var integer = getBody().getVariableValue("integer", callFileName, callLine);
        if (integer instanceof Integer)
            return String.valueOf(Character.valueOf((char) (int) integer));
        else if (integer instanceof BigInteger) {
            var bigInteger = (BigInteger) integer;
            if (bigInteger.compareTo(DataTypes.INT_MAX) > 0)
                new LogError("Char number have to be less or equals 2^31-1.", callFileName, callLine);
            else if (bigInteger.compareTo(DataTypes.INT_MIN) < 0)
                new LogError("Char number have to be greater or equals -2^31.", callFileName, callLine);

            return String.valueOf(Character.valueOf((char) (int) bigInteger.intValue()));
        }
        new LogError("Can't change not a number to char.", callFileName, callLine);
        return null;
    }

}
