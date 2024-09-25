package ru.selfvsself.home_texttotext_api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.selfvsself.home_texttotext_api.client.OpenAiClient;
import ru.selfvsself.home_texttotext_api.model.Completion;

@Slf4j
@Service
public class ChatService {

    private final OpenAiClient openAiClient;

    public ChatService(OpenAiClient openAiClient) {
        this.openAiClient = openAiClient;
    }

    public String getAnswer(Completion completion) {
        var response = openAiClient.chat(completion);
        return "Get answer";
    }

}
