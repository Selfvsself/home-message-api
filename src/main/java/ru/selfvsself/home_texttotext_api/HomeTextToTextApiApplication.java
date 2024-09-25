package ru.selfvsself.home_texttotext_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class HomeTextToTextApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomeTextToTextApiApplication.class, args);
	}

}
