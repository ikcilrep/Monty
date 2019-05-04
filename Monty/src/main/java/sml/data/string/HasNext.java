package sml.data.string;

import ast.expressions.OperationNode;
import sml.data.Method;
import sml.data.tuple.Tuple;

import java.util.ArrayList;

final class HasNext extends Method<Iterator> {

    HasNext(Iterator parent) {
        super(parent, "hasNext");
    }

    @Override
    public Boolean call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        return parent.counter < parent.string.length;
    }

}
