package org.example.decorator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TimedDocumentTest {

    private Document mockDocument;
    private TimedDocument timedDocument;

    // For capturing System.out
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        mockDocument = mock(Document.class);
        when(mockDocument.parse()).thenReturn("Mocked Parsed Content");
        when(mockDocument.getGcsPath()).thenReturn("gs://bucket/mockdoc");

        timedDocument = new TimedDocument(mockDocument);

        // Redirect System.out to capture output
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        // Restore original System.out
        System.setOut(originalOut);
    }

    @Test
    void testParse() {
        String result = timedDocument.parse();
        assertEquals("Mocked Parsed Content", result, "parse() should return the same result as the decorated Document.");

        // Verify that parse() was called on the decorated Document
        verify(mockDocument, times(1)).parse();

        // Check that time was printed
        String output = outContent.toString();
        assertTrue(output.contains("Parsing took:"), "Should print the time taken for parsing.");
    }

    @Test
    void testGetGcsPath() {
        String gcsPath = timedDocument.getGcsPath();
        assertEquals("gs://bucket/mockdoc", gcsPath, "getGcsPath() should delegate to the decorated Document.");

        // Verify that getGcsPath() was called on the decorated Document
        verify(mockDocument, times(1)).getGcsPath();
    }
}
