package ru.selfvsself.home_texttotext_api.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.selfvsself.home_texttotext_api.model.TextRequest;

@Service
public class RequestConsumer {

    private final MessageService messageService;

    public RequestConsumer(MessageService messageService) {
        this.messageService = messageService;
    }

    @KafkaListener(topics = "${kafka.topic.incoming}", groupId = "${kafka.group}", containerFactory = "textRequestKafkaListenerContainerFactory")
    public void requestProcessing(TextRequest order) {
        System.out.println("Received completion: " + order);
    }
}
