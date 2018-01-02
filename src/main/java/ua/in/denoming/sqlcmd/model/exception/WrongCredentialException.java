package ua.in.denoming.sqlcmd.model.exception;

public class WrongCredentialException extends DatabaseException {
    public WrongCredentialException(String message) {
        this(message, null);
    }

    public WrongCredentialException(String message, Throwable cause) {
        super(message, cause);
    }
}
