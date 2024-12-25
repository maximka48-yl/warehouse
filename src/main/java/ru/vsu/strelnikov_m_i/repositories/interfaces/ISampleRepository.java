package ru.vsu.strelnikov_m_i.repositories.interfaces;

import ru.vsu.strelnikov_m_i.entities.Sample;

import java.util.List;
import java.util.Optional;

public interface ISampleRepository <T extends Sample>{
    void add(T o);

    void delete(T o);

    void update(T o);

    Optional<Sample> get(int id);

    int getId(String name);

    List<T> getAll();

    void deleteById(int id);

    List<String> getAllNames();
}
