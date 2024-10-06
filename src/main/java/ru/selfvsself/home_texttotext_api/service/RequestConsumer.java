package ru.selfvsself.home_texttotext_api.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.selfvsself.home_texttotext_api.model.client.Completion;

@Service
public class RequestConsumer {

    private final ChatService chatService;

    public RequestConsumer(ChatService chatService) {
        this.chatService = chatService;
    }

    @KafkaListener(topics = "${kafka.topic.incoming}", groupId = "${kafka.group}", containerFactory = "textRequestKafkaListenerContainerFactory")
    public void requestProcessing(Completion order) {
        System.out.println("Received completion: " + order);
    }
}
