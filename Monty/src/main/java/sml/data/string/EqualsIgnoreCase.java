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

import sml.data.Method;
import sml.data.tuple.Tuple;

final class EqualsIgnoreCase extends Method<StringStruct> {

    EqualsIgnoreCase(StringStruct parent) {
        super(parent, "equalsIgnoreCase",new String[1]);
        addParameter("toCompare");
    }

    @Override
    public Boolean call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        return parent.getString().equalsIgnoreCase(getBody().getStringVariableValue("toCompare", callFileName, callLine).toString());
    }

}
