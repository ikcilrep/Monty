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
import parser.DataTypes;
import parser.LogError;
import sml.casts.ToReal;

public abstract class FunctionDeclarationNode extends DeclarationNode implements Cloneable {
	Block body;

	public ArrayList<VariableDeclarationNode> parameters = new ArrayList<>();

	public FunctionDeclarationNode(String name, DataTypes type) {
		super(name, type);
	}

	public void addParameter(String name, DataTypes type) {
		parameters.add(new VariableDeclarationNode(name, type));
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
		if (arguments.size() > parameters.size())
			new LogError("Too many arguments in " + name + " function call", callFileName, callLine);
		else if (arguments.size() < parameters.size())
			new LogError("Too few arguments in " + name + " function call", callFileName, callLine);
		var runnedArguments = new ArrayList<Object>(Arrays.asList(new Object[arguments.size()]));

		for (int i = 0; i < arguments.size(); i++) {
			var dataType = parameters.get(i).getType();
			var argument = arguments.get(i);
			var value = argument.run();
			var argumentDataType = DataTypes.getDataType(value);
			if (dataType.equals(DataTypes.REAL) && argumentDataType.equals(DataTypes.INTEGER)) {
				value = ToReal.toReal(value, callFileName, callLine);
				argumentDataType = DataTypes.REAL;
			}
			if (!dataType.equals(DataTypes.ANY))
				if (!argumentDataType.equals(dataType))
					new LogError("Wrong data type for" + i + "parameter with name\n\"" + name + "\" in " + getName()
							+ " function call expected " + dataType.toString().toLowerCase() + " got "
							+ argumentDataType.toString().toLowerCase(), callFileName, callLine);

			runnedArguments.set(i, value);
		}
		for (int i = 0; i < runnedArguments.size(); i++) {
			var name = parameters.get(i).getName();
			VariableDeclarationNode variable = null;
			if (!body.hasVariable(name)) {
				variable = new VariableDeclarationNode(name, parameters.get(i).getType());
				body.addVariable(variable, fileName, line);
			} else
				variable = body.getVariable(name, getFileName(), getLine());
			variable.setValue(runnedArguments.get(i));
		}
	}

	public void setBody(Block body) {
		this.body = body;
	}
}
