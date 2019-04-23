package sml.data.string;

import java.math.BigInteger;
import java.util.ArrayList;

import ast.expressions.OperationNode;
import parser.DataTypes;
import parser.LogError;
import sml.data.Method;

public class MultiplyRight extends Method<StringStruct> {

	public MultiplyRight(StringStruct parent) {
		super(parent, "$r_mul");
		addParameter("times");
		addParameter("this");

	}

	@Override
	public Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		var _times = getBody().getVariable("times").getValue();
		int times = -1;
		if (_times instanceof Integer)
			times = (int)_times;
		else if (_times instanceof BigInteger) {
			if (((BigInteger) _times).compareTo(DataTypes.INT_MIN) < 0)
				new LogError("Multiplicand has to be greater than -2^31.", callFileName, callLine);
			else if(((BigInteger) _times).compareTo(DataTypes.INT_MAX) > 0)
				new LogError("Multiplicand has to be less than 2^31 - 1.", callFileName, callLine);
			times = ((BigInteger) _times).intValue();
		} else
			new LogError("Multiplicand has to be integer.", callFileName,callLine);
		return parent.mulitply(times);
	}

}
