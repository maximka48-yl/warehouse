package ru.vsu.strelnikov_m_i.repositories.interfaces;

import ru.vsu.strelnikov_m_i.entities.City;

import java.util.List;

public interface ICityRepository {
    void add(City o);

    void delete(City o);

    void update(City o);

    City get(int id);

    List<City> getAll();
}
