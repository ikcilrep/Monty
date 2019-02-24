package sml.data.string;

import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;
import sml.data.array.Array;

public class Split extends FunctionDeclarationNode {

	public Split() {
		super("split", DataTypes.ANY);
		setBody(new Block(null));
		addParameter("str", DataTypes.STRING);
		addParameter("regex", DataTypes.STRING);
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		var body = getBody();
		var str = (String)body.getVariable("str").getValue();
		var regex = (String)body.getVariable("regex").getValue();
		return new Array(str.split(regex));
	}

}
