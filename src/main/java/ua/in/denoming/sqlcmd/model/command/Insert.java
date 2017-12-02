package ua.in.denoming.sqlcmd.model.command;

import ua.in.denoming.sqlcmd.model.DataSet;
import ua.in.denoming.sqlcmd.model.DatabaseManager;
import ua.in.denoming.sqlcmd.view.View;

public class Insert implements Command {
    private View view;
    private DatabaseManager databaseManager;

    public Insert(View view, DatabaseManager databaseManager) {
        this.view = view;
        this.databaseManager = databaseManager;
    }

    @Override
    public boolean canExecute(String... args) {
        int remainder = (args.length - 1) % 2;
        return (remainder == 0);
    }

    @Override
    public void execute(String... args) {
        String tableName = args[0];
        DataSet dataSet = new DataSet((args.length - 1) / 2);
        for (int i = 1; i < args.length; i += 2) {
            dataSet.put(args[i], args[i + 1]);
        }
        databaseManager.insertData(tableName, dataSet);

        view.writeFormatLine("Values to '%s' table has inserted successfully", tableName);
    }
}
