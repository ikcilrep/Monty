package stdlib.data.array;

import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;

public class ExtendArray extends FunctionDeclarationNode {

	public ExtendArray() {
		super("extendArray", DataTypes.VOID);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("arrayToBeExtended", DataTypes.ARRAY));
		addParameter(new VariableDeclarationNode("arrayToExtend", DataTypes.ARRAY));

	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		setArguments(arguments);
		var arrayToBeExtended = (Array) getBody().getVariableByName("arrayToBeExtended").getValue();
		var arrayToExtend = (Array) getBody().getVariableByName("arrayToExtend").getValue();
		
		return arrayToBeExtended.copy().append(arrayToExtend);
	}

}
