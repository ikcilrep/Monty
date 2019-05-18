package sml.data.tuple;

import parser.DataTypes;
import parser.LogError;
import sml.data.Method;

import java.math.BigInteger;

final class Get extends Method<Tuple> {

    Get(Tuple parent) {
        super(parent, "get",new String[1]);
        addParameter("index");
    }

    @Override
    public Object call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        int index = DataTypes.getAndCheckSmallInteger(body.getVariableValue("index", callFileName, callLine),
                "Index",callFileName,callLine);
        parent.doesHaveElement(index, callFileName, callLine);
        return parent.get(index);
    }

}
