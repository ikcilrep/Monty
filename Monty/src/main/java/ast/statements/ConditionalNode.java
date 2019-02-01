/*
Copyright 2018-2019 Szymon Perlicki

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUObject WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package ast.statements;

import ast.Block;
import ast.NodeTypes;
import ast.expressions.OperationNode;
import sml.casts.ToBoolean;

class ConditionalNode extends Block {
	OperationNode condition;

	public ConditionalNode(OperationNode condition, Block parent, NodeTypes nodeType) {
		super(parent, nodeType);
		this.condition = condition;
	}

	public boolean runnedCondition() {
		return ToBoolean.toBoolean(condition.run(), getFileName(), getLine());

	}
}
