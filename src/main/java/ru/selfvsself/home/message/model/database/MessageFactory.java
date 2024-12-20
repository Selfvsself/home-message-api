package ru.selfvsself.home.message.model.database;

import ru.selfvsself.home.message.model.Role;

import java.util.UUID;

public class MessageFactory {
    public static Message createErrorResponse(UUID userId, Role role) {
        return Message.builder()
                .userId(userId)
                .role(role)
                .status(MessageStatus.PROCESSING_ERROR)
                .tokens(0)
                .text("Error")
                .build();
    }

    public static Message createSuccess(Message message, String model) {
        return Message.builder()
                .id(message.getId())
                .requestId(message.getRequestId())
                .userId(message.getUserId())
                .text(message.getText())
                .role(message.getRole())
                .status(MessageStatus.PROCESSED)
                .model(model)
                .build();
    }
}
