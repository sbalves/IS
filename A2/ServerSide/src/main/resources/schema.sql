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

INSERT INTO professor (name) VALUES ('Nelson Amorim');
INSERT INTO professor (name) VALUES ('Rui Macedo');
INSERT INTO professor (name) VALUES ('Isaura Freitas');
INSERT INTO student (name, birth_date, completed__credits, average_grade) VALUES ('Maria Silva', '2001–06–03', 130, 17);
INSERT INTO student (name, birth_date, completed__credits, average_grade) VALUES ('Ricardo Meneses', '2002–06–03', 120, 15);
INSERT INTO student (name, birth_date, completed__credits, average_grade) VALUES ('Sofia Eduarda', '2000–11–05', 180, 11);
INSERT INTO student (name, birth_date, completed__credits, average_grade) VALUES ('Joao Mercedes', '1996–04–23', 190, 13);
INSERT INTO student (name, birth_date, completed__credits, average_grade) VALUES ('Antonio Pais', '2005–06–03', 130, 14);
INSERT INTO student (name, birth_date, completed__credits, average_grade) VALUES ('Mariana Paiva', '1999–06–03', 170, 12);
INSERT INTO student (name, birth_date, completed__credits, average_grade) VALUES ('Vicente Alcede ', '1998–06–03', 200, 15);
INSERT INTO student_professor (student_id, professor_id) VALUES (1, 1);
INSERT INTO student_professor (student_id, professor_id) VALUES (2, 2);


