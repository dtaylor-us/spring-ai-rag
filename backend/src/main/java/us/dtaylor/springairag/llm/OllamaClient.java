package us.dtaylor.springairag.llm;

import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Service;

@Service
public class OllamaClient {

    private final OllamaChatModel chatModel;

    public OllamaClient(OllamaChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public String ask(String promptText) {
        Prompt prompt = new Prompt(new UserMessage(promptText));
        ChatResponse response = chatModel.call(prompt);
        return response.getResult().getOutput().getText();
    }
}
