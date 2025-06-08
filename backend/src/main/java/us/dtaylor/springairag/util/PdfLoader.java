package us.dtaylor.springairag.util;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

@Component
public class PdfLoader {

    public String extractText(File file) {
        try (PDDocument doc = PDDocument.load(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(doc);
        } catch (IOException e) {
            throw new RuntimeException("Failed to extract PDF text", e);
        }
    }
}
