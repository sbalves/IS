package Entities;

public class Student {

    private String name;
    private String birth_date;
    private Integer completed__credits;
    private Integer average_grade;

    //Over-riding the toString() function as a class function
    public String toString(){
        return "name: " + this.name;
    }
}
