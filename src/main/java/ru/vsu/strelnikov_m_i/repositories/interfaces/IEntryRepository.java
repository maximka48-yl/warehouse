package ru.vsu.strelnikov_m_i.repositories.interfaces;

import ru.vsu.strelnikov_m_i.entities.Entry;
import ru.vsu.strelnikov_m_i.exceptions.ObjectNotFoundException;

import java.util.List;
import java.util.Optional;

public interface IEntryRepository<T extends Entry> {
    void add(T o);

    void delete(T o);

    void deleteById(int id) throws ObjectNotFoundException;

    void update(T o) throws ObjectNotFoundException;

    Optional<Entry> get(int id) throws ObjectNotFoundException;

    List<T> getAll();

    List<T> getByAuthor(int authorId) throws ObjectNotFoundException;
}
