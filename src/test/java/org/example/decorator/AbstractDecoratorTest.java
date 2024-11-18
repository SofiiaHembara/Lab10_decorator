package org.example.decorator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AbstractDecoratorTest {

    @Test
    void testParseDelegation() {
        Document mockDocument = mock(Document.class);
        when(mockDocument.parse()).thenReturn("Delegated Content");

        // Anonymous subclass since AbstractDecorator is abstract
        AbstractDecorator decorator = new AbstractDecorator(mockDocument) {};

        String result = decorator.parse();
        assertEquals("Delegated Content", result, "parse() should delegate to the decorated Document.");
    }

    @Test
    void testGetGcsPathDelegation() {
        Document mockDocument = mock(Document.class);
        when(mockDocument.getGcsPath()).thenReturn("gs://bucket/doc");

        // Anonymous subclass since AbstractDecorator is abstract
        AbstractDecorator decorator = new AbstractDecorator(mockDocument) {};

        String gcsPath = decorator.getGcsPath();
        assertEquals("gs://bucket/doc", gcsPath, "getGcsPath() should delegate to the decorated Document.");
    }

    @Test
    void testNullDecoratedDocument() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new AbstractDecorator(null) {};
        });

        String expectedMessage = "Decorated Document cannot be null";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage), "Should throw IllegalArgumentException when decoratedDocument is null.");
    }
}
