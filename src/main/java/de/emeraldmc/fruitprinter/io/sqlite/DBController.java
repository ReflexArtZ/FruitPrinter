package de.emeraldmc.fruitprinter.io.sqlite;

import de.emeraldmc.fruitprinter.util.Debug;

import java.sql.*;
import java.util.Objects;

/**
 * Controller for controlling database access
 */
public class DBController {
    private String sqlitePath;
    private Connection connection;

    public DBController(String sqlitePath) throws SQLException {
        Objects.requireNonNull(sqlitePath);
        this.sqlitePath = sqlitePath;
        openConnection();
    }

    public void openConnection() throws SQLException {
        connection = DriverManager.getConnection(url());
        Debug.print("Connected to SQLite database!");
    }
    public void closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
            connection = null;
            Debug.print("Closed SQLite connection!");
        }
    }
    public Statement getStatement() throws SQLException {
        return connection.createStatement();
    }
    public PreparedStatement getPreparedStatement(String sql) throws SQLException {
        return connection.prepareStatement(sql);
    }

    private String url() {
        return "jdbc:sqlite:"+sqlitePath;
    }
}
