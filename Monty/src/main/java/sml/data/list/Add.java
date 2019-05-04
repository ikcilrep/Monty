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

package sml.data.list;

import ast.expressions.OperationNode;
import sml.data.Method;
import sml.data.tuple.Tuple;

import java.util.ArrayList;

final class Add extends Method<List> {

    Add(List array) {
        super(array, "add");
        addParameter("other");
    }

    @Override
    public List call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        return parent.add(getBody().getVariable("other", callFileName, callLine).getValue());
    }

}
