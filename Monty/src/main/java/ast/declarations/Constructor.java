package ast.declarations;

import java.util.ArrayList;

import ast.Block;
import ast.expressions.OperationNode;

public class Constructor extends FunctionDeclarationNode {
	private StructDeclarationNode struct;

	public StructDeclarationNode getStruct() {
		return struct;
	}

	public void setStruct(StructDeclarationNode struct) {
		this.struct = struct;
	}

	public Constructor(StructDeclarationNode struct) {
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
		newStruct.addVariable(thisVariable);
		newStruct.incrementNumber();
		if (newStruct.hasFunction("init"))
			newStruct.getFunction("init").call(arguments, callFileName, callLine);
		newStruct.run();
		return newStruct;
	}

}
