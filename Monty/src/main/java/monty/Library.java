package monty;

import java.util.HashMap;

public abstract class Library {
	private HashMap<String, Object> children = new HashMap<>();
	private String name;

	public HashMap<String, Object> getSublibraries() {
		return children;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public abstract void setLibrary();

	protected Library(String name) {
		this.name = name;
		setLibrary();
	}
}
