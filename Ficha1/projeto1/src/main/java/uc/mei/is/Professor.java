package uc.mei.is;

import jakarta.xml.bind.annotation.*;

import java.util.ArrayList;

@XmlRootElement(name = "professor")
// order of the fields in XML
// @XmlType(propOrder = {"price", "name"})

@XmlAccessorType(XmlAccessType.FIELD)
public class Professor {
    //@XmlAttribute
    @XmlAttribute(name = "id")
    long id;
    @XmlElement(name = "name")
    String name;
    @XmlElement(name = "birthDate")
    String birthDate;
    @XmlElement(name = "address")
    String address;
    @XmlElement(name = "phoneNumber")
    long phoneNumber;

    @XmlElement(name = "student")
    ArrayList<Student> listStudents;
    public Professor(){}
    public Professor(long id, String name, String birthDate, String address, long phoneNumber, ArrayList<Student> listStudents) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.listStudents = listStudents;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getAddress() {
        return address;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public ArrayList<Student> getListStudents() {
        return listStudents;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setListStudents(ArrayList<Student> listStudents) {
        this.listStudents = listStudents;
    }


}
