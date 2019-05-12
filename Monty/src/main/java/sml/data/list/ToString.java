package sml.data.list;

import ast.declarations.FunctionDeclarationNode;
import sml.data.Method;
import sml.data.string.MontyString;
import sml.data.tuple.Tuple;

final class ToString extends Method<List> {

    ToString(List parent) {
        super(parent, "$str", FunctionDeclarationNode.EMPTY_PARAMETERS);
    }

    @Override
    public MontyString call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        return parent.asString(callFileName,callLine);
    }

}
