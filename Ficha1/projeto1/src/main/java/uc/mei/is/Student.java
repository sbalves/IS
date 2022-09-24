package uc.mei.is;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "student")
// order of the fields in XML
// @XmlType(propOrder = {"price", "name"})
@XmlAccessorType(XmlAccessType.FIELD)
public class Student {
    @XmlAttribute(name = "id")
    long id;
    @XmlElement(name = "name")
    String name;
    @XmlElement(name = "gender")
    String gender;
    @XmlElement(name = "birthDate")
    String birthDate;
    @XmlElement(name = "registrationDate")
    String registrationDate;
    @XmlElement(name = "address")
    String address;
    @XmlElement(name = "phoneNumber")
    long phoneNumber;

    public Student(){}

    public Student(long id, String name, String gender, String birthDate, String registrationDate, String address, long phoneNumber) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.birthDate = birthDate;
        this.registrationDate = registrationDate;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setphoneNumber(long phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    public void setNome(String name) {
        this.name = name;
    }
}
