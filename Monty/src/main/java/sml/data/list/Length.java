package sml.data.list;

import ast.declarations.FunctionDeclarationNode;
import sml.data.Method;
import sml.data.tuple.Tuple;

final class Length extends Method<List> {

    Length(List parent) {
        super(parent, "length", FunctionDeclarationNode.EMPTY_PARAMETERS);
    }

    @Override
    public Integer call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        return parent.length();
    }

}
