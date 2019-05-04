package sml.data.tuple;

import sml.data.Method;

class Next extends Method<Iterator> {

    Next(Iterator parent) {
        super(parent, "next");
    }

    @Override
    public Object call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        return parent.next();
    }

}
