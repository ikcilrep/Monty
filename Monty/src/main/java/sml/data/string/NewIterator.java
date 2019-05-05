package sml.data.string;

import ast.expressions.OperationNode;
import sml.data.Method;
import sml.data.tuple.Tuple;

import java.util.ArrayList;

final class NewIterator extends Method<StringStruct> {

    NewIterator(StringStruct parent) {
        super(parent, "Iterator");
    }

    @Override
    public Iterator call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        return new Iterator(parent);
    }

}