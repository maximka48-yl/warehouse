package ru.vsu.strelnikov_m_i.repositories.in_memory;

import ru.vsu.strelnikov_m_i.entities.Entry;
import ru.vsu.strelnikov_m_i.exceptions.ObjectNotFoundException;
import ru.vsu.strelnikov_m_i.repositories.filters.EntryFilter;
import ru.vsu.strelnikov_m_i.repositories.interfaces.IEntryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class EntryInMemoryRepository implements IEntryRepository<Entry> {
    private List<Entry> database;
    private int maxId = 0;

    public EntryInMemoryRepository() {
        database = new ArrayList<>();
    }

    @Override
    public void add(Entry o) {
        o.setId(++maxId);
        database.add(o);
    }

    @Override
    public void delete(Entry o) {
        database.remove(o);
    }

    @Override
    public void deleteById(int id) throws ObjectNotFoundException {
        database.remove(database.stream()
                .filter(x -> x.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Entry with id %d is not found ", id))));
    }

    @Override
    public void update(Entry o) throws ObjectNotFoundException {
        database.set(database.indexOf(database.stream()
                .filter(x -> x.getId() == o.getId())
                .findFirst()
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Entry %d is not found", o.getId())))), o);
    }

    @Override
    public Optional<Entry> get(int id) throws ObjectNotFoundException {
        return database.stream()
                .filter(x -> x.getId() == id)
                .findFirst();
    }

    @Override
    public List<Entry> getAll(int rowsOnPage, int currentPage, EntryFilter entryFilter) throws ObjectNotFoundException {
        return List.of();
    }

    @Override
    public List<Entry> getByAuthor(int userId, int rowsOnPage, int currentPage, EntryFilter entryFilter) throws ObjectNotFoundException {
        return List.of();
    }

    @Override
    public int getTotalRows(EntryFilter entryFilter) {
        return 0;
    }

    @Override
    public int getTotalRowsByAuthor(int userId, EntryFilter entryFilter) throws ObjectNotFoundException {
        return 0;
    }
}
