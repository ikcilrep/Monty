package sml.data.checking;

import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.StructDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;
import sml.Sml;

public class IsIterable extends FunctionDeclarationNode {

	public IsIterable() {
		super("isIterable", DataTypes.BOOLEAN);
		setBody(new Block(null));
		addParameter("toCheck", DataTypes.ANY);
	}

	public static boolean isIterable(Object toCheck, String callFileName, int callLine) {
		if (toCheck instanceof String)
			return true;
		if (!(toCheck instanceof StructDeclarationNode))
			return false;
		var structToCheck = (StructDeclarationNode) toCheck;
		if (!structToCheck.hasFunction("Iterator"))
			return false;
		var iterator = structToCheck.getFunction("Iterator");
		if (iterator.parameters.size() > 0)
			return false;
		var iteratorValue = iterator.call(Sml.emptyArgumentList, callFileName, callLine);
		if (!(iteratorValue instanceof StructDeclarationNode))
			return false;
		var iteratorStruct = (StructDeclarationNode) iteratorValue;
		if (!(iteratorStruct.hasFunction("hasNext") && iteratorStruct.hasFunction("next"))
				|| iteratorStruct.getFunction("next").getType().equals(DataTypes.VOID)
				|| !iteratorStruct.getFunction("hasNext").getType().equals(DataTypes.BOOLEAN))
			return false;
		return true;
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		return isIterable(getBody().getVariable("toCheck").getValue(), callFileName, callLine);
	}

}