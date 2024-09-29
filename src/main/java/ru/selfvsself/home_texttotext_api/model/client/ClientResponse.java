package ru.selfvsself.home_texttotext_api.model.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponse {
    private String model;
    private String content;
    private ResponseType type;
}
