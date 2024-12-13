package ru.selfvsself.home.message.service.llm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.selfvsself.home.message.client.api.TextModelClient;

@Slf4j
@Service
public class LocalChatService extends BaseModelService {

    private static final Double DEFAULT_TEMPERATURE = 0.4;

    public LocalChatService(@Qualifier("localModelClient") TextModelClient localModelClient) {
        super(localModelClient);
    }

    @Override
    public boolean isLocalModel() {
        return true;
    }

    @Override
    public int getWeight() {
        return 1;
    }

    @Override
    Double getDefaultTemperature() {
        return DEFAULT_TEMPERATURE;
    }
}
