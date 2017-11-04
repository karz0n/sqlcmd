package ua.in.denoming.sqlcmd.model.command;

import ua.in.denoming.sqlcmd.view.View;

public class Help implements Command {
    private View view;

    public Help(View view) {
        this.view = view;
    }

    @Override
    public void execute(String... args) {
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
}
