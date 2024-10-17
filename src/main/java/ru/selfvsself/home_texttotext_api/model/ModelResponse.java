package ru.selfvsself.home_texttotext_api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class ModelResponse {
    private String model;
    private String content;
    private ResponseType type;
    private Integer requestTokens;
    private Integer responseTokens;
    private UUID requestId;

    public ModelResponse() {
        this.type = ResponseType.ERROR;
        this.model = "Error";
        this.content = "Error";
        this.requestTokens = 0;
        this.responseTokens = 0;
    }
}
