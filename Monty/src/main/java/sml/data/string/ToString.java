package sml.data.string;

import ast.declarations.FunctionDeclarationNode;
import sml.data.Method;
import sml.data.tuple.Tuple;

final class ToString extends Method<MontyString> {

    ToString(MontyString parent) {
        super(parent, "$str", FunctionDeclarationNode.EMPTY_PARAMETERS);
    }

    @Override
    public MontyString call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        return parent;
    }

}
