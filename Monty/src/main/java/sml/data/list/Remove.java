package sml.data.list;

import ast.expressions.OperationNode;
import sml.data.Method;
import sml.data.tuple.Tuple;

import java.util.ArrayList;

final class Remove extends Method<List> {

    Remove(List parent) {
        super(parent, "remove",new String[1]);
        addParameter("value");
    }

    @Override
    public List call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        return parent.remove(getBody().getVariableValue("value", callFileName, callLine));
    }

}
