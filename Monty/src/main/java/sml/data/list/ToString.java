package sml.data.list;

import ast.declarations.FunctionDeclarationNode;
import sml.data.Method;
import sml.data.string.StringStruct;
import sml.data.tuple.Tuple;

final class ToString extends Method<List> {

    ToString(List parent) {
        super(parent, "$str", FunctionDeclarationNode.EMPTY_PARAMETERS);
    }

    @Override
    public StringStruct call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        return parent.asString();
    }

}
