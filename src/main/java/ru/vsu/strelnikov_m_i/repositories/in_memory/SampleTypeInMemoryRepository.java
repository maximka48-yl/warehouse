package ru.vsu.strelnikov_m_i.repositories.in_memory;

import ru.vsu.strelnikov_m_i.entities.SampleType;
import ru.vsu.strelnikov_m_i.repositories.interfaces.ISampleTypeRepository;

import java.util.List;

public class SampleTypeInMemoryRepository implements ISampleTypeRepository {
    @Override
    public void add(SampleType o) {

    }

    @Override
    public void delete(SampleType o) {

    }

    @Override
    public void update(SampleType o) {

    }

    @Override
    public SampleType get(int id) {
        return null;
    }

    @Override
    public List<SampleType> getAll() {
        return List.of();
    }
}
