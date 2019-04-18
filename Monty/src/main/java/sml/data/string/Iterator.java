package sml.data.string;

import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.expressions.OperationNode;
import sml.Sml;
import sml.data.StaticStruct;
import sml.functional.iterable.string.IterableString;

public class Iterator extends FunctionDeclarationNode{

	public Iterator(StaticStruct struct) {
		super("Iterator");
		setBody(new Block(null));
		addParameter("str");
		struct.addFunction(this);
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		return new IterableString(getBody().getStringVariableValue("str")).getFunction("Iterator").call(Sml.emptyArgumentList, callFileName, callLine);
	}

}
