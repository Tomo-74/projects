/**
 * Custom Exception Class. A LogInException is thrown if the user's ID number provided at log in
 * does not match a user in the database.
 *
 * @author Thomas Lonowski
 * @date 26 February 2023
 * @extends Exception
 */
@SuppressWarnings("serial")
public class LogInException extends Exception {
	public LogInException() {
		super();
	}
	
	public LogInException(String message) {
            super(message);
    }
}
