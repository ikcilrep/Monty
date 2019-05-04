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
    private OperationNode condition;

    public ConditionalNode(OperationNode condition, Block parent) {
        super(parent);
        setCondition(condition);
    }

    @Override
    public ConditionalNode copy() {
        var copied = (ConditionalNode) super.copy();
        copied.setCondition(getCondition().copy());
        return copied;
    }

    private OperationNode getCondition() {
        return condition;
    }

    protected void setCondition(OperationNode condition) {
        assert condition != null;
        this.condition = condition;
    }

    protected boolean ranCondition() {
        return ToBoolean.toBoolean(getCondition().run(), getFileName(), getLine());
    }

    @Override
    public void setParent(Block parent) {
        super.setParent(parent);
        getCondition().setParent(parent);
    }
}
