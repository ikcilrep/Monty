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

package ast.statements;

import ast.Block;
import ast.NodeTypes;
import ast.expressions.OperationNode;

public class IfStatementNode extends Block {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2857473091602756554L;
	private OperationNode condition;
	private Block elseBody;

	public IfStatementNode(Block parent, OperationNode condition) {
		super(parent);
		this.condition = condition;
		super.nodeType = NodeTypes.IF_STATEMENT;
	}

	private boolean isInElse = false;

	public boolean isInElse() {
		return isInElse;
	}

	public void setInElse(boolean isInElse) {
		this.isInElse = isInElse;
	}

	public Block getElseBody() {
		return elseBody;
	}

	public void setElseBody(Block elseBody) {
		this.elseBody = elseBody;
	}

	public OperationNode getCondition() {
		return condition;
	}
}
