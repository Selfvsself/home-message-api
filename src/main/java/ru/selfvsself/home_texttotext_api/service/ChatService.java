package ru.selfvsself.home_texttotext_api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.selfvsself.home_texttotext_api.model.TextRequest;
import ru.selfvsself.home_texttotext_api.model.TextResponse;
import ru.selfvsself.home_texttotext_api.model.client.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class ChatService {

    private final OpenAIChatService openAIService;
    private final LocalChatService localChatService;

    public ChatService(OpenAIChatService openAIService, LocalChatService localChatService) {
        this.openAIService = openAIService;
        this.localChatService = localChatService;
    }

    public TextResponse getAnswer(TextRequest textRequest) {
        Completion completion = Completion
                .builder()
                .messages(List.of(new Message(Role.user, textRequest.getContent())))
                .build();
        CompletableFuture<ClientResponse> clientOneResponse = openAIService.getAnswer(completion);
        CompletableFuture<ClientResponse> clientTwoResponse = localChatService.getAnswer(completion);

        TextResponse answer = TextResponse.builder()
                .userId(textRequest.getUserId())
                .userName(textRequest.getUserName())
                .useMessageHistory(textRequest.isUseMessageHistory())
                .useLocalChat(textRequest.isUseLocalChat())
                .model("Error")
                .content("Ошибка получения ответа")
                .build();

        try {
            ClientResponse openAiResponse = clientOneResponse.get();
            ClientResponse localChatResponse = clientTwoResponse.get();

            log.info(openAiResponse.toString());
            log.info(localChatResponse.toString());

            ClientResponse selectedResponse = null;
            if (openAiResponse.getType().equals(ResponseType.SUCCESS)
                    && localChatResponse.getType().equals(ResponseType.SUCCESS)) {
                selectedResponse = openAiResponse;
            } else if (openAiResponse.getType().equals(ResponseType.SUCCESS)) {
                selectedResponse = openAiResponse;
            } else if (localChatResponse.getType().equals(ResponseType.SUCCESS)) {
                selectedResponse = localChatResponse;
            }

            if (selectedResponse != null) {
                answer.setModel(selectedResponse.getModel());
                answer.setContent(selectedResponse.getContent());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return answer;
    }

}
