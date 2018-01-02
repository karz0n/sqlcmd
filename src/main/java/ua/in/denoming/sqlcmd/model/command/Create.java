package ua.in.denoming.sqlcmd.model.command;

import org.apache.commons.lang3.Validate;

import ua.in.denoming.sqlcmd.model.DatabaseManager;
import ua.in.denoming.sqlcmd.view.View;

import java.util.Arrays;

public class Create implements Command {
    private View view;
    private DatabaseManager databaseManager;

    public Create(View view, DatabaseManager databaseManager) {
        this.view = view;
        this.databaseManager = databaseManager;
    }

    @Override
    public boolean canExecute(String... args) {
        return (args.length > 1);
    }

    @Override
    public void execute(String... args) {
        Validate.isTrue(canExecute(args));

        String tableName = args[0];
        String[] columns = Arrays.copyOfRange(args, 1, args.length);
        databaseManager.createTable(tableName, columns);

        view.writeFormatLine("Table '%s' has created successfully", tableName);
    }
}
