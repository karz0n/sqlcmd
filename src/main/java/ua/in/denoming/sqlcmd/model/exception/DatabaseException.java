package ua.in.denoming.sqlcmd.model.exception;

public class DatabaseException extends RuntimeException {
    public DatabaseException(String message) {
        this(message, null);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
