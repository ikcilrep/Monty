package stdlib.casts;

import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;
import parser.MontyException;

public class StringToFloat extends FunctionDeclarationNode {

	public StringToFloat() {
		super("stringToFloat", DataTypes.FLOAT);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("str", DataTypes.STRING));
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		String str = (String) getBody().getVariableByName("str").getValue();
		if (str.matches("[+-]?[0-9]+\\.[0-9]+"))
			return Float.parseFloat(str);
		else if (str.matches("[+-]?[0-9]+"))
			return (Float) (float) Integer.parseInt(str);
		else
			new MontyException("Unknown number format for float type:\t" + str);
		return null;
	}

}
