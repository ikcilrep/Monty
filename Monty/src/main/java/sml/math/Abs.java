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

package sml.math;

import ast.Block;
import parser.LogError;
import sml.NativeFunctionDeclarationNode;
import sml.data.tuple.Tuple;

import java.math.BigInteger;

public final class Abs extends NativeFunctionDeclarationNode {

    public Abs() {
        super("abs", new String[1]);
        setBody(new Block(null));
        addParameter("f");
    }

    @Override
    public Object call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        var f = body.getVariableValue("f", callFileName, callLine);
        if (f instanceof Double)
            return Math.abs((double) f);
        else if (f instanceof BigInteger)
            return ((BigInteger) f).abs();
        else if (f instanceof Integer)
            return Math.abs((int) f);
        new LogError("Can't calculate absolute value of not a number.", callFileName, callLine);
        return null;
    }

}
