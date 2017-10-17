package ua.in.denoming.sqlcmd.model.exception;

public class DatabaseNotFoundException extends DatabaseException {
    public DatabaseNotFoundException() {
        super("Database not found");
    }
}
