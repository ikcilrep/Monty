package sml.data.tuple;

import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;

import java.util.ArrayList;

public class NewTuple extends FunctionDeclarationNode {

    public NewTuple() {
        super("Tuple");
    }

    @Override
    public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
        return new Tuple(arguments);
    }

}
