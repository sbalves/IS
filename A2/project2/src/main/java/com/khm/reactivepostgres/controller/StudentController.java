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



@RestController
@RequestMapping(value = "/api/student")
@RequiredArgsConstructor
@Slf4j
public class StudentController {

    private final StudentRepository studentRepository;
    private final Student_professorRepository student_professorRepository;

    @GetMapping
    public Flux<Student> getAll() {
      return studentRepository.findAll();
    }

    @GetMapping(value = "/{id}")
    public Mono<Student> getOne(@PathVariable Long id) {
        System.out.println("MACACO");
      return studentRepository.findById(id);
    }

    @PostMapping
    public Mono<Student> createStudent(@RequestBody Student student) {
      return studentRepository.save(student);
    }

    @PostMapping(value = "/{number}")
    public Flux<Student> createStudents(@PathVariable int number) {
      return generateRandomStudent(number).subscribeOn(Schedulers.boundedElastic());
    }

    private Flux<Student> generateRandomStudent(int number) {
      return Mono.fromSupplier(
                      () -> Student.builder().name(RandomStringUtils.randomAlphabetic(5)).build())
              .flatMap(studentRepository::save)
              .repeat(number);
    }

    @PutMapping
    public Mono<Student> updateStudent(@RequestBody Student student) {
      return studentRepository
              .findById(student.getId())
              .flatMap(studentResult -> studentRepository.save(student));
    }
      @DeleteMapping
      public Mono<Void> deleteStudent(@RequestBody Student student) {

        return studentRepository.deleteById(student.getId());

      }



}
