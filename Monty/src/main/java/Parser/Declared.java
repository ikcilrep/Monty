package Parser;

import java.util.ArrayList;

import AST.VariableDeclarationNode;
import Parser.Exceptions.NoSuchVariableException;

public class Declared {
	public static ArrayList<VariableDeclarationNode> globalVariables = new ArrayList<VariableDeclarationNode>();

	public VariableDeclarationNode getVariableByName(String name) {

		for (Object e : globalVariables) {
			VariableDeclarationNode variable = (VariableDeclarationNode) e;
			if (variable.getName().equals(name))
				return variable;
		}
		new NoSuchVariableException("No such variable like " + name + '.');
		return null;
	}
}
