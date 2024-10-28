package ru.vsu.strelnikov_m_i.repositories.interfaces;

import ru.vsu.strelnikov_m_i.entities.Role;

import java.util.List;

public interface IRoleRepository {
    void add(Role o);

    void delete(Role o);

    void update(Role o);

    Role get(int id);

    List<Role> getAll();
}
