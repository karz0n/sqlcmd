package ua.in.denoming.sqlcmd.model.command;

import ua.in.denoming.sqlcmd.model.DatabaseManager;
import ua.in.denoming.sqlcmd.model.exception.DatabaseException;
import ua.in.denoming.sqlcmd.model.exception.WrongCountOfArgumentsException;
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
    public void execute(String... args) throws DatabaseException {
        checkArgs(args);

        String tableName = args[0];
        String[] columns = Arrays.copyOfRange(args, 1, args.length);
        databaseManager.createTable(tableName, columns);

        view.writeFormatLine("Table '%s' has created successfully", tableName);
    }

    private void checkArgs(String... args) {
        if (args.length <= 1) {
            throw new WrongCountOfArgumentsException();
        }
    }
}
