package ru.vsu.strelnikov_m_i.services;

import ru.vsu.strelnikov_m_i.entities.User;
import ru.vsu.strelnikov_m_i.enums.RoleType;
import ru.vsu.strelnikov_m_i.exceptions.AccessDeniedException;
import ru.vsu.strelnikov_m_i.exceptions.ObjectNotFoundException;
import ru.vsu.strelnikov_m_i.repositories.interfaces.IUserRepository;

import java.util.List;

public class UserService {
    IUserRepository<User> userRepository;

    public UserService(IUserRepository<User> userRepository) {
        this.userRepository = userRepository;
    }

    public void add(String name, String password, RoleType roleType, String email, String phone) {
        User user = new User(0, name, password.hashCode(), roleType, email, phone);
        userRepository.add(user);
    }

    public void update(int id, String name, String password, RoleType roleType, String email, String phone) {
        User user = userRepository.get(id).orElseThrow(() -> new ObjectNotFoundException("User with id " + id + " not found"));
        user.setFullName(name);
        if (!password.isEmpty()) {
            user.setHashedPassword(password.hashCode());
        }
        user.setRole(roleType);
        user.setEmail(email);
        user.setPhone(phone);
        userRepository.update(user);
    }

    public void delete(int id, int userId) {
        if (id == userId) {
            throw new AccessDeniedException("You cannot delete yourself");
        }
        userRepository.delete(userRepository.get(id).orElseThrow(() -> new ObjectNotFoundException("User with id " + id + " not found")));
    }

    public List<User> getAll() {
        return userRepository.getAll();
    }

}
