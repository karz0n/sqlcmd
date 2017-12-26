package ua.in.denoming.sqlcmd.model.command;

import ua.in.denoming.sqlcmd.model.DataSet;
import ua.in.denoming.sqlcmd.model.DatabaseManager;
import ua.in.denoming.sqlcmd.model.TableGenerator;
import ua.in.denoming.sqlcmd.model.exception.WrongCommandArgumentsException;
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
        if (!canExecute(args)) {
            throw new WrongCommandArgumentsException();
        }

        String tableName = args[0];
        List<DataSet> tableData = databaseManager.obtainTableData(tableName);

        view.writeLine(tableGenerator.generate(tableData));
    }
}
