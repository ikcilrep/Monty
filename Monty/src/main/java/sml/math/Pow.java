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

import parser.DataTypes;
import parser.LogError;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public final class Pow {

    private static Object bigMultiply(BigInteger a, Object b) {
        if (b instanceof Double)
            return BigDecimal.valueOf((double) b).multiply(new BigDecimal(a)).doubleValue();
        return a.multiply((BigInteger) b);
    }

    private static Object bigPow(BigInteger basis, int index) {
        if (index < 0)
            return BigDecimal.ONE.divide(new BigDecimal(basis.pow(-index)), RoundingMode.HALF_UP).doubleValue();
        return basis.pow(index);
    }

    public static Object powerForFloats(double basis, double _index, String callFileName, int callLine) {
        return Math.pow(basis, _index);
    }

    public static Object powerForIntegers(Object basis, Object _index, String callFileName, int callLine) {
        int index = 0;
        if (_index instanceof Integer)
            index = (int) _index;
        else if (_index instanceof BigInteger) {
            var bigIndex = (BigInteger) _index;
            if (bigIndex.compareTo(DataTypes.INT_MAX) > 0)
                new LogError("Index have to be less or equals 2^31-1.", callFileName, callLine);
            else if (bigIndex.compareTo(DataTypes.INT_MIN) < 0)
                new LogError("Index have to be greater or equals -2^31.", callFileName, callLine);

            index = bigIndex.intValue();
        }
        if (basis instanceof Integer)
            return intPow((int) basis, index);
        else if (basis instanceof BigInteger)
            return bigPow((BigInteger) basis, index);
        return null;
    }

    private static Object intPow(int basis, int index) {
        var result = basis;
        for (int i = 1; i < index; i++)
            try {
                result = Math.multiplyExact(result, basis);
            } catch (ArithmeticException e) {
                return bigMultiply(BigInteger.valueOf(result), bigPow(BigInteger.valueOf(basis), index - i));
            }
        return result;
    }

}
