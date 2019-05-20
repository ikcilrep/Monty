package sml.errors;

import ast.Block;
import sml.NativeFunctionDeclarationNode;
import sml.data.returning.Nothing;
import sml.data.returning.VoidType;
import sml.data.tuple.Tuple;

public final class LogError extends NativeFunctionDeclarationNode {

    public LogError() {
        super("logError", new String[1]);
        setBody(new Block(null));
        addParameter("message");
    }

    @Override
    public VoidType call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        new parser.LogError(body.getStringVariableValue("message", callFileName, callLine).toString());
        return Nothing.NOTHING;
    }

}
