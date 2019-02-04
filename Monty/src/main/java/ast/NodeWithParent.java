package ast;

public abstract class NodeWithParent extends Node {
	public abstract void setParent(Block parent);

	public abstract NodeWithParent copy();
}
