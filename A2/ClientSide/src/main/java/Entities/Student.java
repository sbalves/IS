package Entities;

public class Student {

    private String name;
    private String birth_date;
    private Integer completed__credits;
    private Integer average_grade;


    public String getName() {
        return name;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public Integer getCompleted__credits() {
        return completed__credits;
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
        return "name: " + this.name + "birth date: " + this.birth_date + "completed credits: " + this.completed__credits + "average grade: " + this.average_grade;
    }

}
