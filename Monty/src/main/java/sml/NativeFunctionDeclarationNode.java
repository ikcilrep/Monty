package sml;

import ast.declarations.NamedFunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;


public abstract class NativeFunctionDeclarationNode extends NamedFunctionDeclarationNode {
    protected NativeFunctionDeclarationNode(String name, String[] parameters) {
        super(name,parameters);
    }

    @Override
    public NativeFunctionDeclarationNode copy() {
        return this;
    }


    @Override
    protected void addParameter(String name) {
        super.addParameter(name);
        body.addVariable(new VariableDeclarationNode(name), getFileName(), getLine());
    }
}
