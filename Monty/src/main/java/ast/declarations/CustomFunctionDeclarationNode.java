/*
Copyright 2018-2019 Szymon Perlicki

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
import parser.LogError;
import sml.data.returning.BreakType;
import sml.data.returning.Nothing;

public final class CustomFunctionDeclarationNode extends FunctionDeclarationNode {

	public CustomFunctionDeclarationNode(String name) {
		super(name);
	}

	@Override
	public final Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		var variables = new HashMap<String, VariableDeclarationNode>();
		var variablesSet = body.getVariables().entrySet();
		for (Map.Entry<String, VariableDeclarationNode> entry : variablesSet) {
			String key = entry.getKey();
			VariableDeclarationNode value = (entry.getValue());
			variables.put(key, value.copy());
		}
		setArguments(arguments, callFileName, callLine);
		String[] fileNames = { callFileName, getFileName() };
		int[] lines = { callLine, getLine() };
		Object result = null;
		try {
			result = body.run();
		} catch (StackOverflowError e) {
			new LogError("Stack overflow at " + name + " function call", fileNames, lines);
		}

		if (result == null)
			result = Nothing.nothing;
		else {
			if (result instanceof BreakType)
				new LogError("Trying to break function " + getName(), fileNames, lines);
			if (result instanceof ContinueStatementNode)
				new LogError("Trying to continue function " + getName(), fileNames, lines);
		}
		body.setVariables(variables);
		return result;
	}
}
