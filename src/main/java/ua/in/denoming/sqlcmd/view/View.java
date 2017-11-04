package ua.in.denoming.sqlcmd.view;

public interface View {
    @SuppressWarnings("UnusedReturnValue")
    View throwing(Throwable throwable);

    View write(String message);

    @SuppressWarnings("unused")
    View writeFormat(String format, Object... objects);

    View writeLine(String message);

    @SuppressWarnings("UnusedReturnValue")
    View writeFormatLine(String format, Object... objects);

    View line();

    default View indent() {
        return indent(1);
    }

    View indent(int count);

    String readLine();
}
