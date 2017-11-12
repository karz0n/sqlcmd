package ua.in.denoming.sqlcmd.model.command;

import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import ua.in.denoming.sqlcmd.model.DataSet;
import ua.in.denoming.sqlcmd.model.DatabaseManager;
import ua.in.denoming.sqlcmd.model.exception.WrongCountOfArgumentsException;
import ua.in.denoming.sqlcmd.view.View;

import java.util.ArrayList;

public class Find implements Command {
    private static final int ARGUMENTS_COUNT_CONSTRAINT = 1;

    private View view;
    private DatabaseManager databaseManager;

    private static String printTableData(ArrayList<DataSet> dataSets) {
        CellStyle cs = new CellStyle(
            CellStyle.HorizontalAlign.center, CellStyle.AbbreviationStyle.crop, CellStyle.NullStyle.emptyString
        );

        DataSet first = dataSets.get(0);
        int columnCount = first.size();

        Table t = new Table(columnCount, BorderStyle.CLASSIC, ShownBorders.ALL, false, "");

        String[] columnNames = first.getNames();
        for (int i = 0; i < columnCount; i++) {
            t.addCell(columnNames[i], cs);
        }

        for (DataSet dataSet : dataSets) {
            for (int i = 0; i < columnCount; i++) {
                t.addCell(dataSet.getString(i).trim(), cs);
            }
        }

        return t.render();
    }

    public Find(View view, DatabaseManager databaseManager) {
        this.view = view;
        this.databaseManager = databaseManager;
    }

    @Override
    public void execute(String... args) {
        checkArgs(args);

        String tableName = args[0];
        ArrayList<DataSet> dataSets = databaseManager.obtainTableData(tableName);

        view.writeLine(Find.printTableData(dataSets));
    }

    private void checkArgs(String... args) {
        if (args.length != ARGUMENTS_COUNT_CONSTRAINT) {
            throw new WrongCountOfArgumentsException(ARGUMENTS_COUNT_CONSTRAINT, args.length);
        }
    }
}
