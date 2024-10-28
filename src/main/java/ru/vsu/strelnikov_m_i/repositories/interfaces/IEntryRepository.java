package ru.vsu.strelnikov_m_i.repositories.interfaces;

import ru.vsu.strelnikov_m_i.entities.Entry;

import java.util.List;

public interface IEntryRepository {
    void add(Entry o);

    void delete(Entry o);

    void update(Entry o);

    Entry get(int id);

    List<Entry> getAll();
}
