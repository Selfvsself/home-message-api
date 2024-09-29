package ru.selfvsself.home_texttotext_api.model.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Completion {
    private String model;
    private Double temperature;
    private List<Message> messages;
}
