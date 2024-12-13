package ru.selfvsself.home.message.service.llm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.selfvsself.home.message.client.api.TextModelClient;
import ru.selfvsself.home.message.model.Completion;

@Slf4j
@Service
public class OpenAIChatServiceGpt3_5 extends BaseModelService {

    private static final String DEFAULT_MODEL = "gpt-3.5-turbo-1106";
    private static final Double DEFAULT_TEMPERATURE = 0.4;

    public OpenAIChatServiceGpt3_5(@Qualifier("externalModelClient") TextModelClient modelClient) {
        super(modelClient);
    }

    @Override
    public boolean isLocalModel() {
        return false;
    }

    @Override
    public int getWeight() {
        return 5;
    }

    @Override
    Double getDefaultTemperature() {
        return DEFAULT_TEMPERATURE;
    }

    @Override
    protected Completion prepareCompletion(Completion completion) {
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
}
