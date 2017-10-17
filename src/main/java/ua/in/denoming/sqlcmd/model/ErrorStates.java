package ua.in.denoming.sqlcmd.model;

public interface ErrorStates {
    boolean isPasswordIncorrect(String state);
    boolean isDatabaseNotFound(String state);
}
