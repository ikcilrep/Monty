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
import ast.declarations.VariableDeclarationNode;
import parser.LogError;
import sml.NativeFunctionDeclarationNode;
import sml.data.returning.VoidType;
import sml.data.string.MontyString;
import sml.data.tuple.Tuple;

import java.math.BigDecimal;
import java.math.BigInteger;

public final class ToInt extends NativeFunctionDeclarationNode {

    public ToInt() {
        super("toInt", new String[1]);
        setBody(new Block(null));
        addParameter("toBeCasted");
    }

    private static Object toInt(Object toBeCasted, String callFileName, int callLine) {
        if (toBeCasted instanceof VoidType)
            new LogError("Can't cast void to integer", callFileName, callLine);
        if (toBeCasted instanceof BigInteger || toBeCasted instanceof Integer)
            return toBeCasted;
        if (toBeCasted instanceof Double)
            return fromFloat((double) toBeCasted);
        if (toBeCasted instanceof Boolean)
            return fromBoolean((Boolean) toBeCasted);
        if (toBeCasted instanceof MontyString)
            return fromString((MontyString) toBeCasted, callFileName, callLine);
        else
            new LogError("Can't cast type to integer", callFileName, callLine);
        return null;
    }

    private static Object fromString(MontyString str, String fileName, int line) {
        try {
            return Integer.parseInt(str.getString());
        } catch (NumberFormatException e1) {
            try {
                return new BigInteger(str.getString());
            } catch (NumberFormatException e2) {
                new LogError("Wrong format for integer ", fileName, line);
            }
        }
        return null;
    }

    private static Object fromFloat(Double floating) {
        try {
            return Integer.parseInt(((Integer) floating.intValue()).toString());
        } catch (ArithmeticException e) {
            return new BigDecimal(floating).toBigInteger();
        }
    }

    private static int fromBoolean(Boolean bool) {
        if (bool)
            return 1;
        return 0;
    }

    public static BigInteger fromSmallInt(int integer) {
        return BigInteger.valueOf(integer);
    }

    public static void fromSmallIntVariable(VariableDeclarationNode variable, String fileName, int line) {
        variable.setValue(fromSmallInt((int) variable.getValue()), fileName, line);
    }

    @Override
    public Object call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        var toBeCasted = body.getVariableValue("toBeCasted", callFileName, callLine);
        return toInt(toBeCasted, callFileName, callLine);
    }
}
