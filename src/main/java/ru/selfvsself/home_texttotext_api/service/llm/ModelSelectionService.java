package ru.selfvsself.home_texttotext_api.service.llm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.selfvsself.home_texttotext_api.model.client.ModelRequest;
import ru.selfvsself.home_texttotext_api.model.client.ModelResponse;
import ru.selfvsself.home_texttotext_api.model.client.ResponseType;
import ru.selfvsself.home_texttotext_api.service.llm.api.BaseModelService;

import java.util.List;

@Slf4j
@Service
public class ModelSelectionService {

    private final List<BaseModelService> modelServiceList;

    public ModelSelectionService(List<BaseModelService> modelServiceList) {
        this.modelServiceList = modelServiceList.stream()
                .sorted((a, b) -> b.getWeight() - a.getWeight())
                .toList();
    }

    public ModelResponse getAnswer(ModelRequest modelRequest) {
        ModelResponse modelResponse = new ModelResponse();
        for (BaseModelService modelService : modelServiceList) {
            if (modelRequest.isUseLocalModel() && modelService.isLocalModel()) {
                try {
                    ModelResponse response = modelService.getAnswer(modelRequest);
                    log.info(response.toString());
                    if (ResponseType.SUCCESS.equals(response.getType())) {
                        modelResponse = response;
                        break;
                    }
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        }
        modelResponse.setRequestId(modelRequest.getRequestId());
        return modelResponse;
    }
}
