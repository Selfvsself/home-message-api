package ru.selfvsself.home_texttotext_api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.selfvsself.home_texttotext_api.model.client.ClientResponse;
import ru.selfvsself.home_texttotext_api.model.client.Completion;
import ru.selfvsself.home_texttotext_api.model.client.ResponseType;
import ru.selfvsself.home_texttotext_api.service.llm.LocalChatService;
import ru.selfvsself.home_texttotext_api.service.llm.OpenAIChatService;

@Slf4j
@Service
public class ChatService {

    private final LocalChatService localChatService;
    private final OpenAIChatService openAIChatService;

    public ChatService(LocalChatService localChatService, OpenAIChatService openAIChatService) {
        this.localChatService = localChatService;
        this.openAIChatService = openAIChatService;
    }

    public ClientResponse getAnswer(Completion completion) {
        ClientResponse answer = new ClientResponse();
        try {
            ClientResponse openAiResponse = openAIChatService.getAnswer(completion);
            log.info(openAiResponse.toString());
            if (ResponseType.SUCCESS.equals(openAiResponse.getType())) {
                return openAiResponse;
            }
            ClientResponse localChatResponse = localChatService.getAnswer(completion);
            log.info(localChatResponse.toString());
            if (ResponseType.SUCCESS.equals(localChatResponse.getType())) {
                return localChatResponse;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return answer;
    }

    public ClientResponse getAnswerLocally(Completion completion) {
        ClientResponse answer = new ClientResponse();
        try {
            ClientResponse localChatResponse = localChatService.getAnswer(completion);
            log.info(localChatResponse.toString());
            if (ResponseType.SUCCESS.equals(localChatResponse.getType())) {
                return localChatResponse;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return answer;
    }
}
