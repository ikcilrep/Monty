package sml.data.string;

import parser.DataTypes;
import sml.data.Method;
import sml.data.tuple.Tuple;

public class MultiplyLeft extends Method<MontyString> {

    MultiplyLeft(MontyString parent) {
        super(parent, "$a_mul",new String[2]);
        addParameter("this");
        addParameter("times");

    }

    @Override
    public Object call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        return body.getStringVariableValue("this", callFileName,
                callLine).multiply(DataTypes.getAndCheckSmallInteger(body.getVariable("times", callFileName,
                callLine).getValue(), "Multiplier", callFileName,callLine));
    }

}
