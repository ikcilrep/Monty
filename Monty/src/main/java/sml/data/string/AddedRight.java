package sml.data.string;

import ast.expressions.OperationNode;
import sml.data.Method;
import sml.data.tuple.Tuple;

import java.util.ArrayList;

public class AddedRight extends Method<StringStruct> {

    AddedRight(StringStruct parent) {
        super(parent, "$r_add");
        addParameter("other");
        addParameter("this");

    }

    @Override
    public Object call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        return getBody().getVariable("other", callFileName, callLine).getValue().toString() + parent.getString();
    }

}
