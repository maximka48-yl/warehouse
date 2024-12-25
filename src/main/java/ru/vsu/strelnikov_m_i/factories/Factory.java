package ru.vsu.strelnikov_m_i.factories;

import ru.vsu.strelnikov_m_i.commands.Command;

public interface Factory {
    Command getCommand(String name);
}
