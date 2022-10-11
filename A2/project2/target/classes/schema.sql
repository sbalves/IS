CREATE TABLE student (
    id         BIGINT,
    name         VARCHAR(512) NOT NULL,
    birth_date     TIMESTAMP NOT NULL,
    completed__credits INTEGER,
    average_grade     INTEGER,
    PRIMARY KEY(id)
);

CREATE TABLE professor (
    id     BIGINT,
    name VARCHAR(512),
    PRIMARY KEY(id)
);

CREATE TABLE student_professor (
    student_id     BIGINT,
    professor_id BIGINT,
    PRIMARY KEY(student_id,professor_id)
);

ALTER TABLE student_professor ADD CONSTRAINT student_professor_fk1 FOREIGN KEY (student_id) REFERENCES student(id);
ALTER TABLE student_professor ADD CONSTRAINT student_professor_fk2 FOREIGN KEY (professor_id) REFERENCES professor(id);