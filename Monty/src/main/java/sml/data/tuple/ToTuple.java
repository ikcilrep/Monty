package sml.data.tuple;

import ast.Block;
import ast.declarations.StructDeclarationNode;
import ast.expressions.OperationNode;
import ast.statements.ForStatementNode;
import sml.NativeFunctionDeclarationNode;
import sml.casts.ToBoolean;
import sml.data.list.List;

public class ToTuple extends NativeFunctionDeclarationNode {
    public ToTuple() {
        super("toTuple", new String[1]);
        setBody(new Block(null));
        addParameter("value");
    }

    @Override
    public Tuple call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments,callFileName,callLine);
        var value = body.getVariableValue("value", callFileName,callLine);
        if (ForStatementNode.isIterable(value,callFileName,callLine)) {
            var iterator = (StructDeclarationNode) ((StructDeclarationNode) value).getFunction("Iterator", getFileName(), getLine())
                    .call(OperationNode.emptyTuple, callFileName,callLine);
            var next = iterator.getFunction("next", callFileName,callLine);
            var hasNext = iterator.getFunction("hasNext", callFileName,callLine);
            List list = new List(0);
            while (ToBoolean.toBoolean(hasNext.call(OperationNode.emptyTuple, callFileName, callLine),callFileName,callLine))
                list.add(next.call(OperationNode.emptyTuple,callFileName,callLine));

            return new Tuple(list.toArray());
        }
        return arguments;
    }
}
