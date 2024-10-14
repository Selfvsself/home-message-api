package ru.selfvsself.home_texttotext_api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.selfvsself.home_texttotext_api.model.ChatRequest;
import ru.selfvsself.home_texttotext_api.model.ChatResponse;
import ru.selfvsself.home_texttotext_api.model.client.*;
import ru.selfvsself.home_texttotext_api.model.database.Message;
import ru.selfvsself.home_texttotext_api.model.database.MessageStatus;
import ru.selfvsself.home_texttotext_api.model.database.User;
import ru.selfvsself.home_texttotext_api.service.database.MessageService;
import ru.selfvsself.home_texttotext_api.service.database.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ChatResponseService {
    private final UserService userService;
    private final MessageService messageService;
    private final ModelSelectionService modelSelectionService;
    @Value("${chat.max-tokens}")
    private Integer MAX_TOKENS;

    public ChatResponseService(UserService userService, MessageService messageService, ModelSelectionService modelSelectionService) {
        this.userService = userService;
        this.messageService = messageService;
        this.modelSelectionService = modelSelectionService;
    }

    public ChatResponse processRequest(ChatRequest chatRequest) {
        User user = userService.addUserIfNotExists(chatRequest.getChatId(), chatRequest.getUserName());

        Message requestMessage = createErrorRequestMessage(user.getId(), chatRequest);
        Message responseMessage = createErrorResponseMessage(user.getId(), requestMessage.getId());

        ModelResponse modelResponse = getAnswer(chatRequest, user);
        if (ResponseType.SUCCESS.equals(modelResponse.getType())) {
            requestMessage = createSuccessRequestMessage(requestMessage, modelResponse);
            responseMessage = createSuccessResponseMessage(responseMessage, modelResponse);
        }

        requestMessage = messageService.createMessage(requestMessage);
        responseMessage.setRequestId(requestMessage.getId());
        responseMessage = messageService.createMessage(responseMessage);
        return ChatResponse.builder()
                .chatId(chatRequest.getChatId())
                .userName(chatRequest.getUserName())
                .model(responseMessage.getModel())
                .content(responseMessage.getContent())
                .requestId(modelResponse.getRequestId())
                .build();
    }

    private Message createErrorRequestMessage(UUID userId, ChatRequest chatRequest) {
        return Message
                .builder()
                .id(chatRequest.getRequestId())
                .userId(userId)
                .role(Role.user)
                .status(MessageStatus.PROCESSING_ERROR)
                .tokens(0)
                .content(chatRequest.getContent())
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
        messageService.createMessage(responseMessage);
        return responseMessage;
    }

    private Message createSuccessRequestMessage(Message requestMessage, ModelResponse modelResponse) {
        return Message
                .builder()
                .id(requestMessage.getId())
                .userId(requestMessage.getUserId())
                .content(requestMessage.getContent())
                .role(Role.user)
                .status(MessageStatus.PROCESSED)
                .tokens(modelResponse.getRequestTokens())
                .model(modelResponse.getModel())
                .build();
    }

    private Message createSuccessResponseMessage(Message responseMessage, ModelResponse modelResponse) {
        return Message
                .builder()
                .id(responseMessage.getId())
                .userId(responseMessage.getUserId())
                .requestId(responseMessage.getRequestId())
                .role(Role.assistant)
                .status(MessageStatus.PROCESSED)
                .tokens(modelResponse.getResponseTokens())
                .content(modelResponse.getContent())
                .model(modelResponse.getModel())
                .build();
    }

    private ModelResponse getAnswer(ChatRequest chatRequest, User user) {
        ModelRequest modelRequest = createModelRequest(chatRequest, user.getId());
        ModelResponse modelResponse;
        if (chatRequest.isUseLocalModel()) {
            modelResponse = modelSelectionService.getAnswerLocally(modelRequest);
        } else {
            modelResponse = modelSelectionService.getAnswer(modelRequest);
        }
        log.info(modelResponse.toString());
        return modelResponse;
    }

    private ModelRequest createModelRequest(ChatRequest chatRequest, UUID userId) {
        List<CompletionMessage> completionMessages = new ArrayList<>();
        completionMessages.add(new CompletionMessage(Role.user, chatRequest.getContent()));
        if (chatRequest.isUseMessageHistory()) {
            List<Message> messages = messageService.getLast20Messages(userId, MessageStatus.PROCESSED);
            messages = getLimitedMessagesByTokens(messages, MAX_TOKENS);
            List<CompletionMessage> history = messages.stream()
                    .map(m -> new CompletionMessage(m.getRole(), m.getContent()))
                    .toList();
            completionMessages.addAll(history);
        }
        return ModelRequest
                .builder()
                .messages(completionMessages)
                .requestId(chatRequest.getRequestId())
                .useLocalModel(chatRequest.isUseLocalModel())
                .build();
    }

    public List<Message> getLimitedMessagesByTokens(List<Message> messages, int maxTokens) {
        List<Message> result = new ArrayList<>();
        int tokenSum = 0;
        for (Message message : messages) {
            if (message.getTokens() != null) {
                tokenSum += message.getTokens();
                if (tokenSum > maxTokens) {
                    break;
                }
                result.add(message);
            }
        }
        return result;
    }

}
