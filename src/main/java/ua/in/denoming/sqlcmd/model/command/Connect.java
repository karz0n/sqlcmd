package ua.in.denoming.sqlcmd.model.command;

import ua.in.denoming.sqlcmd.model.DatabaseManager;
import ua.in.denoming.sqlcmd.model.exception.WrongArgumentsException;
import ua.in.denoming.sqlcmd.view.View;

public class Connect implements Command {
    private static final int ARGUMENTS_COUNT_CONSTRAINT = 3;

    private View view;
    private DatabaseManager databaseManager;

    public Connect(View view, DatabaseManager databaseManager) {
        this.view = view;
        this.databaseManager = databaseManager;
    }

    @Override
    public boolean canExecute(String... args) {
        return (args.length == ARGUMENTS_COUNT_CONSTRAINT);
    }

    @Override
    public void execute(String... args) {
        if (!canExecute(args)) {
            throw new WrongArgumentsException("Incorrect count of arguments");
        }

        String url = args[0];
        String userName = args[1];
        String password = args[2];
        databaseManager.open(url, userName, password);

        view.writeLine("Database has opened successfully");
    }
}
