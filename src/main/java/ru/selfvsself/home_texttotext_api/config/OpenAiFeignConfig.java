package ru.selfvsself.home_texttotext_api.config;

import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

@Slf4j
public class OpenAiFeignConfig extends FeignConfig {

    private static final String BEARER = "Bearer ";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    @Value("${chat.openai.token}")
    private String token;

    @Bean
    public RequestInterceptor getRequestInterceptor() {
        return requestTemplate -> requestTemplate
                .header(AUTHORIZATION_HEADER, BEARER + token);
    }
}
