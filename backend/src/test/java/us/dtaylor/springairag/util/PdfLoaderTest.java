package us.dtaylor.springairag.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class PdfLoaderTest {

    @Test
    void extractText_ReturnsTextFromPdf() throws IOException {
        File tempPdf = File.createTempFile("test", ".pdf");
        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.LETTER);
            doc.addPage(page);
            try (PDPageContentStream content = new PDPageContentStream(doc, page)) {
                content.beginText();
                content.setFont(PDType1Font.HELVETICA, 12);
                content.newLineAtOffset(100, 700);
                content.showText("Hello PDF!");
                content.endText();
            }
            doc.save(tempPdf);
        }

        PdfLoader loader = new PdfLoader();
        String text = loader.extractText(tempPdf);

        assertTrue(text.contains("Hello PDF!"));

        tempPdf.delete();
    }

    @Test
    void extractText_ReturnsTextFromMultiPagePdf() throws IOException {
        File tempPdf = File.createTempFile("multi", ".pdf");
        try (PDDocument doc = new PDDocument()) {
            for (int i = 1; i <= 2; i++) {
                PDPage page = new PDPage(PDRectangle.LETTER);
                doc.addPage(page);
                try (PDPageContentStream content = new PDPageContentStream(doc, page)) {
                    content.beginText();
                    content.setFont(PDType1Font.HELVETICA, 12);
                    content.newLineAtOffset(100, 700);
                    content.showText("Page " + i);
                    content.endText();
                }
            }
            doc.save(tempPdf);
        }

        PdfLoader loader = new PdfLoader();
        String text = loader.extractText(tempPdf);

        assertTrue(text.contains("Page 1"));
        assertTrue(text.contains("Page 2"));

        tempPdf.delete();
    }

    @Test
    void extractText_ThrowsRuntimeExceptionForNonPdfFile() throws IOException {
        File tempFile = File.createTempFile("notapdf", ".txt");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("This is not a PDF");
        }

        PdfLoader loader = new PdfLoader();
        Exception ex = assertThrows(RuntimeException.class, () -> loader.extractText(tempFile));
        assertTrue(ex.getMessage().contains("Failed to extract PDF text"));

        tempFile.delete();
    }

    @Test
    void extractText_ReturnsEmptyStringForEmptyPdf() throws IOException {
        File tempPdf = File.createTempFile("empty", ".pdf");
        try (PDDocument doc = new PDDocument()) {
            doc.save(tempPdf);
        }

        PdfLoader loader = new PdfLoader();
        String text = loader.extractText(tempPdf);

        assertTrue(text.trim().isEmpty());

        tempPdf.delete();
    }
}
