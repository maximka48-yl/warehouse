package ru.vsu.strelnikov_m_i.repositories.in_memory;

import ru.vsu.strelnikov_m_i.entities.City;
import ru.vsu.strelnikov_m_i.repositories.interfaces.ICityRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CityInMemoryRepository implements ICityRepository<City> {
    private List<City> database;
    private int maxId = 0;

    public CityInMemoryRepository() {
        database = new ArrayList<>();
    }

    @Override
    public void add(City o) {
        o.setId(++maxId);
        database.add(o);
    }

    @Override
    public void delete(City o) {
        database.remove(o);
    }

    @Override
    public void deleteById(int id) {

    }

    @Override
    public void update(City o) {
        database.set(database.indexOf(database.stream()
                .filter(x -> x.getId() == o.getId())
                .findFirst()
                .get()), o);
    }

    @Override
    public Optional<City> get(int id) {
        return database.stream()
                .filter(x -> x.getId() == id)
                .findFirst();
    }

    @Override
    public Optional<City> getByName(String cityName) {
        return null;
    }

    @Override
    public List<City> getAll() {
        return database;
    }
}
