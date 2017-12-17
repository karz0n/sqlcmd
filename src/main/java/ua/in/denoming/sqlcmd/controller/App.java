package ua.in.denoming.sqlcmd.controller;

import ua.in.denoming.sqlcmd.model.*;
import ua.in.denoming.sqlcmd.model.command.*;

import ua.in.denoming.sqlcmd.view.Console;
import ua.in.denoming.sqlcmd.view.View;

public class App implements Runnable, AutoCloseable {
    private DatabaseManager databaseManager;
    private View view;

    public App() {
        this.databaseManager = new JdbcDatabaseManager(
            new PostgreSqlErrorStates(), "org.postgresql.Driver"
        );
        this.view = new Console();
    }

    @Override
    public void run() {
        CommandRegistry registry = new CommandRegistry();
        registry.register("connect", new Connect(view, databaseManager));
        registry.register("tables", new Tables(view, databaseManager));
        registry.register("create", new Create(view, databaseManager));
        registry.register("drop", new Drop(view, databaseManager));
        registry.register("find", new Find(view, databaseManager));
        registry.register("clear", new Clear(view, databaseManager));
        registry.register("delete", new Delete(view, databaseManager));
        registry.register("insert", new Insert(view, databaseManager));
        registry.register("update", new Update(view, databaseManager));
        registry.register("help", new Help(view));

        InputHandler handler = new InputHandler(registry, view);
        handler.setExitCommand("exit");
        handler.setExitBanner("Goodbye, see you later");
        handler.setGreetingBanner("Hello, type 'help' to get help or 'exit' to quit from program");

        handler.handle();
    }

    @Override
    public void close() {
        databaseManager.close();
    }
}
