package com.khm.reactivepostgres.entity;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Builder
@Data
public class Student {


  @Id
  @GeneratedValue(strategy= GenerationType.AUTO)
  private Long id;
  private String name;
  private String birth_date;
  private Integer completed__credits;
  private Integer average_grade;

}
