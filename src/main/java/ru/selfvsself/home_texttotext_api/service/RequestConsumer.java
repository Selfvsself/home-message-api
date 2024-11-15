package ru.selfvsself.home_texttotext_api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.selfvsself.model.ChatRequest;
import ru.selfvsself.model.ChatResponse;
import ru.selfvsself.model.ResponseType;

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
        if (request.getUserId() == null) {
            log.error("'userId' field must not be empty");
            return;
        }
        if (!StringUtils.hasLength(request.getContent())) {
            log.error("'content' field must not be empty for userId = {}", request.getUserId());
            return;
        }
        if (request.getRequestId() == null) {
            UUID requestId = UUID.randomUUID();
            log.info("'requestId' field must not be empty for userId = {}, set random {}", request.getUserId(), requestId);
            request.setRequestId(requestId);
        }
        ChatResponse response = ChatResponse.builder()
                .userId(request.getUserId())
                .requestId(request.getRequestId())
                .content("Error")
                .model("Error")
                .type(ResponseType.ERROR)
                .build();
        try {
            response = chatResponseService.processRequest(request);
            log.info("Response: {}", response);
        } catch (Exception e) {
            log.error("Error during processing request: {}", request, e);
        }
        kafkaTemplate.send(responseTopic, request.getUserId().toString(), response);
    }
}
