package org.example.decorator;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class DBConnectionTest {

    private DBConnection dbConnection;

    @BeforeEach
    void setUp() {
        // Use an in-memory database for testing
        dbConnection = new DBConnection() {
            @SneakyThrows
            @Override
            protected void initializeDatabase() {
                String createTableSQL = "CREATE TABLE IF NOT EXISTS document_cache (" +
                        "gcsPath TEXT PRIMARY KEY," +
                        "parsedText TEXT NOT NULL)";
                Connection connection = null;
                try (Statement stmt = connection.createStatement()) {
                    stmt.execute(createTableSQL);
                }
            }
        };
        DBConnection.setInstance(dbConnection);
    }

    @AfterEach
    void tearDown() {
        // Close the connection and reset the instance
        dbConnection = null;
        DBConnection.setInstance(null);
    }

    @Test
    void testCreateAndGetDocument() throws SQLException {
        String gcsPath = "gs://bucket/testdoc";
        String parsedText = "Test Parsed Content";

        // Ensure the document does not exist initially
        assertNull(dbConnection.getDocument(gcsPath), "Document should not exist in cache initially.");

        // Create the document
        dbConnection.createDocument(gcsPath, parsedText);

        // Retrieve the document
        String retrievedText = dbConnection.getDocument(gcsPath);
        assertEquals(parsedText, retrievedText, "Retrieved text should match the inserted parsed text.");
    }

    @Test
    void testCreateDocumentOverwrite() throws SQLException {
        String gcsPath = "gs://bucket/testdoc2";
        String parsedText1 = "First Parsed Content";
        String parsedText2 = "Second Parsed Content";

        // Insert first parsed text
        dbConnection.createDocument(gcsPath, parsedText1);
        String retrievedText1 = dbConnection.getDocument(gcsPath);
        assertEquals(parsedText1, retrievedText1, "Retrieved text should match the first inserted parsed text.");

        // Overwrite with second parsed text
        dbConnection.createDocument(gcsPath, parsedText2);
        String retrievedText2 = dbConnection.getDocument(gcsPath);
        assertEquals(parsedText2, retrievedText2, "Retrieved text should match the second inserted parsed text.");
    }
}
