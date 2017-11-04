package ua.in.denoming.sqlcmd.model.command;

import ua.in.denoming.sqlcmd.model.DatabaseManager;
import ua.in.denoming.sqlcmd.model.exception.DatabaseException;
import ua.in.denoming.sqlcmd.model.exception.WrongCountOfArgumentsException;
import ua.in.denoming.sqlcmd.view.View;

public class Clear implements Command {
    private static final int ARGUMENTS_COUNT_CONSTRAINT = 1;

    private View view;
    private DatabaseManager databaseManager;

    public Clear(View view, DatabaseManager databaseManager) {
        this.view = view;
        this.databaseManager = databaseManager;
    }

    @Override
    public void execute(String... args) throws DatabaseException {
        checkArgs(args);

        String tableName = args[0];
        databaseManager.clearTable(tableName);

        view.writeFormatLine("Table '%s' has cleared successfully", tableName);
    }

    private void checkArgs(String... args) {
        if (args.length != ARGUMENTS_COUNT_CONSTRAINT) {
            throw new WrongCountOfArgumentsException(ARGUMENTS_COUNT_CONSTRAINT, args.length);
        }
    }
}
