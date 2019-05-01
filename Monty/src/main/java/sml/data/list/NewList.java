package sml.data.list;

import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;

import java.util.ArrayList;

public class NewList extends FunctionDeclarationNode {

    public NewList() {
        super("List");
    }

    @Override
    public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
        return new List(arguments);
    }

}
