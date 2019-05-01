package sml.data.list;

import ast.expressions.OperationNode;
import sml.data.Method;

import java.util.ArrayList;

final class Remove extends Method<List> {

    Remove(List parent) {
        super(parent, "remove");
        addParameter("value");
    }

    @Override
    public List call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        return parent.remove(getBody().getVariableValue("value", callFileName, callLine));
    }

}
