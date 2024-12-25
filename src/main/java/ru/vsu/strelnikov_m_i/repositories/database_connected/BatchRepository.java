package ru.vsu.strelnikov_m_i.repositories.database_connected;

import ru.vsu.strelnikov_m_i.config.DatabaseConnectionConfig;
import ru.vsu.strelnikov_m_i.entities.Batch;
import ru.vsu.strelnikov_m_i.exceptions.DatabaseConnectionFailedException;
import ru.vsu.strelnikov_m_i.exceptions.ObjectNotFoundException;
import ru.vsu.strelnikov_m_i.repositories.interfaces.IBatchRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BatchRepository implements IBatchRepository<Batch> {
    @Override
    public void add(Batch o) {
        try {
            Connection connection = DatabaseConnectionConfig.getConnection();
            PreparedStatement statementSample = connection.prepareStatement("select * from sample where name = ?");
            statementSample.setString(1, o.getSampleName());
            ResultSet resultSetSample = statementSample.executeQuery();
            if (!resultSetSample.next()) {
                throw new ObjectNotFoundException("Sample with name " + o.getSampleName() + " not found");
            }

            PreparedStatement statement = connection.prepareStatement("insert into batch (date, sample_id) values (?, ?)");
            statement.setDate(1, o.getDate());
            statement.setInt(2, resultSetSample.getInt(1));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseConnectionFailedException("Could not add batch to the database: " + e.getMessage());
        }
    }

    @Override
    public void delete(Batch o) {
        deleteById(o.getId());
    }

    @Override
    public void deleteById(int id) throws ObjectNotFoundException {
        try {
            Connection connection = DatabaseConnectionConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement("delete from batch where id = ?");
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseConnectionFailedException("Could not delete batch from database: " + e.getMessage());
        }
    }

    @Override
    public void update(Batch o) {
        try {
            Connection connection = DatabaseConnectionConfig.getConnection();
            PreparedStatement statementSample = connection.prepareStatement("select * from sample where name = ?");
            statementSample.setString(1, o.getSampleName());
            ResultSet resultSetSample = statementSample.executeQuery();
            if (!resultSetSample.next()) {
                throw new ObjectNotFoundException("Sample with name " + o.getSampleName() + " not found");
            }

            PreparedStatement statement = connection.prepareStatement("update batch set date= ?, sample_id= ? where id = ?");
            statement.setDate(1, o.getDate());
            statement.setInt(2, resultSetSample.getInt(1));
            statement.setInt(3, o.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseConnectionFailedException("Could not update batch from database: " + e.getMessage());
        }
    }

    @Override
    public Optional<Batch> get(int id) {
        try {
            Connection connection = DatabaseConnectionConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement("select * from batch join sample on batch.sample_id = sample.id where batch.id = ? order by batch.id");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Batch batch = new Batch(resultSet.getInt(1),
                        resultSet.getDate(2),
                        resultSet.getString(5)
                );
                return Optional.of(batch);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseConnectionFailedException("Could not get batch from database: " + e.getMessage());
        }
    }

    @Override
    public int getId(int batch) {
        try {
            Connection connection = DatabaseConnectionConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement("select * from batch where id = ? order by id");
            statement.setInt(1, batch);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            throw new ObjectNotFoundException("Batch with id " + batch + "is not found");
        } catch (SQLException e) {
            throw new DatabaseConnectionFailedException("Could not get batch from database: " + e.getMessage());
        }
    }

    @Override
    public List<Batch> getAll() {
        try {
            Connection connection = DatabaseConnectionConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement("select * from batch join sample on batch.sample_id = sample.id order by batch.id");
            ResultSet resultSet = statement.executeQuery();
            return parseBatchFromResultSet(resultSet);
        } catch (SQLException e) {
            throw new DatabaseConnectionFailedException("Could not get batch from database: " + e.getMessage());
        }
    }

    @Override
    public List<Integer> getAllIds() {
        try {
            Connection connection = DatabaseConnectionConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement("select * from batch join sample on batch.sample_id = sample.id order by batch.id");
            ResultSet resultSet = statement.executeQuery();
            List<Integer> ids = new ArrayList<>();
            while (resultSet.next()) {
                ids.add(resultSet.getInt(1));
            }
            return ids;
        } catch (SQLException e) {
            throw new DatabaseConnectionFailedException("Could not get batch from database: " + e.getMessage());
        }
    }


    private List<Batch> parseBatchFromResultSet(ResultSet resultSet) throws SQLException {
        List<Batch> entries = new ArrayList<>();
        while (resultSet.next()) {
            entries.add(new Batch(resultSet.getInt(1),
                    resultSet.getDate(2),
                    resultSet.getString(5)
            ));
        }
        return entries;
    }
}
