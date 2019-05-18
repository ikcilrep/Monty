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
import parser.LogError;
import sml.NativeFunctionDeclarationNode;
import sml.data.returning.VoidType;
import sml.data.string.MontyString;
import sml.data.tuple.Tuple;

import java.math.BigInteger;

public final class ToBoolean extends NativeFunctionDeclarationNode {

    public ToBoolean() {
        super("toBoolean", new String[1]);
        setBody(new Block(null));
        addParameter("toBeCasted");
    }

    public static Boolean toBoolean(Object toBeCasted, String callFileName, int callLine) {
        if (toBeCasted instanceof VoidType)
            new LogError("Can't cast void to boolean", callFileName, callLine);
        if (toBeCasted instanceof BigInteger)
            return fromInt((BigInteger) toBeCasted);
        if (toBeCasted instanceof Integer)
            return fromInt((int) toBeCasted);
        if (toBeCasted instanceof Double)
            return fromFloat((double) toBeCasted);
        if (toBeCasted instanceof Boolean)
            return (boolean) toBeCasted;
        if (toBeCasted instanceof MontyString)
            return fromString((MontyString) toBeCasted);
        else
            new LogError("Can't cast structure to boolean", callFileName, callLine);
        return null;
    }

    private static Boolean fromInt(BigInteger integer) {
        return integer.compareTo(BigInteger.valueOf(0)) > 0;
    }

    private static Boolean fromInt(int integer) {
        return integer > 0;
    }

    private static Boolean fromFloat(double floating) {
        return floating > 0;
    }

    private static Boolean fromString(MontyString str) {
        return Boolean.parseBoolean(str.getString());
    }

    @Override
    public Boolean call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        var toBeCasted = body.getVariableValue("toBeCasted", callFileName, callLine);
        return toBoolean(toBeCasted, callFileName, callLine);
    }
}
