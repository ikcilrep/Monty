package sml.io;

import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;
import sml.data.returning.Nothing;

public class Println extends FunctionDeclarationNode{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1275811232488303537L;

	public Println() {
		super("println", DataTypes.VOID);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("toPrint", DataTypes.ANY));
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		System.out.println(getBody().getVariableByName("toPrint").getValue());
		return Nothing.nothing;
	}

}
