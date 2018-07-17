package ast.declarations;

import java.util.ArrayList;

import ast.Block;
import ast.NodeTypes;
import ast.expressions.OperationNode;
import parser.DataTypes;
import parser.MontyException;

public class FunctionDeclarationNode extends DeclarationNode {
	Block body;
	ArrayList<VariableDeclarationNode> parameters = new ArrayList<>();

	public FunctionDeclarationNode(String name, DataTypes type) {
		super(name, type);
		super.nodeType = NodeTypes.FUNCTION_DECLARATION;
	}

	public void addParameter(VariableDeclarationNode parameter) {
		parameters.add(parameter);
	}

	public Object call(ArrayList<OperationNode> arguments) {
		if (arguments.size() > parameters.size())
			new MontyException("Too many arguments in " + name + " function call.");
		else if (arguments.size() < parameters.size())
			new MontyException("Too few arguments in " + name + " function call.");
		for (int i = 0; i < arguments.size(); i++) {
			var name = parameters.get(i).getName();
			var dataType = parameters.get(i).getType();
			if (!body.doesContainVariable(name))
				body.addVariable(new VariableDeclarationNode(name, dataType));
			body.getVariableByName(name).setValue(arguments.get(i).run());
		}
		var result = body.run();
		if (result != null)
			return ((OperationNode) result).run();
		return result;
	}

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
