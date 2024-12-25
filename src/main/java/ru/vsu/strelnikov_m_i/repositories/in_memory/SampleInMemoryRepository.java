package ru.vsu.strelnikov_m_i.repositories.in_memory;


import ru.vsu.strelnikov_m_i.entities.Sample;
import ru.vsu.strelnikov_m_i.exceptions.ObjectNotFoundException;
import ru.vsu.strelnikov_m_i.repositories.interfaces.ISampleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class SampleInMemoryRepository implements ISampleRepository<Sample> {
    private List<Sample> database;
    private int maxId = 0;

    public SampleInMemoryRepository() {
        database = new ArrayList<Sample>();
    }

    @Override
    public void add(Sample o) {
        o.setId(++maxId);
        database.add(o);
    }

    @Override
    public void delete(Sample o) {
        database.remove(o);
    }

    @Override
    public void update(Sample o) {
        database.set(database.indexOf(database.stream()
                .filter(x -> x.getId() == o.getId())
                .findFirst()
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Sample with id %d is not found", o.getId())))), o);
    }

    @Override
    public Optional<Sample> get(int id) {
        return database.stream()
                .filter(x -> x.getId() == id)
                .findFirst();
    }

    @Override
    public int getId(String name) {
        return database.stream()
                .filter(x -> x.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Sample with name %s is not found", name))).getId();
    }

    @Override
    public List<Sample> getAll() {
        return database;
    }

    @Override
    public void deleteById(int id) {

    }

    @Override
    public List<String> getAllNames() {
        return List.of();
    }
}
