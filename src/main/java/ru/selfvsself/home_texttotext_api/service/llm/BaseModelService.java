package ru.selfvsself.home_texttotext_api.service.llm;

import lombok.extern.slf4j.Slf4j;
import ru.selfvsself.home_texttotext_api.client.api.TextModelClient;
import ru.selfvsself.home_texttotext_api.model.*;
import ru.selfvsself.model.ResponseType;

import java.util.Optional;

@Slf4j
public abstract class BaseModelService {

    TextModelClient modelClient;

    public BaseModelService(TextModelClient modelClient) {
        this.modelClient = modelClient;
    }

    ModelResponse getAnswer(Completion completion) {
        ModelResponse answer = new ModelResponse();
        Completion localCompletion = prepareCompletion(completion);
        try {
            var response = modelClient.chat(localCompletion);
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
            if (e.getMessage().toLowerCase().contains("context_length_exceeded")
                    && localCompletion.getMessages().size() > 1) {
                localCompletion.getMessages().remove(0);
                getAnswer(localCompletion);
            }
        }
        return answer;
    }

    abstract boolean isLocalModel();

    abstract int getWeight();

    abstract Double getDefaultTemperature();

    protected Completion prepareCompletion(Completion completion) {
        Completion localCompletion = new Completion(completion);
        log.info(localCompletion.toString());
        if (localCompletion.getTemperature() == null) {
            localCompletion.setTemperature(getDefaultTemperature());
            log.info("Temperature is null. Set default temperature: {}", getDefaultTemperature());
        }
        return localCompletion;
    }

    protected String getContentFromResponse(CompletionResponse response) {
        var choices = Optional.of(response)
                .map(CompletionResponse::getChoices)
                .orElseThrow(() -> new RuntimeException("Response don't have choices"));
        return Optional.ofNullable(choices.get(0))
                .map(CompletionResponse.Choice::getMessage)
                .map(CompletionMessage::getContent)
                .orElseThrow(() -> new RuntimeException("Response don't have content"));
    }
}
