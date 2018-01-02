package ua.in.denoming.sqlcmd.model.command;

import org.apache.commons.lang3.Validate;

import ua.in.denoming.sqlcmd.model.DatabaseManager;
import ua.in.denoming.sqlcmd.model.exception.ConnectionRefusedException;
import ua.in.denoming.sqlcmd.model.exception.DatabaseNotFoundException;
import ua.in.denoming.sqlcmd.model.exception.WrongCredentialException;
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
        Validate.isTrue(canExecute(args));

        String url = args[0];
        String userName = args[1];
        String password = args[2];

        int attempts = 3;
        while (attempts >= 0) {
            try {
                databaseManager.open(url, userName, password);
                break;
            } catch (Throwable e) {
                if (e instanceof WrongCredentialException) {
                    view.writeLine("Incorrect user name or password. Please enter username and password again.");
                    userName = view.readLine();
                    password = view.readLine();
                    attempts--;
                    continue;
                }
                if (e instanceof ConnectionRefusedException) {
                    view.writeLine("Connection was refused. Please enter right url and try again.");
                    attempts--;
                    continue;
                }
                if (e instanceof DatabaseNotFoundException) {
                    view.writeLine("Database not exists. Please enter again.");
                    url = view.readLine();
                    attempts--;
                }
            }
        }

        view.writeLine("Database has opened successfully");
    }
}
