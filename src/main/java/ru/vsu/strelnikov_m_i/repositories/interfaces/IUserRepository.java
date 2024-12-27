package ru.vsu.strelnikov_m_i.repositories.interfaces;

import ru.vsu.strelnikov_m_i.entities.User;
import ru.vsu.strelnikov_m_i.exceptions.ObjectNotFoundException;

import java.util.List;
import java.util.Optional;

public interface IUserRepository <T extends User>{
    void add(T o);

    void delete(T o);

    void update(T o) throws ObjectNotFoundException ;

    Optional<T> get(int id) throws ObjectNotFoundException;

    List<T> getAll();

    List<Integer> getAllIds();
}
