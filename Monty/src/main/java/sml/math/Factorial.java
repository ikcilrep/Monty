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
import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;
import parser.LogError;
import sml.data.StaticStruct;
import sml.data.tuple.Tuple;

import java.math.BigInteger;
import java.util.ArrayList;

public final class Factorial extends FunctionDeclarationNode {
    private static final int[] PRECOMPUTED = {1, 2, 6, 24, 120, 720, 5040, 40320, 362880, 3628800, 39916800,
            479001600};
    private static final BigInteger PRECOMPUTED_LAST = BigInteger.valueOf(PRECOMPUTED[PRECOMPUTED.length - 1]);
    private static final BigInteger PRECOMPUTED_LAST_N = BigInteger.valueOf(PRECOMPUTED.length + 1);
    private static final int MAX = 12;

    public Factorial() {
        super("factorial");
        setBody(new Block(null));
        addParameter("n");
    }

    @Override
    public Object call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        var n = getBody().getVariableValue("n", callFileName, callLine);
        if (n instanceof BigInteger) {
            try {
                n = ((BigInteger) n).intValueExact();
            } catch (ArithmeticException ignored) {
            }
        }
        if (n instanceof Integer) {
            var integer = (int) n;
            if (integer < 0)
                new LogError("Factorial can only be calculated with positive n.");
            if (integer < MAX)
                return PRECOMPUTED[integer - 1];
            var bigInt = BigInteger.valueOf(integer);
            var result = PRECOMPUTED_LAST;
            for (var i = PRECOMPUTED_LAST_N; i.compareTo(bigInt) <= 0; i = i.add(BigInteger.ONE))
                result = result.multiply(i);
            return result;
        }
        new LogError("This function can calculate factorial only for positive integers smaller than 2^31-1.", callFileName,
                callLine);
        return null;
    }

}
