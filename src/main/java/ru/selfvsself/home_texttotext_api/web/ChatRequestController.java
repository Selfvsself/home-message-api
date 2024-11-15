package ru.selfvsself.home_texttotext_api.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.selfvsself.model.ChatRequest;
import ru.selfvsself.home_texttotext_api.service.ChatResponseService;

import java.util.UUID;

@Slf4j
@RequestMapping("/chat")
@RestController
public class ChatRequestController {
    private final ChatResponseService chatResponseService;

    public ChatRequestController(ChatResponseService chatResponseService) {
        this.chatResponseService = chatResponseService;
    }

    @PostMapping("/completions")
    public ResponseEntity<?> chat(@RequestBody ChatRequest request) {
        if (!StringUtils.hasLength(request.getContent())) {
            return new ResponseEntity<>("'content' field must not be empty", HttpStatus.BAD_REQUEST);
        }
        if (request.getUserId() == null) {
            return new ResponseEntity<>("'chatId' field must not be empty", HttpStatus.BAD_REQUEST);
        }
        if (request.getRequestId() == null) {
            request.setRequestId(UUID.randomUUID());
        }

        return ResponseEntity.ok(chatResponseService.processRequest(request));
    }
}
