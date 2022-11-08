package com.khm.reactivepostgres.repository;

import com.khm.reactivepostgres.entity.Professor;
import com.khm.reactivepostgres.entity.Student;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface ProfessorRepository extends R2dbcRepository<Professor, Long> {
  Mono<Professor> findById(Long id);
  Mono<Void> deleteById(Long id);

}
