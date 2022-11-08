package com.khm.reactivepostgres.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.UUID;


@Builder
@Data
public class Professor {

  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  @Column(name="id", nullable=false, updatable=false)
  private Long id;
  private String name;
}
