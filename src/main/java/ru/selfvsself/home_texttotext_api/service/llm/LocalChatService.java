package ru.selfvsself.home_texttotext_api.service.llm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.selfvsself.home_texttotext_api.client.LocalModelClient;
import ru.selfvsself.home_texttotext_api.model.*;
import ru.selfvsself.home_texttotext_api.service.llm.api.BaseModelService;

import java.util.Optional;

@Slf4j
@Service
public class LocalChatService implements BaseModelService {

    private static final Double DEFAULT_TEMPERATURE = 0.4;
    private final LocalModelClient localModelClient;

    public LocalChatService(LocalModelClient localModelClient) {
        this.localModelClient = localModelClient;
    }

    public ModelResponse getAnswer(Completion completion) {
        ModelResponse answer = new ModelResponse();
        try {
            Completion localCompletion = prepareCompletion(completion);
            var response = localModelClient.chat(localCompletion);
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

    @Override
    public boolean isLocalModel() {
        return true;
    }

    @Override
    public int getWeight() {
        return 1;
    }

    private Completion prepareCompletion(Completion completion) {
        Completion localCompletion = new Completion(completion);
        log.info(localCompletion.toString());
        if (localCompletion.getTemperature() == null) {
            localCompletion.setTemperature(DEFAULT_TEMPERATURE);
            log.info("Temperature is null. Set default temperature: {}", DEFAULT_TEMPERATURE);
        }
        return localCompletion;
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
