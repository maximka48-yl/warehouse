package ru.vsu.strelnikov_m_i.repositories.in_memory;

import ru.vsu.strelnikov_m_i.entities.Manufacture;
import ru.vsu.strelnikov_m_i.exceptions.ObjectNotFoundException;
import ru.vsu.strelnikov_m_i.repositories.interfaces.IManufactureRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class ManufactureInMemoryRepository implements IManufactureRepository<Manufacture> {
    private List<Manufacture> database;
    private int maxId = 0;

    public ManufactureInMemoryRepository() {
        database = new ArrayList<Manufacture>();
    }

    @Override
    public void add(Manufacture o) {
        o.setId(++maxId);
        database.add(o);
    }

    @Override
    public void delete(Manufacture o) {
        database.remove(o);
    }

    @Override
    public void deleteById(int id) {

    }

    @Override
    public void update(Manufacture o) {
        database.set(database.indexOf(database.stream()
                .filter(x -> x.getId() == o.getId())
                .findFirst()
                .get()), o);
    }

    @Override
    public Optional<Manufacture> get(int id) {
        return database.stream()
                .filter(x -> x.getId() == id)
                .findFirst();
    }

    @Override
    public List<Manufacture> getAll() {
        return database;
    }

    @Override
    public Manufacture getByName(String name) throws ObjectNotFoundException {
        return database.stream()
                .filter(x -> x.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Manufacture with name %s not found", name)));
    }

    @Override
    public List<String> getAllNames() {
        return List.of();
    }
}
