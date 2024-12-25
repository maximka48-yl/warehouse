package ru.vsu.strelnikov_m_i.services;

import lombok.Setter;
import ru.vsu.strelnikov_m_i.context.AppContext;
import ru.vsu.strelnikov_m_i.entities.User;
import ru.vsu.strelnikov_m_i.exceptions.ObjectNotFoundException;
import ru.vsu.strelnikov_m_i.repositories.interfaces.IUserRepository;

@Setter
public class AuthService {
    private IUserRepository<User> userRepository;

    public AuthService(IUserRepository<User> userRepository) {
        this.userRepository = userRepository;
    }

    public User authorization(int userId, String password) {
        User user = userRepository.get(userId).orElseThrow(
                () -> new ObjectNotFoundException(String.format("Could not find user with id: %d", userId))
        );
        if (user.getHashedPassword() == password.hashCode()) {
            user.setHashedPassword(0);
            AppContext.setUser(user);
            return user;
        }
        throw new IllegalArgumentException("Wrong password");
    }

    public void quit() {
        AppContext.setUser(null);
    }
}
