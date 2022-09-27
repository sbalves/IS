package uc.mei.is;

//xml protocol buffer  um é um formato de texto e outro é binário

import com.github.javafaker.DateAndTime;
import com.github.javafaker.Faker;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Random;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static com.example.tutorial.protos.Protos.*;


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

    private static void printText(ArrayList<Integer> prof, ArrayList<Integer> time, String fileName){
        try {
            String path = System.getProperty("user.dir") + "/" + "student.xml";
            File myObj = new File(path);
            FileWriter myWriter = new FileWriter(fileName);
            myWriter.write("PROFESSOR:" + "\n");
            for(int i = 0; i < prof.size(); i++) {
                myWriter.write(String.valueOf(prof.get(i)) + "\n");
            }
            myWriter.write("TEMPO:" + "\n");
            for(int i = 0; i < time.size(); i++) {
                myWriter.write(String.valueOf(time.get(i)) + "\n");
            }
            myWriter.close();

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    private static int average(int[] arr) {
        int total = 0;

        for(int i=0; i<arr.length; i++){
            total = total + arr[i];
        }

        int average = total / arr.length;
        return average;
    }



    private static void generateXML(int testTimes) throws JAXBException {
        JAXBContext jaxbContext = null;
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        ArrayList<Integer> prof = new ArrayList<Integer>();
        ArrayList<Integer> timeXML = new ArrayList<Integer>();
        ArrayList<Integer> timeGzip = new ArrayList<Integer>();
        String separator = "\\";
        String path = System.getProperty("user.dir") + separator + "student.xml";
        int countXML[] = new int[testTimes], countGzip[] = new int[testTimes], numProfessors, numStudents = 100;

        for(int i = 1; i <= 1000; i = i * 10) {
            numProfessors = i;
            for(int o = 0; o < testTimes; o++) {
                try {

                    jaxbContext = org.eclipse.persistence.jaxb.JAXBContextFactory
                            .createContext(new Class[]{School.class}, null);

                    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

                    jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

                    ArrayList<Professor> profs = generateTeachers(numProfessors, numStudents);
                    School scool = new School(profs);


                    //String pathGzip = System.getProperty("user.dir") + separator + "studentCompressed.xml.gz";
                    //String pathGzipDecomp = System.getProperty("user.dir") + separator + "studentDecompressed.xml";

                    File f = new File(path);
                    long startTime = System.nanoTime();
                    jaxbMarshaller.marshal(scool, f);
                    long timeMillisXML = (System.nanoTime() - startTime)/ 1_000_000;
                    countXML[o] = (int) timeMillisXML;
                    System.out.println("Professors: " + i + "\n" + "TimeXML: " + timeMillisXML + "\n");
                    //compressGzipFile(path, pathGzip);
                    //decompressGzipFile(pathGzip, pathGzipDecomp);
                    long timeMillisGzip = (System.nanoTime() - startTime)/ 1_000_000;
                    countXML[o] = (int) timeMillisGzip;
                    System.out.println("Professors: " + i + "\n" + "TimeGzip: " + timeMillisXML + "\n");

                    School school = new School();
                    school = (School) jaxbUnmarshaller.unmarshal(f);

                } catch (JAXBException e) {
                    e.printStackTrace();
                }
            }
            prof.add(i);
            int tempoMedioXML = average(countXML);
            int tempoMedioGzip = average(countGzip);
            timeXML.add((int) tempoMedioXML);
            timeGzip.add((int) tempoMedioGzip);
            System.out.println("Professors: " + i + "\n" + "AVGTimeXML: " + tempoMedioXML + "\n" + "AVGTimeGzip: " + tempoMedioGzip);

        }
        printText(prof, timeXML, "SchoolXML.txt");
        printText(prof, timeGzip, "SchoolGzip.txt");
    }

    public static void main(String[] args) throws IOException {
        //generateFiles();

        serializeProto(100,100);

        deserializeProto();
    }

}