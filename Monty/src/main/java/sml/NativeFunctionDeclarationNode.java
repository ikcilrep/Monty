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
    protected void addParameter(String name) {
        super.addParameter(name);
        body.addVariable(new VariableDeclarationNode(name), getFileName(), getLine());
    }
}
