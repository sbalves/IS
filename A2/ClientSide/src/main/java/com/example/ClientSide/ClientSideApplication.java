package com.example.ClientSide;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.function.client.WebClient;

public class ClientSideApplication {

	static WebClient getWebClient() {
		WebClient.Builder webClientBuilder = WebClient.builder();
		return webClientBuilder.build();
	}

	public static void main(String[] args) {

		WebClient client = getWebClient();

		

	}

}
