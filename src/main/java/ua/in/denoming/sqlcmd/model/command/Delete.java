package ua.in.denoming.sqlcmd.model.command;

import org.apache.commons.lang3.Validate;

import ua.in.denoming.sqlcmd.model.DatabaseManager;
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
    public boolean canExecute(String... args) {
        return (args.length == ARGUMENTS_COUNT_CONSTRAINT);
    }

    @Override
    public void execute(String... args) {
        Validate.isTrue(canExecute(args));

        String tableName = args[0];
        if (!databaseManager.isTableExists(tableName)) {
            view.writeLine(String.format("Table with '%s' name not exists", tableName));
            String tables = databaseManager.getTables().toString();
            view.writeLine(tables);
            return;
        }

        String column = args[1];
        String searchValue = args[2];
        databaseManager.deleteData(tableName, column, searchValue);
        view.writeFormatLine("Data in table '%s' has deleted successfully", tableName);
    }
}
