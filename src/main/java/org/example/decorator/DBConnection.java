package org.example.decorator;

import lombok.SneakyThrows;

import java.sql.*;

public abstract class DBConnection {
    private static DBConnection dbConnection;
    private Connection connection;

    @SneakyThrows
    DBConnection() {
        // Use a relative path or configure via environment variables for flexibility
        this.connection = DriverManager.getConnection("jdbc:sqlite:cache.db");
        initializeDatabase();
    }

    // Initialize the cache table if it doesn't exist
    @SneakyThrows
    private void initializeDatabase() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS document_cache (" +
                "gcsPath TEXT PRIMARY KEY," +
                "parsedText TEXT NOT NULL)";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        }
    }

    @SneakyThrows
    public String getDocument(String gcsPath) {
        String query = "SELECT parsedText FROM document_cache WHERE gcsPath = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, gcsPath);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("parsedText");
                }
            }
        }
        return null;
    }

    @SneakyThrows
    public void createDocument(String gcsPath, String parsedText) {
        String insertSQL = "INSERT OR REPLACE INTO document_cache (gcsPath, parsedText) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setString(1, gcsPath);
            pstmt.setString(2, parsedText);
            pstmt.executeUpdate();
        }
    }

    public static DBConnection getInstance() {
        if (dbConnection == null) {
            dbConnection = new DBConnection();
        }
        return dbConnection;
    }

    // For testing purposes: allow setting a mock instance
    public static void setInstance(DBConnection instance) {
        dbConnection = instance;
    }

    @SneakyThrows
    protected abstract void initializeDatabase();
}
