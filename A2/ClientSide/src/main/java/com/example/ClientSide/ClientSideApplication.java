package com.example.ClientSide;

import Entities.Student;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.stream.Collector;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static java.util.Collections.sort;

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
	public static Long getActiveStudents(WebClient client) {
		return client.get()
				.uri("/student")
				.retrieve()
				.bodyToFlux(Student.class)
				.filter(cr-> cr.getCompleted__credits() < 180)
				.count()
				.block();
	}


	public static void printActiveStutents(WebClient client){
		System.out.println("######### Total number of active students (credits < 180): " + getActiveStudents(client));
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
		client.get()
				.uri("/student")
				.retrieve()
				.bodyToFlux(Student.class)
				.filter(cr->  cr.getCompleted__credits() >= 120 && cr.getCompleted__credits() < 180)
				.sort((s1,s2) -> s2.getCompleted__credits() - s1.getCompleted__credits())
				.doOnNext(System.out::println)
				.blockLast();
	}


	// 6. Average and standard deviations of all student grades.
	public static void getAvgStdGrades(WebClient client) {
		Long totalStudents = getTotalStudents(client);

		Long mean = client.get()
				.uri("/student")
				.retrieve()
				.bodyToFlux(Student.class)
				.map(Student::getAverage_grade)
				.reduce(Integer::sum)
				.map(cr -> cr/totalStudents)
				.block();

		System.out.println("######### Average of grades of all students: " + mean);

		client.get()
				.uri("/student")
				.retrieve()
				.bodyToFlux(Student.class)
				.map(cr -> pow(cr.getAverage_grade() - mean, 2))
				.reduce(Double::sum)
				.map(cr -> sqrt(cr / totalStudents))
				.doOnNext(cr -> System.out.println("######### Standard deviation of grades of all students: " + cr))
				.block();
	}

	// 7. Average and standard deviations of students who have finished their graduation
	public static void getAvgStdGradesGraduate(WebClient client) {
		Long totalGraduatedStudents = getTotalStudents(client) - getActiveStudents(client);

		Long meanGraduates = client.get()
				.uri("/student")
				.retrieve()
				.bodyToFlux(Student.class)
				.filter(cr -> cr.getCompleted__credits() >= 180)
				.map(Student::getAverage_grade)
				.reduce(Integer::sum)
				.map(cr -> cr / totalGraduatedStudents)
				.block();


		System.out.println("######### Average of grades of all students who have finished their graduation: " + meanGraduates);

		client.get()
				.uri("/student")
				.retrieve()
				.bodyToFlux(Student.class)
				.filter(cr -> cr.getCompleted__credits() >= 180)
				.map(cr -> pow(cr.getAverage_grade() - meanGraduates, 2))
				.reduce(Double::sum)
				.map(cr -> sqrt(cr / totalGraduatedStudents))
				.doOnNext(cr -> System.out.println("######### Standard deviation of grades of students who have finished their graduation: " + cr))
				.block();

	}

	// 8. The name of the eldest student.
	/*
	public static void getEldestStudent(WebClient client) {
		SimpleDateFormat formatter1=new SimpleDateFormat("dd-MM-yyyy");

		client.get()
				.uri("/student")
				.retrieve()
				.bodyToFlux(Student.class)
				.map(cr -> {
					try {
						return formatter1.parse(cr.getBirth_date());
					} catch (ParseException e) {
						throw new RuntimeException(e);
					}
				})
				.sort()
				.take(1)
				.doOnNext(cr -> System.out.println("######### Eldest student: " + cr.ge));


	}
	*/


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

		WebClient client = WebClient.create("http://localhost:8081/api");

		getStudentsBirthDate(client);
		printTotalStudents(client);
		getActiveStudents(client);
        getTotalCompletedCourses(client);

		//to do (já estão os nomes definidos porque n confio na tua organização nem que vás seguir as conveções do java -_-)
		getDataGraduates(client);
		getAvgStdGrades(client);
		getAvgStdGradesGraduate(client);
		//getEldestStudent(client);
		getAvgProfessors(client);
		getListStudentsPerProfessor(client);
		getListAllStudents(client);
	}


}
