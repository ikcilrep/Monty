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

package sml.data.returning;

import ast.declarations.VariableDeclarationNode;

public final class Nothing extends VariableDeclarationNode {

    public final static VoidType NOTHING = new VoidType();
    public final static BreakType BREAK_TYPE = new BreakType();
    public final static ContinueType CONTINUE_TYPE = new ContinueType();

    public Nothing() {
        super("Nothing");
        setValue(NOTHING);
        setHasValueChanged(true);
        setConst(true);
    }

}
