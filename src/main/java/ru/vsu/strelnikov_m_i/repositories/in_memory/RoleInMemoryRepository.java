package ru.vsu.strelnikov_m_i.repositories.in_memory;

import ru.vsu.strelnikov_m_i.entities.Role;
import ru.vsu.strelnikov_m_i.repositories.interfaces.IRoleRepository;

import java.util.List;

public class RoleInMemoryRepository implements IRoleRepository {
    @Override
    public void add(Role o) {

    }

    @Override
    public void delete(Role o) {

    }

    @Override
    public void update(Role o) {

    }

    @Override
    public Role get(int id) {
        return null;
    }

    @Override
    public List<Role> getAll() {
        return List.of();
    }
}
