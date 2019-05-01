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
import ast.declarations.StructDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.LogError;
import sml.Sml;
import sml.data.checking.IsIterable;
import sml.data.returning.BreakType;
import sml.data.returning.ContinueType;

public final class ForStatementNode extends Block {
    private OperationNode iterable;
    private String variableName;

    public ForStatementNode(String variableName, OperationNode array, String fileName, int line, Block parent) {
        super(parent);
        setIterable(array);
        setFileName(fileName);
        setLine(line);
        setVariableName(variableName);
    }

    @Override
    public ForStatementNode copy() {
        var copied = (ForStatementNode) super.copy();
        copied.setIterable(getIterable().copy());
        return copied;

    }

    private OperationNode getIterable() {
        return iterable;
    }

    private String getVariableName() {
        return variableName;
    }

    @Override
    public Object run() {
        Object result;
        var name = getVariableName();
        var isNotNameUnderscore = !name.equals("_");
        var isConst = Character.isUpperCase(name.charAt(0));
        var toBeIterated = getIterable().run();

        if (!IsIterable.isIterable(toBeIterated, getFileName(), getLine()))
            new LogError("Can't iterate over not iterable object.", getFileName(), getLine());
        var iterator = (StructDeclarationNode) ((StructDeclarationNode) toBeIterated).getFunction("Iterator", getFileName(), getLine())
                .call(Sml.emptyArgumentList, getFileName(), getLine());
        var hasNext = iterator.getFunction("hasNext", getFileName(), getLine());
        var next = iterator.getFunction("next", getFileName(), getLine());
        VariableDeclarationNode variable = null;
        if (isNotNameUnderscore)
            if (hasVariable(name))
                variable = getVariable(name, getFileName(), getLine());
            else
                addVariable(variable = new VariableDeclarationNode(name), getFileName(), getLine());

        if (isNotNameUnderscore)
            while ((boolean) hasNext.call(Sml.emptyArgumentList, getFileName(), getLine())) {
                variable.setConst(false);
                variable.setValue(next.call(Sml.emptyArgumentList, getFileName(), getLine()));
                variable.setConst(isConst);
                result = super.run();
                if (result instanceof BreakType)
                    break;
                if (result instanceof ContinueType)
                    continue;
                if (result != null)
                    return result;
            }
        else
            while ((boolean) hasNext.call(Sml.emptyArgumentList, getFileName(), getLine())) {
                next.call(Sml.emptyArgumentList, getFileName(), getLine());
                result = super.run();
                if (result instanceof BreakType)
                    break;
                if (result instanceof ContinueType)
                    continue;
                if (result != null)
                    return result;
            }
        return null;
    }

    private void setIterable(OperationNode iterable) {
        this.iterable = iterable;
    }

    @Override
    public void setParent(Block parent) {
        super.setParent(parent);
        getIterable().setParent(parent);
    }

    private void setVariableName(String variableName) {
        this.variableName = variableName;
    }
}
