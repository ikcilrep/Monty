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

package ast.declarations;

import parser.LogError;
import sml.data.returning.Nothing;

public class VariableDeclarationNode extends DeclarationNode {

    protected boolean isConst = false;
    protected boolean wasValueChanged = false;
    protected Object value = Nothing.NOTHING;

    public VariableDeclarationNode(String name) {
        super(name);
    }

    public static VariableDeclarationNode toMe(Object object, String fileName, int line) {
        try {
            return (VariableDeclarationNode) object;
        } catch (ClassCastException e) {
            new LogError("Can't cast any value to variable.", fileName, line);
        }
        return null;
    }

    public final VariableDeclarationNode copy() {
        var copied = new VariableDeclarationNode(name);
        copied.value = value;
        copied.isConst = isConst;
        copied.wasValueChanged = wasValueChanged;
        return copied;
    }

    public final Object getValue() {
        return value;
    }

    public void setConst(boolean isConst) {
        this.isConst = isConst;
    }

    public final void setValue(Object value, String fileName, int line) {
        if (isConst && wasValueChanged)
            new LogError("Can't change value of const.", fileName, line);
        this.value = value;
        wasValueChanged = true;
    }

}
