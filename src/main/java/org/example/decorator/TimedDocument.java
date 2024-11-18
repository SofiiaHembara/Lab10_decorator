package org.example.decorator;

import java.time.Duration;
import java.time.Instant;

public class TimedDocument extends AbstractDecorator {

    public TimedDocument(Document decoratedDocument) {
        super(decoratedDocument);
    }

    @Override
    public String parse() {
        Instant startTime = Instant.now();

        String parsed = decoratedDocument.parse();

        Instant endTime = Instant.now();

        long timeElapsed = Duration.between(startTime, endTime).toMillis();
        System.out.println("Parsing took: " + timeElapsed + " milliseconds.");

        return parsed;
    }

    // No need to override getGcsPath() as it's already delegated by AbstractDecorator
}
