DROP TABLE IF EXISTS student_professor;
DROP TABLE IF EXISTS student;
DROP TABLE IF EXISTS professor;

CREATE TABLE student (
    id         SERIAL,
    name         VARCHAR(512) NOT NULL,
    birth_date     VARCHAR(512) NOT NULL,
    completed__credits INTEGER,
    average_grade     INTEGER,
    PRIMARY KEY(id)
);

CREATE TABLE professor (
    id     SERIAL,
    name VARCHAR(512),
    PRIMARY KEY(id)
);

CREATE TABLE student_professor (
    id      SERIAL,
    student_id     BIGINT,
    professor_id BIGINT,
    PRIMARY KEY(student_id,professor_id)
);

ALTER TABLE student_professor ADD CONSTRAINT student_professor_fk1 FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE;
ALTER TABLE student_professor ADD CONSTRAINT student_professor_fk2 FOREIGN KEY (professor_id) REFERENCES professor(id) ON DELETE CASCADE;

INSERT INTO professor (name) VALUES ('Nelson');
INSERT INTO professor (name) VALUES ('Rui');
INSERT INTO professor (name) VALUES ('Isaura');
INSERT INTO student (name, birth_date, completed__credits, average_grade) VALUES ('Maria', '2001–06–03', 130, 17);
INSERT INTO student (name, birth_date, completed__credits, average_grade) VALUES ('Ricardo', '2002–06–03', 120, 15);
INSERT INTO student_professor (student_id, professor_id) VALUES (1, 1);
INSERT INTO student_professor (student_id, professor_id) VALUES (2, 2);


