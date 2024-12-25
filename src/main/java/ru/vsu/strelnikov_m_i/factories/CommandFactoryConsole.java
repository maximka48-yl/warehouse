package ru.vsu.strelnikov_m_i.factories;

import ru.vsu.strelnikov_m_i.context.AppContext;
import ru.vsu.strelnikov_m_i.commands.Command;
import ru.vsu.strelnikov_m_i.enums.RoleType;
import ru.vsu.strelnikov_m_i.exceptions.ObjectNotFoundException;

import java.util.HashMap;
import java.util.Map;

public class CommandFactoryConsole implements Factory{
    Map<String, Command> adminCommands;
    Map<String, Command> managerCommands;

    public CommandFactoryConsole() {
        this.adminCommands = new HashMap<>();
        this.managerCommands = new HashMap<>();
    }

    public void addAdminCommand(String name, Command command) {
        adminCommands.put(name, command);
    }

    public void addManagerCommand(String name, Command command) {
        managerCommands.put(name, command);
    }

    public Command getCommand(String command) throws ObjectNotFoundException {
        Command cmd;
        if (AppContext.getUser().getRole() == RoleType.ADMIN) {
            cmd = adminCommands.get(command);
        } else {
            cmd = managerCommands.get(command);
        }
        if (cmd == null){
            throw new ObjectNotFoundException("No such command: " + command);
        }
        return cmd;
    }
}
