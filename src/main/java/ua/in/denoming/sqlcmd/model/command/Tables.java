package ua.in.denoming.sqlcmd.model.command;

import ua.in.denoming.sqlcmd.model.DatabaseManager;
import ua.in.denoming.sqlcmd.view.View;

public class Tables implements Command {
    private View view;
    private DatabaseManager databaseManager;

    public Tables(View view, DatabaseManager databaseManager) {
        this.view = view;
        this.databaseManager = databaseManager;
    }

    @Override
    public void execute(String... args) {
        String tables = databaseManager.getTables().toString();
        view.writeLine(tables);
    }
}
