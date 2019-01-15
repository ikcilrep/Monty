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

package sml.threading;

import ast.expressions.OperationNode;
import parser.LogError;

public class MontyThread implements Runnable {
	private OperationNode expression;
	private Thread thread;

	public MontyThread(OperationNode expression) {
		this.expression = expression;
		thread = new Thread(this);
		try {
			thread.start();
		} catch (OutOfMemoryError e) {
			new LogError("Out of memory in thread");
		}
	}

	@Override
	public void run() {
		expression.run();
	}

}
