package sml.functional.function;

import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.StructDeclarationNode;
import ast.expressions.OperationNode;

public class Function extends StructDeclarationNode {
	FunctionDeclarationNode function;

	public Function(FunctionDeclarationNode function) {
		super(null, "Function");
		this.function = function;
		incrementNumber();
		new Call(this);
	}
	@Override
	public void setParent(Block parent) {
		super.setParent(parent);
		function.getBody().setParent(parent);
	}
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		return function.call(arguments, callFileName, callLine);
	}
}
