package ru.selfvsself.home_texttotext_api.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.selfvsself.home_texttotext_api.model.TextRequest;
import ru.selfvsself.home_texttotext_api.service.MessageService;

@Slf4j
@RequestMapping("/chat")
@RestController
public class ChatController {
    private final MessageService messageService;

    public ChatController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/completions")
    public ResponseEntity<?> chat(@RequestBody TextRequest request) {
        if (!StringUtils.hasLength(request.getContent())) {
            return new ResponseEntity<>("'content' field must not be empty", HttpStatus.BAD_REQUEST);
        }
        if (!StringUtils.hasLength(request.getUserName())) {
            return new ResponseEntity<>("'userName' field must not be empty", HttpStatus.BAD_REQUEST);
        }
        if (request.getChatId() == null) {
            return new ResponseEntity<>("'chatId' field must not be empty", HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(messageService.processRequest(request));
    }
}
