package sml.data.string;

import ast.expressions.OperationNode;
import sml.data.Method;

import java.util.ArrayList;

final class NewIterator extends Method<StringStruct> {

    NewIterator(StringStruct parent) {
        super(parent, "Iterator");
    }

    @Override
    public Iterator call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        return new Iterator(parent);
    }

}
