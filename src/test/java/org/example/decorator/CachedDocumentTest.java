package org.example.decorator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CachedDocumentTest {

    private Document mockDocument;
    private CashedDocument cachedDocument;

    private DBConnection mockDBConnection;

    @BeforeEach
    void setUp() {
        mockDocument = mock(Document.class);
        when(mockDocument.parse()).thenReturn("Parsed Content");
        when(mockDocument.getGcsPath()).thenReturn("gs://bucket/doc1");

        // Mock DBConnection singleton
        mockDBConnection = mock(DBConnection.class);
        DBConnection.setInstance(mockDBConnection);

        cachedDocument = new CashedDocument(mockDocument);
    }

    @Test
    void testParse_CacheMiss() throws SQLException {
        // Simulate cache miss
        when(mockDBConnection.getDocument("gs://bucket/doc1")).thenReturn(null);

        String result = cachedDocument.parse();
        assertEquals("Parsed Content", result, "parse() should return the parsed content from the decorated Document.");

        // Verify that parse() was called on the decorated Document
        verify(mockDocument, times(1)).parse();

        // Verify that createDocument() was called to cache the result
        verify(mockDBConnection, times(1)).createDocument("gs://bucket/doc1", "Parsed Content");
    }

    @Test
    void testParse_CacheHit() throws SQLException {
        // Simulate cache hit
        when(mockDBConnection.getDocument("gs://bucket/doc1")).thenReturn("Cached Parsed Content");

        String result = cachedDocument.parse();
        assertEquals("Cached Parsed Content", result, "parse() should return the cached content.");

        // Verify that parse() was NOT called on the decorated Document
        verify(mockDocument, times(0)).parse();

        // Verify that createDocument() was NOT called
        verify(mockDBConnection, times(0)).createDocument(anyString(), anyString());
    }

    @Test
    void testGetGcsPath() {
        String gcsPath = cachedDocument.getGcsPath();
        assertEquals("gs://bucket/doc1", gcsPath, "getGcsPath() should delegate to the decorated Document.");

        // Verify that getGcsPath() was called on the decorated Document
        verify(mockDocument, times(1)).getGcsPath();
    }
}
