package sml.data.list;

import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;
import sml.data.Method;
import sml.data.tuple.Tuple;

import java.util.ArrayList;

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
