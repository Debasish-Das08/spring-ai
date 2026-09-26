package dev.ai.prompts.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class PromptsController {

    private final ChatClient chatClient;

    public PromptsController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/dad-jokes")
    public String simpleCall()
    {
        return chatClient.prompt("Tell me a Dad joke").call().content();
    }

    @GetMapping("/youtube/{genre}")
    public String bestYoutuber(@PathVariable(value = "genre") String genre)
    {
        String message = "List the top 10 {genre} youtuber with the number of subscriber.";
        //PromptTemplate template = new PromptTemplate(message);
        //Prompt prompt = template.create(Map.of("genre",genre));
        Prompt p1 = new Prompt("List the top 10 "+genre+" youtuber with the number of subscriber.");
        return chatClient.prompt(p1).call().content();
    }

    @GetMapping("/youtube")
    public String bestYoutuberSystemPrompt(@RequestParam(value = "prompt")String promptString)
    {
        var systemMessage = new SystemMessage("You are a youtube analyser, you can only tell you details about youtube. If someone asks you something else you tel then you can only analyse youtube");
    var userMessage = new UserMessage(promptString);
    Prompt prompt = new Prompt(List.of(systemMessage,userMessage));
    return chatClient.prompt(prompt).call().content();
    }

    @GetMapping("/youtube/appended")
    public String bestYoutuberUserPrompt(@RequestParam(value = "prompt")String promptString)
    {
        //var userMessage = new UserMessage(promptString);
       // Prompt prompt = new Prompt(userMessage);
        return chatClient.prompt("You are a youtube analyser, you can only tell you " +
                        "details about youtube. If someone asks you something else you tel then " +
                        "you can only analyse youtube. Now go through the user prompt.")
                .user(promptString).call().content();
    }

}
