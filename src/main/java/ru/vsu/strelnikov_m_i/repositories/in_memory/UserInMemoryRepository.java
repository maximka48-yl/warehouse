package ru.vsu.strelnikov_m_i.repositories.in_memory;

import ru.vsu.strelnikov_m_i.entities.User;
import ru.vsu.strelnikov_m_i.repositories.interfaces.IUserRepository;

import java.util.List;

public class UserInMemoryRepository implements IUserRepository {

    @Override
    public void add(User o) {

    }

    @Override
    public void delete(User o) {

    }

    @Override
    public void update(User o) {

    }

    @Override
    public User get(int id) {
        return null;
    }

    @Override
    public List<User> getAll() {
        return List.of();
    }
}
