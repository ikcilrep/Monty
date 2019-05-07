package sml;

import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import parser.LogError;
import sml.data.tuple.Tuple;

public abstract  class NativeFunctionDeclarationNode extends FunctionDeclarationNode {
    protected NativeFunctionDeclarationNode(String name, String[] parameters) {
        super(name, parameters);
    }
    @Override
    public NativeFunctionDeclarationNode copy() {
        return null;
    }
    protected void setArguments(Tuple arguments, String callFileName, int callLine) {
        var argumentsLength = arguments.length();
        if (parameters.length == 1 && parameters.length  != argumentsLength) {
            var name = this.parameters[0];
            VariableDeclarationNode variable;
            if (!body.hasVariable(name)) {
                variable = new VariableDeclarationNode(name);
                body.addVariable(variable, getFileName(), getLine());
            } else
                variable = body.getVariable(name, getFileName(), getLine());
            variable.setValue(arguments, callFileName,callLine);
            variable.setConst(Character.isUpperCase(name.charAt(0)));
            return;
        } else if (argumentsLength > parameters.length)
            new LogError("Too many arguments in " + getName() + " function call.", callFileName, callLine);
        else if (argumentsLength < parameters.length)
            new LogError("Too few arguments in " + getName() + " function call.", callFileName, callLine);

        for (int i = 0; i < arguments.length(); i++) {
            var name = parameters[i];
            VariableDeclarationNode variable = body.getVariable(name,callFileName,callLine);
            variable.setConst(false);
            variable.setValue(arguments.get(i), callFileName, callLine);
            variable.setConst(Character.isUpperCase(name.charAt(0)));
        }
    }

    @Override
    protected void addParameter(String name) {
        super.addParameter(name);
        try {
            body.addVariable(new VariableDeclarationNode(name), getFileName(), getLine());
        } catch (NullPointerException e) {
            System.out.println(getName());
        }
    }
}
