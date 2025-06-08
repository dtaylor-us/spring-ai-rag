package us.dtaylor.springairag.service;

import org.springframework.stereotype.Service;
import us.dtaylor.springairag.llm.OllamaClient;

import java.util.List;

@Service
public class RagService {

    private final VectorStoreService vectorStoreService;
    private final OllamaClient ollamaClient;

    public RagService(VectorStoreService vectorStoreService, OllamaClient ollamaClient) {
        this.vectorStoreService = vectorStoreService;
        this.ollamaClient = ollamaClient;
    }

    public String askQuestion(String question) {
        List<String> context = vectorStoreService.findRelevant(question);
        String prompt = "Answer the question based only on the following context:\n"
                + String.join("\n", context)
                + "\nQuestion: " + question;
        return ollamaClient.ask(prompt);
    }
}
