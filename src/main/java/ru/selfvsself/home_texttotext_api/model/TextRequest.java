package ru.selfvsself.home_texttotext_api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TextRequest {
    private String userId;
    private String userName;
    private String content;
    private boolean useMessageHistory = true;
    private boolean useLocalChat = true;
}
