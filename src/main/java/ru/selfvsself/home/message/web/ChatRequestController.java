package ru.selfvsself.home.message.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.selfvsself.home.message.service.ChatResponseService;
import ru.selfvsself.model.ChatRequest;

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
        if (request.getRequestId() == null) {
            return new ResponseEntity<>("Request id is null, request is " + request, HttpStatus.BAD_REQUEST);
        }
        if (request.getParticipant() == null) {
            return new ResponseEntity<>("Participant is null, request is " + request, HttpStatus.BAD_REQUEST);
        }
        if (request.getParticipant().getUserId() == null) {
            return new ResponseEntity<>("UserId is null, request is " + request, HttpStatus.BAD_REQUEST);
        }
        if (request.getContent() != null && !StringUtils.hasLength(request.getContent().getText())) {
            return new ResponseEntity<>("content is null, request is " + request, HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(chatResponseService.processRequest(request));
    }
}
