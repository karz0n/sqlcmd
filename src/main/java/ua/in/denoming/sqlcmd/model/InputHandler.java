package ua.in.denoming.sqlcmd.model;

import ua.in.denoming.sqlcmd.model.exception.NotConnectedException;
import ua.in.denoming.sqlcmd.view.View;

import java.util.Arrays;

public class InputHandler {
    private static final String EXIT_COMMAND = "quit";

    private CommandRegistry registry;
    private View view;
    private String exitCommand = EXIT_COMMAND;
    private String exitBanner;
    private String greetingBanner;

    public InputHandler(CommandRegistry registry, View view) {
        this.registry = registry;
        this.view = view;
    }

    public void handle() {
        if (greetingBanner != null) {
            view.writeLine(greetingBanner);
        }

        while (true) {
            try {
                String[] commandParts = view.readLine().trim().split("\\s+");

                String commandName = commandParts[0].toLowerCase();
                if (getExitCommand().equalsIgnoreCase(commandName)) {
                    if (exitBanner != null) {
                        view.writeLine(exitBanner);
                    }
                    break;
                }

                String[] commandArgs = Arrays.copyOfRange(commandParts, 1, commandParts.length);
                registry.invoke(commandName, commandArgs);
            } catch (NotConnectedException e) {
                view.writeLine("First need to establish connection");
            } catch (Exception e) {
                view.throwing(e);
            }
        }
    }

    public void setExitCommand(String value) {
        exitCommand = value;
    }

    public String getExitCommand() {
        return exitCommand;
    }

    public void setExitBanner(String value) {
        exitBanner = value;
    }

    public void setGreetingBanner(String value) {
        greetingBanner = value;
    }
}
