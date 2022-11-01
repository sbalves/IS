package com.example.ClientSide;

import Entities.Student;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Collector;

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
	public static Long getTotalStudents(WebClient client) {
		return client.get()
				.uri("/student")
				.retrieve()
				.bodyToFlux(Student.class)
				.count().block();

		//
	}

	public static void printTotalStudents(WebClient client){
		System.out.println("######### Total number of students: " + getTotalStudents(client));
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
		Integer completedCourses = client.get()
				.uri("/student")
				.retrieve()
				.bodyToFlux(Student.class)
                .map(cr-> cr.getCompleted__credits()/6)
                .reduce(Integer::sum)
				.doOnNext(c-> System.out.println("######### Total number of courses completed for all students: " + c))
                .block();
	}

	// 5. Data of students that are in the last year of their graduation (i.e., whose credits
	//are at least 120 and less than 180)
	public static void getDataGraduates(WebClient client) {
		Student activeStudents = client.get()
				.uri("/student")
				.retrieve()
				.bodyToFlux(Student.class)
				.filter(cr->  cr.getCompleted__credits() >= 120 && cr.getCompleted__credits() < 180)
				.sort((s1,s2) -> s2.getCompleted__credits() - s1.getCompleted__credits())
				.doOnNext(cr -> System.out.println(cr))
				.blockLast();
	}


	// 6. Average and standard deviations of all student grades.
	public static Double getAvgGrades(WebClient client) {
		Long totalStudents = getTotalStudents(client);
		return Double.valueOf(client.get()
				.uri("/student")
				.retrieve()
				.bodyToFlux(Student.class)
				.map(Student::getAverage_grade)
				.reduce(Integer::sum)
				.map(cr -> cr/totalStudents)
				.block());
	}

	public static Double getStdGrades(WebClient client) {
		Long totalStudents = getTotalStudents(client);
		return Double.valueOf(client.get()
				.uri("/student")
				.retrieve()
				.bodyToFlux(Student.class)
				.map(Student::getAverage_grade)
				.map(cr -> stddev(cr))
				.map(cr -> cr/totalStudents)
				.block());
	}

	// 7. Average and standard deviations of students who have finished their graduation
	public static void getAvgStdGradesGraduate(WebClient client) {
	}

	// 8. The name of the eldest student.
	public static void getEldestStudent(WebClient client) {
	}

	// 9. Average number of professors per student.
	public static void getAvgProfessors(WebClient client) {
	}

	// 10. Name and number of students per professor, sorted in descending order.
	public static void getListStudentsPerProfessor(WebClient client) {
	}

	// 11. Complete data of all students, by adding the names of their professors.
	public static void getListAllStudents(WebClient client) {
	}

	public static void main(String[] args) {
		/*
		 * Flux<Student> students = getstudents();
		 * students.subscribe(System.out::println);
		 * students.doOnNext(c->System.out.println(c.getBirth_date()));
		 */
		WebClient client = WebClient.create("http://localhost:8081/api");


		getStudentsBirthDate(client);
		printTotalStudents(client);
		getActiveStudents(client);
        getTotalCompletedCourses(client);

		//to do (já estão os nomes definidos porque n confio na tua organização nem que vás seguir as conveções do java -_-)
		getDataGraduates(client);
		getAvgStdGrades(client);
		getAvgStdGradesGraduate(client);
		getEldestStudent(client);
		getAvgProfessors(client);
		getListStudentsPerProfessor(client);
		getListAllStudents(client);
	}
}
