package ua.in.denoming.sqlcmd.model;

public interface ErrorStates {
    boolean isWrongPassword(String state);
    boolean isDatabaseNotFound(String state);
}
