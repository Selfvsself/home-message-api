package ru.selfvsself.home_texttotext_api.service.llm.api;

import ru.selfvsself.home_texttotext_api.model.client.ModelResponse;
import ru.selfvsself.home_texttotext_api.model.client.Completion;

public interface BaseModelService {
    ModelResponse getAnswer(Completion completion);
    boolean isLocalModel();
    int getWeight();
}
