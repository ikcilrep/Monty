package sml.functional.function;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.StructDeclarationNode;
import ast.expressions.OperationNode;

import java.util.ArrayList;

public class Function extends StructDeclarationNode {
    private FunctionDeclarationNode function;

    public Function(FunctionDeclarationNode function) {
        super(null, "Function");
        this.function = function;
        incrementNumber();
        new Call(this);
    }

    public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
        return function.call(arguments, callFileName, callLine);
    }

    @Override
    public void setParent(Block parent) {
        super.setParent(parent);
        function.getBody().setParent(parent);
    }
}
