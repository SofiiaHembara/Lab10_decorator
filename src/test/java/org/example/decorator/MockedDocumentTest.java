package org.example.decorator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MockedDocumentTest {

    @Test
    void testParse() {
        String expectedContent = "Mocked Document parse";
        MockedDocument mockedDocument = new MockedDocument("gs://bucket/mockdoc");

        String parsedContent = mockedDocument.parse();
        assertEquals(expectedContent, parsedContent, "parse() should return the mocked content.");
    }
    @Test
    void testToString() {
        MockedDocument mockedDocument = new MockedDocument("gs://bucket/mockdoc");
        String expectedString = "MockedDocument{gcsPath='gs://bucket/mockdoc'}";

        assertEquals(expectedString, mockedDocument.toString(), "toString() should return the correct string representation.");
    }
}
