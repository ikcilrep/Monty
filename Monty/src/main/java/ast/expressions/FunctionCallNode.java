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

package ast.expressions;

import java.util.ArrayList;

public final class FunctionCallNode extends NamedExpression implements Cloneable {

	private String name;
	private ArrayList<OperationNode> arguments = new ArrayList<>();

	public FunctionCallNode(String name) {
		this.name = name;
	}

	public final void addArgument(OperationNode argument) {
		arguments.add(argument);
	}

	public final ArrayList<OperationNode> getArguments() {
		return arguments;
	}

	public void setArguments(ArrayList<OperationNode> arguments) {
		this.arguments = arguments;
	}

	@Override
	public final String getName() {
		return name;
	}

	public FunctionCallNode copy() {
		try {
			var copied = (FunctionCallNode) clone();
			var copyOfArguments = new ArrayList<OperationNode>(arguments.size());
			for (var argument : arguments)
				copyOfArguments.add(argument.copy());
			copied.setArguments(copyOfArguments);
			return copied;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;

	}

}
