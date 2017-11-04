package ua.in.denoming.sqlcmd.model.command;

public interface Command {
    void execute(String... args) throws Exception;
}
