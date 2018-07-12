package ast.declarations;

import java.util.ArrayList;
import java.util.List;

import ast.Block;
import ast.NodeTypes;
import parser.DataTypes;

public class FunctionDeclarationNode extends DeclarationNode {
	Block body;
	List<VariableDeclarationNode> parameters;
	ArrayList<VariableDeclarationNode> variables = new ArrayList<>();
	
	public FunctionDeclarationNode(String name, DataTypes type, List<VariableDeclarationNode> parameters) {
		super(name, type);
		this.parameters = parameters;
		super.nodeType = NodeTypes.FUNCTION_DECLARATION;
	}

}
