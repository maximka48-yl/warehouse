package ru.vsu.strelnikov_m_i.repositories.in_memory;

import ru.vsu.strelnikov_m_i.entities.City;
import ru.vsu.strelnikov_m_i.repositories.interfaces.ICityRepository;

import java.util.List;

public class CityInMemoryRepository implements ICityRepository {
    @Override
    public void add(City o) {

    }

    @Override
    public void delete(City o) {

    }

    @Override
    public void update(City o) {

    }

    @Override
    public City get(int id) {
        return null;
    }

    @Override
    public List<City> getAll() {
        return List.of();
    }
}
