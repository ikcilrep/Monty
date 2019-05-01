package sml.data.list;

import ast.expressions.OperationNode;
import sml.data.Method;

import java.util.ArrayList;

class HasNext extends Method<Iterator> {

    HasNext(Iterator parent) {
        super(parent, "hasNext");
    }

    @Override
    public Boolean call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        return parent.hasNext();
    }

}
