package sml.data.tuple;

import ast.declarations.FunctionDeclarationNode;
import sml.data.Method;
import sml.data.string.StringStruct;

final class ToString extends Method<Tuple> {

    ToString(Tuple parent) {
        super(parent, "$str", FunctionDeclarationNode.EMPTY_PARAMETERS);
    }

    @Override
    public StringStruct call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        return parent.asString();
    }

}
