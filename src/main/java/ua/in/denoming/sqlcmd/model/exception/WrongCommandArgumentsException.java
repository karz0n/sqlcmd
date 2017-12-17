package ua.in.denoming.sqlcmd.model.exception;

public class WrongCommandArgumentsException extends RuntimeException {
    public WrongCommandArgumentsException() {
        super("Wrong command arguments");
    }
}
