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
import java.util.Arrays;

import ast.Block;
import ast.expressions.OperationNode;
import parser.LogError;

public abstract class FunctionDeclarationNode extends DeclarationNode implements Cloneable {
	Block body;
	private int parametersSize = 0;
	public ArrayList<VariableDeclarationNode> parameters = new ArrayList<>();

	public FunctionDeclarationNode(String name) {
		super(name);
	}

	public void addParameter(String name) {
		parameters.add(new VariableDeclarationNode(name));
		parametersSize++;
	}

	public abstract Object call(ArrayList<OperationNode> arguments, String callFileName, int callLine);

	public FunctionDeclarationNode copy() {
		try {
			var copied = (FunctionDeclarationNode) clone();
			copied.setBody(body.copy());
			return copied;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Block getBody() {
		return body;
	}

	public ArrayList<VariableDeclarationNode> getParameters() {
		return parameters;
	}

	public void setArguments(ArrayList<OperationNode> arguments, String callFileName, int callLine) {
		var argumentsSize = arguments.size();
		if (argumentsSize > parametersSize)
			new LogError("Too many arguments in " + name + " function call.", callFileName, callLine);
		else if (argumentsSize < parametersSize)
			new LogError("Too few arguments in " + name + " function call.", callFileName, callLine);
		if (argumentsSize == 0)
			return;
		var runnedArguments = new ArrayList<Object>(Arrays.asList(new Object[arguments.size()]));

		for (int i = 0; i < arguments.size(); i++)
			runnedArguments.set(i, arguments.get(i).run());

		for (int i = 0; i < runnedArguments.size(); i++) {
			var name = parameters.get(i).getName();
			VariableDeclarationNode variable = null;
			if (!body.hasVariable(name)) {
				variable = new VariableDeclarationNode(name);
				body.addVariable(variable, fileName, line);
			} else
				variable = body.getVariable(name, getFileName(), getLine());
			variable.setValue(runnedArguments.get(i));
			variable.setConst(Character.isUpperCase(name.charAt(0)));
		}
	}

	public void setBody(Block body) {
		this.body = body;
	}
}
