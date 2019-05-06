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

package sml.io;

import ast.Block;
import sml.NativeFunctionDeclarationNode;
import sml.data.returning.Nothing;
import sml.data.returning.VoidType;
import sml.data.tuple.Tuple;

public final class Print extends NativeFunctionDeclarationNode {

    public Print() {
        super("print",new String[1]);
        setBody(new Block(null));
        addParameter("toPrint");
    }

    @Override
    public VoidType call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        System.out.print(getBody().getStringVariableValue("toPrint", callFileName, callLine));
        return Nothing.nothing;
    }

}
