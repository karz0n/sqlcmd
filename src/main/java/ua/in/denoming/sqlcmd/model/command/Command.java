package ua.in.denoming.sqlcmd.model.command;

public interface Command {
    boolean canExecute(String... args);
    void execute(String... args);
}
