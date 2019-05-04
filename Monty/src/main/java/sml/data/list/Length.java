package sml.data.list;

import ast.expressions.OperationNode;
import sml.data.Method;
import sml.data.tuple.Tuple;

import java.math.BigInteger;
import java.util.ArrayList;

final class Length extends Method<List> {

    Length(List parent) {
        super(parent, "length");
    }

    @Override
    public BigInteger call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        return BigInteger.valueOf(parent.length());
    }

}
