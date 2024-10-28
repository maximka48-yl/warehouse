package ru.vsu.strelnikov_m_i.repositories.in_memory;

import ru.vsu.strelnikov_m_i.entities.Batch;
import ru.vsu.strelnikov_m_i.repositories.interfaces.IBatchRepository;

import java.util.List;

public class BatchInMemoryRepository implements IBatchRepository {

    @Override
    public void add(Batch o) {

    }

    @Override
    public void delete(Batch o) {

    }

    @Override
    public void update(Batch o) {

    }

    @Override
    public Batch get(int id) {
        return null;
    }

    @Override
    public List<Batch> getAll() {
        return List.of();
    }
}
