package ru.selfvsself.home_texttotext_api.client;

import org.springframework.stereotype.Service;
import ru.selfvsself.home_texttotext_api.model.database.Message;
import ru.selfvsself.home_texttotext_api.repository.MessageRepository;

@Service
public class MessageClient {

    private final MessageRepository messageRepository;

    public MessageClient(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message createMessage(Message message) {
        return messageRepository.save(message);
    }
}