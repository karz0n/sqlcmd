package ua.in.denoming.sqlcmd.model.exception;

public class WrongArgumentsException extends RuntimeException {
    public WrongArgumentsException() {
        super("Wrong arguments");
    }

    public WrongArgumentsException(String message) {
        super(message);
    }
}
