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
import ast.ConditionalNode;
import ast.expressions.OperationNode;

public class IfStatementNode extends ConditionalNode {

	private Block elseBody;

	private boolean isInElse = false;

	public IfStatementNode(Block parent, OperationNode condition, String fileName, int line) {
		super(condition, parent);
		this.condition = condition;
		this.fileName = fileName;
		this.line = line;
	}

	public Block getElseBody() {
		return elseBody;
	}

	public boolean isInElse() {
		return isInElse;
	}

	public void setElseBody(Block elseBody) {
		this.elseBody = elseBody;
	}

	public void setInElse(boolean isInElse) {
		this.isInElse = isInElse;
	}
	
	@Override
	public Object run() {
		if (runnedCondition()) {
			return super.run();
		} else if (elseBody != null) {
			return elseBody.run();
		}
		return null;
	}
}
