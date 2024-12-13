package ru.selfvsself.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class ChatRequest {
    private Content content;
    private UUID requestId;
    private Participant participant;
    private boolean useMessageHistory = true;
    private boolean useLocalModel = true;
}
