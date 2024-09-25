package ru.selfvsself.home_texttotext_api.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.selfvsself.home_texttotext_api.model.Completion;
import ru.selfvsself.home_texttotext_api.service.ChatService;

import java.util.List;

@Slf4j
@RequestMapping("/chat")
@RestController
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/completions")
    public String chat(@RequestBody Completion completion) {
        return chatService.getAnswer(completion);
    }
}
