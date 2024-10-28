package ru.vsu.strelnikov_m_i.repositories.in_memory;


import ru.vsu.strelnikov_m_i.entities.Sample;
import ru.vsu.strelnikov_m_i.repositories.interfaces.ISampleRepository;

import java.util.List;

public class SampleInMemoryRepository implements ISampleRepository {
    @Override
    public void add(Sample o) {

    }

    @Override
    public void delete(Sample o) {

    }

    @Override
    public void update(Sample o) {

    }

    @Override
    public Sample get(int id) {
        return null;
    }

    @Override
    public List<Sample> getAll() {
        return List.of();
    }
}
