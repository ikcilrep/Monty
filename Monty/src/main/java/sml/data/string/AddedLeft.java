package sml.data.string;

import sml.data.Method;
import sml.data.tuple.Tuple;

public class AddedLeft extends Method<StringStruct> {

    AddedLeft(StringStruct parent) {
        super(parent, "$add",new String[2]);
        addParameter("this");
        addParameter("other");

    }

    @Override
    public Object call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        return new StringStruct(parent.getString() + getBody().getVariable("other", callFileName, callLine).getValue().toString());
    }

}
