package sml.data.list;

import ast.expressions.OperationNode;
import sml.data.Method;

import java.util.ArrayList;

final class NewIterator extends Method<List> {

    NewIterator(List parent) {
        super(parent, "Iterator");
    }

    @Override
    public Iterator call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        return new Iterator(parent);
    }

}
