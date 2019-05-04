package sml.data.list;

import ast.expressions.OperationNode;
import parser.DataTypes;
import parser.LogError;
import sml.data.Method;
import sml.data.tuple.Tuple;

import java.math.BigInteger;
import java.util.ArrayList;

final class Get extends Method<List> {

    Get(List parent) {
        super(parent, "get");
        addParameter("index");
    }

    @Override
    public Object call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        var _index = getBody().getVariableValue("index", callFileName, callLine);
        int index = 0;
        if (_index instanceof Integer)
            index = (int) _index;
        else if (_index instanceof BigInteger) {
            var bigIndex = (BigInteger) _index;
            if (bigIndex.compareTo(DataTypes.INT_MAX) > 0)
                new LogError("Index have to be less or equals 2^31-1.", callFileName, callLine);
            else if (bigIndex.compareTo(DataTypes.INT_MIN) < 0)
                new LogError("Index have to be greater or equals -2^31.", callFileName, callLine);
            index = bigIndex.intValue();
        }
        parent.doesHaveElement(index, callFileName, callLine);
        return parent.get(index);
    }

}
