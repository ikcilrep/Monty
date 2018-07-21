package stdlib.data.array;

import java.math.BigInteger;
import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;

public class getFromArray extends FunctionDeclarationNode {

	public getFromArray() {
		super("getFromArray", DataTypes.ANY);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("arr", DataTypes.ARRAY));
		addParameter(new VariableDeclarationNode("index", DataTypes.INTEGER));
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		var arr = (Array) getBody().getVariableByName("arr").getValue();
		var index = (BigInteger) getBody().getVariableByName("index").getValue();
		return arr.get(index.intValue());
	}

}
