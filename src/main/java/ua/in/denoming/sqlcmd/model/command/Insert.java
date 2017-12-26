package ua.in.denoming.sqlcmd.model.command;

import ua.in.denoming.sqlcmd.model.DataSet;
import ua.in.denoming.sqlcmd.model.DatabaseManager;
import ua.in.denoming.sqlcmd.model.exception.WrongCommandArgumentsException;
import ua.in.denoming.sqlcmd.view.View;

import java.util.Arrays;

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
        if (!canExecute(args)) {
            throw new WrongCommandArgumentsException();
        }

        String tableName = args[0];
        Object[] values = Arrays.copyOfRange(args, 1, args.length);
        DataSet dataSet = createDataSet(values);
        databaseManager.insertData(tableName, dataSet);

        view.writeFormatLine("Values to '%s' table has inserted successfully", tableName);
    }

    private DataSet createDataSet(Object... values) {
        DataSet dataSet = new DataSet((values.length - 1) / 2);
        for (int i = 0; i < values.length; i += 2) {
            String name = (String) values[i];
            dataSet.put(name, values[i + 1]);
        }
        return dataSet;
    }
}
