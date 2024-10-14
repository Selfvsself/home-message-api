package ru.selfvsself.home_texttotext_api.model.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Completion {
    private String model;
    private Double temperature;
    private List<CompletionMessage> messages;

    public Completion(Completion completion) {
        this.model = completion.getModel();
        this.temperature = completion.getTemperature();
        this.messages = completion.getMessages();
    }
}
