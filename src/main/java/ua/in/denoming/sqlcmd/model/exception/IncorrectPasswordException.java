package ua.in.denoming.sqlcmd.model.exception;

public class IncorrectPasswordException extends DatabaseException {
    public IncorrectPasswordException() {
        super("Incorrect password");
    }
}
