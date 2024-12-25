package ru.vsu.strelnikov_m_i.repositories.database_connected;

import ru.vsu.strelnikov_m_i.config.DatabaseConnectionConfig;
import ru.vsu.strelnikov_m_i.entities.Entry;
import ru.vsu.strelnikov_m_i.enums.EntryType;
import ru.vsu.strelnikov_m_i.exceptions.DatabaseConnectionFailedException;
import ru.vsu.strelnikov_m_i.exceptions.ObjectNotFoundException;
import ru.vsu.strelnikov_m_i.repositories.interfaces.IEntryRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EntryRepository implements IEntryRepository<Entry> {
    @Override
    public void add(Entry o) throws DatabaseConnectionFailedException {
        try {
            Connection connection = DatabaseConnectionConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement("insert into ENTRY (entry_type, batch_id, user_id, date, amount) values (?::entrytype, ?, ?, ?, ?)");
            statement.setString(1, o.getEntryType().name());
            statement.setInt(2, o.getBatchId());
            statement.setInt(3, o.getUserId());
            statement.setDate(4, o.getDate());
            statement.setDouble(5, o.getAmount());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseConnectionFailedException("Could not add entry to the database: " + e.getMessage());
        }
    }

    @Override
    public void delete(Entry o) throws ObjectNotFoundException {
        try {
            Connection connection = DatabaseConnectionConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement("delete from ENTRY where id = ?");
            statement.setInt(1, o.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseConnectionFailedException("Could not delete entry from database: " + e.getMessage());
        }
    }

    @Override
    public void deleteById(int id) throws DatabaseConnectionFailedException {
        try {
            Connection connection = DatabaseConnectionConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement("delete from ENTRY where id = ?");
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseConnectionFailedException("Could not delete entry from database: " + e.getMessage());
        }
    }

    @Override
    public void update(Entry o) throws DatabaseConnectionFailedException {
        try {
            Connection connection = DatabaseConnectionConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement("update entry set entry_type = ?::entrytype, batch_id = ?, user_id = ?, date = ?, amount = ? where id = ?");
            statement.setString(1, o.getEntryType().toString());
            statement.setInt(2, o.getBatchId());
            statement.setInt(3, o.getUserId());
            statement.setDate(4, o.getDate());
            statement.setDouble(5, o.getAmount());
            statement.setInt(6, o.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseConnectionFailedException("Could not update entry: " + e.getMessage());
        }
    }

    @Override
    public Optional<Entry> get(int id) throws ObjectNotFoundException {
        try {
            Connection connection = DatabaseConnectionConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement("select * from ENTRY where id = ? order by id");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Entry entry = new Entry(resultSet.getInt(1),
                        EntryType.valueOf(resultSet.getString(2)),
                        resultSet.getInt(3),
                        resultSet.getInt(4),
                        resultSet.getDate(5),
                        resultSet.getInt(6)
                );
                return Optional.of(entry);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DatabaseConnectionFailedException("Could not get entry from database: " + e.getMessage());
        }
    }

    @Override
    public List<Entry> getAll() {
        try {
            Connection connection = DatabaseConnectionConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement("select * from ENTRY order by id");
            return parseEntryFromResultSet(statement.executeQuery());
        } catch (SQLException e) {
            throw new DatabaseConnectionFailedException("Could not get entries from database: " + e.getMessage());
        }
    }

    @Override
    public List<Entry> getByAuthor(int userId) throws ObjectNotFoundException {
        try {
            Connection connection = DatabaseConnectionConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement("select * from ENTRY where user_id = ? order by id");
            statement.setInt(1, userId);
            return parseEntryFromResultSet(statement.executeQuery());
        } catch (SQLException e) {
            throw new DatabaseConnectionFailedException("Could not get entries from database: " + e.getMessage());
        }
    }


    //TODO: next()
    private List<Entry> parseEntryFromResultSet(ResultSet resultSet) throws SQLException {
        List<Entry> entries = new ArrayList<>();
        while (resultSet.next()) {
            entries.add(new Entry(resultSet.getInt(1),
                    EntryType.valueOf(resultSet.getString(2)),
                    resultSet.getInt(3),
                    resultSet.getInt(4),
                    resultSet.getDate(5),
                    resultSet.getInt(6)
                    ));
        }
        return entries;
    }
}
