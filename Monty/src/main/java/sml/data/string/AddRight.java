package sml.data.string;

import sml.data.Method;
import sml.data.tuple.Tuple;

public class AddRight extends Method<MontyString> {

    AddRight(MontyString parent) {
        super(parent, "$r_a_add", new String[2]);
        addParameter("other");
        addParameter("this");

    }

    @Override
    public Object call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        return body.getStringVariableValue("other", callFileName, callLine).add(body.getStringVariableValue(
                "this", callFileName, callLine));
    }

}
