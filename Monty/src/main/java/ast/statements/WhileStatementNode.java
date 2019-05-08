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

package ast.statements;

import ast.Block;
import ast.ConditionalNode;
import ast.expressions.OperationNode;
import sml.data.returning.BreakType;
import sml.data.returning.ContinueType;

public final class WhileStatementNode extends ConditionalNode {
    private final boolean isDoWhile;

    public WhileStatementNode(Block parent, OperationNode condition, boolean isDoWhile, String fileName, int line) {
        super(condition, parent);
        setCondition(condition);
        this.isDoWhile = isDoWhile;
        setFileName(fileName);
        setLine(line);
    }

    private boolean isDoWhile() {
        return isDoWhile;
    }



    @Override
    public Object run() {
        Object result;
        if (isDoWhile() || runCondition()) {
            do {
                result = super.run();
                if (result instanceof BreakType)
                    break;
                if (result instanceof ContinueType)
                    continue;
                if (result != null)
                    return result;
            } while (runCondition());
        }
        return null;
    }

    @Override
    public WhileStatementNode copy() {
        var copied = new WhileStatementNode(getParent(), getCondition(), isDoWhile,getFileName(),getLine() );
        copied.setChildren(getChildren());
        copied.copyChildren();

        return copied;
    }
}
