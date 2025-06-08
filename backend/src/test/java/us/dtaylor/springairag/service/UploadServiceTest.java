
package us.dtaylor.springairag.service;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.web.multipart.MultipartFile;
import us.dtaylor.springairag.util.PdfLoader;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UploadServiceTest {

    @Test
    void upload_ExtractsTextAndIndexesAndDeletesTempFile() throws Exception {
        PdfLoader pdfLoader = mock(PdfLoader.class);
        VectorStoreService vectorStoreService = mock(VectorStoreService.class);
        MultipartFile multipartFile = mock(MultipartFile.class);

        String filename = "test.pdf";
        String fileContent = "PDF content";
        byte[] fileBytes = fileContent.getBytes();

        when(multipartFile.getOriginalFilename()).thenReturn(filename);
        when(multipartFile.getBytes()).thenReturn(fileBytes);
        when(pdfLoader.extractText(any(File.class))).thenReturn("Extracted text");

        UploadService uploadService = new UploadService(pdfLoader, vectorStoreService);

        uploadService.upload(multipartFile);

        // Capture the temp file passed to extractText
        ArgumentCaptor<File> fileCaptor = ArgumentCaptor.forClass(File.class);
        verify(pdfLoader).extractText(fileCaptor.capture());
        File tempFile = fileCaptor.getValue();

        verify(vectorStoreService).indexText(eq(filename), eq("Extracted text"));
        assertTrue(!tempFile.exists(), "Temp file should be deleted after upload");
    }
}
