package ru.vsu.strelnikov_m_i.repositories.database_connected;

import ru.vsu.strelnikov_m_i.config.DatabaseConnectionConfig;
import ru.vsu.strelnikov_m_i.entities.City;
import ru.vsu.strelnikov_m_i.exceptions.DatabaseConnectionFailedException;
import ru.vsu.strelnikov_m_i.repositories.interfaces.ICityRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CityRepository implements ICityRepository<City> {
    @Override
    public void add(City o) {
        try {
            Connection connection = DatabaseConnectionConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement("insert into city (name) values (?)");
            statement.setString(1, o.getName());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseConnectionFailedException("Could not add city to the database: " + e.getMessage());
        }
    }

    @Override
    public void delete(City o) {

    }

    @Override
    public void deleteById(int id) {
        try {
            Connection connection = DatabaseConnectionConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement("delete from city where id = ?");
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseConnectionFailedException("Could not delete city from database: " + e.getMessage());
        }
    }

    @Override
    public void update(City o) {
        try {
            Connection connection = DatabaseConnectionConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement("update city set name = ? where id = ?");
            statement.setString(1, o.getName());
            statement.setInt(2, o.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseConnectionFailedException("Could not update city: " + e.getMessage());
        }
    }

    @Override
    public Optional<City> get(int id) {
        try {
            Connection connection = DatabaseConnectionConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement("select * from city where id = ? order by id");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                City city = new City(
                        resultSet.getInt(1),
                        resultSet.getString(2)
                );
                return Optional.of(city);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseConnectionFailedException("Could not find city in database: " + e.getMessage());
        }
    }

    @Override
    public Optional<City> getByName(String cityName) {
        try {
            Connection connection = DatabaseConnectionConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement("select * from city where name = ? order by id");
            statement.setString(1, cityName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                City city = new City(
                        resultSet.getInt(1),
                        resultSet.getString(2)
                );
                return Optional.of(city);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseConnectionFailedException("Could not find city in database: " + e.getMessage());
        }
    }

    @Override
    public List<City> getAll() {
        try {
            Connection connection = DatabaseConnectionConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement("select * from city order by id");
            return parseCityFromResultSet(statement.executeQuery());
        } catch (SQLException e) {
            throw new DatabaseConnectionFailedException("Could not get cities from database: " + e.getMessage());
        }
    }

    private List<City> parseCityFromResultSet(ResultSet resultSet) throws SQLException {
        List<City> cities = new ArrayList<>();
        while (resultSet.next()) {
            cities.add(new City(resultSet.getInt(1),
                    resultSet.getString(2)
                    ));
        }
        return cities;
    }
}
