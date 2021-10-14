package clueGame;

/**
 * Exception for Board object to be thrown if abnormal data is found in the config files.
 * 
 * @author Ryne Smith
 * @author Mikayla Sherwood
 *
 */
public class BadConfigFormatException extends Exception {
	public BadConfigFormatException() {
		super("An error has occurred while attempting to load configuration files.");
	}
	
	public BadConfigFormatException(String message) {
		super(message);
	}
}
