package us.dtaylor.springairag.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import us.dtaylor.springairag.service.UploadService;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class UploadControllerTest {

    @Test
    void upload_ReturnsSuccessMessage() throws IOException {
        UploadService uploadService = mock(UploadService.class);
        UploadController controller = new UploadController(uploadService);

        MultipartFile file = mock(MultipartFile.class);

        ResponseEntity<String> response = controller.upload(file);

        verify(uploadService, times(1)).upload(file);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("PDF uploaded and indexed.", response.getBody());
    }
}
