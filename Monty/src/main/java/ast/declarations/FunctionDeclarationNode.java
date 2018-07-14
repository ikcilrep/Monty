package ast.declarations;

import java.util.HashSet;

import ast.Block;
import ast.NodeTypes;
import parser.DataTypes;

public class FunctionDeclarationNode extends DeclarationNode {
	Block body;
	HashSet<VariableDeclarationNode> parameters = new HashSet<>();
	
	public FunctionDeclarationNode(String name, DataTypes type) {
		super(name, type);
		super.nodeType = NodeTypes.FUNCTION_DECLARATION;
	}
	public void addParameter(VariableDeclarationNode parameter) {
		parameters.add(parameter);
	}
	public Block getBody() {
		return body;
	}
	public void setBody(Block body) {
		this.body = body;
	}
	public HashSet<VariableDeclarationNode> getParameters() {
		return parameters;
	}
}
