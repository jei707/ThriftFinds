package com.example.thrifters.database;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    // Database configuration constants
    private static final String DB_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "thriftfinds";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    // Path to the SQL file for database initialization
    private static final String SQL_FILE_PATH = "C:/Users/User/Desktop/ThriftFinds/OOP/src/main/java/com/example/thrifters/database/thriftfinds.sql";

    // Singleton connection instance
    private static Connection connection;

    /**
     * Gets the database connection. Initializes the database if it does not exist.
     * 
     * @return Connection to the database
     * @throws SQLException if there is an error connecting to the database
     */
    public static synchronized Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Connect to the MySQL server
                Connection serverConnection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

                // Initialize the database if necessary
                initializeDatabase(serverConnection);

                // Connect to the specific database
                connection = DriverManager.getConnection(DB_URL + DB_NAME, DB_USER, DB_PASSWORD);
                System.out.println("Connected to the database: " + DB_NAME);
            } catch (SQLException | IOException e) {
                System.err.println("Error establishing database connection: " + e.getMessage());
                throw new SQLException("Failed to connect to the database", e);
            }
        }
        return connection;
    }

    /**
     * Initializes the database by creating it if it does not exist and importing the SQL file.
     * 
     * @param serverConnection Connection to the MySQL server
     * @throws SQLException if there is an error executing SQL commands
     * @throws IOException  if there is an error reading the SQL file
     */
    private static void initializeDatabase(Connection serverConnection) throws SQLException, IOException {
        try (Statement statement = serverConnection.createStatement()) {
            // Create the database if it does not exist
            statement.execute("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
            System.out.println("Database " + DB_NAME + " created or already exists.");

            // Check if the SQL file exists and execute it
            if (Files.exists(Paths.get(SQL_FILE_PATH))) {
                String sql = new String(Files.readAllBytes(Paths.get(SQL_FILE_PATH)));

                // Split SQL commands by semicolon and execute them individually
                String[] sqlCommands = sql.split(";");
                try (Connection dbConnection = DriverManager.getConnection(DB_URL + DB_NAME, DB_USER, DB_PASSWORD);
                     Statement dbStatement = dbConnection.createStatement()) {
                    for (String command : sqlCommands) {
                        if (!command.trim().isEmpty()) {
                            dbStatement.execute(command.trim());
                        }
                    }
                    System.out.println("Database initialized successfully from SQL file.");
                }
            } else {
                System.err.println("SQL file not found at: " + SQL_FILE_PATH);
            }
        }
    }

    /**
     * Closes the database connection if it is open.
     */
    public static synchronized void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }
}
