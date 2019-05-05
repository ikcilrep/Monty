package sml.data.string;

import ast.expressions.OperationNode;
import sml.data.Method;
import sml.data.tuple.Tuple;

import java.util.ArrayList;

public class AddedLeft extends Method<StringStruct> {

    AddedLeft(StringStruct parent) {
        super(parent, "$add",new String[2]);
        addParameter("this");
        addParameter("other");

    }

    @Override
    public Object call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        return parent.getString() + getBody().getVariable("other", callFileName, callLine).getValue().toString();
    }

}
