package ru.selfvsself.home.message.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
    @Builder.Default
    private boolean useLocalModel = true;
    private Integer historyTokens;
}
