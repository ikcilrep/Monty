package sml.data.tuple;

import ast.declarations.FunctionDeclarationNode;
import sml.data.Method;


class HasNext extends Method<Iterator> {

    HasNext(Iterator parent) {
        super(parent, "hasNext", FunctionDeclarationNode.EMPTY_PARAMETERS);
    }

    @Override
    public Boolean call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        return parent.hasNext();
    }

}
