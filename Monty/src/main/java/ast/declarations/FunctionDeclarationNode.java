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
    String[] parameters;
    protected Block body;
    int lastNotNullParameterIndex;
    public FunctionDeclarationNode(String name, String[] parameters, int lastNotNullParameterIndex) {
        super(name);
        setParameters(parameters, lastNotNullParameterIndex);
    }

    public FunctionDeclarationNode(String name, String[] parameters) {
        super(name);
        setParameters(parameters, -1);
    }

    private void setParameters(String[] parameters, int lastNotNullParameterIndex) {
        this.parameters = parameters;
        this.lastNotNullParameterIndex = lastNotNullParameterIndex;
    }

    private void checkArgumentsSize(int argumentsLength, String fileName, int line) {
        if (argumentsLength > parameters.length)
            new LogError("Too many arguments in " + getName() + " function call.", fileName, line);
        else if (argumentsLength < parameters.length)
            new LogError("Too few arguments in " + getName() + " function call.", fileName, line);
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

    public int getParametersLength() {
        return parameters.length;
    }


    protected void setArguments(Tuple arguments, String callFileName, int callLine) {
        var argumentsLength = arguments.length();
        if (parameters.length == 1 && parameters.length != argumentsLength) {
            setArgument(arguments, 0, callFileName, callLine);
            return;
        }
        checkArgumentsSize(argumentsLength, callFileName, callLine);

        for (int i = 0; i < argumentsLength; i++)
            setArgument(arguments.get(i), i, callFileName, callLine);
    }

    private void setArgument(Object value, int index, String fileName, int line) {
        var name = parameters[index];
        var variable = body.getVariable(name, fileName, line);
        var isConst = variable.isConst();
        variable.setConst(false);
        variable.setValue(value, fileName, line);
        variable.setConst(isConst);
    }

    @Override
    public String toString() {
        return "Function<" + getName() + "> <- " + getParametersLength();
    }
}
