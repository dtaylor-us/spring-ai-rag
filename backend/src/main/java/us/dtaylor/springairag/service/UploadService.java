package us.dtaylor.springairag.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import us.dtaylor.springairag.util.PdfLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

@Service
public class UploadService {

    private final PdfLoader pdfLoader;
    private final VectorStoreService vectorStoreService;

    public UploadService(PdfLoader pdfLoader, VectorStoreService vectorStoreService) {
        this.pdfLoader = pdfLoader;
        this.vectorStoreService = vectorStoreService;
    }

    public void upload(MultipartFile file) throws IOException {
        File tempFile = convertToFile(file);

        String content = pdfLoader.extractText(tempFile);
        vectorStoreService.indexText(file.getOriginalFilename(), content);

        Files.delete(tempFile.toPath());
    }

    private File convertToFile(MultipartFile multipart) {
        try {
            File file = File.createTempFile("upload-", ".pdf");
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(multipart.getBytes());
            }
            return file;
        } catch (Exception e) {
            throw new RuntimeException("Failed to store uploaded file", e);
        }
    }
}
