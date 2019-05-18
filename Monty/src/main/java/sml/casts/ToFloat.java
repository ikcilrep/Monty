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

import java.math.BigInteger;

public final class ToFloat extends NativeFunctionDeclarationNode {

    public ToFloat() {
        super("toFloat", new String[1]);
        setBody(new Block(null));
        addParameter("toBeCasted");
    }

    private static Double toFloat(Object toBeCasted, String callFileName, int callLine) {
        if (toBeCasted instanceof VoidType)
            new LogError("Can't cast void to real", callFileName, callLine);
        if (toBeCasted instanceof BigInteger)
            return fromInt((BigInteger) toBeCasted);
        if (toBeCasted instanceof Integer)
            return fromInt((int) toBeCasted);
        if (toBeCasted instanceof Boolean)
            return fromBoolean((Boolean) toBeCasted);
        if (toBeCasted instanceof Double)
            return (double) toBeCasted;
        if (toBeCasted instanceof MontyString)
            return fromString((MontyString) toBeCasted, callFileName, callLine);
        else
            new LogError("Can't cast structure to real", callFileName, callLine);
        return null;
    }

    public static void fromSmallIntVariable(VariableDeclarationNode variable, String fileName, int line) {
        variable.setValue(fromInt((int) variable.getValue()), fileName, line);
    }

    public static void fromBigIntVariable(VariableDeclarationNode variable, String fileName, int line) {
        variable.setValue(fromInt((BigInteger) variable.getValue()), fileName, line);
    }

    public static double fromInt(int integer) {
        return (double) integer;
    }

    public static double fromInt(BigInteger integer) {
        return integer.doubleValue();
    }

    private static double fromBoolean(Boolean bool) {
        if (bool)
            return 1;
        return 0;
    }

    private static Double fromString(MontyString str, String fileName, int line) {
        try {
            return Double.parseDouble(str.getString());
        } catch (NumberFormatException e) {
            new LogError("Unknown number format for float type:\t" + str, fileName, line);
        }
        return null;
    }

    @Override
    public Double call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        var toBeCasted = body.getVariableValue("toBeCasted", callFileName, callLine);
        return toFloat(toBeCasted, callFileName, callLine);
    }

}
