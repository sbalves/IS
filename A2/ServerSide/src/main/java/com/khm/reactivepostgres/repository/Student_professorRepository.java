
package com.khm.reactivepostgres.repository;

import com.khm.reactivepostgres.entity.Professor;
import com.khm.reactivepostgres.entity.Student;
import com.khm.reactivepostgres.entity.Student_professor;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import reactor.core.publisher.Mono;

public interface Student_professorRepository extends R2dbcRepository<Student_professor, Long> {
    Mono<Student_professor> findById(Long id);
    Mono<Void> deleteById(Long id);


}
