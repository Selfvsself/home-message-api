package ru.selfvsself.home_texttotext_api.service.llm.api;

import ru.selfvsself.home_texttotext_api.model.ModelResponse;
import ru.selfvsself.home_texttotext_api.model.Completion;

public interface BaseModelService {
    ModelResponse getAnswer(Completion completion);
    boolean isLocalModel();
    int getWeight();
}
