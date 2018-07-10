package Parser.Exceptions;

public class MontyException {
	protected MontyException(String message) {
		System.out.println(message);
		System.exit(0);
	}
}
