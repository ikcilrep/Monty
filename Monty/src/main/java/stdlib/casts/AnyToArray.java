package stdlib.casts;

import java.util.ArrayList;

import ast.Block;
import ast.declarations.FunctionDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import parser.DataTypes;
import parser.MontyException;
import stdlib.data.array.Array;

public class AnyToArray extends FunctionDeclarationNode {

	public AnyToArray() {
		super("anyToFloat", DataTypes.BOOLEAN);
		setBody(new Block(null));
		addParameter(new VariableDeclarationNode("a", DataTypes.ANY));
	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		var a = getBody().getVariableByName("a").getValue();
		if (!(a instanceof Array))
			new MontyException("Can't cast this expression to integer:\t" + a.toString());
		return ((Array) a).copy();
	}

}
