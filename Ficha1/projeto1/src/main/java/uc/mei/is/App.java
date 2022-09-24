package uc.mei.is;

//xml protocol buffer  um é um formato de texto e outro é binário

import com.github.javafaker.DateAndTime;
import com.github.javafaker.Faker;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;



public class App {

    private static void decompressGzipFile(String gzipFile, String newFile) {
        try {
            FileInputStream fis = new FileInputStream(gzipFile);
            GZIPInputStream gis = new GZIPInputStream(fis);
            FileOutputStream fos = new FileOutputStream(newFile);
            byte[] buffer = new byte[1024];
            int len;
            while((len = gis.read(buffer)) != -1){
                fos.write(buffer, 0, len);
            }
            //close resources
            fos.close();
            gis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void compressGzipFile(String file, String gzipFile) {
        try {
            FileInputStream fis = new FileInputStream(file);
            FileOutputStream fos = new FileOutputStream(gzipFile);
            GZIPOutputStream gzipOS = new GZIPOutputStream(fos);
            byte[] buffer = new byte[1024];
            int len;
            while((len=fis.read(buffer)) != -1){
                gzipOS.write(buffer, 0, len);
            }
            //close resources
            gzipOS.close();
            fos.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private static String randomDate() {
        Random r = new Random();
        String day = String.valueOf(r.nextInt(31-1) + 1);
        String month = String.valueOf(r.nextInt(12-1)+1);
        String year = String.valueOf(r.nextInt( 2020-1970)+1970);
        String date = day + "/" + month + "/" + year;
        return date;
    }
    private static ArrayList<Student> generateStudents(int numStudents){
        ArrayList<Student> lista = new ArrayList<Student>();
        Faker faker = new Faker();
        for(int i = 0; i < numStudents; i++){
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

            Student aux = new Student(id, name, gender, birthDate,registrationDate,address,phoneNumber);
            lista.add(aux);

        }
        return lista;
    }

    private static ArrayList<Professor> generateTeachers(int numProfessors, int numStudents){
        ArrayList<Professor> professors = new ArrayList<Professor>();
        for(int i = 0 ; i < numProfessors; i++){
            ArrayList<Student> students = generateStudents(numStudents);
            professors.add(generateTeacher(students));
        }
        return professors;
    }

    private static Professor generateTeacher(ArrayList<Student> listStudents){
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
        Professor prof = new Professor(id, name, birthDate,address,phoneNumber,listStudents);

        return prof;
    }

    private static void generateFiles(){
        JAXBContext jaxbContext = null;
        try {

            int numProfessors  = 100, numStudents = 100;
            String separator = "\\";


            jaxbContext = org.eclipse.persistence.jaxb.JAXBContextFactory
                    .createContext(new Class[]{School.class}, null);

            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            ArrayList<Professor> profs = generateTeachers(numProfessors,numStudents);
            School scool = new School(profs);

            // XML
            String path = System.getProperty("user.dir") + separator + "student.xml";

            // XML + GZIP
            String pathGzip = System.getProperty("user.dir") + separator + "studentCompressed.xml.gz";
            String pathGzipDecomp = System.getProperty("user.dir") + separator + "studentDecompressed.xml";

            File f = new File(path);
            jaxbMarshaller.marshal(scool, f);
            compressGzipFile(path, pathGzip );
            decompressGzipFile(pathGzip, pathGzipDecomp);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        generateFiles();
    }

}