package com.khm.reactivepostgres.controller;

import com.khm.reactivepostgres.entity.Student;
import com.khm.reactivepostgres.repository.StudentRepository;
import com.khm.reactivepostgres.repository.Student_professorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@RestController
@RequestMapping(value = "/api/student")
@RequiredArgsConstructor
@Slf4j
public class StudentController {

    public void logger(String message){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now) + " | " + message);
    }
    private final StudentRepository studentRepository;
    private final Student_professorRepository student_professorRepository;

    @GetMapping
    public Flux<Student> getAll() {
        logger("Client requested list of all students.");
        return studentRepository.findAll();
    }

    @GetMapping(value = "/{id}")
    public Mono<Student> getOne(@PathVariable Long id) {
        logger("Client requested student " + id + ".");
        return studentRepository.findById(id);
    }

    @PostMapping
    public Mono<Student> createStudent(@RequestBody Student student) {
        logger("Client requested to create student " + student.getId() + ".");
        return studentRepository.save(student);
    }

    @PostMapping(value = "/{number}")
    public Flux<Student> createStudents(@PathVariable int number) {
      return generateRandomStudent(number).subscribeOn(Schedulers.boundedElastic());
    }

    private Flux<Student> generateRandomStudent(int numberq) {
      return Mono.fromSupplier(
                      () -> Student.builder().name(RandomStringUtils.randomAlphabetic(5)).build())
              .flatMap(studentRepository::save)
              .repeat(number);
    }

    @PutMapping
    public Mono<Student> updateStudent(@RequestBody Student student) {
        logger("Client requested to update student " + student.getId() + ".");
        return studentRepository
              .findById(student.getId())
              .flatMap(studentResult -> studentRepository.save(student));
    }
      @DeleteMapping
      public Mono<Void> deleteStudent(@RequestBody Student student) {
        logger("Client requested to delete student " + student.getId() + " .");
        return studentRepository.deleteById(student.getId());

      }



}
