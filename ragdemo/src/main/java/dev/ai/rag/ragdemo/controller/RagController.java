package dev.ai.rag.ragdemo.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class RagController {
    @Value("classpath:/prompts/lukafaku-faq.st")
    private Resource lukaFAQResourcePrompt;

    @Value("classpath:/docs/lukafaku-faq.txt")
    private Resource getLukaFAQResource;

    private final ChatClient chatClient;

    public RagController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/lukafaku/rules")
    public String getCricketFAQ(@RequestParam(value = "message", defaultValue = "Tell me the basic luka faku rules") String message,
                                @RequestParam(value = "stuffit", defaultValue = "false") boolean stuffit) {
        PromptTemplate promptTemplate = new PromptTemplate(lukaFAQResourcePrompt);
        Map<String, Object> promptMap = new HashMap<>();
        if (stuffit) {
            promptMap.put("context", getLukaFAQResource);
        } else {
            promptMap.put("context", "");
        }
        promptMap.put("question", message);

        return chatClient.prompt(promptTemplate.create(promptMap)).call().content();

    }

}
