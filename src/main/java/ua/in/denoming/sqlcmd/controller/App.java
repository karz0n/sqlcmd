package ua.in.denoming.sqlcmd.controller;

import ua.in.denoming.sqlcmd.model.CommandRegistry;
import ua.in.denoming.sqlcmd.model.DatabaseManager;
import ua.in.denoming.sqlcmd.model.InputHandler;
import ua.in.denoming.sqlcmd.model.JdbcDatabaseManager;
import ua.in.denoming.sqlcmd.model.PostgreSqlErrorStates;

import ua.in.denoming.sqlcmd.model.command.Clear;
import ua.in.denoming.sqlcmd.model.command.Connect;
import ua.in.denoming.sqlcmd.model.command.Create;
import ua.in.denoming.sqlcmd.model.command.Delete;
import ua.in.denoming.sqlcmd.model.command.Drop;
import ua.in.denoming.sqlcmd.model.command.Find;
import ua.in.denoming.sqlcmd.model.command.Help;
import ua.in.denoming.sqlcmd.model.command.Insert;
import ua.in.denoming.sqlcmd.model.command.Tables;
import ua.in.denoming.sqlcmd.model.command.Update;

import ua.in.denoming.sqlcmd.view.Console;
import ua.in.denoming.sqlcmd.view.View;

public class App implements Runnable, AutoCloseable {
    private DatabaseManager databaseManager;
    private View view;

    /**
     * Main method
     *
     * @param args arguments of program
     */
    public static void main(String[] args) {
        JdbcDatabaseManager.registerDrivers("org.postgresql.Driver");

        try (App app = new App()) {
            app.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Construct application controller
     */
    @SuppressWarnings("WeakerAccess")
    public App() {
        this.databaseManager = new JdbcDatabaseManager(new PostgreSqlErrorStates());
        this.view = new Console();
    }

    /**
     * Run application controller
     */
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

    /**
     * Close application controller
     */
    @Override
    public void close() {
        databaseManager.close();
    }
}
