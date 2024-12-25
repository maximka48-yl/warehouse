package ru.vsu.strelnikov_m_i.repositories.in_memory;

import ru.vsu.strelnikov_m_i.entities.User;
import ru.vsu.strelnikov_m_i.exceptions.ObjectNotFoundException;
import ru.vsu.strelnikov_m_i.repositories.interfaces.IUserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//todo: rework interface and think about add/delete/update

public class UserInMemoryRepository implements IUserRepository<User> {
    private List<User> database;
    private int maxId = 0;

    public UserInMemoryRepository() {
        database = new ArrayList<User>();
    }


    @Override
    public void add(User o) {
        o.setId(++maxId);
        database.add(o);
    }

    @Override
    public void delete(User o) {
        database.remove(o);
    }

    @Override
    public void update(User o) throws ObjectNotFoundException {
        database.set(database.indexOf(database.stream()
                .filter(x -> x.getId() == o.getId())
                .findFirst()
                .orElseThrow(() -> new ObjectNotFoundException(String.format("User %d is not found", o.getId())))), o);
    }

    @Override
    public Optional<User> get(int id) throws ObjectNotFoundException {
        return database.stream()
                .filter(x -> x.getId() == id)
                .findFirst();
    }

    //todo: think about the necessity of this method
    @Override
    public List<User> getAll() {
        return null;
    }
}
