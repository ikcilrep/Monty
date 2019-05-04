package sml.data.tuple;

import sml.data.Method;


class HasNext extends Method<Iterator> {

    HasNext(Iterator parent) {
        super(parent, "hasNext");
    }

    @Override
    public Boolean call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        return parent.hasNext();
    }

}
