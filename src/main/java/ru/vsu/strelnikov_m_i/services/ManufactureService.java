package ru.vsu.strelnikov_m_i.services;

import ru.vsu.strelnikov_m_i.entities.Manufacture;
import ru.vsu.strelnikov_m_i.exceptions.ObjectNotFoundException;
import ru.vsu.strelnikov_m_i.repositories.interfaces.IManufactureRepository;

import java.util.List;

public class ManufactureService {
    private IManufactureRepository<Manufacture> manufactureRepo;

    public ManufactureService(IManufactureRepository<Manufacture> manufactureRepo) {
        this.manufactureRepo = manufactureRepo;
    }

    public void add(String name, String cityName) {
        manufactureRepo.add(new Manufacture(0, name, cityName));
    }

    public void update(int id, String name, String cityName) {
        Manufacture manufacture = manufactureRepo.get(id).orElseThrow(() -> new ObjectNotFoundException("Manufacture with id" + id + "not found"));
        manufacture.setName(name);
        manufacture.setCityName(cityName);
        manufactureRepo.update(manufacture);
    }

    public void delete(int id) {
        manufactureRepo.deleteById(id);
    }

    public List<Manufacture> getAll() {
        return manufactureRepo.getAll();
    }

    public List<String> getAllNames() {
        return manufactureRepo.getAllNames();
    }
}
