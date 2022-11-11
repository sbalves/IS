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

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


@RestController
@RequestMapping(value = "/api/professor")
@RequiredArgsConstructor
@Slf4j
public class ProfessorController {

  public void logger(String message){
    Logger logger = Logger.getLogger("MyLog");
    logger.setLevel(Level.FINE);
    FileHandler fh;
    try {
      fh = new FileHandler("MyLogFile.log", true);
      logger.addHandler(fh);
      SimpleFormatter formatter = new SimpleFormatter();
      fh.setFormatter(formatter);
      logger.info(message);
      fh.close();
    } catch (SecurityException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  private final ProfessorRepository professorRepository;
  
  @GetMapping
  public Flux<Professor> getAll() {
    logger("Client requested list of all professors.");
    return professorRepository.findAll();
  }

  @GetMapping(value = "/{id}")
  public Mono<Professor> getOne(@PathVariable Long id) {
    logger("Client requested professor " + id + ".");
    return professorRepository.findById(id);
  }

  @PostMapping
  public Mono<Professor> createProfessor(@RequestBody Professor professor) {
    logger("Client requested to create professor.");
    return professorRepository.save(professor);
  }

  @PutMapping
  public Mono<Professor> updateProfessor(@RequestBody Professor professor) {
    logger("Client requested to update professor " + professor.getId() + ".");
    return professorRepository
        .findById(professor.getId())
        .flatMap(professorResult -> professorRepository.save(professor));
  }

  @DeleteMapping
  public Mono<Void> deleteProfessor(@RequestBody Professor professor) {
    logger("Client requested to delete professor " + professor.getId() + " .");
    return professorRepository.deleteById(professor.getId());
  }

}
