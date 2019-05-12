package sml.data.string;

import sml.data.Method;
import sml.data.tuple.Tuple;

public class AddRight extends Method<StringStruct> {

    AddRight(StringStruct parent) {
        super(parent, "$r_a_add",new String[2]);
        addParameter("other");
        addParameter("this");

    }

    @Override
    public Object call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        parent.setString(getBody().getVariable("other", callFileName, callLine).getValue() +
                parent.getString());
        return parent;
    }

}
