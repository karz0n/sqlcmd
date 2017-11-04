package ua.in.denoming.sqlcmd.model;

import ua.in.denoming.sqlcmd.model.command.Command;
import ua.in.denoming.sqlcmd.model.exception.CommandNotFoundException;
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
                String[] parts = read();

                String token = parts[0];
                if (getExitToken().equalsIgnoreCase(token)) {
                    if (exitBanner != null) {
                        view.writeLine(exitBanner);
                    }
                    break;
                }

                if (!commands.containsKey(token)) {
                    throw new CommandNotFoundException();
                }

                commands.get(token).execute(
                    Arrays.copyOfRange(parts, 1, parts.length)
                );
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

    private String[] read() {
        String line = view.readLine();
        return normalize(line).split("\\s+");
    }

    private String normalize(String input) {
        return input.trim().toLowerCase();
    }
}
