package sml.data.list;

import parser.DataTypes;
import sml.data.Method;
import sml.data.tuple.Tuple;

final class Pop extends Method<List> {

    Pop(List parent) {
        super(parent, "pop",new String[1]);
        addParameter("index");
    }

    @Override
    public Object call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        int index = DataTypes.getAndCheckSmallInteger(body.getVariableValue("index", callFileName, callLine),"Index",callFileName,callLine);
        parent.doesHaveElement(index, callFileName, callLine);
        return parent.pop(index);
    }

}
