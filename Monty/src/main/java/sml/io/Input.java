package sml.io;

import java.util.ArrayList;
import java.util.Scanner;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;

public class Input extends FunctionDeclarationNode {
	public static Scanner scanner = new Scanner(System.in);
	public Input() {
		super("input", DataTypes.STRING);
		setBody(new Block(null));
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		String line = scanner.nextLine();
		return line;
	}

}
