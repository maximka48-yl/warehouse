package ru.vsu.strelnikov_m_i.repositories.interfaces;

import ru.vsu.strelnikov_m_i.entities.Manufacture;

import java.util.List;
import java.util.Optional;

public interface IManufactureRepository <T extends Manufacture>{
    void add(T o);

    void delete(T o);

    void deleteById(int id);

    void update(T o);

    Optional<Manufacture> get(int id);

    List<T> getAll();

    T getByName(String manufacture);

    List<String> getAllNames();
}
