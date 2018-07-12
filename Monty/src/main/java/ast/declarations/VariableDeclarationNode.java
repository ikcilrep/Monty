package ast.declarations;

import ast.NodeTypes;
import parser.DataTypes;

public class VariableDeclarationNode extends DeclarationNode {

	public VariableDeclarationNode(String name, DataTypes type) {
		super(name, type);
		super.nodeType = NodeTypes.VARIABLE_DECLARATION;
	}

}
