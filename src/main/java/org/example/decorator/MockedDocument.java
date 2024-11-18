package org.example.decorator;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MockedDocument implements Document {
    private String gcsPath; // Corrected field name

    @Override
    public String parse() {
        try {
            Thread.sleep(2000); // Simulate parsing delay
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "Mocked Document parse";
    }

    @Override
    public String getGcsPath() {
        return gcsPath; // Delegated to the actual field
    }

    @Override
    public String toString() {
        return "MockedDocument{" +
                "gcsPath='" + gcsPath + '\'' +
                '}';
    }
}
