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

import parser.DataTypes;
import parser.LogError;

public final class VariableDeclarationNode extends DeclarationNode implements Cloneable {

	private boolean isDynamic = false;
	private boolean isConst = false;

	public boolean isConst() {
		return isConst;
	}

	public void setConst(boolean isConst) {
		this.isConst = isConst;
	}

	private Object value;

	public VariableDeclarationNode(String name, DataTypes type) {
		super(name, type);
	}

	public final VariableDeclarationNode copy() {
		try {
			return (VariableDeclarationNode) clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	public final Object getValue() {
		return value;
	}

	public final boolean isDynamic() {
		return isDynamic;
	}

	public final void setDynamic(boolean isDynamic) {
		this.isDynamic = isDynamic;
	}

	public final void setValue(Object value, String fileName, int line) {
		if (isConst)
			new LogError("Can't change value of const.", fileName, line);
		this.value = value;
	}

	public final void setValue(Object value) {
		this.value = value;
	}

}
