package ru.vsu.strelnikov_m_i.services;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.vsu.strelnikov_m_i.entities.Batch;
import ru.vsu.strelnikov_m_i.exceptions.ObjectNotFoundException;
import ru.vsu.strelnikov_m_i.repositories.interfaces.IBatchRepository;

import java.sql.Date;
import java.util.List;


public class BatchService {
    private final IBatchRepository<Batch> batchRepository;

    public BatchService(IBatchRepository<Batch> batchRepository) {
        this.batchRepository = batchRepository;
    }

    public void add(Date date, String sample) {
        batchRepository.add(new Batch(0, date, sample));
    }

    public List<Batch> getAll() {
        return batchRepository.getAll();
    }

    public void delete(int id) {
        batchRepository.deleteById(id);
    }

    public void update(int id, Date date, String sample) {
        Batch batch = batchRepository.get(id).orElseThrow(() -> new ObjectNotFoundException("Batch with id " + id + " not found"));
        batch.setDate(date);
        batch.setSampleName(sample);
        batchRepository.update(batch);
    }

    public Batch getById(int id) {
        return batchRepository.get(id).orElseThrow(() -> new ObjectNotFoundException("Batch with id " + id + " not found"));
    }

    public List<Integer> getAllIds() {
        return batchRepository.getAllIds();
    }
}
