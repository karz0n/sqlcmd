package ua.in.denoming.sqlcmd.model.exception;

public class DatabaseException extends Exception {
    @SuppressWarnings("WeakerAccess")
    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
