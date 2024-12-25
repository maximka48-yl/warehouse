package ru.vsu.strelnikov_m_i.commands;

import ru.vsu.strelnikov_m_i.enums.ResponseType;

public interface Command {
    ResponseType execute();
}
