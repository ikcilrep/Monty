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

import ast.expressions.OperationNode;
import sml.data.Method;
import sml.data.tuple.Tuple;

import java.util.ArrayList;

final class Replace extends Method<StringStruct> {

    Replace(StringStruct parent) {
        super(parent, "replace",new String[2]);
        addParameter("regex");
        addParameter("replacement");

    }

    @Override
    public String call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        var body = getBody();
        return parent.getString().replaceAll(body.getStringVariableValue("regex", callFileName, callLine),
                body.getStringVariableValue("replacement", callFileName, callLine));
    }

}
