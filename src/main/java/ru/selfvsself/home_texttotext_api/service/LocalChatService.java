package ru.selfvsself.home_texttotext_api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.selfvsself.home_texttotext_api.client.LocalChatClient;
import ru.selfvsself.home_texttotext_api.model.client.*;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class LocalChatService {

    private final LocalChatClient localChatClient;

    public LocalChatService(LocalChatClient localChatClient) {
        this.localChatClient = localChatClient;
    }

    @Async
    public CompletableFuture<ClientResponse> getAnswer(Completion completion) {
        return CompletableFuture.completedFuture(getAnswerAsync(completion));
    }

    @Async
    private ClientResponse getAnswerAsync(Completion completion) {
        ClientResponse answer = new ClientResponse("local", "Ошибка получения ответа", ResponseType.ERROR);
        try {
            log.info(completion.toString());
            var response = localChatClient.chat(completion);
            log.info(response.toString());
            var choices = Optional.of(response)
                    .map(CompletionResponse::getChoices)
                    .orElseThrow(() -> new RuntimeException("Response don't have choices"));
            if (!choices.isEmpty()) {
                var content = Optional.ofNullable(choices.get(0))
                        .map(CompletionResponse.Choice::getCompletionMessage)
                        .map(CompletionMessage::getContent)
                        .orElseThrow(() -> new RuntimeException("Response don't have content"));
                answer = new ClientResponse(response.getModel(), content, ResponseType.SUCCESS);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return answer;
    }
}
