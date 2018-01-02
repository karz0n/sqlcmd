package ua.in.denoming.sqlcmd.model;

public interface ErrorStates {
    boolean isWrongPassword(String state);
    boolean isConnectionRefused(String state);
}
