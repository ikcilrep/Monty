package AST;

public class Node implements Cloneable {
	NodeTypes nodeType;
	public Node copy() {
		try {
			return (Node) clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
}
