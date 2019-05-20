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
import ast.declarations.StructDeclarationNode;
import sml.NativeFunctionDeclarationNode;
import sml.data.string.MontyString;
import sml.data.tuple.Tuple;

public final class ToString extends NativeFunctionDeclarationNode {

    public ToString() {
        super("toString", new String[1]);
        setBody(new Block(null));
        addParameter("toBeCasted");
    }

    public static MontyString toString(Object toBeCasted, String fileName, int line) {
        if (toBeCasted instanceof StructDeclarationNode)
            return ((StructDeclarationNode) toBeCasted).toString(fileName, line);
        return new MontyString(toBeCasted.toString());
    }

    @Override
    public MontyString call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        return toString(body.getVariableValue("toBeCasted", callFileName, callLine), callFileName, callLine);
    }
}
