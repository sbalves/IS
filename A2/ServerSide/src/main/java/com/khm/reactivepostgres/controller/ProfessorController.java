package com.khm.reactivepostgres.controller;

import com.khm.reactivepostgres.entity.Professor;
import com.khm.reactivepostgres.repository.ProfessorRepository;
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
@RequestMapping(value = "/api/professor")
@RequiredArgsConstructor
@Slf4j
public class ProfessorController {

  private final ProfessorRepository professorRepository;

  @GetMapping
  public Flux<Professor> getAll() {
    return professorRepository.findAll();
  }

  @GetMapping(value = "/{id}")
  public Mono<Professor> getOne(@PathVariable Long id) {
    return professorRepository.findById(id);
  }

  @PostMapping
  public Mono<Professor> createProfessor(@RequestBody Professor professor) {
    return professorRepository.save(professor);
  }

  @PostMapping(value = "/{number}")
  public Flux<Professor> createProfessors(@PathVariable int number) {
    return generateRandomProfessor(number).subscribeOn(Schedulers.boundedElastic());
  }

  private Flux<Professor> generateRandomProfessor(int number) {
    return Mono.fromSupplier(
            () -> Professor.builder().name(RandomStringUtils.randomAlphabetic(5)).build())
        .flatMap(professorRepository::save)
        .repeat(number);
  }

  @PutMapping
  public Mono<Professor> updateProfessor(@RequestBody Professor professor) {
    return professorRepository
        .findById(professor.getId())
        .flatMap(professorResult -> professorRepository.save(professor));
  }

  @DeleteMapping
  public Mono<Void> deleteProfessor(@RequestBody Professor professor) {
    return professorRepository.deleteById(professor.getId());
  }

}
