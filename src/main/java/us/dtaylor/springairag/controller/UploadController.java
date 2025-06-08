package us.dtaylor.springairag.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import us.dtaylor.springairag.service.UploadService;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/rag")
public class UploadController {

    private final UploadService uploadService;

    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) throws IOException {
        uploadService.upload(file);
        return ResponseEntity.ok("PDF uploaded and indexed.");
    }
}
