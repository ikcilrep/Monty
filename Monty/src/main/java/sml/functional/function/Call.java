package sml.functional.function;

import ast.expressions.OperationNode;
import sml.data.Method;

import java.util.ArrayList;

final class Call extends Method<Function> {

    Call(Function parent) {
        super(parent, "call");
    }

    @Override
    public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
        return parent.call(arguments, callFileName, callLine);
    }

}
