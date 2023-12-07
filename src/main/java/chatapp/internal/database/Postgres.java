package chatapp.internal.database;

import chatapp.internal.logger.AppLogger;
import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.logging.Level;
public class Postgres {
    private static final Logger logger = AppLogger.getLogger();
    private static final String PASSWORD;
    private static final String USER;
    private static final String URL;

    static {
        // Load the environment variables from the .env file
        Dotenv dotenv = Dotenv.load();
        PASSWORD = dotenv.get("DB_PASSWORD");
        USER = dotenv.get("DB_USER");
        URL = dotenv.get("DB_URL");

        try {
            // Load the PostgresSQL JDBC driver
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Error loading PostgresSQL JDBC driver.", e);
            // If the driver is not found, throw a runtime exception
            throw new RuntimeException("Error loading PostgresSQL JDBC driver.", e);
        }
    }

    private static volatile Postgres instance;
    private final Connection connection;

    private Postgres() {
        try {
            // Create the database connection
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            logger.log(Level.INFO, "Connected to the database.");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error connecting to the database.", e);
            throw new RuntimeException("Error connecting to the database.", e);
        }
    }

    public static Postgres getInstance() {
        if (instance == null) {
            synchronized (Postgres.class) {
                if (instance == null) {
                    instance = new Postgres();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
            logger.log(Level.INFO, "Closed the database connection.");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error closing the database connection.", e);
            throw new RuntimeException("Error closing the database connection.", e);
        }
    }
}