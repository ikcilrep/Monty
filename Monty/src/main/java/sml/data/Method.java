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

package sml.data;

import ast.Block;
import ast.declarations.TypeDeclarationNode;
import sml.NativeFunctionDeclarationNode;

public abstract class Method<T extends TypeDeclarationNode> extends NativeFunctionDeclarationNode {
    protected final T parent;

    protected Method(T parent, String name, String[] parameters) {
        super(name, parameters);
        setBody(new Block(parent));
        this.parent = parent;
        parent.addFunction(this);
    }

    @Override
    public Method<T> copy() {
        return null;
    }
}
