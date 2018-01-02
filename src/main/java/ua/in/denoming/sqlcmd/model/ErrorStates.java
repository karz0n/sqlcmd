package ua.in.denoming.sqlcmd.model;

public interface ErrorStates {
    boolean isWrongCredential(String state);
    boolean isDatabaseNotFound(String state);
    boolean isConnectionRefused(String state);
}
