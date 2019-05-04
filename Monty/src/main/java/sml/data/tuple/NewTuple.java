package sml.data.tuple;

import ast.declarations.FunctionDeclarationNode;
import ast.declarations.StructDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import parser.LogError;
import sml.data.string.StringStruct;

public class NewTuple extends FunctionDeclarationNode {

    public NewTuple() {
        super("Tuple");
    }

    @Override
    public Object call(Tuple arguments, String callFileName, int callLine) {
        return arguments;
    }
}
