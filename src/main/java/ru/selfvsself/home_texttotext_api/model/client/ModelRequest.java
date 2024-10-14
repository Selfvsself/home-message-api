package ru.selfvsself.home_texttotext_api.model.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
public class ModelRequest extends Completion {
    private UUID requestId;
    private boolean useLocalModel = true;
}
