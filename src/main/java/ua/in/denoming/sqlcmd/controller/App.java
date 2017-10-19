package ua.in.denoming.sqlcmd.controller;

import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import ua.in.denoming.sqlcmd.model.DataSet;
import ua.in.denoming.sqlcmd.model.DatabaseManager;
import ua.in.denoming.sqlcmd.model.JdbcDatabaseManager;
import ua.in.denoming.sqlcmd.model.PostgreSqlErrorStates;
import ua.in.denoming.sqlcmd.model.TableDescription;
import ua.in.denoming.sqlcmd.model.exception.DatabaseException;
import ua.in.denoming.sqlcmd.model.exception.DatabaseNotFoundException;
import ua.in.denoming.sqlcmd.model.exception.IncorrectPasswordException;
import ua.in.denoming.sqlcmd.view.Console;
import ua.in.denoming.sqlcmd.view.View;

import java.util.ArrayList;
import java.util.Arrays;

class App implements Runnable, AutoCloseable {
    private static App instance;

    private View view;
    private DatabaseManager databaseManager;

    static App getInstance() throws Exception {
        if (App.instance == null) {
            App.instance = new App(
                new JdbcDatabaseManager(
                    new PostgreSqlErrorStates(), "org.postgresql.Driver"
                ),
                new Console()
            );
        }
        return App.instance;
    }

    private App(DatabaseManager databaseManager, View view) throws Exception {
        this.view = view;
        this.databaseManager = databaseManager;
    }

    private void doConnect(String url, String user, String password) {
        try {
            if (databaseManager.isOpen()) {
                view.writeLine("Database has already opened");
            } else {
                databaseManager.open(url, user, password);
                view.writeLine("Database has opened successfully");
            }
        } catch (IncorrectPasswordException e) {
            view.writeLine("Incorrect password, please try again");
        } catch (DatabaseNotFoundException e) {
            view.writeLine("Database not found");
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    private void doTables() {
        try {
            ArrayList<TableDescription> tables = databaseManager.getTables();
            view.writeLine(tables.toString());
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    private void doClearTable(String tableName) {
        try {
            databaseManager.clearTable(tableName);
            view.writeFormatLine("Table '%s' has cleared successfully", tableName);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    private void doDropTable(String tableName) {
        try {
            databaseManager.deleteTable(tableName);
            view.writeFormatLine("Table '%s' has dropped successfully", tableName);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    private void doCreateTable(String tableName, String... columns) {
        try {
            databaseManager.createTable(tableName, columns);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    private void doFind(String tableName) {
        try {
            ArrayList<DataSet> dataSets = databaseManager.obtainTableData(tableName);
            view.writeLine(printTableData(dataSets));
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    private void doInsert(String tableName, String... values) {
        try {
            DataSet dataSet = new DataSet(values.length / 2);
            for (int i = 0; i < values.length; i += 2) {
                dataSet.put(
                    values[i],
                    values[i + 1]
                );
            }
            databaseManager.insertData(tableName, dataSet);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    private void doUpdate(String tableName, String column, String searchValue, String value) {
        try {
            databaseManager.updateData(tableName, column, searchValue, value);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    private void doDelete(String tableName, String column, String searchValue) {
        try {
            databaseManager.deleteData(tableName, column, searchValue);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }

    }

    private void doHelp() {
        view.writeLine("Usage: <command> [<param1> <param2> ...]")
            .line()
            .indent(1).writeLine("Commands:")
            .indent(2).write("connect").indent().writeLine("<url> <username> <password>")
            .indent(3).writeLine("connect to database")
            .indent(2).writeLine("getTables")
            .indent(3).writeLine("print list of getTables")
            .indent(2).write("clear").indent().writeLine("<tableName>")
            .indent(3).writeLine("clear specified table")
            .indent(2).write("drop").indent().writeLine("<tableName>")
            .indent(3).writeLine("deleteData specified table")
            .indent(2).write("create").indent().writeLine("<tableName> <column1:type> <column2:type> ...")
            .indent(3).writeLine("create table using specified column names and types")
            .indent(2).write("find").indent().writeLine("<tableName>")
            .indent(3).writeLine("print content of specified table")
            .indent(2).write("insertData").indent().writeLine("<tableName> <column1> <value1> <column2> <value2> ...")
            .indent(3).writeLine("insertData row with specified data")
            .indent(2).write("updateData").indent().writeLine("<tableName> <column1> <value1> <column2> <value2>")
            .indent(3).writeLine("updateData rows where <column1> equal <value1> and set <column2> to <value2>")
            .indent(2).write("deleteData").indent().writeLine("<tableName> <column> <value>")
            .indent(3).writeLine("deleteData specified table where <column> equal <value>")
            .indent(2).writeLine("help")
            .indent(3).writeLine("print this help")
            .indent(2).writeLine("exit")
            .indent(3).writeLine("quit from program")
            .line();
    }

    private void doExit() {
        view.writeLine("By. See you later.");
    }

    private void processInput() {
        view.writeLine("Please enter help and push enter to get help");
        while (true) {
            String[] parts = getUserInput();

            String command = parts[0];
            switch (command) {
                case "connect": {
                    String url = parts[1];
                    String username = parts[2];
                    String password = parts[3];
                    doConnect(url, username, password);
                    break;
                }
                case "tables": {
                    doTables();
                    break;
                }
                case "clear": {
                    String tableName = parts[1];
                    doClearTable(tableName);
                    break;
                }
                case "drop": {
                    String tableName = parts[1];
                    doDropTable(tableName);
                    break;
                }
                case "create": {
                    String tableName = parts[1];
                    String[] columns = Arrays.copyOfRange(parts, 2, parts.length);
                    doCreateTable(tableName, columns);
                    break;
                }
                case "find": {
                    String tableName = parts[1];
                    doFind(tableName);
                    break;
                }
                case "insert": {
                    String tableName = parts[1];
                    String[] args = Arrays.copyOfRange(parts, 2, parts.length);
                    doInsert(tableName, args);
                    break;
                }
                case "update": {
                    String tableName = parts[1];
                    String column = parts[2];
                    String searchValue = parts[3];
                    String value = parts[4];
                    doUpdate(tableName, column, searchValue, value);
                    break;
                }
                case "delete": {
                    String tableName = parts[1];
                    String column = parts[2];
                    String searchValue = parts[3];
                    doDelete(tableName, column, searchValue);
                    break;
                }
                case "help": {
                    doHelp();
                    break;
                }
                case "exit": {
                    doExit();
                    return;
                }
            }
        }
    }

    @Override
    public void run() {
        processInput();
    }

    @Override
    public void close() throws Exception {
        databaseManager.close();
    }

    private String[] getUserInput() {
        String command = view.readLine();
        return normalizeInput(command).split("\\s+");
    }

    private String normalizeInput(String input) {
        return input.trim().toLowerCase();
    }

    private String printTableData(ArrayList<DataSet> dataSets) {
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
}
