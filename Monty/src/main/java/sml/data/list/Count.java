package sml.data.list;

import ast.expressions.OperationNode;
import sml.data.Method;

import java.math.BigInteger;
import java.util.ArrayList;

final class Count extends Method<List> {

    Count(List parent) {
        super(parent, "count");
        addParameter("value");
    }

    @Override
    public BigInteger call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        return BigInteger.valueOf(parent.count(getBody().getVariableValue("value", callFileName, callLine)));
    }

}
