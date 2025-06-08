package us.dtaylor.springairag.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import us.dtaylor.springairag.service.UploadService;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/rag")
@Tag(name = "Upload API", description = "Endpoints for uploading and indexing documents")
public class UploadController {

    private final UploadService uploadService;

    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @Operation(
            summary = "Upload a PDF document",
            description = "Extracts text from the uploaded PDF and stores embeddings in the vector store"
    )
    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) throws IOException {
        uploadService.upload(file);
        return ResponseEntity.ok("PDF uploaded and indexed.");
    }
}
