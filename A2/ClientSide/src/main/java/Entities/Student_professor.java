package Entities;

public class Student_professor {
    private Long id;
    private Long student_id;
    private Long professor_id;

    public Long getId() {
        return id;
    }

    public Long getStudent_id() {
        return student_id;
    }

    public Long getProfessor_id() {
        return professor_id;
    }

    @Override
    public String toString() {
        return "Student_professor{" +
                "id=" + id +
                ", student_id=" + student_id +
                ", professor_id=" + professor_id +
                '}';
    }
}