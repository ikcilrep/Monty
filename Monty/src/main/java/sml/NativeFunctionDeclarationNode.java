package sml;

import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;


public abstract class NativeFunctionDeclarationNode extends FunctionDeclarationNode {
    protected NativeFunctionDeclarationNode(String name, String[] parameters) {
        super(name, parameters);
    }

    @Override
    public NativeFunctionDeclarationNode copy() {
        return null;
    }

    @Override
    protected void setArgument(Object value, int index, String fileName, int line) {
        var name = parameters[index];
        VariableDeclarationNode variable = body.getVariable(name, fileName, line);
        variable.setConst(false);
        variable.setValue(value, fileName, line);
        variable.setConst(Character.isUpperCase(name.charAt(0)));
    }

    @Override
    protected void addParameter(String name) {
        super.addParameter(name);
        body.addVariable(new VariableDeclarationNode(name), getFileName(), getLine());
    }
}
