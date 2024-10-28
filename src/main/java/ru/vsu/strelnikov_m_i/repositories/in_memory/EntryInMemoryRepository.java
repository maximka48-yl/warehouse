package ru.vsu.strelnikov_m_i.repositories.in_memory;

import ru.vsu.strelnikov_m_i.entities.Entry;
import ru.vsu.strelnikov_m_i.repositories.interfaces.IEntryRepository;

import java.util.List;

public class EntryInMemoryRepository implements IEntryRepository {
    @Override
    public void add(Entry o) {

    }

    @Override
    public void delete(Entry o) {

    }

    @Override
    public void update(Entry o) {

    }

    @Override
    public Entry get(int id) {
        return null;
    }

    @Override
    public List<Entry> getAll() {
        return List.of();
    }
}
