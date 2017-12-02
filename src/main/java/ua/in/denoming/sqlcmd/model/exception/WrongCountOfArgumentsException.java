package ua.in.denoming.sqlcmd.model.exception;

public class WrongCountOfArgumentsException extends RuntimeException {
    public WrongCountOfArgumentsException() {
        super("Wrong command arguments");
    }
}
