package ua.in.denoming.sqlcmd.view;

public interface View {
    @SuppressWarnings("UnusedReturnValue")
    View throwing(Throwable throwable);

    View write(String message);

    default View writeLine(String message) {
        write(message);
        return line();
    }

    @SuppressWarnings("UnusedReturnValue")
    View writeFormat(String format, Object... objects);

    @SuppressWarnings("UnusedReturnValue")
    default View writeFormatLine(String format, Object... objects) {
        writeFormat(format, objects);
        return line();
    }

    View line();

    default View indent() {
        return indent(1);
    }

    View indent(int count);

    String readLine();
}
