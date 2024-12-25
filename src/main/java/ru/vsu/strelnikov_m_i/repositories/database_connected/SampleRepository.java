package ru.vsu.strelnikov_m_i.repositories.database_connected;

import ru.vsu.strelnikov_m_i.config.DatabaseConnectionConfig;
import ru.vsu.strelnikov_m_i.entities.Manufacture;
import ru.vsu.strelnikov_m_i.entities.Sample;
import ru.vsu.strelnikov_m_i.exceptions.DatabaseConnectionFailedException;
import ru.vsu.strelnikov_m_i.exceptions.ObjectNotFoundException;
import ru.vsu.strelnikov_m_i.repositories.interfaces.ISampleRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SampleRepository implements ISampleRepository<Sample> {

    @Override
    public void add(Sample o) {
        try {
            Connection connection = DatabaseConnectionConfig.getConnection();
            PreparedStatement statementSampleType = connection.prepareStatement("select * from sampleType where type_name = ?");
            statementSampleType.setString(1, o.getSampleTypeName());
            ResultSet resultSetSampleType = statementSampleType.executeQuery();
            if (!resultSetSampleType.next()) {
                throw new ObjectNotFoundException(String.format("Sample type with name %s is not found", o.getSampleTypeName()));
            }

            PreparedStatement statementManufacture = connection.prepareStatement("select * from manufacture where name = ?");
            statementManufacture.setString(1, o.getManufactureName());
            ResultSet resultSetManufacture = statementManufacture.executeQuery();
            if (!resultSetManufacture.next()) {
                throw new ObjectNotFoundException(String.format("Manufacture with name %s is not found", o.getManufactureName()));
            }

            PreparedStatement statement = connection.prepareStatement("insert into sample (name, sample_type_id, manufacture_id) values (?, ?, ?)");
            statement.setString(1, o.getName());
            statement.setInt(2, resultSetSampleType.getInt(1));
            statement.setInt(3, resultSetManufacture.getInt(1));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseConnectionFailedException("Could not add sample to the database: " + e.getMessage());
        }
    }

    @Override
    public void delete(Sample o) {

    }

    @Override
    public void update(Sample o) {
        try {
            Connection connection = DatabaseConnectionConfig.getConnection();
            PreparedStatement statementSampleType = connection.prepareStatement("select * from sampleType where type_name = ?");
            statementSampleType.setString(1, o.getSampleTypeName());
            ResultSet resultSetSampleType = statementSampleType.executeQuery();
            if (!resultSetSampleType.next()) {
                throw new ObjectNotFoundException(String.format("Sample type with name %s is not found", o.getSampleTypeName()));
            }

            PreparedStatement statementManufacture = connection.prepareStatement("select * from manufacture where name = ?");
            statementManufacture.setString(1, o.getManufactureName());
            ResultSet resultSetManufacture = statementManufacture.executeQuery();
            if (!resultSetManufacture.next()) {
                throw new ObjectNotFoundException(String.format("Manufacture with name %s is not found", o.getManufactureName()));
            }

            PreparedStatement statement = connection.prepareStatement("update sample set name=?, sample_type_id=?, manufacture_id=? where id=?");
            statement.setString(1, o.getName());
            statement.setInt(2, resultSetSampleType.getInt(1));
            statement.setInt(3, resultSetManufacture.getInt(1));
            statement.setInt(4, o.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseConnectionFailedException("Could not update sample in database: " + e.getMessage());
        }
    }

    @Override
    public Optional<Sample> get(int id) {
        try {
            Connection connection = DatabaseConnectionConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "select * from sample join sampletype on sample.sample_type_id = sampletype.id " +
                            "join manufacture on sample.manufacture_id = manufacture.id where sample.id = ? order by sample.id"
            );
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Sample sample = new Sample(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(6),
                        resultSet.getString(8)
                );
                return Optional.of(sample);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseConnectionFailedException("Could not get sample from database: " + e.getMessage());
        }
    }

    @Override
    public int getId(String name) {
        return 0;
    }

    @Override
    public List<Sample> getAll() {
        try {
            Connection connection = DatabaseConnectionConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "select * from sample join sampletype on sample.sample_type_id = sampletype.id " +
                            "join manufacture on sample.manufacture_id = manufacture.id order by sample.id"
            );
            return parseManufactureFromResultSet(statement.executeQuery());
        } catch (SQLException e) {
            throw new DatabaseConnectionFailedException("Could not get samples from database: " + e.getMessage());
        }
    }

    public List<String> getAllNames() {
        try {
            Connection connection = DatabaseConnectionConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "select sample.name from sample join sampletype on sample.sample_type_id = sampletype.id " +
                            "join manufacture on sample.manufacture_id = manufacture.id order by sample.id"
            );
            List<String> names = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                names.add(resultSet.getString(1));
            }
            return names;
        } catch (SQLException e) {
            throw new DatabaseConnectionFailedException("Could not get samples from database: " + e.getMessage());
        }
    }

    @Override
    public void deleteById(int id) {
        try {
            Connection connection = DatabaseConnectionConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement("delete from sample where id = ?");
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseConnectionFailedException("Could not delete sample from database: " + e.getMessage());
        }
    }

    private List<Sample> parseManufactureFromResultSet(ResultSet resultSet) throws SQLException {
        List<Sample> samples = new ArrayList<>();
        while (resultSet.next()) {
            samples.add(new Sample(
                    resultSet.getInt(1),
                    resultSet.getString(2),
                    resultSet.getString(6),
                    resultSet.getString(8)
            ));
        }
        return samples;
    }
}
