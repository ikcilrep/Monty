package sml.data.tuple;

import ast.declarations.FunctionDeclarationNode;
import sml.data.Method;
import sml.data.string.MontyString;

final class ToString extends Method<Tuple> {

    ToString(Tuple parent) {
        super(parent, "$str", FunctionDeclarationNode.EMPTY_PARAMETERS);
    }

    @Override
    public MontyString call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        return parent.asString(callFileName, callLine);
    }

}
