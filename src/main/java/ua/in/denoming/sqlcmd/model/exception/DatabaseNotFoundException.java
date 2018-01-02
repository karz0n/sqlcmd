package ua.in.denoming.sqlcmd.model.exception;

public class DatabaseNotFoundException extends RuntimeException {
    public DatabaseNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
