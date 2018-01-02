package ua.in.denoming.sqlcmd.model.command;

import org.apache.commons.lang3.Validate;

import ua.in.denoming.sqlcmd.model.DatabaseManager;
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
    public boolean canExecute(String... args) {
        return (args.length == ARGUMENTS_COUNT_CONSTRAINT);
    }

    @Override
    public void execute(String... args) {
        Validate.isTrue(canExecute(args));

        String tableName = args[0];
        String column = args[1];
        String searchValue = args[2];
        String value = args[3];
        databaseManager.updateData(tableName, column, searchValue, value);

        view.writeFormatLine("Table '%s' has updated successfully", tableName);
    }
}
