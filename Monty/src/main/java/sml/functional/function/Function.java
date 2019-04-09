package sml.functional.function;


import ast.declarations.FunctionDeclarationNode;
import ast.declarations.StructDeclarationNode;

public class Function extends StructDeclarationNode {
	FunctionDeclarationNode function;
	public Function(FunctionDeclarationNode function) {
		super(null, "Function");
		this.function = function;
		new Call(this);
	}


}
