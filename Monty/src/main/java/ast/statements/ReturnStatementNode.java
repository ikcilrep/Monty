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
import ast.NodeWithParent;
import ast.expressions.OperationNode;

public final class ReturnStatementNode extends NodeWithParent implements Cloneable {

    private OperationNode expression;

    public ReturnStatementNode(OperationNode expression, String fileName, int line) {
        this.expression = expression;
        setFileName(fileName);
        setLine(line);
    }

    @Override
    public NodeWithParent copy() {
        return new ReturnStatementNode(expression,getFileName(), getLine());
    }



    @Override
    public Object run() {
        return expression.run();
    }

    @Override
    public void setParent(Block parent) {
        expression.setParent(parent);
    }
}
