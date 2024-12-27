package ru.vsu.strelnikov_m_i.services;

import lombok.RequiredArgsConstructor;
import ru.vsu.strelnikov_m_i.entities.Batch;
import ru.vsu.strelnikov_m_i.entities.Entry;
import ru.vsu.strelnikov_m_i.entities.User;
import ru.vsu.strelnikov_m_i.enums.EntryType;
import ru.vsu.strelnikov_m_i.enums.RoleType;
import ru.vsu.strelnikov_m_i.exceptions.AccessDeniedException;
import ru.vsu.strelnikov_m_i.exceptions.ObjectNotFoundException;
import ru.vsu.strelnikov_m_i.repositories.filters.EntryFilter;
import ru.vsu.strelnikov_m_i.repositories.interfaces.IBatchRepository;
import ru.vsu.strelnikov_m_i.repositories.interfaces.IEntryRepository;

import java.sql.Date;
import java.util.List;

@RequiredArgsConstructor
public class EntryService {
    public static final int rowsOnPage = 15;
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

    public List<Entry> getAll(int currentPage, EntryFilter entryFilter) {
        if (currentPage < 1 || currentPage > getTotalPages(entryFilter)) {
            throw new IndexOutOfBoundsException("Invalid page number");
        }
        return entryRepository.getAll(rowsOnPage, currentPage, entryFilter);
    }

    public Entry getById(int id, User user) {
        Entry entry = entryRepository.get(id).orElseThrow(() -> new ObjectNotFoundException("Entry not found"));
        if (entry.getUserId() != user.getId() && user.getRole() != RoleType.ADMIN) {
            throw new AccessDeniedException("You have no rights to delete this entry");
        }
        return entry;
    }

    public int getTotalPagesByAuthor(int userId, EntryFilter entryFilter) {
        int rows = entryRepository.getTotalRowsByAuthor(userId, entryFilter);
        int pages = rows / rowsOnPage + (rows % rowsOnPage == 0 ? 0 : 1);
        return pages == 0 ? 1 : pages;
    }

    public int getTotalPages(EntryFilter entryFilter) {
        int rows = entryRepository.getTotalRows(entryFilter);
        int pages = rows / rowsOnPage + (rows % rowsOnPage == 0 ? 0 : 1);
        return pages == 0 ? 1 : pages;
    }

    public int getTotalByAuthor(int userId, EntryFilter entryFilter) {
        return entryRepository.getTotalRowsByAuthor(userId, entryFilter);
    }

    public int getTotal(EntryFilter entryFilter) {
        return entryRepository.getTotalRows(entryFilter);
    }

    public List<Entry> getByAuthor(int authorId, int currentPage, EntryFilter entryFilter) {
        if (currentPage < 1 || currentPage > getTotalPagesByAuthor(authorId, entryFilter)) {
            throw new IndexOutOfBoundsException("Invalid page number");
        }
        return entryRepository.getByAuthor(authorId, rowsOnPage, currentPage, entryFilter);
    }
}
