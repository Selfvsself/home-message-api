package ru.selfvsself.home.message.client.api;

import ru.selfvsself.home.message.model.Completion;
import ru.selfvsself.home.message.model.CompletionResponse;

public interface TextModelClient {
    CompletionResponse chat(Completion requestPayload);
}
