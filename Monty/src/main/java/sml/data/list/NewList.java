package sml.data.list;

import ast.declarations.FunctionDeclarationNode;
import sml.NativeFunctionDeclarationNode;
import sml.data.tuple.Tuple;

public class NewList extends NativeFunctionDeclarationNode {

    public NewList() {
        super("List", FunctionDeclarationNode.EMPTY_PARAMETERS);
    }

    @Override
    public Object call(Tuple arguments, String callFileName, int callLine) {
        var list = new List(arguments);
        list.addThisVariable(callFileName,callLine);
        return list;
    }

    @Override
    public String toString() {
        return "Constructor<"+getName()+">";
    }
}
