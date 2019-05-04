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

import ast.Block;
import ast.expressions.OperationNode;
import parser.LogError;
import sml.data.tuple.Tuple;

import java.util.ArrayList;

public abstract class FunctionDeclarationNode extends DeclarationNode implements Cloneable {
    public ArrayList<VariableDeclarationNode> parameters = new ArrayList<>();
    private Block body;

    public FunctionDeclarationNode(String name) {
        super(name);
    }

    public void addParameter(String name) {
        getParameters().add(new VariableDeclarationNode(name));
    }

    public abstract Object call(Tuple arguments, String callFileName, int callLine);

    public FunctionDeclarationNode copy() {
        try {
            var copied = (FunctionDeclarationNode) clone();
            copied.setBody(getBody().copy());
            return copied;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Block getBody() {
        return body;
    }

    public void setBody(Block body) {
        this.body = body;
    }

    public ArrayList<VariableDeclarationNode> getParameters() {
        return parameters;
    }

    public void setArguments(Tuple arguments, String callFileName, int callLine) {
        var argumentsLength = arguments.length();
        var parametersSize = getParameters().size();
        if (parametersSize == 1 && parametersSize  != argumentsLength) {
            var name = getParameters().get(0).getName();
            VariableDeclarationNode variable;
            if (!body.hasVariable(name)) {
                variable = new VariableDeclarationNode(name);
                body.addVariable(variable, getFileName(), getLine());
            } else
                variable = body.getVariable(name, getFileName(), getLine());
            variable.setValue(arguments);
            variable.setConst(Character.isUpperCase(name.charAt(0)));
            return;
        } else if (argumentsLength > parametersSize)
            new LogError("Too many arguments in " + name + " function call.", callFileName, callLine);
        else if (argumentsLength < parametersSize)
            new LogError("Too few arguments in " + name + " function call.", callFileName, callLine);
        var parameters = getParameters();
        var body = getBody();

        for (int i = 0; i < arguments.length(); i++) {
            var name = parameters.get(i).getName();
            VariableDeclarationNode variable;
            if (!body.hasVariable(name)) {
                variable = new VariableDeclarationNode(name);
                body.addVariable(variable, getFileName(), getLine());
            } else
                variable = body.getVariable(name, getFileName(), getLine());
            variable.setValue(arguments.get(i));
            variable.setConst(Character.isUpperCase(name.charAt(0)));
        }
    }

    @Override
    public String toString() {
        return "Function<" + getName() + "> <- " + getParameters().size();
    }
}
