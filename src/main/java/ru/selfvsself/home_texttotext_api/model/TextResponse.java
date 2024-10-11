package ru.selfvsself.home_texttotext_api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TextResponse {
    private Long chatId;
    private String userName;
    private String model;
    private String content;
}
