package ua.in.denoming.sqlcmd.model.exception;

public class WrongPasswordException extends DatabaseException {
    public WrongPasswordException() {
        super("Incorrect password");
    }
}
