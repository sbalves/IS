package com.example.ClientSide;

import Entities.Student;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ClientSideApplication {
	/*
	public static Flux<Student> getstudents() {
		WebClient client = WebClient.create();
		return client.get()
				.uri("/student")
				.retrieve()
				.bodyToFlux(Student.class);
	}
	*/

	//2. Number of students
	public static void getNumberStudents(Flux<Student> fluxStudents){
		System.out.println(fluxStudents.count().block());
	}

	public static void getStudentsBirthDate(Flux<Student> fluxStudents){

		fluxStudents.subscribe(System.out::println);
		//studentsFlux.subscribe(c-> System.out.println(c.getBirth_date()));
		//System.out.println(responseSpec);

	}

	public static void main(String[] args) {
		/*
		Flux<Student> students = getstudents();
		students.subscribe(System.out::println);
		students.doOnNext(c->System.out.println(c.getBirth_date()));
		*/
		WebClient client = WebClient.create();
		Flux<Student> fluxStudents = client.get()
				.uri("http://localhost:8080/api/student")
				.retrieve()
				.bodyToFlux(Student.class);


		getNumberStudents(fluxStudents);
		getStudentsBirthDate(fluxStudents);
	}
}
