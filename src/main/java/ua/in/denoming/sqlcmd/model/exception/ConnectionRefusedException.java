package ua.in.denoming.sqlcmd.model.exception;

public class ConnectionRefusedException extends DatabaseException {
    public ConnectionRefusedException(String message) {
        this(message, null);
    }

    public ConnectionRefusedException(String message, Throwable cause) {
        super(message, cause);
    }
}
