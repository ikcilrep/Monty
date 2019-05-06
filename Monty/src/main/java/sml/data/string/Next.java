package sml.data.string;

import sml.data.Method;
import sml.data.tuple.Tuple;

final class Next extends Method<Iterator> {

    Next(Iterator parent) {
        super(parent, "next",null);
    }

    @Override
    public Object call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        var toReturn = Character.toString(parent.string[parent.counter]);
        parent.counter++;
        return toReturn;
    }

}
