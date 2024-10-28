package ru.vsu.strelnikov_m_i.repositories.in_memory;

import ru.vsu.strelnikov_m_i.entities.Manufacture;
import ru.vsu.strelnikov_m_i.repositories.interfaces.IManufactureRepository;

import java.util.List;

public class ManufactureInMemoryRepository implements IManufactureRepository {
    @Override
    public void add(Manufacture o) {

    }

    @Override
    public void delete(Manufacture o) {

    }

    @Override
    public void update(Manufacture o) {

    }

    @Override
    public Manufacture get(int id) {
        return null;
    }

    @Override
    public List<Manufacture> getAll() {
        return List.of();
    }
}
