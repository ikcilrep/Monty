package ast.declarations;

import java.util.ArrayList;

import ast.Block;
import ast.NodeTypes;
import ast.expressions.OperationNode;
import parser.DataTypes;


public abstract class FunctionDeclarationNode extends DeclarationNode {
	Block body;
	public ArrayList<VariableDeclarationNode> parameters = new ArrayList<>();

	public FunctionDeclarationNode(String name, DataTypes type) {
		super(name, type);
		super.nodeType = NodeTypes.FUNCTION_DECLARATION;
	}

	public void addParameter(VariableDeclarationNode parameter) {
		parameters.add(parameter);
	}
	public abstract Object call(ArrayList<OperationNode> arguments);


	public Block getBody() {
		return body;
	}

	public ArrayList<VariableDeclarationNode> getParameters() {
		return parameters;
	}

	public void setBody(Block body) {
		this.body = body;
	}
}
