package sml.data.list;

import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;
import sml.data.Method;
import sml.data.tuple.Tuple;

import java.util.ArrayList;

final class NewIterator extends Method<List> {

    NewIterator(List parent) {
        super(parent, "Iterator", FunctionDeclarationNode.EMPTY_PARAMETERS);
    }

    @Override
    public Iterator call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        return new Iterator(parent);
    }

}
