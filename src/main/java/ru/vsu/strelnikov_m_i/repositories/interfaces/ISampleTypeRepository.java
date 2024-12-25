package ru.vsu.strelnikov_m_i.repositories.interfaces;

import ru.vsu.strelnikov_m_i.entities.SampleType;

import java.util.List;
import java.util.Optional;

public interface ISampleTypeRepository<T extends SampleType>{
    void add(T o);

    void delete(T o);

    void deleteById(int id);

    void update(T o);

    Optional<SampleType> get(int id);

    List<T> getAll();

    List<String> getAllNames();
}
