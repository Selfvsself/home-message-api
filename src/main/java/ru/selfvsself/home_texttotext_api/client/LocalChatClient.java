package ru.selfvsself.home_texttotext_api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.selfvsself.home_texttotext_api.config.FeignConfig;
import ru.selfvsself.home_texttotext_api.model.Completion;
import ru.selfvsself.home_texttotext_api.model.CompletionResponse;

@FeignClient(name = "local", url = "${chat.local.baseUrl}", configuration = FeignConfig.class)
public interface LocalChatClient {

    @RequestMapping(method = RequestMethod.POST, value = "${chat.local.chatUrl}", produces = "application/json")
    CompletionResponse chat(Completion request);
}
