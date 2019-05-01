package sml.data.list;

import ast.expressions.OperationNode;
import sml.data.Method;

import java.math.BigInteger;
import java.util.ArrayList;

final class Find extends Method<List> {

    Find(List parent) {
        super(parent, "find");
        addParameter("value");
    }

    @Override
    public BigInteger call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        return BigInteger.valueOf(parent.find(getBody().getVariableValue("value", callFileName, callLine)));
    }

}
