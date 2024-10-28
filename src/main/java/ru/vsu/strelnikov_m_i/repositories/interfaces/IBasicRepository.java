package ru.vsu.strelnikov_m_i.repositories.interfaces;


import java.util.List;

public interface IBasicRepository<T> {
    void add(T o);

    void delete(T o);

    void update(T o);

    T get(int id);

    List<T> getAll();
}
