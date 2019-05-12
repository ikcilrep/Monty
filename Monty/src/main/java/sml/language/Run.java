package sml.language;

import ast.Block;
import lexer.Lexer;
import parser.parsing.Parser;
import sml.NativeFunctionDeclarationNode;
import sml.data.returning.Nothing;
import sml.data.returning.VoidType;
import sml.data.tuple.Tuple;

public final class Run extends NativeFunctionDeclarationNode {

    public Run() {
        super("run",new String[1]);
        setBody(new Block(null));
        addParameter("code");
    }

    @Override
    public VoidType call(Tuple arguments, String callFileName, int callLine) {
        setArguments(arguments, callFileName, callLine);
        Parser.parse(Lexer.lex(getBody().getStringVariableValue("code", callFileName, callLine).toString(), callFileName, callLine)).run();
        return Nothing.nothing;
    }

}
