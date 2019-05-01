package ast.declarations;

import ast.Block;
import ast.expressions.OperationNode;

import java.util.ArrayList;

public class Constructor extends FunctionDeclarationNode {
    private StructDeclarationNode struct;

    Constructor(StructDeclarationNode struct) {
        super(struct.getName());
        setStruct(struct);
        setBody(new Block(null));
    }

    @Override
    public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
        var newStruct = struct.getParent().getStructure(name, callFileName, callLine).copy();
        var thisVariable = new VariableDeclarationNode("This");
        thisVariable.setValue(newStruct);
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
        try {
            return (Constructor) clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public StructDeclarationNode getStruct() {
        return struct;
    }

    public void setStruct(StructDeclarationNode struct) {
        this.struct = struct;
    }

}
