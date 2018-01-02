package ua.in.denoming.sqlcmd.model.command;

import org.apache.commons.lang3.Validate;

import ua.in.denoming.sqlcmd.model.DataSet;
import ua.in.denoming.sqlcmd.model.DatabaseManager;
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
        if (args.length == 1) {
            return false;
        }
        int remainder = (args.length - 1) % 2;
        return (remainder == 0);
    }

    @Override
    public void execute(String... args) {
        Validate.isTrue(canExecute(args));

        String tableName = args[0];
        String[] values = Arrays.copyOfRange(args, 1, args.length);
        DataSet dataSet = createDataSet(values);
        databaseManager.insertData(tableName, dataSet);

        view.writeFormatLine("Values to '%s' table has inserted successfully", tableName);
    }

    private DataSet createDataSet(String... items) {
        DataSet dataSet = new DataSet(items.length / 2);
        for (int i = 0; i < items.length; i += 2) {
            String name = (String) items[i];
            dataSet.set(name, items[i + 1]);
        }
        return dataSet;
    }
}
