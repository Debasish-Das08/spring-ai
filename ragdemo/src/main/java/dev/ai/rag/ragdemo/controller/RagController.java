package dev.ai.rag.ragdemo.controller;

import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@RestController
public class RagController {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;
    @Value("classpath:/prompts/AsianGames-details.st")
    private Resource gamesDetails;

    public RagController(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
        this.chatClient = chatClientBuilder.build();
        this.vectorStore = vectorStore;
    }

    @GetMapping("/asiangames/2026")
    public String getAsianGamesDetails(@RequestParam(value = "message", defaultValue = "How many Gold medals India won till today") String message) {
        List<Document> similarDocuments = vectorStore.similaritySearch(SearchRequest.builder().query(message).topK(2).build());
        List<String> textList = similarDocuments.stream().map(Document::getText).toList();
        Map<String, Object> promptMap = new HashMap<>();
        PromptTemplate promptTemplate = new PromptTemplate(gamesDetails);
        promptMap.put("question", message);
        promptMap.put("document", String.join("\n", textList));
        return chatClient.prompt(promptTemplate.create(promptMap)).call().content();
    }
}
