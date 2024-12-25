package ru.vsu.strelnikov_m_i.repositories.in_memory;

import ru.vsu.strelnikov_m_i.entities.SampleType;
import ru.vsu.strelnikov_m_i.repositories.interfaces.ISampleTypeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class SampleTypeInMemoryRepository implements ISampleTypeRepository<SampleType> {
    private List<SampleType> database;
    private int maxId = 0;

    public SampleTypeInMemoryRepository() {
        database = new ArrayList<SampleType>();
    }

    @Override
    public void add(SampleType o) {
        o.setId(++maxId);
        database.add(o);
    }

    @Override
    public void delete(SampleType o) {
        database.remove(o);
    }

    @Override
    public void deleteById(int id) {

    }

    @Override
    public void update(SampleType o) {
        database.set(database.indexOf(database.stream()
                .filter(x -> x.getId() == o.getId())
                .findFirst()
                .get()), o);
    }

    @Override
    public Optional<SampleType> get(int id) {
        return database.stream()
                .filter(x -> x.getId() == id)
                .findFirst();
    }

    @Override
    public List<SampleType> getAll() {
        return database;
    }

    @Override
    public List<String> getAllNames() {
        return List.of();
    }
}
