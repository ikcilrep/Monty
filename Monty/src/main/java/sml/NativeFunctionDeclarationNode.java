package sml;

import ast.declarations.FunctionDeclarationNode;

public abstract  class NativeFunctionDeclarationNode extends FunctionDeclarationNode {
    protected NativeFunctionDeclarationNode(String name, String[] parameters) {
        super(name, parameters);
    }
    @Override
    public NativeFunctionDeclarationNode copy() {
        return null;
    }
}
