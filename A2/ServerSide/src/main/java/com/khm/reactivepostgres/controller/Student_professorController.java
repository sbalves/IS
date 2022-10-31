package com.khm.reactivepostgres.controller;

import com.khm.reactivepostgres.entity.Student;
import com.khm.reactivepostgres.entity.Student_professor;
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
@RequestMapping(value = "/api/student_professor")
@RequiredArgsConstructor
@Slf4j
public class Student_professorController {

    private final Student_professorRepository Student_professorRepository;

    @GetMapping
    public Flux<Student_professor> getAll() {
        return Student_professorRepository.findAll();
    }

    @GetMapping(value = "/{id}")
    public Mono<Student_professor> getOne(@PathVariable Long id) {
        return Student_professorRepository.findById(id);
    }

    @PostMapping
    public Mono<Student_professor> createProfessor(@RequestBody Student_professor student_professor) {
        return Student_professorRepository.save(student_professor);
    }


    @PutMapping
    public Mono<Student_professor> updateStudent_Professor(@RequestBody Student_professor student_professor) {
        return Student_professorRepository
                .findById(student_professor.getId())
                .flatMap(professorResult -> Student_professorRepository.save(student_professor));
    }

    @DeleteMapping
    public Mono<Void> deleteStudent_Professor(@RequestBody Student_professor student_professor) {
        return Student_professorRepository.deleteById(student_professor.getId());
    }
}
