package ru.vsu.strelnikov_m_i.services;

import ru.vsu.strelnikov_m_i.entities.SampleType;
import ru.vsu.strelnikov_m_i.exceptions.ObjectNotFoundException;
import ru.vsu.strelnikov_m_i.repositories.interfaces.ISampleTypeRepository;

import java.util.List;

public class SampleTypeService {
    ISampleTypeRepository<SampleType> sampleTypeRepository;

    public SampleTypeService(ISampleTypeRepository<SampleType> sampleTypeRepository) {
        this.sampleTypeRepository = sampleTypeRepository;
    }

    public void add(String name) {
        SampleType type = new SampleType(0, name);
        sampleTypeRepository.add(type);
    }

    public void update(int id, String name) {
        SampleType type = sampleTypeRepository.get(id).orElseThrow(() -> new ObjectNotFoundException("Sample type with id " + id + " not found"));
        type.setName(name);
        sampleTypeRepository.update(type);
    }

    public void delete(int id) {
        sampleTypeRepository.deleteById(id);
    }

    public List<SampleType> getAll() {
        return sampleTypeRepository.getAll();

    }

    public List<String> getAllNames() {
        return sampleTypeRepository.getAllNames();
    }
}
