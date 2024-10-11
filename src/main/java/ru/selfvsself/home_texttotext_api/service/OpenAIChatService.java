package ru.selfvsself.home_texttotext_api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.selfvsself.home_texttotext_api.client.OpenAiClient;
import ru.selfvsself.home_texttotext_api.model.client.*;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class OpenAIChatService {

    private static final String DEFAULT_MODEL = "gpt-4o-mini";
    private final OpenAiClient openAiClient;

    public OpenAIChatService(OpenAiClient openAiClient) {
        this.openAiClient = openAiClient;
    }

    @Async
    public CompletableFuture<ClientResponse> getAnswer(Completion completion) {
        Completion request = Completion
                .builder()
                .model(StringUtils.hasLength(completion.getModel()) ? completion.getModel() : DEFAULT_MODEL)
                .temperature(completion.getTemperature() == null ? 0.4 : completion.getTemperature())
                .completionMessages(completion.getCompletionMessages())
                .build();
        return CompletableFuture.completedFuture(getAnswerAsync(request));
    }

    @Async
    private ClientResponse getAnswerAsync(Completion completion) {
        ClientResponse answer = new ClientResponse(DEFAULT_MODEL, "Ошибка получения ответа", ResponseType.ERROR);
        try {
            log.info(completion.toString());
            var response = openAiClient.chat(completion);
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
