package sml.data.tuple;

import ast.declarations.FunctionDeclarationNode;
import sml.data.Method;
import sml.data.list.List;

final class NewIterator extends Method<Tuple> {

    NewIterator(Tuple parent) {
        super(parent, "Iterator", FunctionDeclarationNode.EMPTY_PARAMETERS);
    }

    @Override
    public Iterator call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        return new Iterator(parent);
    }

}
