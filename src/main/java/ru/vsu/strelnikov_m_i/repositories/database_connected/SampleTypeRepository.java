package ru.vsu.strelnikov_m_i.repositories.database_connected;

import ru.vsu.strelnikov_m_i.config.DatabaseConnectionConfig;
import ru.vsu.strelnikov_m_i.entities.SampleType;
import ru.vsu.strelnikov_m_i.exceptions.DatabaseConnectionFailedException;
import ru.vsu.strelnikov_m_i.repositories.interfaces.ISampleTypeRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SampleTypeRepository implements ISampleTypeRepository<SampleType> {
    @Override
    public void add(SampleType o) {
        try {
            Connection connection = DatabaseConnectionConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement("insert into sampletype (type_name) values (?)");
            statement.setString(1, o.getName());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseConnectionFailedException("Could not add sample type to the database: " + e.getMessage());
        }
    }

    @Override
    public void delete(SampleType o) {

    }

    @Override
    public void deleteById(int id) {
        try {
            Connection connection = DatabaseConnectionConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement("delete from sampletype where id = ?");
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseConnectionFailedException("Could not delete sample type from database: " + e.getMessage());
        }
    }

    @Override
    public void update(SampleType o) {
        try {
            Connection connection = DatabaseConnectionConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement("update sampletype set type_name = ? where id = ?");
            statement.setString(1, o.getName());
            statement.setInt(2, o.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseConnectionFailedException("Could not update sample type: " + e.getMessage());
        }
    }

    @Override
    public Optional<SampleType> get(int id) {
        try {
            Connection connection = DatabaseConnectionConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement("select * from sampletype where id = ? order by id");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
            SampleType sampleType = new SampleType(
                    resultSet.getInt("id"),
                    resultSet.getString("type_name")
            );
            return Optional.of(sampleType);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseConnectionFailedException("Could not get sample types from database: " + e.getMessage());
        }
    }

    @Override
    public List<SampleType> getAll() {
        try {
            Connection connection = DatabaseConnectionConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement("select * from sampletype order by id");
            return parseSampleTypeFromResultSet(statement.executeQuery());
        } catch (SQLException e) {
            throw new DatabaseConnectionFailedException("Could not get sample types from database: " + e.getMessage());
        }
    }

    @Override
    public List<String> getAllNames() {
        try {
            Connection connection = DatabaseConnectionConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement("select * from sampletype order by id");
            return parseSampleTypeNamesFromResultSet(statement.executeQuery());
        } catch (SQLException e) {
            throw new DatabaseConnectionFailedException("Could not get sample types from database: " + e.getMessage());
        }
    }

    private List<String> parseSampleTypeNamesFromResultSet(ResultSet resultSet) throws SQLException {
        List<String> sampleTypes = new ArrayList<>();
        while (resultSet.next()) {
            sampleTypes.add(resultSet.getString(2));
        }
        return sampleTypes;
    }

    private List<SampleType> parseSampleTypeFromResultSet(ResultSet resultSet) throws SQLException {
        List<SampleType> sampleTypes = new ArrayList<>();
        while (resultSet.next()) {
            sampleTypes.add(new SampleType(resultSet.getInt(1),
                    resultSet.getString(2)
                    ));
        }
        return sampleTypes;
    }
}
