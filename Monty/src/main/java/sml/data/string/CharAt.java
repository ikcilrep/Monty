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
package sml.data.string;

import parser.DataTypes;
import parser.LogError;
import sml.data.Method;
import sml.data.tuple.Tuple;

import java.math.BigInteger;

final class CharAt extends Method<StringStruct> {

    CharAt(StringStruct parent) {
        super(parent, "charAt",new String[1]);
        addParameter("index");
    }

    @Override
    public StringStruct call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        var body = getBody();
        var _index = body.getVariableValue("index", callFileName, callLine);
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
        try {
            return new StringStruct(String.valueOf(parent.getString().charAt(index)));
        } catch (IndexOutOfBoundsException e) {
            new LogError("Index " + index + " out of bounds for length " + parent.getString().length(), callFileName, callLine);
        }
        return null;
    }

}
