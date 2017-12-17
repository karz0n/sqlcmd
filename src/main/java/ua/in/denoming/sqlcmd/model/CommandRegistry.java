package ua.in.denoming.sqlcmd.model;

import ua.in.denoming.sqlcmd.model.command.Command;
import ua.in.denoming.sqlcmd.model.exception.CommandNotFoundException;

import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {
    private Map<String, Command> registry = new HashMap<>();

    public boolean has(String name) {
        return this.registry.containsKey(name);
    }

    public Command get(String name) {
        return this.registry.get(name);
    }

    public void invoke(String name, String... args) {
        if (!has(name)) {
            throw new CommandNotFoundException();
        }
        get(name).execute(args);
    }

    public void register(String name, Command command) {
        registry.put(name, command);
    }
}
