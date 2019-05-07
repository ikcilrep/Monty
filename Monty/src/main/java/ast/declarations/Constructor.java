package ast.declarations;

import sml.NativeFunctionDeclarationNode;
import sml.data.tuple.Tuple;

public class Constructor extends NativeFunctionDeclarationNode {
    private final StructDeclarationNode struct;

    Constructor(StructDeclarationNode struct) {
        super(struct.getName(), FunctionDeclarationNode.EMPTY_PARAMETERS);
        this.struct = struct;
    }

    @Override
    public Object call(Tuple arguments, String callFileName, int callLine) {
        var newStruct = struct.getParent().getStructure(struct.getName(), callFileName, callLine).copy();
        var thisVariable = new VariableDeclarationNode("This");
        thisVariable.setValue(newStruct, callFileName, callLine);
        thisVariable.setConst(true);
        newStruct.addVariable(thisVariable, callFileName, callLine);
        newStruct.incrementNumber();
        newStruct.run();

        if (newStruct.hasFunction("init"))
            newStruct.getFunction("init", callFileName, callLine).call(arguments, callFileName, callLine);

        return newStruct;
    }

    @Override
    public Constructor copy() {
        return new Constructor(struct);
    }


    @Override
    public String toString() {
        return "Constructor<"+getName()+">";
    }

}
