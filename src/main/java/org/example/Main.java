package org.example;

import org.example.decorator.CashedDocument;
import org.example.decorator.MockedDocument;
import org.example.decorator.TimedDocument;
import org.example.decorator.Document;

public class Main {
    public static void main(String[] args) {
        // Base Document
        Document mockedDocument = new MockedDocument("gs://bucket/mockdoc");

        // Apply TimedDocument Decorator
        Document timedDocument = new TimedDocument(mockedDocument);

        // Apply CachedDocument Decorator
        Document cachedTimedDocument = new CashedDocument(timedDocument);

        // Usage
        String parsedText = cachedTimedDocument.parse();
        System.out.println(parsedText);

        // Subsequent call should retrieve from cache and measure time
        String cachedParsedText = cachedTimedDocument.parse();
        System.out.println(cachedParsedText);
    }
}
