package ru.vsu.strelnikov_m_i.repositories.database_connected;

import ru.vsu.strelnikov_m_i.config.DatabaseConnectionConfig;
import ru.vsu.strelnikov_m_i.entities.User;
import ru.vsu.strelnikov_m_i.enums.RoleType;
import ru.vsu.strelnikov_m_i.exceptions.DatabaseConnectionFailedException;
import ru.vsu.strelnikov_m_i.exceptions.ObjectNotFoundException;
import ru.vsu.strelnikov_m_i.repositories.interfaces.IUserRepository;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository implements IUserRepository<User> {
    @Override
    public void add(User o) {
        try {
            Connection connection = DatabaseConnectionConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement("insert into users (full_name, hashed_password, role_type, email, phone) VALUES (?, ?, ?::roletype, ?, ?)");
            statement.setString(1, o.getFullName());
            statement.setInt(2, o.getHashedPassword());
            statement.setString(3, o.getRole().name());
            statement.setString(4, o.getEmail());
            statement.setString(5, o.getPhone());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseConnectionFailedException("Could not add user to the database: " + e.getMessage());
        }
    }

    @Override
    public void delete(User o) {
        try {
            Connection connection = DatabaseConnectionConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement("delete from users where id = ?");
            statement.setInt(1, o.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseConnectionFailedException("Could not delete user from database: " + e.getMessage());
        }
    }

    @Override
    public void update(User o) throws ObjectNotFoundException {
        try {
            Connection connection = DatabaseConnectionConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement("update users set full_name= ?, hashed_password= ?, role_type= ?::roletype, email= ?, phone= ? where id = ?");
            statement.setString(1, o.getFullName());
            statement.setInt(2, o.getHashedPassword());
            statement.setString(3, o.getRole().name());
            statement.setString(4, o.getEmail());
            statement.setString(5, o.getPhone());
            statement.setInt(6, o.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseConnectionFailedException("Could not update user from database: " + e.getMessage());
        }
    }

    @Override
    public Optional<User> get(int id) throws ObjectNotFoundException {
        try {
            Connection connection = DatabaseConnectionConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement("select * from users where id = ? order by id");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = new User(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getInt(3),
                        RoleType.valueOf(resultSet.getString(4)),
                        resultSet.getString(5),
                        resultSet.getString(6)
                );
                return Optional.of(user);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseConnectionFailedException("Could not get user from database: " + e.getMessage());
        }
    }

    @Override
    public List<User> getAll() {
        try {
            Connection connection = DatabaseConnectionConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement("select * from users order by id");
            ResultSet resultSet = statement.executeQuery();
            return parseUserFromResultSet(resultSet);
        } catch (SQLException e) {
            throw new DatabaseConnectionFailedException("Could not get users from database: " + e.getMessage());
        }
    }

    @Override
    public List<Integer> getAllIds() {
        try {
            Connection connection = DatabaseConnectionConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement("select * from users order by id");
            ResultSet resultSet = statement.executeQuery();
            List<Integer> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(resultSet.getInt(1));
            }
            return list;
        } catch (SQLException e) {
            throw new DatabaseConnectionFailedException("Could not get users from database: " + e.getMessage());
        }
    }

    private List<User> parseUserFromResultSet(ResultSet resultSet) throws SQLException {
        List<User> entries = new ArrayList<>();
        while (resultSet.next()) {
            entries.add(new User(resultSet.getInt(1),
                    resultSet.getString(2),
                    resultSet.getInt(3),
                    RoleType.valueOf(resultSet.getString(4)),
                    resultSet.getString(5),
                    resultSet.getString(6)
            ));
        }
        return entries;
    }
}
