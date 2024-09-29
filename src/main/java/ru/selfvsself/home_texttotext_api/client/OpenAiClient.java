package ru.selfvsself.home_texttotext_api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.selfvsself.home_texttotext_api.config.OpenAiFeignConfig;
import ru.selfvsself.home_texttotext_api.model.client.Completion;
import ru.selfvsself.home_texttotext_api.model.client.CompletionResponse;

@FeignClient(name = "openai", url = "${chat.openai.base-url}", configuration = OpenAiFeignConfig.class)
public interface OpenAiClient {

    @RequestMapping(method = RequestMethod.POST, value = "${chat.openai.chat-url}", produces = "application/json")
    CompletionResponse chat(Completion request);
}
