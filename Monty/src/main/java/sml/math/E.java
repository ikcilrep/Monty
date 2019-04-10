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

package sml.math;

import java.math.BigDecimal;

import ast.declarations.VariableDeclarationNode;
import parser.DataTypes;

public final class E extends VariableDeclarationNode {
	private final static BigDecimal e = new BigDecimal(
			"2.718281828459045235360287471352662497757247093699959574966967627724076630353");
	
	public E() {
		super("E", DataTypes.REAL);
		setValue(e);
		setConst(true);
	}



}
