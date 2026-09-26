package dev.ai.firstdemo.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController
{

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    @GetMapping("/letschat")
    public String generate(@RequestParam (value = "message", defaultValue = "Tell me a good joke") String message)
    {
        System.out.println("here i am ");

        return chatClient.prompt().user(message).call().content();
    }
}
