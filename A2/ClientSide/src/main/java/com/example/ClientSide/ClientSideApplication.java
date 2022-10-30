package com.example.ClientSide;

import Entities.Student;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

public class ClientSideApplication {

	public static Flux<Student> getstudents() {
		WebClient client = WebClient.create();
		return client.get()
				.uri("/student")
				.retrieve()
				.bodyToFlux(Student.class);
	}

	/*
	public static void getstudents(){
		WebClient client = WebClient.create();
		WebClient.ResponseSpec responseSpec = client.get()
				.uri("http://localhost:8080/api/student")
				.retrieve()
				.bodyToMono(Student.class);;


		String responseBody = responseSpec.bodyToMono(String.class).block();
		System.out.println(responseBody);

	}
	*/
	public static void main(String[] args) {
		Flux<Student> students = getstudents();
		students.subscribe(System.out::println);


	}
}
