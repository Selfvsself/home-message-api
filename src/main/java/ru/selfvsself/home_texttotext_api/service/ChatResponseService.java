package ru.selfvsself.home_texttotext_api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.selfvsself.home_texttotext_api.model.*;
import ru.selfvsself.home_texttotext_api.model.database.Message;
import ru.selfvsself.home_texttotext_api.model.database.MessageFactory;
import ru.selfvsself.home_texttotext_api.model.database.MessageStatus;
import ru.selfvsself.home_texttotext_api.model.database.User;
import ru.selfvsself.home_texttotext_api.service.database.MessageService;
import ru.selfvsself.home_texttotext_api.service.database.UserService;
import ru.selfvsself.home_texttotext_api.service.llm.ModelSelectionService;
import ru.selfvsself.model.ChatRequest;
import ru.selfvsself.model.ChatResponse;

import java.util.*;

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

        Message requestMessage = MessageFactory.createErrorResponse(user.getId(), Role.user);
        requestMessage.setId(chatRequest.getRequestId());
        requestMessage.setContent(chatRequest.getContent());
        Message responseMessage = MessageFactory.createErrorResponse(user.getId(), Role.assistant);
        responseMessage.setRequestId(requestMessage.getId());

        ModelResponse modelResponse = getAnswer(chatRequest, user);
        if (ResponseType.SUCCESS.equals(modelResponse.getType())) {
            requestMessage = MessageFactory.createSuccess(requestMessage, modelResponse.getModel());
            requestMessage.setTokens(modelResponse.getRequestTokens() - modelResponse.getHistoryTokens());
            responseMessage = MessageFactory.createSuccess(responseMessage, modelResponse.getModel());
            responseMessage.setTokens(modelResponse.getResponseTokens());
            responseMessage.setContent(modelResponse.getContent());
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

    private ModelResponse getAnswer(ChatRequest chatRequest, User user) {
        ModelRequest modelRequest = createModelRequest(chatRequest, user.getId());
        ModelResponse modelResponse;
        modelResponse = modelSelectionService.getAnswer(modelRequest);
        log.info(modelResponse.toString());
        return modelResponse;
    }

    private ModelRequest createModelRequest(ChatRequest chatRequest, UUID userId) {
        List<CompletionMessage> completionMessages = new ArrayList<>();
        completionMessages.add(new CompletionMessage(Role.user, chatRequest.getContent()));
        int historyTokens = 0;
        if (chatRequest.isUseMessageHistory()) {
            List<Message> messages = messageService.getLast20Messages(userId, MessageStatus.PROCESSED);
            messages = getLimitedMessagesByTokens(messages, MAX_TOKENS);
            for (Message message : messages) {
                completionMessages.add(new CompletionMessage(message.getRole(), message.getContent()));
                historyTokens += message.getTokens();
            }
        }
        Collections.reverse(completionMessages);
        return ModelRequest
                .builder()
                .messages(completionMessages)
                .requestId(chatRequest.getRequestId())
                .useLocalModel(chatRequest.isUseLocalModel())
                .historyTokens(historyTokens)
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
