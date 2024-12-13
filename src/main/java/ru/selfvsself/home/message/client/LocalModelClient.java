package ru.selfvsself.home.message.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.selfvsself.home.message.client.api.TextModelClient;
import ru.selfvsself.home.message.model.Completion;
import ru.selfvsself.home.message.model.CompletionResponse;

@Service("localModelClient")
public class LocalModelClient implements TextModelClient {

    private final WebClient webClient;
    @Value("${chat.local.chat-url}")
    private String chatUrl;

    public LocalModelClient(WebClient localChatWebClient) {
        this.webClient = localChatWebClient;
    }

    public CompletionResponse chat(Completion requestPayload) {
        return webClient.post()
                .uri(chatUrl)
                .body(Mono.just(requestPayload), Completion.class)
                .retrieve()
                .bodyToMono(CompletionResponse.class)
                .block();
    }
}
