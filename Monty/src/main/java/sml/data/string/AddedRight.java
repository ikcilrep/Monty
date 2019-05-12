package sml.data.string;

import sml.data.Method;
import sml.data.tuple.Tuple;

public class AddedRight extends Method<StringStruct> {

    AddedRight(StringStruct parent) {
        super(parent, "$r_add",new String[2]);
        addParameter("other");
        addParameter("this");

    }

    @Override
    public Object call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        return new StringStruct(getBody().getVariable("other", callFileName, callLine).getValue().toString() +
                parent.getString());
    }

}
