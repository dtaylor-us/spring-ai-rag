
package us.dtaylor.springairag.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PdfLoaderTest {

    @Test
    void extractText_ReturnsTextFromPdf() throws IOException {
        // Create a temp PDF file with known text
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
}
