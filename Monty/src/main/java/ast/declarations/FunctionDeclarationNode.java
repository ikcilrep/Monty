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
import parser.LogError;
import sml.data.tuple.Tuple;

public abstract class FunctionDeclarationNode extends DeclarationNode {
    public final static String[] EMPTY_PARAMETERS = new String[0];

    private void setParameters(String[] parameters, int lastNotNullParameterIndex) {
        this.parameters = parameters;
        this.lastNotNullParameterIndex = lastNotNullParameterIndex;
    }
    String[] parameters;
    int lastNotNullParameterIndex;
    Block body;
   
    public FunctionDeclarationNode(String name, String[] parameters, int lastNotNullParameterIndex) {
        super(name);
        setParameters(parameters,lastNotNullParameterIndex);
    }

    public FunctionDeclarationNode(String name, String[] parameters) {
        super(name);
        setParameters(parameters, -1);
    }

    protected void addParameter(String name) {
        getParameters()[++lastNotNullParameterIndex] = name;
    }

    public abstract Object call(Tuple arguments, String callFileName, int callLine);

    public abstract FunctionDeclarationNode copy();

    public Block getBody() {
        return body;
    }

    public void setBody(Block body) {
        this.body = body;
    }

    private String[] getParameters() {
        return parameters;
    }
    public int getParametersLength()
    {
        if (parameters == null)
            return 0;
        return parameters.length;
    }


    protected void setArguments(Tuple arguments, String callFileName, int callLine) {
        var argumentsLength = arguments.length();
        var parameters = getParameters();
        var parametersSize = parameters.length;
        var fileName = getFileName();
        var line = getLine();
        if (parametersSize == 1 && parametersSize  != argumentsLength) {
            var name = this.parameters[0];
            VariableDeclarationNode variable;
            if (!body.hasVariable(name)) {
                variable = new VariableDeclarationNode(name);
                body.addVariable(variable, fileName, line);
            } else
                variable = body.getVariable(name, fileName, line);
            variable.setValue(arguments, fileName, line);
            variable.setConst(Character.isUpperCase(name.charAt(0)));
            return;
        } else if (argumentsLength > parametersSize)
            new LogError("Too many arguments in " + name + " function call.", callFileName, callLine);
        else if (argumentsLength < parametersSize)
            new LogError("Too few arguments in " + name + " function call.", callFileName, callLine);
        var body = getBody();

        for (int i = 0; i < arguments.length(); i++) {
            var name = parameters[i];
            VariableDeclarationNode variable;
            variable = new VariableDeclarationNode(name);
            variable.setValue(arguments.get(i), callFileName, callLine);
            variable.setConst(Character.isUpperCase(name.charAt(0)));
            body.addVariable(variable, getFileName(), getLine());
        }
    }

    @Override
    public String toString() {
        return "Function<" + getName() + "> <- " + getParametersLength();
    }
}
