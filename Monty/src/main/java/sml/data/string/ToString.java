package sml.data.string;

import ast.declarations.FunctionDeclarationNode;
import sml.data.Method;
import sml.data.list.List;
import sml.data.tuple.Tuple;

final class ToString extends Method<StringStruct> {

    ToString(StringStruct parent) {
        super(parent, "$str", FunctionDeclarationNode.EMPTY_PARAMETERS);
    }

    @Override
    public StringStruct call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        return parent;
    }

}
