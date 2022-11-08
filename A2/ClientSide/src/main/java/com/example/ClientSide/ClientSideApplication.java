package com.example.ClientSide;

import Entities.Professor;
import Entities.Student;
import Entities.Student_professor;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collector;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static java.util.Collections.copy;
import static java.util.Collections.sort;


public class ClientSideApplication{

	// 1. Names and birthdates of all students.
	public static void getStudentsBirthDate(WebClient client) {
		System.out.println("#### 1 ####\n\tData of all students: ");
		client.get()
				.uri("/student")
				.retrieve()
				.bodyToFlux(Student.class)
				.doOnNext(cr-> System.out.println("\t\tName: " + cr.getName() + "\t\tBirth Date: " + cr.getBirth_date()))
				.blockLast();
		System.out.println();
	}

	// 2. Number of students
	public static Long getTotalStudents(WebClient client) {
		return client.get()
				.uri("/student")
				.retrieve()
				.bodyToFlux(Student.class)
				.count()
				.block();
	}

	public static void printTotalStudents(WebClient client){
		System.out.println("#### 2 ####\n\tTotal number of students: " + getTotalStudents(client) + "\n");
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
		System.out.println("#### 3 ####\n\tTotal number of active students (credits < 180): " + getActiveStudents(client) + "\n");
	}

	// 4. Total number of courses completed for all students
	public static void getTotalCompletedCourses(WebClient client) {
		System.out.println("#### 4 ####\n\tTotal number of courses completed for all students:");
		client.get()
				.uri("/student")
				.retrieve()
				.bodyToFlux(Student.class)
				.doOnNext(student -> System.out.println("\t\t" + student.getName() + ": " + student.getCompleted__credits()/6))
                .blockLast();
		System.out.println();
	}

	// 5. Data of students that are in the last year of their graduation (i.e., whose credits
	//are at least 120 and less than 180)
	public static void getDataGraduates(WebClient client) {
		System.out.println("#### 5 ####\n\tData of students that are in the last year of their graduation: ");
		client.get()
				.uri("/student")
				.retrieve()
				.bodyToFlux(Student.class)
				.filter(cr->  cr.getCompleted__credits() >= 120 && cr.getCompleted__credits() < 180)
				.sort((s1,s2) -> s2.getCompleted__credits() - s1.getCompleted__credits())
				.doOnNext(cr -> System.out.println("\t" + cr))
				.blockLast();
		System.out.println();
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

		System.out.println("#### 6 ####\n\tAverage of grades of all students: " + mean);

		client.get()
				.uri("/student")
				.retrieve()
				.bodyToFlux(Student.class)
				.map(cr -> pow(cr.getAverage_grade() - mean, 2))
				.reduce(Double::sum)
				.map(cr -> sqrt(cr / totalStudents))
				.doOnNext(cr -> System.out.println("\tStandard deviation of grades of all students: " + cr + "\n"))
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


		System.out.println("#### 7 #### \n\tAverage of grades of all students who have finished their graduation: " + meanGraduates);

		client.get()
				.uri("/student")
				.retrieve()
				.bodyToFlux(Student.class)
				.filter(cr -> cr.getCompleted__credits() >= 180)
				.map(cr -> pow(cr.getAverage_grade() - meanGraduates, 2))
				.reduce(Double::sum)
				.map(cr -> sqrt(cr / totalGraduatedStudents))
				.doOnNext(cr -> System.out.println("\tStandard deviation of grades of students who have finished their graduation: " + cr + "\n"))
				.block();

	}

	// 8. The name of the eldest student.
	public static Student getEldest(Student a, Student b){
		SimpleDateFormat formatter1=new SimpleDateFormat("yyyy–MM–dd");
		try {
			Date date1 = formatter1.parse(a.getBirth_date());
			Date date2 = formatter1.parse(b.getBirth_date());

			if(date1.before(date2)){
				return a;
			}
			else {
				return b;
			}
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	public static void getEldestStudent(WebClient client) {
		client.get()
				.uri("/student")
				.retrieve()
				.bodyToFlux(Student.class)
				.reduce(ClientSideApplication::getEldest)
				.doOnNext(cr -> System.out.println("#### 8 ####\n\tEldest student: " + cr.getName() + "\n")).block();
	}


	// 9. Average number of professors per student.
	public static void getAvgProfessors(WebClient client) {
		Long totalStudents = getTotalStudents(client);
		client.get().uri("/student")
				.retrieve()
				.bodyToFlux(Student.class)
				.publishOn(Schedulers.boundedElastic())
				.map(cr -> client.get().uri("/student_professor")
						.retrieve()
						.bodyToFlux(Student_professor.class)
						.filter(a -> Objects.equals(a.getStudent_id(),cr.getId()))
						.count()
						.block())
				.reduce(Long::sum)
				.map(cr -> Double.valueOf(cr) / totalStudents)
				.doOnNext(average -> System.out.println("#### 9 ####\n\tAverage number of professors per student: " + average))
				.block();
		System.out.println();
	}

	// 10. Name and number of students per professor, sorted in descending order.
	public static void getListStudentsPerProfessor(WebClient client) {
	}

	// 11. Complete data of all students, by adding the names of their professors.
	public static void getListAllStudents(WebClient client) {
		System.out.println("#### 11 ####\n\tData of all students + their professors:");

		client.get()
				.uri("/student")
				.retrieve()
				.bodyToFlux(Student.class)
				.publishOn(Schedulers.boundedElastic())
				.doOnNext(student -> {
					System.out.println("\t\t" + student.toString());
					System.out.println("\t\t" + student.getName() + "'s professors: ");


					client.get()
							.uri("/student_professor")
							.retrieve()
							.bodyToFlux(Student_professor.class)
							.publishOn(Schedulers.boundedElastic())
							.filter(student_professor -> student_professor.getStudent_id() == student.getId())
							.doOnNext(relationship -> {
								client.get()
										.uri("/professor/" + relationship.getProfessor_id())
										.retrieve()
										.bodyToFlux(Professor.class)
										.doOnNext(prof -> System.out.println("\t\t\t" + prof.getName()))
										.blockLast();
							}).blockLast();
					System.out.println("\t\t-----\n");
				})
				.blockLast();

	}

	public static void main(String[] args) {

		WebClient client = WebClient.create("http://localhost:8081/api");

		getStudentsBirthDate(client);
		printTotalStudents(client);
		printActiveStutents(client);
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
