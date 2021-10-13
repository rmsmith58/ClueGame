package clueGame;

public class BadConfigFormatException extends Exception {
	public BadConfigFormatException() {
		super("An error has occurred while attempting to load configuration files.");
	}
	
	public BadConfigFormatException(String message) {
		super(message);
	}
}
