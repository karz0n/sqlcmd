package ua.in.denoming.sqlcmd.model.command;

import ua.in.denoming.sqlcmd.model.DatabaseManager;
import ua.in.denoming.sqlcmd.model.exception.DatabaseException;
import ua.in.denoming.sqlcmd.model.exception.WrongCountOfArgumentsException;
import ua.in.denoming.sqlcmd.view.View;

public class Update implements Command {
    private static final int ARGUMENTS_COUNT_CONSTRAINT = 4;

    private View view;
    private DatabaseManager databaseManager;

    public Update(View view, DatabaseManager databaseManager) {
        this.view = view;
        this.databaseManager = databaseManager;
    }

    @Override
    public void execute(String... args) throws DatabaseException {
        checkArgs(args);

        String tableName = args[0];
        String column = args[1];
        String searchValue = args[2];
        String value = args[3];
        databaseManager.updateData(tableName, column, searchValue, value);

        view.writeFormatLine("Table '%s' has updated successfully", tableName);
    }

    private void checkArgs(String... args) {
        if (args.length != ARGUMENTS_COUNT_CONSTRAINT) {
            throw new WrongCountOfArgumentsException(ARGUMENTS_COUNT_CONSTRAINT, args.length);
        }
    }
}
