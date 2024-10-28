package ru.vsu.strelnikov_m_i.repositories.in_memory;

import ru.vsu.strelnikov_m_i.entities.Role;


public class RoleInMemoryRepository extends BasicAbstractRepository<Role> {
    @Override
    public void add(Role o) {
        database.add(o);
    }
}
