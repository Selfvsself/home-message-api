package ru.selfvsself.home.message.service.database;

import org.springframework.stereotype.Service;
import ru.selfvsself.home.message.model.database.Message;
import ru.selfvsself.home.message.model.database.MessageStatus;
import ru.selfvsself.home.message.repository.MessageRepository;

import java.util.List;
import java.util.UUID;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message createMessage(Message message) {
        return messageRepository.save(message);
    }
    public List<Message> getLast20Messages(UUID userId, MessageStatus status) {
        return messageRepository.findTop20ByUserIdAndStatusOrderByUpdatedAtDesc(userId, status);
    }
}