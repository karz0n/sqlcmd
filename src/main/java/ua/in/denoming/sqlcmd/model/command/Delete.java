package ua.in.denoming.sqlcmd.model.command;

import ua.in.denoming.sqlcmd.model.DatabaseManager;
import ua.in.denoming.sqlcmd.model.exception.DatabaseException;
import ua.in.denoming.sqlcmd.model.exception.WrongCountOfArgumentsException;
import ua.in.denoming.sqlcmd.view.View;

public class Delete implements Command {
    private static final int ARGUMENTS_COUNT_CONSTRAINT = 3;

    private View view;
    private DatabaseManager databaseManager;

    public Delete(View view, DatabaseManager databaseManager) {
        this.view = view;
        this.databaseManager = databaseManager;
    }

    @Override
    public void execute(String... args) throws DatabaseException {
        checkArgs(args);

        String tableName = args[0];
        String column = args[1];
        String searchValue = args[2];
        databaseManager.deleteData(tableName, column, searchValue);

        view.writeFormatLine("Data in table '%s' has deleted successfully", tableName);
    }

    private void checkArgs(String... args) {
        if (args.length != ARGUMENTS_COUNT_CONSTRAINT) {
            throw new WrongCountOfArgumentsException(ARGUMENTS_COUNT_CONSTRAINT, args.length);
        }
    }
}
