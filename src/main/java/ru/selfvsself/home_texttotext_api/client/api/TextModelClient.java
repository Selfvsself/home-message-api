package ru.selfvsself.home_texttotext_api.client.api;

import ru.selfvsself.home_texttotext_api.model.Completion;
import ru.selfvsself.home_texttotext_api.model.CompletionResponse;

public interface TextModelClient {
    CompletionResponse chat(Completion requestPayload);
}
