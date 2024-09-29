package ru.selfvsself.home_texttotext_api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.selfvsself.home_texttotext_api.config.FeignConfig;
import ru.selfvsself.home_texttotext_api.model.client.Completion;
import ru.selfvsself.home_texttotext_api.model.client.CompletionResponse;

@FeignClient(name = "local", url = "${chat.local.base-url}", configuration = FeignConfig.class)
public interface LocalChatClient {

    @RequestMapping(method = RequestMethod.POST, value = "${chat.local.chat-url}", produces = "application/json")
    CompletionResponse chat(Completion request);
}
