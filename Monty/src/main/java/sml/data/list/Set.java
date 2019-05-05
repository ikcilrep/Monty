package sml.data.list;

import ast.expressions.OperationNode;
import parser.DataTypes;
import parser.LogError;
import sml.data.Method;
import sml.data.tuple.Tuple;

import java.math.BigInteger;
import java.util.ArrayList;

final class Set extends Method<List> {

    Set(List parent) {
        super(parent, "set",new String[2]);
        addParameter("index");
        addParameter("value");
    }

    @Override
    public List call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        var body = getBody();
        var _index = body.getVariableValue("index", callFileName, callLine);
        var value = body.getVariableValue("value", callFileName, callLine);
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
        return parent.set(index, value);
    }

}
