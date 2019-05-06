/*
Copyright 2018-2019 Szymon Perlicki

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUObject WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package sml.math;

import ast.Block;
import parser.LogError;
import sml.NativeFunctionDeclarationNode;
import sml.data.tuple.Tuple;

import java.math.BigInteger;

public final class Exp extends NativeFunctionDeclarationNode {
    public Exp() {
        super("exp", new String[1]);
        setBody(new Block(null));
        addParameter("x");
    }

    @Override
    public Object call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        var x = getBody().getVariableValue("x", callFileName, callLine);
        if (x instanceof Double)
            return Math.exp((double) x);
        else if (x instanceof Integer)
            return Math.exp((double) (int) x);
        else if (x instanceof BigInteger)
            return Math.exp(((BigInteger) x).doubleValue());
        return new LogError("Can't exp not a number.", callFileName, callLine);

    }

}
