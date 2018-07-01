package AST;

public class VariableNode extends ExpressionNode {
	private String name;

	public VariableNode(String name) {
		this.name = name;
		super.nodeType = NodeTypes.VARIABLE;
	}

	public String getName() {
		return name;
	}

}
