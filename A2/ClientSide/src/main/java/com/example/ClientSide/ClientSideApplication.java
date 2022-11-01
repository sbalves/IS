package com.example.ClientSide;

import Entities.Student;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ClientSideApplication {

	// 1. Names and birthdates of all students.
	public static void getStudentsBirthDate(WebClient client) {
		Student fluxStudents = client.get()
				.uri("/student")
				.retrieve()
				.bodyToFlux(Student.class)
				.doOnNext(cr-> System.out.println("######### Name: " + cr.getName() + "\t\tBirth Date: " + cr.getBirth_date()))
				.blockLast();
	}

	// 2. Number of students
	public static void getTotalStudents(WebClient client) {
		Flux<Student> fluxStudents = client.get()
				.uri("/student")
				.retrieve()
				.bodyToFlux(Student.class);

		System.out.println("######### Total number of students: " + fluxStudents.count().block());
	}

	// 3. Total number of students that are active (i.e., that have less than 180 credits).
	public static void getActiveStudents(WebClient client) {
		Long activeStudents = client.get()
				.uri("/student")
				.retrieve()
				.bodyToFlux(Student.class)
				.filter(cr-> cr.getCompleted__credits() < 180)
				.count()
				.block();

		System.out.println("######### Total number of active students (credits < 180): " + activeStudents);
	}

	// 4. Total number of courses completed for all students
	public static void getTotalCompletedCourses(WebClient client) {
		int completedCourses = 0;
        completedCourses = client.get()
				.uri("/student")
				.retrieve()
				.bodyToFlux(Student.class)
                .map(cr-> cr.getCompleted__credits()/6)
                .reduce(Integer::sum)
                .subscribe();

		System.out.println("######### Total number of courses completed for all students: " + completedCourses);
	}




	public static void main(String[] args) {
		/*
		 * Flux<Student> students = getstudents();
		 * students.subscribe(System.out::println);
		 * students.doOnNext(c->System.out.println(c.getBirth_date()));
		 */
		WebClient client = WebClient.create("http://localhost:8080/api");


		getStudentsBirthDate(client);
		getTotalStudents(client);
		getActiveStudents(client);
        getTotalCompletedCourses(client);
	}
}
