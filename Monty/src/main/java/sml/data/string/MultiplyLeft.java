package sml.data.string;

import parser.DataTypes;
import parser.LogError;
import sml.data.Method;
import sml.data.tuple.Tuple;

import java.math.BigInteger;

public class MultiplyLeft extends Method<StringStruct> {

    MultiplyLeft(StringStruct parent) {
        super(parent, "$a_mul",new String[2]);
        addParameter("this");
        addParameter("times");

    }

    @Override
    public Object call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        var _times = getBody().getVariable("times", callFileName, callLine).getValue();
        int times = -1;
        if (_times instanceof Integer)
            times = (int) _times;
        else if (_times instanceof BigInteger) {
            if (((BigInteger) _times).compareTo(DataTypes.INT_MIN) < 0)
                new LogError("Multiplier has to be greater than -2^31.", callFileName, callLine);
            else if (((BigInteger) _times).compareTo(DataTypes.INT_MAX) > 0)
                new LogError("Multiplier has to be less than 2^31 - 1.", callFileName, callLine);
            times = ((BigInteger) _times).intValue();
        } else
            new LogError("Multiplier has to be integer.", callFileName, callLine);
        return parent.mulitply(times);
    }

}
