package org.example.decorator;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.ImageSource;
import com.google.cloud.vision.v1.TextAnnotation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.*;
import javax.swing.text.Document;
import java.util.ArrayList;
import java.util.List;
@Getter
@AllArgsConstructor
public class SmartDocument implements Document {
    public String gcsPath;

    @SneakyThrows
    public String parse() {
        List<AnnotateImageRequest> requests = new ArrayList<>();

        ImageSource imgSource = ImageSource.newBuilder().setGcsImageUri(gcsPath).build();
        Image img = Image.newBuilder().setSource(imgSource).build();
        Feature feat = Feature.newBuilder().setType(Type.DOCUMENT_TEXT_DETECTION).build();
        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();
            client.close();

            for (AnnotateImageResponse res : responses) {
                TextAnnotation annotation = res.getFullTextAnnotation();
                return annotation.getText();
            }
        }
        return "";
    }

    @Override
    public int getLength() {
        return 0;
    }

    @Override
    public void addDocumentListener(DocumentListener listener) {

    }

    @Override
    public void removeDocumentListener(DocumentListener listener) {

    }

    @Override
    public void addUndoableEditListener(UndoableEditListener listener) {

    }

    @Override
    public void removeUndoableEditListener(UndoableEditListener listener) {

    }

    @Override
    public Object getProperty(Object key) {
        return null;
    }

    @Override
    public void putProperty(Object key, Object value) {

    }

    @Override
    public void remove(int offs, int len) throws BadLocationException {

    }

    @Override
    public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {

    }

    @Override
    public String getText(int offset, int length) throws BadLocationException {
        return "";
    }

    @Override
    public void getText(int offset, int length, Segment txt) throws BadLocationException {

    }

    @Override
    public Position getStartPosition() {
        return null;
    }

    @Override
    public Position getEndPosition() {
        return null;
    }

    @Override
    public Position createPosition(int offs) throws BadLocationException {
        return null;
    }

    @Override
    public Element[] getRootElements() {
        return new Element[0];
    }

    @Override
    public Element getDefaultRootElement() {
        return null;
    }

    @Override
    public void render(Runnable r) {

    }
}