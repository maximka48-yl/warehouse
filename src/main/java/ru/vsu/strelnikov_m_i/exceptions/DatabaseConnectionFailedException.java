package ru.vsu.strelnikov_m_i.exceptions;

public class DatabaseConnectionFailedException extends RuntimeException {
    public DatabaseConnectionFailedException(String message) {
        super(message);
    }
}
