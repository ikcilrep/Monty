package sml.data.list;

import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;
import sml.data.tuple.Tuple;

import java.util.ArrayList;

public class NewList extends FunctionDeclarationNode {

    public NewList() {
        super("List", FunctionDeclarationNode.EMPTY_PARAMETERS);
    }

    @Override
    public Object call(Tuple arguments, String callFileName, int callLine) {
        return new List(arguments);
    }

}
