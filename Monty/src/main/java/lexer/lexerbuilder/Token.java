 package lexer.lexerbuilder;

public interface Token<T> {
	String text = "";
	public void setText(String text);

	public String getText();

	public T copy() throws CloneNotSupportedException;
}
