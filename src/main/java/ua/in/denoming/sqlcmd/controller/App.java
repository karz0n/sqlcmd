package ua.in.denoming.sqlcmd.controller;

import ua.in.denoming.sqlcmd.model.DatabaseManager;
import ua.in.denoming.sqlcmd.model.JdbcDatabaseManager;
import ua.in.denoming.sqlcmd.model.PostgreSqlErrorStates;
import ua.in.denoming.sqlcmd.model.InputHandler;
import ua.in.denoming.sqlcmd.model.command.*;

import ua.in.denoming.sqlcmd.view.Console;
import ua.in.denoming.sqlcmd.view.View;

public class App implements Runnable, AutoCloseable {
    private View view;
    private DatabaseManager databaseManager;

    public App() {
        this.view = new Console();
        this.databaseManager = new JdbcDatabaseManager(
            new PostgreSqlErrorStates(), "org.postgresql.Driver"
        );
    }

    @Override
    public void run() {
        InputHandler handler = new InputHandler(view);

        String exitToken = "exit";
        handler.setExitToken(exitToken);
        handler.setExitBanner("Goodbye, see you later");
        handler.setGreetingBanner(
            String.format("Hello, type 'help' to get help or '%s' to quit from program", exitToken)
        );

        handler.registerCommand("connect", new Connect(view, databaseManager));
        handler.registerCommand("tables", new Tables(view, databaseManager));
        handler.registerCommand("create", new Create(view, databaseManager));
        handler.registerCommand("drop", new Drop(view, databaseManager));
        handler.registerCommand("find", new Find(view, databaseManager));
        handler.registerCommand("clear", new Clear(view, databaseManager));
        handler.registerCommand("delete", new Delete(view, databaseManager));
        handler.registerCommand("insert", new Insert(view, databaseManager));
        handler.registerCommand("update", new Update(view, databaseManager));
        handler.registerCommand("help", new Help(view));

        handler.handle();
    }

    @Override
    public void close() throws Exception {
        databaseManager.close();
    }
}
