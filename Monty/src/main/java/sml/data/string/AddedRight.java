package sml.data.string;

import sml.data.Method;
import sml.data.tuple.Tuple;

public class AddedRight extends Method<MontyString> {

    AddedRight(MontyString parent) {
        super(parent, "$r_add",new String[2]);
        addParameter("other");
        addParameter("this");

    }

    @Override
    public Object call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        return body.getStringVariableValue("other", callFileName, callLine).added(body.getStringVariableValue(
                "this", callFileName, callLine));
    }

}
