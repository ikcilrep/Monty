package sml.data.list;

import ast.expressions.OperationNode;
import sml.data.Method;

import java.util.ArrayList;

class Next extends Method<Iterator> {

    Next(Iterator parent) {
        super(parent, "next");
    }

    @Override
    public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        return parent.next();
    }

}
