package ast.expressions;

import java.util.ArrayList;

public class FunctionCallNode extends ExpressionNode {
	private String name;
	private ArrayList<ExpressionNode> arguments = new ArrayList<>();

	public FunctionCallNode(String name) {
		this.name = name;
	}

	public void addArgument(ExpressionNode argument) {
		arguments.add(argument);
	}

	public String getName() {
		return name;
	}

	public ArrayList<ExpressionNode> getArguments() {
		return arguments;
	}
}
