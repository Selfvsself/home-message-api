package ru.selfvsself.home_texttotext_api.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.selfvsself.home_texttotext_api.model.ChatRequest;

@Service
public class RequestConsumer {

    private final ChatResponseService chatResponseService;

    public RequestConsumer(ChatResponseService chatResponseService) {
        this.chatResponseService = chatResponseService;
    }

    @KafkaListener(topics = "${kafka.topic.incoming}", groupId = "${kafka.group}", containerFactory = "textRequestKafkaListenerContainerFactory")
    public void requestProcessing(ChatRequest order) {
        System.out.println("Received completion: " + order);
    }
}
