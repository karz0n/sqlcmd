package ua.in.denoming.sqlcmd.model.command;

import ua.in.denoming.sqlcmd.model.DatabaseManager;
import ua.in.denoming.sqlcmd.model.exception.WrongCountOfArgumentsException;
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
    public void execute(String... args) {
        checkArgs(args);

        String url = args[0];
        String user = args[1];
        String password = args[2];
        databaseManager.open(url, user, password);

        view.writeLine("Database has opened successfully");
    }

    private void checkArgs(String... args) {
        if (args.length != ARGUMENTS_COUNT_CONSTRAINT) {
            throw new WrongCountOfArgumentsException(ARGUMENTS_COUNT_CONSTRAINT, args.length);
        }
    }
}
