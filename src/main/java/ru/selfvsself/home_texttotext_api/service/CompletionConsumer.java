package ru.selfvsself.home_texttotext_api.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.selfvsself.home_texttotext_api.model.client.Completion;

@Service
public class CompletionConsumer {

    private final ChatService chatService;

    public CompletionConsumer(ChatService chatService) {
        this.chatService = chatService;
    }

    @KafkaListener(topics = "${kafka.topic.incoming}", groupId = "${kafka.group}", containerFactory = "completionKafkaListenerContainerFactory")
    public void consumeOrder(Completion order) {
        System.out.println("Received completion: " + order);
    }
}
