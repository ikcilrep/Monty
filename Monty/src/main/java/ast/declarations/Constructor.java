package ast.declarations;

import sml.NativeFunctionDeclarationNode;
import sml.data.tuple.Tuple;

public class Constructor extends NativeFunctionDeclarationNode {
    private final TypeDeclarationNode type;

    public Constructor(TypeDeclarationNode type) {
        super(type.getName(), FunctionDeclarationNode.EMPTY_PARAMETERS);
        this.type = type;
    }

    @Override
    public Object call(Tuple arguments, String callFileName, int callLine) {
        var newStruct = type.getParent().getType(type.getName(), callFileName, callLine).copy();
        newStruct.addThisVariable(callFileName, callLine);
        newStruct.incrementNumber();
        newStruct.run();

        if (newStruct.hasFunction("init"))
            newStruct.getFunction("init", callFileName, callLine).call(arguments, callFileName, callLine);

        return newStruct;
    }

    @Override
    public Constructor copy() {
        return new Constructor(type);
    }


    @Override
    public String toString() {
        return "Constructor<" + getName() + ">";
    }

}
