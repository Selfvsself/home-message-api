package ru.selfvsself.home_texttotext_api.model.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ClientResponse {
    private String model;
    private String content;
    private ResponseType type;
    private Integer requestTokens;
    private Integer responseTokens;

    public ClientResponse() {
        this.type = ResponseType.ERROR;
        this.model = "Error";
        this.content = "Error";
        this.requestTokens = 0;
        this.responseTokens = 0;
    }
}
