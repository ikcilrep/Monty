package Parser.Exceptions;

import Monty.MontyException;

public class NoSuchVariableException extends MontyException {

	public NoSuchVariableException(String message) {
		super(message);
	}
}
