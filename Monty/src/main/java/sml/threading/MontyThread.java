package sml.threading;

import ast.expressions.OperationNode;
import parser.MontyException;

public class MontyThread implements Runnable {
	private OperationNode expression;
	private Thread thread;

	public MontyThread(OperationNode expression) {
		this.expression = expression;
		thread = new Thread(this);
		try {
			thread.start();
		} catch (OutOfMemoryError e) {
			new MontyException("Out of memory in thread");
		}
	}

	@Override
	public void run() {
		expression.run();
	}

}
