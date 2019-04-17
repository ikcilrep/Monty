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
import ast.declarations.StructDeclarationNode;
import ast.declarations.VariableDeclarationNode;
import ast.expressions.OperationNode;
import sml.Sml;
import sml.data.checking.IsIterable;
import sml.data.returning.BreakType;
import sml.data.returning.ContinueType;
import sml.data.returning.Nothing;
import sml.functional.iterable.string.IterableString;

public final class ForStatementNode extends Block {
	private OperationNode iterable;
	private String variableName;

	public ForStatementNode(String variableName, OperationNode array, String fileName, int line, Block parent) {
		super(parent);
		this.variableName = variableName;
		this.iterable = array;
		this.fileName = fileName;
		this.line = line;
	}

	public OperationNode getIterable() {
		return iterable;
	}

	public String getVariableName() {
		return variableName;
	}

	@Override
	public void setParent(Block parent) {
		super.setParent(parent);
		iterable.setParent(parent);
	}

	@Override
	public Object run() {
		Object result = null;
		var name = getVariableName();
		var isNotNameUnderscore = !name.equals("_");
		var isConst = Character.isUpperCase(name.charAt(0));
		var toIter = getIterable().run();
		if (toIter instanceof String)
			toIter = new IterableString((String) toIter);
		else if (!IsIterable.isIterable(toIter, fileName, line)) {
			VariableDeclarationNode variable = null;
			if (isNotNameUnderscore)
				if (hasVariable(name))
					variable = getVariable(name, getFileName(), getLine());
				else
					addVariable(variable = new VariableDeclarationNode(name));
			variable.setConst(false);
			variable.setValue(toIter);
			variable.setConst(isConst);
			result = super.run();
			if (result instanceof BreakType || result instanceof ContinueType)
				return Nothing.nothing;
			return result;
		}
		var iterator = (StructDeclarationNode) ((StructDeclarationNode) toIter).getFunction("Iterator")
				.call(Sml.emptyArgumentList, fileName, line);
		var hasNext = iterator.getFunction("hasNext");
		var next = iterator.getFunction("next");
		VariableDeclarationNode variable = null;
		if (isNotNameUnderscore)
			if (hasVariable(name))
				variable = getVariable(name, getFileName(), getLine());
			else
				addVariable(variable = new VariableDeclarationNode(name));

		if (isNotNameUnderscore)
			while ((boolean) hasNext.call(Sml.emptyArgumentList, fileName, line)) {
				variable.setConst(false);
				variable.setValue(next.call(Sml.emptyArgumentList, fileName, line));
				variable.setConst(isConst);
				result = super.run();
				if (result instanceof BreakType)
					break;
				if (result instanceof ContinueType)
					continue;
				if (result != null)
					return result;
			}
		else
			while ((boolean) hasNext.call(Sml.emptyArgumentList, fileName, line)) {
				next.call(Sml.emptyArgumentList, fileName, line);
				result = super.run();
				if (result instanceof BreakType)
					break;
				if (result instanceof ContinueType)
					continue;
				if (result != null)
					return result;
			}
		return null;
	}
}
