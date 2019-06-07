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
import lexer.Token;

public final class ReturnStatementNode extends NodeWithParent {

    private final OperationNode expression;

    public ReturnStatementNode(OperationNode expression, String fileName, int line) {
        this.expression = expression;
        setFileName(fileName);
        setLine(line);
    }

    public ReturnStatementNode(OperationNode expression, Token token) {
        this.expression = expression;
        setFileName(token.getFileName());
        setLine(token.getLine());
    }

    @Override
    public NodeWithParent copy() {
        return new ReturnStatementNode(expression.copy(), getFileName(), getLine());
    }


    @Override
    public OperationNode run() {
        return expression;
    }

    @Override
    public void setParent(Block parent) {
        expression.setParent(parent);
    }
}
