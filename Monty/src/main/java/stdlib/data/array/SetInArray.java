package stdlib.data.array;

import java.math.BigInteger;
import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;

public class SetInArray extends FunctionDeclarationNode {

	public SetInArray() {
		super("setInArray", DataTypes.ARRAY);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("arr", DataTypes.ARRAY));
		addParameter(new VariableDeclarationNode("index", DataTypes.INTEGER));
		addParameter(new VariableDeclarationNode("element", DataTypes.ANY));
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		var body = getBody();
		var arr = (Array) body.getVariableByName("arr").getValue();
		var index = ((BigInteger) body.getVariableByName("index").getValue()).intValue();
		var element = body.getVariableByName("element").getValue();
		return arr.set(index, element);
	}

}
