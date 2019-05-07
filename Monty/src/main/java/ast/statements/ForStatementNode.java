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
import sml.Sml;
import sml.data.returning.BreakType;
import sml.data.returning.ContinueType;

public final class ForStatementNode extends Block {
    private OperationNode iterable;
    private final String variableName;

    private static boolean isIterable(Object toCheck, String callFileName, int callLine) {
        if (toCheck instanceof String)
            return true;
        if (!(toCheck instanceof StructDeclarationNode))
            return false;
        var structToCheck = (StructDeclarationNode) toCheck;
        if (!structToCheck.hasFunction("Iterator"))
            return false;

        var iterator = structToCheck.getFunction("Iterator", callFileName, callLine);
        if (iterator.getParametersLength() > 0)
            return false;
        var iteratorValue = iterator.call(Sml.EMPTY_ARGUMENT_LIST, callFileName, callLine);
        if (!(iteratorValue instanceof StructDeclarationNode))
            return false;
        var iteratorStruct = (StructDeclarationNode) iteratorValue;
        return iteratorStruct.hasFunction("hasNext") && iteratorStruct.hasFunction("next");
    }
    public ForStatementNode(String variableName, OperationNode iterable, String fileName, int line, Block parent) {
        super(parent);
        this.iterable = iterable;
        this.variableName = variableName;
        setFileName(fileName);
        setLine(line);
    }

    @Override
    public ForStatementNode copy() {
        var copied = new ForStatementNode(variableName,iterable.copy(),getFileName(),getLine(), getParent());
        copied.setChildren(getChildren());
        copied.copyChildren();

        return copied;
    }



    @Override
    public Object run() {
        Object result;
        var isNotNameUnderscore = !variableName.equals("_");
        var isConst = Character.isUpperCase(variableName.charAt(0));
        var toBeIterated = iterable.run();
        VariableDeclarationNode variable = null;
        var fileName = getFileName();
        var line = getLine();
        if (isNotNameUnderscore)
            addVariable(variable = new VariableDeclarationNode(variableName), getFileName(), getLine());
        if (isIterable(toBeIterated, getFileName(), getLine())) {
            var iterator = (StructDeclarationNode) ((StructDeclarationNode) toBeIterated).getFunction("Iterator", getFileName(), getLine())
                    .call(Sml.EMPTY_ARGUMENT_LIST, getFileName(), getLine());
            var hasNext = iterator.getFunction("hasNext", getFileName(), getLine());
            var next = iterator.getFunction("next", getFileName(), getLine());

            if (isNotNameUnderscore) {
                while ((boolean) hasNext.call(Sml.EMPTY_ARGUMENT_LIST, fileName, line)) {
                    variable.setConst(false);
                    variable.setValue(next.call(Sml.EMPTY_ARGUMENT_LIST, fileName, line), fileName, line);
                    variable.setConst(isConst);
                    result = super.run();
                    if (result instanceof BreakType)
                        break;
                    if (result instanceof ContinueType)
                        continue;
                    if (result != null)
                        return result;
                }
            } else
                while ((boolean) hasNext.call(Sml.EMPTY_ARGUMENT_LIST, getFileName(), getLine())) {
                    next.call(Sml.EMPTY_ARGUMENT_LIST, getFileName(), getLine());
                    result = super.run();
                    if (result instanceof BreakType)
                        break;
                    if (result instanceof ContinueType)
                        continue;
                    if (result != null)
                        return result;
                }
        } else {
            if (isNotNameUnderscore) {
                variable.setConst(false);
                variable.setValue(toBeIterated, fileName, line);
                variable.setConst(isConst);
            }
            result = super.run();
            if (!(result == null || (result instanceof BreakType || result instanceof ContinueType)))
                return result;
        }
        return null;
    }

    @Override
    public void setParent(Block parent) {
        super.setParent(parent);
        iterable.setParent(parent);
    }
}
