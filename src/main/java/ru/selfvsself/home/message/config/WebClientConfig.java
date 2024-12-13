package ru.selfvsself.home.message.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Value("${chat.openai.base-url}")
    private String openAiUrl;

    @Value("${chat.openai.token}")
    private String openAiToken;

    @Value("${chat.local.base-url}")
    private String localChatUrl;

    @Bean
    public WebClient openAiWebClient() {
        return WebClient.builder()
                .baseUrl(openAiUrl)
                .filter(addAuthorizationHeader(openAiToken))
                .build();
    }

    @Bean
    public WebClient localChatWebClient() {
        return WebClient.builder()
                .baseUrl(localChatUrl)
                .build();
    }

    private ExchangeFilterFunction addAuthorizationHeader(String token) {
        return (request, next) -> next.exchange(
                ClientRequest.from(request)
                        .header("Authorization", "Bearer " + token)
                        .build());
    }
}
