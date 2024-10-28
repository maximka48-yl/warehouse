package ru.vsu.strelnikov_m_i.repositories.interfaces;

import ru.vsu.strelnikov_m_i.entities.SampleType;

import java.util.List;

public interface ISampleTypeRepository {
    void add(SampleType o);

    void delete(SampleType o);

    void update(SampleType o);

    SampleType get(int id);

    List<SampleType> getAll();
}
