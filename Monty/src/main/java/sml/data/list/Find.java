package sml.data.list;

import ast.expressions.OperationNode;
import sml.data.Method;
import sml.data.tuple.Tuple;

import java.math.BigInteger;
import java.util.ArrayList;

final class Find extends Method<List> {

    Find(List parent) {
        super(parent, "find",new String[1]);
        addParameter("value");
    }

    @Override
    public BigInteger call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        return BigInteger.valueOf(parent.find(getBody().getVariableValue("value", callFileName, callLine)));
    }

}
