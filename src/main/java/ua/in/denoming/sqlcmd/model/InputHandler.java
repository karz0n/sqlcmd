package ua.in.denoming.sqlcmd.model;

import ua.in.denoming.sqlcmd.model.command.Command;
import ua.in.denoming.sqlcmd.model.exception.CommandNotFoundException;
import ua.in.denoming.sqlcmd.model.exception.NotConnectedException;
import ua.in.denoming.sqlcmd.model.exception.WrongCountOfArgumentsException;
import ua.in.denoming.sqlcmd.view.View;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class InputHandler {
    private static final String EXIT_TOKEN = "quit";

    private Map<String, Command> commands = new HashMap<>();
    private View view;
    private String exitToken = EXIT_TOKEN;
    private String exitBanner;
    private String greetingBanner;

    public InputHandler(View view) {
        this.view = view;
    }

    public void handle() {
        if (greetingBanner != null) {
            view.writeLine(greetingBanner);
        }
        while (true) {
            try {
                String[] parts = nextCommand().split("\\s+");

                String commandName = parts[0].toLowerCase();
                if (getExitToken().equalsIgnoreCase(commandName)) {
                    if (exitBanner != null) {
                        view.writeLine(exitBanner);
                    }
                    break;
                }
                if (!commands.containsKey(commandName)) {
                    throw new CommandNotFoundException();
                }

                try {
                    Command command = commands.get(commandName);

                    String[] args = Arrays.copyOfRange(parts, 1, parts.length);
                    if (!command.canExecute(args)) {
                        throw new WrongCountOfArgumentsException();
                    }

                    command.execute(args);
                } catch (NotConnectedException e) {
                    view.writeLine("First need to establish connection");
                }
            } catch (Exception e) {
                view.throwing(e);
            }
        }
    }

    public void registerCommand(String token, Command command) {
        commands.put(token, command);
    }

    public void setExitToken(String value) {
        exitToken = value;
    }

    public void setExitBanner(String value) {
        exitBanner = value;
    }

    public void setGreetingBanner(String value) {
        greetingBanner = value;
    }

    public String getExitToken() {
        return exitToken;
    }

    private String nextCommand() {
        return view.readLine().trim();
    }
}
