package us.dtaylor.springairag.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import us.dtaylor.springairag.service.RagService;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/rag")
@Tag(name = "RAG API", description = "Endpoints for Retrieval-Augmented Generation")
public class RagController {

    private final RagService ragService;

    public RagController(RagService ragService) {
        this.ragService = ragService;
    }

    @Operation(
            summary = "Ask a question with RAG",
            description = "Submit a natural language question. Retrieves relevant context from vector store and generates a response using the LLM.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(example = "{\"question\": \"What is Spring AI?\"}"))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful response"),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            }
    )
    @PostMapping("/ask")
    public ResponseEntity<String> ask(@RequestBody Map<String, String> body) {
        String question = body.get("question");
        return ResponseEntity.ok(ragService.askQuestion(question));
    }
}
