package ua.in.denoming.sqlcmd.model;

public final class PostgreSqlErrorStates implements ErrorStates {
    @Override
    public boolean isWrongPassword(String state) {
        return state.equalsIgnoreCase("28P01");
    }

    @Override
    public boolean isConnectionRefused(String state) {
        return state.equalsIgnoreCase("08001");
    }
}
