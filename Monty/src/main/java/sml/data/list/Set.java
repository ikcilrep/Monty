package sml.data.list;

import parser.DataTypes;
import sml.data.Method;
import sml.data.tuple.Tuple;

final class Set extends Method<List> {

    Set(List parent) {
        super(parent, "set", new String[2]);
        addParameter("index");
        addParameter("value");
    }

    @Override
    public List call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        int index = DataTypes.getAndCheckSmallInteger(body.getVariableValue("index", callFileName, callLine),
                "Index", callFileName, callLine);

        parent.doesHaveElement(index, callFileName, callLine);
        return parent.set(index, body.getVariableValue("value", callFileName, callLine));
    }

}
