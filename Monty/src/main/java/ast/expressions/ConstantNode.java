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

import ast.Node;
import parser.DataTypes;

public final class ConstantNode extends Node {
	private Object value;
	private DataTypes type;

	public ConstantNode(Object value, DataTypes type) {
		this.value = value;
		this.type = type;
	}

	public final DataTypes getType() {
		return type;
	}

	public final Object getValue() {
		return value;
	}

}
