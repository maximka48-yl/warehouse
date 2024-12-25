package ru.vsu.strelnikov_m_i.console;

import ru.vsu.strelnikov_m_i.enums.EntryType;
import ru.vsu.strelnikov_m_i.enums.ResponseType;

import java.sql.Date;

public interface Creator {
    ResponseType createEntry(EntryType type, String sample, int batch, Date date, int amount);
}
