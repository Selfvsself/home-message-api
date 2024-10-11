package ru.selfvsself.home_texttotext_api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.selfvsself.home_texttotext_api.client.MessageClient;
import ru.selfvsself.home_texttotext_api.client.UserClient;
import ru.selfvsself.home_texttotext_api.model.TextRequest;
import ru.selfvsself.home_texttotext_api.model.TextResponse;
import ru.selfvsself.home_texttotext_api.model.client.*;
import ru.selfvsself.home_texttotext_api.model.database.Message;
import ru.selfvsself.home_texttotext_api.model.database.MessageStatus;
import ru.selfvsself.home_texttotext_api.model.database.User;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class MessageService {
    private final UserClient userClient;
    private final MessageClient messageClient;

    private final ChatService chatService;

    public MessageService(UserClient userClient, MessageClient messageClient, ChatService chatService) {
        this.userClient = userClient;
        this.messageClient = messageClient;
        this.chatService = chatService;
    }

    public TextResponse processRequest(TextRequest textRequest) {
        User user = userClient.addUserIfNotExists(textRequest.getChatId(), textRequest.getUserName());

        Message requestMessage = createErrorRequestMessage(user.getId(), textRequest);
        Message responseMessage = createErrorResponseMessage(user.getId(), requestMessage.getId());

        ClientResponse clientResponse = getAnswer(textRequest);
        if (ResponseType.SUCCESS.equals(clientResponse.getType())) {
            requestMessage = createSuccessRequestMessage(requestMessage, clientResponse);
            responseMessage = createSuccessResponseMessage(responseMessage, clientResponse);
        }

        requestMessage = messageClient.createMessage(requestMessage);
        responseMessage = messageClient.createMessage(responseMessage);
        return TextResponse.builder()
                .chatId(textRequest.getChatId())
                .userName(textRequest.getUserName())
                .model(responseMessage.getModel())
                .content(responseMessage.getContent())
                .build();
    }

    private Message createErrorRequestMessage(UUID userId, TextRequest textRequest) {
        return Message
                .builder()
                .userId(userId)
                .role(Role.user)
                .status(MessageStatus.PROCESSING_ERROR)
                .tokens(0)
                .content(textRequest.getContent())
                .build();
    }

    private Message createErrorResponseMessage(UUID userId, UUID requestMessageId) {
        Message responseMessage = Message
                .builder()
                .userId(userId)
                .role(Role.assistant)
                .status(MessageStatus.PROCESSING_ERROR)
                .tokens(0)
                .requestId(requestMessageId)
                .build();
        messageClient.createMessage(responseMessage);
        return responseMessage;
    }

    private Message createSuccessRequestMessage(Message requestMessage, ClientResponse clientResponse) {
        return Message
                .builder()
                .id(requestMessage.getId())
                .userId(requestMessage.getUserId())
                .content(requestMessage.getContent())
                .role(Role.user)
                .status(MessageStatus.PROCESSED)
                .tokens(clientResponse.getRequestTokens())
                .model(clientResponse.getModel())
                .build();
    }

    private Message createSuccessResponseMessage(Message responseMessage, ClientResponse clientResponse) {
        return Message
                .builder()
                .id(responseMessage.getId())
                .userId(responseMessage.getUserId())
                .requestId(responseMessage.getRequestId())
                .role(Role.assistant)
                .status(MessageStatus.PROCESSED)
                .tokens(clientResponse.getResponseTokens())
                .content(clientResponse.getContent())
                .model(clientResponse.getModel())
                .build();
    }

    private ClientResponse getAnswer(TextRequest textRequest) {
        Completion completion = Completion
                .builder()
                .messages(List.of(new CompletionMessage(Role.user, textRequest.getContent())))
                .build();
        ClientResponse clientResponse = chatService.getAnswer(completion);
        log.info(clientResponse.toString());
        return clientResponse;
    }

}
