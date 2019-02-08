
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
package ast.statements;

import ast.Block;
import ast.NodeWithParent;
import ast.expressions.VariableNode;
import parser.DataTypes;
import parser.LogError;
import sml.casts.ToBoolean;
import sml.casts.ToFloat;
import sml.casts.ToInt;
import sml.casts.ToString;

public final class ChangeToStatementNode extends NodeWithParent {
	private Block parent;
	private VariableNode variable;
	private DataTypes dataType;

	public ChangeToStatementNode(VariableNode toChangeType, DataTypes dataType, String fileName, int line,
			Block parent) {
		this.variable = toChangeType;
		this.dataType = dataType;
		this.fileName = fileName;
		this.line = line;
		this.parent = parent;
	}

	public DataTypes getDataType() {
		return dataType;
	}

	public VariableNode getVariable() {
		return variable;
	}

	@Override
	public Object run() {
		var newVariableType = getDataType();
		var variable = parent.getVariable(getVariable().getName(), getFileName(), getLine());
		if (!variable.isDynamic()) {
			int[] lines = { getLine(), variable.getLine() };
			String[] fileNames = { getFileName(), variable.getFileName() };
			new LogError("Can't change type of static variable:\tchange " + variable.getName() + " to "
					+ newVariableType.toString().toLowerCase(), fileNames, lines);
		}

		variable.setType(newVariableType);
		switch (newVariableType) {
		case INTEGER:
			variable.setValue(ToInt.toInt(variable.getValue(), getFileName(), getLine()));
			break;
		case BOOLEAN:
			variable.setValue(ToBoolean.toBoolean(variable.getValue(), getFileName(), getLine()));
			break;
		case REAL:
			variable.setValue(ToFloat.toFloat(variable.getValue(), getFileName(), getLine()));
			break;
		case STRING:
			variable.setValue(ToString.toString(variable.getValue(), getFileName(), getLine()));
			break;
		default:
			break;
		}
		return null;
	}

	@Override
	public void setParent(Block parent) {
		this.parent = parent;
	}

	@Override
	public NodeWithParent copy() {

		return null;
	}

}
