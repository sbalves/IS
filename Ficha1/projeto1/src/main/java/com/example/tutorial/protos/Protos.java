package com.example.tutorial.protos;
import com.example.tutorial.protos.StudentP;
import com.example.tutorial.protos.ProfessorP;
import com.example.tutorial.protos.School;
import com.github.javafaker.Faker;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import uc.mei.is.Professor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class Protos {

    private static String randomDate() {
        Random r = new Random();
        String day = String.valueOf(r.nextInt(31-1) + 1);
        String month = String.valueOf(r.nextInt(12-1)+1);
        String year = String.valueOf(r.nextInt( 2020-1970)+1970);
        String date = day + "/" + month + "/" + year;
        return date;
    }

    private static StudentP generateStudent(){
        Faker faker = new Faker();

        String birthDate = randomDate();
        String registrationDate = randomDate();
        String name = faker.name().firstName();
        String address = faker.address().streetAddress();
        String gender = "";
        if(faker.bool().bool()){
            gender = "Homem";
        }else{
            gender = "Mulher";
        }
        int id = new Random().nextInt(999999999);
        int phoneNumber = new Random().nextInt(999999999);

        StudentP.Builder student = StudentP.newBuilder()
                                            .setId(id)
                                            .setName(name)
                                            .setGender(gender)
                                            .setBirthDate(birthDate)
                                            .setRegistrationDate(registrationDate)
                                            .setAddress(address)
                                            .setPhoneNumber(phoneNumber);
        return student.build();
    }

    private static ProfessorP generateTeacher(int numStudents){
        Faker faker = new Faker();
        String birthDate = randomDate();
        String registrationDate = randomDate();
        String name = faker.name().firstName();
        String address = faker.address().streetAddress();
        String gender = "";
        if(faker.bool().bool()){
            gender = "Homem";
        }else{
            gender = "Mulher";
        }
        int id = new Random().nextInt(999999999);
        int phoneNumber = new Random().nextInt(999999999);

        ProfessorP.Builder professor = ProfessorP.newBuilder()
                .setId(id)
                .setName(name)
                .setBirthDate(birthDate)
                .setAddress(address)
                .setPhoneNumber(phoneNumber);

        for(int i = 0; i < numStudents; i++){
            professor.addStudents(generateStudent());
        }

        return professor.build();
    }

    private static School generateTeachers(int numProfessors, int numStudents){
        School.Builder school = School.newBuilder();
        System.out.println("School build");
        long startTime = System.nanoTime();
        for(int i = 0 ; i < numProfessors; i++){
            school.addProfessors(generateTeacher(numStudents));
            System.out.println("Teacher " + i + " generated");
        }
        long elapsedTime = (System.nanoTime() - startTime)/1_000_000;
        System.out.println("elapsed1: " + elapsedTime);
        return school.build();
    }

    public static void serializeProto(int numStudents, int numProfessors) throws IOException {
        System.out.println("Going to proto");
        School school = generateTeachers(numProfessors,numStudents);
        System.out.println("School generated");
        String separator = "/";
        String path = System.getProperty("user.dir") + separator + "studentsProto";

        FileOutputStream output = new FileOutputStream(path);
        System.out.println("Serializing...");
        long startTime = System.nanoTime();
        school.writeTo(output);
        long elapsedTime = (System.nanoTime() - startTime)/1_000_000;
        System.out.println("elapsedTOTAL: " + elapsedTime);
        System.out.println("File serialized!");
        output.close();
    }

    public static void deserializeProto() throws IOException {
        String separator = "/";
        String path = System.getProperty("user.dir") + separator + "studentsProto";

        School school = School.parseFrom(new FileInputStream(path));

        //System.out.println(school.toString());

    }

}
