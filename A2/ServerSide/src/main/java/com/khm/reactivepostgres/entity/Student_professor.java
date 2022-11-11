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
public class Student_professor {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private Long student_id;
    private Long professor_id;

}