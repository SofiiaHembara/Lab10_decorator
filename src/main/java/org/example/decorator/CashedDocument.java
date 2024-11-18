package org.example.decorator;

public class CashedDocument extends AbstractDecorator {

    public CashedDocument(Document decoratedDocument) {
        super(decoratedDocument);
    }

    @Override
    public String parse() {
        String cached = DBConnection.getInstance().getDocument(decoratedDocument.getGcsPath());
        if (cached != null) {
            System.out.println("Retrieved from cache.");
            return cached;
        } else {
            String parsed = decoratedDocument.parse();
            DBConnection.getInstance().createDocument(decoratedDocument.getGcsPath(), parsed);
            System.out.println("Result cached.");
            return parsed;
        }
    }

    // No need to override getGcsPath() as it's already delegated by AbstractDecorator
}
