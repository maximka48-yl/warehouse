package ru.vsu.strelnikov_m_i.repositories.database_connected;

import ru.vsu.strelnikov_m_i.config.DatabaseConnectionConfig;
import ru.vsu.strelnikov_m_i.entities.Manufacture;
import ru.vsu.strelnikov_m_i.exceptions.DatabaseConnectionFailedException;
import ru.vsu.strelnikov_m_i.exceptions.ObjectNotFoundException;
import ru.vsu.strelnikov_m_i.repositories.interfaces.IManufactureRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ManufactureRepository implements IManufactureRepository<Manufacture> {
    @Override
    public void add(Manufacture o) {
        try {
            Connection connection = DatabaseConnectionConfig.getConnection();
            PreparedStatement statement_city = connection.prepareStatement("select * from city where name = ?");
            statement_city.setString(1, o.getCityName());
            ResultSet resultSet_city = statement_city.executeQuery();
            if (!resultSet_city.next()) {
                throw new ObjectNotFoundException(String.format("City with name %s is not found", o.getCityName()));
            }
            PreparedStatement statement = connection.prepareStatement("insert into manufacture (name, city_id) values (?, ?)");
            statement.setString(1, o.getName());
            statement.setInt(2, resultSet_city.getInt(1));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseConnectionFailedException("Could not add manufacture to the database: " + e.getMessage());
        }
    }

    @Override
    public void delete(Manufacture o) {

    }

    @Override
    public void deleteById(int id) {
        try {
            Connection connection = DatabaseConnectionConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement("delete from manufacture where id = ?");
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseConnectionFailedException("Could not delete manufacture from database: " + e.getMessage());
        }
    }

    @Override
    public void update(Manufacture o) {
        try {
            Connection connection = DatabaseConnectionConfig.getConnection();
            PreparedStatement statement_city = connection.prepareStatement("select * from city where name = ?");
            statement_city.setString(1, o.getCityName());
            ResultSet resultSet_city = statement_city.executeQuery();
            if (!resultSet_city.next()) {
                throw new ObjectNotFoundException(String.format("City with name %s is not found", o.getCityName()));
            }
            PreparedStatement statement = connection.prepareStatement("update manufacture set name = ?, city_id = ? where id = ?");
            statement.setString(1, o.getName());
            statement.setInt(2, resultSet_city.getInt(1));
            statement.setInt(3, o.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseConnectionFailedException("Could not update manufacture in database: " + e.getMessage());
        }
    }

    @Override
    public Optional<Manufacture> get(int id) {
        try {
            Connection connection = DatabaseConnectionConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "select * from manufacture join city on manufacture.city_id = city.id where manufacture.id = ? order by manufacture.id"
            );
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Manufacture manufacture = new Manufacture(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(5)
                );
                return Optional.of(manufacture);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseConnectionFailedException("Could not get sample types from database: " + e.getMessage());
        }
    }

    @Override
    public List<Manufacture> getAll() {
        try {
            Connection connection = DatabaseConnectionConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "select * from manufacture join city on manufacture.city_id = city.id  order by manufacture.id"
            );
            return parseManufactureFromResultSet(statement.executeQuery());
        } catch (SQLException e) {
            throw new DatabaseConnectionFailedException("Could not get manufactures from database: " + e.getMessage());
        }
    }

    private List<String> parseManufactureNamesFromResultSet(ResultSet resultSet) throws SQLException {
        List<String> manNames = new ArrayList<>();
        while (resultSet.next()) {
            manNames.add(resultSet.getString(2));
        }
        return manNames;
    }

    private List<Manufacture> parseManufactureFromResultSet(ResultSet resultSet) throws SQLException {
        List<Manufacture> cities = new ArrayList<>();
        while (resultSet.next()) {
            cities.add(new Manufacture(resultSet.getInt(1),
                    resultSet.getString(2),
                    resultSet.getString(5)
            ));
        }
        return cities;
    }

    @Override
    public Manufacture getByName(String manufacture) {
        return null;
    }

    @Override
    public List<String> getAllNames() {
        try {
            Connection connection = DatabaseConnectionConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "select * from manufacture join city on manufacture.city_id = city.id  order by manufacture.id"
            );
            return parseManufactureNamesFromResultSet(statement.executeQuery());
        } catch (SQLException e) {
            throw new DatabaseConnectionFailedException("Could not get manufactures from database: " + e.getMessage());
        }
    }
}
