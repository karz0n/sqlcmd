package ua.in.denoming.sqlcmd.model.command;

import org.apache.commons.lang3.Validate;

import ua.in.denoming.sqlcmd.model.DatabaseManager;
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

        databaseManager.clearTable(tableName);
        view.writeFormatLine("Table '%s' has cleared successfully", tableName);
    }
}
