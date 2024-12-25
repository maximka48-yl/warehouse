package ru.vsu.strelnikov_m_i.repositories.interfaces;

import ru.vsu.strelnikov_m_i.entities.City;

import java.util.List;
import java.util.Optional;

public interface ICityRepository <T extends City> {
    void add(T o);

    void delete(T o);

    void deleteById(int id);

    void update(T o);

    Optional<City> get(int id);

    Optional<City> getByName(String cityName);

    List<T> getAll();
}
