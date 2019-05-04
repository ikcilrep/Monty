package sml.data.checking;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.StructDeclarationNode;
import ast.expressions.OperationNode;
import sml.Sml;
import sml.data.tuple.Tuple;

import java.util.ArrayList;

public class IsIterable extends FunctionDeclarationNode {

    public IsIterable() {
        super("isIterable");
        setBody(new Block(null));
        addParameter("toCheck");
    }

    public static boolean isIterable(Object toCheck, String callFileName, int callLine) {
        if (toCheck instanceof String)
            return true;
        if (!(toCheck instanceof StructDeclarationNode))
            return false;
        var structToCheck = (StructDeclarationNode) toCheck;
        if (!structToCheck.hasFunction("Iterator"))
            return false;

        var iterator = structToCheck.getFunction("Iterator", callFileName, callLine);
        if (iterator.parameters.size() > 0)
            return false;
        var iteratorValue = iterator.call(Sml.EMPTY_ARGUMENT_LIST, callFileName, callLine);
        if (!(iteratorValue instanceof StructDeclarationNode))
            return false;
        var iteratorStruct = (StructDeclarationNode) iteratorValue;
        return iteratorStruct.hasFunction("hasNext") && iteratorStruct.hasFunction("next");
    }

    @Override
    public Object call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        return isIterable(getBody().getVariableValue("toCheck", callFileName, callLine), callFileName, callLine);
    }

}
