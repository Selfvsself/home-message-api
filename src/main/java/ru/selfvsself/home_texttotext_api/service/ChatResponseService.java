package ru.selfvsself.home_texttotext_api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.selfvsself.home_texttotext_api.model.CompletionMessage;
import ru.selfvsself.home_texttotext_api.model.ModelRequest;
import ru.selfvsself.home_texttotext_api.model.ModelResponse;
import ru.selfvsself.home_texttotext_api.model.Role;
import ru.selfvsself.home_texttotext_api.model.database.Message;
import ru.selfvsself.home_texttotext_api.model.database.MessageFactory;
import ru.selfvsself.home_texttotext_api.model.database.MessageStatus;
import ru.selfvsself.home_texttotext_api.service.database.MessageService;
import ru.selfvsself.home_texttotext_api.service.llm.ModelSelectionService;
import ru.selfvsself.model.ChatRequest;
import ru.selfvsself.model.ChatResponse;
import ru.selfvsself.model.ResponseType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ChatResponseService {
    private final MessageService messageService;
    private final ModelSelectionService modelSelectionService;
    @Value("${chat.max-tokens}")
    private Integer MAX_TOKENS;

    public ChatResponseService(MessageService messageService, ModelSelectionService modelSelectionService) {
        this.messageService = messageService;
        this.modelSelectionService = modelSelectionService;
    }

    public ChatResponse processRequest(ChatRequest chatRequest) {
        UUID userId = chatRequest.getParticipant().getUserId();

        Message requestMessage = MessageFactory.createErrorResponse(userId, Role.user);
        requestMessage.setId(chatRequest.getRequestId());
        requestMessage.setContent(chatRequest.getContent());
        Message responseMessage = MessageFactory.createErrorResponse(userId, Role.assistant);
        responseMessage.setRequestId(requestMessage.getId());

        ModelResponse modelResponse = getAnswer(chatRequest, userId);
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
                .participant(chatRequest.getParticipant())
                .model(responseMessage.getModel())
                .content(responseMessage.getContent())
                .requestId(modelResponse.getRequestId())
                .type(modelResponse.getType())
                .build();
    }

    private ModelResponse getAnswer(ChatRequest chatRequest, UUID userId) {
        ModelRequest modelRequest = createModelRequest(chatRequest, userId);
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
