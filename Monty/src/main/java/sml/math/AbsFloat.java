package sml.math;

import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;

public class AbsFloat extends FunctionDeclarationNode {

	public AbsFloat() {
		super("absFloat", DataTypes.FLOAT);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("floating", DataTypes.FLOAT));
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		var floating = (Float) getBody().getVariableByName("floating").getValue();
		if (floating < 0)
			return 0 - floating;
		return floating;
	}

}
