package sml.data.tuple;

import ast.declarations.FunctionDeclarationNode;
import sml.NativeFunctionDeclarationNode;

public class NewTuple extends NativeFunctionDeclarationNode {

    public NewTuple() {
        super("Tuple",FunctionDeclarationNode.EMPTY_PARAMETERS);
    }

    @Override
    public Object call(Tuple arguments, String callFileName, int callLine) {
        return arguments;
    }
}
