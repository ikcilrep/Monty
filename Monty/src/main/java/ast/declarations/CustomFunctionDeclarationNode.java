/*
Copyright 2018 Szymon Perlicki

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package ast.declarations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ast.expressions.OperationNode;
import parser.DataTypes;
import parser.MontyException;

public class CustomFunctionDeclarationNode extends FunctionDeclarationNode {
	public CustomFunctionDeclarationNode(String name, DataTypes type) {
		super(name, type);

	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		var variables = new HashMap<String, VariableDeclarationNode>();
		var variablesSet = body.getVariables().entrySet();
		for (Map.Entry<String, VariableDeclarationNode> entry : variablesSet) {
			String key = entry.getKey();
			VariableDeclarationNode value = ((VariableDeclarationNode) entry.getValue());
			variables.put(key, value.copy());
		}
		setArguments(arguments);
		Object result = null;
		try {
			result = body.run();
		} catch (StackOverflowError e) {
			new MontyException("Stack overflow at " + name + " function call");
		}
		var resultDataType = DataTypes.getDataType(result);
		body.setVariables(variables);

		if (!resultDataType.equals(getType()))
			new MontyException("Function " + getName() + " should returns " + getType().toString().toLowerCase()
					+ ",\nbut returns " + resultDataType.toString().toLowerCase());
		return result;
	}
}
