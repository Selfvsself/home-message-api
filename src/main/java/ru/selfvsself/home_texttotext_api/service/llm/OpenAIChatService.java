package ru.selfvsself.home_texttotext_api.service.llm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.selfvsself.home_texttotext_api.client.ExternalModelClient;
import ru.selfvsself.home_texttotext_api.model.client.*;

import java.util.Optional;

@Slf4j
@Service
public class OpenAIChatService {

    private static final String DEFAULT_MODEL = "gpt-4o-mini";
    private static final Double DEFAULT_TEMPERATURE = 0.4;
    private final ExternalModelClient externalModelClient;

    public OpenAIChatService(ExternalModelClient externalModelClient) {
        this.externalModelClient = externalModelClient;
    }

    public ModelResponse getAnswer(Completion completion) {
        ModelResponse answer = new ModelResponse();
        try {
            Completion openAiCompletion = prepareCompletion(completion);
            var response = externalModelClient.chat(openAiCompletion);
            log.info(response.toString());
            String content = getContentFromResponse(response);
            answer = ModelResponse.builder()
                    .model(response.getModel())
                    .content(content)
                    .type(ResponseType.SUCCESS)
                    .requestTokens(response.getUsage().getPrompt_tokens())
                    .responseTokens(response.getUsage().getCompletion_tokens())
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return answer;
    }

    private Completion prepareCompletion(Completion completion) {
        Completion openAiCompletion = new Completion(completion);
        log.info(openAiCompletion.toString());
        if (openAiCompletion.getModel() == null) {
            openAiCompletion.setModel(DEFAULT_MODEL);
            log.info("Model is null. Set default model: {}", DEFAULT_MODEL);
        }
        if (openAiCompletion.getTemperature() == null) {
            openAiCompletion.setTemperature(DEFAULT_TEMPERATURE);
            log.info("Temperature is null. Set default temperature: {}", DEFAULT_TEMPERATURE);
        }
        return openAiCompletion;
    }

    private String getContentFromResponse(CompletionResponse response) {
        var choices = Optional.of(response)
                .map(CompletionResponse::getChoices)
                .orElseThrow(() -> new RuntimeException("Response don't have choices"));
        return Optional.ofNullable(choices.get(0))
                .map(CompletionResponse.Choice::getMessage)
                .map(CompletionMessage::getContent)
                .orElseThrow(() -> new RuntimeException("Response don't have content"));
    }
}
