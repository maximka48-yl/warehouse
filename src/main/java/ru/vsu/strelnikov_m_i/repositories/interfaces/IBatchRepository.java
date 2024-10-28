package ru.vsu.strelnikov_m_i.repositories.interfaces;

import ru.vsu.strelnikov_m_i.entities.Batch;

import java.util.List;

public interface IBatchRepository {
    void add(Batch o);

    void delete(Batch o);

    void update(Batch o);

    Batch get(int id);

    List<Batch> getAll();
}
