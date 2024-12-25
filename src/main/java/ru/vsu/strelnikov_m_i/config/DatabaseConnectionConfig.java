package ru.vsu.strelnikov_m_i.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnectionConfig {
    private static Properties loadProperties() throws IOException {
        Properties properties = new Properties();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try (InputStream fileInputStream = classLoader.getResourceAsStream("application.yaml")) {
            properties.load(fileInputStream);
        }
        return properties;
    }

    public static Connection getConnection() {
        Connection connection = null;
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
            Properties properties = loadProperties();
            String url = properties.getProperty("url");
            String user = properties.getProperty("username");
            String password = properties.getProperty("password");
            String driver = properties.getProperty("driverClassName");
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException | SQLException | IOException e) {
            System.out.println(e.getMessage());
        }
        return connection;
    }
}