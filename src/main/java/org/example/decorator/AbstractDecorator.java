package org.example.decorator;

public abstract class AbstractDecorator implements Document {
    protected Document decoratedDocument;

    public AbstractDecorator(Document decoratedDocument) {
        this.decoratedDocument = decoratedDocument;
    }
    @Override
    public String parse() {
        return decoratedDocument.parse();
    }

    @Override
    public String getGcsPath() {
        return decoratedDocument.getGcsPath();
    }
}
