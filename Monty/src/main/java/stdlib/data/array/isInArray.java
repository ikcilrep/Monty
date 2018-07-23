package stdlib.data.array;

import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;

public class isInArray extends FunctionDeclarationNode {

	public isInArray() {
		super("isInArray", DataTypes.BOOLEAN);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("arr", DataTypes.ARRAY));
		addParameter(new VariableDeclarationNode("element", DataTypes.ANY));

	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		var body = getBody();
		var arr = (Array) body.getVariableByName("arr").getValue();
		var element = body.getVariableByName("element").getValue();

		return arr.contains(element);
	}

}
