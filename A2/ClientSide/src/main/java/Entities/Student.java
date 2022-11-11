package Entities;

public class Student {

    private long id;
    private String name;
    private String birth_date;
    private Integer completed__credits;
    private Integer average_grade;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public Integer getCompleted__credits() {
        return completed__credits;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public void setCompleted__credits(Integer completed__credits) {
        this.completed__credits = completed__credits;
    }

    public void setAverage_grade(Integer average_grade) {
        this.average_grade = average_grade;
    }

    public Integer getAverage_grade() {
        return average_grade;
    }

    public String toString(){
        return "Name: " + this.name + "\t|\tBirth date: " + this.birth_date + "\t|\tCompleted credits: " + this.completed__credits + "\t|\tAverage grade: " + this.average_grade;
    }

}
