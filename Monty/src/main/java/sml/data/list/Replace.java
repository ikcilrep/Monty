package sml.data.list;

import sml.data.Method;
import sml.data.tuple.Tuple;

final class Replace extends Method<List> {

    Replace(List parent) {
        super(parent, "replace",new String[2]);
        addParameter("toBeReplaced");
        addParameter("replacement");
    }

    @Override
    public List call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        var body = getBody();
        return parent.replace(body.getVariableValue("toBeReplaced", callFileName, callLine), body.getVariableValue("replacement", callFileName, callLine));
    }

}
