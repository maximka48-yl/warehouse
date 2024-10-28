package ru.vsu.strelnikov_m_i.repositories.interfaces;

import ru.vsu.strelnikov_m_i.entities.Manufacture;

import java.util.List;

public interface IManufactureRepository {
    void add(Manufacture o);

    void delete(Manufacture o);

    void update(Manufacture o);

    Manufacture get(int id);

    List<Manufacture> getAll();
}
