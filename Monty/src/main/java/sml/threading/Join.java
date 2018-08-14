package sml.threading;

import java.math.BigInteger;
import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;
import parser.MontyException;
import sml.data.returning.Nothing;

public class Join extends FunctionDeclarationNode {

	public Join() {
		super("join", DataTypes.VOID);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("millis", DataTypes.INTEGER));
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		var millis = ((BigInteger) getBody().getVariableByName("millis").getValue()).intValue();
		try {
			Thread.currentThread().join((long) millis);
		} catch (InterruptedException e) {
			new MontyException("Join for " + millis + " was interrupted");
		}
		return Nothing.nothing;
	}

}