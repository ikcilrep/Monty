package sml.data.list;

import ast.expressions.OperationNode;
import parser.DataTypes;
import parser.LogError;
import sml.data.Method;

import java.math.BigInteger;
import java.util.ArrayList;

final class Sublist extends Method<List> {

    Sublist(List parent) {
        super(parent, "sublist");
        addParameter("begin");
        addParameter("end");
    }

    @Override
    public List call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        var body = getBody();
        var _begin = body.getVariableValue("begin", callFileName, callLine);
        var _end = body.getVariableValue("end", callFileName, callLine);

        int begin = 0;
        int end = 0;

        if (_begin instanceof Integer)
            begin = (int) _begin;
        else if (_begin instanceof BigInteger) {
            var bigIndex = (BigInteger) _begin;
            if (bigIndex.compareTo(DataTypes.INT_MAX) > 0)
                new LogError("Index have to be less or equals 2^31-1.", callFileName, callLine);
            else if (bigIndex.compareTo(DataTypes.INT_MIN) < 0)
                new LogError("Index have to be greater or equals -2^31.", callFileName, callLine);
            begin = bigIndex.intValue();
        }

        if (_end instanceof Integer)
            end = (int) _end;
        else if (_begin instanceof BigInteger) {
            var bigIndex = (BigInteger) _end;
            if (bigIndex.compareTo(DataTypes.INT_MAX) > 0)
                new LogError("Index have to be less or equals 2^31-1.", callFileName, callLine);
            else if (bigIndex.compareTo(DataTypes.INT_MIN) < 0)
                new LogError("Index have to be greater or equals -2^31.", callFileName, callLine);
            end = bigIndex.intValue();
        }

        if (begin < 0)
            new LogError("Begin can't be negative.", callFileName, callLine);
        if (end > parent.length())
            new LogError("End can't be greater than length of list.", callFileName, callLine);
        if (begin > end)
            new LogError("Begin can't be greater or equals to end.", callFileName, callLine);
        return parent.sublist(begin, end);
    }

}
