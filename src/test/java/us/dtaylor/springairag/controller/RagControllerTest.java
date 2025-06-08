// src/test/java/us/dtaylor/springairag/controller/RagControllerTest.java
package us.dtaylor.springairag.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import us.dtaylor.springairag.service.RagService;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class RagControllerTest {

    @Test
    void ask_ReturnsExpectedResponse() {
        RagService ragService = Mockito.mock(RagService.class);
        RagController controller = new RagController(ragService);

        String question = "What is RAG?";
        String expectedAnswer = "RAG stands for Retrieval-Augmented Generation.";
        when(ragService.askQuestion(question)).thenReturn(expectedAnswer);

        ResponseEntity<String> response = controller.ask(Map.of("question", question));

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedAnswer, response.getBody());
    }
}
