package sml.data.list;

import parser.DataTypes;
import sml.data.Method;
import sml.data.tuple.Tuple;

final class MultipliedLeft extends Method<List> {
    MultipliedLeft(List parent) {
        super(parent, "$mul",new String[2]);
        addParameter("this");
        addParameter("times");
    }

    @Override
    public List call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        return parent.multiplied(DataTypes.getAndCheckSmallInteger(body.getVariableValue("times", callFileName,
                callLine),"Multiplier",callFileName,callLine));
    }
}
