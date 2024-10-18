package ru.selfvsself.home_texttotext_api.model.database;

import ru.selfvsself.home_texttotext_api.model.Role;

import java.util.UUID;

public class MessageFactory {
    public static Message createErrorResponse(UUID userId, Role role) {
        return Message.builder()
                .userId(userId)
                .role(role)
                .status(MessageStatus.PROCESSING_ERROR)
                .tokens(0)
                .content("Error")
                .build();
    }

    public static Message createSuccess(Message message, String model) {
        return Message.builder()
                .id(message.getId())
                .requestId(message.getRequestId())
                .userId(message.getUserId())
                .content(message.getContent())
                .role(message.getRole())
                .status(MessageStatus.PROCESSED)
                .model(model)
                .build();
    }
}
