package stdlib;

import java.util.ArrayList;
import java.util.Scanner;

import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;

public class Input extends FunctionDeclarationNode {

	public Input() {
		super("input", DataTypes.STRING);
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		Scanner scanner = new Scanner(System.in);
		String line = scanner.nextLine();
		scanner.close();
		return line;
	}

}
