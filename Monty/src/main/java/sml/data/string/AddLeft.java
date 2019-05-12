package sml.data.string;

import sml.data.Method;
import sml.data.tuple.Tuple;

public class AddLeft extends Method<StringStruct> {

    AddLeft(StringStruct parent) {
        super(parent, "$a_add",new String[2]);
        addParameter("this");
        addParameter("other");

    }

    @Override
    public Object call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        parent.setString(parent.getString() + getBody().getVariable("other", callFileName, callLine).getValue()
);
        return parent;
    }

}
