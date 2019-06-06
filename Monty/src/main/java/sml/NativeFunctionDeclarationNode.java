package sml;

import ast.declarations.NamedFunctionDeclarationNode;


public abstract class NativeFunctionDeclarationNode extends NamedFunctionDeclarationNode {
    protected NativeFunctionDeclarationNode(String name, String[] parameters) {
        super(name,parameters);
    }

    @Override
    public NativeFunctionDeclarationNode copy() {
        return this;
    }

}
