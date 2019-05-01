package sml.data.string;

import ast.expressions.OperationNode;
import sml.data.Method;

import java.util.ArrayList;

final class Next extends Method<Iterator> {

    Next(Iterator parent) {
        super(parent, "next");
    }

    @Override
    public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        var toReturn = Character.toString(parent.string[parent.counter]);
        parent.counter++;
        return toReturn;
    }

}
