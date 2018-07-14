package lexer;
import lexer.lexerbuilder.Token;

public class MontyToken implements Token<MontyToken>, Cloneable{
	private String text;
	TokenTypes type;
	public MontyToken(TokenTypes type) {
		this.type = type;
	}
	public TokenTypes getType() {
		return type;
	}
	@Override
	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public MontyToken copy() throws CloneNotSupportedException {
		return (MontyToken) super.clone();
	}

}
