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

package ast;

import ast.expressions.OperationNode;
import sml.casts.ToBoolean;

public class ConditionalNode extends Block {
    protected void setCondition(OperationNode condition) {
        this.condition = condition;
    }

    private OperationNode condition;

    protected ConditionalNode(OperationNode condition, Block parent) {
        super(parent);
        this.condition = condition;
    }

    @Override
    public ConditionalNode copy() {
        var copied = (ConditionalNode) super.copy();
        copied.condition = condition.copy();
        return copied;
    }


    protected boolean runCondition() {
        return ToBoolean.toBoolean(condition.run(), getFileName(), getLine());
    }

    @Override
    public void setParent(Block parent) {
        super.setParent(parent);
        condition.setParent(parent);
    }
}
