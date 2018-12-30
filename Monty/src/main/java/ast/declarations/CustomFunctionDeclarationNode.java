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
import ast.statements.ContinueStatementNode;
import parser.DataTypes;
import parser.LogError;
import sml.data.returning.BreakType;
import sml.data.returning.Nothing;

public class CustomFunctionDeclarationNode extends FunctionDeclarationNode {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1475119303503798509L;

	public CustomFunctionDeclarationNode(String name, DataTypes type) {
		super(name, type);

	}

	@Override
	public Object call(ArrayList<OperationNode> arguments) {
		var variables = new HashMap<String, VariableDeclarationNode>();
		var variablesSet = body.getVariables().entrySet();
		for (Map.Entry<String, VariableDeclarationNode> entry : variablesSet) {
			String key = entry.getKey();
			VariableDeclarationNode value = (entry.getValue());
			variables.put(key, value.copy());
		}
		setArguments(arguments);
		Object result = null;
		try {
			result = body.run();
		} catch (StackOverflowError e) {
			new LogError("Stack overflow at " + name + " function call", getLastFileName(), getLastLine());
		}
		if (result == null)
			result = Nothing.nothing;
		if (result instanceof BreakType)
			new LogError("Trying to break function " + getName(), getLastFileName(), getLastLine());
		if (result instanceof ContinueStatementNode)
			new LogError("Trying to continue function " + getName(), getLastFileName(), getLastLine());
		body.setVariables(variables);
		var resultDataType = DataTypes.getDataType(result);
		if (resultDataType == null)
			resultDataType = DataTypes.VOID;
		if (!resultDataType.equals(getType()))
			new LogError("Function " + getName() + " should returns " + getType().toString().toLowerCase()
					+ ",\nbut returns " + resultDataType.toString().toLowerCase(), getLastFileName(), getLastLine());
		return result;
	}
}
