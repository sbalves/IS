package com.khm.reactivepostgres.service;

import com.khm.reactivepostgres.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class TransactionalService {

  private final StudentRepository studentRepository;
/*
  @Transactional
  public Mono<Student> doTransaction(CreateTransactionWebRequest request) {
    Long amount = request.getAmount();

    return Mono.zip(studentRepository.findByMemberId(request.getFrom()),
            studentRepository.findByMemberId(request.getTo()))
        .flatMap(balanceTuple -> executeTransaction(balanceTuple, amount));
  }

  private Mono<Student> executeTransaction(Tuple2<Student, Student> balanceTuple, Long amount) {
    Student fromStudent = balanceTuple.getT1();
    Student toStudent = balanceTuple.getT2();
    return deductBalance(fromStudent, amount).flatMap(
            student -> increaseBalance(toStudent, amount));
  }

  private Mono<Student> increaseBalance(Student toStudent, Long amount) {
    return Mono.fromSupplier(() -> new Random().nextDouble())
        .flatMap(randomValue -> increaseBalance(toStudent, amount, randomValue));
  }

  private Mono<Student> increaseBalance(Student toStudent, Long amount, Double randomValue) {
    log.info("random value {}", randomValue);
    if (randomValue > 0.5) {
      toStudent.setBalance(toStudent.getBalance() + amount);
      return studentRepository.save(toStudent);
    }
    return Mono.error(new RuntimeException("randomized error"));
  }

  private Mono<Student> deductBalance(Student fromStudent, Long amount) {
    fromStudent.setBalance(fromStudent.getBalance() - amount);
    return studentRepository.save(fromStudent);
  }
*/

}
