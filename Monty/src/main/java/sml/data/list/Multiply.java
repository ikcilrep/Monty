package sml.data.list;

import parser.DataTypes;
import sml.data.Method;
import sml.data.tuple.Tuple;

final class Multiply extends Method<List> {
    Multiply(List parent) {
        super(parent, "$a_mul",new String[2]);
        addParameter("this");
        addParameter("times");
    }

    @Override
    public List call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        return parent.multiply(DataTypes.getAndCheckSmallInteger(body.getVariableValue("times", callFileName,
                callLine),"Multiplier",callFileName,callLine));
    }
}
