package com.khm.reactivepostgres.repository;

import com.khm.reactivepostgres.entity.Professor;
import com.khm.reactivepostgres.entity.Student;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface StudentRepository extends R2dbcRepository<Student, String> {
  Mono<Student> findById(Long id);
  Mono<Void> deleteById(Long id);

}

