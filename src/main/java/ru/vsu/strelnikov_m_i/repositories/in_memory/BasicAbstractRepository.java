package ru.vsu.strelnikov_m_i.repositories.in_memory;

import ru.vsu.strelnikov_m_i.entities.Identifiable;
import ru.vsu.strelnikov_m_i.repositories.interfaces.IBasicRepository;

import java.util.ArrayList;
import java.util.List;

public abstract class BasicAbstractRepository<T extends Identifiable> implements IBasicRepository<T> {
    protected List<T> database;
    private int maxId = 0;

    public BasicAbstractRepository() {
        database = new ArrayList<T>();
    }

    @Override
    public void add(T o) {
        o.setId(++maxId);
        database.add(o);
    }

    @Override
    public void delete(T o) {
        database.remove(o);
    }

    @Override
    public void update(T o) {
        database.set(database.indexOf(database.stream()
                .filter(x -> x.getId() == o.getId())
                .findFirst()
                .get()), o);
    }

    @Override
    public T get(int id) {
        return database.stream()
                .filter(x -> x.getId() == id)
                .findFirst()
                .get();
    }

    @Override
    public List<T> getAll() {
        return database;
    }
}
