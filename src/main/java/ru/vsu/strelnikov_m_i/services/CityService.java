package ru.vsu.strelnikov_m_i.services;

import ru.vsu.strelnikov_m_i.entities.City;
import ru.vsu.strelnikov_m_i.exceptions.ObjectNotFoundException;
import ru.vsu.strelnikov_m_i.repositories.database_connected.CityRepository;
import ru.vsu.strelnikov_m_i.repositories.interfaces.ICityRepository;

import java.util.List;

public class CityService {
    private final ICityRepository<City> cityRepository;

    public CityService(ICityRepository<City> cityRepository) {
        this.cityRepository = cityRepository;
    }

    public City get(int id) {
        return cityRepository.get(id).orElseThrow(() -> new ObjectNotFoundException("City with id " + id + " not found"));
    }

    public City getByName(String name) {
        return cityRepository.getByName(name).orElseThrow(() -> new ObjectNotFoundException("City with name " + name + " not found"));
    }

    public List<City> getAll() {
        return cityRepository.getAll();
    }

    public void update(int id, String name) {
        City city = cityRepository.get(id).orElseThrow(() -> new ObjectNotFoundException("City with id " + id + " not found"));
        city.setName(name);
        cityRepository.update(city);
    }
    public void add(String name) {
        cityRepository.add(new City(0, name));
    }

    public void delete(int id) {
        cityRepository.deleteById(id);
    }
}
