package ru.selfvsself.home_texttotext_api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.selfvsself.model.ChatRequest;
import ru.selfvsself.model.ChatResponse;

import java.util.UUID;

@Slf4j
@Service
public class RequestConsumer {
    private final KafkaTemplate<String, ChatResponse> kafkaTemplate;
    @Value("${kafka.topic.response}")
    private String responseTopic;

    private final ChatResponseService chatResponseService;

    public RequestConsumer(ChatResponseService chatResponseService, KafkaTemplate<String, ChatResponse> kafkaTemplate) {
        this.chatResponseService = chatResponseService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "${kafka.topic.request}", groupId = "${kafka.group}", containerFactory = "textRequestKafkaListenerContainerFactory")
    public void requestProcessing(ChatRequest request) {
        if (request.getChatId() == null) {
            log.error("'chatId' field must not be empty");
            return;
        }
        if (!StringUtils.hasLength(request.getContent())) {
            log.error("'content' field must not be empty for chatId = {}", request.getChatId());
            return;
        }
        if (!StringUtils.hasLength(request.getUserName())) {
            log.info("'userName' field must not be empty for chatId = {}, set default name", request.getChatId());
            request.setUserName("unknown");
        }
        if (request.getRequestId() == null) {
            UUID requestId = UUID.randomUUID();
            log.info("'requestId' field must not be empty for chatId = {}, set random {}", request.getChatId(), requestId);
            request.setRequestId(requestId);
        }
        ChatResponse response = chatResponseService.processRequest(request);
        log.info("Response: {}", response);
        kafkaTemplate.send(responseTopic, request.getChatId().toString(), response);
    }
}
