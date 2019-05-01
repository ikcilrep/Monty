package sml.data.list;

import ast.expressions.OperationNode;
import sml.data.Method;

import java.math.BigInteger;
import java.util.ArrayList;

final class Length extends Method<List> {

    Length(List parent) {
        super(parent, "length");
    }

    @Override
    public BigInteger call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        return BigInteger.valueOf(parent.length());
    }

}
