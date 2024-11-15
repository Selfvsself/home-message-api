package ru.selfvsself.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class ChatRequest {
    private String content;
    private UUID requestId;
    private UUID userId;
    private boolean useMessageHistory = true;
    private boolean useLocalModel = true;
}
