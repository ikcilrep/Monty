package sml.io;

import java.util.ArrayList;
import java.util.Scanner;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;

public class Input extends FunctionDeclarationNode {

	public Input() {
		super("input", DataTypes.STRING);
		setBody(new Block(null));
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		Scanner scanner = new Scanner(System.in);
		String line = scanner.nextLine();
		scanner.close();
		return line;
	}

}
