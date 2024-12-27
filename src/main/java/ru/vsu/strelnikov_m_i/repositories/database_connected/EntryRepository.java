package ru.vsu.strelnikov_m_i.repositories.database_connected;

import ru.vsu.strelnikov_m_i.config.DatabaseConnectionConfig;
import ru.vsu.strelnikov_m_i.entities.Entry;
import ru.vsu.strelnikov_m_i.enums.EntryType;
import ru.vsu.strelnikov_m_i.exceptions.DatabaseConnectionFailedException;
import ru.vsu.strelnikov_m_i.exceptions.ObjectNotFoundException;
import ru.vsu.strelnikov_m_i.repositories.filters.EntryFilter;
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
    public List<Entry> getAll(int rowsOnPage, int currentPage, EntryFilter entryFilter) {
        StringBuilder query = new StringBuilder("SELECT * FROM ENTRY WHERE 1=1");
        List<Object> params = prepareStatementForAdmin(entryFilter, query);

        query.append(" ORDER BY id LIMIT ? OFFSET ?");
        params.add(rowsOnPage);
        params.add((currentPage - 1) * rowsOnPage);

        try (Connection connection = DatabaseConnectionConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query.toString())) {

            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i));
            }

            return parseEntryFromResultSet(statement.executeQuery());
        } catch (SQLException e) {
            throw new DatabaseConnectionFailedException("Could not get entries from database: " + e.getMessage());
        }
    }


    @Override
    public List<Entry> getByAuthor(int userId, int rowsOnPage, int currentPage, EntryFilter entryFilter) throws ObjectNotFoundException {
        StringBuilder query = new StringBuilder("SELECT * FROM ENTRY WHERE user_id = ?");
        List<Object> params = prepareStatmentForManager(userId, entryFilter, query);

        query.append(" ORDER BY id LIMIT ? OFFSET ?");
        params.add(rowsOnPage);
        params.add((currentPage - 1) * rowsOnPage);

        try (Connection connection = DatabaseConnectionConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query.toString())) {

            // Установка параметров в PreparedStatement
            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i));
            }

            ResultSet resultSet = statement.executeQuery();
            List<Entry> entries = parseEntryFromResultSet(resultSet);
            if (entries.isEmpty()) {
                throw new ObjectNotFoundException("No entries found for the given author and filters.");
            }
            return entries;
        } catch (SQLException e) {
            throw new DatabaseConnectionFailedException("Could not get entries by author from database: " + e.getMessage());
        }
    }

    @Override
    public int getTotalRows(EntryFilter entryFilter) {
        StringBuilder query = new StringBuilder("SELECT COUNT(id) FROM ENTRY WHERE 1=1");
        List<Object> params = prepareStatementForAdmin(entryFilter, query);

        try (Connection connection = DatabaseConnectionConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query.toString())) {

            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i));
            }

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                return 0;
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionFailedException("Could not get total rows from database: " + e.getMessage());
        }
    }

    private List<Object> prepareStatementForAdmin(EntryFilter entryFilter, StringBuilder query) {
        List<Object> params = new ArrayList<>();

        if (entryFilter.getEntryType() != null) {
            query.append(" AND entry_type = ?::entrytype");
            params.add(entryFilter.getEntryType().name());
        }
        if (entryFilter.getBatchId() != null) {
            query.append(" AND batch_id = ?");
            params.add(entryFilter.getBatchId());
        }
        if (entryFilter.getUserId() != null) {
            query.append(" AND user_id = ?");
            params.add(entryFilter.getUserId());
        }
        if (entryFilter.getDate() != null) {
            query.append(" AND date = ?");
            params.add(entryFilter.getDate());
        }
        return params;
    }

    @Override
    public int getTotalRowsByAuthor(int userId, EntryFilter entryFilter) throws ObjectNotFoundException {
        StringBuilder query = new StringBuilder("SELECT COUNT(id) FROM ENTRY WHERE user_id = ?");
        List<Object> params = prepareStatmentForManager(userId, entryFilter, query);

        try (Connection connection = DatabaseConnectionConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query.toString())) {

            // Установка параметров в PreparedStatement
            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i));
            }

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                throw new ObjectNotFoundException("No entries found for the given author and filters.");
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionFailedException("Could not get total rows by author from database: " + e.getMessage());
        }
    }

    private List<Object> prepareStatmentForManager(int userId, EntryFilter entryFilter, StringBuilder query) {
        List<Object> params = new ArrayList<>();
        params.add(userId);

        if (entryFilter.getEntryType() != null) {
            query.append(" AND entry_type = ?::entrytype");
            params.add(entryFilter.getEntryType().name());
        }
        if (entryFilter.getBatchId() != null) {
            query.append(" AND batch_id = ?");
            params.add(entryFilter.getBatchId());
        }
        if (entryFilter.getDate() != null) {
            query.append(" AND date = ?");
            params.add(entryFilter.getDate());
        }
        return params;
    }


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
