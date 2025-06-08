package us.dtaylor.springairag.service;

import org.junit.jupiter.api.Test;
import us.dtaylor.springairag.llm.OllamaClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RagServiceTest {

    @Test
    void askQuestion_ReturnsExpectedAnswer() {
        VectorStoreService vectorStoreService = mock(VectorStoreService.class);
        OllamaClient ollamaClient = mock(OllamaClient.class);

        RagService ragService = new RagService(vectorStoreService, ollamaClient);

        String question = "What is RAG?";
        List<String> context = List.of("RAG stands for Retrieval-Augmented Generation.");
        String expectedPrompt = "Answer the question based only on the following:\n"
                + String.join("\n", context)
                + "\nQuestion: " + question;
        String expectedAnswer = "RAG is Retrieval-Augmented Generation.";

        when(vectorStoreService.findRelevant(question)).thenReturn(context);
        when(ollamaClient.ask(expectedPrompt)).thenReturn(expectedAnswer);

        String actual = ragService.askQuestion(question);

        assertEquals(expectedAnswer, actual);
        verify(vectorStoreService, times(1)).findRelevant(question);
        verify(ollamaClient, times(1)).ask(expectedPrompt);
    }
}
