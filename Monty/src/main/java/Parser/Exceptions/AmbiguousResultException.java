package Parser.Exceptions;

import Monty.MontyException;

public class AmbiguousResultException extends MontyException {

	public AmbiguousResultException(String message) {
		super(message);
	}
}
