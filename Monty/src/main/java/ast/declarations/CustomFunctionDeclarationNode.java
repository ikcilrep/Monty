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

import ast.Operator;
import ast.expressions.IdentifierNode;
import ast.expressions.OperationNode;
import ast.statements.ContinueStatementNode;
import parser.LogError;
import sml.data.returning.BreakType;
import sml.data.returning.Nothing;
import sml.data.tuple.Tuple;

public final class CustomFunctionDeclarationNode extends FunctionDeclarationNode {


    public CustomFunctionDeclarationNode(String name, String[] parameters, int lastNotNullParameterIndex) {
        super(name, parameters, lastNotNullParameterIndex);
    }

    private CustomFunctionDeclarationNode workingCopy() {
        var copied = copy();
        copied.body.copyVariables();
        return copied;
    }
    private Object callWithoutChangingTailRecursionToIteration(Tuple arguments, String callFileName, int callLine) {
        String[] fileNames = {callFileName, getFileName()};
        int[] lines = {callLine, getLine()};
        Object result = null;
        setValuesOfAddedArguments(arguments,callFileName,callLine);
        try {
            return getResult(body.run(),fileNames,lines);
        } catch (StackOverflowError e) {
            return new LogError("Stack overflow at " + name + " function call", fileNames, lines);
        }
    }

    private Object getResult(Object value, String[] fileNames, int[] lines) {
        if (value == null)
            value = Nothing.nothing;
        else {
            if (value instanceof BreakType)
                new LogError("Trying to break function " + getName(), fileNames, lines);
            if (value instanceof ContinueStatementNode)
                new LogError("Trying to continue function " + getName(), fileNames, lines);
        }
        return value;
    }

    private Object changeTailRecursionToIteration(Object value,String fileName, int line) {
        while (value instanceof OperationNode) {
            var operationNodeResult = (OperationNode) value;
            while (operationNodeResult.getOperand().equals(Operator.JUST))
                operationNodeResult = operationNodeResult.getRight();
            var operand = operationNodeResult.getOperand();

            if (operand instanceof IdentifierNode) {
                var identifierNodeOperand = (IdentifierNode) operand;
                if (!identifierNodeOperand.isFunctionCall() ||
                        !identifierNodeOperand.getName().equals(name))
                    return operationNodeResult.run();
                var recArguments = operationNodeResult.getRight().run();

                value = callWithoutChangingTailRecursionToIteration
                        (OperationNode.argumentsToTuple(recArguments), fileName, line);
            } else
                value = operationNodeResult.run();
        }
        return value;
    }

    @Override
    public final Object call(Tuple arguments, String callFileName, int callLine) {
        var workingCopy = workingCopy();
        workingCopy.setArguments(arguments, callFileName, callLine);
        String[] fileNames = {callFileName, getFileName()};
        int[] lines = {callLine, getLine()};
        try {
            return getResult(workingCopy.changeTailRecursionToIteration(workingCopy.body.run(),callFileName,callLine),fileNames,lines);
        } catch (StackOverflowError e) {
            return  new LogError("Stack overflow at " + name + " function call", fileNames, lines);
        }

    }


    @Override
    public CustomFunctionDeclarationNode copy() {
        var copied = new CustomFunctionDeclarationNode(name, parameters, lastNotNullParameterIndex);
        copied.body = body.copy();
        return copied;
    }

    @Override
    protected void setArgument(Object value, int index, String fileName, int line) {
        var name = parameters[index];
        VariableDeclarationNode variable = new VariableDeclarationNode(name);
        variable.setValue(value, fileName, line);
        variable.setConst(Character.isUpperCase(name.charAt(0)));
        body.addVariable(variable, fileName, line);
    }

    private void setValueOfAddedArgument(Object value, int index, String fileName, int line) {
        var name = parameters[index];
        VariableDeclarationNode variable = body.getVariable(name,fileName,line);
        variable.setConst(false);
        variable.setValue(value, fileName, line);
        variable.setConst(Character.isUpperCase(name.charAt(0)));
    }

    private void setValuesOfAddedArguments(Tuple arguments, String fileName, int line) {
        var argumentsLength = arguments.length();
        if (parameters.length == 1 && parameters.length  != argumentsLength)
            setValueOfAddedArgument(arguments, 0,fileName,line);
        else
            checkArgumentsSize(arguments,argumentsLength,fileName,line);

        for (int i = 0; i < argumentsLength; i++)
            setValueOfAddedArgument(arguments.get(i), i,fileName,line);
    }


}
