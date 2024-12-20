package ru.selfvsself.home.message.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
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
        this.messages = new ArrayList<>(completion.getMessages());
    }
}
