package ru.vsu.strelnikov_m_i.repositories.interfaces;

import ru.vsu.strelnikov_m_i.entities.User;

import java.util.List;

public interface IUserRepository {
    void add(User o);

    void delete(User o);

    void update(User o);

    User get(int id);

    List<User> getAll();
}
