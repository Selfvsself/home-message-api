package ru.selfvsself.home_texttotext_api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.selfvsself.home_texttotext_api.client.LocalChatClient;
import ru.selfvsself.home_texttotext_api.client.OpenAiClient;
import ru.selfvsself.home_texttotext_api.model.Completion;

@Slf4j
@Service
public class ChatService {

    private final OpenAiClient openAiClient;
    private final LocalChatClient localChatClient;

    public ChatService(OpenAiClient openAiClient, LocalChatClient localChatClient) {
        this.openAiClient = openAiClient;
        this.localChatClient = localChatClient;
    }

    public String getAnswer(Completion completion) {
        var response1 = openAiClient.chat(completion);
        log.info(response1.toString());
        var response2 = localChatClient.chat(completion);
        log.info(response2.toString());
        return "Get answer";
    }

}
