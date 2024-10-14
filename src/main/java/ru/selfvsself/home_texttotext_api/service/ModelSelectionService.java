package ru.selfvsself.home_texttotext_api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.selfvsself.home_texttotext_api.model.client.ModelRequest;
import ru.selfvsself.home_texttotext_api.model.client.ModelResponse;
import ru.selfvsself.home_texttotext_api.model.client.ResponseType;
import ru.selfvsself.home_texttotext_api.service.llm.LocalChatService;
import ru.selfvsself.home_texttotext_api.service.llm.OpenAIChatService;

@Slf4j
@Service
public class ModelSelectionService {

    private final LocalChatService localChatService;
    private final OpenAIChatService openAIChatService;

    public ModelSelectionService(LocalChatService localChatService, OpenAIChatService openAIChatService) {
        this.localChatService = localChatService;
        this.openAIChatService = openAIChatService;
    }

    public ModelResponse getAnswer(ModelRequest modelRequest) {
        ModelResponse modelResponse = new ModelResponse();
        try {
            ModelResponse openAiResponse = openAIChatService.getAnswer(modelRequest);
            if (ResponseType.SUCCESS.equals(openAiResponse.getType())) {
                log.info(openAiResponse.toString());
                modelResponse = openAiResponse;
            }
            if (!ResponseType.SUCCESS.equals(modelResponse.getType())) {
                ModelResponse localChatResponse = localChatService.getAnswer(modelRequest);
                if (ResponseType.SUCCESS.equals(localChatResponse.getType())) {
                    log.info(openAiResponse.toString());
                    modelResponse = localChatResponse;
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        modelResponse.setRequestId(modelRequest.getRequestId());
        return modelResponse;
    }

    public ModelResponse getAnswerLocally(ModelRequest modelRequest) {
        ModelResponse modelResponse = new ModelResponse();
        try {
            ModelResponse localChatResponse = localChatService.getAnswer(modelRequest);
            if (ResponseType.SUCCESS.equals(localChatResponse.getType())) {
                modelResponse = localChatResponse;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        modelResponse.setRequestId(modelRequest.getRequestId());
        return modelResponse;
    }
}
