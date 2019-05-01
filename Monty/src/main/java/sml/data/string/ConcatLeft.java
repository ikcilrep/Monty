package sml.data.string;

import ast.expressions.OperationNode;
import sml.data.Method;

import java.util.ArrayList;

public class ConcatLeft extends Method<StringStruct> {

    ConcatLeft(StringStruct parent) {
        super(parent, "$add");
        addParameter("this");
        addParameter("other");

    }

    @Override
    public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        return parent.getString() + getBody().getVariable("other", callFileName, callLine).getValue().toString();
    }

}
