package stdlib.data.array;

import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;

public class ReplaceFirstInArray extends FunctionDeclarationNode{

	public ReplaceFirstInArray() {
		super("replaceFirstInArray", DataTypes.ARRAY);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("arr", DataTypes.ARRAY));
		addParameter(new VariableDeclarationNode("toBeReplaced", DataTypes.ANY));
		addParameter(new VariableDeclarationNode("replacement", DataTypes.ANY));


	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		var body = getBody();
		var arr = (Array)body.getVariableByName("arr").getValue();
		var toBeReplaced = body.getVariableByName("toBeReplaced").getValue();
		var replacement = body.getVariableByName("replacement").getValue();

		return arr.replaceFirst(toBeReplaced, replacement);
	}

}
