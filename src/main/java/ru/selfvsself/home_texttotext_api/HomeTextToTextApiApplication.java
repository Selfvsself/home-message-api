package ru.selfvsself.home_texttotext_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class HomeTextToTextApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomeTextToTextApiApplication.class, args);
    }

}
