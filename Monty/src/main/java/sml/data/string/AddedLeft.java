package sml.data.string;

import sml.data.Method;
import sml.data.tuple.Tuple;

public class AddedLeft extends Method<MontyString> {

    AddedLeft(MontyString parent) {
        super(parent, "$add", new String[2]);
        addParameter("this");
        addParameter("other");

    }

    @Override
    public Object call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        return body.getStringVariableValue("this", callFileName, callLine).added(body.getStringVariableValue("other", callFileName, callLine));
    }

}
