package ast.declarations;

import parser.LogError;
import sml.data.tuple.Tuple;

public abstract class NamedFunctionDeclarationNode extends FunctionDeclarationNode {


    protected String name;

    NamedFunctionDeclarationNode(String name, String[] parameters, int lastNotNullParameterIndex) {
        super(parameters, lastNotNullParameterIndex);
        this.name = name;
    }

    public NamedFunctionDeclarationNode(String name, String[] parameters) {
        super(parameters);
        this.name = name;
    }

    public abstract Object call(Tuple arguments, String callFileName, int callLine);

    public abstract NamedFunctionDeclarationNode copy();

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Function<" + name + ">";
    }

    protected void checkArgumentsSize(int argumentsLength, String fileName, int line) {
        if (argumentsLength > parameters.length)
            new LogError("Too many arguments in " + name + " function call.", fileName, line);
        if (argumentsLength < parameters.length)
            new LogError("Too few arguments in " + name + " function call.", fileName, line);
    }


}
