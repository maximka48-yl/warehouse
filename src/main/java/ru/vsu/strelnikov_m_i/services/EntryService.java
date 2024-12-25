package ru.vsu.strelnikov_m_i.services;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.vsu.strelnikov_m_i.context.AppContext;
import ru.vsu.strelnikov_m_i.entities.Batch;
import ru.vsu.strelnikov_m_i.entities.Entry;
import ru.vsu.strelnikov_m_i.entities.User;
import ru.vsu.strelnikov_m_i.enums.EntryType;
import ru.vsu.strelnikov_m_i.enums.RoleType;
import ru.vsu.strelnikov_m_i.exceptions.AccessDeniedException;
import ru.vsu.strelnikov_m_i.exceptions.ObjectNotFoundException;
import ru.vsu.strelnikov_m_i.repositories.interfaces.IBatchRepository;
import ru.vsu.strelnikov_m_i.repositories.interfaces.IEntryRepository;

import java.sql.Date;
import java.util.List;

@RequiredArgsConstructor
public class EntryService {
    private final IEntryRepository<Entry> entryRepository;
    private final IBatchRepository<Batch> batchRepository;

    public void add(EntryType type, int batch, Date date, int amount, User user) {
        int batchId = batchRepository.getId(batch);
        entryRepository.add(new Entry(0, type, batchId, user.getId(), date, amount));
    }

    public void delete(int id, User user) {
        if (entryRepository.get(id).orElseThrow(() -> new ObjectNotFoundException("Entry not found")).getUserId() != user.getId() && user.getRole() != RoleType.ADMIN) {
            throw new AccessDeniedException("You have no rights to delete this entry");
        }
        entryRepository.deleteById(id);
    }

    public void update(int id, EntryType type, int batch, Date date, int amount, User user) {
        if (entryRepository.get(id).orElseThrow(() -> new ObjectNotFoundException("Entry not found")).getUserId() != user.getId() && user.getRole() != RoleType.ADMIN) {
            throw new AccessDeniedException("You have no rights to update this entry");
        }
        int batchId = batchRepository.getId(batch);
        Entry entry = entryRepository.get(id).orElseThrow(() -> new ObjectNotFoundException("Entry not found"));
        entry.setBatchId(batchId);
        entry.setDate(date);
        entry.setAmount(amount);
        entry.setEntryType(type);
        entryRepository.update(entry);
    }

    public List<Entry> getAll() {
        return entryRepository.getAll();
    }

    public Entry getById(int id, User user) {
        Entry entry = entryRepository.get(id).orElseThrow(() -> new ObjectNotFoundException("Entry not found"));
        if (entry.getUserId() != user.getId() && user.getRole() != RoleType.ADMIN) {
            throw new AccessDeniedException("You have no rights to delete this entry");
        }
        return entry;
    }

    public List<Entry> getByAuthor(int authorId) {
        return entryRepository.getByAuthor(authorId);
    }
}
