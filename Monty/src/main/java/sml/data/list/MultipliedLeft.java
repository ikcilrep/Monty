package sml.data.list;

import java.math.BigInteger;
import java.util.ArrayList;

import ast.expressions.OperationNode;
import parser.DataTypes;
import parser.LogError;
import sml.data.Method;

final class MultipliedLeft extends Method<List> {
	 MultipliedLeft(List parent) {
		super(parent, "$mul");
		addParameter("this");
		addParameter("times");
	}

	@Override
	public List call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		setArguments(arguments, callFileName, callLine);
		var _times = getBody().getVariableValue("times");
		int times = 0;
		if (_times instanceof Integer)
			times = (int) _times;
		else if (_times instanceof BigInteger) {
			var bigTimes = (BigInteger) _times;
			if (bigTimes.compareTo(DataTypes.INT_MAX) > 0)
				new LogError("Multiplier have to be less or equals 2^31-1.", callFileName, callLine);
			else if (bigTimes.compareTo(DataTypes.INT_MIN) < 0)
				new LogError("Multiplier have to be greater or equals -2^31.", callFileName, callLine);
			times = bigTimes.intValue();
		}
		return parent.multiplied(times);
	}
}
