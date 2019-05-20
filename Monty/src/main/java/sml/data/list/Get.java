package sml.data.list;

import parser.DataTypes;
import sml.data.Method;
import sml.data.tuple.Tuple;

final class Get extends Method<List> {

    Get(List parent) {
        super(parent, "get",new String[1]);
        addParameter("index");
    }

    @Override
    public Object call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        int index = DataTypes.getAndCheckSmallInteger(body.getVariableValue("index", callFileName, callLine),
                "Index",callFileName,callLine);
        parent.doesHaveElement(index, callFileName, callLine);
        return parent.get(index);
    }

}
