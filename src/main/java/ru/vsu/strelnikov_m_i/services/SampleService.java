package ru.vsu.strelnikov_m_i.services;


import ru.vsu.strelnikov_m_i.entities.Sample;
import ru.vsu.strelnikov_m_i.exceptions.ObjectNotFoundException;
import ru.vsu.strelnikov_m_i.repositories.interfaces.ISampleRepository;

import java.util.List;

public class SampleService {
    ISampleRepository<Sample> sampleRepository;

    public SampleService(ISampleRepository<Sample> sampleRepository) {
        this.sampleRepository = sampleRepository;
    }

    public void add(String name, String sampleTypeName, String manufactureName) {
        Sample sample = new Sample(0, name, sampleTypeName, manufactureName);
        sampleRepository.add(sample);
    }

    public void update(int id, String name, String sampleTypeName, String manufactureName) {
        Sample sample = sampleRepository.get(id).orElseThrow(() -> new ObjectNotFoundException("Sample with id " + id + " not found"));
        sample.setName(name);
        sample.setSampleTypeName(sampleTypeName);
        sample.setManufactureName(manufactureName);
        sampleRepository.update(sample);
    }

    public void delete(int id) {
        sampleRepository.deleteById(id);
    }

    public List<String> getAllNames() {
        return sampleRepository.getAllNames();
    }

    public List<Sample> getAll() {
        return sampleRepository.getAll();
    }
}
