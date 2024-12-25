package ru.vsu.strelnikov_m_i.repositories.interfaces;

import ru.vsu.strelnikov_m_i.entities.Batch;
import ru.vsu.strelnikov_m_i.exceptions.ObjectNotFoundException;

import java.util.List;
import java.util.Optional;

public interface IBatchRepository<T extends Batch> {
    void add(T o);

    void delete(T o);

    void deleteById(int id) throws ObjectNotFoundException;

    void update(T o);

    Optional<Batch> get(int id);

    int getId(int batch);

    List<T> getAll();

    List<Integer> getAllIds();
}
