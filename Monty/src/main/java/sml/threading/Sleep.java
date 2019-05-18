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

package sml.threading;

import ast.Block;
import parser.LogError;
import sml.NativeFunctionDeclarationNode;
import sml.data.returning.Nothing;
import sml.data.returning.VoidType;
import sml.data.tuple.Tuple;

import java.math.BigInteger;

public final class Sleep extends NativeFunctionDeclarationNode {

    public Sleep() {
        super("sleep",new String[1]);
        setBody(new Block(null));
        addParameter("millis");
    }

    @Override
    public VoidType call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        var _millis = body.getVariableValue("millis", callFileName, callLine);
        int millis = 0;
        if (_millis instanceof Integer)
            millis = (int) _millis;
        else if (_millis instanceof BigInteger)
            millis = ((BigInteger) _millis).intValue();
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            new LogError("Sleep for " + millis + " was interrupted", callFileName, callLine);
        }
        return Nothing.nothing;
    }

}
