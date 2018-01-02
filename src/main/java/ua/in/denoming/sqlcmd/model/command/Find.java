package ua.in.denoming.sqlcmd.model.command;

import org.apache.commons.lang3.Validate;

import ua.in.denoming.sqlcmd.model.DataSet;
import ua.in.denoming.sqlcmd.model.DatabaseManager;
import ua.in.denoming.sqlcmd.model.TableGenerator;
import ua.in.denoming.sqlcmd.view.View;

import java.util.List;

public class Find implements Command {
    private static final int ARGUMENTS_COUNT_CONSTRAINT = 1;

    private View view;
    private DatabaseManager databaseManager;
    private TableGenerator tableGenerator;

    public Find(View view, DatabaseManager databaseManager) {
        this.view = view;
        this.databaseManager = databaseManager;
        this.tableGenerator = new TableGenerator();
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
        List<DataSet> tableData = databaseManager.getData(tableName);

        view.writeLine(tableGenerator.generate(tableData));
    }
}
