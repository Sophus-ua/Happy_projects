package exceptions;

public class LoginAndRegistrationException extends RuntimeException {
    public LoginAndRegistrationException() {
        super();
    }

    public LoginAndRegistrationException(String message) {
        super(message);
    }

    public LoginAndRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginAndRegistrationException(Throwable cause) {
        super(cause);
    }
}
