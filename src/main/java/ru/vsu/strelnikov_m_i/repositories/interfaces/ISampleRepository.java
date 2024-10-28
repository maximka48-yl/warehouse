package ru.vsu.strelnikov_m_i.repositories.interfaces;

import ru.vsu.strelnikov_m_i.entities.Sample;

import java.util.List;

public interface ISampleRepository {
    void add(Sample o);

    void delete(Sample o);

    void update(Sample o);

    Sample get(int id);

    List<Sample> getAll();
}
