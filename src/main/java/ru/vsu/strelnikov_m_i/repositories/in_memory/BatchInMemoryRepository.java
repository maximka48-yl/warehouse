package ru.vsu.strelnikov_m_i.repositories.in_memory;

import ru.vsu.strelnikov_m_i.entities.Batch;
import ru.vsu.strelnikov_m_i.exceptions.ObjectNotFoundException;
import ru.vsu.strelnikov_m_i.repositories.interfaces.IBatchRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BatchInMemoryRepository implements IBatchRepository<Batch> {
    private List<Batch> database;
    private int maxId = 0;

    public BatchInMemoryRepository() {
        database = new ArrayList<>();
    }

    @Override
    public void add(Batch o) {
        o.setId(++maxId);
        database.add(o);
    }

    @Override
    public void delete(Batch o) {
        database.remove(o);
    }

    @Override
    public void deleteById(int id) throws ObjectNotFoundException {
        database.remove(database.stream()
                .filter(x -> x.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Batch with id %d is not found", id))));
    }

    @Override
    public void update(Batch o) {
        database.set(database.indexOf(database.stream()
                .filter(x -> x.getId() == o.getId())
                .findFirst()
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Batch with id %d is not found", o.getId())))), o);
    }

    @Override
    public Optional<Batch> get(int id) {
        return database.stream()
                .filter(x -> x.getId() == id)
                .findFirst();
    }

    @Override
    public int getId(int batch) {
        return database.stream()
                .filter(x -> x.getId() == batch)
                .findFirst()
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Batch with id %d is not found", batch))).getId();
    }

    @Override
    public List<Batch> getAll() {
        return database;
    }

    @Override
    public List<Integer> getAllIds() {
        return List.of();
    }
}
