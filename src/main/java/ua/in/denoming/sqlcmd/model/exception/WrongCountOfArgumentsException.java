package ua.in.denoming.sqlcmd.model.exception;

public class WrongCountOfArgumentsException extends RuntimeException {
    public WrongCountOfArgumentsException() {
        super("Wrong count of arguments");
    }

    public WrongCountOfArgumentsException(int expected, int received) {
        super(String.format("Wrong count of arguments, expected %s, received %s", expected, received));
    }
}
